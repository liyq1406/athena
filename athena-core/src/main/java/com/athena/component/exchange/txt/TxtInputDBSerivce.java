package com.athena.component.exchange.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.FileNotFindException;
import com.athena.component.exchange.InputDataService;
import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.StaticInterFaceTaskExecutor;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.field.DateFieldFormat;
import com.athena.component.exchange.field.NumberFieldFormat;
import com.athena.component.exchange.utils.FileUtis;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.util.ComponentUtils;
import com.toft.core3.util.Assert;
import com.toft.utils.UUIDHexGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 下午1:49
 * 读取文本文件输入到数据库
 */
public class TxtInputDBSerivce implements InputDataService {
	protected final Logger logger=Logger.getLogger(TxtInputDBSerivce.class);//定义日志方法
    private final static int LINE_NUM = 10000;
	protected DataParserConfig dataParserConfig;
    //接口编号
	protected String interfaceId;
	//支持多文件的读取	
	protected Map<String, FileInputStream> fileInputStreams;

    private List<String> fieldList = new ArrayList<String>();
    private List<String> updateFieldList = new ArrayList<String>();
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    
    private String datasourceId = "";
    
    //文件运行时间
    protected String file_begintime;
	//文件结束时间
    protected String file_endtime;
    
  //hzg add 2013-1-25
    public DataParserConfig getDataParserConfig() {
		return dataParserConfig;
	}

	public void setDataParserConfig(DataParserConfig dataParserConfig) {
		this.dataParserConfig = dataParserConfig;
	}

	public Map<String, FileInputStream> getFileInputStreams() {
		return fileInputStreams;
	}

	public void setFileInputStreams(Map<String, FileInputStream> fileInputStreams) {
		this.fileInputStreams = fileInputStreams;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	public List<String> getUpdateFieldList() {
		return updateFieldList;
	}

	public void setUpdateFieldList(List<String> updateFieldList) {
		this.updateFieldList = updateFieldList;
	}
	
	public TxtInputDBSerivce(DataParserConfig dataParserConfig){
		this.dataParserConfig = dataParserConfig;
		datasourceId = dataParserConfig.getWriterConfig().getDatasourceId();
		
	}
    @Override
    public boolean read(DataParserConfig dataParserConfig) throws ServiceException {
        boolean result = false;
        interfaceId = dataParserConfig.getId();
        logger.info("接口" + interfaceId + "开始输入");
    	ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
        try {
        	String reader = dataParserConfig.getGroupConfig().getReader(); //读取的源是DB还是TXT
            initSqlString();
            if(!"db".equals(reader)){
            	initStream(readerConfig);
            	//将before方法放到if里面，如果in_zidb中输入文件类型(infiletype)为空就走正常流程先执行before  hzg 2014.3.22
            	if(dataParserConfig.getFileUcMap()==null||dataParserConfig.getFileUcMap().isEmpty()){ 
            		before();
            	}
            	if("xml".equals(reader)){	//以XML格式读取文件
            		result = readXmlList(readerConfig.getEncoding());
            	}else{	//以其他格式读取文件(txt)
            		result = readList(readerConfig.getEncoding());
            	}
            }else{
            	before();
            	result = readListDB();
            }
        } catch (RuntimeException pe) {
            logger.error(pe.getMessage());
            //异常往上层抛 
            throw pe;
        } finally {
            closeFile();
        }

        logger.info("接口" + dataParserConfig.getId() + "结束输入");
        return result;
    }

    /**
     * 流不为空则关闭
     * @author yufei
     * @date 2013-4-1
     * @update hezhiguo
     */
    protected void closeFile() {
    	if(null!=fileInputStreams){
    		Iterator iteartor = fileInputStreams.entrySet().iterator();
    		while (iteartor.hasNext()) {
    			Map.Entry<String, FileInputStream> entry = (Map.Entry<String, FileInputStream>)iteartor.next();
    			try {
    				entry.getValue().close();
    			} catch (IOException e) {
    				logger.error("接口" + interfaceId + "关闭" + entry.getKey() + "文件失败", e);
    			}
    		}
    	}
    }

    /**
     *
     */
    private void initSqlString() {
        DataField[] dataFields = dataParserConfig.getDataFields();
        String[] idKeys = dataParserConfig.getWriterConfig().getIdKeys().split(",");
        StringBuffer insertSqlBuf = new StringBuffer();
        StringBuffer updateSqlBuf = new StringBuffer();

        StringBuffer insertFieldBuf = new StringBuffer();
        StringBuffer insertValueBuf = new StringBuffer();
        StringBuffer updateSqlSetBuf = new StringBuffer();

        int i=0;
        for(DataField dataField:dataFields){
        	if(dataField.getIsParam()){//没有配置isParam属性的字段进行SQL拼接
        		fieldList.add(dataField.getWriterColumn());
        		if(dataField.isUpdate()){
        			updateFieldList.add(dataField.getWriterColumn());
        			updateSqlSetBuf.append(",")
        			.append(dataField.getWriterColumn()).append("=?");
        		}
        		if(i++>0){
        			insertFieldBuf.append(",");
        			insertValueBuf.append(",");
        		}
        		insertFieldBuf.append(dataField.getWriterColumn());
        		insertValueBuf.append("?");
        	}
        }

        String table = dataParserConfig.getWriterConfig().getTable();
        boolean hasMiddle = false; //是否有中间表
        if(table!=null){
            if(table.contains("${")){
                String[] strs = SpaceFinal.replaceSql(table).split("\\.");
                if(strs.length>1){
                    table = strs[1];
                }
            }
            hasMiddle = table.toLowerCase().startsWith("in");
        }

        if("txt".equals(dataParserConfig.getGroupConfig().getReader()) && hasMiddle){
            fieldList.add("WENJMC");
            fieldList.add("WENJLJ");
            updateFieldList.add("WENJMC");
            updateFieldList.add("WENJLJ");
        }

        String tableName = SpaceFinal.replaceSql(dataParserConfig.getWriterConfig().getTable());
        insertSqlBuf.append(" insert into ")
                .append(tableName)
                .append("(").append(insertFieldBuf);


        //如果reader为txt的,并且有中间表
        if("txt".equals(dataParserConfig.getGroupConfig().getReader()) && hasMiddle){
            // 则要将文件名称，文件路径两个字段加上
            insertSqlBuf.append(",WENJMC,WENJLJ) values (").append(insertValueBuf).append(",?,?)");
        }else{
            insertSqlBuf.append(") values (").append(insertValueBuf).append(")");
        }


        updateSqlBuf.append(" update ").append(tableName)
                .append(" set ").append(updateSqlSetBuf.substring(1));
        //如果reader为txt的,并且有中间表
        if("txt".equals(dataParserConfig.getGroupConfig().getReader()) && hasMiddle){
            updateSqlBuf.append(",WENJMC=?,WENJLJ=?");
        }

        updateSqlBuf.append(" where 1=1 ");

        for(String idKey:idKeys){
            updateSqlBuf.append(" and ").append(idKey)
                    .append("=? ");
        }

        dataParserConfig.setInsertSql(insertSqlBuf.toString());
        dataParserConfig.setUpdateSql(updateSqlBuf.toString());


    }

    /**
     * 解析所有文本TXT数据，放入List
     * @param encoding 字符集编码
     * @return
     */
    private boolean readList (String encoding){
        logger.info("接口" + interfaceId + "开始读取输入文件");
        int result = 0;
        int i=0;
        Iterator iteartor = fileInputStreams.entrySet().iterator();
        //如果in_zidb中输入文件类型(infiletype)为空则走原来的正常流程  hzg 2014.3.22
        if(dataParserConfig.getFileUcMap()==null||dataParserConfig.getFileUcMap().isEmpty()){
        	while (iteartor.hasNext()) {
        		Map.Entry<String, FileInputStream> entry = (Map.Entry<String, FileInputStream>)iteartor.next();
        		result += readFile(entry.getKey(), entry.getValue(), encoding) ? TRUE : FALSE;
        	}
        }else{ //否则，取in_zidb用户中心来处理  hzg 2014.3.22 其中增加before方法，用于写文件前可以取到用户中心来做相关操作
        	while (iteartor.hasNext()) {
        		logger.info("开始执行文件输入@@@@@@@@@@@@@@@@");
        		
        		Map.Entry<String, FileInputStream> entry = (Map.Entry<String, FileInputStream>)iteartor.next();
        		if(dataParserConfig.getFileUcMap().containsKey(entry.getKey())){ //v如果map中包含将要读取的文件名，则取出该文件名的用户中心
        			dataParserConfig.setUsercenter(dataParserConfig.getFileUcMap().get(entry.getKey()));
        		}else{
        			logger.error("接口" + interfaceId + "在in_zidb表中找不到输入文件名，请检查in_zidb");
        		}
        		before();
        		result += readFile(entry.getKey(), entry.getValue(), encoding) ? TRUE : FALSE;
        	}
        }
    	return result == fileInputStreams.size() ? true : false;
    }
    
    
    /**
     * 解析所有DB数据，放入List
     * @return
     */
    protected boolean readListDB (){
        logger.info("接口" + interfaceId + "开始解析DB数据");
        try{
        	//before();
		    int totalPage = getTotalPage(dataParserConfig.getReaderConfig().getSql(),dataParserConfig.getReaderConfig().getIsAllSet());
		    for(int i=0;i<totalPage;i++){
		    	List<Map<String,Object>> list =  readDbDate(i,dataParserConfig.getReaderConfig());
		        readFileDB(list);
		    }
		    after();
        } catch (SQLException sqlEx){
            logger.error(sqlEx.getMessage());
            throw new ServiceException(sqlEx);
        }     
    	return true;
    }
    
    /**`
     * 计算分多少页
     */
    public int getTotalPage(String sql,String flagStr) throws SQLException{
        int totalNum = getTotalNum(sql,flagStr);
        int totalPage  = totalNum/LINE_NUM + (totalNum%LINE_NUM==0 ? 0 : 1);
        return totalPage;
    }
    
    /**
    *
    * @param sqlId
    * @param flagStr
    * @return
    * @throws SQLException
    */
	@SuppressWarnings("unchecked")
	private int getTotalNum(String sqlId,String flagStr) throws SQLException{
		int countNum = 0;
       String head = "select count(1) COUNTNUM from (";
       String foot = " ";
       Map<String,String> params = putParams(head,foot.toString(),flagStr); //分页参数替换
       //查询出来COUNTNUM默认的value类型为BigDecimal
       Map<String,BigDecimal> dataMap= (Map<String,BigDecimal>)dataParserConfig.getBaseDao().getSdcDataSource(dataParserConfig.getReaderConfig().getDatasourceId()).selectObject(sqlId, params);
       countNum = Integer.parseInt(dataMap.get("COUNTNUM").toString());
       return countNum;
   }
	
    /**
     * 参数替换
     * @param head 开头参数
     * @param foot 结尾参数
     * @param flagStr
     * @return
     */
    private Map<String,String> putParams(String head,String foot,String flagStr){
    	Map<String,String> params = new HashMap<String,String>();
    	String temp_foot = "".equals(foot.trim())? ")": foot;
    	String tempStr = (head.indexOf("COUNTNUM")!= -1)?" )" :" ";
    	String strmiddle = "false".equals(flagStr)? " "+makeIncrementSql()+" ":" ";
    	String strfoot = "false".equals(flagStr)? " "+foot + tempStr: temp_foot;
    	params.put("middle", strmiddle);
        params.put("head", head);
        params.put("foot", strfoot);
    	return params;
    }

    /**
     * 生成带有增量条件的sql
     * 	 and to_char(edit_time, 'yyyymmddHH24MIss') between
     (SELECT to_char(i.lastcpltime, 'yyyymmddHH24MIss')
     FROM IN_ZIDB i
     WHERE i.inbh = 'id1') and to_char(sysdate, 'yyyymmddHH24MIss')
     * hzg modify 2014.7.17
     * 卡时间改为上次运行时间和开始时间
     * @return
     */
    protected String makeIncrementSql() {
        //1：查找接口总表数据库,拿出此接口的 完成时间
        //2: 以此完成时间，和当前时间做条件 生成串
        //3: 更新此接口总表 此接口的完成时间，上上次完成时间
        StringBuffer sb = new StringBuffer();
        sb.append(" AND EDIT_TIME >= (SELECT B.LASTCPLTIME FROM IN_ZIDB B WHERE B.INBH='"+interfaceId+"') ");
        sb.append(" AND EDIT_TIME < (SELECT B.Yunxkssj FROM IN_ZIDB B WHERE B.INBH='"+interfaceId+"') ");
        return sb.toString();
    }

    /**
    *
    * @param pageNum
    * @param read
    * @throws SQLException
    */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> readDbDate(int pageNum,ExchangerConfig  read) throws SQLException{
   	int endPage = (pageNum+1)*LINE_NUM;  //结束页数条数
   	int startPage = pageNum*LINE_NUM+1;     //开始页数条数
       String head = "select * from ("; 
       String foot =" and rownum <= "+endPage+") t"
       			+" where t.RN >= "+startPage ;
       Map<String,String> params = putParams(head,foot.toString(),read.getIsAllSet()); //分页参数替换
       List<Map<String,Object>> dataList = dataParserConfig.getBaseDao().getSdcDataSource(read.getDatasourceId()).select(read.getSql(), params);
       return dataList;
   }
    
    /**
     * 输始化流
     * @param readerConfig 配置文件的reader属性
     */
	private void initStream(ExchangerConfig readerConfig){
        logger.info("接口" + interfaceId + "初始化多文件数据流");
        logger.info("getFilePath:" + readerConfig.getFilePath());
    	//使用默认的输入文件配置
    	if(!readerConfig.getFilePath().equals("")){
    		try{
    			//读取多文件，但文件开头名必须一致
    			fileInputStreams = getFileInputList(readerConfig.getFilePath());
    		}catch(FileNotFoundException ex){
    			throw new FileNotFindException("接口" + interfaceId + " 文件【"+readerConfig.getFilePath()+"】未找到！");
    		}
    	}else{  
	   		try{
	 			//读取多文件，但文件开头名必须一致
	 			fileInputStreams = getFileInputList2();
	 		}catch(FileNotFoundException ex){
	 			throw new FileNotFindException("接口" + interfaceId + " 文件未找到！");
	 		}  		
	    }
    }
	
	
	/**
     * 读取解析DB数据
     * @param listdb 数据库集合
     * @return 数字 1 表示成功 ，数字 0 表示失败
     */
	protected boolean readFileDB(List<Map<String,Object>> listdb){
        boolean result = false;
    	List<Record> lineList = new ArrayList<Record>();
        int lineNum = 0;
        long startTime = System.currentTimeMillis();	
    	file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
		for(Map<String,Object> map: listdb){
			lineNum++;
			Record record = null;
			record = parserDbDate(map);
			record.setLineNum(lineNum);
			lineList.add(record);
			if(!afterRecord(record)){
				continue;
			}
		    if (lineList.size() == LINE_NUM) {
		        list.add(exeTask("DB", lineNum, lineList));
		        lineList = new ArrayList<Record>();
		    }
		}
		if (lineList.size() > 0) {
		    list.add(exeTask("DB", lineNum, lineList));
		}
		//设置文件解析结束时间
		file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		result = collectTask(list); 
        logger.info("接口" + interfaceId + "读DB结束 , 总记录行数" + lineNum);
    	logger.info("文件解析完成，共解析记录"+lineNum+"条,耗时"+(System.currentTimeMillis()-startTime)+"毫秒.");
        return result;
    } 

	/**
     * 读取解析TXT文件的数据
     * @param fileName
     * @param fileInputStream
     * @param encoding
     * @return 数字 1 表示成功 ，数字 0 表示失败
     */
	protected boolean readFile(String fileName, FileInputStream fileInputStream,String encoding){
		logger.info("接口" + interfaceId + "开始读取文件" + fileName);
		boolean result = false;
		String encodingStr = encoding ==null?"GBK":encoding;
		List<Record> lineList = new ArrayList<Record>();
		int lineNum = 1;
		long startTime = System.currentTimeMillis();	
		try {
			file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,encodingStr));
			String line = null;
			List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
			//before();
			while((line = bufferedReader.readLine()) != null){
				Record record = null;

				if(!beforeRecord(line,fileName,lineNum)){ //跳过某一行后，记录一下行数
					lineNum++;
					continue;
				}
				//logger.info("接口" + interfaceId + "读取文件" + fileName + "第" + lineNum + "行");
				if(StringUtils.isNotEmpty(line.trim())){
					record = parserLine(fileName,line);
					record.setLineNum(lineNum);
				} else {
					logger.info("接口" + interfaceId + "读取文件" + fileName + "第" + lineNum + "行空行数据被忽略！");
				}
				logger.info("接口" + interfaceId + "读取文件" + fileName + "第" + lineNum + "行");
				if(!afterRecord(record)){ //增加方法，毛需求1050，1051，1052等使用  hzg 2013-4-13
					lineNum++;
					continue;
				}
				lineNum++;
				if(record != null){ //判断是否为record空 hzg 2014.3.37
					lineList.add(record);					
				}
				if (lineList.size() == LINE_NUM) {
					list.add(exeTask(fileName, lineNum, lineList));
					lineList = new ArrayList<Record>();
				}
			}
			if (lineList.size() > 0) {
				list.add(exeTask(fileName, lineNum, lineList));
			}
			//设置文件解析结束时间
			file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			result = collectTask(list);
			finishAfter();
		} catch (UnsupportedEncodingException e) {
			logger.error("接口" + interfaceId + "不支持编码 " + e.getMessage());
		} catch(IOException ex){
			logger.error("接口" + interfaceId + "IO输入出错 " + ex.getMessage());
		}finally{
			after();
			if(result){ //true
				//此文件时成功文件---解析没有错  如果是txt-->业务表  要记录文件到日志表中
				if(!dataParserConfig.getWriterConfig().getTable().toLowerCase().startsWith("in")){
					file_info(fileName,result);
				}
			}else{//false
				file_info(fileName,result);
			}
		}
		logger.info("接口" + interfaceId + "结束读取文件" + fileName + " 总记录行数" + (lineNum-1));
		logger.info("文件解析完成，共解析记录"+(lineNum-1)+"条,耗时"+(System.currentTimeMillis()-startTime)+"毫秒.");
		return result;
	}

	/**
	 * 记录文件日志
	 * @author Hezg
	 * @date 2013-2-4
	 * @param wenjmc 文件名称
	 * @param file_satus 运行状态        1 成功(已执行)  -1 失败(已执行)
	 * @return 
	 */
	private void file_info(String wenjmc,boolean file_satus) {
        AtomicInteger insert_num = new AtomicInteger();
        AtomicInteger update_num = new AtomicInteger();
        AtomicInteger error_num = new AtomicInteger();
		Map<String,String> params = new HashMap<String,String>();
		if(file_satus){
			insert_num = dataParserConfig.getInsertCount();
			update_num = dataParserConfig.getUpdateCount();
			error_num = dataParserConfig.getErrorCount();
		}
		params.put("SID", UUIDHexGenerator.getInstance().generate());
		params.put("INBH", interfaceId);
		params.put("fileName", wenjmc);
		params.put("file_begintime", file_begintime);
		params.put("file_endtime", file_endtime);
		params.put("insert_num", String .valueOf(insert_num));
		params.put("update_num", String .valueOf(update_num));
		params.put("error_num", String .valueOf(error_num));
		params.put("file_satus", file_satus?"1":"-1");
		if(!"".equals(datasourceId)&&Integer.parseInt(datasourceId)>=4){
    		FileLog.getInstance(dataParserConfig.getReaderConfig().getDatasourceId()).
    		insert_file_info(params,dataParserConfig.getBaseDao());
    	}else{
    		FileLog.getInstance(datasourceId).insert_file_info(params,dataParserConfig.getBaseDao());
    	}
		//hzg 2013-6-3添加日志
		logger.info("插入记录条数："+insert_num);
		logger.info("更新记录条数："+update_num);
		logger.info("错误记录条数："+error_num);
		//插入和更新完成后将插入数量和更新数量清空  hzg 2013-3-22
		this.dataParserConfig.getInsertCount().lazySet(0);
	    this.dataParserConfig.getUpdateCount().lazySet(0);
	    this.dataParserConfig.getErrorCount().lazySet(0);
	}
	
    /**
     * 钩子方法
     * @param line
     * @param fileName
     * @param lineNum
     */
    @Override
    public boolean beforeRecord(String line, String fileName, int lineNum) {
    	return true;
    }

    /**
     * 钩子方法
     */
    @Override
	public void before() {
		
	}

	public void after() {
		
	}

	public void finishAfter() {
		
	}
	
    public boolean afterRecord(Record record){
    	return true;
    }
    /**
     * 合并子线程的结果
     * @param list   子线程列表
     * @return 子线程的结果
     */
    private boolean collectTask(List<Future<Boolean>> list) {
        int count = 0;
        for (Future<Boolean> future : list) {
            try {
                count += future.get() ? TRUE : FALSE;
            } catch (InterruptedException e) {
                logger.error("接口" + interfaceId + "读取文件出现线程中断" + e.getMessage());
            } catch (ExecutionException e) {
                throw launderThrowable(e);
            } finally {
                future.cancel(true);
            }
        }
        return count == list.size() ? true : false;
    }
    
    /**
     * 
     * @author Hezg
     * @date 2013-3-21
     * @param t  java.util.concurrent.ExecutionException
     * @return
     */
    private RuntimeException launderThrowable(Throwable t) {
    	System.out.println(t.getCause());
    	if (t.getCause() instanceof ServiceException)
    		return (ServiceException) t.getCause();
    	else if (t.getCause() instanceof RuntimeException)
            return (RuntimeException) t.getCause();
    	 else if (t.getCause() instanceof NullPointerException)
             throw (NullPointerException) t.getCause();
        else if (t.getCause() instanceof Error)
            throw (Error) t.getCause();
        else
            throw new ServiceException("Not unchecked", t);
    }
    
    /**
     * 向线程池中添加子任务前执行子任务
     * @author Hezg
     * @date 2013-4-13
     * @param fileName
     * @param lineNum
     * @param lineList
     * @return
     */
    @SuppressWarnings("unchecked")
	protected Future<Boolean> exeTask(String fileName, int lineNum, List<Record> lineList) {
        logger.info("接口" + interfaceId + "读取文件" + fileName + "解析从" + lineNum + "到" + (lineNum + lineList.size()));
        TxtWriterDBTask txtInputDataWriter = null;
        String isTask = dataParserConfig.getWriterConfig().getIsTask();
        if(isTask!=null&&isTask.equals("true")){
        	String writerClass = dataParserConfig.getWriterConfig().getExchangerClass();
        	try {
				Class<? extends TxtWriterDBTask>  dataWriterClazz = FileUtis.getClass(writerClass);
				Constructor<TxtWriterDBTask> txtConstructor = 
					ConstructorUtils.getAccessibleConstructor(dataWriterClazz, new Class[]{DataParserConfig.class,List.class,List.class,String.class,List.class});
				//参数
				Object[] args = new Object[]{dataParserConfig, fieldList, updateFieldList, fileName, lineList};
				//实例化
				txtInputDataWriter = ComponentUtils.instantiateClass(txtConstructor, args);
				Assert.notNull(txtInputDataWriter,"数据输入对象不能为空！");
				logger.info("接口"+ dataParserConfig.getId() + "输入构造类："+txtInputDataWriter.getClass()); 
			} catch (ClassNotFoundException e) { 
				logger.error("接口"+ dataParserConfig.getId() + "输入构造类出错" + writerClass, e); 
			}  
        } else {
        	txtInputDataWriter = new TxtWriterDBTask(dataParserConfig, fieldList, updateFieldList, fileName, lineList);
        }
        return StaticInterFaceTaskExecutor.getPrintTaskExecutor().submit(txtInputDataWriter);
    }
    
    /**
     * 得到需读取多少个文件
     * 处理:/users/ath00/temp/***.txt,***.txt
     * @param readerConfigPath
     * @return 多个文件数据流
     * @throws FileNotFoundException
     */
    private Map<String, FileInputStream> getFileInputList(String readerConfigPath) throws FileNotFoundException{
        logger.info("接口" + interfaceId + "获取多个物理文件");
        Map<String, FileInputStream> fileInputStreams = new HashMap<String, FileInputStream>();
    	int top = readerConfigPath.lastIndexOf("/");
    	String filePath = readerConfigPath.substring(0,top);
    	String fileName = readerConfigPath.substring(top+1,readerConfigPath.length());
    	File file = new File(filePath);
    	File[] in_files = file.listFiles();
    	if(in_files!=null){
    		for(File f:in_files){
        		if(isContainsFile(f.getName(), fileName)){
        			fileInputStreams.put(f.getName(), new FileInputStream(f));
        		}
        	}
    	}else{
    		logger.error("未找到文件"+filePath+"目录或没有对该文件夹的读取权限");
    	}
    	
    	if(fileInputStreams.isEmpty()){
    		throw new FileNotFoundException();
    	}
    	return fileInputStreams;
    	
    }
    

    /**
     * 得到需读取多少个文件
     * 处理:/users/ath00/temp/***.txt,***.txt
     * @param readerConfigPath
     * @return 多个文件数据流
     * @throws FileNotFoundException
     */
    private Map<String, FileInputStream> getFileInputList2() throws FileNotFoundException{
        logger.info("接口" + interfaceId + "获取多个物理文件");
       // Map<String, FileInputStream> fileInputStreams = new HashMap<String, FileInputStream>();
        Map<String, FileInputStream> fileInputStreams = new LinkedHashMap<String, FileInputStream>();
        
    	String filePath = "/users/ath00/tmp/";
    	List<String> filename = dataParserConfig.getInputFileList();
    	 logger.info("接口" + interfaceId + "filename"+filename.size());
    	
    	File file = new File(filePath);
    	File[] in_files = file.listFiles();	
       	int i = filename.size();
    	int j = 0;
    	
    	/*
    	for(int k=0;k<in_files.length;k++){
    		logger.info("接口" + interfaceId + "in_files"+in_files[k]);
    	}
    	*/
    	
    	Arrays.sort(in_files);
    	
    	/*
    	for(int k=0;k<in_files.length;k++){
    		logger.info("接口" + interfaceId + "sorted in_files"+in_files[k]);
    	}
    	*/
    	
    	if(in_files!=null){
      		 for(File f:in_files){
	        		if(isContainsAllFile(f.getName(), filename )){
	        			j++;
	        		}	
		      }
		 		
      		 logger.info("接口" + interfaceId + "i:"+i+"j:"+j);
                   		 
		 	  if(i==j){	 		  
				    		for(File f:in_files){
					              if(isContainsAllFile(f.getName(), filename )){
					        	      fileInputStreams.put(f.getName(), new FileInputStream(f));				        	      
					              }	
				            }
				logger.info("读取文件流完成" );				    		
		       }
		 	 
    	}else{
    		logger.error("未找到文件"+filePath+"目录或没有对该文件夹的读取权限");
    	}
    	
    	if(fileInputStreams.isEmpty()){
    		throw new FileNotFoundException();
    	}
    	 logger.error("fileInputStreams:"+fileInputStreams.size());
	 	 
    	return fileInputStreams;
    	
    }
    
    
    
    
    
    
	/**
	 *  只要file包含所配置的文件名 就返回真
	 * @param file 文件名称
	 * @param fileName 包含文件名 格式：abcdfd,sdss
	 * @return
	 */
    protected  boolean isContainsFile(String file,String fileName){
        //logger.info("接口" + interfaceId + "判断文件名为" + file + "是否属于" + fileName);
		boolean result = false;
		String[] filestrs = fileName.split(",");
		
		for(String filestr : filestrs){
			if(file.contains(filestr)){
				result = true;
				break;
			}
		}
		return result;
	}
    
    /**
	 *  file存在所配置的所有文件名 就返回真
	 * @param file 文件名称
	 * @param fileName 包含文件名 格式：{{all:abc},{all:cba}}
	 * @return
	 */
    protected  boolean isContainsAllFile(String file,List<String> fileName){
    //logger.info("接口" + interfaceId + "判断文件名为" + file + "是否包含" +filestr.substring(4, filestr.length())) ;	
    boolean result = false;    
   
		for(String filestr : fileName){
		if(file.contains( filestr.substring(4, filestr.length()) ) ){
logger.info("接口" + interfaceId + "判断文件名为" + file + "是否包含" +filestr.substring(4, filestr.length())) ;			
					result = true;	
					break;
			 }
		}
		logger.info("接口" + interfaceId + "result:" + result);	    				       				

		return result;
	}

	
	/**
	 * 解析数据库读取的数据行
	 * @author Hezg
	 * @date 2013-3-14
	 * @param map 数据行记录
	 */
	private Record parserDbDate(Map<String,Object> map){
		Record record = new Record();
		//得到所需入库的字段
    	DataField[] dataFileds = dataParserConfig.getDataFields();
    	for(DataField dataField:dataFileds){
    		String strValue = "";
			try {
				if(dataField.getIsParam()){ //放存要入库的字段和值  ，2013-4-1 hzg db->db的数据不进行转换，直接放record中入库
					//record.put(dataField.getWriterColumn(),strNull(map.get(dataField.getReaderColumn())));
					//修改，如果是number或date类型，则进行转换 hzg 2013-7-18 update ,number转换有问题
					record.put(dataField.getWriterColumn(),convertValue(dataField,strNull(map.get(dataField.getReaderColumn()))));
				}
			} catch (ParserException e) {
				logger.error("接口" + interfaceId + "解析 第" + map
                        + "行数据" + dataField.getWriterColumn() + ":" + strValue.trim() + "转换异常  " + e.getMessage());
				//程序转换出错抛异常 1 hzg 2013-7-22
				throw new ServiceException("接口" + interfaceId + "解析 第" + map
                        + "行数据" + dataField.getWriterColumn() + ":" + strValue.trim() + "转换异常  " + e.getMessage(),e);
			}
		}
		return record;
	}
	
    /**
     *
     * @param rowObject
     * @return
     */
	private Record parserLine(String fileName,Object rowObject){
    	Record record = new Record();
    	String line = rowObject.toString();
    	String quote = dataParserConfig.getReaderConfig().getQuote();
    	if(quote!=null&&!"".equals(quote)){
    		line = line.replace(quote, "");
    	}
    	int lineLength = countString(line);
    	//得到所需入库的字段
    	DataField[] dataFileds = dataParserConfig.getDataFields();
    	for(DataField dataField:dataFileds){
    		int start = dataField.getStart();
    		int end = dataField.getStart() + dataField.getLength();
    		String strValue = "";
			if(start<lineLength){
				if(dataField.getLength()==1000){ //针对3030等特殊字符处理，如果line最后截取的长度不确定，那么截取start位到未尾 hzg 2013-4-2
					strValue = line.substring(start);
				}else{
					strValue =  cutString(line,start, Math.min(end,lineLength));
				}
			}
			try {
				if(dataField.getIsParam()){ //放存要入库的字段和值
					record.put(dataField.getWriterColumn(),convertValue(dataField,strValue.trim()));
				}else{  //存放要做为参数的字段为值，配置中的属性为isParam="true"
					record.putParamObject(dataField.getWriterColumn(),convertValue(dataField,strValue.trim()));
				}
			} catch (ParserException e) {
				logger.error("接口" + interfaceId + "解析" + fileName + "第" + record.getLineNum()
                        + "行" + dataField.getWriterColumn() + ":" + strValue.trim() + "转换异常  " + e.getMessage());
			}
		}
		return record;
    	
    }
    
	
	/**
	 * 计算字符串长度,中文算2位
	 * @param str 字符串
	 * @return 长度
	 */
	private int countString(String str){
		try {
			return str.getBytes("GBK").length;
		} catch (Exception e) {
			return str.length();
		}
	}
	
	/**
	 * 截取字符串,中文当做2位处理
	 * @param str 原字符串
	 * @param start 开始下标
	 * @param end 结束下标
	 * @return 截取的字符串
	 */
	private String cutString(String str,int start,int end){
        int length = end - start;//计算长度
        byte[] dest = new byte[length];//新建byte数组
        try {
        	//拷贝byte数组
        	System.arraycopy(str.getBytes("GBK"), start, dest, 0, length);
        	 return new String(dest,"GBK");//返回截取字符串
		} catch (UnsupportedEncodingException e) {
			return str.substring(start, end);
		}
	}
	
	/**
	 * 数据格式转换，如果数据值为空则直接返回
	 * @param dataField 输出字段域
	 * @param strValue 输出值
	 * @return Object
	 * @throws ParserException 数据转换异常
	 */
	private Object convertValue(DataField dataField, Object strValue) throws ParserException {
		String type = dataField.getType();
		if(!"".equals(strValue)&&null!=type){
			if("number".equalsIgnoreCase(type)){
				strValue = new NumberFieldFormat(dataField.getFormat()).parse(strValue.toString());
			}else if("date".equalsIgnoreCase(type)){//日期格式
				strValue = new DateFieldFormat(dataField.getFormat()).parse(strValue.toString());
			} 
		}
		return strValue;
	}
	
	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private Object strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj;
	}
	
    /**
    *
    * @param filename 目标文件
    * @param charset 目标文件的编码格式
    */
	protected void checkFileEnd(File filename, String charset) {

       RandomAccessFile rf = null;
       try {
           rf = new RandomAccessFile(filename, "r");
           long len = rf.length();
           long start = rf.getFilePointer();
           long nextend = start + len - 1;
           String line;
           rf.seek(0);
           line = rf.readLine();
           if (line == null || line.trim().equals("")) {
              	logger.error("线程--接口" + dataParserConfig.getId() +"检查BEGIN==>标识符失败:"+rf.readLine());
               	throw new ServiceException("线程--接口" + dataParserConfig.getId() +"检查BEGIN==>标识符失败:"+rf.readLine());
           }else{
               String end = new String(line.getBytes("ISO-8859-1"), charset);
               if(end != null && end.toUpperCase().contains("BEGIN==>")){
               }else{
	               	logger.error("线程--接口" + dataParserConfig.getId() +"检查BEGIN==>标识符失败:"+rf.readLine());
	               	throw new ServiceException("线程--接口" + dataParserConfig.getId() +"检查BEGIN==>标识符失败:"+rf.readLine());
               }
           }
           rf.seek(nextend);
           int c = -1;
           while (nextend > start) {
               c = rf.read();
               if (c == '\n' || c == '\r') {
                   line = rf.readLine();
                   if (line != null && !line.trim().equals("")) {
                       String end = new String(line.getBytes("ISO-8859-1"), charset);
                       if(end != null && end.toUpperCase().contains("END<==")){
                       	return;
                       }
                   }
                   nextend--;
               }
               nextend--;
               rf.seek(nextend);
               if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
               	logger.error("线程--接口" + dataParserConfig.getId() +"检查END<==标识符失败:"+rf.readLine());
                   throw new ServiceException("线程--接口" + dataParserConfig.getId() +"检查END<==标识符失败:"+rf.readLine());
               }
           }

       } catch (FileNotFoundException e) {
       	logger.error("线程--接口" + interfaceId +"文件没有发现" + e.getMessage());
       } catch (IOException e) {
       	logger.error("线程--接口" + interfaceId +"输入输出错误" + e.getMessage());
       } finally {
           try {
               if (rf != null)
                   rf.close();
           } catch (IOException e) {
           	logger.error("线程--接口" + interfaceId +"输入输出错误" + e.getMessage());
           }
       }
   }
	
	/**
     * 解析所有文本XML数据，放入List
     * @return
     */
    private boolean readXmlList (String encoding){
        logger.info("接口" + interfaceId + "开始读取输入文件");
        int result = 0;
        Iterator iteartor = fileInputStreams.entrySet().iterator();
        //如果in_zidb中输入文件类型(infiletype)为空则走原来的正常流程  hzg 2014.3.22
        if(dataParserConfig.getFileUcMap()==null||dataParserConfig.getFileUcMap().isEmpty()){
        	while (iteartor.hasNext()) {
        		Map.Entry<String, FileInputStream> entry = (Map.Entry<String, FileInputStream>)iteartor.next();
        		result += readXmlFile(entry.getKey(), entry.getValue(), encoding) ? TRUE : FALSE;
        	}
        }else{ //否则，取in_zidb用户中心来处理  hzg 2014.3.22 其中增加before方法，用于写文件前可以取到用户中心来做相关操作
        	while (iteartor.hasNext()) {
        		logger.info("开始执行文件输入@@@@@@@@@@@@@@@@");
        		
        		Map.Entry<String, FileInputStream> entry = (Map.Entry<String, FileInputStream>)iteartor.next();
        		if(dataParserConfig.getFileUcMap().containsKey(entry.getKey())){ //v如果map中包含将要读取的文件名，则取出该文件名的用户中心
        			dataParserConfig.setUsercenter(dataParserConfig.getFileUcMap().get(entry.getKey()));
        		}else{
        			logger.error("接口" + interfaceId + "在in_zidb表中找不到输入文件名，请检查in_zidb");
        		}
        		before();
        		result += readXmlFile(entry.getKey(), entry.getValue(), encoding) ? TRUE : FALSE;
        	}
        }
    	return result == fileInputStreams.size() ? true : false;
    }
    
    /**
     * 读取解析Xml文件的数据
     * @param fileName
     * @param fileInputStream
     * @param encoding
     * @return 数字 1 表示成功 ，数字 0 表示失败
     */
	protected boolean readXmlFile(String fileName, FileInputStream fileInputStream, String encoding){
		logger.info("接口" + interfaceId + "开始读取文件" + fileName);
		boolean result = false;
		List<Record> lineList = new ArrayList<Record>();
		//xml格式的lineNum第一行从<row>开始
		int lineNum = 1;
		long startTime = System.currentTimeMillis();	
		try {
			file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new BufferedReader(new InputStreamReader(fileInputStream,encoding)));
			//获取指定路径下的所有row节点
			List<Element> rowElements = doc.selectNodes("Root/RequestBody/AppRequest/AppReqBody/table/rows/row");
			List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
			for (Element row : rowElements) {
				Record record = null;
				if(!beforeRecord(row.asXML(),fileName,lineNum)){ //跳过某一行后，记录一下行数
					lineNum++;
					continue;
				}
				logger.info("接口" + interfaceId + "读取文件" + fileName + "第" + lineNum + "行");
				record = parserXml(fileName,row);
				record.setLineNum(lineNum);
				logger.info("接口" + interfaceId + "读取文件" + fileName + "第" + lineNum + "行");
				if(!afterRecord(record)){ //增加方法，毛需求1050，1051，1052等使用  hzg 2013-4-13
					lineNum++;
					continue;
				}
				lineNum++;
				if(record != null){ //判断是否为record空 hzg 2014.3.37
					lineList.add(record);					
				}
				if (lineList.size() == LINE_NUM) {
					list.add(exeTask(fileName, lineNum, lineList));
					lineList = new ArrayList<Record>();
				}
			}
			//before();
			if (lineList.size() > 0) {
				list.add(exeTask(fileName, lineNum, lineList));
			}
			//设置文件解析结束时间
			file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			result = collectTask(list);
			finishAfter();
		} catch (UnsupportedEncodingException e) {
			logger.error("接口" + interfaceId + "不支持编码 " + e.getMessage());
		} catch (DocumentException e) {
			logger.error("接口" + interfaceId + "XML文件读取异常 " + e.getMessage());
		} finally{
			after();
			if(result){ //true
				//此文件时成功文件---解析没有错  如果是txt-->业务表  要记录文件到日志表中
				if(!dataParserConfig.getWriterConfig().getTable().toLowerCase().startsWith("in")){
					file_info(fileName,result);
				}
			}else{//false
				file_info(fileName,result);
			}
		}
		logger.info("接口" + interfaceId + "结束读取文件" + fileName + " 总记录行数" + (lineNum-1));
		logger.info("文件解析完成，共解析记录"+(lineNum-1)+"条,耗时"+(System.currentTimeMillis()-startTime)+"毫秒.");
		return result;
	}
	
	/**
    *
    * @param rowObject
    * @return
    */
	private Record parserXml(String fileName,Element rowElement){
	   	Record record = new Record();
	   	//得到所需入库的字段
	   	DataField[] dataFileds = dataParserConfig.getDataFields();
	   	for(DataField dataField : dataFileds){
	   		//输入字段
	   		String readerColumn = dataField.getReaderColumn();
	   		if(readerColumn == null){	//输入字段不存在
	   			continue;
	   		}
	   		//输出字段
	   		String writerColumm = dataField.getWriterColumn();
	   		//获取输入字段对应的值
	   		String strValue = rowElement.attributeValue(readerColumn);
	   		//输入字段对应的值为空，转换为大写后再取一次
	   		strValue = strValue == null ? rowElement.attributeValue(readerColumn.toUpperCase()) : strValue;
	   		if(strValue == null){	//属性不存在
	   			logger.error("接口" + interfaceId + "解析" + fileName + "第" + record.getLineNum()
	   					+ "行，属性:" + readerColumn + "不存在");
	   			continue;
	   		}
			try {
				if(dataField.getIsParam()){ //放存要入库的字段和值
					record.put(writerColumm, convertValue(dataField,strValue.trim()));
				}else{  //存放要做为参数的字段为值，配置中的属性为isParam="true"
					record.putParamObject(writerColumm, convertValue(dataField,strValue.trim()));
				}
			} catch (ParserException e) {
				logger.error("接口" + interfaceId + "解析" + fileName + "第" + record.getLineNum()
                       + "行" + dataField.getWriterColumn() + ":" + strValue.trim() + "转换异常  " + e.getMessage());
			}
		}
		return record;
   }
}



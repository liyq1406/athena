package com.athena.component.exchange.db;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.dom4j.io.XMLWriter;

import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.OutputDataService;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.field.DataField;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.utils.UUIDHexGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 下午1:58
 * 读取数据库写入到txt文本
 */
@Component
public class DBOutputTxtSerivce implements OutputDataService{

    protected final Logger logger=Logger.getLogger(DBOutputTxtSerivce.class);//定义日志方法
    // @Inject
    protected AbstractIBatisDao baseDao;//注入baseDao

    protected DataParserConfig dataParserConfig;

    private static final String AFTER = "after";

    private static final String LineSign = "\\n"; //换行符

    protected final static int PAGESIZE = 10000; //每次固定输入10000条(可配置，默认为10000)

    protected DataField[] dataFields;

    protected String sourceId = ""; //数据源ID

    protected  int total=0; //处理总记录数
    //接口编号
    protected String interfaceId; 
    //文件运行时间
    protected String file_begintime;
	
    public boolean  write(DataParserConfig dataParserConfig) throws ServiceException {
        logger.info("接口" + dataParserConfig.getId() + "开始输出");
        //根据配置获得SQL或SQLID
        boolean flag = false;
        StringBuffer fileName = new StringBuffer();
        file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        interfaceId = dataParserConfig.getId();
        this.dataParserConfig = dataParserConfig;
        this.baseDao = dataParserConfig.getBaseDao();
        try{
            ExchangerConfig readerConfig = dataParserConfig.getReaderConfig(); //得到reader的属性值
            this.dataFields = dataParserConfig.getDataFields(); // 得到配置文件字段
            sourceId = readerConfig.getDatasourceId();
            ExchangerConfig[] ecs = dataParserConfig.getWriterConfigs();
            before();
            for (ExchangerConfig ec : ecs) {
                outPut(ec, readerConfig, createOut(ec));
                fileName.append(ec.getFileName()).append(",");
            }
            after();
            afterAllRecords(ecs);
            flag = true;
        } catch (SQLException sqlEx){
            logger.error(sqlEx.getMessage());
            throw new ServiceException(sqlEx);
        } catch(RuntimeException ex){
            logger.error(ex.getMessage());
            throw new ServiceException(ex);
        }finally{
            logger.info("接口" + dataParserConfig.getId() + "结束输出");
        	file_info(fileName.toString(),flag);

        }
        return flag;
    }

    protected void outPut(ExchangerConfig write, ExchangerConfig read,OutputStreamWriter out) throws SQLException{
        int totalPage = getTotalPage(write.getUsercenter(), read.getSql(),read.getIsAllSet());
        try {
            //文件生成前的方法调用
            fileBefore(out);
            for(int i=0;i<totalPage;i++){
                outPutTxt(i,write,read,  out);
            }
            fileAfter(write, read, out);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("接口" + dataParserConfig.getId() + "无法关闭文件", e);
            }
        }
    }



    /**`
     * 计算分多少页
     */
    public int getTotalPage(String usercenter, String sql,String flagStr) throws SQLException{
        int totalNum = getTotalNum(usercenter, sql,flagStr);
        int totalPage  = totalNum/PAGESIZE + (totalNum%PAGESIZE==0 ? 0 : 1);
        return totalPage;
    }


    /**
     *
     * @param usercenter
     * @param sqlId
     * @param flagStr
     * @return
     * @throws SQLException
     */
	private int getTotalNum(String usercenter,String sqlId,String flagStr) throws SQLException{
    	int countNum = 0;
        String head = "select count(1) COUNTNUM from (";
        String foot = " ";
        Map<String,String> params = putParams(usercenter, head,foot.toString(),flagStr); //分页参数替换
        Map dataMap= (Map)baseDao.getSdcDataSource(sourceId).selectObject(sqlId, params);
        countNum = Integer.parseInt(dataMap.get("COUNTNUM").toString());
        return countNum;
    }

    /**
     *
     * @param pageNum
     * @param write
     * @param read
     * @param out
     * @throws SQLException
     */
	private void outPutTxt(int pageNum,ExchangerConfig write,ExchangerConfig  read, OutputStreamWriter out) throws SQLException{
    	int endPage = (pageNum+1)*PAGESIZE;  //结束页数条数
    	int startPage = pageNum*PAGESIZE+1;     //开始页数条数
        String head = "select * from ("; 
        String foot =" and rownum <= "+endPage+") t"
        			+" where t.RN >= "+startPage ;
        Map<String,String> params = putParams(write.getUsercenter(),head,foot.toString(),read.getIsAllSet()); //分页参数替换
        List<Map<String,String>> dataList = baseDao.getSdcDataSource(sourceId).select(read.getSql(), params);
        txtOutPut(dataList, out); //直接输出为txt
    }
    
    /**
     * 参数替换
     * @param head 开头参数
     * @param foot 结尾参数
     * @param flagStr
     * @return
     */
    protected Map<String,String> putParams(String usercenter, String head,String foot,String flagStr){
    	Map<String,String> params = new HashMap<String,String>();
    	String temp_foot = "".equals(foot.trim())? ")": foot;
    	String tempStr = (head.indexOf("COUNTNUM")!= -1)?" )" :" ";
    	String strmiddle = "false".equals(flagStr)? " "+makeIncrementSql()+" ":" ";
    	String strfoot = "false".equals(flagStr)? " "+foot + tempStr: temp_foot;
        if (!usercenter.equals("ALL")) {
            params.put("usercenter", this.getUsercenter(usercenter));
        }
        params.put("middle", strmiddle);
        params.put("head", head);
        params.put("foot", strfoot);
    	return params;
    }
    /**
     * 将数据集转换为配置的字段集
     * @param dataList
     * @return
     */
    protected List<Map<String,Object>> getDoWriterLine( List<Map<String,String>> dataList){
    	List<Map<String,Object>> outPutlist =  new ArrayList<Map<String,Object>>(); //将输出的数据
        for(int i=0;i<dataList.size();i++){
            Map<String,Object> result = new HashMap<String,Object>();
            for(DataField dataField:dataFields){
                Object value = dataList.get(i).get(dataField.getReaderColumn().toUpperCase());
                if(value!=null&&dataField.getWriterColumn()!=null){
                    result.put(dataField.getWriterColumn(), value);
                }
            }
            outPutlist.add(result);
        }
        return outPutlist;
    }
    
    /**
     * 直接输出为TXT文本 
     * @param list 输出数据集
     */
    private void txtOutPut(List<Map<String,String>> list,OutputStreamWriter out){
    	beforeRecords(list);
		List<Map<String,Object>> outPutlist = getDoWriterLine(list);
		beforeAllRecordsSave(list,outPutlist);
        for(int i=0;i<outPutlist.size();i++){
        	executeOutPut(out, outPutlist.get(i));
        	afterRecord(out,outPutlist.get(i));
        }
    }


    /**
     * 创建输出流
     * @param ec 配置文件对象
     * @return
     */
    private OutputStreamWriter createOut(ExchangerConfig ec) {

        String filePath = ec.getFilePath();
        String fileName = ec.getFileName();
        String encoding = ec.getEncoding();

        //默认为GBK
        if(encoding==null){
            encoding = "GBK";
        }
        //System.out.println("filePath:"+filePath+"---"+"fileName:"+fileName+"---"+"encoding:"+encoding);
        //1：如果没有用户输入的路径 就创建此路径
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }

        //2:生成输出文件路径
        String outName = createRealPath(filePath,fileName);

        //3:创建输出流
        OutputStreamWriter writer = null;
        try {
        	if("true".equals(ec.getIsGoOnOut())){
                writer = new OutputStreamWriter(new FileOutputStream(new File(outName),true),encoding);
        	}else{
                writer = new OutputStreamWriter(new FileOutputStream(new File(outName)),encoding);	
        	}
        } catch (UnsupportedEncodingException e) {
            logger.error("接口" + sourceId + "不支持编码格式为" + encoding, e);
            throw new ServiceException("接口" + sourceId + "不支持编码格式为", e);
        } catch (FileNotFoundException e) {
            logger.error("接口" + sourceId + "没找到文件" + outName, e);
            throw new ServiceException("接口" + sourceId + "没找到文件" + outName, e);
        }

        return writer;
    }
    
    /**
     * 创建XML输出流
     * @author 贺志国
     * @date 2015-12-26
     * @param ec
     * @return
     */
    protected XMLWriter createXmlOut(ExchangerConfig ec){
    	 String filePath = ec.getFilePath();
         String fileName = ec.getFileName();
         
         //1：如果没有用户输入的路径 就创建此路径
         File file = new File(filePath);
         if(!file.exists()){
             file.mkdirs();
         }
         //2:生成输出文件路径
        String outFilePathName = createRealPath(filePath,fileName);
        //3:创建输出流
        XMLWriter writer = null;
        try {
        	if("true".equals(ec.getIsGoOnOut())){
                writer = new XMLWriter(new FileOutputStream(new File(outFilePathName),true));
        	}else{
                writer = new XMLWriter(new FileOutputStream(new File(outFilePathName)));	
        	}
        } catch (UnsupportedEncodingException e) {
            logger.error("接口不支持编码格式为" + e);
            throw new ServiceException("接口不支持编码格式为", e);
        } catch (FileNotFoundException e) {
            logger.error("接口没找到文件" + outFilePathName, e);
            throw new ServiceException("接口没找到文件" + outFilePathName, e);
        }
    	return writer;
    }

    /**
     * 生成输出文件路径
     * @param filePath
     * @param fileName
     * @return
     */
    protected String createRealPath(String filePath, String fileName) {
        StringBuffer sb = new StringBuffer();
        sb.append(filePath);
        sb.append(File.separator);
        sb.append(fileName);
        return sb.toString();
    }

    /**
     * 将数据按格式输出到文本
     * @param out 输出流
     * @param rowObject 
     */
    public void executeOutPut(OutputStreamWriter out, Map<String,Object> rowObject) {

        Map rowObjectMap = (Map) rowObject;
        StringBuffer sb = new StringBuffer();

        //处理要输出的的字符信息
        DataField[] df = dataParserConfig.getDataFields();
        for(int i=0;i<df.length;i++){
            String writerColumn = df[i].getWriterColumn(); //列名
            int length = df[i].getLength(); //列长度
            String separate = df[i].getSeparate(); //分隔符
            String separate_size = df[i].getSeparate_size(); //分隔符的位置

            //作为字符串处理--- null
            String columnValue;
            Object columnObject = rowObjectMap.get(writerColumn);
            columnValue = columnObject!=null?(columnObject.toString()): "";

            //创建一个字段
            createField(sb,columnValue,length,separate,separate_size);
        }

        //生成换行符
        if(sb.length()>0){
            sb.append("\n");
        }

        //out对象输出
        try {
            out.write(sb.toString());
            out.flush();
        } catch (IOException e) {
            logger.error("接口" + sourceId + "IO输出异常", e);
            throw new ServiceException("接口" + sourceId + "IO输出异常", e);
        }

    }

    /**
     * 生成一个字段
     * 	1：如果columnValue为空，则输出length制定长度的字符；
     * 	2：如果columenValue不为空，则输出制定长度的字符；不足长度的补separate；
     * 	3：默认separate是空格；separate_size是 after;
     * @param sb
     * @param columnValue
     * @param length  字段输出的长度
     * @param separate
     * @param separate_size 
     */
    protected void createField(StringBuffer sb, String columnValue, int length,
                             String separate, String separate_size) {
        if(columnValue!=null){
            //输出字段不为空
            String charset = dataParserConfig.getWriterConfigs()[0].getEncoding();
            int value_length = 0;
            try {
                value_length = columnValue.getBytes(charset).length;
            } catch (UnsupportedEncodingException e) {
                logger.error("接口" + sourceId + "创建字符串不支持编码格式为" + charset, e);
            }
            if(length>0){
                if(value_length>=length){
                    //字段长度比要输出的长度长 则从左边截取
                    sb.append(makeStrByLength(columnValue,length,charset));
                }else{
                    if(separate_size!=null){
                        //填充符位置不为空
                        if(separate!=null){
                            //填写了 分隔符
                            fillSeparate(length-value_length,separate_size,sb,separate,columnValue);
                        }else{
                            //没有填写分隔符  则默认补空格
                            fillSeparate(length-value_length, separate_size, sb, " ", columnValue);
                        }
                    }else{
                        //填充符位置为空 默认在后面添加
                        if(separate!=null){
                            //填写了 分隔符
                            fillSeparate(length-value_length,AFTER,sb,separate,columnValue);
                        }else{
                            //没有填写分隔符  则默认补空格
                            fillSeparate(length-value_length, AFTER, sb, " ", columnValue);
                        }
                    }
                }
            }else{
                //没有填写长度  就默认输出 columnValue
                sb.append(columnValue);
            }
        }else{
            //输出字段为空
            if(length>0){
                if(separate!=null){
                    //填充符不为空
                    for(int i=0;i<length;i++){
                        sb.append(separate);
                    }
                }else{
                    //填充符为空 默认用空格填充
                    for(int i=0;i<length;i++){
                        sb.append(" ");
                    }
                }
            }
        }

    }

    /**
     * 如果数据库取出的字段比要生成的字段长，则按照字节来截取
     * @param columnValue
     * @param length
     * @param charset
     * @return
     */
    private String makeStrByLength(String columnValue,int length,String charset){
        String result = null;
        try {
            byte[] bys = columnValue.getBytes(charset);
            byte[] bs = new byte[length];
            for(int i=0;i<length;i++){
                bs[i] = bys[i];
            }
            result = new String(bs,charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("接口" + sourceId + "取出数据库字符串不支持编码格式为" + charset, e);
        }


        return result;
    }

    /**
     * 填写分隔符
     * @param i 填写的位数
     * @param separate_size 分隔符位置
     * @param sb
     * @param separate  分隔符
     * @param columnValue  此字段的值
     */
    private void fillSeparate(int i, String separate_size, StringBuffer sb,
                              String separate,String columnValue) {

        if(AFTER.equals(separate_size)){
            //在后面添加
            sb.append(columnValue);
            for(int j=0;j<i;j++){
                //为了支持换行
                if(LineSign.equals(separate)){
                    sb.append("\n");
                    break;
                }else{
                    sb.append(separate);
                }
            }
        }else{
            //在前面添加
            for(int j=0;j<i;j++){
                //为了支持换行
                if(LineSign.equals(separate)){
                    sb.append("\n");
                    break;
                }else{
                    sb.append(separate);
                }
            }
            sb.append(columnValue);
        }

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
     * 生成所需查询的用户中心
     * @param user
     * @return
     */
	private String getUsercenter(String user){
		String[] str = user.split(",");
		String userCenter = "";
		for(int i=0;i<str.length;i++){
			if(i==str.length-1){
				userCenter +="'"+str[i]+"'";
			}else{
				userCenter +="'"+str[i]+"',";
			}
		}
		return userCenter;
    }
	
    /**
     * 输出每条记录后需要执行的方法
     * @param rowObject  一条记录
     */
    public void afterRecord(OutputStreamWriter out,Map<String,Object> rowObject){
    	
    }
    
    public void  afterAllRecords(ExchangerConfig[] ecs){
        for (ExchangerConfig ec : ecs) {
            String encoding = ec.getEncoding();
            //默认为GBK
            if(encoding==null){
                encoding = "GBK";
            }
            File file = new File(ec.getFilePath() + File.separator + ec.getFileName());
            readFileLineNum(encoding,file);
            String outPutfileName = ec.getAthfilePath() + File.separator + ec.getFileName();
            File newfilepath = new File(ec.getAthfilePath());
            if(!newfilepath.exists()){
            	newfilepath.mkdirs();
            }
            moveOutFile(encoding,ec.getFilePath() + File.separator + ec.getFileName(),outPutfileName);
        }
    }

    /**
     * 读取文件行数据
     * @param file
     */
    public int readFileLineNum(String encoding,File file) {
         return 0;
    }

    /**
     * 读取文件行数据
     * @param file
     */
    public void moveOutFile(String encoding,String oldfileName,String outPutfileName) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {   
            int bytesum = 0;   
            int byteread = 0;   
            File oldfile = new File(oldfileName);   
            if (oldfile.exists()) { // 文件存在时    
            	inStream = new FileInputStream(oldfileName); // 读入原文件   
            	fs = new FileOutputStream(outPutfileName); 
                byte[] buffer = new byte[1444];   
                while ((byteread = inStream.read(buffer)) != -1) {   
                    bytesum += byteread; // 字节数 文件大小    
                    fs.write(buffer, 0, byteread);   
                }   
            }   
        } catch (Exception e) {   
            logger.error("接口" + sourceId + "移动文件错误" + oldfileName, e);
            throw new ServiceException("接口" + sourceId + "移动文件错误" + oldfileName, e);
        }finally {
            try {
            	inStream.close();
            	fs.close();
            } catch (IOException e) {
                logger.error("接口" + interfaceId + "无法关闭文件", e);
                throw new ServiceException("接口" + interfaceId + "无法关闭文件", e);
            }
        }   
    }
    
    public void  beforeAllRecordsSave(List<Map<String,String>> sourcelist,List<Map<String,Object>> outPutlist){
    	int num = outPutlist == null? 0 : outPutlist.size();
    	this.setTotal(num);
    }

    public void  beforeRecords(List<Map<String,String>> sourcelist){

    }
    
	/**
	 * 记录文件日志
	 * @author Hezg
	 * @date 2013-2-4
	 * @param wenjmc 文件名称
	 * @param file_satus 运行状态        1 成功(已执行)  -1 失败(已执行)
	 * @return 
	 */
    protected void file_info(String wenjmc,boolean file_satus) {
        AtomicInteger insert_num = new AtomicInteger();
        AtomicInteger update_num = new AtomicInteger();
        AtomicInteger error_num = new AtomicInteger();
		Map<String,String> params = new HashMap<String,String>();
		params.put("SID", UUIDHexGenerator.getInstance().generate());
		params.put("INBH", interfaceId);
		params.put("fileName", wenjmc);
		params.put("file_begintime", file_begintime);
		params.put("file_endtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		params.put("insert_num", String .valueOf(insert_num));
		params.put("update_num", String .valueOf(update_num));
		params.put("error_num", String .valueOf(error_num));
		params.put("file_satus", file_satus?"1":"-1");
		
		FileLog.getInstance(sourceId).insert_file_info(params,dataParserConfig.getBaseDao());
	}
    
	/**
	 * 文件解析前的方法暴露
	 * @param writer
	 */
	public void fileBefore(OutputStreamWriter writer) {
    
	}
    
	/**
	 * 接口文件输出前执行方法
	 */
	public void before() {
    
	}
	
	/**
	 * 接口文件输出后执行方法
	 */
	public void after() {
    
	}
	
	/**
	 * 为了生成文件尾部
	 * @param out
	 */
	public void fileAfter(ExchangerConfig write, ExchangerConfig read,OutputStreamWriter out) {

	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}

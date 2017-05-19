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
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
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
 * User: xss
 * Date: 15-12-29
 * 读取数据库写入到XML文本
 */
@Component
public class DBOutputXmlSerivce implements OutputDataService{

    protected final Logger logger=Logger.getLogger(DBOutputTxtSerivce.class);//定义日志方法
    // @Inject
    protected AbstractIBatisDao baseDao;//注入baseDao

    protected DataParserConfig dataParserConfig;

    protected DataField[] dataFields;

    protected String sourceId = ""; //数据源ID
    protected String caption = ""; //标题

    protected  int total=0; //处理总记录数
    //接口编号
    protected String interfaceId; 
    //文件运行时间
    protected String file_begintime;
    protected String file_begintime2;
	
    public boolean  write(DataParserConfig dataParserConfig) throws ServiceException {
        logger.info("接口" + dataParserConfig.getId() + "开始输出");
        //根据配置获得SQL或SQLID
        boolean flag = false;
        StringBuffer fileName = new StringBuffer();
        file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        file_begintime2 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        interfaceId = dataParserConfig.getId();
        caption =  dataParserConfig.getCaption();
        
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
            send(ecs);
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

    protected void outPut(ExchangerConfig write, ExchangerConfig read,XMLWriter out) throws SQLException{
        //int totalPage = getTotalPage(write.getUsercenter(), read.getSql(),read.getIsAllSet());
        try {
        	outPutXml(write,read, out);
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
//    public int getTotalPage(String usercenter, String sql,String flagStr) throws SQLException{
//        int totalNum = getTotalNum(usercenter, sql,flagStr);
//        int totalPage  = totalNum/PAGESIZE + (totalNum%PAGESIZE==0 ? 0 : 1);
//        return totalPage;
//    }


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
	private void outPutXml(ExchangerConfig write,ExchangerConfig  read, XMLWriter out) throws SQLException{
//    	int endPage = (pageNum+1)*PAGESIZE;  //结束页数条数
//    	int startPage = pageNum*PAGESIZE+1;     //开始页数条数
        String head =" "; 
        String foot =" ";
        Map<String,String> params = putParams(write.getUsercenter(),head,foot.toString(),read.getIsAllSet()); //分页参数替换
        List<Map<String,String>> dataList = baseDao.getSdcDataSource(sourceId).select(read.getSql(), params);
        xmlOutPut(dataList, out); //生成对象并输出元素内容
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
     * 生成输出XML元素对象
     * XMLWriter
     */
    private void xmlOutPut(List<Map<String,String>> list,XMLWriter out){
    	//beforeRecords(list);
		List<Map<String,Object>> outPutlist = getDoWriterLine(list); //将数据转换为List对应的Map集合
		beforeAllRecordsSave(list,outPutlist); //统计总数
		
		//报文头
        try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement( "Root" );		
			Element RequestHead = root.addElement("RequestHead");
			
			Element MessageType = RequestHead.addElement("MessageType").addText( "4" );
			Element SystemType = RequestHead.addElement("SystemType").addText( "1" );
			Element SourceSystem = RequestHead.addElement("SourceSystem").addText( "ATHENA" );
			Element TargetSystem  = RequestHead.addElement("TargetSystem ");
			Element ServiceName = RequestHead.addElement("ServiceName");
			Element ServiceOperation = RequestHead.addElement("ServiceOperation");
			Element ServiceVersion = RequestHead.addElement("ServiceVersion"); 
			Element ESBInTime  = RequestHead.addElement("ESBInTime"); 
			Element ESBOutTime   = RequestHead.addElement("ESBOutTime "); 
			Element Version   = RequestHead.addElement("Version ").addText( "1.0" ); 
			Element Reserved   = RequestHead.addElement("Reserved ").addText( "1" );  
			
			afterHead(RequestHead);//生成报文头后方法
			
			//报文体公共区域
			Element RequestBody = root.addElement("RequestBody");
			Element AppRequest = RequestBody.addElement("AppRequest");
			Element AppReqHead = AppRequest.addElement("AppReqHead");
			Element TradeCode = AppReqHead.addElement("TradeCode");			
		    Element ReqSerialNo = AppReqHead.addElement("ReqSerialNo").addText(file_begintime2);  
			
			Element TradeTime = AppReqHead.addElement("TradeTime").addText(file_begintime);
			Element TradeDescription  = AppReqHead.addElement("TradeDescription");
			Element TradeLogLevel  = AppReqHead.addElement("TradeLogLevel").addText("1");
			Element Reserved2   = AppReqHead.addElement("Reserved");
			
			//报文体数据区域
			Element AppReqBody = AppRequest.addElement("AppReqBody");
			Element table = AppReqBody.addElement("table");
			table.addAttribute("ID", interfaceId);
			table.addAttribute("NAME", caption);
			table.addAttribute("NUM",new Integer(outPutlist.size()).toString()); 
			Element rows = table.addElement("rows");  
			
			//报文体
	        for(int i=0;i<outPutlist.size();i++){ 
	        	executeOutPut(rows, outPutlist.get(i));
	        }
	        
	        afterBody(RequestBody);//生成报文体后方法
	        out.write(document);
	        out.flush();
	        out.close(); 
        } catch (IOException e) {
            logger.error("接口" + sourceId + "IO输出异常", e);
            throw new ServiceException("接口" + sourceId + "IO输出异常", e);
        }
		

    }
    
    
    /*修改报文头内容*/
    public  void afterHead (Element RequestHead){
    	
    }
    
    
    /*修改报文头体内容*/
    public  void afterBody (Element RequestBody){
    	
    }
    
    
    
    


    /**
     * 创建输出流
     * @param ec 配置文件对象
     * @return
     */
    private XMLWriter createOut(ExchangerConfig ec) {
        String filePath = ec.getFilePath();
        String fileName = ec.getFileName();
        String encoding = ec.getEncoding();

        fileName=fileName.replaceAll("txt", "xml"); 
        //默认为GBK
        encoding = "UTF-8";
        //System.out.println("filePath:"+filePath+"---"+"fileName:"+fileName+"---"+"encoding:"+encoding);
        //1：如果没有用户输入的路径 就创建此路径
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }

        //2:生成输出文件路径
        String outName = createRealPath(filePath,fileName);

        XMLWriter writer = null;
        
        //3:创建输出流
        //OutputStreamWriter writer = null;
        try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");//设置编码格式  
            writer = new XMLWriter(new FileOutputStream(new File(outName)),format);	
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
    public void executeOutPut(Element rows, Map<String,Object> rowObject) { 
        Map rowObjectMap = (Map) rowObject;
        StringBuffer sb = new StringBuffer();

        //处理要输出的的字符信息
        DataField[] df = dataParserConfig.getDataFields();
        Element row  = rows.addElement("row");
        for(int i=0;i<df.length;i++){
            String writerColumn = df[i].getWriterColumn(); //列名

            //作为字符串处理--- null
            String columnValue;
            Object columnObject = rowObjectMap.get(writerColumn);
            columnValue = columnObject!=null?(columnObject.toString()): "";
        	row.addAttribute(writerColumn, columnValue);
            
            //创建一个字段
            //createField(sb,columnValue,length,separate,separate_size);
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
    
	/**
	 * 为了生成文件尾部
	 * @param out
	 */
	public void fileAfter(ExchangerConfig write, ExchangerConfig read,OutputStreamWriter out) {

	}
    
    
    public void  afterAllRecords(ExchangerConfig[] ecs){
        for (ExchangerConfig ec : ecs) {
            String encoding = ec.getEncoding();
            
            String fileName = ec.getFileName();  
            fileName=fileName.replaceAll("txt", "xml"); 
            
            
            //默认为GBK
            if(encoding==null){
                encoding = "UTF-8";
            }
            //0012708
	    	String fileName2 = file_begintime2+"_"+fileName ;
				    	
            File file = new File(ec.getFilePath() + File.separator  + fileName);
            File file2 = new File(ec.getFilePath() + File.separator  + fileName2);
            
            readFileLineNum(encoding,file);
           
            String outPutfileName = ec.getAthfilePath() + File.separator  + fileName;
            String outPutfileName2 = ec.getFilePath() + File.separator  + fileName2;
           
            File newfilepath = new File(ec.getAthfilePath());
            if(!newfilepath.exists()){
            	newfilepath.mkdirs();
            }
            moveOutFile(encoding,ec.getFilePath() + File.separator + fileName,outPutfileName);
            moveOutFile(encoding,ec.getFilePath() + File.separator + fileName,outPutfileName2);           
        }
    }
    
    /*发送*/
    public void  send(ExchangerConfig[] ecs){
    	
    	
    	
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
	 * 接口文件输出前执行方法
	 */
	public void before() {
    
	}
	
	/**
	 * 接口文件输出后执行方法
	 */
	public void after() {
    
	}
	

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}

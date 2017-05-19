package com.athena.component.exchange.txt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.TxtCommand;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.runner.Runner;
import com.toft.core2.dao.database.DbUtils;
import com.toft.utils.UUIDHexGenerator;


/**
 * txt输出
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-5
 */
public class TxtDataWriter implements DataWriter {

	protected static Logger logger = Logger.getLogger(TxtDataWriter.class);	//定义日志方法
	protected DataExchange dataEchange;
	
	//接口表连接
	protected Connection interfaceConn;
	//业务连接
	protected Connection businessConn;
	
//	protected Connection conn_1;
//	
//	protected Connection conn_2;
	
	protected  int total=0; //处理总记录数
	
    //错误信息
    private ArrayList<Map<String,String>> errorMessageList = new ArrayList<Map<String,String>>();	

	
	public ArrayList<Map<String, String>> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<Map<String, String>> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Connection getInterfaceConn() {
		return interfaceConn;
	}

	public void setInterfaceConn(Connection interfaceConn) {
		this.interfaceConn = interfaceConn;
	}

	public Connection getBusinessConn() {
		return businessConn;
	}

	public void setBusinessConn(Connection businessConn) {
		this.businessConn = businessConn;
	}

	public DataExchange getDataEchange() {
		return dataEchange;
	}

	public void setDataEchange(DataExchange dataEchange) {
		this.dataEchange = dataEchange;
	}
	
	protected DataParserConfig dataParserConfig;
	
	private List<OutputStreamWriter> outs = new ArrayList<OutputStreamWriter>();
	
	public TxtDataWriter(DataParserConfig dataParserConfig) {
		this.dataParserConfig = dataParserConfig;
		//为业务处理拿连接
		if(dataParserConfig.getWriterConfigs()[0].getDatasourceId()!=null){
			this.businessConn = DbUtils.getConnection(dataParserConfig.getWriterConfigs()[0].getDatasourceId());
		}
		
		//设置接口表连接
		this.interfaceConn = DbUtils.getConnection(dataParserConfig.getReaderConfig().getDatasourceId());
			
		if(dataParserConfig.getOut()!=null){
			outs.add(dataParserConfig.getOut());
		}else{
			//初始化 输出流对象
			this.initWriter();
		}		
	}
	
	/**
	 * 初始化 输出流对象 根据dataParserConfig .getWriterConfigs()个数创建不同的输出流对象
	 */
	private void initWriter(){
		//创建流对象出来
		ExchangerConfig[] ecs = dataParserConfig.getWriterConfigs();
		for(ExchangerConfig ec : ecs){
			outs.add(createOut(ec));
		}
	}
	
	/**
	 * 根据ExchangerConfig 创建输出对象流
	 * @param ec
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
			writer = new OutputStreamWriter(new FileOutputStream(new File(outName)),encoding);
			
			//文件生成前的方法调用
			fileBefore(writer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	
	/**
	 * 文件解析前的方法暴露
	 * @param writer
	 */
	public void fileBefore(OutputStreamWriter writer) {
    
	}

	/**
	 * 生成输出文件路径
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	private String createRealPath(String filePath, String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		sb.append(File.separator);
		sb.append(fileName);
		return sb.toString();
	}

	@Override
	public void after() {
		logger.info("输出完毕,共输出"+getTotal()+"条记录！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		
		try{
			//对每行信息进行处理的方法   根据writers的个数开启线程 生成多个文件
			if (line instanceof Map) {
				Map rowObject  = (Map)line;
				//创建线程池
				Runner runner = new Runner(dataParserConfig.getWriterConfigs().length);
				//运行线程池中的线程
				
				/**
				 * 按照用户中心 生成文件
				 */
				for(int i=0;i<outs.size();i++){
					String usercenter = dataParserConfig.getWriterConfigs()[i].getUsercenter(); //用户配置的用户中心				
					if(usercenter!=null || "".equals(usercenter)){
						String userStr = (String) rowObject.get("usercenter");
						String USEStr = (String) rowObject.get("USERCENTER");
						userStr = userStr==null?USEStr:userStr;
						
						//2012-8-16 uerStr为null 则说明 不用做用户中心过滤 则直接去生成文件内容
						//                不为ｎｕｌｌ　则说明有可能要做用户中心的过滤　则进一步判断　usercenter的值
						if(userStr==null){
							runner.addCommand(new TxtCommand<Map<String,Object>>(outs.get(i), dataParserConfig, rowObject));
						}else{
							if(usercenter.contains(userStr) || "ALL".equals(usercenter)){ //如果是ALL或者有对应的用户中心 则输出
								runner.addCommand(new TxtCommand<Map<String,Object>>(outs.get(i), dataParserConfig, rowObject));
							}
						}							
					}else{
						runner.addCommand(new TxtCommand<Map<String,Object>>(outs.get(i), dataParserConfig, rowObject));
					}				
				}
				
				runner.finish(true); //阻塞等待线程结束
				
				//关闭线程池			
				runner.shutdown();
				
				logger.info("-----------------------------");
				logger.info(rowIndex+" : "+ line.toString());
				logger.info("-----------------------------");
			}
		}catch(Exception e){
			//向errorMessageList写入错误信息
			makeErrorMessage(record,e.getMessage());
			//errorMessageList.add(createErrorMessage(record,e.getMessage()));
		}
	}
	
	/**
	 * 创建错误信息
	 * @param record 行信息
	 * @param errorMessage 错误异常信息
	 */
	protected void makeErrorMessage(Record record,String errorMessage){
		errorMessageList.add(createErrorMessage(record.getValue(),errorMessage));
	}
	/**
	 * 查询方法
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected List select(String sql){
		return DbUtils.select(sql, interfaceConn);
	}
	
	
	/**
	 * 单记录查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected Map selectOne(String sql){
		return DbUtils.selectOne(sql, interfaceConn);
	}
	
	/**
	 * sql语句执行
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected int execute(String sql){
		return DbUtils.execute(sql, interfaceConn);
	}
	
	
	/**
	 * 提交事务方法
	 */
	public final void commit(){
		try {
			if(interfaceConn!=null)
			{
				interfaceConn.commit();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void rollback() {
	}

	@Override
	public void close() {
		//关闭文件流
		for(OutputStreamWriter out : outs){
			if(out!=null){
				try {
					//关流前 执行 文件的fileEnd  为了生成文件尾部
					fileAfter(out);					
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		if(interfaceConn!=null){
			DbUtils.freeConnection(interfaceConn);
		}
		
		if(businessConn!=null){
			DbUtils.freeConnection(businessConn);
		}
		
	}
	
	
	/**
	 * 查询接口时间表,运行结束时间
	 * @param configId
	 * @return Date
	 */
	@SuppressWarnings("rawtypes")
	public String RunTime(String configId){
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String dateStr=null;
    	try {
    	String sql="select end_time from in_time where in_id='"+configId+"'";
    	Map map=selectOne(sql);
    	Date end_time=(Date) map.get("end_time");
    	dateStr=format.format(end_time);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	return dateStr;
    }
	
	
	/**
	 * 为了生成文件尾部
	 * @param out
	 */
	public void fileAfter(OutputStreamWriter out) {

	}

	@Override
	public boolean before() {
		return true;
	}
	
	@Override
	public boolean beforeRecord(int rowIndex, Object rowObject) {
		return true;
	}

	@Override
	public DataParserConfig getDataParserConfig() {
		return this.dataParserConfig;
	}
	
	/**
	 * 生成UUID
	 * @return String
	 */
	public static String getUUID(){
		return UUIDHexGenerator.getInstance().generate();
	}
	
	/**
	 * 创建错误信息 
	 * @param record  解析的数据
	 * @param message 错误信息
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, String> createErrorMessage(Map record,String message) {
		Map<String, String> result = new HashMap<String, String>();		
		//设置行信息
		result.put("ERRORMESSAGE", record.toString());
		//设置错误信息
		result.put("ERROREXCEPTION", message);
		return result;
	}
}

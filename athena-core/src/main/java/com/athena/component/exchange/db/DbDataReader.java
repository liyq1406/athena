/**
 * 
 */
package com.athena.component.exchange.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.DataReader;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.InziDbUtils;
import com.athena.component.exchange.RowParser;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.athena.component.runner.RunnerService;
import com.toft.core2.dao.database.DbUtils;
import com.toft.mvc.utils.AssertUtils;

/**
 * <p>Title:数据交换平台</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class DbDataReader implements DataReader {
	

//	private static final String dbSchemal1 = "dbSchemal1"; //对应SpaceFinal类的spacename_xqjs  需求计算空间名
//	private static final String dbSchemal2 = "dbSchemal2"; //对应SpaceFinal类的spacename_pcfj  排产发交空间名
//	private static final String dbSchemal3 = "dbSchemal3"; //对应SpaceFinal类的spacename_ckx  参考系空间名
	protected static Logger logger = Logger.getLogger(DbDataReader.class);	//定义日志方法
	protected DataParserConfig dataParserConfig;	
	private Connection conn;
	private RowParser<Map<String,Object>> rowParser;
	
	//错误文件名称
	private Map<String, String> errorFileName = new HashMap<String, String>();
	//成功文件名称
	private Map<String,String> succeedFileName = new HashMap<String, String>();
	//解析的文件路径   在解析文件的时候赋值
	private String filePath;
	//错误文件移到的文件夹路劲
	private String errorPath;
	//成功文件夹移动的路径
	private String backUpPath;
	//分发标志
	private String isGoon;
	
	//文件运行时间
	private String file_begintime;
	//文件结束时间
	private String file_endtime;
	
	//为了解决exchange的静态变量cid的引用
	private DataExchange de; 
	

	public DbDataReader(DataParserConfig dataParserConfig){
		this.dataParserConfig = dataParserConfig;
	}
	
	
	@Override
	public void open(DataWriter dataWriter,
			RunnerService runnerService) {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		
		String datasourceId = readerConfig.getDatasourceId();
		//获取数据库连接
		if(conn==null){
			conn = DbUtils.getConnection(datasourceId);
		}		
		rowParser = new DbRowParser(dataParserConfig,
				dataWriter,runnerService);
	}

	@Override
	public int readLine() {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		DataField[] dataFields = dataParserConfig.getDataFields();
		int i = 0;
	    String sql = SpaceFinal.replaceSql(readerConfig.getSql());
		//replaceSql(readerConfig.getSql());
		
		AssertUtils.notNull(sql, "数据库查询语句不能为空！");
		try {
			if("db".equals(dataParserConfig.getGroupConfig().getWriter())){
				//writer为db的
				i = doDbWriterLine(sql,dataFields);
			}else{
				//db为txt,常规处理
				
				/**
				 * 为了解决生成有增量的文件
				 * 	根据 reader中isAllSet属性
				 * 		如果此字段为空或false,则认为直接执行sql
				 * 	           如果此字段为true,则将sql拼接上 增量条件   editTime 在 '接口完成时间'和 '当前时间'之间.
				 */
				//当前时间
				String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				if("false".equals(dataParserConfig.getReaderConfig().getIsAllSet())){
					sql =  makeIncrementSql(sql,nowTime);
				}
				i = doWriterLine(sql,dataFields);
				
				if("false".equals(dataParserConfig.getReaderConfig().getIsAllSet())){
					de.setNowTime(nowTime); //将当前时间记录 在DispatchServiceImpl类中 结束时 维护接口的完成时间
					////更新接口主表的 完成时间，上上次完成时间
					//InziDbUtils.updateIn_ZIDB(nowTime, dataParserConfig.getId(),  readerConfig.getDatasourceId());
				}
				
				//向输出接口明细表插入数据
				ArrayList<Map<String, String>> errorMeList = ((TxtDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorMessageList();
				//日志记录 将错误信息保存到 接口文件错误记录信息 表中
				insertiIn_out_mx(errorMeList);
				//修改接口总表的总记录 错误记录
				//FileLog.getInstance(dataParserConfig.getReaderConfig().getDatasourceId()).updateIn_out_zb(de.getCID(),i,errorMeList.size());
				InziDbUtils.getInstance().updateIN_zidbCHUCTS(dataParserConfig.getReaderConfig().getDatasourceId(),de.getCID(),i,errorMeList.size());
			}
			
		} catch (SQLException e) {
			logger.error("接口"+de.getCID()+"出现"+e.getMessage());
		}finally{
			//txt的不用关闭，因为会再txtdatawriter关闭
			if("db".equals(dataParserConfig.getGroupConfig().getWriter())){
				close();
			}			
		}
		//返回总记录
		return i;
	}
	
	/**
	 * 遍历errorMeList集合 将解析错误数据，保存到输出表'输出接口表的明细'中
	 * @param errorMeList
	 */
	@SuppressWarnings("rawtypes")
	private void insertiIn_out_mx(ArrayList<Map<String, String>> errorMeList) {
		if(errorMeList.size()>0){
			for(Map map : errorMeList){
				String SID = TxtDataWriter.getUUID();
				String in_cuownr = (String) map.get("ERRORMESSAGE");
				String in_cuowxx = (String) map.get("ERROREXCEPTION");
				FileLog.getInstance(dataParserConfig.getReaderConfig().getDatasourceId()).insertIn_out_nx(SID,de.getCID(),in_cuownr,in_cuowxx);
			}			
		}
		
	}


	/**
	 * 生成带有增量条件的sql
	 * 	 and to_char(edit_time, 'yyyymmddHH24MIss') between
       (SELECT to_char(i.lastcpltime, 'yyyymmddHH24MIss')
          FROM IN_ZIDB i
         WHERE i.inbh = 'id1') and to_char(sysdate, 'yyyymmddHH24MIss')
	 * @param sql
	 * @return
	 */
	protected String makeIncrementSql(String sql,String nowTime) {
		//1：查找接口总表数据库,拿出此接口的 完成时间
		//2: 以此完成时间，和当前时间做条件 生成串
		//3: 更新此接口总表 此接口的完成时间，上上次完成时间	
		StringBuffer sb = new StringBuffer();
		sb.append(sql);		
		sb.append(" AND TO_CHAR(EDIT_TIME, 'yyyymmddHH24MIss') BETWEEN ");
		sb.append(" (SELECT TO_CHAR(i.LASTCPLTIME, 'yyyymmddHH24MIss') ");
		sb.append(" FROM IN_ZIDB i ");
		sb.append(" WHERE i.INBH = '"+dataParserConfig.getId()+"') and '"+nowTime+"' ");

		return sb.toString();
	}



	/**
	 * 查出有多少个文件，按照文件分发，有某个文件错误，则终止;将此文件移动到错误文件夹中.
	 * @param cs
	 * @param sql
	 * @param dataFields
	 * @param i
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int doDbWriterLine(String sql,DataField[] dataFields) throws SQLException{
		int i = 0;
		//拿出要分发的记录来源那些文件
		String readTable = dataParserConfig.getReaderConfig().getTable(); //read配置文件中配置的table
		if(readTable==null){
			//如果为空，则按照常规处理
			//i = doWriterLine(sql, dataFields);
			//2012-09-12  为了db的日志
			i  = doDbToDbWriterLine(sql, dataFields);
		}else{
			//从readTable表中拿出要处理的文件路径，循环文件处理,过滤出错误文件的记录
			String fileSql = "SELECT distinct wenjmc FROM " + SpaceFinal.replaceSql(readTable);
			if(errorFileName.size()>0){
				StringBuffer sb = new StringBuffer();
				sb.append(fileSql+" where wenjmc not in( ");
				Iterator ite = errorFileName.entrySet().iterator();
				while(ite.hasNext()){
					Map.Entry<String, String> entry = (Entry<String, String>) ite.next();
					sb.append("'"+entry.getKey()+"',");
				}
				fileSql = sb.toString().substring(0, sb.toString().length()-1)+" )";
			}
			
			Statement cs = conn.createStatement();
			ResultSet rs = cs.executeQuery(fileSql); //文件
			while(rs!=null&&rs.next()){
				//清除dbdatawriter的 文件 更新 插入数量
				changetozero();
				
				String wenjmc = rs.getString("wenjmc");
				try{
					file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					String fileName_sql = sql + " AND wenjmc = '"+wenjmc+"'";
					
					//设置解析的文件名
					((DbRowParser)(rowParser)).setFileName(wenjmc);
					
					i = doWriterLine(fileName_sql,dataFields);
					file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
					//设置成功的文件，为了最后将成功文件移动到备份文件夹中
					Map<String,String> errorfileMap = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorFileName();
					if(errorfileMap.get(wenjmc)==null){
						//错误文件中没有，则说名为成功文件
						succeedFileName.put(wenjmc,wenjmc);
						file_info(wenjmc,0,"1");
					}else{
						//做错误文件做日志记录
						String SID = file_info(wenjmc,-1,"-1");						
						//将errorfileMap中的对应主建值改成日志表id
						errorfileMap.remove(wenjmc);
						errorfileMap.put(wenjmc, SID);						
						//更新总接口表  的错误文件数量
						InziDbUtils.getInstance().update_cuowjsl(dataParserConfig.getWriterConfig().getDatasourceId(),de.getCID());
						//FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).UpdateInterface_info(de.getCID());
					}
					
				}catch(RuntimeException e){
					logger.error("DbDataReader 出错 :" + e.getMessage());
				}
			}
			
			cs.close();
			rs.close();	
			
			//2012-8-13 文件移动 由aotu做 我们不用对文件处理
//			//1：根据dbwriter记录的错误文件，将文件移到错误文件夹中，并将源文件夹中此文件删除
//			Map<String,String> errorNameMap = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorFileName();
//			FileUtis.moveFile(errorNameMap, errorPath,filePath); //将文件从filePath移到到errorPath路径中
//			FileUtis.deleteFile(errorNameMap,filePath); //删除此文件夹中的errorNameMap的文件
			
			//2：为了记录2,3表的日志,也要把成功文件记录  succeedFileName
			Map<String,String> errorNameMap = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorFileName();
			ArrayList<Map<String, String>> errorMeList = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorMessageList();
			//日志记录 将错误信息保存到 接口文件错误记录信息 表中
			in_errorfile_info(errorNameMap, errorMeList);
		}
		return i;
	}
	
	//2012-09-12 为了DB-DB日志
	private int doDbToDbWriterLine(String sql, DataField[] dataFields) throws SQLException {
		//清除dbdatawriter的 文件 更新 插入数量
		changetozero();
		doWriterLine(sql, dataFields);
		file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());	
		
		//2：为了记录2,3表的日志,也要把成功文件记录  succeedFileName
		ArrayList<Map<String, String>> errorMeList = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter()).getErrorMessageList();
		//日志记录 将错误信息保存到 接口文件错误记录信息 表中
		in_errorfile_info(null, errorMeList);
		return 0;
	}


	/**
	 * 将 dbdatawriter的文件更新数量，插入数量更改为0
	 */
	private void changetozero(){
		DbDataWriter ddw = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter());
		ddw.setFILE_ERROR_COUNT(0);
		ddw.setFILE_INSERT_COUNT(0);
		ddw.setFILE_UPDATE_COUNT(0);
	}
	
	/**
	 * 向 接口文件错误记录信息 表中 记录日志
	 * @param errorNameMap  错误文件名
	 * @param errorMeList 错误信息集合
	 */
	private void in_errorfile_info(Map<String, String> errorNameMap,
			ArrayList<Map<String, String>> errorMeList) {
		if(errorMeList.size()>0){
			for(Map<String,String> map : errorMeList){
				String EID = DbDataWriter.getUUID();
				String SID = null;
				if(errorNameMap==null){
					SID = "";
				}else{
					SID = errorNameMap.get(map.get("WENJMC"));
				}
				String file_errorinfo = map.get("ERRORMESSAGE");
				String error_date = map.get("ERROREXCEPTION");
				FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).File_ErrorInfo(EID,de.getCID(),SID,file_errorinfo,error_date);
			}
		}
	}

	/**
	 * 记录文件日志
	 * @param wenjmc
	 */
	private String file_info(String wenjmc,int error_num,String file_satus) {
		DbDataWriter ddw = ((DbDataWriter)((DbRowParser)rowParser).getDataWriter());
		String SID = DbDataWriter.getUUID();
		int insert_num = 0;
		int update_num = 0;
		if(!file_satus.equals("-1")){
			insert_num = ddw.getFILE_INSERT_COUNT();
			update_num = ddw.getFILE_UPDATE_COUNT();
		}
		if(error_num==-1){
			error_num = ddw.getFILE_ERROR_COUNT();
		}
		//文件做日志记录
		FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).File_info(SID,de.getCID(),wenjmc,file_begintime,file_endtime,insert_num,update_num,error_num,file_satus);
		return SID;
	}
	
	/**
	 * 常规处理
	 * 		执行用户配置的sql,取出每个值，入库
	 * @param cs
	 * @param sql
	 * @param dataFields
	 * @param i
	 * @throws SQLException
	 */
	public int doWriterLine(String sql,DataField[] dataFields) throws SQLException{
		Statement cs = conn.createStatement();
		ResultSet rs = cs.executeQuery(sql);
		int i = 0;
		while(rs!=null&&rs.next()){
			Map<String,Object> result = new HashMap<String,Object>();
			Object value;
			for(DataField dataField:dataFields){
				//value = rs.getObject(dataField.getReaderColumn());
				//为了得到完成的date类型时间 改造
				value = getValueFromRs(rs,dataField);
				
				if(value!=null&&dataField.getWriterColumn()!=null){
					result.put(dataField.getWriterColumn(), value);
					value = null;
				}
			} 		
			rowParser.parse(++i, result);
		}
		rowParser.complete();
		cs.close();	
		
		return i;
	}  
	
	/**
	 *  根据 列元素的 type类型 得到相应类型的值   
	 *  	目前值对date类型做了处理  其他默认都用getObject处理
	 * @param rs 结果集
	 * @param dataField 列元素
	 * @return
	 * @throws SQLException 
	 */
	private Object getValueFromRs(ResultSet rs, DataField dataField) throws SQLException {
		Object result = null;
		if ("date".equals(dataField.getType())) {
			result = rs.getTimestamp(dataField.getReaderColumn());
		} else {
			result = rs.getObject(dataField.getReaderColumn());
		}
		return result;
	}
	@Override
	public void close() {
//		try {
//			if(conn!=null){
//				conn.close();
//			}
//		} catch (SQLException e) {
//			throw new ParserException("数据库连接关闭异常！"+e.getMessage());
//		}	
		//测试
		DbUtils.freeConnection(conn);
	}
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	public Map<String, String> getErrorFileName() {
		return errorFileName;
	}
	public void setErrorFileName(Map<String, String> errorFileName) {
		this.errorFileName = errorFileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getErrorPath() {
		return errorPath;
	}
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}
	public String getBackUpPath() {
		return backUpPath;
	}
	public void setBackUpPath(String backUpPath) {
		this.backUpPath = backUpPath;
	}
	public String getIsGoon() {
		return isGoon;
	}
	public void setIsGoon(String isGoon) {
		this.isGoon = isGoon;
	}
	public Map<String, String> getSucceedFileName() {
		return succeedFileName;
	}
	public void setSucceedFileName(Map<String, String> succeedFileName) {
		this.succeedFileName = succeedFileName;
	}
	public DataExchange getDe() {
		return de;
	}
	public void setDe(DataExchange de) {
		this.de = de;
	}
}

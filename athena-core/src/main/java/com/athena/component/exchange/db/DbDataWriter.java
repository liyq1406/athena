/**
 * 
 */
package com.athena.component.exchange.db;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.field.DataField;
import com.toft.core2.dao.database.DbUtils;
import com.toft.utils.UUIDHexGenerator;

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
public class DbDataWriter implements DataWriter {


	protected static Logger logger = Logger.getLogger(DbDataWriter.class);	//定义日志方法
	//添加
	protected DataExchange dataExchange;
	
	public DataExchange getDataExchange() {
		return dataExchange;
	}

	public void setDataExchange(DataExchange dataExchange) {
		this.dataExchange = dataExchange;
	}

	protected DataParserConfig dataParserConfig;
	
	public Connection conn;
	
	private PreparedStatement insertPs;
	
	private PreparedStatement updatePs;

	private String[] fields;

	private String insertSql;

	private String updateSql;

	private String[] updateFields;

	private String[] idKeys;
	//总更新的条数
	public  int UPDATE_COUNT = 0;
	//总插入的条数
	public  int INSERT_COUNT = 0;
	//总共错误数
	public int ERROR_COUNT = 0;
	
	//某个文件的错误数
	private int FILE_ERROR_COUNT=0;
	//某个文件的更新数
	private int FILE_UPDATE_COUNT=0;
	//某个文件的插入数
	private int FILE_INSERT_COUNT=0;
	
	private TableDbUtils tdu;
   
    
    //错误信息
    private ArrayList<Map<String,String>> errorMessageList = new ArrayList<Map<String,String>>();
    //错误文件的名字
    private Map<String,String> errorFileName = new HashMap<String, String>();
    
    public ArrayList<Map<String, String>> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<Map<String, String>> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}

	//文件路径
    private String WENJLJ; 
	//文件名称
    private String WENJMC;
     
    //定义datasourceId
    private String datasourceId;

	public DbDataWriter(DataParserConfig dataParserConfig) {
		this.dataParserConfig = dataParserConfig;
		this.initWriter();
		tdu = new TableDbUtils();
		}
	
	 /**
	  * 拼接SQL字段对应xml配置文件
	  */
	private void initWriter(){
		DataField[] dataFields = dataParserConfig.getDataFields();
		idKeys = dataParserConfig.getWriterConfig().getIdKeys().split(",");
		List<String> fieldList = new ArrayList<String>();
		List<String> updateFieldList = new ArrayList<String>();
		
		StringBuffer insertSqlBuf = new StringBuffer();
		StringBuffer updateSqlBuf = new StringBuffer();
		
		StringBuffer insertFieldBuf = new StringBuffer();
		StringBuffer insertValueBuf = new StringBuffer();
		StringBuffer updateSqlSetBuf = new StringBuffer();
		
		int i=0;
		for(DataField dataField:dataFields){
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

		
		this.fields = fieldList.toArray(new String[fieldList.size()]);
		this.updateFields = updateFieldList.toArray(new String[updateFieldList.size()]);
		
		//String tableName = SpaceFinal.spacename_ckx+"."+dataParserConfig.getWriterConfig().getTable();
		//String tableName = dataParserConfig.getWriterConfig().getTable();
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
		
		this.insertSql = insertSqlBuf.toString();
		this.updateSql = updateSqlBuf.toString();
		
		
		logger.info(insertSql);
		logger.info(updateSql);
		
		this.initConnection();
	}
	
	private void initConnection(){
		 datasourceId = dataParserConfig.getWriterConfig().getDatasourceId();
		//获取数据库连接
		conn = DbUtils.getConnection(datasourceId);

		try { 
			conn.setAutoCommit(false);
			logger.debug("开始执行SQL，数据库ID：" + conn.toString() + "    " +insertSql);
			insertPs = conn.prepareStatement(insertSql);
			
			//2012-08-30 仓库一接口不做更新
			if("false".equals(dataParserConfig.getWriterConfig().getIsUpdate())){
				updatePs= null;
			}else{
				updatePs = conn.prepareStatement(updateSql);	
			}
					
			logger.info("插入记录语句:"+insertSql);
			logger.info("更新记录语句:"+updateSql);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 文件解析前的处理
	 * return boolean
	 */
	@Override
	public boolean before() {
		return true;
	}

	/**
	 * 文件解析前record的处理
	 * return boolean
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		return true;
	}

	/**
	 * 文件解析后record的处理
	 */
	@Override
	public void afterRecord(int rowIndex, Record record,Object line){
		//是db--db的形式
		if("db".equals(dataParserConfig.getGroupConfig().getReader())){
			String wenjlj = (String) record.get("wenjlj"); //拿出文件路径
			String wenjmc = (String) record.get("wenjmc"); //拿出文件名称
			
			//设置文件名称  文件路径 为了出错使用
			setWENJLJ(wenjlj);
			setWENJMC(wenjmc);

			//从结果中移除
			if(wenjlj!=null && wenjmc!=null){
//				record.remove("wenjlj");
//				record.remove("wenjmc");
			}
		}
		
		//2012-08-25 剔除空格
		String isTirm = dataParserConfig.getWriterConfig().getIsTrim();
		if("true".equals(isTirm)){
			record = TrimRecord(record);
		}
		
		TableRecord tableReocrd = new TableRecord(record.getValue());
		tableReocrd.setTableName(dataParserConfig.getWriterConfig().getTable());		
		tableReocrd.setFields(fields);
		tableReocrd.setUpdateFields(updateFields);
		tableReocrd.setIdKeys(idKeys);
		
		String message = "";
		
		int updateCount = 0;//初始化更新数据条数
		try {
			//打印sql
			debugSQL(insertSql,tdu.generateInsertValues(tableReocrd).toArray());
			debugSQL(updateSql,tdu.generateUpdateValues(tableReocrd).toArray());
			
			updateCount = tdu.saveTable(tableReocrd,insertPs,updatePs);
			UPDATE_COUNT+=updateCount;//更新记录条数
			INSERT_COUNT+=(1-updateCount);//插入记录条数
			
			FILE_INSERT_COUNT+=(1-updateCount);
			FILE_UPDATE_COUNT+=updateCount;
			
			logger.info("-------------------------");
			logger.info(updateCount);
			logger.info("INSERT_COUNT"+ INSERT_COUNT);
			logger.info("UPDATE_COUNT"+ UPDATE_COUNT);
			logger.info("-------------------------");
			
		} catch (SQLException e) {
			message=e.getMessage();
			//对主键冲突的  如果是做更新的，并且为主键冲突的则不做错误数据处理
			String[] str = message.split(":");
			if(!"false".equals(dataParserConfig.getWriterConfig().getIsUpdate()) || !"ORA-00001".equals(str[0])){
				logger.error("保存数据异常：" + e.getMessage());
				ERROR_COUNT = ERROR_COUNT + 1;//错误记录条数
				FILE_ERROR_COUNT = FILE_ERROR_COUNT+1;//记录出文件的个数
				//创建错误信息
				makeErrorMessage(rowIndex, record, message, line);	
			}
		}
	}
	
	/**
	 * 为了外部接口的剔除空格
	 * @param record
	 * @return
	 */
	private Record TrimRecord(Record record) {
		Record resultRecord = new Record();
		
		for(String key : record.getValue().keySet()){
			if(record.get(key) instanceof String){
				resultRecord.put(key, ((String)(record.get(key))).trim());
			}else{
				resultRecord.put(key, record.get(key));
			}			
		}
		
		return resultRecord;
	}

	/**
	 * 创建错误信息
	 * @param rowIndex 行号
	 * @param record 错误记录 在afterRecord方法中对应：Record record
	 * @param message 异常信息 ,Object line
	 * @param errorline 错误行信息
	 */
	protected void makeErrorMessage(int rowIndex, Record record, String message,
			Object line) {
		
		String errorline=null;
		if(line instanceof Map){
			errorline = ((Map) line).toString();
		}else{
			errorline = line.toString();
		}
		
		Map<String,String> errorMap = createErrorMessage(record.getValue(),rowIndex,message,errorline); //创建错误信息
		errorMessageList.add(errorMap);
		if("".equals(record.getString("WENJMC"))){
			setErrorFileNameMap(getWENJMC()); //将错误文件名保存下来
		}else{
			setErrorFileNameMap(record.getString("WENJMC")); //设置错误文件的名称，为了中间表的不拿出错误文件的数据	
		}
	}
	
	public int getFILE_ERROR_COUNT() {
		return FILE_ERROR_COUNT;
	}

	public void setFILE_ERROR_COUNT(int fILE_ERROR_COUNT) {
		FILE_ERROR_COUNT = fILE_ERROR_COUNT;
	}

	public int getFILE_UPDATE_COUNT() {
		return FILE_UPDATE_COUNT;
	}

	public void setFILE_UPDATE_COUNT(int fILE_UPDATE_COUNT) {
		FILE_UPDATE_COUNT = fILE_UPDATE_COUNT;
	}

	public int getFILE_INSERT_COUNT() {
		return FILE_INSERT_COUNT;
	}

	public void setFILE_INSERT_COUNT(int fILE_INSERT_COUNT) {
		FILE_INSERT_COUNT = fILE_INSERT_COUNT;
	}

	public Map<String, String> getErrorFileName() {
		return errorFileName;
	}

	public void setErrorFileName(Map<String, String> errorFileName) {
		this.errorFileName = errorFileName;
	}

	/**
	 * 设置错误文件的名称
	 * 		如果  错误文件名称不存在，再将错误文件名称保存下来
	 * @param string
	 */
	synchronized private void setErrorFileNameMap(String fileName) {
		if(fileName!=null){
			if(errorFileName.get(fileName)==null){
				errorFileName.put(fileName, fileName);
			}
		}
	}

	/**
	 * 创建错误信息
	 * 		设置：
	 * 			文件名
	 * 			行信息
	 * 			行号
	 * 			错误信息
	 * @return
	 */
	private Map<String, String> createErrorMessage(Map record,int rowIndex,String message,String errorline) {
		Map<String, String> result = new HashMap<String, String>();		
		//设置文件名
		result.put("WENJMC", (String)record.get("WENJMC"));
		//设置行信息
		result.put("ERRORMESSAGE", errorline);
		//设置行号
		result.put("ROWNUMBER", String.valueOf(rowIndex));
		//设置错误信息
		result.put("ERROREXCEPTION", message);
		return result;
	}

	/**
	 * 文件解析完后的处理
	 */
	@Override
	public void after() {
		logger.info("插入记录条数:"+INSERT_COUNT);
		logger.info("更新记录条数:"+UPDATE_COUNT);
		logger.info("错误记录条数:"+ERROR_COUNT);
	}
	
	
	/**
	 * 提交事务方法
	 */
	public final void commit(){
		try {
			if(conn!=null)
			{
			conn.commit();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void rollback() {
		
		if(conn!=null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 查询方法
	 * @param sql
	 * @return List
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected List select(String sql){
		return DbUtils.select(sql, datasourceId);
	}
	
	/**
	 * 单记录查询
	 * @param sql
	 * @return Map
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected Map selectOne(String sql){
		return DbUtils.selectOne(sql, datasourceId);
	}
	
	/**
	 * 单字段查询
	 * @param sql
	 * @return Object
	 * @throws SQLException
	 */
	protected Object selectValue(String sql){
		return DbUtils.selectValue(sql, datasourceId);
	}
	
	/**
	 * sql语句执行
	 * @param sql
	 * @return int
	 * @throws SQLException
	 */
	protected int execute(String sql){
		return DbUtils.execute(sql, datasourceId);
	}   
	
	
	
	/**
	 * 调用存储过程
	 */
	public void prepare(String prepareStr){
		try {
			CallableStatement call=conn.prepareCall(prepareStr);
			call.execute();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * 关闭输出
	 */
	public final void close(){
		try {
			if(insertPs!=null)insertPs.close();
			if(updatePs!=null)updatePs.close();		
			
			if(conn!=null){
				DbUtils.freeConnection(conn);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
	  }
	}
	
	/**
	 * 生成UUID
	 * @return String
	 */
	public static String getUUID(){
		return UUIDHexGenerator.getInstance().generate();
	}

	@Override
	public DataParserConfig getDataParserConfig() {
		return this.dataParserConfig;
	}
	
	 public String getWENJLJ() {
			return WENJLJ;
		}
	

		public void setWENJLJ(String wENJLJ) {
			WENJLJ = wENJLJ;
		}

		public String getWENJMC() {
			return WENJMC;
		}

		public void setWENJMC(String wENJMC) {
			WENJMC = wENJMC;
		}
		
		public  void debugSQL(String sql, Object[] params) {
			try {
				class Count { // 统计问号的个数
					public int countQ(String sql) {
						int count = 0;
						for (int i = 0; i < sql.length(); i++) {
							if (sql.charAt(i) == '?') {
								count++;
							}
						}
						return count;
					}
				}

				params = (params == null ? new Object[0] : params); // 排除为空的情况
				if ("".equals(sql.trim())) {// 检查输入的合法性：sql不为空
					logger.info("DebugSQL : 不合法的参数：sql不能为空");
				} else {

					int count = new Count().countQ(sql);

					if (count > params.length) {// 检查输入的合法性：？的个数要和参数的个数相等
						logger.info("DebugSQL : '?' 的个数(" + (count)
								+ ")大于参数的个数(" + params.length + ")");
					} else if (count < params.length) {
						logger.info("DebugSQL : '?' 的个数(" + (count)
								+ ")小于于参数的个数(" + params.length + ")");
					}
					// 依次用参数替换问号
					for (int i = 0; i < params.length && sql.indexOf("?") > -1; i++) {
						sql = sql.replaceFirst("\\?", params[i]==null?"''":"'" + params[i].toString()
								+ "'");
					}
					// 在控制台输出
					logger.info("DebugSQL : you can use PL/SQL to debug the sql : \n" + sql);

				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

}

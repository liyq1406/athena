package com.athena.component.exchange;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.athena.component.exchange.db.TableRecord;
import com.toft.core2.dao.database.DbUtils;

/**
 * 输入接口日志类
 * 
 * @author GJ
 * 
 */

public class DbLog {

	protected static Logger logger = Logger.getLogger(DbLog.class);	//定义日志方法
	
	private Connection conn;
	
	private String datasourceId;

	public Connection getConn() {
		return conn;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	private DbLog() {
		super();
	}

	public static DbLog getInstance(String datasourceId){
		DbLog db = new DbLog();	
		db.setDatasourceId(datasourceId);
		return db;
	}
	
	/**
	 * 插入日志主表
	 */
	public  void InsertInfo(String uuid,String RunEndTime,int INSERT_COUNT, int ERROR_COUNT,int UPDATE_COUNT,int OUTPUT_COUNT) {
		try {
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("insert into "+SpaceFinal.spacename_zbc+".in_log(id,in_name,runstart_time,runend_time,insert_count,update_count,error_count,output_count)");
			strbuf.append("values(");
			strbuf.append("'" + uuid+ "',");
			strbuf.append("'" + StrNull(DataExchange.configId) + "',");
			strbuf.append("to_date('" + StrNull(DataExchange.RunStartTime) + "','yyyy-MM-dd Hh24:Mi:ss'),");
			strbuf.append("to_date('" + StrNull(RunEndTime) + "','yyyy-MM-dd Hh24:Mi:ss'),");
			strbuf.append("'" + StrNull(DataExchange.RunStartTime) + "',");
			strbuf.append("'" + StrNull(RunEndTime) + "',");
			strbuf.append("" + INSERT_COUNT + ",");
			strbuf.append("" + UPDATE_COUNT + ",");
			strbuf.append("" + ERROR_COUNT + ",");
			strbuf.append("" + OUTPUT_COUNT +")");
			conn = DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);		
		} catch (Exception e) {
			logger.error(e.getMessage());
			//释放连接
			DbUtils.freeConnection(conn);
		}finally{
			//释放连接
			DbUtils.freeConnection(conn);
		}
	}
	
	/**
	 * 插入日志从表
	 * @param getMessage
	 */

	public void InsertMX(String getMessage,int ERROR_COUNT,TableRecord tableReocrd)
	{
		try{
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("insert into "+SpaceFinal.spacename_zbc+".in_log_mx(id,cwjl,error_nr,run_cs)");
		strbuf.append("values(");
		strbuf.append("'" + StrNull(DataExchange.configId) + "',");
		strbuf.append(""+ERROR_COUNT+",");
		strbuf.append("'" + StrNull(getMessage) + "',");
		strbuf.append("'" + StrNull(tableReocrd) + "')");
		
		conn = DbUtils.getConnection(datasourceId);
		execute(strbuf.toString(),conn);
		commit(conn);
		}catch(Exception e)
		{	
			e.printStackTrace();
			logger.error(e.getMessage());
			DbUtils.freeConnection(conn);
		}finally{
			DbUtils.freeConnection(conn);
		}
	}
	
		
	/**
	 * 更新日志从表ID
	 */
    public void updateId(String uuid)
    {
    	try{
    	StringBuffer strbuf=new StringBuffer();
    	strbuf.append("update "+SpaceFinal.spacename_zbc+".in_log_mx set id='"+uuid+"' where id='"+StrNull(DataExchange.configId)+"'");
    	
    	execute(strbuf.toString(),conn);
    	commit(conn);
    	}catch(Exception e){
    		logger.error(e.getMessage());
    		DbUtils.freeConnection(conn);
    	}finally{
    		//释放连接
			DbUtils.freeConnection(conn);
    	}
    }
	
    
	/**
	 * 空值处理
	 * 
	 * @param obj
	 * @return Sting
	 */
	private String StrNull(Object obj)// //对象为空返回空串,不为空范围字符串
	{
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * sql语句执行
	 * @param sql
	 * @return
	 * @throws SQLException
	 */

	protected int execute(String sql,Connection conn){
		return DbUtils.execute(sql,conn);
	}
	
	/**
	 * 提交事务方法
	 */
	public void commit(Connection conn){
		try {
			if(conn!=null)
			{
			conn.commit();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
	
}

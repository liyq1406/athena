package com.athena.component.exchange;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.util.exception.ServiceException;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 输入输出的日志记录类
 */
public class FileLog {
	
protected static Logger logger = Logger.getLogger(FileLog.class);	//定义日志方法
	
private Connection conn;

private String datasourceId;



	public String getDatasourceId() {
		return datasourceId;
	}
	
	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}
	
	
	public FileLog(){ 
	}

	/**
	 * datasourceId存入实例
	 * @param datasourceId
	 * @return
	 */
	public static FileLog getInstance(String datasourceId){
		FileLog filelog=new FileLog();
		filelog.setDatasourceId(datasourceId);
		return filelog;
	}



	/**CHENLEI
	 * 插入接口文件错误记录信息表记录
	 * @param EID
	 * @param CID
	 * @param SID
	 * @param FileDataError
	 * @param DataErrorPar
	 */
	public void File_ErrorInfo(String EID,String CID,String SID,String file_errorinfo,String error_date){
		StringBuffer strbuf = new StringBuffer();
		try {
			
			strbuf.append("insert into in_errorfile(EID,INBH,SID,FILE_ERRORINFO,ERROR_DATE,YUNXKSSJ)  ");
			strbuf.append(" SELECT  ");
			strbuf.append("'" + ObjStr(EID) + "',");
			strbuf.append("'" + ObjStr(CID) + "',");
			strbuf.append("'" + ObjStr(SID) + "',");
			strbuf.append("substr('" + ObjStr(file_errorinfo) + "',0,700),");
			strbuf.append("'" + ObjStr(error_date) + "',");
			strbuf.append(" YUNXKSSJ ");
			strbuf.append("  FROM IN_ZIDB WHERE INBH='" + CID + "' ");

			conn = DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(), conn);
			commit(conn);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		} finally {
			DbUtils.freeConnection(conn);
		}
	}


	/**
	 * chenlei
	 * 插入主日志的记录
	 * @param CID
	 * @param in_name 接口名
	 * @param RunStartTime 开始时间
	 * @param in_file_num 文件数量
	 * @param in_run_biaos 接口标识
	 */
	public void Interface_info(String CID,String in_name,String RunStartTime,int in_file_num,String in_run_biaos){
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("insert into in_info_log");
		strbuf.append("(CID, IN_NAME, IN_BEGINTIME, in_file_num, IN_RUN_BIAOS,IN_ERRORFILE_NUM)");
		strbuf.append("values( ");
		strbuf.append(" '" + CID+"',");
		strbuf.append(" '" + in_name+"',");
		strbuf.append("to_date('" + ObjStr(RunStartTime) + "','yyyy-MM-dd Hh24:Mi:ss'),");
		strbuf.append(in_file_num+",");
		strbuf.append("'"+ObjStr(in_run_biaos)+"',");
		strbuf.append(0+")");
	
		conn=DbUtils.getConnection(datasourceId);
	    execute(strbuf.toString(),conn);
	    commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
	}

	/**
	 * CHENLEI
	 * @param SID 主表ID
	 * @param CID 文件日志主键
	 * @param file_begintime 文件运行开始时间
	 * @param file_endtime 文件结束时间
	 * @param insert_num 插入条数
	 * @param update_num 更新条数
	 * @param error_num 错误条数
	 * @param file_satus 运行状态     0  未执行    1 成功(已执行)  -1 失败(已执行)
	 */
	public void File_info(String SID,String CID,String fileName,String file_begintime,String file_endtime,int insert_num,int update_num,int error_num,String file_satus){
		StringBuffer strbuf=new StringBuffer();
		try{				
			strbuf.append("insert into in_file_log  ");
			strbuf.append("(SID,  ");
			strbuf.append("INBH,  ");
			strbuf.append("FILE_NAME,  ");
			strbuf.append("file_begintime, "); 
			strbuf.append("file_endtime,  ");
			strbuf.append(" insert_num,  ");
			strbuf.append(" update_num,  ");
			strbuf.append(" error_num,  ");
			strbuf.append(" file_satus, ");
			strbuf.append(" YUNXKSSJ)  ");  
			strbuf.append(" values(  ");		    
			strbuf.append(" '"+SID+"',");
			strbuf.append("'"+CID+"',");
			strbuf.append("'"+fileName+"',");
			strbuf.append("to_date('" + ObjStr(file_begintime) + "','yyyy-MM-dd Hh24:Mi:ss'),");
			strbuf.append("to_date('" + ObjStr(file_endtime) + "','yyyy-MM-dd Hh24:Mi:ss'),");
		    strbuf.append(insert_num+",");
		    strbuf.append(update_num+",");
		    strbuf.append(error_num+",");
		    strbuf.append(" '"+file_satus +"',");
		    strbuf.append(" sysdate )");
			
			conn=DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
	}


	/**
	 * 记录主日志信息
	 * @author Hezg
	 * @date 2013-2-17
	 * @param params
	 * @return void
	 */
	public void insert_file_info(Map<String,String> params,AbstractIBatisDao baseDao) throws ServiceException{
		try{	
			baseDao.getSdcDataSource(datasourceId).execute("inLog.insertFileInfo", params);	
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 插入接口文件错误记录信息表记录
	 * @author Hezg
	 * @date 2013-2-17
	 * @param params 插入参数
	 * @param baseDao IbatisDao
	 * @throws ServiceException ServiceException异常信息
	 */
	public void insert_file_ErrorInfo(Map<String,String> params,AbstractIBatisDao baseDao) throws ServiceException{
		try {
			baseDao.getSdcDataSource(datasourceId).execute("inLog.insertErrorFileInfo", params);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		} 
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


	/**
	 * 对象为null时为空
	 * @param obj
	 * @return
	 */
	public String ObjStr(Object obj){
		return obj==null?"":obj.toString();
		
	}


	/**
	 *  将in_info_log 将接口状态更新   有一个错误文件 则更新此接口状态为失败
	 *  更新结束时间
	 * @param cID in_info_log 主键
	 */
	public void Update_zt_Interface_info(String cID,String enddate) {
		StringBuffer strbuf=new StringBuffer();
		try{
			strbuf.append("update IN_INFO_LOG i ");
			strbuf.append("set i.in_run_status = (select (case ");
			strbuf.append(" when count(1) > 0 then ");
			strbuf.append(" 0 ");
			strbuf.append(" else ");
			strbuf.append(" 1 ");
			strbuf.append(" end) ");
			strbuf.append(" from in_file_log i ");
			strbuf.append(" where i.cid ='"+cID+"'), ");
			strbuf.append(" i.in_endtime    = to_date('"+enddate+"', ");
			strbuf.append("'yyyy-MM-dd Hh24:Mi:ss') ");
			strbuf.append(" where i.cid = '"+cID+"' ");
			
			conn=DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
	}

	/**
	 * 输出接口
	 *  向'in_out_zb'表插入一条数据
	 *  	运行状态：运行
	 *      开始时间：为当前时间
	 *      插入数量： 0
	 *      错误数量： 0
	 * @param cId
	 */
	public void insertIn_out_zb(String cId,String configId){
		StringBuffer strbuf=new StringBuffer();
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try{
			
			strbuf.append("INSERT INTO IN_OUT_ZB( ");
			strbuf.append(" CID, ");
			strbuf.append(" IN_NAME, ");
			strbuf.append(" IN_BEGINTIME, ");
			strbuf.append(" IN_STATUM, ");
			strbuf.append(" IN_TIAOS, ");
			strbuf.append(" IN_CUOWTS ");
			strbuf.append(") VALUES( ");
			strbuf.append(" '"+cId+"',");
			strbuf.append(" '"+configId+"',");
			strbuf.append("to_date('" + nowTime + "','yyyy-MM-dd Hh24:Mi:ss'),");
			strbuf.append("1,0,0)");
		
			conn=DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
	}
	
	/**
	 * 修改此输出记录的 运行状态为结束  完成时间为当时间
	 * @param cId
	 */
	public void updateIn_out_zb(String cId){
		StringBuffer strbuf=new StringBuffer();
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try{
			
			strbuf.append("UPDATE IN_OUT_ZB ");
			strbuf.append("SET IN_ENDTIME = TO_DATE('"+nowTime+"', 'yyyy-MM-dd Hh24:Mi:ss'),  ");
			strbuf.append(" IN_STATUM = 0 ");
			strbuf.append("WHERE CID = '"+cId+"' ");
			
			conn=DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
	}
	
	/**
	 * 向输出接口保存一条记录 in_errorfile
	 * @param sID 主键
	 * @param cid 对应主表id
	 * @param in_cuownr 错误数据
	 * @param in_cuowxx  错误内容
	 */
	public void insertIn_out_nx(String sID, String cid, String in_cuownr,
			String in_cuowxx) {
		StringBuffer strbuf=new StringBuffer();
		try{
			
			strbuf.append("INSERT INTO in_errorfile ( ");
			strbuf.append(" EID, ");
			strbuf.append("INBH, ");
			strbuf.append("file_errorinfo, ");
			strbuf.append("error_date   ");
			strbuf.append(") VALUES ( ");
			strbuf.append("'"+sID+"',");
			strbuf.append("'"+cid+"',");
			strbuf.append("substr('"+in_cuownr+"',0,700),");
			strbuf.append("'"+in_cuowxx+"')");
		
			conn=DbUtils.getConnection(datasourceId);
			execute(strbuf.toString(),conn);
			commit(conn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
		
	}	
}
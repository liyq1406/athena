package com.athena.component.exchange;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.toft.core2.dao.database.DbUtils;

/**
 * 对in_zidb表的操作
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-13
 */
public class InziDbUtils {
	protected static Logger logger = Logger.getLogger(InziDbUtils.class);
	
	private static final InziDbUtils idus = new InziDbUtils();
	
	public static InziDbUtils getInstance(){
		return idus;
	}
	
	/**
	 * 根据接口Id 拿出此条数据的 用户中心字段
	 * @param datasourceId 数据源id
	 * @param id 接口id
	 * @param readistxt  true: dataread为txt的类，则要得到的结果是isgoon;
	 *                   false：则返回结果为用户中心
	 */
	@SuppressWarnings("rawtypes")
	public  List<String> selectById(String datasourceId,String id,String readType){
		//logger.info("查询IN_ZIDB表数据源ID=>"+datasourceId+"   接口ID=>"+id); 
		List<String> result = null; 
		 ArrayList list = new ArrayList();
		 StringBuffer strbuf=new StringBuffer();
		 Connection conn = null;
		 try{
		  	  strbuf.append(" SELECT t.outfiletype outfiletype,t.infiletype infiletype,t.isgoon isgoon FROM IN_ZIDB t WHERE t.inbh ='");
		  	  strbuf.append(id+"'");
		  	  conn=DbUtils.getConnection(datasourceId);
		  	  list = DbUtils.select(strbuf.toString(),conn);
		  	  
		  	  if(list.size()==1){
		  		  if("toTxt".equals(readType)){
		  			  	result = getUserCenter((String)((Map)list.get(0)).get("outfiletype"));
		  		  }else if("toDb".equals(readType)){
			  			result = getUserCenter((String)((Map)list.get(0)).get("infiletype"));
			  	  }else if("toXml".equals(readType)){//DB到xml  xss 2015.12.29 
			  			result = getUserCenter((String)((Map)list.get(0)).get("outfiletype"));
			  	  }else{
		  			String str = (String)((Map)list.get(0)).get("isgoon");
		  			str = str==null?null:str;
		  			result = new ArrayList<String>();
		  			result.add(str);
		  		  }
		  	
		  	  }
	  	   }catch(Exception e){
	  		   logger.error(e.getMessage());
	  	   }finally{
	  		   DbUtils.freeConnection(conn);
	  	   }
	  	   return result;
	}

	/**
	 * 提交事务方法
	 */
	public  void commit(Connection conn){
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
	 * 格式：{{UW:文件名称},{UL:文件名称},{UW,UK:文件名称}}
	 * @param str 对查询出的outfiletype 结果进行截取
	 * @return
	 */
	public  List<String> getUserCenter(String str){
		
		if("".equals(str)){
			return null;
		}
		
		List<String> list = new ArrayList<String>();
		StringBuffer sb = null;
		for(int i=0;i<str.length();i++){
			if('{'==str.charAt(i)){
				sb= new StringBuffer();
			}else if('}'==str.charAt(i)){
				if(sb!=null){
					list.add(sb.toString());
				}
				sb=null;
			}else{
				if(sb!=null){
					sb.append(str.charAt(i));
				}
			}
		}		
		return list;
	}
	
	/**
	 * 更新接口总表的 完成时间  上上次完成时间
	 * 	将接口总表的 上上次完成时间 赋值给 上次完成时间； 
	 * 				上次完成时间 赋值给 当前时间
	 * @param nowTime
	 */
	public  synchronized void updateIn_ZIDB(String nowTime,String configId,String datasourceId) {
		Connection conn = null;
		try {
			StringBuffer sb = new StringBuffer();
			
			sb.append(" UPDATE IN_ZIDB ");
			sb.append(" set (LASTCPLTIME, LASTLCTIME) = (select to_date('");
			sb.append(nowTime+"', 'yyyymmddHH24MIss'), ");
			sb.append(" t.lastcpltime ");
			sb.append(" from IN_ZIDB t ");
			sb.append(" where t.inbh = '"+configId+"') ");
			sb.append(" where inbh = '"+configId+"' ");
			
			conn = DbUtils.getConnection(datasourceId);
			Statement cs = conn.createStatement();
			cs.executeUpdate(sb.toString());
			commit(conn);
			cs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
	  		   DbUtils.freeConnection(conn);
	  	}
		
	}
	
	/**
	 * zt为1：只改变状态字段 不维护完成时间
	 * 对接口总表 对应接口id的记录的  运行状态  完成时间  上上次完成时间进行维护
	 * @param zt 运行状态
	 * @param datasourceId  数据id
	 * @param bh 接口id
	 */
	public  synchronized void UpdateInZIDB_ZT(String zt,Connection conn,String bh){
		try {
			String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			StringBuffer sb = new StringBuffer();
			
			/**
			 * 接口开始运行  
			 * 	将接口的状态  
			 * 		设置为 1  运行
			 * 		开始时间  设置为 当前时间 
			 */
			if("1".equals(zt)){
				sb.append("UPDATE IN_ZIDB ");
				sb.append("set YUNXZT = '"+zt+"', ");
				sb.append("YUNXKSSJ=to_date('"+nowTime+"', ");
				sb.append(" 'yyyymmddHH24MIss') ");
				sb.append("where inbh = '"+bh+"' ");

			}else{
				/**
				 *  接口结束或者发生异常
				 * 	将接口状态更改为相应的值
				 * 	结束时间 维护为当前时间
				 */
				sb.append("UPDATE IN_ZIDB ");
				sb.append("set (YUNXZT, LASTCPLTIME, LASTLCTIME) = (select '"+zt+"', ");
				sb.append(" to_date('"+nowTime+"', ");
				sb.append(" 'yyyymmddHH24MIss'), ");
				sb.append(" t.lastcpltime ");
				sb.append(" from IN_ZIDB t ");
				sb.append(" where t.inbh = '"+bh+"') ");
				sb.append(" where inbh = '"+bh+"' ");
			}
			
			Statement cs = conn.createStatement();
			cs.executeUpdate(sb.toString());
			commit(conn);
			cs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
	  		   DbUtils.freeConnection(conn);
	  	}
	}

	/**
	 * zt为1：只改变状态字段 不维护完成时间
	 * 对接口总表 对应接口id的记录的  运行状态  完成时间  上上次完成时间进行维护
	 * @param zt 运行状态
	 * @param datasourceId  数据id
	 * @param bh 接口id
	 * hzg 2013-6-14 2050接口卡时间取数据时要货令可能会由于时间问题没取到，因此做此修改
	 */
	public  synchronized void UpdateInZIDB_ZT(String zt,Connection conn,String bh,String nowTime){
		try {
			StringBuffer sb = new StringBuffer();
			
			/**
			 * 接口开始运行  
			 * 	将接口的状态  
			 * 		设置为 1  运行
			 * 		开始时间  设置为 当前时间 
			 */
			if("1".equals(zt)){
				sb.append("UPDATE IN_ZIDB ");
				sb.append("set YUNXZT = '"+zt+"', ");
				sb.append("YUNXKSSJ=to_date('"+nowTime+"', ");
				sb.append(" 'yyyymmddHH24MIss') ");
				sb.append("where inbh = '"+bh+"' ");

			}else {
				/**
				 *  接口结束或者发生异常
				 * 	将接口状态更改为相应的值
				 * 	结束时间 维护为当前时间
				 * update 接口出错完成时间不变 ，以便于可以重转， 只改yunxzt  hzg 2013-10-16
				 */
				sb.append("UPDATE IN_ZIDB ");
				sb.append("set YUNXZT = '"+zt+"' ");
				sb.append(" where inbh = '"+bh+"' ");
			}
			
			Statement cs = conn.createStatement();
			cs.executeUpdate(sb.toString());
			commit(conn);
			cs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
	  		   DbUtils.freeConnection(conn);
	  	}
	}
	

	/**
	 * txt-->db
	 * 改造：
	 * 		将日志表合并 此时维护 总表的 文件数量字段,错误文件数量
     *
	 * @param datasourceId
	 * @param cID
	 * @param in_file_num 解析文件的数量
	 * @param file_error_num 错误文件的数量
	 */
	public  synchronized void updateIn_ZiDbFileNum(String datasourceId, String cID,
			int in_file_num,int file_error_num) {
		Connection conn = null;
		StringBuffer sb = new StringBuffer();
		try{
			
			sb.append(" UPDATE IN_ZIDB SET WENJSL = "+in_file_num+",  ");
			sb.append(" WENJCWSL = "+file_error_num+" ");
			sb.append(" WHERE INBH = '"+cID+"' ");
			
			conn = DbUtils.getConnection(datasourceId);
			Statement cs = conn.createStatement();
			cs.executeUpdate(sb.toString());
			commit(conn);
			cs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
  		   DbUtils.freeConnection(conn);
		}
	}


	/**
	 * db(业务表)-->txt
	 * 根据isreset 做维护 维护 生成条数   出错条数
	 * 	isreset true : 就是生成条数   出错条数为0  
	 * @param cID
	 */
	public  synchronized void updateIn_ZiDbSCTS(String dataSourceId,String cID) {
		Connection conn = null;
		StringBuffer sb = new StringBuffer();
		try{
			
			sb.append(" UPDATE IN_ZIDB SET SHENGCTS = 0,  ");
			sb.append(" CHUCTS = 0 ");
			sb.append(" WHERE INBH = '"+cID+"' ");
			
			conn = DbUtils.getConnection(dataSourceId);
			Statement cs = conn.createStatement();
			cs.executeUpdate(sb.toString());
			commit(conn);
			cs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
  		   DbUtils.freeConnection(conn);
		}
		
	}


	/**
	 * txt-->数据库
	 *   更新 日志总表的 错误文件数量
	 * @param datasourceId
	 * @param cid
	 */
	public  synchronized void update_cuowjsl(String datasourceId, String cid) {
		Connection conn= null;
		StringBuffer strbuf=new StringBuffer();
		try{
			
			strbuf.append("update IN_ZIDB i ");
			strbuf.append("set i.WENJCWSL = WENJCWSL + 1");
			strbuf.append("where i.INBH = '"+cid+"'");
			
			conn = DbUtils.getConnection(datasourceId);
			Statement cs = conn.createStatement();
			cs.executeUpdate(strbuf.toString());
			commit(conn);
			cs.close();

		}catch(SQLException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
		
	}
	
	/**
	 * 更新IN_ZIDB的  总记录数   错误记录数
	 * @param cid
	 * @param i 总记录数
	 * @param size 错误记录数
	 */
	public  synchronized void updateIN_zidbCHUCTS(String datasourceId,String cid, int i, int size) {
		StringBuffer strbuf=new StringBuffer();
		Connection conn= null;
		try{
			
			strbuf.append("UPDATE IN_ZIDB ");
			strbuf.append("SET SHENGCTS = SHENGCTS+"+i+",  ");
			strbuf.append(" CHUCTS = CHUCTS+" + size);
			strbuf.append("WHERE INBH = '"+cid+"' ");
			
			conn = DbUtils.getConnection(datasourceId);
			Statement cs = conn.createStatement();
			cs.executeUpdate(strbuf.toString());
			commit(conn);
			cs.close();
		}catch(SQLException e){
			logger.error(e.getMessage());
		}finally{
			DbUtils.freeConnection(conn);
		}
		
	}


	/**
	 * 总接口运行完 改变接口的状态  
	 * 	如果NowTime 不为空 则完成时间设置为此时间 （db__>txt做增量备份的时候）
	 *  如果'接口文件错误记录信息'表中有错误记录，则认为此接口运行是错误的
	 * @param nowTime
	 * @param connection
	 * @param taskName
	 */
	public  synchronized int UpdateInZIDB_ZTByMx(String nowTime,
			Connection conn, String taskName) {
		int result = 1;
		try {
			if(nowTime==null){
				nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			}
			
			StringBuffer sb = new StringBuffer();
			
			/**
			* 接口结束或者发生异常
			* 	将接口状态更改为相应的值
			* 	结束时间 维护为当前时间
			* update 接口转换错误会被“吃掉”并记录in_errorfile表，这种情况不抛1，所以运行状态应该为0， 接口运行正常结束；异常情况会被捕获并抛1
			* author hzg 2013-3-27
			*/
			
			sb.append("UPDATE IN_ZIDB ");
			sb.append(" set (YUNXZT, LASTCPLTIME, LASTLCTIME) = (select 0 ,");
			sb.append(" to_date('"+nowTime+"', ");
			sb.append(" 'yyyymmddHH24MIss'), ");
			sb.append(" t.lastcpltime ");
			sb.append("from IN_ZIDB t ");
			sb.append(" where t.inbh = '"+taskName+"') ");
			sb.append(" where inbh = '"+taskName+"' ");
			
			
			Statement cs = conn.createStatement();
			if (cs.executeUpdate(sb.toString()) > 0) {
				result = 0;
			} else {
				logger.error("没有更新" + taskName + "接口对应In_ZIDB表中数据");
			}
			commit(conn);
			cs.close(); 
		} catch (SQLException e) {
			logger.error("更新" + taskName + "接口In_ZIDB表中数据出错        " + e.getMessage(), e);
		}finally{
	  		   DbUtils.freeConnection(conn);
	  	}
		return result;
	}

	
	/**
	 *  根据任务名称拿出此任务的运行状态
	 *  	
	 * @param connection 数据库连接
	 * @param taskName 任务名称
	 * @return
	 */
	public  int getZIDB_ZTByid(Connection conn, String taskName) {
		int result = 0;
		try {
			StringBuffer sb = new StringBuffer();
			
			sb.append("select t.yunxzt yunxzt from IN_ZIDB t where t.inbh = '"+taskName+"'");
			
			Statement cs = conn.createStatement();
			ResultSet rs =  cs.executeQuery(sb.toString());
			String yunxzt = null;
			if(rs.next()){
				yunxzt = rs.getString("yunxzt");
			}
			//如果运行状态为 空或者非0 则都表示此接口运行失败
			if(yunxzt==null || !"0".equals(yunxzt)){
				result = 1;
			}
			
			if(rs!=null){
				rs.close();
			}
			if(cs!=null){
				cs.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
	  		   DbUtils.freeConnection(conn);
	  	}
		return result;
	}
}

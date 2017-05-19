package com.athena.component.output;




import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;


public class RkfkTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(RkfkTxtWriter.class);	//定义日志方法
	private String datetime=null;


	public RkfkTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		DateTime();//保存前一次运行的时间
		DeleteTable();//初始化清空接口表
		QueryTable();//查询仓库业务表(UA标签表)数据
		InsertTable();//将业务表数据插入到接口表里
	}
	
	
	/**
	 * 查询时间记录
	 */
	public void DateTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
		String sql="select cj_date from "+SpaceFinal.spacename_ck+".in_rkfk order by cj_date asc";
		Map map=DbUtils.selectOne(sql,interfaceConn);
	    Date date=(Date) map.get("cj_date");
	    if(null!=date){
	    datetime=format.format(date);
	    }
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 清空接口表数据
	 */
	public void DeleteTable(){
		try{
	    	   String sql="delete from "+SpaceFinal.spacename_ck+".in_rkfk";
	    	   DbUtils.execute(sql, interfaceConn);
	       }catch(RuntimeException ex){
	    	   logger.error(ex.getMessage());
	       }finally{
	  			 try{
	  				 if(null!=interfaceConn){
	  					interfaceConn.commit();
	  				 }
	  				 }catch(SQLException e){
	  					 logger.error(e.getMessage());
	  				 }
	           }
	}
		
	 
	 /**
	  * 查询仓库业务表(UA标签表)数据
	  *  	包装状态 为 5的  
	  */
	 public List<Map> QueryTable(){
		 List<Map> list=null;
		 StringBuffer strbuf=new StringBuffer();
		 try{
		 strbuf.append("select yanssl, yaohlh, to_char(ruksj,'yyyy-MM-dd HH24:mi:ss') ruksj, danw,lingjbh,substr(blh,0,1) tbbs from "+SpaceFinal.spacename_ck+".ck_uabq ");
		 if(null==datetime||"".equals(datetime)){
			   strbuf.append("where EDIT_TIME<= sysdate and baozzt = '5'");	   
			   }else{
			   strbuf.append("where EDIT_TIME between to_date('"+datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate and baozzt = '5'");
			   } 
 		 list=DbUtils.select(strbuf.toString(), businessConn);
		 }catch(RuntimeException e){
			 logger.error(e.getMessage());
		 }finally{
			 try{
			 if(null!=businessConn){
				 businessConn.commit();
			 }
			 }catch(SQLException e){
				 logger.error(e.getMessage());
			 }
		 }
		 return list;
	 
	 }
 	 
	 
	  /**
	   * 将业务表数据插入到接口表里
	   */
	   public void InsertTable(){
		 try{
			   List<Map> list=null;
			   StringBuffer	 strbuf =new StringBuffer();
			   list=this.QueryTable();
			   if(list.size()!=0){
			   for (Map map : list) {
			   strbuf.append("insert into "+SpaceFinal.spacename_ck+".in_rkfk");
			   strbuf.append("(YANSSL, YAOHLH, RUKSJ, DANW, CJ_DATE, CLZT,LJH,TBBS)values(");
			   strbuf.append("'"+map.get("yanssl")+"','"+map.get("yaohlh")+"',to_date('"+map.get("ruksj")+"','yyyy-mm-dd HH24:MI:ss')"+",'"
					   			+map.get("danw")+"',sysdate,'0','"+map.get("lingjbh")+"','"+map.get("tbbs")+"')");
			   DbUtils.execute(strbuf.toString(), businessConn);
			   strbuf = new StringBuffer("");
			   }
			 }
			   }catch(RuntimeException ex){
				   logger.error(ex.getMessage());
	           }finally{
	  			 try{
	  				 if(null!=businessConn){
	  					businessConn.commit();
	  				 }
	  				 }catch(SQLException e){
	  					 logger.error(e.getMessage());
	  				 }
	           }
	   }
}

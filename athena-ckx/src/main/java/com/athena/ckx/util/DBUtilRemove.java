package com.athena.ckx.util;


import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * DB工具
 * @author hj
 *
 */
public final class DBUtilRemove {
	/**
	 * 循环删除测试数据（@Test专用）
	 * @param args
	 */
	public static void remove(String[] args){
//		String sql = "delete from ckx_chanxz where usercenter = 'UW2' and chanxzbh = 'UXFL2' ";
		try{
			for (String sql : args) {
				DbUtils.execute(sql, DbUtils.getConnection("1"));
			}
			DbUtils.getConnection("1").commit();
			DbUtils.freeConnection(DbUtils.getConnection("1"));
		}catch( Exception e){
			try {
				DbUtils.getConnection("1").rollback();			
			} catch (SQLException e1) {
			}
		}
	}	
	/**
	 * 检查数据是否存在
	 * @param baseDao
	 * @param sqlId 
	 * @param bean
	 * @return 存在true  不存在  false
	 */
	public static boolean checkCount(AbstractIBatisDao baseDao,String sqlId,Object bean){
		Object obj= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(sqlId,bean);
		try {
			return Integer.valueOf(obj.toString())==0?false:true;
		} catch (NumberFormatException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	/**
	 * 验证编号，自动拼写sql语句
	 * @param map
	 * @param mes
	 */
	public static void checkYN(Map<String ,String> map,String mes){
		
		
		StringBuffer sb = new StringBuffer();
//		map.put("tableName", "chanxz");
		sb.append("select count(*) from  "+getdbSchemal()+map.get("tableName")+" where 1=1 ");
//		map.put("bh", "1");
//		map.put("sex", "1");
//		map.put("biaos", "1"); 
		for (String string : map.keySet()) {
			if(!"tableName".equals(string)){
				sb.append( " and " +string +" = '" +map.get(string)+"'");
			}
		}
		checkBH(sb.toString(),mes);
	}
	/**
	 * 验证编号 ，提供自己拼写sql语句
	 * @param sql
	 * @param mes
	 */
	public static void checkBH(String sql,String mes){
		Connection conn = DbUtils.getConnection(ConstantDbCode.DATASOURCE_CKX);
		Object bd =  DbUtils.selectValue(sql,conn );
		int count  =((BigDecimal)bd).intValue(); 
		DbUtils.freeConnection(conn);
		if( 0 == count ) {
			throw new ServiceException(mes);
		}	
		
	}
	
	/**
	 * 验证编号 ，提供自己拼写sql语句
	 * @param sql
	 * @param mes
	 */
	public static void checkBHDDBH(String sql,String mes){
		Connection conn = DbUtils.getConnection(ConstantDbCode.DATASOURCE_EXTENDS2);
		Object bd =  DbUtils.selectValue(sql,conn );
		int count  =((BigDecimal)bd).intValue(); 
		DbUtils.freeConnection(conn);
		if( 0 == count ) {
			throw new ServiceException(mes);
		}	
		
	}
	
	/**
	 * 读取dbSchemal  
	 * @Param key:dbSchemal1,dbSchemal2,dbSchemal3...
	 * @param projectName:ckx,fj,component,pc,print,xqjs...
	 * @return
	 */
	public static String getdbSchemal(String key,String projectName){
		try {
			InputStream in = DBUtilRemove.class.getClassLoader().getResourceAsStream("com/athena/"+projectName+"/config/sqlmap.properties");
			Properties p = new Properties();
			p.load(in);			
			return p.getProperty(key);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * 重载读取dbSchemal  
	 * @return
	 */
	public static String getdbSchemal(){
		return getdbSchemal("dbSchemal3","ckx");
	}
	/**
	 * 拼写权限语句（权限字段名不明确）
	 * @param column
	 * @param zuhs
	 * @return sql
	 */
	public static String getZuhSql(String column,String zuhs){
		String sql="";
		String[] zuh = zuhs.split(",");
		boolean flag=false;
		for (String str : zuh) {
			if(flag){
				sql +=" or";
			}else{
				sql +="(";
			}
			sql +=" instr("+column+",'"+str+"')>0 ";;
			flag=true;
		}	
		if(flag){
			sql+=")";
		}
		return sql;
	}
	/**
	 * 拼写权限语句（权限字段名：zuh）
	 * @param zuhs
	 * @return
	 */
	public static String getZuhSql(String zuhs){
		return getZuhSql("zuh",zuhs);
	}
	
}

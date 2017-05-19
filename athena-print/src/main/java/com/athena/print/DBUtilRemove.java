package com.athena.print;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.toft.core2.dao.database.DbUtils;

public  class DBUtilRemove {

	private static Logger logger=Logger.getLogger(DBUtilRemove.class);
	/**
	 * 循环删除测试数据（@Test专用）
	 * @param args
	 */
	public static void remove(String[] args){
		try{
			for (String sql : args) {
				DbUtils.execute(sql, DbUtils.getConnection("1"));
			}
			DbUtils.getConnection("1").commit();
		}catch( Exception e){
			try {
				DbUtils.getConnection("1").rollback();
			} catch (SQLException e1) {
				logger.info(e1.getMessage());
			}
			logger.info(e.getMessage());
		}
	}
	
	/**
	 * 循环更新测试数据（@Test专用）
	 * @param args
	 */
	public static void update(String[] args){
		try{
			for (String sql : args) {
				DbUtils.execute(sql, DbUtils.getConnection("1"));
			}
			DbUtils.getConnection("1").commit();
		}catch( Exception e){
			try {
				DbUtils.getConnection("1").rollback();
			} catch (SQLException e1) {
				logger.info(e1.getMessage());
			}
			logger.info(e.getMessage());
		}
	}
}

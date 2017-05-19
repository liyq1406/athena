/**
 * 
 */
package com.athena.component.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Title:SDC 序列号生成器</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class SequenceKeyGenerator {

	private final static Log logger = LogFactory
			.getLog(SequenceKeyGenerator.class);

	private String keyPrefix;// 值前缀

	private String tableName;// 数据表

	private String columnName;// 列

	private Connection conn;

	private int numCount;

	public SequenceKeyGenerator(Connection conn, String tableName,
			String columnName, String keyPrefix, int numCount) {
		this.conn = conn;
		this.tableName = tableName;
		this.columnName = columnName;
		this.keyPrefix = keyPrefix;
		this.numCount = numCount;
	}

	private String computeNewCode(String maxCode, String keyPrefix, int numCount) {

		String newCode = "";
		if (maxCode != null) {
			int i = keyPrefix.length();
			int j = maxCode.length();
			int k = j - i;

			String numPart = maxCode.substring(i, j);
			int theInt = new Integer(numPart).intValue();
			theInt++;
			String numString = new Integer(theInt).toString();
			k = k - numString.length();
			String temp0 = "";
			for (; k > 0; k--) {
				temp0 = temp0 + "0";
			}
			numString = temp0 + numString;
			newCode = keyPrefix + numString;
		} else {
			String temp0 = "";
			for (int k = numCount - 1; k > 0; k--) {
				temp0 = temp0 + "0";
			}
			newCode = keyPrefix + temp0 + "1";
		}
		return newCode;
	}

	public synchronized String getKey() {
		
		// oracle
		long startTime = System.currentTimeMillis();
		boolean locked = false;
		String lockSql = "select TABLE_NAME from SYS_TABLE_LOCK where TABLE_NAME like '"
				+ tableName + "' for update nowait";
		//0010198 0001
		//001cc98 0001
		String getMaxSql = "SELECT MAX(" + columnName + ") AS A FROM "
				+ tableName + " where " + columnName + " like '" + keyPrefix
				+ "%'";

		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;

		ResultSet maxCodeResult = null;
		String maxCode = "";
		String newCode = null;

		try {
			pstm1 = conn.prepareStatement(lockSql);
			logger.info("---------锁定数据:" + lockSql);
			try {
				pstm1.executeQuery();
			} catch (Exception e) {
				logger.error("----------------:"+e.getMessage());
				locked = true;
			}
			if(!locked){
				pstm2 = conn.prepareStatement(getMaxSql);
				logger.info("getMaxSql:" + getMaxSql);
				maxCodeResult = pstm2.executeQuery();
				maxCodeResult.next();
				maxCode = maxCodeResult.getString("A");
				newCode = computeNewCode(maxCode, keyPrefix, numCount);
				logger.info("newCode:" + newCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (maxCodeResult != null)
					maxCodeResult.close();
			} catch (SQLException e1) {
			}

			try {
				if (pstm1 != null)
					pstm1.close();
				if (pstm2 != null)
					pstm2.close();
			} catch (SQLException e1) {
			}
			logger.info("---------释放锁定数据:" + lockSql);
			logger.info("耗时：" + (System.currentTimeMillis() - startTime));
		}
		if (locked) {
			//throw new DataAccessException("其他用户正在操作该表！") {};
		}
		return newCode;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public int getNumCount() {
		return numCount;
	}

	public void setNumCount(int numCount) {
		this.numCount = numCount;
	}
}

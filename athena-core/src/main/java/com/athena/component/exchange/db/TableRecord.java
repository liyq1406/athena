/**
 * 
 */
package com.athena.component.exchange.db;

import java.util.Map;

import com.athena.component.exchange.Record;

/**
 * <p>Title:</p>
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
public class TableRecord extends Record{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9154002809747549206L;

	private String tableName;
	
	private String updateSql;
	
	private String insertSql;
	
//	private String insertValues;//   ?,?
//	
//	private String insertFields;// F001,F002
	
	private String[] fields; //{F001,F002}
	
	private String[] updateFields;//
	
	private String[] idKeys;//
	
	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

    public void setRecord(Map<String, Object> record){
        this.putAll(record);
    }

	public TableRecord(Map<String, Object> record){
		this.putAll(record);
	}

    public TableRecord(){
    }

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
//
//	public String getInsertValues() {
//		return insertValues;
//	}
//
//	public void setInsertValues(String insertValues) {
//		this.insertValues = insertValues;
//	}
//
//	public String getInsertFields() {
//		return insertFields;
//	}
//
//	public void setInsertFields(String insertFields) {
//		this.insertFields = insertFields;
//	}

	public String[] getUpdateFields() {
		return updateFields;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public void setUpdateFields(String[] updateFields) {
		this.updateFields = updateFields;
	}

	public String[] getIdKeys() {
		return idKeys;
	}

	public void setIdKeys(String[] idKeys) {
		this.idKeys = idKeys;
	}
}

/**
 * 
 */
package com.athena.component.test;

import java.util.List;

/**
 * <p>Title:SDC UI组件</p>
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
public class InsertParameter {
	
	private String tableName;//表名称
	
	private List<String> headers;//表头
	
	private List<RowValues> rowValues;//表数据
	
	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<RowValues> getRowValues() {
		return rowValues;
	}

	public void setRowValues(List<RowValues> rowValues) {
		this.rowValues = rowValues;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}

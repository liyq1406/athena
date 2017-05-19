package com.athena.util.tag.xls;

import java.sql.Connection;

import jxl.Workbook;

import org.dom4j.Document;

import com.toft.core2.dao.database.DbUtils;

/**
 * sheet处理器虚拟类
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-17
 */
public abstract class SheetHandler {
	protected Workbook workbook;
	protected Document document;
	protected String sheet;
	protected String table;
	protected String clazz;
	protected String keys;
	protected String dateColumns;
	protected String dateFormats;
	protected Connection conn;
	
	public SheetHandler(Workbook workbook, String sheet, String table,
			String datasourceId, String clazz,String keys,String dateColumns,String dateFormats) {
		super();
		this.workbook = workbook;
		this.sheet = sheet;
		this.table = table;
		this.conn =  DbUtils.getConnection(datasourceId);
		this.clazz = clazz;
		this.keys = keys;
		this.dateColumns = dateColumns;
		this.dateFormats = dateFormats;
	}
	
	/**
	 * 为了作为xml文件处理
	 * @param document
	 * @param sheet
	 * @param table
	 * @param datasourceId
	 * @param clazz
	 * @param keys
	 * @param dateColumns
	 * @param dateFormats
	 */
	public SheetHandler(Document document, String sheet, String table,
			String datasourceId, String clazz,String keys,String dateColumns,String dateFormats) {
		super();
		this.document = document;
		this.sheet = sheet;
		this.table = table;
		this.conn =  DbUtils.getConnection(datasourceId);
		this.clazz = clazz;
		this.keys = keys;
		this.dateColumns = dateColumns;
		this.dateFormats = dateFormats;
	}

	/**
	 * /处理sheet内容
	 * @return 返回错误信息
	 */
	public abstract String doSheetHandler();
	
	public Workbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
	public String getDateColumns() {
		return dateColumns;
	}

	public void setDateColumns(String dateColumns) {
		this.dateColumns = dateColumns;
	}

	public String getDateFormats() {
		return dateFormats;
	}

	public void setDateFormats(String dateFormats) {
		this.dateFormats = dateFormats;
	}
	
	
}

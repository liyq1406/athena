/**
 * 
 */
package com.athena.component.exchange.config;

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
public class GroupConfig {
	public static final String EXCHANGE_DB = "db";
	
	public static final String EXCHANGE_TXT = "txt";
	
	public static final String EXCHANGE_XML = "xml";
	
	private String reader;//输入
	
	private String writer;//输出

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	
}

package com.athena.component.input;


import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class CKWzklszDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(ClddxxDbDataWriter.class);	//定义日志方法
	
	
	public CKWzklszDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行记录解析之后   将时间类型数据格式转化成相应的时间格式形式.
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			String id=super.getUUID();
			record.put("ID", id);
			
			String lingjsl=record.getString("lingjsl");
			StringBuilder sb=new StringBuilder(lingjsl);
			String lingjsl1=sb.delete(0,1).toString();
			double lingjsl2=Double.parseDouble(lingjsl1);
			record.put("lingjsl", lingjsl2/1000);
			
			record.put("jfrq",DateTimeUtil.StringYMDToDate(DateTimeUtil.DateFormat(record.getString("jfrq"))));
			record.put("rksj",DateTimeUtil.StringYMDToDate(DateTimeUtil.DateFormat_Fhtz(record.getString("rksj")) ));
			record.put("dqsj",DateTimeUtil.StringYMDToDate(DateTimeUtil.DateFormat(record.getString("dqsj"))));
			
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		super.afterRecord(rowIndex, record, line);
	
	}
}

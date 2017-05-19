package com.athena.component.input;

import java.util.Date;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class FenpxDBDataWriter extends DbDataWriter{
	private Date date= new Date();
	public FenpxDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			//record.put("creator", "interface");
			record.put("creator", super.dataExchange.getCID());
			record.put("create_time", date);
			//record.put("editor", "interface");
			record.put("editor", super.dataExchange.getCID());
			record.put("edit_time", date);
			
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);	
		}
		super.afterRecord(rowIndex, record, line);
	}

}

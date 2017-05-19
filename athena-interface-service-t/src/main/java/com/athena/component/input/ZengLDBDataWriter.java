package com.athena.component.input;

import java.util.Date;


import com.athena.component.exchange.Record;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class ZengLDBDataWriter extends DbDataWriter {

	private  Date date = new Date();
	public ZengLDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	public void afterRecord(int rowIndex, Record record,Object line){		
		//record.put("CREATOR", "interface");
		record.put("CREATOR", super.dataExchange.getCID());
		record.put("CREATE_TIME", date);
		//record.put("EDITOR", "interface");
		record.put("EDITOR", super.dataExchange.getCID());
		record.put("EDIT_TIME", date);
		super.afterRecord(rowIndex, record, line);
	}

}

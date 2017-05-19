package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class DingdmxDBDataWriter extends DbDataWriter{

	public DingdmxDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		
		try{
		record.put("YAOHQSRQ",DateTimeUtil.StringYMDToDate(record.getString("YAOHQSRQ")));
		record.put("YAOHJSRQ",DateTimeUtil.StringYMDToDate(record.getString("YAOHJSRQ")));
		record.put("FAYRQ", DateTimeUtil.StringYMDToDate(record.getString("FAYRQ")));
		record.put("JIAOFRQ",DateTimeUtil.StringYMDToDate(record.getString("JIAOFRQ")));
		record.put("ZUIHWHSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIHWHSJ")));
		record.put("SHID",DateTimeUtil.StringYMDToDate(record.getString("SHID")));
		record.put("ZUIZDHSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIZDHSJ")));
		record.put("ZUIWDHSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIWDHSJ")));
		record.put("XIAOHSJ",DateTimeUtil.StringYMDToDate(record.getString("XIAOHSJ")));
		record.put("PCKAISSJ",DateTimeUtil.StringYMDToDate(record.getString("PCKAISSJ")));
		record.put("PCJIESSJ",DateTimeUtil.StringYMDToDate(record.getString("PCJIESSJ")));
		record.put("PCEDIT_TIME",DateTimeUtil.StringYMDToDate(record.getString("PCEDIT_TIME")));
		
		Date date=new Date();
		//record.put("CREATOR", "interface");
		record.put("CREATOR", super.dataExchange.getCID());
		record.put("CREATE_TIME", date);
		//record.put("EDITOR", "interface");
		record.put("EDITOR", super.dataExchange.getCID());
		record.put("EDIT_TIME", date);
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
		super.afterRecord(rowIndex, record, line);
	}
	
	
	

}

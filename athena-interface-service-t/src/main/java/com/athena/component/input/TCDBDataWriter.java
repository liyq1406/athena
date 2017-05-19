package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class TCDBDataWriter extends DbDataWriter{

	public TCDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		
		record.put("QIYSJ", DateTimeUtil.StringYMDToDate(record.getString("QIYSJ")));
		record.put("YUJDDSJ", DateTimeUtil.StringYMDToDate(record.getString("YUJDDSJ")));
		record.put("DAODWLDSJ", DateTimeUtil.StringYMDToDate(record.getString("DAODWLDSJ")));
		record.put("ZUIXYJDDSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIXYJDDSJ")));
		record.put("LAXZDDDSJ", DateTimeUtil.StringYMDToDate(record.getString("LAXZDDDSJ")));
		record.put("KAIXZDSJ", DateTimeUtil.StringYMDToDate(record.getString("KAIXZDSJ")));
		
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

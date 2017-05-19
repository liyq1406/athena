package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class NBYHLDBDataWriter extends DbDataWriter{

	public NBYHLDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		record.put("JIAOFJ",DateTimeUtil.StringYMDToDate(record.getString("JIAOFJ")));
		record.put("ZUIZSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIZSJ")));
		record.put("ZUIWSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIWSJ")));
		record.put("FAYSJ",DateTimeUtil.StringYMDToDate(record.getString("FAYSJ")));
		record.put("BEIHSJ",DateTimeUtil.StringYMDToDate(record.getString("BEIHSJ")));
		record.put("SHANGXSJ",DateTimeUtil.StringYMDToDate(record.getString("SHANGXSJ")));
		record.put("YAOHLSCSJ",DateTimeUtil.StringYMDToDate(record.getString("YAOHLSCSJ")));
		record.put("SHIJFYSJ",DateTimeUtil.StringYMDToDate(record.getString("SHIJFYSJ")));
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

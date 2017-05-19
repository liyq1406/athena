package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class DingdDBDataWriter extends DbDataWriter{

	public DingdDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
	   try{
       record.put("DINGDSXSJ",DateTimeUtil.StringYMDToDate(record.getString("DINGDSXSJ")));
       record.put("DINGDFSSJ",DateTimeUtil.StringYMDToDate(record.getString("DINGDFSSJ")));
       record.put("DINGDJSSJ",DateTimeUtil.StringYMDToDate(record.getString("DINGDJSSJ")));
       record.put("ZIYHQRQ",DateTimeUtil.StringYMDToDate(record.getString("ZIYHQRQ")));
		
       
        Date date=new Date();
        //订单回传不需要修改2013-06-18
//		record.put("CREATOR", "interface");
//		record.put("CREATE_TIME", date);
//		record.put("EDITOR", "interface");
//		record.put("EDIT_TIME", date);	
	   }catch(Exception e){
		   super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
	   }
		super.afterRecord(rowIndex, record, line);
	}
	
}




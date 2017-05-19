package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
/**
 * 零件供应商
 * @author kong
 *
 */
public class LingjgysDBDataWriter  extends DbDataWriter{
	public  Date date=new Date();
	public LingjgysDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			record.put("shengxsj",DateTimeUtil.StringYMDToDate(record.getString("shengxsj")));
//			record.put("shengxsj", format.parse("2012-01-01"));
				record.put("shixsj",DateTimeUtil.StringYMDToDate(record.getString("shixsj")));
				record.put("create_time",date);
				//record.put("creator","interface");
				//record.put("editor","interface");
				record.put("creator",super.dataExchange.getCID());
				record.put("editor",super.dataExchange.getCID());
				record.put("edit_time",date);
			super.afterRecord(rowIndex,record,line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
    }
}

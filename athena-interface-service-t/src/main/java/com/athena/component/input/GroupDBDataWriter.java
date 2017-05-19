package com.athena.component.input;

import java.util.Date;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
/**
 * 工作时间编组
 * @author kong
 *
 */
public class GroupDBDataWriter  extends DbDataWriter{
	public  Date date=new Date();
	public GroupDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}

	
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			record.put("create_time",date);
//			record.put("creator","interface");
//			record.put("editor","interface");
			record.put("creator",super.dataExchange.getCID());
			record.put("editor",super.dataExchange.getCID());
			record.put("edit_time",date);
			super.afterRecord(rowIndex,record,line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	

}

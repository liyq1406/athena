package com.athena.component.input;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
/**
 * 调拨明细
 * @author kong
 *
 */
public class DiaobmxDBDataWriter extends DbDataWriter {
	public DiaobmxDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	public  Date date=new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			record.put("create_time",Timestamp.valueOf(df.format(date)));
//			record.put("creator","interface");
//			record.put("editor","interface");
			record.put("creator",super.dataExchange.getCID());
			record.put("editor",super.dataExchange.getCID());
			record.put("edit_time",Timestamp.valueOf(df.format(date)));
			record.put("shengxsj",DateTimeUtil.StringYMDToDate(record.getString("shengxsj")));
			
			super.afterRecord(rowIndex,record,line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}

}

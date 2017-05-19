package com.athena.component.input;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
/**
 * 看板循环规模
 * @author kong
 *
 */
public class KanbxhgmDBDataWriter  extends DbDataWriter{
	public KanbxhgmDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	Date date=new Date();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			record.put("create_time",Timestamp.valueOf(df.format(date)));
			//record.put("creator","interface");
			record.put("creator",super.dataExchange.getCID());

			record.put("EDIT_TIME",Timestamp.valueOf(df.format(date)));
			//record.put("EDITOR","interface");
			record.put("EDITOR",super.dataExchange.getCID());
			
			record.put("weihsj","".equals(record.getString("weihsj").trim())?null:Timestamp.valueOf(record.getString("weihsj")));
//			record.put("weihr","interface");
			// Mantis 3991 当前循环规模值（DANGQXHGM）不从准备层同步  将已下发的循环规模覆盖当前循环规模-->
			//record.put("dangqxhgm",record.getString("xiafxhgm"));
			//record.put("dangqxhgm","");
			String time=record.getString("jissj");
			record.put("jissj","".equals(time.trim())?null:Timestamp.valueOf(time));
			super.afterRecord(rowIndex,record,line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	
	public static void main(String[] args) {

		Timestamp ts = Timestamp.valueOf("2012-07-16 14:21:10");
		
		//2012-07-16 14:21:49.399453
		System.out.println(ts);
	}
}

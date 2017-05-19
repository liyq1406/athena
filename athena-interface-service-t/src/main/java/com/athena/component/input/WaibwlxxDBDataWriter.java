package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class WaibwlxxDBDataWriter  extends DbDataWriter{
	public WaibwlxxDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}

	//public static Date date=new Date();
	private Date date  = new Date();
	
	public boolean before(){
		try{
			String sql="delete from "+SpaceFinal.spacename_ck+".ckx_waibwlxx";
			super.execute(sql);
			super.commit();
			}catch(RuntimeException e)
			{
				logger.error(e.getMessage());
			}
		return super.before();
	}
	
	 public  void afterRecord(int rowIndex, Record record,Object line) {
	    	try {
				record.put("create_time",date);
//				record.put("creator","interface");
//				record.put("editor","interface");
				record.put("creator",super.dataExchange.getCID());
				record.put("editor",super.dataExchange.getCID());
				record.put("edit_time",date);
				record.put("shengxrq",DateTimeUtil.StringYMDToDate(record.getString("shengxrq")));
				super.afterRecord(rowIndex,record,line);
			} catch (Exception e) {
				super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
			}
	    }
}

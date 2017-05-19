package com.athena.component.input;

import java.util.Date;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * 内部接口-工作日历
 * 数据写入类
 * @author kong
 *
 */
public class VerstionDBDataWriter  extends DbDataWriter{

	
	public Date date=new Date();
	public VerstionDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	public boolean before(){
		try{
			String sql="delete from "+SpaceFinal.spacename_ck+".ckx_calendar_version";
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

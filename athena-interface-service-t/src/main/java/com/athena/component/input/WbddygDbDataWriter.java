package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class WbddygDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(WbddygDbDataWriter.class);	//定义日志方法
	public WbddygDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析前操作	
	 */
	@Override
	public boolean before() {
		try{
		String sql="delete from "+SpaceFinal.spacename_ck+".in_wbddyg";
		super.execute(sql);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	/**
	 * 解析后操作
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		String shul=record.get("shul").toString();
		int i_shul=Integer.parseInt(shul);
		record.put("shul", i_shul);
		
		
		//存入创建时间和处理状态初始值
		record.put("cj_date", new Date());
		record.put("clzt", 0);
		super.afterRecord(rowIndex, record, line);
		}catch(RuntimeException e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

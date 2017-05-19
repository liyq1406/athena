package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;


import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class SyofDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(SyofDbDataWriter.class);	//定义日志方法
	public SyofDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public boolean before() {
	   try{
		String sql="delete from "+SpaceFinal.spacename_ckx+".in_syof";
		super.execute(sql);
		super.commit();
	   }catch(RuntimeException e){
		  logger.error(e.getMessage());
	   }
	   return super.before();
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		// 插入创建时间和处理状态初始数据
		record.put("CJ_DATE", new Date());
		record.put("CLZT", 0);
		super.afterRecord(rowIndex, record, line);
	}

	
	
}

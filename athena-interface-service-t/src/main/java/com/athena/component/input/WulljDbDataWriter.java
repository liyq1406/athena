package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class WulljDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(WulljDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public WulljDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**  
	 * 解析数据之前清空ckx_wullj表数据
	 * */
	public boolean before(){
		try {
			String sql = "delete from "+SpaceFinal.spacename_ck+".ckx_wullj";
			super.execute(sql);
			super.commit();
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before(); 
	}
	
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			//record.put("creator", "interface");
			record.put("creator", super.dataExchange.getCID());
			record.put("create_time", date);
			//record.put("editor", "interface");
			record.put("editor", super.dataExchange.getCID());
			record.put("edit_time", date);
			
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);	
		}
		super.afterRecord(rowIndex, record, line);
	}
}

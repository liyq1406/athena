package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class ChukmxDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(ChukmxDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public ChukmxDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间,并将时间类型数据格式转化成【yyyy-MM-dd HH:mm:ss】形式.
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			record.put("creator", "interface");
			record.put("create_time", date);
			record.put("editor", "interface");
			record.put("edit_time", date);
			record.put("shangxsj",DateTimeUtil.StringYMDToDate(record.getString("shangxsj")));
			record.put("daysj",DateTimeUtil.StringYMDToDate(record.getString("daysj")));
			record.put("chukrq",DateTimeUtil.StringYMDToDate(record.getString("chukrq")));
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

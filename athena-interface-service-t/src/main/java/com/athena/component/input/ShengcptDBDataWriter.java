package com.athena.component.input;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.util.date.DateUtil;

public class ShengcptDBDataWriter extends DbDataWriter{
	private Date date= new Date();
	public ShengcptDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			record.put("creator", "interface");
			record.put("create_time", date);
			record.put("editor", "interface");
			record.put("edit_time", date);
			record.put("qiehsj", stringSFMToStringYMD(record.getString("qiehsj")));
			
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);	
		}
		super.afterRecord(rowIndex, record, line);
	}

	
	public  Date stringSFMToStringYMD(String str) {
		
		if(str==null||str.trim().length()==0){
			return null;
		}
		DateFormat yyyyMMddFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date date = new Date();
		try {
			date = yyyyMMddFormat.parse(str);
		} catch (ParseException e) {
			Logger log =  Logger.getLogger(DateUtil.class);
			log.error(e.getMessage());
		}
		return date;
	}
}

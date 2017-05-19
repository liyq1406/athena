package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class KuckzDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(KuckzDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public KuckzDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	
	/**
	 * 解析数据之前，每次导入数据前清除所有数据
	 * hzg 2013-4-3 
	 */
	@Override
	public boolean before()
	{ 
		StringBuilder buff = new StringBuilder();
		try{
			buff.append("delete from "+SpaceFinal.spacename_ckx+".xqjs_ziykzb");
		super.execute(buff.toString());
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间,并将时间类型数据格式转化成【yyyy-MM-dd HH:mm:ss】形式.
	 * */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
//			record.put("creator", "interface");
//			record.put("create_time", date);
//			record.put("editor", "interface");
//			record.put("edit_time", date);
			record.put("ziyhqrq",DateTimeUtil.StringYMDToDate(record.getString("ziyhqrq")));
		super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

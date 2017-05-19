package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class XiaohcysskDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(ClddxxDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public XiaohcysskDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 解析数据之前清空ckx_xiaohcyssk表数据
	 * */
	public boolean befroe(){
		try {
			String sql = "delete from "+SpaceFinal.spacename_ck+".ckx_xiaohcyssk";
			super.execute(sql);
			super.commit();
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before(); 
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间,并将时间类型数据格式转化成【yyyy-MM-dd HH:mm:ss】形式.
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			//record.put("CREATOR", "interface");
			record.put("CREATOR", super.dataExchange.getCID());
			record.put("CREATE_TIME", date);
			//record.put("EDITOR", "interface");
			record.put("EDITOR", super.dataExchange.getCID());
			record.put("EDIT_TIME", date);
			record.put("kaisbhsj","".equals(record.getString("kaisbhsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("kaisbhsj")));
			record.put("chufsxsj","".equals(record.getString("chufsxsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("chufsxsj")));
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
	}
}

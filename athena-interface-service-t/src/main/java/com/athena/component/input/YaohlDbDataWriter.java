package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class YaohlDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(YaohlDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public YaohlDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间,并将时间类型数据格式转化成【yyyy-MM-dd HH:mm:ss】形式.
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			//record.put("creator", "interface");
			record.put("creator", super.dataExchange.getCID());
			record.put("create_time", date);
			//record.put("editor", "interface");
			record.put("editor", super.dataExchange.getCID());
			record.put("edit_time", date);
			record.put("jiaofj","".equals(record.getString("jiaofj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("jiaofj")));
			record.put("zuizsj","".equals(record.getString("zuizsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("zuizsj")));
			record.put("zuiwsj","".equals(record.getString("zuiwsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("zuiwsj")));
			record.put("faysj","".equals(record.getString("faysj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("faysj")));
			record.put("shangxsj","".equals(record.getString("shangxsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("shangxsj")));
			record.put("yaohlscsj","".equals(record.getString("yaohlscsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("yaohlscsj")));
			record.put("xiughyjjfsj","".equals(record.getString("xiughyjjfsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("xiughyjjfsj")));
			record.put("shijfysj","".equals(record.getString("shijfysj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("shijfysj")));
			record.put("laxzdddsj","".equals(record.getString("laxzdddsj").trim())?"":DateTimeUtil.StringYMDToDate(record.getString("laxzdddsj")));
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
	}
}

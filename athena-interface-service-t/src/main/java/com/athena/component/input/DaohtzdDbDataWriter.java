package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class DaohtzdDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(DaohtzdDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public DaohtzdDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	public boolean before() {
		try{
			String sql="delete from "+SpaceFinal.spacename_ckx+".ck_daohtzd ";
			super.execute(sql);
			
		}catch(RuntimeException e)
		{
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
			record.put("utscsj",DateTimeUtil.StringYMDToDate(record.getString("utscsj")));
			record.put("yujddsj",DateTimeUtil.StringYMDToDate(record.getString("yujddsj")));
			record.put("blscsj",DateTimeUtil.StringYMDToDate(record.getString("blscsj")));
			record.put("yanssj",DateTimeUtil.StringYMDToDate(record.getString("yanssj")));
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
	}
}

package com.athena.component.input;

import java.util.Date;
import org.apache.log4j.Logger;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class DDBHYccfDBDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(DDBHYccfDBDataWriter.class);	//定义日志方法
	
	public DDBHYccfDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行解析之后处理方法
	 * 
	 * @param rowIndex
	 *            行标
	 * @param record
	 *            行数据集合
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		if (!record.isEmpty()) {
			record.put("CREATE_TIME", new Date()) ;
//			record.put("EDITOR", "sys") ;
			record.put("EDITOR", super.getDataExchange().getCID()) ;
			record.put("EDIT_TIME", new Date()) ;
		}
		super.afterRecord(rowIndex, record, line);
		}catch(RuntimeException e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

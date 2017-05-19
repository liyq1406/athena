package com.athena.component.input;

import java.util.Date;


import org.apache.log4j.Logger;


import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class CLDV_CODDCDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(CLDV_CODDCDataWriter.class);	//定义日志方法
	
	public CLDV_CODDCDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析之前的操作
	 */
	@Override
	public boolean before() {
		try {
			String sql = "delete from "+SpaceFinal.spacename_ddbh+".in_cldv_coddc";// 清空表数据
			super.execute(sql);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before();
	}

	/**
	 * 行解析前处理
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {

		if (rowIndex==1) {// 文件第一行不导入表
			return false;
		}
		if("".equals(line.toString().trim())){ //如果为空字符行过滤掉
			return false;
		}
		if(line!=null&&line.toString().contains("PDS—ATHENA")){
			return false;
		}
		return true;
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		// 插入创建时间和处理状态初始数据
		record.put("cj_date", new Date());
		record.put("clzt", 0);
		
		super.afterRecord(rowIndex, record, line);
		}catch(RuntimeException e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);	
		}
		
	}

}

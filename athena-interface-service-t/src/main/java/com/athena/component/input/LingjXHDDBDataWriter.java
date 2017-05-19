package com.athena.component.input;

import java.util.Date;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class LingjXHDDBDataWriter extends DbDataWriter {

	private  Date date = new Date();
	public LingjXHDDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 解析前操作	
	 */
	@Override
	public boolean before() {
		try{
		
			String sql="update "+SpaceFinal.spacename_ck+".ckx_lingjxhd set creator = 'temp' where t.biaos = '0'";
			super.execute(sql);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	public void afterRecord(int rowIndex, Record record,Object line){		
		//record.put("CREATOR", "interface");
		record.put("CREATOR", "interface");
		record.put("CREATE_TIME", date);
		//record.put("EDITOR", "interface");
		record.put("EDITOR",  super.dataExchange.getCID());
		record.put("EDIT_TIME", date);
		try {
			record.put("SHENGXR",DateTimeUtil.StringYMDToDate(record.get("SHENGXR").toString()));
			record.put("JIESR",DateTimeUtil.StringYMDToDate(record.get("JIESR").toString()) );
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	
	@Override
	/**
	 * 进行业务数据清空 add by pan.rui
	 */
	public void after() {
		try{
			String sql="update "+SpaceFinal.spacename_ck+".ckx_lingjxhd set yifyhlzl =null,jiaofzl=null,xittzz=null,zhongzzl=null " 
			          +"where (creator = 'temp' and biaos = '1') or creator = 'interface'";
			super.execute(sql);
			String sqlCreator = "update "+SpaceFinal.spacename_ck+".ckx_lingjxhd set creator = '2090'";
			super.execute(sqlCreator);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
	}

}

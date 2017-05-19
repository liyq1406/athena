package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class LingjXHBDBDataWriter extends DbDataWriter {

	public LingjXHBDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 解析数据之前，每次导入数据前根据创建日期保留最近两版数据
	 * hzg 2013-3-11 。
	 * 删除标识为1的记录，1：为接口数据，2：用户数据
	 * hzg 2013-7-3
	 */
	public boolean before()
	{ 
		StringBuilder buff = new StringBuilder();
		try{
			buff.append("delete from "+SpaceFinal.spacename_ckx+".XQJS_KUCJSCSB");
			buff.append(" where create_time  not in(");
			buff.append(" select * from(");
			buff.append(" select create_time from "+SpaceFinal.spacename_ckx+".xqjs_kucjscsb  group by create_time order by create_time desc");
			buff.append(" )s where  rownum<3 ) and flag='1'");
			super.execute(buff.toString());
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	public void afterRecord(int rowIndex, Record record,Object line){
		try{
			
		record.put("JILRQ", DateTimeUtil.StringYMDToDate(record.getString("JILRQ")));
		record.put("CREATE_TIME", new Date());
		
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		super.afterRecord(rowIndex, record, line);
	}

}

package com.athena.component.output;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataReader;

public class LingjXHBDBDataReader extends DbDataReader {

	public LingjXHBDBDataReader(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	protected String makeIncrementSql(String sql,String nowTime) {
		//1：查找接口总表数据库,拿出此接口的 完成时间
		//2: 以此完成时间，和当前时间做条件 生成串
		//3: 更新此接口总表 此接口的完成时间，上上次完成时间	
		StringBuffer sb = new StringBuffer();
		sb.append(sql);		
		sb.append(" AND TO_CHAR(JILRQ, 'yyyymmddHH24MIss') BETWEEN ");
		sb.append(" (SELECT TO_CHAR(i.LASTCPLTIME, 'yyyymmddHH24MIss') ");
		sb.append(" FROM IN_ZIDB i ");
		sb.append(" WHERE i.INBH = '"+dataParserConfig.getId()+"') and '"+nowTime+"' ");

		return sb.toString();
	}

}

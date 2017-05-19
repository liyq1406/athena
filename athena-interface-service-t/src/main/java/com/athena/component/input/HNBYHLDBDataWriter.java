package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.toft.core2.dao.database.DbUtils;

public class HNBYHLDBDataWriter extends DbDataWriter{

	public HNBYHLDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		record.put("JIAOFJ",DateTimeUtil.StringYMDToDate(record.getString("JIAOFJ")));
		record.put("ZUIZSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIZSJ")));
		record.put("ZUIWSJ",DateTimeUtil.StringYMDToDate(record.getString("ZUIWSJ")));
		record.put("FAYSJ",DateTimeUtil.StringYMDToDate(record.getString("FAYSJ")));
		//record.put("BEIHSJ",DateTimeUtil.StringYMDToDate(record.getString("BEIHSJ")));
		record.put("SHANGXSJ",DateTimeUtil.StringYMDToDate(record.getString("SHANGXSJ")));
		record.put("YAOHLSCSJ",DateTimeUtil.StringYMDToDate(record.getString("YAOHLSCSJ")));
		record.put("SHIJFYSJ",DateTimeUtil.StringYMDToDate(record.getString("SHIJFYSJ")));
		Date date=new Date();
		//record.put("CREATOR", "interface");
		record.put("CREATOR", super.dataExchange.getCID());
		record.put("CREATE_TIME", date);
		record.put("EDITOR", "temp");
		record.put("EDIT_TIME", date);
		
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);	
		}
		super.afterRecord(rowIndex, record, line);
	}
	
	@Override
	public void after() {
		//将本次接口的子仓库代码更新
		String sql1 = " UPDATE "+SpaceFinal.spacename_ck+".CK_YAONBHL T SET T.ZICKBH =(SELECT T1.ZICKBH FROM "
				    + SpaceFinal.spacename_ck+".CKX_LINGJCK T1 WHERE T1.USERCENTER = T.USERCENTER AND T1.LINGJBH = T.LINGJBH "
				    + " and T1.CANGKBH = T.CANGKBH) WHERE T.EDITOR = 'temp'";
		super.execute(sql1);
		//将修改人改为interface
		String sql = "update "+SpaceFinal.spacename_ck+".CK_YAONBHL d set d.EDITOR = '2560' where d.EDITOR = 'temp'";
		super.execute(sql);
		super.after();
	}
	

}

package com.athena.component.input;

import java.util.Date;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * DDBH拆分结果
 * @author kong
 *
 */
public class ChaifjgHDBDataWriter extends DbDataWriter{
	public  Date date=new Date();
	public ChaifjgHDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	
	
//	public boolean before() {
//		try{
//			String sql="delete from "+SpaceFinal.spacename_ck+".ddbh_caifjg where flag = '1'";
//			super.execute(sql);
//			
//		}catch(RuntimeException e)
//		{
//			logger.error(e.getMessage());
//		}
//		return super.before();
//	}
	
	
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {
			record.put("flag", "1");
			record.put("create_time",date);
//			record.put("creator","interface");
//			record.put("editor","interface");
			record.put("creator",super.dataExchange.getCID());
			record.put("editor",super.dataExchange.getCID());
			record.put("edit_time",date);
			record.put("emon",DateTimeUtil.StringYMDToDate(record.getString("emon")));
			//此类分别给1150    2430  调用，由于列名不统一，所以需要分别解析
			record.put("emonsj",DateTimeUtil.StringYMDToDate(record.getString("emonsj")));
			record.put("xhsj",DateTimeUtil.StringYMDToDate(record.getString("xhsj")));
			record.put("xiaohsj",DateTimeUtil.StringYMDToDate(record.getString("xiaohsj")));
			record.put("caifsj",DateTimeUtil.StringYMDToDate(record.getString("caifsj")));
			record.put("chaifsj",DateTimeUtil.StringYMDToDate(record.getString("chaifsj")));
			super.afterRecord(rowIndex,record,line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	
	public void after(){
		//首先将flag为1数据也插入ddbh_chaifjginfo，在将flag设置为0;
        try{
	        String insertSql = "insert into " +SpaceFinal.spacename_ck+".ddbh_caifjginfo  select * from "+SpaceFinal.spacename_ck+".ddbh_caifjg t where t.flag = '1'";
	        super.execute(insertSql);
			String updateSql = "update "+SpaceFinal.spacename_ck+".ddbh_caifjg set flag='0' where flag='1' ";
			super.execute(updateSql);

        }catch(Exception e){
        	logger.error(e.getMessage());
        }
		super.after();
	}

}

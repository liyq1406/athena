package com.athena.component.input;

import java.util.Date;
import org.apache.log4j.Logger;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class XXDDbDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(XXDDbDataWriter.class);	//定义日志方法
	
	public XXDDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析之前的处理
	 */
	@Override
	public boolean before() {
		try {
			
//    		String sql="delete from "+SpaceFinal.spacename_ckx+".in_xhdckx";
			String sql = "update " +SpaceFinal.spacename_ckx+".ckx_gongyxhd d set d.gongybs=0";
			super.execute(sql);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before();
	}


	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		String lineStr=line.toString();        	
        if(lineStr.indexOf("PDS—ATHENA")!=-1||lineStr.indexOf("BEGIN==>")!=-1){
        	return false;
        }
        else{                 
        return true;
        }

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
//			record.put("CREATOR", "interface") ;
//			record.put("EDITOR", "interface") ;
			record.put("CREATOR", super.dataExchange.getCID()) ;
			record.put("EDITOR", super.dataExchange.getCID()) ;
			record.put("GONGYBS", "1") ;
			record.put("BIAOS", "2") ;
			record.put("CREATE_TIME", new Date()) ;
			record.put("EDIT_TIME", new Date()) ;
		}
		super.afterRecord(rowIndex, record, line);
		}catch(RuntimeException e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

package com.athena.component.input;




import java.util.Date;

import org.apache.log4j.Logger;


import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class XXDLJDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(XXDLJDataWriter.class);	//定义日志方法

	public XXDLJDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析之前的操作
	 */
	@Override
	public boolean before() {
//		try {
//			String sql="delete from "+SpaceFinal.spacename_ckx+".in_xhdljckx";
//			super.execute(sql);
//		} catch (RuntimeException e) {
//			logger.error(e.getMessage());
//		}
		return super.before();
	}

	/**
	 * 解析行数据之前操作
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
//		String str=null;
//		String czm=null;
//		if(!"".equals(line.toString())){
//			str=line.toString().substring(0, 24);
//			czm=line.toString().substring(0, 1);
//		}
//	    if(str.indexOf("PDS")!=-1){
//        	return false;
//        }
//	    if("D".equals(czm)){
//			return false;
//		}
//		else{
//		   return true;
//		}
	    String str=line.toString() ;
	    if(str.indexOf("PDS")!=-1){
        	return false;
        }
	    
	    return true;
	    
	}

	/**
	 * 解析之后的操作
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		if (!record.isEmpty()) {
			
			String czm = line.toString().substring(0, 1);
			String xxd = record.getString("XIAOHDBH");
			String usercenter1 = xxd.substring(0, 1);
			String usercenter="U"+usercenter1;
			record.put("USERCENTER", usercenter);
			
//				if ("C".equals(czm)) {
//					record.put("GONGYBS", "1");
//				} else 
//					
				if ("M".equals(czm)) {
					record.put("GONGYBS", "1");
				} else if ("D".equals(czm)) {
					record.put("GONGYBS", "0");
				} else if ("S".equals(czm) || "C".equals(czm)) {
					record.put("BIAOS", "2");
					record.put("GONGYBS", "1");
					dataParserConfig.getDataFields()[8].setUpdate(true);
					dataParserConfig.getDataFields()[9].setUpdate(true);

				}
//			record.put("CREATOR", "interface") ;
//			record.put("EDITOR", "interface") ;
			record.put("CREATOR", super.dataExchange.getCID()) ;
			record.put("EDITOR", super.dataExchange.getCID()) ;
			record.put("CREATE_TIME", new Date()) ;
			record.put("EDIT_TIME", new Date()) ;
			
			/**** 增加默认值 hzg 2012-10-21 bug:0004871 ****/
			record.put("XIANBLLKC", 0);
			record.put("YIFYHLZL", 0);
			record.put("JIAOFZL", 0);
			record.put("ZHONGZZL", 0);
		}
		super.afterRecord(rowIndex, record, line);
		
		dataParserConfig.getDataFields()[8].setUpdate(false);
		dataParserConfig.getDataFields()[9].setUpdate(false);
		
		}catch(RuntimeException e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}

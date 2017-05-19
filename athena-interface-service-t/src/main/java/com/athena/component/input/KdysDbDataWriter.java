package com.athena.component.input;






import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * KD件在途、物理点信息接口输入类
 * @author GJ
 */
public class KdysDbDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(KdysDbDataWriter.class);	//定义日志方法
	public KdysDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	
	/**
	 * 数据解析之前清空KD件在途、物理点信息表数据
	 */
	@Override
	public boolean before() {
		try{
			String sql="delete from "+SpaceFinal.spacename_ck+".in_kdwld b where b.CLZT='1' ";
			super.execute(sql);
			super.commit();
		}catch(RuntimeException e)
		{
			logger.error(e.getMessage());
		}	
		return super.before();
	}


	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		if(null!=record)
		record.put("id", getUUID());//ID,为UUID
        String falg=record.getString("falg");
        if("C".equals(falg)){
        	String pap_sheet_id=record.getString("pap_sheet_id");
        	record.put("pap_sheet_id", "");
        	record.put("kdys_sheet_id", pap_sheet_id);
        	
        	String pap_box_id=record.getString("pap_box_id");
        	record.put("pap_box_id","");
        	record.put("kdys_box_id", pap_box_id);
        	
        	//String kdys_box_id=record.getString("kdys_box_id");
        	//String c_point_id=kdys_box_id.substring(0, 3).trim();
        	String c_point_id = line.toString().substring(62,65);
        	record.put("c_point_id", c_point_id);       
        }	
        record.put("clzt", "0");
        record.put("cj_date", new Date());
        
        super.afterRecord(rowIndex, record, line);
		}catch(RuntimeException e){
		  super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
	}
	
	
}

package com.athena.component.output;


import java.io.IOException;
import java.io.OutputStreamWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

//lastModify 王冲, 2012-08-30 11:18
public class CpkkzTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(CpkkzTxtWriter.class);	//定义日志方法
	public CpkkzTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
       
	
	@Override
	public void fileBefore(OutputStreamWriter writer) {
		String DateTime=DateTimeUtil.getDateTimeStr("yyyyMMddHHmmss");
        try {
			writer.write("DEBATHENA  ath1osap01SS00448"+DateTime+"        ");
			writer.write("\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}	
		 super.fileBefore(writer);
	}
    


	@Override
	public void fileAfter(OutputStreamWriter out) {
        try {
			out.write("FINATHENA  ath1osap01SS00448"+totalToString()+"             ");
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		super.fileAfter(out);
	}


	public   String totalToString(){
		StringBuffer total = new StringBuffer(String.valueOf(this.getTotal()) ) ; 
		
		for(int i = total.length() ;i<9;i++){
			total.insert(0, "0") ;
		}
		
		return total.toString();
	}
	
	
	@Override
	public void after() {
//		try{
//			String q_sql="select end_time from "+SpaceFinal.spacename_ck+".in_time";
//		    Map map=super.selectOne(q_sql);
//		    if(map.size()==0){
//		    	String i_sql="insert into in_time(in_id,in_name,end_time)values('OUT_1','',sysdate)";
//		    	super.execute(i_sql);
//		    	super.commit();
//		    }else{
//			String u_sql="update "+SpaceFinal.spacename_ck+".in_time set end_time=sysdate where in_id='OUT_1'"; 
//	        super.execute(u_sql);
//	        super.commit();   
//		    }
//			}catch(RuntimeException e){
//				logger.error(e.getMessage());
//			}
		super.after();
	}

	
    
}

package com.athena.component.output;




import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class LjkckzTxtWriter extends  TxtDataWriter{
	protected static Logger logger = Logger.getLogger(LjkckzTxtWriter.class);	//定义日志方法
	public LjkckzTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	

	/**
	 * 生成文件头 
	 *  文件头格式固定：
	 *  	DEB  HERMES  PSA       SE0559420120927014525
	 * @throws IOException 
	 */
	@Override
	public void fileBefore(OutputStreamWriter writer) {
		String DateTime=DateTimeUtil.getDateTimeStr("yyyyMMddHHmmss");
        try {
			writer.write("DEB  HERMES  PAS       SE05594"+DateTime+"        ");
			writer.write("\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}	
		 super.fileBefore(writer);
	}
    

	/**
	 * 生成文件尾 
	 *  文件尾格式固定：
	 *  	FIN  HERMES  PSA       SE05594000000016
	 * @throws IOException 
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
        try {
			out.write("FIN  HERMES  PAS       SE05594"+totalToString()+"             ");
			
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
		super.after();
	}

    
   
    
	

	
	
}
   
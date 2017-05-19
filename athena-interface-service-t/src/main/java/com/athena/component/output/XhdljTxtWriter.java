package com.athena.component.output;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class XhdljTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(XhdljTxtWriter.class);	//定义日志方法

	public XhdljTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		
	}

	@Override
	public void fileBefore(OutputStreamWriter writer) {
		String BeginTime=DateTimeUtil.getDateTimeStr("yyyyMMdd HH:mm:ss");
		try {
			writer.write(""+BeginTime+" ATHENA—PDS INTERFACE04 BEGIN==>");
			writer.write("\n");
		} catch (IOException e) {
          logger.error(e.getMessage());
		}
		
		super.fileBefore(writer);
	}

	@Override
	public void fileAfter(OutputStreamWriter out) {
		String EndTime=DateTimeUtil.getDateTimeStr("yyyyMMdd HH:mm:ss");
        try {
			out.write(""+EndTime+" ATHENA—PDS INTERFACE04 INTERFACE04="+super.getTotal()+" END<==");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		super.fileAfter(out);
	}
	
	
	
	

}

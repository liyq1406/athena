package com.athena.component.input;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class ShhfhtzWriter extends DbDataWriter{

	public ShhfhtzWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	public boolean before(){
		try{
			String sql="delete from "+SpaceFinal.spacename_ckx+".IN_SHHFHTZ";
			super.execute(sql);
			super.commit();
			}catch(RuntimeException e)
			{
				logger.error(e.getMessage());
			}
		return super.before();
	}
	

    
    public  void afterRecord(int rowIndex, Record record,Object line) {
    	System.out.println(record.size()+"-----------------------");
    	super.afterRecord(rowIndex,record,line);
    }

}

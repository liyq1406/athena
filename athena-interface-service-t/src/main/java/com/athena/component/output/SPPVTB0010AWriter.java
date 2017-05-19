package com.athena.component.output;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class SPPVTB0010AWriter extends DbDataWriter {

	public SPPVTB0010AWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public boolean before() {
		String sql1 = "delete "+SpaceFinal.spacename_ck+".tb0010a";
		String sql2 = "delete "+SpaceFinal.spacename_ck+".tb0010b";
		String sql3 = "delete "+SpaceFinal.spacename_ck+".tb0010" ;
		
		super.execute(sql1);
		super.execute(sql2);
		super.execute(sql3);
		
		// TODO Auto-generated method stub
		return super.before();
	}
	
	
	

}

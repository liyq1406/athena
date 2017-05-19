package com.athena.component.output;

import java.io.IOException;
import java.io.OutputStreamWriter;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.txt.TxtDataWriter;

public class SPPVTB0010Writer extends TxtDataWriter {

	

	public SPPVTB0010Writer(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}

	
	
	
	@Override
	public void fileBefore(OutputStreamWriter writer) {
//		try {
//			writer.write("SCXH,AZPOSVEH,LJH,SL,\n");
//		} catch (IOException e) {
//			logger.info(e.getMessage());
//			// TODO Auto-generated catch block
//		}
		// TODO Auto-generated method stub
		super.fileBefore(writer);
	}




	@Override
	public boolean before() {
	
		StringBuffer buf = new StringBuffer();
		buf.append("") ;
		buf.append("INSERT INTO "+SpaceFinal.spacename_ck+".TB0010 ") ;
		buf.append("(USERCENTER, SCXH, AZPOSVEH, LJH, SL) ") ;
		buf.append("SELECT A.USERCENTER,A.SCXH, A.AZPOSVEH, B.PRODUCT AS LJH, SUM(B.SL) AS SL ") ;
		buf.append("FROM "+SpaceFinal.spacename_ck+".TB0010A A, "+SpaceFinal.spacename_ck+".TB0010B B ") ;
		buf.append("WHERE A.WHOF = B.WHOF ") ;
		buf.append("GROUP BY A.USERCENTER, A.SCXH, A.AZPOSVEH, B.PRODUCT ") ;
		
		super.execute(buf.toString());
		// TODO Auto-generated method stub
		return super.before();
	}
	
	
	

}

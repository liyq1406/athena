package com.athena.component.output;


import java.sql.Connection;
import java.util.Map;

import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.RowParser;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.db.DbDataReader;
import com.athena.component.exchange.db.DbRowParser;
import com.athena.component.runner.RunnerService;

public class ZcpcTxtWriter extends DbDataReader{
    private Connection conn=null;
	public ZcpcTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);	

	}
	@Override
	public void open(DataWriter dataWriter, RunnerService runnerService) {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		//获取数据库连接
		conn=dataParserConfig.getBaseDao().getSdcDataSource("1").getConnection();

		String sql="select zzzx,lingjbh,yxbh,ddrq,'' as filler,replace(to_char(shul, '9999999'),' ','0') " +
				"as shul,zfbj,hgbm,xianh,lyxt,usercenter from "+SpaceFinal.spacename_pcfj+".in_zcpc";
		
		readerConfig.setSql(sql);
		
	}

	

	
	
    
}

package com.athena.component.output;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class KanbxhgmDataWriter extends TxtDataWriter{

	public KanbxhgmDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	
	public void afterRecord(int rowIndex, Record record, Object line) {
		super.afterRecord(rowIndex, record, line);
		//更新此条状态
		Map rowObject = (Map)line;
		updateLine(rowObject);
	}
	
	private void updateLine(Map rowObject) {
		try{
			//更新准备层看板循环规模表，将已下发的循环规模覆盖当前循环规模
			String sql = "update "+SpaceFinal.spacename_xqjs+".xqjs_kanbxhgm t set t.dangqxhgm=t.xiafxhgm,t.SHENGXZT = '0' where t.xunhbm = ? and t.SHENGXZT <> '2' ";
			PreparedStatement st = interfaceConn.prepareStatement(sql);
			st.setString(1, (String)(rowObject.get("xunhbm")));		
			st.executeUpdate();
		}catch(SQLException e){
	         logger.error(e.getMessage());
		}	
		
	}

}

package com.athena.component.output;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class ZhouQddDataWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(DdmxDataWriter.class);	//定义日志方法
	public ZhouQddDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		Map rowObject = (Map)line;
		rowObject.put("dingdhTemp", "");
		super.afterRecord(rowIndex, record, line);
		
		//更新此条状态  hzg 2013-1-28修改
		updateLine(record.get("dingdhTemp").toString());
	}
	
	private void updateLine(String dingdh) {
		try{
			//更新订单，修改
//			String sql = createSQL();
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			st.setString(1, "5");
//			st.setString(2, dingdh);		
//			st.executeUpdate();
			String sql = createSQL(dingdh);
			super.execute(sql);
		}catch(Exception e){
	         logger.error(e.getMessage());
		}	
		
	}
	
	/**
	 * 创建更新订单语句
	 * @return
	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("update "+SpaceFinal.spacename_xqjs+".Xqjs_Dingd d set d.dingdzt = ? where d.dingdh = ?");
//		return sb.toString();
//	}
	
	private String createSQL(String dingdh) {
		StringBuffer sb = new StringBuffer();
		sb.append("update "+SpaceFinal.spacename_xqjs+".Xqjs_Dingd d set d.dingdzt = '5' where d.dingdh = '"+dingdh+"'");
		return sb.toString();
	}

}

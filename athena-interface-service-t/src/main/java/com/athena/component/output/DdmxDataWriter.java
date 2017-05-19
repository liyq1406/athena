package com.athena.component.output;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;


/**
 * 订单明细输出接口
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-25
 */
public class DdmxDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(DdmxDataWriter.class);	//定义日志方法
	protected String userCenter =  ""; //用户中心
	protected String lingjbh = ""; //零件号
	protected String lastH = ""; //显示DH0200
	protected Map tmp_rowObject = new HashMap();
	
	private PreparedStatement st;
	
	public DdmxDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		Map rowObject = (Map)line;
		String usercetner = rowObject.get("usercenter").toString();
		String tmp_lingjbh = rowObject.get("lingjbh").toString();
		if(userCenter.equals(usercetner)){
			rowObject.put("A", "");
			rowObject.put("B", "");
			rowObject.put("C", "");
			rowObject.put("D", "");
			rowObject.put("E", "");
			if(lingjbh.equals(tmp_lingjbh)){
				rowObject.put("F", "");
				rowObject.put("G", "");
				lastH = rowObject.get("H").toString();
				rowObject.put("H", "");
			} else{
				rowObject.put("H", lastH+"\r\n");
				rowObject.put("F", rowObject.get("F")+"\r\n");
				rowObject.put("G", rowObject.get("G")+"\r\n");
			}
		}else{
			if(!"".equals(lastH)){
				rowObject.put("J", lastH+"\r\n");
			}
			rowObject.put("A", rowObject.get("A")+"\r\n");
			rowObject.put("B", rowObject.get("B")+"\r\n");
			rowObject.put("C", rowObject.get("C")+"\r\n");
			rowObject.put("D", rowObject.get("D")+"\r\n");
			rowObject.put("E", rowObject.get("E")+"\r\n");
			rowObject.put("F", rowObject.get("F")+"\r\n");
			rowObject.put("G", rowObject.get("G")+"\r\n");
			rowObject.put("H", "");
		}
		userCenter = usercetner;
		lingjbh = tmp_lingjbh;
		tmp_rowObject.put("DDH", rowObject.get("DDH"));
		rowObject.put("usercenter", "");
		rowObject.put("lingjbh", "");
		rowObject.put("DDH", "");
		rowObject.put("JFYYDM", rowObject.get("JFYYDM")+"\r");
		
		super.afterRecord(rowIndex, record, line);
		
		//更新此条状态
		//Map rowObject = (Map)line;
		updateLine(tmp_rowObject);
	}



	/**
	 * 更新行信息
	 * @param rowObject
	 */
	@SuppressWarnings("rawtypes")
	private void updateLine(Map rowObject) {
		//更新订单
		updateDingdSQL((String)(rowObject.get("DDH")));
		//更新订单明细
		updateDingdmxSQL1((String)(rowObject.get("DDH")));
	}
	
	/**
	 * 创建更新订单语句
	 * @return
	 */
	private void updateDingdSQL(String dingdh) {
		StringBuffer sb = new StringBuffer();
		sb.append("update "+SpaceFinal.spacename_xqjs+".Xqjs_Dingd d set d.dingdzt = ? where d.dingdh = ?");
		try {
			st = interfaceConn.prepareStatement(sb.toString());
			st.setString(1, "5");
			st.setString(2, dingdh);		
			st.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 更新订单明细语句
	 * @return
	 */
	private void updateDingdmxSQL1(String dingdh) {
		StringBuffer sb = new StringBuffer();
		sb.append("update "+SpaceFinal.spacename_xqjs+".xqjs_dingdmx d set d.zhuangt = ? where d.dingdh = ?");
		try {
			st = interfaceConn.prepareStatement(sb.toString());
			st.setString(1, "5");
			st.setString(2, dingdh);		
			st.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 为了生成文件尾部
	 * @param out
	 */
	public void fileAfter(OutputStreamWriter out) {
		try {
			out.write(lastH+"\r\n");
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		super.fileAfter(out);
	}
	
	
	
	
}

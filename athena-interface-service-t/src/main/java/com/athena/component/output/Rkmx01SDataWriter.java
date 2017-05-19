package com.athena.component.output;

import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;

/**
 * 处理仓库明细表 操作码为O1S的结果集输出
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-19
 */
public class Rkmx01SDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(Rkmx01SDataWriter.class);	//定义日志方法

	public Rkmx01SDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 改变供应商类型的值  为内部供应商时，填1；否则空值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean beforeRecord(int rowIndex, Object rowObject) {
		//改变供应商类型的值  为内部供应商时，填1；否则空值
		//得到供应商类型
		String gongyslx = (String) ((Map)rowObject).get("GONGYSLX");
		if(gongyslx!=null){
			if(!"1".equals(gongyslx)){
				((Map)rowObject).remove("GONGYSLX");
				((Map)rowObject).put("GONGYSLX", " ");
			}
		}
		return super.beforeRecord(rowIndex, rowObject);
	}
	
	/**
	 * 输出处理仓库明细表 操作码为O3的结果集
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
		//关闭连接
		if(interfaceConn!=null){
			DbUtils.freeConnection(interfaceConn);
		}
		
		if(businessConn!=null){
			DbUtils.freeConnection(businessConn);
		}
		
		//输出处理仓库明细表 操作码为O3的结果集
		dataEchange.doExchange("CKMX_03",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
	}

}

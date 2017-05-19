package com.athena.component.output;

import java.io.OutputStreamWriter;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;

public class RkmxA39DataWriter extends TxtDataWriter {
	/**
	 * A39正常退货表
	 * @author chenlei
	 * @vesion 1.0
	 * @date 2012-4-23
	 */
	public RkmxA39DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 调用A53
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
//		setTotal(getTotal()+dataEchange.total) ;
		//输出处理仓库正常退库表 操作码为A53的结果集
		dataEchange.doExchange("2460_CKLSZ_ZCTK_A53",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
		
	}
}

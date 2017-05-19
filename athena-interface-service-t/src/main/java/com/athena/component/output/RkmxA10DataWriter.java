package com.athena.component.output;

import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * 处理返修单和退货单表 操作码为A10的结果集输出
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-19
 */
public class RkmxA10DataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxA10DataWriter.class);	//定义日志方法

	public RkmxA10DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 调用A25 纠纷单
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
		//输出处理仓库明细表 操作码为A25的结果集
		dataEchange.doExchange("CKMX_A25",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
	}

}

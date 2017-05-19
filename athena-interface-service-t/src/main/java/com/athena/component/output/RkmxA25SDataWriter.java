package com.athena.component.output;

import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * 处理A25S纠纷表
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-20
 */
public class RkmxA25SDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxA25SDataWriter.class);	//定义日志方法

	public RkmxA25SDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 调用A35S
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
		//输出处理仓库明细表 操作码为A35的结果集
		dataEchange.doExchange("CKMX_A35",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
	}
 
}

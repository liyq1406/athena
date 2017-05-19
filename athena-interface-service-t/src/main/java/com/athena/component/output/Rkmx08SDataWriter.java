package com.athena.component.output;

import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;

/**
 * 处理返修单和退货单表 操作码为O8S的结果集输出
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-19
 */
public class Rkmx08SDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(Rkmx08SDataWriter.class);	//定义日志方法

	public Rkmx08SDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 调用A10
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
		dataEchange.doExchange("CKMX_A10",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
	}

}

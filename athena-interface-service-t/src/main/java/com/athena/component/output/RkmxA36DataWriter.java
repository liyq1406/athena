package com.athena.component.output;

import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * A36备货单输出
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-20
 * lastModify  By 王冲, 2012-09-03 13:18  内容：处理A38
 */
public class RkmxA36DataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxA36DataWriter.class);	//定义日志方法

	public RkmxA36DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 调用A38
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
		//输出处理仓库明细表 操作码为A38的结果集
		dataEchange.doExchange("2460_CKLSZ_ZCTK_A38",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
		
	}
}

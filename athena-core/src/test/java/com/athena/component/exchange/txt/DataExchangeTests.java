package com.athena.component.exchange.txt;

import org.junit.Test;

import com.athena.component.exchange.DataExchange;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class DataExchangeTests  extends AbstractCompomentTests{
	
	@Inject
	private DataExchange dataExchange;
	
	
	/**
	 * 测试文本文件读取，数据库写入
	 */
	@Test
	public void testTxtReaderDbWriter(){
//		dataExchange.doExchange("txt_bn361");
		
		dataExchange.doExchange("db_bn361","");
	}
}

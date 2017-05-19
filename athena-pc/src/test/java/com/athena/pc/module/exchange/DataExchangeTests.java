package com.athena.pc.module.exchange;

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
//		dataExchange.doExchange("in_kdwld");
	}
}

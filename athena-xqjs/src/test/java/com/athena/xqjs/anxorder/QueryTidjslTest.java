package com.athena.xqjs.anxorder;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
@TestData(locations = {"classpath:testData/anxorder/QueryTidjslTest.xls"})
public class QueryTidjslTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	
	/**
	 * 查询替代件
	 * @throws Exception 
	 * **/
	@Test
	public void testQueryTidjsl() throws Exception{
		String usercenter = "UL" ;
		String cangk = "WMP" ;
		String lingjbh = "ZQ80571780" ;
		String riq = "2012-04-16" ;
		BigDecimal tidjsl = this.anxOrderService.queryTidjsl(cangk, usercenter, lingjbh, riq) ;
		assertEquals(tidjsl,new BigDecimal("500")) ;
	}
}

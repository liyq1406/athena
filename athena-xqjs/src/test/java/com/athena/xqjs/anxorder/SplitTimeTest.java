package com.athena.xqjs.anxorder;


import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
/**
 * 
 * @author 李智
 *
 */
@TestData(locations = {"classpath:testData/anxorder/ChushtjJs.xls"})
public class SplitTimeTest extends AbstractCompomentTests{
	@Inject
	private AnxOrderService anxOrderService;
	
	@Test
	public void splitTimeTest() throws Exception{
		anxOrderService.splitTime();
	}
	
}

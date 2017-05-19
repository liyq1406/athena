package com.athena.xqjs.kdorder;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.kdorder.service.KdOrderService;
import com.toft.core3.container.annotation.Inject;

public class DoKDCalculateTest extends AbstractCompomentTests{
	@Inject
	private KdOrderService kdorderservice;
	@Test
	@TestData(locations = {"classpath:testData/kdOrder/DoKDCalculateTest.xls"})
	public void testDoKDCalculateTest() throws Exception{
		String [] banc = {"112531"};
		this.kdorderservice.doKDCalculate("2012-02-13", "001", banc, "TEST1234","97X","123");
	}
}

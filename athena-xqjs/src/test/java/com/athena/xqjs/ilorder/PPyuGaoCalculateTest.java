package com.athena.xqjs.ilorder;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.YugzjbService;
import com.toft.core3.container.annotation.Inject;

public class PPyuGaoCalculateTest extends AbstractCompomentTests{

	@Inject
	private IlOrderService ilorderservice;
	@Inject
	private YugzjbService yugzjbservice;
	@Test
	@TestData(locations={"classpath:testData/ilorder/PPyuGaoCalculate.xls"})
	public void testPPyuGaoCalculate() throws Exception{
		
		BigDecimal yingyu = BigDecimal.ZERO;
//		ilorderservice.ppyuGaoCalculate("PP", yingyu);
//		ilorderservice.ppyuGaoCalculate("PS", yingyu);
//		ilorderservice.ppyuGaoCalculate("PJ", yingyu);
		List list = yugzjbservice.queryAllYugzjb();
		org.junit.Assert.assertEquals(list.size(), 24);
	}
}

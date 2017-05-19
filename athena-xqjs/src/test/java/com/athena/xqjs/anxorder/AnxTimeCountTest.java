package com.athena.xqjs.anxorder;

import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.athena.xqjs.module.anxorder.service.AnxjscszjbService;
import com.toft.core3.container.annotation.Inject;
@TestData(locations = {"classpath:testData/anxorder/AnxTimeCount.xls"})
public class AnxTimeCountTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	@Inject
	private AnxjscszjbService anxjscszjbService;
	
	/**
	 * 到货时间
	 * @throws Exception 
	 * **/
	@Test
	public void testAnxTimeCount() throws Exception{
		List<Anxjscszjb> list = this.anxjscszjbService.queryAnxjscszjbForTest() ;
		for (Anxjscszjb bean : list) {
			this.anxOrderService.anxTimeCount(bean, "CD", "SYS");
		}
	}
}

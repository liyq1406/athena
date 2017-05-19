package com.athena.xqjs.anxorder;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
@TestData(locations = {"classpath:testData/anxorder/DataReady.xls"})
public class AnxDataPreparationTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	
	/**
	 * 数据准备
	 * @throws Exception 
	 * **/
	@Test
	public void testAnxDataPreparation() throws Exception{
		this.anxOrderService.anxDataPreparation("SYS");
	}
}

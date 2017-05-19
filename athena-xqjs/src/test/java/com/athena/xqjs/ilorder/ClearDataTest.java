package com.athena.xqjs.ilorder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzjService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzjcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzsService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzscService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/ilOrder/cleanData.xls" })
public class ClearDataTest extends AbstractCompomentTests {

	@Inject
	private IlOrderService ilorderservice;

	@Inject
	private MaoxqhzpService maoxqhzpService;
	
	@Inject
	private MaoxqhzpcService maoxqhzpcService;

	@Inject
	private MaoxqhzjService maoxqhzjService;

	@Inject
	private MaoxqhzjcService maoxqhzjcService;

	@Inject
	private MaoxqhzsService maoxqhzsService;

	@Inject
	private MaoxqhzscService maoxqhzscService;

	@Test
	public void testClearData() {

		String[] array = { "pp", "ps", "pj" };

		for(String arr:array){
			this.ilorderservice.clearData(arr);
		}
		assertEquals(this.maoxqhzpService.select().size(), 0);
		assertEquals(this.maoxqhzpcService.selectAll().size(), 0);
		assertEquals(this.maoxqhzsService.selectAll().size(), 0);
		assertEquals(this.maoxqhzscService.select().size(), 0);
		assertEquals(this.maoxqhzjService.select().size(), 0);
		assertEquals(this.maoxqhzjcService.select().size(), 0);

	}

}

package com.athena.xqjs.ilorder;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/ilOrder/getOrderNumber.xls" })
public class GetOrderNumbersTest extends AbstractCompomentTests {

	@Inject
	private IlOrderService ilorderservice;

	@Test
	public void testGetNumber() {
		String[] array = { "pp", "pS", "pp", "pj", "ps" };
		String[] usercenter = { "UL", "UL", "UL", "UL", "UL" };
		String[] gongysdm = { "M105800000", "M105800000", "M105800003", "M105800001", "M105800002" };
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < array.length; i++) {
			String orderNumber = this.ilorderservice.getOrderNumber(array[i], usercenter[i], gongysdm[i],new HashMap());
			map.put(i, orderNumber);
		}
//		System.out.println(map.get(0));
//		System.out.println(map.get(1));
//		System.out.println(map.get(2));
//		System.out.println(map.get(3));
//		System.out.println(map.get(4));
//		assertEquals(map.get(0).toString(), "PA413001");
//		assertEquals(map.get(1).toString(), "PA413001");
//		assertEquals(map.get(2).toString(), "PA413001");
//		assertEquals(map.get(3).toString(), "PJA413001");
//		assertEquals(map.get(4).toString(), "PSA413001");

	}

}

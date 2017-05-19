package com.athena.xqjs.ilorder;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Maoxqhzjc;
import com.athena.xqjs.entity.ilorder.Maoxqhzpc;
import com.athena.xqjs.entity.ilorder.Maoxqhzsc;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzjcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzscService;
import com.toft.core3.container.annotation.Inject;

public class YuGaoCalculateTest extends AbstractCompomentTests{
	@Inject
	private MaoxqhzpcService maoxqhzpcservice;
	@Inject
	private MaoxqhzscService maoxqhzscservice;
	@Inject
	private MaoxqhzjcService maoxqhzjcservice;
	@Inject
	private IlOrderService ilorderservice;
	@Test
	@TestData(locations={"classpath:testData/ilorder/YuGaoCalculate.xls"})
	public void TestYuGaoCalculate() throws Exception{
		List<Maoxqhzpc> listP = maoxqhzpcservice.select();
		List<Maoxqhzsc> listS = maoxqhzscservice.select();
		List<Maoxqhzjc> listJ = maoxqhzjcservice.select();
		BigDecimal yingyu = BigDecimal.ZERO;
		Dingdlj dingdlj = new Dingdlj();
		Maoxqhzpc maoxqhzpc = new Maoxqhzpc();
		Maoxqhzsc maoxqhzsc = new Maoxqhzsc();
		Maoxqhzjc maoxqhzjc = new Maoxqhzjc();
		maoxqhzpc = listP.get(0);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzpc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(0).setScale(0));
		
		maoxqhzpc = listP.get(1);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzpc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(22.0).setScale(1));
		
		maoxqhzsc = listS.get(0);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzsc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(0).setScale(0));
		
		maoxqhzsc = listS.get(1);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzsc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(22.0).setScale(1));
		
		maoxqhzjc = listJ.get(0);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzjc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(0).setScale(0));
		
		maoxqhzjc = listJ.get(1);
		yingyu = ilorderservice.yuGaoCalculate(new BigDecimal(100), maoxqhzjc, yingyu, 0, dingdlj);
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(22.0).setScale(1));
	}
	
}

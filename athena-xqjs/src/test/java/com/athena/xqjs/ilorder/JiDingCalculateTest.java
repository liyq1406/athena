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

public class JiDingCalculateTest extends AbstractCompomentTests{
	@Inject
	private MaoxqhzpcService maoxqhzpcservice;
	@Inject
	private MaoxqhzscService maoxqhzscservice;
	@Inject
	private MaoxqhzjcService maoxqhzjcservice;
	@Inject
	private IlOrderService ilorderservice;
	
	@Test
	@TestData(locations={"classpath:testData/ilorder/jiDingCalculate.xls"})
	public void testJiDingCalculate() throws Exception{
		List<Maoxqhzpc> listP = maoxqhzpcservice.select();
		List<Maoxqhzsc> listS = maoxqhzscservice.select();
		List<Maoxqhzjc> listJ = maoxqhzjcservice.select();
		BigDecimal yingyu = BigDecimal.ZERO;
		Dingdlj dingdlj = new Dingdlj();
		
		for (Maoxqhzpc maoxqhzpc:listP){ 
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzpc, false,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(17.5));
		
		for (Maoxqhzpc maoxqhzpc:listP){ 
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzpc, true,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(17.5));
		
		for(Maoxqhzsc maoxqhzsc:listS){
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzsc, false,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(14.0).setScale(1));
		for(Maoxqhzsc maoxqhzsc:listS){
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzsc, true,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(14.0).setScale(1));
		for(Maoxqhzjc maoxqhzjc:listJ){
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzjc, false,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(19.0).setScale(1));
		for(Maoxqhzjc maoxqhzjc:listJ){
//			yingyu = ilorderservice.jiDingCalculate(new BigDecimal(100), maoxqhzjc, true,yingyu , 0,1, dingdlj,"97W",null);
		}
		org.junit.Assert.assertEquals(yingyu, new BigDecimal(19.0).setScale(1));
	}
}

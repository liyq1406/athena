package com.athena.xqjs.ilorder;




import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxqhzjfpxh;
import com.athena.xqjs.entity.ilorder.Maoxqhzpfpxh;
import com.athena.xqjs.entity.ilorder.Maoxqhzsfpxh;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzjfpxhService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpfpxhService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzsfpxhService;
import com.toft.core3.container.annotation.Inject;



public class MaoxqhzTest extends AbstractCompomentTests {
	@Inject
	private IlOrderService ilorderservice;
	
	@Inject 
	private MaoxqhzpfpxhService maoxqhzpfpxhservice;
	@Inject 
	private MaoxqhzsfpxhService maoxqhzsfpxhservice;
	@Inject 
	private MaoxqhzjfpxhService maoxqhzjfpxhservice;
	
	@Test
	@TestData(locations={"classpath:testData/ilorder/maoxqhz.xls"})
	public void testMaoxqhz(){
		
		this.ilorderservice.maoxqhzfpxh("PP");
		
		this.ilorderservice.maoxqhzfpxh("PS");
		
		this.ilorderservice.maoxqhzfpxh("PJ");
		
		List<Maoxqhzpfpxh> listP = maoxqhzpfpxhservice.select();
		for(Maoxqhzpfpxh  maoxqhzpfpxh : listP){
			org.junit.Assert.assertEquals(maoxqhzpfpxh.getUsercenter(), "UW");
			org.junit.Assert.assertEquals(maoxqhzpfpxh.getLingjbh(), "9673186980");
			org.junit.Assert.assertEquals(maoxqhzpfpxh.getFenpxh(), "W35F0");
			org.junit.Assert.assertEquals(maoxqhzpfpxh.getP0(), new BigDecimal(10));	
		}
		List<Maoxqhzsfpxh> listS = maoxqhzsfpxhservice.select();
		for(Maoxqhzsfpxh  maoxqhzsfpxh : listS){
			org.junit.Assert.assertEquals(maoxqhzsfpxh.getUsercenter(), "UW");
			org.junit.Assert.assertEquals(maoxqhzsfpxh.getLujbh(), "WI00000027");
			org.junit.Assert.assertEquals(maoxqhzsfpxh.getFenpxh(), "W35F0");
			org.junit.Assert.assertEquals(maoxqhzsfpxh.getS0(), new BigDecimal(10));	
		}
		List<Maoxqhzjfpxh> listJ = maoxqhzjfpxhservice.select();
		for(Maoxqhzjfpxh  maoxqhzjfpxh : listJ){
			org.junit.Assert.assertEquals(maoxqhzjfpxh.getUsercenter(), "UW");
			org.junit.Assert.assertEquals(maoxqhzjfpxh.getLujbh(), "WI00000027");
			org.junit.Assert.assertEquals(maoxqhzjfpxh.getFenpxh(), "W35F0");
			org.junit.Assert.assertEquals(maoxqhzjfpxh.getJ0(), new BigDecimal(10));	
		}
		
	}
	
	
 
}

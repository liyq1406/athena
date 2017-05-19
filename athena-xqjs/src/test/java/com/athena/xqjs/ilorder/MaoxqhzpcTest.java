package com.athena.xqjs.ilorder;

import java.text.ParseException;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzjcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzscService;
import com.toft.core3.container.annotation.Inject;

public class MaoxqhzpcTest extends AbstractCompomentTests {

	@Inject
	private IlOrderService ilorderservice;
	@Inject
	private MaoxqhzpcService  maoxqhzpcservice;
	@SuppressWarnings("unused")
	@Inject
	private MaoxqhzscService  maoxqhzscservice;
	@Inject
	private MaoxqhzjcService  maoxqhzjcservice;			
	@Test
	@TestData(locations={"classpath:testData/ilorder/maoxqhzc.xls"})
	public void testMaoxqhzc() throws ParseException{
//		this.ilorderservice.maoxqhzglckx("2012-2-13", "PP");
//		List<Maoxqhzpc> listP = this.maoxqhzpcservice.select();
//		for(Maoxqhzpc maoxqhzpc:listP){
//			org.junit.Assert.assertEquals(maoxqhzpc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzpc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzpc.getLingjbh(), "9673186980");
//			org.junit.Assert.assertEquals(maoxqhzpc.getZhizlx(), "97W");
//		}
//		this.ilorderservice.maoxqhzglckx("2012-2-13", "PS");
//		List<Maoxqhzsc> listS = this.maoxqhzscservice.select();
//		for(Maoxqhzsc maoxqhzsc:listS){
//			org.junit.Assert.assertEquals(maoxqhzsc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzsc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzsc.getLingjbh(), "9673186980");
//			org.junit.Assert.assertEquals(maoxqhzsc.getZhizlx(), "97W");
//		}
//		this.ilorderservice.maoxqhzglckx("2012-2-13", "PJ");
//		List<Maoxqhzjc> listJ = this.maoxqhzjcservice.select();
//		for(Maoxqhzjc maoxqhzjc:listJ){
//			org.junit.Assert.assertEquals(maoxqhzjc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzjc.getUsercenter(), "UW");
//			org.junit.Assert.assertEquals(maoxqhzjc.getLingjbh(), "9673186980");
//			org.junit.Assert.assertEquals(maoxqhzjc.getZhizlx(), "97W");
//		}
	}
	
}

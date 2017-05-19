package com.athena.xqjs.ilorder;

import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpcService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/ilorder/check.xls" })
public class CheckTest  extends AbstractCompomentTests{
	
	@Inject
	private MaoxqhzpcService maoxqhzpcservice;
	@Inject
	private YicbjService yicbjservice;

	@Test
	public void testCheck(){
		maoxqhzpcservice.checkAll("PP");
		maoxqhzpcservice.checkAll("PS");
		maoxqhzpcservice.checkAll("PJ");
		List<Yicbj> list = yicbjservice.select();
		org.junit.Assert.assertEquals(list.size(), 8);
		org.junit.Assert.assertEquals(list.get(0).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(0).getLingjbh(), "9673186980");
		org.junit.Assert.assertEquals(list.get(0).getCuowlx(), "100");
		org.junit.Assert.assertEquals(list.get(1).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(1).getLingjbh(), "9673186982");
		org.junit.Assert.assertEquals(list.get(1).getCuowlx(), "200");
		
		
		org.junit.Assert.assertEquals(list.get(2).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(2).getLingjbh(), "9673186983");
		org.junit.Assert.assertEquals(list.get(2).getCuowlx(), "100");
		org.junit.Assert.assertEquals(list.get(3).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(3).getLingjbh(), "9673186984");
		org.junit.Assert.assertEquals(list.get(3).getCuowlx(), "200");
		org.junit.Assert.assertEquals(list.get(4).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(4).getLingjbh(), "9673186985");
		org.junit.Assert.assertEquals(list.get(4).getCuowlx(), "200");
		
		
		org.junit.Assert.assertEquals(list.get(5).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(5).getLingjbh(), "9673186986");
		org.junit.Assert.assertEquals(list.get(5).getCuowlx(), "100");
		org.junit.Assert.assertEquals(list.get(6).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(6).getLingjbh(), "9673186987");
		org.junit.Assert.assertEquals(list.get(6).getCuowlx(), "200");
		org.junit.Assert.assertEquals(list.get(7).getJismk(), "31");
		org.junit.Assert.assertEquals(list.get(7).getLingjbh(), "9673186988");
		org.junit.Assert.assertEquals(list.get(7).getCuowlx(), "200");
	}
}

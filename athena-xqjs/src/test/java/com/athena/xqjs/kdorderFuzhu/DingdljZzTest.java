package com.athena.xqjs.kdorderFuzhu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.IlYaohlService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testData/kdOrder/DingdljGzDb.xls"})
public class DingdljZzTest extends AbstractCompomentTests{
	@Inject
	private DingdljService dingdljService ;
	@Inject
	private IlYaohlService ilYaohlService;
	
	public void initDb() {
		
	}
	
	@Test
	public void queryDingdljByParamTest() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("dingdh", "101P6200");
		map.put("gongysdm", "M90010010");
		map.put("jihyz", "W01");
		map.put("usercenter", "UW");
		map.put("lingjbh", "9666800080");
		map.put("searchSymbols", "3");
		map.put("jfbl", new BigDecimal(0.2).toString());
		map.put("dingdzt", Const.DINGD_STATUS_YSX);
		Map resultMap 
			= dingdljService.queryDingdljByParam(new Dingdlj(),map);
		
		List<Dingdlj> list = (ArrayList<Dingdlj>)resultMap.get("rows");
//		Dingdlj dingdlj = list.get(0);
//		Dingdlj assertDingdlj = new Dingdlj();
//		assertDingdlj.setDingdh("101P6200");
//		assertDingdlj.setDingdljddlj(new BigDecimal(100));
//		assertDingdlj.setDingdzt("2");
//		assertDingdlj.setDingdzzsj("2011-09-10");
//		assertDingdlj.setGongysdm("M90010010");
//		assertDingdlj.setId("201");
//		assertDingdlj.setJfbl(new BigDecimal(0.3));
//		assertDingdlj.setJiaofljddlj(new BigDecimal(30));
//		assertDingdlj.setJihyz("W01");
//		assertDingdlj.setLingjbh("9666800080");
//		assertDingdlj.setUsercenter("UW");
//		assertDingdlj.setYaohlgsljyjf(3);
//		assertDingdlj.setYaohlgsljyzz(0);
//		assertDingdlj.setYaohlgszl(10);
//		assertDingdlj.setZhongwmc("零件0");
//		assertDingdlj.setZhongzljddlj(BigDecimal.ZERO);
		
		org.junit.Assert.assertEquals(1, list.size());
	}
	@Test
	public void queryAllowGzslTest() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("dingdh", "101P6200");
		map.put("usercenter", "UW");
		map.put("lingjbh", "9666800080");
		map.put("yaohlzt", Const.YAOHL_BIAOD);
		double d = dingdljService.queryAllowZzsl(map);
		
		
		org.junit.Assert.assertEquals(new Double(50) , new Double(d));
	}
	
	@Test
	public void updateYaohlZtTest() {
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("dingdh", "101P6200");
//		map.put("usercenter", "UW");
//		map.put("lingjbh", "9666800080");
		
		Yaohl yaohl = new Yaohl();
		yaohl.setYaohlzt("05");
		yaohl.setDingdh("101P6200");
		yaohl.setUsercenter("UW");
		yaohl.setLingjbh("9666800080");
		
		String s = ilYaohlService.updateYaohlZt(yaohl);
		org.junit.Assert.assertEquals("", s);
	}
	
	@Test
	public void dingdljgzTest() {
		List<Dingdlj> Dingdljs = new ArrayList<Dingdlj>();
		Dingdlj lj = new Dingdlj();
		lj.setDingdh("101P6200");
		lj.setUsercenter("UW");
		lj.setLingjbh("9666800080");
		Dingdljs.add(lj);
		lj = new Dingdlj();
		lj.setDingdh("101P6202");
		lj.setUsercenter("UW");
		lj.setLingjbh("9666800080");
		Dingdljs.add(lj);
		
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("test");
		
		List l = dingdljService.dingdljZz(Dingdljs, loginUser);
		org.junit.Assert.assertEquals(2, l.size());
	}
}

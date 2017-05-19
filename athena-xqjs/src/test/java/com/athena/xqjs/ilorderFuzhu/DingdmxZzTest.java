package com.athena.xqjs.ilorderFuzhu;

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
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testData/ilOrder/DingdljGzDb.xls"})
public class DingdmxZzTest extends AbstractCompomentTests{
	@Inject
	private DingdmxService dingdmxService ;
	
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
			= dingdmxService.queryDingdmxByParam(new Dingdmx(),map);
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
		map.put("dingdmxid", "202");
		map.put("yaohlzt", Const.YAOHL_BIAOD);
		double d = dingdmxService.queryMxAllowZzsl(map);
		
		org.junit.Assert.assertEquals(new Double(50) , new Double(d));
	}
	
	@Test
	public void dingdljgzTest() {
		List<Dingdmx> Dingdmxs = new ArrayList<Dingdmx>();
		Dingdmx mx = new Dingdmx();
		mx.setDingdh("101P6200");
		mx.setUsercenter("UW");
		mx.setLingjbh("9666800080");
		Dingdmxs.add(mx);
		mx = new Dingdmx();
		mx.setDingdh("101P6202");
		mx.setUsercenter("UW");
		mx.setLingjbh("9666800080");
		Dingdmxs.add(mx);
		
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("test");
		
		List l = dingdmxService.dingdmxZz(Dingdmxs, loginUser);
		org.junit.Assert.assertEquals(2, l.size());
	}
}

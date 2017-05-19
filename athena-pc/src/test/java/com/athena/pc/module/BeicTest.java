package com.athena.pc.module;


import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.entity.Beic;
import com.athena.pc.module.service.BeicService;
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.YueMnService;
import com.athena.pc.module.webInterface.PCDailyProduceService;
import com.athena.pc.module.webInterface.PCLeijjfService;
import com.toft.core3.container.annotation.Inject;

//@TestData(locations = {"classpath:testdata/pc/zbc_test.xls"})
public class BeicTest extends AbstractCompomentTests {

	@Inject
	private BeicService beicService;

	@Inject
	private YueMnService yueMnService; 

	@Inject
	private DailyRollService dailyRollService; 
	@Inject
	private PCLeijjfService pCLeijjfService; 
	
	@Inject 
	private PCDailyProduceService pCDailyProduceService;
	
	private Map<String,String> params;
	/**
	 * 测试插入一条备储计划
	 * @author gswang
	 * @date 2011-12-01
	 */
//	@Test
	public void testSaveBeic(){
//		Beic beic = new Beic();
//		beic.setBeicjhh("BC1201111");
//		beic.setBeicjhmxh("BCMX120111101");
//		beic.setKaissj("2012-01-01");
//		beic.setJiessj("2012-01-10");
//		beic.setLingjbh("LJ110");
//		beic.setLingjsl(new BigDecimal("200"));
//		beic.setUsercenter("UW");
//		ArrayList<Beic> insertList = new ArrayList<Beic>();
//		ArrayList<Beic> editList = new ArrayList<Beic>();
//		ArrayList<Beic> deleteList = new ArrayList<Beic>();
//		String username = "root";
//		insertList.add(beic);
//		editList.add(beic);
//		deleteList.add(beic);
//		Integer operate = new Integer(1);
//		beicService.batchDelete(beic);
//		String result = beicService.saveBeic(beic,operate,insertList,editList,deleteList,username);
//		assertEquals(result, "success"); 
	}
	
	@Test
	@TestData(locations = {"classpath:testdata/pc/zxc_test.xls"})		
	public void testTemp(){
//		@TestData(locations = {"classpath:testdata/pc/zbc_test.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zxc_test.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zbc_test1.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zxc_test1.xls"})	
//		@TestData(locations = {"classpath:testdata/pc/zbc_test2.xls"})
//		params = new HashMap<String,String>();
//		params.put("USERCENTER", "UX");
//		params.put("today", "2012-10-20");	
//		params.put("Dingdlx", "PJ");
////		params.put("Dingdlx", "PP");
////		yueMnService.updateDingd(params);
//		pCLeijjfService.callPcSchedule("");
//		pCDailyProduceService.callPcDailyProduce("");
	}

//	@Test
//	@TestData(locations = {"classpath:testdata/pc/zbc_test042502.xls"})		
	public void testTempTemp(){
//		@TestData(locations = {"classpath:testdata/pc/zbc_test.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zxc_test.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zbc_test1.xls"})
//		@TestData(locations = {"classpath:testdata/pc/zxc_test1.xls"})	
//		@TestData(locations = {"classpath:testdata/pc/zbc_test2.xls"})
//		params = new HashMap<String,String>();
//		params.put("USERCENTER", "UX");
//		params.put("today", "2012-10-20");	
//		params.put("Dingdlx", "PJ");
////		params.put("Dingdlx", "PP");
////		yueMnService.updateDingd(params);
//		pCLeijjfService.callPcSchedule("");
//		pCDailyProduceService.callPcDailyProduce("");
	}
	
	@Test
	public void testMain(){
		String name = "PEISLX,PEISLXMC,BAOZLX,BAOZSL,ZUICDDCWS,TONGBJPBS,SHANGXD,PEITSXBS,BEIHTQQ,XIAOHCCXC,SHIFGJ,BEIZ,SHIFBHD,CANGKBH,BIAOS,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME,ZICKBH,WLGYY,USERCENTER";
		String nameOne[] = name.split(",");
		StringBuffer re = new StringBuffer();
		for(String n : nameOne){
			re.append("c.").append(n.trim()).append("=").append("s.").append(n.trim()).append(",");
		}
		System.out.println(re.toString());
	}
}

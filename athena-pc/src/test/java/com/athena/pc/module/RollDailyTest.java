package com.athena.pc.module;

import static org.junit.Assert.assertEquals;


import com.athena.db.ConstantDbCode;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData; 
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.YueMnService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
    @TestData(locations = {"classpath:testdata/pc/clearData.xls"})
public class RollDailyTest extends AbstractCompomentTests {
	@Inject
	private YueMnService yueMnService; 

	@Inject
	private DailyRollService dailyRollService; 
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	private Map<String,String> params;
	private Map<String,String> dateRange;
	private List<Map<String,String>> shengxList;
	private List<Map<String,String>> qckc;
	
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
    		Map<String,String> shengx1 = new HashMap<String,String>();
    		dateRange = new HashMap<String,String>();
    		shengxList = new ArrayList<Map<String,String>>();
    		shengx1.put("SHENGCXBH", "UW5L1"); 
    		shengxList.add(shengx1);
    		Map<String,String> shengx2 = new HashMap<String,String>();
    		shengx2.put("SHENGCXBH", "UW5L2"); 
    		shengxList.add(shengx2);
    		Map<String,String> shengx3 = new HashMap<String,String>();
    		shengx3.put("SHENGCXBH", "UW5L3"); 
    		shengxList.add(shengx3);
    		params = new HashMap<String,String>();
    		params.put("kaissj", "2012-03-05");
    		params.put("jiessj", "2012-04-01");
    		params.put("nextjiessj", "2012-04-29");
    		params.put("shengcx", "'UW5L1','UW5L2','UW5L3'");
    		dateRange.put("MINSJ", "2012-01-20");
    		dateRange.put("MAXSJ", "2012-04-20");
    		params.put("biaos", "Y");
    		params.put("TIQQ", "2"); 
    		params.put("jihybh", "root");
    		params.put("USERCENTER", "UW");
    		params.put("MINTIME", "0.25");
    		params.put("ZENGCTS", "3");
    		params.put("chanxzbh", "UW5L");		
    		params.put("today", "2012-02-27");	
    		params.put("period", "201203");
    		
//    		params.put("GUND", "G"); 
//    		params.put("today", "2012-03-05");
        };   
    }; 
    
//    @Test
	@TestData(locations = {"classpath:testdata/pc/CalendarGDNew.xls"})
	public void testcalPC(){ 
//		params.put("biaos", "R");
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
	}
    
//    @Test
	@TestData(locations = {"classpath:testdata/pc/CalendarGDNewBanc.xls"})
	public void testcalPCParseBanc(){ 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
	}
	
	/**
	 * 测试拆分GEVP外部要货令接口表中的数据到日滚动排产中
	 * 零件：LJ002，       客户编号：KH00000001，订单提前期：1天                   
	 * GEVP外部要货令表，要货令号：YHL003，要货数量：150，交付时间：2012-03-08  
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @Test
	@TestData(locations = {"classpath:testdata/pc/ParseGEVPMaoxq.xls"})
	public void testParseMaoxqGEVP(){ 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		
		final List<Map<String,String>> shengxList = dailyRollService.getChangxList(params);
		dailyRollService.initParam(shengxList,params);
		dailyRollService.cleanInitData(params); 
		dailyRollService.parseRiMaoxq(shengxList, params);
//		dailyRollService.parseMaoxqGEVP(shengxList, params);
		params.put("RIQ", "2012-03-07");
		params.put("LINGJBH", "LJ002");
		
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseRiMaoxq",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "60"); 
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L2"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "50"); 
		assertEquals(String.valueOf(result.get(1).get("CHANXH")), "UW5L1"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "41"); 
		assertEquals(String.valueOf(result.get(2).get("CHANXH")), "UW5L3"); 
	}
	
	/**
	 * 测试解析PJ订单的毛需求，从订单明细表中选取最新的订单号，根据订单号从订单零件表中选择此订单号的预告
	 * 零件：LJ002，       客户编号：W02，订单提前期：2天          最新订单号：JC305         
	 * 订单零件表，订单号：JC304，P1数量：100，P1日期：2012-03-08
	 * 订单零件表，订单号：JC305，P1数量：130，P1日期：2012-03-08   
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/ParseRiMaoxq.xls"})
	public void testParseRiMaoxq(){ 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		
		final List<Map<String,String>> shengxList = dailyRollService.getChangxList(params);
		dailyRollService.initParam(shengxList,params);
		dailyRollService.cleanInitData(params); 
		dailyRollService.parseRiMaoxq(shengxList, params);
		params.put("RIQ", "2012-03-06");
		params.put("LINGJBH", "LJ002");
		
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseRiMaoxq",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "52"); 
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L2"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "43"); 
		assertEquals(String.valueOf(result.get(1).get("CHANXH")), "UW5L1"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "36"); 
		assertEquals(String.valueOf(result.get(2).get("CHANXH")), "UW5L3"); 
	}
    
	/**
	 * 测试当计算当天在之前已经计算并属于封闭期，那当天不重新排产
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @Test
	@TestData(locations = {"classpath:testdata/pc/BlockDailyXQ.xls"})
	public void testCalBlockDailyXQ(){ 
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
		
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("kaissj", "2012-03-05");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		params.put("LINGJBH", "LJ003");
		params.put("CHANXH", "UW5L1");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalBlockDailyXQ",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L1");
		
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "83"); 
		assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-03-05");
		
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "101"); 
		assertEquals(String.valueOf(result.get(1).get("SHIJ")), "2012-03-06");
		
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "20"); 
		assertEquals(String.valueOf(result.get(2).get("SHIJ")), "2012-03-07");
		
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "67"); 
		assertEquals(String.valueOf(result.get(3).get("SHIJ")), "2012-03-08");
		
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "37"); 
		assertEquals(String.valueOf(result.get(4).get("SHIJ")), "2012-03-09");

	}
    
	/**
	 * 测试当计算当天在之前已经计算并属于封闭期，那当天不重新排产
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @Test
	@TestData(locations = {"classpath:testdata/pc/BlockDailyXQTwo.xls"})
	public void testCalBlockDailyXQTwo(){ 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("kaissj", "2012-03-06");
		params.put("today", "2012-03-06");
		dailyRollService.calPC(params,qckc); 
		
		params.put("LINGJBH", "LJ003");
		params.put("CHANXH", "UW5L1");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalBlockDailyXQ",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L1");
		
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "101"); 
		assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-03-06");
		
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "54"); 
		assertEquals(String.valueOf(result.get(1).get("SHIJ")), "2012-03-07");
		
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "54"); 
		assertEquals(String.valueOf(result.get(2).get("SHIJ")), "2012-03-08");
		
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "54"); 
		assertEquals(String.valueOf(result.get(3).get("SHIJ")), "2012-03-09");
		
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "37"); 
		assertEquals(String.valueOf(result.get(4).get("SHIJ")), "2012-03-12");
	}
    
	/**
	 * 测试当生产线预计排产量小于月模拟每日确保产能时进行增产，当预计排产量大于月模拟每日确保产能时，取预计排产量进行排产。
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/Yujipcl.xls"})
	public void testYujipcl(){ 
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
		
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		params.put("riq", "2012-03-05");
		params.put("CHANXH", "UW5L2");
		params.put("biaoshi", "Y");
		List<Map<String,String>> meirqbcl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryMeirqbcl",params);
		assertEquals(String.valueOf(meirqbcl.get(0).get("CHANXH")), "UW5L2");
		
		assertEquals(String.valueOf(meirqbcl.get(0).get("LINGJSL")), "278"); 
		assertEquals(String.valueOf(meirqbcl.get(0).get("SHIJ")), "2012-03-05");
		
		assertEquals(String.valueOf(meirqbcl.get(1).get("LINGJSL")), "270"); 
		assertEquals(String.valueOf(meirqbcl.get(1).get("SHIJ")), "2012-03-06");
		
		assertEquals(String.valueOf(meirqbcl.get(2).get("LINGJSL")), "284"); 
		assertEquals(String.valueOf(meirqbcl.get(2).get("SHIJ")), "2012-03-07");
		
		assertEquals(String.valueOf(meirqbcl.get(3).get("LINGJSL")), "242"); 
		assertEquals(String.valueOf(meirqbcl.get(3).get("SHIJ")), "2012-03-08");
		
		assertEquals(String.valueOf(meirqbcl.get(4).get("LINGJSL")), "264"); 
		assertEquals(String.valueOf(meirqbcl.get(4).get("SHIJ")), "2012-03-09");
		
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryRIGDPCJHMX",params);
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L2");
		
		assertEquals(String.valueOf(result.get(0).get("YUJPCL")), "272"); 
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "278"); 
		assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-03-05");
		
		assertEquals(String.valueOf(result.get(1).get("YUJPCL")), "190"); 
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "270"); 
		assertEquals(String.valueOf(result.get(1).get("SHIJ")), "2012-03-06");
		
		assertEquals(String.valueOf(result.get(2).get("YUJPCL")), "148"); 
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "284"); 
		assertEquals(String.valueOf(result.get(2).get("SHIJ")), "2012-03-07");
		
		assertEquals(String.valueOf(result.get(3).get("YUJPCL")), "130"); 
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "242"); 
		assertEquals(String.valueOf(result.get(3).get("SHIJ")), "2012-03-08");
		
		assertEquals(String.valueOf(result.get(4).get("YUJPCL")), "136"); 
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "258"); 
		assertEquals(String.valueOf(result.get(4).get("SHIJ")), "2012-03-09");
	}
    
	/**
	 * 将生产线当天的生产计划分配到每个班时，如果当天生产线没有对应的班生产，将不分配，并且将此信息记录在排产消息表中提示给用户。
	 * @author 王国首	
	 * @date 2012-03-21
	 ****/
    @Test
	@TestData(locations = {"classpath:testdata/pc/nullBan.xls"})
	public void testNullBan(){ 
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
		
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		params.put("RIQ", "2012-03-06");
		params.put("CHANXH", "UW5L2");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryNullBan",params);
		assertEquals(String.valueOf(result.size()), "0"); 
		params.put("RIQ", "2012-03-06");
		params.put("CHANXH", "UW5L1");
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryNullBan",params);
		assertEquals(String.valueOf(result.size()), "5");
	}
    
    @Test
	@TestData(locations = {"classpath:testdata/pc/ParseBanc.xls"})
	public void testParseBanc(){ 
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
    	
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		params.put("RIQ", "2012-03-05");
		params.put("CHANXH", "UW5L2");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseBancRIGDPCJHMX",params);
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L2");
		
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(0).get("BANC")), "B");
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "20"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(1).get("BANC")), "C");
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "20"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ005"); 
		assertEquals(String.valueOf(result.get(2).get("BANC")), "C");
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "59"); 
		
		assertEquals(String.valueOf(result.get(3).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(result.get(3).get("BANC")), "C");
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "69"); 
		
		assertEquals(String.valueOf(result.get(4).get("LINGJBH")), "LJ004"); 
		assertEquals(String.valueOf(result.get(4).get("BANC")), "C");
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "123"); 
		
		
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseBanc",params);
		assertEquals(String.valueOf(result.get(0).get("CHANXH")), "UW5L2");
		
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(0).get("BAN")), "B");
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "20"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(result.get(1).get("BAN")), "B");
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "50"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ004"); 
		assertEquals(String.valueOf(result.get(2).get("BAN")), "B");
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "28"); 
		
		assertEquals(String.valueOf(result.get(3).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(result.get(3).get("BAN")), "C");
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "20"); 
		
		assertEquals(String.valueOf(result.get(4).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(result.get(4).get("BAN")), "C");
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "19"); 
		
		assertEquals(String.valueOf(result.get(5).get("LINGJBH")), "LJ005"); 
		assertEquals(String.valueOf(result.get(5).get("BAN")), "C");
		assertEquals(String.valueOf(result.get(5).get("LINGJSL")), "59"); 
		
		assertEquals(String.valueOf(result.get(6).get("LINGJBH")), "LJ004"); 
		assertEquals(String.valueOf(result.get(6).get("BAN")), "D");
		assertEquals(String.valueOf(result.get(6).get("LINGJSL")), "95"); 
	}

}

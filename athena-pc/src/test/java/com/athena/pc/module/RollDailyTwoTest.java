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

import com.athena.component.service.Message;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData; 
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.PCRunTimeException;
import com.athena.pc.module.service.YueMnService;
import com.athena.pc.module.webInterface.PCDailyRollService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
//    @TestData(locations = {"classpath:testdata/pc/clearData.xls"})
public class RollDailyTwoTest extends AbstractCompomentTests {
	@Inject
	private YueMnService yueMnService; 

	@Inject
	private DailyRollService dailyRollService; 
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	@Inject
	private PCDailyRollService pCDailyRollService;
	
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
    		params.put("kaissj", "2012-07-02");
    		params.put("jiessj", "2012-07-29");
    		params.put("nextjiessj", "2012-09-02");
    		params.put("shengcx", "'UW5L1','UW5L2','UW5L3'");
    		dateRange.put("MINSJ", "2012-06-01");
    		dateRange.put("MAXSJ", "2012-09-20");
    		params.put("biaos", "Y");
    		params.put("TIQQ", "2"); 
    		params.put("jihybh", "root");
    		params.put("USERCENTER", "UW");
    		params.put("MINTIME", "0.25");
    		params.put("ZENGCTS", "3");
    		params.put("chanxzbh", "UW5L");		
    		params.put("today", "2012-06-21");	
    		params.put("period", "201207");
    		
//    		params.put("GUND", "G"); 
//    		params.put("today", "2012-03-05");
        };   
    }; 
    
    @Test
//	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
//	@TestData(locations = {"classpath:testdata/pc/junhengpcwangc.xls"})
	public void testcalPC(){ 
//		params.put("biaos", "R");
		params.put("today", "2012-06-26");
		yueMnService.calPC(params,qckc); 
	}

//	@Test
//	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
//    @TestData(locations = {"classpath:testdata/pc/junhengpcwangc.xls"})
	public void testcalPCRi(){ 
		params.put("biaos", "R");
		params.put("today", "2012-07-02");
		dailyRollService.calPC(params,qckc); 
	}
	
//	@Test
//	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
//    @TestData(locations = {"classpath:testdata/pc/junhengpcwangc.xls"})
	public void testcalPCGUN(){ 
		params.put("biaos", "G");
		params.put("today", "2012-07-02");
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
	 * 测试拆分外部订单预告接口表中的数据到日滚动排产中
	 * 零件：LJ001，      订单提前期：1天                   
	 * 外部订单预告表，零件编号：LJ001，数量：200，订单日期：2012-3-9  ，来源：W01
	 * 零件编号：LJ001，数量：250，订单日期：2012-4-5  ，来源：W01
	 * 注意：来源w01必须在客户成品库中设置对应的仓库编号，不然查询不到对应的订单提前期
	 * @author 王国首	
	 * @date 2012-03-30
	 ****/
    @SuppressWarnings("unchecked")
//	@Test
	@TestData(locations = {"classpath:testdata/pc/Wbddyg.xls"})
	public void testParseMaoxqWbddyg(){ 
    	params.put("today", "2012-03-06");
		params.put("kaissj", "2012-03-06");
		
		final List<Map<String,String>> shengxList = dailyRollService.getChangxList(params);
		yueMnService.initParam(shengxList,params);
		yueMnService.cleanInitData(params); 
		yueMnService.parseMaoxqWbddyg(shengxList,params);
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-09");
		
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseWbddyg",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "13"); 
		assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-03-05"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "20"); 
		assertEquals(String.valueOf(result.get(1).get("SHIJ")), "2012-03-05"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "18"); 
		assertEquals(String.valueOf(result.get(2).get("SHIJ")), "2012-03-05"); 
		
		assertEquals(String.valueOf(result.get(3).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "13"); 
		assertEquals(String.valueOf(result.get(3).get("SHIJ")), "2012-03-06"); 
		
		assertEquals(String.valueOf(result.get(4).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "20"); 
		assertEquals(String.valueOf(result.get(4).get("SHIJ")), "2012-03-06"); 
		
		assertEquals(String.valueOf(result.get(5).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(5).get("LINGJSL")), "18"); 
		assertEquals(String.valueOf(result.get(5).get("SHIJ")), "2012-03-06"); 
		
		assertEquals(String.valueOf(result.get(6).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(6).get("LINGJSL")), "12"); 
		assertEquals(String.valueOf(result.get(6).get("SHIJ")), "2012-03-07"); 
		
		assertEquals(String.valueOf(result.get(7).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(7).get("LINGJSL")), "20"); 
		assertEquals(String.valueOf(result.get(7).get("SHIJ")), "2012-03-07"); 
		
		assertEquals(String.valueOf(result.get(8).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(8).get("LINGJSL")), "17"); 
		assertEquals(String.valueOf(result.get(8).get("SHIJ")), "2012-03-07"); 
		
		assertEquals(String.valueOf(result.get(9).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(9).get("LINGJSL")), "12"); 
		assertEquals(String.valueOf(result.get(9).get("SHIJ")), "2012-03-08"); 
		
		assertEquals(String.valueOf(result.get(10).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(10).get("LINGJSL")), "20"); 
		assertEquals(String.valueOf(result.get(10).get("SHIJ")), "2012-03-08"); 
		
		assertEquals(String.valueOf(result.get(11).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(11).get("LINGJSL")), "17"); 
		assertEquals(String.valueOf(result.get(11).get("SHIJ")), "2012-03-08"); 
		
		params.put("kaissj", "2012-04-02");
		params.put("jiessj", "2012-04-09");
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseWbddyg",params);
		assertEquals(String.valueOf(result.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(0).get("LINGJSL")), "21"); 
		assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-04-02"); 
		
		assertEquals(String.valueOf(result.get(1).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(1).get("LINGJSL")), "34"); 
		assertEquals(String.valueOf(result.get(1).get("SHIJ")), "2012-04-02"); 
		
		assertEquals(String.valueOf(result.get(2).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(2).get("LINGJSL")), "30"); 
		assertEquals(String.valueOf(result.get(2).get("SHIJ")), "2012-04-02"); 
		
		assertEquals(String.valueOf(result.get(3).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(3).get("LINGJSL")), "21"); 
		assertEquals(String.valueOf(result.get(3).get("SHIJ")), "2012-04-03"); 
		
		assertEquals(String.valueOf(result.get(4).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(4).get("LINGJSL")), "33"); 
		assertEquals(String.valueOf(result.get(4).get("SHIJ")), "2012-04-03"); 
		
		assertEquals(String.valueOf(result.get(5).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(5).get("LINGJSL")), "29"); 
		assertEquals(String.valueOf(result.get(5).get("SHIJ")), "2012-04-03"); 
		
		assertEquals(String.valueOf(result.get(6).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(6).get("LINGJSL")), "21"); 
		assertEquals(String.valueOf(result.get(6).get("SHIJ")), "2012-04-04"); 
		
		assertEquals(String.valueOf(result.get(7).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(7).get("LINGJSL")), "33"); 
		assertEquals(String.valueOf(result.get(7).get("SHIJ")), "2012-04-04"); 
		
		assertEquals(String.valueOf(result.get(8).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(result.get(8).get("LINGJSL")), "29"); 
		assertEquals(String.valueOf(result.get(8).get("SHIJ")), "2012-04-04"); 
	}
    
//    @Test
	@TestData(locations = {"classpath:testdata/pc/Wbddyg1.xls"})
	public void testWbddyg(){ 
    	params.put("today", "2012-03-05");
		params.put("kaissj", "2012-03-05");
		
		final List<Map<String,String>> shengxList = dailyRollService.getChangxList(params);
		yueMnService.initParam(shengxList,params);
		yueMnService.cleanInitData(params); 
		yueMnService.parseMaoxqWbddyg(shengxList,params);
    }	
	
//	@Test
	@TestData(locations = {"classpath:testdata/pc/outputNUP.xls"})
	public void testCalOutPut(){
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("kaissj", "2012-03-05"); 
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		params.put("period", "201203");
		params.put("yuemnjhh", "'UWUW5L1201203','UWUW5L2201203','UWUW5L3201203'");
		params.put("chanxzbh", "UW5L,UW5M");
		yueMnService.calOutPut(params);
    }	
	
//	@Test
	@TestData(locations = {"classpath:testdata/pc/outputNUP.xls"})
	public void testCalOutPutRi(){ 
		params.put("chanxzbh", "UW5L,UW5M");
		dailyRollService.calOutPut(params);
    }	
	
	/**
	 * 测试调用日滚动模拟接口模拟数据。
	 * @author 王国首	
	 * @date 2012-02-13
	 */
//	@Test
	@TestData(locations = {"classpath:testdata/pc/PCDailyRollService.xls"})
	public void testPCDailyRollService(){
		yueMnService.calPC(params,qckc);
		params.put("shifqr", "Y");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpcTest.updateYmnQR",params);
		String bizJson = "111";
		pCDailyRollService.callPcDailyRoll(bizJson);
	}
	
	/**
	 * 测试当小于平均工时班分配完后将空班列表清空。
	 * @author 王国首	
	 * @date 2012-02-13
	 */
//	@Test
	@TestData(locations = {"classpath:testdata/pc/outputNUP.xls"})
	public void testParseBanAgent(){ 
		List<String> banc  = new ArrayList<String>();
		Map<String,List<Map<String, String>>> smallBanLj = new HashMap<String,List<Map<String, String>>>();
		Map<String, String> nullBanLjMap = new HashMap<String, String>();
		List<Map<String, String>> finishBanLJ = new ArrayList<Map<String, String>>();
		double avgTime = 0;
		nullBanLjMap.put("LINGJBH", "LJ001");
		Map<String, String> result = dailyRollService.parseBanAgent(banc,smallBanLj,nullBanLjMap,finishBanLJ,avgTime);
		assertEquals(String.valueOf(result.size()), "0"); 
    }	
	
	/**
	 * 测试在封闭期期初库存加上计划排产量，减去需求判断期初库存是否小于0，当小于0时，将此消息写入消息提醒表中提醒计划员。
	 * @author 王国首	
	 * @date 2012-02-13
	 */
//	@Test
	@TestData(locations = {"classpath:testdata/pc/outputNUP.xls"})
	public void testCalBlockDailyXQQckc(){ 
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("today", "2012-03-05");
		final List<Map<String,String>> shengxList = dailyRollService.getChangxList(params);
		dailyRollService.initParam(shengxList,params);
		Map<String,String> blockMxqMap = new HashMap<String,String>();
		blockMxqMap.put("MAOXQ", "150");
		double qckcsl = 10;
		String lingjh = "LJ001";
		double result = dailyRollService.calBlockDailyXQQckc(shengxList,params,blockMxqMap,qckcsl,lingjh);
		assertEquals(String.valueOf(result), "-140.0");
    }	
}

package com.athena.pc.module;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.entity.Rigdpcmx;
import com.athena.pc.entity.Yuemn;
import com.athena.pc.entity.Yuemnjh;
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.MaoxqhzService;
import com.athena.pc.module.service.RigdService;
import com.athena.pc.module.service.YueMnService;
import com.athena.pc.module.service.YuemnQueryService;
import com.athena.pc.module.service.YuemnjhgzService;
import com.toft.core3.container.annotation.Inject;
//	@TestData(locations = {"classpath:testdata/pc/queryDailyRoll.xls"})
public class YuemnjhgzTest extends AbstractCompomentTests {

	@Inject
	private YuemnjhgzService yuemnjhgzService;
	
	@Inject
	private YueMnService yueMnService; 

	@Inject
	private DailyRollService dailyRollService; 
	
	@Inject
	private RigdService rigdService; 
	
	@Inject
	private YuemnQueryService yuemnQueryService; 
	
	@Inject
	private Yuemnjh bean;

	@Inject
	private MaoxqhzService maoxqhzService; 
	
	private Map<String,String> params;
	private Map<String,String> dateRange;
	private List<Map<String,String>> shengxList;
	private List<Map<String,String>> qckc;
	
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
        	bean = new Yuemnjh();
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
    
        };   
    }; 
	/**
	 * 查询生产线测试用例
	 */
//	@Test
	public void testSelectChanx(){
		params.put("today", "2012-03-05");
		yueMnService.calPC(params,qckc); 
		
		params.put("biaos", "R");
		params.put("GUND", "G");
		params.put("kaissj", "2012-03-05");
		params.put("today", "2012-03-05");
		dailyRollService.calPC(params,qckc); 
		
		List<Map<String,String>> result = yuemnjhgzService.selectChanx(params.get("USERCENTER"));
		assertEquals(String.valueOf(result.get(0).get("SHENGCXBH")), "UW5L1"); 
		assertEquals(String.valueOf(result.get(1).get("SHENGCXBH")), "UW5L2"); 
		assertEquals(String.valueOf(result.get(2).get("SHENGCXBH")), "UW5L3"); 
		
	}
	
//	@Test
	public void testSelectYuemn(){
		params.put("oppobj", "'UW5L1','UW5L2','UW5L3'");
		params.put("start", "2012-03-05");
		params.put("end", "2012-04-01");
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-04-01");
		List<Yuemn> result = yuemnQueryService.selectYuemn(params);
		assertEquals(String.valueOf(result.get(0).getGongzbh()), "UWUW5L120120305"); 
		params.put("riq", "2012-03-05");
		List<Map<String,String>> rs = yuemnQueryService.selectGongyzq(params);
		assertEquals(String.valueOf(rs.get(0).get("GONGYZQ")), "201203"); 
		rs = yuemnQueryService.selectGyzqsjfw("201203");
		assertEquals(String.valueOf(rs.get(0).get("KAISSJ")), "2012-03-05"); 
		assertEquals(String.valueOf(rs.get(0).get("JIESSJ")), "2012-04-01"); 
		String rsStr = yuemnQueryService.selectUpGyzq("201203");
		assertEquals(rsStr, "201202"); 
		rsStr = yuemnQueryService.selectNextGyzq("201203");
		assertEquals(rsStr, "201204"); 
		params.put("yuemnjhh", "'UWUW5L1201202'");
		Yuemn bean = new Yuemn();
		yuemnQueryService.updateYuedmnjhb(params);
		rs = yuemnQueryService.selectAllErrorMessage(params);
		Map<String,Object> rsMap = yuemnQueryService.selectMessage(bean,params);
		rsStr = yuemnQueryService.selectErrorMessage(params);
		rs = yuemnQueryService.selectShifqrOfYuedmnjh(params);
		rs = yuemnQueryService.selectChanx(params);
		
	}
	
	/**
	 * 月模拟计划跟踪查询
	 */
//	@Test
	public void testSelect(){
		params.put("usercenter", "UW");
		params.put("lingjbh", "LJ001");
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-05");
		params.put("chanxh", "UW5L1");
		Map<String,Object> result = yuemnjhgzService.select(bean,params);
	}
	
	/**
	 * 日滚动模拟计划
	 */
//	@Test
	public void testSaveRigd(){
		params.put("usercenter", "UW");
		params.put("lingjbh", "LJ001");
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-05");
		params.put("chanxh", "UW5L1");
		String banList = "B,C,D,E";
		String bancList = "BAN1,BAN2,BAN3,BAN4";
		ArrayList<Rigdpcmx> benList = new ArrayList<Rigdpcmx>();
		Rigdpcmx RigdpcmxBean = new Rigdpcmx();
		RigdpcmxBean.setBan1(new BigDecimal("20"));
		RigdpcmxBean.setBan2(new BigDecimal("10"));
		RigdpcmxBean.setBan3(new BigDecimal("0"));
		RigdpcmxBean.setBan4(new BigDecimal("0"));
		RigdpcmxBean.setLingjsl(new BigDecimal("30"));
		RigdpcmxBean.setChanxh("UW5L1");
		RigdpcmxBean.setLingjbh("LJ001");
		RigdpcmxBean.setShij("2012-03-05");
		RigdpcmxBean.setUsercenter("UW");
		benList.add(RigdpcmxBean);
		String result = rigdService.saveRigd(benList,banList, bancList,params);
		assertEquals(result, "success"); 
		RigdpcmxBean.setBan1(new BigDecimal("5"));
		RigdpcmxBean.setBan2(new BigDecimal("5"));
		RigdpcmxBean.setBan3(new BigDecimal("10"));
		RigdpcmxBean.setBan4(new BigDecimal("10"));
		result = rigdService.saveRigd(benList,banList, bancList,params);
	}
	
	/**
	 * 日滚动模拟计划
	 */
//	@Test
	public void testTest(){
		params.put("usercenter", "UW");
		params.put("lingjbh", "LJ001");
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-05");
		params.put("chanxh", "UW5L1");
		String banList = "B,C,D,E";
		String bancList = "BAN1,BAN2,BAN3,BAN4";
		params.put("CHANXH", "'UW5L1','UW5L2','UW5L3'");
		params.put("SHIJ", "2012-03-05");
		Rigdpcmx RigdpcmxBean = new Rigdpcmx();
		String result = rigdService.selectShiFSX(params);
		List<Map<String,String>> rs = rigdService.selectBanc(params);
		result = rigdService.selectErrorMessage(params);
		rigdService.selectMessage(RigdpcmxBean,params);
		result = rigdService.selectShiFPC(params);
		assertEquals(result, "0"); 
	}
	
	@Test
//	@TestData(locations = {"classpath:testdata/pc/clearData.xls"})
	public void testClear(){
		
	}
}

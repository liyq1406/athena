package com.athena.pc.module;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.entity.EquilibLJ;
import com.athena.pc.entity.EquilibScx;
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.EquilibriaSCXService;
import com.athena.pc.module.service.YueMnService;
import com.athena.pc.module.webInterface.PCDailyRollService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;

public class EquilibScxTest  extends AbstractCompomentTests{
	@Inject
	private YueMnService yueMnService; 
	@Inject
	private EquilibriaSCXService equilibriaSCXService; 

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
        };   
    }; 
    
    public Map<String, String> initJbInfo(Map<String,String> pa){
    	Map<String, String> jbInfo  = new HashMap<String, String>() ;
    	// 用户中心
		jbInfo.put("UC", "UW");
		// 标识
		jbInfo.put("BS", "Y");
		// 产线组编号
		jbInfo.put("SCXZBH", "");
		// 生产线号
		jbInfo.put("SCX", "'UW5L1','UW5L2','UW5L3'");
		// 开始时间
		jbInfo.put("KSSJ", "2012-07-02");
		// 结束时间
		jbInfo.put("JSSJ", "2012-07-06");
		//最小时间单位
		jbInfo.put("MINTIME", "0.25");
		//句点
		jbInfo.put("PERIOD", "201207");
		//日期
		jbInfo.put("TODAY", "2012-06-21");
		
		jbInfo.put("DATE", "2012-07-02");
		
		if(pa == null){
			return jbInfo;
		}
		for(Map.Entry<String, String> map:pa.entrySet()){
			jbInfo.put(map.getKey(), map.getValue()) ;
		}
		
		return jbInfo;
		
    }
   
    /**
     * 均衡(月模拟) TP001
     */
//    @Test
//	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
//	public void testcalPC(){ 
//		params.put("biaos", "Y");
//		params.put("today", "2012-11-01");
//		params.put("kaissj", "2012-11-01");
//		params.put("period", "201211");
//		params.put("jiessj", "2012-12-04");
//		params.put("nextjiessj", "2012-12-31");
//		yueMnService.calPC(params,qckc); 
//		
//	}
    
    /**
     * 均衡(月模拟) TP001
     */
    @Test
	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
	public void testcalPC(){ 
		params.put("today", "2012-06-26");
		yueMnService.calPC(params,qckc); 
		
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "8.0"); 
		
	}
    
	/**
	 * 增产(月模拟)TP002
	 */
	 @Test
	 @TestData(locations = {"classpath:testdata/pc/jhzc.xls"})
	public void testcalZC() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "8.0"); 
	}

	/**
	 * 减产(月模拟)TP003
	 */
	 @Test
	 @TestData(locations = {"classpath:testdata/pc/jhjc.xls"})
	public void testcalJC() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "22.0"); 
	}

	/**
	 * 异常情况(月模拟)TP004
	 */
	@Test
	 @TestData(locations = {"classpath:testdata/pc/jhyc.xls"})
	public void testcalYCLJJH() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("DATE", "2012-07-05");
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(map)) ; 
		assertEquals(String.valueOf(scxs.get(1).getGS()), "22.0");
		assertEquals(scxs.get(1).getCHANXH(), "UW5L1");
	}

	/**
	 * 共线零件交换(月模拟)TP005
	 */
	@Test
	 @TestData(locations = {"classpath:testdata/pc/jhgxljjh.xls"})
	public void testcalGXLJJH() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("DATE", "2012-07-06");
		List<EquilibLJ> ljs = equilibriaSCXService.selectLjSL(this.initJbInfo(map)) ;
		assertEquals(String.valueOf(ljs.get(5).getLINGJSL()), "64.0");
		assertEquals(ljs.get(5).getCHANXH(), "UW5L2");
	}

	/**
	 * 多条产线均衡(月模拟)TP006
	 */
	@Test
	 @TestData(locations = {"classpath:testdata/pc/jhdt.xls"})
	public void testcalDTJH() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "8.0"); 
	}

	/**
	 * 滚动周期模拟的均衡计算 TP007
	 */
	@Test
	@TestData(locations = {"classpath:testdtata/pc/jhdt.xls"})
	public void testcalGDZQ() {
		params.put("today", "2012-06-26");
		params.put("biaos", "G");
		yueMnService.calPC(params, qckc);
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("DATE", "2012-07-02");
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(map)) ; 
		assertEquals(String.valueOf(scxs.get(1).getGS()), "8.0");
		assertEquals(scxs.get(1).getCHANXH(), "UW5L3");
		
	}

	/**
	 * 日滚动的均衡计算TP008
	 */
	@Test
	@TestData(locations = {"classpath:testdata/pc/junhengpc.xls"})
	public void testcalRGD() {
		params.put("today", "2012-06-26");
		params.put("biaos", "R");
		params.put("today", "2012-07-02");
		dailyRollService.calPC(params,qckc); 
		
		Map<String, String> map = new HashMap<String, String>() ;
		map.put("DATE", "2012-07-02");
		map.put("BS", "R");
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "8.0"); 
	}

	/**
	 * 月模拟正常流程的均衡计算后，工时与零件向上取整TP009
	 */
	@Test
	@TestData(locations = {"classpath:testdata/pc/jhdt.xls"})
	public void testcalJHQZ() {
		params.put("today", "2012-06-26");
		yueMnService.calPC(params, qckc);
		List<EquilibScx> scxs = equilibriaSCXService.selectScxGs(this.initJbInfo(null)) ; 
		assertEquals(String.valueOf(scxs.get(0).getGS()), "8.0"); 
	}
	
	
	
    
    
}

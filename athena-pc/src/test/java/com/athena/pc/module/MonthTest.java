package com.athena.pc.module;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.module.service.YueMnService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;

@TestData(locations = {"classpath:testdata/pc/Calendar.xls"})
public class MonthTest extends AbstractCompomentTests {
	@Inject
	private YueMnService yueMnService;
	
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
    		params.put("kaissj", "2012-01-01");
    		params.put("jiessj", "2012-01-29");
    		params.put("nextjiessj", "2012-02-28");
    		params.put("shengcx", "'UW5L1','UW5L2','UW5L3'");
    		dateRange.put("MINSJ", "2011-12-20");
    		dateRange.put("MAXSJ", "2012-02-20");
    		params.put("biaos", "Y");
    		params.put("today", "2011-12-25");
    		params.put("TIQQ", "2"); 
    		params.put("jihybh", "root");
    		params.put("USERCENTER", "UW");
    		params.put("MINTIME", "0.25");
    		params.put("ZENGCTS", "3");
        };   
    }; 
    
	/**
	 * 测试获取期初库存
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@Test
	@TestData(locations = {"classpath:testdata/pc/qckc.xls"})
	public void testqiChuKuCun(){ 
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> qckc = yueMnService.qiChuKuCun(params,qckcAll); 
		assertEquals(qckc.get(0).get("LINGJBH"), "LJ001"); 
		assertEquals(String.valueOf(qckc.get(0).get("KCSL")), "850"); 
		assertEquals(String.valueOf(qckc.get(0).get("LINGJSL")), "1545"); 
		assertEquals(String.valueOf(qckc.get(0).get("MAOXQ")), "1500"); 
		assertEquals(String.valueOf(qckc.get(0).get("QCKC")), "895"); 
		assertEquals(qckc.get(1).get("LINGJBH"), "LJ002"); 
		assertEquals(String.valueOf(qckc.get(1).get("KCSL")), "780"); 
		assertEquals(String.valueOf(qckc.get(1).get("LINGJSL")), "1530"); 
		assertEquals(String.valueOf(qckc.get(1).get("MAOXQ")), "1500"); 
		assertEquals(String.valueOf(qckc.get(1).get("QCKC")), "810"); 
		assertEquals(qckc.get(2).get("LINGJBH"), "LJ003"); 
		assertEquals(String.valueOf(qckc.get(2).get("KCSL")), "200"); 
		assertEquals(String.valueOf(qckc.get(2).get("LINGJSL")), "0"); 
		assertEquals(String.valueOf(qckc.get(2).get("MAOXQ")), "0"); 
		assertEquals(String.valueOf(qckc.get(2).get("QCKC")), "200"); 
		List<Map<String,String>> qckcTemp = yueMnService.qiChuKuCun(params,qckc); 
		assertEquals(qckc.get(0).get("LINGJBH"), "LJ001"); 
		assertEquals(String.valueOf(qckcTemp.get(0).get("KCSL")), "850"); 
		assertEquals(String.valueOf(qckcTemp.get(0).get("LINGJSL")), "1545"); 
		assertEquals(String.valueOf(qckcTemp.get(0).get("MAOXQ")), "1500"); 
		assertEquals(String.valueOf(qckcTemp.get(0).get("QCKC")), "895"); 
		assertEquals(qckc.get(1).get("LINGJBH"), "LJ002"); 
		assertEquals(String.valueOf(qckcTemp.get(1).get("KCSL")), "780"); 
		assertEquals(String.valueOf(qckcTemp.get(1).get("LINGJSL")), "1530"); 
		assertEquals(String.valueOf(qckcTemp.get(1).get("MAOXQ")), "1500"); 
		assertEquals(String.valueOf(qckcTemp.get(1).get("QCKC")), "810"); 
		assertEquals(qckc.get(2).get("LINGJBH"), "LJ003"); 
		assertEquals(String.valueOf(qckcTemp.get(2).get("KCSL")), "200"); 
		assertEquals(String.valueOf(qckcTemp.get(2).get("LINGJSL")), "0"); 
		assertEquals(String.valueOf(qckcTemp.get(2).get("MAOXQ")), "0"); 
		assertEquals(String.valueOf(qckcTemp.get(2).get("QCKC")), "200"); 
	} 
	
	/**
	 * 测试获取满足条件的备储计划
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@Test
	//@TestData(locations = {"classpath:testdata/pc/qckc.xls"})
	public void testParseBeicJh(){ 
		List<Map<String,String>> beicList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpc.queryBeicjh",params);
		assertEquals(String.valueOf(beicList.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(beicList.get(0).get("LINGJSL")), "2500"); 
		assertEquals(String.valueOf(beicList.get(0).get("SHENGCBL")), "25");
		assertEquals(String.valueOf(beicList.get(0).get("LINGJSLCX")), "625");
		
		assertEquals(String.valueOf(beicList.get(1).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(beicList.get(1).get("LINGJSL")), "2500"); 
		assertEquals(String.valueOf(beicList.get(1).get("SHENGCBL")), "40");
		assertEquals(String.valueOf(beicList.get(1).get("LINGJSLCX")), "1000");
		
		assertEquals(String.valueOf(beicList.get(2).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(beicList.get(2).get("LINGJSL")), "2500"); 
		assertEquals(String.valueOf(beicList.get(2).get("SHENGCBL")), "35");
		assertEquals(String.valueOf(beicList.get(2).get("LINGJSLCX")), "875");
		
		assertEquals(String.valueOf(beicList.get(3).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(beicList.get(3).get("LINGJSL")), "1430"); 
		assertEquals(String.valueOf(beicList.get(3).get("SHENGCBL")), "33");
		assertEquals(String.valueOf(beicList.get(3).get("LINGJSLCX")), "472");
		
		assertEquals(String.valueOf(beicList.get(4).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(beicList.get(4).get("LINGJSL")), "1430"); 
		assertEquals(String.valueOf(beicList.get(4).get("SHENGCBL")), "40");
		assertEquals(String.valueOf(beicList.get(4).get("LINGJSLCX")), "572");
		
		assertEquals(String.valueOf(beicList.get(5).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(beicList.get(5).get("LINGJSL")), "1430"); 
		assertEquals(String.valueOf(beicList.get(5).get("SHENGCBL")), "27");
		assertEquals(String.valueOf(beicList.get(5).get("LINGJSLCX")), "387");
	} 
	
	/**
	 * 测试备储计划拆分
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/qckc1.xls"})
	public void testParseBeic(){ 
		yueMnService.cleanInitData(params);
		yueMnService.parseBeic(shengxList,params);
		yueMnService.statDailyLingj(shengxList, params);
		params.put("lingjbh", "LJ002");
		params.put("chanxh", "UW5L1");
		params.put("kaissj", "2012-01-10");
		params.put("jiessj", "2012-01-31");
		List<Map<String,String>> beicList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseBeic",params);
		assertEquals(String.valueOf(beicList.get(0).get("LINGJBH")), "LJ002"); 
		assertEquals(String.valueOf(beicList.get(0).get("CHANXH")), "UW5L1"); 
		
		assertEquals(String.valueOf(beicList.get(0).get("SHIJ")), "2012-01-10");
		assertEquals(String.valueOf(beicList.get(0).get("LINGJSL")), "37");

		assertEquals(String.valueOf(beicList.get(1).get("SHIJ")), "2012-01-11");
		assertEquals(String.valueOf(beicList.get(1).get("LINGJSL")), "37");
		
		assertEquals(String.valueOf(beicList.get(2).get("SHIJ")), "2012-01-12");
		assertEquals(String.valueOf(beicList.get(2).get("LINGJSL")), "37");
		
		assertEquals(String.valueOf(beicList.get(3).get("SHIJ")), "2012-01-13");
		assertEquals(String.valueOf(beicList.get(3).get("LINGJSL")), "37");
		
		assertEquals(String.valueOf(beicList.get(4).get("SHIJ")), "2012-01-14");
		assertEquals(String.valueOf(beicList.get(4).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(5).get("SHIJ")), "2012-01-16");
		assertEquals(String.valueOf(beicList.get(5).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(6).get("SHIJ")), "2012-01-17");
		assertEquals(String.valueOf(beicList.get(6).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(7).get("SHIJ")), "2012-01-18");
		assertEquals(String.valueOf(beicList.get(7).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(8).get("SHIJ")), "2012-01-19");
		assertEquals(String.valueOf(beicList.get(8).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(9).get("SHIJ")), "2012-01-20");
		assertEquals(String.valueOf(beicList.get(9).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(10).get("SHIJ")), "2012-01-29");
		assertEquals(String.valueOf(beicList.get(10).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(11).get("SHIJ")), "2012-01-30");
		assertEquals(String.valueOf(beicList.get(11).get("LINGJSL")), "36");
		
		assertEquals(String.valueOf(beicList.get(12).get("SHIJ")), "2012-01-31");
		assertEquals(String.valueOf(beicList.get(12).get("LINGJSL")), "36");
	} 

	/**
	 * 测试安全库存计算
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/aqkc.xls"})
	public void testCalAnqkc(){ 
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		assertEquals(String.valueOf(anqkcMap.get("UWLJ001")), "900.0"); 
		assertEquals(String.valueOf(anqkcMap.get("UWLJ002")), "600.0"); 
		assertEquals(String.valueOf(anqkcMap.get("UWLJ003")), "548.0"); 
	} 

	/**
	 * 测试计算每日净需求（正常情况）
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/aqkc.xls"})
	public void testCalDailyJXQ(){ 
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		List<Map<String, String>> 	chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setAheadPeriod(yueMnService.calAddTiqqRil(params,chanxzrl));
		yueMnService.calDailyJXQ(shengxList,params,tempQckc);
		
		params.put("testkssj", "2012-01-02");
		params.put("testjssj", "2012-01-02");
		List<Map<String,String>> jinXuQiu = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyJXQ",params);
		
		assertEquals(String.valueOf(jinXuQiu.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("JINXQ")), "518");
		
		assertEquals(String.valueOf(jinXuQiu.get(1).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("JINXQ")), "462");
	} 

	/**
	 * 测试第二天的期初库存
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/aqkc.xls"})
	public void testCalDailyJXQQckc(){ 
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-02");
		List<Map<String, String>> chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setAheadPeriod(yueMnService.calAddTiqqRil(params,chanxzrl));
		List<Map<String,String>> Qckc = yueMnService.calDailyJXQ(shengxList,params,tempQckc);
		
		assertEquals(String.valueOf(Qckc.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(Qckc.get(0).get("QCKC")), "979.0");
		
		assertEquals(String.valueOf(Qckc.get(2).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(Qckc.get(2).get("QCKC")), "548.0");
	} 
	
	/**
	 * 测试计算每日净需求（检查当每日净需求小于0时,那当日每日净需求更新为0）
	 * 将零件LJ001的安全库存降低（有900改为200），净需求计算出为负数。
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/JxqLessZero.xls"})
	public void testCalDailyJXQLessZero(){ 
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		List<Map<String, String>> chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setAheadPeriod(yueMnService.calAddTiqqRil(params,chanxzrl));
		yueMnService.calDailyJXQ(shengxList,params,tempQckc);
		
		params.put("testkssj", "2012-01-02");
		params.put("testjssj", "2012-01-02");
		List<Map<String,String>> jinXuQiu = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyJXQ",params);
		
		assertEquals(String.valueOf(jinXuQiu.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("JINXQ")), "0");
		
		assertEquals(String.valueOf(jinXuQiu.get(1).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("JINXQ")), "462");
	} 
	
	/**
	 * 测试计算产线零件日需求
	 * 
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/jxqcx.xls"})
	public void testCalDailyCXJXQ(){ 
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.calDailyCXJXQ(shengxList,params);
		
		params.put("testkssj", "2012-01-02");
		List<Map<String,String>> jinXuQiu = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyCXJXQ",params);
		
		assertEquals(String.valueOf(jinXuQiu.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("JINXQ")), "130");
		
		assertEquals(String.valueOf(jinXuQiu.get(1).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("JINXQ")), "208");
		
		assertEquals(String.valueOf(jinXuQiu.get(2).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("CHANXH")), "UW5L3");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("JINXQ")), "182");
		
		assertEquals(String.valueOf(jinXuQiu.get(3).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("JINXQ")), "102");
		
		assertEquals(String.valueOf(jinXuQiu.get(4).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(4).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(4).get("JINXQ")), "208");
		
		assertEquals(String.valueOf(jinXuQiu.get(5).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(5).get("CHANXH")), "UW5L3");
		assertEquals(String.valueOf(jinXuQiu.get(5).get("JINXQ")), "153");
	} 

	/**
	 * 测试计算产线零件日需求(将拆分出的产线零件日需求同经济批量做比较，当小于经济批量时，产线零件日需数量取经济批量的数量为净需求)
	 * 将CKX_SHENGCX_LINGJ 表 零件LJ001，UW5L1产线的经济批量修改为150，是否启用经济批量修改为"N"
	 * 将CKX_SHENGCX_LINGJ 表 零件LJ003，UW5L1产线的经济批量修改为150，是否启用经济批量修改为"Y"
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/jxqcxJinjpl.xls"})
	public void testCalDailyCXJXQJinjpl(){ 
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.calDailyCXJXQ(shengxList,params);
		
		params.put("testkssj", "2012-01-02");
		List<Map<String,String>> jinXuQiu = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyCXJXQ",params);
		
		assertEquals(String.valueOf(jinXuQiu.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("JINXQ")), "130");
		
		assertEquals(String.valueOf(jinXuQiu.get(1).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("JINXQ")), "208");
		
		assertEquals(String.valueOf(jinXuQiu.get(2).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("CHANXH")), "UW5L3");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("JINXQ")), "182");
		
		assertEquals(String.valueOf(jinXuQiu.get(3).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("JINXQ")), "150");
		
		assertEquals(String.valueOf(jinXuQiu.get(4).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(4).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(4).get("JINXQ")), "208");
		
		assertEquals(String.valueOf(jinXuQiu.get(5).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(5).get("CHANXH")), "UW5L3");
		assertEquals(String.valueOf(jinXuQiu.get(5).get("JINXQ")), "153");
	} 

	/**
	 * 测试计算库存系数方法
	 * @author 王国首	
	 * @date 2011-12-30
	 */
	@Test
	public void testCalkuCunXiShu(){ 
		List<Map<String,String>> qckc = new ArrayList<Map<String,String>>();
		Map<String,String> tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ001");
		tempMap.put("QCKC", "200");
		qckc.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ002");
		tempMap.put("QCKC", "300");
		qckc.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ003");
		tempMap.put("QCKC", "500");
		qckc.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ004");
		tempMap.put("QCKC", "120");
		qckc.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ005");
		tempMap.put("QCKC", "400");
		qckc.add(tempMap);
		
		List<Map<String,String>> chanxlj = new ArrayList<Map<String,String>>();
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ001");
		tempMap.put("MAOXQ", "500");
		chanxlj.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ002");
		tempMap.put("MAOXQ", "150");
		chanxlj.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ003");
		tempMap.put("MAOXQ", "325");
		chanxlj.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ004");
		tempMap.put("MAOXQ", "800");
		chanxlj.add(tempMap);
		tempMap = new HashMap<String,String>();
		tempMap.put("LINGJBH", "LJ005");
		tempMap.put("MAOXQ", "100");
		chanxlj.add(tempMap);
		
		List<Map<String,String>> test =  yueMnService.calkuCunXiShu(qckc,chanxlj,"UW5L"); 
    	assertEquals(test.get(0).get("LINGJBH"), "LJ004"); 
    	assertEquals(test.get(0).get("KUCXS"), "0.15"); 
    	assertEquals(test.get(1).get("LINGJBH"), "LJ001");
    	assertEquals(test.get(1).get("KUCXS"), "0.4"); 
    	assertEquals(test.get(2).get("LINGJBH"), "LJ003");
    	assertEquals(test.get(2).get("KUCXS"), "1.54"); 
    	assertEquals(test.get(3).get("LINGJBH"), "LJ002");
    	assertEquals(test.get(3).get("KUCXS"), "2.0"); 
    	assertEquals(test.get(4).get("LINGJBH"), "LJ005");
    	assertEquals(test.get(4).get("KUCXS"), "4.0"); 
	} 
}

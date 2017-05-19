package com.athena.pc.module;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.module.service.PCRunTimeException;
import com.athena.pc.module.service.YueMnService;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;

public class MonthTwoTest extends AbstractCompomentTests {
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
    		params.put("period", "201201");
    		params.put("chanxzbh", "UW5L");	
        };   
    }; 

	
	/**
	 * 循环计算每日工时
	 * 
	 * @author 王国首	
	 * @date 2011-12-29
	 */
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/dailyTime.xls"})
	public void testcalDailyTime(){ 
//		Map<String, String> testTemp = new HashMap<String,String>();
//		testTemp.put("USERCENTER", "UW");
//		testTemp.put("BANC", "2C03QBSX");
//		testTemp.put("RIQ", "2012-01-02");
//		testTemp.put("SHIFGZR", "0");
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpcTest.updateRiLi",testTemp);
		
		yueMnService.initParam(shengxList,params);
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		List<Map<String, String>> chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setAheadPeriod(yueMnService.calAddTiqqRil(params,chanxzrl));
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		Map<String, String> avgMap = yueMnService.calWeekAvgHours(shengxList,params);
		Map<String, List<String>> chanxzWC = yueMnService.getChanxzWeekWorkCalendar(params,chanxzrl);
		yueMnService.setBlockWorkCalendar(yueMnService.parseBlockWorkCalendar(params,shengxList,chanxzWC));
		final Iterator<String> it = chanxzWC.keySet().iterator(); 
		it.hasNext();
		List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
		yueMnService.calDailyTime(shengxList,params,tempQckc,avgMap,weekRil);
		
		params.put("testkssj", "2012-01-02");
		params.put("testjssj", "2012-01-02");
		
		List<Map<String,String>> gongs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyTime",params);
		
		assertEquals(String.valueOf(gongs.get(0).get("CHANXH")), "UW5L1"); 
		assertEquals(String.valueOf(gongs.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(gongs.get(0).get("LINGJSL")), "140");
		assertEquals(String.valueOf(gongs.get(0).get("HOUR")), "9");
		assertEquals(String.valueOf(gongs.get(0).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(0).get("ALLHOUR")), "11.75");
		
		assertEquals(String.valueOf(gongs.get(1).get("CHANXH")), "UW5L1"); 
		assertEquals(String.valueOf(gongs.get(1).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(gongs.get(1).get("LINGJSL")), "52");
		assertEquals(String.valueOf(gongs.get(1).get("HOUR")), "2.75");
		assertEquals(String.valueOf(gongs.get(1).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(1).get("ALLHOUR")), "11.75");
		
		assertEquals(String.valueOf(gongs.get(2).get("CHANXH")), "UW5L2"); 
		assertEquals(String.valueOf(gongs.get(2).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(gongs.get(2).get("LINGJSL")), "220");
		assertEquals(String.valueOf(gongs.get(2).get("HOUR")), "14.25");
		assertEquals(String.valueOf(gongs.get(2).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(2).get("ALLHOUR")), "19.75");
		
		assertEquals(String.valueOf(gongs.get(3).get("CHANXH")), "UW5L2"); 
		assertEquals(String.valueOf(gongs.get(3).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(gongs.get(3).get("LINGJSL")), "91");
		assertEquals(String.valueOf(gongs.get(3).get("HOUR")), "5.5");
		assertEquals(String.valueOf(gongs.get(3).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(3).get("ALLHOUR")), "19.75");
	} 

	/**
	 * 测试计算产线零件日需求(当某一天某条产线当天工作日历为不上班时，拆分净需求时，这条产线不安排净需求生产)
	 * 将ckx_calendar_version 表 2012-01-02，UW5L3产线的工作日历是否工作日修改为0
	 * @author 王国首	
	 * @date 2011-12-29
	 */
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/jxqcxNoWork.xls"})
	public void testCalDailyCXJXQNoWork(){ 
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.setWeekCal(true);
		yueMnService.calDailyCXJXQ(shengxList,params);
		
		params.put("testkssj", "2012-01-02");
		List<Map<String,String>> jinXuQiu = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyCXJXQ",params);
		
		assertEquals(String.valueOf(jinXuQiu.get(0).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(0).get("JINXQ")), "232");
		
		assertEquals(String.valueOf(jinXuQiu.get(1).get("LINGJBH")), "LJ001");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(1).get("JINXQ")), "372");
		
		assertEquals(String.valueOf(jinXuQiu.get(2).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("CHANXH")), "UW5L1");
		assertEquals(String.valueOf(jinXuQiu.get(2).get("JINXQ")), "152");
		
		assertEquals(String.valueOf(jinXuQiu.get(3).get("LINGJBH")), "LJ003");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("CHANXH")), "UW5L2");
		assertEquals(String.valueOf(jinXuQiu.get(3).get("JINXQ")), "310");
	} 
	
	/**
	 * 根据需要增产时间进行增产
	 * 
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/zengchan.xls"})
	public void testCalZengchan(){ 
		yueMnService.initParam(shengxList,params);
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		List<Map<String, String>> chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		Map<String, String> avgMap = yueMnService.calWeekAvgHours(shengxList,params);
		Map<String, List<String>> chanxzWC = yueMnService.getChanxzWeekWorkCalendar(params,chanxzrl);
		final Iterator<String> it = chanxzWC.keySet().iterator(); 
		it.hasNext();
		List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
	    String shengcxbh = "UW5L1";
	    params.put("shengcxCX", shengcxbh);  
	    params.put("kaissjPC", "2012-01-02");
		List<Map<String, String>> chanxlj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpc.queryDailyChanxLj",params);  
	    List<Map<String, String>> kuCunXiShu  = yueMnService.calkuCunXiShu(tempQckc,chanxlj,"UW5L");
	    double zengcsj = 1.75;
	    Map<String,Map<String,String>> zengc = yueMnService.calZengchan(params,tempQckc,chanxlj,shengcxbh,zengcsj);

	    assertEquals(String.valueOf(zengc.get("LJ001").get("ZENGCSL")), "27.0");
	    assertEquals(String.valueOf(zengc.get("LJ001").get("ZENGCLJSJ")), "1.75");
	} 

	/**
	 * 循环计算每日工时(净需求加上增产)
	 * 
	 * @author 王国首	
	 * @date 2011-12-29
	 ****/
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/dailyTimeAddZc.xls"})
	public void testcalDailyTimeAddZc(){ 
		yueMnService.initParam(shengxList,params);
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		List<Map<String,String>> qckcAll = new ArrayList<Map<String,String>>();
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckcAll);
		Map<String, String> anqkcMap = yueMnService.calAnqkc(params);
		tempQckc = yueMnService.setAnqkcToQckc(params,tempQckc,anqkcMap);
		List<Map<String, String>> chanxzrl = yueMnService.getChanxzWorkCalendar(params);
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		Map<String, String> avgMap = yueMnService.calWeekAvgHours(shengxList,params);
		Map<String, List<String>> chanxzWC = yueMnService.getChanxzWeekWorkCalendar(params,chanxzrl);
		final Iterator<String> it = chanxzWC.keySet().iterator(); 
		it.hasNext();
		List<String> weekRil =(ArrayList<String>)chanxzWC.get(it.next());
		yueMnService.calDailyTime(shengxList,params,tempQckc,avgMap,weekRil);
		
		params.put("testkssj", "2012-01-02");
		params.put("testjssj", "2012-01-02");
		
		List<Map<String,String>> gongs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryCalDailyTime",params);
		
		assertEquals(String.valueOf(gongs.get(0).get("CHANXH")), "UW5L1"); 
		assertEquals(String.valueOf(gongs.get(0).get("LINGJBH")), "LJ001"); 
		assertEquals(String.valueOf(gongs.get(0).get("LINGJSL")), "130");
		assertEquals(String.valueOf(gongs.get(0).get("HOUR")), "8.25");
		assertEquals(String.valueOf(gongs.get(0).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(0).get("ALLHOUR")), "9.75");
		
		assertEquals(String.valueOf(gongs.get(1).get("CHANXH")), "UW5L1"); 
		assertEquals(String.valueOf(gongs.get(1).get("LINGJBH")), "LJ003"); 
		assertEquals(String.valueOf(gongs.get(1).get("LINGJSL")), "26");
		assertEquals(String.valueOf(gongs.get(1).get("HOUR")), "1.5");
		assertEquals(String.valueOf(gongs.get(1).get("SHIJ")), "2012-01-02");
		assertEquals(String.valueOf(gongs.get(1).get("ALLHOUR")), "9.75");
		
	}
	
	/**
	 * 测试计算最大平均工时
	 *
	 * @author 王国首	
	 * @date 2011-12-29
	 */
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/avgtime.xls"})
	public void testCalWeekAvgHours(){ 
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.initParam(shengxList,params);
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		Map<String, String> avgMap = yueMnService.calWeekAvgHours(shengxList,params);
		
		assertEquals(String.valueOf(avgMap.get("UW5L1")), "11.75");
		assertEquals(String.valueOf(avgMap.get("UW5L2")), "19.75");
		assertEquals(String.valueOf(avgMap.get("UW5L3")), "13.75");
	} 
	
	/**
	 * 测试计算最大平均工时(当最大平均工时小于22小时，终止计算，报警提示计划员最大平均工时大于22小时)
	 * 将将节拍修改为为10
	 * @author 王国首	
	 * @date 2011-12-29
	 */
	@SuppressWarnings("unchecked")
    @Test
	@TestData(locations = {"classpath:testdata/pc/avgtime8.xls"})
	public void testCalWeekAvgHoursLess(){ 
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.initParam(shengxList,params);
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		Map<String, String> avgMap = yueMnService.calWeekAvgHours(shengxList,params);
		
		assertEquals(String.valueOf(avgMap.get("UW5L1")), "8.0");
		assertEquals(String.valueOf(avgMap.get("UW5L2")), "10.0");
		assertEquals(String.valueOf(avgMap.get("UW5L3")), "8.0");
	}   
    @Rule        
    public ExpectedException thrown= ExpectedException.none(); 

	/**
	 * 测试计算最大平均工时(当最大平均工时大于22小时，终止计算，报警提示计划员最大平均工时大于22小时)
	 * 将将节拍修改为为10
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test  
    @TestData(locations = {"classpath:testdata/pc/avgtime22.xls"})
    public void throwsbigAvgTimeGreatException() {
//    	thrown.expect(ServiceException.class); 
    	thrown.expect(RuntimeException.class);
		thrown.expectMessage("最大平均工时大于二十二小时");
		params.put("kaissjWeeK", "2012-01-02");
		params.put("jiessjWeeK", "2012-01-07");
		yueMnService.initParam(shengxList,params);
		yueMnService.setTeSuTime(yueMnService.getTeSuTime(params));
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		yueMnService.calWeekAvgHours(shengxList,params);
    } 
    
	/**
	 * 测试当分配产线时间，终止计算，报警提示计划员没有分配产线排产)
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test
    public void throwsNoChanxException() {
//    	thrown.expect(ServiceException.class);
    	thrown.expect(RuntimeException.class);
    	thrown.expectMessage("没有分配产线排产");
    	params.put("chanxzbh", "UW8L");		
    	List<Map<String, String>> beginStocks = new ArrayList<Map<String, String>>();
		yueMnService.calPC(params,beginStocks);
    } 
    
	/**
	 * 测试当排产时间大于最大平均工时时，且大于22小时时，终止计算，并将警告信息提示给计划员)
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test 
    public void throwsPaicTimeGreatException() {
//    	thrown.expect(ServiceException.class); 
    	thrown.expect(RuntimeException.class);
    	thrown.expectMessage("每日工时大于平均值");
    	yueMnService.setMessagePc(new ArrayList<Map<String, String>>());
    	yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
    	yueMnService.checkPaicTime(params,21.00,3,20,"UW5L1","2012-02-16");
    }

	/**
	 * 测试当排产时间大于最大平均工时时，小于于22小时时，记录一条提示信息到信息表)
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test
    public void throwsPaicTimeGreat() {
    	yueMnService.setMessagePc(new ArrayList<Map<String, String>>());
    	yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
    	yueMnService.checkPaicTime(params,11,3,10,"UW5L1","2012-02-16");
    	List<Map<String, String>> result = yueMnService.getMessagePc();
    	assertEquals(String.valueOf(result.get(0).get("SHIJ")), "2012-02-16");
    }
    
	/**
	 * 测试错误的时间时间转换
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test
    public void throwsErrTimeCharException() {
    	Map<String, String> req = new HashMap();
    	req.put("LINGJBH", "LJ001");
    	req.put("CANGKDM", "XYG");
    	req.put("KAISSJ", "");
    	req.put("JIESSJ", "2012-02-12");
    	params.put("LJ001XYG", "3");
    	yueMnService.setOrderAheadOfTime(req,params);
    }
    
	/**
	 * 测试检查开始时间是否大于等于结束时间，如果错误记录错误信息到日志中
	 * 在日志中查看错误信息
	 * @author 王国首	
	 * @date 2011-12-29
	 */
    @Test
	public void testValidDateOfBetween(){
		yueMnService.validDateOfBetween("2012-02-03", "");
	}
	
}

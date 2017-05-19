package com.athena.pc.module;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;

import com.athena.component.service.Message;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData; 
import com.athena.pc.module.service.PCRunTimeException;
import com.athena.pc.module.service.YueMnService;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


import com.athena.db.ConstantDbCode;

	@TestData(locations = {"classpath:testdata/pc/CalendarGD.xls"})
public class GDMonthTest extends AbstractCompomentTests {
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
//    		params.put("today", "2012-03-07");	
//    		params.put("kaissj", "2012-03-07");
        };   
    }; 
	
	//@Test
	@TestData(locations = {"classpath:testdata/pc/GunD.xls"})
	public void testcalPC(){ 
		yueMnService.calPC(params,qckc); 
	}

	//@Test
	@TestData(locations = {"classpath:testdata/pc/GunD.xls"})
	public void testcalPCGunD(){ 
		params.put("GUND", "G"); 
		params.put("today", "2012-03-05");
		params.put("kaissj", "2012-03-07");
		yueMnService.calPC(params,qckc); 
	}

    @Rule        
    public ExpectedException thrown= ExpectedException.none(); 
    
	/**
	 * 测试月模拟时间某天某条产线工作日为关闭，在滚动月模拟中当天的是否工作为开启,月模拟明细是否正常插入
	 * 将月模拟时，产线：UW5L3，2012-03-22工作日历为关闭，滚动月模拟时，产线：UW5L3，2012-03-22工作日历修改为开启
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	//@Test
//	@TestData(locations = {"classpath:testdata/pc/GunD.xls"})
	public void testGDYuemn(){ 
		yueMnService.calPC(params,qckc);
		Map<String, String> Temp = new HashMap<String,String>();
		Temp.put("CHANXH", "UW5L3");
		Temp.put("SHIJ", "2012-03-06");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryDailyTimeChanxMx",Temp);
		assertEquals(String.valueOf(result.get(0).get("GONGZBH")), "UWUW5L320120306");
		assertEquals(String.valueOf(result.get(0).get("HOUR")), "13.25");
		assertEquals(String.valueOf(result.get(0).get("MEIRQBCN")), "201");
		assertEquals(String.valueOf(result.get(0).get("GUNDMNGS")), "null");
		assertEquals(String.valueOf(result.get(0).get("GUNDQBCN")), "null");
		params.put("GUND", "G"); 
		params.put("today", "2012-03-05");	
		params.put("kaissj", "2012-03-05");
		yueMnService.calPC(params,qckc);
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryDailyTimeChanxMx",Temp);
		assertEquals(String.valueOf(result.get(0).get("GONGZBH")), "UWUW5L320120306");
		assertEquals(String.valueOf(result.get(0).get("HOUR")), "13.25");
		assertEquals(String.valueOf(result.get(0).get("MEIRQBCN")), "201");
		assertEquals(String.valueOf(result.get(0).get("GUNDMNGS")), "10.5");
		assertEquals(String.valueOf(result.get(0).get("GUNDQBCN")), "159");
	}
	
	/**
	 * 测试当仓库US的包装类型，仓库US的包装容量为空时，将包装问题信息写入消息提醒
	 * 
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/baoz.xls"})
	public void testBaozErr(){ 
//    	thrown.expect(ServiceException.class); 
		thrown.expect(RuntimeException.class); 
    	thrown.expectMessage("包装信息有误请修改包装信息");
		final List<Map<String,String>> shengxList = yueMnService.getChangxList(params);
		
		if (shengxList.size() <= 0) {
			throw new PCRunTimeException(new Message("pc.ymn.nullChangx.error","i18n.pc.pc").getMessage()); 
		}
		yueMnService.initParam(shengxList,params);
		yueMnService.cleanInitData(params); 

		yueMnService.parseMaoxq(shengxList, params);
		if(params.get("GUND")!= null && "G".equals(params.get("GUND"))){
			yueMnService.parseNoWorkCalReq(params, yueMnService.getNoWorkCalReqs());
			yueMnService.setNoWorkCalReqs(new ArrayList<Map<String,String>>());
			yueMnService.parseRiMaoxq(shengxList, params);
		}
		yueMnService.parseBeic(shengxList, params); 
		yueMnService.parseNoWorkCalReq(params, yueMnService.getNoWorkCalReqs());
		yueMnService.statDailyLingj(shengxList, params);
		List<Map<String,String>> tempQckc = new ArrayList<Map<String,String>>();
		tempQckc = yueMnService.qiChuKuCun(params,tempQckc);
		yueMnService.calYueMoN(shengxList, params,tempQckc);
		yueMnService.insertMessage();
	} 
	
	/**
	 * 测试需求拆分到提前时间段，将需求拆分到其他产线
	 * 
	 * 在订单明细中增加一条pp记录零件：LJ001，开始时间：2012-03-06，结束时间：2012-03-06，零件数量：50
	 * 将产线'UW5L3'，2012-03-05工作日历是否工作日修改为不工作
	 * 在订单明细中增加一条pp记录零件：LJ002，开始时间：2012-03-09，结束时间：2012-03-09，零件数量：50
	 * 将产线'UW5L3'，2012-03-09工作日历是否工作日修改为不工作
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/reqOfLine.xls"})
	public void testReqOfLine(){ 
		yueMnService.initParam(shengxList,params);
		yueMnService.cleanInitData(params); 
		yueMnService.parseMaoxq(shengxList, params);
		List<Map<String,String>> temp = yueMnService.getNoWorkCalReqs();
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-05");
		params.put("lingjbh", "LJ001");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseRxq",params);
		assertEquals(String.valueOf(result.size()), "6"); 
		yueMnService.parseNoWorkCalReq(params, yueMnService.getNoWorkCalReqs());
		params.put("kaissj", "2012-03-05");
		params.put("jiessj", "2012-03-05");
		params.put("lingjbh", "LJ001");
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryParseRxq",params);
		assertEquals(String.valueOf(result.size()), "8"); 
	} 
	
	/**
	 * 测试计算期初库存减未拆分的需求
	 * 
	 * 在备储计划明细中增加一条记录零件：LJ002，开始时间：2012-03-05，结束时间：2012-03-06，零件数量：150
	 * 将产线'UW5L1','UW5L2','UW5L3'，2012-03-05，2012-03-06工作日历是否工作日修改为不工作
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/qckcMinusMXQ.xls"})
	public void testCalQckcMinusXQ(){ 
		yueMnService.initParam(shengxList,params);
		yueMnService.cleanInitData(params); 
		yueMnService.parseMaoxq(shengxList, params);
		yueMnService.parseBeic(shengxList, params); 
		yueMnService.parseNoWorkCalReq(params, yueMnService.getNoWorkCalReqs());
		yueMnService.statDailyLingj(shengxList, params);
		List<Map<String,String>> tempQckc = yueMnService.qiChuKuCun(params,qckc);
		
		assertEquals(String.valueOf(tempQckc.get(0).get("QCKC")), "1195");
		assertEquals(String.valueOf(tempQckc.get(1).get("QCKC")), "865");
		assertEquals(String.valueOf(tempQckc.get(2).get("QCKC")), "600");
		
		yueMnService.calQckcMinusXQ(params,tempQckc);
		
		assertEquals(String.valueOf(tempQckc.get(0).get("QCKC")), "1195");
		assertEquals(String.valueOf(tempQckc.get(1).get("QCKC")), "714.0");
		assertEquals(String.valueOf(tempQckc.get(2).get("QCKC")), "600");
	} 

	/**
	 * 测试月模拟时间某天某条产线工作日为关闭，在滚动月模拟中当天的是否工作为开启,月模拟明细是否正常插入
	 * 将月模拟时，产线：UW5L3，2012-03-22工作日历为关闭，滚动月模拟时，产线：UW5L3，2012-03-22工作日历修改为开启
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/CloseWorkCal.xls"})
	public void testCloseWorkCal(){ 
		yueMnService.calPC(params,qckc);
		Map<String, String> Temp = new HashMap<String,String>();
		Temp.put("CHANXH", "UW5L3");
		Temp.put("SHIJ", "2012-03-22");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryDailyTimeChanxMx",Temp);
		assertEquals(String.valueOf(result.size()), "0");
		Map<String, String> testTemp = new HashMap<String,String>();
		testTemp.put("USERCENTER", "UW");
		testTemp.put("BANC", "2C03QBSX");
		testTemp.put("RIQ", "2012-03-22");
		testTemp.put("SHIFGZR", "1");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpcTest.updateRiLi",testTemp);
		params.put("GUND", "G"); 
		params.put("today", "2012-03-07");	
		params.put("kaissj", "2012-03-07");
		yueMnService.calPC(params,qckc);
		result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryDailyTimeChanxMx",Temp);
		assertEquals(String.valueOf(result.size()), "1");
	}
	
	/**
	 * 测试获取开始时间之前的所有工作时间
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetAheadOfTimeCal(){ 
		List<String> shengcxCal = new ArrayList<String>();
		shengcxCal.add("2012-03-05");
		shengcxCal.add("2012-03-06");
		shengcxCal.add("2012-03-07");
		shengcxCal.add("2012-03-08");
		shengcxCal.add("2012-03-09");
		Map<String,List<String>> tempCalendar = new HashMap<String,List<String>>();
		tempCalendar.put("UW5L3", shengcxCal);
		yueMnService.setWorkCalendar(tempCalendar);
		List<String> shengcx = new ArrayList<String>();
		shengcx.add("2012-03-05");
		shengcx.add("2012-03-06");
		Map<String, String> req = new HashMap<String, String>();
		req.put("KAISSJ", "2012-03-07");
		req.put("JIESSJ", "2012-03-08");
		req.put("SHENGCXBH", "UW5L3");
		List<String> result = yueMnService.findSubCal(shengcx,req);
		assertEquals(String.valueOf(result.get(0)), "2012-03-05");
		assertEquals(String.valueOf(result.get(1)), "2012-03-06");
		assertEquals(String.valueOf(result.get(2)), "2012-03-07");
	}
	
	/**
	 * 测试当期初库存小于等于零时，在数据库中记录一条报警信息
	 * @author 王国首	
	 * @date 2011-12-29
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCheckQckc(){ 
		params.put("SCXBH", "UW5L3");
		params.put("SHIJ", "2012-03-19");
		params.put("LEIX", "2");
		yueMnService.setMessagePc(new ArrayList<Map<String, String>>());
		yueMnService.setYueMnjhh(yueMnService.getYueMoNijhh(shengxList,params));
		yueMnService.checkQckc(params,"LJ001",-10.0,params.get("SCXBH"),params.get("USERCENTER"),params.get("SHIJ"),params.get("LEIX"));
		params.put("YUEMNJHH", yueMnService.getYueMnjhh().get("UW5L3"));
		yueMnService.insertMessage();
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryYuemnjhh", params);
		assertEquals(String.valueOf(result.size()), "1"); 
	} 
	
}

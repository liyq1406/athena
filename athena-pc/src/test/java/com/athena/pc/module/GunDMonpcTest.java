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

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.pc.module.webInterface.PCScheduleService;
import com.athena.pc.module.service.YueMnService;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;


	//@TestData(locations = {"classpath:testdata/pc/CalendarGD.xls"})
public class GunDMonpcTest extends AbstractCompomentTests {
	@Inject
	private YueMnService yueMnService; 
	@Inject
	private PCScheduleService pCScheduleService;
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	private Map<String,String> params;
	private Map<String,String> params1;
	private Map<String,String> dateRange;
	private List<Map<String,String>> shengxList;
	private List<Map<String,String>> shengxList1;
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
    		
    		params1 = new HashMap<String,String>();
    		params1.put("kaissj", "2012-02-20");
    		params1.put("jiessj", "2012-03-04");
    		params1.put("nextjiessj", "2012-04-01");
    		params1.put("shengcx", "'UW5L1','UW5L2','UW5L3'");
    		params1.put("biaos", "Y");
    		params1.put("TIQQ", "2"); 
    		params1.put("jihybh", "root");
    		params1.put("USERCENTER", "UW");
    		params1.put("MINTIME", "0.25");
    		params1.put("ZENGCTS", "3");
    		params1.put("chanxzbh", "UW5L");		
    		params1.put("today", "2012-02-18");	
    		params1.put("period", "201202");
        };   
    }; 
	
    @Rule        
    public ExpectedException thrown= ExpectedException.none(); 

	//@Test
//	@TestData(locations = {"classpath:testdata/pc/GunD.xls"})
	public void testcalPC(){ 
		yueMnService.calPC(params,qckc); 
	}

	//@Test
//	@TestData(locations = {"classpath:testdata/pc/GunD.xls"})
	public void testcalPCGunD(){ 
		params.put("GUND", "G"); 
		params.put("today", "2012-03-05");	
		yueMnService.calPC(params,qckc); 
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
	public void testCalWeekAvgHours(){ 
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
	 * 测试当需求为空时，抛出异常信息显示给客户
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/NullXQ.xls"})
	public void testCheckRequirement(){ 
//    	thrown.expect(ServiceException.class); 
    	thrown.expect(RuntimeException.class);
    	thrown.expectMessage("计划排产时间内需求为空");
    	List<Map<String, String>> beginStocks = new ArrayList<Map<String, String>>();
		yueMnService.calPC(params,beginStocks);
	} 
	
	/**
	 * 测试调用滚动月模拟接口模拟数据。
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@Test
	@TestData(locations = {"classpath:testdata/pc/GunDYuemn.xls"})
	public void testPCScheduleService(){
		yueMnService.calPC(params1,qckc); 
		yueMnService.calPC(params,qckc);
		params.put("shifqr", "Y");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpcTest.updateYmnQR",params);
		String bizJson = "111";
		pCScheduleService.callPcSchedule(bizJson);
		Map<String, String> Temp = new HashMap<String,String>();
		Temp.put("CHANXH", "UW5L3");
		Temp.put("SHIJ", "2012-03-06");
		List<Map<String,String>> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("monpcTest.queryDailyTimeChanxMx",Temp);
		assertEquals(String.valueOf(result.get(0).get("GONGZBH")), "UWUW5L320120306");

	}

	/**
	 * 测试当调用滚动月模拟出错时，程序能继续运行
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/NullCalendar.xls"})
	public void testPcScheduleErr(){ 
    	String bizJson = "111";
		String result = pCScheduleService.callPcSchedule(bizJson);
		assertEquals(String.valueOf(result), "null");
		params.put("shifqr", "N");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("monpcTest.updateYmnQR",params);
	}
	
	/**
	 * 测试当计算时间内工作日历为空时，抛出异常信息显示给客户
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/NullCalendar.xls"})
	public void testCheckCalendar(){ 
//    	thrown.expect(ServiceException.class); 
		thrown.expect(RuntimeException.class);
    	thrown.expectMessage("排产时间内，工作日历为空");
		yueMnService.calPC(params,new ArrayList<Map<String, String>>());
	} 
	
	/**
	 * 测试当包装类型或包装容量为空时，抛出异常信息显示给客户
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/checkBaozrl.xls"})
	public void testCheckBaozrl(){ 
//    	thrown.expect(ServiceException.class); 
		thrown.expect(RuntimeException.class);
    	thrown.expectMessage("包装信息有误请修改包装信息");
		yueMnService.calPC(params,new ArrayList<Map<String, String>>());
	} 
	
	/**
	 * 测试当产线组每个零件生产比例不为100%时，抛出异常信息显示给客户
	 * @author 王国首	
	 * @date 2012-02-13
	 */
	@SuppressWarnings("unchecked")
	@Test
	@TestData(locations = {"classpath:testdata/pc/checkShengcbl.xls"})
	public void testCheckShengcbl(){ 
//    	thrown.expect(ServiceException.class); 
		thrown.expect(RuntimeException.class);
    	thrown.expectMessage("零件生产比例有误请调整零件生产比例");
		yueMnService.calPC(params,new ArrayList<Map<String, String>>());
	} 
}

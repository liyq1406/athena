package com.athena.fj.module;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
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
import com.athena.component.wtc.WtcResponse;
import com.athena.fj.entity.Peizdmx;
import com.athena.fj.entity.Peizjh;
import com.athena.fj.entity.Wulgz;
import com.athena.fj.module.service.PeizjhService;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


import com.athena.db.ConstantDbCode;

/**
 * <p>
 * Title:配载计划测试类
 * </p>
 * <p>
 * Description:定义配载计划测试点
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-1-5
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
@TestData(locations = {"classpath:testdata/fj/peizjh.xls"})
public class PeizjhTest extends AbstractCompomentTests {

	@Inject
	private PeizjhService peizjhService;
	@Inject
	private Peizjh bean;
	@Inject
	private AbstractIBatisDao baseDao;
	private Map<String,String> params;
	private Map<String,String> parameter;
	private String username="root";
	private List<String> bhlList= new ArrayList<String>();
	private List<String> yaohlList= new ArrayList<String>();
	@Rule        
    public ExpectedException thrown= ExpectedException.none(); 
	 @Rule  
	    public ExternalResource resource= new ExternalResource() {  
	        @Override  
	        protected void before() throws Throwable {  
	    		params = new HashMap<String,String>();
	    		params.put("peizdh", "PW0002"); 
	    		params.put("yaocmxh", "ycmx02"); 
	    		params.put("usercenter", "UW"); 
	    		
	    		parameter = new HashMap<String,String>();
	    		parameter.put("peizdh","PW0001");
	    		parameter.put("yaohlList","20005");
	    		parameter.put("bhdList","BCP000000219");
	    		parameter.put("cheph","鄂A20023");
	    		parameter.put("success","true");
	    		
	    		bean.setUsercenter("UW");
	    		bean.setPeizdh("PW0002");
	    		bhlList.add("BCP000000203");
	    		bean.setBhlList(bhlList);
	    		bean.setChep("鄂A10005");
	    		bean.setCreator(username);
	    		bean.setCreateTime(DateUtil.curDateTime());
	    		bean.setEditor(username);
	    		bean.setEditTime(DateUtil.curDateTime());
	    		bean.setDaocsj("2012-02-25 10:00");
	    		bean.setFaysj("2012-02-25 8:30");
	    		bean.setJihcx("DC");
	    		bean.setJihzt("1");
	    		bean.setYunslx("LX1");
	    		bean.setKehbm("WUHAN");
	    		bean.setDaysj(DateUtil.curDateTime());
	    		bean.setYunssbm("CYS001");
	    		bean.setYaocmxh("ycmx02");
	    		bean.setYaohls("'20005','20006','20007'");
	    		yaohlList.add("20005");
	    		yaohlList.add("20006");
	    		bean.setYaohlList(yaohlList);
	        };   
	    }; 
	
	
	/**
	 * 配载计划查询测试
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSelectPeizjh(){
		Map<String,Object> peizjhMap = peizjhService.select(bean,params);
		List<Peizjh> list = new ArrayList<Peizjh>();
		list =  (List<Peizjh>) peizjhMap.get("rows");
		assertEquals("PW0002",list.get(0).getPeizdh());
		assertEquals("ycmx02",list.get(0).getYaocmxh());
	}
	
	
	/**
	 * 根据传入的sqlmapId获得需要的集合
	 * 所有车型查询测试
	 * @return 车型集合map
	 */
	@Test
	public void testSelectChex(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		list = peizjhService.selectMap("queryChexMap","UW");
		assertEquals("DC",list.get(0).get("CHEXBH").toString());
		assertEquals("大车",list.get(0).get("CHEXMC").toString());
		assertEquals("XC",list.get(1).get("CHEXBH").toString());
		assertEquals("小车",list.get(1).get("CHEXMC").toString());
	}
	
	
	/**
	 * 根据配载计划号查询要货令测试
	 */
	@Test
	public void testSelectYhl(){
		List<Map<String,Object>> list = peizjhService.select(params);
		assertEquals("20005",list.get(0).get("YAOHLH"));
		assertEquals("E",list.get(0).get("XIEHD"));
		assertEquals("2501",list.get(0).get("BAOZXH"));
		
		assertEquals("20006",list.get(1).get("YAOHLH"));
		assertEquals("F",list.get(1).get("XIEHD"));
		assertEquals("2501A",list.get(1).get("BAOZXH"));
		
	}
	
	/**
	 * 自动配载时查询没有备货的要货令
	 */
	@Test
	public void testQueryNotBeiHOfYaohl(){
		List<Map<String,Object>> noMatchYaohlList = peizjhService.queryNotBeiHOfYaohl(bean);
		assertEquals("PW0002",noMatchYaohlList.get(0).get("PEIZDH"));
		assertEquals(new BigDecimal(7),noMatchYaohlList.get(0).get("LINGJSL"));
		assertEquals("20026",noMatchYaohlList.get(0).get("YAOHLH"));
		assertEquals("95004",noMatchYaohlList.get(0).get("LINGJBH"));
		assertEquals("2501B",noMatchYaohlList.get(0).get("BAOZXH"));
		assertEquals("B",noMatchYaohlList.get(0).get("XIEHD"));
		
	}

	/**
	 * 手工配载时查询没有备上货的要货令
	 */
	@Test
	public void testQueryNotBeiHOfYaohlSG(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("yaohls", "'20005','20006','20007'");
		param.put("usercenter", "UW");
		List<Map<String,Object>> noMatchYaohlList = peizjhService.queryNotBeiHOfYaohlSG(param);
		assertEquals("20005",noMatchYaohlList.get(0).get("YAOHLH"));
		assertEquals("95001",noMatchYaohlList.get(0).get("LINGJBH"));
		assertEquals("2501",noMatchYaohlList.get(0).get("BAOZXH"));
		assertEquals("E",noMatchYaohlList.get(0).get("XIEHD"));
	}
	

	/**
	 * 根据要货令号查询得到备货信息
	 */
	@Test
	public void testGetBeihdList(){
		String beihls = "'BCP000000203'";
		List<Peizdmx> PeizdmxList = peizjhService.getBeihdList(beihls);
		assertEquals("BCP000000203",PeizdmxList.get(0).getBeihdh());
		assertEquals("111",PeizdmxList.get(0).getCangkbh());
		assertEquals(new BigDecimal(2),PeizdmxList.get(0).getBaozsl());
		
	}
	
	/**
	 * 向配载单和配载单明细表中插入数据
	 * date 2012-3-1
	 */
	@Test
	public void testBatchInsert(){
		List<Peizdmx> listPeizdmx = new ArrayList<Peizdmx>();
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("BCP000000203");
		pzdmx.setCangkbh("111");
		pzdmx.setZickbh("z01");
		pzdmx.setBaozsl(new BigDecimal("10"));
		pzdmx.setPeizdh("PW0001");
		listPeizdmx.add(pzdmx);
		bean.setPeizdh("PW0003");
		peizjhService.batchInsert(bean, listPeizdmx, username);
		assertTrue(true);
	}
	
	/**
	 * 修改配载计划车牌，计划状态和打印时间测试
	 * date 2012-3-1
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdatePeizjh(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("peizjhTest.updatePeizjh",bean);
		Map<String,Object> map1 = peizjhService.select(bean,params);
		List<Peizjh> list = new ArrayList<Peizjh>();
		list =  (List<Peizjh>) map1.get("rows");
		assertEquals("鄂A10005",list.get(0).getChep());
	}
	
	/**
	 * 自动配载，仓库返回消息成功，全部备货后进行批量更新操作(修改配载计划、修改要车明细表状态，插入配载单和配载明细)
	 * date 2012-3-1
	 */
	@Test
	public void testBatchUpdate(){
		List<Peizdmx> listPeizdmx = new ArrayList<Peizdmx>();
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("BCP000000204");
		pzdmx.setCangkbh("111");
		pzdmx.setZickbh("z01");
		pzdmx.setBaozsl(new BigDecimal("10"));
		pzdmx.setPeizdh("PW0001");
		listPeizdmx.add(pzdmx);
		bean.setPeizdh("PW0004");
		peizjhService.batchUpdate(bean, listPeizdmx, username);
		assertTrue(true);
	}
	/**
	 * 自动配载后，要货令部分满足情况下用户选择继续配载，
	 * 记物流故障，删除没有配载的要货令明细，改更要货令锁定状态，将确认配载信息插入配载单
	 * date 2012-3-1
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSurePeiz(){
		Wulgz wulgzBean = new Wulgz();
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", "UW");
		param.put("peizdh", "PW0001");
		//获得选中的一条记录的值
		Map<String,Object> map1 = peizjhService.select(wulgzBean,param);
		List<Peizjh> list = new ArrayList<Peizjh>();
		list =  (List<Peizjh>) map1.get("rows");
		
		wulgzBean.setYaocmxh(list.get(0).getYaocmxh());
		wulgzBean.setPeizdh(list.get(0).getPeizdh());
		wulgzBean.setJihcx(list.get(0).getJihcx());
		wulgzBean.setShijcx("cx008");
		wulgzBean.setShijcp("鄂AF3501");
		wulgzBean.setGuzyy("车型不正确！！！");
		wulgzBean.setDaocsj(list.get(0).getDaocsj());
		wulgzBean.setShijdcsj("2011-12-28");
		wulgzBean.setYunssbm(list.get(0).getYunssbm());
		wulgzBean.setCreator("root");
		wulgzBean.setCreateTime(DateUtil.curDateTime());
		wulgzBean.setEditor("root");
		wulgzBean.setEditTime(DateUtil.curDateTime());
		
		List<Peizdmx> listPeizdmx = new ArrayList<Peizdmx>();
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("BCP000000205");
		pzdmx.setCangkbh("111");
		pzdmx.setZickbh("z01");
		pzdmx.setBaozsl(new BigDecimal("12"));
		pzdmx.setPeizdh("PW0001");
		listPeizdmx.add(pzdmx);
		bean.setPeizdh("PW0005");
		peizjhService.surePeiz(bean, wulgzBean, listPeizdmx, username);
		assertTrue(true);
	}
	
	/**
	 * 手工配载流程（全部配载），将配载信息插入配载单和配载单明细表
	 * date 2012-3-1
	 */
	@Test
	public void testBatchPeizSG(){
		List<Peizdmx> listPeizdmx = new ArrayList<Peizdmx>();
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("BCP000000206");
		pzdmx.setCangkbh("111");
		pzdmx.setZickbh("z01");
		pzdmx.setBaozsl(new BigDecimal("11"));
		listPeizdmx.add(pzdmx);
		bean.setPeizdh("PW0001");
		peizjhService.batchPeizSG(bean, listPeizdmx, username);
		assertTrue(true);
	}
	
	/**
	 * 手工配载流程（继续配载），更新要货令表中的锁定状态为未锁定，将配载信息插入配载单和配载单明细表
	 * date 2012-3-1
	 */
	@Test
	public void testSurePeizSG(){
		List<Peizdmx> listPeizdmx = new ArrayList<Peizdmx>();
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("BCP000000207");
		pzdmx.setCangkbh("111");
		pzdmx.setZickbh("z01");
		pzdmx.setBaozsl(new BigDecimal("17"));
		listPeizdmx.add(pzdmx);
		peizjhService.surePeizSG(bean, listPeizdmx, username);
	}
	
	
	/**
	 * 自动配载时，查要货令明细表获取要货令发运时间测试
	 * date 2012-3-1
	 */
/*	@Test
	public void testQueryFaysjOfYaohlmx(){
		String faysj = peizjhService.queryFaysjOfYaohlmx("PW0001");
		assertEquals("2012-01-14 08:30",faysj);
	}*/
	
	/**
	 * 手工配载时，查询要货令表获取要货令发运时间集合测试
	 * date 2012-3-1
	 */
	@Test
	public void testQueryFaysjOfYaohl(){
		//获得发运时间
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", bean.getUsercenter());
		param.put("yaohls", bean.getYaohls());
		List<Map<String,String>> list = peizjhService.queryFaysjOfYaohl(param);
		assertEquals("2012-01-14 08:30",list.get(0).get("FAYSJ"));
		
	}
	
	/**
	 * 手工配载时，获取发运时间
	 * date 2012-3-1
	 */
/*	@Test
	public void testGetFaysj(){
		String faysj = peizjhService.getFaysj(bean);
		assertEquals("2012-01-14 08:30",faysj);
	}*/
	
	/**
	 * 手工配载时，获取到车时间
	 * date 2012-3-1
	 */
/*	@Test
	public void testGetDaocsj(){
		String daocsj = peizjhService.getDaocsj(bean);
		assertEquals("2012-01-14 06:50",daocsj);
	}*/
	
	
	/**
	 * 将配载信息插入配载单表测试
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testInsertPeizd(){
		Peizjh bean = new Peizjh();
		Map<String,String> param = new HashMap<String,String>();
		param.put("peizdh", "PW0006");
		param.put("usercenter", "UW");
		//获得选中的一条记录的值
		Map<String,Object> mapPzjh = peizjhService.select(bean,param);
		List<Peizjh> list = new ArrayList<Peizjh>();
		list =  (List<Peizjh>) mapPzjh.get("rows");
		
		bean.setPeizdh(list.get(0).getPeizdh());
		bean.setYunssbm(list.get(0).getYunssbm());
		bean.setJihcx(list.get(0).getJihcx());
		bean.setChep(list.get(0).getChep());
		bean.setDaocsj(list.get(0).getDaocsj());
		bean.setFaysj(list.get(0).getFaysj());
		bean.setKehbm(list.get(0).getKehbm());
		bean.setDaysj(list.get(0).getDaysj());
		bean.setPeizdh(list.get(0).getPeizdh());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("peizjhTest.insertPeizd", bean);
		
		Peizjh pzjhBean = (Peizjh) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjhTest.queryPeizd",param);
		assertEquals(list.get(0).getPeizdh(),pzjhBean.getPeizdh());
		assertEquals(list.get(0).getYunssbm(),pzjhBean.getYunssbm());
		assertEquals(list.get(0).getJihcx(),pzjhBean.getJihcx());
		assertEquals(list.get(0).getDaocsj(),pzjhBean.getDaocsj());
		assertEquals(list.get(0).getKehbm(),pzjhBean.getKehbm());
		
	}
	
	/**
	 * 将备货令信息插入到配载单明细表测试
	 */
	@Test
	public void testInsertPeizdmx(){
		Peizdmx pzdmx = new Peizdmx();
		pzdmx.setBeihdh("bhl001");
		pzdmx.setPeizdh("PW0002");
		pzdmx.setBaozsl(new BigDecimal("10"));
		pzdmx.setCangkbh("ck001");
		pzdmx.setZickbh("z01");
		pzdmx.setCreator("root");
		pzdmx.setCreateTime(DateUtil.curDateTime());
		pzdmx.setEditor("root");
		pzdmx.setEditTime(DateUtil.curDateTime());
		
		//将备货令信息插入到配载单明细表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("peizjhTest.insertPeizdmx", pzdmx);
		Map<String,String> param = new HashMap<String,String>();
		param.put("beihdh","bhl001");
		param.put("peizdh","PW0002");
		pzdmx = (Peizdmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjhTest.queryPeizdmx", param);
		assertEquals("bhl001",pzdmx.getBeihdh());
		assertEquals("PW0002",pzdmx.getPeizdh());
		assertEquals(new BigDecimal(10),pzdmx.getBaozsl());
		assertEquals("ck001",pzdmx.getCangkbh());
		assertEquals("z01",pzdmx.getZickbh());
		
	}
	
	
	
	
	/**
	 * 删除配载计划，增加物流故障，同时修改要车计划状态为物流故障
	 */
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDelPeizjh(){
		Wulgz bean = new Wulgz();
		//获得选中的一条记录的值
		Map<String,Object> map1 = peizjhService.select(bean,params);
		List<Peizjh> list = new ArrayList<Peizjh>();
		list =  (List<Peizjh>) map1.get("rows");
		
		bean.setYaocmxh(list.get(0).getYaocmxh());
		bean.setPeizdh(list.get(0).getPeizdh());
		bean.setJihcx(list.get(0).getJihcx());
		bean.setShijcx("cx008");
		bean.setShijcp("鄂AF3501");
		bean.setGuzyy("车型不正确！！！");
		bean.setDaocsj(list.get(0).getDaocsj());
		bean.setShijdcsj("2011-12-28");
		bean.setYunssbm(list.get(0).getYunssbm());
		bean.setCreator("root");
		bean.setCreateTime(DateUtil.curDateTime());
		bean.setEditor("root");
		bean.setEditTime(DateUtil.curDateTime());
		
		peizjhService.delPeizjh(bean,username);
		
		//比较插入到物流故障表的数据
		bean = (Wulgz)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjhTest.queryWulgz", params);
		assertEquals(list.get(0).getYaocmxh(),bean.getYaocmxh());
		assertEquals(list.get(0).getPeizdh(),bean.getPeizdh());
		assertEquals(list.get(0).getJihcx(),bean.getJihcx());
		assertEquals("cx008",bean.getShijcx());
		assertEquals("鄂AF3501",bean.getShijcp());
		assertEquals("车型不正确！！！",bean.getGuzyy());
		
		//查询配载计划是否被删除
		List<?> count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("peizjhTest.queryPeizjh", params);
		assertEquals("0",String.valueOf(count.size()));
		
		//查询要车明细状态是否被修改为4（物流故障）
		String yaoczt =  (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjhTest.queryYaocmx", params);
		assertEquals("4",yaoczt);
		
	}
	
	/**
	 * 仓库返回消息是否正确
	 */
	@Test
	public void testCheckStoreResponse(){
		boolean status = peizjhService.checkStoreResponse(parameter);
		assertEquals(true,status);
	}
	
	/**
	 *继续配载测试仓库返回消息，正常流程
	 */
	@Test
	public void testSureStoreResponse(){
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0000");
		wtc.put("parameter", parameter);
		Map<String, String> result = peizjhService.sureStoreResponse(wtc);
		assertEquals("BCP000000219",result.get("bhdList"));
	}
	/**
	 * 继续配载测试仓库返回消息，异常流程
	 */
	@Test
	public void testThrowExceptionSureStoreResponse(){
		thrown.expect(ServiceException.class);
		thrown.expectMessage("配载单查询数据库没有数据！");
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0001");
		wtc.put("parameter", parameter);
		wtc.put("respdesc", "配载单查询数据库没有数据！");
		peizjhService.sureStoreResponse(wtc);
		
	}
	
	/**
	 * 向仓库发送删除备货单的消息，正常流程
	 */
	@Test
	public void testDelStoreResponse(){
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0000");
		wtc.put("parameter", parameter);
		boolean status = peizjhService.delStoreResponse(wtc);
		assertEquals(true,status);
	}
	/**
	 * 向仓库发送删除备货单的消息，异常流程
	 */
	@Test
	public void testThrowExceptionDelStoreResponse(){
		thrown.expect(ServiceException.class);
		thrown.expectMessage("得到备货单号错误!");
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0001");
		wtc.put("parameter", parameter);
		wtc.put("respdesc", "得到备货单号错误!");
		peizjhService.delStoreResponse(wtc);
	}
	
	/**
	 * 解析仓库返回的消息，正常流程
	 */
	@Test
	public void testParseCheckWtcResponse(){
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0000");
		wtc.put("parameter", parameter);
		Map<String, String> result =peizjhService.parseCheckWtcResponse(wtc);
		assertEquals("20005",result.get("yaohlList"));
		
	}
	
	/**
	 * 解析仓库返回的消息，异常流程
	 */
	@Test
	public void testThrowExceptionParseCheckWtcResponse(){
		thrown.expect(ServiceException.class);
		thrown.expectMessage("查询数据库没有数据!");
		Map<String,Object> map = new HashMap<String,Object>();
		WtcResponse wtc = (WtcResponse) new WtcResponse(map);
		wtc.put("response", "0001");
		wtc.put("parameter", parameter);
		wtc.put("respdesc", "查询数据库没有数据!");
		peizjhService.parseCheckWtcResponse(wtc);
	}
	
	
	/**
	 * 根据查询区域条件查询未配载要货令集合测试
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	@TestData(locations = {"classpath:testdata/fj/peizjh_yaohl.xls"})
	public void testSelectNoPeizYaohl(){
		params.put("yslxbh", "LX1");
		params.put("yaohlhs", "'20007','20008','20009'");
		Map<String,Object> map = peizjhService.selectNoPeizYaohl(bean, params);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		list =  (List<Map<String, String>>) map.get("rows");
		assertEquals("20003",list.get(0).get("YAOHLH"));
		assertEquals("95001",list.get(0).get("LINGJBH"));
		assertEquals("2501",list.get(0).get("BAOZXH"));
		assertEquals("2012-01-14 08:30",list.get(0).get("FAYSJ"));
	}
	
	/**
	 *新增配载计划
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testSavePeizjh(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("yaohls", "'30025','30027'");
		param.put("peizdh", "PW0015");
		param.put("usercenter", "UW");
		param.put("creator", "root");
		peizjhService.savePeizjh(bean, param);
	}
	/**
	 *增加要货令明细
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testAddYaohlmx(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("peizdh", "PW0006");
		param.put("yaohls", "'20065','20067'");
		param.put("usercenter", "UW");
		param.put("creator", "root");
		peizjhService.addYaohlmx(bean,param);
	}
	
	/**
	 *修改运输商
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testUpdatePeizjhYunss(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("peizdh", "PW0006");
		param.put("usercenter", "UW");
		param.put("yunssbm", "CYS001");
		peizjhService.updatePeizjhYunss(param);
	}
	
	
	/**
	 * 配载计划下的要货令零件汇总
	 * @author 贺志国
	 * @date 2012-4-5
	 */
	@Test
	@TestData(locations = {"classpath:testdata/fj/peizjh_yaohl.xls"})
	public void testSelectPeizjhYaohlOfLingj(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("peizdh", "PW0005");
		param.put("usercenter", "UW");
		List<Map<String,String>> list = peizjhService.selectPeizjhYaohlOfLingj(param);
		assertEquals("95004",list.get(0).get("LINGJBH"));
	}
	
	
	/**
	 * 配载计划下的要货令包装汇总
	 * @author 贺志国
	 * @date 2012-4-6
	 */
	@Test
	public void testSelectPeizjhYaohlOfBaoz(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("peizdh", "PW0005");
		param.put("usercenter", "UW");
		List<Map<String,String>> list = peizjhService.selectPeizjhYaohlOfBaoz(param);
		assertEquals("2501A",list.get(0).get("BAOZXH"));
		
	}
	
	/**
	 * 根据运输路线查询未配载要货令集合
	 * @author 贺志国
	 * @date 2012-4-6
	 */
	@Test
	public void testSelectYaohlOfTuij(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", "UW");
		param.put("yaohlhs", "'20003','20004','20005'");
		List<Map<String,String>> list  = peizjhService.selectYaohlOfTuij(param);
		assertEquals("2501",list.get(0).get("BAOZXH"));
		assertEquals("95001",list.get(0).get("LINGJBH"));
		assertEquals("D",list.get(0).get("XIEHD"));
	}
	
	

}

package com.athena.xqjs.module.diaobl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.diaobl.Diaobmx;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.diaobl.service.DiaobsqService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 调拨审核测试类
 * 
 * @author Niesy
 * 
 *         date:2011-12-17
 */
@TestData(locations = { "classpath:testData/xqjs/diaobsq.xls" })
public class VerifyTest extends AbstractCompomentTests {

	@Inject
	private DiaobshService diaobshService;

	@Inject
	private AbstractIBatisDao baseDao;

	@Inject
	private DiaobsqService diaobsqService;

	private Diaobsq diaobsq;

	private Diaobsqmx diaobsqmx;

	private Diaobmx diaobmx;

	private Diaobmx diaobmx1;

	private Map<String, String> map;

	private Map<String, Object> map1;
	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			String diaobsqdh = "S2310001";
			diaobsq = new Diaobsq();
			// 数据准备
			diaobsq.setDiaobsqdh(diaobsqdh);
			diaobsq.setBanc("0001");
			diaobsq.setUsercenter("UW");
			diaobsq.setDiaobsqsj(CommonFun.getJavaTime().substring(0, 10));
			diaobsq.setChengbzx("klll");
			diaobsq.setHuijkm("wh");
			diaobsq.setZhuangt("20");
			diaobsq.setBeiz("ok");
			diaobsq.setCreate_time("2012-02-12 20:51:45:400");
			diaobsq.setCreator("001");
			diaobsq.setEdit_time("2012-02-12 20:51:45:400");
			diaobsq.setEditor("001");
			// 调拨申请明细
			diaobsqmx = new Diaobsqmx();
			diaobsqmx.setUsercenter("UW");
			diaobsqmx.setDiaobsqdh(diaobsq.getDiaobsqdh());
			diaobsqmx.setLingjbh("7540759080");
			diaobsqmx.setLux("97W");
			diaobsqmx.setShenbsl(new BigDecimal(100));
			diaobsqmx.setCreate_time(diaobsq.getCreate_time());
			diaobsqmx.setCreator("001");
			diaobsqmx.setEdit_time(diaobsq.getEdit_time());
			diaobsqmx.setEditor("001");
			// 调拨明细
			map1 = new HashMap<String, Object>();
			diaobmx = new Diaobmx();
			diaobmx.setUsercenter("UW");
			diaobmx.setDiaobsqdh("S2210001");
			diaobmx.setCangkbh("W05");
			diaobmx.setLingjbh("7540759080");
			diaobmx.setLux("97W");
			diaobmx.setShenbsl(new BigDecimal(200));
			diaobmx.setShipsl(new BigDecimal(100));
			diaobmx1 = new Diaobmx();
			diaobmx1.setUsercenter("UW");
			diaobmx1.setDiaobsqdh("S2210001");
			diaobmx1.setCangkbh("W01");
			diaobmx1.setLingjbh("7540759080");
			diaobmx1.setLux("97W");
			diaobmx1.setShenbsl(new BigDecimal(200));
			diaobmx1.setShipsl(new BigDecimal(100));
			map1.put("diaobmx", diaobmx);
			map1.put("diaobmx1", diaobmx1);
			// Map数据准备
			map = new HashMap<String, String>();
			map.put("usercenter", diaobsqmx.getUsercenter());
			map.put("diaobsqdh", diaobsq.getDiaobsqdh());
			map.put("lingjbh", diaobsqmx.getLingjbh());
			map.put("lux", diaobsqmx.getLux());
		};
	};

	/**
	 * 调拨审核-生效测试
	 */
	@Test
	public void confirmTest() {
		// 插入之前清除该数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desq", map);
		// 插入数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.ins_passed", diaobsq);
		diaobsq.setNewEdit_time(CommonFun.getJavaTime());
		diaobsq.setNewEditor("005");
		String str = diaobshService.modifyState(diaobsq);
		Diaobsq dr = diaobsqService.selectDiaobsq(diaobsq);
		// 比较返回结果
		org.junit.Assert.assertEquals("3", str);
		// 比较两个状态
		org.junit.Assert.assertEquals(Const.DIAOBL_ZT_EFFECT, dr.getZhuangt());
	}

	/**
	 * 调拨审核-生效测试2
	 */
	@Test
	public void confirm2Test() {
		diaobsq.setNewEdit_time(CommonFun.getJavaTime());
		diaobsq.setEditor("005");
		diaobsq.setDiaobsqdh("S2C21035");
		String str = diaobshService.modifyState(diaobsq);
		// 比较返回结果
		org.junit.Assert.assertEquals("2", str);
	}

	/**
	 * 调拨审核-申请明细查询测试
	 */
	@Test
	public void querysqmxTest() {
		Diaobsq bean = new Diaobsq();
		// 数据准备
		bean.setUsercenter("UW");
		bean.setDiaobsqdh("S2210001");
		// 调用调拨审核service方法，点击调拨单号，链接跳转
		Map<String, Object> map = diaobshService.sumShipsl(bean);
		List<Diaobsqmx> ls = (List<Diaobsqmx>) map.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx sr = ls.get(i);
			System.out.println(sr.getLingjbh() + " " + sr.getLingjmc() + " " + sr.getShipsl());
		}
		org.junit.Assert.assertEquals(1, ls.size());
	}

	/**
	 * 调拨审核 调拨单号生成的测试
	 */
	@Test
	public void generationDhTest() {
		map.put("diaobsqdh", "S2210001");
		diaobsq.setDiaobsqdh("S2210001");
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);
		// 插入数据
		Set<Map.Entry<String, Object>> set = map1.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			diaobshService.insertDiaobmx((Diaobmx) entry.getValue());
		}
		Diaobmx bean = new Diaobmx();
		bean.setUsercenter("UW");
		bean.setDiaobsqdh("S2210001");
		bean.setLux("97W");
		List<Diaobmx> ls = diaobshService.diaobdhGeneration(bean);
		String dh = ls.get(0).getDiaobdh();
		String dh1 = "W00000001";
		org.junit.Assert.assertEquals(dh1, dh);

	}

	/**
	 * 调拨审核 调拨申请明细状态改变
	 * 
	 */
	@Test
	public void updateZtMx() {
		// map.remove("diaobsqdh");
		map.put("diaobsqdh", "S2210001");
		// 清除数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		// 插入数据
		diaobsqmx.setDiaobsqdh("S2210001");
		diaobsqService.doInsertmx(diaobsqmx);
		// 改状态
		diaobsqmx.setNewEdit_time(CommonFun.getJavaTime());
		diaobsqmx.setNewZhuangt(Const.DIAOBL_ZT_APPLYING);
		diaobsqmx.setZhuangt(Const.DIAOBL_ZT_APPLYING);
		diaobsqmx.setNewEditor("003");
		diaobshService.updateMxState(diaobsqmx);
		// 比较
		Diaobsqmx dx = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", map);
		org.junit.Assert.assertEquals(diaobsqmx.getNewZhuangt(), dx.getZhuangt());

	}

	/**
	 * 调拨审核 调拨明细增加测试
	 */
	@Test
	public void insMxTest() {
		Diaobmx bean = new Diaobmx();
		bean.setCangkbh("W05");
		bean.setUsercenter("UW");
		bean.setDiaobsqdh("S2210001");
		bean.setLingjbh("7540759080");
		bean.setLux("97W");
		bean.setShenbsl(new BigDecimal(100));
		bean.setShipsl(new BigDecimal(100));
		map.put("diaobsqdh", "S2210001");
		map.put("cangkbh", bean.getCangkbh());
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);
		// 插入
		diaobshService.insertDiaobmx(bean);
		// 查询
		Diaobmx dm = (Diaobmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.selDbmx", map);
		// 比较
		org.junit.Assert.assertEquals(bean.getCangkbh(), dm.getCangkbh());
		org.junit.Assert.assertEquals(bean.getDiaobsqdh(), dm.getDiaobsqdh());
	}

	/**
	 * 获取时间测试
	 */
	@Test
	public void getSysDateTest() {
		String time = diaobshService.getSysdate();
		System.out.println(time + " " + time.length());
		org.junit.Assert.assertEquals(23, time.length());

	}

	/**
	 * 打印json字符串拼接测试
	 */
	@Test
	public void stringJsonTest() {
		map.put("diaobsqdh", "S2210001");
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);
		// 插入数据
		Set<Map.Entry<String, Object>> set = map1.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			diaobshService.insertDiaobmx((Diaobmx) entry.getValue());
		}
		diaobsqmx.setDiaobsqdh("S2210001");
		// 拼接方法
		String json = diaobshService.printDiaobmx(diaobsqmx);
		System.out.println(json);
		String exp = "[{\"XUH1\":1,\"CANGKBH\":\"W01\",\"LUX1\":\"97W\",\"LINGJBH1\":\"7540759080\",\"LINGJMC1\":\"隔套\",\"DIAOBSL1\":100},{\"XUH1\":1,\"CANGKBH\":\"W05\",\"LUX1\":\"97W\",\"LINGJBH1\":\"7540759080\",\"LINGJMC1\":\"隔套\",\"DIAOBSL1\":100}]";
		org.junit.Assert.assertEquals(exp, json);
	}

	/**
	 * list转map 测试
	 */
	@Test
	public void mapTest() {
		ArrayList<String> ls = new ArrayList<String>();
		Map map = diaobshService.listToMap(ls);
		System.out.println(map);
		String mapStr = map.toString();
		org.junit.Assert.assertEquals("{total=0, rows=[]}", mapStr);

	}

	/**
	 * 终止该状态测试
	 */
	@Test
	public void stopTest() {
		Diaobmx bean = new Diaobmx();
		bean.setCangkbh("W05");
		bean.setUsercenter("UW");
		bean.setDiaobsqdh("S2210001");
		bean.setLingjbh("7540759080");
		bean.setLux("97W");
		bean.setShenbsl(new BigDecimal(100));
		bean.setShipsl(new BigDecimal(100));
		map.put("diaobsqdh", "S2210001");
		map.put("cangkbh", bean.getCangkbh());
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);
		// 插入
		diaobshService.insertDiaobmx(bean);
		ArrayList<Diaobmx> mx = new ArrayList<Diaobmx>();
		mx.add(bean);
		// 改状态
		diaobshService.cangkStop(mx);
		// 查询
		Diaobmx dm = (Diaobmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.selDbmx", map);
		// 比较
		org.junit.Assert.assertEquals(Const.DIAOBL_ZT_STOPPED, dm.getZhuangt());
		org.junit.Assert.assertEquals(bean.getDiaobsqdh(), dm.getDiaobsqdh());

	}

	/**
	 * 按零件终止-测试
	 */
	@Test
	public void lingTest() {

		diaobsqmx.setDiaobsqdh("S2210001");
		diaobsqmx.setLingjbh("7540759080");
		ArrayList<Diaobsqmx> mx = new ArrayList<Diaobsqmx>();
		mx.add(diaobsqmx);
		// 改状态
		diaobshService.lingjStop(mx);
		// 查询
		List<Diaobmx> dm = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobsqTest.selDbmx", map);
		for (int i = 0; i < dm.size(); i++) {

			org.junit.Assert.assertEquals(Const.DIAOBL_ZT_STOPPED, dm.get(i).getZhuangt());
		}

	}

	/**
	 * 调拨申请明细查询-测试
	 */
	@Test
	public void selTest() {
		List ls = (List) diaobshService.select(diaobsq).get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx ds = (Diaobsqmx) ls.get(i);
			org.junit.Assert.assertEquals(diaobsq.getUsercenter(), ds.getUsercenter());
			org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), ds.getDiaobsqdh());
		}

	}

	/**
	 * 调拨申请明细终止-测试
	 */
	@Test
	public void zhongziTest() {
		map.put("diaobsqdh", "S2210001");
		diaobsq.setDiaobsqdh("S2210001");
		// 查询调拨明细
		List<Diaobmx> dm = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobsqTest.selDbmx", map);
		for (int i = 0; i < dm.size(); i++) {
			diaobshService.updateZhongzi(dm.get(i));
		}
		List<Diaobmx> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobsqTest.selDbmx", map);
		for (int i = 0; i < ls.size(); i++) {
			Diaobmx ds = (Diaobmx) ls.get(i);
			org.junit.Assert.assertEquals(diaobsq.getUsercenter(), ds.getUsercenter());
			org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), ds.getDiaobsqdh());
			org.junit.Assert.assertEquals(Const.DIAOBL_ZT_STOPPED, ds.getZhuangt());
		}
	}

	/**
	 * 调拨申请明细分页查询-测试
	 */
	@Test
	public void selectTest() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		// 插入之前清除该数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desq", map);
		// 插入数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.ins_passed", diaobsq);
		Map<String, Object> db = diaobshService.select(diaobsq, map);
		List<?> ls = (List<?>) db.get("rows");
		Diaobsq dmx = (Diaobsq) ls.get(0);
		org.junit.Assert.assertEquals(diaobsq.getUsercenter(), dmx.getUsercenter());
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), dmx.getDiaobsqdh());

	}

	/**
	 * 调拨申请明细实批数量汇总-测试
	 */
	@Test
	public void shipslTest() {
		map.put("diaobsqdh", "S2210001");
		diaobsq.setDiaobsqdh("S2210001");
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);
		// 插入数据
		Set<Map.Entry<String, Object>> set = map1.entrySet();
		for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			diaobshService.insertDiaobmx((Diaobmx) entry.getValue());
		}
		// 调用service方法
		Map<String, Object> sl = diaobshService.sumShipsl(diaobsq);
		List<?> ls = (List<?>) sl.get("rows");
		Diaobsqmx sqmx = (Diaobsqmx) ls.get(0);
		org.junit.Assert.assertEquals(diaobsq.getUsercenter(), sqmx.getUsercenter());
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), sqmx.getDiaobsqdh());
		org.junit.Assert.assertEquals(new BigDecimal(200), sqmx.getShipsl());
		// 先删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.detelDbmx", map);

	}

	/**
	 * 调拨申请明细实批数量汇总-测试
	 */
	@Test
	public void zhuangtTest() {

		map.put("diaobsqdh", "S2210001");
		// 清除数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		// 插入数据
		diaobsqmx.setDiaobsqdh("S2210001");
		diaobsqService.doInsertmx(diaobsqmx);
		diaobsqmx.setZhuangt(Const.DIAOBL_ZT_APPROVING);
		diaobshService.updateDiaobdhState(diaobsqmx);
		// 查询调拨申请
		diaobsq.setDiaobsqdh("S2210001");
		Diaobsq dr = diaobsqService.selectDiaobsq(diaobsq);
		org.junit.Assert.assertEquals(diaobsqmx.getZhuangt(), dr.getZhuangt());

	}

	/**
	 * 状态改变方法测试-未审核
	 */
	@Test
	public void changeTest() {
		diaobsqmx.setDiaobsqdh("S2C21036");
		diaobshService.changeState(diaobsqmx);
		// 查询调拨申请
		diaobsq.setDiaobsqdh("S2C21036");
		Diaobsq dr = diaobsqService.selectDiaobsq(diaobsq);
		// 比较状态
		org.junit.Assert.assertEquals(Const.DIAOBL_ZT_APPLYING, dr.getZhuangt());

	}

	/**
	 * 状态改变方法测试-审核中
	 */
	@Test
	public void change1Test() {
		diaobsqmx.setDiaobsqdh("S2C21037");
		diaobshService.changeState(diaobsqmx);
		// 查询调拨申请
		diaobsq.setDiaobsqdh("S2C21037");
		Diaobsq dr = diaobsqService.selectDiaobsq(diaobsq);
		// 比较状态
		org.junit.Assert.assertEquals(Const.DIAOBL_ZT_APPROVING, dr.getZhuangt());

	}

	/**
	 * 状态改变方法测试-已审核
	 */
	@Test
	public void change2Test() {
		diaobsqmx.setDiaobsqdh("S2C21038");
		diaobshService.changeState(diaobsqmx);
		// 查询调拨申请
		diaobsq.setDiaobsqdh("S2C21038");
		Diaobsq dr = diaobsqService.selectDiaobsq(diaobsq);
		// 比较状态
		org.junit.Assert.assertEquals(Const.DIAOBL_ZT_PASSED, dr.getZhuangt());

	}

	/**
	 * 调拨单号生成测试-存在
	 */
	@Test
	@TestData(locations = { "classpath:testData/xqjs/diaobsq.xls" })
	public void Generation1Test() {
		diaobmx.setDiaobsqdh("S2C21038");
		diaobmx.setCangkbh("W01");
		diaobmx.setLingjbh("7540759081");
		List<Diaobmx> ls = diaobshService.diaobdhGeneration(diaobmx);
		String dh = ls.get(0).getDiaobdh();
		org.junit.Assert.assertEquals("W00000001", dh);
	}

	/**
	 * 终止查询测试
	 */
	@Test
	public void selectZzTest() {
		diaobsq.setDiaobsqdh("S2C21036");
		Map<String, Object> sl = diaobshService.selectZhongzi(diaobsq);
		List<?> ls = (List<?>) sl.get("rows");
		Diaobsqmx sqmx = (Diaobsqmx) ls.get(0);
		org.junit.Assert.assertEquals(diaobsq.getUsercenter(), sqmx.getUsercenter());
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), sqmx.getDiaobsqdh());
		org.junit.Assert.assertEquals(new BigDecimal(699), sqmx.getShenbsl());
	}

	/**
	 * 生效测试
	 */
	@Test
	public void effectTest() {
		diaobsq.setDiaobsqdh("S2C21036");
		diaobmx.setDiaobsqdh("S2C21036");
		diaobshService.effect(diaobmx, diaobsq);
		diaobshService.selectZhongzi(diaobsq);
		diaobshService.selectZhongzimx(diaobsqmx);

	}

}

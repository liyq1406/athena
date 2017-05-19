package com.athena.xqjs.module.diaobl;

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
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.diaobl.service.DiaobsqService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@TestData(locations = { "classpath:testData/xqjs/diaobsq.xls" })
public class DiaobsqTest extends AbstractCompomentTests {

	@Inject
	private DiaobsqService diaobsqService;

	@Inject
	private DiaobshService diaobshService;

	@Inject
	private AbstractIBatisDao baseDao;

	private Diaobsq diaobsq;

	private Diaobsqmx diaobsqmx;

	private Map<String, String> map;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			String diaobsqdh = "S2210001";
			diaobsq = new Diaobsq();
			// 数据准备
			diaobsq.setDiaobsqdh(diaobsqdh);
			diaobsq.setBanc("0001");
			diaobsq.setUsercenter("UW");
			diaobsq.setDiaobsqsj(CommonFun.getJavaTime().substring(0, 10));
			diaobsq.setChengbzx("klll");
			diaobsq.setHuijkm("wh");
			diaobsq.setZhuangt("00");
			diaobsq.setBeiz("ok");
			diaobsq.setCreate_time(CommonFun.getJavaTime());
			diaobsq.setCreator("001");
			diaobsq.setEdit_time(CommonFun.getJavaTime());
			diaobsq.setEditor("001");
			// 调拨申请明细
			diaobsqmx = new Diaobsqmx();
			diaobsqmx.setUsercenter("UW");
			diaobsqmx.setDiaobsqdh(diaobsq.getDiaobsqdh());
			diaobsqmx.setLingjbh("7540759080");
			diaobsqmx.setLux("97W");
			diaobsqmx.setShenbsl(new BigDecimal(100));
			diaobsqmx.setCreate_time(diaobsq.getDiaobsqsj());
			diaobsqmx.setCreator("001");
			diaobsqmx.setEdit_time(diaobsq.getDiaobsqsj());
			diaobsqmx.setEditor("001");
			// Map数据准备
			map = new HashMap<String, String>();
			map.put("usercenter", diaobsqmx.getUsercenter());
			map.put("diaobsqdh", diaobsq.getDiaobsqdh());
			map.put("lingjbh", diaobsqmx.getLingjbh());
			map.put("lux", diaobsqmx.getLux());
		};
	};

	/**
	 * 联合方法测试 插入调拨申请 插入调拨申请明细
	 */
	/*
	 * @Test public void sqFalseTest(){ List<Diaobsqmx> ls = new
	 * ArrayList<Diaobsqmx>(); ls.add(diaobsqmx); boolean flag =
	 * diaobsqService.sqInsert(diaobsq, ls);
	 * org.junit.Assert.assertEquals(false,flag); }
	 */

	/**
	 * 联合方法测试 插入调拨申请 插入调拨申请明细
	 */
	@Test
	public void sqTest() {

		this.clearTest();
		List<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
		ls.add(diaobsqmx);
		List<Diaobsq> dbsq = new ArrayList<Diaobsq>();
		dbsq.add(diaobsq);
		diaobsqService.sqInsert(dbsq, ls);
		Diaobsq test1 = diaobsqService.selectDiaobsq(diaobsq);
		// 是否插入成功
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), test1.getDiaobsqdh());
		org.junit.Assert.assertEquals(diaobsq.getUsercenter(), test1.getUsercenter());
		Diaobsqmx test2 = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", map);
		// 比较用户中心是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getUsercenter(), test2.getUsercenter());
		// 比较调拨单号是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getDiaobsqdh(), test2.getDiaobsqdh());
		// 比较零件号是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getLingjbh(), test2.getLingjbh());
	}

	/**
	 * 联合方法测试 插入调拨申请 插入调拨申请明细 调拨明细插入失败
	 */
	/*
	 * @Test public void sq2Test(){
	 * 
	 * this.clearTest(); List<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
	 * Diaobsqmx diaobsqmx1 = new Diaobsqmx(); ls.add(diaobsqmx1); boolean flag
	 * = diaobsqService.sqInsert(diaobsq, ls);
	 * org.junit.Assert.assertEquals(false,flag); }
	 */

	/**
	 * 删除插入的数据
	 */
	@Test
	public void clearTest() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		// 插入之前清除该数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desq", map);
	}

	/**
	 * 
	 * diaobsqTest
	 * 
	 * 调拨令申请测试 新增调拨申请、调拨申请明细
	 * 
	 * @return
	 */
	@Test
	public void diaobsqTest() {

		diaobsqService.doInsert(diaobsq);
		Diaobsq test1 = diaobsqService.selectDiaobsq(diaobsq);
		// 是否插入成功
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), test1.getDiaobsqdh());
		org.junit.Assert.assertEquals(diaobsq.getUsercenter(), test1.getUsercenter());

	}

	/**
	 * 
	 * diaobsqTest
	 * 
	 * 调拨令申请查询测试
	 * 
	 * @return
	 */
	@Test
	public void sqQRTest() {
		Diaobsq test1 = diaobsqService.selectDiaobsq(diaobsq);
		// 比较
		org.junit.Assert.assertEquals(diaobsq.getDiaobsqdh(), test1.getDiaobsqdh());
	}

	@Test
	public void insMxTest() {
		this.clearTest();
		// 插入调拨明细
		diaobsqService.doInsertmx(diaobsqmx);
		// 查询该条插入记录
		Diaobsqmx test2 = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", map);
		// 比较用户中心是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getUsercenter(), test2.getUsercenter());
		// 比较调拨单号是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getDiaobsqdh(), test2.getDiaobsqdh());
		// 比较零件号是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getLingjbh(), test2.getLingjbh());
		// 比较路线是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getLux(), test2.getLux());
		// 申报数量是否相同
		org.junit.Assert.assertEquals(diaobsqmx.getShenbsl(), test2.getShenbsl());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desq", map);
	}

	@Test
	public void getLingjmcTest() {
		// 是否查询到零件号
		Diaobsqmx d = diaobsqService.selectLingjmc(map);
		org.junit.Assert.assertNotNull(d.getLingjmc());
		System.out.println(d.getLingjmc());
	}

	@Test
	public void dhGeneration() {
		String sqdh = diaobsqService.getdiaosqdh("UW");
		// 获取当前时间
		String time = CommonFun.getJavaTime();
		String mon = time.substring(5, 7);
		// 月份转换为16进制
		String month = Integer.toHexString(Integer.parseInt(mon)).toUpperCase();
		String exp = "SC" + month + time.substring(8, 10) + "001";
		org.junit.Assert.assertEquals(exp, sqdh);
		diaobsq.setDiaobsqdh(sqdh);
		diaobsq.setUsercenter("UW");
		diaobsqService.doInsert(diaobsq);
	}

	/**
	 * 当天存在调拨申请单号-测试递增
	 */
	@Test
	public void dhGeneration2() {
		String sqdh = diaobsqService.getdiaosqdh("UW");
		// 获取当前时间
		String time = CommonFun.getJavaTime();
		String mon = time.substring(5, 7);
		// 月份转换为16进制
		String month = Integer.toHexString(Integer.parseInt(mon)).toUpperCase();
		String exp = "SC" + month + time.substring(8, 10) + "002";
		org.junit.Assert.assertEquals(exp, sqdh);
	}

	@Test
	public void demoTest() {
		BigDecimal a = new BigDecimal(66).divide(new BigDecimal(5)).setScale(1, BigDecimal.ROUND_UP);
		System.out.println(a);
		double b = 66 / (double) 5;
		System.out.println(b);
		System.out.println(CommonFun.getJavaTime());
		double xuH = b < 0 ? 0 : b;
		System.out.println(xuH);
	}

	/**
	 * 零件名称为空时
	 */
	@Test
	public void lingjmcTest() {
		map.put("lingjbh", "445522sss");
		// 是否查询到零件号
		try {
			diaobsqService.selectLingjmc(map);
		} catch (Exception e) {
			System.out.println(e.toString());
			org.junit.Assert.assertEquals(null, e.getCause());
		}
	}
}

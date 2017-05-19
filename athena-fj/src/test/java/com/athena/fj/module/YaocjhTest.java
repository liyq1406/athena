package com.athena.fj.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.WarpCelZ;
import com.athena.fj.entity.WarpLXZ;
import com.athena.fj.entity.WrapCacheData;
import com.athena.fj.entity.WrapCelL;
import com.athena.fj.entity.YaoCJhMx;
import com.athena.fj.module.service.YaocjhService;
import com.toft.core3.container.annotation.Inject;

/**
 * 要车计划Test类
 * 
 * @author 贺志国(hezhiguo)
 * @date 创建日期 2011-12-8
 * @Email:zghe@isoftstone.com
 */
public class YaocjhTest extends AbstractCompomentTests {

	@Inject
	private YaocjhService ycjhService;
	private Map<String, String> params;
	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			params = new HashMap<String, String>();
			params.put("DATE", "2012-01-14 23:59:59");
			params.put("KEH", "WUHAN");
			params.put("GYSDM", "CYS001");
			params.put("KSSJ", "2012-01-14 08:00");
			params.put("JSSJ", "2012-01-14 14:00");
			params.put("UC", "UW");

		};
	};

	/* 测式selectJihgz方法 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocmx.xls" })
	public void testSelectJihgz() {
		List<Map<String,Object>> listObj = ycjhService.selectJihgz(params);
		assertEquals("LX1",listObj.get(0).get("YUNSLX"));
		assertEquals("UW",listObj.get(0).get("USERCENTER"));
	}

	/***************** BY 王冲 date :2012-02-09 *****************************/
	/* 更新要货令表中的发运时间 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocjh.xls" })
	public void testUpdateFaYSJ() {
		int i = ycjhService.updateFaYSJ(params);
		assertEquals(2, i);
	}

	/* 归集客户供应商 */
	@Test
	public void testSelectKehGys() {
		List<Map<String, String>> clientList = ycjhService.selectKehGYS(params);
		assertEquals("ZHENGZ", clientList.get(0).get("KEH"));
		assertEquals("CYS002", clientList.get(0).get("GONGYSDM"));
	}

	/* 归集路线组 */
	@Test
	public void testSelectLuXZ() {
		List<Map<String, String>> clientList = ycjhService.selectLuXZ(params);
		assertEquals("LX1", clientList.get(0).get("YUNSLXBH"));
		assertEquals("WUHAN", clientList.get(0).get("KEHBH"));
		assertEquals("C01", clientList.get(0).get("CANGKBH"));
		assertEquals("CYS001", clientList.get(0).get("CHENGYSBH"));
		assertEquals("10", clientList.get(0).get("ZUIDTQFYSJ"));
	}

	/* 归集路线组交付时刻 */
	@Test
	public void testSelectJiaoFSK() {
		List<Map<String, String>> clientList = ycjhService
				.selectJiaoFSK(params);
		assertEquals("LX1", clientList.get(0).get("YUNSLXBH"));
		assertEquals("08:00", clientList.get(0).get("JIAOFSK"));
	}

	/**
	 * 测式归集车辆申报资源
	 */
	@Test
	public void testSelectCLBySB() {
		List<Map<String, String>> clientList = ycjhService.selectCLBySB(params);
		assertEquals("CYS001", clientList.get(0).get("YUNSSBM"));
		assertEquals("DC", clientList.get(0).get("CHEX"));
		assertEquals("2", clientList.get(0).get("SHUL"));
		assertEquals("5", clientList.get(0).get("ZUIDSL"));
	}

	/**
	 * 测式归集用户中心车辆资源
	 */
	@Test
	public void testSelectCLByUC() {
		List<Map<String, String>> clientList = ycjhService.selectCLByUC(params);
		assertEquals("CYS001", clientList.get(0).get("YUNSSBM"));
		assertEquals("DC", clientList.get(0).get("CHEX"));
		assertEquals("2", clientList.get(0).get("SHUL"));
		assertEquals("5", clientList.get(0).get("ZUIDSL"));
	}

	/**
	 * 测式归集配载策略
	 */
	@Test
	public void testSelectCelZ() {
		List<Map<String, String>> clientList = ycjhService.selectCelZ(params);
		assertEquals("CL0001", clientList.get(0).get("CELBH"));
		assertEquals("A01", clientList.get(0).get("BAOZZBH"));
		assertEquals("3", clientList.get(0).get("BAOZSL"));
		assertEquals("3", clientList.get(0).get("BAOZBSJS"));
	}

	/**
	 * 测式归集要贷令号
	 */
	@Test
	public void testSelectYaoHL() {
		List<HashMap<String, String>> clientList = ycjhService.selectYaoHL(params);
		assertEquals("2501", clientList.get(0).get("BAOZXH"));
		assertEquals("A01", clientList.get(0).get("BAOZZBH"));
		assertEquals("20003", clientList.get(0).get("YAOHLH"));
		assertEquals("95001", clientList.get(0).get("LINGJBH"));
	}

	/**
	 * 测式归集用户中心
	 */
	@Test
	public void testSelectUc() {
		List<Map<String, String>> clientList = ycjhService.selectUc();
		assertEquals("UW", clientList.get(0).get("USERCENTER"));
	}
	/**
	 * 测式归集仓库
	 */
	@Test
	public void testSelectCK() {
		List<Map<String, String>> clientList = ycjhService.selectLXZCK(params) ;
		assertEquals("LX1", clientList.get(0).get("YUNSLXBH"));
		assertEquals("C01", clientList.get(0).get("CANGKBH"));
	}
	/**
	 * 测式封装仓库
	 */
	@Test
	public void testWarpLXZCK() {
		List<Map<String, String>> clientList = ycjhService.selectLXZCK(params) ;
		Map<String, HashSet<String>> warp = ycjhService.wrapLXZCK(clientList);

		HashSet<String> lxs = warp.get("LX1");
		assertEquals("[C01]", lxs.toString());
	}

	/**
	 * 测式封装路线组
	 */
	@Test
	public void testWarpLXZ() {
		List<Map<String, String>> kehGys = ycjhService.selectKehGYS(params);
		List<Map<String, String>> lxzList = ycjhService.selectLuXZ(params);
		Map<String, ArrayList<WarpLXZ>> warp = ycjhService.getWarpLXZ(kehGys, lxzList);

		List<WarpLXZ> lxs = warp.get("CYS001:WUHAN");
		assertEquals("C01", lxs.get(0).getCkbh());
		assertEquals("LX1", lxs.get(0).getLzxbh());
		assertEquals("WUHAN", lxs.get(0).getKeh());
		assertEquals("CYS001", lxs.get(0).getGysdm());
	}

	/**
	 * 测式封装路线组交付时刻
	 */
	@Test
	public void testWarpJiaoFSK() {
		List<Map<String, String>> clientList = ycjhService
				.selectJiaoFSK(params);
		Map<String, ArrayList<LinkedList<String>>> warp = ycjhService.warpJiaoFSK(clientList, "2012-01-14");
		List<LinkedList<String>> lxs = warp.get("LX1");
		assertEquals("2012-01-14 08:00", lxs.get(0).getFirst());
		assertEquals("2012-01-14 09:00", lxs.get(0).getLast());
	}

	/**
	 * 测式封装策略组
	 */
	@Test
	public void testRepeatCelZ() {
		List<Map<String, String>> clientList = ycjhService.selectCelZ(params);
		Map<String, WarpCelZ> map = ycjhService.getWarpCelZ(clientList);
		WarpCelZ cl = map.get("CL0001");
		assertEquals("CL0001", cl.getCelH());
		assertEquals("DC", cl.getCx());
		assertEquals("大车", cl.getCxName());

	}

	/***
	 * 测式封装车辆资源
	 */
	@Test
	public void testWarpCelL() {
		List<Map<String, String>> clientList = ycjhService.selectCLBySB(params);
		Map<String, WrapCelL> map = ycjhService.warpCelL(clientList);
		WrapCelL cel = map.get("CYS003:XC");
		assertEquals("XC", cel.getClLx());
		assertEquals("CYS003", cel.getCys());
		assertEquals(1, cel.getSbSl());
		assertEquals(0, cel.getSysl());
		assertEquals(5, cel.getZdsl());

	}


	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-28
	 * @time 下午02:56:01
	 * @description 测试根据路线组的最大提前期，归集要货令，及装车 ,混装归集
	 */
	@SuppressWarnings("unused")
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocjh_SJGJ.xls" })
	public void testSJGJ() {

		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertTrue(true);

	}


	/* 测式接口 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocjh.xls" })
	public void testInterface() {
		// 生成明细
		ycjhService.createYaoHLJhMx();
		assertTrue(true);
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-28
	 * @time 下午02:59:35
	 * @description 测试生成要车明细，及 混装
	 */
	@SuppressWarnings("unused")
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocjh.xls" })
	public void testShengCYaoCJHmx() {

		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW","2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	
	public static void prl(List<YaoCJhMx> y) {
	for (YaoCJhMx mx : y) {
		System.out.print("备注:" + mx.getNote() + ",");
		System.out.print("车型:" + mx.getCllx() + ":");
		System.out.print("运输商:" + mx.getCys() + ":");
		System.out.print("策略编号:" + mx.getCelbh() + ":");
		System.out.print("仓库编号:" + mx.getLxz().getCkbh() + ":");
		System.out.print("客户:" + mx.getKhbm() + ":");
		System.out.print("车名:" + mx.getClName() + ":");
		System.out.print("路线编号:" + mx.getLxz().getLzxbh() + ",");
		System.out.print("发贷时间:" + mx.getFysj() + ",");
		System.out.print("要车时间:" + mx.getYcsj() + ",");
		System.out.print("计划包装数:" + mx.getJhsl() + ",");
		System.out.print("实际包装数:" + (mx.getSjsl()) + ",");
		System.out.print("装载率:" + (mx.getZmy()) + ",");
		System.out.print("是否装满:" + (mx.getSfzm()) + ",");
		System.out.print("要贷令号:" + mx.getYhlbh().size() + ",");
		Iterator<String> blxs = mx.getYhlbh().iterator();
		while (blxs.hasNext()) {
			System.out.print(blxs.next() + ",");
		}
		System.out.print("包装号:" + mx.getBzbh().size() + ",");
		Iterator<String> bzlxs = mx.getBzbh().keySet().iterator();
		while (bzlxs.hasNext()) {
			String bzn = bzlxs.next();
			// System.out.print(bzn+",") ;
			System.out.print(bzn + ":" + mx.getBzbh().get(bzn) + ",");
		}
		System.out.print("零件号:" + mx.getLinj().size() + ",");
		Iterator<String> ljs = mx.getLinj().keySet().iterator();
		while (ljs.hasNext()) {
			String lj = ljs.next();
			System.out.print(lj + ":" + mx.getLinj().get(lj) + ",");
		}
		System.out.println();

	}
}
	

}

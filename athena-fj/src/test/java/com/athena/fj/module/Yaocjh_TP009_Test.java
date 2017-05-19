package com.athena.fj.module;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.WrapCacheData;
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
public class Yaocjh_TP009_Test extends AbstractCompomentTests {

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

	/***************** BY 王冲 date :2012-02-09 *****************************/
	
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
	
	/**
	 * 路线组为空
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c001.xls" })
	@Test
	public void testLxzIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	/**
	 * 路线组交付时刻为空
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c002.xls" })
	@Test
	public void testJiaoFSKIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	/**
	 * 车辆资源为空 c003,c008
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c003.xls" })
	@Test
	public void testJiaoCLZYIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	
	/**
	 * 配载策略为空
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c004.xls" })
	@Test
	public void testJiaoPZCLIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	/**
	 * 根据包装组基数,如果能找到相应的包装数,则生成一条要车明细
	 */
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c005.xls" })
	@Test
	public void testJiaoBZJSYCMX() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertEquals("CL0002", cache.getYaoMZ().get(0).getCelbh());
	}
	
	/**
	 * 配载策略包装组与要货令的包组不相同，则进入下一包装组匹配
	 */
	@SuppressWarnings("unused")
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c006.xls" })
	public void testJiaoBZJSNextBzz() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	/**
	 * 根据包装组基数,如果不能找到想就的包装数,则此包装组未装满 不能生成要车明累
	 */
	@SuppressWarnings("unused")
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c007.xls" })
	public void testJiaoBZJS() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	/**
	 * 生成的要车明细的要货令不能来自于两个仓库
	 */
	@SuppressWarnings("unused")
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c009.xls" })
	public void testCk() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	/**
	 * 如果该时刻有未装满的要车明细,则根据路线组最大提前期，归集要货令，并进行要车计算
	 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c010.xls" })
	public void testHasWMZ() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertEquals(0, cache.getYaoHz().size());
		
	}

}

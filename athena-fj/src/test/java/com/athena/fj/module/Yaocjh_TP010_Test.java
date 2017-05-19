package com.athena.fj.module;

import static org.junit.Assert.assertEquals;


import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.WrapCacheData;
import com.athena.fj.module.service.YaocjhService;
import com.toft.core3.container.annotation.Inject;

/**
 * 要车计划Test类
 * 
 * @author 贺志国(hezhiguo)
 * @date 创建日期 2011-12-8
 * @Email:zghe@isoftstone.com
 */
public class Yaocjh_TP010_Test extends AbstractCompomentTests {

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

	
	
	/**
	 * 如果该时刻没有未装满的要车明细,则不用跨时间归集
	 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c011.xls" })
	public void testHasMZ() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertEquals(1, cache.getYaoHz().size());
		assertEquals(1, cache.getYaoMZ().size());
		
	}
	/**
	 * 如果在最后一个发运时刻点有未装满的车，则考滤第二天的第一个发运时间点归集要货令，并计算
	 */
	
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c012.xls" })
	public void testLastTimeHasWMZ() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertEquals(1, cache.getYaoMZ().size());
		
	}
	/**
	 * 如果在最后一个发运时刻点有未装满的车，则考滤第二天的第一个发运时间点归集要货令，并计算,计算完，
	 * 考滤最后一个发运时刻点至第二的的第一个发运时刻的要货令计算
	 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c013.xls" })
	public void testLastTimeHasWMZByLxztqq() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals(1, cache.getYaoHz().size());
		assertEquals(1, cache.getYaoMZ().size());

	}
	/**
	 * 如果在最后一个发运时刻点有未装满的车，则考滤第二天的第一个发运时间点归集要货令，并计算,计算完，
	 * 考滤最后一个发运时刻点至第二的的第一个发运时刻的要货令计算
	 * ,计算完后，如果还有未装满的车，则根据该路线组的最大提前期与第二天的第一天发运时间点进行要货令归集计算
	 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c014.xls" })
	public void testLastTimeHasWMZByTomowAndLxztqq() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals(1, cache.getYaoHz().size());
		assertEquals(1, cache.getYaoMZ().size());

	}
	
	/**路线组最大提前期为空**/
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c015.xls" })
	public void testLXZTQQIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals(1, cache.getYaoMZ().size());

	}	
	/** 将生成的要车明细的发运时刻与客户的要车提前前期进行计算得出要车时间
	 * ，如果路线组的最大提前期为空，或者要提前期为空,则要车时间=发运时间 
	 * C015
	 * **/
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c016.xls" })
	public void testYCTQQIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals("2012-01-14 08:00", cache.getYaoMZ().get(0).getFysj());
		assertEquals("2012-01-14 08:00", cache.getYaoMZ().get(0).getYcsj());
		assertEquals(1, cache.getYaoMZ().size());
	}
	/** 将生成的要车明细的发运时刻与客户的要车提前前期进行计算得出要车时间
	 * ，如果路线组的最大提前期为空，或者要提前期为空,则要车时间=发运时间-要车提前期 
	 * C016
	 * **/
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c017.xls" })
	public void testYCTQQIsNotNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals("2012-01-14 08:00", cache.getYaoMZ().get(0).getFysj());
		assertEquals("2012-01-14 07:40", cache.getYaoMZ().get(0).getYcsj());
		assertEquals(1, cache.getYaoMZ().size());
	}
	/**
	 * 混装  TP010  C001
	 * 将未装满的车辆按同一路线组,同一运输时刻进行混装,如果混装的车辆数量少于同等条件下未装满车的数量,则混装
	 * **/
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c018.xls" })
	public void testLessHZ() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals(0, cache.getYaoMZ().size());
		assertEquals(1, cache.getYaoHz().get("2012-01-14 08:00").get("C01").size());
	}
	
	/**
	 * 混装  TP010  C002
	 * 将未装满的车辆按同一路线组,同一运输时刻进行混装,如果混装的车辆数量大于或
	 * 等于同等条件下未装满车的数量,则不混装
	 * **/
	@Test
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_c019.xls" })
	public void testMoreHZ() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
				"2012-01-14 23:59:59");
		assertEquals(0, cache.getYaoMZ().size());
		assertEquals(2, cache.getYaoHz().get("2012-01-14 08:00").get("C01").size());
	}
	

}

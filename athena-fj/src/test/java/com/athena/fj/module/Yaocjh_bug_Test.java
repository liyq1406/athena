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
public class Yaocjh_bug_Test extends AbstractCompomentTests {

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
	
	
	/**
	 * 在混装计算前，保存已用过的配载策略 (bug:0000433   tp012 c001)
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/yaocjh.xls" })
	@Test
	public void testLxzIsNull() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-01-14 23:59:59");
		assertTrue(true);
	}
	
	/**
	 * 在混装计算前，测试倍数基数 (bug:0000460     tp009 c005     )
	 */
	@SuppressWarnings("unused")
	@TestData(locations = { "classpath:testdata/fj/ycjh/yaocjh_0000460.xls" })
	@Test
	public void testJs() {
		WrapCacheData cache = ycjhService.shengCYaoCJHmx("UW",
		"2012-04-17 23:59:59");
		assertTrue(true);
	}

}

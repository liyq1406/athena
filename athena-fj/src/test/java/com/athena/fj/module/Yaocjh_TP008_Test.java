package com.athena.fj.module;


import static org.junit.Assert.*;
import org.junit.Test;

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
public class Yaocjh_TP008_Test extends AbstractCompomentTests {

	@Inject
	private YaocjhService ycjhService;


	/***************** BY 王冲 date :2012-02-09 *****************************/

	/* 测式接口,  如果没有供应商-客户 ,则终止执行 */
	@Test
	@TestData(locations = { "classpath:testdata/fj/yaocjh.xls" })
	public void testShengCYaoCJHmx() {

		WrapCacheData cache = 	ycjhService.shengCYaoCJHmx("UW",
				"2011-01-11 23:59:59");
		assertEquals(null,cache);
	}
	
	

}

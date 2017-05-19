package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.module.xuqjs.service.TicxxsjService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 剔除休息时间
 * @author denggq
 * @date 2012-4-12
 */
@Component
public class TicxxsjTest extends AbstractCompomentTests{
	
	@Inject
	private TicxxsjService service;

	@Test
	public void test(){
		service.calculate("test");//生成所有用户中心的剔除休息日的工作时间
//		service.calculateGongzsjmb();//生成所有用户中心的剔除休息日的工作时间
	}
	
}

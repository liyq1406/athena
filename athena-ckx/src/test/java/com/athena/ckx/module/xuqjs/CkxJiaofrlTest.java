package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.module.xuqjs.service.CkxJiaofrlService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 交付日历
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class CkxJiaofrlTest extends AbstractCompomentTests{
	
	@Inject
	private CkxJiaofrlService service;

	@Test
	public void test(){
		service.calculate("UW", "C", "test");
	}
	
}

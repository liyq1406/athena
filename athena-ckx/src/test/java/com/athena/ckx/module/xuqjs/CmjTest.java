package com.athena.ckx.module.xuqjs;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.module.xuqjs.service.CmjService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description CMJ
 * @author denggq
 * @date 2012-4-20
 */

@Component
public class CmjTest extends AbstractCompomentTests{
	
	@Inject
	private CmjService service;
	
	//日志
	private static final Logger logger = Logger.getLogger(CkxGongyxhdTest.class);

	@Test
	public void test(){
		logger.info("-------------test------------");
		service.calculateCmj("test");
	}
	
}

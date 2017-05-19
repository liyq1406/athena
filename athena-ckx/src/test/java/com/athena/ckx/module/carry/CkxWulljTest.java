package com.athena.ckx.module.carry;

import org.junit.Test;

import com.athena.ckx.module.carry.service.CkxWulljService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxWulljTest extends AbstractCompomentTests {
	@Inject
	private CkxWulljService service;
	 @Test
	 public void test(){
		service.addWullj("ROOT");
	 }
}

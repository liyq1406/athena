package com.athena.ckx.module;

import org.junit.Test;

import com.athena.ckx.util.Impl.CkxCommonFuncImpl;
import com.athena.component.test.AbstractCompomentTests;

import com.toft.core3.container.annotation.Inject;

public class DataTest extends AbstractCompomentTests{
	@Inject
	private CkxCommonFuncImpl ckxCommonFuncImpl;
	@Test
	public void init(){
		ckxCommonFuncImpl.calulateHebTime();
	}

}

package com.athena.xqjs.anxorder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
public class AnxCheckChaifsjTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	
	/**
	 * 检查拆分时间
	 * @throws Exception 
	 * **/
	@Test
	public void testAnxTimeCount() throws Exception{
		Anxjscszjb bean = new Anxjscszjb() ;
		bean.setChaifsj("2012-04-17 10:20:12")  ;
		boolean flag = this.anxOrderService.checkChaifsj(bean,new LoginUser()) ;
		assertEquals(flag,false) ;
	}
}

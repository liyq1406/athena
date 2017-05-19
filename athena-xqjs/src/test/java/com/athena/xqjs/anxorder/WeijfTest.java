package com.athena.xqjs.anxorder;

import java.math.BigDecimal;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
@TestData(locations = {"classpath:testData/anxorder/Weijf.xls"})
public class WeijfTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	
	/**
	 * 未交付数量
	 * @throws Exception 
	 * **/
	@Test
	public void testGetWeijf() throws Exception{
		Anxjscszjb bean = new Anxjscszjb() ;
		bean.setBeihzq(new BigDecimal("2")) ;
		bean.setYunszq(new BigDecimal("6")) ;
		bean.setUsercenter("UW") ;
		bean.setLingjbh("9683978780") ;
		bean.setXiaohd("WF113W011") ;
		bean.setYifyhlzl(new BigDecimal("100")) ;
		bean.setJiaofzl(new BigDecimal("120")) ;
		bean.setZhongzzl(new BigDecimal("160")) ;
		LoginUser user = new LoginUser() ; 
		/*BigDecimal weijf = this.anxOrderService.getWeijf(bean, "SYS");*/
		//assertEquals(weijf,BigDecimal.ZERO) ;
	}
}

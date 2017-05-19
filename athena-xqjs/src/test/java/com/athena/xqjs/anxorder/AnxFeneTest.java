package com.athena.xqjs.anxorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
public class AnxFeneTest  extends AbstractCompomentTests{
	
	@Inject
	private AnxOrderService anxOrderService;
	
	/**
	 * 未交付数量
	 * @throws Exception 
	 * **/
	@Test
	public void testAnFene() throws Exception{
		BigDecimal  zongyhl = new BigDecimal("150") ;
		Dingdmx bean = new Dingdmx() ;
		Dingdmx bean_1 = new Dingdmx() ;
		Dingdmx bean_1_1 = new Dingdmx() ;
		Dingdmx mx = new Dingdmx() ;
		Dingdmx dmx = new Dingdmx() ;
		bean.setUsercenter("UW") ;
		bean.setCangkdm("c01") ;
		bean.setLingjbh("lj001") ;
		bean.setGongyfe(new BigDecimal("0.6")) ;
		bean.setZhidgys("g01") ;
		bean.setGongysdm("g01") ;
		bean.setUabzucrl(new BigDecimal("50")) ;
		bean.setUabzucsl(new BigDecimal("10")) ;
		bean.setZuixqdl(new BigDecimal("100")) ;
		
		bean_1.setUsercenter("UW") ;
		bean_1.setCangkdm("c01") ;
		bean_1.setLingjbh("lj001") ;
		bean_1.setGongyfe(new BigDecimal("0.6")) ;
		bean_1.setZhidgys("g01") ;
		bean_1.setGongysdm("g01") ;
		bean_1.setUabzucrl(new BigDecimal("50")) ;
		bean_1.setUabzucsl(new BigDecimal("10")) ;
		bean_1.setZuixqdl(new BigDecimal("100")) ;
		
		bean_1_1.setUsercenter("UW") ;
		bean_1_1.setCangkdm("c01") ;
		bean_1_1.setLingjbh("lj001") ;
		bean_1_1.setGongyfe(new BigDecimal("0.6")) ;
		bean_1_1.setZhidgys("g02") ;
		bean_1_1.setGongysdm("g01") ;
		bean_1_1.setUabzucrl(new BigDecimal("50")) ;
		bean_1_1.setUabzucsl(new BigDecimal("10")) ;
		bean_1_1.setZuixqdl(new BigDecimal("100")) ;
		
		mx.setUsercenter("UW") ;
		mx.setCangkdm("c01") ;
		mx.setLingjbh("lj002") ;
		mx.setGongyfe(new BigDecimal("0.6")) ;
		mx.setZhidgys("g01") ;
		mx.setGongysdm("g01") ;
		mx.setUabzucrl(new BigDecimal("50")) ;
		mx.setUabzucsl(new BigDecimal("10")) ;
		mx.setZuixqdl(new BigDecimal("100")) ;
		
		dmx.setUsercenter("UW") ;
		dmx.setCangkdm("c02") ;
		dmx.setLingjbh("lj002") ;
		dmx.setGongyfe(new BigDecimal("0.6")) ;
		dmx.setZhidgys("g01") ;
		dmx.setGongysdm("g01") ;
		dmx.setUabzucrl(new BigDecimal("50")) ;
		dmx.setUabzucsl(new BigDecimal("10")) ;
		dmx.setZuixqdl(new BigDecimal("100")) ;
		
		List<Dingdmx> all = new ArrayList<Dingdmx>() ;
		all.add(bean_1_1) ;
		all.add(bean_1) ;
		all.add(bean) ;
		all.add(dmx) ;
		all.add(mx) ;
		for (Dingdmx dingdmx : all) {
			//Map<String, BigDecimal> map  = this.anxOrderService.fenejs(dingdmx, zongyhl, "97W") ;
			BigDecimal sl  = this.anxOrderService.fenejs(dingdmx, zongyhl) ;
		}
	}
}

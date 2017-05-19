package com.athena.xqjs.kdorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.kdorder.service.KdOrderService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/kdOrder/Rouxbl.xls" })
public class RouxblCheckTest extends AbstractCompomentTests {

	@Inject
	private KdOrderService kdOrderService;
	
	@Test
	public void testRouxblCheck() throws Exception {
		
		Dingdlj bean1 = new Dingdlj() ;
		Dingdlj bean2 = new Dingdlj() ;
		Dingdlj bean3 = new Dingdlj() ;
		Dingdlj bean4= new Dingdlj() ;
		Dingdlj bean5= new Dingdlj() ;
		
		bean1.setDingdh("11P01003") ;
		bean1.setLingjbh("JL123459") ;
		bean1.setUsercenter("UL") ;
		bean1.setP0sl(new BigDecimal("100")) ;
		bean1.setP1sl(new BigDecimal("320")) ;
		bean1.setP2sl(new BigDecimal("210")) ;
		bean1.setP3sl(new BigDecimal("200")) ;
		bean1.setP4sl(new BigDecimal("250")) ;
		bean1.setP0fyzqxh("201209") ;
		bean1.setGongysdm("G4") ;
		

		
		bean2.setDingdh("11P01001") ;
		bean2.setLingjbh("JL123457") ;
		bean2.setUsercenter("UL") ;
		bean2.setP0sl(new BigDecimal("100")) ;
		bean2.setP1sl(new BigDecimal("320")) ;
		bean2.setP2sl(new BigDecimal("210")) ;
		bean2.setP3sl(new BigDecimal("200")) ;
		bean2.setP4sl(new BigDecimal("175")) ;
		bean2.setP0fyzqxh("201207") ;
		bean2.setGongysdm("G2") ;
		
		bean3.setDingdh("11P01002") ;
		bean3.setLingjbh("JL123458") ;
		bean3.setUsercenter("UL") ;
		bean3.setP0sl(new BigDecimal("100")) ;
		bean3.setP1sl(new BigDecimal("320")) ;
		bean3.setP2sl(new BigDecimal("210")) ;
		bean3.setP3sl(new BigDecimal("200")) ;
		bean3.setP4sl(new BigDecimal("150")) ;
		bean3.setP0fyzqxh("201208") ;
		bean3.setGongysdm("G3") ;
		
		bean4.setDingdh("11P01004") ;
		bean4.setLingjbh("JL123460") ;
		bean4.setUsercenter("UL") ;
		bean4.setP0sl(new BigDecimal("100")) ;
		bean4.setP1sl(new BigDecimal("320")) ;
		bean4.setP2sl(new BigDecimal("210")) ;
		bean4.setP3sl(new BigDecimal("200")) ;
		bean4.setP4sl(new BigDecimal("550")) ;
		bean4.setP0fyzqxh("201210") ;
		bean4.setGongysdm("G5") ;
		
		bean5.setDingdh("11P01005") ;
		bean5.setLingjbh("JL123461") ;
		bean5.setUsercenter("UL") ;
		bean5.setP0sl(new BigDecimal("100")) ;
		bean5.setP1sl(new BigDecimal("320")) ;
		bean5.setP2sl(new BigDecimal("210")) ;
		bean5.setP3sl(new BigDecimal("200")) ;
		bean5.setP4sl(new BigDecimal("180")) ;
		bean5.setP0fyzqxh("201211") ;
		bean5.setGongysdm("G6") ;
		
		List<Dingdlj> list = new ArrayList<Dingdlj>() ;
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);
		list.add(bean4);
		list.add(bean5);
		
		for (Dingdlj obj:list) {
			//this.kdOrderService.rouxblCheck(obj) ;
		}
		
	}

}

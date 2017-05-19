package com.athena.xqjs.ilorder;

import java.math.BigDecimal;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.ilorder.service.DayOrderCountService;
import com.toft.core3.container.annotation.Inject;


@TestData(locations = { "classpath:testData/ilOrder/DayOrderCountService.xls" })
public class DayCountServiceTest  extends AbstractCompomentTests{
	
	@Inject
	private DayOrderCountService dayOrderCountService ;
	
	@Test
	public void testDayCountQuery()throws Exception{
		
		Dingdlj dingdlj = new Dingdlj() ;
		dingdlj.setId("1") ;
		dingdlj.setDingdh("ddh001") ;
		dingdlj.setLingjbh("lj1") ;
		dingdlj.setGongysdm("A") ;
		dingdlj.setCangkdm("W01") ;
		dingdlj.setGongyslx("1") ;
		dingdlj.setUsercenter("UL") ;
		dingdlj.setDingdnr("9988") ;
		dingdlj.setP0sl(new BigDecimal("700")) ;
		dingdlj.setJiaofm("100") ;
		dingdlj.setP1sl(new BigDecimal("1200")) ;
		dingdlj.setUabzucsl(new BigDecimal("10")) ;
		dingdlj.setUabzucrl(new BigDecimal("20")) ;
		dingdlj.setDinghcj("d01") ;
		dingdlj.setDanw("T") ;
		dingdlj.setDingdzzsj("2012-02-23") ;
		dingdlj.setZiyhqrq("2012-02-12") ;
		dingdlj.setDingdzt("1") ;
		dingdlj.setYugsfqz("1") ;
		dingdlj.setP0fyzqxh("201101");
		String [] partten = {"pp","pj","ps"} ;
		for(String ms:partten){
			//this.dayOrderCountService.countDayOrder(dingdlj,ms) ;
		}
	}
	
}
	

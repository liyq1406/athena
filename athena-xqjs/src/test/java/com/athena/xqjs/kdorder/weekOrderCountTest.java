package com.athena.xqjs.kdorder;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.kdorder.service.KdOrderService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/kdOrder/WeekOrderCount.xls" })
public class weekOrderCountTest extends AbstractCompomentTests {

	@Inject
	private KdOrderService kdOrderService;
	@Inject
	private DingdmxService dingdmxService;
	
	@Test

	public void testWeekOrderCount() throws Exception {
		Map<String,String> map = new HashMap<String,String>() ;
		Dingdlj bean = new Dingdlj() ;
		bean.setDingdnr("9988") ;
		bean.setP0fyzqxh("201210") ;
		bean.setUsercenter("UL") ;
		bean.setLingjbh("ZQ80571780") ;
		bean.setP0sl(new BigDecimal("150")) ;
		bean.setP1sl(new BigDecimal("100")) ;
		bean.setP2sl(new BigDecimal("260")) ;
		bean.setP3sl(new BigDecimal("300")) ;
		bean.setP4sl(new BigDecimal("80")) ;
		bean.setDingdh("d001") ;
		//this.kdOrderService.weekOrderCount(bean,"123") ;
		map.put("lingjbh","ZQ80571780") ;
		map.put("usercenter","UL") ;
		map.put("dingdh","d001") ;
		List<Dingdmx> all = this.dingdmxService.queryListMx(map);
		for (Dingdmx dingdmx : all) {
			assertEquals(75,dingdmx.getShul().intValue());
		}
		
	}

}

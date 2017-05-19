package com.athena.xqjs.anxorder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.athena.xqjs.module.anxorder.service.AnxjscszjbService;
import com.toft.core3.container.annotation.Inject;
/**
 * 
 * @author 李智
 *
 */
@TestData(locations = {"classpath:testData/anxorder/ChushtjJs.xls"})
public class AnxResourceCountTest extends AbstractCompomentTests{
	@Inject
	private AnxOrderService anxOrderService;
	@Inject
	private AnxjscszjbService anxjscszjbService;
	
	//资源计算
	@Test
	public void anxResourceCountTest() throws Exception{
		this.anxOrderService.anxDataPreparation("SYS");
		Map<String,String> maps = new HashMap<String,String>();
		maps.put("usercenter", "UW") ;
		maps.put("lingjbh", "9666800081");
		maps.put("xiaohd", "xiaohd001");
		List<Anxjscszjb> anxjscszjbList = this.anxjscszjbService.queryAllAnxjscszjbList(maps) ;
		for (Anxjscszjb bean : anxjscszjbList) {
			//Map<String, BigDecimal> map = anxOrderService.anxResourceCount(bean, "SYS", "");
		}
	}
	
}

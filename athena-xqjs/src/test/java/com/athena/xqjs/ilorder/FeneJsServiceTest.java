package com.athena.xqjs.ilorder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.toft.core3.container.annotation.Inject;
@TestData(locations = { "classpath:testData/ilOrder/fenejs.xls" })
public class FeneJsServiceTest extends AbstractCompomentTests {

	@Inject
	private IlOrderService ilOrderService;

	@Inject
	private DingdljService dingdljService;

	@Test
	public void testCount() throws Exception {
		List<Dingdlj> allDingdlj = this.dingdljService.queryAllDingdlj();
		Iterator<Dingdlj> iterm = allDingdlj.iterator();
		Dingdlj bean = null;
		TreeMap<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
		Map maps = new HashMap() ;
		while (iterm.hasNext()) {
			bean = iterm.next();
			maps = this.ilOrderService.gongysFeneJs(bean,new BigDecimal(1030));
//			 System.out.println("object1============"+maps.get("yaohl"));
//			 System.out.println("object2============"+maps.get("yingyu"));
		}

	}

}

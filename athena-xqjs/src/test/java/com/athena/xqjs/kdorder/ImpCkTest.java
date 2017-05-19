package com.athena.xqjs.kdorder;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.kdorder.service.ImpAqkcService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/kdOrder/impck.xls" })
public class ImpCkTest extends AbstractCompomentTests{

	@Inject
	private ImpAqkcService impAqkcService;
	
	private Map<String, String> map;
	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", "UW");
			map.put("lingjbh", "9666800080");
		};
	};

	@SuppressWarnings("rawtypes")
	@Test
	public void selckTest() {
		Lingjck l = new Lingjck();
		Map a = impAqkcService.select(l, map);
		Assert.assertNotNull(a);
	}
	
	@Test
	public void up1Test() {
		List<Lingjck> list = new ArrayList<Lingjck>();
		Lingjck l = new Lingjck();
		l.setUsercenter("UW");
		l.setLingjbh("9681830880");
		l.setCangkbh("W05");
		l.setJistzz(BigDecimal.TEN);
		list.add(l);
		boolean flag = impAqkcService.updateKc(list, "aaa", CommonFun.getJavaTime());
		Assert.assertEquals(true, flag);
	}
	
	@Test
	public void readMuluTest() throws Exception {

		String filePath = "testData/kdorder/dinghanqkc-2.xls";
		String path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		String url = this.getClass().getClassLoader().getResource(path).getFile();
			url = URLDecoder.decode(url, "UTF-8");
		// String t = new String(url.getBytes(Charset.forName("UTF-8")));
		File file = new File(url);
		System.out.println(file.getPath());
			List<?> ls = impAqkcService.importExc(file.getPath());
			System.out.println(ls.size());
			org.junit.Assert.assertEquals(3, ls.size());
	}

}

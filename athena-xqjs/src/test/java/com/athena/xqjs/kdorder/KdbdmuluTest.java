package com.athena.xqjs.kdorder;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.module.kdorder.service.KdbdmlService;
import com.toft.core3.container.annotation.Inject;

public class KdbdmuluTest extends AbstractCompomentTests{
	@Inject
	private KdbdmlService kdbdmlservice;
	@Test
	public void testKdbdmulu() throws Exception{
		String filePath = "testData/kdOrder/catalogue pap du 20.12.20111.xls";
		String  path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		  String  url = this.getClass().getClassLoader().getResource(path).getFile();
				url = URLDecoder.decode(url, "UTF-8");
		  //String  t = new String(url.getBytes(Charset.forName("UTF-8")));
		  File  file = new File(url);
		  System.out.println(file.getPath());
		  String lujing = file.getPath();
		  this.kdbdmlservice.deleteKdbdml();
		this.kdbdmlservice.readMulu(lujing);
		List list = this.kdbdmlservice.queryKdbdml();
		assertEquals(list.size(), 1774);
		
		this.kdbdmlservice.MuluCompared();
		
		this.kdbdmlservice.deleteKdbdml();
		List listD = this.kdbdmlservice.queryKdbdml();
		assertEquals(listD.size(), 0);
	}

}

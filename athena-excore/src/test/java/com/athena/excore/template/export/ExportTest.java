package com.athena.excore.template.export;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.excore.template.export.supports.DefaultProssData;
import com.athena.excore.template.export.supports.ProcessDataStaticProxy;
import com.toft.core3.container.annotation.Inject;

/**
 * 
 * @author 王冲
 * @date 2012-04-25
 * @time 16:03
 * @description 测试文件 导出
 * 
 */
public class ExportTest extends AbstractCompomentTests {
	@Inject
	private DefaultProssData processData;

	private Map<String, Object> dataSource;
	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			dataSource = new HashMap<String, Object>();
			dataSource.put("username", "王冲");
			dataSource.put("company", "软通动力");
		};
	};

	/**
	 * 测试文件流
	 * @throws Exception
	 */
	@Test
	public void testProcessFileStream() throws Exception {
		OutputStream stream = processData.processFileStream("pcfj.ftl",
				dataSource);
		Assert.assertTrue(stream.toString().length()>0);
//		System.out.println(stream);
	}

	/**
	 * 测试byte
	 * @throws Exception
	 */
	@Test
	public void testProcessByteCache() throws Exception {
		byte[] bt = processData.processByteCache("pcfj.ftl", dataSource);
		Assert.assertTrue(bt.length>0);
//		System.out.println(bt.toString());
	}
	/**
	 * 测试压缩
	 * @throws Exception
	 */
	@Test
	public void testProcessZipStream() throws Exception {
		ProcessDataStaticProxy defaults =ProcessDataStaticProxy.getInstances("ABC",ExportConstants.FILE_TXT,processData);
		byte[] bt = defaults.processByteCache("pcfj.ftl", dataSource) ;
		Assert.assertTrue(bt.length>0);
//		System.out.println(bt.toString());
	}
}

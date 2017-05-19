package com.athena.xqjs.module.zygzbj;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.zygzbj.service.MafmxqService;
import com.athena.xqjs.module.zygzbj.service.ZygzService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjcxService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjjsService;
import com.toft.core3.container.annotation.Inject;

/**
 * 
 * @author dsimedd001
 *
 */
public class ZygzbjTest extends AbstractCompomentTests {

	@Inject
	private ZygzbjjsService zyjs;
	@Inject
	private ZygzService zygz;
	
	@Inject
	private ZygzbjcxService zycx;
	@Inject
	private MafmxqService mafmxq;
	
	/**
	 * 测试查询未发运零件
	 * @throws Exception 
	 */
	@Test
	public void testQueryWeifylj() throws Exception{
		Map params = new HashMap();
		params.put("usercenter", "UW");
		params.put("lingjbh", "9632452880");
		params.put("yaohlzt", "01");
		org.junit.Assert.assertNotNull(zygz.queryWeifylj(new Yaohl(), params));
	}
	
	/**
	 * 测试查询maf库毛需求对比信息
	 * @throws Exception 
	 */
	@Test
	public void testQueryMafmxq() throws Exception{
		Map params = new HashMap();
		//org.junit.Assert.assertNotNull(mafmxq.queryMafmxq(params));
	}
	
	/** 
	 
	@Test
	public void testJs(){
		Map params = new HashMap();
		try {
			params.put("usercenter", "UW");
			params.put("jslx", "002");
			params.put("zyhqrq", "2012-01-01");
			params.put("xuqbc", "140251");
			params.put("xuqcfsj", "2011-11-15");
			params.put("xuqly", "DKS");
			params.put("jsrq", "2012-02-11"); 
			params.put("username", "sys");
			ArrayList<Maoxq> list3 = new ArrayList<Maoxq>();
			Maoxq maoxq3 = new Maoxq();
			maoxq3.setXuqbc("140251");
			maoxq3.setXuqly("DKS");
			list3.add(maoxq3);
			org.junit.Assert.assertNotNull(zyjs.jiS(params, list3));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
		* 测试计算方法 .
	 */
	
}

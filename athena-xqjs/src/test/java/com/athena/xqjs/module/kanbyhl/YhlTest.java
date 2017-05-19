package com.athena.xqjs.module.kanbyhl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.yaohl.service.YaohlService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 导入最大要货量测试
 * 
 * @author Nsy
 * 
 */
@TestData(locations = { "classpath:testData/xqjs/kanbyhl.xls" })
public class YhlTest extends AbstractCompomentTests {

	@Inject
	private YaohlService yaohlService;

	@Inject
	private AbstractIBatisDao baseDao;

	private Kanbxhgm kanbxhgm;

	private Map<String, Object> map;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			kanbxhgm = new Kanbxhgm();
			// Map数据准备
			map = new HashMap<String, Object>();
		};
	};

	/**
	 * 查询零件是否存在
	 */
	@Test
	public void selectLjTest() {
		map.put("usercenter", "UW");
		map.put("lingjbh", "968163485S");
		boolean flag = yaohlService.selectLingj(map);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 修改内部要货令的状态
	 */
	@Test
	public void updateZtTest() {
		map.put("yaohlh", "M80010014");
		map.put("editor", "AAA");
		map.put("edit_time", "2011-02-10 21:57:27:027");
		yaohlService.updateYhlN(map, "root");
	}

	/**
	 * 修改内部要货令的状态 异常测试
	 */
	@Test
	public void updateZtExceptionTest() {
		map.put("yaohlh", "M40010010");
		map.put("editor", "968163485S");
		map.put("edit_time", "2011-02-10 21:57:27:027");
		yaohlService.updateYhlN(map, "root");

	}

	/**
	 * 修改外部要货令的状态
	 */
	@Test
	public void updateZt1Test() {
		map.put("yaohlh", "M90010010");
		map.put("editor", "AAA");
		map.put("edit_time", "2011-02-10 21:57:27:027");
		map.put("dingdmxid", "1");
		map.put("dingdh", "11P0100");
		map.put("yaohsl", "100");
		// 订单零件
		map.put("lingjbh", "ZQ80572280");
		map.put("gongysdm", "M105800000");
		map.put("cangkbh", "L05");
		// 零件仓库
		map.put("usercenter", "UL");
		map.put("lingjbh", "ZQ80572280");
		int flag = yaohlService.updateYhl(map, "aaa");
		Assert.assertEquals(1, flag);
	}

	/**
	 * 修改外部要货令的状态 异常测试
	 */
	@Test
	public void updateException1Test() {
		map.put("yaohlh", "M90010010");
		map.put("editor", "968163485S");
		map.put("edit_time", "2011-1-4");
		try {
			yaohlService.updateYhl(map, "aaa");
			Assert.fail("异常测试失败！");
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * 查询内部要货令
	 */
	@Test
	public void queryYhlNTest() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "0");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		yaohlService.selectN(kanbxhgm, map1);
	}

	/**
	 * 查询内部要货令
	 */
	@Test
	public void queryYhlN1Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "1");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		yaohlService.selectN(kanbxhgm, map1);
	}

	/**
	 * 查询内部要货令
	 */
	@Test
	public void queryYhlN2Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "2");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		yaohlService.selectN(kanbxhgm, map1);
	}

	/**
	 * 查询内部要货令
	 */
	@Test
	public void queryYhlN3Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "3");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		yaohlService.selectN(kanbxhgm, map1);
	}

	/**
	 * 查询外部要货令
	 */
	@Test
	public void queryYhlTest() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "0");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		map1.put("zt", "00");
		yaohlService.select(kanbxhgm, map1);
	}

	/**
	 * 查询外部要货令
	 */
	@Test
	public void queryYhl1Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "1");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		map1.put("zt", "01");
		yaohlService.select(kanbxhgm, map1);
	}

	/**
	 * 查询外部要货令
	 */
	@Test
	public void queryYhl2Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "2");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		map1.put("zt", "01");
		yaohlService.select(kanbxhgm, map1);
	}

	/**
	 * 查询外部要货令
	 */
	@Test
	public void queryYhl3Test() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("dingdh", "11P0100");
		map1.put("dingdmxid", "1");
		map1.put("usercenter", "UL");
		map1.put("jihyz", "j01");
		map1.put("sj", "3");
		map1.put("qssj", "2011-1-01");
		map1.put("jssj", "2011-10-01");
		map1.put("zt", "01");
		yaohlService.select(kanbxhgm, map1);
	}

}

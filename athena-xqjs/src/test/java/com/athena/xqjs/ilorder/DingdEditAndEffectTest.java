package com.athena.xqjs.ilorder;

import java.math.BigDecimal;
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
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.IleditAndEffectService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/ilorder/DingdEe.xls" })
public class DingdEditAndEffectTest extends AbstractCompomentTests {
	@Inject
	private IleditAndEffectService iEffectService;

	private Dingd dd;

	private Dingdlj ddlj;

	private Dingdmx ddmx;

	private Map<String, String> map;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			dd = new Dingd();
			ddlj = new Dingdlj();
			ddmx = new Dingdmx();
			map = new HashMap<String, String>();
		};
	};

	/**
	 * 新增订单测试
	 */
	@Test
	public void testInsertDd() {
		List<Dingd> ls = new ArrayList<Dingd>();
		dd.setDingdh("DD1020304");
		dd.setHeth("HTH1020304");
		dd.setGongysdm("M90010010");
		dd.setUsercenter("UW");
		dd.setDingdlx("0");
		dd.setFahzq("201204");
		dd.setShiffsgys("1");
		dd.setBeiz("adfeeeeeeeee");
		String creator = "root";
		String time = CommonFun.getJavaTime();
		ls.add(dd);
//		String msg = iEffectService.insDingdIl(ls, creator, time, "P","");
//		Assert.assertNotNull(msg);
	}

	/**
	 * 新增订单测试
	 */
	@Test
	public void testInsertDd1() {
		dd.setDingdh("DD1020305");
		dd.setHeth("HTH1020305");
		dd.setGongysdm("M90010010");
		dd.setUsercenter("UW");
		dd.setDingdlx("0");
		dd.setFahzq("201204");
		dd.setShiffsgys("1");
		dd.setBeiz("adfeeeeeeeee");
		String creator = "root";
		String time = CommonFun.getJavaTime();
		dd.setCreator(creator);
		dd.setCreate_time(time);
		dd.setEdit_time(time);
		dd.setEditor(creator);
		iEffectService.saveDingdIl(dd);
	}

	/**
	 * 新增订单零件测试测试
	 */
	@Test
	public void testInsertDdLj() {
		List<Dingdlj> ls = new ArrayList<Dingdlj>();
		ddlj.setDingdh("DD1020304");
		ddlj.setGongysdm("M90010010");
		ddlj.setUsercenter("UW");
		ddlj.setP0fyzqxh("2012-10-01");
		ddlj.setLingjbh("9666800080");
		ddlj.setDanw("KG");
		ddlj.setUabzlx("aaa");
		ddlj.setUabzuclx("abb");
		ddlj.setUabzucrl(BigDecimal.TEN);
		ddlj.setUabzucsl(BigDecimal.TEN);
		ddlj.setP0sl(BigDecimal.TEN);
		ddlj.setP1sl(BigDecimal.TEN);
		ddlj.setP2sl(BigDecimal.TEN);
		ddlj.setP3sl(BigDecimal.TEN);
		ddlj.setP4sl(BigDecimal.TEN);
		ddlj.setJihyz("JHZ");
		String creator = "root";
		String time = CommonFun.getJavaTime();
		ls.add(ddlj);
		String msg = iEffectService.insDingdljIl(ls, creator, time);
		Assert.assertNotNull(msg);
	}

	/**
	 * 新增订单明细测试
	 */
	@Test
	public void testInsertDdMx() {
		List<Dingdmx> ls = new ArrayList<Dingdmx>();
		ddmx.setDingdh("DD1020304");
		ddmx.setGongysdm("M90010010");
		ddmx.setUsercenter("UW");
		ddmx.setLingjbh("9666800080");
		ddmx.setDanw("KG");
		ddmx.setUabzlx("aaa");
		ddmx.setUabzuclx("abb");
		ddmx.setUabzucrl(BigDecimal.TEN);
		ddmx.setUabzucsl(BigDecimal.TEN);
		ddmx.setCangkdm("W05");
		ddmx.setJihyz("JHZ");
		ddmx.setLeix("1");
		ddmx.setShul(BigDecimal.valueOf(100));
		String creator = "root";
		String time = CommonFun.getJavaTime();
		ls.add(ddmx);
		String msg = iEffectService.insDingdmxIl(ls, creator, time);
		Assert.assertNotNull(msg);
	}

	/**
	 * 订单查询测试
	 */
	@Test
	public void testqueryDd() {
		map.put("usercenter", "UW");
		map.put("dingdh", "121P6200");
		Map<String, Object> reultMap = iEffectService.selectDd(dd, map);
		Assert.assertEquals(1, reultMap.get("total"));
	}

	/**
	 * 订单零件查询测试
	 */
	@Test
	public void testqueryDdLj() {
		map.put("usercenter", "UW");
		map.put("dingdh", "121P6200");
		Map<String, Object> reultMap = iEffectService.selectDalj(dd, map);
		Assert.assertEquals(2, reultMap.get("total"));
	}

	/**
	 * 订单明细查询测试
	 */
	@Test
	public void testqueryDdmx() {
		map.put("usercenter", "UW");
		map.put("dingdh", "121P6200");
		Map<String, Object> reultMap = iEffectService.selectDaMx(dd, map);
		Assert.assertEquals(2, reultMap.get("total"));
	}

	/**
	 * 删除订单正常测试
	 */
	@Test
	public void testDeleteDd() {
		List<Dingd> ls = new ArrayList<Dingd>();
		dd.setDingdh("121P6200");
		dd.setDingdzt(Const.DINGD_STATUS_YDY);
		ls.add(dd);
		String newEditor = "jion";
		String editTime = CommonFun.getJavaTime();
		boolean flag = iEffectService.deleteDd(ls, newEditor, editTime);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 删除订单正常测试
	 */
	@Test
	@TestData(locations = { "classpath:testData/ilorder/DingdEe.xls" })
	public void testDeleteDdlj() {
		List<Dingdlj> ls = new ArrayList<Dingdlj>();
		ddlj.setDingdh("121P6200");
		ddlj.setDingdzt(Const.DINGD_STATUS_YDY);
		ls.add(ddlj);
		String newEditor = "jion";
		String editTime = CommonFun.getJavaTime();
		boolean flag = iEffectService.deleteDdLj(ls);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 删除订单正常测试
	 */
	@Test
	public void testDeleteDdmx() {
		List<Dingdmx> ls = new ArrayList<Dingdmx>();
		ddmx.setDingdh("121P6202");
		ls.add(ddmx);
		String newEditor = "jion";
		String editTime = CommonFun.getJavaTime();
		boolean flag = iEffectService.deleteDdMx(ls);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 修改订单状态（待生效、生效、拒绝)-测试
	 */
	@Test
	@TestData(locations = { "classpath:testData/ilorder/DingdEe.xls" })
	public void testUpdateDdZt1() {
		List<Dingd> ls = new ArrayList<Dingd>();
		dd.setDingdh("121P6201");
		dd.setDingdzt("1");
		String newEditor = "jion";
		String editTime = CommonFun.getJavaTime();
		String flag = "0";
		ls.add(dd);
		String resultStr = iEffectService.updateDaStatus(ls, newEditor, editTime, flag);
		Assert.assertNotNull(resultStr);
	}

	/**
	 * 修改订单状态（待生效、生效、拒绝)-测试
	 */
	@Test
	public void testUpdateDdZt2() {
		List<Dingd> ls = new ArrayList<Dingd>();
		dd.setDingdh("121P6203");
		dd.setDingdzt("2");
		String newEditor = "jion";
		String editTime = CommonFun.getJavaTime();
		String flag = "1";
		ls.add(dd);
		String resultStr = iEffectService.updateDaStatus(ls, newEditor, editTime, flag);
		Assert.assertNotNull(resultStr);
	}

	/**
	 * 修改订单状态（待生效、生效、拒绝)-测试
	 */
	@Test
	public void testUpdateDdZt3() {
		List<Dingd> ls = new ArrayList<Dingd>();
		dd.setDingdh("121P6206");
		String newEditor = "jion";
		dd.setDingdzt("2");
		String editTime = CommonFun.getJavaTime();
		String flag = "2";
		ls.add(dd);
		String resultStr = iEffectService.updateDaStatus(ls, newEditor, editTime, flag);
		Assert.assertNotNull(resultStr);
	}

	/**
	 * 更新订单明细数量
	 */
	@Test
	public void testUpdatemxSl() {
		List<Dingdmx> ls = new ArrayList<Dingdmx>();
		ddmx.setId("201");
		ddmx.setShul(BigDecimal.TEN);
		ddmx.setJiaofrq("2012-03-15");
		String newEditor = "jion";
		String newEditTime = CommonFun.getJavaTime();
		ls.add(ddmx);
		String resultStr = iEffectService.updateDdmxSl(ls, newEditor, newEditTime);
		Assert.assertNotNull(resultStr);
	}

	/**
	 * 更新订单明细数量
	 */
	@Test
	public void testUpdateDdljSl() {
		List<Dingdlj> ls = new ArrayList<Dingdlj>();
		ddlj.setId("201");
		ddlj.setP0sl(BigDecimal.TEN);
		ddlj.setP1sl(BigDecimal.TEN);
		ddlj.setP2sl(BigDecimal.TEN);
		ddlj.setP3sl(BigDecimal.TEN);
		String newEditor = "jion";
		String newEditTime = CommonFun.getJavaTime();
		ls.add(ddlj);
		String resultStr = iEffectService.updateDdljSl(ls, newEditor, newEditTime);
		Assert.assertNotNull(resultStr);
	}

	/**
	 * 汇总IL/按需订单明细至订单零件
	 */
	@Test
	@TestData(locations = { "classpath:testData/ilorder/DingdEe.xls" })
	public void testSumDdmxToLj() {
		ddlj.setDingdh("121P6200");
		ddlj.setLingjbh("9666800080");
		ddlj.setUsercenter("UW");
		String prq = "2012-08-01";
		ddlj.setP0fyzqxh(prq);
		Map<String, String> map1 = iEffectService.getPartsDate(ddlj);
		map1.put("RQ", map1.get("P0"));
		Map<String, Object> pMap = iEffectService.sumDdmxToLj(map1);
		Assert.assertNotNull(pMap);
	}

	/**
	 * 汇总IL/按需订单明细至订单零件
	 */
	@Test
	public void testSumDdmxToLj1() {
		ddlj.setDingdh("121P6201");
		ddlj.setLingjbh("9666800080");
		ddlj.setUsercenter("UW");
		String prq = "2012-08-01";
		ddlj.setP0fyzqxh(prq);
		Map<String, String> map2 = iEffectService.getPartsDate(ddlj);
		map2.put("RQ", map2.get("P0"));
		Map<String, Object> pMap = iEffectService.sumDdmxToLj(map2);
		Assert.assertNotNull(pMap);
	}

	/**
	 * 汇总IL/按需订单明细至订单零件
	 */
	@Test
	public void testSumDdmxToLj2() {
		ddlj.setDingdh("121P6202");
		ddlj.setLingjbh("9666800080");
		ddlj.setUsercenter("UW");
		String prq = "2012-08-01";
		ddlj.setP0fyzqxh(prq);
		Map<String, String> map3 = iEffectService.getPartsDate(ddlj);
		map3.put("RQ", map3.get("P0"));
		Map<String, Object> pMap = iEffectService.sumDdmxToLj(map3);
		Assert.assertNotNull(pMap);
	}

}

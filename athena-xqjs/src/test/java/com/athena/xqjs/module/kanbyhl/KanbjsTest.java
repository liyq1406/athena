package com.athena.xqjs.module.kanbyhl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.kanbyhl.Wullj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.kanbyhl.service.AssisterDateService;
import com.athena.xqjs.module.kanbyhl.service.KanbjsService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 导入最大要货量测试
 * 
 * @author Nsy
 * 
 */
@TestData(locations = { "classpath:testData/xqjs/kanbyhl.xls" })
public class KanbjsTest extends AbstractCompomentTests {

	@Inject
	private KanbjsService kanbjsService;
	@Inject
	private AssisterDateService assisterDateService;

	@Inject
	private AbstractIBatisDao baseDao;

	private Kanbxhgm kanbxhgm;

	private Map<String, String> map;

	private Maoxqmx maoxqmx;

	private Wullj wullj;

	@Inject(value = "lingj")
	private Lingj lingj;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			kanbxhgm = new Kanbxhgm();
			wullj = new Wullj();
			maoxqmx = new Maoxqmx();
			// Map数据准备
			map = new HashMap<String, String>();
		};
	};

	/**
	 * 毛需求查询
	 */
	@Test
	public void queryMaoxqTest() {
		
		Map<String, Object> mapExp = kanbjsService.select(kanbxhgm);
		List<?> ls = (List<?>) mapExp.get("rows");
		Set<String> set = new TreeSet<String>();
		for (int i = 0; i < ls.size(); i++) {
			Maoxq m = (Maoxq) ls.get(i);
			set.add(m.getXuqbc());
		}
		Object[] obj = set.toArray();
		Assert.assertEquals("170757", (String) obj[0]);
	}

	/**
	 * 毛需求明细查询
	 */
	@Test
	public void queryMaoxqMxTest() {
		map.put("xuqbc", "94916");
		Map<String, Object> mapExp = kanbjsService.selectMx(kanbxhgm, map);
		List<Maoxqmx> ls = (ArrayList<Maoxqmx>) mapExp.get("rows");
		Assert.assertEquals("94916", ls.get(0).getXuqbc());
		Assert.assertEquals(67, mapExp.get("total"));
	}


	/**
	 * 查询计划员组测试
	 */
	@Test
	public void queryJhyzTest() {
		lingj.setUsercenter("UW");
		lingj.setLingjbh("7903017068");
		String jihy = kanbjsService.getJihyz(lingj);
		Assert.assertNotNull(jihy);
	}

	/**
	 * 需求类型为日 R1/RD模式产线下消耗点比例之和不为百分百，报警
	 */
	@Test
	public void xiaoblBjTest() {
		List<Wullj> ls = kanbjsService.xiaohblBJ();
		System.out.println(ls.size());
		Assert.assertEquals(2, ls.size());
		Assert.assertEquals("9673186980", ls.get(0).getLingjbh());
	}

	/**
	 * 需求类型为日 供应商比例之和不为百分百，报警
	 */
	@Test
	public void gonyfeBJTest() {
		List<Wullj> ls = kanbjsService.gonyfeBJ();
		System.out.println(ls.size());
		Assert.assertEquals(1, ls.size());
		Assert.assertEquals("9673186980", ls.get(0).getLingjbh());
	}

	/**
	 * 日期 指定截取年月日
	 */
	@Test
	public void getJsDateTest() {
		String dateStr = "2012-02-20";
		String str = kanbjsService.getJsDate(dateStr);
		Assert.assertEquals("201202", str);
	}

	/**
	 * 查询装车系数
	 */
	@Test
	public void getZhuangcxsTest() {
		wullj.setUsercenter("UL");
		wullj.setLingjbh("ZQ80571780");
		BigDecimal k = kanbjsService.getZhuangcxs(wullj);
		Assert.assertEquals(BigDecimal.valueOf(1), k);
	}

	/**
	 * 获取指定版次日需求类型的需求起始时间与结束时间
	 */
	public void getIntervalTimeTest() {
//		maoxqmx.setXuqbc("94916");
//		maoxqmx.setUsercenter("UW");
//		Map<String, String> tmap = kanbjsService.getIntervalTime(maoxqmx);
//		Assert.assertTrue(tmap.containsValue("2012-02-14"));
//		Assert.assertEquals("2012-07-23", tmap.get("ENDTIME"));
	}

	/**
	 * 求某一年里某月的开始日期
	 */
	@Test
	public void monthOfStartTest() {
		String date = "2012-02-24";
		String ret = kanbjsService.monthOfStart(date);
		System.out.println(ret);
		Assert.assertEquals("2012-02-01", ret);
	}

	/**
	 * 求某一年里某月的结束日期
	 */
	@Test
	public void monthOfEndTest() {
		String date = "2012-02-24";
		String ret = kanbjsService.monthOfEnd(date);
		System.out.println(ret);
		Assert.assertEquals("2012-02-29", ret);
	}

	/**
	 * 需求类型为周期, 计算周期开始时间和结束时间
	 */
	@Test
	public void jsZQRQTest() {

		Calendar calendar = new GregorianCalendar();
		// 设置日期格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// 让日期加2
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);
		// 计算日期即开始日期
		String jsDate = sf.format(calendar.getTime());
		// 月份加1后日期往前推1天即为计算结束日期
		// 月份加1
		System.out.println("计算开始日期：" + jsDate);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		// 让日期加-1
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		// 计算结束日期
		String jsEnd = sf.format(calendar.getTime());
		System.out.println("计算结束日期：" + jsEnd);
		Map<String, String> rqMap = assisterDateService.cycleJudge();
		Assert.assertEquals(jsDate, rqMap.get("jsDate"));
		Assert.assertEquals(jsEnd, rqMap.get("jsEnd"));

	}

	/**
	 * 点击确认后，更新计算参数处理设置
	 */
	@Test
	public void updateJsszTest() {
		map.put("usercenter", "UW");
		map.put("jiscldm", "42");
		map.put("param1", "94916");
		map.put("param2", Const.MAOXQ_XUQLY_CLV);
		map.put("param3", "all");
		map.put("param4", "m01");
		map.put("param5", CommonFun.getJavaTime().substring(0, 15));
		kanbjsService.updateJssz(map);
		Map<String, String> expMap1 = kanbjsService.selJscssz(map);
		Assert.assertEquals("94916", expMap1.get("PARAM1"));
		map.put("param1", "94917");
		kanbjsService.updateJssz(map);
		Map<String, String> expMap2 = kanbjsService.selJscssz(map);
		Assert.assertEquals("94917", expMap2.get("PARAM1"));
	}

	/**
	 * 循环编码的生成
	 */
	@Test
	public void xhbmGenerationTest() {
		kanbxhgm.setUsercenter("UW");
		kanbxhgm.setChanx("UW5L2");
		String bm = kanbjsService.xhbmGeneration(kanbxhgm);
		System.out.println(bm);
		org.junit.Assert.assertEquals("W5245679", bm);
	}

	/**
	 * 看板计算异常报警 更新异常报警表
	 */
	@Test
	public void kanbBJTest() {
		String jihy = "m02";
		String cuowlx = "400";
		String uString = "UW";
		String lString = "7540759080";
		wullj.setUsercenter(uString);
		wullj.setLingjbh(lString);
		lingj.setUsercenter(uString);
		lingj.setLingjbh(lString);
		String jihyz = kanbjsService.getJihyz(lingj);
		List<Wullj> ls = new ArrayList<Wullj>();
		ls.add(wullj);
		map.put("usercenter", uString);
		map.put("lingjbh", lString);
		map.put("jismk", "42");
		map.put("jihyz", jihyz);
		map.put("jihydm", jihy);
		map.put("cuowlx", cuowlx);
		map.put("cuowxxxx", "UW用户中心下的零件号7540759080供应商份额之和不为100%");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhlTest.delYicbj", map);
		kanbjsService.kanbBJ(ls, cuowlx);
		int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryYicbj", map);
		Assert.assertEquals(1, count);

	}

	/**
	 * 存在指定供应商时 剔除一个用户中心，零件下对应的其它供应商
	 */
	@Test
	public void filterSuppliersTest() {
		wullj.setUsercenter("UW");
		wullj.setFenpqh("W0520");
		wullj.setGongysbh("M100970000");
		wullj.setLingjbh("96816350KQ");
		wullj.setZhidgys("M100970001");
		wullj.setGyfe(BigDecimal.valueOf(0.4));
		List<Wullj> ls = new ArrayList<Wullj>();
		ls.add(wullj);
		Wullj wullj1 = new Wullj();
		wullj1.setUsercenter("UW");
		wullj1.setFenpqh("W0520");
		wullj1.setGongysbh("M100970001");
		wullj1.setLingjbh("96816350KQ");
		wullj1.setGyfe(BigDecimal.valueOf(0.4));
		ls.add(wullj1);
		Wullj wullj2 = new Wullj();
		wullj2.setUsercenter("UW");
		wullj2.setFenpqh("W0520");
		wullj2.setGongysbh("M100970002");
		wullj2.setLingjbh("96816350KQ");
		wullj2.setGyfe(BigDecimal.valueOf(0.2));
		ls.add(wullj2);
		List<Wullj> fls = kanbjsService.filterSuppliers(ls);
		System.out.println(fls.get(0).getFenpqh() + "\t" + fls.get(0).getGongysbh() + "\t" + fls.get(0).getGyfe());
		Assert.assertEquals(1, fls.size());

	}

	/**
	 * 查询指定模式关联毛需求过滤数据 RD测试
	 */
	@Test
	public void getWulljRdTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		String constMos = Const.KANB_JS_NEIBMOS_RD;
		String selMos = "kanbyhl.jsRRD";
		List<Wullj> ls = kanbjsService.getWullj(maoxqmx, constMos, selMos);
		System.out.println("=======getWulljRdTest" + ls.size());
		Assert.assertEquals(2, ls.size());

	}

	/**
	 * 需求类型为日，内部模式为RD的CMJ和CMJMAX 日模式RD测试
	 */
	@Test
	public void jsRRDTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsRRD(maoxqmx);
		System.out.println("======= jsRRDTest" + ls.size());
		Assert.assertEquals(2, ls.size());
	}

	/**
	 * 查询指定模式关联毛需求过滤数据 R1测试
	 */
	@Test
	public void getWulljR1Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		String constMos = Const.KANB_JS_WAIBMOS_R1;
		String selMos = "kanbyhl.jsRR1";
		List<Wullj> ls = kanbjsService.getWullj(maoxqmx, constMos, selMos);
		System.out.println("======= getWulljR1Test" + ls.size());
		Assert.assertEquals(2, ls.size());

	}

	/**
	 * 需求类型为日，内部模式为RD的CMJ和CMJMAX 日模式RD测试
	 */
	@Test
	public void jsRR1Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsRR1(maoxqmx);
		System.out.println("=======jsrr1" + ls.size());
		Assert.assertEquals(2, ls.size());
	}
 
	/**
	 * 查询指定模式关联毛需求过滤数据 R1测试
	 */
	@Test
	public void getWulljRmTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		String constMos = Const.KANB_JS_NEIBMOS_RM;
		String selMos = "kanbyhl.jsRRM";
		List<Wullj> ls = kanbjsService.getWullj(maoxqmx, constMos, selMos);
		System.out.println("=======jrm" + ls.size());
		Assert.assertEquals(2, ls.size());
	}

	/**
	 * 需求类型为日，内部模式为RD的CMJ和CMJMAX 日模式RD测试
	 */
	@Test
	public void jsRRmTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsRRM(maoxqmx);
		// Assert.assertEquals(66, ls.size());
		System.out.println("=======jsrrm" + ls.size());
		Assert.assertEquals(2, ls.size());
	}

	/**
	 * 查询指定模式关联毛需求过滤数据 R1测试
	 */
	@Test
	public void getWulljR2Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		String constMos = Const.KANB_JS_WAIBMOS_R2;
		String selMos = "kanbyhl.jsRR2";
		List<Wullj> ls = kanbjsService.getWullj(maoxqmx, constMos, selMos);
		System.out.println("=======jzr2" + ls.size());
		Assert.assertEquals(0, ls.size());
	}

	/**
	 * 需求类型为日，内部模式为RD的CMJ和CMJMAX 日模式RD测试
	 */
	@Test
	public void jsRR2Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsRR2(maoxqmx);
		System.out.println("=======zrr2" + ls.size());
		Assert.assertEquals(0, ls.size());
	}

	/**
	 * 需求类型为周，内部模式为RD的CMJ和CMJMAX 周模式RD测试
	 */
	@Test
	public void jsZRDTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsZRD(maoxqmx);
		System.out.println("=======zrd" + ls.size());
		Assert.assertEquals(0, ls.size());
	}

	/**
	 * 需求类型为周，内部模式为RD的CMJ和CMJMAX 周模式RD测试
	 */
	@Test
	public void jsZRMTest() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsZRM(maoxqmx);
		System.out.println("=======zrm" + ls.size());
		Assert.assertEquals(2, ls.size());
	}

	/**
	 * 需求类型为周，内部模式为RD的CMJ和CMJMAX 周模式R1测试
	 */
	@Test
	public void jsZR1Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsZR1(maoxqmx);
		System.out.println("=======zr1" + ls.size());
		Assert.assertEquals(2, ls.size());
	}

	/**
	 * 需求类型为周，内部模式为RD的CMJ和CMJMAX 周模式R2测试
	 */
	@Test
	public void jsZR2Test() {
		maoxqmx.setXuqbc("94916");
		maoxqmx.setUsercenter("UW");
		List<Kanbxhgm> ls = kanbjsService.jsZR2(maoxqmx);
		System.out.println("=======zr2" + ls.size());
		Assert.assertEquals(0, ls.size());
	}

	/**
	 * 汇总R1的循环卡数
	 */
	@Test
	public void sumKbXhdTest() {
		List<Kanbxhgm> ls = new ArrayList<Kanbxhgm>();
		kanbxhgm.setUsercenter("UW");
		kanbxhgm.setLingjbh("7903017068");
		kanbxhgm.setXiaohd("xhd1");
		kanbxhgm.setJisxhgm(BigDecimal.valueOf(3));
		ls.add(kanbxhgm);
		Kanbxhgm k1 = new Kanbxhgm();
		k1.setUsercenter("UW");
		k1.setLingjbh("7903017068");
		k1.setXiaohd("xhd1");
		k1.setJisxhgm(BigDecimal.valueOf(3));
		ls.add(k1);
		List<Kanbxhgm> xhls = kanbjsService.sumKbXhd(ls);
		Assert.assertEquals(BigDecimal.valueOf(6), xhls.get(0).getJisxhgm());
	}

	/**
	 * 汇总R2的循环卡数
	 */
	@Test
	public void sumKbCkTest() {
		List<Kanbxhgm> ls = new ArrayList<Kanbxhgm>();
		kanbxhgm.setUsercenter("UW");
		kanbxhgm.setLingjbh("7903017068");
		kanbxhgm.setCangkdm("WSY");
		kanbxhgm.setJisxhgm(BigDecimal.valueOf(3));
		ls.add(kanbxhgm);
		Kanbxhgm k1 = new Kanbxhgm();
		k1.setUsercenter("UW");
		k1.setLingjbh("7903017068");
		k1.setCangkdm("WSY");
		k1.setJisxhgm(BigDecimal.valueOf(3));
		ls.add(k1);
		List<Kanbxhgm> xhls = kanbjsService.sumKbCk(ls);
		Assert.assertEquals(BigDecimal.valueOf(6), xhls.get(0).getJisxhgm());
	}

	/*
	 * @Test public void upKbTest(){ kanbjsService.updateKB("all", "005",
	 * CommonFun.getJavaTime(), ls);
	 * 
	 * 
	 * 
	 * }
	 */

	@Test
	public void wholeTest() {
		String str = kanbjsService.numeration("UW");
		System.out.println(str);
		org.junit.Assert.assertNotNull(str);
	}

	@Test
	public void demoTest() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++) {
				str.append("=====ssssssss=====").append(i).append("aaaa");
				str.append(j).append("\n");
			}

		}
		System.out.println(str.toString());

	}

}

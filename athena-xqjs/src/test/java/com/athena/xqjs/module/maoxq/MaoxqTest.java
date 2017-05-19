package com.athena.xqjs.module.maoxq;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.junit.Assert;
import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.maoxq.action.NullCalendarCenterException;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@TestData(locations = { "classpath:testData/maoxq/maoxq.xls", "classpath:testData/maoxq/calendar.xls" })
public class MaoxqTest extends AbstractCompomentTests {

	@Inject
	private MaoxqCompareService maoxqService;

	@Inject
	private AbstractIBatisDao baseDao;


	/**
	 * 毛需求版次-生成測試 當天無毛需求版次
	 */
	@Test
	public void maoxqBancGenerationTest() {

		String banc = maoxqService.maoxqBancGeneration("DKS");
		System.out.println(banc);
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Assert.assertEquals(
				"DKS" + year + String.format("%02d", month)
						+ String.format("%02d", day) + "01", banc);
	}

	/**
	 * 读取excel数据-测试
	 */
	@Test
	public void readExeclTest() {
		String filePath = "testData/maoxq/KDweek.xls";
		String path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		String url = this.getClass().getClassLoader().getResource(path)
				.getFile();
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println(e1.toString());
		}
		// String t = new String(url.getBytes(Charset.forName("UTF-8")));
		File file = new File(url);
		System.out.println(file.getPath());
		String str = null;
		try {
			str = maoxqService.insExcelData(file.getPath(), "SSSSSS",
					"aa01");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(str);
		Assert.assertNotNull(str);
		/*
		 * for (int i = 0; i < list.size(); i++) { CompareWeek ck = list.get(i);
		 * System.out.println("usercenter:" + ck.getUsercenter() + "\t" +
		 * "lingjbh:" + ck.getLingjbh() + "\t" + "zhizlx:" + ck.getZhizlx() +
		 * "xuqrq:" + ck.getXuqrq() + "xuqsl:" + ck.getXuqsl()); }
		 */
	}

	/**
	 * 读取excel数据-测试
	 */
	@Test
	public void readExecl1Test() {
		String filePath = "testData/maoxq/KDweek2.xls";
		String path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		String url = this.getClass().getClassLoader().getResource(path)
				.getFile();
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println(e1.toString());
		}
		// String t = new String(url.getBytes(Charset.forName("UTF-8")));
		File file = new File(url);
		System.out.println(file.getPath());
		String str = null;
		try {
			str = maoxqService.insExcelData(file.getPath(), "SSSSSS", "aa01");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(str);
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Assert.assertEquals(
				"SG" + year + String.format("%02d", month)
						+ String.format("%02d", day) + "01", str);
		/*
		 * for (int i = 0; i < list.size(); i++) { CompareWeek ck = list.get(i);
		 * System.out.println("usercenter:" + ck.getUsercenter() + "\t" +
		 * "lingjbh:" + ck.getLingjbh() + "\t" + "zhizlx:" + ck.getZhizlx() +
		 * "xuqrq:" + ck.getXuqrq() + "xuqsl:" + ck.getXuqsl()); }
		 */
	}

	/**
	 * 从当前月份 周期顺序后推
	 */
	@Test
	public void getCycTest() {
		Map<String, String> map = maoxqService.getCyc("2012-02-14", 3);
		System.out.println(map.toString());
		Assert.assertEquals("201203", map.get("P1"));
	}

	/**
	 * 查询某版毛需求的开始日期
	 */
	@Test
	public void startRqTest() {
		String rq = maoxqService.startYearRq("94916");
		System.out.println(rq);
		Assert.assertEquals("2012-02-14", rq);
	}

	/**
	 * 拼周-测试
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void weekTest() throws NullCalendarCenterException {
		Map<String, String> weekMap = maoxqService.getWeekIl("2012-02-14", 5);
		System.out.println(weekMap.toString());
		Assert.assertEquals("201208", weekMap.get("S0"));
	}

	/**
	 * 需求类型为日J0-J8
	 */
	@Test
	public void pJTest() {
		Map<String, String> daysMap = maoxqService.getDays("2012-02-14", 9,new HashMap<String,String>());
		System.out.println(daysMap.toString());
		Assert.assertEquals("2012-02-14", daysMap.get("J0"));
		Assert.assertEquals("2012-02-15", daysMap.get("J1"));
		Assert.assertEquals("2012-02-16", daysMap.get("J2"));
	}

	/**
	 * 新增毛需求明细
	 */
	@Test
	public void insMxqmxTest() {
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setActive("1");
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setShiycj("L07");
		e.setLingjbh("7903008237");
		e.setChanx("UWL6");
		e.setZhizlx("97W");
		e.setXuqrq("2012-10-03");
		e.setXuqsl(BigDecimal.TEN);
		ls.add(e);
		String creator = "Moze";
		String creatTime = CommonFun.getJavaTime();
		String message = maoxqService.insMxqmx(ls, creator, creatTime);
		System.out.println(message);
		Assert.assertEquals("新增成功！", message);
	}

	/**
	 * 新增毛需求明细1
	 * 
	 */
	@Test
	public void insMxqmx1Test() {
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setActive("1");
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setShiycj("L2");
		e.setLingjbh("9673186980");
		e.setChanx("UW5L2");
		e.setZhizlx("97W");
		e.setXuqrq("2012-02-17");
		e.setXuqsl(BigDecimal.TEN);
		ls.add(e);
		String creator = "Moze";
		String creatTime = CommonFun.getJavaTime();
		String message = maoxqService.insMxqmx(ls, creator, creatTime);
		System.out.println(message);
		Assert.assertNotSame("新增成功！", message);
	}

	/**
	 * 毛需求比较PP 用户中心
	 */
	@Test
	public void comparePpOrPsTest() {
		int maxIndex = 5;
		CompareCyc bean = new CompareCyc();
		Map<String, String> map = maoxqService.getCyc("2012-02-14", maxIndex);
		Map<String, Object> mapRt = null;
		map.put("xuqbc", "94916");
		map.put("xuqbc2", "94917");
		map.put("xuqlx", "Cyc");
		map.put("mode", "usercenter");
		map.put("xsfs", "1");
		try {
			mapRt = maoxqService.comparePpOrPs(map, bean, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求比较PP 产线
	 */
	@Test
	public void comparePpOrPs1Test() {
		int maxIndex = 5;
		CompareCyc bean = new CompareCyc();
		Map<String, String> map = maoxqService.getCyc("2012-02-14", maxIndex);
		Map<String, Object> mapRt = null;
		map.put("xuqbc", "94916");
		map.put("xuqbc2", "94917");
		map.put("xuqlx", "Cyc");
		map.put("mode", "chanx");
		try {
			mapRt = maoxqService.comparePpOrPs(map, bean, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(mapRt.toString());
		Assert.assertEquals(7, mapRt.get("total"));
	}

	/**
	 * 毛需求比较PP 产线
	 */
	@Test
	public void comparePpOrPs4Test() {
		int maxIndex = 5;
		CompareCyc bean = new CompareCyc();
		Map<String, String> map = maoxqService.getCyc("2012-02-14", maxIndex);
		Map<String, Object> mapRt = null;
		map.put("xuqbc", "94916");
		map.put("xuqbc2", "94920");
		map.put("xuqlx", "Cyc");
		map.put("mode", "chanx");
		try {
			mapRt = maoxqService.comparePpOrPs(map, bean, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(mapRt.toString());
		Assert.assertEquals(7, mapRt.get("total"));
	}

	/**
	 * 毛需求比较PS 产线
	 */
	@Test
	public void comparePpOrPs2Test() {
		int maxIndex = 64;
		CompareCyc bean = new CompareCyc();
		Map<String, String> map = maoxqService.getCyc("2012-02-14", maxIndex);
		Map<String, Object> mapRt = null;
		map.put("xuqbc", "94916");
		map.put("xuqbc2", "94917");
		map.put("xuqlx", "Cyc");
		map.put("mode", "chanx");
		try {
			mapRt = maoxqService.comparePpOrPs(map, bean, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求比较PS 用户中心
	 */
	@Test
	public void comparePpOrPs3Test() {
		int maxIndex = 64;
		CompareCyc bean = new CompareCyc();
		Map<String, String> map = maoxqService.getCyc("2012-02-14", maxIndex);
		Map<String, Object> mapRt = null;
		map.put("xuqbc", "94916");
		map.put("xuqbc2", "94918");
		map.put("xuqlx", "Week");
		map.put("mode", "usercenter");
		try {
			mapRt = maoxqService.comparePpOrPs(map, bean, false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PP-产线
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void queryTest() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Cyc");
		e.setXsfs("1");
		String mode = "chanx";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PP-用户中心
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void query1Test() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Cyc");
		e.setXsfs("1");
		String mode = "usercenter";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PS-用户中心
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void query2Test() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Week");
		e.setXsfs("1");
		String mode = "usercenter";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PS-产线
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void query3Test() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Week");
		e.setXsfs("1");
		String mode = "chanx";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PJ-用户中心
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void query4Test() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Days");
		e.setXsfs("0");
		String mode = "usercenter";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 毛需求查询-测试 PJ-产线
	 * @throws NullCalendarCenterException 
	 */
	@Test
	public void query5Test() throws NullCalendarCenterException {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		e.setUsercenter("UW");
		e.setZhizlx("97W");
		e.setXuqlx("Days");
		e.setXsfs("1");
		String mode = "chanx";
		Map<String, Object> mapRt = maoxqService.query(mode, e, false);
		System.out.println(mapRt.toString());
	}

	/**
	 * 判断存储模式 用户中心与产线
	 */
	@Test
	public void storageModeTest() {
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94916");
		String mode = maoxqService.storageMode(e);
		System.out.println(mode);
		Assert.assertEquals("chanx", mode);
	}



	/**
	 * 毛需求查詢
	 */
	@Test
	public void selectMxqTest() {
		Maoxq xqbean = new Maoxq();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> mapRt = maoxqService.selectMxq(map, xqbean);
		System.out.println(mapRt.toString());
	}

	/**
	 * 修改備註-測試
	 */
	@Test
	public void updateCommentTest() {
		String newEditor = "mozeA";
		String newEditTime = CommonFun.getJavaTime();
		List<Maoxq> ls = new ArrayList<Maoxq>();
		Maoxq e = new Maoxq();
		e.setXuqbc("94916");
		ls.add(e);
		boolean flag = maoxqService.updateComment(ls, newEditor, newEditTime);
		Assert.assertEquals(true, flag);

	}

	/**
	 * 毛需求刪除-測試
	 */
	@Test
	public void deleteTest() {
		String newEditor = "mozeA";
		String newEditTime = CommonFun.getJavaTime();
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94918");
		ls.add(e);
		boolean flag = maoxqService.delete(ls, newEditor, newEditTime);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 修改毛需求数量-測試
	 */
	@Test
	public void updateSlTest() {
		String newEditor = "mozeA";
		String newEditTime = CommonFun.getJavaTime();
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setId("56096");
		ls.add(e);
		boolean flag = maoxqService.updateSl(ls, newEditor, newEditTime);
		Assert.assertEquals(true, flag);
	}

	@Test
	public void demoTest() {
		Map map = maoxqService.getCyc("2012-02-14", 15);
		System.out.println(map);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sf.format(sf.parse("2012-01-15"));
			Calendar calendar = sf.getCalendar();
			// System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
		} catch (ParseException e) {
			System.out.println(e.toString());
		}
		List<String> rqLs = new ArrayList<String>();
		rqLs.add("20120308");
		rqLs.add("20120205");
		rqLs.add("20120309");
		Object[] str = rqLs.toArray();
		Arrays.sort(str);
		for (int i = 0; i < str.length; i++) {

			System.out.println(str[i]);
		}
	}

	/**
	 * 指定运输时刻、CMJ测试
	 */
	@Test
	public void appointTcTest() {
		List<Maoxq> mls = new ArrayList<Maoxq>();
		Maoxq m = new Maoxq();
		m.setXuqbc("94920");
		// 指定工业开始时间
		m.setZdgyzqfrom("201202");
		// 指定结束月份
		m.setZdgyzqto("201203");
		m.setShifjscmj("1");
		m.setShifzdyssk("1");
		mls.add(m);
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94920");
		ls.add(e);
		Map<String, String> mapA = new HashMap<String, String>();
		mapA.put("shifzdyssk", "1");
		mapA.put("shifjscmj", "1");
		mapA.put("zdgyzqto", "201203");
		mapA.put("zdgyzqfrom", "201202");
		mapA.put("editor", "mozeA");
		String appoint = "and chanx is null and zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'";
		mapA.put("appoint", appoint);
		boolean flag = maoxqService.appointTc(mls, ls, mapA);
		Assert.assertEquals(true, flag);
	}

	/**
	 * 指定运输时刻、CMJ测试
	 */
	@Test
	public void appointTc1Test() {
		List<Maoxq> mls = new ArrayList<Maoxq>();
		Maoxq m = new Maoxq();
		m.setXuqbc("94919");
		List<CompareCyc> ls = new ArrayList<CompareCyc>();
		CompareCyc e = new CompareCyc();
		e.setXuqbc("94919");
		ls.add(e);
		Map<String, String> mapA = new HashMap<String, String>();
		String appoint = "and chanx is null and zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'";
		mapA.put("appoint", appoint);
		boolean flag = maoxqService.appointTc(mls, ls, mapA);
		Assert.assertEquals(false, flag);
	}

	/**
	 * 令存为测试
	 */
	@Test
	public void saveAs() {

		CompareCyc e = new CompareCyc();
		e.setXuqbc("94920");
		e.setCreate_time(CommonFun.getJavaTime());
		e.setCreator("ddd");
		String beiz = "aaaaaaas";
		boolean flag = maoxqService.saveAs(e, beiz);
		Assert.assertEquals(true, flag);
	}

}

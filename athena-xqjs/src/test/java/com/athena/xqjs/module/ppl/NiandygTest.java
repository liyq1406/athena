package com.athena.xqjs.module.ppl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.ppl.ComparePpl;
import com.athena.xqjs.entity.ppl.Manager;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.entity.ppl.Niandygmx;
import com.athena.xqjs.entity.ppl.Xqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.ilorder.service.MaoxqmxService;
import com.athena.xqjs.module.ppl.service.NiandygService;
import com.athena.xqjs.module.ppl.service.NiandygmxService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * PPL年度预告测试用例
 * 
 * @author Xiahui
 * @CreateTime 2012-2-13
 */
@TestData(locations = { "classpath:testdata/xqjs/pplmxq.xls", "classpath:testdata/xqjs/calendar.xls" })
public class NiandygTest extends AbstractCompomentTests {
	@Inject
	private MaoxqService maoxqService;
	@Inject
	private MaoxqmxService maoxqmxService;
	@Inject
	private NiandygService niandygService;
	@Inject
	private NiandygmxService niandygmxService;
	@Inject
	private AbstractIBatisDao baseDao;
	private Map<String, String> params;
	private Niandyg niandyg;
	private Niandygmx niandygmx;
	private Lingj lj;
	private Gongys gys;
	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			niandyg = new Niandyg();
			niandygmx = new Niandygmx();
			niandyg.setActive("1");
			niandyg.setMaoxqbc("170757");
			niandyg.setPpllx("IL");
			niandyg.setJisnf("2013");
			niandyg.setJihydm("j01");
			niandyg.setCreator("AAA");
			niandyg.setCreate_time(CommonFun.getJavaTime());
			niandyg.setEditor("AAA");
			niandyg.setEdit_time(CommonFun.getJavaTime());

			// 数据准备
			params = new HashMap<String, String>();
		}
	};

	/**
	 * 测试插入一条年度预告信息
	 * 
	 * @author Xiahui
	 * @throws ServerException
	 * @date 2012-2-13
	 */
	@Test
	// @TestData(locations = {"classpath:testData/xqjs/pplmxq.xls"})
	public void testInsertNiandyg() throws ServerException {
		String time = CommonFun.getJavaTime(Const.TIME_FORMAT_yyyyMMddHHmm);
		// 设置ppl版次为
		niandyg.setPplbc("PPL" + time);
		String pplbc = niandygService.doInsert(niandyg);
		assertEquals("PPL" + time, niandyg.getPplbc());
	}

	/**
	 * 测试查询年度预告信息
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testSelectNiandyg() {
		params.put("pplbc", "PPL201102141058");
		Map<String, Object> resultMap = niandygService.select(niandyg, params);
		ArrayList<Object> ls = (ArrayList<Object>) resultMap.get("rows");
		// 获取查询结果，判断结果
		assertEquals("170757", ((Niandyg) ls.get(0)).getMaoxqbc());
		assertEquals("2012", ((Niandyg) ls.get(0)).getJisnf());

	}

	/**
	 * 测试修改年度预告信息
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testUpdateNiandyg() {
		niandyg.setBeiz("第一次修改年度预告信息");
		niandyg.setPplbc("PPL201102141058");
		niandyg.setEditor("WWW");
		niandyg.setEdittime("2011-02-14 12:25:09:009");
		niandyg.setEdit_time("2011-02-14 12:49:09:009");
		niandyg.setNeweditor("xia");

		String pplbc = niandygService.doUpdate(niandyg);

		params.put("pplbc", "PPL201102141058");
		Map<String, Object> resultMap = niandygService.select(niandyg, params);
		ArrayList<Object> ls = (ArrayList<Object>) resultMap.get("rows");
		// 获取查询结果，判断结果
		assertEquals("第一次修改年度预告信息", ((Niandyg) ls.get(0)).getBeiz());
	}

	/**
	 * 选择一版ppl年度预告信息获取年度预告明细
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testSelectNiandygmx() {
		params.put("pplbc", "PPL201102141058");
		Map<String, Object> resultMap = niandygmxService.select(niandygmx, params);
		ArrayList<Object> ls = (ArrayList<Object>) resultMap.get("rows");
		// 获取查询结果，判断结果

		assertEquals("88", ((Niandygmx) ls.get(0)).getId());

	}

	/**
	 * 检查参数将某一版次的毛需求明细信息插入到需求明细表中
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testCheckValue() {
		params.put("xuqbc", "94916");
		params.put("LX", "IL");
		String xuqlx = params.get(Const.PARAMS_PPL_LINGJLEIXING);
		if (Const.LINGJIAN_LX_KD.equals(xuqlx)) {
			// 获取制造路线
			params.put("zhizlx", " and (zhizlx = '" + Const.ZHIZAOLUXIAN_KD_PSA + "' or zhizlx = '" + Const.ZHIZAOLUXIAN_KD_AIXIN + "' )");
		} else if (Const.LINGJIAN_LX_IL.equals(xuqlx)) {
			// 获取制造路线
			params.put("zhizlx", " and zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'");
		}
		niandygService.checkvalue(params);
		List ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pplTest.queryXqmx");

		assertEquals("9673186980", ((Xqmx) ls.get(0)).getLingjbh());
		assertEquals("96816348KQ", ((Xqmx) ls.get(1)).getLingjbh());

	}

	/**
	 * 修改年度预告明细的信息
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	@Test
	public void testUpdateNiandygmx() {
		niandygmx.setId("88");
		niandygmx.setP0sl(new BigDecimal(150));
		niandygmx.setPplbc("PPL201102141058");
		niandygmx.setUsercenter("UW");
		niandygmx.setGongysdm("M100970001");
		niandygmx.setLingjbh("9673186981");
		niandygmx.setEditor("WWW");
		niandygmx.setEdit_time("2012-02-14 11:19:45:108");
		niandygmxService.doUpdate(niandygmx);
		params.put("pplbc", "PPL201102141058");
		Map<String, Object> resultMap = niandygmxService.select(niandygmx, params);
		ArrayList<Object> ls = (ArrayList<Object>) resultMap.get("rows");
		// 获取查询结果，判断结果
		assertEquals(new BigDecimal(150), ((Niandygmx) ls.get(0)).getP0sl());
	}

	/**
	 * 计算某一版次毛需求的 IL件PPl
	 * 
	 * @author Xiahui
	 * @throws ServerException
	 * @date 2012-2-14
	 */
	@Test
	@TestData(locations = { "classpath:testdata/xqjs/pplmxq.xls" })
	public void testILniandygCollect() {
		Map<String, Object> mxmap = new HashMap<String, Object>();
		mxmap.put(Const.PARAMS_PPL_LINGJLEIXING, "IL");
		mxmap.put(Const.MAOXQ_BC, "94916");
		mxmap.put("jihydm", "A01");
		// 设置计算年份
		mxmap.put(Const.NAINDYG_JISNF, "2012");

		// 设置创建人
		mxmap.put("creator", "xia");
		mxmap.put("create_time", CommonFun.getJavaTime());
		mxmap.put("editor", "xia");
		mxmap.put("edit_time", CommonFun.getJavaTime());
		try {
			niandygService.niandygCollect(mxmap);
		} catch (ServerException e) {
			System.out.println(e.toString());
		}
		mxmap.put("lingjbh", "9673186980");
		Niandygmx bean = (Niandygmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pplTest.selectNiandygmxLingj", mxmap);
		assertEquals(new BigDecimal(15), bean.getP4sl());

	}

	/**
	 * 计算某一版次毛需求的 IL件PPl
	 * 
	 * @author Xiahui
	 * @throws ServerException
	 * @date 2012-2-15
	 */
	@Test
	@TestData(locations = { "classpath:testdata/xqjs/pplmxq.xls" })
	public void testKDNiandygCollect() throws ServerException {
		Map<String, Object> mxmap = new HashMap<String, Object>();
		mxmap.put(Const.PARAMS_PPL_LINGJLEIXING, "KD");
		mxmap.put(Const.MAOXQ_BC, "94917");
		mxmap.put("jihydm", "A01");
		mxmap.put("xuqcfsj", "2011-10-25");
		// 设置计算年份
		mxmap.put(Const.NAINDYG_JISNF, "2012");

		// 设置创建人
		mxmap.put("creator", "xia");
		mxmap.put("create_time", CommonFun.getJavaTime());
		mxmap.put("editor", "xia");
		mxmap.put("edit_time", CommonFun.getJavaTime());
		niandygService.niandygCollect(mxmap);
		mxmap.put("lingjbh", "9673186981");
		Niandygmx bean = (Niandygmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pplTest.selectNiandygmxLingj", mxmap);
		assertEquals(new BigDecimal(113), bean.getP0sl());
		assertEquals(new BigDecimal(114), bean.getP1sl());

	}

	/**
	 * 比较两个版次的PPL的明细信息
	 */
	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testComparePPL() {
		Map<String, Object> pplbc = new HashMap<String, Object>();
		pplbc.put("pplbc1", "PPL201102141058");
		pplbc.put("pplbc2", "PPL201102141059");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pplTest.queryBijiaobc", pplbc);
		assertEquals("-100%", ((ComparePpl) list.get(0)).getP1());
	}

	/**
	 * 测试零件的数量格式化方法
	 */
	@Test
	public void testFormatsl() {
		assertEquals("0000010000", niandygService.formatsl(new BigDecimal(10000)));
	}

	/**
	 * 查询计算参数设置
	 */
	@Test
	public void testSelectJisclcssz() {
		boolean count = niandygService.selectJisclcssz();
		assertEquals(true, count);

	}

	/**
	 * 测试修改计算参数设置表
	 */
	@Test
	public void testUpdateJisclcssz() {
		params.put("updatetime", CommonFun.getJavaTime());
		niandygService.updateJisclcssz(params);
		boolean count = niandygService.selectJisclcssz();

		assertEquals(true, count);
	}

	/**
	 * 查询零件表获取零件名称与订货车间
	 */
	@Test
	public void testSelectLingj() {
		lj = new Lingj();
		lj.setLingjbh("9673186981");
		lj.setUsercenter("UW");
		lj = niandygmxService.selectLingj(lj);
		assertEquals("后尾灯", lj.getZhongwmc());
		assertEquals("W05", lj.getDinghcj());
		assertEquals("WI", lj.getJihy());

	}

	/**
	 * 查询供应商表获取供应商名称
	 */
	@Test
	public void testSelectGongys() {
		gys = new Gongys();
		gys.setGcbh("M105180002");
		gys.setUsercenter("UW");
		gys = niandygmxService.selectGongys(gys);
		assertEquals("供应商11", gys.getGongsmc());

	}

	/**
	 * 查询计划员组
	 */
	@Test
	public void testSelectjihyz() {
		params.put("usercenter", "UW");
		// 获取计划员组
		List<?> list = niandygService.selectjihyz(params);
		assertEquals("001", ((Manager) list.get(0)).getZuh());
		assertEquals("002", ((Manager) list.get(1)).getZuh());
		assertEquals("003", ((Manager) list.get(2)).getZuh());
	}

	/**
	 * 向计算参数设置表中插入一条数据
	 */
	@Test
	public void testInsertJisclcssz() {
		params.put("jihyz", "UW1");
		params.put("caozy", "AAA");
		params.put("updatetime", CommonFun.getJavaTime());
		niandygService.insertJisclcssz(params);
		boolean count = niandygService.selectJisclcssz();
		assertEquals(true, count);

	}

	/**
	 * 导出txt文本
	 * 
	 * @throws IOException
	 */
	@Test
	@TestData(locations = { "classpath:testdata/xqjs/pplmxq.xls" })
	public void testWriteText() throws Exception {
		// 判断文件是否存在如果存在则删除文件
		niandygmx = new Niandygmx();
		params.put("pplbc", "ppl201112290240");
		niandygService.writeTxt(niandygmx, params);
		System.out.println(System.getProperty("java.io.tmpdir") + "ppl年度预告明细.txt");
		assertEquals(true, new File(System.getProperty("java.io.tmpdir") + "ppl年度预告明细.txt").exists());
	}

	/**
	 * 保存数据年度预告明细
	 */

	@Test
	// @TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public void testSavemx() {
		String pplbc = "PPL201102141058";
		String p0xqzq = "201201";
		String user = "xia";
		List insert = new ArrayList();
		Niandygmx nx = new Niandygmx();
		nx.setLingjbh("ZQ80571780");
		nx.setPplbc(pplbc);
		nx.setZhizlx("97W");
		nx.setLingjdw("个");
		nx.setUsercenter("UL");
		nx.setGongysdm("M105180003");
		nx.setP0sl(new BigDecimal(100));
		nx.setP1sl(new BigDecimal(100));
		nx.setP2sl(new BigDecimal(100));
		nx.setP3sl(new BigDecimal(100));
		nx.setP4sl(new BigDecimal(100));
		nx.setP5sl(new BigDecimal(100));
		nx.setP6sl(new BigDecimal(100));
		nx.setP7sl(new BigDecimal(100));
		nx.setP8sl(new BigDecimal(100));
		nx.setP9sl(new BigDecimal(100));
		nx.setP10sl(new BigDecimal(100));
		nx.setP11sl(new BigDecimal(101));
		insert.add(nx);
		List delete = new ArrayList();
		Niandygmx nx1 = new Niandygmx();
		nx1.setPplbc(pplbc);
		nx1.setLingjbh("9673186981");
		nx1.setGongysdm("M100970001");
		nx1.setUsercenter("UW");
		nx1.setEditor("WWW");
		nx1.setEdit_time("2012-02-14 11:19:45:108");
		delete.add(nx1);

		List update = new ArrayList();
		Niandygmx nx2 = new Niandygmx();
		nx2.setPplbc(pplbc);
		nx2.setGongysdm("M100970001");
		nx2.setLingjbh("968163456B");
		nx2.setUsercenter("UW");
		nx2.setP8sl(new BigDecimal(999));
		nx2.setEditor("WWW");
		nx2.setEdit_time("2012-02-14 11:19:45:110");

		update.add(nx2);
		niandygmxService.saveMx(insert, update, delete, user, pplbc, p0xqzq);
		nx = (Niandygmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pplTest.selectNiandygmx", nx);
		assertEquals("1", nx.getActive());
		nx1 = (Niandygmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pplTest.selectNiandygmx", nx1);
		assertEquals("0", nx1.getActive());
		nx2 = (Niandygmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("pplTest.selectNiandygmx", nx2);
		assertEquals(new BigDecimal(999), nx2.getP8sl());
	}

}

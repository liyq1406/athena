package com.athena.xqjs.module.diaobl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.diaobl.service.DiaobsqOperationService;
import com.athena.xqjs.module.diaobl.service.DiaobsqService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 调拨申请增删改查-测试 方法测试为顺序执行
 * 
 * @author Niesy
 * 
 */
@TestData(locations = { "classpath:testData/xqjs/diaobsq.xls" })
public class DiaobsqQueryTest extends AbstractCompomentTests {

	@Inject
	private DiaobsqOperationService diaobsqOperationService;

	@Inject
	private DiaobsqService diaobsqService;

	@Inject
	private DiaobshService diaobshService;

	private Diaobsq diaobsq;

	private Diaobsqmx diaobsqmx;

	private Map<String, String> map;

	@Inject
	private AbstractIBatisDao baseDao;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			String diaobsqdh = "S2210001";
			diaobsq = new Diaobsq();
			// 数据准备
			diaobsq.setDiaobsqdh(diaobsqdh);
			diaobsq.setBanc("0001");
			diaobsq.setUsercenter("UW");
			diaobsq.setDiaobsqsj(CommonFun.getJavaTime().substring(0, 10));
			diaobsq.setChengbzx("klll");
			diaobsq.setHuijkm("wh");
			diaobsq.setZhuangt("00");
			diaobsq.setBeiz("ok");
			diaobsq.setCreate_time("2012-02-12 20:51:45:400");
			diaobsq.setCreator("001");
			diaobsq.setEdit_time("2012-02-12 20:51:45:400");
			diaobsq.setEditor("001");
			// 调拨申请明细
			diaobsqmx = new Diaobsqmx();
			diaobsqmx.setUsercenter("UW");
			diaobsqmx.setDiaobsqdh(diaobsq.getDiaobsqdh());
			diaobsqmx.setLingjbh("7540759080");
			diaobsqmx.setLux("97W");
			diaobsqmx.setShenbsl(new BigDecimal(100));
			diaobsqmx.setCreate_time(diaobsq.getDiaobsqsj());
			diaobsqmx.setCreator("001");
			diaobsqmx.setEdit_time(diaobsq.getDiaobsqsj());
			diaobsqmx.setEditor("001");
			// Map数据准备
			map = new HashMap<String, String>();
			map.put("usercenter", diaobsqmx.getUsercenter());
			map.put("diaobsqdh", diaobsq.getDiaobsqdh());
			map.put("lingjbh", diaobsqmx.getLingjbh());
			map.put("lux", diaobsqmx.getLux());
		};
	};

	/**
	 * 删除插入的数据
	 */
	@Test
	public void clearTest() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", map);
		// 插入之前清除该数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desq", map);
	}

	/**
	 * 测试调拨令查询-新增，主页面查询
	 */
	@Test
	public void queryInsqTest() {

		// 插入调拨申请
		diaobsqService.doInsert(diaobsq);
		// 查询插入的调拨申请记录
		Map<String, Object> resultMap = diaobsqOperationService.select(diaobsq, map);
		/*
		 * Set<Entry<String, Object>> set = resultMap.entrySet(); //遍历Map
		 * for(Iterator<Entry<String, Object>> it = set.iterator();it.hasNext();
		 * ){ Entry<String, Object> entry = (Map.Entry<String, Object>)
		 * it.next(); System.out.println(entry.getValue().getClass()); }
		 */
		@SuppressWarnings("unchecked")
		ArrayList<Object> ls = (ArrayList<Object>) resultMap.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsq dr = (Diaobsq) ls.get(i);
			// 比较是否相同
			org.junit.Assert.assertEquals(diaobsqmx.getUsercenter(), dr.getUsercenter());
			org.junit.Assert.assertEquals(diaobsqmx.getDiaobsqdh(), dr.getDiaobsqdh());
			org.junit.Assert.assertEquals(diaobsq.getBeiz(), dr.getBeiz());

		}

	}

	/**
	 * 测试调拨令查询-新增，明细查询
	 */
	@Test
	public void queryInmxTest() {
		// 插入调拨申请明细
		diaobsqOperationService.insert(diaobsqmx);
		// 查询插入的调拨申请记录
		List<Diaobsqmx> ls = (List<Diaobsqmx>) diaobshService.select(diaobsq).get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx dr = (Diaobsqmx) ls.get(i);
			// 比较是否相同
			org.junit.Assert.assertEquals(diaobsqmx.getUsercenter(), dr.getUsercenter());
			org.junit.Assert.assertEquals(diaobsqmx.getDiaobsqdh(), dr.getDiaobsqdh());
			org.junit.Assert.assertEquals(diaobsqmx.getShenbsl(), dr.getShenbsl());
		}

	}

	/**
	 * 调拨申请未生效改状态,修改时间改变时 更新失败
	 */
	@Test
	public void cancleTest() {

		diaobsq.setEdit_time("2011-12-16 19:22:57");
		diaobsq.setEditor("001");
		diaobsq.setNewEditor("003");
		diaobsq.setNewEdit_time(CommonFun.getJavaTime());
		diaobsq.setZhuangt("00");
		diaobsq.setNewZhuangt("22");
		ArrayList<Diaobsq> ls = new ArrayList<Diaobsq>();
		ls.add(0, diaobsq);
		// 调用service方法
		String flag = diaobsqOperationService.updateCancle(ls);
		org.junit.Assert.assertEquals("false", flag);
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("usercenter", "UW"); map.put("diaobsqdh","S2210001");
		 * Map<String, Object> resultMap =
		 * diaobsqOperationService.select(diaobsq,map); ArrayList<?> result =
		 * (ArrayList<?>) resultMap.get("rows"); //判断两个状态是否是相同
		 * org.junit.Assert.assertEquals(1, result.size()); Diaobsq dr =
		 * (Diaobsq)result.get(0);
		 * org.junit.Assert.assertEquals(diaobsq.getNewZhuangt(),
		 * dr.getZhuangt());
		 */

	}

	/**
	 * 调拨申请未生效改状态,修改时间改变时 更新成功
	 */
	@Test
	public void cancleSuccessTest() {

		diaobsq.setEdit_time("2012-02-12 20:51:45:400");
		diaobsq.setEditor("001");
		diaobsq.setNewEditor("003");
		diaobsq.setNewEdit_time(CommonFun.getJavaTime());
		diaobsq.setZhuangt("00");
		diaobsq.setNewZhuangt("22");
		ArrayList<Diaobsq> ls = new ArrayList<Diaobsq>();
		ls.add(0, diaobsq);
		// 调用service方法
		String flag = diaobsqOperationService.updateCancle(ls);
		org.junit.Assert.assertEquals("true", flag);
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", "UW");
		map.put("diaobsqdh", "S2210001");
		Diaobsq diaobsqTest = diaobsqService.selectDiaobsq(diaobsq);
		// 判断两个状态是否是相同
		org.junit.Assert.assertEquals(diaobsq.getNewZhuangt(), diaobsqTest.getZhuangt());

	}

	/**
	 * 调拨申请查询-明细增加测试
	 */
	@Test
	public void insTest() {
		Diaobsqmx mx = new Diaobsqmx();
		mx.setUsercenter("UW");
		mx.setDiaobsqdh("S2C16028");
		mx.setLingjbh("92");
		mx.setLux("97w");
		mx.setShenbsl(new BigDecimal(100));
		mx.setCreator("003");
		mx.setCreate_time("2012-02-12 20:51:45:400");
		mx.setCreator("003");
		mx.setEdit_time("2012-02-12 20:51:45:400");
		// 查询数据库，有没增加这条记录
		Map<String, String> test = new HashMap<String, String>();
		test.put("usercenter", mx.getUsercenter());
		test.put("lingjh", mx.getLingjbh());
		test.put("diaobsqdh", mx.getDiaobsqdh());
		test.put("lux", mx.getLux());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", test);
		// 调用diaobsqOperation 的insert方法
		diaobsqOperationService.insert(mx);
		// 查询
		Diaobsqmx mx1 = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", test);
		// 比较序号是否正确
		org.junit.Assert.assertEquals(new Integer(1), mx1.getXuh());
		// 比较用户中心是否相同
		Assert.assertEquals(mx.getUsercenter(), mx1.getUsercenter());
		// 比较调拨申请单号是否相同
		Assert.assertEquals(mx.getDiaobsqdh(), mx1.getDiaobsqdh());
		// 比较零件号是否相同
		Assert.assertEquals(mx.getLingjbh(), mx1.getLingjbh());
		// 比较路线是否相同
		Assert.assertEquals(mx.getLux(), mx1.getLux());
	}

	/**
	 * 调拨申请查询-明细修改失败测试
	 */
	@Test
	public void updateTest() {
		Diaobsqmx mx = new Diaobsqmx();
		mx.setUsercenter("UW");
		mx.setDiaobsqdh("S2C21035");
		mx.setLingjbh("96816348KQ");
		mx.setLux("97W");
		mx.setShenbsl(new BigDecimal(100));
		mx.setCreator("003");
		mx.setCreate_time("2012-02-12 20:51:45:400");
		mx.setCreator("003");
		mx.setEdit_time("2012-02-12 20:51:45:400");
		List<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
		ls.add(mx);
		// 查询数据库，有没增加这条记录
		Map<String, String> test = new HashMap<String, String>();
		test.put("usercenter", mx.getUsercenter());
		test.put("lingjh", mx.getLingjbh());
		test.put("diaobsqdh", mx.getDiaobsqdh());
		test.put("lux", mx.getLux());

		// 比较路线是否相同
		Assert.assertEquals(Const.FLAG_FALSE, diaobsqOperationService.update(ls));
	}

	/**
	 * 调拨申请查询-明细修改成功测试
	 */
	@Test
	public void update2Test() {
		Diaobsqmx mx = new Diaobsqmx();
		mx.setUsercenter("UW");
		mx.setDiaobsqdh("S2C21035");
		mx.setLingjbh("96816348KQ");
		mx.setLux("97W");
		mx.setShenbsl(new BigDecimal(100));
		mx.setEditor("001");
		mx.setCreate_time("2012-02-12 09:05:44:012");
		mx.setNewEditor("003");
		mx.setEdit_time("2012-02-12 09:25:44:012");
		List<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
		ls.add(mx);
		// 查询数据库，有没增加这条记录
		Map<String, String> test = new HashMap<String, String>();
		test.put("usercenter", mx.getUsercenter());
		test.put("lingjh", mx.getLingjbh());
		test.put("diaobsqdh", mx.getDiaobsqdh());
		test.put("lux", mx.getLux());

		// 比较路线是否相同
		Assert.assertEquals(Const.FLAG_TRUE, diaobsqOperationService.update(ls));
	}

	/**
	 * 调拨申请查询-修改申报数量测试
	 */
	@Test
	public void modifyTest() {
		Diaobsqmx sr = new Diaobsqmx();
		sr.setShenbsl(new BigDecimal(200));
		sr.setUsercenter("UW");
		sr.setDiaobsqdh("S2C16028");
		sr.setLingjbh("97");
		sr.setLux("97w");
		sr.setEditor("001");
		sr.setEdit_time("2012-02-12 20:51:45:400");
		sr.setNewEditor("002");
		// 获取当前时间
		sr.setNewEdit_time(CommonFun.getJavaTime());
		// 查询数据库，有没增加这条记录
		Map<String, String> test = new HashMap<String, String>();
		test.put("usercenter", sr.getUsercenter());
		test.put("lingjh", sr.getLingjbh());
		test.put("diaobsqdh", sr.getDiaobsqdh());
		test.put("lux", sr.getLux());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobsqTest.desqmx", test);
		// 调用diaobsqOperation 的insert方法
		diaobsqOperationService.insert(sr);
		// 查询数据库，有没修改这条记录
		// 调用查询方法
		Diaobsqmx mx1 = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", test);
		org.junit.Assert.assertEquals(new BigDecimal(200), mx1.getShenbsl());
	}

	/**
	 * 调拨令查询-删除测试
	 */
	@Test
	public void deleteTest() {
		Diaobsqmx mx = new Diaobsqmx();
		mx.setUsercenter("UW");
		mx.setDiaobsqdh("S2C16028");
		mx.setLingjbh("97");
		mx.setLux("97w");
		ArrayList<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
		ls.add(0, mx);
		// 调用删除方法
		diaobsqOperationService.doDelete(ls);
		// 调用查询方法
		// 查询数据库，有没修改这条记录
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", mx.getUsercenter());
		map.put("lingjh", mx.getLingjbh());
		map.put("diaobsqdh", mx.getDiaobsqdh());
		map.put("lux", mx.getLux());
		// 调用查询方法
		Diaobsqmx mx1 = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobsqTest.test_selectDiaobsqmx", map);
		org.junit.Assert.assertNull("删除成功", mx1);
	}

	/**
	 * 调拨令查询-删除失败测试
	 */
	@Test
	public void deleteFailTest() {
		Diaobsqmx mx = new Diaobsqmx();
		mx.setUsercenter("UW");
		mx.setDiaobsqdh("S233028");
		mx.setLingjbh("97");
		mx.setLux("97w");
		ArrayList<Diaobsqmx> ls = new ArrayList<Diaobsqmx>();
		ls.add(0, mx);
		// 调用删除方法
		String flag = diaobsqOperationService.doDelete(ls);
		org.junit.Assert.assertEquals("false", flag);
	}

	/**
	 * 版次生成测试 流水递增1 　　* Java里数字转字符串前面自动补0的实现。 　　
	 */
	@Test
	public void regTest() {
		int number = 100;
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		String str = String.format("%04d", number);
		Diaobsq diaobsqTest = diaobsqService.selectDiaobsq(diaobsq);
		String banc = diaobsqTest.getBanc();
		String banc1 = diaobsqOperationService.updateBanc(diaobsqTest);
		int i = banc1.compareTo(banc);
		org.junit.Assert.assertEquals(1, i);
		/*
		 * System.out.println("00".compareTo("30")); Diaobsqmx mx1 = new
		 * Diaobsqmx(); mx1.setUsercenter("UW"); Diaobsqmx mx4 = new
		 * Diaobsqmx(); mx4.setUsercenter("UH"); Diaobsqmx mx5 = new
		 * Diaobsqmx(); mx5.setUsercenter("UX");
		 * 
		 * List<Diaobsqmx> all = new ArrayList<Diaobsqmx>() ;
		 * 
		 * all.add(mx1); all.add(mx1); all.add(mx1); all.add(mx1); all.add(mx1);
		 * all.add(mx1); all.add(mx1); all.add(mx1); all.add(mx1); all.add(mx1);
		 * all.add(mx1); all.add(mx1); all.add(mx4); all.add(mx4); all.add(mx4);
		 * all.add(mx4); all.add(mx4); all.add(mx4); all.add(mx4); all.add(mx5);
		 * all.add(mx5); all.add(mx5);
		 * 
		 * 
		 * List<String> ls = new ArrayList<String>(); for(int
		 * i=0;i<all.size();i++){ Diaobsqmx dbmx = all.get(i); ls.add(i,
		 * dbmx.getUsercenter());
		 * 
		 * }
		 * 
		 * JSONArray ar = new JSONArray(); JSONObject jsonObject = null;
		 * Set<String> set = new TreeSet<String>();
		 * 
		 * for(int i = 0; i < ls.size(); i++){ set.add(ls.get(i)); }
		 * 
		 * for (Iterator<String> iter = set.iterator(); iter.hasNext();) {
		 * String element = (String) iter.next(); int index =
		 * ls.indexOf(element);
		 * 
		 * // jsonObject.put("usercenter",element); int lastindex =
		 * ls.lastIndexOf(element); System.out.println(element+":" + "\t" +index
		 * +"\t"+lastindex); int rowCount = lastindex-index+1; int pageSize = 8;
		 * int totalPage = (rowCount-1)/pageSize+1; System.out.println(totalPage
		 * +"\t"+rowCount); int j = 0; for(int i=index;i<=lastindex;i++){
		 * 
		 * 
		 * jsonObject.put("xuh"+(++j),j);
		 * 
		 * } for(int i=1;i<=totalPage;i++){ int pageEndRow; int pageStartRow;
		 * if(i*pageSize<rowCount){ pageEndRow = i*pageSize; pageStartRow =
		 * pageEndRow - pageSize; }else{ pageEndRow = rowCount; pageStartRow =
		 * pageSize*(totalPage-1); } jsonObject = new JSONObject(); for(int
		 * j=pageStartRow;j<pageEndRow;j++ ){ int temp = j%8;
		 * jsonObject.put("xuh"+temp,j);
		 * 
		 * System.out.print(j+";"); } System.out.println(); ar.add(jsonObject);
		 * } } String json = ar.toString(); System.out.println(json);
		 */

	}

}

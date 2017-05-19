package com.athena.xqjs.module.kanbyhl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.kanbyhl.service.KanbsgszService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 导入最大要货量测试
 * 
 * @author Nsy
 * 
 */
@TestData(locations = { "classpath:testData/xqjs/kanbyhl.xls" })
public class KanbsgszTest extends AbstractCompomentTests {

	@Inject
	private KanbsgszService kanbsgszService;

	@Inject
	private AbstractIBatisDao baseDao;

	private Kanbxhgm kanbxhgm;

	private Map<String, String> map;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			kanbxhgm = new Kanbxhgm();
			// Map数据准备
			map = new HashMap<String, String>();
		};
	};

	/**
	 * 查询看板规模计算RD
	 */
	@Test
	public void rDTest() {
		LoginUser loginUser = new LoginUser();
		map.put("usercenter", "UW");
		map.put("gonghms", Const.KANB_JS_NEIBMOS_RD);
		Map<String, Object> mapResult = kanbsgszService.select(kanbxhgm, map, loginUser);
		List<?> ls = (List<?>) mapResult.get("rows");
		kanbxhgm = (Kanbxhgm) ls.get(0);
		org.junit.Assert.assertEquals(Const.KANB_JS_NEIBMOS_RD, kanbxhgm.getGonghms());

	}

	/**
	 * 查询看板规模计算R1
	 */
	@Test
	public void r1Test() {
		LoginUser loginUser = new LoginUser();
		map.put("usercenter", "UW");
		map.put("gonghms", Const.KANB_JS_WAIBMOS_R1);
		Map<String, Object> mapResult = kanbsgszService.select(kanbxhgm, map, loginUser);
		List<?> ls = (List<?>) mapResult.get("rows");
		kanbxhgm = (Kanbxhgm) ls.get(0);
		org.junit.Assert.assertEquals(Const.KANB_JS_WAIBMOS_R1, kanbxhgm.getGonghms());
	}

	/**
	 * 查询看板规模计算R2
	 */
	@Test
	public void r2Test() {
		LoginUser loginUser = new LoginUser();
		map.put("usercenter", "UW");
		map.put("gonghms", Const.KANB_JS_WAIBMOS_R2);
		Map<String, Object> mapResult = kanbsgszService.select(kanbxhgm, map, loginUser);
		List<?> ls = (List<?>) mapResult.get("rows");
		kanbxhgm = (Kanbxhgm) ls.get(0);
		org.junit.Assert.assertEquals(Const.KANB_JS_WAIBMOS_R2, kanbxhgm.getGonghms());
	}

	/**
	 * 查询看板规模计算RM
	 */
	@Test
	public void rMTest() {
		LoginUser loginUser = new LoginUser();
		map.put("usercenter", "UW");
		map.put("gonghms", Const.KANB_JS_NEIBMOS_RM);
		Map<String, Object> mapResult = kanbsgszService.select(kanbxhgm, map, loginUser);
		List<?> ls = (List<?>) mapResult.get("rows");
		kanbxhgm = (Kanbxhgm) ls.get(0);
		org.junit.Assert.assertEquals(Const.KANB_JS_NEIBMOS_RM, kanbxhgm.getGonghms());
	}

	/**
	 * 批量插入看板循环规模
	 */
	@Test
	public void insTest() {
		List<Kanbxhgm> ls = new ArrayList<Kanbxhgm>();
		kanbxhgm.setXunhbm("L5L00001");
		ls.add(kanbxhgm);
		Kanbxhgm kanbxhgm1 = new Kanbxhgm();
		kanbxhgm1.setXunhbm("L5L00002");
		ls.add(kanbxhgm1);
		kanbsgszService.doInsert(ls);
		kanbxhgm1 = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm", kanbxhgm);
		org.junit.Assert.assertEquals("L5L00001", kanbxhgm1.getXunhbm());
		kanbxhgm1.setXunhbm("L5L00002");
		Kanbxhgm kanbxhgm2 = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm", kanbxhgm1);
		org.junit.Assert.assertEquals("L5L00002", kanbxhgm2.getXunhbm());
	}

}

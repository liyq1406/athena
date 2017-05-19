package com.athena.xqjs.module.zygzbj;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.entity.zygzbj.ZiygzbjHz;
import com.athena.xqjs.entity.zygzbj.Ziygzbjmx;
import com.athena.xqjs.entity.zygzbj.Ziygzlj;
import com.athena.xqjs.module.zygzbj.service.MafmxqService;
import com.athena.xqjs.module.zygzbj.service.ZygzService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjcxService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjjsService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testdata/zygzbj/zygzbjData.xls"})
public class ZygzTest extends AbstractCompomentTests  {

	@Inject
	private ZygzbjjsService zyjs;
	
	@Inject
	private ZygzService zygz;
	
	@Inject
	private ZygzbjcxService zycx;
	
	@Inject
	private MafmxqService mafmxq;
	
	private ZiygzbjHz hz;
	private Ziygzlj zygzlj;
	private Yaohl yaohl;
	
	private  Map<String ,String> params;
	
	@Rule
    public   ExternalResource resource= new ExternalResource(){
   	@Override
   	protected void before() throws Throwable{
   		hz = new ZiygzbjHz();
   		zygzlj = new Ziygzlj();
   		yaohl = new Yaohl();
   		//数据准备
   		params=new HashMap<String ,String>();
   	}
	};
	
	/**
	 * 资源跟踪报警汇总查询测试
	 */
	@Test
	public void testSelZygzbjhz(){
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		org.junit.Assert.assertNotNull(zycx.selZygzbjhz(new ZiygzbjHz(), params));
	}
	
	/**
	 * 资源跟踪报警明细查询测试
	 */
	@Test
	public void testSelZygzbjmx(){
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		org.junit.Assert.assertNotNull(zycx.selZygzbjmx(params));
	}
	
	/**
	 * 资源跟踪报警明细查询测试
	 */
	@Test
	public void testSelZygzbjmxs(){
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		org.junit.Assert.assertNotNull(zycx.selZygzbjmxs(new Ziygzbjmx(), params));
	}
	
	/**
	 * 资源跟踪报警明细查询测试
	 */
	@Test
	public void testQueryMxq(){
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		params.put("jslx", "005");
		org.junit.Assert.assertNotNull(zyjs.queryMxq(new Maoxq(), params));
	}

	/**
	 * 测试保存资源跟踪报警汇总,明细,删除资源跟踪报警汇总,明细
	 */
	@Test
	public void testSaveZiygzbjhz(){
		params.put("usercenter", "UW");
		params.put("zygzlx", "test");
		org.junit.Assert.assertNotNull(zyjs.delZygzbjhz(params));
	}
	
	/**
	 * 测试查询毛需求明细
	 * @throws Exception 
	 */
	@Test
	public void testSelMxqmx() throws Exception{
		params.put("xuqbc", "094916");
		params.put("usercenter", "UW");
		params.put("lingjbh", "9632452880");
		params.put("xuqly", "DIP");
		params.put("xuqcfsj", "20110125");
		org.junit.Assert.assertNotNull(zyjs.selMxqmx(new Maoxqmx(), params));
	}
	
	/**
	 * 周期最大最小
	 * @throws Exception 
	 */
	@Test
	public void testSelZqMaxMin() throws Exception{
		params.put("usercenter", "UW");
		params.put("xuqlx", "nianzq");
		params.put("rilbc", "N301KONG");
		org.junit.Assert.assertNotNull(zyjs.selZqMaxMin("201003", params));
	}
	
	/**
	 * 测试查询周序/周期
	 * @throws Exception 
	 */
	@Test
	public void testSelXuqlx() throws Exception{
		params.put("usercenter", "UW");
		params.put("rilbc", "N301KONG");
		params.put("xuqlx", "nianzx");
		org.junit.Assert.assertNotNull(zyjs.selXuqlx(params, "2012-03-13"));
	}
	
	/**
	 * 测试查询仓库
	 * @throws Exception 
	 */
	@Test
	public void testSelCk() throws Exception{
		params.put("USERCENTER", "UW");
		params.put("FENPQBH", "W0500");
		params.put("LINGJBH", "9632452880");
		org.junit.Assert.assertNotNull(zyjs.selCk(params));
		org.junit.Assert.assertNotNull(zyjs.selCk(params));
		params.put("LINGJBH", "1111");
		org.junit.Assert.assertNotNull(zyjs.selCk(params));
	}
	
	/**
	 * 测试总毛需求比较
	 * @throws Exception 
	 */
	@Test
	public void testCompareZongXq() throws Exception{
		params.put("usercenter", "UW");
		params.put("lingjbh", "9632452880");
		params.put("cangkdm", "C11");
		org.junit.Assert.assertNotNull(zyjs.compareZongXq("M", params, "2012-3-30", "2012-3-31", BigDecimal.ONE));
		org.junit.Assert.assertNotNull(zyjs.compareZongXq("M", params, "2012-3-30","2013-1-24", BigDecimal.ZERO));
	}
	
	/**
	 * 测试毛需求比较
	 * @throws Exception 
	 */
	@Test
	public void testCompareXq() throws Exception{
		params.put("usercenter", "UW");
		params.put("lingjbh", "9632452880");
		params.put("cangkdm", "C11");
		org.junit.Assert.assertNotNull(zyjs.compareXq("M", params, "2012-3-30", BigDecimal.ONE));
		org.junit.Assert.assertNotNull(zyjs.compareXq("M", params, "2013-1-24", BigDecimal.ZERO));
	}
	
	/**
	 * 测试查询下一周期
	 * @throws Exception 
	 */
	@Test
	public void testSelNzq() throws Exception{
		params.put("usercenter", "UW");
		params.put("rilbc", "N301KONG");
		org.junit.Assert.assertNotNull(zyjs.selNzq(params, "2012-03-13"));
	}
	
	/**
	 * 测试查询下一周序
	 * @throws Exception 
	 */
	@Test
	public void testSelNzx() throws Exception{
		params.put("usercenter", "UW");
		params.put("rilbc", "N301KONG");
		org.junit.Assert.assertNotNull(zyjs.selNzx(params, "2012-03-13"));
	}
	
	/**
	 * 测试保存资源跟踪报警明细
	 * @throws Exception 
	 */
	@Test
	public void testSaveZygzbjmx(){
		org.junit.Assert.assertEquals(zyjs.saveZygzbjmx(hz, 2, 3, BigDecimal.ONE, BigDecimal.ONE, "2012-03-17", "2012-03-18"),1);
	}
	
	/**
	 * 测试日期差
	 * @throws Exception 
	 */
	@Test
	public void testDaysOfDate() throws Exception{
		params.put("usercenter", "UW");
		params.put("rilbc", "N301KONG");
		org.junit.Assert.assertNotNull(zyjs.daysOfDate("2010-03-17", "2010-04-11", params));
	}
	
	/**
	 * 测试计算方法 .
	 */
	@Test
	public void testJs(){
		params.put("usercenter", "UW");
		params.put("jslx", "005");
		params.put("zyhqrq", "2012-01-01");
		params.put("xuqbc", "094916");
		params.put("xuqcfsj", "2011-11-15");
		params.put("xuqly", "DIP");
		params.put("jsrq", "2012-02-11"); 
		params.put("username", "sys");
		ArrayList<Maoxq> list = new ArrayList<Maoxq>();
		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc("094916");
		maoxq.setXuqly("DIP");
		list.add(maoxq);
		try {
			zyjs.jiS(params, list);
			params.put("usercenter", "UW");
			params.put("jslx", "004");
			params.put("zyhqrq", "2012-01-01");
			params.put("xuqbc", "094916");
			params.put("xuqcfsj", "2011-11-15");
			params.put("xuqly", "DIP");
			params.put("jsrq", "2012-02-11"); 
			params.put("username", "sys");
			zyjs.jiS(params, list);
			params.put("usercenter", "UW");
			params.put("jslx", "003");
			params.put("zyhqrq", "2012-01-01");
			params.put("xuqbc", "140250");
			params.put("xuqcfsj", "2011-11-15");
			params.put("xuqly", "CLV");
			params.put("jsrq", "2012-02-11"); 
			params.put("username", "sys");
			ArrayList<Maoxq> list2 = new ArrayList<Maoxq>();
			Maoxq maoxq2 = new Maoxq();
			maoxq2.setXuqbc("140250");
			maoxq2.setXuqly("CLV");
			list2.add(maoxq2);
			zyjs.jiS(params, list2);
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
			zyjs.jiS(params, list3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * 测试查询maf库毛需求对比信息
	 * @throws Exception 
	 */
	@Test
	public void testQueryMafmxq() throws Exception{
		//org.junit.Assert.assertNotNull(mafmxq.queryMafmxq(params));
	}
	
	/**
	 * 测试查询集装箱列表
	 * @throws Exception 
	 */
	@Test
	public void testQueryJizxlb() throws Exception{
		org.junit.Assert.assertNotNull(zygz.queryJizxlb(zygzlj, params));
	}
	
	/**
	 * 测试查询资源跟踪报警汇总
	 * @throws Exception 
	 */
	@Test
	public void testQueryZygz() throws Exception{
		org.junit.Assert.assertNotNull(zygz.queryZygz(hz, params));
	}
	
	/**
	 * 测试查询未发运零件
	 * @throws Exception 
	 */
	@Test
	public void testQueryWeifylj() throws Exception{
		params.put("usercenter", "UW");
		params.put("lingjbh", "9632452880");
		params.put("yaohlzt", "01");
		org.junit.Assert.assertNotNull(zygz.queryWeifylj(yaohl, params));
	}
	
	/**
	 * 测试查询资源跟踪集装箱列表
	 * @throws Exception 
	 */
	@Test
	public void testQueryZygzjzx() throws Exception{
		org.junit.Assert.assertNotNull(zygz.queryZygzjzx(zygzlj, params));
	}
	
	/**
	 * 测试更新交付时间
	 * @throws Exception 
	 */
	@Test
	public void testUpdateJiaofsj() throws Exception{
		params.put("tcno", "TC01");
		params.put("zuixyjddsj", "2012-03-13");
		org.junit.Assert.assertNotNull(zygz.updateJiaofsj(params));
	}
	
	/**
	 * 测试查询未装箱要货令列表
	 * @throws Exception 
	 */
	@Test
	public void testQueryWeizxyhllb() throws Exception{
		org.junit.Assert.assertNotNull(zygz.queryWeizxyhllb(zygzlj,params));
	}
}

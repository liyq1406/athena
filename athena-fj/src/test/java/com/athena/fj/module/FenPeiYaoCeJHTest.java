package com.athena.fj.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.Peizjh;
import com.athena.fj.entity.YAOCMx;
import com.athena.fj.module.service.FenPeiYaoCeJhService;
import com.athena.fj.module.service.YaocjhService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.support.PageableSupport;
//import com.toft.core3.util.Assert;

import com.athena.db.ConstantDbCode;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-20
 * @time 下午01:21:19
 * @description 
 */
@TestData(locations = {"classpath:testdata/fj/fenpycjh.xls"})
public class FenPeiYaoCeJHTest extends AbstractCompomentTests  {
	@Inject
	private FenPeiYaoCeJhService fenPeiYaoCeJhService; 
	@Inject
	private AbstractIBatisDao baseDao;
	private Map<String, String> params;
	private PageableSupport page = null;
	@Inject
	private  YaocjhService yaocjhService = null;
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {   
        	params = new HashMap<String, String>();
        	params.put("YAOCJHXH", "2012-01-14");
        	params.put("UC", "UW");
        	params.put("usercenter", "UW");
        	params.put("DATE", "2012-01-14");
        	params.put("SHENBSJ", "2012-01-14");
        	params.put("GCBH", "CYS001");
        	params.put("YUNSSBM", "CYS001");
        	params.put("YUNSSBH", "CYS001");
        	
        	 page = new PageableSupport() {
			};
        	
        };   
    };  
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-20
     * @time 下午01:26:04
     * @description   根据用户中心 查询0210要车计划总表   
     */
	@Test
    public void selectYaoCeJhZb()
    {
    	//根据用户中心 查询0210要车计划总表   
    	Map<String,Object>  selectYaoCeJhZb = fenPeiYaoCeJhService.selectYaoCeJhZb(page,params) ;
    	List<Map<String, String>> map  = (List<Map<String, String>>) selectYaoCeJhZb.get("rows") ;
    	
    	assertEquals("大车,小车",map.get(0).get("CHEXMC"));
    	assertEquals("2012-01-14",map.get(0).get("YAOCJHXH"));
    	assertEquals("UW",map.get(0).get("USERCENTER"));
    	assertEquals("11",map.get(0).get("ZONGCC"));
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:45:09
     * @description  查询车辆明细 by 要车计划总表主键
     */
    @Test
    public void selectYaocjhCelMx()
    {
    	Map<String,Object>  selectYaoCeJhZb = fenPeiYaoCeJhService.selectYaocjhCelMx(page,params ) ;
    	List<Map<String, String>> map  = (List<Map<String, String>>) selectYaoCeJhZb.get("rows") ;
    	assertEquals("DC",map.get(0).get("CHEX"));
    	assertEquals("6",map.get(0).get("ZONGCC"));
    	
    }
    
    /**
     * 根据要车计划序号和用户中心查询所有的要车明细
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectYaoCelMxAll(){
    	Map<String,Object> map  = fenPeiYaoCeJhService.selectYaoCelMxAll(page, params);
    	assert(true);
    }
    
    /**
     * 根据要车计划序号和用户中心查询尚未分配的要车明细
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    @TestData(locations = { "classpath:testdata/fj/yaocmx.xls" })
	public void testSelectYaoCelMxAllNotAssign(){
    	List<Map<String,String>> list  = fenPeiYaoCeJhService.selectYaoCelMxAllNotAssign(page, params);
    	assertEquals("2012-01-14",list.get(0).get("YAOCJHXH"));
    	assertEquals("2012-01-14 08:00",list.get(0).get("FAYSJ"));
    	 
    }
    
    /**
     * 参考系运输商查询
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectYunss(){
    	List<Map<String,String>> list = fenPeiYaoCeJhService.selectYunss(params);
    	assertEquals("CYS001",list.get(0).get("GCBH"));
    }
    
    /**
     * 运输商对应的车辆明细查询
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectYunssMx(){
    	List<Map<String,String>> list = fenPeiYaoCeJhService.selectYunssMx(params);
    	assertEquals("DC",list.get(0).get("CHEX"));
    	assertEquals("CYS001",list.get(0).get("YUNSSBM"));
    }
    
    /**
     * 根据运输商得到所有的路线组
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectLxzByCys(){
    	List<Map<String,String>> LxzByCyslist = fenPeiYaoCeJhService.selectLxzByCys(params);
    	assertEquals("CYS001",LxzByCyslist.get(0).get("GCBH"));
    	assertEquals("LX1",LxzByCyslist.get(0).get("YUNSLXBH"));
    }
    
	/**
	 * 测式封装路线组
	 */
	@Test
	public void testWrapLxzByCys() {
		List<Map<String,String>> LxzByCyslist = fenPeiYaoCeJhService.selectLxzByCys(params);
		Map<String,Set<String>> warp = fenPeiYaoCeJhService.wrapLxzByCys(LxzByCyslist);

		Set<String> lxs = warp.get("CYS001");
		assertEquals("[LX1]", lxs.toString());
	}
	
    
	
    /**
     * 查询客户,运输商关系
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testQueryYaocjhKehGys(){
    	List<Map<String,String>> list = fenPeiYaoCeJhService.queryYaocjhKehGys(params);
    	assertEquals("KH00000001",list.get(0).get("KEHBH"));
    	assertEquals("0001",list.get(0).get("CHENGYSBH"));
    	
    }
    
    /**
     * 判断这天的要车计划是否完成
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testCheckYaoCelJhZbIsComplete(){
    	List<Map<String,String>> list = fenPeiYaoCeJhService.checkYaoCelJhZbIsComplete(params);
    	@SuppressWarnings("unused")
		String zcc = list.get(0).get("ZCC");
    	assert(true);
    }
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:45:44
     * @description  查询运营商
     */
    @Test
    public void selectYaocjhYyS()
    {
    	System.out.println(fenPeiYaoCeJhService.selectYaocjhYySByClSb(params ) );
    	assertTrue(true);
    }
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:46:43
     * @description    根据运输商编号和用户中心查询要车明细
     */
   /* @Test
    public void selectYaoCelMx()
    {
    	System.out.println(fenPeiYaoCeJhService.selectYaoCelMx(page,params ) );
    	assertTrue(true);
    }*/
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:47:32
     * @description  更新要车计划明细的要车计划号
     */
    @Test
    public void updateYaoCMx()
    {
    	Map<String,String> map = new HashMap<String, String>() ;
    	map.put("YAOCJHH", "w001") ;
    	map.put("ID", "1") ;
    	fenPeiYaoCeJhService.updateYaoCMx(map ) ;
    	assertTrue(true);
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:49:00
     * @description  更新要车计划总表
     */
    @Test
    public void updateYaoCZB()
    {
    	fenPeiYaoCeJhService.updateYaoCZB(params ) ;
    	assertTrue(true);
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:49:54
     * @description  新增要车计划号
     */
    @Test
    @TestData(locations = {"classpath:testdata/fj/yaocjh.xls"})
    public void insertYAOCJH()
    {
    	Map<String,String> map = new HashMap<String, String>() ;
    	map.put("YAOCJHH",String.valueOf(new  Random(10).nextInt(10000000))) ;
    	map.put("USERCENTER", String.valueOf(new  Random(10).nextInt(10000000))) ;
    	map.put("SHIFQR", "1") ;
    	map.put("ZONGCC", "123") ;
    	map.put("YUNSSBM", "123") ;
    	map.put("CREATOR", "123") ;
    	map.put("CREATE_TIME", "123") ;
    	fenPeiYaoCeJhService.insertYAOCJH(map ) ;
    	assertTrue(true);
    } 
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午04:59:20
     * @description   新增配载计划 
     */
    @Test
    @TestData(locations = {"classpath:testdata/fj/yaocjh.xls"})
    public void insertPeiZJH()
    {
    	
    	Map<String,String> map = new HashMap<String, String>() ;
    	map.put("ID", "9898");
    	map.put("YAOCMXH", "2012-01-14") ;
    	map.put("PEIZDH", String.valueOf(new  Random(10).nextInt(10000000))) ;
    	map.put("USERCENTER", "UW") ;
    	map.put("DAOCSJ", "2012-11-11 10:30") ;
    	map.put("YUNSSBM", "CYS001") ;
    	map.put("JIHCX", "6") ;
    	map.put("CHEP", "7") ;
    	map.put("YUNSLX", "8") ;
    	map.put("JIHZT", "9") ;
    	map.put("DAYSJ", "2012-11-11 10:30") ;
    	map.put("CREATOR", "12") ;
    	fenPeiYaoCeJhService.insertPeiZJH(map) ;
    	assertTrue(true);
    }
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午05:40:21
     * @description  新增要贷令明细
     */
    @Test
    @TestData(locations = {"classpath:testdata/fj/yaocjh.xls"})
    public void insertYaoHLMX()
    {
    	Map<String,String> map = new HashMap<String, String>() ;
    	map.put("YAOHLBH",String.valueOf(new  Random(10).nextInt(10000000))) ;
    	map.put("PEIZDH", String.valueOf(new  Random(10).nextInt(10000000))) ;
    	map.put("YAOCMXH", "1") ;
    	map.put("BAOZDM", "1") ;
    	map.put("LINGJMC", "1") ;
    	map.put("LINGJSL", "1") ;
    	map.put("FAYSJ", "2012-11-11 10:30") ;
    	map.put("KEHBM", "1") ;
    	map.put("CREATOR", "1") ;
    	
    	
    	fenPeiYaoCeJhService.insertYaoHLMX(map ) ;
    	assertTrue(true);
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午05:55:43
     * @description  查询车辆资源
     */
    @Test
    public void queryCelZy()
    {
    	//参数
		Map<String, String> params = new HashMap<String, String>();
		//运输商编码
		params.put("YUNSSBM", "ys001");
		//用户中心
		params.put("UC","UW");
		//申报时间
		params.put("YAOCJHXH", "2012-01-14");
    	System.out.println(fenPeiYaoCeJhService.queryCelZy(params ) );
    	assertTrue(true);
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午05:55:43
     * @description   验证要车计划总表是否完成
     */
    @Test
    public void checkYaoCelJhZbIsWc()
    {
    	System.out.println(fenPeiYaoCeJhService.checkYaoCelJhZbIsWc( params) );
    	assertTrue(true);
    }
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午06:08:08
     * @description  重新分配
     */
    @Test
    public void caculateForFenPei()
    {
    	// 参数
		Map<String, String> params = new HashMap<String, String>();
		// 用户中心
		params.put("UC", "123");
		//要车计划号
		params.put("YAOCJHXH", "2012-01-14");
		
		//删除0204要货令明细
		this.fenPeiYaoCeJhService.deleteYaohlmx(params) ;
		//删除0203配载计划 
		this.fenPeiYaoCeJhService.deletePeiZaiJh(params) ;
		//删除0201要车计划
		this.fenPeiYaoCeJhService.deleteYaoCeJh(params) ;
		//置空0202要车明细表的要车序号
		this.fenPeiYaoCeJhService.updateYaoCeMxOfYaoCJHHToNull(params) ;
    	assertTrue(true);
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-2-22
     * @time 下午06:08:08
     * @description  重新计算
     */
    @Test
    public void caculateFotJiShuan()
    {
    	// 参数
		Map<String, String> params = new HashMap<String, String>();
		// 用户中心
		params.put("UC", "123");
		//要车计划号
		params.put("YAOCJHXH", "2012-01-14");
		
		//解除锁定配载
		this.fenPeiYaoCeJhService.updateYaoHL(params) ;
		//清空计划要贷令
		this.fenPeiYaoCeJhService.deleteJhYaoL(params) ;
		//清空要车明细
		this.fenPeiYaoCeJhService.deleteYaoCmx(params) ;
		//清空车辆明细
		this.fenPeiYaoCeJhService.deleteCelMx(params) ;
		//清空要车计划总表
		this.fenPeiYaoCeJhService.deleteJHZb(params) ;
		
		//重新计算
		yaocjhService.shengCYaoCJHmx("", "2012-01-14 23:59:59") ;
		
		
		//清空车辆明细
		this.fenPeiYaoCeJhService.deleteCelMx(params) ;
		//清空要车计划总表
		this.fenPeiYaoCeJhService.deleteJHZb(params) ;
		
		//统计车名称
		List<Map<String, String>> selectCelXx = this.fenPeiYaoCeJhService.selectCelXx(params) ;
		//新增要车计划
		int sl = 0;
		Set<String> set = new HashSet<String>();
		for(Map<String, String> map:selectCelXx)
		{
			set.add(map.get("CHEXMC")) ;
			 sl+=Integer.parseInt(map.get("SL")) ;
		}
		this.yaocjhService.addYaoCJHZb("2012-01-14", sl, set, "123") ;
		
		//新增车辆明细
		Map<String, Integer> cl = new HashMap<String, Integer>();
		for(Map<String, String> map:selectCelXx)
		{
			cl.put(map.get("JIHCX"), Integer.parseInt(map.get("SL")) );
		}
		this.yaocjhService.addCLMx("2012-01-14", cl, "123") ;
    	assertTrue(true);
    }
    
    
    /**
     * 更新车辆明细总车次
     */
 /*   @Test
	public void testUpdateChelmx(){
    	Map<String,String> params  = new HashMap<String, String>();
    	params.put("UC", "UW");
		params.put("IDs","'9888'");
		params.put("CHEX", "XC");
		params.put("YAOCJHXH", "2012-01-14");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateChelmx",params);
		assertTrue(true);
	}*/
    
    
    /**
     * 将要货令重置为未锁定状态
     * @author 贺志国
     * @date 2012-4-5
     */
  /*  @Test
	public void testUpdateYaohlOfIDs(){
    	Map<String,String> params  = new HashMap<String, String>();
    	params.put("UC", "UW");
		params.put("IDs","'9888'");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaohlOfIDs",params);
		assertTrue(true);
	}*/
    
    /**
     * 更新车辆明细总车次状态
     * @author 贺志国
     * @date 2012-4-5
     */
  /*  @Test
	public void testUpdateYaocjhzb(){
    	Map<String,String> params  = new HashMap<String, String>();
    	params.put("UC", "UW");
		params.put("IDs","'9888'");
		params.put("YAOCJHXH", "2012-01-14");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaocjhzb",params);
		assertTrue(true);
	}
    
    
    *//**
     * 删除计划要货令明细
     * @author 贺志国
     * @date 2012-4-5
     *//*
    @Test
	public void testDelJihYHLmx(){
    	Map<String,String> params  = new HashMap<String, String>();
		params.put("IDs","'9888'");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.delJihYHLmx",params);
		assertTrue(true);
	}
    
    *//**
     * 删除要车明细
     * @author 贺志国
     * @date 2012-4-5
     *//*
    @Test
    public void testDelYaocmx(){
    	Map<String,String> params  = new HashMap<String, String>();
    	params.put("UC", "UW");
    	params.put("IDs","'9888'");
    	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.delYaocmx",params);
    	assertTrue(true);
    }*/
    
    /**
     * 要车明细删除，批量执行
     * @author 贺志国
     * @date 2012-4-5
     */
    @Test
    public void testDeleteYaoCMxBatch(){
    	params.put("UC", "UW");
    	params.put("IDs","'9893'");
		params.put("CHEX", "DC");
		params.put("YAOCJHXH", "2012-01-14");
		ArrayList<YAOCMx> list =  new ArrayList<YAOCMx>();
		YAOCMx ycmx = new YAOCMx();
		ycmx.setJIHCX("DC");
		ycmx.setJIHCX("XC");
		list.add(ycmx);
		fenPeiYaoCeJhService.deleteYaoCMxBatch(params, list);
		assertTrue(true);
    	
    	
    }
    
    
}

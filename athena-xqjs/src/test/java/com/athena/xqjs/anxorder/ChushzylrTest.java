package com.athena.xqjs.anxorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.module.anxorder.service.ChushzybService;
import com.athena.xqjs.module.common.service.GongyxhdService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.toft.core3.container.annotation.Inject;
/**
 * 
 * @author 李智
 *
 */
@TestData(locations = {"classpath:testData/anxorder/chushzylr.xls"})
public class ChushzylrTest extends AbstractCompomentTests{
	@Inject
	//零件消耗点service
	private LingjxhdService lingjxhdService;
	@Inject
	//工艺消耗点service
	private GongyxhdService gongyxhdService;
	@Inject
	//初始化资源service
	private ChushzybService chushzybService;
	
	@Test
	public void doInsertTest(){
		Chushzyb bean = new Chushzyb();
		bean.setLingjbh("100PB200");
		bean.setUsercenter("UW");
		bean.setShengcxbh("00001");
		bean.setXiaohdbh("000000002");
		bean.setJicdwjfl(new BigDecimal(500));
		bean.setXianbkc(new BigDecimal(1111));
		bean.setLiush("100000001");
		bean.setBeiz("test");
		//int flag = chushzybService.doInsert(bean);
		//org.junit.Assert.assertEquals(1, flag);
	}
	
	public void queryChushzybByParamTest() {
		Map<String,String> map = new HashMap<String, String>();
		Map result = chushzybService.queryChushzybByParam(new Chushzyb(), map);
		List<Chushzyb> list = (ArrayList<Chushzyb>)result.get("rows");
		org.junit.Assert.assertEquals(1, list.size());
		
	}
	@Test
	public void doUpdateTest(){
		Chushzyb bean = new Chushzyb();
		bean.setId(0);
		bean.setXianbkc(new BigDecimal(2222));
		bean.setLiush("100000001");
		bean.setBeiz("test");
		List<Chushzyb> chushzybs = new ArrayList<Chushzyb>();
		chushzybs.add(bean);
		
		//boolean flag = chushzybService.doUpdate(chushzybs);
		//org.junit.Assert.assertEquals(false, flag);
	}
	
	@Test
	public void doUpdateTest2(){
		Map<String,String> map = new HashMap<String, String>();
		Map result = chushzybService.queryChushzybByParam(new Chushzyb(), map);
		List<Chushzyb> list = (ArrayList<Chushzyb>)result.get("rows");
		
		Chushzyb bean = list.get(0);
		bean.setXianbkc(new BigDecimal(2222));
		bean.setLiush("100000001");
		bean.setBeiz("tes2t");
		List<Chushzyb> chushzybs = new ArrayList<Chushzyb>();
		chushzybs.add(bean);
		
		//boolean flag = chushzybService.doUpdate(chushzybs);
		//org.junit.Assert.assertEquals(true, flag);
	}
	
	@Test
	public void doRemoveTest(){
		Chushzyb bean = new Chushzyb();
		bean.setId(0);
		List<Chushzyb> chushzybs = new ArrayList<Chushzyb>();
		chushzybs.add(bean);
		
		boolean flag = chushzybService.doRemove(chushzybs);
		org.junit.Assert.assertEquals(false, flag);
	}
	
	@Test
	public void doRemoveTest2(){
		Map<String,String> map = new HashMap<String, String>();
		Map result = chushzybService.queryChushzybByParam(new Chushzyb(), map);
		List<Chushzyb> list = (ArrayList<Chushzyb>)result.get("rows");
		
		Chushzyb bean = list.get(0);
		List<Chushzyb> chushzybs = new ArrayList<Chushzyb>();
		chushzybs.add(bean);
		
		boolean flag = chushzybService.doRemove(chushzybs);
		org.junit.Assert.assertEquals(true, flag);
	}
	
	@Test
	public void queryShengcxByParamTest(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercenter", "UW");
		params.put("lingjbh", "100PB200");
		List<Lingjxhd> list = this.lingjxhdService.queryShengcxByParam(params);
		
		org.junit.Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void queryXiaohdByParamTest(){ 
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercenter", "UW");
		params.put("lingjbh", "100PB200");
		params.put("shengcxbh", "00001");
		
		List<Lingjxhd> list = this.lingjxhdService.queryXiaohdByParam(params);
		org.junit.Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void queryWeijfzlByParamTest(){ 
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercenter", "UW");
		params.put("lingjbh", "100PB200");
		params.put("shengcxbh", "00001");
		params.put("xiaohdbh", "000000001");
		
		Lingjxhd lingjxhd = this.lingjxhdService.queryWeijfzlByParam(params);
		
		org.junit.Assert.assertEquals(new BigDecimal(500), lingjxhd.getJicdwjfl());
	}
	
	public void queryLiushByXhdTest(){ 
		Map<String, String> params = new HashMap<String, String>();
		params.put("gongyxhd", "000000001");
		
		String liush = this.gongyxhdService.queryLiushByXhd(params);
		
		org.junit.Assert.assertEquals("100000000", liush);
	}
	
}

/**
 * 
 */
package com.athena.xqjs.module.laxkaix;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.laxkaix.Laxjh;
import com.athena.xqjs.entity.laxkaix.Laxjhmx;
import com.athena.xqjs.entity.laxkaix.LinsKeytclj;
import com.athena.xqjs.entity.laxkaix.LinsTclj;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.laxkaix.service.LaxkaixTestService;
import com.toft.core3.container.annotation.Inject;

/**
 * @author dsimedd001
 *
 */
@TestData(locations = {"classpath:testData/laxkaix/laxjh.xls"})
public class LaxjhTest extends AbstractCompomentTests{
	
	@Inject
	private LaxjhService laxjhService;
	
	@Inject
	private LaxkaixTestService laxkaixTestService;

	/**
	 * 获取重箱区库存零件数量Test
	 */
	@Test
	public void getZxqkcljslTest(){
		Map zxqkc = new HashMap();
		zxqkc.put("9672978480", new BigDecimal("74"));
		zxqkc.put("ZQ80571880", new BigDecimal("152"));
		zxqkc.put("7903011394", new BigDecimal("68"));
		zxqkc.put("9681830880", new BigDecimal("5"));
		zxqkc.put("9676420880", new BigDecimal("56"));
		zxqkc.put("ZQ80604180", new BigDecimal("135"));
		Assert.assertEquals(new BigDecimal("74"),laxjhService.getZxqkcljsl(zxqkc,"9672978480"));
		Assert.assertEquals(new BigDecimal("152"),laxjhService.getZxqkcljsl(zxqkc,"ZQ80571880"));
		Assert.assertEquals(new BigDecimal("68"),laxjhService.getZxqkcljsl(zxqkc,"7903011394"));
		Assert.assertEquals(new BigDecimal("5"),laxjhService.getZxqkcljsl(zxqkc,"9681830880"));
		Assert.assertEquals(new BigDecimal("56"),laxjhService.getZxqkcljsl(zxqkc,"9676420880"));
		Assert.assertEquals(new BigDecimal("135"),laxjhService.getZxqkcljsl(zxqkc,"ZQ80604180"));
	}
	/**
	 * 计算零件数量Test
	 */
	@Test
	public void getLjslsTest(){
		Map<String,BigDecimal> ljkcMap = new HashMap<String,BigDecimal>();
		ljkcMap.put("ZQ80571880",  new BigDecimal("74"));
		ljkcMap.put("7903011266",  new BigDecimal("30"));
		ljkcMap.put("7903011394",  new BigDecimal("108"));
		ljkcMap.put("ZQ80571780",  new BigDecimal("108"));
		ljkcMap.put("ZQ80571781",  new BigDecimal("23"));
		ljkcMap.put("9676370180",  new BigDecimal("28"));
		ljkcMap.put("9676370181",  new BigDecimal("11"));
		ljkcMap.put("9676370182",  new BigDecimal("3"));
		Map<String,List<String>> tdjMap = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add("9676370181");
		list.add("9676370182");
		tdjMap.put("9676370180", list);
		List<String> newList = new ArrayList<String>();
		Map<String,List<String>> newTdjMap = new HashMap<String,List<String>>();
		newList.add("ZQ80571781");
		newTdjMap.put("ZQ80571780", newList);
		BigDecimal ljsls = laxjhService.getLjsls(ljkcMap, tdjMap, "9676370180");
		Assert.assertEquals(new BigDecimal("42"),ljsls);
		Assert.assertEquals(new BigDecimal("131"),laxjhService.getLjsls(ljkcMap, newTdjMap, "ZQ80571780"));
	}
	/**
	 * 获取替代件Map Test
	 */
	@Test
	public void getTdjMapTest(){
		List<Map<String,Object>> allTdjList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> newmap = new HashMap<String,Object>();
		Map<String,Object> testmap = new HashMap<String,Object>();
		map.put("LINGJBH", "9676370180");
		map.put("TIDLJH", "9676370181");
		allTdjList.add(map);
		newmap.put("LINGJBH", "9676370189");
		newmap.put("TIDLJH", "9676370183");
		allTdjList.add(newmap);
		testmap.put("LINGJBH", "9676370180");
		testmap.put("TIDLJH", "9676370182");
		allTdjList.add(testmap);
		Map tdjMap = laxjhService.getTdjMap(allTdjList);
		List list = (List)tdjMap.get("9676370180");
		Assert.assertEquals("9676370181",list.get(0));
		Assert.assertEquals("9676370182",list.get(1));
	}
	/**
	 * 获取所有的替代件
	 */
	@Test
	public void getAlllTdjListTest(){
		Laxjh laxjh = new Laxjh();
		laxjh.setUsercenter("UW");
		List<Map<String, Object>> list = laxjhService.getAlllTdjList(laxjh);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LINGJBH", "9676370180");
		map.put("TIDLJH", "9676370181");
		Assert.assertEquals(map,list.get(0));
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("LINGJBH", "9676370280");
		map1.put("TIDLJH", "9676370281");
		Assert.assertEquals(map1,list.get(1));
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("LINGJBH", "9676370380");
		map2.put("TIDLJH", "9676370381");
		Assert.assertEquals(map2,list.get(2));
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("LINGJBH", "ZQ80572180");
		map3.put("TIDLJH", "ZQ81572181");
		Assert.assertEquals(map3,list.get(3));
	}
	/**
	 * 获取所有零件库存Test
	 */
	@Test
	public void getAllLjkcTest(){
		Laxjh laxjh = new Laxjh();
		laxjh.setUsercenter("UW");
		List<Map<String, Object>> list = laxjhService.getAllLjkc(laxjh);
		Assert.assertEquals(48,list.size());
		Map<String, Object> map = list.get(0);
		Assert.assertEquals("7540759080",map.get("LINGJBH"));
		Assert.assertEquals(new BigDecimal(32),map.get("SUMLINGJSL"));
		Map<String, Object> map47 = list.get(47);
		Assert.assertEquals("ZQ80605980",map47.get("LINGJBH"));
		Assert.assertEquals(new BigDecimal(32),map47.get("SUMLINGJSL"));
	}

	/**
	 * 插入零时需求Test
	 */
	@Test
	public void insertLinsXuqTest(){
		List<LinsXuq> list = new ArrayList<LinsXuq>();
		for(int i=0;i<5;i++){
			LinsXuq lxuq = new LinsXuq();
			lxuq.setUsercenter("UW");
			// 设置毛需求版次
			lxuq.setMaoxqbc("A004");
			String jihydm = "A021";
			// 设置计划员代码
			lxuq.setJihydm(jihydm);
			// 设置零件编号
			lxuq.setLingjh("A00011"+i);
			// 设置零件CMJ
			lxuq.setCmj(new BigDecimal("67"));
			// 设置创建者
			lxuq.setCreator("root");
			// 设置创建时间
			lxuq.setCreateTime(DateUtil.curDateTime());
			laxkaixTestService.deleteLinsXuq(lxuq);
			list.add(lxuq);
		}
		laxjhService.insertLinsXuq(list);
		
	}
	/**
	 * 获取零件重箱区库存，将其Map进行分组
	 */
	@Test
	public void getZxqkcTest(){
		Map map = new HashMap();
		List<Map<String, Object>> zxqkcList = new ArrayList();
		Map newMap = new HashMap();
		newMap.put("LINGJH", "ZQ80572080");
		newMap.put("SUMLINGJSL", new BigDecimal(34));
		zxqkcList.add(newMap);
		Map testMap = new HashMap();
		testMap.put("LINGJH", "ZQ80572081");
		testMap.put("SUMLINGJSL", new BigDecimal(35));
		zxqkcList.add(testMap);
		Map test = new HashMap();
		test.put("LINGJH", "ZQ80572082");
		test.put("SUMLINGJSL", new BigDecimal(36));
		zxqkcList.add(test);
		map = laxjhService.getZxqkc(zxqkcList);
		Assert.assertEquals(new BigDecimal(34),map.get("ZQ80572080"));
		Assert.assertEquals(new BigDecimal(35),map.get("ZQ80572081"));
		Assert.assertEquals(new BigDecimal(36),map.get("ZQ80572082"));
	}
	/**
	 * 插入临时零件
	 */
	@Test
	public void insertLinsTcljTest(){
		List<LinsTclj> linsTcljList = new ArrayList();
		for(int i=0;i<5;i++){
			LinsTclj tclj = new LinsTclj();
			// 设置用户中心
			tclj.setUsercenter("UW");
			String jihydm = "A06";
			// 设置计划员代码
			tclj.setJihydm(jihydm);
			// 设置TC NO
			tclj.setTcNo("TC101"+i);
			// 设置启运点时间
				String qiysjs = "2012-03-23";
				tclj.setQiysj(qiysjs);
			// 设置目的地
			tclj.setMudd("DPC");
			// 设置零件号
			tclj.setLingjh("A93921122"+i);
			// 设置零件数量
			tclj.setLingjsl(new BigDecimal(i));
			// 设置物理点
			tclj.setWuld("WHG");
			// 设置预计到达时间
			tclj.setYujddsj("2012-05-01");
			// 设置拉箱指定到达时间
			tclj.setLaxzdddsj("2012-04-25");
			tclj.setCreator("root");
			String createTime = DateUtil.curDateTime();
			tclj.setCreateTime(createTime);
			linsTcljList.add(tclj);
		}
		laxjhService.insertLinsTclj(linsTcljList);
		List<Map<String,Object>> tcljlist = laxjhService.getSumLingjslByTc(null);
		List lingjbhList = new ArrayList();
		for(Map<String,Object> map:tcljlist){
			String lingjbh = (String)map.get("LINGJH");
			if(lingjbh.equals("A939211220")){
				lingjbhList.add(lingjbh);
			}
		}
		Assert.assertEquals("A939211220",lingjbhList.get(0));
		
	}
	/**
	 * 根据用户中心UW获取重箱区库存
	 */
	@Test
	public void getZxqkcByUcUWTest(){
		String usercenter="UW";
		List<Map<String, Object>> sumzxqkc = new ArrayList<Map<String, Object>>();
		sumzxqkc = laxjhService.getZxqkcByUc(usercenter);
	}
	/**
	 * 根据用户中心UX获取重箱区库存
	 */
	@Test
	public void getZxqkcByUcUXTest(){
		String usercenter = "UX";
		List<Map<String, Object>> newsumzxqkc = new ArrayList();
		newsumzxqkc = laxjhService.getZxqkcByUc(usercenter);
	}
	/**
	 * 拉箱计划Test
	 * @throws ParseException 
	 */
	@Test
	public void jsLaxjhTest() throws ParseException{
		LinsXuq linsXuq = new LinsXuq();
		// 设置毛需求版次
		linsXuq.setMaoxqbc("A002");
		// 设置用户中心
		Laxjh laxjh = new Laxjh();
		laxjh.setUsercenter("UW");
		laxjh.setAnqkc(new BigDecimal("1"));
		laxjh.setLaxjx(new BigDecimal("5"));
		laxjh.setSuanfcl("1");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		laxjhService.jsLaxjh(linsXuq, laxjh, loginUser,"");
	}
	/**
	 * 更新临时需求Test
	 */
	@Test
	public void updateLinsxuqTest(){
		List keyList = new ArrayList();
		String key = "UW:A021:TC01:A000110";
		keyList.add(key);
		Map tcljManzslMap = new HashMap();
		tcljManzslMap.put(key, new BigDecimal("32"));
		LinsXuq linsXuq = new LinsXuq();
		laxjhService.updateLinsxuq(keyList, tcljManzslMap, linsXuq,"root");
		LinsXuq xuq = new LinsXuq();
		xuq.setUsercenter("UW");
		xuq.setJihydm("A021");
		xuq.setLingjh("A000110");
		LinsXuq resultxuq = laxkaixTestService.getLinsXuq(xuq);
		Assert.assertEquals(new BigDecimal("32"), resultxuq.getManzsl());
		
	}

	/**
	 * 查询拉箱计划中可用TC
	 */
	@Test
	public void getSelectLaxjhKeytcTest(){
		 Map tjmap = new HashMap();
		 tjmap.put("suanfcl", "1");
		 List<Map<String, Object>> map = laxjhService.getSelectLaxjhKeytc(tjmap);
	}
	/**
	 *  获取该用户中心下的可用零件明细
	 */
	@Test
	public void getNewKeyTcljListTest(){
		Laxjh bean = new Laxjh();
		bean.setUsercenter("UW");
		List newKeyTcljList = laxjhService.getNewKeyTcljList(bean);
	}
	/**
	 * 获取零件信息
	 */
	@Test
	public void getTcljInfoTest(){
		List<LinsKeytclj> newKeyTcljList = new ArrayList();
		LinsKeytclj tclj = new LinsKeytclj();
		tclj.setUsercenter("UW");
		tclj.setJihydm("A01");
		tclj.setTcNo("TC01");
		tclj.setLingjh("ZQ80572080");
		newKeyTcljList.add(tclj);
		laxjhService.getTcljInfo(newKeyTcljList);
	}
	@Test
	public void getNewTcKeyListTest(){
		List<Map<String, Object>> tcManzlist = new ArrayList();
		Map map = new HashMap();
		map.put("USERCENTER", "UW");
		map.put("JIHYDM", "A01");
		map.put("TCNO", "TC01");
		tcManzlist.add(map);
		laxjhService.getNewTcKeyList(tcManzlist);
	}

	/**
	 * 更新可用TC和可用TC零件
	 */
	@Test
	public void updateKeytcAndljTest(){
		String username = "root";
		String key ="UW:A01:TC01";
		String newkey ="UW:A02:TC02";
		List tcNoList = new ArrayList();
		tcNoList.add(key);
		tcNoList.add(newkey);
		Map tcLingjMap = new HashMap();
		List list = new ArrayList();
		list.add("ZQ80572080");
		list.add("ZQ80572081");
		list.add("ZQ80572082");
		tcLingjMap.put(key, list);
		List newList = new ArrayList();
		newList.add("ZQ80572080");
		newList.add("ZQ80572081");
		tcLingjMap.put(newkey, newList);
		Map manydMap = new HashMap();
		String lingjkey = key+":ZQ80572080";
		String newlingjkey = newkey+":ZQ80572080";
		manydMap.put(lingjkey, new BigDecimal(34));
		manydMap.put(newlingjkey, new BigDecimal(23));
		laxjhService.updateKeytcAndlj(tcNoList, tcLingjMap, manydMap, username);
	}
	
	/**
	 * 获取需求数量为null的零件信息 条件:用户中心、毛需求版次
	 */
	@Test
	public void getLingjbjTest(){
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setMaoxqbc("A002");
		laxjhService.getLingjbj(linsXuq);
	}
	/**
	 * 获取滞箱天数
	 */
	@Test
	public void getZxtsTest(){
		laxjhService.getZxts("");
	}

	@Test
	public void queryLaxjhTest(){
		Laxjh laxjh = new Laxjh();
		laxjhService.queryLaxjh(laxjh, laxjh);
	}
	@Test
	public void editLinsxuqTest(){
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setXuqsl(new BigDecimal("2000"));
		linsXuq.setEditor("root");
		linsXuq.setEditTime(DateUtil.getCurrentDate());
		linsXuq.setUsercenter("UW");
		linsXuq.setJihydm("A01");
		linsXuq.setLingjh("ZQ80604180");
		laxjhService.editLinsxuq(linsXuq, loginUser);
	}
	@Test
	public void saveLinsXuqTest(){
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setUsercenter("UW");
		linsXuq.setJihydm("A01");
		linsXuq.setLingjh("ZQ805719PT");
		linsXuq.setXuqsl(new BigDecimal("2001"));
		linsXuq.setZhongxqkc(new BigDecimal("1001"));
		linsXuq.setCangkkc(new BigDecimal("100"));
		linsXuq.setCmj(new BigDecimal("32.456"));
		laxjhService.saveLinsXuq(linsXuq, loginUser);
	}

	@Test
	public void deleteLinsxuqTest(){
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setUsercenter("UW");
		linsXuq.setJihydm("A01");
		linsXuq.setLingjh("ZQ80604180");
		laxjhService.deleteLinsxuq(linsXuq);
	}

	@Test
	public void queryLaxjhWeimzljTest(){
		Weimzlj weimzlj = new Weimzlj();
		Map<String, String> params = new HashMap<String, String>();
		weimzlj.setJihNo("UW20120000");
		laxjhService.queryLaxjhWeimzlj(weimzlj,params);
	}
	@Test
	public void editLaxjhmxTest(){
		Laxjhmx laxjhmx = new Laxjhmx();
		laxjhmx.setUsercenter("UW");
		laxjhmx.setLaxjhNo("UW20120000");
		laxjhmx.setId("6");
		laxjhmx.setTcNo("TC06");
		laxjhService.editLaxjhmx(laxjhmx);
	}

	@Test
	public void sxLaxjhTest(){
		Laxjh laxjh = new Laxjh();
		laxjh.setLaxjhh("UW20120000");
		laxjh.setUsercenter("UW");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		laxjhService.sxLaxjh(laxjh, loginUser);
	}
	@Test
	public void qxLaxjhTest(){
		Laxjh laxjh = new Laxjh();
		laxjh.setLaxjhh("UW20120000");
		laxjh.setUsercenter("UW");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		laxjhService.qxLaxjh(laxjh, loginUser);
	}
	/**
	 * 获取预计到货数量Test
	 */
	@Test
	public void getYujdhslTest(){
		TC tc = new TC();
		tc.setLaxzdddsj("2012-05-08");
		List<Map<String, Object>> list = laxjhService.getYujdhsl(tc);
		Map<String,Object> map = list.get(0);
		String lingjbh = (String)map.get("LINGJBH");
		Assert.assertEquals("7540759080",lingjbh);
	}
	 public static String getUUID(){ 
	        String s = UUID.randomUUID().toString(); 
	        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	} 

}

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
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.Kaixjh;
import com.athena.xqjs.entity.laxkaix.Kaixjhmx;
import com.athena.xqjs.entity.laxkaix.LinsKeytclj;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.module.laxkaix.service.KaixjhService;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.laxkaix.service.LaxkaixTestService;
import com.toft.core3.container.annotation.Inject;
import com.toft.utils.json.JSONException;

/**
 * @author dsimedd001
 *
 */
@TestData(locations = {"classpath:testData/laxkaix/laxjh.xls"})
public class KaixjhTest extends AbstractCompomentTests{
	@Inject
	private KaixjhService kaixjhService;
	@Inject
	private LaxjhService laxjhService;
	@Inject
	private LaxkaixTestService laxkaixTestService;
	/**
	 * 计算开箱计划零件短缺Test
	 */
	@Test
	public void jsLingjDuanqByBcTest(){
		Maoxqmx xqmx = new Maoxqmx();
		xqmx.setXuqbc("A004");
		xqmx.setUsercenter("UW");
		Kaixjh laxjh = new Kaixjh();
		laxjh.setKaixjx(new BigDecimal("10"));
		laxjh.setAnqkc(new BigDecimal("1"));
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		loginUser.setUsername("root");
		kaixjhService.jsLingjDuanq(xqmx, laxjh,loginUser);
	}
	@Test
	public void jsLingjDuanqByBcErrorTest(){
		Maoxqmx xqmx = new Maoxqmx();
		xqmx.setXuqbc("A121");
		xqmx.setUsercenter("UW");
		Kaixjh laxjh = new Kaixjh();
		laxjh.setKaixjx(new BigDecimal("1"));
		laxjh.setAnqkc(new BigDecimal("1"));
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		loginUser.setUsername("root");
		kaixjhService.jsLingjDuanq(xqmx, laxjh,loginUser);
	}
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
		BigDecimal zxqkcB = kaixjhService.getZxqkcljsl(zxqkc,"ZQ80604180");
		Assert.assertEquals(new BigDecimal("135"),zxqkcB);
	}
	/**
	 * 计算零件数量Test
	 */
	@Test
	public void getLjslsTest(){
		Map ljkcMap = new HashMap();
		ljkcMap.put("ZQ80571880",  new BigDecimal("74"));
		ljkcMap.put("7903011266",  new BigDecimal("30"));
		ljkcMap.put("7903011394",  new BigDecimal("108"));
		ljkcMap.put("ZQ80571780",  new BigDecimal("108"));
		ljkcMap.put("ZQ80571780",  new BigDecimal("23"));
		ljkcMap.put("9676370180",  new BigDecimal("28"));
		ljkcMap.put("9676370181",  new BigDecimal("11"));
		ljkcMap.put("9676370182",  new BigDecimal("3"));
		Map tdjMap = new HashMap();
		List list = new ArrayList();
		list.add("9676370181");
		list.add("9676370182");
		tdjMap.put("9676370180", list);
		BigDecimal ljsls = kaixjhService.getLjsls(ljkcMap, tdjMap, "9676370180");
		Assert.assertEquals(new BigDecimal("42"),ljsls);
	}
	/**
	 * 获取替代件Map Test
	 */
	@Test
	public void getTdjMapTest(){
		List<Map<String,Object>> allTdjList = new ArrayList();
		Map<String,Object> map = new HashMap();
		Map<String,Object> newmap = new HashMap();
		Map<String,Object> testmap = new HashMap();
		map.put("LINGJBH", "9676370180");
		map.put("TIDLJH", "9676370181");
		allTdjList.add(map);
		newmap.put("LINGJBH", "9676370189");
		newmap.put("TIDLJH", "9676370183");
		allTdjList.add(newmap);
		testmap.put("LINGJBH", "9676370180");
		testmap.put("TIDLJH", "9676370182");
		allTdjList.add(testmap);
		Map tdjMap = kaixjhService.getTdjMap(allTdjList);
		List list = (List)tdjMap.get("9676370180");
		Assert.assertEquals("9676370181",list.get(0));
	}
	/**
	 * 获取所有的替代件
	 */
	@Test
	public void getAlllTdjListTest(){
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setUsercenter("UW");
		List<Map<String, Object>> list = kaixjhService.getAlllTdjList(kaixjh);
		Map<String, Object> map = list.get(0);
		Assert.assertEquals("9676370180",map.get("LINGJBH"));
	}
	/**
	 * 获取所有零件库存Test
	 */
	@Test
	public void getAllLjkcTest(){
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setUsercenter("UW");
		List<Map<String, Object>> list = kaixjhService.getAllLjkc(kaixjh);
		Map<String, Object> map = list.get(0);
		Assert.assertEquals("ZQ80571880",map.get("LINGJBH"));
		Assert.assertEquals(new BigDecimal(30),map.get("SUMLINGJSL"));
	}
	/**
	 * 获取零件数量Map Test
	 */
	@Test
	public void getljslMapTest(){
		List<Map<String,Object>> ljslList = new ArrayList();
		Map<String,Object> map = new HashMap();
		Map<String,Object> newmap = new HashMap();
		Map<String,Object> testmap = new HashMap();
		map.put("LINGJBH", "ZQ80571880");
		map.put("SUMLINGJSL", new BigDecimal("152"));
		ljslList.add(map);
		newmap.put("LINGJBH", "7903011394");
		newmap.put("SUMLINGJSL", new BigDecimal("68"));
		ljslList.add(newmap);
		testmap.put("LINGJBH", "9672978480");
		testmap.put("SUMLINGJSL", new BigDecimal("74"));
		ljslList.add(testmap);
		Map ljslMap = kaixjhService.getljslMap(ljslList);
		Assert.assertEquals(new BigDecimal(152),ljslMap.get("ZQ80571880"));
	}
	/**
	 * 获取预计到货数量Test
	 */
	@Test
	public void getYujdhslTest(){
		TC tc = new TC();
		tc.setLaxzdddsj("2012-05-08");
		List<Map<String, Object>> list = kaixjhService.getYujdhsl(tc);
	}
	
	
	/**
	 * 插入零时需求Test
	 */
	@Test
	public void insertLinsXuqTest(){
		List<LinsXuq> list = new ArrayList();
		for(int i=0;i<5;i++){
			LinsXuq lxuq = new LinsXuq();
			lxuq.setUsercenter("UW");
			// 设置毛需求版次
			lxuq.setMaoxqbc("A008");
			String jihydm = "A02";
			// 设置计划员代码
			lxuq.setJihydm(jihydm);
			// 设置零件编号
			lxuq.setLingjh("A00012"+i);
			// 设置零件CMJ
			lxuq.setCmj(new BigDecimal("67"));
			// 设置创建者
			lxuq.setCreator("root");
			// 设置创建时间
			lxuq.setCreateTime(DateUtil.curDateTime());
			list.add(lxuq);
		}
		kaixjhService.insertLinsXuq(list);
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
		map = kaixjhService.getZxqkc(zxqkcList);
		Assert.assertEquals(new BigDecimal(34),map.get("ZQ80572080"));
	}

	/**
	 * 根据用户中心UW获取重箱区库存
	 */
	@Test
	public void getZxqkcByUcUWTest(){
		String usercenter="UW";
		List<Map<String, Object>> sumzxqkc = new ArrayList();
		sumzxqkc = kaixjhService.getZxqkcByUc(usercenter);
	}
	/**
	 * 根据用户中心UX获取重箱区库存
	 */
	@Test
	public void getZxqkcByUcUXTest(){
		String usercenter = "UX";
		List<Map<String, Object>> newsumzxqkc = new ArrayList();
		newsumzxqkc = kaixjhService.getZxqkcByUc(usercenter);
	}
	@Test
	public void jsKaixjhTest() throws ParseException{
		LinsXuq linsXuq = new LinsXuq();
		// 设置毛需求版次
		linsXuq.setMaoxqbc("A004");
		// 设置用户中心
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setUsercenter("UW");
		kaixjh.setAnqkc(new BigDecimal("1"));
		kaixjh.setKaixjx(new BigDecimal("10"));
		kaixjh.setSuanfcl("1");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		kaixjhService.jsKaixjh(linsXuq, kaixjh, loginUser,"");
	}
	@Test
	public void updateLinsxuqTest(){
		List keyList = new ArrayList();
		String key = "UW:A01:TC01:ZQ80572080";
		keyList.add(key);
		Map tcljManzslMap = new HashMap();
		tcljManzslMap.put(key, new BigDecimal("32"));
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setMaoxqbc("A002");
		kaixjhService.updateLinsxuq(keyList, tcljManzslMap, linsXuq,"root");
	}
	
	@Test
	public void getSelectKaixjhKeytcTest(){
		 Map tjmap = new HashMap();
		 tjmap.put("suanfcl", "1");
		 List<Map<String, Object>> map = kaixjhService.getSelectKaixjhKeytc(tjmap);
	}
	
	@Test
	public void getNewKeyTcljListTest(){
		Kaixjh bean = new Kaixjh();
		bean.setUsercenter("UW");
		List newKeyTcljList = kaixjhService.getNewKeyTcljList(bean);
	}
	@Test
	public void getTcljInfoTest(){
		List<LinsKeytclj> newKeyTcljList = new ArrayList();
		LinsKeytclj tclj = new LinsKeytclj();
		tclj.setUsercenter("UW");
		tclj.setJihydm("A01");
		tclj.setTcNo("TC01");
		tclj.setLingjh("ZQ80572080");
		newKeyTcljList.add(tclj);
		kaixjhService.getTcljInfo(newKeyTcljList);
	}
	@Test
	public void getNewTcKeyListTest(){
		List<Map<String, Object>> tcManzlist = new ArrayList();
		Map map = new HashMap();
		map.put("USERCENTER", "UW");
		map.put("JIHYDM", "A01");
		map.put("TCNO", "TC01");
		tcManzlist.add(map);
		kaixjhService.getNewTcKeyList(tcManzlist);
	}

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
		kaixjhService.updateKeytcAndlj(tcNoList, tcLingjMap, manydMap, username);
	}

	@Test
	public void getLingjbjTest(){
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setMaoxqbc("A002");
		kaixjhService.getLingjbj(linsXuq);
	}
	@Test
	public void getZxtsTest(){
		kaixjhService.getZxts("");
	}
	
	@Test
	public void queryLingjbjTest() throws JSONException{
		LinsXuq bean = new LinsXuq();
		bean.setUsercenter("UW");
		Map map = kaixjhService.queryLingjbj(bean, bean,"10","1");
	}
	@Test
	public void queryLaxjhTest(){
		Kaixjh kaixjh = new Kaixjh();
		kaixjhService.queryKaixjh(kaixjh, kaixjh);
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
		linsXuq.setLingjh("7903008211");
		kaixjhService.editLinsxuq(linsXuq, loginUser);
	}
	@Test
	public void saveLinsXuqTest(){
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setUsercenter("UW");
		linsXuq.setJihydm("A01");
		linsXuq.setLingjh("ZQ805719PF");
		linsXuq.setXuqsl(new BigDecimal("2001"));
		linsXuq.setZhongxqkc(new BigDecimal("1001"));
		linsXuq.setCangkkc(new BigDecimal("100"));
		linsXuq.setCmj(new BigDecimal("32.456"));
		kaixjhService.saveLinsxuq(linsXuq, loginUser);
	}

	@Test
	public void deleteLinsxuqTest(){
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setUsercenter("UW");
		linsXuq.setJihydm("A01");
		linsXuq.setLingjh("ZQ80604181");
		laxjhService.deleteLinsxuq(linsXuq);
	}
	
	@Test
	public void queryKaixjhWeimzljTest(){
		Weimzlj weimzlj = new Weimzlj();
		weimzlj.setJihNo("UW20120000");
		kaixjhService.queryKaixjhWeimzlj(weimzlj);
	}
	
	@Test
	public void editKaixjhmxTest(){
		Kaixjhmx kaixjhmx = new Kaixjhmx();
		kaixjhmx.setUsercenter("UW");
		kaixjhmx.setKaixjhNo("UW20120000");
		kaixjhmx.setId("6");
		kaixjhmx.setTcNo("TC06");
		kaixjhService.editKaixjhmx(kaixjhmx);
	}
	@Test
	public void saveKaixjhmxTest(){
		Kaixjhmx kaixjhmx = new Kaixjhmx();
		kaixjhmx.setUsercenter("UW");
		kaixjhmx.setKaixjhNo("UW20120000");
		kaixjhmx.setId("9");
		kaixjhmx.setTcNo("TC10");
		kaixjhmx.setKaixzdsj("2012-04-09");
		kaixjhmx.setQiysj("2012-02-12");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		laxkaixTestService.deleteKaixjhmxByKaijhNo(kaixjhmx);
		kaixjhService.saveKaixjhmx(kaixjhmx,loginUser);
	}
	@Test
	public void sxKaixjhTest(){
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setKaixjhh("UW20120000");
		kaixjh.setUsercenter("UW");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		kaixjhService.sxKaixjh(kaixjh, loginUser);
	}
	@Test
	public void qxKaixjhTest(){
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setKaixjhh("UW20120000");
		kaixjh.setUsercenter("UW");
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("root");
		kaixjhService.qxKaixjh(kaixjh, loginUser);
	}
	
	
	 public static String getUUID(){ 
	        String s = UUID.randomUUID().toString(); 
	        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	} 
}

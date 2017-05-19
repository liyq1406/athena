package com.athena.fj.module;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.module.service.DictionaryService;
import com.toft.core3.container.annotation.Inject;

/**
 * 数据字典测试
 * @author 贺志国
 * @date 2012-3-2
 *
 */
@TestData(locations = {"classpath:testdata/fj/dictionary.xls"})
public class DictionaryTest extends AbstractCompomentTests {
	@Inject
	private DictionaryService dictionaryService;
	
	/**
	 * 参考系客户查询
	 * @author hzg
	 * @date 2012-3-2
	 */
	@Test
	public void testSelectKeh(){
		List<Map<String,String>> kehList = dictionaryService.selectKeh();
		assertEquals("WUHAN",kehList.get(0).get("KEHBH"));
		assertEquals("武汉一厂",kehList.get(0).get("KEHMC"));
	}
	
	/**
	 * 参考系运输商查询
	 * @author hzg
	 * @date 2012-3-2
	 */
	@Test
	public void testSelectYunss(){
		List<Map<String,String>> yunssList = dictionaryService.selectYunss("UW");
		assertEquals("CYS001",yunssList.get(0).get("GCBH"));
		assertEquals("汉阳物流中心001",yunssList.get(0).get("GONGSMC"));
	}
	
	/**
	 * 参考系车型查询
	 * @author hzg
	 * @date 2012-3-2
	 */
	@Test
	public void testSelectChex(){
		List<Map<String,String>> yunssList = dictionaryService.selectChex();
		assertEquals("DC",yunssList.get(0).get("CHEXBH"));
		assertEquals("大车",yunssList.get(0).get("CHEXMC"));
	}
	
	/**
	 * 参考系运输路线查询
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testSelectYunslx(){
		List<Map<String,String>> list = dictionaryService.selectYunslx("UW");
		assertEquals("LX1",list.get(0).get("YUNSLXBH"));
		assertEquals("W001",list.get(0).get("YUNSLXMC"));
	}
	
	/**
	 * 参考系客户成品库查询，根据运输路线查询客户
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testSelectKehCpk(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("yunslxbh", "LX1");
		param.put("usercenter", "UW");
		List<Map<String,String>> list = dictionaryService.selectKehCpk(param);
		assertEquals("WUHAN",list.get(0).get("KEHBH"));
		assertEquals("武汉一厂",list.get(0).get("KEHMC"));
		
	}
	
	
	/**
	 * 参考系客户成品库查询，根据运输路线查询运输商
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testSelectYunssCpk(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("yunslxbh", "LX1");
		param.put("usercenter", "UW");
		List<Map<String,String>> list = dictionaryService.selectYunssCpk(param);
		assertEquals("CYS002",list.get(0).get("CHENGYSBH"));
		assertEquals("汉阳物流中心002",list.get(0).get("GONGSMC"));
		
	}
	
	/**
	 * 参考系车型运输商关系查询，根据运输商查询车型
	 * @author 贺志国
	 * @date 2012-3-26
	 */
	@Test
	public void testSelectChexYunss(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("yunssbm", "CYS001");
		param.put("usercenter", "UW");
		List<Map<String,String>> list = dictionaryService.selectChexYunss(param);
		assertEquals("DC",list.get(0).get("CHEXBH"));
		assertEquals("大车",list.get(0).get("CHEXMC"));
		
	}
	
	
	
	
	
}

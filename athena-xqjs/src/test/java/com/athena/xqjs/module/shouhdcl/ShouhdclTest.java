package com.athena.xqjs.module.shouhdcl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.shouhdcl.Jusgzd;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.shouhdcl.service.ShouhdclService;
import com.toft.core3.container.annotation.Inject;

/**
 * 
 * @author dsimedd001
 *
 */
@TestData(locations = {"classpath:testdata/shouhdcl/shouhdclData.xls"})
public class ShouhdclTest extends AbstractCompomentTests {

	@Inject
	private ShouhdclService shouhdcl;
	
	private  Map<String ,String> param;
	private Jusgzd jusgzd;
	private ArrayList<Jusgzd> jusgzds;
	private Yaohl yaohl;
	private Dingd dingd;
	@Rule
    public   ExternalResource resource= new ExternalResource(){
   	@Override
   	protected void before() throws Throwable{
   		
   		//数据准备
   		param = new HashMap<String ,String>();
   		jusgzd = new Jusgzd();
   		yaohl = new Yaohl();
   		jusgzds = new ArrayList<Jusgzd>();
   		dingd = new Dingd();
   	}
	};	
	
	/**
	 * 测试查询收货待处理
	 */
	@Test
	public void testQueryShouhdcl(){
		param.put("pageNo", "1");
		param.put("pageSize", "10");
		org.junit.Assert.assertNotNull(shouhdcl.queryShouhdcl(jusgzd, param));
	}
	
	/**
	 * 测试查询收货待处理列表
	 */
	@Test
	public void testQueryShouhdclshlb(){
		param.put("pageNo", "1");
		param.put("pageSize", "10");
		org.junit.Assert.assertNotNull(shouhdcl.queryShouhdclshlb(jusgzd, param));
	}
	
	/**
	 * 测试审核
	 */
	@Test
	public void testShenH(){
		param.put("pageNo", "1");
		param.put("pageSize", "10");
		jusgzd.setUsercenter("UW");
		jusgzd.setJusgzdh("Bb000186");
		jusgzd.setChuljg("1");
		jusgzds.add(jusgzd);
		shouhdcl.shenH(jusgzds);
		org.junit.Assert.assertNotNull(shouhdcl.queryShouhdcl(jusgzd, param));
	}
	
	/**
	 * 测试查询要货令
	 */
	@Test
	public void testQueryYaohl(){
		
		jusgzd.setUsercenter("UW");
		jusgzd.setJusgzdh("Bb000187");
		
		dingd.setUsercenter("UW");
		dingd.setDingdh("DINGD001");
		dingd.setGongysdm("111111");
		dingd.setDingdlx("3");
		dingd.setHeth("11111");
		dingd.setFahzq("201206");
		dingd.setBeiz("1111");
		dingd.setShiffsgys("1");
		//shouhdcl.saveLinsdd(dingd, jusgzd, "sys");
		
		dingd.setDingdh("DINGD002");
		dingd.setGongysdm("111111");
		dingd.setDingdlx("3");
		dingd.setHeth("11111");
		dingd.setFahzq("201206");
		dingd.setBeiz("1111");
		dingd.setShiffsgys("2");
		//shouhdcl.saveLinsdd(dingd, jusgzd, "sys");
		
		param.put("pageNo", "1");
		param.put("pageSize", "10");
		org.junit.Assert.assertNotNull(shouhdcl.queryYaohl(yaohl, param));
	}
	
	/**
	 * 测试指定要货令
	 */
	@Test
	public void testZhiDyhl(){
		jusgzd.setYaohlh("M90010019");
		jusgzd.setDingdh("11P0101");
		jusgzd.setUsercenter("UW");
		jusgzd.setJusgzdh("Bb000201");
		yaohl.setYaohlh(jusgzd.getYaohlh());
		org.junit.Assert.assertTrue(shouhdcl.checkYhl(yaohl));
		shouhdcl.zhiDyhl(jusgzd);
		org.junit.Assert.assertFalse(shouhdcl.checkYhl(yaohl));
	}
	
	/**
	 * 测试批量指定要货令
	 */
	@Test
	public void testPiLzdyhl(){
		jusgzd.setUsercenter("UW");
		jusgzd.setJusgzdh("Bb000186");
		jusgzd.setLingjbh("9676410080");
		jusgzds.add(jusgzd);
		org.junit.Assert.assertEquals("批量指定要货令成功", shouhdcl.piLzdyhl(jusgzds));
		//org.junit.Assert.assertEquals("已经指定0个要货令,还有1个要货令指定不成功,因为表达状态要货令不足,请为剩下的创建临时订单", shouhdcl.piLzdyhl(jusgzds));
	}
}

package com.athena.xqjs.module.aixdd;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Aixddppjg;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.kdorder.service.AixddService;
import com.toft.core3.container.annotation.Inject;

/**
 * 
 * @author dsimedd001
 *
 */
@TestData(locations = {"classpath:testdata/aixdd/aixddData.xls"})
public class AixddTest extends AbstractCompomentTests {

	@Inject
	private AixddService aixdd;
	private Dingdmx dingdmx;
	private Map<String,String> params;
	
	@Rule
    public   ExternalResource resource= new ExternalResource(){
   	@Override
   	protected void before() throws Throwable{
   		dingdmx = new Dingdmx();
   		//数据准备
   		params = new HashMap<String ,String>();
   	}
	};
	
	
	/**
	 * 测试匹配装箱规则
	 */
	@Test
	public void testpiPzxgz(){
		dingdmx.setLingjbh("9666800080");
		dingdmx.setUsercenter("UW");
		dingdmx.setGongysdm("M90010010");
		dingdmx.setDingdh("101P6203");
		aixdd.piPzxgz(dingdmx);
		org.junit.Assert.assertNotNull(dingdmx);
	}
	
	/**
	 * 测试查询爱信订单匹配规则
	 */
	@Test
	public void testqueryAixddppgz(){
		params.put("lingjbh", "9666800080");
		params.put("usercenter", "UW");
		params.put("dingdh", "101P6203");
		org.junit.Assert.assertNotNull(aixdd.queryAixddppgzjg(new Aixddppjg(), params));
	}
	
	/**
	 * 测试查询订单零件
	 */
	@Test
	public void testqueryDingdlj(){
		Map<String,String> params = new HashMap<String, String>();
		params.put("lingjbh", "9666800080");
		params.put("usercenter", "UW");
		params.put("dingdh", "101P6203");
		org.junit.Assert.assertNotNull(aixdd.queryDingdlj(new Dingdlj(), params));
	}
	/**
	 * 测试保存爱信订单匹配结果
	 */
	@Test
	public void testsaveAixddppgzjg(){
		Aixddppjg aixddppjg = new Aixddppjg();
		aixddppjg.setDinghsl(BigDecimal.ZERO);
		aixddppjg.setFayrq("2012-02-20");
		org.junit.Assert.assertNotNull(aixdd.saveAixddppgzjg(aixddppjg));
	}
	/**
	 * 测试更新爱信订单匹配结果
	 */
	@Test
	public void testupdateAixddppgzjg(){
		Aixddppjg aixddppjg = new Aixddppjg();
		aixddppjg.setId("4028829835ec99080135ec9cb38d000e");
		aixddppjg.setDinghsl(BigDecimal.ZERO);
		aixddppjg.setFayrq("2012-02-20");
		org.junit.Assert.assertNotNull(aixdd.updateAixddppgzjg(aixddppjg));
	}
}

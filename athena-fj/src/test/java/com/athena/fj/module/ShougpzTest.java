package com.athena.fj.module;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.YaoCJhMx;
import com.athena.fj.module.service.ShougpzService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testdata/fj/shougpz.xls"})
public class ShougpzTest extends AbstractCompomentTests {
	@Inject
	private ShougpzService shougpzService; 
	private Map<String, String> params; 
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {   
        	params = new HashMap<String, String>();
        	params.put("UC", "UW");
        	params.put("lxzbh", "LX1");
        	params.put("LXZBH", "LX1");
        	params.put("PZBH","CL0001" ) ;
        	params.put("CKBH","C01" ) ;
        	
        };   
    };  
    
    /**
     * 根据运输路线查询配载策略  2012-03-30 hzg
     */ 
    @Test
    public void testSelectYahhlOfkh(){
    	List<Map<String,String>> list = shougpzService.selectPeizcl(params);
    	assertEquals("CL0001",list.get(0).get("CELBH"));
    	assertEquals("大车策略",list.get(0).get("CELMC"));
    	assertEquals("DC",list.get(0).get("CHEXBH"));
    }
   
    /**************by 王冲  2012-03-27**********/ 
    
     /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:24:05
     * @description  测试归集客户 供应商
     */ 
    @Test
   @TestData(locations = {"classpath:testdata/fj/yaocjh.xls"})
    public void testSelectGYSKEH(){
    	params.put("LXZBH", "LX2");
    	List<Map<String,String>> list = this.shougpzService.selectGYSKEH(params) ;
    	assertEquals("WUHAN",list.get(0).get("KEH"));
    	assertEquals("CYS002",list.get(0).get("GONGYSDM"));
    	
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:28:46
     * @description  归集仓库
     */
    @Test
    public void testSelectCK(){
    	List<Map<String,String>> list = this.shougpzService.selectCK(params) ;
    	assertEquals("XYG",list.get(0).get("CANGKBH"));
    	
    	
    }
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:31:48
     * @description 归集测略  
     */
    @Test
    public void testSelectCelZ(){
    	params.put("KEH","WUHAN" ) ;
    	params.put("GYSDM", "CYS001") ;
    	List<Map<String,String>> list = this.shougpzService.selectCelZ(params) ;
    	assertEquals("大车",list.get(0).get("CHEXMC"));
    	assertEquals("CL0001",list.get(0).get("CELBH"));
    	assertEquals("A01",list.get(0).get("BAOZZBH"));
    	assertEquals("3",list.get(0).get("BAOZBSJS"));
    	assertEquals("4",list.get(0).get("YOUXJ"));
    	assertEquals("3",list.get(0).get("BAOZSL"));
    	assertEquals("DC",list.get(0).get("CHEXBH"));
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:36:10
     * @description  测试归集要货令
     */
    @Test
    public void testSelectYHL(){
    	params.put("KEH","WUHAN" ) ;
    	params.put("GYSDM", "CYS001") ;
    	List<HashMap<String,String>> list = this.shougpzService.selectYHL(params) ;
    	assertEquals("20003",list.get(0).get("YAOHLH"));
    	assertEquals("A01",list.get(0).get("BAOZZBH"));
    	assertEquals("2501",list.get(0).get("BAOZXH"));
    	assertEquals("C01",list.get(0).get("NEIBGYS_CANGKBH"));
    	assertEquals("8",list.get(0).get("YAOHSL"));
    	assertEquals("95001",list.get(0).get("LINGJBH"));
    	
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:45:57
     * @description  测试推荐配载
     */
    @Test
    @SuppressWarnings("unused")
    public void testTuiJYaoCJHmx(){
    	params.put("LXZBH","LX2" ) ;
		List<YaoCJhMx> yaoCMx =this.shougpzService.tuiJYaoCJHmx(params) ;
//		YaocjhTest.prl(yaoCMx) ;
    	assert(true);
    	
    }
    
    
    /**
     * @author 王冲
     * @email jonw@sina.com
     * @date 2012-3-27
     * @time 下午05:45:57
     * @description  测试推荐配载 混装
     */
    @Test
    @SuppressWarnings("unused")
    @TestData(locations = {"classpath:testdata/fj/yaocjh_tjpz_hz.xls"})
    public void testTuiJYaoCJHmxHZ(){
    	params.put("LXZBH","LX2" ) ;
		List<YaoCJhMx> yaoCMx =this.shougpzService.tuiJYaoCJHmx(params) ;
//		YaocjhTest.prl(yaoCMx) ;
    	assert(true);
    	
    }
}

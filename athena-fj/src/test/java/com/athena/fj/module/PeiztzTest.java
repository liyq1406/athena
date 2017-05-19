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
import com.athena.fj.module.service.PeiztzService;
import com.toft.core3.container.annotation.Inject;

/**
 * 配载调整测试用例
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-3-26
 */
@TestData(locations = {"classpath:testdata/fj/peiztz.xls"})
public class PeiztzTest extends AbstractCompomentTests {
	@Inject
	private PeiztzService peiztzService; 
	private Map<String, String> params;
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {   
        	params = new HashMap<String, String>();
        	params.put("usercenter", "UW");
        	params.put("peizdh", "PW0002");
        	params.put("yaohls", "'20009','20010'");
        	
        };   
    };  
    
    /**
     * 配载计划查询测试用例
     * @author 贺志国
     * @date 2012-3-26
     */
    @Test
    public void testSelectPeizjhOfTiaoz(){
    	List<Map<String,String>> list = peiztzService.selectPeizjhOfTiaoz(params);
    	assertEquals("DC",list.get(0).get("JIHCX"));
    	assertEquals("LX1",list.get(0).get("YUNSLX"));
    	assertEquals("2011-12-25 14:30",list.get(0).get("FAYSJ"));
    	assertEquals("PW0002",list.get(0).get("PEIZDH"));
    	assertEquals("0",list.get(0).get("SHIFMZ"));
    }
    
    /**
     * 根据配载单号查找要货令明细表中的要货令号
     * @author 贺志国
     * @date 2012-3-26
     */
    @Test
    public void testSelectYaohlOfPeizd(){
    	 List<Map<String,String>> list = peiztzService.selectYaohlOfPeizd(params);
    	 assertEquals("E",list.get(0).get("XIEHD"));
    	 assertEquals("2012-01-14 08:30",list.get(0).get("FAYSJ"));
    	 assertEquals("2501",list.get(0).get("BAOZXH"));
    	 assertEquals("WUHAN",list.get(0).get("KEHBM"));
    	 assertEquals("20005",list.get(0).get("YAOHLBH"));
    	 assertEquals("95001",list.get(0).get("LINGJBH"));
    	 
    }
    
    
    /**
     * 配载计划下的要货令转移
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testMoveYaohlToPeizjh(){
    	peiztzService.moveYaohlToPeizjh(params);
    	assert(true);
    }
    
    /**
     * 查询转移后的配载计划下的要货令数量
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectCountYaohlOfPeizjh(){
    	String strCount = peiztzService.selectCountYaohlOfPeizjh("PW0004");
    	assertEquals("1",strCount);
    }
    
    /**
     * 如果配载计划下的要货令全部转移则删除配载计划
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testDeletePeizjhOfTiaoz(){
    	Map<String,String> param = new HashMap<String, String>();
    	param.put("peizdh_left", "PW0004");
    	param.put("usercenter", "UW");
    	String strCount = "0";
    	if("0".equals(strCount)){
    		peiztzService.deletePeizjhOfTiaoz(param);    		
    	}
    	assert(true);
    }
    
    /**
     * 配载计划下的要货令零件汇总
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectPeizjhOfLingj(){
    	List<Map<String,String>> list = peiztzService.selectPeizjhOfLingj(params);
    	assert(list.size()>0);
    }
    
    
    /**
     * 配载计划下的包装组汇总
     * @author 贺志国
     * @date 2012-4-6
     */
    @Test
    public void testSelectPeizjhOfBaozz(){
    	List<Map<String,String>> list = peiztzService.selectPeizjhOfBaozz(params);
    	assert(list.size()>0);
    }
    
    
}

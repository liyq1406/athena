package com.athena.fj.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.entity.Chelsb;
import com.athena.fj.module.service.ChelsbService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


import com.athena.db.ConstantDbCode;

/**
 * 车辆资源申报测试
 * @author 贺志国
 * @date 2011-12-19
 *
 */
@TestData(locations = {"classpath:testdata/fj/chelsb.xls"})
public class ChelsbTest extends AbstractCompomentTests {

	@Inject
	private ChelsbService chelsbService;
	@Inject
	private AbstractIBatisDao baseDao;
	
	private Map<String,String> params;
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
    		params = new HashMap<String,String>();
    		params.put("usercenter", "UW");
    		params.put("gcbh", "yun01");
    		params.put("shebsj", "2011-12-19");
    		params.put("chex", "DC");
    		
        };   
    }; 
	
    /**
     * 查询车辆资源测试
     */
    @Test
	public void testSelect(){
		Chelsb pageChelsb = new Chelsb();
		Map<String,Object> map = chelsbService.select(pageChelsb, params);
		String usercenter = (String) ((Chelsb)(((ArrayList)map.get("rows")).get(0))).getUsercenter();
		assertEquals(usercenter,"UW");
	}
    
    /**
     * 查询车型测试
     */
    @Test
    public void testSelectChex(){
    	List<Map<String,String>> chexList =  chelsbService.selectChex();
    	assertEquals("DC",chexList.get(0).get("CHEXBH"));
    	assertEquals("大车",chexList.get(0).get("CHEXMC"));
    	assertEquals("XC",chexList.get(1).get("CHEXBH"));
    	assertEquals("小车",chexList.get(1).get("CHEXMC"));

    }
    
    /**
     * 查询运输商测试
     */
    @Test
    public void testSelectYunss(){
    	List<Map<String,String>> yunssList =  chelsbService.selectYunss("UW");
    	assertEquals("CYS001",yunssList.get(0).get("GCBH"));
    	assertEquals("汉阳物流中心001",yunssList.get(0).get("GONGSMC"));
    	assertEquals("CYS002",yunssList.get(1).get("GCBH"));
    	assertEquals("汉阳物流中心002",yunssList.get(1).get("GONGSMC"));

    }
    
    /**
     * 批量保存增删操作
     */
    @Test
    public void testSave(){
    	List<Chelsb> insertList = new ArrayList<Chelsb>();
    	List<Chelsb> deleteList = new ArrayList<Chelsb>();
    	chelsbService.save(insertList, deleteList, "root","UW");
    	assertTrue(true);
    }
    
    
    /**
     * 测试批量增加
     */
    @Test
    public void testBatchInsert(){
    	List<Chelsb> list = new ArrayList<Chelsb>();
    	Chelsb bean = new Chelsb();
    	Chelsb bean1 = new Chelsb();
		bean.setUsercenter("UW");
		bean.setYunssbm("yun01");
		bean.setShenbsj("2011-12-19");
		bean.setShul(new BigDecimal("5"));
		bean.setChex("DC");
		
		bean1.setUsercenter("UW");
		bean1.setYunssbm("yun02");
		bean1.setShenbsj("2011-12-20");
		bean1.setShul(new BigDecimal("8"));
		bean1.setChex("XC");
        list.add(bean);
        list.add(bean1);
    	chelsbService.batchInsert(list,"123","UW");
    	
    	
    	List<Map<String,String>> chelsbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("chelsb-test.queryChelsb",bean);
    	assertEquals("UW",chelsbList.get(0).get("USERCENTER"));
    	assertEquals("yun01",chelsbList.get(0).get("YUNSSBM"));
    	assertEquals("DC",chelsbList.get(0).get("CHEX"));
    	
    }
    
    
    
    /**
     * 测试删除
     */
    @Test
	public void testBatchDelete(){
		Chelsb bean = new Chelsb();
		ArrayList<Chelsb> list = new ArrayList<Chelsb>();
		bean.setUsercenter("UW");
		bean.setYunssbm("yun01");
		bean.setShenbsj("2011-12-19");
		bean.setChex("DC");
		list.add(bean);
		chelsbService.batchDelete(list,"123");
		
		List<Map<String,String>> chelsbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("chelsb-test.queryChelsbdelete", bean);
		assertEquals(0,chelsbList.size());
		
	}
}

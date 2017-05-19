package com.athena.xqjs.module.kanbyhl;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Shengcx;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.kanbyhl.service.KanbxhgmService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 导入最大要货量测试
 * @author Nsy
 *
 */
@TestData(locations = {"classpath:testData/xqjs/kanbyhl.xls"})
public class KanbxhgmTest extends AbstractCompomentTests {

	    @Inject
	    private KanbxhgmService kanbxhgmService;
	    
	    @Inject
		private AbstractIBatisDao baseDao;

		private Kanbxhgm   kanbxhgm;
		
		private Map<String, String>  map;
		
	    @Rule  
	    public ExternalResource resource= new ExternalResource() {  
	        @Override  
	        protected void before() throws Throwable {  
	        	kanbxhgm = new Kanbxhgm();
	    		//Map数据准备
	    	    map = new HashMap<String, String>();
	        };   
	    }; 
	    
	    /**
	     * 规模变化测试
	     * 小于
	     */
         @Test
         public  void   selectGmbhltTest(){
        	   map.put("gmbh", "02");
        	   map.put("bh", "50");
        	   map.put("usercenter", "UW");
        	   map.put("gonghms", "R1");
        	   Map<String,Object> mapExp = kanbxhgmService.select(kanbxhgm, map);
        	   List<?> ls = (List<?>) mapExp.get("rows");
        	   Kanbxhgm kb = (Kanbxhgm) ls.get(0);
        	   Assert.assertEquals("W5200019", kb.getXunhbm());
         }
	    
         /**
 	     * 规模变化测试
 	     * 等于
 	     */
          @Test
          public  void   selectGmbhqtTest(){
         	   map.put("gmbh", "01");
         	   map.put("bh", "50");
	       	   map.put("usercenter", "UW");
	       	   map.put("gonghms", "R1");
	       	   Map<String,Object> mapExp = kanbxhgmService.select(kanbxhgm, map);
	       	   List<?> ls = (List<?>) mapExp.get("rows");
	       	   Kanbxhgm kb = (Kanbxhgm) ls.get(0);
	       	   Assert.assertEquals("W2100016", kb.getXunhbm());
          }
	    
          /**
   	        * 规模变化测试
   	        * 大于
   	        */
            @Test
            public  void   selectGmbhgtTest(){
           	   map.put("gmbh", "03");
           	   map.put("bh", "50");
	      	   map.put("usercenter", "UW");
	      	   map.put("gonghms", "R1");
	      	   Map<String,Object> mapExp = kanbxhgmService.select(kanbxhgm, map);
	      	   List<Kanbxhgm> ls = (List<Kanbxhgm>) mapExp.get("rows");
	      	   Set<String> set = new TreeSet<String>();
	    	   for(int i=0;i<ls.size();i++){
	    		  set.add(ls.get(i).getXunhbm());
	    	   }
	    	   Object[]obj = set.toArray();
	      	   Assert.assertEquals("W2100019", (String)obj[0]);
            }
            

              
           /**
       	     * 更新看板循环规模计算
       	     * 最大要货量
       	     * 
       	     */
            @Test
            public  void   updateZdyhlTest(){
               kanbxhgm.setZuidyhl(new BigDecimal(100));
               kanbxhgm.setWeihr("aaaaa");
               kanbxhgm.setWeihsj(CommonFun.getJavaTime());
               kanbxhgm.setXunhbm("WCK00008");
               kanbxhgm.setLingjbh("lj001");
           	   kanbxhgmService.updateZuidyhl(kanbxhgm);
           	   Kanbxhgm kanbxhgm1 = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm",kanbxhgm);
           	 System.out.println("==============="+kanbxhgm1.getShengxzt()+"==========="+kanbxhgm1.getWeihr());
           	   Assert.assertEquals(new BigDecimal(100), kanbxhgm1.getZuidyhl());
            } 
            
            /**
       	     * 查询产线
       	     * 
       	     */
            @Test
            public  void   queryChanxTest(){
               map.put("usercenter", "UW");
               List<Shengcx> ls =  (List<Shengcx>) kanbxhgmService.selectChanx(map);
               Set<String> set = new TreeSet<String>();
	    	   for(int i=0;i<ls.size();i++){
	    		  set.add(ls.get(i).getShengcxbh());
	    	   }
	    	   Object[]obj = set.toArray();
	    	   Assert.assertEquals("scx01", (String)obj[0]);
	    	   Assert.assertEquals("scx02", (String)obj[1]);
	    	   Assert.assertEquals("scx03", (String)obj[2]);
            } 
              
            /**
       	     * 批量修改看板循环规模中的数据
       	     * 测试 - false
       	     * 
       	     */
            @Test
            public  void   updateMoreTest(){
            	List<Kanbxhgm> ls = new ArrayList<Kanbxhgm>();
            	  kanbxhgm.setZuidyhl(new BigDecimal(100));
                  kanbxhgm.setWeihr("aaaaa");
                  kanbxhgm.setWeihsj(CommonFun.getJavaTime());
                  kanbxhgm.setXunhbm("WCK00008");
                  kanbxhgm.setLingjbh("lj001");
            	  ls.add(kanbxhgm);
            	  String flag = kanbxhgmService.doUpdate(ls);
            	  Kanbxhgm kanbxhgm1 = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm",kanbxhgm);
            	  System.out.println("==============="+flag+"==============="+kanbxhgm1.getShengxzt());
            	  Assert.assertEquals("false", flag);
            	  
          
              
            } 
           
            /**
       	     * 批量修改看板循环规模中的数据
       	     * 测试 - true
       	     * 
       	     */
            @Test
            public  void   updateMore1Test(){
            	List<Kanbxhgm> ls = new ArrayList<Kanbxhgm>();
            	  kanbxhgm.setZuidyhl(new BigDecimal(100));
                  kanbxhgm.setWeihr("AAA");
                  kanbxhgm.setWeihsj("2011-02-10 21:57:27:027");
                  kanbxhgm.setXunhbm("LCK00009");
                  kanbxhgm.setLingjbh("lj002");
                  kanbxhgm.setUsercenter("UL");
                  kanbxhgm.setShengxzt("1");
            	  ls.add(kanbxhgm);
            	  String flag = kanbxhgmService.doUpdate(ls);
            	  Kanbxhgm kanbxhgm1 = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm",kanbxhgm);
            	  Assert.assertEquals(kanbxhgm.getShengxzt(), kanbxhgm1.getShengxzt());
            	  Assert.assertEquals("true", flag);
              
            } 
              
              
              
              
              
              
}

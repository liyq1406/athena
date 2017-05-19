package com.athena.xqjs.module.kanbyhl;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jxl.read.biff.BiffException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.kanbyhl.service.DaorzdyhlService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 导入最大要货量测试
 * @author Nsy
 *
 */
@TestData(locations = {"classpath:testData/xqjs/kanbyhl.xls"})
public class ImportMaxAmountTest extends AbstractCompomentTests {

	    @Inject
	    private DaorzdyhlService daorzdyhlService;
	    
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
	     * 查询看板规模计算所有
	     */
	    @Test
	    public  void   selAllTest(){
	    	  @SuppressWarnings("unchecked")
			  List<Kanbxhgm> ls = (List<Kanbxhgm>) daorzdyhlService.queryKanbxhgm();
	    	  Set<String> set = new TreeSet<String>();
	    	  for(int i=0;i<ls.size();i++){
	    		  set.add(ls.get(i).getXunhbm());
	    	  }
	    	  Object[]obj = set.toArray();
	    	  System.out.println(ls.size());
	    	  org.junit.Assert.assertEquals("12345678",(String)obj[0]);
	    }
	   
	  @Test
	  public  void   readMuluTest(){
		  
		  String  filePath = "testData/xqjs/kanbdrzdyhl.xls";
		  String  path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		  String  url = this.getClass().getClassLoader().getResource(path).getFile();
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				System.out.println(e1.toString());
			}
		  //String  t = new String(url.getBytes(Charset.forName("UTF-8")));
		  File  file = new File(url);
		  System.out.println(file.getPath());
		   try {
			List<?> ls= daorzdyhlService.readMulu(file.getPath());
			System.out.println(ls.size());
			org.junit.Assert.assertEquals(8, ls.size());
		} catch (BiffException e) {
			System.out.println("BiffException");
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (ParseException e) {
			System.out.println("ParseException");
		}
	  }
	  
	  @Test
	  public  void   updateKbTest(){
		  String  filePath = "testData/xqjs/kanbdrzdyhl.xls";
		  String  path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		  String  url = this.getClass().getClassLoader().getResource(path).getFile();
		  try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				System.out.println(e1.toString());
			}
		  File  file = new File(url);
		  try {
			String flag = daorzdyhlService.updateKanb(file.getPath());
			System.out.println("ss"+flag);
			kanbxhgm.setXunhbm("w1234567");
			kanbxhgm.setLingjbh("lj001");
			kanbxhgm = (Kanbxhgm) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhlTest.queryKanbxhgm",kanbxhgm);
			org.junit.Assert.assertEquals(new BigDecimal(2000), kanbxhgm.getZuidyhl());
		} catch (BiffException e) {
			System.out.println("BiffException");
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (ParseException e) {
			System.out.println("ParseException");
		}
		  
		  
		  
	  }
	  
	  
	  
	 /**
	 * 导入最大要货量更新看板循环规模
	 * fail 测试
	 * 
	 */
	@Test
	  public  void   updateKb2Test(){
		  String  filePath = "testData/xqjs/kanbdrzdyhl1.xls";
		  String  path = new String(filePath.getBytes(Charset.forName("UTF-8")));
		  String  url = this.getClass().getClassLoader().getResource(path).getFile();
		  try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				System.out.println(e1.toString());
			}
		  File  file = new File(url);
		  try {
			daorzdyhlService.updateKanb(file.getPath());
		} catch (BiffException e) {
			System.out.println("BiffException");
			org.junit.Assert.fail("测试异常失败！");
		} catch (IOException e) {
			System.out.println("IOException");
			org.junit.Assert.fail("测试异常失败！");
		} catch (ParseException e) {
			System.out.println("ParseException");
			org.junit.Assert.fail("测试异常失败！");
		}catch(Exception  e){
			System.out.println("Exception");
		}
	  }
	
}

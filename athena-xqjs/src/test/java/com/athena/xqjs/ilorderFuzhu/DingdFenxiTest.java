package com.athena.xqjs.ilorderFuzhu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.ilorder.service.DingdFenxiService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testData/ilOrder/dingdFenxi.xls"})
public class DingdFenxiTest extends AbstractCompomentTests{
	@Inject
	private DingdFenxiService dingdFenxiService ;
	
	public void initDb() {
		
	}
	
	@Test
	public void test() {
		//选中的需求版次
		String xuqbc = "A001";
		//选中的比较的订单号1
		String dingdh1 = "101P6200";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "A001";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(4, resultDingdlj.size());
	}
	
	public List<Dingdlj> common(String xuqbc,String dingdh1,String dingdh2,String jizhunValue) {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		
		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc(xuqbc);
		Dingd dingd1 = new Dingd();
		dingd1.setDingdh(dingdh1);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdh(dingdh2);
		
		Map<String,Object> resultDingdlj = new HashMap<String, Object>();
		
		//如果需求版次不为空，就一定是需求和订单比较
		if(xuqbc != null) {
			/*if(xuqbc.equals(jizhunValue)) {
				resultDingdlj = dingdFenxiService.dingdBijiaoByMaoxq(new Dingd(), maoxq, dingd1, 2, loginUser);
			} else {
				resultDingdlj = dingdFenxiService.dingdBijiaoByMaoxq(new Dingd(),maoxq, dingd1, 1, loginUser);
			}*/
		}
		else {
			/*try {
				if(dingdh1.equals(jizhunValue)) {
					resultDingdlj = dingdFenxiService.dingdBijiaoByDingd(new Dingd(),dingd1, dingd2, loginUser);
				} else {
					resultDingdlj = dingdFenxiService.dingdBijiaoByDingd(new Dingd(),dingd2, dingd1, loginUser);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
		}
		return (List<Dingdlj>)resultDingdlj.get("rows");
	}
	
	@Test
	public void test1() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = "A001";
		//选中的比较的订单号1
		String dingdh1 = "101P6200";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6200";
		
		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc(xuqbc);
		Dingd dingd1 = new Dingd();
		dingd1.setDingdh(dingdh1);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdh(dingdh2);
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(4, resultDingdlj.size());
	}
	
	@Test
	public void test3() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = "A002";
		//选中的比较的订单号1
		String dingdh1 = "101P6201";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "A002";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(4, resultDingdlj.size());
	}
	
	@Test
	public void test4() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = "A002";
		//选中的比较的订单号1
		String dingdh1 = "101P6201";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6201";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(4, resultDingdlj.size());
	}
	
	//-----------上面为订单和毛需求比较。下面为订单和订单比较-----------------
	@Test
	public void test7() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6200";
		//选中的比较的订单号2
		String dingdh2 = "101P6203";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6200";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test8() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6200";
		//选中的比较的订单号2
		String dingdh2 = "101P6203";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6203";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test9() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6201";
		//选中的比较的订单号2
		String dingdh2 = "101P6204";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6201";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test10() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6201";
		//选中的比较的订单号2
		String dingdh2 = "101P6204";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6204";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test11() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6202";
		//选中的比较的订单号2
		String dingdh2 = "101P6205";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6202";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test12() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6202";
		//选中的比较的订单号2
		String dingdh2 = "101P6205";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6205";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
}

package com.athena.xqjs.kdorderFuzhu;

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
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.module.ilorder.service.DingdFenxiService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = {"classpath:testData/kdOrder/dingdFenxi.xls"})
public class DingdFenxiTest extends AbstractCompomentTests{
	@Inject
	private DingdFenxiService dingdFenxiService ;
	
	public void initDb() {
		
	}
	
	@Test
	public void test() throws Exception {
		//选中的需求版次
		String pplbc = "PPL201204161730";
		//选中的比较的订单号1
		String dingdh1 = "101P6206";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6206";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(pplbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(3, resultDingdlj.size());
	}
	
	public List<Dingdlj> common(String pplbc,String dingdh1,String dingdh2,String jizhunValue) throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		
		Niandyg niandyg = new Niandyg();
		niandyg.setPplbc(pplbc);
		Dingd dingd1 = new Dingd();
		dingd1.setDingdh(dingdh1);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdh(dingdh2);
		
		Map<String,Object> resultDingdlj = new HashMap<String, Object>();
		
		//如果需求版次不为空，就一定是需求和订单比较
		if(pplbc != null) {
			resultDingdlj = dingdFenxiService.dingdBijiaoByNiandyg(new Dingd(), niandyg, dingd1, 2, loginUser);
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
	public void test1() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String pplbc = "PPL201204161730";
		//选中的比较的订单号1
		String dingdh1 = "101P6209";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6209";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(pplbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(3, resultDingdlj.size());
	}
	
	@Test
	public void test2() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String pplbc = "PPL201204161730";
		//选中的比较的订单号1
		String dingdh1 = "101P6210";
		//选中的比较的订单号2
		String dingdh2 = "";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6210";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(pplbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(3, resultDingdlj.size());
	}
	//-----------上面为订单和毛需求比较。下面为订单和订单比较-----------------
	@Test
	public void test7() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6206";
		//选中的比较的订单号2
		String dingdh2 = "101P6209";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6206";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test8() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6206";
		//选中的比较的订单号2
		String dingdh2 = "101P6209";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6209";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test9() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6210";
		//选中的比较的订单号2
		String dingdh2 = "101P6209";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6209";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
	@Test
	public void test10() throws Exception {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");
		//选中的需求版次
		String xuqbc = null;
		//选中的比较的订单号1
		String dingdh1 = "101P6210";
		//选中的比较的订单号2
		String dingdh2 = "101P6209";
		//基准的值(版次或者订单号)
		String jizhunValue = "101P6210";
		
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		
		resultDingdlj = common(xuqbc, dingdh1, dingdh2, jizhunValue);
		
		org.junit.Assert.assertEquals(2, resultDingdlj.size());
	}
	
}

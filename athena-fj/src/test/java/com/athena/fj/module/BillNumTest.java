package com.athena.fj.module;

import static org.junit.Assert.assertEquals;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.fj.module.common.BillNumUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-22
 * @time 下午02:22:56
 * @description 
 */
public class BillNumTest extends AbstractCompomentTests {

	@Inject
	private BillNumUtil  baseDao;
	 @Rule         
     public ExpectedException thrown= ExpectedException.none(); 
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午02:24:48
	 * @description  存在要车计划号和配载单号  
	 */
	@Test
	@TestData(locations = {"classpath:testdata/fj/billNum1.xls"})
	public void testYaoCJHH1()
	{
    	
    	assertEquals("PW0002",baseDao.createDJNum("UW", 4, BillNumUtil.BILL_PZDH,"P"));
    	assertEquals("W0002",baseDao.createDJNum("UW", 4, BillNumUtil.BILL_YCJHH,""));
    	
    	
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午02:24:48
	 * @description  不存在要车计划号和配载单号  
	 */
	@Test
	@TestData(locations = {"classpath:testdata/fj/billNum2.xls"})
	public void testYaoCJHH2()
	{
    	assertEquals("PW0001",baseDao.createDJNum("UW", 4, BillNumUtil.BILL_PZDH,"P"));
    	assertEquals("W001",baseDao.createDJNum("UW", 3, BillNumUtil.BILL_YCJHH,""));
    	assertEquals("W00000001",baseDao.createDJNum("UW", 8, BillNumUtil.BILL_ZCDH,null));
    	
	}
	
	/**
	 * 测试用户中心为空
	 */
	@Test
	@TestData(locations = {"classpath:testdata/fj/billNum2.xls"})
	public void voidTestUWIsNull(){
		thrown.expect(ServiceException.class); 
    	thrown.expectMessage("用户中心为空,或者长度不正确!");
    	baseDao.createDJNum(null, 3, 1,null);
	}
	
}

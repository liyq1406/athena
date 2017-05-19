package com.athena.fj.module;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.fj.module.common.CollectionUtil;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


import com.athena.db.ConstantDbCode;

/**
 * 
 * @author 
 *
 */
public class CollectionUtilTest extends AbstractCompomentTests {

	@Inject
	CollectionUtil ColUtil;
	@Inject
	private AbstractIBatisDao baseDao;
	/**
	 * 将List中需要的某一个字段值保存到map中
	 */
	@Test
	public void testConvertListToMap(){
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("peizjhTest.queryChexMap");
		Map<String,String> map = ColUtil.convertListToMap(list, "CHEXBH", "CHEXMC");
		assertEquals("大车",map.get("DC"));
	}
	/**
	 * 将List中的值拼成一个json字符串返回用于下拉列表框
	 */
	@Test
	public void testListToJson(){
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("peizjhTest.queryChexMap");
		String str = ColUtil.listToJson(list, "CHEXBH", "CHEXMC");
		assertEquals("{'DC':'大车','XC':'小车'}",str);
	}
	
	/**
	 * 将字符串型List拼装成SQL中in能接受的字符串，形如'20001','20002'
	 */
	@Test
	public void testListToString(){
		List<String> strList = new ArrayList<String>();
		strList.add("20001");
		strList.add("20002");
		String str = ColUtil.listToString(strList);
		assertEquals("'20001','20002'",str);
		
	}
}

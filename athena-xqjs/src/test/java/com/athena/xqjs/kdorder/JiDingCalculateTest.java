package com.athena.xqjs.kdorder;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.kdorder.Kdmxqhzc;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.kdorder.service.KdOrderService;
import com.athena.xqjs.module.kdorder.service.KdmxqhzcService;
import com.toft.core3.container.annotation.Inject;

public class JiDingCalculateTest extends AbstractCompomentTests{
	@Inject
	private KdOrderService kdorderservice;
	@Inject
	private KdmxqhzcService kdmxqhzcservice;
	@Test
	@TestData(locations={"classpath:testData/kdorder/JiDingCalculateTest.xls"})
	public void testJiDingCalculate() throws Exception{
		//String [] banc = {"112531"};
		//String [] usercenter = {"UW"};
		//this.kdorderservice.kdLineconvertRow(usercenter, "2012-02-13", banc, Const.ZHIZAOLUXIAN_KD_PSA);// 取出需要计算的毛需求并进行行列转换

		//try {
		//	this.kdorderservice.checkKeyNumber();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}// 关键参数校验

		ArrayList<Kdmxqhzc> list = (ArrayList) this.kdmxqhzcservice.selectDingd(Const.PP);// 得到毛需求汇总参考系表数据

		Map<String,Object> zhouqi = new HashMap();// 用于汇总周期要货
		for(Kdmxqhzc kdmxqhzc:list){
			zhouqi = this.kdorderservice.jiDingCalculate(new BigDecimal(100), kdmxqhzc, false, zhouqi);
			assertEquals(zhouqi.get("yingyu"), new BigDecimal(632632));
			assertEquals(zhouqi.get("yaohl"), new BigDecimal(1440000));
		}
		for(Kdmxqhzc kdmxqhzc:list){
			
			zhouqi =this.kdorderservice.yuGaoCalculate(new BigDecimal(100), kdmxqhzc, zhouqi);
			assertEquals(zhouqi.get("yingyu"), new BigDecimal(-807478));
			assertEquals(zhouqi.get("yaohl"), BigDecimal.ZERO);
		}
		
	}

}

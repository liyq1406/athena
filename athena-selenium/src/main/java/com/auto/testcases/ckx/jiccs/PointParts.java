package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;

import java.io.IOException;

import org.junit.Test;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.PointPartsPage;

public class PointParts extends PublicVerify{
	public PointPartsPage pointPartsPage = new PointPartsPage();
	
	public PointParts() throws IOException {
		super();
	}
	
	@Test
	public void editPointParts() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuPointParts();
		pointPartsPage.butt_search.click();
		Thread.sleep(1000);
		
		pointPartsPage.text_mark.click();
		pointPartsPage.opti_mark2.click();
		pointPartsPage.butt_save.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		pointPartsPage.text_conditionMark.click();
		pointPartsPage.opti_conditionMark2.click();
		pointPartsPage.butt_search.click();
		Thread.sleep(1000);
		
		pointPartsPage.text_mark.click();
		pointPartsPage.opti_mark.click();
		pointPartsPage.butt_save.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		//退出系统
		logoutSystem();
	}
}

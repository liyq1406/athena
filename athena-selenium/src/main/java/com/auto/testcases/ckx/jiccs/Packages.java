package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;

import java.io.IOException;

import org.junit.Test;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.PackagesPage;

public class Packages extends PublicVerify{
	public PackagesPage packagesPage = new PackagesPage();
	
	public Packages() throws IOException {
		super();
	}
	
	@Test
	public void editPackages() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuPackages();
		packagesPage.butt_search.click();
		Thread.sleep(1000);
		
		packagesPage.text_mark.click();
		packagesPage.opti_mark2.click();
		packagesPage.butt_save.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		packagesPage.text_conditionMark.click();
		packagesPage.opti_conditionMark2.click();
		packagesPage.butt_search.click();
		Thread.sleep(1000);
		
		packagesPage.text_mark.click();
		packagesPage.opti_mark.click();
		packagesPage.butt_save.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		//退出系统
		logoutSystem();
	}
}

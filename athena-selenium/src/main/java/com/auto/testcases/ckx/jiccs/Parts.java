package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;

import java.io.IOException;

import org.junit.Test;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.PartsPage;

public class Parts extends PublicVerify{
	public PartsPage partsPage = new PartsPage();
	
	public Parts() throws IOException {
		super();
	}
	
	@Test
	public void editSystParts() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuSystParts();
		partsPage.butt_search.click();
		Thread.sleep(1000);
		partsPage.list_parts.select(2);
		partsPage.list_parts.text_usercenter.click();
		//修改
		partsPage.butt_edit.click();
		Thread.sleep(1000);
		//提交修改
		partsPage.butt_submit.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		Thread.sleep(1000);
		partsPage.list_parts.select(2);
		partsPage.list_parts.text_usercenter.click();
		//失效
		partsPage.butt_invalid.click();
		HolmosWindow.dealConfirm(true);
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		//选择失效查询
		partsPage.text_mark.click();
		partsPage.opti_mark2.click();
		partsPage.butt_search.click();
		Thread.sleep(1000);
		//判断失效条数
		assertEquals(partsPage.list_parts.getSize(), 2);
		
		partsPage.list_parts.select(2);
		partsPage.list_parts.text_usercenter.click();
		//生效
		partsPage.butt_valid.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		partsPage.butt_search.click();
		Thread.sleep(1000);
		//验证没有失效条数了
		assertEquals(partsPage.list_parts.getSize(), 0);
		
		//退出系统
		logoutSystem();
	}
}

package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;

import java.io.IOException;

import org.junit.Test;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.SuppliersPage;


public class Suppliers extends PublicVerify{
	public SuppliersPage suppliersPage = new SuppliersPage();
	
	public Suppliers() throws IOException {
		super();
	}
	
	@Test
	public void editSuppliers() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuSuppliers();
		suppliersPage.butt_search.click();
		Thread.sleep(1000);
		suppliersPage.list_parts.select(2);
		suppliersPage.list_parts.text_usercenter.click();
		//修改
		suppliersPage.butt_edit.click();
		Thread.sleep(1000);
		//提交修改
		suppliersPage.butt_submit.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		
		Thread.sleep(1000);
		suppliersPage.list_parts.select(2);
		suppliersPage.list_parts.text_usercenter.click();
		//失效
		suppliersPage.butt_invalid.click();
		HolmosWindow.dealConfirm(true);
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		//选择失效查询
		suppliersPage.text_mark.click();
		suppliersPage.opti_mark2.click();
		suppliersPage.butt_search.click();
		Thread.sleep(1000);
		//判断失效条数
		assertEquals(suppliersPage.list_parts.getSize(), 2);
		
		suppliersPage.list_parts.select(2);
		suppliersPage.list_parts.text_usercenter.click();
		//生效
		suppliersPage.butt_valid.click();
		Thread.sleep(1000);
		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		suppliersPage.butt_search.click();
		Thread.sleep(1000);
		//验证没有失效条数了
		assertEquals(suppliersPage.list_parts.getSize(), 0);
		
		//退出系统
		logoutSystem();
	}
}

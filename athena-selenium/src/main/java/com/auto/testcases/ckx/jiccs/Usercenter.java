package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;
import holmos.webtest.junitextentions.HolmosRunner;
import holmos.webtest.junitextentions.annotations.Parameter;
import holmos.webtest.junitextentions.annotations.Source;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.UsercenterPage;

@RunWith(HolmosRunner.class)
public class Usercenter extends PublicVerify{
	public UsercenterPage usercenterPage = new UsercenterPage();
	
	public Usercenter() throws IOException {
		super();
	}
	
	@Test
	public void deleteUsercenter() {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuUsercenter();
		
		usercenterPage.butt_search.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			if(usercenterPage.list_usercenters.getSize() > 1) {
				usercenterPage.list_usercenters.select(2);
				usercenterPage.list_usercenters.text_usercenterID.click();
				usercenterPage.butt_delete.click();
			}
			else {
				break;
			}
		}
		usercenterPage.butt_save.click();
		HolmosWindow.dealConfirm(true);
		usercenterPage.butt_search.click();
		//删除完没有数据
		assertEquals(usercenterPage.list_usercenters.getSize(), 1);
	}
	
	@Test
    @Source(file="addUsercenter.xls",sourceID=1) //导入xls文件
    @Parameter(sourceID=1,threadPoolSize=1,value=1)
    public void addUsercenter(String usercenterID, String usercenterName)throws InterruptedException
    {   
		usercenterPage.butt_add.click();
		usercenterPage.list_usercenters.select(2);
		usercenterPage.list_usercenters.text_usercenterID.click();
		System.out.print(usercenterID);
		usercenterPage.text_usercenterID.setText(usercenterID);
		usercenterPage.list_usercenters.text_usercenterName.click();
		usercenterPage.text_usercenterName.setText(usercenterID);
		usercenterPage.butt_save.click();
		HolmosWindow.dealConfirm(true);
		
		usercenterPage.butt_search.click();
		//保存完有3条数据
		//assertEquals(usercenterPage.list_usercenters.getSize(), 4);
    }
}

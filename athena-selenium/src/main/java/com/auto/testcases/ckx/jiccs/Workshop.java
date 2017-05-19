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
import com.auto.testcases.page.ckx.jiccs.WorkshopPage;

@RunWith(HolmosRunner.class)
public class Workshop extends PublicVerify{
	public WorkshopPage workshopPage = new WorkshopPage();
	
	public Workshop() throws IOException {
		super();
	}
	
	@Test
	public void deleteWorkshop() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuWorkshop();
		
		workshopPage.butt_search.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			if(workshopPage.list_workshops.getSize() > 1) {
				workshopPage.list_workshops.select(2);
				workshopPage.list_workshops.text_workshopID.click();
				workshopPage.butt_delete.click();
			}
			else {
				break;
			}
		}
		workshopPage.butt_save.click();
		Thread.sleep(1000);
	}
	
	@Test
    @Source(file="addWorkshop.xls",sourceID=1) //导入xls文件
    @Parameter(sourceID=1,threadPoolSize=1,value=1)
    public void addWorkshop(String workshopID, String workshopName, String remark) throws InterruptedException
    {   
		while(true) {
			if(workshopPage.butt_add.isExist()) {
				workshopPage.butt_add.click();
				workshopPage.list_workshops.select(2);
				workshopPage.list_workshops.text_workshopID.click();
				workshopPage.text_workshopID.setText(workshopID);
				workshopPage.list_workshops.text_workshopName.click();
				workshopPage.text_workshopName.setText(workshopName);
				workshopPage.list_workshops.text_remark.click();
				workshopPage.text_remark.setText(remark);
				
				workshopPage.butt_save.click();
				Thread.sleep(1000);
				assertEquals(HolmosWindow.dealAlert(), "保存成功");
				break;
			}
			else {
				Thread.sleep(1000);
			}
		//保存完有3条数据
		//assertEquals(usercenterPage.list_usercenters.getSize(), 4);
		}
    }
	
	@Test
	public void quitWin() {
		//退出系统
		logoutSystem();
	}
}

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
import com.auto.testcases.page.ckx.jiccs.SystParametersPage;

@RunWith(HolmosRunner.class)
public class SystParameters extends PublicVerify{
	public SystParametersPage systParametersPage = new SystParametersPage();
	
	public SystParameters() throws IOException {
		super();
	}
	
	@Test
	public void deleteSystParameters() throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuSystParameters();
		
		systParametersPage.text_dictType.click();
		systParametersPage.opti_dictType.click();
		systParametersPage.butt_search.click();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		systParametersPage.labl_dictType.click();
		systParametersPage.butt_edit.click();
		Thread.sleep(1000);
		while(true) {
			if(systParametersPage.list_dicts.getSize() > 3) {
				//systParametersPage.list_dicts.select(4);
				//systParametersPage.list_dicts.text_dictID.click();
				systParametersPage.labl_dictID3.click();
				systParametersPage.butt_delete.click();
			}
			else {
				break;
			}
		}
		systParametersPage.butt_save.click();
		Thread.sleep(1000);
	}
	
	@Test
    @Source(file="addSystParameters.xls",sourceID=1) //导入xls文件
    @Parameter(sourceID=1,threadPoolSize=1,value=1)
    public void addSystParameters(String dictID, String dictName, String min, String max, String sequence) throws InterruptedException
    {   
		while(true) {
			if(systParametersPage.butt_edit.isExist()) {
				systParametersPage.labl_dictType.click();
				systParametersPage.butt_edit.click();
				Thread.sleep(1000);
				
				systParametersPage.butt_add.click();
				systParametersPage.list_dicts.select(2);
				systParametersPage.list_dicts.text_dictID.click();
				systParametersPage.text_dictID.setText(dictID);
				
				systParametersPage.list_dicts.text_dictName.click();
				systParametersPage.text_dictName.setText(dictName);
				
				systParametersPage.list_dicts.text_qj.click();
				systParametersPage.opti_qj.click();
				
				systParametersPage.list_dicts.text_min.click();
				systParametersPage.text_min.setText(min);
				
				systParametersPage.list_dicts.text_max.click();
				systParametersPage.text_max.setText(max);
				
				systParametersPage.list_dicts.text_sequence.click();
				systParametersPage.text_sequence.setText(sequence);
				
				systParametersPage.butt_save.click();
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

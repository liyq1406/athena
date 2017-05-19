package com.auto.testcases.ckx.jiccs;

import static org.junit.Assert.assertEquals;
import holmos.webtest.basetools.HolmosWindow;
import holmos.webtest.element.Element;
import holmos.webtest.junitextentions.HolmosRunner;
import holmos.webtest.junitextentions.annotations.Parameter;
import holmos.webtest.junitextentions.annotations.Source;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.testcases.page.ckx.jiccs.PartsSuppliersPage;
@RunWith(HolmosRunner.class)
public class PartsSuppliers extends PublicVerify{
	public PartsSuppliersPage partsSuppliersPage = new PartsSuppliersPage();
	
	public PartsSuppliers() throws IOException {
		super();
	}
	
	@Test
	@Source(file="editPartsSuppliers.xls",sourceID=1) //导入xls文件
    @Parameter(sourceID=1,threadPoolSize=1,value=1)
	public void editPartsSuppliers(String ucPackages,String ucCounts,
			String uaPackages,String ucInuaCounts,String uaCounts,String usPackages,
			String usCounts,String ucType,String lineucCounts) throws InterruptedException {
		//管理员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
				Steup.usercenter.get("root"));
		clickMenuZbc();
		clickMenuPartsSuppliers();
		partsSuppliersPage.butt_search.click();
		Thread.sleep(1000);
		
		partsSuppliersPage.labl_ucPackages.click();
		partsSuppliersPage.text_ucPackages.setText(ucPackages);
		partsSuppliersPage.labl_ucCounts.click();
		partsSuppliersPage.text_ucCounts.setText(ucCounts);
		partsSuppliersPage.labl_uaPackages.click();
		partsSuppliersPage.text_uaPackages.setText(uaPackages);
		partsSuppliersPage.labl_ucInuaCounts.click();
		partsSuppliersPage.text_ucInuaCounts.setText(ucInuaCounts);
		partsSuppliersPage.labl_uaCounts.click();
		partsSuppliersPage.text_uaCounts.setText(uaCounts);
		
		partsSuppliersPage.labl_usPackages.click();
		partsSuppliersPage.text_usPackages.setText(usPackages);
		partsSuppliersPage.labl_usCounts.click();
		partsSuppliersPage.text_usCounts.setText(usCounts);
		partsSuppliersPage.labl_ucType.click();
		partsSuppliersPage.text_ucType.setText(ucType);
		partsSuppliersPage.labl_lineucCounts.click();
		partsSuppliersPage.text_lineucCounts.setText(lineucCounts);
		//保存
		partsSuppliersPage.butt_saveAll.focus();
		partsSuppliersPage.butt_saveAll.click();
		Thread.sleep(2000);
		HolmosWindow.dealAlert();
		
		//查询
		partsSuppliersPage.butt_search.click();
		Thread.sleep(1000);
		//验证
		assertEquals(partsSuppliersPage.labl_ucPackages.getText(), ucPackages);
		assertEquals(partsSuppliersPage.labl_ucCounts.getText(), ucCounts);
		assertEquals(partsSuppliersPage.labl_uaPackages.getText(), uaPackages);
		assertEquals(partsSuppliersPage.labl_ucInuaCounts.getText(), ucInuaCounts);
		assertEquals(partsSuppliersPage.labl_uaCounts.getText(), uaCounts);
		
		assertEquals(partsSuppliersPage.labl_usPackages.getText(), usPackages);
		assertEquals(partsSuppliersPage.labl_usCounts.getText(), usCounts);
		assertEquals(partsSuppliersPage.labl_ucType.getText(), ucType);
		assertEquals(partsSuppliersPage.labl_lineucCounts.getText(), lineucCounts);
//		assertEquals(HolmosWindow.dealAlert(), "保存成功");
		//退出系统
		logoutSystem();
	}
}

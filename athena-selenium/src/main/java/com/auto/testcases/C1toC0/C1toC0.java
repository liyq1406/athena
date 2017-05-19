package com.auto.testcases.C1toC0;

import static org.junit.Assert.assertEquals;
import holmos.webtest.Allocator;
import holmos.webtest.basetools.HolmosWindow;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.common.Util;
import com.auto.common.page.CommonPage;
import com.auto.testcases.page.Ilzqdd.DingdjsPage;
import com.auto.testcases.page.chuanjlsyhl.ChuanjlsyhlPage;
import com.auto.testcases.page.daohsb.DaohsbPage;
import com.auto.testcases.page.daohsb.DaohxxllscPage;
import com.auto.testcases.page.daohys.DaohysPage;
import com.auto.testcases.page.daohys.YansdscPage;
import com.auto.testcases.page.kanbsk.KanbskPage;
import com.auto.testcases.page.kanbxh.KanbxhjsPage;
import com.auto.testcases.page.kanbxh.NeibkbxhglPage;
import com.auto.testcases.page.kanbxh.NeibxhgmsgszPage;
import com.auto.testcases.page.kanbxh.WaibkbxhglPage;
import com.auto.testcases.page.kanbxh.WaibxhgmsgszPage;

/**
 * C1-C0测试路线
 * @author lizhi
 * @date 2012-10-13
 */
public class C1toC0 extends PublicVerify {
	//Il订单计算
	public DingdjsPage dingdjsPage = new DingdjsPage();
	
	//公用界面
	public CommonPage commonPage = new CommonPage();
	
	public Properties C1toC0Properties;
	
	public C1toC0() throws IOException {
		super();
		C1toC0Properties = Util.propertiesUrl("C1toC0");
	}
	
	/**
	 *执行接口 
	 */
	
	public void C1toC0_executeInterfaceForAnxddjs() {
		//DDBH拆分
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"DDBHclvfunc");
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"DDBHsppvfunc");
		
		//calculate计算未来几日剔除休息时间
		
		//1.DDBH输出3110：DDBH拆分-CLV-ATH3 
		executeInterface(Steup.url.get("interface_ddbh"), "3110");
		//2.DDBH输出3150：DDBH拆分-SPPV-ATH3
		executeInterface(Steup.url.get("interface_ddbh"), "3150");
		//1.执行层输出2120：库存快照
		executeInterface(Steup.url.get("interface_zxc"), "2120");
		//2.准备层输入1160: 资源快照
		executeInterface(Steup.url.get("interface_zbc"), "1160");
		//3.准备层输入1040：零件
		executeInterface(Steup.url.get("interface_zbc"), "1040");
		//4.准备层输入1070：零件供应商
		executeInterface(Steup.url.get("interface_zbc"), "1070");
		//5.准备层输入1090：零件消耗点
		executeInterface(Steup.url.get("interface_zbc"), "1090");
		//6.准备层输入1050：周期毛需求（IL：周期)
		executeInterface(Steup.url.get("interface_zbc"), "1050");
		
		//取DDBH拆分-SPPV-ATH3过来
		downLoadDDBHAndUploadZBC("ath4oath302.txt", "ath3iath401.txt");
		//准备层输入1150：DDBH拆分结果-SPPV
		executeInterface(Steup.url.get("interface_zbc"), "1150");
		
		//ath3iath401,ath3iath501
		//取DDBH拆分-CLV-ATH3过来
		downLoadZXCAndUploadZBC("ath4oath301.txt", "ath3iath401.txt");
		downLoadZXCAndUploadZBC("ath5oath301.txt", "ath3iath501.txt");
		//准备层输入1730：DDBH拆分结果-CLV
		executeInterface(Steup.url.get("interface_zbc"), "1730");
	}
	
	/**
	 * 订单计算-按需订单
	 */
	
	public void C1toC0_anxdingdjs() {
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
				Steup.usercenter.get("jhl"));
		
		//Il订单计算
		clickMenuZbc();
		clickMenuIlDdjs();
		//选择计算类型
		chooseJislx(C1toC0Properties.getProperty("anxdingdjs_jislx"));
		//生成订单
		dingdjsPage.link_scdd.click();
		String alertMessage = "";
		while(true) {
			if(HolmosWindow.dealAlert()!=null) {
				alertMessage = HolmosWindow.dealAlert();
				break;
			}
			else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//判断
		assertEquals(alertMessage, "计算完成！");
		//退出系统
		logoutSystem();
	}
	
	/**
	 *执行接口 
	 */
	@Test
	public void C1toC0_executeInterface() {
		//1.准备层输出1610:按需订单
		executeInterface(Steup.url.get("interface_zbc"), "1610");
		//2.准备层输出1620:订单明细（既定、预告）
		executeInterface(Steup.url.get("interface_zbc"), "1620");
		//3.准备层输出1230:IL订单明细（既定、预告）
		executeInterface(Steup.url.get("interface_zbc"), "1230");
		
		//从准备层转订单文件
		downLoadZBCAndUploadZXC("ath3oath133.txt", "ath1iath310.txt");
		downLoadZBCAndUploadZXC("ath3oath233.txt", "ath1iath333.txt");
		//1.执行层输入2420:订单
		executeInterface(Steup.url.get("interface_zxc"), "2420");
		
		//从准备层转订单明细文件
		downLoadZBCAndUploadZXC("ath3oath134.txt", "ath1iath311.txt");
		downLoadZBCAndUploadZXC("ath3oath111.txt", "ath1iath332.txt");
		downLoadZBCAndUploadZXC("ath3oath211.txt", "ath1iath334.txt");
		//2.执行层输入2190:订单明细
		executeInterface(Steup.url.get("interface_zxc"), "2190");
		
		//4.输入2430：DDBH拆分-SPPV
		executeInterface(Steup.url.get("interface_zxc"), "2430");
		//5.输入2630：线边库存初始化资源表
		executeInterface(Steup.url.get("interface_zxc"), "2630");
		
		//批量：订单拆分要货令：STODDCFYHL
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"STODDCFYHL");
	}
	
	/**
	 * 349： C0初始化布线
	 */
	public void R1toR1_chushbxForc0() {
		try{
			//物流工艺员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("zxc_wlgyy"), Steup.password,
					Steup.usercenter.get("zxc_wlgyy"));
			//选择仓库
			chooseCkh(C1toC0Properties.getProperty("zxc_ckh"));
			//输入功能号351
			commonPage.text_gongnh.setText("349");
			//回车
			sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			//F1
			sendKeys(Keys.F1);
			Thread.sleep(1000);
			//判断
			assertEquals(commonPage.labl_message.getText(), "请继续操作!");
			//返回
			sendKeys(Keys.F12);
			//退出系统
			logoutZxcSystem();
			
			//批量：C0要货令计算：INSC0yhlmain
			execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"INSC0yhlmain");
			//执行层输出2520：要货令-EFI
			executeInterface(Steup.url.get("interface_zxc"), "2520");
			//执行层输入2040：发货通知
			executeInterface(Steup.url.get("interface_zxc"), "2040");
			//电子到货通知单预处理RCVBatDhtzd_EFI生成待处理状态拒收跟踪单
			execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"RCVBatDhtzd_EFI");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * 点击键盘
	 */
	private void sendKeys(CharSequence keysToSend) {
		Actions action=new Actions((WebDriver)Allocator.getInstance().currentWindow.getDriver().getEngine());
		action.build();
		action.sendKeys(keysToSend).perform();
	}
}

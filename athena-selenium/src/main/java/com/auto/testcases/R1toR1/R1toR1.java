package com.auto.testcases.R1toR1;

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
 * R1-R1测试路线
 * @author lizhi
 * @date 2012-10-13
 */
public class R1toR1 extends PublicVerify {
	//看板循环计算
	public KanbxhjsPage kanbxhjsPage = new KanbxhjsPage();
	//看板循环手工设置(内部)
	public NeibxhgmsgszPage neibxhgmsgszPage = new NeibxhgmsgszPage();
	//看板循环手工设置（外部）
	public WaibxhgmsgszPage waibxhgmsgszPage = new WaibxhgmsgszPage();
	//看板循环管理(内部)
	public NeibkbxhglPage neibkbxhglPage = new NeibkbxhglPage();
	//看板循环管理(外部)
	public WaibkbxhglPage waibkbxhglPage = new WaibkbxhglPage();
	//到货申报
	public DaohsbPage daohsbPage = new DaohsbPage();
	//到货信息录入删除
	public DaohxxllscPage daohxxllscPage = new DaohxxllscPage();
	//到货验收
	public DaohysPage daohysPage = new DaohysPage();
	//验收单删除
	public YansdscPage yansdscPage = new YansdscPage();
	//线边扫卡
	public KanbskPage kanbskPage = new KanbskPage();
	//创建临时要货令
	public ChuanjlsyhlPage chuanjlsyhlPage = new ChuanjlsyhlPage();
	
	//公用界面
	public CommonPage commonPage = new CommonPage();
	
	public Properties R1toR1Properties;
	
	public R1toR1() throws IOException {
		super();
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	/**
	 * R1-R1看板循环计算case
	 * @throws InterruptedException 
	 */
	
	public void R1toR1_kanbxhjsCase()  {
		try{
			//输入1052:周期毛需求(IL：日)执行接口
			//executeInterface(Steup.url.get("interface_zbc"), "1052");
			//输入1051：周期毛需求（IL：周期)执行接口
			executeInterface(Steup.url.get("interface_zbc"), "1051");
			//准备层POA登陆准备层
			loginSystem(Steup.url.get("zbc"), Steup.username.get("zbc_POA"), Steup.password,
					Steup.usercenter.get("zbc_POA"));
			//看板循环规模计算
			clickMenuZbc();
			clickMenuKanxhjs();
			//选择毛需求
			kanbxhjsPage.radio_maoxq.click();
			//确认
			kanbxhjsPage.link_qr.clickAndWaitForLoad();
			//判断
			//WebDriver w = (WebDriver)Allocator.getInstance().currentWindow.getDriver().getEngine();
			//w.switchTo().alert();
			////assertEquals(HolmosWindow.dealAlert(), "确认成功！");
			//选择用户中心
			chooseUsercenter(Steup.usercenter.get("zbc_POA"));
			//点击看板计算
			kanbxhjsPage.butt_kbjs.clickAndWaitForLoad();
			Thread.sleep(10000);
			//判断
			//assertEquals(HolmosWindow.dealAlert(), "计算成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
		}
	}
	
	/**
	 * R1-R1内部循环规模手工设置case
	 */
	public void R1toR1_neibxunhgmsz() {
		//物流工艺员登陆准备层系统
		loginSystem(Steup.url.get("zbc"), Steup.username.get("wlgyy"), Steup.password,
				Steup.usercenter.get("wlgyy"));
		//点击看板循环手工设置(内部)菜单
		clickMenuZbc();
		clickMenuNeibxhgmsgsz();
		//查询
		neibxhgmsgszPage.link_cx.clickAndWaitForLoad();
		//填写循环规模
		String[] xunhgms = R1toR1Properties.getProperty("neibxhgmsgsz_xunhgm").split(";");
		neibxhgmsgszPage.textField1.click();
		neibxhgmsgszPage.text_xunhgm.setText(xunhgms[0]);
		neibxhgmsgszPage.textField2.click();
		neibxhgmsgszPage.text_xunhgm.setText(xunhgms[1]);
		neibxhgmsgszPage.textField3.click();
		neibxhgmsgszPage.text_xunhgm.setText(xunhgms[2]);
		//点击创建
		neibxhgmsgszPage.link_cj.click();
		//判断
		//assertEquals(HolmosWindow.dealAlert(), "创建成功！");
		//退出系统
		logoutSystem();
	}
	
	/**
	 * R1-R1外部循环规模手工设置case
	 */
	public void R1toR1_waibxunhgmsz() {
		//Il工艺员登陆准备层系统
		loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
				Steup.usercenter.get("jhl"));
		//点击看板循环手工设置(内部)菜单
		clickMenuZbc();
		clickMenuWaibxhgmsgsz();
		//查询
		waibxhgmsgszPage.link_cx.clickAndWaitForLoad();
		//填写循环规模
		String[] xunhgms = R1toR1Properties.getProperty("waibxhgmsgsz_xunhgm").split(";");
		waibxhgmsgszPage.textField1.click();
		waibxhgmsgszPage.text_xunhgm.setText(xunhgms[0]);
		waibxhgmsgszPage.textField2.click();
		waibxhgmsgszPage.text_xunhgm.setText(xunhgms[1]);
		waibxhgmsgszPage.textField3.click();
		waibxhgmsgszPage.text_xunhgm.setText(xunhgms[2]);
		//点击创建
		waibxhgmsgszPage.link_cj.click();
		//判断
		//assertEquals(HolmosWindow.dealAlert(), "创建成功！");
		//退出系统
		logoutSystem();
	}
	
	/**
	 *内部看板循环管理 (生效功能)
	 */
	
	public void R1toR1_neibkbxhgl() {
		try{
			//Il工艺员登陆准备层系统
			loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
					Steup.usercenter.get("jhl"));
			//点击看板循环手工设置(内部)菜单
			clickMenuZbc();
			clickMenuNeibkbxhgl();
			//查询
			neibkbxhglPage.link_cx.clickAndWaitForLoad();
			//选择看板循环
			for(int i=2; i<=10; i++) {
				//List list = neibkbxhglPage.kanbxhsTable.getElementsContentByRow(i);
				//neibkbxhglPage.kanbxhsTable.click();
	
				neibkbxhglPage.kanbxhs.select(i);
				String text = neibkbxhglPage.kanbxhs.labl_lingjbh.getText();
				String checks = R1toR1Properties.getProperty("neibkbxhgl_checks");
				//如果判断包含在参数中
				if(checks.contains(text)) {
					//选中此行
					neibkbxhglPage.kanbxhs.labl_lingjbh.click();
				}
			}
			//生效
			neibkbxhglPage.link_sx.click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//判断
			//assertEquals(HolmosWindow.dealAlert(), "修改成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
		}
	}
	
	/**
	 *外部看板循环管理 (生效功能)
	 */
	
	public void R1toR1_waibkbxhgl() {
		try{
			//Il工艺员登陆准备层系统
			loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
					Steup.usercenter.get("jhl"));
			//点击看板循环手工设置(外部)菜单
			clickMenuZbc();
			clickMenuWaibkbxhgl();
			//查询
			waibkbxhglPage.link_cx.clickAndWaitForLoad();
			//选择看板循环
			for(int i=2; i<=10; i++) {
				waibkbxhglPage.kanbxhs.select(i);
				String text = waibkbxhglPage.kanbxhs.labl_lingjbh.getText();
				String checks = R1toR1Properties.getProperty("waibkbxhgl_checks");
				//如果判断包含在参数中
				if(checks.contains(text)) {
					//选中此行
					waibkbxhglPage.kanbxhs.labl_lingjbh.click();
				}
			}
			//生效
			waibkbxhglPage.link_sx.click();
			Thread.sleep(3000);
			//判断
			//assertEquals(HolmosWindow.dealAlert(), "修改成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
		}
	}
	
	/**
	 *执行接口 
	 */
	
	public void R1toR1_executeInterface() {
		//准备层接口
		//输出1100：看板循环规模
		executeInterface(Steup.url.get("interface_zbc"), "1100");
		//输出1280:仓库循环时间
		executeInterface(Steup.url.get("interface_zbc"), "1280");
		
		
		//执行层接口
		//输入2240:仓库循环时间
		downLoadZBCAndUploadZXC("ath3oath118.txt", "ath1iath318.txt");
		executeInterface(Steup.url.get("interface_zxc"), "2240");
		//输入2070:看板循环规模
		downLoadZBCAndUploadZXC("ath3oath131.txt", "ath1iath321.txt");
		executeInterface(Steup.url.get("interface_zxc"), "2070");
		
		//看板要货令计算批量INSBatKanbgm
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"INSBatKanbgm");
		//执行层接口
		//输出2520：要货令（EFI）
		executeInterface(Steup.url.get("interface_zxc"), "2520");
		//输入2040：EFI发货通知
		executeInterface(Steup.url.get("interface_zxc"), "2040");
		
		//电子到货通知单预处理RCVBatDhtzd_EFI
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"RCVBatDhtzd_EFI");
		
		//执行层接口
		//输出2370：UA标签
		executeInterface(Steup.url.get("interface_zxc"), "2370");
		//输出2360：到货通知单
		executeInterface(Steup.url.get("interface_zxc"), "2360");
		//输出2380：外部要货令
		executeInterface(Steup.url.get("interface_zxc"), "2380");
		
		//准备层接口
		//1400：外部要货令
		executeInterface(Steup.url.get("interface_zbc"), "1400");
		//1380：到货通知单
		executeInterface(Steup.url.get("interface_zbc"), "1380");
		//1390：UA标签
		executeInterface(Steup.url.get("interface_zbc"), "1390");
	}
	
	/**
	 * 执行层到货申报
	 * @throws InterruptedException 
	 */
	
	public void R1toR1_daohsb() {
		try{
			//执行层收货员执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
			//输入功能号101
			commonPage.text_gongnh.setText("101");
			HolmosWindow.maxSizeWindow();
			//回车
			sendKeys(Keys.ENTER);
			//输入bl号
			daohsbPage.text_blh.setText(R1toR1Properties.getProperty("daohsb_blh"));
			//回车
			sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			//F5全选
			sendKeys(Keys.F5);
			//F8打印到货单
			sendKeys(Keys.F8);
			Thread.sleep(1000);
			//判断
			//assertEquals(commonPage.labl_message.getText(), "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//返回
			sendKeys(Keys.F12);
			//退出系统
			logoutZxcSystem();
		}
	}
	
	/**
	 * 执行层到货验收
	 * @throws InterruptedException 
	 */
	
	public void R1toR1_daohys() {
		try{
			//执行层收货员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
			//输入功能号103
			commonPage.text_gongnh.setText("103");
			//回车
			sendKeys(Keys.ENTER);
			//输入bl号
			daohysPage.text_blh.setText(R1toR1Properties.getProperty("daohys_blh"));
			//F1查询
			sendKeys(Keys.F1);
			Thread.sleep(1000);
			//选中
			daohysPage.check_blh.click();
			//操作到货单（执行F3）
			sendKeys(Keys.F3);
			Thread.sleep(1000);
			//F3验收
			sendKeys(Keys.F3);
			Thread.sleep(1000);
			//判断
			//assertEquals(commonPage.labl_message.getText(), "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//返回
			sendKeys(Keys.F12);
			//退出系统
			logoutZxcSystem();
		}
	}
	
	/**
	 * 验收单删除143(为了循环的到货验收)
	 */
	
	public void R1toR1_yansdsc() {
		try {
			//执行层收货员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
			//输入功能号143
			commonPage.text_gongnh.setText("143");
			//回车
			sendKeys(Keys.ENTER);
			//输入UT号
			yansdscPage.text_uth.setText(R1toR1Properties.getProperty("yansdsc_uth"));
			//删除理由
			yansdscPage.text_shancyy.setText("测试");
			//F6
			sendKeys(Keys.F6);
			Thread.sleep(1000);
			//判断
			//assertEquals(commonPage.labl_message.getText(), "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//返回
			sendKeys(Keys.F12);
			//退出系统
			logoutZxcSystem();
		}
	}
	
	/**
	 * 到货信息录入删除127(为了循环的到货申报)
	 */
	
	public void R1toR1_daohxxllsc() {
		try {
			//执行层收货员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
			//输入功能号127
			commonPage.text_gongnh.setText("127");
			//回车
			sendKeys(Keys.ENTER);
			//输入UT号
			daohxxllscPage.text_uth.setText(R1toR1Properties.getProperty("daohxxllsc_uth"));
			//回车
			sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			//F2
			sendKeys(Keys.F2);
			Thread.sleep(1000);
			//判断
			//assertEquals(commonPage.labl_message.getText(), "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//返回
			sendKeys(Keys.F12);
			//退出系统
			logoutZxcSystem();
		}
	}
	
	/**
	 * 内部看板循环规模管理(冻结功能)
	 */
	
	public void R1toR1_neikanbxhgmdj() {
		//执行层POA登陆执行层系统
		loginSystem(Steup.url.get("zxc"), Steup.username.get("zxc_POA"), Steup.password,
				Steup.usercenter.get("zxc_POA"));
		//切换到参考系
		clickQiehckx();
		//点击看板循环手工设置(外部)菜单
		clickMenuZxc();
		clickMenuNeibkbxhgl();
		//查询
		waibkbxhglPage.link_cx.clickAndWaitForLoad();
		//选择看板循环
		for(int i=2; i<=10; i++) {
			waibkbxhglPage.kanbxhs.select(i);
			String text = waibkbxhglPage.kanbxhs.labl_lingjbh.getText();
			String checks = R1toR1Properties.getProperty("neibkbxhgldj_checks");
			//如果循环编号包含在参数中
			if(checks.contains(text)) {
				//选中此行
				waibkbxhglPage.kanbxhs.labl_lingjbh.click();
			}
		}
		//冻结
		neibkbxhglPage.link_dj.click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//判断
		//assertEquals(HolmosWindow.dealAlert(), "冻结成功！");
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 外部看板循环规模管理(冻结功能)
	 */
	
	public void R1toR1_waikanbxhgmdj() {
		//执行层POA登陆执行层系统
		loginSystem(Steup.url.get("zxc"), Steup.username.get("zxc_POA"), Steup.password,
				Steup.usercenter.get("zxc_POA"));
		//切换到参考系
		clickQiehckx();
		//点击看板循环手工设置(外部)菜单
		clickMenuZxc();
		clickMenuWaibkbxhgl();
		//查询
		waibkbxhglPage.link_cx.clickAndWaitForLoad();
		//选择看板循环
		for(int i=2; i<=10; i++) {
			waibkbxhglPage.kanbxhs.select(i);
			String text = waibkbxhglPage.kanbxhs.labl_lingjbh.getText();
			String checks = R1toR1Properties.getProperty("waibkbxhgldj_checks");
			//如果循环编号包含在参数中
			if(checks.contains(text)) {
				//选中此行
				waibkbxhglPage.kanbxhs.labl_lingjbh.click();
			}
		}
		//冻结
		neibkbxhglPage.link_dj.click();
		//判断
		//assertEquals(HolmosWindow.dealAlert(), "冻结成功！");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//退出系统
		logoutSystem();
	}
	/**
	 * 执行层线边扫卡
	 */
	public void R1toR1_xianbsk() {
		//线边扫卡员登陆执行层系统
		loginSystem(Steup.url.get("zxc"), Steup.username.get("xianbsky"), Steup.password,
				Steup.usercenter.get("xianbsky"));
		//选择仓库
		chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
		//输入功能号343
		commonPage.text_gongnh.setText("343");
		
		//回车
		sendKeys(Keys.ENTER);
		//输入要货令号
		kanbskPage.text_yaohlh.setText(R1toR1Properties.getProperty("kanbsk_yaohlh"));
		//F3确定
		sendKeys(Keys.F3);
		//返回
		sendKeys(Keys.F12);
		//退出系统
		logoutZxcSystem();
	}
	
	/**
	 * 创建临时要货令
	 */
	
	public void R1toR1_chuanjlsyhl() {
		//物流工艺员登陆执行层系统
		loginSystem(Steup.url.get("zxc"), Steup.username.get("zxc_wlgyy"), Steup.password,
				Steup.usercenter.get("zxc_wlgyy"));
		//选择仓库
		chooseCkh(R1toR1Properties.getProperty("zxc_ckh"));
		//输入功能号351
		commonPage.text_gongnh.setText("351");
		//回车
		sendKeys(Keys.ENTER);
		//选择要货令类型
		chooseYaohllx(R1toR1Properties.getProperty("chuanjlsyhl_yaohllx"));
		//选择供货模式
		chooseGonghms(R1toR1Properties.getProperty("chuanjlsyhl_gonghms"));
		//选择是否发送供应商
		chooseShiffsgys(R1toR1Properties.getProperty("chuanjlsyhl_shiffsgys"));
		//输入看板循环编码
		chuanjlsyhlPage.text_xunhbm.setText(R1toR1Properties.getProperty("chuanjlsyhl_xunhbm"));
		//输入要货数量
		chuanjlsyhlPage.text_yaohsl.setText(R1toR1Properties.getProperty("chuanjlsyhl_yaohsl"));
		//输入零件号
		//chuanjlsyhlPage.text_lingjbh.setText(R1toR1Properties.getProperty("chuanjlsyhl_lingjbh"));
		//输入目的地
		//chuanjlsyhlPage.text_mudd.setText(R1toR1Properties.getProperty("chuanjlsyhl_mudd"));
		//F2增加
		sendKeys(Keys.F2);
		//判断
		//assertEquals(commonPage.labl_message.getText(), "操作成功！");
		//返回
		sendKeys(Keys.F12);
		//退出系统
		logoutZxcSystem();
	}
	
	@Test
	public void R1toR1_main() {
		while(true) {
			R1toR1_kanbxhjsCase();
			R1toR1_neibkbxhgl();
			R1toR1_waibkbxhgl();
			R1toR1_executeInterface();
			R1toR1_daohsb();
			R1toR1_daohys();
			R1toR1_yansdsc();
			R1toR1_daohxxllsc();
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

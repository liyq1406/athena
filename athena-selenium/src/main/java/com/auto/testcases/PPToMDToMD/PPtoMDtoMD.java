package com.auto.testcases.PPToMDToMD;

import static org.junit.Assert.assertEquals;
import holmos.webtest.Allocator;
import holmos.webtest.asserttool.HolmosSimpleCheckTool;
import holmos.webtest.basetools.HolmosWindow;
import holmos.webtest.element.Link;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.common.Util;
import com.auto.common.page.CommonPage;
import com.auto.testcases.page.Ilzqdd.DingdjsPage;
import com.auto.testcases.page.Ilzqdd.DingdmxPage;
import com.auto.testcases.page.Ilzqdd.DingdxgsxPage;
import com.auto.testcases.page.Ilzqdd.JuanlDddyPage;
import com.auto.testcases.page.Ilzqdd.JuanlDingdjsPage;
import com.auto.testcases.page.anxzqdd.AnxcshjsPage;
import com.auto.testcases.page.cangkjyk.CangkjykPage;
import com.auto.testcases.page.daohxxll.DaohxxllPage;
import com.auto.testcases.page.daohys.DaohysPage;
import com.auto.testcases.page.daohys.YansdscPage;
import com.auto.testcases.page.maoxq.MaoxqcxbjPage;
/**
 * PP-MD-MD测试路线
 * @author lizhi
 * @date 2012-10-25
 */
public class PPtoMDtoMD extends PublicVerify {
	//Il订单计算
	public DingdjsPage dingdjsPage = new DingdjsPage();
	//卷料订单计算
	public JuanlDingdjsPage juanlDingdjsPage = new JuanlDingdjsPage();
	//卷料订单定义
	public JuanlDddyPage juanlDddyPage = new JuanlDddyPage();
	//Il订单修改
	public DingdxgsxPage dingdxgsxPage = new DingdxgsxPage();
	//Il订单明细
	public DingdmxPage dingdmxPage = new DingdmxPage();
	//到货信息录入
	public DaohxxllPage daohxxllPage = new DaohxxllPage();
	//到货验收
	public DaohysPage daohysPage = new DaohysPage();
	//验收单删除
	public YansdscPage yansdscPage = new YansdscPage();
	//按需初始化计算
	public AnxcshjsPage anxcshjsPage = new AnxcshjsPage();
	//仓库间移库
	public CangkjykPage cangkjykPage = new CangkjykPage();
	//毛需求查询/比较界面
	public MaoxqcxbjPage maoxqcxbjPage = new MaoxqcxbjPage();
	
	//公用界面
	public CommonPage commonPage = new CommonPage();
	
	public Properties PPtoMDtoMDProperties;
	//保存随即生成的blh
	private String blh = "";
	
	public PPtoMDtoMD() throws IOException {
		super();
		PPtoMDtoMDProperties = Util.propertiesUrl("PPtoMDtoMD");
	}
	
	/**
	 *执行接口 
	 */
	@Test
	public void PPtoMDtoMD_executeInterfaceForIljs() {
		//准备层输入1050：周期毛需求（IL：周期):ath3inup02.txt
		executeInterface(Steup.url.get("interface_zbc"), "1050");
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("zbc_POA"), Steup.password,
				Steup.usercenter.get("zbc_POA"));
		
		//毛需求查询/比较
		clickMenuZbc();
		clickMenuMaoxqbj();
		//选择需求来源
		chooseXuqly2(PPtoMDtoMDProperties.getProperty("maoxqcxbj_xuqly"));
		//选择需求版次
		maoxqcxbjPage.text_xuqbc.click();
		//选择今天
		commonPage.link_today.click();
		//输入序号
		maoxqcxbjPage.text_xuh.setText(PPtoMDtoMDProperties.getProperty("maoxqcxbj_xuh"));
		//查询
		maoxqcxbjPage.link_cx.clickAndWaitForLoad();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//判断
		if(maoxqcxbjPage.maoxqs.isExist()) {
			assertEquals(maoxqcxbjPage.maoxqs.getSize(), 2);//查询出一条，算表头有2条
		}
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 订单计算-IL周期订单
	 * @throws InterruptedException 
	 */
	@Test
	public void PPtoMDtoMD_dingdjs() throws InterruptedException {
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
				Steup.usercenter.get("jhl"));
		
		//Il订单计算
		clickMenuZbc();
		clickMenuIlDdjs();
		//选择计算类型
		chooseJislx(PPtoMDtoMDProperties.getProperty("ILDingdjs_jislx"));
		//选择需求来源
		chooseXuqly(PPtoMDtoMDProperties.getProperty("ILDingdjs_xuqly"));
		//选择资源获取日期
		chooseZiyhqrq(PPtoMDtoMDProperties.getProperty("ILDingdjs_ziyhqrq"));
		//选择第1条毛需求
		if(dingdjsPage.maoxqs.isExist()) {
			dingdjsPage.maoxqs.select(2);
			dingdjsPage.maoxqs.labl_xuqbc.click();
		}
		//确定
		dingdjsPage.link_qd.click();
		//生成订单
		dingdjsPage.link_scdd.click();
		while(true) {
			if(HolmosWindow.dealAlert()!=null) {
				HolmosWindow.dealConfirm(false);
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
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 订单查询
	 */
	@Test
	public void PPtoMDtoMD_dingdcx() {
		try {
			cxIldingdmx("UW");
			//cxIldingdmx("UL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HolmosWindow.closeAllWindows();
		}
	}
	
	/**
	 * 订单计算-卷料订单定义
	 */
	@Test
	public void PPtoMDtoMD_juanldddy() {
		try {
			//Il计划员登陆准备层
			loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
					Steup.usercenter.get("jhl"));
			
			//卷料订单定义
			clickMenuZbc();
			clickMenujuanlDddy();
			
			assertJuanldy("UW");
			assertJuanldy("UL");
			//退出系统
			logoutSystem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 订单计算-卷料订单
	 * @throws InterruptedException 
	 */
	@Test
	public void PPtoMDtoMD_juanldingdjs() throws InterruptedException {
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
				Steup.usercenter.get("jhl"));
		
		//Il订单计算
		clickMenuZbc();
		clickMenujuanlDdjs();
		//全选订单
		juanlDingdjsPage.check_quanx.click();
		//选择需求来源
		chooseXuqly3(PPtoMDtoMDProperties.getProperty("juanlDingdjs_xuqly"));
		//选择资源获取日期
		chooseZiyhqrq2(PPtoMDtoMDProperties.getProperty("juanlDingdjs_ziyhqrq"));
		//选择第1条毛需求
		if(juanlDingdjsPage.maoxqs.isExist()) {
			juanlDingdjsPage.maoxqs.select(2);
			juanlDingdjsPage.maoxqs.labl_xuqbc.click();
		}
		//确定
		juanlDingdjsPage.link_qd.click();
		//生成订单
		juanlDingdjsPage.link_scdd.click();
		String alertMess = "";
		while(true) {
			if(HolmosWindow.dealAlert()!=null) {
				alertMess = HolmosWindow.dealAlert();
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
		//assertEquals(alertMess, "计算成功");
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 卷料订单查询
	 */
	@Test
	public void PPtoMDtoMD_juanldingdcx() {
		try {
			cxJuanldingdmx("UW");
			cxJuanldingdmx("UL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HolmosWindow.closeAllWindows();
		}
	}
	
	/**
	 * 查询Il订单明细
	 * @param usercenter
	 * @throws Exception
	 */
	private void cxIldingdmx(String usercenter) throws Exception {
		//审核人登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("shr"), Steup.password,
				Steup.usercenter.get("shr"));
		
		//Il订单查询
		clickMenuZbc();
		//点击订单修改/生效
		clickMenuIlDdxgsx();
		//选择用户中心
		chooseUsercenter4(usercenter);
		//查询
		dingdxgsxPage.link_cx.clickAndWaitForLoad();
		WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
				.getDriver().getEngine());
		String firstHandler = driver.getWindowHandles().iterator().next();
		//验证7条订单
		for(int i=1;i<8;i++) {
			Thread.sleep(1000);
			
			System.out.println(driver.getWindowHandles());
			Thread.sleep(3000);
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
			}
			
			//点击订单号打开订单明细
			Class sxPageClass = dingdxgsxPage.getClass();
			Field field = sxPageClass.getField("dingd"+i);
			
			//Object obj = field.get(Link.class);
			Object obj = field.get(dingdxgsxPage);
			Link dingdLink = (Link)obj;
			dingdLink.click();
			
			Thread.sleep(3000);
 			driver = ((WebDriver) Allocator.getInstance().currentWindow
					.getDriver().getEngine());
			
			System.out.println(driver.getWindowHandles());
			Thread.sleep(3000);
			Set<String> handles = driver.getWindowHandles();
			handles.remove(firstHandler);
			for (String handle : handles) {
				driver.switchTo().window(handle);
			}
			
			List<String> mxList = new ArrayList<String>();
			switch (i) {
				case 1:
					mxList.add("订单下零件列表P0:2012年10月,M101820000,98038700DS,NP,1,4,18,42,0");
					break;
				case 2:
					mxList.add("订单下零件列表P0:2013年01月,M100750000,7552389880,NP,5400,0,0,0,0");
					break;
				case 3:
					mxList.add("订单下零件列表P0:2012年10月,M212090000,K900000018,NP,187.698,214.788,209.153,217.025,0");
					break;
				case 4:
					mxList.add("订单下零件列表P0:2012年10月,M102690000,K900000005,PP,0,0,200,0,WMP");
					break;
				case 5:
					mxList.add("订单下零件列表P0:2012年10月,M100230000,7903053374,PP,20000,17500,17500,15000,WMP");
					break;
				case 6:
					mxList.add("订单下零件列表P0:2012年11月,M201790000,7903053374,PP,37500,17500,15000,0,WMP");
					mxList.add("订单下零件列表P0:2012年11月,M201790000,7903053543,PP,289600,134400,128000,0,W05");
					mxList.add("订单下零件列表P0:2012年11月,M201790000,7903053543,PP,14400,4800,16000,0,W25");
					break;
				case 7:
					mxList.add("订单下零件列表P0:2012年10月,M102580000,7903053025,PP,5000,0,0,5000,WCP");
					mxList.add("订单下零件列表P0:2012年10月,M102580000,7903053025,PP,5000,0,0,5000,WMP");
					mxList.add("订单下零件列表P0:2012年10月,M102580000,K900000018,NP,187.698,214.788,209.153,217.025,0");
					break;	
				default:
					break;
			}
			assertDingdmx(mxList);
			HolmosWindow.close();
		}
		System.out.println(driver.getWindowHandles());
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 查询卷料订单明细
	 * @param usercenter
	 * @throws Exception
	 */
	private void cxJuanldingdmx(String usercenter) throws Exception {
		//审核人登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("shr"), Steup.password,
				Steup.usercenter.get("shr"));
		
		//Il订单查询
		clickMenuZbc();
		//点击订单修改/生效
		clickMenuIlDdxgsx();
		//选择用户中心
		chooseUsercenter4(usercenter);
		//选择订单类型
		dingdxgsxPage.text_dingdlx.click();
		dingdxgsxPage.div_dingdlx_JL.click();
		//查询
		dingdxgsxPage.link_cx.clickAndWaitForLoad();
		WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
				.getDriver().getEngine());
		String firstHandler = driver.getWindowHandles().iterator().next();
		//验证3条订单
		for(int i=1;i<3;i++) {
			Thread.sleep(1000);
			
			System.out.println(driver.getWindowHandles());
			Thread.sleep(3000);
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
			}
			
			//点击订单号打开订单明细
			Class sxPageClass = dingdxgsxPage.getClass();
			Field field = sxPageClass.getField("dingd"+i);
			
			//Object obj = field.get(Link.class);
			Object obj = field.get(dingdxgsxPage);
			Link dingdLink = (Link)obj;
			dingdLink.click();
			
			Thread.sleep(3000);
 			driver = ((WebDriver) Allocator.getInstance().currentWindow
					.getDriver().getEngine());
			
			System.out.println(driver.getWindowHandles());
			Thread.sleep(3000);
			Set<String> handles = driver.getWindowHandles();
			handles.remove(firstHandler);
			for (String handle : handles) {
				driver.switchTo().window(handle);
			}
			
			List<String> mxList = new ArrayList<String>();
			switch (i) {
				case 1:
//					mxList.add("订单下零件列表P0:2012年10月,M102580000,7903053025,PP,5000,0,5000,0,WCP");
//					mxList.add("订单下零件列表P0:2012年10月,M102580000,7903053025,PP,5000,0,5000,0,WMP");
					mxList.add("订单下零件列表P0:2012年11月,MBG0000000,K900000717,PP,495,120,120,0,WYZ");
					break;
				case 2:
					mxList.add("订单下零件列表P0:2012年11月,MWG0000000,K900000717,PP,215,50,55,0,WYZ");
					mxList.add("订单下零件列表P0:2012年11月,MWG0000000,K900000731,PP,0,0,0,0,WYZ");
					mxList.add("订单下零件列表P0:2012年11月,MWG0000000,K900000736,PP,0,0,0,0,WYZ");
					break;
				case 3:
					break;
				default:
					break;
			}
			assertDingdmx(mxList);
			HolmosWindow.close();
		}
		System.out.println(driver.getWindowHandles());
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}
		//退出系统
		logoutSystem();
	}
	/**
	 * 验证卷料定义的订单
	 * @param usercenter
	 * @throws InterruptedException
	 */
	private void assertJuanldy(String usercenter) throws InterruptedException {
		int flag = 0;
		while(true) {
			//点击增加
			juanlDddyPage.butt_zj.click();
			Thread.sleep(1000);
			//订单号(J+2位分钟数+2位秒数+4位随机数)
			SimpleDateFormat sdf = new SimpleDateFormat("mmss");
			String time = sdf.format(new Date());
			juanlDddyPage.text_dingdh.setText("J"+time+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9));
			
			//选择用户中心
			chooseUsercenter2(usercenter);
			//输入供应商
			juanlDddyPage.text_gongysdm.focus();
			juanlDddyPage.text_gongysdm.setText(PPtoMDtoMDProperties.getProperty("juanldddy_gongysdm"+flag));
			//输出计算周期
			juanlDddyPage.text_jiszq.setText(PPtoMDtoMDProperties.getProperty("juanldddy_jiszq"));
			//提交
			juanlDddyPage.butt_tj.focus();
			Thread.sleep(1000);
			juanlDddyPage.butt_tj.click();
			Thread.sleep(500);
			//如果没有弹出框
			if(("").equals(HolmosWindow.dealAlert()) || null==HolmosWindow.dealAlert()) {
				flag++;
			}
			//三次退出
			if(flag==2) {
				break;
			}
		}
		
		//点击查询
		chooseUsercenter3(usercenter);
		juanlDddyPage.link_cx.clickAndWaitForLoad();
		//判断是否有2条信息
		assertEquals(juanlDddyPage.list_dingds.getSize(), 3);
	}
	
	/**
	 * 验证订单明细
	 * @param mxList
	 * @throws InterruptedException
	 */
	private void assertDingdmx(List<String> mxList) throws Exception {
		for(int i=0; i < mxList.size(); i++) {
			WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
					.getDriver().getEngine());
			System.out.println(driver.getCurrentUrl());
			dingdmxPage.list_dingdmxs.select(i+2);
			String mx = mxList.get(i);
			String[] mxs = mx.split(",");
			if(!mxs[0].equals(""))
				//HolmosSimpleCheckTool.assertEqual(dingdmxPage.labl_p0rq.getText(), mxs[0]);
				assertEquals(dingdmxPage.labl_p0rq.getText(), mxs[0]);
			if(!mxs[1].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_gys.getText(), mxs[1]);
			if(!mxs[2].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_lingjh.getText(), mxs[2]);
			if(!mxs[3].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_gonghms.getText(), mxs[3]);
			if(!mxs[4].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p0.getText(), mxs[4]);
			if(!mxs[5].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p1.getText(), mxs[5]);
			if(!mxs[6].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p2.getText(), mxs[6]);
			if(!mxs[7].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p3.getText(), mxs[7]);
			if(!mxs[8].equals("") && !mxs[8].equals("0"))
				assertEquals(dingdmxPage.list_dingdmxs.labl_cangk.getText(), mxs[8]);
				
		}
	}
	
	/**
	 * 订单生效
	 */
	
	public void PPtoMDtoMD_dingdsx() {
		//审核人登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("shr"), Steup.password,
				Steup.usercenter.get("shr"));
		
		//Il订单修改生效
		clickMenuZbc();
		clickMenuIlDdxgsx();
		//选择制作时间开头(今天)
		dingdxgsxPage.text_zhizsjstart.setText("2012-10-30 00:00:00");
		//选择制作时间结束(今天)
		dingdxgsxPage.text_zhizsjend.click();
		chooseToDayTime();
		//查询
		dingdxgsxPage.link_cx.clickAndWaitForLoad();
		//全选
		dingdxgsxPage.check_quanx.setValue(true);
		//待生效
		dingdxgsxPage.link_dsx.clickAndWaitForLoad();
		//全选
		dingdxgsxPage.check_quanx.click();
		dingdxgsxPage.check_quanx.click();
		//生效
		dingdxgsxPage.link_sx.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//判断
		assertEquals(HolmosWindow.dealAlert(), "生效成功！");
		//退出系统
		logoutSystem();
	}
	
	/**
	 *执行接口 
	 */
	
	public void PPtoMDtoMD_executeInterfaceFordhll() {
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
		
		//批量：订单拆分要货令：STODDCFYHL
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"STODDCFYHL");
		
		//执行层输出2520：要货令-EFI
		executeInterface(Steup.url.get("interface_zxc"), "2520");
	}
	
	/**
	 * 到货信息录入-到货验收-验收单删除-到货验收
	 */
	
	public void PPtoMDtoMD_daohxxll_ys_sc_ys() {
		try {
			//执行层收货员执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(PPtoMDtoMDProperties.getProperty("zxc_ckh"));
			//输入功能号102
			commonPage.text_gongnh.setText("102");
			//回车
			sendKeys(Keys.ENTER);
			//输入BL号
			//daohxxllPage.text_blh.setText(PPtoMDtoMDProperties.getProperty("daohxxll_blh"));
			//集装箱号/卡车号
			daohxxllPage.text_tch.setText(PPtoMDtoMDProperties.getProperty("daohxxll_tch"));
			//输入承运商编号
			daohxxllPage.text_chengysdm.setText(PPtoMDtoMDProperties.getProperty("daohxxll_chengysdm"));
			//输入零件编号
			daohxxllPage.text_lingjbh.setText(PPtoMDtoMDProperties.getProperty("daohxxll_lingjbh"));
			//回车
			sendKeys(Keys.ENTER);
			//输入供应商编号
			daohxxllPage.text_gongysdm.setText(PPtoMDtoMDProperties.getProperty("daohxxll_gongysdm"));
			sendKeys(Keys.TAB);
			//输入零件总数
			daohxxllPage.text_lingjzsl.setText(PPtoMDtoMDProperties.getProperty("daohxxll_lingjzsl"));
			//输入批次号
			//daohxxllPage.text_pich1.setText(PPtoMDtoMDProperties.getProperty("daohxxll_pich1"));
			//输入失效期
			//daohxxllPage.text_shixrq.setText(PPtoMDtoMDProperties.getProperty("daohxxll_shixrq"));
			//输入要货令号
			daohxxllPage.text_yaohlh.setText(PPtoMDtoMDProperties.getProperty("daohxxll_yaohlh"));
			//回车
			sendKeys(Keys.ENTER);
			//输入零件数量
			daohxxllPage.text_lingjsl.setText(PPtoMDtoMDProperties.getProperty("daohxxll_lingjsl"));
			//输入UA型号
			daohxxllPage.text_uaxh.setText(PPtoMDtoMDProperties.getProperty("daohxxll_uaxh"));
			//输入UC型号
			daohxxllPage.text_ucxh.setText(PPtoMDtoMDProperties.getProperty("daohxxll_ucxh"));
			//输入UC个数
			daohxxllPage.text_ucgs.setText(PPtoMDtoMDProperties.getProperty("daohxxll_ucgs"));
			//输入批次号
			//daohxxllPage.text_pich.setText(PPtoMDtoMDProperties.getProperty("daohxxll_pich"));
			
			//F2添加
			sendKeys(Keys.F2);  
			Thread.sleep(1000);
			//录入不存在的bl号
			while(true) {
				if(!commonPage.labl_message.getText().equals("请继续操作!")) {
					//随即生成BL号
					String randomBlh =  "BL"+new Random().nextInt(999999999);
					daohxxllPage.text_blh.setText(randomBlh);
					//成功保存BL号
					blh = randomBlh;
					//输入零件总数
					daohxxllPage.text_lingjzsl.setText(PPtoMDtoMDProperties.getProperty("daohxxll_lingjzsl"));
				}
				else {
					break;
				}
				sendKeys(Keys.F2);
				Thread.sleep(1000);
			}
			//F7完成录入
			sendKeys(Keys.F7);
			Thread.sleep(1000);
			//判断
			assertEquals(commonPage.labl_message.getText(), "操作成功!");
			//返回
			sendKeys(Keys.F12);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutZxcSystem();
		}
		//为了取上步的BL号，写在一个方法里面
		try {
			//执行层收货员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("shl"), Steup.password,
					Steup.usercenter.get("shl"));
			//选择仓库
			chooseCkh(PPtoMDtoMDProperties.getProperty("zxc_ckh"));
			//输入功能号103
			commonPage.text_gongnh.setText("103");
			//回车
			sendKeys(Keys.ENTER);
			//输入bl号
			daohysPage.text_blh.setText(blh);
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
			assertEquals(commonPage.labl_message.getText(), "请继续操作!");
			//保存UTH下步删除
			String uth = daohysPage.labl_uth.getText();
			//返回
			sendKeys(Keys.F12);
			//输入功能号143验收单删除
			commonPage.text_gongnh.setText("143");
			//回车
			sendKeys(Keys.ENTER);
			//输入UT号
			yansdscPage.text_uth.setText(uth);
			//删除理由
			yansdscPage.text_shancyy.setText("测试");
			//F6
			sendKeys(Keys.F6);
			Thread.sleep(1000);
			//判断
			assertEquals(commonPage.labl_message.getText(), "操作成功!");
			//返回
			sendKeys(Keys.F12);
			//再验收
			//输入功能号103
			commonPage.text_gongnh.setText("103");
			//回车
			sendKeys(Keys.ENTER);
			//输入bl号
			daohysPage.text_blh.setText(blh);
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
			assertEquals(commonPage.labl_message.getText(), "请继续操作!");
			//返回
			sendKeys(Keys.F12);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			//logoutZxcSystem();
		}
	}
	
	/**
	 * 408库内质检零件打印清单 前面的到货信息录入不能循环做，所以这步暂时去掉
	 *
	public void PPtoMDtoMD_zhijdyqd() {
		try {
			//执行层收货员登陆执行层系统
			loginSystem(Steup.url.get("zxc"), Steup.username.get("zjy"), Steup.password,
					Steup.usercenter.get("zjy"));
			//选择仓库
			chooseCkh(PPtoMDtoMDProperties.getProperty("zxc_ckh"));
			//输入功能号408
			commonPage.text_gongnh.setText("408");
			//回车
			sendKeys(Keys.ENTER);
			//F1查询
			sendKeys(Keys.F1);
			Thread.sleep(1000);
			//F5全选
			sendKeys(Keys.F5);
			//F2打印
			sendKeys(Keys.F2);
			//返回
			sendKeys(Keys.F12);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutZxcSystem();
		}
	}*/
	
	/**
	 * 按需初始化计算
	 */
	
	public void PPtoMDtoMD_anxcshjs() {
		try{
			//Il计划员登陆准备层系统
			loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
					Steup.usercenter.get("jhl"));
			//点击按需初始化计算菜单
			clickMenuZbc();
			clickMenuAnxcshjs();
			//选择供货模式
			chooseZbcGonghms(PPtoMDtoMDProperties.getProperty("anxcshjs_gonghms"));
			//提交计算
			anxcshjsPage.link_tjjs.click();
			String alertMessage = "";
			while(true) {
				if(HolmosWindow.dealAlert()!=null) {
					alertMessage = HolmosWindow.dealAlert();
					break;
				}
				else {
					Thread.sleep(5000);
				}
			}
			
			//判断
			assertEquals(alertMessage, "计算成功！");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
		}
	}
	
	/**
	 * 执行接口
	 */
	public void PPtoMDtoMD_executeInterfaceForAnxddjs() {
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
		
		//批量：订单拆分要货令：STODDCFYHL
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"STODDCFYHL");
		
		//执行层输出2550：订单
		executeInterface(Steup.url.get("interface_zxc"), "2550");
		//执行层输出2380：外部要货令
		executeInterface(Steup.url.get("interface_zxc"), "2380");
		//执行层输出2350：内部要货令
		executeInterface(Steup.url.get("interface_zxc"), "2350");
		
		//1.输入1590：订单
		downLoadZXCAndUploadZBC("ath1oath307.txt", "ath3iath107.txt");
		executeInterface(Steup.url.get("interface_zbc"), "1590");
		//2.输入1400：外部要货令
		downLoadZXCAndUploadZBC("ath1oath305.txt", "ath3iath105.txt");
		executeInterface(Steup.url.get("interface_zbc"), "1400");
		//3.输入1410：内部要货令
		downLoadZXCAndUploadZBC("ath1oath200.txt", "ath3iat199.txt");
		executeInterface(Steup.url.get("interface_zbc"), "1410");
	}
	
	/**
	 * 按需计算
	 */
	public void PPtoMDtoMD_anxddJs() {
		try{
			//Il计划员登陆准备层
			loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
					Steup.usercenter.get("jhl"));
			
			//按需计算
			clickMenuZbc();
			clickMenuIlDdjs();
			//选择计算类型
			chooseJislx(PPtoMDtoMDProperties.getProperty("anxddJs_jislx"));
			//选择资源获取日期
			chooseZiyhqrq(PPtoMDtoMDProperties.getProperty("anxddJs_ziyhqrq"));
			
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
			assertEquals(alertMessage, "是否导出中间表内容");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
		}
	}
	
	/**
	 * 执行接口
	 */
	public void PPtoMDtoMD_executeInterfaceForAnxddzz() {
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
		
		//批量：订单拆分要货令：STODDCFYHL
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"STODDCFYHL");
		//批量：按需备货STOBatAXSto
		execCommand(Steup.batchHost, Steup.batchName, Steup.batchPassword,"STOBatAXSto");
	}
	
	/**
	 * 329仓库间移库
	 */
	
	public void PPtoMDtoMD_cangkjyk() {
		try{
			//仓库管理员登陆执行层
			loginSystem(Steup.url.get("zxc"), Steup.username.get("ckgly"), Steup.password,
					Steup.usercenter.get("ckgly"));
			
			//选择仓库
			chooseCkh(PPtoMDtoMDProperties.getProperty("zxc_ckh"));
			//输入功能号329
			commonPage.text_gongnh.setText("329");
			//回车
			sendKeys(Keys.ENTER);
			//输入移库仓库
			cangkjykPage.text_jiesckbh.setText(PPtoMDtoMDProperties.getProperty("cangkjyk_ckh"));
			//输入仓库号
			cangkjykPage.text_lingjbh.setText(PPtoMDtoMDProperties.getProperty("cangkjyk_lingjbh"));
			HolmosWindow.maxSizeWindow();
			//F1查询
			sendKeys(Keys.F1);
			Thread.sleep(1000);
			//F5全选
			sendKeys(Keys.F5);
			//F2添加
			sendKeys(Keys.F2);
			Thread.sleep(1000);
			//F7查看跳到维护页面
			sendKeys(Keys.F7);
			Thread.sleep(1000);
			//F5全选
			sendKeys(Keys.F5);
			//F8打印
			sendKeys(Keys.F8);
			Thread.sleep(1000);
			//判断操作是否成功
			assertEquals(commonPage.labl_message.getText(), "操作成功!");
			//F11返回上一步
			sendKeys(Keys.F11);
			Thread.sleep(1000);
			//F7再次查看
			sendKeys(Keys.F7);
			Thread.sleep(1000);
			//F5全选
			sendKeys(Keys.F5);
			//F6删除(为了下次循环做)
			sendKeys(Keys.F6);
			HolmosWindow.dealConfirm(true);
			Thread.sleep(1000);
			//判断操作是否成功
			assertEquals(commonPage.labl_message.getText(), "操作成功!");
			//F12返回
			sendKeys(Keys.F12);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//退出系统
			logoutSystem();
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

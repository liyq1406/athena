package com.auto.testcases.PPtoRMtoRD;

import static org.junit.Assert.assertEquals;
import holmos.webtest.Allocator;
import holmos.webtest.basetools.HolmosWindow;
import holmos.webtest.element.Link;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;
import com.auto.common.Util;
import com.auto.common.page.CommonPage;
import com.auto.testcases.page.KDzqdd.AnxDingdmxPage;
import com.auto.testcases.page.KDzqdd.DingddyPage;
import com.auto.testcases.page.KDzqdd.DingdjsPage;
import com.auto.testcases.page.KDzqdd.DingdmxPage;
import com.auto.testcases.page.KDzqdd.DingdxgsxPage;
import com.auto.testcases.page.maoxq.MaoxqcxbjPage;
/**
 * PP-MD-MD测试路线
 * @author lizhi
 * @date 2012-10-25
 */
public class PPtoRMtoRD extends PublicVerify {
	//KD订单计算
	public DingdjsPage dingdjsPage = new DingdjsPage();
	//KD AX订单定义
	public DingddyPage dingddyPage = new DingddyPage();
	//KD订单修改
	public DingdxgsxPage dingdxgsxPage = new DingdxgsxPage();
	//KD订单明细
	public DingdmxPage dingdmxPage = new DingdmxPage();
	//AX订单明细
	public AnxDingdmxPage anxdingdmxPage = new AnxDingdmxPage();
	//毛需求查询/比较界面
	public MaoxqcxbjPage maoxqcxbjPage = new MaoxqcxbjPage();
	
	//公用界面
	public CommonPage commonPage = new CommonPage();
	
	public Properties PPtoRMtoRDProperties;
	
	public PPtoRMtoRD() throws IOException {
		super();
		PPtoRMtoRDProperties = Util.propertiesUrl("PPtoRMtoRD");
	}
	
	/**
	 *执行接口 
	 */
	@Test
	public void PPtoRMtoRD_executeInterfaceForKDjs() {
		//准备层输入1050：周期毛需求（IL：周期):ath3inup02.txt
		executeInterface(Steup.url.get("interface_zbc"), "1051");
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("zbc_POA"), Steup.password,
				Steup.usercenter.get("zbc_POA"));
		
		//毛需求查询/比较
		clickMenuZbc();
		clickMenuMaoxqbj();
		//选择需求来源
		chooseXuqly2(PPtoRMtoRDProperties.getProperty("maoxqcxbj_xuqly"));
		//选择需求版次
		maoxqcxbjPage.text_xuqbc.click();
		//选择今天
		commonPage.link_today.click();
		//输入序号
		maoxqcxbjPage.text_xuh.setText(PPtoRMtoRDProperties.getProperty("maoxqcxbj_xuh"));
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
	 * KD订单计算-订单定义
	 */
	@Test
	public void PPtoRMtoRD_dingddy() {
		try {
			//Il计划员登陆准备层
			loginSystem(Steup.url.get("zbc"), Steup.username.get("root"), Steup.password,
					Steup.usercenter.get("root"));
			
			//卷料订单定义
			clickMenuZbc();
			clickMenuKDDddy();
			
			assertDingddy("1","78001H");
			assertDingddy("2","52455R");
			//退出系统
			logoutSystem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 订单计算-KD周期订单
	 */
	@Test
	public void PPtoRMtoRD_dingdjs() {
		//Il计划员登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("jhl"), Steup.password,
				Steup.usercenter.get("jhl"));
		
		//KD订单计算
		clickMenuZbc();
		commonPage.menu_dingdjs.click();
		commonPage.menu_KDzqdd.click();
		assertDingdjs(3);
		assertDingdjs(2);
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 订单查询
	 */
	@Test
	public void PPtoRMtoRD_dingdcx() {
		try {
			cxKDdingdmx("KD");
			cxKDdingdmx("AX");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HolmosWindow.closeAllWindows();
		}
	}
	
	/**
	 * 查询KD订单明细
	 * @param usercenter
	 * @throws Exception
	 */
	private void cxKDdingdmx(String dingdlx) throws Exception {
		//审核人登陆准备层
		loginSystem(Steup.url.get("zbc"), Steup.username.get("shr"), Steup.password,
				Steup.usercenter.get("shr"));
		
		//Il订单查询
		clickMenuZbc();
		//点击订单修改/生效
		clickMenuKDDdxgsx();
		//选择订单类型
		dingdxgsxPage.text_dingdlx.click();
		if("KD".equals(dingdlx)) {
			dingdxgsxPage.div_dingdlx_KD.click();
		} else {
			dingdxgsxPage.div_dingdlx_AX.click();
		}
		
		//查询
		dingdxgsxPage.link_cx.clickAndWaitForLoad();
		WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
				.getDriver().getEngine());
		String firstHandler = driver.getWindowHandles().iterator().next();
		//验证2条订单
		for(int i=1;i<2;i++) {
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
					if("KD".equals(dingdlx)) {
						mxList.add("订单下零件列表P0:2012年11月,78001H  P2,7552160880,309400,46900,92400,61600,58100");
						mxList.add("订单下零件列表P0:2012年11月,78001H  P2,7903009337,3600,1650,2250,1650,0");
						assertDingdmx(mxList);
					}
					else {
						mxList.add("订单下零件列表P0:2012年11月,52455R  01,9671978680,34952,5904,9016,0,0");
						mxList.add("订单下零件列表P0:2012年11月,52455R  01,9671978480,8480,2496,4096,0,0");
						assertAXDingdmx(mxList);
					}
					break;
				default:
					break;
			}
			//assertDingdmx(mxList);
			//HolmosWindow.close();
		}
		
		if("KD".equals(dingdlx)) {
			//切换成UX用户中心
			dingdmxPage.text_usercenter.click();
			dingdmxPage.opti_usercenter.addXpathLocator("//div[@id='field-pop-kd_usercenter']/div[3]");
			dingdmxPage.opti_usercenter.click();
			//查询
			dingdxgsxPage.link_cx.clickAndWaitForLoad();
			//验证2条订单
			for(int i=1;i<2;i++) {
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
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
				}
				
				List<String> mxList = new ArrayList<String>();
				switch (i) {
					case 1:
						mxList.add("订单下零件列表P0:2012年11月,78001H  P2,7903602089,368000,64000,80000,64000,48000");
						mxList.add("订单下零件列表P0:2012年11月,78001H  P2,7903602218,103600,18200,23800,16800,15050");
						break;
					default:
						break;
				}
				assertDingdmx(mxList);
				//HolmosWindow.close();
			}
		}
		HolmosWindow.close();
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}
		//退出系统
		logoutSystem();
	}
	
	/**
	 * 验证订单明细
	 * @param mxList
	 * @throws InterruptedException
	 */
	private void assertDingdmx(List<String> mxList) throws InterruptedException {
		for(int i=0; i < mxList.size(); i++) {
			WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
					.getDriver().getEngine());
			System.out.println(driver.getCurrentUrl());
			dingdmxPage.list_dingdmxs.select(i+2);
			String mx = mxList.get(i);
			String[] mxs = mx.split(",");
			if(!mxs[0].equals(""))
				assertEquals(dingdmxPage.labl_p0rq.getText(), mxs[0]);
			if(!mxs[1].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_gys.getText(), mxs[1]);
			if(!mxs[2].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_lingjh.getText(), mxs[2]);
			if(!mxs[3].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p0.getText(), mxs[3]);
			if(!mxs[4].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p1.getText(), mxs[4]);
			if(!mxs[5].equals(""))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p2.getText(), mxs[5]);
			if(!mxs[6].equals("") && !mxs[6].equals("0"))
				assertEquals(dingdmxPage.list_dingdmxs.labl_p3.getText(), mxs[6]);
		}
	}
	
	/**
	 * 验证订单明细
	 * @param mxList
	 * @throws InterruptedException
	 */
	private void assertAXDingdmx(List<String> mxList) throws InterruptedException {
		for(int i=0; i < mxList.size(); i++) {
			WebDriver driver = ((WebDriver) Allocator.getInstance().currentWindow
					.getDriver().getEngine());
			System.out.println(driver.getCurrentUrl());
			anxdingdmxPage.list_dingdmxs.select(i+2);
			String mx = mxList.get(i);
			String[] mxs = mx.split(",");
			if(!mxs[0].equals(""))
				assertEquals(anxdingdmxPage.labl_p0rq.getText(), mxs[0]);
			if(!mxs[1].equals(""))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_gys.getText(), mxs[1]);
			if(!mxs[2].equals(""))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_lingjh.getText(), mxs[2]);
			if(!mxs[3].equals(""))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_p0.getText(), mxs[3]);
			if(!mxs[4].equals(""))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_p1.getText(), mxs[4]);
			if(!mxs[5].equals(""))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_p2.getText(), mxs[5]);
			if(!mxs[6].equals("") && !mxs[6].equals("0"))
				assertEquals(anxdingdmxPage.list_dingdmxs.labl_p3.getText(), mxs[6]);
		}
	}
	
	private void assertDingdjs(int dingdIndex) {
		
		commonPage.menu_dingdjsForKD.clickAndWaitForLoad();
		//选择订单
		dingdjsPage.text_dingd.click();
		dingdjsPage.div_dingdh.addXpathLocator("//div[@id='field-pop-layout_dingdh']/div["+dingdIndex+"]");
		try {
			Thread.sleep(1000);
		
			dingdjsPage.div_dingdh.click();
			//选择需求来源
			chooseXuqly(PPtoRMtoRDProperties.getProperty("KDDingdjs_xuqly"));
			//选择资源获取日期
			chooseZiyhqrq(PPtoRMtoRDProperties.getProperty("KDDingdjs_ziyhqrq"));
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
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * 验证定义的订单
	 * @param usercenter
	 * @throws InterruptedException
	 */
	private void assertDingddy(String dingdlx,String gongysdm) throws InterruptedException {
		int flag = 0;
		while(true) {
			//点击增加
			dingddyPage.butt_zj.click();
			Thread.sleep(1000);
			//订单号(J+2位分钟数+2位秒数+4位随机数)
			SimpleDateFormat sdf = new SimpleDateFormat("mmss");
			String time = sdf.format(new Date());
			dingddyPage.text_dingdh.focus();
			String dingdh = "";
			if(dingdlx.equals("1")) {
				dingdh = "J"+time+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9);
			} else {
				dingdh = "A"+time+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9)+new Random().nextInt(9);
			}
			dingddyPage.text_dingdh.setText(dingdh);
			
			//选择订单类型
			dingddyPage.text_dingdlx.click();
			dingddyPage.opti_dingdlx.addXpathLocator("//div[@id='field-pop-kdorder_dingdlx']/div[@value='"+dingdlx+"']");
			dingddyPage.opti_dingdlx.click();
			//选择供应商
			dingddyPage.text_gongysdm.click();
			dingddyPage.opti_gongysdm.addAttributeLocator("value", gongysdm);
			dingddyPage.opti_gongysdm.click();
			//输入计算周期
			dingddyPage.text_jiszq.focus();
			dingddyPage.text_jiszq.setText(PPtoRMtoRDProperties.getProperty("dingddy_jiszq"));
			//输入发运周期
			dingddyPage.text_fahzq.setText(PPtoRMtoRDProperties.getProperty("dingddy_fayzq"));
			//提交
			dingddyPage.butt_tj.focus();
			Thread.sleep(1000);
			dingddyPage.butt_tj.click();
			Thread.sleep(500);
			//如果弹出框
			if(("插入成功").equals(HolmosWindow.dealAlert()) || null==HolmosWindow.dealAlert()) {
				flag++;
			}
			//1次退出
			if(flag==1) {
				break;
			}
		}
		
		//点击查询订单类型
		dingddyPage.text_dingdlxForSearch.click();
		dingddyPage.opti_dingdlx.addXpathLocator("//div[@id='field-pop-kd_dingdlx']/div[@value='"+dingdlx+"']");
		dingddyPage.opti_dingdlx.click();
		//选择订单状态
		dingddyPage.text_dingdzt.click();
		dingddyPage.opti_dingdzt.addXpathLocator("//div[@id='field-pop-kd_dingdzt']/div[2]");
		dingddyPage.opti_dingdzt.click();
		dingddyPage.link_cx.clickAndWaitForLoad();
		//判断是否有1条信息
		assertEquals(dingddyPage.list_dingds.getSize(), 2);
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

package com.auto.common;

import holmos.webtest.EngineType;
import holmos.webtest.basetools.HolmosWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.auto.common.page.CommonPage;
import com.auto.common.page.InterfacePage;
import com.auto.init.ReadConfig;
import com.auto.testcases.page.LoginPage;
import com.jcraft.jsch.ChannelSftp;

/**
 * 公共元素、场景
 * @date 2012-8-9
 * @author lizhi
 */
public class PublicVerify extends Steup {
	//打开的模式
	public static EngineType engineType = EngineType.WebDriverIE;
	//properties对象
	public Properties loginProperties;
	//登陆页面
	private LoginPage loginPage = new LoginPage();
	//公用页面
	private CommonPage commonPage = new CommonPage();
	//接口界面
	private InterfacePage interfacePage = new InterfacePage();
	
	public PublicVerify() throws IOException {
		loginProperties = Util.propertiesUrl("login");
		ReadConfig readConfig = new ReadConfig();
		// 读取配置文件
		readConfig.viewXML(); 
	}
	
	/**
	 * 设置运行会话
	 * @throws Exception 
	 */
	// 针对所有测试，只执行一次，且必须为static void
	@BeforeClass
	public static void setUp() throws Exception {
//		ReadConfig readConfig = new ReadConfig();
//		// 读取配置文件
//		readConfig.viewXML(); 
//		// 实例化selenium
//		selenium = new AthenaSelenium("localhost", 4444, browser,
//				url.get("xqjs")); 
//		selenium.start();
//		selenium.useXpathLibrary("javascript-xpath");
//		
//		//登陆
//		PublicVerify p = new PublicVerify();
//		p.loginSystem(Steup.url.get("xqjs"), Steup.username.get("xtgly"), Steup.password,
//				Steup.usercenter.get("xtgly"));
//		//等待页面加载
//		selenium.waitForPageToLoad(Steup.timeout);
	}

	/**
	 * 关闭会话
	 * @throws IOException 
	 */
	// 针对所有测试，只在最后执行一次，且必须为static void
	@AfterClass
	public static void tearDown() throws IOException {
//		//登陆
//		PublicVerify p = new PublicVerify();
//		p.logoutSystem();
//		
//		// 关闭窗口
//		selenium.close(); 
//		// 关闭selenium 窗口
//		selenium.stop(); 
	}

	/**
	 * 登录系统动作
	 * 
	 * @param loginUrl 登录的访问地址
	 * @param loginUsername 登录的用户名
	 * @param loginPassword 登录的密码
	 * @param loginUserCenter 登陆用户中心
	 * @return
	 * @throws Exception
	 */
	public void loginSystem(String loginUrl, String loginUsername,
			String loginPassword, String loginUserCenter)  {
//		HolmosWindow.openNewWindow(engineType,"www.sina.com");
//		//loginPage.text_name.addNameLocator("q");
//		loginPage.text_name.setText("11111");
		//打开登陆页面
		HolmosWindow.openNewWindow(engineType,loginUrl);
		while(true) {
			if(loginPage.text_name.isExist()) break;
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//HolmosWindow.maxSizeWindow();
		//输入登录名
		loginPage.text_name.focus();
		loginPage.text_name.setText(loginUsername);
		//输入密码
		loginPage.text_pwd.setText(loginPassword);
		//选择用户中心
		loginPage.como_usercenter.selectByValue(loginUserCenter);
		//点击登陆
		loginPage.butt_login.clickAndWaitForLoad();
		//HolmosWindow.maxSizeWindow();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 退出系统动作
	 */
	public void logoutSystem() {
		//点击登出
		loginPage.link_logout.clickAndWaitForLoad();
		HolmosWindow.closeAllWindows();
	}
	
	/**
	 * 执行层退出系统动作
	 */
	public void logoutZxcSystem() {
		//F12退出
		//HolmosWindow.KeyDown(Keys.F12);
		HolmosWindow.closeAllWindows();
	}
	
	/**
	 * 执行接口
	 * @param interfaceUrl 接口地址
	 * @param interfaceId 接口ID
	 */
	public void executeInterface(String interfaceUrl,String interfaceId) {
		//打开接口页面
		HolmosWindow.openNewWindow(engineType, interfaceUrl);
		//输入接口ID
		interfacePage.text_interfaceId.focus();
		interfacePage.text_interfaceId.setText(interfaceId);
		//点击确认
		interfacePage.butt_goto.clickAndWaitForLoad();
		//关闭页面
		HolmosWindow.closeAllWindows();
	}
	/**
	 * 选择用户中心
	 * @param usercenter 用户中心
	 */
	public void chooseUsercenter(String usercenter) {
		commonPage.text_usercenter.click();
		if(usercenter.equals("UW")) {
			commonPage.div_UW.click();
		}
		else if(usercenter.equals("UL")) {
			commonPage.div_UL.click();
		}
		else if(usercenter.equals("UX")) {
			commonPage.div_UX.click();
		}
	}
	
	/**
	 * 选择用户中心
	 * @param usercenter 用户中心
	 */
	public void chooseUsercenter2(String usercenter) {
		commonPage.text_usercenter2.click();
		if(usercenter.equals("UW")) {
			commonPage.div_UW2.addXpathLocator("//div[@id='field-pop-kdorder_usercenter']/div[1]");
			commonPage.div_UW2.click();
		}
		else if(usercenter.equals("UL")) {
			commonPage.div_UL2.addXpathLocator("//div[@id='field-pop-kdorder_usercenter']/div[2]");
			commonPage.div_UL2.click();
		}
		else if(usercenter.equals("UX")) {
			commonPage.div_UX2.addXpathLocator("//div[@id='field-pop-kdorder_usercenter']/div[3]");
			commonPage.div_UX2.click();
		}
	}
	
	/**
	 * 选择用户中心
	 * @param usercenter 用户中心
	 */
	public void chooseUsercenter3(String usercenter) {
		commonPage.text_usercenter3.click();
		if(usercenter.equals("UW")) {
			commonPage.div_UW.click();
		}
		else if(usercenter.equals("UL")) {
			commonPage.div_UL.click();
		}
		else if(usercenter.equals("UX")) {
			commonPage.div_UX.click();
		}
	}
	
	/**
	 * 选择用户中心
	 * @param usercenter 用户中心
	 */
	public void chooseUsercenter4(String usercenter) {
		commonPage.text_usercenter4.click();
		if(usercenter.equals("UW")) {
			commonPage.div_UW.click();
		}
		else if(usercenter.equals("UL")) {
			commonPage.div_UL.click();
		}
		else if(usercenter.equals("UX")) {
			commonPage.div_UX.click();
		}
	}
	/**
	 * 点击准备层菜单
	 */
	public void clickMenuZbc() {
		commonPage.menu_zbc.clickAndWaitForLoad();
	}
	
	/**
	 * 点击用户中心菜单
	 */
	public void clickMenuUsercenter() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_usercenter.clickAndWaitForLoad();
	}
	/**
	 * 点击车间菜单
	 */
	public void clickMenuWorkshop() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_workshop.clickAndWaitForLoad();
	}
	
	/**
	 * 点击系统参数
	 */
	public void clickMenuSystParameters() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_systParameters.clickAndWaitForLoad();
	}
	
	/**
	 * 点击通用零件
	 */
	public void clickMenuSystParts() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_systParts.clickAndWaitForLoad();
	}
	
	/**
	 * 点击包装
	 */
	public void clickMenuPackages() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_packages.clickAndWaitForLoad();
	}
	
	/**
	 * 点击零件供应商
	 */
	public void clickMenuPartsSuppliers() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_PartsSuppliers.clickAndWaitForLoad();
	}
	
	/**
	 * 点击供应商
	 */
	public void clickMenuSuppliers() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_suppliers.clickAndWaitForLoad();
	}
	
	/**
	 * 点击消耗点-零件
	 */
	public void clickMenuPointParts() {
		commonPage.menu_ckx.click();
		commonPage.menu_jiccs.click();
		commonPage.menu_pointParts.clickAndWaitForLoad();
	}
	/**
	 * 点击毛需求查询/比较
	 */
	public void clickMenuMaoxqbj() {
		commonPage.menu_maoxq.click();
		commonPage.menu_maoxqbj.clickAndWaitForLoad();
	}
	/**
	 * 点击看板循环菜单
	 */
	public void clickMenuKanxhjs() {
		commonPage.menu_kbyhl.click();
		commonPage.menu_kbxh.click();
		commonPage.menu_kanbxhjs.clickAndWaitForLoad();
	}
	/**
	 * 点击内部循环规模手工设置菜单
	 */
	public void clickMenuNeibxhgmsgsz() {
		commonPage.menu_kbyhl.click();
		commonPage.menu_kbxh.click();
		commonPage.menu_neibxhgmsgsz.clickAndWaitForLoad();
	}
	/**
	 * 点击外部循环规模手工设置菜单
	 */
	public void clickMenuWaibxhgmsgsz() {
		commonPage.menu_kbyhl.click();
		commonPage.menu_kbxh.click();
		commonPage.menu_waibxhgmsgsz.clickAndWaitForLoad();
	}
	/**
	 * 点击外部看板循环管理菜单
	 */
	public void clickMenuWaibkbxhgl() {
		commonPage.menu_kbyhl.click();
		commonPage.menu_kbxh.click();
		commonPage.menu_waibkbxhgl.clickAndWaitForLoad();
	}
	/**
	 * 点击内部看板循环管理菜单
	 */
	public void clickMenuNeibkbxhgl() {
		commonPage.menu_kbyhl.click();
		commonPage.menu_kbxh.click();
		commonPage.menu_neibkbxhgl.clickAndWaitForLoad();
	}
	
	/**
	 * 点击IL周期订单的订单计算
	 */
	public void clickMenuIlDdjs() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_Ilzqdd.click();
		commonPage.menu_dingdjsForIl.clickAndWaitForLoad();
	}
	/**
	 * 点击IL周期订单的卷料订单定义
	 */
	public void clickMenujuanlDddy() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_Ilzqdd.click();
		commonPage.menu_juanldddy.clickAndWaitForLoad();
	}
	
	/**
	 * 点击IL周期订单的卷料订单计算
	 */
	public void clickMenujuanlDdjs() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_Ilzqdd.click();
		commonPage.menu_juanlddjs.clickAndWaitForLoad();
	}
	
	/**
	 * 点击IL周期订单的订单修改生效
	 */
	public void clickMenuIlDdxgsx() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_Ilzqdd.click();
		commonPage.menu_dingdxgsxForIl.clickAndWaitForLoad();
	}
	
	/**
	 * 点击IL周期订单的订单修改生效
	 */
	public void clickMenuKDDdxgsx() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_KDzqdd.click();
		commonPage.menu_dingdxgsxForKD.clickAndWaitForLoad();
	}
	/**
	 * 点击KD周期订单的订单计算
	 */
	public void clickMenuKDDdjs() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_KDzqdd.click();
		commonPage.menu_dingdjsForKD.clickAndWaitForLoad();
	}
	
	/**
	 * 点击KD周期订单的订单定义
	 */
	public void clickMenuKDDddy() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_KDzqdd.click();
		commonPage.menu_dingddyForKD.clickAndWaitForLoad();
	}
	
	/**
	 * 点击按需初始化计算
	 */
	public void clickMenuAnxcshjs() {
		commonPage.menu_dingdjs.click();
		commonPage.menu_anxdd.click();
		commonPage.menu_anxcshtjjs.clickAndWaitForLoad();
	}
	
	/**
	 * 选择计算类型
	 * @param jslx 计算类型
	 */
	public void chooseJislx(String jislx) {
		commonPage.text_jislx.click();
		commonPage.opti_jislx.addXpathLocator("//div[@value='"+jislx+"']");
		commonPage.opti_jislx.click();
	}
	
	/**
	 * 选择计算类型
	 * @param xuqly 计算类型
	 */
	public void chooseXuqly(String xuqly) {
		commonPage.text_xuqly.click();
		commonPage.opti_xuqly.addXpathLocator("//div[@value='"+xuqly+"']");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		commonPage.opti_xuqly.click();
	}
	
	/**
	 * 选择需求来源
	 * @param xuqly 需求来源
	 */
	public void chooseXuqly2(String xuqly) {
		commonPage.text_xuqly2.click();
		commonPage.opti_xuqly.addXpathLocator("//div[@value='"+xuqly+"']");
		commonPage.opti_xuqly.click();
	}
	
	/**
	 * 选择需求来源
	 * @param xuqly 需求来源
	 * @throws InterruptedException 
	 */
	public void chooseXuqly3(String xuqly) throws InterruptedException {
		commonPage.text_xuqly3.click();
		Thread.sleep(500);
		commonPage.opti_xuqly.addXpathLocator("//div[@value='"+xuqly+"']");
		commonPage.opti_xuqly.click();
	}
	/**
	 * 选择计算日期
	 * @param ziyhqrq 计算日期
	 * @throws InterruptedException 
	 */
	public void chooseZiyhqrq(String ziyhqrq) throws InterruptedException {
		commonPage.text_ziyhqrq.click();
		commonPage.opti_ziyhqrq.addXpathLocator("//div[@value='"+ziyhqrq+"']");
		Thread.sleep(1000);
		commonPage.opti_ziyhqrq.click();
	}
	/**
	 * 选择计算日期
	 * @param ziyhqrq 计算日期
	 */
	public void chooseZiyhqrq2(String ziyhqrq) {
		commonPage.text_ziyhqrq2.click();
		commonPage.opti_ziyhqrq.addXpathLocator("//div[@value='"+ziyhqrq+"']");
		commonPage.opti_ziyhqrq.click();
	}
	
	/**
	 * 选择供货模式
	 * @param jslx 计算类型
	 */
	public void chooseZbcGonghms(String gonghms) {
		commonPage.text_zbcGonghms.click();
		commonPage.opti_zbcGonghms.addXpathLocator("//div[@value='"+gonghms+"']");
		commonPage.opti_zbcGonghms.click();
	}
	
	/***************执行层***********************/
	/**
	 * 选择仓库
	 * @param ckh 仓库号
	 */
	public void chooseCkh(String ckh) {
		commonPage.text_cangkbh.click();
		commonPage.opti_cangkbh.addXpathLocator("//li[@realvalue='"+ckh+"']");
		commonPage.opti_cangkbh.click();
	}
	
	/**
	 * 切换到参考系
	 */
	public void clickQiehckx() {
		commonPage.butt_qiehckx.clickAndWaitForLoad();
	}
	
	/**
	 * 点击执行层菜单
	 */
	public void clickMenuZxc() {
		commonPage.menu_zxc.clickAndWaitForLoad();
	}
	
	/**
	 * 选择要货令类型
	 * @param usercenter 要货令类型
	 */
	public void chooseYaohllx(String yaohllx) {
		commonPage.text_yaohllx.click();
		commonPage.opti_yaohlxl.addXpathLocator("//li[@realvalue='"+yaohllx+"']");
		commonPage.opti_yaohlxl.click();
	}
	
	/**
	 * 选择供货模式
	 * @param gonghms 供货模式
	 */
	public void chooseGonghms(String gonghms) {
		commonPage.text_gonghms.click();
		commonPage.opti_gonghms.addXpathLocator("//li[@realvalue='"+gonghms+"']");
		commonPage.opti_gonghms.click();
	}
	
	/**
	 * 选择是否发送供应商
	 * @param usercenter 是否发送供应商
	 */
	public void chooseShiffsgys(String shiffsgys) {
		commonPage.text_shiffsgys.click();
		commonPage.opti_shiffsgys.addXpathLocator("//div[@id='pop_shiffsgys']/ul/li[@realvalue='"+shiffsgys+"']");
		commonPage.opti_shiffsgys.click();
	}
	
	/**
	 * 执行批处理
	 */
	public void execCommand(String host,String username,String password,String command) {
		Connection connection = new Connection(host);// 创建一个连接实例
		Session sess = null;
		try {
			connection.connect();// Now connect
			boolean isAuthenticated = connection.authenticateWithPassword(username, password);// Authenticate
			if (isAuthenticated == false)throw new IOException("user and password error");
			sess = connection.openSession();// Create a session
			sess.requestPTY("bash");
			sess.startShell();
			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
			PrintWriter out = new PrintWriter(sess.getStdin());
			out.println(command);//命令
			out.println("exit");
			out.close();
			sess.waitForCondition(ChannelCondition.CLOSED|ChannelCondition.EOF | ChannelCondition.EXIT_STATUS,30000);
			System.out.println("下面是从stdout输出:");
			while (true) {
				String line = stdoutReader.readLine();
				if (line == null)break;
				System.out.println(line);
			}
			System.out.println("下面是从stderr输出:");
			while (true) {
				String line = stderrReader.readLine();
				if (line == null)break;
				System.out.println(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
		finally {
			sess.close();/* Close this session */
			connection.close();/* Close the connection */
		}
	}
	
	/**
	 * 从ZBC接口下载文件上传到ZXC接口
	 * @author lizhi
	 * @param downloadFileName 下载的文件名
	 * @param uploadFileName 上传的文件名
	 * 
	 */
	public void downLoadZBCAndUploadZXC(String downloadFileName, String uploadFileName) {
		SFTPUtil sftpUtil = new SFTPUtil();		
		//得到下载的SFTP对象
		ChannelSftp downLoadSFTP
			=sftpUtil.connect(Steup.zbcInterfaceSFTPHost, Steup.zbcInterfaceSFTPport, Steup.zbcInterfaceSFTPName, Steup.zbcInterfaceSFTPpwd);
		uploadFileName = "c:\\"+uploadFileName;
		//下载到本地C盘，文件名为上传名
		sftpUtil.download(Steup.zbcInterfaceSFTPdirectory, downloadFileName, uploadFileName, downLoadSFTP);
		
		//得到上传的SFTP对象
		ChannelSftp uploadSFTP
			=sftpUtil.connect(Steup.zxcInterfaceSFTPHost, Steup.zxcInterfaceSFTPport, Steup.zxcInterfaceSFTPName, Steup.zxcInterfaceSFTPpwd);
		//上传到执行层接口的指定路径
		sftpUtil.upload(Steup.zxcInterfaceSFTPdirectory, uploadFileName, uploadSFTP);
	}
	
	/**
	 * 从ZXC接口下载文件上传到ZBC接口
	 * @author lizhi
	 * @param downloadFileName 下载的文件名
	 * @param uploadFileName 上传的文件名
	 * 
	 */
	public void downLoadZXCAndUploadZBC(String downloadFileName, String uploadFileName) {
		SFTPUtil sftpUtil = new SFTPUtil();		
		//得到下载的SFTP对象
		ChannelSftp downLoadSFTP
			=sftpUtil.connect(Steup.zxcInterfaceSFTPHost, Steup.zxcInterfaceSFTPport, Steup.zxcInterfaceSFTPName, Steup.zxcInterfaceSFTPpwd);
		uploadFileName = "c:\\"+uploadFileName;
		//下载到本地C盘，文件名为上传名
		sftpUtil.download(Steup.zxcInterfaceSFTPdirectory, downloadFileName, uploadFileName, downLoadSFTP);
		
		//得到上传的SFTP对象
		ChannelSftp uploadSFTP
			=sftpUtil.connect(Steup.zbcInterfaceSFTPHost, Steup.zbcInterfaceSFTPport, Steup.zbcInterfaceSFTPName, Steup.zbcInterfaceSFTPpwd);
		//上传到准备层接口的指定路径
		sftpUtil.upload(Steup.zbcInterfaceSFTPdirectory, uploadFileName, uploadSFTP);
	}
	
	/**
	 * 从DDBH接口下载文件上传到ZBC接口
	 * @author lizhi
	 * @param downloadFileName 下载的文件名
	 * @param uploadFileName 上传的文件名
	 * 
	 */
	public void downLoadDDBHAndUploadZBC(String downloadFileName, String uploadFileName) {
		SFTPUtil sftpUtil = new SFTPUtil();		
		//得到下载的SFTP对象
		ChannelSftp downLoadSFTP
			=sftpUtil.connect(Steup.ddbhInterfaceSFTPHost, Steup.ddbhInterfaceSFTPport, Steup.ddbhInterfaceSFTPName, Steup.ddbhInterfaceSFTPpwd);
		uploadFileName = "c:\\"+uploadFileName;
		//下载到本地C盘，文件名为上传名
		sftpUtil.download(Steup.ddbhInterfaceSFTPdirectory, downloadFileName, uploadFileName, downLoadSFTP);
		
		//得到上传的SFTP对象
		ChannelSftp uploadSFTP
			=sftpUtil.connect(Steup.zbcInterfaceSFTPHost, Steup.zbcInterfaceSFTPport, Steup.zbcInterfaceSFTPName, Steup.zbcInterfaceSFTPpwd);
		//上传到准备层接口的指定路径
		sftpUtil.upload(Steup.zbcInterfaceSFTPdirectory, uploadFileName, uploadSFTP);
	}
	
	/*******时间控件(只写了选择当天的办法，其他还没写)********/
	public void chooseToDayTime() {
		commonPage.link_today.click();
	}
}

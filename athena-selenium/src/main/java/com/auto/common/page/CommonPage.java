package com.auto.common.page;
import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;
/**
 * 公用页面，页面相同的元素
 * @author 李智
 * @date 2012-10-13
 */
public class CommonPage extends Page{
	public CommonPage() {
		this.comment = "公用界面";
		this.init();
	}
	
	public TextField text_usercenter = new TextField("用户中心");
	{
		text_usercenter.addXpathLocator("//div[@id='comp_usercenter']/table/tbody/tr/td/input");
	}
	
	public TextField text_usercenter3 = new TextField("查询条件用户中心");
	{
		text_usercenter3.addXpathLocator("//div[@id='gridLayout_usercenter']/table/tbody/tr/td/input");
	}
	
	public TextField text_usercenter4 = new TextField("查询条件用户中心");
	{
		text_usercenter4.addXpathLocator("//div[@id='il_usercenter']/table/tbody/tr/td/input");
	}
	
	public TextField text_usercenter2 = new TextField("卷料订单添加表单的用户中心");
	{
		text_usercenter2.addXpathLocator("//div[@id='kdorder_usercenter']/table/tbody/tr/td/input");
	}
	
	public Element div_UW = new Element("UW");
	{
		div_UW.addAttributeLocator("value", "UW");
	}
	public Element div_UW2 = new Element("UW");
	
	public Element div_UL = new Element("UL");
	{
		div_UL.addAttributeLocator("value", "UL");
	}
	public Element div_UL2 = new Element("UW");
	
	public Element div_UX = new Element("UX");
	{
		div_UX.addAttributeLocator("value", "UX");
	}
	public Element div_UX2 = new Element("UW");
	public TextField text_jislx = new TextField("计算类型");
	{
		text_jislx.addXpathLocator("//div[@id='layout_xuqlx']/table/tbody/tr/td/input");
	}
	public Element opti_jislx = new Element("计算类型选择项");
	
	public TextField text_xuqly = new TextField("需求来源");
	{
		text_xuqly.addXpathLocator("//div[@id='layout_xuqly']/table/tbody/tr/td/input");
	}
	public TextField text_xuqly2 = new TextField("需求来源(另一种xpath)");
	{
		text_xuqly2.addXpathLocator("//div[@id='sel_xuqly']/table/tbody/tr/td/input");
	}
	public TextField text_xuqly3 = new TextField("需求来源(另一种xpath)");
	{
		text_xuqly3.addXpathLocator("//div[@id='gridLayout_xuqly']/table/tbody/tr/td/input");
	}
	public Element opti_xuqly = new Element("需求来源选择项");
	
	public TextField text_ziyhqrq = new TextField("资源获取日期");
	{
		text_ziyhqrq.addXpathLocator("//div[@id='layout_ziyhqrq']/table/tbody/tr/td/input");
	}
	public TextField text_ziyhqrq2 = new TextField("资源获取日期(另一种xpath)");
	{
		text_ziyhqrq2.addXpathLocator("//div[@id='gridLayout_ziyhqrq']/table/tbody/tr/td/input");
	}
	public Element opti_ziyhqrq = new Element("资源获取日期选择项");
	
	public TextField text_zbcGonghms = new TextField("准备层供货模式");
	{
		text_zbcGonghms.addXpathLocator("//div[@id='fieldLayout_gonghmx']/table/tbody/tr/td/input");
	}
	public Element opti_zbcGonghms = new Element("资源获取日期选择项");
	
	
	public Link menu_zbc = new Link("准备层菜单");
	{
		menu_zbc.addXpathLocator("//div[@id='layout-north']/table[2]/tbody/tr/td[2]/span[2]/a");
		menu_zbc.addLinkTextLocator("准备层");
	}
	public Link menu_ckx = new Link("参考系菜单");
	{
		menu_ckx.addLinkTextLocator("参考系");
		menu_ckx.addXpathLocator("//div[@id='sys-menu']/div[1]/span");
	}
	public Link menu_jiccs = new Link("基础参数菜单");
	{
		menu_jiccs.addLinkTextLocator("基础参数");
		menu_jiccs.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/span/a");
	}
	public Link menu_usercenter = new Link("用户中心菜单");
	{
		menu_usercenter.addLinkTextLocator("用户中心");
		menu_usercenter.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li/span/a");
	}
	public Link menu_workshop = new Link("车间菜单");
	{
		menu_workshop.addLinkTextLocator("车间");
		menu_workshop.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[2]/span/a");
	}
	public Link menu_systParameters = new Link("系统参数");
	{
		menu_systParameters.addLinkTextLocator("系统参数");
		menu_systParameters.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[4]/span/a");
	}
	
	public Link menu_systParts = new Link("通用零件");
	{
		menu_systParts.addLinkTextLocator("通用零件");
		menu_systParts.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[5]/span/a");
	}
	
	public Link menu_packages = new Link("包装");
	{
		menu_packages.addLinkTextLocator("包装");
		menu_systParts.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[10]/span/a");
	}
	
	public Link menu_PartsSuppliers = new Link("零件-供应商/仓库-包装");
	{
		menu_PartsSuppliers.addLinkTextLocator("零件-供应商/仓库-包装");
		menu_PartsSuppliers.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[11]/span/a");
	}
	
	public Link menu_suppliers = new Link("供应商");
	{
		menu_suppliers.addLinkTextLocator("供应商|承运商|运输商");
		menu_suppliers.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[8]/span/a");
	}
	
	public Link menu_pointParts = new Link("消耗点-零件");
	{
		menu_pointParts.addLinkTextLocator("消耗点-零件");
		menu_pointParts.addXpathLocator("//div[@id='sys-menu']/div[3]/ul/li/ul/li[13]/span/a");
	}
	
	
	
	
	
	public Link menu_maoxq = new Link("毛需求菜单");
	{
		menu_maoxq.addLinkTextLocator("毛需求");
		menu_maoxq.addXpathLocator("//div[@id='sys-menu']/div[5]/span");
	}
	public Link menu_maoxqbj = new Link("毛需求查询/比较菜单");
	{
		menu_maoxqbj.addLinkTextLocator("毛需求查询/比较");
	}
	public Element menu_kbyhl = new Element("看板及要货令");
	{
		menu_kbyhl.addXpathLocator("//div[@id='sys-menu']/div[17]/span");
		menu_kbyhl.addLinkTextLocator("看板及要货令");
	}
	public Link menu_kbxh = new Link("看板循环");
	{
		menu_kbxh.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/span/a");
	}
	public Link menu_kanbxhjs = new Link("看板循环计算菜单");
	{
		menu_kanbxhjs.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/ul/li[5]/span/a");
		menu_kanbxhjs.addLinkTextLocator("看板循环计算");
	}
	public Link menu_waibxhgmsgsz = new Link("循环规模手工设置(外部)");
	{
		menu_waibxhgmsgsz.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/ul/li[1]/span/a");
		menu_waibxhgmsgsz.addLinkTextLocator("循环规模手工设置(外部)");
	}
	public Link menu_neibxhgmsgsz = new Link("循环规模手工设置(内部)");
	{
		menu_neibxhgmsgsz.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/ul/li[2]/span/a");
		menu_neibxhgmsgsz.addLinkTextLocator("循环规模手工设置(内部)");
	}
	public Link menu_neibkbxhgl = new Link("看板循环管理(内部)");
	{
		menu_neibkbxhgl.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/ul/li[3]/span/a");
		menu_neibkbxhgl.addLinkTextLocator("看板循环管理(内部)");
	}
	public Link menu_waibkbxhgl = new Link("看板循环管理(外部)");
	{
		menu_waibkbxhgl.addXpathLocator("//div[@id='sys-menu']/div[19]/ul/li/ul/li[4]/span/a");
		menu_waibkbxhgl.addLinkTextLocator("看板循环管理(外部)");
	}
	
	public Element menu_dingdjs = new Element("订单计算");
	{
		menu_dingdjs.addXpathLocator("//div[@id='sys-menu']/div[13]/span");
		menu_dingdjs.addLinkTextLocator("订单计算");
	}
	public Element menu_Ilzqdd = new Element("IL周期订单");
	{
		menu_Ilzqdd.addLinkTextLocator("IL周期订单");
	}
	public Element menu_dingdjsForIl = new Element("IL周期订单下订单计算");
	{
		menu_dingdjsForIl.addXpathLocator("//div[@id='sys-menu']/div[15]/ul/li/ul/li[]/span/a");
		menu_dingdjsForIl.addLinkTextLocator("订单计算");
	}
	public Element menu_KDzqdd = new Element("KD周期订单");
	{
		menu_KDzqdd.addLinkTextLocator("KD周期订单");
	}
	public Element menu_dingdjsForKD = new Element("IL周期订单下订单计算");
	{
		menu_dingdjsForKD.addXpathLocator("//div[@id='sys-menu']/div[15]/ul/li[2]/ul/li[4]/span/a");
		menu_dingdjsForKD.addLinkTextLocator("订单计算");
	}
	
	public Element menu_dingddyForKD = new Element("IL周期订单下订单定义");
	{
		menu_dingddyForKD.addXpathLocator("//div[@id='sys-menu']/div[15]/ul/li[2]/ul/li[1]/span/a");
		menu_dingddyForKD.addLinkTextLocator("订单定义");
	}
	
	
	public Element menu_juanldddy = new Element("IL周期订单下订单定义");
	{
		menu_juanldddy.addXpathLocator("//div[@id='sys-menu']/div[20]/ul/li/ul/li[]/span/a");
		menu_juanldddy.addLinkTextLocator("卷料订单定义");
	}
	public Element menu_juanlddjs = new Element("IL周期订单下卷料订单计算");
	{
		menu_juanlddjs.addXpathLocator("//div[@id='sys-menu']/div[21]/ul/li/ul/li[]/span/a");
		menu_juanlddjs.addLinkTextLocator("卷料订单计算");
	}
	
	public Element menu_dingdxgsxForIl = new Element("IL周期订单下订单修改");
	{
		menu_dingdxgsxForIl.addXpathLocator("//div[@id='sys-menu']/div[16]/ul/li/ul/li[]/span/a");
		menu_dingdxgsxForIl.addLinkTextLocator("订单修改/生效");
	}
	public Element menu_dingdxgsxForKD = new Element("KD周期订单下订单修改");
	{
		menu_dingdxgsxForKD.addXpathLocator("//div[@id='sys-menu']/div[15]/ul/li[2]/ul/li[5]/span/a");
		menu_dingdxgsxForKD.addLinkTextLocator("订单修改/生效");
	}
	public Element menu_anxdd = new Element("按需订单");
	{
		menu_anxdd.addLinkTextLocator("按需订单");
	}
	public Element menu_anxcshtjjs = new Element("按需初始化提交计算");
	{
		menu_anxcshtjjs.addLinkTextLocator("初始化提交计算");
	}
	
	/*************执行层*******************/
	public Link menu_zxc = new Link("执行层菜单");
	{
		menu_zbc.addLinkTextLocator("执行层");
	}
	public TextField text_gongnh = new TextField("功能号输入框");
	{
		text_gongnh.addNameLocator("pageId");
	}
	public Element labl_message = new Element("提示消息");
	{
		labl_message.addXpathLocator("//div[@id='prompt']/span");
	}
	
	public Button butt_qiehckx = new Button("切换到参考系");
	{
		butt_qiehckx.addLinkTextLocator("切换到参考系");
	}
	public TextField text_cangkbh = new TextField("仓库");
	{
		text_cangkbh.addNameLocator("cangkbh");
	}
	public Element opti_cangkbh = new Element("仓库选择项");
	
	public TextField text_yaohllx = new TextField("要货令类型");
	{
		text_yaohllx.addNameLocator("nwbflag");
	}
	public Element opti_yaohlxl = new Element("要货令类型选择项");
	
	public TextField text_gonghms = new TextField("供货模式");
	{
		text_gonghms.addNameLocator("gonghms");
	}
	public Element opti_gonghms = new Element("供货模式选择项");
	
	public TextField text_shiffsgys = new TextField("是否发送供应商");
	{
		text_shiffsgys.addNameLocator("shiffsgys");
	}
	public Element opti_shiffsgys = new Element("是否发送供应商选择项");
	
	/******时间控件******/
	public Link link_today = new Link("今天");
	{
		link_today.addLinkTextLocator("今天");
	}
	
}

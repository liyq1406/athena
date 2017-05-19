package com.auto.testcases.page.kanbxh;
import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;

import holmos.webtest.element.Button;
import holmos.webtest.element.Link;
import holmos.webtest.element.RadioButton;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


/**
 * 看板循环界面
 * @author 李智
 */
public class KanbxhjsPage extends Page{
	public Properties R1toR1Properties;
	
	public KanbxhjsPage() {
		this.comment = "看板循环界面";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	public RadioButton radio_maoxq = new RadioButton("选择毛需求");
	{
		radio_maoxq.addXpathLocator("//tr[@id='"+R1toR1Properties.getProperty("kanbxhjs_radioMaoxqbc")+"']/td/input");
	}
	
	public Link link_qr = new Link("确认按钮");
	{
		link_qr.addLinkTextLocator("确认");
	}
	
	public TextField text_usercenter = new TextField("用户中心");
	{
		text_usercenter.addXpathLocator("//div[@id='comp_usercenter']/table/tbody/tr/td/input");
	}
	
	public Button butt_kbjs = new Button("看板计算");
	{
		butt_kbjs.addIDLocator("cfbtn");
		butt_kbjs.addLinkTextLocator("看板计算");
	}
	
}

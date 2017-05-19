package com.auto.testcases.page.kanbxh;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;


/**
 * 循环规模手工设置(外部)
 * @author 李智
 */
public class WaibxhgmsgszPage extends Page{
	public Properties R1toR1Properties;
	
	public WaibxhgmsgszPage() {
		this.comment = "循环规模手工设置(外部)";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	public Element textField1 = new Element("循环规模1");
	public Element textField2 = new Element("循环规模2");
	public Element textField3 = new Element("循环规模3");
	{
		String textXunhgm = R1toR1Properties.getProperty("waibxhgmsgsz_checkLingj");
		String[] textXunhgms = textXunhgm.split(";");
		textField1.addXpathLocator("//tr[@id='"+textXunhgms[0]+"']/td[7]");
		textField2.addXpathLocator("//tr[@id='"+textXunhgms[1]+"']/td[7]");
		textField3.addXpathLocator("//tr[@id='"+textXunhgms[2]+"']/td[7]");
	}
	public TextField text_xunhgm = new TextField("循环规模输入框");
	{
		text_xunhgm.addXpathLocator("//div[@id='field_e_jisxhgm']/input");
	}
	
	public Link link_cx = new Link("查询按钮");
	{
		link_cx.addLinkTextLocator("查 询");
		link_cx.addXpathLocator("//div[@id='kbxh']/div[3]/table/tbody/tr/td/div/div/div/a");
	}
	
	public Link link_cj = new Link("创建按钮");
	{
		link_cj.addLinkTextLocator("创建");
	}
}

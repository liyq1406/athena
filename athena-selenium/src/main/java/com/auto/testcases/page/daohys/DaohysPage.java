package com.auto.testcases.page.daohys;
import holmos.webtest.element.CheckBox;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.util.Properties;

import com.auto.common.Util;


/**
 * 到货验收界面
 * @author 李智
 */
public class DaohysPage extends Page{
	public Properties R1toR1Properties;
	
	public DaohysPage() {
		this.comment = "到货验收界面";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	public TextField text_blh = new TextField("BL号输入框");
	{
		text_blh.addNameLocator("blh");
	}
	
	public CheckBox check_blh = new CheckBox("选择BL号");
	{
		check_blh.addXpathLocator("//div[@class='grid-content']/table/tbody/tr[2]/td[3]/input");
	}
	
	public Element labl_uth = new Element("UT号文本");
	{
		labl_uth.addXpathLocator("//div[@id='uth']/span");
	}
	
	public Element labl_messgae = new Element("提示消息");
	{
		labl_messgae.addXpathLocator("//div[@id='prompt']/span");
	}
}

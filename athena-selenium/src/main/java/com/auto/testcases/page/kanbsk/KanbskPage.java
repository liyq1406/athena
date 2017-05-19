package com.auto.testcases.page.kanbsk;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;


/**
 * 看板扫卡界面
 * @author 李智
 */
public class KanbskPage extends Page{
	public Properties R1toR1Properties;
	
	public KanbskPage() {
		this.comment = "看板扫卡界面";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	public TextField text_yaohlh = new TextField("要货令号");
	{
		text_yaohlh.addNameLocator("yaohlh");
	}
	
	public Element labl_messgae = new Element("提示消息");
	{
		labl_messgae.addXpathLocator("//div[@id='prompt']/span");
	}
}

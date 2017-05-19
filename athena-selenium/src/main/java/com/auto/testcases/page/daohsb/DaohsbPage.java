package com.auto.testcases.page.daohsb;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;


/**
 * 到货申报界面
 * @author 李智
 */
public class DaohsbPage extends Page{
	public Properties R1toR1Properties;
	
	public DaohsbPage() {
		this.comment = "到货申报界面";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	public TextField text_blh = new TextField("BL号");
	{
		text_blh.addNameLocator("blh");
	}
}

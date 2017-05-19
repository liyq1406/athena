package com.auto.testcases.page.chuanjlsyhl;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.Util;


/**
 * 创建临时要货令界面
 * @author 李智
 */
public class ChuanjlsyhlPage extends Page{
	public Properties R1toR1Properties;
	
	public ChuanjlsyhlPage() {
		this.comment = "创建临时要货令界面";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	
	public TextField text_xunhbm = new TextField("看板循环编码");
	{
		text_xunhbm.addNameLocator("xunhbm");
	}
	
	public TextField text_yaohsl = new TextField("要货数量");
	{
		text_yaohsl.addNameLocator("yaohsl");
	}
	
	public TextField text_lingjbh = new TextField("零件编号");
	{
		text_lingjbh.addNameLocator("lingjbh");
	}
	
	public TextField text_mudd = new TextField("目的地");
	{
		text_mudd.addNameLocator("mudd");
	}
}

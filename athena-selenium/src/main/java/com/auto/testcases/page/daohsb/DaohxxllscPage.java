package com.auto.testcases.page.daohsb;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


/**
 * 到货信息录入删除界面
 * @author 李智
 */
public class DaohxxllscPage extends Page{

	public DaohxxllscPage() {
		this.comment = "到货信息录入删除界面";
		this.init();
	}
	
	public TextField text_uth = new TextField("UT号输入框");
	{
		text_uth.addNameLocator("uth");
	}
}

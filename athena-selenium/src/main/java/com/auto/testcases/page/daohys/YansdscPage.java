package com.auto.testcases.page.daohys;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


/**
 * 验收单删除界面
 * @author 李智
 */
public class YansdscPage extends Page{

	public YansdscPage() {
		this.comment = "验收单删除界面";
		this.init();
	}
	
	public TextField text_uth = new TextField("UT号输入框");
	{
		text_uth.addNameLocator("uth");
	}
	
	public TextField text_shancyy = new TextField("删除原因");
	{
		text_shancyy.addNameLocator("shancyy");
	}
}

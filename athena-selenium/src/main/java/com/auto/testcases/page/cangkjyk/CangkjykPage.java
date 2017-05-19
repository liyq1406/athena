package com.auto.testcases.page.cangkjyk;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


/**
 * 仓库间移库界面
 * @author 李智
 */
public class CangkjykPage extends Page{	
	public CangkjykPage() {
		this.comment = "仓库间移库界面";
		this.init();
	}
	
	public TextField text_lingjbh = new TextField("零件编号");
	{
		text_lingjbh.addNameLocator("lingjbh");
	}
	
	public TextField text_jiesckbh = new TextField("接受仓库");
	{
		text_jiesckbh.addNameLocator("jiesckbh");
	}
}

package com.auto.common.page;
import holmos.webtest.element.Button;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

/**
 * 接口界面
 * @author 李智
 * @date 2012-10-13
 */
public class InterfacePage extends Page{
	public InterfacePage() {
		this.comment = "接口界面";
		this.init();
	}
	
	public Button butt_goto = new Button("确认按钮");
	{
		butt_goto.addIDLocator("_button");
		butt_goto.addLinkTextLocator("确认");
	}
	
	public TextField text_interfaceId = new TextField("接口ID输入框");
	{
		text_interfaceId.addIDLocator("zz");
		text_interfaceId.addNameLocator("zz");
	}
}

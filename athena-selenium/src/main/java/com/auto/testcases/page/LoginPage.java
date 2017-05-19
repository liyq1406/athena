package com.auto.testcases.page;
import holmos.webtest.element.Button;
import holmos.webtest.element.Comobobox;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


public class LoginPage extends Page{
	public LoginPage() {
		this.comment = "登陆界面";
		this.init();
	}
	
	public Button butt_login = new Button("登陆按钮");
	{
		butt_login.addIDLocator("loginButton");
		butt_login.addLinkTextLocator("登陆");
	}
	
	public TextField text_name = new TextField("用户名输入框");
	{
		text_name.addIDLocator("username");
		text_name.addNameLocator("username");
	}
	
	public TextField text_pwd = new TextField("密码输入框");
	{
		text_pwd.addIDLocator("password");
		text_pwd.addNameLocator("password");
	}
	
	public Comobobox como_usercenter = new Comobobox("用户中心");
	{
		como_usercenter.addIDLocator("agencyId");
		como_usercenter.addNameLocator("agencyId");
	}
	
	public Link link_logout = new Link("安全退出");
	{
		link_logout.addLinkTextLocator("安全退出");
	}
}

package com.auto;
import holmos.webtest.element.Button;
import holmos.webtest.element.Comobobox;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;


public class AthenaPage extends Page{
	public AthenaPage() {
		this.comment = "登陆界面";
		this.init();
	}
	
	public Button loginBut = new Button("登陆按钮");
	{
		loginBut.addIDLocator("loginButton");
		loginBut.addLinkTextLocator("登陆");
	}
	
	public TextField nameText = new TextField("用户名输入框");
	{
		nameText.addIDLocator("username");
		nameText.addNameLocator("username");
	}
	
	public TextField pwdTest = new TextField("密码输入框");
	{
		pwdTest.addIDLocator("password");
		pwdTest.addNameLocator("password");
	}
	
	public Comobobox usercenterCom = new Comobobox("用户中心");
	{
		usercenterCom.addIDLocator("agencyId");
		usercenterCom.addNameLocator("agencyId");
	}
}

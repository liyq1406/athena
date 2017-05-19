package com.auto.testcases.page.anxzqdd;
import holmos.webtest.element.Link;
import holmos.webtest.struct.Page;


/**
 * 按需初始化计算
 * @author 李智
 */
public class AnxcshjsPage extends Page{
	
	public AnxcshjsPage() {
		this.comment = "按需初始化计算";
		this.init();
	}
	
	public Link link_tjjs = new Link("提交计算按钮");
	{
		link_tjjs.addLinkTextLocator("提交计算");
	}
}

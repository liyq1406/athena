package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

public class UsercenterPage extends Page{
	public UsercenterPage() {
		this.comment = "用户中心";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_save = new Button("保存");
	public Button butt_add = new Button("添加");
	public Button butt_delete = new Button("删除");
	public TextField text_usercenterID = new TextField("用户中心编号");
	public TextField text_usercenterName = new TextField("用户中心名称");
	
	public SearchUsercenter list_usercenters = new SearchUsercenter("查询结果");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_save.addXpathLocator("//div[@id='button_save']/div/div/a");
		butt_add.addXpathLocator("//div[@id='editorAdd']");
		butt_delete.addXpathLocator("//div[@id='editorRemove']");
		text_usercenterID.addXpathLocator("//div[@id='field_e_usercenter']/input");
		text_usercenterName.addXpathLocator("//div[@id='field_e_centername']/input");
		list_usercenters.addXpathLocator("//div[@id='grid_usercenter']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchUsercenter extends Collection {

		public SearchUsercenter(String comment) {
			super(comment);
		}
		public Element text_usercenterID = new Element("用户中心编号");
		public Element text_usercenterName = new Element("用户中心名称");
		{
			text_usercenterID.addXpathLocator("./td[1]");
			text_usercenterName.addXpathLocator("./td[2]");
		}
	}
}

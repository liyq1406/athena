package com.auto.testcases.page.maoxq;
import com.auto.testcases.page.kanbxh.NeibkbxhglPage.SearchKanbxh;

import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * 毛需求查询/比较界面
 * @author 李智
 */
public class MaoxqcxbjPage extends Page{
	
	public MaoxqcxbjPage() {
		this.comment = "毛需求查询/比较界面";
		this.init();
	}
	
	public TextField text_xuqbc = new TextField("需求版次");
	{
		text_xuqbc.addXpathLocator("//div[@id='sel_xuqbc']/table/tbody/tr/td/input");
	}
	
	public TextField text_xuh = new TextField("版次流水号");
	{
		text_xuh.addNameLocator("xuh");
	}
	
	public Link link_cx = new Link("查询按钮");
	{
		link_cx.addLinkTextLocator("查 询");
	}
	
	public SearchMaoxq maoxqs = new SearchMaoxq("查询结果");
	{
		maoxqs.addXpathLocator("//div[@id='grid_maoxq']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchMaoxq extends Collection {

		public SearchMaoxq(String comment) {
			super(comment);
		}
	}
}

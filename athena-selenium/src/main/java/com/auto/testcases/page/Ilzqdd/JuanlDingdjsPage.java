package com.auto.testcases.page.Ilzqdd;
import holmos.webtest.element.Button;
import holmos.webtest.element.CheckBox;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * 卷料订单计算
 * @author 李智
 */
public class JuanlDingdjsPage extends Page{
	
	public JuanlDingdjsPage() {
		this.comment = "卷料订单计算";
		this.init();
	}
	public CheckBox check_quanx = new CheckBox("订单全选");
	
	{
		check_quanx.addXpathLocator("//div[@id='grid_dingdh']/div[3]/table/tbody/tr/td/div/table/tbody/tr[2]/th[2]/div");
	}
	
	public Link link_qd = new Link("确认按钮");
	{
		link_qd.addLinkTextLocator("确定");
	}
	
	public Link link_scdd = new Link("生成订单按钮");
	{
		link_scdd.addLinkTextLocator("生成订单");
	}
	
	public Button butt_last= new Button("最后页");
	{
		butt_last.addClassLocator("pLast pButton disabled");
	}
	
	public SearchMaoxq maoxqs = new SearchMaoxq("查询结果");
	{
		maoxqs.addXpathLocator("//div[@id='grid_maoxqo']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchMaoxq extends Collection {

		public SearchMaoxq(String comment) {
			super(comment);
		}
		public Element labl_xuqbc = new Element("查询结果中的需求版次");
		{
			labl_xuqbc.addXpathLocator("./td[2]");
		}
	}
	
}

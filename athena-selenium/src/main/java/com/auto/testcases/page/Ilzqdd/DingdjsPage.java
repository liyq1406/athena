package com.auto.testcases.page.Ilzqdd;
import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;
import com.auto.testcases.page.maoxq.MaoxqcxbjPage.SearchMaoxq;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.RadioButton;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * Il订单计算
 * @author 李智
 */
public class DingdjsPage extends Page{
	public Properties PPToMDToMDProperties;
	
	public DingdjsPage() {
		this.comment = "Il订单计算";
		this.init();
	}
	{
		PPToMDToMDProperties = Util.propertiesUrl("PPtoMDtoMD");
	}
	
	
	public Element radio_maoxq = new Element("选择毛需求");
	{
		radio_maoxq.addXpathLocator("//tr[@id='"+PPToMDToMDProperties.getProperty("ILDingdjs_radioMaoxqbc")+"']/td");
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
		maoxqs.addXpathLocator("//div[@id='maoxqGrid']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr");
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

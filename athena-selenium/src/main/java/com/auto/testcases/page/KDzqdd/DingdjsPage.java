package com.auto.testcases.page.KDzqdd;
import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

import java.util.Properties;

import com.auto.common.Util;


/**
 * KD订单计算
 * @author 李智
 */
public class DingdjsPage extends Page{
	public Properties PPtoRMtoRDProperties;
	
	public DingdjsPage() {
		this.comment = "KD订单计算";
		this.init();
	}
	{
		PPtoRMtoRDProperties = Util.propertiesUrl("PPtoRMtoRD");
	}
	
	public TextField text_dingd = new TextField("订单号选择");
	public Element div_dingdh = new Element("选择的订单");
	public Link link_qd = new Link("确认按钮");
	public Link link_scdd = new Link("生成订单按钮");
	public Button butt_last= new Button("最后页");
	public SearchMaoxq maoxqs = new SearchMaoxq("查询结果");
	{
		text_dingd.addXpathLocator("//div[@id='layout_dingdh']/table/tbody/tr/td/input");
		div_dingdh.addXpathLocator("//div[@id='field-pop-layout_dingdh']/div[2]");
		link_qd.addLinkTextLocator("确定");
		link_scdd.addLinkTextLocator("生成订单");
		butt_last.addClassLocator("pLast pButton disabled");
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

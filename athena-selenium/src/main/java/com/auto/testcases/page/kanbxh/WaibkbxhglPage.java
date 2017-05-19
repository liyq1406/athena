package com.auto.testcases.page.kanbxh;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.Table;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;


/**
 * 看板循环管理(外部)
 * @author 李智
 */
public class WaibkbxhglPage extends Page{
	public Properties R1toR1Properties;
	
	public WaibkbxhglPage() {
		this.comment = "看板循环管理(外部)";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}	
	public Link link_cx = new Link("查询按钮");
	{
		link_cx.addLinkTextLocator("查 询");
		link_cx.addXpathLocator("//div[@id='kbxh']/div[3]/table/tbody/tr/td/div/div/div/a");
	}
	
	public Link link_sx = new Link("生效按钮");
	{
		link_sx.addLinkTextLocator("生效");
	}
	
	public Table kanbxhsTable = new Table(""); 
	{
		kanbxhsTable.addXpathLocator("//div[@id='kbxh']/div[5]/table/tbody/tr/td[2]/div/table/tbody");
	}
	
	public SearchKanbxh kanbxhs = new SearchKanbxh("查询结果");
	{
		kanbxhs.addXpathLocator("//div[@id='kbxh']/div[5]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchKanbxh extends Collection {

		public SearchKanbxh(String comment) {
			super(comment);
		}
		public Element labl_xuhbm = new Element("查询结果中的循环编码");
		{
			labl_xuhbm.addXpathLocator("./td[1]");
		}
		public Element labl_lingjbh = new Element("查询结果中的零件编码");
		{
			labl_lingjbh.addXpathLocator("./td[2]");
		}
	}
}

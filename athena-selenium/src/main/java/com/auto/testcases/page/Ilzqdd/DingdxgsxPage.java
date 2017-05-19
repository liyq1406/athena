package com.auto.testcases.page.Ilzqdd;
import holmos.webtest.element.CheckBox;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

import java.util.Properties;


/**
 * Il订单修改/生效
 * @author 李智
 */
public class DingdxgsxPage extends Page{
	
	public DingdxgsxPage() {
		this.comment = "Il订单修改/生效";
		this.init();
	}

	public TextField text_gongysdm = new TextField("供应商查询条件");
	{
		text_gongysdm.addNameLocator("gongysdm");
	}
	
	public TextField text_dingdlx = new TextField("订单类型");
	{
		text_dingdlx.addXpathLocator("//div[@id='il_dingdlx']/table/tbody/tr/td/input");
	}
	
	public Element div_dingdlx_IL = new Element("IL订单类型");
	public Element div_dingdlx_AX = new Element("按需订单类型");
	public Element div_dingdlx_JL = new Element("卷料订单类型");
	{
		div_dingdlx_IL.addXpathLocator("//div[@id='field-pop-il_dingdlx']/div[1]");
		div_dingdlx_AX.addXpathLocator("//div[@id='field-pop-il_dingdlx']/div[2]");
		div_dingdlx_JL.addXpathLocator("//div[@id='field-pop-il_dingdlx']/div[3]");
	}
	
	public SearchDingds list_dingds = new SearchDingds("订单查询结果");
	{
		list_dingds.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchDingds extends Collection {

		public SearchDingds(String comment) {
			super(comment);
		}
		
		public Element labl_dingdh = new Element("查询结果中的订单号");
		{
			labl_dingdh.addXpathLocator("./td[1]/a");
		}
		
		public Element labl_gys = new Element("查询结果中的供应商");
		{
			labl_gys.addXpathLocator("./td[5]");
		}
		
	}
	
	public Link dingd1 = new Link("第1条订单链接");
	{
		dingd1.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[1]/a");
	}
	
	public Link dingd2 = new Link("第2条订单链接");
	{
		dingd2.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[3]/td[1]/a");
	}
	
	public Link dingd3 = new Link("第3条订单链接");
	{
		dingd3.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[4]/td[1]/a");
	}
	
	public Link dingd4 = new Link("第4条订单链接");
	{
		dingd4.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[5]/td[1]/a");
	}
	
	public Link dingd5 = new Link("第5条订单链接");
	{
		dingd5.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[6]/td[1]/a");
	}
	
	public Link dingd6 = new Link("第6条订单链接");
	{
		dingd6.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[7]/td[1]/a");
	}
	
	public Link dingd7 = new Link("第7条订单链接");
	{
		dingd7.addXpathLocator("//div[@id='ilorderEe']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[8]/td[1]/a");
	}
	
	public TextField text_zhizsjstart = new TextField("制作时间的开头");
	{
		text_zhizsjstart.addNameLocator("dingdjssjFrom");
	}
	
	public TextField text_zhizsjend = new TextField("制作时间的结束");
	{
		text_zhizsjend.addNameLocator("dingdjssjTo");
	}
	
	public CheckBox check_quanx = new CheckBox("全选");
	{
		check_quanx.addXpathLocator("//div[@class='table-header-fixed']/table/tbody/tr[2]/th[2]/div");
	}
	
	public Link link_cx = new Link("查询按钮");
	{
		link_cx.addLinkTextLocator("查 询");
	}
	public Link link_dsx = new Link("待生效按钮");
	{
		link_dsx.addLinkTextLocator("待生效");
	}
	public Link link_sx = new Link("生效按钮");
	{
		link_sx.addLinkTextLocator("生效");
	}
	
}

package com.auto.testcases.page.KDzqdd;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * KD订单修改/生效
 * @author 李智
 */
public class DingdxgsxPage extends Page{
	
	public DingdxgsxPage() {
		this.comment = "KD订单修改/生效";
		this.init();
	}

	public TextField text_dingdlx = new TextField("订单类型");
	public Element div_dingdlx_KD = new Element("KD订单类型");
	public Element div_dingdlx_AX = new Element("爱信订单类型");
	public SearchDingds list_dingds = new SearchDingds("订单查询结果");
	public Link dingd1 = new Link("第1条订单链接");
	public Link link_cx = new Link("查询按钮");
	
	{
		text_dingdlx.addXpathLocator("//div[@id='kd_dingdlx']/table/tbody/tr/td/input");
		div_dingdlx_KD.addXpathLocator("//div[@id='field-pop-kd_dingdlx']/div[1]");
		div_dingdlx_AX.addXpathLocator("//div[@id='field-pop-kd_dingdlx']/div[2]");
		list_dingds.addXpathLocator("//div[@id='grid_kdorder']/div[5]/table/tbody/tr/td[2]/div/table/tbody/tr");
		dingd1.addXpathLocator("//div[@id='grid_kdorder']/div[5]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[2]/a");
		link_cx.addLinkTextLocator("查 询");
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
}

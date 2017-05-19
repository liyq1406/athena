package com.auto.testcases.page.Ilzqdd;
import holmos.webtest.element.Element;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * Il订单明细
 * @author 李智
 */
public class DingdmxPage extends Page{
	
	public DingdmxPage() {
		this.comment = "Il订单明细";
		this.init();
	}
	
	public SearchDingdmxs list_dingdmxs = new SearchDingdmxs("订单明细查询结果");
	{
		list_dingdmxs.addXpathLocator("//div[@id='ilorderEe']/div[6]/div/table/tbody/tr");
	}
	
	public Element labl_p0rq = new Element("p0日期");
	{
		labl_p0rq.addXpathLocator("//div[@id='ilorderEe']/div[2]/div");
	}
	
	public class SearchDingdmxs extends Collection {

		public SearchDingdmxs(String comment) {
			super(comment);
		}
		
		public Element labl_lingjh = new Element("查询结果中的零件号");
		{
			labl_lingjh.addXpathLocator("./td[5]");
		}
		
		public Element labl_cangk = new Element("查询结果中的仓库");
		{
			labl_cangk.addXpathLocator("./td[6]");
		}
		
		public Element labl_gys = new Element("查询结果中的供应商");
		{
			labl_gys.addXpathLocator("./td[4]");
		}
		
		public Element labl_gonghms = new Element("供货模式");
		{
			labl_gonghms.addXpathLocator("./td[8]");
		}
		
		public Element labl_p0 = new Element("p0");
		{
			labl_p0.addXpathLocator("./td[9]");
		}
		
		public Element labl_p1 = new Element("p1");
		{
			labl_p1.addXpathLocator("./td[10]");
		}
		
		public Element labl_p2 = new Element("p2");
		{
			labl_p2.addXpathLocator("./td[11]");
		}
		
		public Element labl_p3 = new Element("p3");
		{
			labl_p3.addXpathLocator("./td[12]");
		}
	}
}

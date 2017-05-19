package com.auto.testcases.page.KDzqdd;
import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * KD AX订单定义
 * @author 李智
 */
public class DingddyPage extends Page{
	
	public DingddyPage() {
		this.comment = "订单定义";
		this.init();
	}
	
	public Link link_cx = new Link("查询按钮");
	public Button butt_zj = new Button("增加订单按钮");
	public TextField text_dingdh = new TextField("订单号"); 
	public TextField text_dingdlx = new TextField("订单类型");
	public Element opti_dingdlx = new Element("订单类型选择项");
	public TextField text_gongysdm = new TextField("供应商代码");
	public Element opti_gongysdm = new Element("供应商代码选择项");
	public TextField text_dingdzt = new TextField("订单状态");
	public Element opti_dingdzt = new Element("订单状态选择项");
	public TextField text_jiszq = new TextField("计算周期");
	public TextField text_fahzq = new TextField("发运周期");
	public Button butt_tj = new Button("提交添加订单按钮");
	public SearchDingds list_dingds = new SearchDingds("订单查询结果");
	public TextField text_dingdlxForSearch = new TextField("查询条件中订单类型");
	
	public class SearchDingds extends Collection {
		public SearchDingds(String comment) {
			super(comment);
		}
	}
	
	{
		link_cx.addLinkTextLocator("查 询");
		butt_zj.addLinkTextLocator("添加订单");
		text_dingdh.addXpathLocator("//div[@id='kdorder_dingdh']/input");
		text_dingdlx.addXpathLocator("//div[@id='kdorder_dingdlx']/table/tbody/tr/td/input");
		text_gongysdm.addXpathLocator("//div[@id='kdorder_gongysdm']/table/tbody/tr/td/input");
		text_dingdzt.addXpathLocator("//div[@id='kd_dingdzt']/table/tbody/tr/td/input");
		text_jiszq.addXpathLocator("//div[@id='kdorder_jiszq']/input");
		text_fahzq.addXpathLocator("//div[@id='kdorder_fahzq']/input");
		butt_tj.addXpathLocator("//div[@id='button_btnSubmit']/div/div/a");
		list_dingds.addXpathLocator("//div[@id='grid_kdorder']/div[5]/table/tbody/tr/td[2]/div/table/tbody/tr");
		text_dingdlxForSearch.addXpathLocator("//div[@id='kd_dingdlx']/table/tbody/tr/td/input");
	}
}

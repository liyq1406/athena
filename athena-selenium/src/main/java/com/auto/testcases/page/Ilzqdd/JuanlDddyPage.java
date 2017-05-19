package com.auto.testcases.page.Ilzqdd;
import com.auto.testcases.page.Ilzqdd.DingdxgsxPage.SearchDingds;

import holmos.webtest.element.Button;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;


/**
 * 卷料订单定义
 * @author 李智
 */
public class JuanlDddyPage extends Page{
	
	public JuanlDddyPage() {
		this.comment = "卷料订单定义";
		this.init();
	}
	
	public Link link_cx = new Link("查询按钮");
	public Button butt_zj = new Button("增加订单按钮");
	public TextField text_dingdh = new TextField("订单号"); 
	public TextField text_gongysdm = new TextField("供应商代码");
	public TextField text_jiszq = new TextField("计算周期");
	public Button butt_tj = new Button("提交订单按钮");
	public SearchDingds list_dingds = new SearchDingds("订单查询结果");
	public class SearchDingds extends Collection {
		public SearchDingds(String comment) {
			super(comment);
		}
	}
	
	{
		link_cx.addLinkTextLocator("查 询");
		butt_zj.addLinkTextLocator("增 加");
		text_dingdh.addXpathLocator("//div[@id='kdorder_dingdh']/input");
		text_gongysdm.addNameLocator("gongysdm");
		text_jiszq.addNameLocator("jiszq");
		butt_tj.addXpathLocator("//form[@id='form_ildingddy']/div[2]/table/tbody/tr/td/div/div/div/a");
		list_dingds.addXpathLocator("//div[@id='grid_ildingddy']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
}

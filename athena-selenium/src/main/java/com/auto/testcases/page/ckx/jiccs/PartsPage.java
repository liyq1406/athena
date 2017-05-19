package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

public class PartsPage extends Page{
	public PartsPage() {
		this.comment = "零件";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_edit = new Button("修改");
	public Button butt_valid = new Button("有效");
	public Button butt_invalid = new Button("失效");
	public Button butt_submit = new Button("提交");
	public TextField text_mark = new TextField("标识");
	public Element opti_mark = new Element("有效");
	public Element opti_mark2 = new Element("无效");
	public SearchParts list_parts = new SearchParts("查询结果");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_edit.addXpathLocator("//div[@id='button_edit']/div/div/a");
		butt_valid.addXpathLocator("//div[@id='button_youx']/div/div/a");
		butt_invalid.addXpathLocator("//div[@id='button_del']/div/div/a");
		butt_submit.addXpathLocator("//form[@id='form_lingj']/div[2]/table/tbody/tr/td/div/div/div/a");
		text_mark.addXpathLocator("//div[@id='record_biaos']/table/tbody/tr/td/input");
		opti_mark.addXpathLocator("//div[@id='field-pop-record_biaos']/div[2]");
		opti_mark2.addXpathLocator("//div[@id='field-pop-record_biaos']/div[3]");
		list_parts.addXpathLocator("//div[@id='grid_lingj']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	public class SearchParts extends Collection {

		public SearchParts(String comment) {
			super(comment);
		}
		public Element text_usercenter = new Element("用户用心");
		{
			text_usercenter.addXpathLocator("./td[1]");
		}
		public Element text_mark = new Element("标识");
		{
			text_mark.addXpathLocator("./td[21]");
		}
	}
}

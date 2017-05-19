package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

public class PackagesPage extends Page{
	public PackagesPage() {
		this.comment = "包装";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_save = new Button("保存");
	public TextField text_conditionMark = new TextField("标识");
	public Element opti_conditionMark = new Element("有效");
	public Element opti_conditionMark2 = new Element("无效");
	
	public TextField text_mark = new TextField("标识");
	public Element opti_mark = new Element("有效");
	public Element opti_mark2 = new Element("无效");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_save.addXpathLocator("//div[@id='button_save']/div/div/a");
		text_conditionMark.addXpathLocator("//div[@id='baoz_biaos']/table/tbody/tr/td/input");
		opti_conditionMark.addXpathLocator("//div[@id='field-pop-baoz_biaos']/div[2]");
		opti_conditionMark2.addXpathLocator("//div[@id='field-pop-baoz_biaos']/div[3]");
		
		text_mark.addXpathLocator("//div[@id='grid_baoz']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[13]");
		opti_mark.addXpathLocator("//div[@id='field-pop-field_e_biaos']/div[1]");
		opti_mark2.addXpathLocator("//div[@id='field-pop-field_e_biaos']/div[2]");
	}
}

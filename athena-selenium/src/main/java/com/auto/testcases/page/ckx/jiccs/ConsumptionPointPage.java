package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

public class ConsumptionPointPage extends Page{
	public ConsumptionPointPage() {
		this.comment = "工艺消耗点";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_save = new Button("保存");
	public TextField text_conditionCraftsMark = new TextField("工艺标识");
	public Element opti_conditionCraftsMark = new Element("有效");
	public Element opti_conditionCraftsMark2 = new Element("无效");
	public TextField text_conditionMark = new TextField("标识");
	public Element opti_conditionMark = new Element("有效");
	public Element opti_conditionMark2 = new Element("无效");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_save.addXpathLocator("//div[@id='button_save']/div/div/a");
		text_conditionCraftsMark.addXpathLocator("//div[@id='gongyxhd_gongybs']/table/tbody/tr/td/input");
		opti_conditionCraftsMark.addXpathLocator("//div[@id='field-pop-gongyxhd_gongybs']/div[2]");
		opti_conditionCraftsMark2.addXpathLocator("//div[@id='field-pop-gongyxhd_gongybs']/div[3]");
		text_conditionMark.addXpathLocator("//div[@id='gongyxhd_biaos']/table/tbody/tr/td/input");
		opti_conditionMark.addXpathLocator("//div[@id='field-pop-gongyxhd_biaos']/div[2]");
		opti_conditionMark2.addXpathLocator("//div[@id='field-pop-gongyxhd_biaos']/div[3]");
	}
}

package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

public class WorkshopPage extends Page{
	public WorkshopPage() {
		this.comment = "车间";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_save = new Button("保存");
	public Button butt_add = new Button("添加");
	public Button butt_delete = new Button("删除");
	public TextField text_workshopID = new TextField("车间编码");
	public TextField text_workshopName = new TextField("车间名称");
	public TextField text_remark = new TextField("备注");
	
	public SearchWorkshop list_workshops = new SearchWorkshop("查询结果");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_save.addXpathLocator("//div[@id='button_save']/div/div/a");
		butt_add.addXpathLocator("//div[@id='editorAdd']");
		butt_delete.addXpathLocator("//div[@id='editorRemove']");
		text_workshopID.addXpathLocator("//div[@id='field_e_chejbm']/input");
		text_workshopName.addXpathLocator("//div[@id='field_e_chejmc']/input");
		text_remark.addXpathLocator("//div[@id='field_e_beiz']/input");
		list_workshops.addXpathLocator("//div[@id='grid_chej']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr");
	}
	
	public class SearchWorkshop extends Collection {

		public SearchWorkshop(String comment) {
			super(comment);
		}
		public Element text_workshopID = new Element("车间编码");
		public Element text_workshopName = new Element("车间名称");
		public Element text_remark = new Element("备注");
		{
			text_workshopID.addXpathLocator("./td[2]");
			text_workshopName.addXpathLocator("./td[3]");
			text_remark.addXpathLocator("./td[4]");
		}
	}
}

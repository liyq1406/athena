package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Collection;
import holmos.webtest.struct.Page;

public class SystParametersPage extends Page{
	public SystParametersPage() {
		this.comment = "系统参数";
		this.init();
	}
	
	public TextField text_dictType = new TextField("字典类型");
	public Element opti_dictType = new Element("字典类型选项");
	public Button butt_search = new Button("查询");
	public Button butt_edit = new Button("修改");
	public Element labl_dictType = new Element("字典类型列表labl");
	
	public Button butt_add = new Button("增加");
	public Button butt_delete = new Button("删除");
	public Button butt_save = new Button("提交");
	public Button butt_close = new Button("关闭");
	
	public TextField text_dictID = new TextField("字典编码");
	public TextField text_dictName = new TextField("字典名称");
	public TextField text_min = new TextField("区间min值");
	public TextField text_max = new TextField("区间max值");
	public TextField text_sequence = new TextField("排序");
	
	public Element opti_qj = new Element("区间选项");
	
	public SearchDict list_dicts = new SearchDict("查询结果");
	public Element labl_dictID3 = new Element("第三列");
	
	{
		text_dictType.addXpathLocator("//div[@id='xitcsdy_zidlxmc']/table/tbody/tr/td/input");
		opti_dictType.addXpathLocator("//div[@id='field-pop-xitcsdy_zidlxmc']/div[2]");
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_edit.addXpathLocator("//div[@id='button_edit']/div/div/a");
		labl_dictType.addXpathLocator("//div[@id='grid_xitcsdy']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td");
		
		butt_add.addXpathLocator("//div[@id='editorAdd']");
		butt_delete.addXpathLocator("//div[@id='editorRemove']");
		butt_save.addXpathLocator("//div[@id='button_submit_zdy']/div/div/a");
		butt_close.addXpathLocator("//div[@id='button_close']/div/div/a");
		
		text_dictID.addXpathLocator("//div[@id='field_e_zidbm']/input");
		text_dictName.addXpathLocator("//div[@id='field_e_zidmc']/input");
		text_min.addXpathLocator("//div[@id='field_e_qujzxz']/input");
		text_max.addXpathLocator("//div[@id='field_e_qujzdz']/input");
		text_sequence.addXpathLocator("//div[@id='field_e_paix']/input");
		
		opti_qj.addXpathLocator("//div[@id='field-pop-field_e_shifqj']/div[2]");
		
		list_dicts.addXpathLocator("//div[@id='zidlist']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr");
		labl_dictID3.addXpathLocator("//div[@id='zidlist']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr[4]/td");
	}
	
	public class SearchDict extends Collection {

		public SearchDict(String comment) {
			super(comment);
		}
		public TextField text_dictID = new TextField("字典编码");
		public TextField text_dictName = new TextField("字典名称");
		public TextField text_qj = new TextField("是否区间");
		public TextField text_min = new TextField("区间min值");
		public TextField text_max = new TextField("区间max值");
		public TextField text_sequence = new TextField("排序");
		{
			text_dictID.addXpathLocator("./td[1]");
			text_dictName.addXpathLocator("./td[2]");
			text_qj.addXpathLocator("./td[4]");
			text_min.addXpathLocator("./td[5]");
			text_max.addXpathLocator("./td[6]");
			text_sequence.addXpathLocator("./td[7]");
		}
	}
}

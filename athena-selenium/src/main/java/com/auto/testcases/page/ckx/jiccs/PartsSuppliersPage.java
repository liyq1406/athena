package com.auto.testcases.page.ckx.jiccs;

import holmos.webtest.element.Button;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

public class PartsSuppliersPage extends Page{
	public PartsSuppliersPage() {
		this.comment = "零件-供应商";
		this.init();
	}
	
	public Button butt_search = new Button("查询");
	public Button butt_saveAll = new Button("保存");
	public Element labl_ucPackages = new Element("供应商UC的包装类型");
	public Element labl_ucCounts = new Element("供应商UC的容量");
	public Element labl_uaPackages = new Element("供应商UA的包装类型");
	public Element labl_ucInuaCounts = new Element("供应商UA里UC的个数");
	public Element labl_uaCounts = new Element("供应商UA的容量");
	
	public TextField text_ucPackages = new TextField("供应商UC的包装类型");
	public TextField text_ucCounts = new TextField("供应商UC的容量");
	public TextField text_uaPackages = new TextField("供应商UA的包装类型");
	public TextField text_ucInuaCounts = new TextField("供应商UA里UC的个数");
	public TextField text_uaCounts = new TextField("供应商UA的容量");
	
	public Element labl_usPackages = new Element("仓库US的包装类型");
	public Element labl_usCounts = new Element("仓库US的容量");
	public Element labl_ucType = new Element("上线UC类型");
	public Element labl_lineucCounts = new Element("上线UC的容量");
	
	public TextField text_usPackages = new TextField("仓库US的包装类型");
	public TextField text_usCounts = new TextField("仓库US的容量");
	public TextField text_ucType = new TextField("上线UC类型");
	public TextField text_lineucCounts = new TextField("上线UC的容量");
	
	{
		butt_search.addXpathLocator("//div[@id='button_submit']/div/div/a");
		butt_saveAll.addXpathLocator("//div[@id='button_save_all']/div/div/a");
		labl_ucPackages.addXpathLocator("//div[@id='grid_lingjgys']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[4]");
		labl_ucCounts.addXpathLocator("//div[@id='grid_lingjgys']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[5]");
		labl_uaPackages.addXpathLocator("//div[@id='grid_lingjgys']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[6]");
		labl_ucInuaCounts.addXpathLocator("//div[@id='grid_lingjgys']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[7]");
		labl_uaCounts.addXpathLocator("//div[@id='grid_lingjgys']/div[6]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[8]");
		
		text_ucPackages.addXpathLocator("//div[@id='field_e_ucbzlx']/input");
		text_ucCounts.addXpathLocator("//div[@id='field_e_ucrl']/input");
		text_uaPackages.addXpathLocator("//div[@id='field_e_uabzlx']/input");
		text_ucInuaCounts.addXpathLocator("//div[@id='field_e_uaucgs']/input");
		text_uaCounts.addXpathLocator("//div[@id='field_e_uarl']/input");
		
		labl_usPackages.addXpathLocator("//div[@id='grid_lingjck']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[4]");
		labl_usCounts.addXpathLocator("//div[@id='grid_lingjck']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[5]");
		labl_ucType.addXpathLocator("//div[@id='grid_lingjck']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[6]");
		labl_lineucCounts.addXpathLocator("//div[@id='grid_lingjck']/div[4]/table/tbody/tr/td[2]/div/table/tbody/tr[2]/td[7]");
		
		text_usPackages.addXpathLocator("//div[@id='field_e_usbzlx']/input");
		text_usCounts.addXpathLocator("//div[@id='field_e_usbzrl']/input");
		text_ucType.addXpathLocator("//div[@id='field_e_uclx']/input");
		text_lineucCounts.addXpathLocator("//div[@id='field_e_ucrl']/input");
	}
}

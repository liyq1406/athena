package com.auto.testcases.page.daohxxll;
import holmos.webtest.element.Element;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.util.Properties;

import com.auto.common.Util;


/**
 * 到货信息录入界面
 * @author 李智
 */
public class DaohxxllPage extends Page{
	public Properties PPtoMDtoMDProperties;
	
	public DaohxxllPage() {
		this.comment = "到货信息录入界面";
		this.init();
	}
	{
		PPtoMDtoMDProperties = Util.propertiesUrl("PPtoMDtoMD");
	}
	
	public TextField text_blh = new TextField("BL号输入框");
	{
		text_blh.addNameLocator("blh");
	}
	
	public TextField text_tch = new TextField("集装箱号/卡车号输入框");
	{
		text_tch.addNameLocator("tch");
	}
	
	public TextField text_chengysdm = new TextField("承运商编号输入框");
	{
		text_chengysdm.addNameLocator("chengysdm");
	}
	
	public TextField text_lingjbh = new TextField("零件编号输入框");
	{
		text_lingjbh.addNameLocator("lingjbh");
	}
	
	public TextField text_xiehzt = new TextField("卸货站台");
	{
		text_xiehzt.addNameLocator("xiehzt");
	}
	
	public TextField text_gongysdm = new TextField("供应商编号输入框");
	{
		text_gongysdm.addNameLocator("gongysdm");
	}
	
	public TextField text_lingjzsl = new TextField("零件总数输入框");
	{
		text_lingjzsl.addNameLocator("lingjzsl");
	}
	
	public TextField text_pich1 = new TextField("批次号输入框");
	{
		text_pich1.addNameLocator("pich1");
	}
	
	public TextField text_shixrq = new TextField("失效期输入框");
	{
		text_shixrq.addNameLocator("shixrq");
	}
	
	public TextField text_yaohlh = new TextField("要货令号输入框");
	{
		text_yaohlh.addNameLocator("yaohlh");
	}
	
	public TextField text_lingjsl = new TextField("零件数量输入框");
	{
		text_lingjsl.addNameLocator("lingjsl");
	}
	
	public TextField text_uaxh = new TextField("UA型号输入框");
	{
		text_uaxh.addNameLocator("uaxh");
	}
	
	public TextField text_ucxh = new TextField("UC型号输入框");
	{
		text_ucxh.addNameLocator("ucxh");
	}
	
	public TextField text_ucgs = new TextField("UC个数输入框");
	{
		text_ucgs.addNameLocator("ucgs");
	}
	
	public TextField text_pich = new TextField("批次号输入框");
	{
		text_pich.addNameLocator("pich");
	}
	
	public Element labl_messgae = new Element("提示消息");
	{
		labl_messgae.addXpathLocator("//div[@id='prompt']/span");
	}
}

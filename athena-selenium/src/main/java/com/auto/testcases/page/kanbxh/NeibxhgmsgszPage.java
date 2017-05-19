package com.auto.testcases.page.kanbxh;
import holmos.webtest.element.Element;
import holmos.webtest.element.Link;
import holmos.webtest.element.TextField;
import holmos.webtest.struct.Page;

import java.io.IOException;
import java.util.Properties;

import com.auto.common.PublicVerify;
import com.auto.common.Util;


/**
 * 循环规模手工设置(内部)
 * @author 李智
 */
public class NeibxhgmsgszPage extends Page{
	public Properties R1toR1Properties;
	
	public NeibxhgmsgszPage() {
		this.comment = "循环规模手工设置(内部)";
		this.init();
	}
	{
		R1toR1Properties = Util.propertiesUrl("R1toR1");
	}
	public Element textField1 = new Element("循环规模1");
	public Element textField2 = new Element("循环规模2");
	public Element textField3 = new Element("循环规模3");
	{
		String textXunhgm = R1toR1Properties.getProperty("neibxhgmsgsz_checkLingj");
		String[] textXunhgms = textXunhgm.split(";");
		textField1.addXpathLocator("//tr[@id='"+textXunhgms[0]+"']/td[7]");
		textField2.addXpathLocator("//tr[@id='"+textXunhgms[1]+"']/td[7]");
		textField3.addXpathLocator("//tr[@id='"+textXunhgms[2]+"']/td[7]");
	}
	public TextField text_xunhgm = new TextField("循环规模输入框");
	{
		text_xunhgm.addXpathLocator("//div[@id='field_e_jisxhgm']/input");
	}
	
	public Link link_cx = new Link("查询按钮");
	{
		link_cx.addLinkTextLocator("查 询");
		link_cx.addXpathLocator("//div[@id='kbxh']/div[3]/table/tbody/tr/td/div/div/div/a");
	}
	
	public Link link_cj = new Link("创建按钮");
	{
		link_cj.addLinkTextLocator("创建");
	}
	
//	public LingjNavigation checks_lingj = new LingjNavigation("选择零件");
//	{
//		checks_lingj.addXpathLocator("//div[@class='grid-content youi-editor']/table/tbody/tr/td/div/table/tbody");
//	}
//	public class LingjNavigation extends SubPage {
//		public LingjNavigation(String comment) {
//			super(comment);
//		}
//		{
//			try {
//				PublicVerify publicVerify = new PublicVerify();
//				R1toR1Properties = Util.getProperties(publicVerify.propertiesUrl("R1toR1"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		public Element checkBox1 = new Element("多选框1");
//		public Element checkBox2 = new Element("多选框2");
//		public Element checkBox3 = new Element("多选框3");
//		{
//			String checkLingj = R1toR1Properties.getProperty("neibxhgmsgsz_checkLingj");
//			String[] checkLingjs = checkLingj.split(";");
//			checkBox1.addXpathLocator("//tr[@id='"+checkLingjs[0]+"']/td[2]/div");
//			checkBox2.addXpathLocator("//tr[@id='"+checkLingjs[1]+"']/td[2]/div");
//			checkBox3.addXpathLocator("//tr[@id='"+checkLingjs[2]+"']/td[2]/div");
//		}
//	}
//	
//	public XunhgmNavigation texts_xunhgm = new XunhgmNavigation("循环规模框");
//	{
//		checks_lingj.addXpathLocator("//div[@id='grid-content youi-editor']/table/tbody/tr/td/div/table/tbody");
//	}
//	public class XunhgmNavigation extends SubPage {
//		public XunhgmNavigation(String comment) {
//			super(comment);
//		}
//		{
//			try {
//				PublicVerify publicVerify = new PublicVerify();
//				R1toR1Properties = Util.getProperties(publicVerify.propertiesUrl("R1toR1"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		public TextField textField1 = new TextField("循环规模1");
//		public TextField textField2 = new TextField("循环规模2");
//		public TextField textField3 = new TextField("循环规模3");
//		{
//			String textXunhgm = R1toR1Properties.getProperty("neibxhgmsgsz_checkLingj");
//			String[] textXunhgms = textXunhgm.split(";");
//			textField1.addXpathLocator("//tr[id='"+textXunhgms[0]+"']/td[7]");
//			textField2.addXpathLocator("//tr[id='"+textXunhgms[0]+"']/td[7]");
//			textField3.addXpathLocator("//tr[id='"+textXunhgms[0]+"']/td[7]");
//		}
//	}
}

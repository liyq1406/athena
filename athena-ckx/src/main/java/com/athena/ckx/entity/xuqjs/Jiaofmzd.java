package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 交互码字典
 * @author denggq
 * 2012-3-29
 */
public class Jiaofmzd extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 4096777548664516721L;

	private String usercenter;	//用户中心
	
	private String jiaofm;		//交付码
	
	private String shuom;		//说明
	
	private String beiz;		//备注
	
	private String biaos;		//标识
	
	public String getJiaofm() {
		return jiaofm;
	}

	public String getShuom() {
		return shuom;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}

	public void setShuom(String shuom) {
		this.shuom = shuom;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

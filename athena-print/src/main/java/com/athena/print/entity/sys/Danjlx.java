package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Danjlx extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;	//包装类型
	
	private String zidbm;		//字典编码
	
	private String zidmc;		//字典名称
	
	private String beiz;		//备注
	
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

	
	public String getZidbm() {
		return zidbm;
	}

	public void setZidbm(String zidbm) {
		this.zidbm = zidbm;
	}

	public String getZidmc() {
		return zidmc;
	}

	public void setZidmc(String zidmc) {
		this.zidmc = zidmc;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
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

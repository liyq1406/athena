package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Chej extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;	//用户中心
	
	private String chejbm;		//车间编码
	
	private String chejmc;		//车间名称
	
	private String beiz;		//备注
	
	private Integer paix;		//排序
	
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


	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}


	public String getChejbm() {
		return chejbm;
	}

	public String getChejmc() {
		return chejmc;
	}

	public void setChejbm(String chejbm) {
		this.chejbm = chejbm;
	}

	public void setChejmc(String chejmc) {
		this.chejmc = chejmc;
	}

	public Integer getPaix() {
		return paix;
	}

	public void setPaix(Integer paix) {
		this.paix = paix;
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

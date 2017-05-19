package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_chanxz_jhy extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 123443333333333L;

	public void setId(String id) {
	}

	public String getId() {
		return null;
	}
	
	
	private String usercenter;//用户中心
	private String chanxzbh; //产线组编号
	private String jihybh;  //计划员编号
	private String jihyxm; //计划员姓名
	private String beiz;//备注
	private String creator;//创建人
	private Date create_time;//创建时间
	private String editor;//修改人
	private Date edit_time;//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanxzbh() {
		return chanxzbh;
	}

	public void setChanxzbh(String chanxzbh) {
		this.chanxzbh = chanxzbh;
	}

	public String getJihybh() {
		return jihybh;
	}

	public void setJihybh(String jihybh) {
		this.jihybh = jihybh;
	}

	public String getJihyxm() {
		return jihyxm;
	}

	public void setJihyxm(String jihyxm) {
		this.jihyxm = jihyxm;
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

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}

    
}

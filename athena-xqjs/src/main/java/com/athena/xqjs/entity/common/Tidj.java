package com.athena.xqjs.entity.common;

import java.util.Date;

import com.toft.core3.support.PageableSupport;

public class Tidj extends PageableSupport{
	private String usercenter;	
	private String lingjbh	;
	private String tidljh	;
	private String creator	;
	
	private Date create_time;	
	private String editor	;
	private Date edit_time	;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getTidljh() {
		return tidljh;
	}
	public void setTidljh(String tidljh) {
		this.tidljh = tidljh;
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

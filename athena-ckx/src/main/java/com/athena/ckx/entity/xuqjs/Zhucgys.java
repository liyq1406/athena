package com.athena.ckx.entity.xuqjs;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 
 * @author xss
 * 0010495
 */
public class Zhucgys extends PageableSupport implements Domain{
	private static final long serialVersionUID = -5248754705483459535L;

	private String usercenter;//用户中心
	
	private String masterfanhs;//主返供应商
	
	private String slavefanhs;//从返供应商
	
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

	public String getMasterfanhs() {
		return masterfanhs;
	}

	public void setMasterfanhs(String masterfanhs) {
		this.masterfanhs = masterfanhs;
	}

	public String getSlavefanhs() {
		return slavefanhs;
	}

	public void setSlavefanhs(String slavefanhs) {
		this.slavefanhs = slavefanhs;
	}
	
	public void setId(String id) {
		
	}

	public String getId() {
		return null;
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
}

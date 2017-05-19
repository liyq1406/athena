package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Usercenter extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;		//用户中心编号
	
	private String centername;		//用户中心名称
	
	private String biaos;			//标识
	
	private String creator;			//增加人
	
	private String create_time;		//增加时间
	
	private String editor;			//修改人
	
	private String edit_time;		//修改时间
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCentername() {
		return centername;
	}

	public void setCentername(String centername) {
		this.centername = centername;
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

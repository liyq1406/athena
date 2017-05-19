package com.athena.ckx.entity.xuqjs;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 
 * @author xss
 * 0010495
 */
public class Fanhjzs extends PageableSupport implements Domain{
	private static final long serialVersionUID = -5248754705483459534L;

	private String usercenter;//用户中心
	
	private String fanhs;//返还商 
	private String jizgys;//记账供应商	
	private String creator;//创建人
	private String create_time;//创建时间
	private String editor;//编辑人
	private String edit_time;//编辑时间
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	public String getFanhs() {
		return fanhs;
	}

	public void setFanhs(String fanhs) {
		this.fanhs = fanhs;
	}
	
	public String getJizgys() {
		return jizgys;
	}

	public void setJizgys(String jizgys) {
		this.jizgys = jizgys;
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
		return creator;
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

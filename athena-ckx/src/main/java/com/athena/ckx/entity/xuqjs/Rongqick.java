package com.athena.ckx.entity.xuqjs;

import com.toft.core3.support.PageableSupport;

/**
 * 
 * @author xss
 * 0010495
 */
public class Rongqick extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5248754705483459535L;

	private String usercenter;//用户中心	
	private String rongqqbh;//容器区编号	
	private String cangkbh;//仓库编号	
	
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
	
	
	public String getRongqqbh() {
		return rongqqbh;
	}

	public void setRongqqbh(String rongqqbh) {
		this.rongqqbh = rongqqbh;
	}
	
	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	
	
}

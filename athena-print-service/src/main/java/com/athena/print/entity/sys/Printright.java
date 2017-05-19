/**
 *
 */
package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 用户组
 * @author
 * @version
 * 
 */
public class Printright extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -2696132078900050481L;
	
	private String userscode;//not null
	private String scodes;// not null
	private String storagescode;//not null
	private String spcodes;
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
	
	
	public String getUserscode() {
		return userscode;
	}

	public void setUserscode(String userscode) {
		this.userscode = userscode;
	}

	public String getScodes() {
		return scodes;
	}

	public void setScodes(String scodes) {
		this.scodes = scodes;
	}

	public String getStoragescode() {
		return storagescode;
	}

	public void setStoragescode(String storagescode) {
		this.storagescode = storagescode;
	}

	public String getSpcodes() {
		return spcodes;
	}

	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
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
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}

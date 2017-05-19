/**
 *
 */
package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 03打印机组
 * @author
 * @version
 * 
 */
public class PrintDevicegroup extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 3251439493266791436L;
	
	private String usercenter;
	private String spcodes;//pgid
	private String sdesc;//sdesc
	private String sname;//sname
	private String nflag;//nflag
	private String active;//
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
	
	
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getSpcodes() {
		return spcodes;
	}
	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
	}
	public String getSdesc() {
		return sdesc;
	}
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getNflag() {
		return nflag;
	}
	public void setNflag(String nflag) {
		this.nflag = nflag;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
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
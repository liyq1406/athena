/**
 *
 */
package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 用户明细
 * @author
 * @version
 * 
 */
public class Printuserinfo extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -2696132078900050481L;
	
	
	//用户中心
	private String usercenter;
	//用户编号
	private String usercode;
	//用户组编号
	private String userscode;
	//用户名称
	private String sname;
	//用户类型
	private String usertype;
	//创建人
	private String creator;
	//创建时间
	private String create_time;
	//修改人
	private String editor;
	//修改时间
	private String edit_time;
	

	
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	//return the usertype
	public String getUsertype() {
		return usertype;
	}
	//the usertype to set
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	//return the usercode
	public String getUsercode() {
		return usercode;
	}
	//the usercode to set
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	//return the userscode
	public String getUserscode() {
		return userscode;
	}
	//the userscode to set
	public void setUserscode(String userscode) {
		this.userscode = userscode;
	}
	//return the sname
	public String getSname() {
		return sname;
	}
	//the sname to set
	public void setSname(String sname) {
		this.sname = sname;
	}
	//return the creator
	public String getCreator() {
		return creator;
	}
	//the creator to set
	public void setCreator(String creator) {
		this.creator = creator;
	}
	//return the create_time
	public String getCreate_time() {
		return create_time;
	}
	//the create_time to set
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	//return the editor
	public String getEditor() {
		return editor;
	}
	//the editor to set
	public void setEditor(String editor) {
		this.editor = editor;
	}
	//return the edit_time
	public String getEdit_time() {
		return edit_time;
	}
	//the edit_time to set
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	//the id to set
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	//return the id
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}

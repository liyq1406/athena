package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_yongh_chengys extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}
	
	 private String usercenter       ;//用户中心
	 private String yonghbh          ;//用户编号
	 private String chengysbh        ;//承运商编号

	 private String creator          ;//创建人
	 private Date create_time      ;//创建时间
	 private String editor           ;//修改人
	 private Date edit_time        ;//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getYonghbh() {
		return yonghbh;
	}

	public void setYonghbh(String yonghbh) {
		this.yonghbh = yonghbh;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
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

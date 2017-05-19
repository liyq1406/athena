package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 供货模式-毛需求
 * @author qizhogntao
 * 2012-4-9
 */
public class GonghmsMxq extends PageableSupport implements Domain{

	private static final long serialVersionUID = 5921770120285939135L;
	
	
	private String gonghms;      //供货模式
	
	private String xuqly;        //需求来源
	
	private String dinghlx;      //订货路线
	
	private String creator;      //创建人
	
	private String create_time;  //创建时间
	
	private String editor;       //修改人 
	
	private String edit_time;    //修改时间

	
	public String getGonghms() {
		return gonghms;
	}

	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}

	public String getDinghlx() {
		return dinghlx;
	}

	public void setDinghlx(String dinghlx) {
		this.dinghlx = dinghlx;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}

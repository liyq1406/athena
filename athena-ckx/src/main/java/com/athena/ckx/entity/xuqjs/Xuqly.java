package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 需求来源-作用域
 * @author qizhongtao
 * 2012-4-17
 */
public class Xuqly extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -4356646075725547976L;
	
	private String xuqly;        //需求来源
	
	private String zuoyy;        //作用域
	
	private String creator;      //创建人
	
	private String create_time;  //创建时间
	
	private String editor;       //修改人
	
	private String edit_time;    //修改时间
	
	public String getXuqly() {
		return xuqly;
	}
	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}
	public String getZuoyy() {
		return zuoyy;
	}
	public void setZuoyy(String zuoyy) {
		this.zuoyy = zuoyy;
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

package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 资源跟踪类型对应表
 * @author qizhongtao
 * 2012-4-17
 */
public class Ziygzlx extends PageableSupport implements Domain{

	private static final long serialVersionUID = 4819610661324063099L;
	
	private String jislxbh;         //计算机类型编号
	
	private String xuqly;           //需求来源
	
	private String dinghlx;          //订货路线
	
	private String gonghms;         //供货模式
	
	private String jisff;           //计算方法
	
	private String jslxmc;          //计算类型名称
	
	private String creator;         //创建人
	
	private String create_time;     //创建时间
	
	private String editor;          //修改人
	
	private String edit_time;       //修改时间
	
	
	public String getJislxbh() {
		return jislxbh;
	}
	public void setJislxbh(String jislxbh) {
		this.jislxbh = jislxbh;
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
	public String getGonghms() {
		return gonghms;
	}
	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}
	public String getJisff() {
		return jisff;
	}
	public void setJisff(String jisff) {
		this.jisff = jisff;
	}
	public String getJslxmc() {
		return jslxmc;
	}
	public void setJslxmc(String jslxmc) {
		this.jslxmc = jslxmc;
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

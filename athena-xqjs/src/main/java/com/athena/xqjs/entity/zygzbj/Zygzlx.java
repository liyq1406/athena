package com.athena.xqjs.entity.zygzbj;

import com.toft.core3.support.PageableSupport;

/**
 * 资源跟踪类型bean
 * @author WL
 * @date 2011-02-02
 */
public class Zygzlx extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6759823025672627573L;
	
	/**
	 * 计算类型编号
	 */
	private String jislxbh = "";
	
	/**
	 * 需求类型
	 */
	private String xuqly = "";
	
	/**
	 * 制造陆逊
	 */
	private String zhizlx = "";
	
	/**
	 * 供应商编码
	 */
	private String gonghms = "";
	
	/**
	 * 计算方法
	 */
	private String jisff = "";
	
	/**
	 * 计算类型名称
	 */
	private String jslxmc = "";
	
	/**
	 * 创建人
	 */
	private String creator = "";
	
	/**
	 * 创建时间
	 */
	private String create_time = "";
	
	/**
	 * 编辑人
	 */
	private String editor = "";
	
	/**
	 * 编辑时间
	 */
	private String edit_time = "";

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

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
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
}

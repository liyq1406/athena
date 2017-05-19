package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:供货模式-毛需求
 * </p>
 * <p>
 * Description:供货模式-毛需求
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李智
 * @version v1.0
 * @date 2012-4-12
 */
public class GonghmsMaoxq extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;
	private String gonghms;
	private String xuqly;
	private String dinghlx;
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
	
	/**
	 * @return the gonghms
	 */
	public String getGonghms() {
		return gonghms;
	}
	/**
	 * @param gonghms the gonghms to set
	 */
	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}
	/**
	 * @return the xuqly
	 */
	public String getXuqly() {
		return xuqly;
	}
	/**
	 * @param xuqly the xuqly to set
	 */
	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}
	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/**
	 * @return the edit_time
	 */
	public String getEdit_time() {
		return edit_time;
	}
	/**
	 * @param edit_time the edit_time to set
	 */
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getDinghlx() {
		return dinghlx;
	}

	public void setDinghlx(String dinghlx) {
		this.dinghlx = dinghlx;
	}
}

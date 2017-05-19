package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:WTC调用实体类
 * </p>
 * <p>
 * Description:WTC调用实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author NIESY
 * @version v1.0
 * @date 2012-6-25
 */
public class XqjsWtcDy extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -441833092727154001L;

	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * WTC资源名称
	 */
	private String ziybh;
	/**
	 * 功能名称
	 */
	private String gongndm;
	/**
	 * WTC服务代码
	 */
	private String fuwdm;
	/**
	 * 是否失效
	 */
	private String active;
	/**
	 * 创建时间
	 */
	private String create_time = null;

	/**
	 * 创建人
	 */
	private String creator = null;

	/**
	 * 修改时间
	 */
	private String edit_time = null;

	/**
	 * 修改人
	 */
	private String editor = null;

	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getGongndm() {
		return gongndm;
	}

	public void setGongndm(String gongndm) {
		this.gongndm = gongndm;
	}
	public String getFuwdm() {
		return fuwdm;
	}
	public void setFuwdm(String fuwdm) {
		this.fuwdm = fuwdm;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getZiybh() {
		return ziybh;
	}

	public void setZiybh(String ziybh) {
		this.ziybh = ziybh;
	}
}

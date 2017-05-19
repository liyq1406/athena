package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 分装线排产数量
 * @author dsimedd001
 *
 */
public class Fenzxpcsl extends PageableSupport{

	private static final long serialVersionUID = -978898090951565360L;

	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 大线线号
	 */
	private String daxxh;

	/**
	 * 分装线号
	 */
	private String fenzxh;

	/**
	 * 日期
	 */
	private String riq;

	/**
	 * 计划上线量
	 */
	private int jihsxl;

	/**
	 * 计划下线量
	 */
	private int jihxxl;

	/**
	 * 备注
	 */
	private String beiz;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;

	/**
	 * 修改人
	 */
	private String editor;

	/**
	 * 修改时间
	 */
	private String edit_time;

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public int getJihsxl() {
		return jihsxl;
	}

	public void setJihsxl(int jihsxl) {
		this.jihsxl = jihsxl;
	}

	public int getJihxxl() {
		return jihxxl;
	}

	public void setJihxxl(int jihxxl) {
		this.jihxxl = jihxxl;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
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

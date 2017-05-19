package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;

public class Gongyzx  extends PageableSupport{
	private String gongyzx;	//工业周序
	private String kaissj;//	开始时间
	private String jiessj;//	结束时间
	private String creator;//	创建人
	private String creattime;// 创建时间
	private String editor;//	修改人
	private String edittime ;//	修改时间
	public String getGongyzx() {
		return gongyzx;
	}
	/**
	 * 设置工业周序
	 * @param gongyzx
	 */
	public void setGongyzx(String gongyzx) {
		this.gongyzx = gongyzx;
	}
	/**
	 * 得到开始时间
	 * @param gongyzx
	 */
	public String getKaissj() {
		return kaissj;
	}
	/**
	 * 设置开始时间
	 * @param gongyzx
	 */
	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}
	/**
	 * 得到结束时间
	 * @param gongyzx
	 */
	public String getJiessj() {
		return jiessj;
	}
	/**
	 * 设置结束时间
	 * @param gongyzx
	 */
	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}
	/**
	 * 得到创建人
	 * @param gongyzx
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * 设置创建人
	 * @param gongyzx
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * 得到创建时间
	 * @param gongyzx
	 */
	public String getCreattime() {
		return creattime;
	}
	/**
	 * 设置创建时间
	 * @param gongyzx
	 */
	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}
	/**
	 * 得到修改人
	 * @param gongyzx
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * 设置修改人
	 * @param gongyzx
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/**
	 * 得到修改时间
	 * @param gongyzx
	 */
	public String getEdittime() {
		return edittime;
	}
	/**
	 * 设置修改时间
	 * @param gongyzx
	 */
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	
}

package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;

/**
 * 工作时间JAVABEAN
 * @author WL
 *
 */
public class CalendarTeam extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7204087829062736303L;

	/**
	 * 工作时间编组号
	 */
	private String bianzh;
	/**
	 * 星期序号
	 */
	private String xingqxh;
	/**
	 * 序号
	 */
	private String xuh;
	/**
	 * 开始时间
	 */
	private String kaissj;
	/**
	 * 截至时间
	 */
	private String jiezsj;
	/**
	 * 班
	 */
	private String ban;
	/**
	 * 调整时间
	 */
	private String tiaozsj;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 编辑人
	 */
	private String editor;
	/**
	 * 编辑时间
	 */
	private String edit_time;
	/**
	 * 标识
	 */
	private String biaos;
	public String getBianzh() {
		return bianzh;
	}
	public void setBianzh(String bianzh) {
		this.bianzh = bianzh;
	}
	public String getXingqxh() {
		return xingqxh;
	}
	public void setXingqxh(String xingqxh) {
		this.xingqxh = xingqxh;
	}
	public String getXuh() {
		return xuh;
	}
	public void setXuh(String xuh) {
		this.xuh = xuh;
	}
	public String getKaissj() {
		return kaissj;
	}
	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}
	public String getJiezsj() {
		return jiezsj;
	}
	public void setJiezsj(String jiezsj) {
		this.jiezsj = jiezsj;
	}
	public String getBan() {
		return ban;
	}
	public void setBan(String ban) {
		this.ban = ban;
	}
	public String getTiaozsj() {
		return tiaozsj;
	}
	public void setTiaozsj(String tiaozsj) {
		this.tiaozsj = tiaozsj;
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
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	
	
}

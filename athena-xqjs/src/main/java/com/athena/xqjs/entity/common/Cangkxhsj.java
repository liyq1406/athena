package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 仓库循环时间
 * @author WL
 *
 */
public class Cangkxhsj extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7883657924977423364L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 仓库编号
	 */
	private String cangkbh;
	/**
	 * 分配循环仓库
	 */
	private String fenpqhck;
	/**
	 * 模式
	 */
	private String mos;
	/**
	 * 仓库送货频次
	 */
	private BigDecimal cangkshpcf;
	/**
	 * 仓库送货时间
	 */
	private BigDecimal cangkshsj;
	/**
	 * 仓库返回时间
	 */
	private BigDecimal cangkfhsj;
	/**
	 * 备货时间
	 */
	private BigDecimal beihsj;
	/**
	 * I类型备货时间
	 */
	private BigDecimal ibeihsj;
	/**
	 * P类型备货时间
	 */
	private BigDecimal pbeihsj;
	/**
	 * 是否自动补货
	 */
	private String shifzdbh;
	/**
	 * 生效标识
	 */
	private String shengxbs;
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
	 * 组号
	 */
	private String zuh;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getFenpqhck() {
		return fenpqhck;
	}
	public void setFenpqhck(String fenpqhck) {
		this.fenpqhck = fenpqhck;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	
	public BigDecimal getCangkshpcf() {
		return cangkshpcf;
	}
	public void setCangkshpcf(BigDecimal cangkshpcf) {
		this.cangkshpcf = cangkshpcf;
	}
	public BigDecimal getCangkshsj() {
		return cangkshsj;
	}
	public void setCangkshsj(BigDecimal cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	public BigDecimal getCangkfhsj() {
		return cangkfhsj;
	}
	public void setCangkfhsj(BigDecimal cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	public BigDecimal getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(BigDecimal beihsj) {
		this.beihsj = beihsj;
	}
	public BigDecimal getIbeihsj() {
		return ibeihsj;
	}
	public void setIbeihsj(BigDecimal ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	public BigDecimal getPbeihsj() {
		return pbeihsj;
	}
	public void setPbeihsj(BigDecimal pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	public String getShifzdbh() {
		return shifzdbh;
	}
	public void setShifzdbh(String shifzdbh) {
		this.shifzdbh = shifzdbh;
	}
	public String getShengxbs() {
		return shengxbs;
	}
	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
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
	public String getZuh() {
		return zuh;
	}
	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	
	
}

package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * @初始化资源表
 * @author   李智
 * @date     2012-3-23
 * @update   hzg 增加 chukl、yicxhl 2015.7.15
 */
public class Chushzyb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 编号
	 */
	private Integer id;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 生产线编号
	 */
	private String shengcxbh;
	/**
	 * 消耗点编号
	 */
	private String xiaohdbh;
	/**
	 * 继承的未交付量
	 */
	private BigDecimal jicdwjfl = BigDecimal.ZERO;
	/**
	 * 线边库存
	 */
	private BigDecimal xianbkc = BigDecimal.ZERO;
	/**
	 * 流水号
	 */
	private String liush;
	/**
	 * 备注
	 */
	private String beiz="";
	/**
	 * 是否初始化路线,0=否，1=是
	 * 默认为是
	 */
	private String flag = "1";
	
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 编辑人
	 */
	private String editor;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	
	/**
	 * 编辑时间
	 */
	private String edit_time;
	
	
	/**
	 * 出库量 mantis:0011510 by hzg 2015.7.15 add
	 */
	private BigDecimal chukl = BigDecimal.ZERO;
	/**
	 * 异常消耗量mantis:0011510 by hzg 2015.7.15 add
	 */
	private BigDecimal yicxhl = BigDecimal.ZERO;
	
	/**
	 * sppv消耗量 add by zbb 2015.12.09
	 */
	private BigDecimal sppvxhl = BigDecimal.ZERO;
	
	/**
	 * 订单计算时间 mantis:0011510 by hzg 2015.7.15 add
	 */
	private String dingdjssj;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the usercenter
	 */
	public String getUsercenter() {
		return usercenter;
	}
	/**
	 * @param usercenter the usercenter to set
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	/**
	 * @return the lingjbh
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	/**
	 * @param lingjbh the lingjbh to set
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	/**
	 * @return the shengcxbh
	 */
	public String getShengcxbh() {
		return shengcxbh;
	}
	/**
	 * @param shengcxbh the shengcxbh to set
	 */
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	/**
	 * @return the xiaohdbh
	 */
	public String getXiaohdbh() {
		return xiaohdbh;
	}
	/**
	 * @param xiaohdbh the xiaohdbh to set
	 */
	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}
	/**
	 * @return the jicdwjfl
	 */
	public BigDecimal getJicdwjfl() {
		return jicdwjfl;
	}
	/**
	 * @param jicdwjfl the jicdwjfl to set
	 */
	public void setJicdwjfl(BigDecimal jicdwjfl) {
		this.jicdwjfl = jicdwjfl;
	}
	/**
	 * @return the xianbkc
	 */
	public BigDecimal getXianbkc() {
		return xianbkc;
	}
	/**
	 * @param xianbkc the xianbkc to set
	 */
	public void setXianbkc(BigDecimal xianbkc) {
		this.xianbkc = xianbkc;
	}
	/**
	 * @return the liush
	 */
	public String getLiush() {
		return liush;
	}
	/**
	 * @param liush the liush to set
	 */
	public void setLiush(String liush) {
		this.liush = liush;
	}
	/**
	 * @return the beiz
	 */
	public String getBeiz() {
		return beiz;
	}
	/**
	 * @param beiz the beiz to set
	 */
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public BigDecimal getChukl() {
		return chukl;
	}
	public void setChukl(BigDecimal chukl) {
		this.chukl = chukl;
	}
	public BigDecimal getYicxhl() {
		return yicxhl;
	}
	public void setYicxhl(BigDecimal yicxhl) {
		this.yicxhl = yicxhl;
	}
	
	
	public BigDecimal getSppvxhl() {
		return sppvxhl;
	}
	public void setSppvxhl(BigDecimal sppvxhl) {
		this.sppvxhl = sppvxhl;
	}
	public String getDingdjssj() {
		return dingdjssj;
	}
	public void setDingdjssj(String dingdjssj) {
		this.dingdjssj = dingdjssj;
	}
}

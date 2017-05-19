package com.athena.xqjs.entity.maoxq;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：CompareWeek
 * <p>
 * 类描述： 毛需求周比较
 * </p>
 * 创建人：Niesy
 * <p>
 * 创建时间：2012-02-11
 * </p>
 * 
 * @version
 * 
 */
public class MaoxqDays extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * ID
     */
    private  String  id;
    
    /**
     * 需求版次
     */
    private  String  xuqbc;
    
	
	/**
	 * 用户中心
	 */
    private  String  usercenter;
    
    /**
     * 零件号
     */
    private  String  lingjbh;
    
    /**
     * 零件名称
     */
    private  String  lingjmc;
    
    /**
     * 零件单位
     */
    private  String  danw;
    
    /**
     * 需求数量
     */
    private  BigDecimal  xuqsl;
    
    /**
     * 计划员组
     */
    private  String  jihyz;
    
    /**
     * 制造路线
     */
    private  String  zhizlx;
    
    /**
     * 需求日期
     */
    private  String  xuqrq;
    
    /**
     * 产线
     */
    private  String  chanx;
    
    /**
     * 使用车间
     */
    private  String  shiycj;
    
    /**
     * 需求所属周
     */
    private  String  xuqz;
    
    /**
     * 需求所属周期
     */
    private  String  xuqsszq;
    
    /**
     * 创建人
     */
    private  String  creator;
    
    /**
     * 创建时间
     */
    private  String  create_time;
    
    /**
     * 修改人
     */
    private  String  editor;
    
    /**
     * 新修改人
     */
    private  String  newEditor;
    
    /**
     * 修改时间
     */
    private  String  edit_time;
    
    /**
     * 删除标示
     */
    private  String  active;
    
    /**
     * 新修改时间
     */
    private  String  newEdit_time;
    
    /**
     * 需求开始时间
     */
    private  String  xuqksrq;
    
    /**
     * 需求结束时间
     */
    private  String  xuqjsrq;
    
    /**
     * 日需求数量 J0
     */
    private  BigDecimal  j0;
    
    /**
     * 日需求数量 J1
     */
    private  BigDecimal  j1;
    
    /**
     * 日需求数量 J2
     */
    private  BigDecimal  j2;
    
    /**
     * 日需求数量 J3
     */
    private  BigDecimal  j3;
    
    /**
     * 日需求数量 J4
     */
    private  BigDecimal  j4;
    
    /**
     * 日需求数量 J5
     */
    private  BigDecimal  j5;
    
    /**
     * 日需求数量 J6
     */
    private  BigDecimal  j6;
    
    /**
     * 日需求数量 J7
     */
    private  BigDecimal  j7;
    
    /**
     * 日需求数量 J8
     */
    private  BigDecimal  j8;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXuqbc() {
		return xuqbc;
	}

	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public BigDecimal getXuqsl() {
		return xuqsl;
	}

	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getXuqrq() {
		return xuqrq;
	}

	public void setXuqrq(String xuqrq) {
		this.xuqrq = xuqrq;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getShiycj() {
		return shiycj;
	}

	public void setShiycj(String shiycj) {
		this.shiycj = shiycj;
	}

	public String getXuqz() {
		return xuqz;
	}

	public void setXuqz(String xuqz) {
		this.xuqz = xuqz;
	}

	public String getXuqsszq() {
		return xuqsszq;
	}

	public void setXuqsszq(String xuqsszq) {
		this.xuqsszq = xuqsszq;
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

	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getNewEdit_time() {
		return newEdit_time;
	}

	public void setNewEdit_time(String newEdit_time) {
		this.newEdit_time = newEdit_time;
	}

	public String getXuqksrq() {
		return xuqksrq;
	}

	public void setXuqksrq(String xuqksrq) {
		this.xuqksrq = xuqksrq;
	}

	public String getXuqjsrq() {
		return xuqjsrq;
	}

	public void setXuqjsrq(String xuqjsrq) {
		this.xuqjsrq = xuqjsrq;
	}

	public BigDecimal getJ0() {
		return j0;
	}

	public void setJ0(BigDecimal j0) {
		this.j0 = j0;
	}

	public BigDecimal getJ1() {
		return j1;
	}

	public void setJ1(BigDecimal j1) {
		this.j1 = j1;
	}

	public BigDecimal getJ2() {
		return j2;
	}

	public void setJ2(BigDecimal j2) {
		this.j2 = j2;
	}

	public BigDecimal getJ3() {
		return j3;
	}

	public void setJ3(BigDecimal j3) {
		this.j3 = j3;
	}

	public BigDecimal getJ4() {
		return j4;
	}

	public void setJ4(BigDecimal j4) {
		this.j4 = j4;
	}

	public BigDecimal getJ5() {
		return j5;
	}

	public void setJ5(BigDecimal j5) {
		this.j5 = j5;
	}

	public BigDecimal getJ6() {
		return j6;
	}

	public void setJ6(BigDecimal j6) {
		this.j6 = j6;
	}

	public BigDecimal getJ7() {
		return j7;
	}

	public void setJ7(BigDecimal j7) {
		this.j7 = j7;
	}

	public BigDecimal getJ8() {
		return j8;
	}

	public void setJ8(BigDecimal j8) {
		this.j8 = j8;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}   
    
   
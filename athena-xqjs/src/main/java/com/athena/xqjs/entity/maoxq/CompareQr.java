package com.athena.xqjs.entity.maoxq;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：CompareWeek
 * <p>
 * 类描述： 需求比较结果
 * </p>
 * 创建人：xss 
 * 
 * @version
 * 
 */
public class CompareQr extends PageableSupport implements Domain{

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
    private  String  xuqbc1;
    
    /**
     * 需求版次2
     */
    private  String  xuqbc2;
    
    /**
     * 基准版次
     */
    private  String  jiz;
	
	/**
	 * 用户中心
	 */
    private  String  usercenter;
    
    /**
     * 比较方式
     * 
     */
    private  String  bjfs;
    
    private  String xsfs;
    
    /**
     * 创建人
     */
    private  String  creator;
    
    /**
     * 创建时间
     */
    private  String  create_time;   

    /**
     * 状态
     */
    private  String  zhuangt; 

	/**
	 * 伪列、行号
	 */
	private String rownums;
	
	
	private String lingjbh;
	
	private String lingjmc;
	
	private String chanx;
	
	private String gongysbh;
	
	private String gongysmc;
	
	private String gcbh; 
	
	private String chengysmc;
	
	private String danw;
	
	private String zhizlx;
	
	private String jhyz; 
	
	
	
	
	
	public String getXsfs() {
		return xsfs;
	}

	public void setXsfs(String xsfs) {
		this.xsfs = xsfs;
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

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

	public String getGcbh() {
		return gcbh;
	}

	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getJhyz() {
		return jhyz;
	}

	public void setJhyz(String jhyz) {
		this.jhyz = jhyz;
	}

	/**
     * 周期需求数量   P0
     */
    private  BigDecimal  p0Cyc1;
    
    /**
     * 周期需求数量2   P0
     */
    private  BigDecimal  p0Cyc2;
    
    /**
     * 周期需求数量   P0差额
     */
    private  String  p0Margin;
    
    /**
     * 周期需求数量   P1
     */
    private  BigDecimal  p1Cyc1;
    
    /**
     * 周期需求数量2   P1
     */
    private  BigDecimal  p1Cyc2;
    
    /**
     * 周期需求数量   P1差额
     */
    private  String   p1Margin;
    
    /**
     * 周期需求数量   P2
     */
    private  BigDecimal  p2Cyc1;
    
    /**
     * 周期需求数量2   P2
     */
    private  BigDecimal  p2Cyc2;
    
    /**
     * 周期需求数量   P2差额
     */
    private  String  p2Margin;
    
    /**
     * 周期需求数量   P3
     */
    private  BigDecimal  p3Cyc1;
    
    /**
     * 周期需求数量2   P3
     */
    private  BigDecimal  p3Cyc2;
    
    /**
     * 周期需求数量   P3差额
     */
	private String p3Margin;
    
    /**
     * 周期需求数量   P4
     */
    private  BigDecimal  p4Cyc1;
    
    /**
     * 周期需求数量2   P4
     */
    private  BigDecimal  p4Cyc2;
    
    /**
     * 周期需求数量   P4差额
     */
    private  String  p4Margin;
    
    
    /**
     * 需求版次
     */
    private  String  xuqbc;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRownums() {
		return rownums;
	}

	public void setRownums(String rownums) {
		this.rownums = rownums;
	}

	public String getXuqbc1() {
		return xuqbc1;
	}

	public void setXuqbc1(String xuqbc1) {
		this.xuqbc1 = xuqbc1;
	}

	public String getXuqbc2() {
		return xuqbc2;
	}

	public void setXuqbc2(String xuqbc2) {
		this.xuqbc2 = xuqbc2;
	}

	public String getJiz() {
		return jiz;
	}

	public void setJiz(String jiz) {
		this.jiz = jiz;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getBjfs() {
		return bjfs;
	}

	public void setBjfs(String bjfs) {
		this.bjfs = bjfs;
	}

	public BigDecimal getP0Cyc1() {
		return p0Cyc1;
	}

	public void setP0Cyc1(BigDecimal p0Cyc1) {
		this.p0Cyc1 = p0Cyc1;
	}

	public BigDecimal getP0Cyc2() {
		return p0Cyc2;
	}

	public void setP0Cyc2(BigDecimal p0Cyc2) {
		this.p0Cyc2 = p0Cyc2;
	}

	public String getP0Margin() {
		return p0Margin;
	}

	public void setP0Margin(String p0Margin) {
		this.p0Margin = p0Margin;
	}

	public BigDecimal getP1Cyc1() {
		return p1Cyc1;
	}

	public void setP1Cyc1(BigDecimal p1Cyc1) {
		this.p1Cyc1 = p1Cyc1;
	}

	public BigDecimal getP1Cyc2() {
		return p1Cyc2;
	}

	public void setP1Cyc2(BigDecimal p1Cyc2) {
		this.p1Cyc2 = p1Cyc2;
	}

	public String getP1Margin() {
		return p1Margin;
	}

	public void setP1Margin(String p1Margin) {
		this.p1Margin = p1Margin;
	}

	public BigDecimal getP2Cyc1() {
		return p2Cyc1;
	}

	public void setP2Cyc1(BigDecimal p2Cyc1) {
		this.p2Cyc1 = p2Cyc1;
	}

	public BigDecimal getP2Cyc2() {
		return p2Cyc2;
	}

	public void setP2Cyc2(BigDecimal p2Cyc2) {
		this.p2Cyc2 = p2Cyc2;
	}

	public String getP2Margin() {
		return p2Margin;
	}

	public void setP2Margin(String p2Margin) {
		this.p2Margin = p2Margin;
	}

	public BigDecimal getP3Cyc1() {
		return p3Cyc1;
	}

	public void setP3Cyc1(BigDecimal p3Cyc1) {
		this.p3Cyc1 = p3Cyc1;
	}

	public BigDecimal getP3Cyc2() {
		return p3Cyc2;
	}

	public void setP3Cyc2(BigDecimal p3Cyc2) {
		this.p3Cyc2 = p3Cyc2;
	}

	public String getP3Margin() {
		return p3Margin;
	}

	public void setP3Margin(String p3Margin) {
		this.p3Margin = p3Margin;
	}

	public BigDecimal getP4Cyc1() {
		return p4Cyc1;
	}

	public void setP4Cyc1(BigDecimal p4Cyc1) {
		this.p4Cyc1 = p4Cyc1;
	}

	public BigDecimal getP4Cyc2() {
		return p4Cyc2;
	}

	public void setP4Cyc2(BigDecimal p4Cyc2) {
		this.p4Cyc2 = p4Cyc2;
	}

	public String getP4Margin() {
		return p4Margin;
	}

	public void setP4Margin(String p4Margin) {
		this.p4Margin = p4Margin;
	}

	public String getXuqbc() {
		return xuqbc;
	}

	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}

    
    
}   
    
   
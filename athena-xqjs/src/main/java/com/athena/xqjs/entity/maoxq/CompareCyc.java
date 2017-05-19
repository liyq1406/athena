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
public class CompareCyc extends PageableSupport{

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
	 * 显示方式:按用户中心，按产线显示，默认按产线
	 */
    private  String  xsfs;
    
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
     * 年期需求数量  一月
     */
    private  BigDecimal  january;
    
    /**
     * 年期需求数量  二月
     */
    private  BigDecimal  feburary;
    
    /**
     * 年期需求数量  三月
     */
    private  BigDecimal  march;
    
    /**
     * 年期需求数量  四月
     */
    private  BigDecimal  april;
    
    /**
     * 年期需求数量  五月
     */
    private  BigDecimal  may;
    
    /**
     * 年期需求数量  六月
     */
    private  BigDecimal  june;
    
    /**
     * 年期需求数量  七月
     */
    private  BigDecimal  july;
    
    /**
     * 年期需求数量  八月
     */
    private  BigDecimal  august;
    
    /**
     * 年期需求数量  九月
     */
    private  BigDecimal  september;
    
    /**
     * 年期需求数量  十月
     */
    private  BigDecimal  october;
    
	/**
	 * 年期需求数量 十一月
	 */
    private  BigDecimal  november;
    
    /**
     * 年期需求数量  十二月
     */
    private  BigDecimal  december;
    
    /**
     * 年期需求数量  十三月
     */
    private  BigDecimal  p13;
    
    /**
     * 年期需求数量  十四月
     */
    private  BigDecimal  p14;
    
    /**
     * 年期需求数量  十五月
     */
    private  BigDecimal  p15;
    
    /**
     * 需求类型PP/PS/PJ
     */
    private  String  xuqlx;
    
	/**
	 * 是否指定运输时刻
	 */
	private String shifzdyssk;

	/**
	 * 指定工业周期FROM
	 */
	private String zdgyzqfrom;

	/**
	 * 指定工业周期TO
	 */
	private String zdgyzqto;

	/**
	 * 是否计算CMJ版本
	 */
	private String shifjscmj;
	
	
	//xss 20161014 v4_008
	private String gongysbh;	
	private String gongysmc;
	private String gcbh;
	private String chengysmc;
	
	private String xuqbc1; 
	private String xuqbc2; 
	private String jiz;
	private String jhyz;
	

	
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

	public String getShifzdyssk() {
		return shifzdyssk;
	}

	public void setShifzdyssk(String shifzdyssk) {
		this.shifzdyssk = shifzdyssk;
	}

	public String getZdgyzqfrom() {
		return zdgyzqfrom;
	}

	public void setZdgyzqfrom(String zdgyzqfrom) {
		this.zdgyzqfrom = zdgyzqfrom;
	}

	public String getZdgyzqto() {
		return zdgyzqto;
	}

	public void setZdgyzqto(String zdgyzqto) {
		this.zdgyzqto = zdgyzqto;
	}

	public String getShifjscmj() {
		return shifjscmj;
	}

	public void setShifjscmj(String shifjscmj) {
		this.shifjscmj = shifjscmj;
	}

	/**
	 * 伪列、行号
	 */
	private String rownums;

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



	public String getXuqlx() {
		return xuqlx;
	}

	public void setXuqlx(String xuqlx) {
		this.xuqlx = xuqlx;
	}

	public BigDecimal getP13() {
		return p13;
	}

	public void setP13(BigDecimal p13) {
		this.p13 = p13;
	}

	public BigDecimal getP14() {
		return p14;
	}

	public void setP14(BigDecimal p14) {
		this.p14 = p14;
	}

	public BigDecimal getP15() {
		return p15;
	}

	public void setP15(BigDecimal p15) {
		this.p15 = p15;
	}

	public BigDecimal getJanuary() {
		return january;
	}

	public void setJanuary(BigDecimal january) {
		this.january = january;
	}

	public BigDecimal getFeburary() {
		return feburary;
	}

	public void setFeburary(BigDecimal feburary) {
		this.feburary = feburary;
	}

	public BigDecimal getMarch() {
		return march;
	}

	public void setMarch(BigDecimal march) {
		this.march = march;
	}

	public BigDecimal getApril() {
		return april;
	}

	public void setApril(BigDecimal april) {
		this.april = april;
	}

	public BigDecimal getMay() {
		return may;
	}

	public void setMay(BigDecimal may) {
		this.may = may;
	}

	public BigDecimal getJune() {
		return june;
	}

	public void setJune(BigDecimal june) {
		this.june = june;
	}

	public BigDecimal getJuly() {
		return july;
	}

	public void setJuly(BigDecimal july) {
		this.july = july;
	}

	public BigDecimal getAugust() {
		return august;
	}

	public void setAugust(BigDecimal august) {
		this.august = august;
	}

	public BigDecimal getSeptember() {
		return september;
	}

	public void setSeptember(BigDecimal september) {
		this.september = september;
	}

	public BigDecimal getOctober() {
		return october;
	}

	public void setOctober(BigDecimal october) {
		this.october = october;
	}

	public BigDecimal getNovember() {
		return november;
	}

	public String getRownums() {
		return rownums;
	}

	public void setRownums(String rownums) {
		this.rownums = rownums;
	}

	public void setNovember(BigDecimal november) {
		this.november = november;
	}

	public BigDecimal getDecember() {
		return december;
	}

	public void setDecember(BigDecimal december) {
		this.december = december;
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

	public String getXsfs() {
		return xsfs;
	}

	public void setXsfs(String xsfs) {
		this.xsfs = xsfs;
	}

	public String getP3Margin() {
		return p3Margin;
	}

	public void setP3Margin(String p3Margin) {
		this.p3Margin = p3Margin;
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

	public String getJhyz() {
		return jhyz;
	}

	public void setJhyz(String jhyz) {
		this.jhyz = jhyz;
	}
    
   
    
    
    
}   
    
   
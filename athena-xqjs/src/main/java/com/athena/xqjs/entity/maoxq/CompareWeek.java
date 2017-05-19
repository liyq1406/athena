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
 * 创建时间：2012-02-08
 * </p>
 * 
 * @version
 * 
 */
public class CompareWeek extends PageableSupport{

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
     * 周需求数量1 S0
     */
    private  BigDecimal  s0Week1;
    
    /**
     * 周需求数量2 S0
     */
    private  BigDecimal  s0Week2;
    
    /**
     * 周需求数量差额百分比 S0
     */
    private  String   s0Margin;
    
    /**
     * 周需求数量1 S1
     */
    private  BigDecimal  s1Week1;
    
    /**
     * 周需求数量2 S1
     */
    private  BigDecimal  s1Week2;
    
    /**
     * 周需求数量差额百分比 S1
     */
    private  String   s1Margin;
    
    /**
     * 周需求数量1 S2
     */
    private  BigDecimal  s2Week1;
    
    /**
     * 周需求数量2 S2
     */
    private  BigDecimal  s2Week2;
    
    /**
     * 周需求数量差额百分比 S2
     */
    private  String   s2Margin;
    
    /**
     * 周需求数量1 S3
     */
    private  BigDecimal  s3Week1;
    
    /**
     * 周需求数量2 S3
     */
    private  BigDecimal  s3Week2;
    
    /**
     * 周需求数量差额百分比 S3
     */
    private  String   s3Margin;
    
    /**
     * 周需求数量1 S4
     */
    private  BigDecimal  s4Week1;
    
    /**
     * 周需求数量2 S4
     */
    private  BigDecimal  s4Week2;
    
    /**
     * 周需求数量差额百分比 S4
     */
    private  String   s4Margin;
    
    /**
     * 周需求数量1 S5
     */
    private  BigDecimal  s5Week1;
    
    /**
     * 周需求数量2 S5
     */
    private  BigDecimal  s5Week2;
    
    /**
     * 周需求数量差额百分比 S5
     */
    private  String   s5Margin;
    
    /**
     * 周需求数量1 S6
     */
    private  BigDecimal  s6Week1;
    
    /**
     * 周需求数量2 S6
     */
    private  BigDecimal  s6Week2;
    
    /**
     * 周需求数量差额百分比 S6
     */
    private  String   s6Margin;
    
    /**
     * 周需求数量1 S7
     */
    private  BigDecimal  s7Week1;
    
    /**
     * 周需求数量2 S7
     */
    private  BigDecimal  s7Week2;
    
    /**
     * 周需求数量差额百分比 S7
     */
    private  String   s7Margin;
    
    /**
     * 周需求数量1 S8
     */
    private  BigDecimal  s8Week1;
    
    /**
     * 周需求数量2 S8
     */
    private  BigDecimal  s8Week2;
    
    /**
     * 周需求数量差额百分比 S8
     */
    private  String   s8Margin;
    
    /**
     * 周需求数量1 S9
     */
    private  BigDecimal  s9Week1;
    
    /**
     * 周需求数量2 S9
     */
    private  BigDecimal  s9Week2;
    
    /**
     * 周需求数量差额百分比 S9
     */
    private  String   s9Margin;
    
    /**
     * 周需求数量1 S10
     */
    private  BigDecimal  s10Week1;
    
    /**
     * 周需求数量2 S10
     */
    private  BigDecimal  s10Week2;
    
    /**
     * 周需求数量差额百分比 S10
     */
    private  String   s10Margin;
    
    /**
     * 周需求数量1 S11
     */
    private  BigDecimal  s11Week1;
    
    /**
     * 周需求数量2 S11
     */
    private  BigDecimal  s11Week2;
    
    /**
     * 周需求数量差额百分比 S11
     */
    private  String   s11Margin;
    
    /**
     * 周需求数量1 S12
     */
    private  BigDecimal  s12Week1;
    
    /**
     * 周需求数量2 S12
     */
    private  BigDecimal  s12Week2;
    
    /**
     * 周需求数量差额百分比 S12
     */
    private  String   s12Margin;
    
    /**
     * 周需求数量1 S13
     */
    private  BigDecimal  s13Week1;
    
    /**
     * 周需求数量2 S13
     */
    private  BigDecimal  s13Week2;
    
    /**
     * 周需求数量差额百分比 S13
     */
    private  String   s13Margin;
    
    /**
     * 周需求数量1 S14
     */
    private  BigDecimal  s14Week1;
    
    /**
     * 周需求数量2 S14
     */
    private  BigDecimal  s14Week2;
    
    /**
     * 周需求数量差额百分比 S14
     */
    private  String   s14Margin;
    
    /**
     * 周需求数量1 S15
     */
    private  BigDecimal  s15Week1;
    
    /**
     * 周需求数量2 S15
     */
    private  BigDecimal  s15Week2;
    
    /**
     * 周需求数量差额百分比 S15
     */
    private  String   s15Margin;
    
    /**
     * 周需求数量1 S16
     */
    private  BigDecimal  s16Week1;
    
    /**
     * 周需求数量2 S16
     */
    private  BigDecimal  s16Week2;
    
    /**
     * 周需求数量差额百分比 S16
     */
    private  String   s16Margin;
    
    /**
     * 周需求数量1 S17
     */
    private  BigDecimal  s17Week1;
    
    /**
     * 周需求数量2 S17
     */
    private  BigDecimal  s17Week2;
    
    /**
     * 周需求数量差额百分比 S17
     */
    private  String   s17Margin;
    
    /**
     * 周需求数量1 S18
     */
    private  BigDecimal  s18Week1;
    
    /**
     * 周需求数量2 S18
     */
    private  BigDecimal  s18Week2;
    
    /**
     * 周需求数量差额百分比 S18
     */
    private  String   s18Margin;
    
    /**
     * 周需求数量1 S19
     */
    private  BigDecimal  s19Week1;
    
    /**
     * 周需求数量2 S19
     */
    private  BigDecimal  s19Week2;
    
    /**
     * 周需求数量差额百分比 S19
     */
    private  String   s19Margin;
    
    /**
     * 周需求数量1 S20
     */
    private  BigDecimal  s20Week1;
    
    /**
     * 周需求数量2 S20
     */
    private  BigDecimal  s20Week2;
    
    /**
     * 周需求数量差额百分比 S20
     */
    private  String   s20Margin;
    
    /**
     * 周需求数量1 S21
     */
    private  BigDecimal  s21Week1;
    
    /**
     * 周需求数量2 S21
     */
    private  BigDecimal  s21Week2;
    
    /**
     * 周需求数量差额百分比 S21
     */
    private  String   s21Margin;
    
    /**
     * 周需求数量1 S22
     */
    private  BigDecimal  s22Week1;
    
    /**
     * 周需求数量2 S22
     */
    private  BigDecimal  s22Week2;
    
    /**
     * 周需求数量差额百分比 S22
     */
    private  String   s22Margin;
    
    /**
     * 周需求数量1 S23
     */
    private  BigDecimal  s23Week1;
    
    /**
     * 周需求数量2 S23
     */
    private  BigDecimal  s23Week2;
    
    /**
     * 周需求数量差额百分比 S23
     */
    private  String   s23Margin;
    
    /**
     * 周需求数量1 S24
     */
    private  BigDecimal  s24Week1;
    
    /**
     * 周需求数量2 S24
     */
    private  BigDecimal  s24Week2;
    
    /**
     * 周需求数量差额百分比 S24
     */
    private  String   s24Margin;
    
    /**
     * 周需求数量1 S25
     */
    private  BigDecimal  s25Week1;
    
    /**
     * 周需求数量2 S25
     */
    private  BigDecimal  s25Week2;
    
    /**
     * 周需求数量差额百分比 S25
     */
    private  String   s25Margin;
    
    /**
     * 周需求数量1 S26
     */
    private  BigDecimal  s26Week1;
    
    /**
     * 周需求数量2 S26
     */
    private  BigDecimal  s26Week2;
    
    /**
     * 周需求数量差额百分比 S26
     */
    private  String   s26Margin;
    
    /**
     * 周需求数量1 S27
     */
    private  BigDecimal  s27Week1;
    
    /**
     * 周需求数量2 S27
     */
    private  BigDecimal  s27Week2;
    
    /**
     * 周需求数量差额百分比 S27
     */
    private  String   s27Margin;
    
    /**
     * 周需求数量1 S28
     */
    private  BigDecimal  s28Week1;
    
    /**
     * 周需求数量2 S28
     */
    private  BigDecimal  s28Week2;
    
    /**
     * 周需求数量差额百分比 S28
     */
    private  String   s28Margin;
    
    /**
     * 周需求数量1 S29
     */
    private  BigDecimal  s29Week1;
    
    /**
     * 周需求数量2 S29
     */
    private  BigDecimal  s29Week2;
    
    /**
     * 周需求数量差额百分比 S29
     */
    private  String   s29Margin;
    
    /**
     * 周需求数量1 S30
     */
    private  BigDecimal  s30Week1;
    
    /**
     * 周需求数量2 S30
     */
    private  BigDecimal  s30Week2;
    
    /**
     * 周需求数量差额百分比 S30
     */
    private  String   s30Margin;
    
    /**
     * 周需求数量1 S31
     */
    private  BigDecimal  s31Week1;
    
    /**
     * 周需求数量2 S31
     */
    private  BigDecimal  s31Week2;
    
    /**
     * 周需求数量差额百分比 S31
     */
    private  String   s31Margin;
    
    /**
     * 周需求数量1 S32
     */
    private  BigDecimal  s32Week1;
    
    /**
     * 周需求数量2 S32
     */
    private  BigDecimal  s32Week2;
    
    /**
     * 周需求数量差额百分比 S32
     */
    private  String   s32Margin;
    
    /**
     * 周需求数量1 S33
     */
    private  BigDecimal  s33Week1;
    
    /**
     * 周需求数量2 S33
     */
    private  BigDecimal  s33Week2;
    
    /**
     * 周需求数量差额百分比 S33
     */
    private  String   s33Margin;
    
    /**
     * 周需求数量1 S34
     */
    private  BigDecimal  s34Week1;
    
    /**
     * 周需求数量2 S34
     */
    private  BigDecimal  s34Week2;
    
    /**
     * 周需求数量差额百分比 S34
     */
    private  String   s34Margin;
    
    /**
     * 周需求数量1 S35
     */
    private  BigDecimal  s35Week1;
    
    /**
     * 周需求数量2 S35
     */
    private  BigDecimal  s35Week2;
    
    /**
     * 周需求数量差额百分比 S35
     */
    private  String   s35Margin;
    
    /**
     * 周需求数量1 S36
     */
    private  BigDecimal  s36Week1;
    
    /**
     * 周需求数量2 S36
     */
    private  BigDecimal  s36Week2;
    
    /**
     * 周需求数量差额百分比 S36
     */
    private  String   s36Margin;
    
    /**
     * 周需求数量1 S37
     */
    private  BigDecimal  s37Week1;
    
    /**
     * 周需求数量2 S37
     */
    private  BigDecimal  s37Week2;
    
    /**
     * 周需求数量差额百分比 S37
     */
    private  String   s37Margin;
    
    /**
     * 周需求数量1 S38
     */
    private  BigDecimal  s38Week1;
    
    /**
     * 周需求数量2 S38
     */
    private  BigDecimal  s38Week2;
    
    /**
     * 周需求数量差额百分比 S38
     */
    private  String   s38Margin;
    
    /**
     * 周需求数量1 S39
     */
    private  BigDecimal  s39Week1;
    
    /**
     * 周需求数量2 S39
     */
    private  BigDecimal  s39Week2;
    
    /**
     * 周需求数量差额百分比 S39
     */
    private  String   s39Margin;
    
    /**
     * 周需求数量1 S40
     */
    private  BigDecimal  s40Week1;
    
    /**
     * 周需求数量2 S40
     */
    private  BigDecimal  s40Week2;
    
    /**
     * 周需求数量差额百分比 S40
     */
    private  String   s40Margin;
    
    /**
     * 周需求数量1 S41
     */
    private  BigDecimal  s41Week1;
    
    /**
     * 周需求数量2 S41
     */
    private  BigDecimal  s41Week2;
    
    /**
     * 周需求数量差额百分比 S41
     */
    private  String   s41Margin;
    
    /**
     * 周需求数量1 S42
     */
    private  BigDecimal  s42Week1;
    
    /**
     * 周需求数量2 S42
     */
    private  BigDecimal  s42Week2;
    
    /**
     * 周需求数量差额百分比 S42
     */
    private  String   s42Margin;
    
    /**
     * 周需求数量1 S43
     */
    private  BigDecimal  s43Week1;
    
    /**
     * 周需求数量2 S43
     */
    private  BigDecimal  s43Week2;
    
    /**
     * 周需求数量差额百分比 S43
     */
    private  String   s43Margin;
    
    /**
     * 周需求数量1 S44
     */
    private  BigDecimal  s44Week1;
    
    /**
     * 周需求数量2 S44
     */
    private  BigDecimal  s44Week2;
    
    /**
     * 周需求数量差额百分比 S44
     */
    private  String   s44Margin;
    
    /**
     * 周需求数量1 S45
     */
    private  BigDecimal  s45Week1;
    
    /**
     * 周需求数量2 S45
     */
    private  BigDecimal  s45Week2;
    
    /**
     * 周需求数量差额百分比 S45
     */
    private  String   s45Margin;
    
    /**
     * 周需求数量1 S46
     */
    private  BigDecimal  s46Week1;
    
    /**
     * 周需求数量2 S46
     */
    private  BigDecimal  s46Week2;
    
    /**
     * 周需求数量差额百分比 S46
     */
    private  String   s46Margin;
    
    /**
     * 周需求数量1 S47
     */
    private  BigDecimal  s47Week1;
    
    /**
     * 周需求数量2 S47
     */
    private  BigDecimal  s47Week2;
    
    /**
     * 周需求数量差额百分比 S47
     */
    private  String   s47Margin;
    
    /**
     * 周需求数量1 S48
     */
    private  BigDecimal  s48Week1;
    
    /**
     * 周需求数量2 S48
     */
    private  BigDecimal  s48Week2;
    
    /**
     * 周需求数量差额百分比 S48
     */
    private  String   s48Margin;
    
    /**
     * 周需求数量1 S49
     */
    private  BigDecimal  s49Week1;
    
    /**
     * 周需求数量2 S49
     */
    private  BigDecimal  s49Week2;
    
    /**
     * 周需求数量差额百分比 S49
     */
    private  String   s49Margin;
    
    /**
     * 周需求数量1 S50
     */
    private  BigDecimal  s50Week1;
    
    /**
     * 周需求数量2 S50
     */
    private  BigDecimal  s50Week2;
    
    /**
     * 周需求数量差额百分比 S50
     */
    private  String   s50Margin;
    
    /**
     * 周需求数量1 S51
     */
    private  BigDecimal  s51Week1;
    
    /**
     * 周需求数量2 S51
     */
    private  BigDecimal  s51Week2;
    
    /**
     * 周需求数量差额百分比 S51
     */
    private  String   s51Margin;
    
    /**
     * 周需求数量1 S52
     */
    private  BigDecimal  s52Week1;
    
    /**
     * 周需求数量2 S52
     */
    private  BigDecimal  s52Week2;
    
    /**
     * 周需求数量差额百分比 S52
     */
    private  String   s52Margin;
    
    /**
     * 周需求数量1 S53
     */
    private  BigDecimal  s53Week1;
    
    /**
     * 周需求数量2 S53
     */
    private  BigDecimal  s53Week2;
    
    /**
     * 周需求数量差额百分比 S53
     */
    private  String   s53Margin;
    
    /**
     * 周需求数量1 S54
     */
    private  BigDecimal  s54Week1;
    
    /**
     * 周需求数量2 S54
     */
    private  BigDecimal  s54Week2;
    
    /**
     * 周需求数量差额百分比 S54
     */
    private  String   s54Margin;
    
    /**
     * 周需求数量1 S55
     */
    private  BigDecimal  s55Week1;
    
    /**
     * 周需求数量2 S55
     */
    private  BigDecimal  s55Week2;
    
    /**
     * 周需求数量差额百分比 S55
     */
    private  String   s55Margin;
    
    /**
     * 周需求数量1 S56
     */
    private  BigDecimal  s56Week1;
    
    /**
     * 周需求数量2 S56
     */
    private  BigDecimal  s56Week2;
    
    /**
     * 周需求数量差额百分比 S56
     */
    private  String   s56Margin;
    
    /**
     * 周需求数量1 S57
     */
    private  BigDecimal  s57Week1;
    
    /**
     * 周需求数量2 S57
     */
    private  BigDecimal  s57Week2;
    
    /**
     * 周需求数量差额百分比 S57
     */
    private  String   s57Margin;
    
    /**
     * 周需求数量1 S58
     */
    private  BigDecimal  s58Week1;
    
    /**
     * 周需求数量2 S58
     */
    private  BigDecimal  s58Week2;
    
    /**
     * 周需求数量差额百分比 S58
     */
    private  String   s58Margin;
    
    /**
     * 周需求数量1 S59
     */
    private  BigDecimal  s59Week1;
    
    /**
     * 周需求数量2 S59
     */
    private  BigDecimal  s59Week2;
    
    /**
     * 周需求数量差额百分比 S59
     */
    private  String   s59Margin;
    
    /**
     * 周需求数量1 S60
     */
    private  BigDecimal  s60Week1;
    
    /**
     * 周需求数量2 S60
     */
    private  BigDecimal  s60Week2;
    
    /**
     * 周需求数量差额百分比 S60
     */
    private  String   s60Margin;
    
    /**
     * 周需求数量1 S61
     */
    private  BigDecimal  s61Week1;
    
    /**
     * 周需求数量2 S61
     */
    private  BigDecimal  s61Week2;
    
    /**
     * 周需求数量差额百分比 S61
     */
    private  String   s61Margin;
    
    /**
     * 周需求数量1 S62
     */
    private  BigDecimal  s62Week1;
    
    /**
     * 周需求数量2 S62
     */
    private  BigDecimal  s62Week2;
    
    /**
     * 周需求数量差额百分比 S62
     */
    private  String   s62Margin;
    
    /**
     * 周需求数量1 S63
     */
    private  BigDecimal  s63Week1;
    
    /**
     * 周需求数量2 S63
     */
    private  BigDecimal  s63Week2;
    
    /**
     * 周需求数量差额百分比 S63
     */
    private  String   s63Margin;
    
    /**
     * 周需求数量1 S64
     */
    private  BigDecimal  s64Week1;
    
    /**
     * 周需求数量2 S64
     */
    private  BigDecimal  s64Week2;
    
    /**
     * 周需求数量差额百分比 S64
     */
    private  String   s64Margin;

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

	public BigDecimal getS0Week1() {
		return s0Week1;
	}

	public void setS0Week1(BigDecimal s0Week1) {
		this.s0Week1 = s0Week1;
	}

	public BigDecimal getS0Week2() {
		return s0Week2;
	}

	public void setS0Week2(BigDecimal s0Week2) {
		this.s0Week2 = s0Week2;
	}

	public String getS0Margin() {
		return s0Margin;
	}

	public void setS0Margin(String s0Margin) {
		this.s0Margin = s0Margin;
	}

	public BigDecimal getS1Week1() {
		return s1Week1;
	}

	public void setS1Week1(BigDecimal s1Week1) {
		this.s1Week1 = s1Week1;
	}

	public BigDecimal getS1Week2() {
		return s1Week2;
	}

	public void setS1Week2(BigDecimal s1Week2) {
		this.s1Week2 = s1Week2;
	}

	public String getS1Margin() {
		return s1Margin;
	}

	public void setS1Margin(String s1Margin) {
		this.s1Margin = s1Margin;
	}

	public BigDecimal getS2Week1() {
		return s2Week1;
	}

	public void setS2Week1(BigDecimal s2Week1) {
		this.s2Week1 = s2Week1;
	}

	public BigDecimal getS2Week2() {
		return s2Week2;
	}

	public void setS2Week2(BigDecimal s2Week2) {
		this.s2Week2 = s2Week2;
	}

	public String getS2Margin() {
		return s2Margin;
	}

	public void setS2Margin(String s2Margin) {
		this.s2Margin = s2Margin;
	}

	public BigDecimal getS3Week1() {
		return s3Week1;
	}

	public void setS3Week1(BigDecimal s3Week1) {
		this.s3Week1 = s3Week1;
	}

	public BigDecimal getS3Week2() {
		return s3Week2;
	}

	public void setS3Week2(BigDecimal s3Week2) {
		this.s3Week2 = s3Week2;
	}

	public String getS3Margin() {
		return s3Margin;
	}

	public void setS3Margin(String s3Margin) {
		this.s3Margin = s3Margin;
	}

	public BigDecimal getS4Week1() {
		return s4Week1;
	}

	public void setS4Week1(BigDecimal s4Week1) {
		this.s4Week1 = s4Week1;
	}

	public BigDecimal getS4Week2() {
		return s4Week2;
	}

	public void setS4Week2(BigDecimal s4Week2) {
		this.s4Week2 = s4Week2;
	}

	public String getS4Margin() {
		return s4Margin;
	}

	public void setS4Margin(String s4Margin) {
		this.s4Margin = s4Margin;
	}

	public BigDecimal getS5Week1() {
		return s5Week1;
	}

	public void setS5Week1(BigDecimal s5Week1) {
		this.s5Week1 = s5Week1;
	}

	public BigDecimal getS5Week2() {
		return s5Week2;
	}

	public void setS5Week2(BigDecimal s5Week2) {
		this.s5Week2 = s5Week2;
	}

	public String getS5Margin() {
		return s5Margin;
	}

	public void setS5Margin(String s5Margin) {
		this.s5Margin = s5Margin;
	}

	public BigDecimal getS6Week1() {
		return s6Week1;
	}

	public void setS6Week1(BigDecimal s6Week1) {
		this.s6Week1 = s6Week1;
	}

	public BigDecimal getS6Week2() {
		return s6Week2;
	}

	public void setS6Week2(BigDecimal s6Week2) {
		this.s6Week2 = s6Week2;
	}

	public String getS6Margin() {
		return s6Margin;
	}

	public void setS6Margin(String s6Margin) {
		this.s6Margin = s6Margin;
	}

	public BigDecimal getS7Week1() {
		return s7Week1;
	}

	public void setS7Week1(BigDecimal s7Week1) {
		this.s7Week1 = s7Week1;
	}

	public BigDecimal getS7Week2() {
		return s7Week2;
	}

	public void setS7Week2(BigDecimal s7Week2) {
		this.s7Week2 = s7Week2;
	}

	public String getS7Margin() {
		return s7Margin;
	}

	public void setS7Margin(String s7Margin) {
		this.s7Margin = s7Margin;
	}

	public BigDecimal getS8Week1() {
		return s8Week1;
	}

	public void setS8Week1(BigDecimal s8Week1) {
		this.s8Week1 = s8Week1;
	}

	public BigDecimal getS8Week2() {
		return s8Week2;
	}

	public void setS8Week2(BigDecimal s8Week2) {
		this.s8Week2 = s8Week2;
	}

	public String getS8Margin() {
		return s8Margin;
	}

	public void setS8Margin(String s8Margin) {
		this.s8Margin = s8Margin;
	}

	public BigDecimal getS9Week1() {
		return s9Week1;
	}

	public void setS9Week1(BigDecimal s9Week1) {
		this.s9Week1 = s9Week1;
	}

	public BigDecimal getS9Week2() {
		return s9Week2;
	}

	public void setS9Week2(BigDecimal s9Week2) {
		this.s9Week2 = s9Week2;
	}

	public String getS9Margin() {
		return s9Margin;
	}

	public void setS9Margin(String s9Margin) {
		this.s9Margin = s9Margin;
	}

	public BigDecimal getS10Week1() {
		return s10Week1;
	}

	public void setS10Week1(BigDecimal s10Week1) {
		this.s10Week1 = s10Week1;
	}

	public BigDecimal getS10Week2() {
		return s10Week2;
	}

	public void setS10Week2(BigDecimal s10Week2) {
		this.s10Week2 = s10Week2;
	}

	public String getS10Margin() {
		return s10Margin;
	}

	public void setS10Margin(String s10Margin) {
		this.s10Margin = s10Margin;
	}

	public BigDecimal getS11Week1() {
		return s11Week1;
	}

	public void setS11Week1(BigDecimal s11Week1) {
		this.s11Week1 = s11Week1;
	}

	public BigDecimal getS11Week2() {
		return s11Week2;
	}

	public void setS11Week2(BigDecimal s11Week2) {
		this.s11Week2 = s11Week2;
	}

	public String getS11Margin() {
		return s11Margin;
	}

	public void setS11Margin(String s11Margin) {
		this.s11Margin = s11Margin;
	}

	public BigDecimal getS12Week1() {
		return s12Week1;
	}

	public void setS12Week1(BigDecimal s12Week1) {
		this.s12Week1 = s12Week1;
	}

	public BigDecimal getS12Week2() {
		return s12Week2;
	}

	public void setS12Week2(BigDecimal s12Week2) {
		this.s12Week2 = s12Week2;
	}

	public String getS12Margin() {
		return s12Margin;
	}

	public void setS12Margin(String s12Margin) {
		this.s12Margin = s12Margin;
	}

	public BigDecimal getS13Week1() {
		return s13Week1;
	}

	public void setS13Week1(BigDecimal s13Week1) {
		this.s13Week1 = s13Week1;
	}

	public BigDecimal getS13Week2() {
		return s13Week2;
	}

	public void setS13Week2(BigDecimal s13Week2) {
		this.s13Week2 = s13Week2;
	}

	public String getS13Margin() {
		return s13Margin;
	}

	public void setS13Margin(String s13Margin) {
		this.s13Margin = s13Margin;
	}

	public BigDecimal getS14Week1() {
		return s14Week1;
	}

	public void setS14Week1(BigDecimal s14Week1) {
		this.s14Week1 = s14Week1;
	}

	public BigDecimal getS14Week2() {
		return s14Week2;
	}

	public void setS14Week2(BigDecimal s14Week2) {
		this.s14Week2 = s14Week2;
	}

	public String getS14Margin() {
		return s14Margin;
	}

	public void setS14Margin(String s14Margin) {
		this.s14Margin = s14Margin;
	}

	public BigDecimal getS15Week1() {
		return s15Week1;
	}

	public void setS15Week1(BigDecimal s15Week1) {
		this.s15Week1 = s15Week1;
	}

	public BigDecimal getS15Week2() {
		return s15Week2;
	}

	public void setS15Week2(BigDecimal s15Week2) {
		this.s15Week2 = s15Week2;
	}

	public String getS15Margin() {
		return s15Margin;
	}

	public void setS15Margin(String s15Margin) {
		this.s15Margin = s15Margin;
	}

	public BigDecimal getS16Week1() {
		return s16Week1;
	}

	public void setS16Week1(BigDecimal s16Week1) {
		this.s16Week1 = s16Week1;
	}

	public BigDecimal getS16Week2() {
		return s16Week2;
	}

	public void setS16Week2(BigDecimal s16Week2) {
		this.s16Week2 = s16Week2;
	}

	public String getS16Margin() {
		return s16Margin;
	}

	public void setS16Margin(String s16Margin) {
		this.s16Margin = s16Margin;
	}

	public BigDecimal getS17Week1() {
		return s17Week1;
	}

	public void setS17Week1(BigDecimal s17Week1) {
		this.s17Week1 = s17Week1;
	}

	public BigDecimal getS17Week2() {
		return s17Week2;
	}

	public void setS17Week2(BigDecimal s17Week2) {
		this.s17Week2 = s17Week2;
	}

	public String getS17Margin() {
		return s17Margin;
	}

	public void setS17Margin(String s17Margin) {
		this.s17Margin = s17Margin;
	}

	public BigDecimal getS18Week1() {
		return s18Week1;
	}

	public void setS18Week1(BigDecimal s18Week1) {
		this.s18Week1 = s18Week1;
	}

	public BigDecimal getS18Week2() {
		return s18Week2;
	}

	public void setS18Week2(BigDecimal s18Week2) {
		this.s18Week2 = s18Week2;
	}

	public String getS18Margin() {
		return s18Margin;
	}

	public void setS18Margin(String s18Margin) {
		this.s18Margin = s18Margin;
	}

	public BigDecimal getS19Week1() {
		return s19Week1;
	}

	public void setS19Week1(BigDecimal s19Week1) {
		this.s19Week1 = s19Week1;
	}

	public BigDecimal getS19Week2() {
		return s19Week2;
	}

	public void setS19Week2(BigDecimal s19Week2) {
		this.s19Week2 = s19Week2;
	}

	public String getS19Margin() {
		return s19Margin;
	}

	public void setS19Margin(String s19Margin) {
		this.s19Margin = s19Margin;
	}

	public BigDecimal getS20Week1() {
		return s20Week1;
	}

	public void setS20Week1(BigDecimal s20Week1) {
		this.s20Week1 = s20Week1;
	}

	public BigDecimal getS20Week2() {
		return s20Week2;
	}

	public void setS20Week2(BigDecimal s20Week2) {
		this.s20Week2 = s20Week2;
	}

	public String getS20Margin() {
		return s20Margin;
	}

	public void setS20Margin(String s20Margin) {
		this.s20Margin = s20Margin;
	}

	public BigDecimal getS21Week1() {
		return s21Week1;
	}

	public void setS21Week1(BigDecimal s21Week1) {
		this.s21Week1 = s21Week1;
	}

	public BigDecimal getS21Week2() {
		return s21Week2;
	}

	public void setS21Week2(BigDecimal s21Week2) {
		this.s21Week2 = s21Week2;
	}

	public String getS21Margin() {
		return s21Margin;
	}

	public void setS21Margin(String s21Margin) {
		this.s21Margin = s21Margin;
	}

	public BigDecimal getS22Week1() {
		return s22Week1;
	}

	public void setS22Week1(BigDecimal s22Week1) {
		this.s22Week1 = s22Week1;
	}

	public BigDecimal getS22Week2() {
		return s22Week2;
	}

	public void setS22Week2(BigDecimal s22Week2) {
		this.s22Week2 = s22Week2;
	}

	public String getS22Margin() {
		return s22Margin;
	}

	public void setS22Margin(String s22Margin) {
		this.s22Margin = s22Margin;
	}

	public BigDecimal getS23Week1() {
		return s23Week1;
	}

	public void setS23Week1(BigDecimal s23Week1) {
		this.s23Week1 = s23Week1;
	}

	public BigDecimal getS23Week2() {
		return s23Week2;
	}

	public void setS23Week2(BigDecimal s23Week2) {
		this.s23Week2 = s23Week2;
	}

	public String getS23Margin() {
		return s23Margin;
	}

	public void setS23Margin(String s23Margin) {
		this.s23Margin = s23Margin;
	}

	public BigDecimal getS24Week1() {
		return s24Week1;
	}

	public void setS24Week1(BigDecimal s24Week1) {
		this.s24Week1 = s24Week1;
	}

	public BigDecimal getS24Week2() {
		return s24Week2;
	}

	public void setS24Week2(BigDecimal s24Week2) {
		this.s24Week2 = s24Week2;
	}

	public String getS24Margin() {
		return s24Margin;
	}

	public void setS24Margin(String s24Margin) {
		this.s24Margin = s24Margin;
	}

	public BigDecimal getS25Week1() {
		return s25Week1;
	}

	public void setS25Week1(BigDecimal s25Week1) {
		this.s25Week1 = s25Week1;
	}

	public BigDecimal getS25Week2() {
		return s25Week2;
	}

	public void setS25Week2(BigDecimal s25Week2) {
		this.s25Week2 = s25Week2;
	}

	public String getS25Margin() {
		return s25Margin;
	}

	public void setS25Margin(String s25Margin) {
		this.s25Margin = s25Margin;
	}

	public BigDecimal getS26Week1() {
		return s26Week1;
	}

	public void setS26Week1(BigDecimal s26Week1) {
		this.s26Week1 = s26Week1;
	}

	public BigDecimal getS26Week2() {
		return s26Week2;
	}

	public void setS26Week2(BigDecimal s26Week2) {
		this.s26Week2 = s26Week2;
	}

	public String getS26Margin() {
		return s26Margin;
	}

	public void setS26Margin(String s26Margin) {
		this.s26Margin = s26Margin;
	}

	public BigDecimal getS27Week1() {
		return s27Week1;
	}

	public void setS27Week1(BigDecimal s27Week1) {
		this.s27Week1 = s27Week1;
	}

	public BigDecimal getS27Week2() {
		return s27Week2;
	}

	public void setS27Week2(BigDecimal s27Week2) {
		this.s27Week2 = s27Week2;
	}

	public String getS27Margin() {
		return s27Margin;
	}

	public void setS27Margin(String s27Margin) {
		this.s27Margin = s27Margin;
	}

	public BigDecimal getS28Week1() {
		return s28Week1;
	}

	public void setS28Week1(BigDecimal s28Week1) {
		this.s28Week1 = s28Week1;
	}

	public BigDecimal getS28Week2() {
		return s28Week2;
	}

	public void setS28Week2(BigDecimal s28Week2) {
		this.s28Week2 = s28Week2;
	}

	public String getS28Margin() {
		return s28Margin;
	}

	public void setS28Margin(String s28Margin) {
		this.s28Margin = s28Margin;
	}

	public BigDecimal getS29Week1() {
		return s29Week1;
	}

	public void setS29Week1(BigDecimal s29Week1) {
		this.s29Week1 = s29Week1;
	}

	public BigDecimal getS29Week2() {
		return s29Week2;
	}

	public void setS29Week2(BigDecimal s29Week2) {
		this.s29Week2 = s29Week2;
	}

	public String getS29Margin() {
		return s29Margin;
	}

	public void setS29Margin(String s29Margin) {
		this.s29Margin = s29Margin;
	}

	public BigDecimal getS30Week1() {
		return s30Week1;
	}

	public void setS30Week1(BigDecimal s30Week1) {
		this.s30Week1 = s30Week1;
	}

	public BigDecimal getS30Week2() {
		return s30Week2;
	}

	public void setS30Week2(BigDecimal s30Week2) {
		this.s30Week2 = s30Week2;
	}

	public String getS30Margin() {
		return s30Margin;
	}

	public void setS30Margin(String s30Margin) {
		this.s30Margin = s30Margin;
	}

	public BigDecimal getS31Week1() {
		return s31Week1;
	}

	public void setS31Week1(BigDecimal s31Week1) {
		this.s31Week1 = s31Week1;
	}

	public BigDecimal getS31Week2() {
		return s31Week2;
	}

	public void setS31Week2(BigDecimal s31Week2) {
		this.s31Week2 = s31Week2;
	}

	public String getS31Margin() {
		return s31Margin;
	}

	public void setS31Margin(String s31Margin) {
		this.s31Margin = s31Margin;
	}

	public BigDecimal getS32Week1() {
		return s32Week1;
	}

	public void setS32Week1(BigDecimal s32Week1) {
		this.s32Week1 = s32Week1;
	}

	public BigDecimal getS32Week2() {
		return s32Week2;
	}

	public void setS32Week2(BigDecimal s32Week2) {
		this.s32Week2 = s32Week2;
	}

	public String getS32Margin() {
		return s32Margin;
	}

	public void setS32Margin(String s32Margin) {
		this.s32Margin = s32Margin;
	}

	public BigDecimal getS33Week1() {
		return s33Week1;
	}

	public void setS33Week1(BigDecimal s33Week1) {
		this.s33Week1 = s33Week1;
	}

	public BigDecimal getS33Week2() {
		return s33Week2;
	}

	public void setS33Week2(BigDecimal s33Week2) {
		this.s33Week2 = s33Week2;
	}

	public String getS33Margin() {
		return s33Margin;
	}

	public void setS33Margin(String s33Margin) {
		this.s33Margin = s33Margin;
	}

	public BigDecimal getS34Week1() {
		return s34Week1;
	}

	public void setS34Week1(BigDecimal s34Week1) {
		this.s34Week1 = s34Week1;
	}

	public BigDecimal getS34Week2() {
		return s34Week2;
	}

	public void setS34Week2(BigDecimal s34Week2) {
		this.s34Week2 = s34Week2;
	}

	public String getS34Margin() {
		return s34Margin;
	}

	public void setS34Margin(String s34Margin) {
		this.s34Margin = s34Margin;
	}

	public BigDecimal getS35Week1() {
		return s35Week1;
	}

	public void setS35Week1(BigDecimal s35Week1) {
		this.s35Week1 = s35Week1;
	}

	public BigDecimal getS35Week2() {
		return s35Week2;
	}

	public void setS35Week2(BigDecimal s35Week2) {
		this.s35Week2 = s35Week2;
	}

	public String getS35Margin() {
		return s35Margin;
	}

	public void setS35Margin(String s35Margin) {
		this.s35Margin = s35Margin;
	}

	public BigDecimal getS36Week1() {
		return s36Week1;
	}

	public void setS36Week1(BigDecimal s36Week1) {
		this.s36Week1 = s36Week1;
	}

	public BigDecimal getS36Week2() {
		return s36Week2;
	}

	public void setS36Week2(BigDecimal s36Week2) {
		this.s36Week2 = s36Week2;
	}

	public String getS36Margin() {
		return s36Margin;
	}

	public void setS36Margin(String s36Margin) {
		this.s36Margin = s36Margin;
	}

	public BigDecimal getS37Week1() {
		return s37Week1;
	}

	public void setS37Week1(BigDecimal s37Week1) {
		this.s37Week1 = s37Week1;
	}

	public BigDecimal getS37Week2() {
		return s37Week2;
	}

	public void setS37Week2(BigDecimal s37Week2) {
		this.s37Week2 = s37Week2;
	}

	public String getS37Margin() {
		return s37Margin;
	}

	public void setS37Margin(String s37Margin) {
		this.s37Margin = s37Margin;
	}

	public BigDecimal getS38Week1() {
		return s38Week1;
	}

	public void setS38Week1(BigDecimal s38Week1) {
		this.s38Week1 = s38Week1;
	}

	public BigDecimal getS38Week2() {
		return s38Week2;
	}

	public void setS38Week2(BigDecimal s38Week2) {
		this.s38Week2 = s38Week2;
	}

	public String getS38Margin() {
		return s38Margin;
	}

	public void setS38Margin(String s38Margin) {
		this.s38Margin = s38Margin;
	}

	public BigDecimal getS39Week1() {
		return s39Week1;
	}

	public void setS39Week1(BigDecimal s39Week1) {
		this.s39Week1 = s39Week1;
	}

	public BigDecimal getS39Week2() {
		return s39Week2;
	}

	public void setS39Week2(BigDecimal s39Week2) {
		this.s39Week2 = s39Week2;
	}

	public String getS39Margin() {
		return s39Margin;
	}

	public void setS39Margin(String s39Margin) {
		this.s39Margin = s39Margin;
	}

	public BigDecimal getS40Week1() {
		return s40Week1;
	}

	public void setS40Week1(BigDecimal s40Week1) {
		this.s40Week1 = s40Week1;
	}

	public BigDecimal getS40Week2() {
		return s40Week2;
	}

	public void setS40Week2(BigDecimal s40Week2) {
		this.s40Week2 = s40Week2;
	}

	public String getS40Margin() {
		return s40Margin;
	}

	public void setS40Margin(String s40Margin) {
		this.s40Margin = s40Margin;
	}

	public BigDecimal getS41Week1() {
		return s41Week1;
	}

	public void setS41Week1(BigDecimal s41Week1) {
		this.s41Week1 = s41Week1;
	}

	public BigDecimal getS41Week2() {
		return s41Week2;
	}

	public void setS41Week2(BigDecimal s41Week2) {
		this.s41Week2 = s41Week2;
	}

	public String getS41Margin() {
		return s41Margin;
	}

	public void setS41Margin(String s41Margin) {
		this.s41Margin = s41Margin;
	}

	public BigDecimal getS42Week1() {
		return s42Week1;
	}

	public void setS42Week1(BigDecimal s42Week1) {
		this.s42Week1 = s42Week1;
	}

	public BigDecimal getS42Week2() {
		return s42Week2;
	}

	public void setS42Week2(BigDecimal s42Week2) {
		this.s42Week2 = s42Week2;
	}

	public String getS42Margin() {
		return s42Margin;
	}

	public void setS42Margin(String s42Margin) {
		this.s42Margin = s42Margin;
	}

	public BigDecimal getS43Week1() {
		return s43Week1;
	}

	public void setS43Week1(BigDecimal s43Week1) {
		this.s43Week1 = s43Week1;
	}

	public BigDecimal getS43Week2() {
		return s43Week2;
	}

	public void setS43Week2(BigDecimal s43Week2) {
		this.s43Week2 = s43Week2;
	}

	public String getS43Margin() {
		return s43Margin;
	}

	public void setS43Margin(String s43Margin) {
		this.s43Margin = s43Margin;
	}

	public BigDecimal getS44Week1() {
		return s44Week1;
	}

	public void setS44Week1(BigDecimal s44Week1) {
		this.s44Week1 = s44Week1;
	}

	public BigDecimal getS44Week2() {
		return s44Week2;
	}

	public void setS44Week2(BigDecimal s44Week2) {
		this.s44Week2 = s44Week2;
	}

	public String getS44Margin() {
		return s44Margin;
	}

	public void setS44Margin(String s44Margin) {
		this.s44Margin = s44Margin;
	}

	public BigDecimal getS45Week1() {
		return s45Week1;
	}

	public void setS45Week1(BigDecimal s45Week1) {
		this.s45Week1 = s45Week1;
	}

	public BigDecimal getS45Week2() {
		return s45Week2;
	}

	public void setS45Week2(BigDecimal s45Week2) {
		this.s45Week2 = s45Week2;
	}

	public String getS45Margin() {
		return s45Margin;
	}

	public void setS45Margin(String s45Margin) {
		this.s45Margin = s45Margin;
	}

	public BigDecimal getS46Week1() {
		return s46Week1;
	}

	public void setS46Week1(BigDecimal s46Week1) {
		this.s46Week1 = s46Week1;
	}

	public BigDecimal getS46Week2() {
		return s46Week2;
	}

	public void setS46Week2(BigDecimal s46Week2) {
		this.s46Week2 = s46Week2;
	}

	public String getS46Margin() {
		return s46Margin;
	}

	public void setS46Margin(String s46Margin) {
		this.s46Margin = s46Margin;
	}

	public BigDecimal getS47Week1() {
		return s47Week1;
	}

	public void setS47Week1(BigDecimal s47Week1) {
		this.s47Week1 = s47Week1;
	}

	public BigDecimal getS47Week2() {
		return s47Week2;
	}

	public void setS47Week2(BigDecimal s47Week2) {
		this.s47Week2 = s47Week2;
	}

	public String getS47Margin() {
		return s47Margin;
	}

	public void setS47Margin(String s47Margin) {
		this.s47Margin = s47Margin;
	}

	public BigDecimal getS48Week1() {
		return s48Week1;
	}

	public void setS48Week1(BigDecimal s48Week1) {
		this.s48Week1 = s48Week1;
	}

	public BigDecimal getS48Week2() {
		return s48Week2;
	}

	public void setS48Week2(BigDecimal s48Week2) {
		this.s48Week2 = s48Week2;
	}

	public String getS48Margin() {
		return s48Margin;
	}

	public void setS48Margin(String s48Margin) {
		this.s48Margin = s48Margin;
	}

	public BigDecimal getS49Week1() {
		return s49Week1;
	}

	public void setS49Week1(BigDecimal s49Week1) {
		this.s49Week1 = s49Week1;
	}

	public BigDecimal getS49Week2() {
		return s49Week2;
	}

	public void setS49Week2(BigDecimal s49Week2) {
		this.s49Week2 = s49Week2;
	}

	public String getS49Margin() {
		return s49Margin;
	}

	public void setS49Margin(String s49Margin) {
		this.s49Margin = s49Margin;
	}

	public BigDecimal getS50Week1() {
		return s50Week1;
	}

	public void setS50Week1(BigDecimal s50Week1) {
		this.s50Week1 = s50Week1;
	}

	public BigDecimal getS50Week2() {
		return s50Week2;
	}

	public void setS50Week2(BigDecimal s50Week2) {
		this.s50Week2 = s50Week2;
	}

	public String getS50Margin() {
		return s50Margin;
	}

	public void setS50Margin(String s50Margin) {
		this.s50Margin = s50Margin;
	}

	public BigDecimal getS51Week1() {
		return s51Week1;
	}

	public void setS51Week1(BigDecimal s51Week1) {
		this.s51Week1 = s51Week1;
	}

	public BigDecimal getS51Week2() {
		return s51Week2;
	}

	public void setS51Week2(BigDecimal s51Week2) {
		this.s51Week2 = s51Week2;
	}

	public String getS51Margin() {
		return s51Margin;
	}

	public void setS51Margin(String s51Margin) {
		this.s51Margin = s51Margin;
	}

	public BigDecimal getS52Week1() {
		return s52Week1;
	}

	public void setS52Week1(BigDecimal s52Week1) {
		this.s52Week1 = s52Week1;
	}

	public BigDecimal getS52Week2() {
		return s52Week2;
	}

	public void setS52Week2(BigDecimal s52Week2) {
		this.s52Week2 = s52Week2;
	}

	public String getS52Margin() {
		return s52Margin;
	}

	public void setS52Margin(String s52Margin) {
		this.s52Margin = s52Margin;
	}

	public BigDecimal getS53Week1() {
		return s53Week1;
	}

	public void setS53Week1(BigDecimal s53Week1) {
		this.s53Week1 = s53Week1;
	}

	public BigDecimal getS53Week2() {
		return s53Week2;
	}

	public void setS53Week2(BigDecimal s53Week2) {
		this.s53Week2 = s53Week2;
	}

	public String getS53Margin() {
		return s53Margin;
	}

	public void setS53Margin(String s53Margin) {
		this.s53Margin = s53Margin;
	}

	public BigDecimal getS54Week1() {
		return s54Week1;
	}

	public void setS54Week1(BigDecimal s54Week1) {
		this.s54Week1 = s54Week1;
	}

	public BigDecimal getS54Week2() {
		return s54Week2;
	}

	public void setS54Week2(BigDecimal s54Week2) {
		this.s54Week2 = s54Week2;
	}

	public String getS54Margin() {
		return s54Margin;
	}

	public void setS54Margin(String s54Margin) {
		this.s54Margin = s54Margin;
	}

	public BigDecimal getS55Week1() {
		return s55Week1;
	}

	public void setS55Week1(BigDecimal s55Week1) {
		this.s55Week1 = s55Week1;
	}

	public BigDecimal getS55Week2() {
		return s55Week2;
	}

	public void setS55Week2(BigDecimal s55Week2) {
		this.s55Week2 = s55Week2;
	}

	public String getS55Margin() {
		return s55Margin;
	}

	public void setS55Margin(String s55Margin) {
		this.s55Margin = s55Margin;
	}

	public BigDecimal getS56Week1() {
		return s56Week1;
	}

	public void setS56Week1(BigDecimal s56Week1) {
		this.s56Week1 = s56Week1;
	}

	public BigDecimal getS56Week2() {
		return s56Week2;
	}

	public void setS56Week2(BigDecimal s56Week2) {
		this.s56Week2 = s56Week2;
	}

	public String getS56Margin() {
		return s56Margin;
	}

	public void setS56Margin(String s56Margin) {
		this.s56Margin = s56Margin;
	}

	public BigDecimal getS57Week1() {
		return s57Week1;
	}

	public void setS57Week1(BigDecimal s57Week1) {
		this.s57Week1 = s57Week1;
	}

	public BigDecimal getS57Week2() {
		return s57Week2;
	}

	public void setS57Week2(BigDecimal s57Week2) {
		this.s57Week2 = s57Week2;
	}

	public String getS57Margin() {
		return s57Margin;
	}

	public void setS57Margin(String s57Margin) {
		this.s57Margin = s57Margin;
	}

	public BigDecimal getS58Week1() {
		return s58Week1;
	}

	public void setS58Week1(BigDecimal s58Week1) {
		this.s58Week1 = s58Week1;
	}

	public BigDecimal getS58Week2() {
		return s58Week2;
	}

	public void setS58Week2(BigDecimal s58Week2) {
		this.s58Week2 = s58Week2;
	}

	public String getS58Margin() {
		return s58Margin;
	}

	public void setS58Margin(String s58Margin) {
		this.s58Margin = s58Margin;
	}

	public BigDecimal getS59Week1() {
		return s59Week1;
	}

	public void setS59Week1(BigDecimal s59Week1) {
		this.s59Week1 = s59Week1;
	}

	public BigDecimal getS59Week2() {
		return s59Week2;
	}

	public void setS59Week2(BigDecimal s59Week2) {
		this.s59Week2 = s59Week2;
	}

	public String getS59Margin() {
		return s59Margin;
	}

	public void setS59Margin(String s59Margin) {
		this.s59Margin = s59Margin;
	}

	public BigDecimal getS60Week1() {
		return s60Week1;
	}

	public void setS60Week1(BigDecimal s60Week1) {
		this.s60Week1 = s60Week1;
	}

	public BigDecimal getS60Week2() {
		return s60Week2;
	}

	public void setS60Week2(BigDecimal s60Week2) {
		this.s60Week2 = s60Week2;
	}

	public String getS60Margin() {
		return s60Margin;
	}

	public void setS60Margin(String s60Margin) {
		this.s60Margin = s60Margin;
	}

	public BigDecimal getS61Week1() {
		return s61Week1;
	}

	public void setS61Week1(BigDecimal s61Week1) {
		this.s61Week1 = s61Week1;
	}

	public BigDecimal getS61Week2() {
		return s61Week2;
	}

	public void setS61Week2(BigDecimal s61Week2) {
		this.s61Week2 = s61Week2;
	}

	public String getS61Margin() {
		return s61Margin;
	}

	public void setS61Margin(String s61Margin) {
		this.s61Margin = s61Margin;
	}

	public BigDecimal getS62Week1() {
		return s62Week1;
	}

	public void setS62Week1(BigDecimal s62Week1) {
		this.s62Week1 = s62Week1;
	}

	public BigDecimal getS62Week2() {
		return s62Week2;
	}

	public void setS62Week2(BigDecimal s62Week2) {
		this.s62Week2 = s62Week2;
	}

	public String getS62Margin() {
		return s62Margin;
	}

	public void setS62Margin(String s62Margin) {
		this.s62Margin = s62Margin;
	}

	public BigDecimal getS63Week1() {
		return s63Week1;
	}

	public void setS63Week1(BigDecimal s63Week1) {
		this.s63Week1 = s63Week1;
	}

	public BigDecimal getS63Week2() {
		return s63Week2;
	}

	public void setS63Week2(BigDecimal s63Week2) {
		this.s63Week2 = s63Week2;
	}

	public String getS63Margin() {
		return s63Margin;
	}

	public void setS63Margin(String s63Margin) {
		this.s63Margin = s63Margin;
	}

	public BigDecimal getS64Week1() {
		return s64Week1;
	}

	public void setS64Week1(BigDecimal s64Week1) {
		this.s64Week1 = s64Week1;
	}

	public BigDecimal getS64Week2() {
		return s64Week2;
	}

	public void setS64Week2(BigDecimal s64Week2) {
		this.s64Week2 = s64Week2;
	}

	public String getS64Margin() {
		return s64Margin;
	}

	public void setS64Margin(String s64Margin) {
		this.s64Margin = s64Margin;
	}

    

	
	
	
	
	

    
}

package com.athena.xqjs.entity.report;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 毛需求报表实体类
 * @author WL
 *
 */
public class RepMaoxq extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2520443943882665886L;

	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 零件名称
	 */
	private String lingjmc;
	
	/**
	 * 单位
	 */
	private String danw;
	
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	
	/**
	 * 供应商名称
	 */
	private String gongysmc;
	
	/**
	 * 供应份额
	 */
	private BigDecimal gongyfe;
	
	/**
	 * 订货路线
	 */
	private String dinghlx;
	
	/**
	 * 产线
	 */
	private String chanx;
	
	/**
	 * 计划员
	 */
	private String jihy;
	
	/**
	 * 类型,1.IL,2.KD
	 */
	private String leix;
	
	/**
	 * 日需求0
	 */
	private BigDecimal J0;
	/**
	 * 日需求1
	 */
	private BigDecimal J1;
	/**
	 * 日需求2
	 */
	private BigDecimal J2;
	/**
	 * 日需求3
	 */
	private BigDecimal J3;
	/**
	 * 日需求4
	 */
	private BigDecimal J4;
	/**
	 * 日需求5
	 */
	private BigDecimal J5;
	/**
	 * 日需求6
	 */
	private BigDecimal J6;
	/**
	 * 日需求7
	 */
	private BigDecimal J7;
	/**
	 * 日需求8
	 */
	private BigDecimal J8;
	/**
	 * 日需求9
	 */
	private BigDecimal J9;
	/**
	 * 日需求10
	 */
	private BigDecimal J10;
	/**
	 * 日需求11
	 */
	private BigDecimal J11;
	/**
	 * 日需求12
	 */
	private BigDecimal J12;
	/**
	 * 日需求13
	 */
	private BigDecimal J13;

	/**
	 * 周需求0
	 */
	private BigDecimal S0;
	
	/**
	 * 周需求1
	 */
	private BigDecimal S1;
	
	/**
	 * 周需求2
	 */
	private BigDecimal S2;
	
	/**
	 * 周需求3
	 */
	private BigDecimal S3;
	
	/**
	 * 周需求4
	 */
	private BigDecimal S4;
	
	/**
	 * 月需求0
	 */
	private BigDecimal P0;
	
	/**
	 * 月需求1
	 */
	private BigDecimal P1;
	
	/**
	 * 月需求2
	 */
	private BigDecimal P2;
	
	/**
	 * 月需求3
	 */
	private BigDecimal P3;
	
	/**
	 * 月需求4
	 */
	private BigDecimal P4;
	
	/**
	 * 月需求5
	 */
	private BigDecimal P5;
	
	/**
	 * 月需求6
	 */
	private BigDecimal P6;
	
	/**
	 * 月需求7
	 */
	private BigDecimal P7;
	
	/**
	 * 月需求8
	 */
	private BigDecimal P8;
	
	/**
	 * 月需求9
	 */
	private BigDecimal P9;
	
	/**
	 * 月需求10
	 */
	private BigDecimal P10;
	
	/**
	 * 月需求11
	 */
	private BigDecimal P11;
	
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 创建时间
	 */
	private String create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

	public BigDecimal getGongyfe() {
		return gongyfe;
	}

	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}

	public String getDinghlx() {
		return dinghlx;
	}

	public void setDinghlx(String dinghlx) {
		this.dinghlx = dinghlx;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	

	public BigDecimal getJ0() {
		return J0;
	}

	public void setJ0(BigDecimal j0) {
		J0 = j0;
	}

	public BigDecimal getJ1() {
		return J1;
	}

	public void setJ1(BigDecimal j1) {
		J1 = j1;
	}

	public BigDecimal getJ2() {
		return J2;
	}

	public void setJ2(BigDecimal j2) {
		J2 = j2;
	}

	public BigDecimal getJ3() {
		return J3;
	}

	public void setJ3(BigDecimal j3) {
		J3 = j3;
	}

	public BigDecimal getJ4() {
		return J4;
	}

	public void setJ4(BigDecimal j4) {
		J4 = j4;
	}

	public BigDecimal getJ5() {
		return J5;
	}

	public void setJ5(BigDecimal j5) {
		J5 = j5;
	}

	public BigDecimal getJ6() {
		return J6;
	}

	public void setJ6(BigDecimal j6) {
		J6 = j6;
	}

	public BigDecimal getJ7() {
		return J7;
	}

	public void setJ7(BigDecimal j7) {
		J7 = j7;
	}

	public BigDecimal getJ8() {
		return J8;
	}

	public void setJ8(BigDecimal j8) {
		J8 = j8;
	}

	public BigDecimal getJ9() {
		return J9;
	}

	public void setJ9(BigDecimal j9) {
		J9 = j9;
	}

	public BigDecimal getJ10() {
		return J10;
	}

	public void setJ10(BigDecimal j10) {
		J10 = j10;
	}

	public BigDecimal getJ11() {
		return J11;
	}

	public void setJ11(BigDecimal j11) {
		J11 = j11;
	}

	public BigDecimal getJ12() {
		return J12;
	}

	public void setJ12(BigDecimal j12) {
		J12 = j12;
	}

	public BigDecimal getJ13() {
		return J13;
	}

	public void setJ13(BigDecimal j13) {
		J13 = j13;
	}

	public BigDecimal getS0() {
		return S0;
	}

	public void setS0(BigDecimal s0) {
		S0 = s0;
	}

	public BigDecimal getS1() {
		return S1;
	}

	public void setS1(BigDecimal s1) {
		S1 = s1;
	}

	public BigDecimal getS2() {
		return S2;
	}

	public void setS2(BigDecimal s2) {
		S2 = s2;
	}

	public BigDecimal getS3() {
		return S3;
	}

	public void setS3(BigDecimal s3) {
		S3 = s3;
	}

	public BigDecimal getS4() {
		return S4;
	}

	public void setS4(BigDecimal s4) {
		S4 = s4;
	}

	public BigDecimal getP0() {
		return P0;
	}

	public void setP0(BigDecimal p0) {
		P0 = p0;
	}

	public BigDecimal getP1() {
		return P1;
	}

	public void setP1(BigDecimal p1) {
		P1 = p1;
	}

	public BigDecimal getP2() {
		return P2;
	}

	public void setP2(BigDecimal p2) {
		P2 = p2;
	}

	public BigDecimal getP3() {
		return P3;
	}

	public void setP3(BigDecimal p3) {
		P3 = p3;
	}

	public BigDecimal getP4() {
		return P4;
	}

	public void setP4(BigDecimal p4) {
		P4 = p4;
	}

	public BigDecimal getP5() {
		return P5;
	}

	public void setP5(BigDecimal p5) {
		P5 = p5;
	}

	public BigDecimal getP6() {
		return P6;
	}

	public void setP6(BigDecimal p6) {
		P6 = p6;
	}

	public BigDecimal getP7() {
		return P7;
	}

	public void setP7(BigDecimal p7) {
		P7 = p7;
	}

	public BigDecimal getP8() {
		return P8;
	}

	public void setP8(BigDecimal p8) {
		P8 = p8;
	}

	public BigDecimal getP9() {
		return P9;
	}

	public void setP9(BigDecimal p9) {
		P9 = p9;
	}

	public BigDecimal getP10() {
		return P10;
	}

	public void setP10(BigDecimal p10) {
		P10 = p10;
	}

	public BigDecimal getP11() {
		return P11;
	}

	public void setP11(BigDecimal p11) {
		P11 = p11;
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

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}
	
	
}

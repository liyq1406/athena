package com.athena.xqjs.entity.zygzbj;

import com.toft.core3.support.PageableSupport;

/**
 * 资源跟踪零件bean
 * @author WL
 * @date 2011-03-12
 */
public class Ziygzlj extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7165276372465276812L;
	
	/**
	 * TC号
	 */
	private String tcno;
	
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	
	/**
	 * 供应商名称
	 */
	private String gongysmc;
	
	/**
	 * 零件总量
	 */
	private Double lingjzl;
	
	/**
	 * 物理点
	 */
	private String zuiswld;
	
	/**
	 * 交付时间(最新预计到达时间)
	 */
	private String zuixyjddsj;
	
	/**
	 * 运输路线(路径代码)
	 */
	private String lujdm;
	
	/**
	 * UT号
	 */
	private String utj;
	
	/**
	 * 要货令号
	 */
	private String yaohlh;
	
	/**
	 * UA类型
	 */
	private String uaxh;
	
	/**
	 * UA容量
	 */
	private String uarl;
	
	public String getTcno() {
		return tcno;
	}

	public void setTcno(String tcno) {
		this.tcno = tcno;
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

	public Double getLingjzl() {
		return lingjzl;
	}

	public void setLingjzl(Double lingjzl) {
		this.lingjzl = lingjzl;
	}

	public String getZuiswld() {
		return zuiswld;
	}

	public void setZuiswld(String zuiswld) {
		this.zuiswld = zuiswld;
	}

	public String getZuixyjddsj() {
		return zuixyjddsj;
	}

	public void setZuixyjddsj(String zuixyjddsj) {
		this.zuixyjddsj = zuixyjddsj;
	}

	public String getLujdm() {
		return lujdm;
	}

	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}

	public String getUtj() {
		return utj;
	}

	public void setUtj(String utj) {
		this.utj = utj;
	}

	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getUaxh() {
		return uaxh;
	}

	public void setUaxh(String uaxh) {
		this.uaxh = uaxh;
	}

	public String getUarl() {
		return uarl;
	}

	public void setUarl(String uarl) {
		this.uarl = uarl;
	}

}

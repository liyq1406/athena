package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 供应商份额计算
 * @author WL
 *
 */
public class GongysFeneJis extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7202516791600148262L;
	
	/**
	 * 供应商编号
	 */
	private String gongysbh;
	
	/**
	 * 供应商当前要货数量
	 */
	private BigDecimal yaohsl;
	
	/**
	 * 计划份额
	 */
	private BigDecimal jihfene;
	
	/**
	 * 供应商累积
	 */
	private BigDecimal gongyslj;
	
	/**
	 * 供应商实际功耗份额差异
	 */
	private BigDecimal gongyssjghfecy;

	
	

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public BigDecimal getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(BigDecimal yaohsl) {
		this.yaohsl = yaohsl;
	}

	public BigDecimal getJihfene() {
		return jihfene;
	}

	public void setJihfene(BigDecimal jihfene) {
		this.jihfene = jihfene;
	}

	public BigDecimal getGongyslj() {
		return gongyslj;
	}

	public void setGongyslj(BigDecimal gongyslj) {
		this.gongyslj = gongyslj;
	}

	public BigDecimal getGongyssjghfecy() {
		return gongyssjghfecy;
	}

	public void setGongyssjghfecy(BigDecimal gongyssjghfecy) {
		this.gongyssjghfecy = gongyssjghfecy;
	}
	
	

}

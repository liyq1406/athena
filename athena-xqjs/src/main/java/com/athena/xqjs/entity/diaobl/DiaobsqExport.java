package com.athena.xqjs.entity.diaobl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;

/**
 * @see    调拨导出格式
 * @author wuyichao 
 *
 */
public class DiaobsqExport  extends PageableSupport implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5658372427168803265L;
	
	/**
	 * 调拨申请单号
	 */
	private String diaobsqdh;
	
	/**
	 * 成本中心
	 */
	private String chengbzx;
	
	/**
	 * 调拨申请时间
	 */
	private String diaobsqsj;

	/**
	 * 申请人
	 */
	private String creator;
	
	/**
	 * 状态
	 */
	private String zhuangt;
	
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 零件名称
	 */
	private String lingjmc;
	
	/**
	 * 申报数量
	 */
	private BigDecimal shenbsl;
	
	/**
	 * 实批数量
	 */
	private BigDecimal shipsl;
	
	/**
	 * 制造路线
	 */
	private String lux;
	
	/**
	 * 要货时间
	 */
	private String yaohsj;
	
	/**
	 * 计划员
	 */
	private String jihy;

	public String getDiaobsqdh() {
		return diaobsqdh;
	}

	public void setDiaobsqdh(String diaobsqdh) {
		this.diaobsqdh = diaobsqdh;
	}

	public String getChengbzx() {
		return chengbzx;
	}

	public void setChengbzx(String chengbzx) {
		this.chengbzx = chengbzx;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
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

	public BigDecimal getShenbsl() {
		return shenbsl;
	}

	public void setShenbsl(BigDecimal shenbsl) {
		this.shenbsl = shenbsl;
	}

	public BigDecimal getShipsl() {
		return shipsl;
	}

	public void setShipsl(BigDecimal shipsl) {
		this.shipsl = shipsl;
	}

	public String getLux() {
		return lux;
	}

	public void setLux(String lux) {
		this.lux = lux;
	}


	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public String getDiaobsqsj() {
		return diaobsqsj;
	}

	public void setDiaobsqsj(String diaobsqsj) {
		this.diaobsqsj = diaobsqsj;
	}

	public String getYaohsj() {
		return yaohsj;
	}

	public void setYaohsj(String yaohsj) {
		this.yaohsj = yaohsj;
	}
	
	
	
}

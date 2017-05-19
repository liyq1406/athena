package com.athena.xqjs.entity.kdorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

// TODO: Auto-generated Javadoc
/**
 * The Class Kdbdml.
 */
public class Kdbdml extends PageableSupport{
	
	/** ID. */
	private String id;
	
	/** 用户中心. */
	private String usercenter;
	
	/** 订货车间. */
	private String dinghcj;
	
	/** 零件编号. */
	private String lingjbh;
	
	/** uc类型. */
	private String uclx;
	
	/** uc容量. */
	private BigDecimal ucrl;
	
	/** ua类型. */
	private String ualx;
	
	/** ua容量. */
	private BigDecimal uarl;
	
	/** 发货地. */
	private String fahd;
	
	/** 制造路线. */
	private String zhizlx;
	
	/** 第一次启运时间. */
	private String diycqysj;
	/** 零件类型. */
	private String lingjlx;
	/** 车型. */
	private String chex;

	//xss-v4_014  卸货点 
	private String xiehd;
	
	
	public String getXiehd() {
		return xiehd;
	}

	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}


	/**
	 * Gets the 零件类型.
	 *
	 * @return the 零件类型
	 */
	public String getLingjlx() {
		return lingjlx;
	}

	/**
	 * Sets the 零件类型.
	 *
	 * @param lingjlx the new 零件类型
	 */
	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	/**
	 * Gets the 车型.
	 *
	 * @return the 车型
	 */
	public String getChex() {
		return chex;
	}

	/**
	 * Sets the 车型.
	 *
	 * @param chex the new 车型
	 */
	public void setChex(String chex) {
		this.chex = chex;
	}

	/**
	 * Gets the 用户中心.
	 *
	 * @return the 用户中心
	 */
	public String getUsercenter() {
		return usercenter;
	}
	/**
	 * 取得 - iD.
	 *
	 * @return iD
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设定 - iD.
	 *
	 * @param iD - iD
	 */
	
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the 用户中心.
	 *
	 * @param usercenter the new 用户中心
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	/**
	 * Gets the 订货车间.
	 *
	 * @return the 订货车间
	 */
	public String getDinghcj() {
		return dinghcj;
	}
	
	/**
	 * Sets the 订货车间.
	 *
	 * @param dinghcj the new 订货车间
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	
	/**
	 * Gets the 零件编号.
	 *
	 * @return the 零件编号
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	
	/**
	 * Sets the 零件编号.
	 *
	 * @param lingjbh the new 零件编号
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	/**
	 * Gets the uc类型.
	 *
	 * @return the uc类型
	 */
	public String getUclx() {
		return uclx;
	}
	
	/**
	 * Sets the uc类型.
	 *
	 * @param uclx the new uc类型
	 */
	public void setUclx(String uclx) {
		this.uclx = uclx;
	}
	
	/**
	 * Gets the uc容量.
	 *
	 * @return the uc容量
	 */
	public BigDecimal getUcrl() {
		return ucrl;
	}
	
	/**
	 * Sets the uc容量.
	 *
	 * @param ucrl the new uc容量
	 */
	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}
	
	/**
	 * Gets the ua类型.
	 *
	 * @return the ua类型
	 */
	public String getUalx() {
		return ualx;
	}
	
	/**
	 * Sets the ua类型.
	 *
	 * @param ualx the new ua类型
	 */
	public void setUalx(String ualx) {
		this.ualx = ualx;
	}
	
	/**
	 * Gets the ua容量.
	 *
	 * @return the ua容量
	 */
	public BigDecimal getUarl() {
		return uarl;
	}
	
	/**
	 * Sets the ua容量.
	 *
	 * @param uarl the new ua容量
	 */
	public void setUarl(BigDecimal uarl) {
		this.uarl = uarl;
	}
	
	/**
	 * Gets the 发货地.
	 *
	 * @return the 发货地
	 */
	public String getFahd() {
		return fahd;
	}
	
	/**
	 * Sets the 发货地.
	 *
	 * @param fahd the new 发货地
	 */
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	
	/**
	 * Gets the 制造路线.
	 *
	 * @return the 制造路线
	 */
	public String getZhizlx() {
		return zhizlx;
	}
	
	/**
	 * Sets the 制造路线.
	 *
	 * @param zhizlx the new 制造路线
	 */
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	/**
	 * Gets the 第一次启运时间.
	 *
	 * @return the 第一次启运时间
	 */
	public String getDiycqysj() {
		return diycqysj;
	}
	
	/**
	 * Sets the 第一次启运时间.
	 *
	 * @param diycqysj the new 第一次启运时间
	 */
	public void setDiycqysj(String diycqysj) {
		this.diycqysj = diycqysj;
	}
	
}

package com.athena.xqjs.entity.zygzbj;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Ziygzbjmxq extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8575424413072739330L;

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 产线
	 */
	private String chanx;
	
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 制造路线
	 */
	private String zhizlx;
	
	/**
	 * 仓库代码
	 */
	private String cangkdm;
	
	/**
	 * 消耗比例
	 */
	private BigDecimal xiaohbl;
	
	/**
	 * 消耗点
	 */
	private String xiaohd;
	
	/**
	 * 仓库集合(包括订货库和订货库下的线边库)
	 */
	private String xianbk;
	
	

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

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

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getCangkdm() {
		return cangkdm;
	}

	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}

	public BigDecimal getXiaohbl() {
		return xiaohbl;
	}

	public void setXiaohbl(BigDecimal xiaohbl) {
		this.xiaohbl = xiaohbl;
	}

	public String getXianbk() {
		return xianbk;
	}

	public void setXianbk(String xianbk) {
		this.xianbk = xianbk;
	}
	
	
}

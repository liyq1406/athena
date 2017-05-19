
package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 资源快照表
 * @author 李智
 * @version 1.0
 * @date 2011-2-9
 * 
 */
public class Ziykzb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	private String ID;

	
	/**
	 * 资源获取日期
	 */
	private String ziyhqrq;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 零件编号
	 */
	private String lingjbh;
		
	/**
	 * 零件号
	 */
	private String LINGJH;
	
	/**
	 * 零件名称
	 */
	private String lingjmc;
	
	/**
	 * 仓库代码
	 */
	private String cangkdm;
	
	/**
	 * 库存数量
	 */
	private BigDecimal kucsl;
	
	/**
	 * 订单累计
	 */
	private BigDecimal dingdlj;
	
	
	/**
	 * 交付累计
	 */
	private BigDecimal jiaoflj;
	
	/**
	 * 订单终止累积
	 */
	private BigDecimal dingdzzlj; 
	
	/**
	 * 安全库存天数
	 */
	private String anqkcts;
	
	/**
	 * 系统调整值
	 */
	private BigDecimal xttzz;
	
	/**
	 * 计算调整数值
	 */
	private String jstzsz;
	
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	/**
	 * 安全库存
	 */
	private BigDecimal anqkc;

	/**
	 * 子仓库编号
	 */
	private String zickbh;
	

	public BigDecimal getAnqkc() {
		return anqkc;
	}

	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getZiyhqrq() {
		return ziyhqrq;
	}

	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLINGJH() {
		return LINGJH;
	}

	public void setLINGJH(String lINGJH) {
		LINGJH = lINGJH;
	}

	public String getCangkdm() {
		return cangkdm;
	}

	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}


	public BigDecimal getKucsl() {
		return kucsl;
	}

	public void setKucsl(BigDecimal kucsl) {
		this.kucsl = kucsl;
	}

	public BigDecimal getDingdlj() {
		return dingdlj;
	}

	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}

	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}

	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}

	public BigDecimal getDingdzzlj() {
		return dingdzzlj;
	}

	public void setDingdzzlj(BigDecimal dingdzzlj) {
		this.dingdzzlj = dingdzzlj;
	}

	public String getAnqkcts() {
		return anqkcts;
	}

	public void setAnqkcts(String anqkcts) {
		this.anqkcts = anqkcts;
	}

	public BigDecimal getXttzz() {
		return xttzz;
	}

	public void setXttzz(BigDecimal xttzz) {
		this.xttzz = xttzz;
	}

	public String getJstzsz() {
		return jstzsz;
	}

	public void setJstzsz(String jstzsz) {
		this.jstzsz = jstzsz;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
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
	
	
}
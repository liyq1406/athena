package com.athena.xqjs.entity.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;

/**
 * 
 * @author wuyichao
 * @see 库存分析
 * 
 */
public class Kucfx extends PageableSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1110255257137477211L;

	private String usercenter;

	private String lingjbh;

	private String shiycj; // 使用车间

	private String cangkdm; // 仓库代码

	private BigDecimal kucsl; // 库存数量

	private BigDecimal xuqsl; // 需求数量

	private BigDecimal workts; // 工作天数

	private BigDecimal zworkts; // 总工作天数

	private BigDecimal xuqslAfter; // 需求数量

	private BigDecimal worktsAfter; // 工作天数

	private BigDecimal zworktsAfter; // 总工作天数

	private BigDecimal cmj;

	private BigDecimal tians; // 天数

	private String jihy; // 计划员

	private BigDecimal ysgsl; // 杨四港数量

	private BigDecimal zhongxqsl; // 重箱区107数量

	private String creator;

	private Date createtime;

	private String zhizlx; // 制造路线

	private String nianzq;

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

	public String getShiycj() {
		return shiycj;
	}

	public void setShiycj(String shiycj) {
		this.shiycj = shiycj;
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

	public BigDecimal getCmj() {
		return cmj;
	}

	public void setCmj(BigDecimal cmj) {
		this.cmj = cmj;
	}

	public BigDecimal getTians() {
		return tians;
	}

	public void setTians(BigDecimal tians) {
		this.tians = tians;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public BigDecimal getYsgsl() {
		return ysgsl;
	}

	public void setYsgsl(BigDecimal ysgsl) {
		this.ysgsl = ysgsl;
	}

	public BigDecimal getZhongxqsl() {
		return zhongxqsl;
	}

	public void setZhongxqsl(BigDecimal zhongxqsl) {
		this.zhongxqsl = zhongxqsl;
	}

	public BigDecimal getXuqsl() {
		return xuqsl;
	}

	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}

	public BigDecimal getWorkts() {
		return workts;
	}

	public void setWorkts(BigDecimal workts) {
		this.workts = workts;
	}

	public BigDecimal getZworkts() {
		return zworkts;
	}

	public void setZworkts(BigDecimal zworkts) {
		this.zworkts = zworkts;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public BigDecimal getXuqslAfter() {
		return xuqslAfter;
	}

	public void setXuqslAfter(BigDecimal xuqslAfter) {
		this.xuqslAfter = xuqslAfter;
	}

	public BigDecimal getWorktsAfter() {
		return worktsAfter;
	}

	public void setWorktsAfter(BigDecimal worktsAfter) {
		this.worktsAfter = worktsAfter;
	}

	public BigDecimal getZworktsAfter() {
		return zworktsAfter;
	}

	public void setZworktsAfter(BigDecimal zworktsAfter) {
		this.zworktsAfter = zworktsAfter;
	}

	public String getNianzq() {
		return nianzq;
	}

	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

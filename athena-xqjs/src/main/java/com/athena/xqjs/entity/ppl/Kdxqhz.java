package com.athena.xqjs.entity.ppl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/*
 * kd毛需求汇总参考系bean
 * author   夏晖
 * date     2011-12-7
 */
public class Kdxqhz extends PageableSupport {
	private static final long serialVersionUID = 1L;
	private String nianzq        ; //所属年周期
	private String usercenter     ; //用户中心  
	private String lingjbh        ;//零件编号
	private String zhizlx         ; //制造路线
    private String gongysdm       ; //供应商代码
    private String gongysmc       ; //供应商名称
    private String  lingjmc       ;//零件名称
    private String  jihy          ;//计划员
    private String  dinghcj       ;//订货车间
    private BigDecimal  baozrl    ;//包装容量
    private String   baozlx       ;//包装类型
	private String sx             ; //周序
	private BigDecimal xuqsl      ; //需求数量
	private String danw           ; //单位 
	private String id             ; //主键
	private String  active        ; //删除标识
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNianzq() {
		return nianzq;
	}
	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
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
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
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
	public String getLingjmc() {
		return lingjmc;
	}
	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	public String getJihy() {
		return jihy;
	}
	public void setJihy(String jihy) {
		this.jihy = jihy;
	}
	public String getDinghcj() {
		return dinghcj;
	}
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	public BigDecimal getBaozrl() {
		return baozrl;
	}
	public void setBaozrl(BigDecimal baozrl) {
		this.baozrl = baozrl;
	}
	public String getBaozlx() {
		return baozlx;
	}
	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}
	public String getSx() {
		return sx;
	}
	public void setSx(String sx) {
		this.sx = sx;
	}
	public BigDecimal getXuqsl() {
		return xuqsl;
	}
	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	
	

	                             


}

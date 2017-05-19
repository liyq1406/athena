package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * @按需毛需求bean
 * @author   李明
 * @date     2012-3-19
 */
public class AnxMaoxqzjb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private String usercenter ;
	private String ofh ;
	private String zhongzlxh ;
	private String hanzbs ;
	private String emon ;
	private String shunxh ;
	private String lingjbh;
	private String danw;
	private String xiaohd;
	private String xhsj;
	private BigDecimal xiaohxs ;
	private String xqly ;
	private String chejh ;
	private String chanx ;
	private String zhizlx ;
	private String caifsj ;
	private String chaifsj ;
	private String xuqbc ;
	//额外增加字段、
	private String mintime ;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getOfh() {
		return ofh;
	}
	public void setOfh(String ofh) {
		this.ofh = ofh;
	}
	public String getZhongzlxh() {
		return zhongzlxh;
	}
	public void setZhongzlxh(String zhongzlxh) {
		this.zhongzlxh = zhongzlxh;
	}
	public String getHanzbs() {
		return hanzbs;
	}
	public void setHanzbs(String hanzbs) {
		this.hanzbs = hanzbs;
	}
	public String getEmon() {
		return emon;
	}
	public void setEmon(String emon) {
		this.emon = emon;
	}
	public String getShunxh() {
		return shunxh;
	}
	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getXhsj() {
		return xhsj;
	}
	public void setXhsj(String xhsj) {
		this.xhsj = xhsj;
	}
	public BigDecimal getXiaohxs() {
		return xiaohxs;
	}
	public void setXiaohxs(BigDecimal xiaohxs) {
		this.xiaohxs = xiaohxs;
	}
	public String getXqly() {
		return xqly;
	}
	public void setXqly(String xqly) {
		this.xqly = xqly;
	}
	public String getChejh() {
		return chejh;
	}
	public void setChejh(String chejh) {
		this.chejh = chejh;
	}
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getCaifsj() {
		return caifsj;
	}
	public void setCaifsj(String caifsj) {
		this.caifsj = caifsj;
	}
	public String getChaifsj() {
		return chaifsj;
	}
	public void setChaifsj(String chaifsj) {
		this.chaifsj = chaifsj;
	}
	public String getXuqbc() {
		return xuqbc;
	}
	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}
	public String getMintime() {
		return mintime;
	}
	public void setMintime(String mintime) {
		this.mintime = mintime;
	}
	
}

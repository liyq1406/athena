package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:物流路径实体类
 * </p>
 * <p>
 * Description:物流路径实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-1-4
 */
public class Wullj extends PageableSupport {

	private static final long serialVersionUID = -6416990806688863683L;
	private String usercenter;
	private String fenpqh;
	private String lingjbh;
	private String gongysbh;
	private String lujbh;
	private String lujmc;
	private String fahd;
	private String waibms;
	private String zhidgys;
	private String jiaofm;
	private BigDecimal beihzq;
	private BigDecimal yunszq;
	private String gcbh;
	private String xiehztbh;
	private BigDecimal songhpc;
	private String mudd;
	private String dinghck;
	private String mos2;
	private BigDecimal cangkshpc2;
	private BigDecimal cangkshsj2;
	private BigDecimal cangkfhsj2;
	private BigDecimal beihsj2;
	private BigDecimal ibeihsj2;
	private BigDecimal pbeihsj2;
	private String xianbck;
	private String mos;
	private BigDecimal cangkshpc;
	private BigDecimal cangkshsj;
	private BigDecimal cangkfhsj;
	private BigDecimal beihsj;
	private BigDecimal ibeihsj;
	private BigDecimal pbeihsj;
	private String shengcxbh ;
	private String creator ;
	private String create_time ;
	private String editor ;
	private String edit_time ;
	private BigDecimal beihsjc ;
	private String zhizlx ;

	private String jianglms2 ;
	private String shengxsj2;
	private String jianglms;
	private String shengxsj;
	private String wulgyyz;
	private String wulgyyz1;
	private String wulgyyz2;
	
	
	public String getWulgyyz() {
		return wulgyyz;
	}
	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}
	public String getWulgyyz1() {
		return wulgyyz1;
	}
	public void setWulgyyz1(String wulgyyz1) {
		this.wulgyyz1 = wulgyyz1;
	}
	public String getWulgyyz2() {
		return wulgyyz2;
	}
	public void setWulgyyz2(String wulgyyz2) {
		this.wulgyyz2 = wulgyyz2;
	}
	public String getJianglms2() {
		return jianglms2;
	}
	public void setJianglms2(String jianglms2) {
		this.jianglms2 = jianglms2;
	}
	public String getShengxsj2() {
		return shengxsj2;
	}
	public void setShengxsj2(String shengxsj2) {
		this.shengxsj2 = shengxsj2;
	}
	public String getJianglms() {
		return jianglms;
	}
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	/*
	 * 方便计算，额外增加字段，xiaohd
	 * */
	private String xiaohd ;
	
	
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
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
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public BigDecimal getBeihsjc() {
		return beihsjc;
	}
	public void setBeihsjc(BigDecimal beihsjc) {
		this.beihsjc = beihsjc;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getLujbh() {
		return lujbh;
	}
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	public String getLujmc() {
		return lujmc;
	}
	public void setLujmc(String lujmc) {
		this.lujmc = lujmc;
	}
	public String getFahd() {
		return fahd;
	}
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	public String getWaibms() {
		return waibms;
	}
	public void setWaibms(String waibms) {
		this.waibms = waibms;
	}
	public String getZhidgys() {
		return zhidgys;
	}
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	public BigDecimal getYunszq() {
		return yunszq;
	}
	public void setYunszq(BigDecimal yunszq) {
		this.yunszq = yunszq;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public BigDecimal getSonghpc() {
		return songhpc;
	}
	public void setSonghpc(BigDecimal songhpc) {
		this.songhpc = songhpc;
	}
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	public String getDinghck() {
		return dinghck;
	}
	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}
	public String getMos2() {
		return mos2;
	}
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	public BigDecimal getCangkshpc2() {
		return cangkshpc2;
	}
	public void setCangkshpc2(BigDecimal cangkshpc2) {
		this.cangkshpc2 = cangkshpc2;
	}
	public BigDecimal getCangkshsj2() {
		return cangkshsj2;
	}
	public void setCangkshsj2(BigDecimal cangkshsj2) {
		this.cangkshsj2 = cangkshsj2;
	}
	public BigDecimal getCangkfhsj2() {
		return cangkfhsj2;
	}
	public void setCangkfhsj2(BigDecimal cangkfhsj2) {
		this.cangkfhsj2 = cangkfhsj2;
	}
	public BigDecimal getBeihsj2() {
		return beihsj2;
	}
	public void setBeihsj2(BigDecimal beihsj2) {
		this.beihsj2 = beihsj2;
	}
	public BigDecimal getIbeihsj2() {
		return ibeihsj2;
	}
	public void setIbeihsj2(BigDecimal ibeihsj2) {
		this.ibeihsj2 = ibeihsj2;
	}
	public BigDecimal getPbeihsj2() {
		return pbeihsj2;
	}
	public void setPbeihsj2(BigDecimal pbeihsj2) {
		this.pbeihsj2 = pbeihsj2;
	}
	public String getXianbck() {
		return xianbck;
	}
	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public BigDecimal getCangkshpc() {
		return cangkshpc;
	}
	public void setCangkshpc(BigDecimal cangkshpc) {
		this.cangkshpc = cangkshpc;
	}
	public BigDecimal getCangkshsj() {
		return cangkshsj;
	}
	public void setCangkshsj(BigDecimal cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	public BigDecimal getCangkfhsj() {
		return cangkfhsj;
	}
	public void setCangkfhsj(BigDecimal cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	public BigDecimal getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(BigDecimal beihsj) {
		this.beihsj = beihsj;
	}
	public BigDecimal getIbeihsj() {
		return ibeihsj;
	}
	public void setIbeihsj(BigDecimal ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	public BigDecimal getPbeihsj() {
		return pbeihsj;
	}
	public void setPbeihsj(BigDecimal pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	
}

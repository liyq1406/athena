package com.athena.pc.entity;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Qickc extends PageableSupport  implements Domain{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7747528786420255322L;

	private String usercenter;   //用户中心
	private String shij;         //时间
	private String biaos;        //标识（Y=月模拟，R=日滚动，G=滚动周期模拟）
	private BigDecimal jinxq;    //净需求
	private String lingjbh;      //零件编号
	private BigDecimal lingjsl;  //零件数量（计划排产量）
	private String anqkc;        //安全库存
	private BigDecimal kcsl;     //库存数量（当前库存）
	private BigDecimal maoxq;    //毛需求（待消耗）
	private BigDecimal qickc;    //期初库存
	private BigDecimal kucbz;    //库存比值
	private BigDecimal beicsl;    //库存比值
	private  BigDecimal yaohsl;   //发交数量
	private  BigDecimal ruksl;   //入库数量
	

	public BigDecimal getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(BigDecimal yaohsl) {
		this.yaohsl = yaohsl;
	}

	public BigDecimal getRuksl() {
		return ruksl;
	}

	public void setRuksl(BigDecimal ruksl) {
		this.ruksl = ruksl;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public BigDecimal getJinxq() {
		return jinxq;
	}

	public void setJinxq(BigDecimal jinxq) {
		this.jinxq = jinxq;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public BigDecimal getKucbz() {
		return kucbz;
	}

	public void setKucbz(BigDecimal kucbz) {
		this.kucbz = kucbz;
	}

	public String getShij() {
		return shij;
	}

	public void setShij(String shij) {
		this.shij = shij;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public BigDecimal getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}

	

	public String getAnqkc() {
		return anqkc;
	}

	public void setAnqkc(String anqkc) {
		this.anqkc = anqkc;
	}

	public BigDecimal getQickc() {
		return qickc;
	}

	public void setQickc(BigDecimal qickc) {
		this.qickc = qickc;
	}

	public BigDecimal getKcsl() {
		return kcsl;
	}

	public void setKcsl(BigDecimal kcsl) {
		this.kcsl = kcsl;
	}

	public BigDecimal getMaoxq() {
		return maoxq;
	}

	public void setMaoxq(BigDecimal maoxq) {
		this.maoxq = maoxq;
	}


	public BigDecimal getBeicsl() {
		return beicsl;
	}

	public void setBeicsl(BigDecimal beicsl) {
		this.beicsl = beicsl;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		
		return null;
	}

}

package com.athena.xqjs.entity.diaobl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Yicsbcz extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private String usercenter ;
	private String liush ;
	private String lingjbh;
	private String shengbd;
	private String caozlx;
	private BigDecimal caozsl ; 
	private String caozsj ; 
	private String flag;
	
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLiush() {
		return liush;
	}
	public void setLiush(String liush) {
		this.liush = liush;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getShengbd() {
		return shengbd;
	}
	public void setShengbd(String shengbd) {
		this.shengbd = shengbd;
	}
	public String getCaozlx() {
		return caozlx;
	}
	public void setCaozlx(String caozlx) {
		this.caozlx = caozlx;
	}
	public BigDecimal getCaozsl() {
		return caozsl;
	}
	public void setCaozsl(BigDecimal caozsl) {
		this.caozsl = caozsl;
	}
	public String getCaozsj() {
		return caozsj;
	}
	public void setCaozsj(String caozsj) {
		this.caozsj = caozsj;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
	
	
	
	
}

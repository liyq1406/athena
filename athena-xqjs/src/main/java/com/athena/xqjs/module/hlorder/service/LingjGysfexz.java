package com.athena.xqjs.module.hlorder.service;

import java.math.BigDecimal;

public class LingjGysfexz {
	private String lingjbh; //零件编号 
	private String gongysbh; //供应商编号 
	private BigDecimal gongyfe; //合同设置的供应份额
	private BigDecimal gongyljfe; //供应累计的份额
	private BigDecimal bencgongyfe; //本次的供应份额
	private Boolean sfzyh = true;// 是否还需要跟其他供应商要货 true : 要货; false : 不要货
	public Boolean getSfzyh() {
		return sfzyh;
	}
	public void setSfzyh(Boolean sfzyh) {
		this.sfzyh = sfzyh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public BigDecimal getGongyfe() {
		return gongyfe;
	}
	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}
	public BigDecimal getGongyljfe() {
		return gongyljfe;
	}
	public void setGongyljfe(BigDecimal gongyljfe) {
		this.gongyljfe = gongyljfe;
	}
	public BigDecimal getBencgongyfe() {
		return bencgongyfe;
	}
	public void setBencgongyfe(BigDecimal bencgongyfe) {
		this.bencgongyfe = bencgongyfe;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	
	
	
	

}

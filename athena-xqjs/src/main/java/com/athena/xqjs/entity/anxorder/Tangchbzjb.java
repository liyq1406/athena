package com.athena.xqjs.entity.anxorder;

import com.toft.core3.support.PageableSupport;

public class Tangchbzjb extends PageableSupport {

	/**
	 * 小火车合并中间表
	 * @author zbb
	 */
	private static final long serialVersionUID = -7846501501576153614L;

	private String usercenter;
	private String chanx;
	private String gongysbh;
	private String xiaohcbh;
	private Integer tangc;
	private String dangqtcrq;
	private Integer guijtc;
	private String guijtcrq;
	private Integer hebtc;
	private Integer liush;
	
	public Tangchbzjb() {
		super();
	}
	public Tangchbzjb(String usercenter, String chanx, String gongysbh,
			String xiaohcbh, Integer tangc, String dangqtcrq, Integer guijtc,
			String guijtcrq, Integer hebtc, Integer liush) {
		super();
		this.usercenter = usercenter;
		this.chanx = chanx;
		this.gongysbh = gongysbh;
		this.xiaohcbh = xiaohcbh;
		this.tangc = tangc;
		this.dangqtcrq = dangqtcrq;
		this.guijtc = guijtc;
		this.guijtcrq = guijtcrq;
		this.hebtc = hebtc;
		this.liush = liush;
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
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public Integer getTangc() {
		return tangc;
	}
	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}
	public String getDangqtcrq() {
		return dangqtcrq;
	}
	public void setDangqtcrq(String dangqtcrq) {
		this.dangqtcrq = dangqtcrq;
	}
	public Integer getGuijtc() {
		return guijtc;
	}
	public void setGuijtc(Integer guijtc) {
		this.guijtc = guijtc;
	}
	public String getGuijtcrq() {
		return guijtcrq;
	}
	public void setGuijtcrq(String guijtcrq) {
		this.guijtcrq = guijtcrq;
	}
	public Integer getHebtc() {
		return hebtc;
	}
	public void setHebtc(Integer hebtc) {
		this.hebtc = hebtc;
	}
	public Integer getLiush() {
		return liush;
	}
	public void setLiush(Integer liush) {
		this.liush = liush;
	}
	
}

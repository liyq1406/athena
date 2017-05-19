package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

public class CdSdMaoxuq {

	private String usercenter; // 用户中心
	private String lingjbh; // 零件编号
	private String chanx; // 产线
	private String xiaohd; // 消耗点
	private BigDecimal xiaohbl; // 消耗点比例
	private String zhizlx; // 制造路线
	private String mos; // 模式
	private String dinghck; // 订货仓库
	private String xianbck; // 线边仓库
	private String fenpqbh; // 分配循环
	private BigDecimal xianbllkc;//线边理论库存

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

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public BigDecimal getXiaohbl() {
		return xiaohbl;
	}

	public void setXiaohbl(BigDecimal xiaohbl) {
		this.xiaohbl = xiaohbl;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getMos() {
		return mos;
	}

	public void setMos(String mos) {
		this.mos = mos;
	}

	public String getDinghck() {
		return dinghck;
	}

	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}

	public String getXianbck() {
		return xianbck;
	}

	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}

	public String getFenpqbh() {
		return fenpqbh;
	}

	public void setFenpqbh(String fenpqbh) {
		this.fenpqbh = fenpqbh;
	}

	public BigDecimal getXianbllkc() {
		return xianbllkc;
	}

	public void setXianbllkc(BigDecimal xianbllkc) {
		this.xianbllkc = xianbllkc;
	}

}

/**
 * 根据nup单量份拆分出毛需求结果
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;


public class Clddxx2TmpNup {
	private String usercenter;//用户中心
	
	private String scxh;//生产线号
	
	private String lingjbh;//零件编号
	
	private BigDecimal shul;//n.shul*t.shul
	
	private String zhizlx;
	
	private String chej;//车间
	
	private String danw;//单位
	
	private Date yjsxsj;
	
	private String zijsycj;

	public Date getYjsxsj() {
		return yjsxsj;
	}

	public void setYjsxsj(Date yjsxsj) {
		this.yjsxsj = yjsxsj;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getScxh() {
		return scxh;
	}

	public void setScxh(String scxh) {
		this.scxh = scxh;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public BigDecimal getShul() {
		return shul;
	}

	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getChej() {
		return chej;
	}

	public void setChej(String chej) {
		this.chej = chej;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getZijsycj() {
		return zijsycj;
	}

	public void setZijsycj(String zijsycj) {
		this.zijsycj = zijsycj;
	}

	
}
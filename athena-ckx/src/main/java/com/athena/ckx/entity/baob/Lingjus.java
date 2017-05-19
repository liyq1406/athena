

package com.athena.ckx.entity.baob;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


public class Lingjus extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String 	   usercenter;	//用户中心
	
	private String 	   lingjbh;
	
	private String     zhuangtsx;
	
	private BigDecimal lingjsl;
	
	private String     usxh;
	
	private BigDecimal usrl;
	
	private String     cangkbh;
	
	private String     zickbh;
	
	private String     cangklx;
	
	private String     ush;
	
	private String     elh;
	
	private String     rukrq;
	
	private String     zhizlx;

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

	public String getZhuangtsx() {
		return zhuangtsx;
	}

	public void setZhuangtsx(String zhuangtsx) {
		this.zhuangtsx = zhuangtsx;
	}

	public BigDecimal getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getUsxh() {
		return usxh;
	}

	public void setUsxh(String usxh) {
		this.usxh = usxh;
	}

	public BigDecimal getUsrl() {
		return usrl;
	}

	public void setUsrl(BigDecimal usrl) {
		this.usrl = usrl;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	public String getUsh() {
		return ush;
	}

	public void setUsh(String ush) {
		this.ush = ush;
	}

	public String getElh() {
		return elh;
	}

	public void setElh(String elh) {
		this.elh = elh;
	}

	public String getRukrq() {
		return rukrq;
	}

	public void setRukrq(String rukrq) {
		this.rukrq = rukrq;
	}
	
	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

}

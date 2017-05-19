package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/*
 * 过点设置
 * @author zbb
 * @date 2016-1-11
 */
public class Guodsz extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 3108964907949042741L;
	private String usercenter;
	private String shangxd;
	private String xiaxd;
	private String leix; 
	private String chejcx;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getShangxd() {
		return shangxd;
	}
	public void setShangxd(String shangxd) {
		this.shangxd = shangxd;
	}
	public String getXiaxd() {
		return xiaxd;
	}
	public void setXiaxd(String xiaxd) {
		this.xiaxd = xiaxd;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getChejcx() {
		return chejcx;
	}
	public void setChejcx(String chejcx) {
		this.chejcx = chejcx;
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

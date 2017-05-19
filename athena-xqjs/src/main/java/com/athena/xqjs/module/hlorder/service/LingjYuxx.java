/**
 * 零件盈余计算结果保存类
 */
package com.athena.xqjs.module.hlorder.service;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class LingjYuxx {
	private String gongysdm; //供应商代码
	private BigDecimal quzyhl; //取整要货量
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public BigDecimal getQuzyhl() {
		return quzyhl;
	}
	public void setQuzyhl(BigDecimal quzyhl) {
		this.quzyhl = quzyhl;
	}
	

}

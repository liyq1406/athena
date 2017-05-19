package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 排产计算日期
 * @author dsimedd001
 *
 */
public class CalcDay extends PageableSupport{

	private static final long serialVersionUID = 4137156212367804721L;

	/**
	 * 线号
	 */
	private String xianh;

	/**
	 * 日期
	 */
	private String riq;

	/**
	 * 是否工作日
	 */
	private String shifgzr;

	/**
	 * 工时
	 */
	private Double gongs;

	public String getXianh() {
		return xianh;
	}

	public void setXianh(String xianh) {
		this.xianh = xianh;
	}

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public String getShifgzr() {
		return shifgzr;
	}

	public void setShifgzr(String shifgzr) {
		this.shifgzr = shifgzr;
	}

	public Double getGongs() {
		return gongs;
	}

	public void setGongs(Double gongs) {
		this.gongs = gongs;
	}

}

package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 排产数量
 * @author dsimedd001
 *
 */
public class Paicsl extends PageableSupport {

	private static final long serialVersionUID = -5031074463641994189L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 线号
	 */
	private String xianh;
	
	/**
	 * 日期
	 */
	private String riq;

	/**
	 * 计划上线量
	 */
	private int jihsxl;

	/**
	 * 计划下线量（用于排空）
	 */
	private int jihxxl;

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

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

	public int getJihsxl() {
		return jihsxl;
	}

	public void setJihsxl(int jihsxl) {
		this.jihsxl = jihsxl;
	}

	public int getJihxxl() {
		return jihxxl;
	}

	public void setJihxxl(int jihxxl) {
		this.jihxxl = jihxxl;
	}

}

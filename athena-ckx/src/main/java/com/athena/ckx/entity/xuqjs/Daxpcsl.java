package com.athena.ckx.entity.xuqjs;

import com.toft.core3.support.PageableSupport;

/**
 * 大线排产数量
 * @author dsimedd001
 *
 */
public class Daxpcsl extends PageableSupport{

	private static final long serialVersionUID = -8547001961304315667L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 车间
	 */
	private String chej;

	/**
	 * 大线线号
	 */
	private String daxxh;

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

	/**
	 * 备注
	 */
	private String beiz;
	
	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChej() {
		return chej;
	}

	public void setChej(String chej) {
		this.chej = chej;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
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

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


}

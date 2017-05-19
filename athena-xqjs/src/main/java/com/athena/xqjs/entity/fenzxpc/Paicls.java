package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 排产流水
 * @author dsimedd001
 *
 */
public class Paicls extends PageableSupport {

	private static final long serialVersionUID = -1788479302840111418L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 大线线号
	 */
	private String daxxh;

	/**
	 * 分装线号
	 */
	private String fenzxh;

	/**
	 * 最后计算车辆（订单号）
	 */
	private String zuihjscl;

	/**
	 * 分装线总顺序号
	 */
	private String fenzxzsxh;

	/**
	 * 排产开始时间
	 */
	private String paickssj;

	/**
	 * 排产结束时间
	 */
	private String paicjssj;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;
	

	/**
	 * 插空偏移量
	 */
	private String chakpyl;




	public String getChakpyl() {
		return chakpyl;
	}

	public void setChakpyl(String chakpyl) {
		this.chakpyl = chakpyl;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getZuihjscl() {
		return zuihjscl;
	}

	public void setZuihjscl(String zuihjscl) {
		this.zuihjscl = zuihjscl;
	}

	public String getFenzxzsxh() {
		return fenzxzsxh;
	}

	public void setFenzxzsxh(String fenzxzsxh) {
		this.fenzxzsxh = fenzxzsxh;
	}

	public String getPaickssj() {
		return paickssj;
	}

	public void setPaickssj(String paickssj) {
		this.paickssj = paickssj;
	}

	public String getPaicjssj() {
		return paicjssj;
	}

	public void setPaicjssj(String paicjssj) {
		this.paicjssj = paicjssj;
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

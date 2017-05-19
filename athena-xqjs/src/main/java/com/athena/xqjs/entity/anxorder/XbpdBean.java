package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 线边盘点修正
 * 
 * @author dsimedd001
 * 
 */
public class XbpdBean extends PageableSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 产线
	 */
	private String shengcxbh;
	/**
	 * 消耗点编号
	 */
	private String xiaohdbh;
	/**
	 * 线边理论库存
	 */
	private BigDecimal xianbllkc;
	/**
	 * 总装流水号
	 */
	private String zhongzlxh;
	/**
	 * 待消耗
	 */
	private String kucsldxh;
	/**
	 * 盘点实际库存
	 */
	private String pdsjkc;
	/**
	 * 差异
	 */
	private String cy;

	/**
	 * 差异
	 */
	private String scjssj;

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

	public String getShengcxbh() {
		return shengcxbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public String getXiaohdbh() {
		return xiaohdbh;
	}

	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}

	public BigDecimal getXianbllkc() {
		return xianbllkc;
	}

	public void setXianbllkc(BigDecimal xianbllkc) {
		this.xianbllkc = xianbllkc;
	}

	public String getZhongzlxh() {
		return zhongzlxh;
	}

	public void setZhongzlxh(String zhongzlxh) {
		this.zhongzlxh = zhongzlxh;
	}

	public String getKucsldxh() {
		return kucsldxh;
	}

	public void setKucsldxh(String kucsldxh) {
		this.kucsldxh = kucsldxh;
	}

	public String getPdsjkc() {
		return pdsjkc;
	}

	public void setPdsjkc(String pdsjkc) {
		this.pdsjkc = pdsjkc;
	}

	public String getCy() {
		return cy;
	}

	public void setCy(String cy) {
		this.cy = cy;
	}

	public String getScjssj() {
		return scjssj;
	}

	public void setScjssj(String scjssj) {
		this.scjssj = scjssj;
	}
	
	


}

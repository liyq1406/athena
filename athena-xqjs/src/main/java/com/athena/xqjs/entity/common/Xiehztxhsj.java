/**
 *
 */
package com.athena.xqjs.entity.common;
import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * <p>
 * Title:卸货站台循环时间实体类
 * </p>
 * <p>
 * Description:卸货站台循环时间实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-3-27
 */
public class Xiehztxhsj extends PageableSupport{
	
	private static final long serialVersionUID = 6807755220652558230L;
	
	private String xiehztbh;//卸货站台编号
	private String usercenter;//用户中心
	private String cangkbh;//仓库编号
	private String mos;//模式
	private String shengxbs;//生效标志
	private BigDecimal beihsj ;//备货时间C
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public String getShengxbs() {
		return shengxbs;
	}
	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
	}
	public BigDecimal getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(BigDecimal beihsj) {
		this.beihsj = beihsj;
	}
	
}
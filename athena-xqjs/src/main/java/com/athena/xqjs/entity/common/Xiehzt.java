/**
 *
 */
package com.athena.xqjs.entity.common;
import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * <p>
 * Title:卸货站台实体类
 * </p>
 * <p>
 * Description:卸货站台实体类
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
public class Xiehzt extends PageableSupport{
	
	private static final long serialVersionUID = 6807755220652558230L;
	
	private String xiehztbh;//工业周期
	private String usercenter;//开始时间
	private String xiehztmc;//结束时间
	private BigDecimal yunxtqdhsj ;//允许提前到货时间（分钟）
	private BigDecimal shjgsj ;//时间间隔
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
	public String getXiehztmc() {
		return xiehztmc;
	}
	public void setXiehztmc(String xiehztmc) {
		this.xiehztmc = xiehztmc;
	}
	public BigDecimal getYunxtqdhsj() {
		return yunxtqdhsj;
	}
	public void setYunxtqdhsj(BigDecimal yunxtqdhsj) {
		this.yunxtqdhsj = yunxtqdhsj;
	}
	public BigDecimal getShjgsj() {
		return shjgsj;
	}
	public void setShjgsj(BigDecimal shjgsj) {
		this.shjgsj = shjgsj;
	}
	
}
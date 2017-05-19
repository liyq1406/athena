/**
 *
 */
package com.athena.xqjs.entity.common;
import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * <p>
 * Title:运输时刻实体类
 * </p>
 * <p>
 * Description:运输时刻实体类
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
public class Yunssk extends PageableSupport{
	
	private static final long serialVersionUID = 6807755220652558230L;
	
	private String xiehztbh;//卸货站台
	private String usercenter;//用户中心
	private String gcbh;//供应商编号
	private String fassj;//发送时间
	private String daohsj;//到货时间
	private BigDecimal xuh ;//序号
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
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getFassj() {
		return fassj;
	}
	public void setFassj(String fassj) {
		this.fassj = fassj;
	}
	public String getDaohsj() {
		return daohsj;
	}
	public void setDaohsj(String daohsj) {
		this.daohsj = daohsj;
	}
	public BigDecimal getXuh() {
		return xuh;
	}
	public void setXuh(BigDecimal xuh) {
		this.xuh = xuh;
	}
	
}
package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * 卸货站台标准工作时间
 * @author hj
 * @Date 2012-03-19
 */
public class CkxBiaozgzsj extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6790607714902341609L;
	
	private String xiehztbh;//卸货站台编号
	private Double gongzsj;//工作时间
	
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public Double getGongzsj() {
		return gongzsj;
	}
	public void setGongzsj(Double gongzsj) {
		this.gongzsj = gongzsj;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return "";
	}
	
}


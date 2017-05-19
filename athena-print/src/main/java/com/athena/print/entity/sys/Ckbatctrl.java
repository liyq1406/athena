package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 批量交易码
 * @author wangyu
 * @date 2012-1-12
 */
public class Ckbatctrl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private Integer xuh;	//序号
	
	private String batcode;		//仓库编号
	
	private String batname;		//单据编号
	
	private String batcn;	//交易码
	
	public Integer getXuh() {
		return xuh;
	}

	public void setXuh(Integer xuh) {
		this.xuh = xuh;
	}

	public String getBatcode() {
		return batcode;
	}

	public void setBatcode(String batcode) {
		this.batcode = batcode;
	}

	public String getBatname() {
		return batname;
	}

	public void setBatname(String batname) {
		this.batname = batname;
	}

	public String getBatcn() {
		return batcn;
	}

	public void setBatcn(String batcn) {
		this.batcn = batcn;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

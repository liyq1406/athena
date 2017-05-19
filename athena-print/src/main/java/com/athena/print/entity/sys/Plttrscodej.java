package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 交易码
 * @author wangyu
 * @date 2012-1-12
 */
public class Plttrscodej extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String trscode;		//仓库编号
	
	private String trsname;		//单据编号
	
	private String trstype;	//交易码
	
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getTrscode() {
		return trscode;
	}

	public void setTrscode(String trscode) {
		this.trscode = trscode;
	}

	public String getTrsname() {
		return trsname;
	}

	public void setTrsname(String trsname) {
		this.trsname = trsname;
	}

	public String getTrstype() {
		return trstype;
	}

	public void setTrstype(String trstype) {
		this.trstype = trstype;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

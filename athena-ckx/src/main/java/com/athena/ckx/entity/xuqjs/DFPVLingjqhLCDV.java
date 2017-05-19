package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件切换
 * 
 * @author CSY 2016-05-04
 */
public class DFPVLingjqhLCDV extends PageableSupport implements Domain {

	private static final long serialVersionUID = 5473820186894872128L;

	private String banbh;		//版本号
	private String tiaoj;		//切换条件
	private String beiz1;		//备注1
	private String beiz2;		//备注2
	private String beiz3;		//备注3
	
	public String getBanbh() {
		return banbh;
	}

	public void setBanbh(String banbh) {
		this.banbh = banbh;
	}

	public String getTiaoj() {
		return tiaoj;
	}

	public void setTiaoj(String tiaoj) {
		this.tiaoj = tiaoj;
	}

	public String getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return null;
	}

	public void setId(String arg0) {

	}

}

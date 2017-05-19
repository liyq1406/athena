package com.athena.ckx.entity.baob;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class JihuChengys extends PageableSupport implements Domain{

	/**
	 * 11458
	 */
	private static final long serialVersionUID = 1L;
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	private String jihy;
	private String chengys;
	private String factory_1;
	private String factory_2;
	//0011894
	private String chengyslx;
	private String chengysmc;
	
	
	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}
	public String getChengyslx() {
		return chengyslx;
	}

	public void setChengyslx(String chengyslx) {
		this.chengyslx = chengyslx;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public String getFactory_1() {
		return factory_1;
	}

	public void setFactory_1(String factory_1) {
		this.factory_1 = factory_1;
	}

	public String getFactory_2() {
		return factory_2;
	}

	public void setFactory_2(String factory_2) {
		this.factory_2 = factory_2;
	}

	public String getFactory_3() {
		return factory_3;
	}

	public void setFactory_3(String factory_3) {
		this.factory_3 = factory_3;
	}

	public String getFactory_4() {
		return factory_4;
	}

	public void setFactory_4(String factory_4) {
		this.factory_4 = factory_4;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String factory_3;
	private String factory_4;
	private long num_1;
	private long num_2;
	private long num_3;
	private long num_4;
	

	public String getChengys() {
		return chengys;
	}

	public void setChengys(String chengys) {
		this.chengys = chengys;
	}

	public long getNum_1() {
		return num_1;
	}

	public void setNum_1(long num_1) {
		this.num_1 = num_1;
	}

	public long getNum_2() {
		return num_2;
	}

	public void setNum_2(long num_2) {
		this.num_2 = num_2;
	}

	public long getNum_3() {
		return num_3;
	}

	public void setNum_3(long num_3) {
		this.num_3 = num_3;
	}

	public long getNum_4() {
		return num_4;
	}

	public void setNum_4(long num_4) {
		this.num_4 = num_4;
	}
	
	
}

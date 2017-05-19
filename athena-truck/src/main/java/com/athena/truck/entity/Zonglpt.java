package com.athena.truck.entity;

import java.util.ArrayList;
import java.util.List;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Zonglpt extends PageableSupport implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3242498244528380931L;

	
	private  List<Zonglptmx> zonglptmx = new ArrayList<Zonglptmx>();
	
	
	private String quymc;
	
	private String quybh;
	
	private String quymcs;	
	
	public String getQuymcs() {
		return quymcs;
	}

	public void setQuymcs(String quymcs) {
		this.quymcs = quymcs;
	}

	public String getShenbkc() {
		return shenbkc;
	}

	public void setShenbkc(String shenbkc) {
		this.shenbkc = shenbkc;
	}

	public String getPaidkc() {
		return paidkc;
	}

	public void setPaidkc(String paidkc) {
		this.paidkc = paidkc;
	}
	

	public String getFangkc() {
		return fangkc;
	}

	public void setFangkc(String fangkc) {
		this.fangkc = fangkc;
	}

	public String getFangkcs() {
		return fangkcs;
	}

	public void setFangkcs(String fangkcs) {
		this.fangkcs = fangkcs;
	}

	public String getDazt() {
		return dazt;
	}

	public void setDazt(String dazt) {
		this.dazt = dazt;
	}

	public String getShenbkcs() {
		return shenbkcs;
	}

	public void setShenbkcs(String shenbkcs) {
		this.shenbkcs = shenbkcs;
	}

	public String getPaidkcs() {
		return paidkcs;
	}

	public void setPaidkcs(String paidkcs) {
		this.paidkcs = paidkcs;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String shenbkc;
	
	private String paidkc;		
	
	private String fangkc;	
	
	private String dazt;
	
	private String shenbkcs;
	
	private String paidkcs;
	
	private String fangkcs;
	
	public String getQuybh() {
		return quybh;
	}

	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}

	public List<Zonglptmx> getZonglptmx() {
		return zonglptmx;
	}

	public void setZonglptmx(List<Zonglptmx> zonglptmx) {
		this.zonglptmx = zonglptmx;
	}

	public String getQuymc() {
		return quymc;
	}

	public void setQuymc(String quymc) {
		this.quymc = quymc;
	}

	public String getId() {
		
		return null;
	}

	public void setId(String arg0) {
		
		
	}
	


}

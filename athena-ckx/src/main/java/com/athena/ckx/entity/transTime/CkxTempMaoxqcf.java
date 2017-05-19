package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkxTempMaoxqcf extends PageableSupport  implements Domain{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3808994081537383702L;
	private String usercenter;
	private String fenpq;
	private String lingjbh;
	private String gongys;
	private String chengys;
	private String xiehzt;
	private Integer shul;
	private Double tij;
	private String kaissj;
	private String jiezsj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	private String maoxqbc;//毛需求版次
	
	public String getMaoxqbc() {
		return maoxqbc;
	}

	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getFenpq() {
		return fenpq;
	}

	public void setFenpq(String fenpq) {
		this.fenpq = fenpq;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getGongys() {
		return gongys;
	}

	public void setGongys(String gongys) {
		this.gongys = gongys;
	}

	public String getChengys() {
		return chengys;
	}

	public void setChengys(String chengys) {
		this.chengys = chengys;
	}

	public String getXiehzt() {
		return xiehzt;
	}

	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}

	public Integer getShul() {
		return shul;
	}

	public void setShul(Integer shul) {
		this.shul = shul;
	}

	public Double getTij() {
		return tij;
	}

	public void setTij(Double tij) {
		this.tij = tij;
	}

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiezsj() {
		return jiezsj;
	}

	public void setJiezsj(String jiezsj) {
		this.jiezsj = jiezsj;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

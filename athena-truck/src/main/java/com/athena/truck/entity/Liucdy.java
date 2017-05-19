package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-1-28
 */
public class Liucdy extends PageableSupport implements Domain{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 684769652884682482L;
	
	private String usercenter;
	private String quybh;
	private String daztbh;
	private String liucbh;
	private String liucmc;
	private String leix;
	private String biaozsj;
	private String liucbs;
	private String biaos;
	private String creator;
	private String editor;
	
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getQuybh() {
		return quybh;
	}

	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getLiucbh() {
		return liucbh;
	}

	public void setLiucbh(String liucbh) {
		this.liucbh = liucbh;
	}

	public String getLiucmc() {
		return liucmc;
	}

	public void setLiucmc(String liucmc) {
		this.liucmc = liucmc;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

	public String getBiaozsj() {
		return biaozsj;
	}

	public void setBiaozsj(String biaozsj) {
		this.biaozsj = biaozsj;
	}

	public String getLiucbs() {
		return liucbs;
	}

	public void setLiucbs(String liucbs) {
		this.liucbs = liucbs;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}

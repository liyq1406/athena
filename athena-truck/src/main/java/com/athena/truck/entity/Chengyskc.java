package com.athena.truck.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 承运商-卡车
 * @author CSY
 * @date   20160908
 *
 */
public class Chengyskc extends PageableSupport implements Domain{

	private static final long serialVersionUID = 3196732868065228459L;
	
	private String kach;		//卡车号
	private String chengys;		//承运商
	private String biaos;		//标识 0-无效 1-有效
	private String creator;		//创建人
	private Date create_time;	//创建时间
	private String editor;		//修改人
	private Date edit_time;		//修改时间
	private String beiz1;		//备注1
	private String beiz2;		//备注2
	private String beiz3;		//备注3
	private Date beiz4;			//备注4
	private int beiz5;			//备注5
	private String denglzt;		//卡车号
	private String chengysmc;	//承运商名称
	
	public String getChengysmc() {
		return chengysmc;
	}
	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}
	public String getKach() {
		return kach;
	}
	public void setKach(String kach) {
		this.kach = kach;
	}
	public String getChengys() {
		return chengys;
	}
	public void setChengys(String chengys) {
		this.chengys = chengys;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
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
	public Date getBeiz4() {
		return beiz4;
	}
	public void setBeiz4(Date beiz4) {
		this.beiz4 = beiz4;
	}
	public int getBeiz5() {
		return beiz5;
	}
	public void setBeiz5(int beiz5) {
		this.beiz5 = beiz5;
	}
	public String getDenglzt() {
		return denglzt;
	}
	public void setDenglzt(String denglzt) {
		this.denglzt = denglzt;
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

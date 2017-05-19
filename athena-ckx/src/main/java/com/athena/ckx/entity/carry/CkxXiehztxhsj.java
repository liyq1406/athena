package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 卸货站台循环时间
 * @author kong
 *
 */
public class CkxXiehztxhsj extends PageableSupport  implements Domain{

	private static final long serialVersionUID = 8835841197170795917L;
	private String usercenter;
	private String cangkbh;
	private String xiehztbh;
	private String mos;
	private Double beihsj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	private String shengxbs;
	
	
	
	
	
	public String getShengxbs() {
		return shengxbs;
	}

	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getXiehztbh() {
		return xiehztbh;
	}

	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}

	public String getMos() {
		return mos;
	}

	public void setMos(String mos) {
		this.mos = mos;
	}

	public Double getBeihsj() {
		return beihsj;
	}

	public void setBeihsj(Double beihsj) {
		this.beihsj = beihsj;
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

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return "";
	}

}

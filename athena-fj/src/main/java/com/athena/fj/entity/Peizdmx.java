package com.athena.fj.entity;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:配载单明细实体类
 * </p>
 * <p>
 * Description:定义配载单明细实体变量
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-1-11
 */
public class Peizdmx extends PageableSupport implements Domain {

	
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8982890271608134063L;
	 
	private String beihdh;        //备货单号
	private String peizdh;        //配载单号
	private BigDecimal baozsl;    //包装数量
	private String cangkbh;       //仓库编号
	private String zickbh;         //发货站台fahzt  update fahzt->zickbh(子仓库编号) 2012-7-3 hzg
	private String creator;       //P_创建人
	private String createTime;   //P_创建时间
	private String editor;        //P_修改人
	private String editTime;     //P_修改时间
	
	public String getBeihdh() {
		return beihdh;
	}

	public void setBeihdh(String beihlh) {
		this.beihdh = beihlh;
	}

	public String getPeizdh() {
		return peizdh;
	}

	public void setPeizdh(String peizdh) {
		this.peizdh = peizdh;
	}

	public BigDecimal getBaozsl() {
		return baozsl;
	}

	public void setBaozsl(BigDecimal baozsl) {
		this.baozsl = baozsl;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
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

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub

	}

}

package com.athena.fj.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:装车发运实体类
 * </p>
 * <p>
 * Description:定义装车发运实体变量格式
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
 * @date 2012-1-17
 */
public class Zhuangcfy extends PageableSupport implements Domain {

	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -512078775464834264L;

	
	private String zhuangcdh;   //装车单号
	private String usercenter;  //用户中心
	private String peizdh;      //配载单号
	private String yunssbm;     //运输商编码
	private String gongsmc;     //运输商名称
	private String jihcx;       //计划车型
	private String chep;        //车牌
	private String gongysbm;    //供应商编码
	private String creator;     //P_创建人
	private String createTime; //P_创建时间
	private String editor;      //P_修改人
	private String editTime;   //P_修改时间
	
	private String cangkbh;    //仓库编号
	
	private String yaohlhs;    //要货令号，以'',''用于in查询
	
	private String daodTime;   //到达时间
	public String getYaohlhs() {
		return yaohlhs;
	}

	public void setYaohlhs(String yaohlhs) {
		this.yaohlhs = yaohlhs;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getZhuangcdh() {
		return zhuangcdh;
	}

	public void setZhuangcdh(String zhuangcdh) {
		this.zhuangcdh = zhuangcdh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getPeizdh() {
		return peizdh;
	}

	public void setPeizdh(String peizdh) {
		this.peizdh = peizdh;
	}

	public String getYunssbm() {
		return yunssbm;
	}

	public void setYunssbm(String yunssbm) {
		this.yunssbm = yunssbm;
	}

	public String getJihcx() {
		return jihcx;
	}

	public void setJihcx(String jihcx) {
		this.jihcx = jihcx;
	}

	public String getChep() {
		return chep;
	}

	public void setChep(String chep) {
		this.chep = chep;
	}

	public String getGongysbm() {
		return gongysbm;
	}

	public void setGongysbm(String gongysbm) {
		this.gongysbm = gongysbm;
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

	public String getDaodTime() {
		return daodTime;
	}

	public void setDaodTime(String daodTime) {
		this.daodTime = daodTime;
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

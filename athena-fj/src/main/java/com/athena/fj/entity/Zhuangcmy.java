package com.athena.fj.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:装车明细实体类
 * </p>
 * <p>
 * Description:定义装车明细实体变量格式
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
public class Zhuangcmy extends PageableSupport implements Domain {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 5801402954836633421L;

	private String uch;        //UC号
	private String zhuangcdh;  //装车单号
	private String uskh;       //US卡号
	private String kehbm;      //客户编码
	private String baozdm;     //包装代码
	private String lingjmc;    //零件名称
	private String lingjsl;    //零件数量
	private String lingjbh;    //零件编号
	private String beihlh;     //备货令号
	private String cangkbh;    //仓库编号
	private String xiehd;      //卸货点
	private String yaohlbh;    //要货令编号
	private String creator;    //P_创建人
	private String createTime;//P_创建时间
	private String editor;     //P_修改人
	private String editTime;  //P_修改时间
	
	private String uah;      //UA号
	private String uaxh;     //UA型号
	
	private String gongysdm;  //供应商代码
	private String gongysmc;  //供应商名称
	private String danw;      //单位
	private String pich;      //批次号
	private String ucxh;      //UC型号
	private String ucgs;      //UC个数
	private String ucrl;      //UC容量
	

	public String getUah() {
		return uah;
	}

	public void setUah(String uah) {
		this.uah = uah;
	}

	public String getUaxh() {
		return uaxh;
	}

	public void setUaxh(String uaxh) {
		this.uaxh = uaxh;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getPich() {
		return pich;
	}

	public void setPich(String pich) {
		this.pich = pich;
	}

	public String getUcxh() {
		return ucxh;
	}

	public void setUcxh(String ucxh) {
		this.ucxh = ucxh;
	}

	public String getUcgs() {
		return ucgs;
	}

	public void setUcgs(String ucgs) {
		this.ucgs = ucgs;
	}

	public String getUcrl() {
		return ucrl;
	}

	public void setUcrl(String ucrl) {
		this.ucrl = ucrl;
	}

	public String getUch() {
		return uch;
	}

	public void setUch(String uch) {
		this.uch = uch;
	}

	public String getZhuangcdh() {
		return zhuangcdh;
	}

	public void setZhuangcdh(String zhuangcdh) {
		this.zhuangcdh = zhuangcdh;
	}

	public String getUskh() {
		return uskh;
	}

	public void setUskh(String uskh) {
		this.uskh = uskh;
	}

	public String getKehbm() {
		return kehbm;
	}

	public void setKehbm(String kehbm) {
		this.kehbm = kehbm;
	}

	public String getBaozdm() {
		return baozdm;
	}

	public void setBaozdm(String baozdm) {
		this.baozdm = baozdm;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getBeihlh() {
		return beihlh;
	}

	public void setBeihlh(String beihlh) {
		this.beihlh = beihlh;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getXiehd() {
		return xiehd;
	}

	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}

	public String getYaohlbh() {
		return yaohlbh;
	}

	public void setYaohlbh(String yaohlbh) {
		this.yaohlbh = yaohlbh;
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

package com.athena.truck.entity;

import com.toft.core3.support.PageableSupport;

/*
 * 车辆排队信息
 */
public class Chelpd extends PageableSupport {
	private static final long serialVersionUID = 1L;
	private String usercenter	;       //用户中心
	private String yundh	    ;       //运单号
	private String kach	      ;     	//卡车号
	private String chewbh     ;         //车位号
	private String chengysbh  ;         //承运商编号
	private String chengysmc  ;         //承运商名称
	private String quybh      ;         //等待区域编号
	private String daztbh     ;         //大站台编号
	private String paidzt     ;         //排队状态（0排队，1准入）
	private Integer paidxh     ;         //排队序号（置顶序号为0，重复置顶负数）
	private String xiehxh     ;         //卸货序号
	private String paidsx     ;         //排队属性（1普通，2专用，3急件 ）
	private String yuanpdsx	  ;         //原排队属性（1普通，2专用，3急件 ）
	private String zdchew     ;         //指定车位
	private String beiz       ;         //备注
	private String beiz1      ;         //备注1
	private String beiz2      ;         //备注2
	private String beiz3      ;         //备注3
	private String creator    ;         //创建人
	private String create_time;         //创建时间
	private String editor     ;         //修改人
	private String edit_time  ;         //修改时间
	private String neweditor  ;         //新修改人
	private String liucbh     ;         //流程编号
	private String liucmc     ;         //流程名称
	private String zhuangt    ;         //运单状态
	private String zhuangtmc  ;         //运单状态名称
	public String getZhuangtmc() {
		return zhuangtmc;
	}
	public String getChengysmc() {
		return chengysmc;
	}
	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}
	public void setZhuangtmc(String zhuangtmc) {
		this.zhuangtmc = zhuangtmc;
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
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getNeweditor() {
		return neweditor;
	}
	public void setNeweditor(String neweditor) {
		this.neweditor = neweditor;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getYundh() {
		return yundh;
	}
	public void setYundh(String yundh) {
		this.yundh = yundh;
	}
	public String getKach() {
		return kach;
	}
	public void setKach(String kach) {
		this.kach = kach;
	}
	public String getChewbh() {
		return chewbh;
	}
	public void setChewbh(String chewbh) {
		this.chewbh = chewbh;
	}
	public String getChengysbh() {
		return chengysbh;
	}
	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
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
	public String getPaidzt() {
		return paidzt;
	}
	public void setPaidzt(String paidzt) {
		this.paidzt = paidzt;
	}
	public Integer getPaidxh() {
		return paidxh;
	}
	public void setPaidxh(Integer paidxh) {
		this.paidxh = paidxh;
	}
	public String getXiehxh() {
		return xiehxh;
	}
	public void setXiehxh(String xiehxh) {
		this.xiehxh = xiehxh;
	}
	public String getPaidsx() {
		return paidsx;
	}
	public void setPaidsx(String paidsx) {
		this.paidsx = paidsx;
	}
	public String getYuanpdsx() {
		return yuanpdsx;
	}
	public void setYuanpdsx(String yuanpdsx) {
		this.yuanpdsx = yuanpdsx;
	}
	public String getZdchew() {
		return zdchew;
	}
	public void setZdchew(String zdchew) {
		this.zdchew = zdchew;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
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
}

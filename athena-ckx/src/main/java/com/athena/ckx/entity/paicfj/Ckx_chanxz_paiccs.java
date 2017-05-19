package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_chanxz_paiccs extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String banb           ;//版本
	private String chanxzbh       ;//产线组编号
	private String usercenter     ;//用户中心
	private String gundzq         ;//滚动周期
	private String tiqq           ;//排产提前期
	private String fengbq         ;//封闭期
	private String zengcts        ;//增产天数
	private String shifzskcsx     ;//是否遵守库存上线
	private Double dagdw          ;//生产大纲最小时间单位（分钟）
	private String creator;       //创建人
	private Date   create_time;   //创建时间
	private String editor;        //修改人
	private Date  edit_time;      //修改时间
	
	
	public String getShifzskcsx() {
		return shifzskcsx;
	}

	public void setShifzskcsx(String shifzskcsx) {
		this.shifzskcsx = shifzskcsx;
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

	public String getBanb() {
		return banb;
	}

	public void setBanb(String banb) {
		this.banb = banb;
	}

	public String getChanxzbh() {
		return chanxzbh;
	}

	public void setChanxzbh(String chanxzbh) {
		this.chanxzbh = chanxzbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getGundzq() {
		return gundzq;
	}

	public void setGundzq(String gundzq) {
		this.gundzq = gundzq;
	}

	public String getTiqq() {
		return tiqq;
	}

	public void setTiqq(String tiqq) {
		this.tiqq = tiqq;
	}

	public String getFengbq() {
		return fengbq;
	}

	public void setFengbq(String fengbq) {
		this.fengbq = fengbq;
	}

	public String getZengcts() {
		return zengcts;
	}

	public void setZengcts(String zengcts) {
		this.zengcts = zengcts;
	}

	
	public Double getDagdw() {
		return dagdw;
	}

	public void setDagdw(Double dagdw) {
		this.dagdw = dagdw;
	}


	
	public void setId(String id) {
	}

	public String getId() {
		return null;
	}

}

package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 仓库
 * @author denggq
 * @date 2012-1-12
 */
public class Cangk extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String cangklx;		//仓库类型
	
	private String rongqcbh;	//容器场编号
	
	private String jihyzbh;		//计划员组编号（删掉）
	
	private String wulgyyz;		//物流工艺员组编号
	
	private Double daoctqq;		//到车提前期(排产)
	
	private String zhantlx;     //站台类型
	
	private Double zuidkctsmrz; //最大库存天数默认值
	
	private Double anqkctsmrz;  //安全库存天数默认值
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String role;		//角色
	
	private String xiehztbz;   //卸货站台编组
	
	private String wulgyyzmc;  //物流工艺员组名称
	
	private String chukms;     //出库模式(H:H模式,A:其它模式)
	
	private String gongc;		//工厂
	
	private String xianh;		//线号
	
	public String getChukms() {
		return chukms;
	}

	public void setChukms(String chukms) {
		this.chukms = chukms;
	}

	public String getWulgyyzmc() {
		return wulgyyzmc;
	}

	public void setWulgyyzmc(String wulgyyzmc) {
		this.wulgyyzmc = wulgyyzmc;
	}

	public String getXiehztbz() {
		return xiehztbz;
	}

	public void setXiehztbz(String xiehztbz) {
		this.xiehztbz = xiehztbz;
	}

	public String getZhantlx() {
		return zhantlx;
	}

	public void setZhantlx(String zhantlx) {
		this.zhantlx = zhantlx;
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

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	public String getRongqcbh() {
		return rongqcbh;
	}

	public void setRongqcbh(String rongqcbh) {
		this.rongqcbh = rongqcbh;
	}

	public void setJihyzbh(String jihyzbh) {
		this.jihyzbh = jihyzbh;
	}

	public String getJihyzbh() {
		return jihyzbh;
	}

	public Double getDaoctqq() {
		return daoctqq;
	}

	public void setDaoctqq(Double daoctqq) {
		this.daoctqq = daoctqq;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getWulgyyz() {
		return wulgyyz;
	}

	public Double getZuidkctsmrz() {
		return zuidkctsmrz;
	}

	public void setZuidkctsmrz(Double zuidkctsmrz) {
		this.zuidkctsmrz = zuidkctsmrz;
	}

	public Double getAnqkctsmrz() {
		return anqkctsmrz;
	}

	public void setAnqkctsmrz(Double anqkctsmrz) {
		this.anqkctsmrz = anqkctsmrz;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

	public String getGongc() {
		return gongc;
	}

	public void setGongc(String gongc) {
		this.gongc = gongc;
	}

	public String getXianh() {
		return xianh;
	}

	public void setXianh(String xianh) {
		this.xianh = xianh;
	}

}

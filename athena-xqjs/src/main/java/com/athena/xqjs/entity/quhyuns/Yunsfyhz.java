package com.athena.xqjs.entity.quhyuns;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Yunsfyhz extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;		//用户名称
	
	private String danjh;		//单据号
	
	private String danjmc;		//单据名称
	
	private String danjlx;		//单据类型
	
	private String shenhzt;		//审核状态
	
	private String banch;		//版次号
	
	private Double yunszfy;		//运输总费用
	
	private Double yunsfy;  	//运输费用

	private Double fankfy;		//返空费用
	
	private Double jinjfy;		//紧急费用
	
	private Double lingxfy;		//零星费用
	
	private Double tuopfy;		//托盘费用
	
	private Double tuopfkfy;	//托盘返空费用
	
	private String yusr;		//预审人
	
	private String yus_time;		//预审时间
	
	private String chusr;		//初审人
	
	private String chus_time;		//初审时间
	
	private String zhongsr;		//终审人
	
	private String zhongs_time;		//终审时间
	
	private String  biaos;

	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	
	private Double zhengxlfmdj;//正向立方米单价
	
	private Double fanxlfmdj;//反向立方米单价
	
	private String gongysdm;	//供应商代码
	
	private String gongysmc;	//承运商名称
	
	private String chengysdm;	//承运商代码


	private Double zhengxztj;//正向总体积
	
	
	private Double fanxztj;//反向总体积
	
	
	private Double zhengxzfy;//正向总费用
	
	
	private Double fanxzfy;//反向总费用
	
	
	private String jinjjdjh;//紧急件单据号
	
	private String  save_time;//重算保存时间
	private String  chongs_time; //重算时间
	
	
	private String shenhkssj;  //审核开始时间	
	
	private String shenhjssj;	//审核结束时间
	
	
	private String uclist; //用户组对应的有权限的用户中心
	
	
	private String lingxing1; //零星类别
	
	private String lingxing2; 
	
	private String lingxing3; 
	
	private String lingxing4; 
	
	private String lingxing5; 
	
	private String lingxing6; 
	
	private String lingxing7; 
	
	private String lingxing8;
	
	private String lingxing9; 
	
	private String lingxing10;
	
	private String flag;//计算状态
	
	
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getLingxing1() {
		return lingxing1;
	}

	public void setLingxing1(String lingxing1) {
		this.lingxing1 = lingxing1;
	}

	public String getLingxing2() {
		return lingxing2;
	}

	public void setLingxing2(String lingxing2) {
		this.lingxing2 = lingxing2;
	}

	public String getLingxing3() {
		return lingxing3;
	}

	public void setLingxing3(String lingxing3) {
		this.lingxing3 = lingxing3;
	}

	public String getLingxing4() {
		return lingxing4;
	}

	public void setLingxing4(String lingxing4) {
		this.lingxing4 = lingxing4;
	}

	public String getLingxing5() {
		return lingxing5;
	}

	public void setLingxing5(String lingxing5) {
		this.lingxing5 = lingxing5;
	}

	public String getLingxing6() {
		return lingxing6;
	}

	public void setLingxing6(String lingxing6) {
		this.lingxing6 = lingxing6;
	}

	public String getLingxing7() {
		return lingxing7;
	}

	public void setLingxing7(String lingxing7) {
		this.lingxing7 = lingxing7;
	}

	public String getLingxing8() {
		return lingxing8;
	}

	public void setLingxing8(String lingxing8) {
		this.lingxing8 = lingxing8;
	}

	public String getLingxing9() {
		return lingxing9;
	}

	public void setLingxing9(String lingxing9) {
		this.lingxing9 = lingxing9;
	}

	public String getLingxing10() {
		return lingxing10;
	}

	public void setLingxing10(String lingxing10) {
		this.lingxing10 = lingxing10;
	}

	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}

	public String getShenhkssj() {
		return shenhkssj;
	}

	public void setShenhkssj(String shenhkssj) {
		this.shenhkssj = shenhkssj;
	}

	public String getShenhjssj() {
		return shenhjssj;
	}

	public void setShenhjssj(String shenhjssj) {
		this.shenhjssj = shenhjssj;
	}

	public String getSave_time() {
		return save_time;
	}

	public void setSave_time(String save_time) {
		this.save_time = save_time;
	}

	public String getChongs_time() {
		return chongs_time;
	}

	public void setChongs_time(String chongs_time) {
		this.chongs_time = chongs_time;
	}

	public String getJinjjdjh() {
		return jinjjdjh;
	}

	public void setJinjjdjh(String jinjjdjh) {
		this.jinjjdjh = jinjjdjh;
	}

	public Double getZhengxztj() {
		return zhengxztj;
	}

	public void setZhengxztj(Double zhengxztj) {
		this.zhengxztj = zhengxztj;
	}

	public Double getFanxztj() {
		return fanxztj;
	}

	public void setFanxztj(Double fanxztj) {
		this.fanxztj = fanxztj;
	}

	public Double getZhengxzfy() {
		return zhengxzfy;
	}

	public void setZhengxzfy(Double zhengxzfy) {
		this.zhengxzfy = zhengxzfy;
	}

	public Double getFanxzfy() {
		return fanxzfy;
	}

	public void setFanxzfy(Double fanxzfy) {
		this.fanxzfy = fanxzfy;
	}

	public Double getZhengxlfmdj() {
		return zhengxlfmdj;
	}

	public void setZhengxlfmdj(Double zhengxlfmdj) {
		this.zhengxlfmdj = zhengxlfmdj;
	}

	public Double getFanxlfmdj() {
		return fanxlfmdj;
	}

	public void setFanxlfmdj(Double fanxlfmdj) {
		this.fanxlfmdj = fanxlfmdj;
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

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
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

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public Double getYunszfy() {
		return yunszfy;
	}

	public void setYunszfy(Double yunszfy) {
		this.yunszfy = yunszfy;
	}

	public Double getYunsfy() {
		return yunsfy;
	}

	public void setYunsfy(Double yunsfy) {
		this.yunsfy = yunsfy;
	}

	public Double getFankfy() {
		return fankfy;
	}

	public void setFankfy(Double fankfy) {
		this.fankfy = fankfy;
	}

	public Double getJinjfy() {
		return jinjfy;
	}

	public void setJinjfy(Double jinjfy) {
		this.jinjfy = jinjfy;
	}

	public Double getLingxfy() {
		return lingxfy;
	}

	public void setLingxfy(Double lingxfy) {
		this.lingxfy = lingxfy;
	}

	public Double getTuopfy() {
		return tuopfy;
	}

	public void setTuopfy(Double tuopfy) {
		this.tuopfy = tuopfy;
	}

	public Double getTuopfkfy() {
		return tuopfkfy;
	}

	public void setTuopfkfy(Double tuopfkfy) {
		this.tuopfkfy = tuopfkfy;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDanjh() {
		return danjh;
	}

	public void setDanjh(String danjh) {
		this.danjh = danjh;
	}

	public String getDanjmc() {
		return danjmc;
	}

	public void setDanjmc(String danjmc) {
		this.danjmc = danjmc;
	}

	public String getDanjlx() {
		return danjlx;
	}

	public void setDanjlx(String danjlx) {
		this.danjlx = danjlx;
	}

	public String getShenhzt() {
		return shenhzt;
	}

	public void setShenhzt(String shenhzt) {
		this.shenhzt = shenhzt;
	}

	public String getBanch() {
		return banch;
	}

	public void setBanch(String banch) {
		this.banch = banch;
	}

	

	
	public String getYusr() {
		return yusr;
	}

	public void setYusr(String yusr) {
		this.yusr = yusr;
	}

	public String getYus_time() {
		return yus_time;
	}

	public void setYus_time(String yus_time) {
		this.yus_time = yus_time;
	}

	public String getChusr() {
		return chusr;
	}

	public void setChusr(String chusr) {
		this.chusr = chusr;
	}

	public String getChus_time() {
		return chus_time;
	}

	public void setChus_time(String chus_time) {
		this.chus_time = chus_time;
	}

	public String getZhongsr() {
		return zhongsr;
	}

	public void setZhongsr(String zhongsr) {
		this.zhongsr = zhongsr;
	}

	public String getZhongs_time() {
		return zhongs_time;
	}

	public void setZhongs_time(String zhongs_time) {
		this.zhongs_time = zhongs_time;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

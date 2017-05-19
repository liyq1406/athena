package com.athena.ckx.entity.baob;

 

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Beihlgz extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;

	private  String beihdh;		//备货单号
	
	private String beihdzt;    //备货单状态
	
	private String bhsj;   //备货时间
	
	private String shangxsj;    //最晚上线时间
	
	private String usercenter;    //用户中心
	
	private String chanx;      //产线
	 
	private String cangkbh;       //仓库
	
	private String zickbh;   //子仓库
	
	private String lingjbh;       //零件编号
	
	private String lingjmc;   //零件名称
	
	private String ush;    //US号
	
	private String elh;   //EL号
	
	private String kuwbh;    //库位编号
	
	private String beihsl;    //备货数量
	
	private String gongysdm;      //供应商代码
	 
	private String gongysmc;       //供应商名称
	
	private String yaohlh;   //要货令号
	
	private String yaohsl;       //要货数量
	
	private String keh;   //客户
	
	private String xiehd;   //卸货点
	
	private String editor;       //要货令操作者
	
	private String createtime;
	
	private String create_time;
	
	private String finishedtime;
	
	
	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getFinishedtime() {
		return finishedtime;
	}

	public void setFinishedtime(String finishedtime) {
		this.finishedtime = finishedtime;
	}

	public String getBeihdh() {
		return beihdh;
	}

	public void setBeihdh(String beihdh) {
		this.beihdh = beihdh;
	}

	

	public String getBeihdzt() {
		return beihdzt;
	}

	public void setBeihdzt(String beihdzt) {
		this.beihdzt = beihdzt;
	}

	public String getBhsj() {
		return bhsj;
	}

	public void setBhsj(String bhsj) {
		this.bhsj = bhsj;
	}

	public String getShangxsj() {
		return shangxsj;
	}

	public void setShangxsj(String shangxsj) {
		this.shangxsj = shangxsj;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
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

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getUsh() {
		return ush;
	}

	public void setUsh(String ush) {
		this.ush = ush;
	}

	public String getElh() {
		return elh;
	}

	public void setElh(String elh) {
		this.elh = elh;
	}

	public String getKuwbh() {
		return kuwbh;
	}

	public void setKuwbh(String kuwbh) {
		this.kuwbh = kuwbh;
	}

	public String getBeihsl() {
		return beihsl;
	}

	public void setBeihsl(String beihsl) {
		this.beihsl = beihsl;
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

	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(String yaohsl) {
		this.yaohsl = yaohsl;
	}

	public String getKeh() {
		return keh;
	}

	public void setKeh(String keh) {
		this.keh = keh;
	}

	public String getXiehd() {
		return xiehd;
	}

	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
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

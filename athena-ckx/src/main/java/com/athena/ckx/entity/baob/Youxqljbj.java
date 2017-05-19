package com.athena.ckx.entity.baob;



import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Youxqljbj extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;

	private String usercenter;
	
	private  String lingjbh;		//零件编号
	
	private String lingjmc;    //零件名称
	
	private String gongysdm;   //供应商代码
	
	private String gongysmc;    //供应商名称
	
	private String shixrq;    //失效日期
	
	private String pich;      //批次号
	 
	private String ucgs;       //UC个数
	
	private String cangkbh;       //仓库
	
	private String zickbh;   //子仓库
	
	private String elh;    //EL号
	
	private String ush;   //US号
	
	private String kuwbh;//库位编号
	
	private String lingjsl;//零件数量
	
	private String rukrq; //入库日期
	
	private String editime;

	private String baojtqq;
	
	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBaojtqq() {
		return baojtqq;
	}

	public void setBaojtqq(String baojtqq) {
		this.baojtqq = baojtqq;
	}

	public String getEditime() {
		return editime;
	}

	public void setEditime(String editime) {
		this.editime = editime;
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

	public String getShixrq() {
		return shixrq;
	}

	public void setShixrq(String shixrq) {
		this.shixrq = shixrq;
	}

	public String getPich() {
		return pich;
	}

	public void setPich(String pich) {
		this.pich = pich;
	}

	public String getUcgs() {
		return ucgs;
	}

	public void setUcgs(String ucgs) {
		this.ucgs = ucgs;
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

	public String getElh() {
		return elh;
	}

	public void setElh(String elh) {
		this.elh = elh;
	}

	public String getUsh() {
		return ush;
	}

	public void setUsh(String ush) {
		this.ush = ush;
	}

	public String getKuwbh() {
		return kuwbh;
	}

	public void setKuwbh(String kuwbh) {
		this.kuwbh = kuwbh;
	}

	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getRukrq() {
		return rukrq;
	}

	public void setRukrq(String rukrq) {
		this.rukrq = rukrq;
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

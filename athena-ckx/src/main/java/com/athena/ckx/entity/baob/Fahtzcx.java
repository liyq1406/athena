package com.athena.ckx.entity.baob;



import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Fahtzcx extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;

	private  String lingjbh;		//零件编号
	
	private String usercenter;
	
	private String blh;    //发货通知号(BL)
	
	private String tch;   //集装箱号
	
	private String lingjsl;    //零件数量
	
	private String dingdh;    //订单号
	
	private String blscsj;      //发运时间
	 
	private String cangkbh;       //目的地
	
	//private String yunsms;   //运输方式
	
	private String uarl;       //包装数量
	
	private String gongysdm;   //供应商代码
	
	private String yaohlh;    //要货令
	
	private String jihy;   //计划员
	
	private String createtime;
	
	private String finishedtime;
	

	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getBlh() {
		return blh;
	}

	public void setBlh(String blh) {
		this.blh = blh;
	}

	public String getTch() {
		return tch;
	}

	public void setTch(String tch) {
		this.tch = tch;
	}

	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getDingdh() {
		return dingdh;
	}

	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}

	public String getBlscsj() {
		return blscsj;
	}

	public void setBlscsj(String blscsj) {
		this.blscsj = blscsj;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getUarl() {
		return uarl;
	}

	public void setUarl(String uarl) {
		this.uarl = uarl;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
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

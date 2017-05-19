package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Gongysfe extends PageableSupport implements Domain{

	private static final long serialVersionUID = -789942336451874994L;
	
	private String usercenter;		//用户中心
	
	private String lingjbh;			//零件编号
	
	private String gongysdm;		//供应商代码
	
	private Double biaodsl;			//表达数量
	
	private String edit_time;		//修改时间
	
	private String feneczsj;		//份额重置时间
	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public Double getBiaodsl() {
		return biaodsl;
	}

	public void setBiaodsl(Double biaodsl) {
		this.biaodsl = biaodsl;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFeneczsj() {
		return feneczsj;
	}

	public void setFeneczsj(String feneczsj) {
		this.feneczsj = feneczsj;
	}

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}

}

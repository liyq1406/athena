package com.athena.ckx.entity.baob;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件-计划员-供应商

 */
public class Lingjjhygys extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String lingjbh;    //零件编号
	
	private String gongysbh;   //供应商编号
	
	private String gongsmc;    //供应商名称（公司名称）
	
	private String gongyfe;    //供应份额
	
	private String fahd;      //发货地
	 
	private String mudd;       //目的地
	
	private String chengysbh;   //承运商
	
	private String jihy;       //计划员
	
	private String zhongwmc;   //零件名称（中文名称）
	

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

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getGongyfe() {
		return gongyfe;
	}

	public void setGongyfe(String gongyfe) {
		this.gongyfe = gongyfe;
	}

	

	public String getFahd() {
		return fahd;
	}

	public void setFahd(String fahd) {
		this.fahd = fahd;
	}

	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	

	public String getZhongwmc() {
		return zhongwmc;
	}

	public void setZhongwmc(String zhongwmc) {
		this.zhongwmc = zhongwmc;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}

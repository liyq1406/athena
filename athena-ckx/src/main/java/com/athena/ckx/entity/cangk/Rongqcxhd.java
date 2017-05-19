package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * @description 容器场-消耗点
 * @author wangyu
 * @String 2012-12-10
 */
public class Rongqcxhd extends PageableSupport implements Domain{

	private static final long serialVersionUID = -8255964247726895051L;

	private String gongyxhd;	//工业消耗点
	
	private Integer chessl;		//距EMON点的车身数量
	
	private Integer pianysj;		//距EMON点偏移时间
	
	private String liush;		//按需截止流水号
	
	private String gongybs;		//工艺标识
	
	private String biaos;		//标识
	
	private String shengcxbh;  //生产线编号
	
	private String creator		;//	  创建人
	
	private String create_time	;//	  创建时间
	
	private String editor		;//	  修改人
	
	private String edit_time	;//	  修改时间
	

	private String zuh;				//组号
	
	private String rongqcbh; //容器场编号
	
	private String rongqcbhYN; //容器场编号
	
	
	public String getRongqcbhYN() {
		return rongqcbhYN;
	}
	public void setRongqcbhYN(String rongqcbhYN) {
		this.rongqcbhYN = rongqcbhYN;
	}
	public String getRongqcbh() {
		return rongqcbh;
	}
	public void setRongqcbh(String rongqcbh) {
		this.rongqcbh = rongqcbh;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	public String getZuh() {
		return zuh;
	}

	
	public String getGongyxhd() {
		return gongyxhd;
	}

	public Integer getChessl() {
		return chessl;
	}


	public Integer getPianysj() {
		return pianysj;
	}


	public String getLiush() {
		return liush;
	}


	public String getGongybs() {
		return gongybs;
	}


	public String getBiaos() {
		return biaos;
	}


	public String getCreator() {
		return creator;
	}


	public String getCreate_time() {
		return create_time;
	}


	public String getEditor() {
		return editor;
	}


	public String getEdit_time() {
		return edit_time;
	}


	public void setGongyxhd(String gongyxhd) {
		this.gongyxhd = gongyxhd;
	}


	public void setChessl(Integer chessl) {
		this.chessl = chessl;
	}


	public void setPianysj(Integer pianysj) {
		this.pianysj = pianysj;
	}


	public void setLiush(String liush) {
		this.liush = liush;
	}


	public void setGongybs(String gongybs) {
		this.gongybs = gongybs;
	}


	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	public void setEditor(String editor) {
		this.editor = editor;
	}


	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}


	@Override
	public void setId(String id) {
		
	}


	@Override
	public String getId() {
		return null;
	}
	

	
	
	
}

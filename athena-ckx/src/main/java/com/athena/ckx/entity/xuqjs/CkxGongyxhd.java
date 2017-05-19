package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * @description 工艺消耗点
 * @author denggq
 * @String 2012-4-17
 */
public class CkxGongyxhd extends PageableSupport implements Domain{

	private static final long serialVersionUID = -8255964247726895051L;

	private String gongyxhd;	//工业消耗点
	
	private Integer chessl;		//距EMON点的车身数量
	
	private Integer pianysj;		//距EMON点偏移时间
	
	private String liush;		//按需截止流水号
	
	private String gongybs;		//工艺标识
	
	private String biaos;		//标识
	
	private String shengcxbh;  //生产线编号
	
	private String ycbiaos;    //隐藏标识
	
	private String creator		;//	  创建人
	
	private String create_time	;//	  创建时间
	
	private String editor		;//	  修改人
	
	private String edit_time	;//	  修改时间
	

	private String zuh;				//组号
	
	
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public String getYcbiaos() {
		return ycbiaos;
	}
	public void setYcbiaos(String ycbiaos) {
		this.ycbiaos = ycbiaos;
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

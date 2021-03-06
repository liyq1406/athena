package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 小火车运输时刻表
 * @author denggq
 * @String 2012-4-12
 */
public class CkxXiaohcyssk extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String shengcxbh;	//生产线编号
	
	private String xiaohcbh;	//小火车编号
	
	private String riq;			//日期
	
	private Integer tangc;		//趟次
	
	private String kaisbhsj;	//开始备货时间
	
	private String chufsxsj;	//出发上线时间
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getShengcxbh() {
		return shengcxbh;
	}

	public String getXiaohcbh() {
		return xiaohcbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public Integer getTangc() {
		return tangc;
	}

	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public String getRiq() {
		return riq;
	}

	public String getKaisbhsj() {
		return kaisbhsj;
	}

	public String getChufsxsj() {
		return chufsxsj;
	}

	public void setKaisbhsj(String kaisbhsj) {
		this.kaisbhsj = kaisbhsj;
	}

	public void setChufsxsj(String chufsxsj) {
		this.chufsxsj = chufsxsj;
	}

	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}

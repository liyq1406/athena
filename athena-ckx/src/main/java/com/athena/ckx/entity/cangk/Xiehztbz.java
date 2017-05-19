package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 卸货站台
 * @author hj
 * @date 2012-8-21
 */
public class Xiehztbz extends PageableSupport implements Domain{

	private static final long serialVersionUID = 7620319809421142675L;

	
	
	private String xiehztbzh;	//卸货站台编号
	
	
	private Integer chulsj;		//处理时间（分钟）
	
	private String biaos;		//标识
	
	private Integer tongsjdcs;	//同时接待车数
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	private String wulgyyz;     //物流工艺员组
	
	private String usercenter;//用户中心
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getWulgyyz() {
		return wulgyyz;
	}

	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getXiehztbzh() {
		return xiehztbzh;
	}

	public void setXiehztbzh(String xiehztbzh) {
		this.xiehztbzh = xiehztbzh;
	}

	public Integer getChulsj() {
		return chulsj;
	}

	public void setChulsj(Integer chulsj) {
		this.chulsj = chulsj;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public Integer getTongsjdcs() {
		return tongsjdcs;
	}

	public void setTongsjdcs(Integer tongsjdcs) {
		this.tongsjdcs = tongsjdcs;
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

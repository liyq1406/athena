package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 分配区
 * @author denggq
 * 2012-3-21
 */
public class Fenpq extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 4096777548664516721L;

	private String usercenter;	//用户中心
	
	private String shengcxbh;	//生产线编号
	
	private String fenpqh;		//分配区编号
	
	private String fenpqmc;		//分配区名称
	
	private String jihyzbh;		//计划员组编号（需求计算用） 
	
	private String wulgyyz;		//物流工艺员组
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String fenzxh;		//分装线号
	
	private String uclist;      //用户组对应的有权限的用户中心   add by lc 2017.2.17
	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getShengcxbh() {
		return shengcxbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public String getFenpqh() {
		return fenpqh;
	}

	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}

	public String getFenpqmc() {
		return fenpqmc;
	}

	public void setFenpqmc(String fenpqmc) {
		this.fenpqmc = fenpqmc;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public void setJihyzbh(String jihyzbh) {
		this.jihyzbh = jihyzbh;
	}

	public String getJihyzbh() {
		return jihyzbh;
	}


	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getWulgyyz() {
		return wulgyyz;
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

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

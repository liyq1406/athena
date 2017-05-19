package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class JinjjQuhYuns extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	//紧急件取货运输费用参考系
	private String usercenter;		//用户名称
	
	private String gongysdm;		//供应商代码
	
	private String gongysmc;		//供应商名称
	
	private String chengysdm;		//承运商代码
	
	private String chengysmc;		//承运商名称
	
	private String shengxsj;		//生效时间
	
	private String shixsj;		//失效时间
	
	
	private Double tangcdj;		//趟次单价
	
	private String creator;		//创建人
	
	private String create_time;		//创建时间
	
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String biaos;	//标识
	
	
	private String jinjjid;		//主键
	
	
	private String youxsj;//有效时间
	
	private String uclist; //用户组对应的有权限的用户中心
	
	
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}
	
	public String getYouxsj() {
		return youxsj;
	}

	public void setYouxsj(String youxsj) {
		this.youxsj = youxsj;
	}

	public String getJinjjid() {
		return jinjjid;
	}

	public void setJinjjid(String jinjjid) {
		this.jinjjid = jinjjid;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public String getShengxsj() {
		return shengxsj;
	}

	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}

	public String getShixsj() {
		return shixsj;
	}

	public void setShixsj(String shixsj) {
		this.shixsj = shixsj;
	}

	public Double getTangcdj() {
		return tangcdj;
	}

	public void setTangcdj(Double tangcdj) {
		this.tangcdj = tangcdj;
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

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

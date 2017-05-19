package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 卸货站台
 * @author denggq
 * @date 2012-1-16
 */
public class Xiehzt extends PageableSupport implements Domain{

	private static final long serialVersionUID = 7620319809421142675L;

	private String usercenter;	//用户中心
	
	private String xiehztbh;	//卸货站台编号
	
	private String xiehztbhq;	//卸货站台编号
	
	private String xiehztmc;	//卸货站台名称
	
	private Integer yunxtqdhsj;	//允许提前到货时间(分钟)
	
	private Integer shjgsj;		//收货间隔时间(分钟)
	
	private String cangkbh;		//仓库编号
	
	private String gongcbm;		//工厂编码
	
	private Integer chulsj;		//处理时间（分钟）
	
	private String biaos;		//标识
	
	private Integer tongsjdcs;	//同时接待车数
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String xiehztbzh;//卸货站台编组号
	
	private String daztbh;//大站台编号 hzg 2015.1.27

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getXiehztbhq() {
		return xiehztbhq;
	}

	public void setXiehztbhq(String xiehztbhq) {
		this.xiehztbhq = xiehztbhq;
	}

	public String getXiehztbzh() {
		return xiehztbzh;
	}

	public void setXiehztbzh(String xiehztbzh) {
		this.xiehztbzh = xiehztbzh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXiehztbh() {
		return xiehztbh;
	}

	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}

	public String getXiehztmc() {
		return xiehztmc;
	}

	public void setXiehztmc(String xiehztmc) {
		this.xiehztmc = xiehztmc;
	}

	public Integer getYunxtqdhsj() {
		return yunxtqdhsj;
	}

	public void setYunxtqdhsj(Integer yunxtqdhsj) {
		this.yunxtqdhsj = yunxtqdhsj;
	}

	public Integer getShjgsj() {
		return shjgsj;
	}

	public void setShjgsj(Integer shjgsj) {
		this.shjgsj = shjgsj;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}


	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setTongsjdcs(Integer tongsjdcs) {
		this.tongsjdcs = tongsjdcs;
	}

	public Integer getTongsjdcs() {
		return tongsjdcs;
	}

	public void setGongcbm(String gongcbm) {
		this.gongcbm = gongcbm;
	}

	public String getGongcbm() {
		return gongcbm;
	}

	public void setChulsj(Integer chulsj) {
		this.chulsj = chulsj;
	}

	public Integer getChulsj() {
		return chulsj;
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

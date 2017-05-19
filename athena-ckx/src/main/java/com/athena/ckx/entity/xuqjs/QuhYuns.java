package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class QuhYuns extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	//取货运输费用参考系
	private String usercenter;		//用户名称
	
	private String gongysdm;		//供应商代码
	
	private String gongysmc;		//供应商名称
	
	private String chengysdm;		//承运商代码
	
	private String chengysmc;		//承运商名称
	
	private String shengxsj;		//生效时间
	
	private String shixsj;		//失效时间
	
	private String quhid;  //取货id


	private Double zhixqhdj;		//支线取货单价
	
	private Double quyjpdj;		//区域集配单价
	
	private Double ganxysdj;		//干线运输单价
	
	private Double cangcpsdj;		//仓储配送单价
	
	private Double fankdj;		//返空单价
	
	private String creator;		//创建人
	
	private String create_time;		//创建时间
	
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String biaos;	//标识
	
	private String youxsj;//有效时间
	
	private String uclist; //用户组对应的有权限的用户中心
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}
	public Double getZhixqhdj() {
		return zhixqhdj;
	}

	public void setZhixqhdj(Double zhixqhdj) {
		this.zhixqhdj = zhixqhdj;
	}

	public Double getQuyjpdj() {
		return quyjpdj;
	}

	public void setQuyjpdj(Double quyjpdj) {
		this.quyjpdj = quyjpdj;
	}

	public Double getGanxysdj() {
		return ganxysdj;
	}

	public void setGanxysdj(Double ganxysdj) {
		this.ganxysdj = ganxysdj;
	}

	public Double getCangcpsdj() {
		return cangcpsdj;
	}

	public void setCangcpsdj(Double cangcpsdj) {
		this.cangcpsdj = cangcpsdj;
	}

	public Double getFankdj() {
		return fankdj;
	}

	public void setFankdj(Double fankdj) {
		this.fankdj = fankdj;
	}

	public String getYouxsj() {
		return youxsj;
	}

	public void setYouxsj(String youxsj) {
		this.youxsj = youxsj;
	}

	public String getQuhid() {
		return quhid;
	}

	public void setQuhid(String quhid) {
		this.quhid = quhid;
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


	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
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

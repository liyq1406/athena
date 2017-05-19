package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Xitcsdy extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;	//用户中心
	
	private String zidlx;		//字典类型
	
	private String zidlxmc;		//字典类型名称
	
	private String zidbm;		//字典编码
	
	private String zidmc;		//字典名称
	
	private String beiz;		//备注
	
	private String shifqj;		//是否启用区间
	
	private Double qujzdz;		//区间MAX值
	
	private Double qujzxz;		//区间MIN值
	
	private Integer paix;		//排序
	
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

	public String getZidlx() {
		return zidlx;
	}

	public void setZidlx(String zidlx) {
		this.zidlx = zidlx;
	}

	public String getZidlxmc() {
		return zidlxmc;
	}

	public void setZidlxmc(String zidlxmc) {
		this.zidlxmc = zidlxmc;
	}

	public String getZidbm() {
		return zidbm;
	}

	public void setZidbm(String zidbm) {
		this.zidbm = zidbm;
	}

	public String getZidmc() {
		return zidmc;
	}

	public void setZidmc(String zidmc) {
		this.zidmc = zidmc;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getShifqj() {
		return shifqj;
	}

	public void setShifqj(String shifqj) {
		this.shifqj = shifqj;
	}

	public Double getQujzdz() {
		return qujzdz;
	}

	public void setQujzdz(Double qujzdz) {
		this.qujzdz = qujzdz;
	}

	public Double getQujzxz() {
		return qujzxz;
	}

	public void setQujzxz(Double qujzxz) {
		this.qujzxz = qujzxz;
	}

	public Integer getPaix() {
		return paix;
	}

	public void setPaix(Integer paix) {
		this.paix = paix;
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

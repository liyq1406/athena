package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Xitcsdy extends PageableSupport{
	private static final long serialVersionUID = 1L;
	private String usercenter   ; 
	private String key;
	private String value;
	private String zidlx        ; 
	private String zidbm        ; 
	private String zidlxmc      ; 
	private String zidmc        ; 
	private String beiz         ; 
	private String shifqj       ; 
	private BigDecimal qujzdz       ; 
	private BigDecimal qujzxz       ; 
	private Integer paix        ; 
	private String creator      ; 
	private String create_time  ; 
	private String editor       ; 
	private String edit_time    ; 
	
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
	public String getZidbm() {
		return zidbm;
	}
	public void setZidbm(String zidbm) {
		this.zidbm = zidbm;
	}
	public String getZidlxmc() {
		return zidlxmc;
	}
	public void setZidlxmc(String zidlxmc) {
		this.zidlxmc = zidlxmc;
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
	public BigDecimal getQujzdz() {
		return qujzdz;
	}
	public void setQujzdz(BigDecimal qujzdz) {
		this.qujzdz = qujzdz;
	}
	public BigDecimal getQujzxz() {
		return qujzxz;
	}
	public void setQujzxz(BigDecimal qujzxz) {
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


}

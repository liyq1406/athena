package com.athena.xqjs.entity.common;

import java.sql.Date;

import com.toft.core3.support.PageableSupport;

public class Jdygzq extends PageableSupport{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String	dinghlx;
   private String	suozgyzq;
   private Integer	jidzqs;
   private Integer	yugzqs;
   private String	creator;
   private Date	create_time	;
   private String	editor;
   private Date	edit_time;
   private String	dingdnr;
   
public Date getCreate_time() {
	return create_time;
}
public void setCreate_time(Date create_time) {
	this.create_time = create_time;
}
public Date getEdit_time() {
	return edit_time;
}
public void setEdit_time(Date edit_time) {
	this.edit_time = edit_time;
}
public String getDinghlx() {
	return dinghlx;
}
public void setDinghlx(String dinghlx) {
	this.dinghlx = dinghlx;
}
public String getSuozgyzq() {
	return suozgyzq;
}
public void setSuozgyzq(String suozgyzq) {
	this.suozgyzq = suozgyzq;
}
public Integer getJidzqs() {
	return jidzqs;
}
public void setJidzqs(Integer jidzqs) {
	this.jidzqs = jidzqs;
}
public Integer getYugzqs() {
	return yugzqs;
}
public void setYugzqs(Integer yugzqs) {
	this.yugzqs = yugzqs;
}
public String getCreator() {
	return creator;
}
public void setCreator(String creator) {
	this.creator = creator;
}

public String getEditor() {
	return editor;
}
public void setEditor(String editor) {
	this.editor = editor;
}

public String getDingdnr() {
	return dingdnr;
}
public void setDingdnr(String dingdnr) {
	this.dingdnr = dingdnr;
}
   

}

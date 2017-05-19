package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 既定-预告-周期
 * @author qizhongtao
 * 2012-4-16
 */
public class Jdygzq extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 2785625161063587993L;
	
	      
	private String dinghlx;       //订货路线
	
	private String suozgyzq;      //订单制作所在工业周期
	
	private Integer jidzqs;        //订单既定周期数
	
	private Integer yugzqs;        //订单预告周期数
	
	private String dingdnr;       //订单内容
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;     //修改时间
	
	
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
	public String getDingdnr() {
		return dingdnr;
	}
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

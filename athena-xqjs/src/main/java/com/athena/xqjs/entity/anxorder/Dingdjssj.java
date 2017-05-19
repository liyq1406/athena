package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;
/**
 * 订单时间计算
 * @author zbb
 *
 */
public class Dingdjssj extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3704773610668070020L;
	
	private String usercenter;
	private String dingdlx; 
	private String dingdbh; 
	private String dingdsxsj; 
	private String dingdjssj;
	private String creator; 
	private Date create_time; 
	private String editor; 
	private Date edit_time;
	private String beiz1; 
	private String beiz2; 
	private BigDecimal beiz3;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getDingdlx() {
		return dingdlx;
	}
	public void setDingdlx(String dingdlx) {
		this.dingdlx = dingdlx;
	}
	public String getDingdbh() {
		return dingdbh;
	}
	public void setDingdbh(String dingdbh) {
		this.dingdbh = dingdbh;
	}
	
	public String getDingdsxsj() {
		return dingdsxsj;
	}
	public void setDingdsxsj(String dingdsxsj) {
		this.dingdsxsj = dingdsxsj;
	}
	public String getDingdjssj() {
		return dingdjssj;
	}
	public void setDingdjssj(String dingdjssj) {
		this.dingdjssj = dingdjssj;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}
	public BigDecimal getBeiz3() {
		return beiz3;
	}
	public void setBeiz3(BigDecimal beiz3) {
		this.beiz3 = beiz3;
	}
	
	
}

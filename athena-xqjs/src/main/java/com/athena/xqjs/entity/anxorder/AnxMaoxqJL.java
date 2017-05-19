package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;
/**
 * @按需毛需求bean
 * @author   zbb
 * @date     2015-11-17
 */
public class AnxMaoxqJL extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private String usercenter ;
	//焊装标识
	private String hanzbs ;	
	private String lingjbh;
	private String danw;
	private String xiaohd;
	private String xiaohcbh;
	private Short tangc;
	private String xiaohcrq;
	private BigDecimal xiaohxs ;
	private String xuqly ;
	private String chejh ;
	private String chanx ;
	private String dangqms; //当前模式
	
	private String flag;    //处理标识	
	//创建者
	private String creator;
	//创建时间
	private Date create_time; 
	//修改者
	private String editor; 
	//修改时间
	private Date edit_time;
	
	public String getDangqms() {
		return dangqms;
	}
	public void setDangqms(String dangqms) {
		this.dangqms = dangqms;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	public String getHanzbs() {
		return hanzbs;
	}
	public void setHanzbs(String hanzbs) {
		this.hanzbs = hanzbs;
	}

	
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public Short getTangc() {
		return tangc;
	}
	public void setTangc(Short tangc) {
		this.tangc = tangc;
	}
	public String getXiaohcrq() {
		return xiaohcrq;
	}
	public void setXiaohcrq(String xiaohcrq) {
		this.xiaohcrq = xiaohcrq;
	}	
	public BigDecimal getXiaohxs() {
		return xiaohxs;
	}
	public void setXiaohxs(BigDecimal xiaohxs) {
		this.xiaohxs = xiaohxs;
	}
	public String getXuqly() {
		return xuqly;
	}
	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}
	public String getChejh() {
		return chejh;
	}
	public void setChejh(String chejh) {
		this.chejh = chejh;
	}
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
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
	
}

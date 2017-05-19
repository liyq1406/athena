package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * CMJ
 * @author denggq
 * @String 2012-3-26
 */
public class Cmj extends PageableSupport implements Domain{

	private static final long serialVersionUID = 462812396261385771L;

	private String usercenter   ;//  用户中心 
	
	private String chanxck    	;//  产线仓库编号（循环 仓库 生产线）
	
	private String lingjbh    	;//  零件编号
	
	private String gongyzq		;//  工业周期 201111
	
	private String jisrq        ;//  计算时间
	
	private Double cmj         ;//  cmj
	
	private String creator		;//	  创建人
	
	private String create_time	;//	  创建时间
	
	private String editor		;//	  修改人
	
	private String edit_time	;//	  修改时间


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getchanxck() {
		return chanxck;
	}


	public String getLingjbh() {
		return lingjbh;
	}


	public String getGongyzq() {
		return gongyzq;
	}


	public String getJisrq() {
		return jisrq;
	}


	public Double getCmj() {
		return cmj;
	}


	public void setchanxck(String chanxck) {
		this.chanxck = chanxck;
	}


	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}


	public void setGongyzq(String gongyzq) {
		this.gongyzq = gongyzq;
	}


	public void setJisrq(String jisrq) {
		this.jisrq = jisrq;
	}


	public void setCmj(Double cmj) {
		this.cmj = cmj;
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


	@Override
	public void setId(String id) {
		
	}


	@Override
	public String getId() {
		return null;
	}
	

	
	
	
}

package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_peizbzbh extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {
	}

	public String getId() {
		return null;
	}

	 private String  baozlx            ;//包装类型
	 private String  baozzbh           ;//包装组编号
	 private String  creator           ;//创建人
	 private Date    create_time       ;//创建时间
	 private String  editor            ;//修改人
	 private Date    edit_time         ;//修改时间

	 
	 private String changd;
	 private String kuand;
	 private String gaod;
	 private String baozzl;
	 private String caiz;
	 private String zhedgd;
	 private String duidcs;
	 
	public String getChangd() {
		return changd;
	}

	public void setChangd(String changd) {
		this.changd = changd;
	}

	public String getKuand() {
		return kuand;
	}

	public void setKuand(String kuand) {
		this.kuand = kuand;
	}

	public String getGaod() {
		return gaod;
	}

	public void setGaod(String gaod) {
		this.gaod = gaod;
	}

	public String getBaozzl() {
		return baozzl;
	}

	public void setBaozzl(String baozzl) {
		this.baozzl = baozzl;
	}

	public String getCaiz() {
		return caiz;
	}

	public void setCaiz(String caiz) {
		this.caiz = caiz;
	}

	public String getZhedgd() {
		return zhedgd;
	}

	public void setZhedgd(String zhedgd) {
		this.zhedgd = zhedgd;
	}

	public String getDuidcs() {
		return duidcs;
	}

	public void setDuidcs(String duidcs) {
		this.duidcs = duidcs;
	}

	public String getBaozlx() {
		return baozlx;
	}

	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}

	public String getBaozzbh() {
		return baozzbh;
	}

	public void setBaozzbh(String baozzbh) {
		this.baozzbh = baozzbh;
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

package com.athena.ckx.entity.baob;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
//V4_041
public class Anxbb extends PageableSupport implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}
	private String gongc;
	public String getGongc() {
		return gongc;
	}

	public void setGongc(String gongc) {
		this.gongc = gongc;
	}
	private String factory;
	private String yaohllx;
	private String flag;
	private long   shul;
	private long   jihgcsl;
	private String tongjrq;
	private int biaos;
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
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

	public int getBiaos() {
		return biaos;
	}

	public void setBiaos(int biaos) {
		this.biaos = biaos;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getYaohllx() {
		return yaohllx;
	}

	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getShul() {
		return shul;
	}

	public void setShul(long shul) {
		this.shul = shul;
	}

	public long getJihgcsl() {
		return jihgcsl;
	}

	public void setJihgcsl(long jihgcsl) {
		this.jihgcsl = jihgcsl;
	}

	public String getTongjrq() {
		return tongjrq;
	}

	public void setTongjrq(String tongjrq) {
		this.tongjrq = tongjrq;
	}

}

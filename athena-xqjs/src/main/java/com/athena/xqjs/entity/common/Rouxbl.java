package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:柔性比例
 * </p>
 * <p>
 * Description:柔性比例
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-3-6
 */
public class Rouxbl extends PageableSupport{
	private static final long serialVersionUID = -6416990806688863683L;
	private String  usercenter	;	
	private String gongysbh ;
	private String guanjljjb ;
	private String dinghzq ;
	private BigDecimal rouxbl ;
	private String creator ;
	private String create_time ;
	private String editor ;
	private String edit_time ;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public String getGuanjljjb() {
		return guanjljjb;
	}
	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}
	
	public String getDinghzq() {
		return dinghzq;
	}
	public void setDinghzq(String dinghzq) {
		this.dinghzq = dinghzq;
	}
	public BigDecimal getRouxbl() {
		return rouxbl;
	}
	public void setRouxbl(BigDecimal rouxbl) {
		this.rouxbl = rouxbl;
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
	
}

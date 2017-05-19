package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 交互码设定
 * @author denggq
 * 2012-4-6
 */
public class Jiaofmsd extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -5833857958093933169L;

	private String usercenter;	//用户中心
	
	private String jiaofm;		//交付码
	
	private String zhouxh;		//周序号
	
	private String xingqxh;		//星期序号
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	public String getJiaofm() {
		return jiaofm;
	}

	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getZhouxh() {
		return zhouxh;
	}

	public String getXingqxh() {
		return xingqxh;
	}

	public void setZhouxh(String zhouxh) {
		this.zhouxh = zhouxh;
	}

	public void setXingqxh(String xingqxh) {
		this.xingqxh = xingqxh;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

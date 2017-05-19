package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 交互日历
 * @author denggq
 * 2012-4-6
 */
public class CkxJiaofrl extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1273930224548134768L;

	private String usercenter;	//用户中心
	
	private String jiaofm;		//交付码
	
	private String ri;			//日
	
	private String nianzq;		//年周期
	
	private String zhoux;		//周序
	
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

	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}

	public String getNianzq() {
		return nianzq;
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

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getRi() {
		return ri;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

	public void setZhoux(String zhoux) {
		this.zhoux = zhoux;
	}

	public String getZhoux() {
		return zhoux;
	}
	
	
	
}

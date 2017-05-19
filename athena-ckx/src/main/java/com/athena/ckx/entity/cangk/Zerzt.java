package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 责任主体
 * @author wangyu
 * @date 2014-2-19
 */
public class Zerzt extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String zrztdm;		//物流商代码
	
	private String zrztlx;		//物流商类型
	
	private String zrztmc;	//物流商名称
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	
	public String getZrztdm() {
		return zrztdm;
	}

	public void setZrztdm(String zrztdm) {
		this.zrztdm = zrztdm;
	}

	public String getZrztlx() {
		return zrztlx;
	}

	public void setZrztlx(String zrztlx) {
		this.zrztlx = zrztlx;
	}

	public String getZrztmc() {
		return zrztmc;
	}

	public void setZrztmc(String zrztmc) {
		this.zrztmc = zrztmc;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}

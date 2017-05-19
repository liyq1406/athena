package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 库位等级与包装
 * @author denggq
 * @date 2012-1-17
 */
public class KuwdjBaoz extends PageableSupport implements Domain{

	private static final long serialVersionUID = -269196014034274771L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String kuwdjbh;		//库位等级编号
	
	private String baozlx;		//包装类型
	
	private Integer baozgs;		//包装个数
	
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

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getKuwdjbh() {
		return kuwdjbh;
	}

	public void setKuwdjbh(String kuwdjbh) {
		this.kuwdjbh = kuwdjbh;
	}

	public String getBaozlx() {
		return baozlx;
	}

	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}

	public Integer getBaozgs() {
		return baozgs;
	}

	public void setBaozgs(Integer baozgs) {
		this.baozgs = baozgs;
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

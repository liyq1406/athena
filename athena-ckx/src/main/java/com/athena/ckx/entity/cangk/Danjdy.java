package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 单据打印
 * @author denggq
 * @date 2012-1-30
 */
public class Danjdy extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1544078392399969177L;

	private String usercenter;	//用户中心
	
	private String kehbh;		//客户编号
	
	private String cangkbh;		//仓库编号
	
	private String danjlx;		//单据类型
	
	private String shifdy;		//是否打印
	
	private Integer dayls;		//打印联数
	
	private Integer dayfs;		//打印份数
	
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

	public String getKehbh() {
		return kehbh;
	}

	public void setKehbh(String kehbh) {
		this.kehbh = kehbh;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getDanjlx() {
		return danjlx;
	}

	public void setDanjlx(String danjlx) {
		this.danjlx = danjlx;
	}

	public String getShifdy() {
		return shifdy;
	}

	public void setShifdy(String shifdy) {
		this.shifdy = shifdy;
	}

	public Integer getDayls() {
		return dayls;
	}

	public void setDayls(Integer dayls) {
		this.dayls = dayls;
	}

	public Integer getDayfs() {
		return dayfs;
	}

	public void setDayfs(Integer dayfs) {
		this.dayfs = dayfs;
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

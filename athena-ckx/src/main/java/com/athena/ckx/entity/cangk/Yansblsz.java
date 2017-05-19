package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件类型验收比例设置
 * @author denggq
 * @date 2012-2-6
 */
public class Yansblsz extends PageableSupport implements Domain{

	private static final long serialVersionUID = 8260157513422433144L;

	private String usercenter;	//用户中心
	
	private String lingjlx;		//零件类型
	
	private String yansxbh;		//验收项编号
	
	private String cangkbh;		//仓库编号
	
	private Integer yansbl;		//验收比例
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	
	
	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public Integer getYansbl() {
		return yansbl;
	}

	public void setYansbl(Integer yansbl) {
		this.yansbl = yansbl;
	}

	public String getYansxbh() {
		return yansxbh;
	}

	public void setYansxbh(String yansxbh) {
		this.yansxbh = yansxbh;
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

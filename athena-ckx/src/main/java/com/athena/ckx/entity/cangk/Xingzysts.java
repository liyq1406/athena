package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 行政验收提示验收项
 * @author denggq
 * @date 2012-2-6
 */
public class Xingzysts extends PageableSupport implements Domain{

	private static final long serialVersionUID = 7213111369253932489L;

	private String yansxbh;	//验收项编号
	
	private String yansxsm;		//验收项说明
	
	private String biaos;		//标识

	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	public String getYansxbh() {
		return yansxbh;
	}

	public void setYansxbh(String yansxbh) {
		this.yansxbh = yansxbh;
	}

	public String getYansxsm() {
		return yansxsm;
	}

	public void setYansxsm(String yansxsm) {
		this.yansxsm = yansxsm;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
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

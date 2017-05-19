package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件验收比例设置
 * @author denggq
 * @date 2012-2-6
 */
public class Yansbllj extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1360678789475738613L;

	private String usercenter;	//用户中心
	
	private String lingjbh;		//零件编号
	
	private String lingjmc;     //零件名称
	
	private String gongysbh;	//供应商编号
	
	private String yansxbh;		//验收项编号
	
	private String yansxsm;		//验收项说明
	
	private Integer yansbl;		//验收比例
	
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

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
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

	public String getYansxsm() {
		return yansxsm;
	}

	public void setYansxsm(String yansxsm) {
		this.yansxsm = yansxsm;
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

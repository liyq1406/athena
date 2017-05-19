package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 订单计算依赖关系
 * @author qizhongtao
 * 2012-4-16
 */
public class Dingdjsyl extends PageableSupport implements Domain{

	
	private static final long serialVersionUID = 6823361588518808533L;
	
	private String gongysbh;    //供应商编号
	
	private String waibghms;    //外部供货模式
	
	private String yilgx;       //订单计算依赖关系
	
	private String rowid;		//数据库rowid
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getWaibghms() {
		return waibghms;
	}
	public void setWaibghms(String waibghms) {
		this.waibghms = waibghms;
	}
	public String getYilgx() {
		return yilgx;
	}
	public void setYilgx(String yilgx) {
		this.yilgx = yilgx;
	}
	public void setRowid(String rowid) {
		this.rowid = rowid;
	}
	public String getRowid() {
		return rowid;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

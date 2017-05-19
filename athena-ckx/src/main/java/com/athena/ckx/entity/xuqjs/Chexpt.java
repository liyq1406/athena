package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 车型平台关系
 * @author denggq
 * 2012-4-18
 */
public class Chexpt extends PageableSupport implements Domain{

	
	private static final long serialVersionUID = -1380802476991454811L;

	private String usercenter;    	//用户中心
	
	private String chejbhzz;    	//总装车间编号
	
	private String shengcxbhzz;     //总装生产线编号
	
	private String chejbhhz;		//焊装车间编号
	
	private String shengcxbhhz;		//焊装线编号
	
	private String shengcptbh;		//焊装生产平台编号
	
	private String lcdv;			//LCDV条件
	
	private String creator;			//增加人
	
	private String create_time;		//增加时间
	
	private String editor;			//修改人
	
	private String edit_time;		//修改时间
	
	public String getUsercenter() {
		return usercenter;
	}
	public String getChejbhzz() {
		return chejbhzz;
	}
	public String getShengcxbhzz() {
		return shengcxbhzz;
	}
	public String getChejbhhz() {
		return chejbhhz;
	}
	public String getShengcxbhhz() {
		return shengcxbhhz;
	}
	public String getShengcptbh() {
		return shengcptbh;
	}
	public String getLcdv() {
		return lcdv;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public void setChejbhzz(String chejbhzz) {
		this.chejbhzz = chejbhzz;
	}
	public void setShengcxbhzz(String shengcxbhzz) {
		this.shengcxbhzz = shengcxbhzz;
	}
	public void setChejbhhz(String chejbhhz) {
		this.chejbhhz = chejbhhz;
	}
	public void setShengcxbhhz(String shengcxbhhz) {
		this.shengcxbhhz = shengcxbhhz;
	}
	public void setShengcptbh(String shengcptbh) {
		this.shengcptbh = shengcptbh;
	}
	public void setLcdv(String lcdv) {
		this.lcdv = lcdv;
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
		
	}
	@Override
	public String getId() {
		return null;
	}
	
	
}

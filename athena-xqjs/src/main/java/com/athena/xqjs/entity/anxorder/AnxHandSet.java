package com.athena.xqjs.entity.anxorder;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 按需手动下发实体类
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-7-7
 */
public class AnxHandSet extends PageableSupport implements Domain {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	private String usercenter; //用户中心
	private String zhuangt;  //状态
	private String shezrq;  //设置日期
	private String beiz;  //备注
	private String beiz1;  //备注1
	private String beiz2;  //备注2
	private String creator;  //创建人
	private String create_time;  //创建时间
	private String editor;  //修改人
	private String edit_time;  //修改时间
	
	private String startTime;
	private String endTime;
	
	
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getShezrq() {
		return shezrq;
	}
	public void setShezrq(String shezrq) {
		this.shezrq = shezrq;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
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

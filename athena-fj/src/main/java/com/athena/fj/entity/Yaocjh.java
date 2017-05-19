package com.athena.fj.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 要车计划实体类
 * @author hezhiguo
 * @date 2011-12-5
 * @Email:zghe@isoftstone.com
 */
public class Yaocjh  extends PageableSupport implements Domain{ 
	
	public static final String STATUE_WULGZ = "4";//要车状态为物流故障
	public static final String STATUE_ZC = "3";//要车状态为已装车
	public static final String STATUE_PEIZ = "2";//要车状态为已配载
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7209681779037285825L;

	private String yaocjhh;       //要车计划号 
	private String usercenter;    //用户中心  
	private String kaissj;        //开始时间
	private String jiessj;        //结束时间
	private String shifqr="1";        //是否确认
	private String yunssbm;       //运输商编码    
	private String zongcc;        //总车次
	private String creator;       //P_创建人
	private String createTime;    //P_创建时间
	private String editor;        //P_修改人
	private String editTime;      //P_修改时间

	
	
	public String getYaocjhh() {
		return yaocjhh;
	}
	public void setYaocjhh(String yaocjhh) {
		this.yaocjhh = yaocjhh;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getKaissj() {
		return kaissj;
	}
	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}
	public String getJiessj() {
		return jiessj;
	}
	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}
	public String getShifqr() {
		return shifqr;
	}
	public void setShifqr(String shifqr) {
		this.shifqr = shifqr;
	}
	public String getYunssbm() {
		return yunssbm;
	}
	public void setYunssbm(String yunssbm) {
		this.yunssbm = yunssbm;
	}
	public String getZongcc() {
		return zongcc;
	}
	public void setZongcc(String zongcc) {
		this.zongcc = zongcc;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public void setId(String id) { 
		
	}
	
	public String getId() {
		
		return null;
	}
	
	
}

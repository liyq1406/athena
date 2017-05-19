package com.athena.fj.entity;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:车辆资源申报实体类
 * </p>
 * <p>
 * Description:定义车辆资源申报实体变量
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2011-12-19
 */
public class Chelsb extends PageableSupport implements Domain {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5157666252421965656L;
	
	private String usercenter;      //用户中心
	private String yunssbm;         //运输商编码
	private String shenbsj;        //申报时间
	private String chex;           //车型
	private BigDecimal shul;       //数量
	private String creator;       //创建人
	private String createTime;   //创建时间
	private String editor;        //修改人
	private String editTime;     //修改时间
	

	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getYunssbm() {
		return yunssbm;
	}
	public void setYunssbm(String yunssbm) {
		this.yunssbm = yunssbm;
	}
	public String getChex() {
		return chex;
	}
	public void setChex(String chex) {
		this.chex = chex;
	}
	public BigDecimal getShul() {
		return shul;
	}
	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	
	public String getShenbsj() {
		return shenbsj;
	}
	public void setShenbsj(String shenbsj) {
		this.shenbsj = shenbsj;
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
	public String getId() {
		return null;
	}
	@Override
	public void setId(String arg0) {
		
	}  

}

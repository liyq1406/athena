package com.athena.pc.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:累计交付差额实体类
 * </p>
 * <p>
 * Description:定义累计交付差额实体变量
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-6-14
 */
public class Leijce extends PageableSupport  implements Domain  {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -478655543879144487L;

	private String shij;           //时间（主键）
	private String usercenter;     //用户中心（主键）
	private String lingjbh;        //零件编号（主键）
	private String maoxq;          //毛需求
	private String sjjf;           //实际交付量
	private String leijjfce;       //累计交付差额
	private String creator;        //创建人
	private String create_time;    //创建时间
	private String editor;         //编辑人
	private String edit_time;      //编辑时间
	
	
	public String getShij() {
		return shij;
	}

	public void setShij(String shij) {
		this.shij = shij;
	}

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

	public String getMaoxq() {
		return maoxq;
	}

	public void setMaoxq(String maoxq) {
		this.maoxq = maoxq;
	}

	public String getSjjf() {
		return sjjf;
	}

	public void setSjjf(String sjjf) {
		this.sjjf = sjjf;
	}

	public String getLeijjfce() {
		return leijjfce;
	}

	public void setLeijjfce(String leijjfce) {
		this.leijjfce = leijjfce;
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

	public String getId() {
		return null;
	}

	public void setId(String arg0) {
		
	}

}

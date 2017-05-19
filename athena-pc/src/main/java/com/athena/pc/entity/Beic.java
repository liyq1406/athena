package com.athena.pc.entity;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:备储计划实体类
 * </p>
 * <p>
 * Description:定义备储计划实体变量
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
 * @date 2012-2-24
 */
public class Beic extends PageableSupport  implements Domain {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 628717330242320234L;
	
	private String beicjhmxh;				//备储计划明细号
	private String beicjhh;					//备储计划号
	private String lingjbh;					//零件编号
	private String usercenter;          	//用户中心
	private String kaissj;          		//开始时间
	private String jiessj;          		//结束时间
	private BigDecimal lingjsl;				//零件数量
	private String creator;                 //创建人
	private String createTime;              //创建时间
	private String editor;                  //修改人
	private String editTime;                //修改时间
	
	public String getBeicjhmxh() {
		return beicjhmxh;
	}
	public void setBeicjhmxh(String beicjhmxh) {
		this.beicjhmxh = beicjhmxh;
	}
	public String getBeicjhh() {
		return beicjhh;
	}
	public void setBeicjhh(String beicjhh) {
		this.beicjhh = beicjhh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
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
	public BigDecimal getLingjsl() {
		return lingjsl;
	}
	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}
	public String getId() {
		return null;
	}
	public void setId(String arg0) {
		
	}
	
	
}

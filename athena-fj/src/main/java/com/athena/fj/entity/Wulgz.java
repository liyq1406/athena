package com.athena.fj.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:物流故障实体类
 * </p>
 * <p>
 * Description:定义物流故障实体变量
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
 * @date 2012-1-3
 */
public class Wulgz extends PageableSupport implements Domain {

	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 7132631636319510129L;
	
	private String yaocmxh;  //要车明细号
	private String peizdh;   //配载单号
	private String jihcx;    //计划车型
	private String shijcx;   //实际车型
	private String shijcp;   //实际车牌
	private String guzyy;    //故障原因
	private String daocsj;   //到车时间
	private String shijdcsj; //实际到车时间
	private String yunssbm;  //运输商编码
	private String creator;  //P_创建人
	private String createTime;//P_创建时间
	private String editor;   //P_修改人
	private String editTime;//P_修改时间
	
	private String usercenter; //用户中心
	
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getYaocmxh() {
		return yaocmxh;
	}

	public void setYaocmxh(String yaocmxh) {
		this.yaocmxh = yaocmxh;
	}

	public String getPeizdh() {
		return peizdh;
	}

	public void setPeizdh(String peizdh) {
		this.peizdh = peizdh;
	}

	public String getJihcx() {
		return jihcx;
	}

	public void setJihcx(String jihcx) {
		this.jihcx = jihcx;
	}

	public String getShijcx() {
		return shijcx;
	}

	public void setShijcx(String shijcx) {
		this.shijcx = shijcx;
	}

	public String getShijcp() {
		return shijcp;
	}

	public void setShijcp(String shijcp) {
		this.shijcp = shijcp;
	}

	public String getGuzyy() {
		return guzyy;
	}

	public void setGuzyy(String guzyy) {
		this.guzyy = guzyy;
	}

	public String getDaocsj() {
		return daocsj;
	}

	public void setDaocsj(String daocsj) {
		this.daocsj = daocsj;
	}

	public String getShijdcsj() {
		return shijdcsj;
	}

	public void setShijdcsj(String shijdcsj) {
		this.shijdcsj = shijdcsj;
	}

	public String getYunssbm() {
		return yunssbm;
	}

	public void setYunssbm(String yunssbm) {
		this.yunssbm = yunssbm;
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

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub

	}

}

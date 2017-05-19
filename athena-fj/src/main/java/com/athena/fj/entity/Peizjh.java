package com.athena.fj.entity;

import java.util.List;

import com.athena.component.entity.Domain;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:配载计划实体类
 * </p>
 * <p>
 * Description:定义配载计划实体变量
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
 * @date 2011-12-26
 */
@Component
public class Peizjh extends PageableSupport implements Domain {

	public static final String STATUE_PRINT0 = "0"; //计划状态为未打印
	public static final String STATUE_PRINT = "1"; //计划状态为已打印
	public static final String STATUE_PRINT2 = "2"; //计划状态为处理中
	
	public static final String WTC_CHECK_STORE = "30301"; //WTC服务交易码：验证仓库资源
	public static final String WTC_STORE_SURE = "30302"; //WTC服务交易码：确认仓库资源
	public static final String WTC_STORE_DEL = "30303"; //WTC服务交易码：删除备货单
	
	public static final String SUCCESS_TRANSFER = "0000"; //仓库返回消息正确：响应码0000
	
	public static final String SUODPZ_STATE_0 = "0";      //未锁定
	public static final String SUODPZ_STATE_1 = "1";      //锁定
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -3203709781370563478L;
	
	private String yaocmxh;     //要车明细号
	private String peizdh;      //配载单号
	private String usercenter;  //用户中心
	private String daocsj;      //到车时间
	private String faysj;       //发运时间
	private String fromDaocsj;  //到车时间
	private String toDaocsj;    //到车时间
	private String yunssbm;     //运输商编码
	private String jihcx;       //计划车型
	private String chep;        //车牌
	private String kehbm;       //客户编码
	private String jihzt;       //计划状态
	private String daysj;       //打印时间     
	
	private String cangkbh;     //仓库编号
	private String yunslx;      //运输路线
	
	private String jihyzbh;     //仓库计划员组编号
	
	private String creator;     //创建人
	private String createTime; //创建时间
	private String editor;      //修改人
	private String editTime;   //修改时间 
	private String gongsmc;     //运输商名称
	private String yaohls;              //要货令编号，以“,”分隔的字符串
	private List<String> yaohlList; 	//要货令号列表
	private List<String> bhlList;		//备货令号列表

	private String suodpz;             //锁定状态，0：未锁定，1：锁定
	
	private String zuiwsj;       //最晚交付时间	

	public String getZuiwsj() {
		return zuiwsj;
	}
	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}
	public String getJihyzbh() {
		return jihyzbh;
	}
	public void setJihyzbh(String jihyzbh) {
		this.jihyzbh = jihyzbh;
	}
	public String getYaohls() {
		return yaohls;
	}
	public void setYaohls(String yaohls) {
		this.yaohls = yaohls;
	}
	public List<String> getYaohlList() {
		return yaohlList;
	}
	public void setYaohlList(List<String> yaohlList) {
		this.yaohlList = yaohlList;
	}
	public List<String> getBhlList() {
		return bhlList;
	}
	public void setBhlList(List<String> bhlList) {
		this.bhlList = bhlList;
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
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	public String getDaocsj() {
		return daocsj;
	}
	public void setDaocsj(String daocsj) {
		this.daocsj = daocsj;
	}
	
	public String getFaysj() {
		return faysj;
	}
	public void setFaysj(String faysj) {
		this.faysj = faysj;
	}
	public String getFromDaocsj() {
		return fromDaocsj;
	}
	public void setFromDaocsj(String fromDaocsj) {
		this.fromDaocsj = fromDaocsj;
	}
	public String getToDaocsj() {
		return toDaocsj;
	}
	public void setToDaocsj(String toDaocsj) {
		this.toDaocsj = toDaocsj;
	}
	public String getYunssbm() {
		return yunssbm;
	}
	public void setYunssbm(String yunssbm) {
		this.yunssbm = yunssbm;
	}
	public String getJihcx() {
		return jihcx;
	}
	public void setJihcx(String jihcx) {
		this.jihcx = jihcx;
	}
	public String getChep() {
		return chep;
	}
	public void setChep(String chep) {
		this.chep = chep;
	}
	public String getKehbm() {
		return kehbm;
	}
	public void setKehbm(String kehbm) {
		this.kehbm = kehbm;
	}
	public String getJihzt() {
		return jihzt;
	}
	public void setJihzt(String jihzt) {
		this.jihzt = jihzt;
	}
	
	public String getDaysj() {
		return daysj;
	}
	public void setDaysj(String daysj) {
		this.daysj = daysj;
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
	
	public String getGongsmc() {
		return gongsmc;
	}
	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}
	@Override
	public String getId() {
		
		return null;
	}
	@Override
	public void setId(String arg0) {
		
	}
	public String getSuodpz() {
		return suodpz;
	}
	public void setSuodpz(String suodpz) {
		this.suodpz = suodpz;
	}
	
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getYunslx() {
		return yunslx;
	}
	public void setYunslx(String yunslx) {
		this.yunslx = yunslx;
	}
	
	

}

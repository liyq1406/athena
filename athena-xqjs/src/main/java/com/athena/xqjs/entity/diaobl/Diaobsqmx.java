package com.athena.xqjs.entity.diaobl;


import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称： Diaobsqmx
 * <p>
 * 类描述：调拨申请明细
 * </p>
 * 创建人：Niesy
 * <p>
 * 创建时间：2011-12-13 
 * </p>
 * 修改备注：
 * <p>
 * @version 
 * </p>
 */
public class Diaobsqmx extends PageableSupport{
	 private static final long serialVersionUID = -6416990806689862783L;
	 
	/**
		 * 仓库编号
		 * 0012338
	*/
	private String cangkbh = null;

	/**
	 * 序号
	 */
	private Integer xuh = null;

	/**
	 * 用户中心
	 */
	private String usercenter = null;

	/**
	 * 调拨申请单号
	 */
	private String diaobsqdh = null;

	/**
	 * 路线
	 */
	private String lux = null;

	/**
	 * 路线
	 */
	private String zhizlx = null;

	/**
	 * 零件号
	 */
	private String lingjbh = null;

	/**
	 * 零件名称
	 */
	private String lingjmc = null;

	/**
	 * 申报数量
	 */
	private BigDecimal shenbsl = null;
	
	
	private String tempShenbsl = null;

	/**
	 * 实批数量
	 */
	private BigDecimal shipsl = null;

	/**
	 * 已执行数量
	 */
	private BigDecimal zhixsl = null;

	/**
	 * 执行百分比
	 */
	@SuppressWarnings("unused")
	private String zhixbfb = null;

	/**
	 * 是否带包装
	 */
	private String shifdbz = null;

	/**
	 * 是否多次交付
	 */
	private String shifdcjf = null;

	/**
	 * 状态
	 */
	private String zhuangt = null;
	
	
	/**
	 * 包装类型
	 * 0012338
	 */
	private String uclx = null;

	/**
	 * 包装容量
	 */
	private BigDecimal ucrl = null;
	
	/**
	 * 要货时间
	 */
	private String yaohsj = null;

	/**
	 * 新状态
	 */
	private String newZhuangt = null;

	/**
	 * 版次
	 */
	private String banc = null;

	/**
	 * 计划员组
	 */
	private String jihy = null;
	/**
	 * 计划员组
	 */
	private String jihyz = null;
	/**
	 * 创建时间
	 */
	private String create_time = null;

	/**
	 * 创建人
	 */
	private String creator = null;

	/**
	 * 修改时间
	 */
	private String edit_time = null;

	/**
	 * 修改人
	 */
	private String editor = null;

	/**
	 * 当前修改时间
	 */
	private String newEdit_time = null;

	/**
	 * 当前修改人
	 */
	private String newEditor = null;
	
    /**
	 * 备注
	 */
	private String beiz = null;
	
	/**
	 * 标识
	 */
	private String active = null;
	
	/**
	 * 单位
	 */
	private String danw = null;

	/**
	 * 资源获取日期
	 */
	private String ziyhqrq = null;
	
	/**
	 * EXCEL下载错误信息
	 */
	private String tishi=null;
	
	private String chayibz=null;
	public String getChayibz() {
		return chayibz;
	}

	public void setChayibz(String chayibz) {
		this.chayibz = chayibz;
	}

	public String getTishi() {
		return tishi;
	}

	public void setTishi(String tishi) {
		this.tishi = tishi;
	}

	public Integer getXuh() {
		return xuh;
	}

	public void setXuh(Integer xuh) {
		this.xuh = xuh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDiaobsqdh() {
		return diaobsqdh;
	}

	public void setDiaobsqdh(String diaobsqdh) {
		this.diaobsqdh = diaobsqdh;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getLux() {
		return lux;
	}

	public void setLux(String lux) {
		this.lux = lux;
	}

	

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public BigDecimal getShenbsl() {
		return shenbsl;
	}

	public void setShenbsl(BigDecimal shenbsl) {
		this.shenbsl = shenbsl;
	}

	public BigDecimal getShipsl() {
		return shipsl;
	}

	public void setShipsl(BigDecimal shipsl) {
		if(this.shipsl == null){
			this.shipsl = BigDecimal.ZERO;
		}
		this.shipsl = shipsl;
	}

	public BigDecimal getZhixsl() {
		if(null == this.zhixsl){
			this.zhixsl = BigDecimal.ZERO;
		}
		return zhixsl;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	public void setZhixsl(BigDecimal zhixsl) {
		if(this.zhixsl == null){
			this.zhixsl = BigDecimal.ZERO;
		}
		this.zhixsl = zhixsl;
	}

	public String getZhixbfb() {
		String  bfb = "";
		if(BigDecimal.ZERO == this.shipsl || this.shenbsl == null){
			return 0+"%";
		}else{
			if(null == zhixsl){
				   this.zhixsl = BigDecimal.ZERO;
				}
			bfb = this.zhixsl.divide(this.shipsl, 3, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP) + "%";
			 
		}
		return  bfb;
	}

	public String getYaohsj() {
		return yaohsj;
	}

	public void setYaohsj(String yaohsj) {
		this.yaohsj = yaohsj;
	}

	public void setZhixbfb(String zhixbfb) {
		this.zhixbfb = zhixbfb;
	}

	public String getShifdbz() {
		return shifdbz;
	}

	public void setShifdbz(String shifdbz) {
		this.shifdbz = shifdbz;
	}

	public String getShifdcjf() {
		return shifdcjf;
	}

	public void setShifdcjf(String shifdcjf) {
		this.shifdcjf = shifdcjf;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getNewEdit_time() {
		return newEdit_time;
	}

	public void setNewEdit_time(String newEdit_time) {
		this.newEdit_time = newEdit_time;
	}

	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getNewZhuangt() {
		return newZhuangt;
	}

	public void setNewZhuangt(String newZhuangt) {
		this.newZhuangt = newZhuangt;
	}

	public String getBanc() {
		return banc;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public void setBanc(String banc) {
		this.banc = banc;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public String getZiyhqrq() {
		return ziyhqrq;
	}

	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}

	public String getTempShenbsl() {
		return tempShenbsl;
	}

	public void setTempShenbsl(String tempShenbsl) {
		this.tempShenbsl = tempShenbsl;
	}
	
	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getUclx() {
		return uclx;
	}

	public void setUclx(String uclx) {
		this.uclx = uclx;
	}

	public BigDecimal getUcrl() {
		return ucrl;
	}

	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}

}

	

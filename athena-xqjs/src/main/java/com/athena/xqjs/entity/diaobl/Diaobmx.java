package com.athena.xqjs.entity.diaobl;


import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * 调拨明细bean
 * </p>
 * author   夏晖
 * date     2011-11-21
 */
public class Diaobmx extends PageableSupport{
	
	 private static final long serialVersionUID = -6416990806688863783L;
	 
	/**
	 * 序号
	 */
	private Integer xuh = null;

	/**
	 * 用户中心
	 */
	private String usercenter = null;

	/**
	 * 仓库编号
	 */
	private String cangkbh = null;
	
	//0012338
	private String ziyhqrq = null;
	

	/**
	 * 子仓库编号
	 */
	private String zickbh = null;

	/**
	 * 调拨申请单号
	 */
	private String diaobsqdh = null;

	/**
	 * 生效时间
	 */
	private String shengxsj = null;

	/**
	 * 调拨单号
	 */
	private String diaobdh = null;

	/**
	 * 零件号
	 */
	private String lingjbh = null;

	/**
	 * 零件名称
	 */
	private String lingjmc = null;

	/**
	 * 零件库存数量
	 */
	private String lingjsl = null;

	/**
	 * 路线
	 */
	private String lux = null;

	/**
	 * 库存数量
	 */
	private BigDecimal kucsl = null;

	/**
	 * 申报数量
	 */
	private BigDecimal shenbsl = null;

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
	 */
	private String uclx = null;

	/**
	 * 包装容量
	 */
	private BigDecimal ucrl = null;

	/**
	 * 备注
	 */
	private String beiz = null;

	/**
	 * 需求申请人
	 */
	private String xqsqr = null;
	/**
	 * 成本中心
	 */
	private String chengbzx =null;
	
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
	 private String  newEdit_time = null;
	 
	 /**
	  * 当前修改人
	  */
	 private String  newEditor = null;
	
	/**
	 * 标识
	 */
	private String active = null;
	
	
	/* 累积量
	 * v4_018
	 * */
	private BigDecimal leijl = BigDecimal.ZERO;
		

	public BigDecimal getLeijl() {
		return leijl;
	}

	public void setLeijl(BigDecimal leijl) {
		this.leijl = leijl;
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

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public String getDiaobsqdh() {
		return diaobsqdh;
	}

	public void setDiaobsqdh(String diaobsqdh) {
		this.diaobsqdh = diaobsqdh;
	}

	public String getShengxsj() {
		return shengxsj;
	}

	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}

	public String getDiaobdh() {
		return diaobdh;
	}

	public void setDiaobdh(String diaobdh) {
		this.diaobdh = diaobdh;
	}

	

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getLux() {
		return lux;
	}

	public void setLux(String lux) {
		this.lux = lux;
	}

	public String getXqsqr() {
		return xqsqr;
	}

	public void setXqsqr(String xqsqr) {
		this.xqsqr = xqsqr;
	}

	public String getChengbzx() {
		return chengbzx;
	}

	public void setChengbzx(String chengbzx) {
		this.chengbzx = chengbzx;
	}

	public BigDecimal getKucsl() {
		return kucsl;
	}

	public void setKucsl(BigDecimal kucsl) {
		this.kucsl = kucsl;
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
		if(shipsl == null){
			this.shipsl = BigDecimal.ZERO;
		} else {
			this.shipsl = shipsl;
		}
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

	public BigDecimal getZhixsl() {
		return zhixsl;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setZhixsl(BigDecimal zhixsl) {
		if(null == zhixsl){
			this.zhixsl = BigDecimal.ZERO;
		}
		this.zhixsl = zhixsl;
	}

	public String getZhixbfb() {
		String  bfb = "";
		if(BigDecimal.ZERO == this.shipsl || null == this.shipsl){
			return 0+"%";
		}else{
			if(null == zhixsl){
				   this.zhixsl = BigDecimal.ZERO;
				}
			bfb = this.zhixsl.divide(this.shipsl, 3, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP) + "%";
			 
		}
		return  bfb;
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

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
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

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	
	public String getZiyhqrq() {
		return ziyhqrq;
	}

	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}
	
}

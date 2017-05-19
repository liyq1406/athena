package com.athena.xqjs.entity.yaohl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Yaohl extends PageableSupport {
	private static final long serialVersionUID = 1L;
	/**
	 * 要货令号
	 */
	private String yaohlh;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 循环编码
	 */
	private String xunhbm;
	/**
	 * 交付时间
	 */
	private String jiaofj;
	/**
	 * 最早交付时间
	 */
	private String zuizsj;
	/**
	 * 最晚交付时间
	 */
	private String zuiwsj;
	/**
	 * 零件编号
	 */
	private String lingjbh;

	/**
	 * 零件名称
	 */
	private String lingjmc;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * 要货数量
	 */
	private BigDecimal yaohsl;
	/**
	 * UA包装型号
	 */
	private String baozxh;
	/**
	 * UA容量
	 */
	private BigDecimal usxh;
	/**
	 * UC型号
	 */
	private String ucxh;
	/**
	 * UC容量
	 */
	private BigDecimal ucrl;
	/**
	 * UC个数
	 */
	private Integer ucgs;
	/**
	 * 发运时间
	 */
	private String faysj;
	/**
	 * 上线时间
	 */
	private String shangxsj;
	/**
	 * 卸货点
	 */
	private String xiehd;
	/**
	 * 新卸货点
	 */
	private String xinxhd;
	/**
	 * 目的地
	 */
	private String mudd;
	/**
	 * 新目的地
	 */
	private String xinmdd;
	/**
	 * 目的地类型
	 */
	private String muddlx;
	/**
	 * 要货令状态
	 */
	private String yaohlzt;
	
	/**
	 * 是否调整
	 */
	private String shiftt;
	

	public String getShiftt() {
		return shiftt;
	}

	public void setShiftt(String shiftt) {
		this.shiftt = shiftt;
	}

	public String getDingdmxid() {
		return dingdmxid;
	}

	public void setDingdmxid(String dingdmxid) {
		this.dingdmxid = dingdmxid;
	}

	/**
	 * 定单号
	 */
	private String dingdh;
	/**
	 * 申报人
	 */
	private String shengbr;
	/**
	 * 属性
	 */
	private String yaohlsl;
	/**
	 * 子仓库编号
	 */
	private String zickbh;
	/**
	 * 要货令类型
	 */
	private String yaohllx;
	/**
	 * 同步流水号区间
	 */
	private String tongblshqj;
	/**
	 * 跳号标识
	 */
	private String tiaohbs;
	/**
	 * 备注
	 */
	private String beiz;
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	/**
	 * 供应商名称
	 */
	private String gongysmc;

	/**
	 * 承运商代码
	 */
	private String chengysdm;
	/**
	 * 车间
	 */
	private String chej;
	/**
	 * 上线方式
	 */
	private String shnagxfs;
	/**
	 * 产线
	 */
	private String chanx;
	/**
	 * 客户
	 */
	private String keh;
	/**
	 * 配送类别
	 */
	private String peislb;
	/**
	 * 要货令生成时间
	 */
	private String yaohlscsj;
	/**
	 * 修改后预计交付时间
	 */
	private String xiughyjjfsj;
	/**
	 * 发货地
	 */
	private String fahd;
	/**
	 * 交付状态
	 */
	private String jiaofzt;
	/**
	 * 计划员组
	 */
	private String jihyz;
	/**
	 * 实际发运时间
	 */
	private String shijfysj;
	/**
	 * 仓库编号
	 */
	private String cangkbh;

	/**
	 * 未交付数量
	 */
	private BigDecimal weijfsl;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 修改人
	 */
	private String editor;

	/**
	 * 修改时间
	 */
	private String edit_time;
	/**
	 * 新修改人
	 */
	private String neweditor;
	/**
	 * 新修改时间
	 */
	private String edittime;
	/**
	 * 订单明细id
	 */
	private String dingdmxid;

	/**
	 * 可终止量
	 */
	private BigDecimal allowGzsl;

	/**
	 * 拉箱指定到达时间
	 */
	private String laxzdddsj;

	/**
	 * 是否发生供应商
	 */
	private String shifpz;

	/**
	 * 交付数量
	 */
	private BigDecimal daijfsl;

	/**
	 * 是否临时卡
	 */

	private String shiflsk;
	
	/**
	 * 交付总量
	 */
	private String jiaofzl;
	
	/**
	 * 产生原因
	 */
	private String chansyy;
	
	/**
	 * 终止原因
	 */
	private String zhongzyy;
	
	/**
	 * 原要货令号
	 */
	private String yuanyhlh;
	
	/**
	 * 状态
	 * @return
	 */
	private String zhuangt;
	
	/**
	 * 原最早交付时间
	 */
	private String yuanzuizsj;
	/**
	 * 原最晚交付时间
	 */
	private String yuanzuiwsj;
	
	
	
	public String getYuanzuizsj() {
		return yuanzuizsj;
	}

	public void setYuanzuizsj(String yuanzuizsj) {
		this.yuanzuizsj = yuanzuizsj;
	}

	public String getYuanzuiwsj() {
		return yuanzuiwsj;
	}

	public void setYuanzuiwsj(String yuanzuiwsj) {
		this.yuanzuiwsj = yuanzuiwsj;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getJiaofzl() {
		return jiaofzl;
	}

	public void setJiaofzl(String jiaofzl) {
		this.jiaofzl = jiaofzl;
	}

	public BigDecimal getAllowGzsl() {
		return allowGzsl;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public void setAllowGzsl(BigDecimal allowGzsl) {
		this.allowGzsl = allowGzsl;
	}

	public String getNeweditor() {
		return neweditor;
	}

	public void setNeweditor(String neweditor) {
		this.neweditor = neweditor;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
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

	public BigDecimal getWeijfsl() {
		return weijfsl;
	}

	public void setWeijfsl(BigDecimal weijfsl) {
		this.weijfsl = weijfsl;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getShijfysj() {
		return shijfysj;
	}

	public void setShijfysj(String shijfysj) {
		this.shijfysj = shijfysj;
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

	public String getFahd() {
		return fahd;
	}

	public void setFahd(String fahd) {
		this.fahd = fahd;
	}

	public String getJiaofzt() {
		return jiaofzt;
	}

	public void setJiaofzt(String jiaofzt) {
		this.jiaofzt = jiaofzt;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXunhbm() {
		return xunhbm;
	}

	public void setXunhbm(String xunhbm) {
		this.xunhbm = xunhbm;
	}

	public String getJiaofj() {
		return jiaofj;
	}

	public void setJiaofj(String jiaofj) {
		this.jiaofj = jiaofj;
	}

	public String getZuizsj() {
		return zuizsj;
	}

	public void setZuizsj(String zuizsj) {
		this.zuizsj = zuizsj;
	}

	public String getZuiwsj() {
		return zuiwsj;
	}

	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public BigDecimal getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(BigDecimal yaohsl) {
		this.yaohsl = yaohsl;
	}

	public String getBaozxh() {
		return baozxh;
	}

	public void setBaozxh(String baozxh) {
		this.baozxh = baozxh;
	}

	public BigDecimal getUsxh() {
		return usxh;
	}

	public void setUsxh(BigDecimal usxh) {
		this.usxh = usxh;
	}

	public String getUcxh() {
		return ucxh;
	}

	public void setUcxh(String ucxh) {
		this.ucxh = ucxh;
	}

	public BigDecimal getUcrl() {
		return ucrl;
	}

	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}

	public Integer getUcgs() {
		return ucgs;
	}

	public void setUcgs(Integer ucgs) {
		this.ucgs = ucgs;
	}

	public String getFaysj() {
		return faysj;
	}

	public void setFaysj(String faysj) {
		this.faysj = faysj;
	}

	public String getShangxsj() {
		return shangxsj;
	}

	public void setShangxsj(String shangxsj) {
		this.shangxsj = shangxsj;
	}

	public String getXiehd() {
		return xiehd;
	}

	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}

	public String getXinxhd() {
		return xinxhd;
	}

	public void setXinxhd(String xinxhd) {
		this.xinxhd = xinxhd;
	}

	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getXinmdd() {
		return xinmdd;
	}

	public void setXinmdd(String xinmdd) {
		this.xinmdd = xinmdd;
	}

	public String getMuddlx() {
		return muddlx;
	}

	public void setMuddlx(String muddlx) {
		this.muddlx = muddlx;
	}

	public String getYaohlzt() {
		return yaohlzt;
	}

	public void setYaohlzt(String yaohlzt) {
		this.yaohlzt = yaohlzt;
	}

	public String getDingdh() {
		return dingdh;
	}

	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}

	public String getShengbr() {
		return shengbr;
	}

	public void setShengbr(String shengbr) {
		this.shengbr = shengbr;
	}

	public String getYaohlsl() {
		return yaohlsl;
	}

	public void setYaohlsl(String yaohlsl) {
		this.yaohlsl = yaohlsl;
	}

	public String getYaohllx() {
		return yaohllx;
	}

	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}

	public String getTongblshqj() {
		return tongblshqj;
	}

	public void setTongblshqj(String tongblshqj) {
		this.tongblshqj = tongblshqj;
	}

	public String getTiaohbs() {
		return tiaohbs;
	}

	public void setTiaohbs(String tiaohbs) {
		this.tiaohbs = tiaohbs;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

	public String getChej() {
		return chej;
	}

	public void setChej(String chej) {
		this.chej = chej;
	}

	public String getShnagxfs() {
		return shnagxfs;
	}

	public void setShnagxfs(String shnagxfs) {
		this.shnagxfs = shnagxfs;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getKeh() {
		return keh;
	}

	public void setKeh(String keh) {
		this.keh = keh;
	}

	public String getPeislb() {
		return peislb;
	}

	public void setPeislb(String peislb) {
		this.peislb = peislb;
	}

	public String getYaohlscsj() {
		return yaohlscsj;
	}

	public void setYaohlscsj(String yaohlscsj) {
		this.yaohlscsj = yaohlscsj;
	}

	public String getXiughyjjfsj() {
		return xiughyjjfsj;
	}

	public void setXiughyjjfsj(String xiughyjjfsj) {
		this.xiughyjjfsj = xiughyjjfsj;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getLaxzdddsj() {
		return laxzdddsj;
	}

	public void setLaxzdddsj(String laxzdddsj) {
		this.laxzdddsj = laxzdddsj;
	}

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}

	public String getShifpz() {
		return shifpz;
	}

	public void setShifpz(String shifpz) {
		this.shifpz = shifpz;
	}

	public BigDecimal getDaijfsl() {
		return daijfsl;
	}

	public void setDaijfsl(BigDecimal daijfsl) {
		this.daijfsl = daijfsl;
	}

	public String getShiflsk() {
		return shiflsk;
	}

	public void setShiflsk(String shiflsk) {
		this.shiflsk = shiflsk;
	}

	public String getChansyy() {
		return chansyy;
	}

	public void setChansyy(String chansyy) {
		this.chansyy = chansyy;
	}

	public String getZhongzyy() {
		return zhongzyy;
	}

	public void setZhongzyy(String zhongzyy) {
		this.zhongzyy = zhongzyy;
	}

	public String getYuanyhlh() {
		return yuanyhlh;
	}

	public void setYuanyhlh(String yuanyhlh) {
		this.yuanyhlh = yuanyhlh;
	}
	
	
}

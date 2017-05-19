package com.athena.xqjs.entity.yaohl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * 内部要货令 bean
 */
public class Yaonbhl extends PageableSupport{
	private static final long serialVersionUID = 1L;
	
	
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
	 * 要货令号
	 */
    private String yaohlh    ;
  /**
   * 用户中心
   */
	private String usercenter;  
	/**
	 * 循环编码
	 */
	private String xunhbm    ;  
	/**
	 * 交付时间
	 */
	private String jiaofj    ;
	/**
	 * 最早交付时间
	 */
	private String zuizsj    ; 
	/**
	 * 最晚交付时间
	 */
	private String zuiwsj    ;  
	/**
	 * 零件编号
	 */
	private String lingjbh   ;  
	/**
	 * 单位
	 */
	private String danw      ;  
	/**
	 * 要货数量
	 */
	private String yaohsl    ;  
	/**
	 * 上线包装型号
	 */
	private String baozxh    ; 
	/**
	 * 上线包装容量
	 */
	private BigDecimal usxh      ; 
	/**
	 * 发运时间
	 */
	private String faysj     ; 
	/**
	 * 备货时间
	 */
	private String beihsj    ;  
	/**
	 *上线时间
	 */
	private String shangxsj  ; 
	/**
	 * 仓库编号
	 */
	private String cangkbh   ;  
	/**
	 * 子仓库编号
	 */
	private String zickbh    ; 
	/**
	 * 目的地
	 */
	private String mudd      ;  
	/**
	 * 目的地类型
	 */
	private String muddlx    ; 
	/**
	 * 要货令状态
	 */
	private String yaohlzt   ; 
	/**
	 * 订单号
	 */
	private String dingdh    ;  
	/**
	 * 申报人/计划员
	 */
	private String shengbr   ; 
	/**
	 * 属性
	 */
	private String yaohlsl   ;  
	/**
	 * 是否配载
	 */
	private String shifpz    ; 
	/**
	 * 锁定配载
	 */
	private String suodpz    ; 
	/**
	 * 交付类型
	 */
	private String jiaoflx   ;  
	/**
	 *交付总量
	 */
	private BigDecimal jiaofzl   ;  
	/**
	 * 要货令类型
	 */
	private String yaohllx   ;  
	/**
	 * 要货令类别
	 */
	private String yaohllb   ;  
	/**
	 * 同步流水号区间
	 */
	private String tongblshqj;  
	/**
	 * 备注
	 */
	private String beiz      ;  
	/**
	 * 小火车号
	 */
	private String xiaohch   ;  
	/**
	 * 躺次
	 */
	private Integer tangc     ; 
	/**
	 * 车厢
	 */
	private Integer chex      ;  
	/**
	 * 层
	 */
	private Integer ceng      ;  
	/**
	 * 是否归集
	 */
	private String shifgj    ;  
	/**
	 * 是否生产备货单
	 */
	private String shifscbhd ;  
	/**
	 * 是否打印UC
	 */
	private String shifdyuc  ;  
	/**
	 * 供应商代码
	 */
	private String gongysdm  ; 
	/**
	 * 供应商名称
	 */
	private String gongysmc  ;  
	/**
	 * 车间
	 */
	private String chej      ;
	/**
	 * 上线方式
	 */
	private String shangxfs  ; 
	/**
	 * 方式
	 */
	private String chanx     ;
	/**
	 * 客户
	 */
	private String keh       ;  
	/**
	 * 配送方式
	 */
	private String peislb    ;  
	/**
	 * 要货令生成方式
	 */
	private String yaohlscsj ;  
	/**
	 * 创建人
	 */
	private String   creator;
	/**
	 * 创建时间
	 */
	private  String   create_time;
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
	private   String neweditor;
	/**
	 * 新修改时间
	 */
	private  String   edittime;

	/**
	 * 计划员
	 */
	private String jihyz;
	
	
	/**
	 * 交付数量
	 */
	private BigDecimal daijfsl;
	
	
	
	
	
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
	public String getYaohsl() {
		return yaohsl;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}
	public void setYaohsl(String yaohsl) {
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
	public String getFaysj() {
		return faysj;
	}
	public void setFaysj(String faysj) {
		this.faysj = faysj;
	}
	public String getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(String beihsj) {
		this.beihsj = beihsj;
	}
	public String getShangxsj() {
		return shangxsj;
	}
	public void setShangxsj(String shangxsj) {
		this.shangxsj = shangxsj;
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
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
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
	public String getShifpz() {
		return shifpz;
	}
	public void setShifpz(String shifpz) {
		this.shifpz = shifpz;
	}
	public String getSuodpz() {
		return suodpz;
	}
	public void setSuodpz(String suodpz) {
		this.suodpz = suodpz;
	}
	public String getJiaoflx() {
		return jiaoflx;
	}
	public void setJiaoflx(String jiaoflx) {
		this.jiaoflx = jiaoflx;
	}
	public BigDecimal getJiaofzl() {
		return jiaofzl;
	}
	public void setJiaofzl(BigDecimal jiaofzl) {
		this.jiaofzl = jiaofzl;
	}
	public String getYaohllx() {
		return yaohllx;
	}
	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}
	public String getYaohllb() {
		return yaohllb;
	}
	public void setYaohllb(String yaohllb) {
		this.yaohllb = yaohllb;
	}
	public String getTongblshqj() {
		return tongblshqj;
	}
	public void setTongblshqj(String tongblshqj) {
		this.tongblshqj = tongblshqj;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public String getXiaohch() {
		return xiaohch;
	}
	public void setXiaohch(String xiaohch) {
		this.xiaohch = xiaohch;
	}
	public Integer getTangc() {
		return tangc;
	}
	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}
	public Integer getChex() {
		return chex;
	}
	public void setChex(Integer chex) {
		this.chex = chex;
	}
	public Integer getCeng() {
		return ceng;
	}
	public void setCeng(Integer ceng) {
		this.ceng = ceng;
	}
	public String getShifgj() {
		return shifgj;
	}
	public void setShifgj(String shifgj) {
		this.shifgj = shifgj;
	}
	public String getShifscbhd() {
		return shifscbhd;
	}
	public void setShifscbhd(String shifscbhd) {
		this.shifscbhd = shifscbhd;
	}
	public String getShifdyuc() {
		return shifdyuc;
	}
	public void setShifdyuc(String shifdyuc) {
		this.shifdyuc = shifdyuc;
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
	public String getShangxfs() {
		return shangxfs;
	}
	public void setShangxfs(String shangxfs) {
		this.shangxfs = shangxfs;
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
	public BigDecimal getDaijfsl() {
		return daijfsl;
	}
	public void setDaijfsl(BigDecimal daijfsl) {
		this.daijfsl = daijfsl;
	}

}

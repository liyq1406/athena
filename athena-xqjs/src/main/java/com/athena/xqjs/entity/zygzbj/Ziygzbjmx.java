package com.athena.xqjs.entity.zygzbj;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 资源跟踪报警明细bean
 * @author WL
 * @date 2011-02-02
 */
public class Ziygzbjmx extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -202328169602634774L;

	/**
	 * 主键ID
	 */
	private String id = "";
	/**
	 * 资源汇总ID
	 */
	private String ziyhzid = "";
	/**
	 * 汇总序号
	 */
	private Integer huizxh = 0;
	/**
	 * 明细
	 */
	private Integer mingxxh = 0;
	/**
	 * 资源跟踪报警类型
	 */
	private String baojlx = "";
	/**
	 * 用户中心
	 */
	private String usercenter = "";
	/**
	 * 仓库
	 */
	private String cangkdm = "";
	/**
	 * 零件号
	 */
	private String lingjbh = "";
	/**
	 * 零件名称
	 */
	private String lingjmc = "";
	/**
	 * 开始时间
	 */
	private String starttime = "";
	/**
	 * 结束时间
	 */
	private String endtime = "";
	/**
	 * 需求量
	 */
	private BigDecimal xuql;
	/**
	 * 已交付量
	 */
	private BigDecimal yijfl;
	/**
	 * 未发运量
	 */
	private BigDecimal weifyl;
	/**
	 * 计算类型
	 */
	private String jislx = "";
	
	/**
	 * 制造路线
	 */
	private String zhizlx;
	
	/**
	 * 年周序
	 */
	private String nianzx;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZiyhzid() {
		return ziyhzid;
	}
	public void setZiyhzid(String ziyhzid) {
		this.ziyhzid = ziyhzid;
	}
	public Integer getHuizxh() {
		return huizxh;
	}
	public void setHuizxh(Integer huizxh) {
		this.huizxh = huizxh;
	}
	public String getBaojlx() {
		return baojlx;
	}
	public void setBaojlx(String baojlx) {
		this.baojlx = baojlx;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCangkdm() {
		return cangkdm;
	}
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
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
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	public void setJislx(String jislx) {
		this.jislx = jislx;
	}
	public BigDecimal getXuql() {
		return xuql;
	}
	public void setXuql(BigDecimal xuql) {
		this.xuql = xuql;
	}
	public BigDecimal getYijfl() {
		return yijfl;
	}
	public void setYijfl(BigDecimal yijfl) {
		this.yijfl = yijfl;
	}
	public BigDecimal getWeifyl() {
		return weifyl;
	}
	public void setWeifyl(BigDecimal weifyl) {
		this.weifyl = weifyl;
	}
	public String getJislx() {
		return jislx;
	}
	public Integer getMingxxh() {
		return mingxxh;
	}
	public void setMingxxh(Integer mingxxh) {
		this.mingxxh = mingxxh;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getNianzx() {
		return nianzx;
	}
	public void setNianzx(String nianzx) {
		this.nianzx = nianzx;
	}
	
	
}

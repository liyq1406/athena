package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 爱信订单匹配结果
 * @author WL
 * @version
 * 
 */
public class Aixddppjg extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5505014442602223756L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 订单号
	 */
	private String dingdh;
	/**
	 * 发货周期
	 */
	private String fahzq;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 发运日期
	 */
	private String fayrq;
	/**
	 * 数量
	 */
	private BigDecimal shul;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * 订货数量
	 */
	private BigDecimal dinghsl;
	/**
	 * 差异量
	 */
	private BigDecimal chayl;
	/**
	 * 多UA堆数
	 */
	private int duouads;
	/**
	 * 少UA堆数
	 */
	private int shaouads;
	
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	
	/**
	 * UA包装容量
	 */
	private String uabzucrl;
	
	/**
	 * UA包装类型
	 */
	private String uabzlx;
	
	/**
	 * 零件名称
	 */
	private String lingjmc;
	
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public String getLingjmc() {
		return lingjmc;
	}
	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getDingdh() {
		return dingdh;
	}
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}
	public String getFahzq() {
		return fahzq;
	}
	public void setFahzq(String fahzq) {
		this.fahzq = fahzq;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getFayrq() {
		return fayrq;
	}
	public void setFayrq(String fayrq) {
		this.fayrq = fayrq;
	}
	public BigDecimal getShul() {
		return shul;
	}
	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public BigDecimal getDinghsl() {
		return dinghsl;
	}
	public void setDinghsl(BigDecimal dinghsl) {
		this.dinghsl = dinghsl;
	}
	public BigDecimal getChayl() {
		return chayl;
	}
	public void setChayl(BigDecimal chayl) {
		this.chayl = chayl;
	}
	public int getDuouads() {
		return duouads;
	}
	public void setDuouads(int duouads) {
		this.duouads = duouads;
	}
	public int getShaouads() {
		return shaouads;
	}
	public void setShaouads(int shaouads) {
		this.shaouads = shaouads;
	}
	public String getUabzucrl() {
		return uabzucrl;
	}
	public void setUabzucrl(String uabzucrl) {
		this.uabzucrl = uabzucrl;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	
	
}

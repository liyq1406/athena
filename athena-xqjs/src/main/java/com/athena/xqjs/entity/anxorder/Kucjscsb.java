package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * @按需:库存计算参数表bean
 * @author   李明
 * @date     2012-3-19
 */
public class Kucjscsb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private String usercenter ;
	private BigDecimal yicxhl ;
	private BigDecimal daohl;
	private String xiaohd;
	private String lingjbh;
	private String flag;
	private String jilrq ;
	/*
	 * 以下为方便计算增加字段
	 * */
	private BigDecimal daixhl ;
	private BigDecimal lilkc ;
	private String fenpqh ;
	private String gongysbh ;
	/**
	 * 总装流水号
	 */
	private String zhongzlxh;
	/**
	 * 库存待消耗
	 */
	private BigDecimal kucsldxh ;
	/**
	 * 线边理论库存
	 */
	private BigDecimal xianbllkc ;
	/**
	 * 盘点实际库存
	 */
	private BigDecimal pdsjkc ;
	
	/**
	 * 盘点时的待消耗
	 */
	private BigDecimal panddxh;
	
	/**
	 * 盘点时的出库量 
	 */
	private BigDecimal pandckl;
	
	/**
	 * 盘点时的异常消耗
	 */
	private BigDecimal pandycxh;
	
	/**
	 * 按需上次计算时间
	 */
	private String anxscjssj;
	
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public BigDecimal getLilkc() {
		return lilkc;
	}
	public void setLilkc(BigDecimal lilkc) {
		this.lilkc = lilkc;
	}
	public BigDecimal getDaixhl() {
		return daixhl;
	}
	public void setDaixhl(BigDecimal daixhl) {
		this.daixhl = daixhl;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public BigDecimal getYicxhl() {
		return yicxhl;
	}
	public void setYicxhl(BigDecimal yicxhl) {
		this.yicxhl = yicxhl;
	}
	public BigDecimal getDaohl() {
		return daohl;
	}
	public void setDaohl(BigDecimal daohl) {
		this.daohl = daohl;
	}
	public String getJilrq() {
		return jilrq;
	}
	public void setJilrq(String jilrq) {
		this.jilrq = jilrq;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
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
	public String getZhongzlxh() {
		return zhongzlxh;
	}
	public void setZhongzlxh(String zhongzlxh) {
		this.zhongzlxh = zhongzlxh;
	}
	public BigDecimal getKucsldxh() {
		return kucsldxh;
	}
	public void setKucsldxh(BigDecimal kucsldxh) {
		this.kucsldxh = kucsldxh;
	}
	public BigDecimal getXianbllkc() {
		return xianbllkc;
	}
	public void setXianbllkc(BigDecimal xianbllkc) {
		this.xianbllkc = xianbllkc;
	}
	public BigDecimal getPdsjkc() {
		return pdsjkc;
	}
	public void setPdsjkc(BigDecimal pdsjkc) {
		this.pdsjkc = pdsjkc;
	}
	public BigDecimal getPanddxh() {
		return panddxh;
	}
	public void setPanddxh(BigDecimal panddxh) {
		this.panddxh = panddxh;
	}
	public BigDecimal getPandckl() {
		return pandckl;
	}
	public void setPandckl(BigDecimal pandckl) {
		this.pandckl = pandckl;
	}
	public BigDecimal getPandycxh() {
		return pandycxh;
	}
	public void setPandycxh(BigDecimal pandycxh) {
		this.pandycxh = pandycxh;
	}
	public String getAnxscjssj() {
		return anxscjssj;
	}
	public void setAnxscjssj(String anxscjssj) {
		this.anxscjssj = anxscjssj;
	}
	
	
	
}

/**
 * 零件参考
 */
package com.athena.xqjs.module.hlorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * @author Administrator
 *
 */
public class LingjCangkYuxx {
	private String usercenter; //用户中心
	private String lingjbh;//零件编号
	private String cangkdm; //仓库代码
	private BigDecimal anqkc; //安全库存
	private BigDecimal yaohl; //要货量
	private java.math.BigDecimal jingd; //既定
	private BigDecimal yiny; //原始盈余
	private List<LingjYuxx>gongyss = new ArrayList<LingjYuxx>(); //包含的供应商
	private Map<String,BigDecimal> shijgyfe; //实际供应份额
	private Boolean shijgyfejs = false;  //实际供应份额计算标记
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getCangkdm() {
		return cangkdm;
	}
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}
	
	
	/**
	 * 产生主键
	 * @return
	 */
	public String createKey(){
		return usercenter+"_"+cangkdm+"_"+lingjbh;
	}
	
	/**
	 * 增加供应商
	 * @param lingjYuxx
	 */
	public void addGongys(LingjYuxx lingjYuxx){
		if(lingjYuxx != null){
			gongyss.add(lingjYuxx);
		}
	}
	
	
	public BigDecimal getYaohl() {
		return yaohl;
	}
	public void setYaohl(BigDecimal yaohl) {
		this.yaohl = yaohl;
	}
	public BigDecimal getAnqkc() {
		return anqkc;
	}
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}
	public java.math.BigDecimal getJingd() {
		return jingd;
	}
	public void setJingd(java.math.BigDecimal jingd) {
		this.jingd = jingd;
	}
	
	
	/**
	 * 计算盈余
	 * @param jingd
	 * @return
	 */
	public BigDecimal computerYingy(){
		long leijyhl = 0; //累计要货量
		for (LingjYuxx gys: gongyss){
			leijyhl = leijyhl + gys.getQuzyhl().longValue(); //累计取整要货量
		}
		if(yiny == null){
			yiny = BigDecimal.ZERO;
		}
		long dangqyiny  = leijyhl - jingd.longValue()+ yiny.longValue(); //当前盈余
		if(dangqyiny >= 0){
			return BigDecimal.valueOf(dangqyiny);
		}else{
			return BigDecimal.ZERO;
		}
	}
	public BigDecimal getYiny() {
		return yiny;
	}
	public void setYiny(BigDecimal yiny) {
		if(yiny == null){
			yiny = BigDecimal.ZERO;
		}
		this.yiny = yiny;
	}
	public Map<String, BigDecimal> getShijgyfe() {
		return shijgyfe;
	}
	public void setShijgyfe(Map<String, BigDecimal> shijgyfe) {
		this.shijgyfe = shijgyfe;
	}
	public Boolean getShijgyfejs() {
		return shijgyfejs;
	}
	public void setShijgyfejs(Boolean shijgyfejs) {
		this.shijgyfejs = shijgyfejs;
	}
	
	
}

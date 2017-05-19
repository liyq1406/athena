package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 预告中间表
 * @author 李明
 * @version V1.0
 * @date 2012-01-03
 */
public class Yugzjb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal uabzucsl;//UA包装内UC数量
	private Date yaohjsrq;//要货结束日期
	private String gongysdm;//供应商代码
	private String id;//ID
	private String lingjbh;//零件号
	private String dingdh;//订单号
	private String usercenter;//用户中心
	private BigDecimal uabzucrl;//UA包装内UC容量
	private String uabzuclx;//UA包装内UC类型
	private String gongyslx;//供应商类型
	private String uabzlx;//UA包装类型
	private Date yaohqsrq;//要货起始日期
	private String cangkdm;//仓库代码
	private BigDecimal shul;//数量
	private String danw;//单位
	private String gonghlx;//供货类型
	private String p0fyzqxh;//p0周期序号
	private String zqxh;//周期序号
	private String dinghcj;
	private String lingjsx;
	
	public String getDinghcj() {
		return dinghcj;
	}

	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}

	public String getLingjsx() {
		return lingjsx;
	}

	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}

	public String getZqxh() {
		return zqxh;
	}

	public void setZqxh(String zqxh) {
		this.zqxh = zqxh;
	}

	public String getP0fyzqxh() {
		return p0fyzqxh;
	}

	public void setP0fyzqxh(String p0fyzqxh) {
		this.p0fyzqxh = p0fyzqxh;
	}

	public BigDecimal getUabzucsl(){
		return this.uabzucsl;
	}
	
	public void setUabzucsl(BigDecimal uabzucsl){
		this.uabzucsl = uabzucsl;
	}
	

	
	public Date getYaohjsrq() {
		return yaohjsrq;
	}

	public void setYaohjsrq(Date yaohjsrq) {
		this.yaohjsrq = yaohjsrq;
	}

	public Date getYaohqsrq() {
		return yaohqsrq;
	}

	public void setYaohqsrq(Date yaohqsrq) {
		this.yaohqsrq = yaohqsrq;
	}

	public String getGongysdm(){
		return this.gongysdm;
	}
	
	public void setGongysdm(String gongysdm){
		this.gongysdm = gongysdm;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getLingjbh(){
		return this.lingjbh;
	}
	
	public void setLingjbh(String lingjbh){
		this.lingjbh = lingjbh;
	}
	
	public String getDingdh(){
		return this.dingdh;
	}
	
	public void setDingdh(String dingdh){
		this.dingdh = dingdh;
	}
	
	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	
	public BigDecimal getUabzucrl(){
		return this.uabzucrl;
	}
	
	public void setUabzucrl(BigDecimal uabzucrl){
		this.uabzucrl = uabzucrl;
	}
	
	public String getUabzuclx(){
		return this.uabzuclx;
	}
	
	public void setUabzuclx(String uabzuclx){
		this.uabzuclx = uabzuclx;
	}
	
	public String getGongyslx(){
		return this.gongyslx;
	}
	
	public void setGongyslx(String gongyslx){
		this.gongyslx = gongyslx;
	}
	
	public String getUabzlx(){
		return this.uabzlx;
	}
	
	public void setUabzlx(String uabzlx){
		this.uabzlx = uabzlx;
	}
	
	
	
	public String getCangkdm(){
		return this.cangkdm;
	}
	
	public void setCangkdm(String cangkdm){
		this.cangkdm = cangkdm;
	}
	
	public BigDecimal getShul(){
		return this.shul;
	}
	
	public void setShul(BigDecimal shul){
		this.shul = shul;
	}
	
	public String getDanw(){
		return this.danw;
	}
	
	public void setDanw(String danw){
		this.danw = danw;
	}
	
	public String getGonghlx(){
		return this.gonghlx;
	}
	
	public void setGonghlx(String gonghlx){
		this.gonghlx = gonghlx;
	}
	
}
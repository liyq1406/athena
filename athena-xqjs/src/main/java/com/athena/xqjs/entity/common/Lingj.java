
package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 零件表
 * @author
 * @version
 * 
 */
@Component(value="lingj")
public class Lingj extends PageableSupport{
	
	private static final long serialVersionUID = 8313908517789579717L;
	
	private String kaisrq;//开始日期
	private String dinghcj;//订货车间
	private String zhongwmc;//零件中文名称
	private String zongcldm;//总成类型代码
	private String lingjzl;//零件重量
	private String lingjsx;//零件属性
	private String lingjlx;//零件类型
	private String gongybm;//工艺编码
	private String diycqysj;//第一次启运时间
	private String zhizlx;//制造路线
	private String danw;//单位
	private String anqm;//安全码
	private String biaos;//生效标识
	private String fawmc;//零件法文名称
	private String jiesrq;//结束日期
	private String guanjljjb;//关键零件级别
	private String usercenter;//用户中心
	private BigDecimal zhuangcxs;// 装车系数
	private String lingjbh;//零件号
	private String jihy;//计划员代码
	private String anjmlxhd;//按件目录卸货点

	
	

	public String getKaisrq(){
		return this.kaisrq;
	}
	
	public void setKaisrq(String kaisrq){
		this.kaisrq = kaisrq;
	}
	public String getDinghcj(){
		return this.dinghcj;
	}
	
	public void setDinghcj(String dinghcj){
		this.dinghcj = dinghcj;
	}
	public String getZhongwmc(){
		return this.zhongwmc;
	}
	
	public void setZhongwmc(String zhongwmc){
		this.zhongwmc = zhongwmc;
	}
	public String getZongcldm(){
		return this.zongcldm;
	}
	
	public void setZongcldm(String zongclx){
		this.zongcldm = zongclx;
	}
	public String getLingjzl(){
		return this.lingjzl;
	}
	
	public void setLingjzl(String lingjzl){
		this.lingjzl = lingjzl;
	}
	public String getLingjlx(){
		return this.lingjlx;
	}
	
	public void setLingjlx(String lingjlx){
		this.lingjlx = lingjlx;
	}
	public String getGongybm(){
		return this.gongybm;
	}
	
	public void setGongybm(String gongybm){
		this.gongybm = gongybm;
	}
	public String getDiycqysj(){
		return this.diycqysj;
	}
	
	public void setDiycqysj(String diycqysj){
		this.diycqysj = diycqysj;
	}

	
	
	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getDanw(){
		return this.danw;
	}
	
	public void setDanw(String danw){
		this.danw = danw;
	}
	public String getAnqm(){
		return this.anqm;
	}
	
	public void setAnqm(String anqm){
		this.anqm = anqm;
	}
	
	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getFawmc(){
		return this.fawmc;
	}
	
	public void setFawmc(String fawmc){
		this.fawmc = fawmc;
	}
	public String getJiesrq(){
		return this.jiesrq;
	}
	
	public void setJiesrq(String jiesrq){
		this.jiesrq = jiesrq;
	}

	public String getGuanjljjb() {
		return guanjljjb;
	}

	public BigDecimal getZhuangcxs() {
		return zhuangcxs;
	}

	public void setZhuangcxs(BigDecimal zhuangcxs) {
		this.zhuangcxs = zhuangcxs;
	}

	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}

	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	
	
	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getJihy(){
		return this.jihy;
	}
	
	public void setJihy(String jihy){
		this.jihy = jihy;
	}
	
	public  Lingj() {
		 
	}

	public String getLingjsx() {
		return lingjsx;
	}

	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}

	public String getAnjmlxhd() {
		return anjmlxhd;
	}

	public void setAnjmlxhd(String anjmlxhd) {
		this.anjmlxhd = anjmlxhd;
	}
	
	
}
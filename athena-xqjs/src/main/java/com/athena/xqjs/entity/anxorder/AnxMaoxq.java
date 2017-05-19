package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * @按需毛需求bean
 * @author   李明
 * @date     2012-3-19
 */
public class AnxMaoxq extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private String usercenter ;
	private String ofh ;
	private String zhongzlxh ;
	private String hanzbs ;
	private String emon ;
	private String shunxh ;
	private String lingjbh;
	private String danw;
	private String xiaohd;
	private String xhsj;
	private BigDecimal xiaohxs ;
	private String xuqly ;
	private String chejh ;
	private String chanx ;
	private String zhizlx ;
	private String caifsj ;
	//增加字段
	private String startTime ;
	private String startTime2 ;
	private String waibms;
	private String mos2;
	
	//   0007182: 增加按需毛需求查询界面  按需 毛需求主页面初始化
	private String laiybs;//来源标识1，CLV 商业化；2，JT；3，SPPV；
	
	private String dangqms; //当前模式
	
	private String flag;    //处理标识
	
	private String xhsjTo;  //消耗时间段从
	 
	private String xhsjFrom;  //消耗时间段至
	
	
	public String getLaiybs() {
		return laiybs;
	}
	public void setLaiybs(String laiybs) {
		this.laiybs = laiybs;
	}
	public String getDangqms() {
		return dangqms;
	}
	public void setDangqms(String dangqms) {
		this.dangqms = dangqms;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getXhsjTo() {
		return xhsjTo;
	}
	public void setXhsjTo(String xhsjTo) {
		this.xhsjTo = xhsjTo;
	}
	public String getXhsjFrom() {
		return xhsjFrom;
	}
	public void setXhsjFrom(String xhsjFrom) {
		this.xhsjFrom = xhsjFrom;
	}
	public String getStartTime2() {
		return startTime2;
	}
	public void setStartTime2(String startTime2) {
		this.startTime2 = startTime2;
	}
	public String getWaibms() {
		return waibms;
	}
	public void setWaibms(String waibms) {
		this.waibms = waibms;
	}
	public String getMos2() {
		return mos2;
	}
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getOfh() {
		return ofh;
	}
	public void setOfh(String ofh) {
		this.ofh = ofh;
	}
	public String getZhongzlxh() {
		return zhongzlxh;
	}
	public void setZhongzlxh(String zhongzlxh) {
		this.zhongzlxh = zhongzlxh;
	}
	public String getHanzbs() {
		return hanzbs;
	}
	public void setHanzbs(String hanzbs) {
		this.hanzbs = hanzbs;
	}
	public String getEmon() {
		return emon;
	}
	public void setEmon(String emon) {
		this.emon = emon;
	}
	public String getShunxh() {
		return shunxh;
	}
	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
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
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getXhsj() {
		return xhsj;
	}
	public void setXhsj(String xhsj) {
		this.xhsj = xhsj;
	}
	public BigDecimal getXiaohxs() {
		return xiaohxs;
	}
	public void setXiaohxs(BigDecimal xiaohxs) {
		this.xiaohxs = xiaohxs;
	}
	
	public String getXuqly() {
		return xuqly;
	}
	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}
	public String getChejh() {
		return chejh;
	}
	public void setChejh(String chejh) {
		this.chejh = chejh;
	}
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getCaifsj() {
		return caifsj;
	}
	public void setCaifsj(String caifsj) {
		this.caifsj = caifsj;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	
}

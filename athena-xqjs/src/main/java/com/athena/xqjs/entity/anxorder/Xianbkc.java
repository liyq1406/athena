package com.athena.xqjs.entity.anxorder;
import java.math.BigDecimal;
import java.util.Date;

import com.toft.core3.support.PageableSupport;
/*
 * 线边库存
 * @author zbb
 */
		
		
public class Xianbkc extends PageableSupport  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7554758297405767579L;
	//用户中心
	private String usercenter;
	//产线
	private String chanx;
	//零件编号
	private String lingjbh;
	//消耗点/仓库
	private String mudd; 
	//类型,1仓库，2消耗点
	private String leix; 
	//单位
	private String danw;
	//盈余
	private BigDecimal yingy; 
	//总装流水号
	private String zongzlsh;
	//整车序号
	private Long zhengcxh; 
	//盘点异常
	private BigDecimal pandcy; 
	//标识
	private String biaos; 
	//状态
	private String status; 
	//创建者
	private String creator;
	//创建时间
	private Date create_time; 
	//修改者
	private String editor; 
	//修改时间
	private Date edit_time; 
	//备注1
	private String beiz1; 
	//备注2
	private String beiz2; 
	//备注3
	private BigDecimal beiz3;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public BigDecimal getYingy() {
		return yingy;
	}
	public void setYingy(BigDecimal yingy) {
		this.yingy = yingy;
	}
	public String getZongzlsh() {
		return zongzlsh;
	}
	public void setZongzlsh(String zongzlsh) {
		this.zongzlsh = zongzlsh;
	}
	public Long getZhengcxh() {
		return zhengcxh;
	}
	public void setZhengcxh(Long zhengcxh) {
		this.zhengcxh = zhengcxh;
	}
	public BigDecimal getPandcy() {
		return pandcy;
	}
	public void setPandcy(BigDecimal pandcy) {
		this.pandcy = pandcy;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}
	public BigDecimal getBeiz3() {
		return beiz3;
	}
	public void setBeiz3(BigDecimal beiz3) {
		this.beiz3 = beiz3;
	}
}

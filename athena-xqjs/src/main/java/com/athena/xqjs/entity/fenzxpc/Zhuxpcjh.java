package com.athena.xqjs.entity.fenzxpc;

import java.io.Serializable;

/**
 * 主线计划
 * @author dsimedd001
 *
 */
public class Zhuxpcjh implements Comparable<Zhuxpcjh>, Serializable{

	private static final long serialVersionUID = 7648937049850298447L;
	
	/**
	 * 大线总顺序（离线前）
	 */
	private String daxzsx_lixq;
	
	/**
	 * 大线总顺序（离线后）
	 */
	private String daxzsx_lixh;
	
	/**
	 * 预计进焊装时间
	 */
	private String yujjhzsj;
	
	/**
	 * 大线顺序（当日）
	 */
	private int daxsx;

	/**
	 * 离线类型
	 */
	private String lixlx;

	/**
	 * 总成号
	 */
	private String zongch;

	/**
	 * 订单号
	 */
	private String dingdh;

	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 大线线号
	 */
	private String daxxh;

	/**
	 * 展开日期
	 */
	private String zhankrq;

	/**
	 * 离线点
	 */
	private String lixd;

	/**
	 * 提前车位
	 */
	private Integer tiqcw;

	/**
	 * 焊装车型
	 */
	private String hanzcx;

	/**
	 * 需求来源	1:SPPV	2:CLV
	 */
	private String xuqly;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;
	
	/**
	 * 拆分完成
	 */
	private boolean spiltFinished = Boolean.FALSE;
	
	private String newyujjhzsj;//新的进焊装时间，用作分装线去重

	public String getNewyujjhzsj() {
		return newyujjhzsj;
	}

	public void setNewyujjhzsj(String newyujjhzsj) {
		this.newyujjhzsj = newyujjhzsj;
	}

	public String getDaxzsx_lixq() {
		return daxzsx_lixq;
	}

	public void setDaxzsx_lixq(String daxzsx_lixq) {
		this.daxzsx_lixq = daxzsx_lixq;
	}

	public String getDaxzsx_lixh() {
		return daxzsx_lixh;
	}

	public void setDaxzsx_lixh(String daxzsx_lixh) {
		this.daxzsx_lixh = daxzsx_lixh;
	}

	public String getYujjhzsj() {
		return yujjhzsj;
	}

	public void setYujjhzsj(String yujjhzsj) {
		this.yujjhzsj = yujjhzsj;
	}

	public int getDaxsx() {
		return daxsx;
	}

	public void setDaxsx(int daxsx) {
		this.daxsx = daxsx;
	}

	public String getLixlx() {
		return lixlx;
	}

	public void setLixlx(String lixlx) {
		this.lixlx = lixlx;
	}

	public String getZongch() {
		return zongch;
	}

	public void setZongch(String zongch) {
		this.zongch = zongch;
	}

	public String getDingdh() {
		return dingdh;
	}

	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getZhankrq() {
		return zhankrq;
	}

	public void setZhankrq(String zhankrq) {
		this.zhankrq = zhankrq;
	}

	public String getLixd() {
		return lixd;
	}

	public void setLixd(String lixd) {
		this.lixd = lixd;
	}

	public Integer getTiqcw() {
		return tiqcw;
	}

	public void setTiqcw(Integer tiqcw) {
		this.tiqcw = tiqcw;
	}

	public String getHanzcx() {
		return hanzcx;
	}

	public void setHanzcx(String hanzcx) {
		this.hanzcx = hanzcx;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
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

	public boolean isSpiltFinished() {
		return spiltFinished;
	}

	public void setSpiltFinished(boolean spiltFinished) {
		this.spiltFinished = spiltFinished;
	}

	/**
	 * 先比较预计进焊装时间，再比较大线顺序号
	 */
	public int compareTo(Zhuxpcjh o) {
		int result = this.getYujjhzsj().compareTo(o.getYujjhzsj());
		if(result != 0){
			return result;
		}
		if(this.getDaxsx() < o.getDaxsx()){
			return -1;
		}else if(this.getDaxsx() > o.getDaxsx()){
			return 1;
		}else{
			return 0;
		}
	}

}

package com.athena.xqjs.entity.fenzxpc;

import java.io.Serializable;

import com.toft.core3.support.PageableSupport;

/**
 * 分装线排产计划
 * @author dsimedd001
 *
 */
public class Fenzxpcjh extends PageableSupport implements Comparable<Fenzxpcjh>, Serializable{

	private static final long serialVersionUID = -1641274261290719477L;

	/**
	 * 分装线总顺序（离线前）
	 */
	private String fenzxzsx_lixq;

	/**
	 * 分装线总顺序（离线后）
	 */
	private String fenzxzsx_lixh;

	/**
	 * 预计上线日期
	 */
	private String yujsxrq;

	/**
	 * 分装线顺序
	 */
	private String fenzsx;
	
	/**
	 * 对应大线顺序
	 */
	private Integer duiydxsx;
	
	/**
	 * 上线顺序
	 */
	private Integer shangxsx;

	/**
	 * 下线顺序
	 */
	private Integer xiaxsx;

	/**
	 * 分总成号
	 */
	private String fenzch;

	/**
	 * 订单号
	 */
	private String dingdh;

	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 分装线号
	 */
	private String fenzxh;

	/**
	 * 焊装车型
	 */
	private String hanzcx;

	/**
	 * 零件类型	L:左		R:右
	 */
	private String lingjlx;

	/**
	 * 零件用途	B:备件	W:外销	A:安全库存
	 */
	private String lingjyt;

	/**
	 * 离线点（fenzx）
	 */
	private String lixd;

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
	 * 展开日期
	 */
	private String zhankrq;
	
	/**
	 * 提前车位
	 */
	private Integer tiqcw;
	
	/**
	 * 是否备件外销
	 */
	private boolean beijwx;
	
	/**
	 * 预计下线日期
	 */
	private String yujxxrq;
	
	/**
	 * 对应的大线总顺序（离线前）
	 */
	private String daxzsx_liq;
	
	/**
	 * 大线离线点
	 */
	private String daxLixd;
	
	/**
	 * 类型（1：主线计划拆分出的计划	2：备件外销插入	3：备件外销单独排产）
	 */
	private String leix;
	
	/**
	 * 大线离线点
	 */
	private String daxlxd;
	
	/**
	 * 是否分装线排空
	 */
	private String shiffzxpk;
	
	
	/**
	 * 离线导致前一天被挤过来的计划
	 */
	private String xixldjh;//离线来的计划
	
	
	/**
	 * 总成拆分系数，系数有多少，就拆分为多少个分装线排产计划
	 */
	private Integer zhongccfxs;//总成拆分系数
	
	



	public Integer getZhongccfxs() {
		return zhongccfxs;
	}

	public void setZhongccfxs(Integer zhongccfxs) {
		this.zhongccfxs = zhongccfxs;
	}

	public String getXixldjh() {
		return xixldjh;
	}

	public void setXixldjh(String xixldjh) {
		this.xixldjh = xixldjh;
	}

	public String getShiffzxpk() {
		return shiffzxpk;
	}

	public void setShiffzxpk(String shiffzxpk) {
		this.shiffzxpk = shiffzxpk;
	}

	public String getDaxlxd() {
		return daxlxd;
	}

	public void setDaxlxd(String daxlxd) {
		this.daxlxd = daxlxd;
	}

	public String getFenzxzsx_lixq() {
		return fenzxzsx_lixq;
	}

	public void setFenzxzsx_lixq(String fenzxzsx_lixq) {
		this.fenzxzsx_lixq = fenzxzsx_lixq;
	}

	public String getFenzxzsx_lixh() {
		return fenzxzsx_lixh;
	}

	public void setFenzxzsx_lixh(String fenzxzsx_lixh) {
		this.fenzxzsx_lixh = fenzxzsx_lixh;
	}

	public String getYujsxrq() {
		return yujsxrq;
	}

	public void setYujsxrq(String yujsxrq) {
		this.yujsxrq = yujsxrq;
	}

	public String getFenzsx() {
		return fenzsx;
	}

	public void setFenzsx(String fenzsx) {
		this.fenzsx = fenzsx;
	}

	public String getFenzch() {
		return fenzch;
	}

	public void setFenzch(String fenzch) {
		this.fenzch = fenzch;
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

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getHanzcx() {
		return hanzcx;
	}

	public void setHanzcx(String hanzcx) {
		this.hanzcx = hanzcx;
	}

	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public String getLingjyt() {
		return lingjyt;
	}

	public void setLingjyt(String lingjyt) {
		this.lingjyt = lingjyt;
	}

	public String getLixd() {
		return lixd;
	}

	public void setLixd(String lixd) {
		this.lixd = lixd;
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

	public String getZhankrq() {
		return zhankrq;
	}

	public void setZhankrq(String zhankrq) {
		this.zhankrq = zhankrq;
	}

	public Integer getDuiydxsx() {
		return duiydxsx;
	}

	public void setDuiydxsx(Integer duiydxsx) {
		this.duiydxsx = duiydxsx;
	}

	public Integer getShangxsx() {
		return shangxsx;
	}

	public void setShangxsx(Integer shangxsx) {
		this.shangxsx = shangxsx;
	}

	public Integer getXiaxsx() {
		return xiaxsx;
	}

	public void setXiaxsx(Integer xiaxsx) {
		this.xiaxsx = xiaxsx;
	}

	public Integer getTiqcw() {
		return tiqcw;
	}

	public void setTiqcw(Integer tiqcw) {
		this.tiqcw = tiqcw;
	}

	public boolean isBeijwx() {
		return beijwx;
	}

	public void setBeijwx(boolean beijwx) {
		this.beijwx = beijwx;
	}

	public String getYujxxrq() {
		return yujxxrq;
	}

	public void setYujxxrq(String yujxxrq) {
		this.yujxxrq = yujxxrq;
	}
	
	public String getDaxzsx_liq() {
		return daxzsx_liq;
	}

	public void setDaxzsx_liq(String daxzsx_liq) {
		this.daxzsx_liq = daxzsx_liq;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

	public String getDaxLixd() {
		return daxLixd;
	}

	public void setDaxLixd(String daxLixd) {
		this.daxLixd = daxLixd;
	}

	/**
	 * 先比较预计进焊装时间，再比较计算顺序或对应大线顺序
	 */
	@Override
	public int compareTo(Fenzxpcjh o) {
		if(this.getFenzxzsx_lixq() != null && o.getFenzxzsx_lixq() != null){
			return this.getFenzxzsx_lixq().compareTo(o.getFenzxzsx_lixq());
		}
		if(this.getYujsxrq().compareTo(o.getYujsxrq()) != 0){
			return this.getYujsxrq().compareTo(o.getYujsxrq());
		}
		if(this.getShangxsx() != null && o.getShangxsx() != null){
			if(this.getShangxsx() < o.getShangxsx()){
				return -1;
			}else if(this.getShangxsx() > o.getShangxsx()){
				return 1;
			}else{
				return 0;
			}
		}else if(this.getXiaxsx() != null && o.getXiaxsx() != null){
			if(this.getXiaxsx() < o.getXiaxsx()){
				return -1;
			}else if(this.getXiaxsx() > o.getXiaxsx()){
				return 1;
			}else{
				return 0;
			}
		}else{
			if(this.getDuiydxsx() < o.getDuiydxsx()){
				return -1;
			}else if(this.getDuiydxsx() > o.getDuiydxsx()){
				return 1;
			}else{
				return 0;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		// 用户中心 + 分装线号 + 订单号 + 零件类型相同
		if(obj instanceof Fenzxpcjh){
			Fenzxpcjh jh = (Fenzxpcjh)obj;
			return this.getUsercenter().equals(jh.getUsercenter()) 
					&& this.getFenzxh().equals(jh.getFenzxh())
					&& this.getDingdh().equals(jh.getDingdh())
					&& (this.getLingjlx() == null || this.getLingjlx().equals(jh.getLingjlx()));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getUsercenter().hashCode() 
					+ this.getFenzxh().hashCode() 
					+ this.getDingdh().hashCode()
					+ this.getLingjlx() == null ? 55 : this.getLingjlx().hashCode();
	}

	
	
}

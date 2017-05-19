package com.athena.xqjs.entity.fenzxpc;

import java.util.Comparator;

import com.toft.core3.support.PageableSupport;

/**
 * 备件外销计划
 * @author dsimedd001
 *
 */
public class Beijwxjh extends PageableSupport {

	private static final long serialVersionUID = 4226246303133107981L;
	
	/**
	 * 流水号
	 */
	private String liush;
	
	/**
	 * 预计进焊装时间
	 */
	private String yujjhzsj;

	/**
	 * 总共数量
	 */
	private Integer shul;

	/**
	 * 完成数量
	 */
	private Integer wancsl;

	/**
	 * 排产数量
	 */
	private Integer paicsl;

	/**
	 * 总成号
	 */
	private String zongch;

	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 大线线号
	 */
	private String daxxh;

	/**
	 * 分装线号
	 */
	private String fenzxh;

	/**
	 * 展开日期
	 */
	private String zhankrq;

	/**
	 * 备件外销
	 */
	private String beijwx;

	/**
	 * 预留
	 */
	private String filler;

	/**
	 * 需求来源
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
	 * 零件类型
	 */
	private String lingjlx;
	
	/**
	 * 备件外销排序方式
	 */
	private String beijwxpxfs;
	
	/**
	 * 归集后的数量
	 */
	private Integer totalCount;

	public String getLiush() {
		return liush;
	}

	public void setLiush(String liush) {
		this.liush = liush;
	}

	public String getYujjhzsj() {
		return yujjhzsj;
	}

	public void setYujjhzsj(String yujjhzsj) {
		this.yujjhzsj = yujjhzsj;
	}

	public Integer getShul() {
		return shul;
	}

	public void setShul(Integer shul) {
		this.shul = shul;
	}

	public Integer getWancsl() {
		return wancsl;
	}

	public void setWancsl(Integer wancsl) {
		this.wancsl = wancsl;
	}

	public Integer getPaicsl() {
		return paicsl;
	}

	public void setPaicsl(Integer paicsl) {
		this.paicsl = paicsl;
	}

	public String getZongch() {
		return zongch;
	}

	public void setZongch(String zongch) {
		this.zongch = zongch;
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

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getZhankrq() {
		return zhankrq;
	}

	public void setZhankrq(String zhankrq) {
		this.zhankrq = zhankrq;
	}

	public String getBeijwx() {
		return beijwx;
	}

	public void setBeijwx(String beijwx) {
		this.beijwx = beijwx;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
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

	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}
	
	public String getBeijwxpxfs() {
		return beijwxpxfs;
	}

	public void setBeijwxpxfs(String beijwxpxfs) {
		this.beijwxpxfs = beijwxpxfs;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 比较预计进焊装时间
	 */
	public static Comparator<Beijwxjh> timeComparator = new Comparator<Beijwxjh>() {
		public int compare(Beijwxjh o1, Beijwxjh o2) {
			return o1.getYujjhzsj().compareTo(o2.getYujjhzsj());
		}
	};
	
	/**
	 * 比较数量
	 */
	public static Comparator<Beijwxjh> shulComparator = new Comparator<Beijwxjh>() {
		public int compare(Beijwxjh o1, Beijwxjh o2) {
			if(o1.getBeijwxpxfs().equals("1")){	//由小到大排序
				return o1.getTotalCount() - o2.getTotalCount();
			}else{	//由大到小排序
				return o2.getTotalCount() - o1.getTotalCount();
			}
		}
	};
	
	
}

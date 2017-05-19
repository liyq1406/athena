package com.athena.xqjs.entity.fenzxpc;

import java.util.Date;

import com.toft.core3.support.PageableSupport;

/**
 * 过点申报信息
 * @author dsimedd001
 *
 */
public class Guodsbxx extends PageableSupport {

	private static final long serialVersionUID = 4374787693237209563L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 订单号
	 */
	private String dingdh;
	
	/**
	 * 总成号
	 */
	private String zongch;

	/**
	 * 流水号
	 */
	private String zongzlsh;

	/**
	 * 分装线顺序号
	 */
	private String shunxh;

	/**
	 * 上线点
	 */
	private String hanzsxd;

	/**
	 * 生产线
	 */
	private String shengcx;

	/**
	 * 过点时间
	 */
	private Date guodsj;

	/**
	 * 物理点
	 */
	private String wuld;

	/**
	 * 申报类型
	 */
	private String shengblx;

	/**
	 * 报废状态	0:未处理		1:已处理
	 */
	private String baofzt;
	
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

	public String getZongch() {
		return zongch;
	}

	public void setZongch(String zongch) {
		this.zongch = zongch;
	}

	public String getZongzlsh() {
		return zongzlsh;
	}

	public void setZongzlsh(String zongzlsh) {
		this.zongzlsh = zongzlsh;
	}

	public String getShunxh() {
		return shunxh;
	}

	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
	}

	public String getShengcx() {
		return shengcx;
	}

	public void setShengcx(String shengcx) {
		this.shengcx = shengcx;
	}

	public Date getGuodsj() {
		return guodsj;
	}

	public void setGuodsj(Date guodsj) {
		this.guodsj = guodsj;
	}

	public String getWuld() {
		return wuld;
	}

	public void setWuld(String wuld) {
		this.wuld = wuld;
	}

	public String getShengblx() {
		return shengblx;
	}

	public void setShengblx(String shengblx) {
		this.shengblx = shengblx;
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

	public String getHanzsxd() {
		return hanzsxd;
	}

	public void setHanzsxd(String hanzsxd) {
		this.hanzsxd = hanzsxd;
	}

	public String getBaofzt() {
		return baofzt;
	}

	public void setBaofzt(String baofzt) {
		this.baofzt = baofzt;
	}
	
}

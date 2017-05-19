package com.athena.xqjs.entity.kdorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * 实体: kd毛需求明细
 * @author 陈骏
 * @version
 * 
 */
public class Kdmaoxq extends PageableSupport{
	/**
	 * 使用车间
	 */
	private String shiycj;
	/**
	 * 需求数量
	 */
	private BigDecimal xuqsl;
	/**
	 * 需求日期
	 */
	private String xuqrq;
	/**
	 * 起始周序号
	 */
	private String qiszxh;
	/**
	 * 需求所属周期
	 */
	private String xuqsszq;
	/**
	 * 制造路线
	 */
	private String zhizlx;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 需求所属周
	 */
	private String xuqz;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * ID
	 */
	private String id;
	/**
	 * 产线
	 */
	private String chanx;
	/**
	 * 需求版次
	 */
	private String xuqbc;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 修改人
	 */
	private String editor;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 修改时间
	 */
	private String edit_time;
	private String active;
	private String cangkdm;
	public String getCangkdm() {
		return cangkdm;
	}

	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getShiycj() {
		return shiycj;
	}

	public void setShiycj(String shiycj) {
		this.shiycj = shiycj;
	}

	public BigDecimal getXuqsl() {
		return xuqsl;
	}

	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}

	public String getXuqrq() {
		return xuqrq;
	}

	public void setXuqrq(String xuqrq) {
		this.xuqrq = xuqrq;
	}

	public String getQiszxh() {
		return qiszxh;
	}

	public void setQiszxh(String qiszxh) {
		this.qiszxh = qiszxh;
	}

	public String getXuqsszq() {
		return xuqsszq;
	}

	public void setXuqsszq(String xuqsszq) {
		this.xuqsszq = xuqsszq;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXuqz() {
		return xuqz;
	}

	public void setXuqz(String xuqz) {
		this.xuqz = xuqz;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getXuqbc() {
		return xuqbc;
	}

	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getXuqksrq() {
		return xuqksrq;
	}

	public void setXuqksrq(String xuqksrq) {
		this.xuqksrq = xuqksrq;
	}

	public String getXuqjsrq() {
		return xuqjsrq;
	}

	public void setXuqjsrq(String xuqjsrq) {
		this.xuqjsrq = xuqjsrq;
	}

	public String getXuqcfsj() {
		return xuqcfsj;
	}

	public void setXuqcfsj(String xuqcfsj) {
		this.xuqcfsj = xuqcfsj;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCangk() {
		return cangk;
	}

	public void setCangk(String cangk) {
		this.cangk = cangk;
	}

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	private String xuqksrq ;
	
	private String xuqjsrq ;
	
	private String xuqcfsj;
	
	private String xuqly;

	private String starttime;
	
	private String  endtime;
	
	private  String  cangk;
	
	private String cangklx;
}

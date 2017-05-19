package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 总成拆分结果
 * @author dsimedd001
 *
 */
public class Zongccfjg extends PageableSupport{

	private static final long serialVersionUID = -564471741926566650L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;

	/**
	 * 车间
	 */
	private String chej;

	/**
	 * 总成号
	 */
	private String zongch;

	/**
	 * 零件
	 */
	private String lingj;

	/**
	 * 消耗点
	 */
	private String xiaohd;

	/**
	 * 单位
	 */
	private String danw;

	/**
	 * 系数
	 */
	private Integer xis;
	
	/**
	 * 分装线号
	 */
	private String fenzxh;
	
	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;

	/**
	 * 修改人
	 */
	private String editor;

	/**
	 * 修改时间
	 */
	private String edit_time;

	/**
	 * 零件类型
	 */
	private String lingjlx;
	
	/**
	 * 展开日期
	 */
	private String zhankrq;
	
	/**
	 * 开始展开日期
	 */
	private String kaiszkrq;
	
	/**
	 * 结束展开日期
	 */
	private String jieszkrq;
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChej() {
		return chej;
	}

	public void setChej(String chej) {
		this.chej = chej;
	}

	public String getZongch() {
		return zongch;
	}

	public void setZongch(String zongch) {
		this.zongch = zongch;
	}

	public String getLingj() {
		return lingj;
	}

	public void setLingj(String lingj) {
		this.lingj = lingj;
	}

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public Integer getXis() {
		return xis;
	}

	public void setXis(Integer xis) {
		this.xis = xis;
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

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public String getKaiszkrq() {
		return kaiszkrq;
	}

	public void setKaiszkrq(String kaiszkrq) {
		this.kaiszkrq = kaiszkrq;
	}

	public String getJieszkrq() {
		return jieszkrq;
	}

	public void setJieszkrq(String jieszkrq) {
		this.jieszkrq = jieszkrq;
	}

	public String getZhankrq() {
		return zhankrq;
	}

	public void setZhankrq(String zhankrq) {
		this.zhankrq = zhankrq;
	}
}

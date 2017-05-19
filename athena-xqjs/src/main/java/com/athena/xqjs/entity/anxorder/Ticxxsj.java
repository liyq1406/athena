package com.athena.xqjs.entity.anxorder;

import com.toft.core3.support.PageableSupport;

/**
 * 未来几日剔除休息时间
 * @author dsimedd001
 *
 */
public class Ticxxsj extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7053177617675076474L;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 产线/仓库
	 */
	private String chanxck;
	/**
	 * 工作日
	 */
	private String gongzr;
	/**
	 * 顺序号
	 */
	private String shunxh;
	/**
	 * 日期
	 */
	private String riq;
	/**
	 * 时间段开始时刻
	 */
	private String shijdkssj;
	/**
	 * 时间段结束时刻
	 */
	private String shijdjssj;
	/**
	 * 时间长度（单位：分）
	 */
	private String shijcd;
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
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getChanxck() {
		return chanxck;
	}
	public void setChanxck(String chanxck) {
		this.chanxck = chanxck;
	}
	public String getGongzr() {
		return gongzr;
	}
	public void setGongzr(String gongzr) {
		this.gongzr = gongzr;
	}
	public String getShunxh() {
		return shunxh;
	}
	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
	}
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getShijdkssj() {
		return shijdkssj;
	}
	public void setShijdkssj(String shijdkssj) {
		this.shijdkssj = shijdkssj;
	}
	public String getShijdjssj() {
		return shijdjssj;
	}
	public void setShijdjssj(String shijdjssj) {
		this.shijdjssj = shijdjssj;
	}
	public String getShijcd() {
		return shijcd;
	}
	public void setShijcd(String shijcd) {
		this.shijcd = shijcd;
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
	
	

}

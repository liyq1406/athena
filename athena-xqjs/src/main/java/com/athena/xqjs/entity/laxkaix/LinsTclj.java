/**
 * 
 */
package com.athena.xqjs.entity.laxkaix;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * @author dsimedd001
 * 
 */
public class LinsTclj extends PageableSupport {
	private String usercenter; // 用户中心
	private String jihydm;// 计划员代码
	private String tcNo; // TC号
	private String qiysj;// 启运时间
	private String mudd;// 目的地
	private String lingjh;// 零件号
	private BigDecimal lingjsl;// 零件数量
	private String wuld;// 物理点
	private String yujddsj;// 预计到达神龙时间
	private String laxzdddsj;// 拉箱指定时间
	private String creator;//创建人
	private String createTime;//创建时间
	private String editor;//修改人
	private String editTime;//修改时间
	
	private String lajxsj;//拉箱界限时间
	
	private String zuixyjddsj;//最新预计到达时间
	
	private String ziysj;
	/**
	 * 取得 usercenter
	 * 
	 * @return the usercenter
	 */
	public String getUsercenter() {
		return usercenter;
	}

	/**
	 * @param usercenter
	 *            the usercenter to set
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	/**
	 * 取得 jihydm
	 * 
	 * @return the jihydm
	 */
	public String getJihydm() {
		return jihydm;
	}

	/**
	 * @param jihydm
	 *            the jihydm to set
	 */
	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
	}

	/**
	 * 取得 tcNo
	 * 
	 * @return the tcNo
	 */
	public String getTcNo() {
		return tcNo;
	}

	/**
	 * @param tcNo
	 *            the tcNo to set
	 */
	public void setTcNo(String tcNo) {
		this.tcNo = tcNo;
	}

	/**
	 * 取得 qiysj
	 * 
	 * @return the qiysj
	 */
	public String getQiysj() {
		return qiysj;
	}

	/**
	 * @param qiysj
	 *            the qiysj to set
	 */
	public void setQiysj(String qiysj) {
		this.qiysj = qiysj;
	}

	/**
	 * 取得 mudd
	 * 
	 * @return the mudd
	 */
	public String getMudd() {
		return mudd;
	}

	/**
	 * @param mudd
	 *            the mudd to set
	 */
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	/**
	 * 取得 lingjh
	 * 
	 * @return the lingjh
	 */
	public String getLingjh() {
		return lingjh;
	}

	/**
	 * @param lingjh
	 *            the lingjh to set
	 */
	public void setLingjh(String lingjh) {
		this.lingjh = lingjh;
	}

	/**
	 * 取得 lingjsl
	 * 
	 * @return the lingjsl
	 */
	public BigDecimal getLingjsl() {
		return lingjsl;
	}

	/**
	 * @param lingjsl
	 *            the lingjsl to set
	 */
	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}

	/**
	 * 取得 wuld
	 * 
	 * @return the wuld
	 */
	public String getWuld() {
		return wuld;
	}

	/**
	 * @param wuld
	 *            the wuld to set
	 */
	public void setWuld(String wuld) {
		this.wuld = wuld;
	}

	

	/**
	 * 取得 yujddsj
	 * @return the yujddsj
	 */
	public String getYujddsj() {
		return yujddsj;
	}

	/**
	 * @param yujddsj the yujddsj to set
	 */
	public void setYujddsj(String yujddsj) {
		this.yujddsj = yujddsj;
	}

	/**
	 * 取得 laxzdddsj
	 * @return the laxzdddsj
	 */
	public String getLaxzdddsj() {
		return laxzdddsj;
	}

	/**
	 * @param laxzdddsj the laxzdddsj to set
	 */
	public void setLaxzdddsj(String laxzdddsj) {
		this.laxzdddsj = laxzdddsj;
	}

	/**
	 * 取得 creator
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 取得 createTime
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 取得 editor
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * 取得 editTime
	 * @return the editTime
	 */
	public String getEditTime() {
		return editTime;
	}

	/**
	 * @param editTime the editTime to set
	 */
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	/**
	 * 取得 lajxsj
	 * @return the lajxsj
	 */
	public String getLajxsj() {
		return lajxsj;
	}

	/**
	 * @param lajxsj the lajxsj to set
	 */
	public void setLajxsj(String lajxsj) {
		this.lajxsj = lajxsj;
	}

	/**
	 * 取得 zuixyjddsj
	 * @return the zuixyjddsj
	 */
	public String getZuixyjddsj() {
		return zuixyjddsj;
	}

	/**
	 * @param zuixyjddsj the zuixyjddsj to set
	 */
	public void setZuixyjddsj(String zuixyjddsj) {
		this.zuixyjddsj = zuixyjddsj;
	}

	/**
	 * 取得 ziysj
	 * @return the ziysj
	 */
	public String getZiysj() {
		return ziysj;
	}

	/**
	 * @param ziysj the ziysj to set
	 */
	public void setZiysj(String ziysj) {
		this.ziysj = ziysj;
	}

	
	
	
}

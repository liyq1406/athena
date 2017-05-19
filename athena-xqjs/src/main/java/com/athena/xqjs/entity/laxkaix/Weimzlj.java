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
public class Weimzlj extends PageableSupport {
	private String usercenter;// 用户中心
	private String jihNo;// 拉箱/开箱计划号
	private String lingjh;// 零件号
	private String mudd;// 目的地
	private BigDecimal weimzsl;// 未满足数量
	private String duandsj;// 断点时间
	private BigDecimal cangkkc;//仓库库存
	private BigDecimal zhongxqkc;// 重箱区库存
	private String creator;//创建人
	private String createTime;//创建时间
	private String editor;//修改人
	private String editTime;//修改时间
	
	
	private String zhongwmc;
	private String jihy;
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
	 * 取得 jihNo
	 * 
	 * @return the jihNo
	 */
	public String getJihNo() {
		return jihNo;
	}

	/**
	 * @param jihNo
	 *            the jihNo to set
	 */
	public void setJihNo(String jihNo) {
		this.jihNo = jihNo;
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
	 * 取得 weimzsl
	 * 
	 * @return the weimzsl
	 */
	public BigDecimal getWeimzsl() {
		return weimzsl;
	}

	/**
	 * @param weimzsl
	 *            the weimzsl to set
	 */
	public void setWeimzsl(BigDecimal weimzsl) {
		this.weimzsl = weimzsl;
	}

	/**
	 * 取得 duandsj
	 * 
	 * @return the duandsj
	 */
	public String getDuandsj() {
		return duandsj;
	}

	/**
	 * @param duandsj
	 *            the duandsj to set
	 */
	public void setDuandsj(String duandsj) {
		this.duandsj = duandsj;
	}

	/**
	 * 取得 cangkkc
	 * 
	 * @return the cangkkc
	 */
	public BigDecimal getCangkkc() {
		return cangkkc;
	}

	/**
	 * @param cangkkc
	 *            the cangkkc to set
	 */
	public void setCangkkc(BigDecimal cangkkc) {
		this.cangkkc = cangkkc;
	}

	/**
	 * 取得 zhongxqkc
	 * 
	 * @return the zhongxqkc
	 */
	public BigDecimal getZhongxqkc() {
		return zhongxqkc;
	}

	/**
	 * @param zhongxqkc
	 *            the zhongxqkc to set
	 */
	public void setZhongxqkc(BigDecimal zhongxqkc) {
		this.zhongxqkc = zhongxqkc;
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
	 * 取得 zhongwmc
	 * @return the zhongwmc
	 */
	public String getZhongwmc() {
		return zhongwmc;
	}

	/**
	 * @param zhongwmc the zhongwmc to set
	 */
	public void setZhongwmc(String zhongwmc) {
		this.zhongwmc = zhongwmc;
	}

	/**
	 * 取得 jihy
	 * @return the jihy
	 */
	public String getJihy() {
		return jihy;
	}

	/**
	 * @param jihy the jihy to set
	 */
	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	
}

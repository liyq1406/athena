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
public class LinsXuq extends PageableSupport{
	private String usercenter;      // 用户中心
	private String jihydm;      // 计划员代码
	private String maoxqbc; // 毛需求版次
	private String lingjh;      // 零件号
	private String mudd;        // 目的地
	private BigDecimal cmj;     // CMJ
	private BigDecimal xuqsl;   // 需求数量
	private String duandsj;       // 断点时间
	private BigDecimal manzsl;  // 满足数量
	private BigDecimal cangkkc; // 仓库库存
	private BigDecimal zhongxqkc; // 重箱区库存
	private BigDecimal zhixts;   // 滞箱天数
	private String creator;//创建人
	private String createTime;//创建时间
	private String editor;//修改人
	private String editTime;//修改时间
	
	private BigDecimal anqkc;
	private BigDecimal laxjx;
	private BigDecimal kaixjx;
	private String laxbs;
	private BigDecimal ziyts;
	private String kyljdm;
	private BigDecimal daohl;
	private String laxjxDuandsj;
	/**
	 * 取得 usercenter
	 * @return the usercenter
	 */
	public String getUsercenter() {
		return usercenter;
	}

	/**
	 * @param usercenter the usercenter to set
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
	 * 取得 maoxqbc
	 * 
	 * @return the maoxqbc
	 */
	public String getMaoxqbc() {
		return maoxqbc;
	}

	/**
	 * @param maoxqbc
	 *            the maoxqbc to set
	 */
	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
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
	 * 取得 cmj
	 * @return the cmj
	 */
	public BigDecimal getCmj() {
		return cmj;
	}

	/**
	 * @param cmj the cmj to set
	 */
	public void setCmj(BigDecimal cmj) {
		this.cmj = cmj;
	}

	/**
	 * 取得 xuqsl
	 * 
	 * @return the xuqsl
	 */
	public BigDecimal getXuqsl() {
		return xuqsl;
	}

	/**
	 * @param xuqsl
	 *            the xuqsl to set
	 */
	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
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
	 * 取得 manzsl
	 * 
	 * @return the manzsl
	 */
	public BigDecimal getManzsl() {
		return manzsl;
	}

	/**
	 * @param manzsl
	 *            the manzsl to set
	 */
	public void setManzsl(BigDecimal manzsl) {
		this.manzsl = manzsl;
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
	 * 取得 zhixts
	 * 
	 * @return the zhixts
	 */
	public BigDecimal getZhixts() {
		return zhixts;
	}

	/**
	 * @param zhixts
	 *            the zhixts to set
	 */
	public void setZhixts(BigDecimal zhixts) {
		this.zhixts = zhixts;
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
	 * 取得 anqkc
	 * @return the anqkc
	 */
	public BigDecimal getAnqkc() {
		return anqkc;
	}

	/**
	 * @param anqkc the anqkc to set
	 */
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}

	/**
	 * 取得 laxjx
	 * @return the laxjx
	 */
	public BigDecimal getLaxjx() {
		return laxjx;
	}

	/**
	 * @param laxjx the laxjx to set
	 */
	public void setLaxjx(BigDecimal laxjx) {
		this.laxjx = laxjx;
	}

	/**
	 * 取得 kaixjx
	 * @return the kaixjx
	 */
	public BigDecimal getKaixjx() {
		return kaixjx;
	}

	/**
	 * @param kaixjx the kaixjx to set
	 */
	public void setKaixjx(BigDecimal kaixjx) {
		this.kaixjx = kaixjx;
	}

	/**
	 * 取得 laxbs
	 * @return the laxbs
	 */
	public String getLaxbs() {
		return laxbs;
	}

	/**
	 * @param laxbs the laxbs to set
	 */
	public void setLaxbs(String laxbs) {
		this.laxbs = laxbs;
	}

	/**
	 * 取得 ziyts
	 * @return the ziyts
	 */
	public BigDecimal getZiyts() {
		return ziyts;
	}

	/**
	 * @param ziyts the ziyts to set
	 */
	public void setZiyts(BigDecimal ziyts) {
		this.ziyts = ziyts;
	}

	/**
	 * 取得 kyljdm
	 * @return the kyljdm
	 */
	public String getKyljdm() {
		return kyljdm;
	}

	/**
	 * @param kyljdm the kyljdm to set
	 */
	public void setKyljdm(String kyljdm) {
		this.kyljdm = kyljdm;
	}

	/**
	 * 取得 daohl
	 * @return the daohl
	 */
	public BigDecimal getDaohl() {
		return daohl;
	}

	/**
	 * @param daohl the daohl to set
	 */
	public void setDaohl(BigDecimal daohl) {
		this.daohl = daohl;
	}

	/**
	 * 取得 laxjxDuandsj
	 * @return the laxjxDuandsj
	 */
	public String getLaxjxDuandsj() {
		return laxjxDuandsj;
	}

	/**
	 * @param laxjxDuandsj the laxjxDuandsj to set
	 */
	public void setLaxjxDuandsj(String laxjxDuandsj) {
		this.laxjxDuandsj = laxjxDuandsj;
	}

	
	
}

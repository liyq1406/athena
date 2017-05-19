package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 实体: 零件消耗点
 */
public class Lingjxhd extends PageableSupport {
	private static final long serialVersionUID = 8313908517789579717L;
	private String usercenter;// 用户中心
	private String lingjbh;// 零件号
	private String xiaohdbh;// 消耗点编号
	private String wulbh;// 物流编号
	private BigDecimal jicdwjfl;//继承的未交付量
	private String fenpqbh;
	private String shengcxbh;
	private String peislxbh;
	private String qianhcbs;
	private String jipbzwz;
	private String shengxr;
	private String jiesr;
	private String guanlybh;
	private BigDecimal xiaohbl;
	private BigDecimal tiqjsxhbl;
	private String gongysbh;
	private String shunxglbz;
	private String zidfhbz;
	private String xianbyhlx;
	private String yancsxbz;
	private String beihdbz;
	private String xiaohcbh;
	private String xiaohccxbh;
	private String yuanxhdbh;
	private BigDecimal anqkcts;
	private BigDecimal anqkcs;
	private BigDecimal xianbllkc;
	private BigDecimal yifyhlzl;
	private BigDecimal jiaofzl;
	private BigDecimal xittzz;
	private String pdsbz;
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
	private BigDecimal zhongzzl;
	private String biaos;
	
	private String pdsshengxsj; //PDS生效时间
	private String pdsshixsj;   //PDS失效时间
	
	
	public String getPdsshengxsj() {
		return pdsshengxsj;
	}
	public void setPdsshengxsj(String pdsshengxsj) {
		this.pdsshengxsj = pdsshengxsj;
	}
	public String getPdsshixsj() {
		return pdsshixsj;
	}
	public void setPdsshixsj(String pdsshixsj) {
		this.pdsshixsj = pdsshixsj;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	/**
	 * @return the jicdwjfl
	 */
	public BigDecimal getJicdwjfl() {
		return jicdwjfl;
	}
	/**
	 * @param jicdwjfl the jicdwjfl to set
	 */
	public void setJicdwjfl(BigDecimal jicdwjfl) {
		this.jicdwjfl = jicdwjfl;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getXiaohdbh() {
		return xiaohdbh;
	}
	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}
	public String getWulbh() {
		return wulbh;
	}
	public void setWulbh(String wulbh) {
		this.wulbh = wulbh;
	}
	public String getFenpqbh() {
		return fenpqbh;
	}
	public void setFenpqbh(String fenpqbh) {
		this.fenpqbh = fenpqbh;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public String getPeislxbh() {
		return peislxbh;
	}
	public void setPeislxbh(String peislxbh) {
		this.peislxbh = peislxbh;
	}
	public String getQianhcbs() {
		return qianhcbs;
	}
	public void setQianhcbs(String qianhcbs) {
		this.qianhcbs = qianhcbs;
	}
	public String getJipbzwz() {
		return jipbzwz;
	}
	public void setJipbzwz(String jipbzwz) {
		this.jipbzwz = jipbzwz;
	}
	public String getShengxr() {
		return shengxr;
	}
	public void setShengxr(String shengxr) {
		this.shengxr = shengxr;
	}
	public String getJiesr() {
		return jiesr;
	}
	public void setJiesr(String jiesr) {
		this.jiesr = jiesr;
	}
	public String getGuanlybh() {
		return guanlybh;
	}
	public void setGuanlybh(String guanlybh) {
		this.guanlybh = guanlybh;
	}
	public BigDecimal getXiaohbl() {
		return xiaohbl;
	}
	public void setXiaohbl(BigDecimal xiaohbl) {
		this.xiaohbl = xiaohbl;
	}

	public BigDecimal getTiqjsxhbl() {
		return tiqjsxhbl;
	}
	public void setTiqjsxhbl(BigDecimal tiqjsxhbl) {
		this.tiqjsxhbl = tiqjsxhbl;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getShunxglbz() {
		return shunxglbz;
	}
	public void setShunxglbz(String shunxglbz) {
		this.shunxglbz = shunxglbz;
	}
	public String getZidfhbz() {
		return zidfhbz;
	}
	public void setZidfhbz(String zidfhbz) {
		this.zidfhbz = zidfhbz;
	}
	public String getXianbyhlx() {
		return xianbyhlx;
	}
	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}
	public String getYancsxbz() {
		return yancsxbz;
	}
	public void setYancsxbz(String yancsxbz) {
		this.yancsxbz = yancsxbz;
	}
	public String getBeihdbz() {
		return beihdbz;
	}
	public void setBeihdbz(String beihdbz) {
		this.beihdbz = beihdbz;
	}
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public String getXiaohccxbh() {
		return xiaohccxbh;
	}
	public void setXiaohccxbh(String xiaohccxbh) {
		this.xiaohccxbh = xiaohccxbh;
	}
	public String getYuanxhdbh() {
		return yuanxhdbh;
	}
	public void setYuanxhdbh(String yuanxhdbh) {
		this.yuanxhdbh = yuanxhdbh;
	}
	public BigDecimal getAnqkcts() {
		return anqkcts;
	}
	public void setAnqkcts(BigDecimal anqkcts) {
		this.anqkcts = anqkcts;
	}
	public BigDecimal getAnqkcs() {
		return anqkcs;
	}
	public void setAnqkcs(BigDecimal anqkcs) {
		this.anqkcs = anqkcs;
	}
	public BigDecimal getXianbllkc() {
		return xianbllkc;
	}
	public void setXianbllkc(BigDecimal xianbllkc) {
		this.xianbllkc = xianbllkc;
	}
	public BigDecimal getYifyhlzl() {
		return yifyhlzl;
	}
	public void setYifyhlzl(BigDecimal yifyhlzl) {
		this.yifyhlzl = yifyhlzl;
	}
	public BigDecimal getJiaofzl() {
		return jiaofzl;
	}
	public void setJiaofzl(BigDecimal jiaofzl) {
		this.jiaofzl = jiaofzl;
	}
	public BigDecimal getXittzz() {
		return xittzz;
	}
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}
	public String getPdsbz() {
		return pdsbz;
	}
	public void setPdsbz(String pdsbz) {
		this.pdsbz = pdsbz;
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
	public BigDecimal getZhongzzl() {
		return zhongzzl;
	}
	public void setZhongzzl(BigDecimal zhongzzl) {
		this.zhongzzl = zhongzzl;
	}
	
}
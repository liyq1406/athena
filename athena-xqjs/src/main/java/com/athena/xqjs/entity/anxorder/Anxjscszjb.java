package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * @按需计算参数中间表bean
 * @author   李明
 * @date     2012-3-27
 */
public class Anxjscszjb extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	//第一部分：按需毛需求
	private String usercenter ;
	private String chej ;
	private String xianh;
	private String liush;
	private String xiaohd;
	private String lingjbh;
	private String danw;
	private BigDecimal shul ;
	private String zhizlx ;
	private String xiaohsj;
	private String chaifsj ;
	private String xqly ;
	
	//第二部分：物流路径总表
	private String gongysbh;
	private String fenpbh ;
	private String lujbh;
	private String lujmc;
	private String fahd;
	private String zhidgys;
	private String jiaofm;
	private BigDecimal beihzq;
	private BigDecimal yunszq;
	private String gcbh;
	private String xiehztbh;
	private BigDecimal songhpc;
	private String mudd;
	private String dinghck;
	private String mos2;
	private BigDecimal cangkshpc2;
	private BigDecimal cangkshsj2;
	private BigDecimal cangkfhsj2;
	private BigDecimal beihsj2;
	private BigDecimal ibeihsj2;
	private BigDecimal pbeihsj2;
	private String xianbck;
	private String mos;
	private BigDecimal cangkshpc;
	private BigDecimal cangkshsj;
	private BigDecimal cangkfhsj;
	private BigDecimal beihsj;
	private BigDecimal ibeihsj;
	private BigDecimal pbeihsj;
	private String shengcxbh ;
	private BigDecimal beihsjc ;
	private BigDecimal gongysfe ;
	private String jianglms2 ;
	private String shengxsj2;
	private String jianglms;
	private String shengxsj;
	private String wjianglms;
	/**
	 * 供应商类型
	 */
	private String gongyslx;
	/**
	 * 订货库类型
	 */
	private String dinghcklx;

	//零件消耗点
	private BigDecimal anqkcs ;
	private BigDecimal xianbllkc ;
	private BigDecimal yifyhlzl ;
	private BigDecimal jiaofzl ;
	private BigDecimal zhongzzl ;
	private String xiaohcbh;
	private String xiaohccxbh;
	private String xianbyhlx;
	/**
	 * 零件消耗点系统调整值(年末已发总量-交付总量)
	 */
	private BigDecimal xhdxittzz;
	
	//零件仓库MD
	private String ckusbzlx ;
	private BigDecimal ckusbzrl ;
	private String ckuclx;
	private BigDecimal ckucrl;
	private String zickbh;
	/**
	 * 仓库交付总量
	 */
	private BigDecimal ckjiaofzl;
	/**
	 * 仓库终止总量
	 */
	private BigDecimal ckzhongzzl;
	/**
	 * 仓库已发要货总量
	 */
	private BigDecimal ckyifyhlzl;
	/**
	 * 零件仓库系统调整值(年末已发总量-交付总量)
	 */
	private BigDecimal ckxittzz;
	/**
	 * 零件仓库安全库存数量
	 */
	private BigDecimal anqkcsl;
	
	//零件供应商CD
	private String gysucbzlx;
	private BigDecimal gysucrl;
	private String gysuabzlx;
	private BigDecimal gysuaucgs;
	private BigDecimal zuixqdl;
	private String gongyhth;
	
	/**
	 * 中文名称
	 */
	private String zhongwmc;
	
	/**
	 * 计划员
	 */
	private String jihy;
	
	/**
	 * 零件属性
	 */
	private String lingjsx;

	public BigDecimal getGongysfe() {
		return gongysfe;
	}
	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}
	public String getXianbyhlx() {
		return xianbyhlx;
	}
	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}
	public String getXiaohccxbh() {
		return xiaohccxbh;
	}
	public void setXiaohccxbh(String xiaohccxbh) {
		this.xiaohccxbh = xiaohccxbh;
	}
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	public String getGongyhth() {
		return gongyhth;
	}
	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public String getFenpbh() {
		return fenpbh;
	}
	public void setFenpbh(String fenpbh) {
		this.fenpbh = fenpbh;
	}
	public String getJianglms2() {
		return jianglms2;
	}
	public void setJianglms2(String jianglms2) {
		this.jianglms2 = jianglms2;
	}
	public String getShengxsj2() {
		return shengxsj2;
	}
	public void setShengxsj2(String shengxsj2) {
		this.shengxsj2 = shengxsj2;
	}
	public String getJianglms() {
		return jianglms;
	}
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getCkusbzlx() {
		return ckusbzlx;
	}
	public void setCkusbzlx(String ckusbzlx) {
		this.ckusbzlx = ckusbzlx;
	}
	public BigDecimal getCkusbzrl() {
		return ckusbzrl;
	}
	public void setCkusbzrl(BigDecimal ckusbzrl) {
		this.ckusbzrl = ckusbzrl;
	}
	public String getCkuclx() {
		return ckuclx;
	}
	public void setCkuclx(String ckuclx) {
		this.ckuclx = ckuclx;
	}
	public BigDecimal getCkucrl() {
		return ckucrl;
	}
	public void setCkucrl(BigDecimal ckucrl) {
		this.ckucrl = ckucrl;
	}
	public String getGysucbzlx() {
		return gysucbzlx;
	}
	public void setGysucbzlx(String gysucbzlx) {
		this.gysucbzlx = gysucbzlx;
	}
	public BigDecimal getGysucrl() {
		return gysucrl;
	}
	public void setGysucrl(BigDecimal gysucrl) {
		this.gysucrl = gysucrl;
	}
	public String getGysuabzlx() {
		return gysuabzlx;
	}
	public void setGysuabzlx(String gysuabzlx) {
		this.gysuabzlx = gysuabzlx;
	}
	public BigDecimal getGysuaucgs() {
		return gysuaucgs;
	}
	public void setGysuaucgs(BigDecimal gysuaucgs) {
		this.gysuaucgs = gysuaucgs;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getLujbh() {
		return lujbh;
	}
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	public String getLujmc() {
		return lujmc;
	}
	public void setLujmc(String lujmc) {
		this.lujmc = lujmc;
	}
	public String getFahd() {
		return fahd;
	}
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	public String getZhidgys() {
		return zhidgys;
	}
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	public BigDecimal getYunszq() {
		return yunszq;
	}
	public void setYunszq(BigDecimal yunszq) {
		this.yunszq = yunszq;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public BigDecimal getSonghpc() {
		return songhpc;
	}
	public void setSonghpc(BigDecimal songhpc) {
		this.songhpc = songhpc;
	}
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	public String getDinghck() {
		return dinghck;
	}
	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}
	public String getMos2() {
		return mos2;
	}
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	public BigDecimal getCangkshpc2() {
		return cangkshpc2;
	}
	public void setCangkshpc2(BigDecimal cangkshpc2) {
		this.cangkshpc2 = cangkshpc2;
	}
	public BigDecimal getCangkshsj2() {
		return cangkshsj2;
	}
	public void setCangkshsj2(BigDecimal cangkshsj2) {
		this.cangkshsj2 = cangkshsj2;
	}
	public BigDecimal getCangkfhsj2() {
		return cangkfhsj2;
	}
	public void setCangkfhsj2(BigDecimal cangkfhsj2) {
		this.cangkfhsj2 = cangkfhsj2;
	}
	public BigDecimal getBeihsj2() {
		return beihsj2;
	}
	public void setBeihsj2(BigDecimal beihsj2) {
		this.beihsj2 = beihsj2;
	}
	public BigDecimal getIbeihsj2() {
		return ibeihsj2;
	}
	public void setIbeihsj2(BigDecimal ibeihsj2) {
		this.ibeihsj2 = ibeihsj2;
	}
	public BigDecimal getPbeihsj2() {
		return pbeihsj2;
	}
	public void setPbeihsj2(BigDecimal pbeihsj2) {
		this.pbeihsj2 = pbeihsj2;
	}
	public String getXianbck() {
		return xianbck;
	}
	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public BigDecimal getCangkshpc() {
		return cangkshpc;
	}
	public void setCangkshpc(BigDecimal cangkshpc) {
		this.cangkshpc = cangkshpc;
	}
	public BigDecimal getCangkshsj() {
		return cangkshsj;
	}
	public void setCangkshsj(BigDecimal cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	public BigDecimal getCangkfhsj() {
		return cangkfhsj;
	}
	public void setCangkfhsj(BigDecimal cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	public BigDecimal getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(BigDecimal beihsj) {
		this.beihsj = beihsj;
	}
	public BigDecimal getIbeihsj() {
		return ibeihsj;
	}
	public void setIbeihsj(BigDecimal ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	public BigDecimal getPbeihsj() {
		return pbeihsj;
	}
	public void setPbeihsj(BigDecimal pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public BigDecimal getBeihsjc() {
		return beihsjc;
	}
	public void setBeihsjc(BigDecimal beihsjc) {
		this.beihsjc = beihsjc;
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
	public BigDecimal getZhongzzl() {
		return zhongzzl;
	}
	public void setZhongzzl(BigDecimal zhongzzl) {
		this.zhongzzl = zhongzzl;
	}
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
	public String getXianh() {
		return xianh;
	}
	public void setXianh(String xianh) {
		this.xianh = xianh;
	}
	public String getLiush() {
		return liush;
	}
	public void setLiush(String liush) {
		this.liush = liush;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public BigDecimal getShul() {
		return shul;
	}
	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getXiaohsj() {
		return xiaohsj;
	}
	public void setXiaohsj(String xiaohsj) {
		this.xiaohsj = xiaohsj;
	}
	public String getChaifsj() {
		return chaifsj;
	}
	public void setChaifsj(String chaifsj) {
		this.chaifsj = chaifsj;
	}
	public String getXqly() {
		return xqly;
	}
	public void setXqly(String xqly) {
		this.xqly = xqly;
	}
	public String getGongyslx() {
		return gongyslx;
	}
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}
	public String getDinghcklx() {
		return dinghcklx;
	}
	public void setDinghcklx(String dinghcklx) {
		this.dinghcklx = dinghcklx;
	}
	public String getZickbh() {
		return zickbh;
	}
	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}
	public BigDecimal getAnqkcsl() {
		return anqkcsl;
	}
	public void setAnqkcsl(BigDecimal anqkcsl) {
		this.anqkcsl = anqkcsl;
	}
	public String getZhongwmc() {
		return zhongwmc;
	}
	public void setZhongwmc(String zhongwmc) {
		this.zhongwmc = zhongwmc;
	}
	public String getJihy() {
		return jihy;
	}
	public void setJihy(String jihy) {
		this.jihy = jihy;
	}
	public String getLingjsx() {
		return lingjsx;
	}
	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}
	public BigDecimal getXhdxittzz() {
		return xhdxittzz;
	}
	public void setXhdxittzz(BigDecimal xhdxittzz) {
		this.xhdxittzz = xhdxittzz;
	}
	public BigDecimal getCkxittzz() {
		return ckxittzz;
	}
	public void setCkxittzz(BigDecimal ckxittzz) {
		this.ckxittzz = ckxittzz;
	}
	public BigDecimal getCkjiaofzl() {
		return ckjiaofzl;
	}
	public void setCkjiaofzl(BigDecimal ckjiaofzl) {
		this.ckjiaofzl = ckjiaofzl;
	}
	public BigDecimal getCkzhongzzl() {
		return ckzhongzzl;
	}
	public void setCkzhongzzl(BigDecimal ckzhongzzl) {
		this.ckzhongzzl = ckzhongzzl;
	}
	public BigDecimal getCkyifyhlzl() {
		return ckyifyhlzl;
	}
	public void setCkyifyhlzl(BigDecimal ckyifyhlzl) {
		this.ckyifyhlzl = ckyifyhlzl;
	}
	public String getWjianglms() {
		return wjianglms;
	}
	public void setWjianglms(String wjianglms) {
		this.wjianglms = wjianglms;
	}
	
}

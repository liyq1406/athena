package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * @按需初始化布线中间表bean
 * @author 李智
 * @date 2012-3-27
 */
public class Anxcshbxzjb extends PageableSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2879644765933279371L;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 零件编号
	 */
	private String lingjbh;
	/**
	 * 发货地
	 */
	private String fahd;
	/**
	 * 目的地
	 */
	private String mudd;
	/**
	 * 消耗点
	 */
	private String xiaohd;
	/**
	 * 流水号
	 */
	private String liush;
	/**
	 * 制造路线
	 */
	private String zhizlx;
	/**
	 * 数量
	 */
	private BigDecimal shul;
	private String gongysbh;
	private String lujbh;
	private String lujmc;
	private String waibms;
	private String zhidgys;
	private String jiaofm;
	private BigDecimal beihzq;
	private BigDecimal yunszq;
	private String gcbh;
	private String xiehztbh;
	private BigDecimal songhpc;
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
	private String shengcxbh;
	private BigDecimal beihsjc;
	private BigDecimal anqkcs;
	private BigDecimal xianbllkc;
	private BigDecimal yifyhlzl;
	private BigDecimal jiaofzl;
	private BigDecimal zhongzzl;
	private String chej;
	private String xianh;
	private String danw;
	private String xiaohsj;
	private String chaifsj;
	private String xqly;
	private String ckusbzlx;
	private BigDecimal ckusbzrl;
	private String ckuclx;
	private BigDecimal ckucrl;
	private String gysucbzlx;
	private BigDecimal gysucrl;
	private String gysuabzlx;
	private BigDecimal gysuaucgs;
	private String jianglms2;
	private String shengxsj2;
	private String jianglms;
	private String shengxsj;
	private String gongyhth;
	private String xianbyhlx;
	private String xiaohcbh;
	private String xiaohccxbh;
	private String fenpqbh;
	private BigDecimal gongyfe;
	private BigDecimal zuixqdl;
	private String zickbh;
	/**
	 * 供应商类型
	 */
	private String gongyslx;
	/**
	 * 订货库类型
	 */
	private String dinghcklx;
	
	/**
	 * 安全库存数量
	 */
	private BigDecimal anqkcsl;
	/**
	 * @return the zuixqdl
	 */
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}
	/**
	 * @param zuixqdl the zuixqdl to set
	 */
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	/**
	 * @return the gongyfe
	 */
	public BigDecimal getGongyfe() {
		return gongyfe;
	}
	/**
	 * @param gongyfe the gongyfe to set
	 */
	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}
	/**
	 * @return the fenpqbh
	 */
	public String getFenpqbh() {
		return fenpqbh;
	}
	/**
	 * @param fenpqbh the fenpqbh to set
	 */
	public void setFenpqbh(String fenpqbh) {
		this.fenpqbh = fenpqbh;
	}
	/**
	 * @return the xianbyhlx
	 */
	public String getXianbyhlx() {
		return xianbyhlx;
	}
	/**
	 * @param xianbyhlx the xianbyhlx to set
	 */
	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}
	/**
	 * @return the xiaohcbh
	 */
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	/**
	 * @param xiaohcbh the xiaohcbh to set
	 */
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	/**
	 * @return the xiaohccxbh
	 */
	public String getXiaohccxbh() {
		return xiaohccxbh;
	}
	/**
	 * @param xiaohccxbh the xiaohccxbh to set
	 */
	public void setXiaohccxbh(String xiaohccxbh) {
		this.xiaohccxbh = xiaohccxbh;
	}
	/**
	 * @return the ckusbzlx
	 */
	public String getCkusbzlx() {
		return ckusbzlx;
	}
	/**
	 * @param ckusbzlx the ckusbzlx to set
	 */
	public void setCkusbzlx(String ckusbzlx) {
		this.ckusbzlx = ckusbzlx;
	}
	/**
	 * @return the ckusbzrl
	 */
	public BigDecimal getCkusbzrl() {
		return ckusbzrl;
	}
	/**
	 * @param ckusbzrl the ckusbzrl to set
	 */
	public void setCkusbzrl(BigDecimal ckusbzrl) {
		this.ckusbzrl = ckusbzrl;
	}
	/**
	 * @return the ckuclx
	 */
	public String getCkuclx() {
		return ckuclx;
	}
	/**
	 * @param ckuclx the ckuclx to set
	 */
	public void setCkuclx(String ckuclx) {
		this.ckuclx = ckuclx;
	}
	/**
	 * @return the ckucrl
	 */
	public BigDecimal getCkucrl() {
		return ckucrl;
	}
	/**
	 * @param ckucrl the ckucrl to set
	 */
	public void setCkucrl(BigDecimal ckucrl) {
		this.ckucrl = ckucrl;
	}
	/**
	 * @return the gysucbzlx
	 */
	public String getGysucbzlx() {
		return gysucbzlx;
	}
	/**
	 * @param gysucbzlx the gysucbzlx to set
	 */
	public void setGysucbzlx(String gysucbzlx) {
		this.gysucbzlx = gysucbzlx;
	}
	/**
	 * @return the gysucrl
	 */
	public BigDecimal getGysucrl() {
		return gysucrl;
	}
	/**
	 * @param gysucrl the gysucrl to set
	 */
	public void setGysucrl(BigDecimal gysucrl) {
		this.gysucrl = gysucrl;
	}
	/**
	 * @return the gysuabzlx
	 */
	public String getGysuabzlx() {
		return gysuabzlx;
	}
	/**
	 * @param gysuabzlx the gysuabzlx to set
	 */
	public void setGysuabzlx(String gysuabzlx) {
		this.gysuabzlx = gysuabzlx;
	}
	/**
	 * @return the gysuaucgs
	 */
	public BigDecimal getGysuaucgs() {
		return gysuaucgs;
	}
	/**
	 * @param gysuaucgs the gysuaucgs to set
	 */
	public void setGysuaucgs(BigDecimal gysuaucgs) {
		this.gysuaucgs = gysuaucgs;
	}
	/**
	 * @return the jianglms2
	 */
	public String getJianglms2() {
		return jianglms2;
	}
	/**
	 * @param jianglms2 the jianglms2 to set
	 */
	public void setJianglms2(String jianglms2) {
		this.jianglms2 = jianglms2;
	}
	/**
	 * @return the shengxsj2
	 */
	public String getShengxsj2() {
		return shengxsj2;
	}
	/**
	 * @param shengxsj2 the shengxsj2 to set
	 */
	public void setShengxsj2(String shengxsj2) {
		this.shengxsj2 = shengxsj2;
	}
	/**
	 * @return the jianglms
	 */
	public String getJianglms() {
		return jianglms;
	}
	/**
	 * @param jianglms the jianglms to set
	 */
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	/**
	 * @return the shengxsj
	 */
	public String getShengxsj() {
		return shengxsj;
	}
	/**
	 * @param shengxsj the shengxsj to set
	 */
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	/**
	 * @return the gongyhth
	 */
	public String getGongyhth() {
		return gongyhth;
	}
	/**
	 * @param gongyhth the gongyhth to set
	 */
	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}
	/**
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
	 * @return the lingjbh
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	/**
	 * @param lingjbh the lingjbh to set
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	/**
	 * @return the fahd
	 */
	public String getFahd() {
		return fahd;
	}
	/**
	 * @param fahd the fahd to set
	 */
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	/**
	 * @return the mudd
	 */
	public String getMudd() {
		return mudd;
	}
	/**
	 * @param mudd the mudd to set
	 */
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	/**
	 * @return the xiaohd
	 */
	public String getXiaohd() {
		return xiaohd;
	}
	/**
	 * @param xiaohd the xiaohd to set
	 */
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	/**
	 * @return the liush
	 */
	public String getLiush() {
		return liush;
	}
	/**
	 * @param liush the liush to set
	 */
	public void setLiush(String liush) {
		this.liush = liush;
	}
	/**
	 * @return the zhizlx
	 */
	public String getZhizlx() {
		return zhizlx;
	}
	/**
	 * @param zhizlx the zhizlx to set
	 */
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	/**
	 * @return the shul
	 */
	public BigDecimal getShul() {
		return shul;
	}
	/**
	 * @param shul the shul to set
	 */
	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	/**
	 * @return the gongysbh
	 */
	public String getGongysbh() {
		return gongysbh;
	}
	/**
	 * @param gongysbh the gongysbh to set
	 */
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	/**
	 * @return the lujbh
	 */
	public String getLujbh() {
		return lujbh;
	}
	/**
	 * @param lujbh the lujbh to set
	 */
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	/**
	 * @return the lujmc
	 */
	public String getLujmc() {
		return lujmc;
	}
	/**
	 * @param lujmc the lujmc to set
	 */
	public void setLujmc(String lujmc) {
		this.lujmc = lujmc;
	}
	/**
	 * @return the waibms
	 */
	public String getWaibms() {
		return waibms;
	}
	/**
	 * @param waibms the waibms to set
	 */
	public void setWaibms(String waibms) {
		this.waibms = waibms;
	}
	/**
	 * @return the zhidgys
	 */
	public String getZhidgys() {
		return zhidgys;
	}
	/**
	 * @param zhidgys the zhidgys to set
	 */
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	/**
	 * @return the jiaofm
	 */
	public String getJiaofm() {
		return jiaofm;
	}
	/**
	 * @param jiaofm the jiaofm to set
	 */
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	/**
	 * @return the beihzq
	 */
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	/**
	 * @param beihzq the beihzq to set
	 */
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	/**
	 * @return the yunszq
	 */
	public BigDecimal getYunszq() {
		return yunszq;
	}
	/**
	 * @param yunszq the yunszq to set
	 */
	public void setYunszq(BigDecimal yunszq) {
		this.yunszq = yunszq;
	}
	/**
	 * @return the gcbh
	 */
	public String getGcbh() {
		return gcbh;
	}
	/**
	 * @param gcbh the gcbh to set
	 */
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	/**
	 * @return the xiehztbh
	 */
	public String getXiehztbh() {
		return xiehztbh;
	}
	/**
	 * @param xiehztbh the xiehztbh to set
	 */
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	/**
	 * @return the songhpc
	 */
	public BigDecimal getSonghpc() {
		return songhpc;
	}
	/**
	 * @param songhpc the songhpc to set
	 */
	public void setSonghpc(BigDecimal songhpc) {
		this.songhpc = songhpc;
	}
	
	/**
	 * @return the dinghck
	 */
	public String getDinghck() {
		return dinghck;
	}
	/**
	 * @param dinghck the dinghck to set
	 */
	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}
	/**
	 * @return the mos2
	 */
	public String getMos2() {
		return mos2;
	}
	/**
	 * @param mos2 the mos2 to set
	 */
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	/**
	 * @return the cangkshpc2
	 */
	public BigDecimal getCangkshpc2() {
		return cangkshpc2;
	}
	/**
	 * @param cangkshpc2 the cangkshpc2 to set
	 */
	public void setCangkshpc2(BigDecimal cangkshpc2) {
		this.cangkshpc2 = cangkshpc2;
	}
	/**
	 * @return the cangkshsj2
	 */
	public BigDecimal getCangkshsj2() {
		return cangkshsj2;
	}
	/**
	 * @param cangkshsj2 the cangkshsj2 to set
	 */
	public void setCangkshsj2(BigDecimal cangkshsj2) {
		this.cangkshsj2 = cangkshsj2;
	}
	/**
	 * @return the cangkfhsj2
	 */
	public BigDecimal getCangkfhsj2() {
		return cangkfhsj2;
	}
	/**
	 * @param cangkfhsj2 the cangkfhsj2 to set
	 */
	public void setCangkfhsj2(BigDecimal cangkfhsj2) {
		this.cangkfhsj2 = cangkfhsj2;
	}
	/**
	 * @return the beihsj2
	 */
	public BigDecimal getBeihsj2() {
		return beihsj2;
	}
	/**
	 * @param beihsj2 the beihsj2 to set
	 */
	public void setBeihsj2(BigDecimal beihsj2) {
		this.beihsj2 = beihsj2;
	}
	/**
	 * @return the ibeihsj2
	 */
	public BigDecimal getIbeihsj2() {
		return ibeihsj2;
	}
	/**
	 * @param ibeihsj2 the ibeihsj2 to set
	 */
	public void setIbeihsj2(BigDecimal ibeihsj2) {
		this.ibeihsj2 = ibeihsj2;
	}
	/**
	 * @return the pbeihsj2
	 */
	public BigDecimal getPbeihsj2() {
		return pbeihsj2;
	}
	/**
	 * @param pbeihsj2 the pbeihsj2 to set
	 */
	public void setPbeihsj2(BigDecimal pbeihsj2) {
		this.pbeihsj2 = pbeihsj2;
	}
	/**
	 * @return the xianbck
	 */
	public String getXianbck() {
		return xianbck;
	}
	/**
	 * @param xianbck the xianbck to set
	 */
	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}
	/**
	 * @return the mos
	 */
	public String getMos() {
		return mos;
	}
	/**
	 * @param mos the mos to set
	 */
	public void setMos(String mos) {
		this.mos = mos;
	}
	/**
	 * @return the cangkshpc
	 */
	public BigDecimal getCangkshpc() {
		return cangkshpc;
	}
	/**
	 * @param cangkshpc the cangkshpc to set
	 */
	public void setCangkshpc(BigDecimal cangkshpc) {
		this.cangkshpc = cangkshpc;
	}
	/**
	 * @return the cangkshsj
	 */
	public BigDecimal getCangkshsj() {
		return cangkshsj;
	}
	/**
	 * @param cangkshsj the cangkshsj to set
	 */
	public void setCangkshsj(BigDecimal cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	/**
	 * @return the cangkfhsj
	 */
	public BigDecimal getCangkfhsj() {
		return cangkfhsj;
	}
	/**
	 * @param cangkfhsj the cangkfhsj to set
	 */
	public void setCangkfhsj(BigDecimal cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	/**
	 * @return the beihsj
	 */
	public BigDecimal getBeihsj() {
		return beihsj;
	}
	/**
	 * @param beihsj the beihsj to set
	 */
	public void setBeihsj(BigDecimal beihsj) {
		this.beihsj = beihsj;
	}
	/**
	 * @return the ibeihsj
	 */
	public BigDecimal getIbeihsj() {
		return ibeihsj;
	}
	/**
	 * @param ibeihsj the ibeihsj to set
	 */
	public void setIbeihsj(BigDecimal ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	/**
	 * @return the pbeihsj
	 */
	public BigDecimal getPbeihsj() {
		return pbeihsj;
	}
	/**
	 * @param pbeihsj the pbeihsj to set
	 */
	public void setPbeihsj(BigDecimal pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	/**
	 * @return the shengcxbh
	 */
	public String getShengcxbh() {
		return shengcxbh;
	}
	/**
	 * @param shengcxbh the shengcxbh to set
	 */
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	/**
	 * @return the beihsjc
	 */
	public BigDecimal getBeihsjc() {
		return beihsjc;
	}
	/**
	 * @param beihsjc the beihsjc to set
	 */
	public void setBeihsjc(BigDecimal beihsjc) {
		this.beihsjc = beihsjc;
	}
	/**
	 * @return the anqkcs
	 */
	public BigDecimal getAnqkcs() {
		return anqkcs;
	}
	/**
	 * @param anqkcs the anqkcs to set
	 */
	public void setAnqkcs(BigDecimal anqkcs) {
		this.anqkcs = anqkcs;
	}
	/**
	 * @return the xianbllkc
	 */
	public BigDecimal getXianbllkc() {
		return xianbllkc;
	}
	/**
	 * @param xianbllkc the xianbllkc to set
	 */
	public void setXianbllkc(BigDecimal xianbllkc) {
		this.xianbllkc = xianbllkc;
	}
	/**
	 * @return the yifyhlzl
	 */
	public BigDecimal getYifyhlzl() {
		return yifyhlzl;
	}
	/**
	 * @param yifyhlzl the yifyhlzl to set
	 */
	public void setYifyhlzl(BigDecimal yifyhlzl) {
		this.yifyhlzl = yifyhlzl;
	}
	/**
	 * @return the jiaofzl
	 */
	public BigDecimal getJiaofzl() {
		return jiaofzl;
	}
	/**
	 * @param jiaofzl the jiaofzl to set
	 */
	public void setJiaofzl(BigDecimal jiaofzl) {
		this.jiaofzl = jiaofzl;
	}
	/**
	 * @return the zhongzzl
	 */
	public BigDecimal getZhongzzl() {
		return zhongzzl;
	}
	/**
	 * @param zhongzzl the zhongzzl to set
	 */
	public void setZhongzzl(BigDecimal zhongzzl) {
		this.zhongzzl = zhongzzl;
	}
	/**
	 * @return the chej
	 */
	public String getChej() {
		return chej;
	}
	/**
	 * @param chej the chej to set
	 */
	public void setChej(String chej) {
		this.chej = chej;
	}
	/**
	 * @return the xianh
	 */
	public String getXianh() {
		return xianh;
	}
	/**
	 * @param xianh the xianh to set
	 */
	public void setXianh(String xianh) {
		this.xianh = xianh;
	}
	/**
	 * @return the danw
	 */
	public String getDanw() {
		return danw;
	}
	/**
	 * @param danw the danw to set
	 */
	public void setDanw(String danw) {
		this.danw = danw;
	}
	/**
	 * @return the xiaohsj
	 */
	public String getXiaohsj() {
		return xiaohsj;
	}
	/**
	 * @param xiaohsj the xiaohsj to set
	 */
	public void setXiaohsj(String xiaohsj) {
		this.xiaohsj = xiaohsj;
	}
	/**
	 * @return the chaifsj
	 */
	public String getChaifsj() {
		return chaifsj;
	}
	/**
	 * @param chaifsj the chaifsj to set
	 */
	public void setChaifsj(String chaifsj) {
		this.chaifsj = chaifsj;
	}
	/**
	 * @return the xqly
	 */
	public String getXqly() {
		return xqly;
	}
	/**
	 * @param xqly the xqly to set
	 */
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
	
	
}

package com.athena.xqjs.entity.kdorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 实体: KD毛需求_汇总_参考系
 * 
 * @author 袁修瑞
 * @version V1.0
 * @Date：2012-12-11
 */
@SuppressWarnings("serial")
public class Kdmxqhzc extends PageableSupport {

	private String guanjljjb;// 制造路线
	private String zhizlx;// 制造路线
	private BigDecimal beihzq;// 备货周期
	private String fahd;// 发货地
	private String dinghck;// 订货仓库
	private BigDecimal dinghaqkc;// 订货安全库存
	private String dinghcj;// 订货车间
	private BigDecimal kuc;// 库存
	private BigDecimal uabzucsl;// UA包装内UC数量
	private String lujdm;// 路径代码
	private String danw;// 单位

	private BigDecimal fayzq;// 发运周期
	private BigDecimal anqkc;// 安全库存

	private String jihyz;// 计划员组
	private String gongysdm;// 供应商代码
	private BigDecimal uabzucrl;// UA包装内UC容量
	private BigDecimal dingdlj;// 订单累计
	private String uabzuclx;// UA包装内UC类型
	private BigDecimal gongysfe;// 供应商份额
	private String ziyhqrq;// 资源获取日期
	private BigDecimal jiaoflj;// 交付累计
	private BigDecimal dingdzzlj;// 订单终止累计
	private String uabzlx;// UA包装类型
	private String id;//
	private String lingjbh;//
	private String usercenter;// 用户中心
	private String waibghms;// 外部供货模式
	private String dingdnr;// 订单内容
	private String gongyslx;// 供应商类型

	private String s0sszxh;// S0所属周序号
	private BigDecimal s0;// S0
	private BigDecimal s1;// S1
	private BigDecimal s2;// S2
	private BigDecimal s3;// S3
	private BigDecimal s4;// S4
	private BigDecimal s5;// S5
	private BigDecimal s6;// S6
	private BigDecimal s7;// S7
	private BigDecimal s8;// S8
	private BigDecimal s9;// S9
	private BigDecimal s10;// S10
	private BigDecimal s11;// S11
	private BigDecimal s12;// S12
	private BigDecimal s13;// S13
	private BigDecimal s14;// S14
	private BigDecimal s15;// S15
	private BigDecimal s16;// S16
	private BigDecimal s17;// S17
	private BigDecimal s18;// S18
	private BigDecimal s19;// S19
	private BigDecimal s20;// S20
	private BigDecimal s21;// S21
	private BigDecimal s22;// S22
	private BigDecimal s23;// S23
	private BigDecimal xittzz;// 系统调整值
	private BigDecimal jstzsz;// 计算调整值
	private BigDecimal daixh;// 待消耗
	private String zhidgys;// 指定供应商
	private String cangklx;// 仓库类型
	private String lingjmc;
	private String gongsmc;
	private String neibyhzx;
	private String xiehzt;
	private String wulgyyz1;
	private String wulgyyz;
	private String gcbh;
	
	private BigDecimal uarl;

	public String getGuanjljjb() {
		return guanjljjb;
	}

	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public BigDecimal getBeihzq() {
		return beihzq;
	}

	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}

	public String getFahd() {
		return fahd;
	}

	public void setFahd(String fahd) {
		this.fahd = fahd;
	}

	public String getDinghck() {
		return dinghck;
	}

	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}

	public BigDecimal getDinghaqkc() {
		return dinghaqkc;
	}

	public void setDinghaqkc(BigDecimal dinghaqkc) {
		this.dinghaqkc = dinghaqkc;
	}

	public String getDinghcj() {
		return dinghcj;
	}

	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}

	public BigDecimal getKuc() {
		return kuc;
	}

	public void setKuc(BigDecimal kuc) {
		this.kuc = kuc;
	}

	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}

	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}

	public String getLujdm() {
		return lujdm;
	}

	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public BigDecimal getFayzq() {
		return fayzq;
	}

	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}

	public BigDecimal getAnqkc() {
		return anqkc;
	}

	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}

	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}

	public BigDecimal getDingdlj() {
		return dingdlj;
	}

	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}

	public String getUabzuclx() {
		return uabzuclx;
	}

	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}

	public BigDecimal getGongysfe() {
		return gongysfe;
	}

	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}

	public String getZiyhqrq() {
		return ziyhqrq;
	}

	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}

	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}

	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}

	public String getUabzlx() {
		return uabzlx;
	}

	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getWaibghms() {
		return waibghms;
	}

	public void setWaibghms(String waibghms) {
		this.waibghms = waibghms;
	}

	public String getDingdnr() {
		return dingdnr;
	}

	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}

	public String getGongyslx() {
		return gongyslx;
	}

	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}

	public String getS0sszxh() {
		return s0sszxh;
	}

	public void setS0sszxh(String s0sszxh) {
		this.s0sszxh = s0sszxh;
	}

	public BigDecimal getS0() {
		return s0;
	}

	public void setS0(BigDecimal s0) {
		this.s0 = s0;
	}

	public BigDecimal getS1() {
		return s1;
	}

	public void setS1(BigDecimal s1) {
		this.s1 = s1;
	}

	public BigDecimal getS2() {
		return s2;
	}

	public void setS2(BigDecimal s2) {
		this.s2 = s2;
	}

	public BigDecimal getS3() {
		return s3;
	}

	public void setS3(BigDecimal s3) {
		this.s3 = s3;
	}

	public BigDecimal getS4() {
		return s4;
	}

	public void setS4(BigDecimal s4) {
		this.s4 = s4;
	}

	public BigDecimal getS5() {
		return s5;
	}

	public void setS5(BigDecimal s5) {
		this.s5 = s5;
	}

	public BigDecimal getS6() {
		return s6;
	}

	public void setS6(BigDecimal s6) {
		this.s6 = s6;
	}

	public BigDecimal getS7() {
		return s7;
	}

	public void setS7(BigDecimal s7) {
		this.s7 = s7;
	}

	public BigDecimal getS8() {
		return s8;
	}

	public void setS8(BigDecimal s8) {
		this.s8 = s8;
	}

	public BigDecimal getS9() {
		return s9;
	}

	public void setS9(BigDecimal s9) {
		this.s9 = s9;
	}

	public BigDecimal getS10() {
		return s10;
	}

	public void setS10(BigDecimal s10) {
		this.s10 = s10;
	}

	public BigDecimal getS11() {
		return s11;
	}

	public void setS11(BigDecimal s11) {
		this.s11 = s11;
	}

	public BigDecimal getS12() {
		return s12;
	}

	public void setS12(BigDecimal s12) {
		this.s12 = s12;
	}

	public BigDecimal getS13() {
		return s13;
	}

	public void setS13(BigDecimal s13) {
		this.s13 = s13;
	}

	public BigDecimal getS14() {
		return s14;
	}

	public void setS14(BigDecimal s14) {
		this.s14 = s14;
	}

	public BigDecimal getS15() {
		return s15;
	}

	public void setS15(BigDecimal s15) {
		this.s15 = s15;
	}

	public BigDecimal getS16() {
		return s16;
	}

	public void setS16(BigDecimal s16) {
		this.s16 = s16;
	}

	public BigDecimal getS17() {
		return s17;
	}

	public void setS17(BigDecimal s17) {
		this.s17 = s17;
	}

	public BigDecimal getS18() {
		return s18;
	}

	public void setS18(BigDecimal s18) {
		this.s18 = s18;
	}

	public BigDecimal getS19() {
		return s19;
	}

	public void setS19(BigDecimal s19) {
		this.s19 = s19;
	}

	public BigDecimal getS20() {
		return s20;
	}

	public void setS20(BigDecimal s20) {
		this.s20 = s20;
	}

	public BigDecimal getS21() {
		return s21;
	}

	public void setS21(BigDecimal s21) {
		this.s21 = s21;
	}

	public BigDecimal getS22() {
		return s22;
	}

	public void setS22(BigDecimal s22) {
		this.s22 = s22;
	}

	public BigDecimal getS23() {
		return s23;
	}

	public void setS23(BigDecimal s23) {
		this.s23 = s23;
	}

	public BigDecimal getXittzz() {
		return xittzz;
	}

	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}

	public BigDecimal getDaixh() {
		return daixh;
	}

	public void setDaixh(BigDecimal daixh) {
		this.daixh = daixh;
	}

	public String getZhidgys() {
		return zhidgys;
	}

	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getNeibyhzx() {
		return neibyhzx;
	}

	public void setNeibyhzx(String neibyhzx) {
		this.neibyhzx = neibyhzx;
	}

	public String getXiehzt() {
		return xiehzt;
	}

	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}

	public String getWulgyyz1() {
		return wulgyyz1;
	}

	public void setWulgyyz1(String wulgyyz1) {
		this.wulgyyz1 = wulgyyz1;
	}

	public String getWulgyyz() {
		return wulgyyz;
	}

	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getGcbh() {
		return gcbh;
	}

	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}

	public BigDecimal getDingdzzlj() {
		return dingdzzlj;
	}

	public void setDingdzzlj(BigDecimal dingdzzlj) {
		this.dingdzzlj = dingdzzlj;
	}

	public BigDecimal getJstzsz() {
		return jstzsz;
	}

	public void setJstzsz(BigDecimal jstzsz) {
		this.jstzsz = jstzsz;
	}

	public BigDecimal getUarl() {
		return uarl;
	}

	public void setUarl(BigDecimal uarl) {
		this.uarl = uarl;
	}
	

}
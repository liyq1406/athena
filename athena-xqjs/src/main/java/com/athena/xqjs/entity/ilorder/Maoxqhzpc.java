package com.athena.xqjs.entity.ilorder;


import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 毛需求_汇总_P_参考系
 * @author 李明
 * @version
 * 2011-12-01
 */
public class Maoxqhzpc extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 零件属性
	 */
	private String lingjsx;
	/**
	 * 发货地
	 */
	private String fahd;
	/**
	 * 供应商合同号
	 */
	private String gongyhth;
	/**
	 * 使用车间
	 */
	private String shiycj;
	/**
	 * 订单终止累计
	 */
	private BigDecimal dingdzzlj;
	/**
	 * 供应商类型
	 */
	private String gongyslx;
	
	/**
	 * 指定供应商
	 */
	private String zhidgys;
	/**
	 * ID
	 */
	private String id;
	/**
	 * P8
	 */
	private BigDecimal p8;
	/**
	 * 订单累计
	 */
	private BigDecimal dingdlj;
	/**
	 * UA包装类型
	 */
	private String uabzlx;
	/**
	 * 制造路线
	 */
	private String zhizlx;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 需求拆分日期
	 */
	private String xuqcfrq;
	/**
	 * P9
	 */
	private BigDecimal p9;
	/**
	 * 待消耗
	 */
	private BigDecimal daixh;
	/**
	 * 包装容量
	 */
	private BigDecimal baozrl;
	/**
	 * UA包装内UC类型
	 */
	private String uabzuclx;
	/**
	 * 资源获取日期
	 */
	private String ziyhqrq;
	/**
	 * P10
	 */
	private BigDecimal p10;
	/**
	 * 供应商份额
	 */
	private BigDecimal gongysfe;
	/**
	 * 系统调整值
	 */
	private BigDecimal xittzz;
	/**
	 * P6
	 */
	private BigDecimal p6;
	/**
	 * P2
	 */
	private BigDecimal p2;
	/**
	 * P0周期序号
	 */
	private String p0zqxh;
	/**
	 * P4
	 */
	private BigDecimal p4;
	/**
	 * 订货仓库（线边仓库）
	 */
	private String cangkdm;
	/**
	 * 库存
	 */
	private BigDecimal kuc;
	/**
	 * 外部供货模式
	 */
	private String waibghms;
	/**
	 * 是否依赖库存
	 */
	private String shifylkc;
	/**
	 * P7
	 */
	private BigDecimal p7;
	/**
	 * P11
	 */
	private BigDecimal p11;
	/**
	 * 安全库存
	 */
	private BigDecimal anqkc;
	/**
	 * 路径代码
	 */
	private String lujdm;
	/**
	 * 调整计算值
	 */
	private BigDecimal tiaozjsz;
	/**
	 * 交付累计
	 */
	private BigDecimal jiaoflj;
	/**
	 * 是否依赖待消耗
	 */
	private String shifyldxh;
	/**
	 * P0
	 */
	private BigDecimal p0;
	/**
	 * 是否依赖待交付
	 */
	private String shifyldjf;
	/**
	 * 订货车间
	 */
	private String dinghcj;
	/**
	 * P1
	 */
	private BigDecimal p1;
	/**
	 * 计划员组
	 */
	private String jihydz;
	/**
	 * 订单内容
	 */
	private String dingdnr;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * P3
	 */
	private BigDecimal p3;
	/**
	 * 是否依赖安全库存
	 */
	private String shifylaqkc;
	/**
	 * 发运周期
	 */
	private BigDecimal fayzq;
	/**
	 * 预告是否取整
	 */
	private String yugsfqz;
	/**
	 * P5
	 */
	private BigDecimal p5;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 备货周期
	 */
	private BigDecimal beihzq;
	/**
	 * UA包装内UC容量
	 */
	private BigDecimal uabzucrl;
	/**
	 * UA包装内UC数量
	 */
	private BigDecimal uabzucsl;
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	
	/**
	 * 最小起订量
	 */
	private BigDecimal zuixqdl;
	
	/**
	 * 交付码
	 */
	private String jiaofm;
	
	
	private String gongyshth ;
	
	
	private String zhidgsy ;
	
	private String cangklx;
	
	private String lingjmc;
	
	private String gongsmc;
	
	private String neibyhzx;
	
	private String wulgyyz;
	
	private String wulgyyz1;
	
	private String xiehztbh;
	
	private String gcbh;
	

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

	public String getGongyshth() {
		return gongyshth;
	}

	public void setGongyshth(String gongyshth) {
		this.gongyshth = gongyshth;
	}


	public String getZhidgsy() {
		return zhidgsy;
	}

	public void setZhidgsy(String zhidgsy) {
		this.zhidgsy = zhidgsy;
	}

	public String getJiaofm() {
		return jiaofm;
	}

	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}

	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}

	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}

	/**
	 * 取得 - 指定供应商
	 *
	 * @return the 指定供应商
	 */
	public String getZhidgys() {
		return zhidgys;
	}

	/**
	 * 设置 - 指定供应商
	 *
	 * @param zhidgys the new 指定供应商
	 */
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}

	/**
	 * 取得 - 供应商类型.
	 *
	 * @return the 供应商类型
	 */
	public String getGongyslx() {
		return gongyslx;
	}
	
	/**
	 * 设置 - 供应商类型.
	 *
	 * @param gongyslx the new 供应商类型
	 */
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}
	
	/**
	 * 取得 - iD.
	 *
	 * @return the iD
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置 - iD.
	 *
	 * @param id the new iD
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 取得 - p8.
	 *
	 * @return the p8
	 */
	public BigDecimal getP8() {
		return p8;
	}
	
	/**
	 * 设置 - p8.
	 *
	 * @param p8 the new p8
	 */
	public void setP8(BigDecimal p8) {
		this.p8 = p8;
	}
	
	/**
	 * 取得 - 订单累计.
	 *
	 * @return the 订单累计
	 */
	public BigDecimal getDingdlj() {
		return dingdlj;
	}
	
	/**
	 * 设置 - 订单累计.
	 *
	 * @param dingdlj the new 订单累计
	 */
	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}
	
	/**
	 * 取得 - uA包装类型.
	 *
	 * @return the uA包装类型
	 */
	public String getUabzlx() {
		return uabzlx;
	}
	
	/**
	 * 设置 - uA包装类型.
	 *
	 * @param uabzlx the new uA包装类型
	 */
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	
	/**
	 * 取得 - 制造路线.
	 *
	 * @return the 制造路线
	 */
	public String getZhizlx() {
		return zhizlx;
	}
	
	/**
	 * 设置 - 制造路线.
	 *
	 * @param zhizlx the new 制造路线
	 */
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	/**
	 * 取得 - 零件号.
	 *
	 * @return the 零件号
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	
	/**
	 * 设置 - 零件号.
	 *
	 * @param lingjbh the new 零件号
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	/**
	 * 取得 - 需求拆分日期.
	 *
	 * @return the 需求拆分日期
	 */
	public String getXuqcfrq() {
		return xuqcfrq;
	}
	
	/**
	 * 设置 - 需求拆分日期.
	 *
	 * @param xuqcfrq the new 需求拆分日期
	 */
	public void setXuqcfrq(String xuqcfrq) {
		this.xuqcfrq = xuqcfrq;
	}
	
	/**
	 * 取得 - p9.
	 *
	 * @return the p9
	 */
	public BigDecimal getP9() {
		return p9;
	}
	
	/**
	 * 设置 - p9.
	 *
	 * @param p9 the new p9
	 */
	public void setP9(BigDecimal p9) {
		this.p9 = p9;
	}
	
	/**
	 * 取得 - 待消耗.
	 *
	 * @return the 待消耗
	 */
	public BigDecimal getDaixh() {
		return daixh;
	}
	
	/**
	 * 设置 - 待消耗.
	 *
	 * @param daixh the new 待消耗
	 */
	public void setDaixh(BigDecimal daixh) {
		this.daixh = daixh;
	}
	
	/**
	 * 取得 - 包装容量.
	 *
	 * @return the 包装容量
	 */
	public BigDecimal getBaozrl() {
		return baozrl;
	}
	
	/**
	 * 设置 - 包装容量.
	 *
	 * @param baozrl the new 包装容量
	 */
	public void setBaozrl(BigDecimal baozrl) {
		this.baozrl = baozrl;
	}
	
	/**
	 * 取得 - uA包装内UC类型.
	 *
	 * @return the uA包装内UC类型
	 */
	public String getUabzuclx() {
		return uabzuclx;
	}
	
	/**
	 * 设置 - uA包装内UC类型.
	 *
	 * @param uabzuclx the new uA包装内UC类型
	 */
	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}
	
	/**
	 * 取得 - 资源获取日期.
	 *
	 * @return the 资源获取日期
	 */
	public String getZiyhqrq() {
		return ziyhqrq;
	}
	
	/**
	 * 设置 - 资源获取日期.
	 *
	 * @param ziyhqrq the new 资源获取日期
	 */
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}
	
	/**
	 * 取得 - p10.
	 *
	 * @return the p10
	 */
	public BigDecimal getP10() {
		return p10;
	}
	
	/**
	 * 设置 - p10.
	 *
	 * @param p10 the new p10
	 */
	public void setP10(BigDecimal p10) {
		this.p10 = p10;
	}
	
	/**
	 * 取得 - 供应商份额.
	 *
	 * @return the 供应商份额
	 */
	public BigDecimal getGongysfe() {
		return gongysfe;
	}
	
	/**
	 * 设置 - 供应商份额.
	 *
	 * @param gongysfe the new 供应商份额
	 */
	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}
	
	/**
	 * 取得 - 系统调整值.
	 *
	 * @return the 系统调整值
	 */
	public BigDecimal getXittzz() {
		return xittzz;
	}
	
	/**
	 * 设置 - 系统调整值.
	 *
	 * @param xittzz the new 系统调整值
	 */
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}
	
	/**
	 * 取得 - p6.
	 *
	 * @return the p6
	 */
	public BigDecimal getP6() {
		return p6;
	}
	
	/**
	 * 设置 - p6.
	 *
	 * @param p6 the new p6
	 */
	public void setP6(BigDecimal p6) {
		this.p6 = p6;
	}
	
	/**
	 * 取得 - p2.
	 *
	 * @return the p2
	 */
	public BigDecimal getP2() {
		return p2;
	}
	
	/**
	 * 设置 - p2.
	 *
	 * @param p2 the new p2
	 */
	public void setP2(BigDecimal p2) {
		this.p2 = p2;
	}
	
	/**
	 * 取得 - p0周期序号.
	 *
	 * @return the p0周期序号
	 */
	public String getP0zqxh() {
		return p0zqxh;
	}
	
	/**
	 * 设置 - p0周期序号.
	 *
	 * @param p0zqxh the new p0周期序号
	 */
	public void setP0zqxh(String p0zqxh) {
		this.p0zqxh = p0zqxh;
	}
	
	/**
	 * 取得 - p4.
	 *
	 * @return the p4
	 */
	public BigDecimal getP4() {
		return p4;
	}
	
	/**
	 * 设置 - p4.
	 *
	 * @param p4 the new p4
	 */
	public void setP4(BigDecimal p4) {
		this.p4 = p4;
	}
	
	/**
	 * 取得 - 订货仓库（线边仓库）.
	 *
	 * @return the 订货仓库（线边仓库）
	 */
	public String getCangkdm() {
		return cangkdm;
	}
	
	/**
	 * 设置 - 订货仓库（线边仓库）.
	 *
	 * @param cangkdm the new 订货仓库（线边仓库）
	 */
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}
	
	/**
	 * 取得 - 库存.
	 *
	 * @return the 库存
	 */
	public BigDecimal getKuc() {
		return kuc;
	}
	
	/**
	 * 设置 - 库存.
	 *
	 * @param kuc the new 库存
	 */
	public void setKuc(BigDecimal kuc) {
		this.kuc = kuc;
	}
	
	/**
	 * 个ttttt
	 *
	 * @return the 外部供货模式
	 */
	public String getWaibghms() {
		return waibghms;
	}
	
	/**
	 * 设置 - 外部供货模式.
	 *
	 * @param waibghms the new 外部供货模式
	 */
	public void setWaibghms(String waibghms) {
		this.waibghms = waibghms;
	}
	
	/**
	 * 取得 - 是否依赖库存.
	 *
	 * @return the 是否依赖库存
	 */
	public String getShifylkc() {
		return shifylkc;
	}
	
	/**
	 * 设置 - 是否依赖库存.
	 *
	 * @param shifylkc the new 是否依赖库存
	 */
	public void setShifylkc(String shifylkc) {
		this.shifylkc = shifylkc;
	}
	
	/**
	 * 取得 - p7.
	 *
	 * @return the p7
	 */
	public BigDecimal getP7() {
		return p7;
	}
	
	/**
	 * 设置 - p7.
	 *
	 * @param p7 the new p7
	 */
	public void setP7(BigDecimal p7) {
		this.p7 = p7;
	}
	
	/**
	 * 取得 - p11.
	 *
	 * @return the p11
	 */
	public BigDecimal getP11() {
		return p11;
	}
	
	/**
	 * 设置 - p11.
	 *
	 * @param p11 the new p11
	 */
	public void setP11(BigDecimal p11) {
		this.p11 = p11;
	}
	
	/**
	 * 取得 - 安全库存.
	 *
	 * @return the 安全库存
	 */
	public BigDecimal getAnqkc() {
		return anqkc;
	}
	
	/**
	 * 设置 - 安全库存.
	 *
	 * @param anqkc the new 安全库存
	 */
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}
	
	/**
	 * 取得 - 路径代码.
	 *
	 * @return the 路径代码
	 */
	public String getLujdm() {
		return lujdm;
	}
	
	/**
	 * 设置 - 路径代码.
	 *
	 * @param lujdm the new 路径代码
	 */
	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}
	
	/**
	 * 取得 - 调整计算值.
	 *
	 * @return the 调整计算值
	 */
	public BigDecimal getTiaozjsz() {
		return tiaozjsz;
	}
	
	/**
	 * 设置 - 调整计算值.
	 *
	 * @param tiaozjsz the new 调整计算值
	 */
	public void setTiaozjsz(BigDecimal tiaozjsz) {
		this.tiaozjsz = tiaozjsz;
	}
	
	/**
	 * 取得 - 交付累计.
	 *
	 * @return the 交付累计
	 */
	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}
	
	/**
	 * 设置 - 交付累计.
	 *
	 * @param jiaoflj the new 交付累计
	 */
	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}
	
	/**
	 * 取得 - 是否依赖待消耗.
	 *
	 * @return the 是否依赖待消耗
	 */
	public String getShifyldxh() {
		return shifyldxh;
	}
	
	/**
	 * 设置 - 是否依赖待消耗.
	 *
	 * @param shifyldxh the new 是否依赖待消耗
	 */
	public void setShifyldxh(String shifyldxh) {
		this.shifyldxh = shifyldxh;
	}
	
	/**
	 * 取得 - p0.
	 *
	 * @return the p0
	 */
	public BigDecimal getP0() {
		return p0;
	}
	
	/**
	 * 设置 - p0.
	 *
	 * @param p0 the new p0
	 */
	public void setP0(BigDecimal p0) {
		this.p0 = p0;
	}
	
	/**
	 * 取得 - 是否依赖待交付.
	 *
	 * @return the 是否依赖待交付
	 */
	public String getShifyldjf() {
		return shifyldjf;
	}
	
	/**
	 * 设置 - 是否依赖待交付.
	 *
	 * @param shifyldjf the new 是否依赖待交付
	 */
	public void setShifyldjf(String shifyldjf) {
		this.shifyldjf = shifyldjf;
	}
	
	/**
	 * 取得 - 订货车间.
	 *
	 * @return the 订货车间
	 */
	public String getDinghcj() {
		return dinghcj;
	}
	
	/**
	 * 设置 - 订货车间.
	 *
	 * @param dinghcj the new 订货车间
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	
	/**
	 * 取得 - p1.
	 *
	 * @return the p1
	 */
	public BigDecimal getP1() {
		return p1;
	}
	
	/**
	 * 设置 - p1.
	 *
	 * @param p1 the new p1
	 */
	public void setP1(BigDecimal p1) {
		this.p1 = p1;
	}
	
	/**
	 * 取得 - 计划员组.
	 *
	 * @return the 计划员组
	 */
	public String getJihydz() {
		return jihydz;
	}
	
	/**
	 * 设置 - 计划员组.
	 *
	 * @param jihydz the new 计划员组
	 */
	public void setJihydz(String jihydz) {
		this.jihydz = jihydz;
	}
	
	/**
	 * 取得 - 订单内容.
	 *
	 * @return the 订单内容
	 */
	public String getDingdnr() {
		return dingdnr;
	}
	
	/**
	 * 设置 - 订单内容.
	 *
	 * @param dingdnr the new 订单内容
	 */
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}
	
	/**
	 * 取得 - 单位.
	 *
	 * @return the 单位
	 */
	public String getDanw() {
		return danw;
	}
	
	/**
	 * 设置 - 单位.
	 *
	 * @param danw the new 单位
	 */
	public void setDanw(String danw) {
		this.danw = danw;
	}
	
	/**
	 * 取得 - p3.
	 *
	 * @return the p3
	 */
	public BigDecimal getP3() {
		return p3;
	}
	
	/**
	 * 设置 - p3.
	 *
	 * @param p3 the new p3
	 */
	public void setP3(BigDecimal p3) {
		this.p3 = p3;
	}
	
	/**
	 * 取得 - 是否依赖安全库存.
	 *
	 * @return the 是否依赖安全库存
	 */
	public String getShifylaqkc() {
		return shifylaqkc;
	}
	
	/**
	 * 设置 - 是否依赖安全库存.
	 *
	 * @param shifylaqkc the new 是否依赖安全库存
	 */
	public void setShifylaqkc(String shifylaqkc) {
		this.shifylaqkc = shifylaqkc;
	}
	
	/**
	 * 取得 - 预告是否取整.
	 *
	 * @return the 预告是否取整
	 */
	public String getYugsfqz() {
		return yugsfqz;
	}
	
	/**
	 * 设置 - 预告是否取整.
	 *
	 * @param yugsfqz the new 预告是否取整
	 */
	public void setYugsfqz(String yugsfqz) {
		this.yugsfqz = yugsfqz;
	}
	
	/**
	 * 取得 - p5.
	 *
	 * @return the p5
	 */
	public BigDecimal getP5() {
		return p5;
	}
	
	/**
	 * 设置 - p5.
	 *
	 * @param p5 the new p5
	 */
	public void setP5(BigDecimal p5) {
		this.p5 = p5;
	}


	/**
	 * 取得 - 用户中心.
	 *
	 * @return the 用户中心
	 */
	public String getUsercenter() {
		return usercenter;
	}
	
	/**
	 * 设置 - 用户中心.
	 *
	 * @param usercenter the new 用户中心
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	/**
	 * 取得 - uA包装内UC容量.
	 *
	 * @return the uA包装内UC容量
	 */
	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}
	
	/**
	 * 设置 - uA包装内UC容量.
	 *
	 * @param uabzucrl the new uA包装内UC容量
	 */
	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}
	
	/**
	 * 取得 - uA包装内UC数量.
	 *
	 * @return the uA包装内UC数量
	 */
	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}
	
	/**
	 * 设置 - uA包装内UC数量.
	 *
	 * @param uabzucsl the new uA包装内UC数量
	 */
	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}
	
	/**
	 * 取得 - 供应商代码.
	 *
	 * @return the 供应商代码
	 */
	public String getGongysdm() {
		return gongysdm;
	}
	
	/**
	 * 设置 - 供应商代码.
	 *
	 * @param gongysdm the new 供应商代码
	 */
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	
	/**
	 * 取得 - 发运周期.
	 *
	 * @return the 发运周期
	 */
	public BigDecimal getFayzq() {
		return fayzq;
	}
	
	/**
	 * 设置 - 发运周期.
	 *
	 * @param fayzq the new 发运周期
	 */
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}
	
	/**
	 * 取得 - 备货周期.
	 *
	 * @return the 备货周期
	 */
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	
	/**
	 * 设置 - 备货周期.
	 *
	 * @param beihzq the new 备货周期
	 */
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Maoxqhzpc [gongyslx=" + gongyslx + ", id=" + id + ", p8=" + p8
				+ ", dingdlj=" + dingdlj + ", uabzlx=" + uabzlx + ", zhizlx="
				+ zhizlx + ", lingjbh=" + lingjbh + ", xuqcfrq=" + xuqcfrq
				+ ", p9=" + p9 + ", daixh=" + daixh + ", baozrl=" + baozrl
				+ ", uabzuclx=" + uabzuclx + ", ziyhqrq=" + ziyhqrq + ", p10="
				+ p10 + ", gongysfe=" + gongysfe + ", xittzz=" + xittzz
				+ ", p6=" + p6 + ", p2=" + p2 + ", p0zqxh=" + p0zqxh + ", p4="
				+ p4 + ", cangkdm=" + cangkdm + ", kuc=" + kuc + ", waibghms="
				+ waibghms + ", shifylkc=" + shifylkc + ", p7=" + p7 + ", p11="
				+ p11 + ", anqkc=" + anqkc + ", lujdm=" + lujdm + ", tiaozjsz="
				+ tiaozjsz + ", jiaoflj=" + jiaoflj + ", shifyldxh="
				+ shifyldxh + ", p0=" + p0 + ", shifyldjf=" + shifyldjf
				+ ", dinghcj=" + dinghcj + ", p1=" + p1 + ", jihydz=" + jihydz
				+ ", dingdnr=" + dingdnr + ", danw=" + danw + ", p3=" + p3
				+ ", shifylaqkc=" + shifylaqkc + ", fayzq=" + fayzq
				+ ", yugsfqz=" + yugsfqz + ", p5=" + p5 + ", usercenter="
				+ usercenter + ", beihzq=" + beihzq + ", uabzucrl=" + uabzucrl
				+ ", uabzucsl=" + uabzucsl + ", gongysdm=" + gongysdm + "]";
	}

	/**
	 * @param dingdzzlj the dingdzzlj to set
	 */
	public void setDingdzzlj(BigDecimal dingdzzlj) {
		this.dingdzzlj = dingdzzlj;
	}

	/**
	 * @return the dingdzzlj
	 */
	public BigDecimal getDingdzzlj() {
		return dingdzzlj;
	}

	/**
	 * @param shiycj the shiycj to set
	 */
	public void setShiycj(String shiycj) {
		this.shiycj = shiycj;
	}

	/**
	 * @return the shiycj
	 */
	public String getShiycj() {
		return shiycj;
	}

	/**
	 * @param gongyhth the gongyhth to set
	 */
	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}

	/**
	 * @return the gongyhth
	 */
	public String getGongyhth() {
		return gongyhth;
	}

	/**
	 * @param lingjsx the lingjsx to set
	 */
	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}

	/**
	 * @return the lingjsx
	 */
	public String getLingjsx() {
		return lingjsx;
	}

	/**
	 * @param fahd the fahd to set
	 */
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}

	/**
	 * @return the fahd
	 */
	public String getFahd() {
		return fahd;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anqkc == null) ? 0 : anqkc.hashCode());
		result = prime * result + ((baozrl == null) ? 0 : baozrl.hashCode());
		result = prime * result + ((beihzq == null) ? 0 : beihzq.hashCode());
		result = prime * result + ((cangkdm == null) ? 0 : cangkdm.hashCode());
		result = prime * result + ((daixh == null) ? 0 : daixh.hashCode());
		result = prime * result + ((danw == null) ? 0 : danw.hashCode());
		result = prime * result + ((dingdlj == null) ? 0 : dingdlj.hashCode());
		result = prime * result + ((dingdnr == null) ? 0 : dingdnr.hashCode());
		result = prime * result
				+ ((dingdzzlj == null) ? 0 : dingdzzlj.hashCode());
		result = prime * result + ((dinghcj == null) ? 0 : dinghcj.hashCode());
		result = prime * result + ((fahd == null) ? 0 : fahd.hashCode());
		result = prime * result + ((fayzq == null) ? 0 : fayzq.hashCode());
		result = prime * result
				+ ((gongyhth == null) ? 0 : gongyhth.hashCode());
		result = prime * result
				+ ((gongysdm == null) ? 0 : gongysdm.hashCode());
		result = prime * result
				+ ((gongysfe == null) ? 0 : gongysfe.hashCode());
		result = prime * result
				+ ((gongyshth == null) ? 0 : gongyshth.hashCode());
		result = prime * result
				+ ((gongyslx == null) ? 0 : gongyslx.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jiaoflj == null) ? 0 : jiaoflj.hashCode());
		result = prime * result + ((jiaofm == null) ? 0 : jiaofm.hashCode());
		result = prime * result + ((jihydz == null) ? 0 : jihydz.hashCode());
		result = prime * result + ((kuc == null) ? 0 : kuc.hashCode());
		result = prime * result + ((lingjbh == null) ? 0 : lingjbh.hashCode());
		result = prime * result + ((lingjsx == null) ? 0 : lingjsx.hashCode());
		result = prime * result + ((lujdm == null) ? 0 : lujdm.hashCode());
		result = prime * result + ((p0 == null) ? 0 : p0.hashCode());
		result = prime * result + ((p0zqxh == null) ? 0 : p0zqxh.hashCode());
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p10 == null) ? 0 : p10.hashCode());
		result = prime * result + ((p11 == null) ? 0 : p11.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
		result = prime * result + ((p4 == null) ? 0 : p4.hashCode());
		result = prime * result + ((p5 == null) ? 0 : p5.hashCode());
		result = prime * result + ((p6 == null) ? 0 : p6.hashCode());
		result = prime * result + ((p7 == null) ? 0 : p7.hashCode());
		result = prime * result + ((p8 == null) ? 0 : p8.hashCode());
		result = prime * result + ((p9 == null) ? 0 : p9.hashCode());
		result = prime * result
				+ ((shifylaqkc == null) ? 0 : shifylaqkc.hashCode());
		result = prime * result
				+ ((shifyldjf == null) ? 0 : shifyldjf.hashCode());
		result = prime * result
				+ ((shifyldxh == null) ? 0 : shifyldxh.hashCode());
		result = prime * result
				+ ((shifylkc == null) ? 0 : shifylkc.hashCode());
		result = prime * result + ((shiycj == null) ? 0 : shiycj.hashCode());
		result = prime * result
				+ ((tiaozjsz == null) ? 0 : tiaozjsz.hashCode());
		result = prime * result + ((uabzlx == null) ? 0 : uabzlx.hashCode());
		result = prime * result
				+ ((uabzuclx == null) ? 0 : uabzuclx.hashCode());
		result = prime * result
				+ ((uabzucrl == null) ? 0 : uabzucrl.hashCode());
		result = prime * result
				+ ((uabzucsl == null) ? 0 : uabzucsl.hashCode());
		result = prime * result
				+ ((usercenter == null) ? 0 : usercenter.hashCode());
		result = prime * result
				+ ((waibghms == null) ? 0 : waibghms.hashCode());
		result = prime * result + ((xittzz == null) ? 0 : xittzz.hashCode());
		result = prime * result + ((xuqcfrq == null) ? 0 : xuqcfrq.hashCode());
		result = prime * result + ((yugsfqz == null) ? 0 : yugsfqz.hashCode());
		result = prime * result + ((zhidgsy == null) ? 0 : zhidgsy.hashCode());
		result = prime * result + ((zhidgys == null) ? 0 : zhidgys.hashCode());
		result = prime * result + ((zhizlx == null) ? 0 : zhizlx.hashCode());
		result = prime * result + ((ziyhqrq == null) ? 0 : ziyhqrq.hashCode());
		result = prime * result + ((zuixqdl == null) ? 0 : zuixqdl.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Maoxqhzpc)) {
			return false;
		}
		Maoxqhzpc other = (Maoxqhzpc) obj;
		if (anqkc == null) {
			if (other.anqkc != null) {
				return false;
			}
		} else if (!anqkc.equals(other.anqkc)) {
			return false;
		}
		if (baozrl == null) {
			if (other.baozrl != null) {
				return false;
			}
		} else if (!baozrl.equals(other.baozrl)) {
			return false;
		}
		if (beihzq == null) {
			if (other.beihzq != null) {
				return false;
			}
		} else if (!beihzq.equals(other.beihzq)) {
			return false;
		}
		if (cangkdm == null) {
			if (other.cangkdm != null) {
				return false;
			}
		} else if (!cangkdm.equals(other.cangkdm)) {
			return false;
		}
		if (daixh == null) {
			if (other.daixh != null) {
				return false;
			}
		} else if (!daixh.equals(other.daixh)) {
			return false;
		}
		if (danw == null) {
			if (other.danw != null) {
				return false;
			}
		} else if (!danw.equals(other.danw)) {
			return false;
		}
		if (dingdlj == null) {
			if (other.dingdlj != null) {
				return false;
			}
		} else if (!dingdlj.equals(other.dingdlj)) {
			return false;
		}
		if (dingdnr == null) {
			if (other.dingdnr != null) {
				return false;
			}
		} else if (!dingdnr.equals(other.dingdnr)) {
			return false;
		}
		if (dingdzzlj == null) {
			if (other.dingdzzlj != null) {
				return false;
			}
		} else if (!dingdzzlj.equals(other.dingdzzlj)) {
			return false;
		}
		if (dinghcj == null) {
			if (other.dinghcj != null) {
				return false;
			}
		} else if (!dinghcj.equals(other.dinghcj)) {
			return false;
		}
		if (fahd == null) {
			if (other.fahd != null) {
				return false;
			}
		} else if (!fahd.equals(other.fahd)) {
			return false;
		}
		if (fayzq == null) {
			if (other.fayzq != null) {
				return false;
			}
		} else if (!fayzq.equals(other.fayzq)) {
			return false;
		}
		if (gongyhth == null) {
			if (other.gongyhth != null) {
				return false;
			}
		} else if (!gongyhth.equals(other.gongyhth)) {
			return false;
		}
		if (gongysdm == null) {
			if (other.gongysdm != null) {
				return false;
			}
		} else if (!gongysdm.equals(other.gongysdm)) {
			return false;
		}
		if (gongysfe == null) {
			if (other.gongysfe != null) {
				return false;
			}
		} else if (!gongysfe.equals(other.gongysfe)) {
			return false;
		}
		if (gongyshth == null) {
			if (other.gongyshth != null) {
				return false;
			}
		} else if (!gongyshth.equals(other.gongyshth)) {
			return false;
		}
		if (gongyslx == null) {
			if (other.gongyslx != null) {
				return false;
			}
		} else if (!gongyslx.equals(other.gongyslx)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (jiaoflj == null) {
			if (other.jiaoflj != null) {
				return false;
			}
		} else if (!jiaoflj.equals(other.jiaoflj)) {
			return false;
		}
		if (jiaofm == null) {
			if (other.jiaofm != null) {
				return false;
			}
		} else if (!jiaofm.equals(other.jiaofm)) {
			return false;
		}
		if (jihydz == null) {
			if (other.jihydz != null) {
				return false;
			}
		} else if (!jihydz.equals(other.jihydz)) {
			return false;
		}
		if (kuc == null) {
			if (other.kuc != null) {
				return false;
			}
		} else if (!kuc.equals(other.kuc)) {
			return false;
		}
		if (lingjbh == null) {
			if (other.lingjbh != null) {
				return false;
			}
		} else if (!lingjbh.equals(other.lingjbh)) {
			return false;
		}
		if (lingjsx == null) {
			if (other.lingjsx != null) {
				return false;
			}
		} else if (!lingjsx.equals(other.lingjsx)) {
			return false;
		}
		if (lujdm == null) {
			if (other.lujdm != null) {
				return false;
			}
		} else if (!lujdm.equals(other.lujdm)) {
			return false;
		}
		if (p0 == null) {
			if (other.p0 != null) {
				return false;
			}
		} else if (!p0.equals(other.p0)) {
			return false;
		}
		if (p0zqxh == null) {
			if (other.p0zqxh != null) {
				return false;
			}
		} else if (!p0zqxh.equals(other.p0zqxh)) {
			return false;
		}
		if (p1 == null) {
			if (other.p1 != null) {
				return false;
			}
		} else if (!p1.equals(other.p1)) {
			return false;
		}
		if (p10 == null) {
			if (other.p10 != null) {
				return false;
			}
		} else if (!p10.equals(other.p10)) {
			return false;
		}
		if (p11 == null) {
			if (other.p11 != null) {
				return false;
			}
		} else if (!p11.equals(other.p11)) {
			return false;
		}
		if (p2 == null) {
			if (other.p2 != null) {
				return false;
			}
		} else if (!p2.equals(other.p2)) {
			return false;
		}
		if (p3 == null) {
			if (other.p3 != null) {
				return false;
			}
		} else if (!p3.equals(other.p3)) {
			return false;
		}
		if (p4 == null) {
			if (other.p4 != null) {
				return false;
			}
		} else if (!p4.equals(other.p4)) {
			return false;
		}
		if (p5 == null) {
			if (other.p5 != null) {
				return false;
			}
		} else if (!p5.equals(other.p5)) {
			return false;
		}
		if (p6 == null) {
			if (other.p6 != null) {
				return false;
			}
		} else if (!p6.equals(other.p6)) {
			return false;
		}
		if (p7 == null) {
			if (other.p7 != null) {
				return false;
			}
		} else if (!p7.equals(other.p7)) {
			return false;
		}
		if (p8 == null) {
			if (other.p8 != null) {
				return false;
			}
		} else if (!p8.equals(other.p8)) {
			return false;
		}
		if (p9 == null) {
			if (other.p9 != null) {
				return false;
			}
		} else if (!p9.equals(other.p9)) {
			return false;
		}
		if (shifylaqkc == null) {
			if (other.shifylaqkc != null) {
				return false;
			}
		} else if (!shifylaqkc.equals(other.shifylaqkc)) {
			return false;
		}
		if (shifyldjf == null) {
			if (other.shifyldjf != null) {
				return false;
			}
		} else if (!shifyldjf.equals(other.shifyldjf)) {
			return false;
		}
		if (shifyldxh == null) {
			if (other.shifyldxh != null) {
				return false;
			}
		} else if (!shifyldxh.equals(other.shifyldxh)) {
			return false;
		}
		if (shifylkc == null) {
			if (other.shifylkc != null) {
				return false;
			}
		} else if (!shifylkc.equals(other.shifylkc)) {
			return false;
		}
		if (shiycj == null) {
			if (other.shiycj != null) {
				return false;
			}
		} else if (!shiycj.equals(other.shiycj)) {
			return false;
		}
		if (tiaozjsz == null) {
			if (other.tiaozjsz != null) {
				return false;
			}
		} else if (!tiaozjsz.equals(other.tiaozjsz)) {
			return false;
		}
		if (uabzlx == null) {
			if (other.uabzlx != null) {
				return false;
			}
		} else if (!uabzlx.equals(other.uabzlx)) {
			return false;
		}
		if (uabzuclx == null) {
			if (other.uabzuclx != null) {
				return false;
			}
		} else if (!uabzuclx.equals(other.uabzuclx)) {
			return false;
		}
		if (uabzucrl == null) {
			if (other.uabzucrl != null) {
				return false;
			}
		} else if (!uabzucrl.equals(other.uabzucrl)) {
			return false;
		}
		if (uabzucsl == null) {
			if (other.uabzucsl != null) {
				return false;
			}
		} else if (!uabzucsl.equals(other.uabzucsl)) {
			return false;
		}
		if (usercenter == null) {
			if (other.usercenter != null) {
				return false;
			}
		} else if (!usercenter.equals(other.usercenter)) {
			return false;
		}
		if (waibghms == null) {
			if (other.waibghms != null) {
				return false;
			}
		} else if (!waibghms.equals(other.waibghms)) {
			return false;
		}
		if (xittzz == null) {
			if (other.xittzz != null) {
				return false;
			}
		} else if (!xittzz.equals(other.xittzz)) {
			return false;
		}
		if (xuqcfrq == null) {
			if (other.xuqcfrq != null) {
				return false;
			}
		} else if (!xuqcfrq.equals(other.xuqcfrq)) {
			return false;
		}
		if (yugsfqz == null) {
			if (other.yugsfqz != null) {
				return false;
			}
		} else if (!yugsfqz.equals(other.yugsfqz)) {
			return false;
		}
		if (zhidgsy == null) {
			if (other.zhidgsy != null) {
				return false;
			}
		} else if (!zhidgsy.equals(other.zhidgsy)) {
			return false;
		}
		if (zhidgys == null) {
			if (other.zhidgys != null) {
				return false;
			}
		} else if (!zhidgys.equals(other.zhidgys)) {
			return false;
		}
		if (zhizlx == null) {
			if (other.zhizlx != null) {
				return false;
			}
		} else if (!zhizlx.equals(other.zhizlx)) {
			return false;
		}
		if (ziyhqrq == null) {
			if (other.ziyhqrq != null) {
				return false;
			}
		} else if (!ziyhqrq.equals(other.ziyhqrq)) {
			return false;
		}
		if (zuixqdl == null) {
			if (other.zuixqdl != null) {
				return false;
			}
		} else if (!zuixqdl.equals(other.zuixqdl)) {
			return false;
		}
		return true;
	}

	/**
	 * @param cangklx the cangklx to set
	 */
	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	/**
	 * @return the cangklx
	 */
	public String getCangklx() {
		return cangklx;
	}
	
	
}
package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


// TODO: Auto-generated Javadoc
/**
 * 实体: 毛需求_汇总_S_参考系
 * @author  李明
 * @version
 * 2011-12-01
 */
public class Maoxqhzsc extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	/**
	 * //最小起订量
	 */
	private BigDecimal zuixqdl;
	/**
	 * //发货地
	 */
	private String fahd;
	/**
	 * //零件属性
	 */
	private String lingjsx;
	
	/**
	 * //订单终止累计
	 */
	private BigDecimal dingdzzlj;
	/**
	 * //用户中心
	 */
	private String usercenter;
	/**
	 * //库存
	 */
	private BigDecimal kuc;
	/**
	 * //S0
	 */
	private BigDecimal s0;
	/**
	 * //S1
	 */
	private BigDecimal s1;
	/**
	 * //S2
	 */
	private BigDecimal s2;
	/**
	 * //S3
	 */
	private BigDecimal s3;
	/**
	 * //S5
	 */
	private BigDecimal s5;
	/**
	 * //S4
	 */
	private BigDecimal s4;
	/**
	 * //S6
	 */
	private BigDecimal s6;
	/**
	 * //S7
	 */
	private BigDecimal s7;
	/**
	 * //S8
	 */
	private BigDecimal s8;
	/**
	 * //S9
	 */
	private BigDecimal s9;
	/**
	 * //S10
	 */
	private BigDecimal s10;
	/**
	 * //是否依赖库存
	 */
	private String shifylkc;
	/**
	 * //UA包装内UC类型
	 */
	private String uabzuclx;
	/**
	 * //UA包装类型
	 */
	private String uabzlx;
	/**
	 * //发运周期
	 */
	private BigDecimal fayzq;
	/**
	 * //订货仓库（线边仓库）
	 */
	private String cangkdm;
	/**
	 * //订货车间
	 */
	private String dinghcj;
	/**
	 * //计划员组
	 */
	private String jihyz;
	/**
	 * //是否依赖待交付
	 */
	private String shifyldjf;
	/**
	 * //备货周期
	 */
	private BigDecimal beihzq;
	/**
	 * //供应商份额
	 */
	private BigDecimal gongysfe;
	/**
	 * //供应商类型
	 */
	private String gongyslx;
	/**
	 * //单位
	 */
	private String danw;
	/**
	 * //资源获取日期
	 */
	private String ziyhqrq;
	/**
	 * //UA包装内UC数量
	 */
	private BigDecimal uabzucsl;
	/**
	 * //预告是否取整
	 */
	private String yugsfqz;
	/**
	 * //路径代码
	 */
	private String lujdm;
	/**
	 * //交付累计
	 */
	private BigDecimal jiaoflj;
	/**
	 * //供应商代码
	 */
	private String gongysdm;
	/**
	 * //ID
	 */
	private String id;
	/**
	 * //是否依赖安全库存
	 */
	private String shifylaqkc;
	/**
	 * //外部供货模式
	 */
	private String waibghms;
	/**
	 * //需求拆分日期
	 */
	private String xuqcfrq;
	/**
	 * //制造路线
	 */
	private String zhizlx;
	/**
	 * //订单内容
	 */
	private String dingdnr;
	/**
	 * //系统调整值
	 */
	private BigDecimal xittzz;
	/**
	 * //是否依赖待消耗
	 */
	private String shifyldxh;
	/**
	 * //调整计算值
	 */
	private BigDecimal tiaozjsz;
	/**
	 * //零件号
	 */
	private String lingjbh;
	/**
	 * //订单累计
	 */
	private BigDecimal dingdlj;
	/**
	 * //S0周序号
	 */
	private String s0zxh;
	/**
	 * //待消耗
	 */
	private BigDecimal daixh;
	/**
	 * //安全库存
	 */
	private BigDecimal anqkc;
	/**
	 * //UA包装内UC容量
	 */
	private BigDecimal uabzucrl;
	/**
	 * //使用车间
	 */
	private String shiycj;
	/**
	 * //供应商合同号
	 */
	private String gongyhth;
	
	
	/**
	 * //交付码
	 */
	private String jiaofm;
	/**
	 * //交付码
	 */
	private String zhidgys;
	/**
	 * /包装容量
	 */
	private BigDecimal baozrl;
	
    private String lingjmc;
	
	private String gongsmc;
	
	private String neibyhzx;
	
	private String cangklx;
	
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

	public String getWulgyyz() {
		return wulgyyz;
	}

	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getWulgyyz1() {
		return wulgyyz1;
	}

	public void setWulgyyz1(String wulgyyz1) {
		this.wulgyyz1 = wulgyyz1;
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

	public BigDecimal getBaozrl() {
		return baozrl;
	}

	public void setBaozrl(BigDecimal baozrl) {
		this.baozrl = baozrl;
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

	/**
	 * 取得 - //用户中心.
	 *
	 * @return the //用户中心
	 */
	public String getUsercenter() {
		return usercenter;
	}
	
	/**
	 * 设置 - //用户中心.
	 *
	 * @param usercenter the new //用户中心
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	/**
	 * 取得 - //库存.
	 *
	 * @return the //库存
	 */
	public BigDecimal getKuc() {
		return kuc;
	}
	
	/**
	 * 设置 - //库存.
	 *
	 * @param kuc the new //库存
	 */
	public void setKuc(BigDecimal kuc) {
		this.kuc = kuc;
	}
	
	/**
	 * 取得 - //S7.
	 *
	 * @return the //S7
	 */
	public BigDecimal getS7() {
		return s7;
	}
	
	/**
	 * 设置 - //S7.
	 *
	 * @param s7 the new //S7
	 */
	public void setS7(BigDecimal s7) {
		this.s7 = s7;
	}
	
	/**
	 * 取得 - //S10.
	 *
	 * @return the //S10
	 */
	public BigDecimal getS10() {
		return s10;
	}
	
	/**
	 * 设置 - //S10.
	 *
	 * @param s10 the new //S10
	 */
	public void setS10(BigDecimal s10) {
		this.s10 = s10;
	}
	
	/**
	 * 取得 - //是否依赖库存.
	 *
	 * @return the //是否依赖库存
	 */
	public String getShifylkc() {
		return shifylkc;
	}
	
	/**
	 * 设置 - //是否依赖库存.
	 *
	 * @param shifylkc the new //是否依赖库存
	 */
	public void setShifylkc(String shifylkc) {
		this.shifylkc = shifylkc;
	}
	
	/**
	 * 取得 - //UA包装内UC类型.
	 *
	 * @return the //UA包装内UC类型
	 */
	public String getUabzuclx() {
		return uabzuclx;
	}
	
	/**
	 * 设置 - //UA包装内UC类型.
	 *
	 * @param uabzuclx the new //UA包装内UC类型
	 */
	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}
	
	/**
	 * 取得 - //UA包装类型.
	 *
	 * @return the //UA包装类型
	 */
	public String getUabzlx() {
		return uabzlx;
	}
	
	/**
	 * 设置 - //UA包装类型.
	 *
	 * @param uabzlx the new //UA包装类型
	 */
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	
	/**
	 * 取得 - //发运周期.
	 *
	 * @return the //发运周期
	 */
	public BigDecimal getFayzq() {
		return fayzq;
	}
	
	/**
	 * 设置 - //发运周期.
	 *
	 * @param fayzq the new //发运周期
	 */
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}
	
	/**
	 * 取得 - //S1.
	 *
	 * @return the //S1
	 */
	public BigDecimal getS1() {
		return s1;
	}
	
	/**
	 * 设置 - //S1.
	 *
	 * @param s1 the new //S1
	 */
	public void setS1(BigDecimal s1) {
		this.s1 = s1;
	}
	
	/**
	 * 取得 - //订货仓库（线边仓库）.
	 *
	 * @return the //订货仓库（线边仓库）
	 */
	public String getCangkdm() {
		return cangkdm;
	}
	
	/**
	 * 设置 - //订货仓库（线边仓库）.
	 *
	 * @param cangkdm the new //订货仓库（线边仓库）
	 */
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}
	
	/**
	 * 取得 - //S5.
	 *
	 * @return the //S5
	 */
	public BigDecimal getS5() {
		return s5;
	}
	
	/**
	 * 设置 - //S5.
	 *
	 * @param s5 the new //S5
	 */
	public void setS5(BigDecimal s5) {
		this.s5 = s5;
	}
	
	/**
	 * 取得 - //订货车间.
	 *
	 * @return the //订货车间
	 */
	public String getDinghcj() {
		return dinghcj;
	}
	
	/**
	 * 设置 - //订货车间.
	 *
	 * @param dinghcj the new //订货车间
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	
	/**
	 * 取得 - //计划员组.
	 *
	 * @return the //计划员组
	 */
	public String getJihyz() {
		return jihyz;
	}
	
	/**
	 * 设置 - //计划员组.
	 *
	 * @param jihyz the new //计划员组
	 */
	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}
	
	/**
	 * 取得 - //S0.
	 *
	 * @return the //S0
	 */
	public BigDecimal getS0() {
		return s0;
	}
	
	/**
	 * 设置 - //S0.
	 *
	 * @param s0 the new //S0
	 */
	public void setS0(BigDecimal s0) {
		this.s0 = s0;
	}
	
	/**
	 * 取得 - //是否依赖待交付.
	 *
	 * @return the //是否依赖待交付
	 */
	public String getShifyldjf() {
		return shifyldjf;
	}
	
	/**
	 * 设置 - //是否依赖待交付.
	 *
	 * @param shifyldjf the new //是否依赖待交付
	 */
	public void setShifyldjf(String shifyldjf) {
		this.shifyldjf = shifyldjf;
	}
	
	/**
	 * 取得 - //备货周期.
	 *
	 * @return the //备货周期
	 */
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	
	/**
	 * 设置 - //备货周期.
	 *
	 * @param beihzq the new //备货周期
	 */
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	
	/**
	 * 取得 - //供应商份额.
	 *
	 * @return the //供应商份额
	 */
	public BigDecimal getGongysfe() {
		return gongysfe;
	}
	
	/**
	 * 设置 - //供应商份额.
	 *
	 * @param gongysfe the new //供应商份额
	 */
	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}
	
	/**
	 * 取得 - //S3.
	 *
	 * @return the //S3
	 */
	public BigDecimal getS3() {
		return s3;
	}
	
	/**
	 * 设置 - //S3.
	 *
	 * @param s3 the new //S3
	 */
	public void setS3(BigDecimal s3) {
		this.s3 = s3;
	}
	
	/**
	 * 取得 - //供应商类型.
	 *
	 * @return the //供应商类型
	 */
	public String getGongyslx() {
		return gongyslx;
	}
	
	/**
	 * 设置 - //供应商类型.
	 *
	 * @param gongyslx the new //供应商类型
	 */
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}
	
	/**
	 * 取得 - //单位.
	 *
	 * @return the //单位
	 */
	public String getDanw() {
		return danw;
	}
	
	/**
	 * 设置 - //单位.
	 *
	 * @param danw the new //单位
	 */
	public void setDanw(String danw) {
		this.danw = danw;
	}
	
	/**
	 * 取得 - //资源获取日期.
	 *
	 * @return the //资源获取日期
	 */
	public String getZiyhqrq() {
		return ziyhqrq;
	}
	
	/**
	 * 设置 - //资源获取日期.
	 *
	 * @param ziyhqrq the new //资源获取日期
	 */
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}
	
	/**
	 * 取得 - //UA包装内UC数量.
	 *
	 * @return the //UA包装内UC数量
	 */
	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}
	
	/**
	 * 设置 - //UA包装内UC数量.
	 *
	 * @param uabzucsl the new //UA包装内UC数量
	 */
	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}
	
	/**
	 * 取得 - //S2.
	 *
	 * @return the //S2
	 */
	public BigDecimal getS2() {
		return s2;
	}
	
	/**
	 * 设置 - //S2.
	 *
	 * @param s2 the new //S2
	 */
	public void setS2(BigDecimal s2) {
		this.s2 = s2;
	}
	
	/**
	 * 取得 - //预告是否取整.
	 *
	 * @return the //预告是否取整
	 */
	public String getYugsfqz() {
		return yugsfqz;
	}
	
	/**
	 * 设置 - //预告是否取整.
	 *
	 * @param yugsfqz the new //预告是否取整
	 */
	public void setYugsfqz(String yugsfqz) {
		this.yugsfqz = yugsfqz;
	}
	
	/**
	 * 取得 - //S4.
	 *
	 * @return the //S4
	 */
	public BigDecimal getS4() {
		return s4;
	}
	
	/**
	 * 设置 - //S4.
	 *
	 * @param s4 the new //S4
	 */
	public void setS4(BigDecimal s4) {
		this.s4 = s4;
	}
	
	/**
	 * 取得 - //路径代码.
	 *
	 * @return the //路径代码
	 */
	public String getLujdm() {
		return lujdm;
	}
	
	/**
	 * 设置 - //路径代码.
	 *
	 * @param lujdm the new //路径代码
	 */
	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}
	
	/**
	 * 取得 - //交付累计.
	 *
	 * @return the //交付累计
	 */
	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}
	
	/**
	 * 设置 - //交付累计.
	 *
	 * @param jiaoflj the new //交付累计
	 */
	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}
	
	/**
	 * 取得 - //S6.
	 *
	 * @return the //S6
	 */
	public BigDecimal getS6() {
		return s6;
	}
	
	/**
	 * 设置 - //S6.
	 *
	 * @param s6 the new //S6
	 */
	public void setS6(BigDecimal s6) {
		this.s6 = s6;
	}
	
	/**
	 * 取得 - //供应商代码.
	 *
	 * @return the //供应商代码
	 */
	public String getGongysdm() {
		return gongysdm;
	}
	
	/**
	 * 设置 - //供应商代码.
	 *
	 * @param gongysdm the new //供应商代码
	 */
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	
	/**
	 * 取得 - //ID.
	 *
	 * @return the //ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置 - //ID.
	 *
	 * @param id the new //ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 取得 - //是否依赖安全库存.
	 *
	 * @return the //是否依赖安全库存
	 */
	public String getShifylaqkc() {
		return shifylaqkc;
	}
	
	/**
	 * 设置 - //是否依赖安全库存.
	 *
	 * @param shifylaqkc the new //是否依赖安全库存
	 */
	public void setShifylaqkc(String shifylaqkc) {
		this.shifylaqkc = shifylaqkc;
	}
	
	/**
	 * 取得 - //外部供货模式.
	 *
	 * @return the //外部供货模式
	 */
	public String getWaibghms() {
		return waibghms;
	}
	
	/**
	 * 设置 - //外部供货模式.
	 *
	 * @param waibghms the new //外部供货模式
	 */
	public void setWaibghms(String waibghms) {
		this.waibghms = waibghms;
	}
	
	/**
	 * 取得 - //需求拆分日期.
	 *
	 * @return the //需求拆分日期
	 */
	public String getXuqcfrq() {
		return xuqcfrq;
	}
	
	/**
	 * 设置 - //需求拆分日期.
	 *
	 * @param xuqcfrq the new //需求拆分日期
	 */
	public void setXuqcfrq(String xuqcfrq) {
		this.xuqcfrq = xuqcfrq;
	}
	
	/**
	 * 取得 - //制造路线.
	 *
	 * @return the //制造路线
	 */
	public String getZhizlx() {
		return zhizlx;
	}
	
	/**
	 * 设置 - //制造路线.
	 *
	 * @param zhizlx the new //制造路线
	 */
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	/**
	 * 取得 - //订单内容.
	 *
	 * @return the //订单内容
	 */
	public String getDingdnr() {
		return dingdnr;
	}
	
	/**
	 * 设置 - //订单内容.
	 *
	 * @param dingdnr the new //订单内容
	 */
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}
	
	/**
	 * 取得 - //系统调整值.
	 *
	 * @return the //系统调整值
	 */
	public BigDecimal getXittzz() {
		return xittzz;
	}
	
	/**
	 * 设置 - //系统调整值.
	 *
	 * @param xittzz the new //系统调整值
	 */
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}
	
	/**
	 * 取得 - //是否依赖待消耗.
	 *
	 * @return the //是否依赖待消耗
	 */
	public String getShifyldxh() {
		return shifyldxh;
	}
	
	/**
	 * 设置 - //是否依赖待消耗.
	 *
	 * @param shifyldxh the new //是否依赖待消耗
	 */
	public void setShifyldxh(String shifyldxh) {
		this.shifyldxh = shifyldxh;
	}
	
	/**
	 * 取得 - //调整计算值.
	 *
	 * @return the //调整计算值
	 */
	public BigDecimal getTiaozjsz() {
		return tiaozjsz;
	}
	
	/**
	 * 设置 - //调整计算值.
	 *
	 * @param tiaozjsz the new //调整计算值
	 */
	public void setTiaozjsz(BigDecimal tiaozjsz) {
		this.tiaozjsz = tiaozjsz;
	}
	
	/**
	 * 取得 - //零件号.
	 *
	 * @return the //零件号
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	
	/**
	 * 设置 - //零件号.
	 *
	 * @param lingjbh the new //零件号
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	/**
	 * 取得 - //订单累计.
	 *
	 * @return the //订单累计
	 */
	public BigDecimal getDingdlj() {
		return dingdlj;
	}
	
	/**
	 * 设置 - //订单累计.
	 *
	 * @param dingdlj the new //订单累计
	 */
	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}
	
	/**
	 * 取得 - //S9.
	 *
	 * @return the //S9
	 */
	public BigDecimal getS9() {
		return s9;
	}
	
	/**
	 * 设置 - //S9.
	 *
	 * @param s9 the new //S9
	 */
	public void setS9(BigDecimal s9) {
		this.s9 = s9;
	}
	
	/**
	 * 取得 - //S0周序号.
	 *
	 * @return the //S0周序号
	 */
	public String getS0zxh() {
		return s0zxh;
	}
	
	/**
	 * 设置 - //S0周序号.
	 *
	 * @param s0zxh the new //S0周序号
	 */
	public void setS0zxh(String s0zxh) {
		this.s0zxh = s0zxh;
	}
	
	/**
	 * 取得 - //待消耗.
	 *
	 * @return the //待消耗
	 */
	public BigDecimal getDaixh() {
		return daixh;
	}
	
	/**
	 * 设置 - //待消耗.
	 *
	 * @param daixh the new //待消耗
	 */
	public void setDaixh(BigDecimal daixh) {
		this.daixh = daixh;
	}
	
	/**
	 * 取得 - //安全库存.
	 *
	 * @return the //安全库存
	 */
	public BigDecimal getAnqkc() {
		return anqkc;
	}
	
	/**
	 * 设置 - //安全库存.
	 *
	 * @param anqkc the new //安全库存
	 */
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}
	
	/**
	 * 取得 - //UA包装内UC容量.
	 *
	 * @return the //UA包装内UC容量
	 */
	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}
	
	/**
	 * 设置 - //UA包装内UC容量.
	 *
	 * @param uabzucrl the new //UA包装内UC容量
	 */
	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}
	
	/**
	 * 取得 - //S8.
	 *
	 * @return the //S8
	 */
	public BigDecimal getS8() {
		return s8;
	}
	
	/**
	 * 设置 - //S8.
	 *
	 * @param s8 the new //S8
	 */
	public void setS8(BigDecimal s8) {
		this.s8 = s8;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Maoxqhzsc [usercenter=" + usercenter + ", kuc=" + kuc + ", s0="
				+ s0 + ", s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + ", s5="
				+ s5 + ", s4=" + s4 + ", s6=" + s6 + ", s7=" + s7 + ", s8="
				+ s8 + ", s9=" + s9 + ", s10=" + s10 + ", shifylkc=" + shifylkc
				+ ", uabzuclx=" + uabzuclx + ", uabzlx=" + uabzlx + ", fayzq="
				+ fayzq + ", cangkdm=" + cangkdm + ", dinghcj=" + dinghcj
				+ ", jihyz=" + jihyz + ", shifyldjf=" + shifyldjf + ", beihzq="
				+ beihzq + ", gongysfe=" + gongysfe + ", gongyslx=" + gongyslx
				+ ", danw=" + danw + ", ziyhqrq=" + ziyhqrq + ", uabzucsl="
				+ uabzucsl + ", yugsfqz=" + yugsfqz + ", lujdm=" + lujdm
				+ ", jiaoflj=" + jiaoflj + ", gongysdm=" + gongysdm + ", id="
				+ id + ", shifylaqkc=" + shifylaqkc + ", waibghms=" + waibghms
				+ ", xuqcfrq=" + xuqcfrq + ", zhizlx=" + zhizlx + ", dingdnr="
				+ dingdnr + ", xittzz=" + xittzz + ", shifyldxh=" + shifyldxh
				+ ", tiaozjsz=" + tiaozjsz + ", lingjbh=" + lingjbh
				+ ", dingdlj=" + dingdlj + ", s0zxh=" + s0zxh + ", daixh="
				+ daixh + ", anqkc=" + anqkc + ", uabzucrl=" + uabzucrl + "]";
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
	 * @param zuixqdl the zuixqdl to set
	 */
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}

	/**
	 * @return the zuixqdl
	 */
	public BigDecimal getZuixqdl() {
		return zuixqdl;
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
				+ ((gongyslx == null) ? 0 : gongyslx.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jiaoflj == null) ? 0 : jiaoflj.hashCode());
		result = prime * result + ((jiaofm == null) ? 0 : jiaofm.hashCode());
		result = prime * result + ((jihyz == null) ? 0 : jihyz.hashCode());
		result = prime * result + ((kuc == null) ? 0 : kuc.hashCode());
		result = prime * result + ((lingjbh == null) ? 0 : lingjbh.hashCode());
		result = prime * result + ((lingjsx == null) ? 0 : lingjsx.hashCode());
		result = prime * result + ((lujdm == null) ? 0 : lujdm.hashCode());
		result = prime * result + ((s0 == null) ? 0 : s0.hashCode());
		result = prime * result + ((s0zxh == null) ? 0 : s0zxh.hashCode());
		result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
		result = prime * result + ((s10 == null) ? 0 : s10.hashCode());
		result = prime * result + ((s2 == null) ? 0 : s2.hashCode());
		result = prime * result + ((s3 == null) ? 0 : s3.hashCode());
		result = prime * result + ((s4 == null) ? 0 : s4.hashCode());
		result = prime * result + ((s5 == null) ? 0 : s5.hashCode());
		result = prime * result + ((s6 == null) ? 0 : s6.hashCode());
		result = prime * result + ((s7 == null) ? 0 : s7.hashCode());
		result = prime * result + ((s8 == null) ? 0 : s8.hashCode());
		result = prime * result + ((s9 == null) ? 0 : s9.hashCode());
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
		if (!(obj instanceof Maoxqhzsc)) {
			return false;
		}
		Maoxqhzsc other = (Maoxqhzsc) obj;
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
		if (jihyz == null) {
			if (other.jihyz != null) {
				return false;
			}
		} else if (!jihyz.equals(other.jihyz)) {
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
		if (s0 == null) {
			if (other.s0 != null) {
				return false;
			}
		} else if (!s0.equals(other.s0)) {
			return false;
		}
		if (s0zxh == null) {
			if (other.s0zxh != null) {
				return false;
			}
		} else if (!s0zxh.equals(other.s0zxh)) {
			return false;
		}
		if (s1 == null) {
			if (other.s1 != null) {
				return false;
			}
		} else if (!s1.equals(other.s1)) {
			return false;
		}
		if (s10 == null) {
			if (other.s10 != null) {
				return false;
			}
		} else if (!s10.equals(other.s10)) {
			return false;
		}
		if (s2 == null) {
			if (other.s2 != null) {
				return false;
			}
		} else if (!s2.equals(other.s2)) {
			return false;
		}
		if (s3 == null) {
			if (other.s3 != null) {
				return false;
			}
		} else if (!s3.equals(other.s3)) {
			return false;
		}
		if (s4 == null) {
			if (other.s4 != null) {
				return false;
			}
		} else if (!s4.equals(other.s4)) {
			return false;
		}
		if (s5 == null) {
			if (other.s5 != null) {
				return false;
			}
		} else if (!s5.equals(other.s5)) {
			return false;
		}
		if (s6 == null) {
			if (other.s6 != null) {
				return false;
			}
		} else if (!s6.equals(other.s6)) {
			return false;
		}
		if (s7 == null) {
			if (other.s7 != null) {
				return false;
			}
		} else if (!s7.equals(other.s7)) {
			return false;
		}
		if (s8 == null) {
			if (other.s8 != null) {
				return false;
			}
		} else if (!s8.equals(other.s8)) {
			return false;
		}
		if (s9 == null) {
			if (other.s9 != null) {
				return false;
			}
		} else if (!s9.equals(other.s9)) {
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
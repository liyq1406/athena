package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 毛需求_汇总_J_参考系
 * @author 李明
 * @version
 * 2011-12-01
 */
public class MjMaoxqhzjcTmp extends PageableSupport{
	
	private static final long serialVersionUID = 1L;
	private BigDecimal dingdzzlj;//订单终止累计
	private String j0rq;//J0日期
	private String gongyslx;//供应商类型
	private String dinghcj;//订货车间
	private String cangkdm;//订货仓库（线边仓库）
	private String id;//ID
	private String ziyhqrq;//资源获取日期
	private BigDecimal j6;//J6
	private BigDecimal j1;//J1
	private BigDecimal uabzucrl;//UA包装内UC容量
	private BigDecimal j0;//J0
	private BigDecimal kuc;//库存
	private BigDecimal beihzq;//备货周期
	private String lingjbh;//零件号
	private String shifylkc;//是否依赖库存
	private String jihyz;//计划员组
	private BigDecimal j5;//J5
	private BigDecimal fayzq;//发运周期
	private String shifyldjf;//是否依赖待交付
	private String danw;//单位
	private String shifyldxh;//是否依赖待消耗
	private BigDecimal j12;//J12
	private BigDecimal j11;//J11
	private BigDecimal anqkc;//安全库存
	private BigDecimal j2;//J2
	private BigDecimal gongysfe;//供应商份额
	private String waibghms;//外部供货模式
	private String yugsfqz;//预告是否取整
	private BigDecimal dingdlj;//订单累计
	private BigDecimal j4;//J4
	private String usercenter;//用户中心
	private String dingdnr;//订单内容
	private BigDecimal uabzucsl;//UA包装内UC数量
	private String uabzlx;//UA包装类型
	private BigDecimal daixh;//待消耗
	private String uabzuclx;//UA包装内UC类型
	private String shifylaqkc;//是否依赖安全库存
	private BigDecimal j9;//J9
	private BigDecimal j13;//J13
	private BigDecimal tiaozjsz;//调整计算值
	private String zhizlx;//制造路线
	private BigDecimal jiaoflj;//交付累计
	private BigDecimal j10;//J10
	private BigDecimal j8;//J8
	private String lujdm;//路径代码
	private BigDecimal j15;//J15
	private BigDecimal xittzz;//系统调整值
	private String gongysdm;//供应商代码
	private String xuqcfrq;//需求拆分日期
	private BigDecimal j7;//J7
	private BigDecimal j14;//J14
	private BigDecimal j3;//J3
	//private BigDecimal baozrl;//包装容量
	private String fahd;//发货地
	private String lingjsx;//零件属性
	private BigDecimal zuixqdl;//最小起订量
	private String cangklx;
	//private String gongyhth;
	
	private String usbzlx; //US包装类型
	private BigDecimal usbzrl;//US包装容量
	
	private String dinghck;
    private String lingjmc;
	
	private String gongsmc;
	
	private String neibyhzx;
	private String wulgyyz;
	private String wulgyyz1;
	private String xiehztbh;
	private String gcbh;
	private String mos2;
	private BigDecimal beihsj2; //备货时间BEIHSJ2
	private BigDecimal cangkshsj2;//仓库送货时间2
	private BigDecimal cangkfhsj2;//仓库返回时间2

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
	
	/**
	 * J1日期
	 */
	private String j1riq;
	/**
	 * J2日期
	 */
	private String j2riq;
	/**
	 * J3日期
	 */
	private String j3riq;
	/**
	 * J4日期
	 */
	private String j4riq;
	/**
	 * J5日期
	 */
	private String j5riq;
	/**
	 * J6日期
	 */
	private String j6riq;
	/**
	 * J7日期
	 */
	private String j7riq;
	/**
	 * J8日期
	 */
	private String j8riq;
	/**
	 * J9日期
	 */
	private String j9riq;
	/**
	 * J10日期
	 */
	private String j10riq;
	/**
	 * J11日期
	 */
	private String j11riq;
	/**
	 * J12日期
	 */
	private String j12riq;
	/**
	 * J13日期
	 */
	private String j13riq;
	/**
	 * J14日期
	 */
	private String j14riq;
	/**
	 * J15日期
	 */
	private String j15riq;
	public String getJ1riq() {
		return j1riq;
	}
	public void setJ1riq(String j1riq) {
		this.j1riq = j1riq;
	}
	public String getJ2riq() {
		return j2riq;
	}
	public void setJ2riq(String j2riq) {
		this.j2riq = j2riq;
	}
	public String getJ3riq() {
		return j3riq;
	}
	public void setJ3riq(String j3riq) {
		this.j3riq = j3riq;
	}
	public String getJ4riq() {
		return j4riq;
	}
	public void setJ4riq(String j4riq) {
		this.j4riq = j4riq;
	}
	public String getJ5riq() {
		return j5riq;
	}
	public void setJ5riq(String j5riq) {
		this.j5riq = j5riq;
	}
	public String getJ6riq() {
		return j6riq;
	}
	public void setJ6riq(String j6riq) {
		this.j6riq = j6riq;
	}
	public String getJ7riq() {
		return j7riq;
	}
	public void setJ7riq(String j7riq) {
		this.j7riq = j7riq;
	}
	public String getJ8riq() {
		return j8riq;
	}
	public void setJ8riq(String j8riq) {
		this.j8riq = j8riq;
	}
	public String getJ9riq() {
		return j9riq;
	}
	public void setJ9riq(String j9riq) {
		this.j9riq = j9riq;
	}
	public String getJ10riq() {
		return j10riq;
	}
	public void setJ10riq(String j10riq) {
		this.j10riq = j10riq;
	}
	public String getJ11riq() {
		return j11riq;
	}
	public void setJ11riq(String j11riq) {
		this.j11riq = j11riq;
	}
	public String getJ12riq() {
		return j12riq;
	}
	public void setJ12riq(String j12riq) {
		this.j12riq = j12riq;
	}
	public String getJ13riq() {
		return j13riq;
	}
	public void setJ13riq(String j13riq) {
		this.j13riq = j13riq;
	}
	public String getJ14riq() {
		return j14riq;
	}
	public void setJ14riq(String j14riq) {
		this.j14riq = j14riq;
	}
	public String getJ15riq() {
		return j15riq;
	}
	public void setJ15riq(String j15riq) {
		this.j15riq = j15riq;
	}
	public String getCangklx() {
		return cangklx;
	}
	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}
	
	private String zhidgys;//指定供应商
	private String jiaofm;//交付吗

	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	public String getZhidgys() {
		return zhidgys;
	}
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	public String getJ0rq() {
		return j0rq;
	}
	public void setJ0rq(String j0rq) {
		this.j0rq = j0rq;
	}
	public String getGongyslx() {
		return gongyslx;
	}
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}
	public String getDinghcj() {
		return dinghcj;
	}
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	public String getCangkdm() {
		return cangkdm;
	}
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZiyhqrq() {
		return ziyhqrq;
	}
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}
	public BigDecimal getJ6() {
		return j6;
	}
	public void setJ6(BigDecimal j6) {
		this.j6 = j6;
	}
	public BigDecimal getJ1() {
		return j1;
	}
	public void setJ1(BigDecimal j1) {
		this.j1 = j1;
	}
	
	public BigDecimal getJ0() {
		return j0;
	}
	public void setJ0(BigDecimal j0) {
		this.j0 = j0;
	}
	public BigDecimal getKuc() {
		return kuc;
	}
	public void setKuc(BigDecimal kuc) {
		this.kuc = kuc;
	}
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getShifylkc() {
		return shifylkc;
	}
	public void setShifylkc(String shifylkc) {
		this.shifylkc = shifylkc;
	}
	public String getJihyz() {
		return jihyz;
	}
	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}
	public BigDecimal getJ5() {
		return j5;
	}
	public void setJ5(BigDecimal j5) {
		this.j5 = j5;
	}
	public BigDecimal getFayzq() {
		return fayzq;
	}
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}
	public String getShifyldjf() {
		return shifyldjf;
	}
	public void setShifyldjf(String shifyldjf) {
		this.shifyldjf = shifyldjf;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getShifyldxh() {
		return shifyldxh;
	}
	public void setShifyldxh(String shifyldxh) {
		this.shifyldxh = shifyldxh;
	}
	public BigDecimal getJ12() {
		return j12;
	}
	public void setJ12(BigDecimal j12) {
		this.j12 = j12;
	}
	public BigDecimal getJ11() {
		return j11;
	}
	public void setJ11(BigDecimal j11) {
		this.j11 = j11;
	}
	public BigDecimal getAnqkc() {
		return anqkc;
	}
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}
	public BigDecimal getJ2() {
		return j2;
	}
	public void setJ2(BigDecimal j2) {
		this.j2 = j2;
	}
	public BigDecimal getGongysfe() {
		return gongysfe;
	}
	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}
	public String getWaibghms() {
		return waibghms;
	}
	public void setWaibghms(String waibghms) {
		this.waibghms = waibghms;
	}
	public String getYugsfqz() {
		return yugsfqz;
	}
	public void setYugsfqz(String yugsfqz) {
		this.yugsfqz = yugsfqz;
	}
	public BigDecimal getDingdlj() {
		return dingdlj;
	}
	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}
	public BigDecimal getJ4() {
		return j4;
	}
	public void setJ4(BigDecimal j4) {
		this.j4 = j4;
	}

	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getDingdnr() {
		return dingdnr;
	}
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}
	
	
	public BigDecimal getDaixh() {
		return daixh;
	}
	public void setDaixh(BigDecimal daixh) {
		this.daixh = daixh;
	}
	
	public String getShifylaqkc() {
		return shifylaqkc;
	}
	public void setShifylaqkc(String shifylaqkc) {
		this.shifylaqkc = shifylaqkc;
	}
	public BigDecimal getJ9() {
		return j9;
	}
	public void setJ9(BigDecimal j9) {
		this.j9 = j9;
	}
	public BigDecimal getJ13() {
		return j13;
	}
	public void setJ13(BigDecimal j13) {
		this.j13 = j13;
	}
	public BigDecimal getTiaozjsz() {
		return tiaozjsz;
	}
	public void setTiaozjsz(BigDecimal tiaozjsz) {
		this.tiaozjsz = tiaozjsz;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}
	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}
	public BigDecimal getJ10() {
		return j10;
	}
	public void setJ10(BigDecimal j10) {
		this.j10 = j10;
	}
	public BigDecimal getJ8() {
		return j8;
	}
	public void setJ8(BigDecimal j8) {
		this.j8 = j8;
	}
	public String getLujdm() {
		return lujdm;
	}
	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}
	public BigDecimal getJ15() {
		return j15;
	}
	public void setJ15(BigDecimal j15) {
		this.j15 = j15;
	}
	public BigDecimal getXittzz() {
		return xittzz;
	}
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public String getXuqcfrq() {
		return xuqcfrq;
	}
	public void setXuqcfrq(String xuqcfrq) {
		this.xuqcfrq = xuqcfrq;
	}
	public BigDecimal getJ7() {
		return j7;
	}
	public void setJ7(BigDecimal j7) {
		this.j7 = j7;
	}
	public BigDecimal getJ14() {
		return j14;
	}
	public void setJ14(BigDecimal j14) {
		this.j14 = j14;
	}
	public BigDecimal getJ3() {
		return j3;
	}
	public void setJ3(BigDecimal j3) {
		this.j3 = j3;
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
	public String getMos2() {
		return mos2;
	}
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	public String getUsbzlx() {
		return usbzlx;
	}
	public void setUsbzlx(String usbzlx) {
		this.usbzlx = usbzlx;
	}
	public BigDecimal getUsbzrl() {
		return usbzrl;
	}
	public void setUsbzrl(BigDecimal usbzrl) {
		this.usbzrl = usbzrl;
	}
	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}
	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}
	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}
	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public String getUabzuclx() {
		return uabzuclx;
	}
	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}
	public String getDinghck() {
		return dinghck;
	}
	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}
	public BigDecimal getBeihsj2() {
		return beihsj2;
	}
	public void setBeihsj2(BigDecimal beihsj2) {
		this.beihsj2 = beihsj2;
	}
	
	
}
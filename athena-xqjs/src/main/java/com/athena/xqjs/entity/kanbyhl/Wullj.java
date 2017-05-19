package com.athena.xqjs.entity.kanbyhl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 参考系物流路径总图表
 * 
 * @author Niesy
 * 
 * date 2012-01-15
 *
 */
public class Wullj  extends PageableSupport{

	private static final long serialVersionUID = 1L;
	
    private String     usercenter; //用户中心     
	private String     fenpqh;    //循环         
	private String     lingjbh;  //零件编号     
	private String     gongysbh;//供应商       
	private String     lujbh;   //路径编号     
	private String     lujmc;   //路径名称     
	private String     fahd;    // 发货地
	private String     waibms;  // 外部模式
	private String     zhidgys; // 指定供应商
	private String     jiaofm;  // 交付码
	private BigDecimal beihzq;  // 备货周期
	private BigDecimal yunszq;  // 运输周期
	private String     gcbh;    // 承运商\供应
	private String     xiehzt;  // 卸货站台
	private BigDecimal songhpc; // 送货频次
	private BigDecimal beihsjc; // 备货时间C
	private String     mudd;    // 目的地
	private String     dinghck; // 定货库
	private String     mos2;    // 模式2
	private BigDecimal cangkshpc2; // 仓库送货频次2
	private BigDecimal cangkshsj2; // 仓库送货时间2
	private BigDecimal cangkfhsj2; // 仓库返回时间2
	private BigDecimal beihsj2;    // 备货时间2
	private BigDecimal ibeihsj2;   // I类型备货时间2
	private BigDecimal pbeihsj2;   // P类型备货时间2
	private String     xianbck;    // 线边库
	private String     mos;        // 模式
	private BigDecimal cangkshpc;  // 仓库送货频次
	private BigDecimal cangkshsj;  // 仓库送货时间
	private BigDecimal cangkfhsj;  // 仓库返回时间
	private BigDecimal beihsj;     // 备货时间
	private BigDecimal ibeihsj;    // I类型备货时间
	private BigDecimal pbeihsj;    // P类型备货时间
	private String     chanx;      // 产线           
	private BigDecimal xuqsl;      //需求数量
	private String     xiaohdbh;   //消耗点编号
	private BigDecimal xiaohbl;    //消耗比例
	private BigDecimal gyfe;    //供应份额
	private Integer    gongzr;     //工作天数
	private BigDecimal cmj; // 每日消耗
	private String jianglms; // 将来模式RD
	private String shengxsj; // 将来生效时间
	private String jianglms2;// 将来模式2（RM）
	private String shengxsj2;// 将来生效时间2
	private String wjianglms;// 外部将来模式
	private String wshengxsj;// 外部将来生效时间
	private String shifjlms;// 是否是将来模式
	private BigDecimal zhuangcxs;// 装车系数
	private String xianbyhlx;// 装车系数
	private String yancsxbz;// 装车系数
	private BigDecimal tr;// 循环时间
	private String jihyz;// 计划员组
	private String zhizlx;// 制造路线
	private String gongyslx;// 供应商类型
	private String dinghcklx;// 定货库类型
	private String wulgyyz2;// 定货库物流工艺员组
	private String wulgyyz1;// 线边库物流工艺员组
	private String wulgyyz;// 循环物流工艺员组
	private String rilbc;// 循环物流工艺员组

	/**
	 * 供应商UC的包装类型
	 */
	private String ucbzlx;
	/**
	 * 供应商UC的容量
	 */
	private BigDecimal ucrl;
	/**
	 * 供应商UA的包装类型
	 */
	private String uabzlx;
	/**
	 * 供应商UA里UC的个数
	 */
	private BigDecimal uaucgs;
	/**
	 * 仓库US的包装类型
	 */
	private String bzlx;
	/**
	 * 仓库US的包装容量
	 */
	private BigDecimal bzrl;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
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
	public String getWaibms() {
		return waibms;
	}
	public void setWaibms(String waibms) {
		this.waibms = waibms;
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
	public BigDecimal getXuqsl() {
		return xuqsl;
	}
	public BigDecimal getGyfe() {
		return gyfe;
	}
	public void setGyfe(BigDecimal gyfe) {
		this.gyfe = gyfe;
	}

	public String getShifjlms() {
		return shifjlms;
	}

	public void setShifjlms(String shifjlms) {
		this.shifjlms = shifjlms;
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

	public String getWjianglms() {
		return wjianglms;
	}

	public void setWjianglms(String wjianglms) {
		this.wjianglms = wjianglms;
	}

	public String getWshengxsj() {
		return wshengxsj;
	}

	public void setWshengxsj(String wshengxsj) {
		this.wshengxsj = wshengxsj;
	}
	public void setXuqsl(BigDecimal xuqsl) {
		this.xuqsl = xuqsl;
	}
	public String getXiehzt() {
		return xiehzt;
	}
	public BigDecimal getCmj() {
		return cmj;
	}
	public void setCmj(BigDecimal cmj) {
		this.cmj = cmj;
	}
	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
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
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	public String getXiaohdbh() {
		return xiaohdbh;
	}
	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}
	public BigDecimal getXiaohbl() {
		return xiaohbl;
	}
	public void setXiaohbl(BigDecimal xiaohbl) {
		this.xiaohbl = xiaohbl;
	}
	public Integer getGongzr() {
		return gongzr;
	}
	public void setGongzr(Integer gongzr) {
		this.gongzr = gongzr;
	}

	public BigDecimal getZhuangcxs() {
		return zhuangcxs;
	}

	public void setZhuangcxs(BigDecimal zhuangcxs) {
		this.zhuangcxs = zhuangcxs;
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

	public String getUcbzlx() {
		return ucbzlx;
	}

	public void setUcbzlx(String ucbzlx) {
		this.ucbzlx = ucbzlx;
	}

	public BigDecimal getUcrl() {
		return ucrl;
	}

	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}

	public String getUabzlx() {
		return uabzlx;
	}

	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}

	public BigDecimal getUaucgs() {
		return uaucgs;
	}

	public void setUaucgs(BigDecimal uaucgs) {
		this.uaucgs = uaucgs;
	}

	public String getBzlx() {
		return bzlx;
	}

	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
	}

	public BigDecimal getBzrl() {
		return bzrl;
	}

	public void setBzrl(BigDecimal bzrl) {
		this.bzrl = bzrl;
	}

	public BigDecimal getTr() {
		return tr;
	}

	public void setTr(BigDecimal tr) {
		this.tr = tr;
	}

	public String getJihyz() {
		return jihyz;
	}

	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
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

	public String getWulgyyz2() {
		return wulgyyz2;
	}

	public void setWulgyyz2(String wulgyyz2) {
		this.wulgyyz2 = wulgyyz2;
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

	public BigDecimal getBeihsjc() {
		return beihsjc;
	}

	public void setBeihsjc(BigDecimal beihsjc) {
		this.beihsjc = beihsjc;
	}

	public String getRilbc() {
		return rilbc;
	}

	public void setRilbc(String rilbc) {
		this.rilbc = rilbc;
	}



	

}

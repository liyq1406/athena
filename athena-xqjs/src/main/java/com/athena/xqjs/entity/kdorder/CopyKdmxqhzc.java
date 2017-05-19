package com.athena.xqjs.entity.kdorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: KD毛需求_汇总_参考系（复制）
 * @author 李明
 * @version V1.0
 * @Date：2012-1-30
 */
public class CopyKdmxqhzc extends PageableSupport{
	
	private String zhizlx;//制造路线
	private String lingjbh;//零件编号
	private String usercenter;//用户中心
	private String gongysdm;//供应商代码
	private BigDecimal gongysfe;//供应商份额
	private BigDecimal beihzq;//备货周期
	private String dingdnr;//订单内容
	private String fahd;//发货地
	private String dinghck;//订货仓库
	//private String dinghaqkc;//订货安全库存
	private String dinghcj;//订货车间
	//private String kuc;//库存
	private BigDecimal uabzucsl;//UA包装内UC数量
	private String lujdm;//路径代码
	private String danw;//单位
	private BigDecimal fayzq;//发运周期
	//private String anqkc;//安全库存
	private String jihyz;//计划员组
	private BigDecimal uabzucrl;//UA包装内UC容量
	//private BigDecimal dingdlj;//订单累计
	private String uabzuclx;//UA包装内UC类型
	private String ziyhqrq;//资源获取日期
	//private BigDecimal jiaoflj;//交付累计
	private String uabzlx;//UA包装类型
	private String id;//id
	private String waibghms;
	
	
	
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
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	public BigDecimal getFayzq() {
		return fayzq;
	}
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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
	
	public String getDinghcj() {
		return dinghcj;
	}
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
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

	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
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
	
	
	
}
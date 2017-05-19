package com.athena.xqjs.entity.shouhdcl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 据收跟踪单信息
 * @author WL
 * @date 2012-04-01
 *
 */
public class Jusgzd extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7914344649634321643L;

	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 拒收跟踪单号
	 */
	private String jusgzdh;
	/**
	 * 拒收单号
	 */
	private String jusdh;
	/**
	 * UT号
	 */
	private String uth;
	/**
	 * BL号
	 */
	private String blh;
	/**
	 * 批次号
	 */
	private String pich;
	/**
	 * 零件编号
	 */
	private String lingjbh;
	/**
	 * 零件名称
	 */
	private String lingjmc;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * 失效日期
	 */
	private String shixrq;
	/**
	 * UC号
	 */
	private String uch;
	/**
	 * 拒收零件数量
	 */
	private BigDecimal jusljs;
	/**
	 * UA号
	 */
	private String uah;
	/**
	 * 供货模式
	 */
	private String gonghms;
	/**
	 * UA包装类型
	 */
	private String uabzlx;
	/**
	 * UC包装类型
	 */
	private String ucbzlx;
	/**
	 * UC个数
	 */
	private BigDecimal ucgs;
	/**
	 * 供应商代码
	 */
	private String gongysdm;
	/**
	 * 供应商名称
	 */
	private String gongysmc;
	/**
	 * 承运商代码
	 */
	private String chengysdm;
	/**
	 * 承运商名称
	 */
	private String chengysmc;
	/**
	 * 操作时间
	 */
	private String caozsj;
	/**
	 * 卸货点
	 */
	private String xiehd;
	/**
	 * 仓库编号
	 */
	private String cangkbh;
	/**
	 * 子仓库编号
	 */
	private String zickbh;
	/**
	 * 拒收原因
	 */
	private String jusyy;
	/**
	 * 状态
	 */
	private String zhuangt;
	/**
	 * 处理结果
	 */
	private String chuljg;
	/**
	 * 操作员
	 */
	private String caozy;
	/**
	 * 验收员
	 */
	private String yansy;
	/**
	 * 要货令号
	 */
	private String yaohlh;
	/**
	 * 订单号
	 */
	private String dingdh;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 修改人
	 */
	private String editor;
	/**
	 * 修改时间
	 */
	private String edit_time;
	
	/**
	 * 计划员
	 */
	private String jihy;
	
	/**
	 * UA容量
	 */
	private BigDecimal uarl;
	
	/**
	 * UC容量
	 */
	private BigDecimal uchl;
	
	/**
	 * 发货时间
	 */
	private String fahsj;
	
	/**
	 * 到货时间
	 */
	private String daohsj;
	private String dinghcj;
	private String jihydm;
	private String cangkbh1;
	
	//审核人
	private String beiz4;
	private String beiz7;
	public String getBeiz4() {
		return beiz4;
	}
	public void setBeiz4(String beiz4) {
		this.beiz4 = beiz4;
	}
	public String getBeiz7() {
		return beiz7;
	}
	public void setBeiz7(String beiz7) {
		this.beiz7 = beiz7;
	}
	public String getJihy() {
		return jihy;
	}
	public void setJihy(String jihy) {
		this.jihy = jihy;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getJusgzdh() {
		return jusgzdh;
	}
	public void setJusgzdh(String jusgzdh) {
		this.jusgzdh = jusgzdh;
	}
	public String getJusdh() {
		return jusdh;
	}
	public void setJusdh(String jusdh) {
		this.jusdh = jusdh;
	}
	public String getUth() {
		return uth;
	}
	public void setUth(String uth) {
		this.uth = uth;
	}
	public String getBlh() {
		return blh;
	}
	public void setBlh(String blh) {
		this.blh = blh;
	}
	public String getPich() {
		return pich;
	}
	public void setPich(String pich) {
		this.pich = pich;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getLingjmc() {
		return lingjmc;
	}
	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getShixrq() {
		return shixrq;
	}
	public void setShixrq(String shixrq) {
		this.shixrq = shixrq;
	}
	public String getUch() {
		return uch;
	}
	public void setUch(String uch) {
		this.uch = uch;
	}
	public BigDecimal getJusljs() {
		return jusljs;
	}
	public void setJusljs(BigDecimal jusljs) {
		this.jusljs = jusljs;
	}
	public String getUah() {
		return uah;
	}
	public void setUah(String uah) {
		this.uah = uah;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public String getUcbzlx() {
		return ucbzlx;
	}
	public void setUcbzlx(String ucbzlx) {
		this.ucbzlx = ucbzlx;
	}
	public BigDecimal getUcgs() {
		return ucgs;
	}
	public void setUcgs(BigDecimal ucgs) {
		this.ucgs = ucgs;
	}
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public String getGongysmc() {
		return gongysmc;
	}
	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}
	public String getChengysdm() {
		return chengysdm;
	}
	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}
	public String getChengysmc() {
		return chengysmc;
	}
	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}
	public String getCaozsj() {
		return caozsj;
	}
	public void setCaozsj(String caozsj) {
		this.caozsj = caozsj;
	}
	public String getXiehd() {
		return xiehd;
	}
	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getZickbh() {
		return zickbh;
	}
	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}
	public String getJusyy() {
		return jusyy;
	}
	public void setJusyy(String jusyy) {
		this.jusyy = jusyy;
	}
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getChuljg() {
		return chuljg;
	}
	public void setChuljg(String chuljg) {
		this.chuljg = chuljg;
	}
	public String getCaozy() {
		return caozy;
	}
	public void setCaozy(String caozy) {
		this.caozy = caozy;
	}
	public String getYansy() {
		return yansy;
	}
	public void setYansy(String yansy) {
		this.yansy = yansy;
	}
	public String getYaohlh() {
		return yaohlh;
	}
	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}
	public String getDingdh() {
		return dingdh;
	}
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
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
	public String getGonghms() {
		return gonghms;
	}
	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}
	public BigDecimal getUarl() {
		return uarl;
	}
	public void setUarl(BigDecimal uarl) {
		this.uarl = uarl;
	}
	public BigDecimal getUchl() {
		return uchl;
	}
	public void setUchl(BigDecimal uchl) {
		this.uchl = uchl;
	}
	public String getFahsj() {
		return fahsj;
	}
	public void setFahsj(String fahsj) {
		this.fahsj = fahsj;
	}
	public String getDaohsj() {
		return daohsj;
	}
	public void setDaohsj(String daohsj) {
		this.daohsj = daohsj;
	}
	/**
	 * 取得 dinghcj
	 * @return the dinghcj
	 */
	public String getDinghcj() {
		return dinghcj;
	}
	/**
	 * @param dinghcj the dinghcj to set
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	/**
	 * 取得 jihydm
	 * @return the jihydm
	 */
	public String getJihydm() {
		return jihydm;
	}
	/**
	 * @param jihydm the jihydm to set
	 */
	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
	}
	/**
	 * 取得 cangkbh1
	 * @return the cangkbh1
	 */
	public String getCangkbh1() {
		return cangkbh1;
	}
	/**
	 * @param cangkbh1 the cangkbh1 to set
	 */
	public void setCangkbh1(String cangkbh1) {
		this.cangkbh1 = cangkbh1;
	}
	
	
}

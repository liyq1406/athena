package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * 运单&运单明细
 * @author huilit
 *
 */
public class Yund extends PageableSupport implements Domain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3196732868065228459L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	//共有字段
	private String usercenter;	//用户中心（工厂）
	private String yundh;		//运单号
	
	//以下为运单字段
	private String quybh;		//等待区域编号
	private String daztbh;		//大站台编号
	private String kach;		//卡车号
	private String shengbsj;	//申报时间
	private String rucsj;		//入厂时间
	private String chucsj;		//出厂时间
	private String flag;		//混装标识（1正常、2混装）
	private String zhuangt;		//运单状态
	private String zhuangtmc;	//状态对应流程名称
	private String jijbs;		//急件状态（0或null非急件、1急件）
	
	//以下为运单明细字段
	private String blh;			//BL号
	private String uth;			//UT号
	private String zuizsj;		//最早交付时间
	private String zuiwsj;		//最晚交付时间
	private String cangkbh;		//仓库编号（大站台?）
	private String xiehzt;		//卸货站台（车位?）
	private String chengysdm;	//承运商代码
	private String daohzt;		//到货通知单状态
	private Integer uasl;		//UA包装数量
	
	private String gongsmc;	//供承运商名称
	//以下为流程定义表字段
	private String biaozsj;
	
	//以下为容器单据表相关字段
	private String danjbh;
	
	//共有通用字段
	private String beiz;		//备注
	private String beiz1;		//备注1
	private String beiz2;		//备注2
	private String beiz3;		//备注3
	private String creator;		//创建人
	private String createTime;	//创建时间
	private String editor;		//修改人
	private String editTime;	//修改时间
	private Integer paidtqqsx;  //排队提前上限
	private Integer min;        //实际提前时间
	private String quymc;		//等待区域编号
	
	private String tiqpdbs;		//提前排队标识（0正常，1提前）
	private String YUNDH;		//运单号
	
	private String sb;
	
	private String pd;
	
	private String fk;
	
	private String cw;
	
	private String jj;
	
	private String zhunrsj;     //准入时间
	public String getZhunrsj() {
		return zhunrsj;
	}

	public void setZhunrsj(String zhunrsj) {
		this.zhunrsj = zhunrsj;
	}
	public String getJj() {
		return jj;
	}

	public void setJj(String jj) {
		this.jj = jj;
	}

	public String getCw() {
		return cw;
	}

	public void setCw(String cw) {
		this.cw = cw;
	}

	public String getSb() {
		return sb;
	}

	public void setSb(String sb) {
		this.sb = sb;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}

	public String getFk() {
		return fk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getQuymc() {
		return quymc;
	}

	public void setQuymc(String quymc) {
		this.quymc = quymc;
	}

	public Integer getPaidtqqsx() {
		return paidtqqsx;
	}

	public void setPaidtqqsx(Integer paidtqqsx) {
		this.paidtqqsx = paidtqqsx;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getYundh() {
		return yundh;
	}

	public void setYundh(String yundh) {
		this.yundh = yundh;
	}

	public String getBlh() {
		return blh;
	}

	public void setBlh(String blh) {
		this.blh = blh;
	}

	public String getUth() {
		return uth;
	}

	public void setUth(String uth) {
		this.uth = uth;
	}

	public String getZuizsj() {
		return zuizsj;
	}

	public void setZuizsj(String zuizsj) {
		this.zuizsj = zuizsj;
	}

	public String getZuiwsj() {
		return zuiwsj;
	}

	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getXiehzt() {
		return xiehzt;
	}

	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getId() {
		return yundh;
	}

	public void setId(String arg0) {
		this.yundh = arg0;
	}

	public String getQuybh() {
		return quybh;
	}

	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getKach() {
		return kach;
	}

	public void setKach(String kach) {
		this.kach = kach;
	}

	public String getShengbsj() {
		return shengbsj;
	}

	public void setShengbsj(String shengbsj) {
		this.shengbsj = shengbsj;
	}

	public String getRucsj() {
		return rucsj;
	}

	public void setRucsj(String rucsj) {
		this.rucsj = rucsj;
	}

	public String getChucsj() {
		return chucsj;
	}

	public void setChucsj(String chucsj) {
		this.chucsj = chucsj;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getZhuangtmc() {
		return zhuangtmc;
	}

	public void setZhuangtmc(String zhuangtmc) {
		this.zhuangtmc = zhuangtmc;
	}

	public String getJijbs() {
		return jijbs;
	}

	public void setJijbs(String jijbs) {
		this.jijbs = jijbs;
	}

	public String getBiaozsj() {
		return biaozsj;
	}

	public void setBiaozsj(String biaozsj) {
		this.biaozsj = biaozsj;
	}

	public String getTiqpdbs() {
		return tiqpdbs;
	}

	public void setTiqpdbs(String tiqpdbs) {
		this.tiqpdbs = tiqpdbs;
	}

	public String getDaohzt() {
		return daohzt;
	}

	public void setDaohzt(String daohzt) {
		this.daohzt = daohzt;
	}

	public Integer getUasl() {
		return uasl;
	}

	public void setUasl(Integer uasl) {
		this.uasl = uasl;
	}

	public String getDanjbh() {
		return danjbh;
	}

	public void setDanjbh(String danjbh) {
		this.danjbh = danjbh;
	}

	public String getYUNDH() {
		return YUNDH;
	}

	public void setYUNDH(String yUNDH) {
		YUNDH = yUNDH;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}
	
	

}

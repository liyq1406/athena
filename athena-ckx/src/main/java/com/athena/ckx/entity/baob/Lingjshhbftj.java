package com.athena.ckx.entity.baob;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件收货和报废统计
 * @author lc 
 */
public class Lingjshhbftj extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;

	private String usercenter;     //用户中心
	
	private  String lingjbh;     //零件编号
	
	private String lingjmc;     //零件名称
	
	private String cangkbh;     //仓库编号
	
	private BigDecimal ruksl;     //入库数量
	
	private String lingjzt;     //状态
	
	private String ruksj;     //入库时间
	
	private String startrukrq;     //开始入库时间
	 
	private String endrukrq;     //结束入库时间
	
	private String createTime;     //创建时间
	
	private String yaohlh;     //要货令号
	
	private String gongysdm;     //供应商代码
	
	private String chengysdm;     //承运商代码
	
	private BigDecimal yanssl;     //验收数量
	
	private String yuany;     //报废原因
	
	private String zhuangtsx;     //供货模式
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public BigDecimal getRuksl() {
		return ruksl;
	}

	public void setRuksl(BigDecimal ruksl) {
		this.ruksl = ruksl;
	}

	public String getLingjzt() {
		return lingjzt;
	}

	public void setLingjzt(String lingjzt) {
		this.lingjzt = lingjzt;
	}

	public String getStartrukrq() {
		return startrukrq;
	}

	public void setStartrukrq(String startrukrq) {
		this.startrukrq = startrukrq;
	}

	public String getEndrukrq() {
		return endrukrq;
	}

	public void setEndrukrq(String endrukrq) {
		this.endrukrq = endrukrq;
	}
	
	public String getRuksj() {
		return ruksj;
	}

	public void setRuksj(String ruksj) {
		this.ruksj = ruksj;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getYaohlh() {
		return yaohlh;
	}
	
	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}
	
	public String getGongysdm() {
		return gongysdm;
	}
	
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	
	public String getChengysdm() {
		return chengysdm;
	}
	
	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}
	
	public BigDecimal getYanssl() {
		return yanssl;
	}

	public void setYanssl(BigDecimal yanssl) {
		this.yanssl = yanssl;
	}
	
	public String getYuany() {
		return yuany;
	}
	
	public void setYuany(String yuany) {
		this.yuany = yuany;
	}
	
	public String getZhuangtsx() {
		return zhuangtsx;
	}
	
	public void setZhuangtsx(String zhuangtsx) {
		this.zhuangtsx = zhuangtsx;
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

}

package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class DdbhMaoxqsxxh {
    private String daxxh; //大线线号

    private String whof; //订单好号

    private BigDecimal sxsxh;//上线顺序号

    private Date caifrq;//拆分日期
    
    private BigDecimal ckpyy;//插空的偏移量 
    
    private BigDecimal zhanysxsxh;//转运记录处理的最大数量序号

     public String getDaxxh() {
        return daxxh;
    }

    public void setDaxxh(String daxxh) {
        this.daxxh = daxxh == null ? null : daxxh.trim();
    }

    public String getWhof() {
        return whof;
    }

    public void setWhof(String whof) {
        this.whof = whof == null ? null : whof.trim();
    }

    public BigDecimal getSxsxh() {
        return sxsxh;
    }

    public void setSxsxh(BigDecimal sxsxh) {
        this.sxsxh = sxsxh;
    }

    public Date getCaifrq() {
        return caifrq;
    }

    public void setCaifrq(Date caifrq) {
        this.caifrq = caifrq;
    }

	public BigDecimal getCkpyy() {
		return ckpyy;
	}

	public void setCkpyy(BigDecimal ckpyy) {
		this.ckpyy = ckpyy;
	}

	public BigDecimal getZhanysxsxh() {
		return zhanysxsxh;
	}

	public void setZhanysxsxh(BigDecimal zhanysxsxh) {
		this.zhanysxsxh = zhanysxsxh;
	}
    
    
}
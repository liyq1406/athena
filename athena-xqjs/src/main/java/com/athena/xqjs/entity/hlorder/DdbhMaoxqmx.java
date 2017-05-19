package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class DdbhMaoxqmx {
    private String id;

    private String usercenter;

    private String shiycj;

    private String chanx;

    private Date xuqrq;

    private String lingjbh;

    private BigDecimal lingjsl;

    private String danw;

    private String zhizlx;

    private Date xuqksrq;

    private Date xuqjsrq;
    
    private String flag; //0-默认状态，1-接口使用中间状态，2-接口输出完成状态
    
    private Date xuqcfsj;//需求拆分时间
    
    private String beiz; //备注
    
    private BigDecimal beiz1; //备注
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getShiycj() {
        return shiycj;
    }

    public void setShiycj(String shiycj) {
        this.shiycj = shiycj == null ? null : shiycj.trim();
    }

    public String getChanx() {
        return chanx;
    }

    public void setChanx(String chanx) {
        this.chanx = chanx == null ? null : chanx.trim();
    }

    public Date getXuqrq() {
        return xuqrq;
    }

    public void setXuqrq(Date xuqrq) {
        this.xuqrq = xuqrq;
    }

    public String getLingjbh() {
        return lingjbh;
    }

    public void setLingjbh(String lingjbh) {
        this.lingjbh = lingjbh == null ? null : lingjbh.trim();
    }

    public BigDecimal getLingjsl() {
        return lingjsl;
    }

    public void setLingjsl(BigDecimal lingjsl) {
        this.lingjsl = lingjsl;
    }

    public String getDanw() {
        return danw;
    }

    public void setDanw(String danw) {
        this.danw = danw == null ? null : danw.trim();
    }

    public String getZhizlx() {
        return zhizlx;
    }

    public void setZhizlx(String zhizlx) {
        this.zhizlx = zhizlx == null ? null : zhizlx.trim();
    }

    public Date getXuqksrq() {
        return xuqksrq;
    }

    public void setXuqksrq(Date xuqksrq) {
        this.xuqksrq = xuqksrq;
    }

    public Date getXuqjsrq() {
        return xuqjsrq;
    }

    public void setXuqjsrq(Date xuqjsrq) {
        this.xuqjsrq = xuqjsrq;
    }

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getXuqcfsj() {
		return xuqcfsj;
	}

	public void setXuqcfsj(Date xuqcfsj) {
		this.xuqcfsj = xuqcfsj;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public BigDecimal getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(BigDecimal beiz1) {
		this.beiz1 = beiz1;
	}

	
    
    
}
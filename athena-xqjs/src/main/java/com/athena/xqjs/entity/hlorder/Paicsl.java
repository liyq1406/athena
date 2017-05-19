package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Paicsl {
    private BigDecimal jihsxl;

    private BigDecimal jihxxl;

    private String beiz;

    private String creator;

    private Date createTime;
    
    private String chej;

    private String daxxh;

    private Date riq;

    private String usercenter;

    public String getChej() {
        return chej;
    }

    public void setChej(String chej) {
        this.chej = chej == null ? null : chej.trim();
    }

    public String getDaxxh() {
        return daxxh;
    }

    public void setDaxxh(String daxxh) {
        this.daxxh = daxxh == null ? null : daxxh.trim();
    }

    public Date getRiq() {
        return riq;
    }

    public void setRiq(Date riq) {
        this.riq = riq;
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public BigDecimal getJihsxl() {
        return jihsxl;
    }

    public void setJihsxl(BigDecimal jihsxl) {
        this.jihsxl = jihsxl;
    }

    public BigDecimal getJihxxl() {
        return jihxxl;
    }

    public void setJihxxl(BigDecimal jihxxl) {
        this.jihxxl = jihxxl;
    }

    public String getBeiz() {
        return beiz;
    }

    public void setBeiz(String beiz) {
        this.beiz = beiz == null ? null : beiz.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
/**
 * 备件外销计划
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Beijwxjh {
    private String liush;

    private Date yujjhzsj;

    private BigDecimal shul;

    private BigDecimal wancsl;

    private String zongch;

    private String usercenter;

    private String daxxh;

    private String fenzxh;

    private String zhankrq;

    private String beijwx;

    private String filler;

    private String xuqly;

    private String creator;

    private Date createTime;

    private String wenjmc;

    private String wenjlj;

    public String getLiush() {
        return liush;
    }

    public void setLiush(String liush) {
        this.liush = liush == null ? null : liush.trim();
    }

    public Date getYujjhzsj() {
        return yujjhzsj;
    }

    public void setYujjhzsj(Date yujjhzsj) {
        this.yujjhzsj = yujjhzsj;
    }

    public BigDecimal getShul() {
        return shul;
    }

    public void setShul(BigDecimal shul) {
        this.shul = shul;
    }

    public BigDecimal getWancsl() {
        return wancsl;
    }

    public void setWancsl(BigDecimal wancsl) {
        this.wancsl = wancsl;
    }

    public String getZongch() {
        return zongch;
    }

    public void setZongch(String zongch) {
        this.zongch = zongch == null ? null : zongch.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getDaxxh() {
        return daxxh;
    }

    public void setDaxxh(String daxxh) {
        this.daxxh = daxxh == null ? null : daxxh.trim();
    }

    public String getFenzxh() {
        return fenzxh;
    }

    public void setFenzxh(String fenzxh) {
        this.fenzxh = fenzxh == null ? null : fenzxh.trim();
    }

    public String getZhankrq() {
        return zhankrq;
    }

    public void setZhankrq(String zhankrq) {
        this.zhankrq = zhankrq == null ? null : zhankrq.trim();
    }

    public String getBeijwx() {
        return beijwx;
    }

    public void setBeijwx(String beijwx) {
        this.beijwx = beijwx == null ? null : beijwx.trim();
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler == null ? null : filler.trim();
    }

    public String getXuqly() {
        return xuqly;
    }

    public void setXuqly(String xuqly) {
        this.xuqly = xuqly == null ? null : xuqly.trim();
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

    public String getWenjmc() {
        return wenjmc;
    }

    public void setWenjmc(String wenjmc) {
        this.wenjmc = wenjmc == null ? null : wenjmc.trim();
    }

    public String getWenjlj() {
        return wenjlj;
    }

    public void setWenjlj(String wenjlj) {
        this.wenjlj = wenjlj == null ? null : wenjlj.trim();
    }
}
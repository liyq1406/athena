/**
 * time:2015.12.7
 * 
 * @author liuquan 
 * descreption:Nup单量份信息
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Nup {
    private String usercenter;

    private String lcdv24;

    private String lcdvbzk;

    private String zhankrq;

    private String lingjbh;

    private String chej;

    private BigDecimal shul;

    private String danw;

    private String zhizlx;

    private String biaos;

    private String yansbs;

    private String yezbs;

    private String uwjbs;

    private String creator;

    private Date createTime;

    private String editor;

    private Date editTime;

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getLcdv24() {
        return lcdv24;
    }

    public void setLcdv24(String lcdv24) {
        this.lcdv24 = lcdv24 == null ? null : lcdv24.trim();
    }

    public String getLcdvbzk() {
        return lcdvbzk;
    }

    public void setLcdvbzk(String lcdvbzk) {
        this.lcdvbzk = lcdvbzk == null ? null : lcdvbzk.trim();
    }

    public String getZhankrq() {
        return zhankrq;
    }

    public void setZhankrq(String zhankrq) {
        this.zhankrq = zhankrq == null ? null : zhankrq.trim();
    }

    public String getLingjbh() {
        return lingjbh;
    }

    public void setLingjbh(String lingjbh) {
        this.lingjbh = lingjbh == null ? null : lingjbh.trim();
    }

    public String getChej() {
        return chej;
    }

    public void setChej(String chej) {
        this.chej = chej == null ? null : chej.trim();
    }

    public BigDecimal getShul() {
        return shul;
    }

    public void setShul(BigDecimal shul) {
        this.shul = shul;
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

    public String getBiaos() {
        return biaos;
    }

    public void setBiaos(String biaos) {
        this.biaos = biaos == null ? null : biaos.trim();
    }

    public String getYansbs() {
        return yansbs;
    }

    public void setYansbs(String yansbs) {
        this.yansbs = yansbs == null ? null : yansbs.trim();
    }

    public String getYezbs() {
        return yezbs;
    }

    public void setYezbs(String yezbs) {
        this.yezbs = yezbs == null ? null : yezbs.trim();
    }

    public String getUwjbs() {
        return uwjbs;
    }

    public void setUwjbs(String uwjbs) {
        this.uwjbs = uwjbs == null ? null : uwjbs.trim();
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

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor == null ? null : editor.trim();
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}
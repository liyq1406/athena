/**
 * time:2015.12.7
 * 
 * @author liuquan 
 * descreption:大线
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Dax {
    private BigDecimal paicfbq;//排产封闭期

    private BigDecimal chaifts;//拆分天数

    private String beiz;//备注

    private String biaos;//标识

    private String creator;//创建人

    private Date createTime;//创建时间

    private String editor;//修改人

    private Date editTime;//创建时间
    
    private String daxxh;//大线线号

    private String usercenter;//用户中心

    public String getDaxxh() {
        return daxxh;
    }

    public void setDaxxh(String daxxh) {
        this.daxxh = daxxh == null ? null : daxxh.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public BigDecimal getPaicfbq() {
        return paicfbq;
    }

    public void setPaicfbq(BigDecimal paicfbq) {
        this.paicfbq = paicfbq;
    }

    public BigDecimal getChaifts() {
        return chaifts;
    }

    public void setChaifts(BigDecimal chaifts) {
        this.chaifts = chaifts;
    }

    public String getBeiz() {
        return beiz;
    }

    public void setBeiz(String beiz) {
        this.beiz = beiz == null ? null : beiz.trim();
    }

    public String getBiaos() {
        return biaos;
    }

    public void setBiaos(String biaos) {
        this.biaos = biaos == null ? null : biaos.trim();
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
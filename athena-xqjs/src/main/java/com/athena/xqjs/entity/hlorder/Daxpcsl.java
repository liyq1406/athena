package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Daxpcsl {
    private BigDecimal jihsxl;//计划上线量

    private BigDecimal jihxxl;//计划下线量（用于排空）

    private String beiz;//备注

    private String creator;//创建人

    private Date createTime;//创建时间

    private String wenjmc;//文件名称

    private String wenjlj;//文件路径
    
    private String chej;//车间

    private String daxxh;//大线线号

    private Date riq;//日期

    private String usercenter;//用户中心
    
    private String editor;//修改人
    
    private Date editTime;//修改时间
    
    private BigDecimal jihsxlWithOutTransport;//计划上线量（不含转运）

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

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public BigDecimal getJihsxlWithOutTransport() {
		return jihsxlWithOutTransport;
	}

	public void setJihsxlWithOutTransport(BigDecimal jihsxlWithOutTransport) {
		this.jihsxlWithOutTransport = jihsxlWithOutTransport;
	}
    
}
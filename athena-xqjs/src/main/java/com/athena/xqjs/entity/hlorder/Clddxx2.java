/**
 * time:2015.12.7
 * @author liuquan
 * descreption:车辆上线计划	
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Clddxx2 {
    private String id;//id

    private String usercenter;//用户中心

    private String whof;//订单号

    private String lcdv24;//lcdv24

    private String yplbj;//预批量标记

    private Date yjsxsj;//预计上线时间

    private String scxh;//生产线

    private String yjsyhsj;//展开日期

    private BigDecimal sxsxh;//上线顺序号

    private String lcdv;//lcdv

    private String leix;//类型

    private Date cjDate;//创建时间

    private String clzt;//处理状态

    private String lcdvbzk;//半展开编码
    
    private Integer shul;//汇总数量
    
    private BigDecimal liush; 
    
    private String nooutflag; //没有转运出去的标记 0 已转运出去 1 没有转运出去
    
    private String outscxh; //转运处理前的产线

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

    public String getWhof() {
        return whof;
    }

    public void setWhof(String whof) {
        this.whof = whof == null ? null : whof.trim();
    }

    public String getLcdv24() {
        return lcdv24;
    }

    public void setLcdv24(String lcdv24) {
        this.lcdv24 = lcdv24 == null ? null : lcdv24.trim();
    }

    public String getYplbj() {
        return yplbj;
    }

    public void setYplbj(String yplbj) {
        this.yplbj = yplbj == null ? null : yplbj.trim();
    }

    public Date getYjsxsj() {
        return yjsxsj;
    }

    public void setYjsxsj(Date yjsxsj) {
        this.yjsxsj = yjsxsj;
    }

    public String getScxh() {
        return scxh;
    }

    public void setScxh(String scxh) {
        this.scxh = scxh == null ? null : scxh.trim();
    }

    public String getYjsyhsj() {
        return yjsyhsj;
    }

    public void setYjsyhsj(String yjsyhsj) {
        this.yjsyhsj = yjsyhsj == null ? null : yjsyhsj.trim();
    }

    public BigDecimal getSxsxh() {
        return sxsxh;
    }

    public void setSxsxh(BigDecimal sxsxh) {
        this.sxsxh = sxsxh;
    }

    public String getLcdv() {
        return lcdv;
    }

    public void setLcdv(String lcdv) {
        this.lcdv = lcdv == null ? null : lcdv.trim();
    }

    public String getLeix() {
        return leix;
    }

    public void setLeix(String leix) {
        this.leix = leix == null ? null : leix.trim();
    }

    public Date getCjDate() {
        return cjDate;
    }

    public void setCjDate(Date cjDate) {
        this.cjDate = cjDate;
    }

    public String getClzt() {
        return clzt;
    }

    public void setClzt(String clzt) {
        this.clzt = clzt == null ? null : clzt.trim();
    }

    public String getLcdvbzk() {
        return lcdvbzk;
    }

    public void setLcdvbzk(String lcdvbzk) {
        this.lcdvbzk = lcdvbzk == null ? null : lcdvbzk.trim();
    }

	public Integer getShul() {
		return shul;
	}

	public void setShul(Integer shul) {
		this.shul = shul;
	}

	public BigDecimal getLiush() {
		return liush;
	}

	public void setLiush(BigDecimal liush) {
		this.liush = liush;
	}

	public String getNooutflag() {
		return nooutflag;
	}

	public void setNooutflag(String nooutflag) {
		this.nooutflag = nooutflag;
	}

	public String getOutscxh() {
		return outscxh;
	}

	public void setOutscxh(String outscxh) {
		this.outscxh = outscxh;
	}
	
	
	
    
    
}
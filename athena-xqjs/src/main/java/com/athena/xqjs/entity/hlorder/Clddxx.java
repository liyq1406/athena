/**
 * 九天排产商业化时间
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;
import java.util.Date;

public class Clddxx {
	private String usercenter;//用户中心

	private String whof;//订单号
	    
    private String lcdv24;//整车版本号

    private String yplbj;//预批量标记

    private Date yjjhzrq;//预计进焊装日期

    private String yjjhzsx;//预计进焊装循序

    private String scxh;//生产线

    private Date yjjzlsj;//预计进总量时间

    private Date yjsyhsj;//预计商业化时间

    private BigDecimal sxsxh;//上线顺序号

    private String lcdv;//LCDV

    private Date cjDate;//附加字段 

    private String clzt;//处理状态

    private String wenjmc;//文件名称

    private String wenjlj;//文件路径

    private Date shangxsj;//上线时间

    private Date kaibsj;//开始时间

    private String hanzscx;//焊装生产线
    
    private String lcdvbzk;//半展开码

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

    public Date getYjjhzrq() {
        return yjjhzrq;
    }

    public void setYjjhzrq(Date yjjhzrq) {
        this.yjjhzrq = yjjhzrq;
    }

    public String getYjjhzsx() {
        return yjjhzsx;
    }

    public void setYjjhzsx(String yjjhzsx) {
        this.yjjhzsx = yjjhzsx == null ? null : yjjhzsx.trim();
    }

    public String getScxh() {
        return scxh;
    }

    public void setScxh(String scxh) {
        this.scxh = scxh == null ? null : scxh.trim();
    }

    public Date getYjjzlsj() {
        return yjjzlsj;
    }

    public void setYjjzlsj(Date yjjzlsj) {
        this.yjjzlsj = yjjzlsj;
    }

    public Date getYjsyhsj() {
        return yjsyhsj;
    }

    public void setYjsyhsj(Date yjsyhsj) {
        this.yjsyhsj = yjsyhsj;
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

    public Date getShangxsj() {
        return shangxsj;
    }

    public void setShangxsj(Date shangxsj) {
        this.shangxsj = shangxsj;
    }

    public Date getKaibsj() {
        return kaibsj;
    }

    public void setKaibsj(Date kaibsj) {
        this.kaibsj = kaibsj;
    }

    public String getHanzscx() {
        return hanzscx;
    }

    public void setHanzscx(String hanzscx) {
        this.hanzscx = hanzscx == null ? null : hanzscx.trim();
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

	public String getLcdvbzk() {
		return lcdvbzk;
	}

	public void setLcdvbzk(String lcdvbzk) {
		this.lcdvbzk = lcdvbzk == null ? null : lcdvbzk.trim();
	}
    
    
}
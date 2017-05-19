/**
 * time:2015.12.7
 * @author liuquan
 * descreption:车辆上线计划汇总表
 */
package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;

public class Clddxx2Tmp{
	private String id;//id
	
	private BigDecimal shul;//汇总数量

    private String yjsxsj;//总成号
    
    private String lcdv24;//大线总顺序（离线前）

    private String lcdvbzk;

    private String scxh;//生产线号

    private String usercenter;//用户中心

    private String yjsyhsj;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public String getScxh() {
        return scxh;
    }

    public void setScxh(String scxh) {
        this.scxh = scxh == null ? null : scxh.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getYjsyhsj() {
        return yjsyhsj;
    }

    public void setYjsyhsj(String yjsyhsj) {
        this.yjsyhsj = yjsyhsj == null ? null : yjsyhsj.trim();
    }

    public BigDecimal getShul() {
        return shul;
    }

    public void setShul(BigDecimal shul) {
        this.shul = shul;
    }

    public String getYjsxsj() {
        return yjsxsj;
    }

    public void setYjsxsj(String yjsxsj) {
        this.yjsxsj = yjsxsj == null ? null : yjsxsj.trim();
    }
}
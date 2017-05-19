package com.athena.xqjs.entity.ppl;
/**
 * 参考系计划员分组
 * @author Xiahui
 * @date 2012-1-11
 */
public class Manager {
	private static final long serialVersionUID = 1L;
	private String zuh;        //组号
	private String usercenter; //用户中心
	private String  zhizlx;    //制造路线
	private String  biaos;     //标识
	public String getZuh() {
		return zuh;
	}
	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
}

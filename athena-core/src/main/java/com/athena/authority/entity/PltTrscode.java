package com.athena.authority.entity;

public class PltTrscode {
	private String usercenter;		//用户中心
	private String trscode;			//交易码
	private String trsname;			//交易名称
	private String trstype;			//交易类型
	private long timeout;			//超时时间
	private String logflag;			//日志标志
	private String ctrlflag;		//控制标志
	private String service;			//服务名称
	private String commmode;		//通讯模式
	private String commtype;		//通讯方式
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getTrscode() {
		return trscode;
	}
	public void setTrscode(String trscode) {
		this.trscode = trscode;
	}
	public String getTrsname() {
		return trsname;
	}
	public void setTrsname(String trsname) {
		this.trsname = trsname;
	}
	public String getTrstype() {
		return trstype;
	}
	public void setTrstype(String trstype) {
		this.trstype = trstype;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public String getLogflag() {
		return logflag;
	}
	public void setLogflag(String logflag) {
		this.logflag = logflag;
	}
	public String getCtrlflag() {
		return ctrlflag;
	}
	public void setCtrlflag(String ctrlflag) {
		this.ctrlflag = ctrlflag;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getCommmode() {
		return commmode;
	}
	public void setCommmode(String commmode) {
		this.commmode = commmode;
	}
	public String getCommtype() {
		return commtype;
	}
	public void setCommtype(String commtype) {
		this.commtype = commtype;
	}
}

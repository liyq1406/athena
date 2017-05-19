package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 焊装分装线
 */
public class Fenzx extends PageableSupport{

	private static final long serialVersionUID = -6054770827737898022L;

	private String usercenter;	//用户中心
	
	private String fenzxh;		//分装线号
	
	private String daxxh;		//大线线号
	
	private String xiaohdbh;	//消耗点编号
	
	private Integer fenzxddxxlzcs;	//分装线到大线悬链总成数
	
	private Integer fenzxxs;		//分装线系数
	
	private String beijwxpxfs;	//备件外销排序方式
	
	private Integer shengcjp		;//生产节拍
	
	private Integer chews;			;//车位数
	
	private String biaos		;//标识
	
	private String creator		;//创建人
	
	private String create_time	;//创建时间
	
	private String editor		;//修改人
	
	private String edit_time	;//修改时间
	
	private Integer chessl		;//车身数量
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getXiaohdbh() {
		return xiaohdbh;
	}

	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public Integer getChessl() {
		return chessl;
	}

	public void setChessl(Integer chessl) {
		this.chessl = chessl;
	}

	public Integer getFenzxddxxlzcs() {
		return fenzxddxxlzcs;
	}

	public void setFenzxddxxlzcs(Integer fenzxddxxlzcs) {
		this.fenzxddxxlzcs = fenzxddxxlzcs;
	}

	public Integer getFenzxxs() {
		return fenzxxs;
	}

	public void setFenzxxs(Integer fenzxxs) {
		this.fenzxxs = fenzxxs;
	}

	public String getBeijwxpxfs() {
		return beijwxpxfs;
	}

	public void setBeijwxpxfs(String beijwxpxfs) {
		this.beijwxpxfs = beijwxpxfs;
	}

	public Integer getShengcjp() {
		return shengcjp;
	}

	public void setShengcjp(Integer shengcjp) {
		this.shengcjp = shengcjp;
	}

	public Integer getChews() {
		return chews;
	}

	public void setChews(Integer chews) {
		this.chews = chews;
	}

}

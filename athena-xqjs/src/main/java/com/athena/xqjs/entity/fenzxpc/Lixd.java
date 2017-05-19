package com.athena.xqjs.entity.fenzxpc;

import java.io.Serializable;

import com.toft.core3.support.PageableSupport;

/**
 * 离线点
 */
public class Lixd extends PageableSupport implements Comparable<Lixd>, Serializable{
	
	private static final long serialVersionUID = 3765089653353044600L;

	private String usercenter;	;//用户中心
	
	private String xianh		;//线号（大线/分装线）
	
	private String lixd			;//离线点
	
	private String leix			;//类型
	
	private String duiyxhd		;//对应消耗点
	
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

	public String getXianh() {
		return xianh;
	}

	public void setXianh(String xianh) {
		this.xianh = xianh;
	}

	public String getLixd() {
		return lixd;
	}

	public void setLixd(String lixd) {
		this.lixd = lixd;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

	public String getDuiyxhd() {
		return duiyxhd;
	}

	public void setDuiyxhd(String duiyxhd) {
		this.duiyxhd = duiyxhd;
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

	/**
	 * 按车身数量排序
	 */
	public int compareTo(Lixd o) {
		if(this.getChessl() < o.getChessl()){
			return 1;
		}else if(this.getChessl() > o.getChessl()){
			return -1;
		}else{
			return 0;
		}
	}

}

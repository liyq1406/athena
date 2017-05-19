package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 供应商-承运商-卸货站台
 * @author kong 
 *
 */
public class CkxGongysChengysXiehzt extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6790607714902341609L;
	private String usercenter;

	private String gcbh;
	private String xiehztbh;
	private Integer shengxpc;//生效频次
	private Integer jispc;//计算频次
	private Integer shijpc;//实际频次
	private Integer ches;//车数
	private Integer gongzsj;//工作时间
	private String jisrq;//计算日期
	private String biaos;
	private String creator;//创建人
	private String create_time;
	private String editor;
	private String edit_time;
	
	private String xiehztbzs;//根据用户的业务权限，查询有哪些有权限的卸货站台编组的集合
	
	
	
	
	public String getXiehztbzs() {
		return xiehztbzs;
	}
	public void setXiehztbzs(String xiehztbzs) {
		this.xiehztbzs = xiehztbzs;
	}
	public String getJisrq() {
		return jisrq;
	}
	public void setJisrq(String jisrq) {
		this.jisrq = jisrq;
	}
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return "";
	}
	public Integer getShengxpc() {
		return shengxpc;
	}
	public void setShengxpc(Integer shengxpc) {
		this.shengxpc = shengxpc;
	}
	public Integer getJispc() {
		return jispc;
	}
	public void setJispc(Integer jispc) {
		this.jispc = jispc;
	}
	public Integer getShijpc() {
		return shijpc;
	}
	public void setShijpc(Integer shijpc) {
		this.shijpc = shijpc;
	}
	public Integer getChes() {
		return ches;
	}
	public void setChes(Integer ches) {
		this.ches = ches;
	}
	public Integer getGongzsj() {
		return gongzsj;
	}
	public void setGongzsj(Integer gongzsj) {
		this.gongzsj = gongzsj;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	
}

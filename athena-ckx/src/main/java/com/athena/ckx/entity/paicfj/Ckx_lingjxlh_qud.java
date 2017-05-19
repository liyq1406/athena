package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_lingjxlh_qud extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	private String qid;
	
	public String getQid() {		
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}
	private String   usercenter       ;//用户中心
	 private String   lingjbh          ;//零件编号
	 private String   xulhqz           ;//序列号前缀编码
	 private String   xulhks           ;//序列号开始编码
	 private String   xulhjs           ;//序列号结束编码
	 private String   qisrq            ;//起始日期
	 private String   jiesrq           ;//结束日期
	 private String   zhidr            ;//制单人
	 private String   zhidsj           ;//制单时间
	 private String   biaos           ;//标识
	 private String   creator          ;//创建人
	 private Date   create_time      ;//创建时间
	 private String   editor           ;//修改人
	 private Date   edit_time        ;//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getXulhqz() {
		return xulhqz;
	}

	public void setXulhqz(String xulhqz) {
		this.xulhqz = xulhqz;
	}

	public String getXulhks() {
		return xulhks;
	}

	public void setXulhks(String xulhks) {
		this.xulhks = xulhks;
	}

	public String getXulhjs() {
		return xulhjs;
	}

	public void setXulhjs(String xulhjs) {
		this.xulhjs = xulhjs;
	}

	public String getQisrq() {
		return qisrq;
	}

	public void setQisrq(String qisrq) {
		this.qisrq = qisrq;
	}

	public String getJiesrq() {
		return jiesrq;
	}

	public void setJiesrq(String jiesrq) {
		this.jiesrq = jiesrq;
	}

	public String getZhidr() {
		return zhidr;
	}

	public void setZhidr(String zhidr) {
		this.zhidr = zhidr;
	}

	public String getZhidsj() {
		return zhidsj;
	}

	public void setZhidsj(String zhidsj) {
		this.zhidsj = zhidsj;
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

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}
	 

}

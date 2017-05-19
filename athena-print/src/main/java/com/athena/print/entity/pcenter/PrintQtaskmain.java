/**
 *
 */
package com.athena.print.entity.pcenter;


import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 作业打印队列主表
 * @author
// * @version
 * 
 */
public class PrintQtaskmain extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 5872962343484582255L;
	
	private String usercenter;//用户中心
	private String qid;//自动生成-作业编号
	private String saccount;//用户编号
	private String pgid;//打印机组编号
	private String storagecode;//目的仓库编号
	private String scode;//单据组编号
	private String sdevicecode;//打印机编号
	private String createtime;//任务创建时间
	private String finishedtime;//完成时间
	private String status;//作业状态
	private String createtimes;//任务创建时间
	private String finishedtimes;//完成时间
	private String seq;//作业编号
	
	
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCreatetimes() {
		return createtimes;
	}
	public void setCreatetimes(String createtimes) {
		this.createtimes = createtimes;
	}
	public String getFinishedtimes() {
		return finishedtimes;
	}
	public void setFinishedtimes(String finishedtimes) {
		this.finishedtimes = finishedtimes;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getSaccount() {
		return saccount;
	}
	public void setSaccount(String saccount) {
		this.saccount = saccount;
	}
	public String getPgid() {
		return pgid;
	}
	public void setPgid(String pgid) {
		this.pgid = pgid;
	}
	public String getStoragecode() {
		return storagecode;
	}
	public void setStoragecode(String storagecode) {
		this.storagecode = storagecode;
	}
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	
	public String getSdevicecode() {
		return sdevicecode;
	}
	public void setSdevicecode(String sdevicecode) {
		this.sdevicecode = sdevicecode;
	}
	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getFinishedtime() {
		return finishedtime;
	}
	public void setFinishedtime(String finishedtime) {
		this.finishedtime = finishedtime;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	//	public Integer getStatus() {
//		return status;
//	}
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
	public void setId(String id) {
		this.setQid(id);
		
	}
	public String getId() {
		return this.getQid();
	}
	
	
}
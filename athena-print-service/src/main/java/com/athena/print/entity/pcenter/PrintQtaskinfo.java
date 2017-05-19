/**
 *
 */
package com.athena.print.entity.pcenter;
import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 作业打印队列子表
 * @author
// * @version
 * 
 */
public class PrintQtaskinfo extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 5872962343484582255L;
	
	private String usercenter;//用户中心
	private String seq;//子任务编号
	private String qid;//自动生成-作业编号
	private String sfilename;//文件路径
	private String spars;//打印参数
	private String modelnumber;//模板编号
	private int printnumber;//打印份数
	private int printunitcount;//打印联数
	private String printtype; //打印样式
	private int status;//作业状态
	private String daypc;//打印批次 扩展用
	private String quyzs;//区域标识
	
	
	
	public String getDaypc() {
		return daypc;
	}
	public void setDaypc(String daypc) {
		this.daypc = daypc;
	}
	public String getQuyzs() {
		return quyzs;
	}
	public void setQuyzs(String quyzs) {
		this.quyzs = quyzs;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getSfilename() {
		return sfilename;
	}
	public void setSfilename(String sfilename) {
		this.sfilename = sfilename;
	}
	public String getSpars() {
		return spars;
	}
	public void setSpars(String spars) {
		this.spars = spars;
	}
	public String getModelnumber() {
		return modelnumber;
	}
	public void setModelnumber(String modelnumber) {
		this.modelnumber = modelnumber;
	}
	public int getPrintnumber() {
		return printnumber;
	}
	public void setPrintnumber(int printnumber) {
		this.printnumber = printnumber;
	}
	public int getPrintunitcount() {
		return printunitcount;
	}
	public void setPrintunitcount(int printunitcount) {
		this.printunitcount = printunitcount;
	}
	public String getPrinttype() {
		return printtype;
	}
	public void setPrinttype(String printtype) {
		this.printtype = printtype;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
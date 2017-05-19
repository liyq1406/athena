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
public class PrintQtasksheet extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 8395801344728987892L;
	
	private String  seq;//子任务编号
	private String qid;//自动生成-作业编号
	private Integer area;//区域
	private String spars;//打印数据
	
	
	
	public Integer getArea() {
		return area;
	}
	public void setArea(Integer area) {
		this.area = area;
	}
	public String getSpars() {
		return spars;
	}
	public void setSpars(String spars) {
		this.spars = spars;
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
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
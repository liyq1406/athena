package com.athena.pc.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 定义工时调整实体类
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-6-20
 */
public class LineTimeUpdate extends PageableSupport  implements Domain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String linegroup;      //产线组
	 
	private String usercenter;     //用户中心
	
	private String lineNum;        //产线号
	
	private BigDecimal workTime;   //工时
	
	private String startTime;        //开始时间
	
	private String endTime;	       //结束时间
	
	private String editor;         //编辑人
	
	private Date editorDate;       //编辑日期
	
	private String zhoux;          //周序
	
	private String biaos;          //标识，'Y'月模拟，'G'滚动月模拟
	
	private String xingq;          //星期
	
	private String gongzbh;       //工作编号
	
	public String getGongzbh() {
		return gongzbh;
	}
	public void setGongzbh(String gongzbh) {
		this.gongzbh = gongzbh;
	}
	public String getXingq() {
		return xingq;
	}
	public void setXingq(String xingq) {
		this.xingq = xingq;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getZhoux() {
		return zhoux;
	}
	public void setZhoux(String zhoux) {
		this.zhoux = zhoux;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEditorDate() {
		return editorDate;
	}
	public void setEditorDate(Date editorDate) {
		this.editorDate = editorDate;
	}
	public String getLinegroup() {
		return linegroup;
	}
	public void setLinegroup(String linegroup) {
		this.linegroup = linegroup;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLineNum() {
		return lineNum;
	}
	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	} 
	
	public BigDecimal getWorkTime() {
		return workTime;
	}
	public void setWorkTime(BigDecimal workTime) {
		this.workTime = workTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public void setId(String id) {
		
	}
	@Override
	public String getId() {
		
		return "";
	}
	
}

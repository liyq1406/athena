package com.athena.truck.entity;


/**
 * PDA卡车相关
 * @author CSY
 *
 */
public class PDATruck {

	private String trscode;			//交易码
	private String response;		//相应码
	private String respdesc;		//信息
	private Object parameter;	//值
	private String trans_bran_code;	//用户中心
	private String oper_no;			//操作员
	private String seqno;			//
	private String host_seqno;		//
	private String trsdate;			//操作日期
	private String buflen;			//缓冲区
	private String total_page;		//总页数
	private String op_name;			//操作名
	
	public String getOp_name() {
		return op_name;
	}
	public void setOp_name(String op_name) {
		this.op_name = op_name;
	}
	public String getRespdesc() {
		return respdesc;
	}
	public void setRespdesc(String respdesc) {
		this.respdesc = respdesc;
	}
	public String getTrscode() {
		return trscode;
	}
	public void setTrscode(String trscode) {
		this.trscode = trscode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public String getTrans_bran_code() {
		return trans_bran_code;
	}
	public void setTrans_bran_code(String trans_bran_code) {
		this.trans_bran_code = trans_bran_code;
	}
	public String getOper_no() {
		return oper_no;
	}
	public void setOper_no(String oper_no) {
		this.oper_no = oper_no;
	}
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getHost_seqno() {
		return host_seqno;
	}
	public void setHost_seqno(String host_seqno) {
		this.host_seqno = host_seqno;
	}
	public String getTrsdate() {
		return trsdate;
	}
	public void setTrsdate(String trsdate) {
		this.trsdate = trsdate;
	}
	public String getBuflen() {
		return buflen;
	}
	public void setBuflen(String buflen) {
		this.buflen = buflen;
	}
	public String getTotal_page() {
		return total_page;
	}
	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}
	
}

package com.athena.util.athenalog;
public class SysLog {	
	private String  cid;
	private String  usercenter;
	private String  operators;
	private String  operators_ip;
	private String  trans_content;
	private String  trans_desc;
	private String  trans_url;
	private String  cflag;
	private String  clevel;
	private String  cclass;
	private String  cexception;
	private String  create_time;
	private String  module_name;
	private String  process_name;
	/**
	 * 日志类叙述标准化
	 */
	public String toString(){		
		StringBuffer sb = new StringBuffer();
		sb.append("Here is the Businese Log {")
		  .append("process_name:["+process_name+"]")
		  .append("usercenter:["+usercenter+"]")
		  .append("operators:["+operators+"]")
		  .append("use ip:["+operators_ip+"]")
		  .append("to control ")
		  .append("module_name:["+module_name+"]")
		  .append("title:["+trans_desc+"]")
		  .append("content:["+trans_content+"]")
		  .append("by url:["+trans_url+"]")
		  .append("on time:["+create_time+"]")
		  .append("this log's clevel:["+clevel+"]")
		  .append("cflag:["+cflag+"]")
		  .append("may be have exception:["+cexception+"]")
		  .append("in address:["+cclass+"] } ");		
		return sb.toString();
	}
	
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	/**
	 * @return the usercenter
	 */
	public String getUsercenter() {
		return usercenter;
	}
	/**
	 * @param usercenter the usercenter to set
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	/**
	 * @return the operators
	 */
	public String getOperators() {
		return operators;
	}
	/**
	 * @param operators the operators to set
	 */
	public void setOperators(String operators) {
		this.operators = operators;
	}
	/**
	 * @return the operators_ip
	 */
	public String getOperators_ip() {
		return operators_ip;
	}
	/**
	 * @param operators_ip the operators_ip to set
	 */
	public void setOperators_ip(String operators_ip) {
		this.operators_ip = operators_ip;
	}

	/**
	 * @return the trans_content
	 */
	public String getTrans_content() {
		return trans_content;
	}
	/**
	 * @param trans_content the trans_content to set
	 */
	public void setTrans_content(String trans_content) {
		this.trans_content = trans_content;
	}
	/**
	 * @return the trans_desc
	 */
	public String getTrans_desc() {
		return trans_desc;
	}
	/**
	 * @param trans_desc the trans_desc to set
	 */
	public void setTrans_desc(String trans_desc) {
		this.trans_desc = trans_desc;
	}
	/**
	 * @return the trans_url
	 */
	public String getTrans_url() {
		return trans_url;
	}
	/**
	 * @param trans_url the trans_url to set
	 */
	public void setTrans_url(String trans_url) {
		this.trans_url = trans_url;
	}
	/**
	 * @return the cflag
	 */
	public String getCflag() {
		return cflag;
	}
	/**
	 * @param cflag the cflag to set
	 */
	public void setCflag(String cflag) {
		this.cflag = cflag;
	}
	/**
	 * @return the clevel
	 */
	public String getClevel() {
		return clevel;
	}
	/**
	 * @param clevel the clevel to set
	 */
	public void setClevel(String clevel) {
		this.clevel = clevel;
	}
	/**
	 * @return the cclass
	 */
	public String getCclass() {
		return cclass;
	}
	/**
	 * @param cclass the cclass to set
	 */
	public void setCclass(String cclass) {
		this.cclass = cclass;
	}
	/**
	 * @return the cexception
	 */
	public String getCexception() {
		return cexception;
	}
	/**
	 * @param cexception the cexception to set
	 */
	public void setCexception(String cexception) {
		this.cexception = cexception;
	}
	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}
	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	/**
	 * @return the module_code
	 */
	public String getModule_name() {
		return module_name;
	}

	/**
	 * @param module_code the module_code to set
	 */
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	/**
	 * @return the process_name
	 */
	public String getProcess_name() {
		return process_name;
	}

	/**
	 * @param process_name the process_name to set
	 */
	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}

}

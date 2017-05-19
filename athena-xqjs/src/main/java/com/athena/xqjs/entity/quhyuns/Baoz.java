package com.athena.xqjs.entity.quhyuns;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Baoz extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String baozlx;		//包装类型
	
	private String baozmc;		//包装名称
	
	private Integer changd;		//长度
	
	private Integer kuand;		//高度
	
	private Integer gaod;		//高度
	
	private Double baozzl;		//包装质量
	
	private String caiz;		//材质
	
	private String shifhs;		//是否回收
	
	private String leib;		//类别
	
	private Integer zhedgd;		//折叠高度
	
	private Integer duidcs;		//堆垛层数
	
	private String baiffx;		//摆放方向
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	public String getBaozlx() {
		return baozlx;
	}
	
	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}
	
	public String getBaozmc() {
		return baozmc;
	}
	
	public void setBaozmc(String baozmc) {
		this.baozmc = baozmc;
	}
	
	public Integer getChangd() {
		return changd;
	}
	
	public void setChangd(Integer changd) {
		this.changd = changd;
	}
	
	public Integer getKuand() {
		return kuand;
	}
	
	public void setKuand(Integer kuand) {
		this.kuand = kuand;
	}
	
	public Integer getGaod() {
		return gaod;
	}
	
	public void setGaod(Integer gaod) {
		this.gaod = gaod;
	}
	
	
	public Double getBaozzl() {
		return baozzl;
	}

	public void setBaozzl(Double baozzl) {
		this.baozzl = baozzl;
	}

	public String getCaiz() {
		return caiz;
	}
	
	public void setCaiz(String caiz) {
		this.caiz = caiz;
	}
	
	public String getShifhs() {
		return shifhs;
	}
	
	public void setShifkhs(String shifhs) {
		this.shifhs = shifhs;
	}
	
	public String getLeib() {
		return leib;
	}
	
	public void setLeib(String leib) {
		this.leib = leib;
	}
	
	public Integer getZhedgd() {
		return zhedgd;
	}
	
	public void setZhedgd(Integer zhedgd) {
		this.zhedgd = zhedgd;
	}
	
	public Integer getDuidcs() {
		return duidcs;
	}
	
	public void setDuidcs(Integer duidcs) {
		this.duidcs = duidcs;
	}
	
	public String getBaiffx() {
		return baiffx;
	}
	
	public void setBanffx(String baiffx) {
		this.baiffx = baiffx;
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

	public void setShifhs(String shifhs) {
		this.shifhs = shifhs;
	}

	public void setBaiffx(String baiffx) {
		this.baiffx = baiffx;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}

package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Gongcy extends PageableSupport implements Domain {

	private static final long serialVersionUID = 5476335191608570513L;
	
	private String usercenter        ;//用户中心
	 
	private String gcbh              ;//供应商编号|承运商编号|运输商编号
	 
	private String leix              ;//类型（内部供应商|外部供应商|承运商|运输商）(1|2|3|4)
	 
	private String gonghlx           ;//供货类型（制造路线）
	 
	private String gongsmc           ;//公司名称
	 
	private String diz               ;//地址
	 
	private String youb              ;//邮编
	 
	private String lianxr            ;//联系人
	 
	private String dianh             ;//电话
	 
	private String chuanz            ;//传真
	 
	private Double beihzq            ;//备货周期
	 
	private Double fayzq             ;//发运周期
	 
	private Double kacbztj           ;//卡车标准体积
	 
	private Integer songhzdpc         ;//每天送货最大频次
	 
	private Integer songhzxpc         ;//每天送货最小频次
	 
	private String neibgys_cangkbh   ;//内部供应商对应仓库编号
	 
	private Double edzzl             ;//额定装载率
	 
	private Double bodfdxs           ;//波动放大系数
	
	private String fayd				 ;//发运地
	
	private String neibyhzx			 ;//内部供应商用户中心
	 
	private String biaos			 ;//标识
	
	private String creator           ;//创建人
	 
	private String create_time       ;//创建时间
	 
	private String editor            ;//修改人
	 
	private String edit_time         ;//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getGcbh() {
		return gcbh;
	}

	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

	public String getGonghlx() {
		return gonghlx;
	}

	public void setGonghlx(String gonghlx) {
		this.gonghlx = gonghlx;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getDiz() {
		return diz;
	}

	public void setDiz(String diz) {
		this.diz = diz;
	}

	public String getYoub() {
		return youb;
	}

	public void setYoub(String youb) {
		this.youb = youb;
	}

	public String getLianxr() {
		return lianxr;
	}

	public void setLianxr(String lianxr) {
		this.lianxr = lianxr;
	}

	public String getDianh() {
		return dianh;
	}

	public void setDianh(String dianh) {
		this.dianh = dianh;
	}

	public String getChuanz() {
		return chuanz;
	}

	public void setChuanz(String chuanz) {
		this.chuanz = chuanz;
	}

	public Double getBeihzq() {
		return beihzq;
	}

	public void setBeihzq(Double beihzq) {
		this.beihzq = beihzq;
	}

	public Double getFayzq() {
		return fayzq;
	}

	public void setFayzq(Double fayzq) {
		this.fayzq = fayzq;
	}

	public Double getKacbztj() {
		return kacbztj;
	}

	public void setKacbztj(Double kacbztj) {
		this.kacbztj = kacbztj;
	}

	public Integer getSonghzdpc() {
		return songhzdpc;
	}

	public void setSonghzdpc(Integer songhzdpc) {
		this.songhzdpc = songhzdpc;
	}

	public Integer getSonghzxpc() {
		return songhzxpc;
	}

	public void setSonghzxpc(Integer songhzxpc) {
		this.songhzxpc = songhzxpc;
	}

	public String getNeibgys_cangkbh() {
		return neibgys_cangkbh;
	}

	public void setNeibgys_cangkbh(String neibgys_cangkbh) {
		this.neibgys_cangkbh = neibgys_cangkbh;
	}

	public Double getEdzzl() {
		return edzzl;
	}

	public void setEdzzl(Double edzzl) {
		this.edzzl = edzzl;
	}

	public Double getBodfdxs() {
		return bodfdxs;
	}

	public void setBodfdxs(Double bodfdxs) {
		this.bodfdxs = bodfdxs;
	}
	
	 public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	
	public void setFayd(String fayd) {
		this.fayd = fayd;
	}

	public String getFayd() {
		return fayd;
	}

	public void setNeibyhzx(String neibyhzx) {
		this.neibyhzx = neibyhzx;
	}

	public String getNeibyhzx() {
		return neibyhzx;
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

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}
	 


}

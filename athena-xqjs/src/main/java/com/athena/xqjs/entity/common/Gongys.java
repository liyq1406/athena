/**
 *
 */
package com.athena.xqjs.entity.common;
import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 供应商/承运商/运输商
 * @author
 * @version
 * 
 */
public class Gongys extends PageableSupport{
	
	private static final long serialVersionUID = 6807755220652558230L;
	
	private String usercenter;//用户中心
	private String gongsmc;//公司名称
	private String chuanz;//传真
	private String neibgysCangkbh;//内部供应商对应仓库编号
	private String diz;//地址
	private BigDecimal fayzq;//发运周期
	private String songhzdpc;//每天送货最大频次
	private String gonghlx;//供货类型（制造路线）
	private String leix;//类型（内部供应商/外部供应商/承运商/运输商）
	private String dianh;//电话
	private String youb;//邮编
	private String gcbh;//供应商编号/承运商编号/运输商编号
	private String lianxr;//联系人
	private String kacbztj;//卡车标准体积
	private BigDecimal beihzq;//备货周期
	private String songhzxpc;//每天送货最小频次
	private String fayd;// 运输商发运地
	private String neibyhzx ;
	
	public String getNeibyhzx() {
		return neibyhzx;
	}

	public void setNeibyhzx(String neibyhzx) {
		this.neibyhzx = neibyhzx;
	}

	public BigDecimal getFayzq() {
		return fayzq;
	}

	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}

	public String getGonghlx() {
		return gonghlx;
	}

	public void setGonghlx(String gonghlx) {
		this.gonghlx = gonghlx;
	}

	public BigDecimal getBeihzq() {
		return beihzq;
	}

	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}

	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	public String getGongsmc(){
		return this.gongsmc;
	}
	
	public void setGongsmc(String gongsmc){
		this.gongsmc = gongsmc;
	}
	public String getChuanz(){
		return this.chuanz;
	}
	
	public void setChuanz(String chuanz){
		this.chuanz = chuanz;
	}
	public String getNeibgysCangkbh(){
		return this.neibgysCangkbh;
	}
	
	public void setNeibgysCangkbh(String neibgysCangkbh){
		this.neibgysCangkbh = neibgysCangkbh;
	}
	public String getDiz(){
		return this.diz;
	}
	
	public void setDiz(String diz){
		this.diz = diz;
	}
	public String getSonghzdpc(){
		return this.songhzdpc;
	}
	
	public void setSonghzdpc(String songhzdpc){
		this.songhzdpc = songhzdpc;
	}

	public String getLeix(){
		return this.leix;
	}
	
	public void setLeix(String leix){
		this.leix = leix;
	}
	public String getDianh(){
		return this.dianh;
	}
	
	public void setDianh(String dianh){
		this.dianh = dianh;
	}
	public String getYoub(){
		return this.youb;
	}
	
	public void setYoub(String youb){
		this.youb = youb;
	}
	public String getGcbh(){
		return this.gcbh;
	}
	
	public void setGcbh(String gcbh){
		this.gcbh = gcbh;
	}

	public String getFayd() {
		return fayd;
	}

	public void setFayd(String fayd) {
		this.fayd = fayd;
	}

	public String getLianxr(){
		return this.lianxr;
	}
	
	public void setLianxr(String lianxr){
		this.lianxr = lianxr;
	}
	public String getKacbztj(){
		return this.kacbztj;
	}
	
	public void setKacbztj(String kacbztj){
		this.kacbztj = kacbztj;
	}

	public String getSonghzxpc(){
		return this.songhzxpc;
	}
	
	public void setSonghzxpc(String songhzxpc){
		this.songhzxpc = songhzxpc;
	}
	
}
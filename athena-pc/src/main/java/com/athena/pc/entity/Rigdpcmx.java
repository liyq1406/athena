package com.athena.pc.entity;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:日滚动排产计划明细实体类
 * </p>
 * <p>
 * Description:定义日滚动排产计划明细实体变量
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-3-8
 */
public class Rigdpcmx extends PageableSupport  implements Domain{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -3618893970564209029L;

	private String usercenter;    //用户中心
	private String chanxh;        //产线号
	private String lingjbh;       //零件编号
	private String shij;          //时间
	private String zhuangt;       //状态
	private BigDecimal lingjsl;   //零件数量
	private BigDecimal shijscsl;  //实际生产数量
	private String editor;        //修改人
	private String edit_time;     //修改时间
	private String creator;       //创建人
	private String create_time;   //创建时间
	private BigDecimal currbalance;//当日差额
	private BigDecimal ban1;   //BAN1
	private BigDecimal ban2;   //BAN1
	private BigDecimal ban3;   //BAN1
	private BigDecimal ban4;   //BAN1
	private BigDecimal ban5;   //BAN1
	private BigDecimal ban6;   //BAN1
	private BigDecimal ban7;   //BAN1
	private BigDecimal ban8;   //BAN1
	private BigDecimal ban9;   //BAN1
	private BigDecimal ban10;  //BAN1
	
	private String banc1;   //BAN1
	private String banc2;   //BAN1
	private String banc3;   //BAN1
	private String banc4;   //BAN1
	private String banc5;   //BAN1
	private String banc6;   //BAN1
	private String banc7;   //BAN1
	private String banc8;   //BAN1
	private String banc9;   //BAN1
	private String banc10;  //BAN1
	private ArrayList<String> ban;  //班
/*	
	public String getUSERCENTER() {
		return usercenter;
	}

	public void setUSERCENTER(String uSERCENTER) {
		usercenter = uSERCENTER;
	}

	public String getCHANXH() {
		return chanxh;
	}

	public void setCHANXH(String cHANXH) {
		chanxh = cHANXH;
	}

	public String getLINGJBH() {
		return lingjbh;
	}

	public void setLINGJBH(String lINGJBH) {
		lingjbh = lINGJBH;
	}

	public String getSHIJ() {
		return shij;
	}

	public void setSHIJ(String sHIJ) {
		shij = sHIJ;
	}

	public String getZHUANGT() {
		return zhuangt;
	}

	public void setZHUANGT(String zHUANGT) {
		zhuangt = zHUANGT;
	}

	public BigDecimal getLINGJSL() {
		return lingjsl;
	}

	public void setLINGJSL(BigDecimal lINGJSL) {
		lingjsl = lINGJSL;
	}

	public BigDecimal getSHIJSCSL() {
		return shijscsl;
	}

	public void setSHIJSCSL(BigDecimal sHIJSCSL) {
		shijscsl = sHIJSCSL;
	}

	public String getEDITOR() {
		return editor;
	}

	public void setEDITOR(String eDITOR) {
		editor = eDITOR;
	}

	public String getEDIT_TIME() {
		return edit_time;
	}

	public void setEDIT_TIME(String eDIT_TIME) {
		edit_time = eDIT_TIME;
	}

	public String getCREATOR() {
		return creator;
	}

	public void setCREATOR(String cREATOR) {
		creator = cREATOR;
	}

	public String getCREATE_TIME() {
		return create_time;
	}

	public void setCREATE_TIME(String cREATE_TIME) {
		create_time = cREATE_TIME;
	}

	public BigDecimal getBAN1() {
		return ban1;
	}

	public void setBAN1(BigDecimal bAN1) {
		ban1 = bAN1;
	}

	public BigDecimal getBAN2() {
		return ban2;
	}

	public void setBAN2(BigDecimal bAN2) {
		ban2 = bAN2;
	}

	public BigDecimal getBAN3() {
		return ban3;
	}

	public void setBAN3(BigDecimal bAN3) {
		ban3 = bAN3;
	}

	public BigDecimal getBAN4() {
		return ban4;
	}

	public void setBAN4(BigDecimal bAN4) {
		ban4 = bAN4;
	}

	public BigDecimal getBAN5() {
		return ban5;
	}

	public void setBAN5(BigDecimal bAN5) {
		ban5 = bAN5;
	}

	public BigDecimal getBAN6() {
		return ban6;
	}

	public void setBAN6(BigDecimal bAN6) {
		ban6 = bAN6;
	}

	public BigDecimal getBAN7() {
		return ban7;
	}

	public void setBAN7(BigDecimal bAN7) {
		ban7 = bAN7;
	}

	public BigDecimal getBAN8() {
		return ban8;
	}

	public void setBAN8(BigDecimal bAN8) {
		ban8 = bAN8;
	}

	public BigDecimal getBAN9() {
		return ban9;
	}

	public void setBAN9(BigDecimal bAN9) {
		ban9 = bAN9;
	}

	public BigDecimal getBAN10() {
		return ban10;
	}

	public void setBAN10(BigDecimal bAN10) {
		ban10 = bAN10;
	}

	public String getBANC1() {
		return banc1;
	}

	public void setBANC1(String bANC1) {
		banc1 = bANC1;
	}

	public String getBANC2() {
		return banc2;
	}

	public void setBANC2(String bANC2) {
		banc2 = bANC2;
	}

	public String getBANC3() {
		return banc3;
	}

	public void setBANC3(String bANC3) {
		banc3 = bANC3;
	}

	public String getBANC4() {
		return banc4;
	}

	public void setBANC4(String bANC4) {
		banc4 = bANC4;
	}

	public String getBANC5() {
		return banc5;
	}

	public void setBANC5(String bANC5) {
		banc5 = bANC5;
	}

	public String getBANC6() {
		return banc6;
	}

	public void setBANC6(String bANC6) {
		banc6 = bANC6;
	}

	public String getBANC7() {
		return banc7;
	}

	public void setBANC7(String bANC7) {
		banc7 = bANC7;
	}

	public String getBANC8() {
		return banc8;
	}

	public void setBANC8(String bANC8) {
		banc8 = bANC8;
	}

	public String getBANC9() {
		return banc9;
	}

	public void setBANC9(String bANC9) {
		banc9 = bANC9;
	}

	public String getBANC10() {
		return banc10;
	}

	public void setBANC10(String bANC10) {
		banc10 = bANC10;
	}

	public ArrayList<String> getBan() {
		return ban;
	}

	public void setBan(ArrayList<String> ban) {
		this.ban = ban;
	}
*/
	public void setId(String id) {
		
	}

	public String getId() {
		
		return null;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanxh() {
		return chanxh;
	}

	public void setChanxh(String chanxh) {
		this.chanxh = chanxh;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getShij() {
		return shij;
	}

	public void setShij(String shij) {
		this.shij = shij;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public BigDecimal getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}

	public BigDecimal getShijscsl() {
		return shijscsl;
	}

	public void setShijscsl(BigDecimal shijscsl) {
		this.shijscsl = shijscsl;
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

	public BigDecimal getBan1() {
		return ban1;
	}

	public void setBan1(BigDecimal ban1) {
		this.ban1 = ban1;
	}

	public BigDecimal getBan2() {
		return ban2;
	}

	public void setBan2(BigDecimal ban2) {
		this.ban2 = ban2;
	}

	public BigDecimal getBan3() {
		return ban3;
	}

	public void setBan3(BigDecimal ban3) {
		this.ban3 = ban3;
	}

	public BigDecimal getBan4() {
		return ban4;
	}

	public void setBan4(BigDecimal ban4) {
		this.ban4 = ban4;
	}

	public BigDecimal getBan5() {
		return ban5;
	}

	public void setBan5(BigDecimal ban5) {
		this.ban5 = ban5;
	}

	public BigDecimal getBan6() {
		return ban6;
	}

	public void setBan6(BigDecimal ban6) {
		this.ban6 = ban6;
	}

	public BigDecimal getBan7() {
		return ban7;
	}

	public void setBan7(BigDecimal ban7) {
		this.ban7 = ban7;
	}

	public BigDecimal getBan8() {
		return ban8;
	}

	public void setBan8(BigDecimal ban8) {
		this.ban8 = ban8;
	}

	public BigDecimal getBan9() {
		return ban9;
	}

	public void setBan9(BigDecimal ban9) {
		this.ban9 = ban9;
	}

	public BigDecimal getBan10() {
		return ban10;
	}

	public void setBan10(BigDecimal ban10) {
		this.ban10 = ban10;
	}

	public String getBanc1() {
		return banc1;
	}

	public void setBanc1(String banc1) {
		this.banc1 = banc1;
	}

	public String getBanc2() {
		return banc2;
	}

	public void setBanc2(String banc2) {
		this.banc2 = banc2;
	}

	public String getBanc3() {
		return banc3;
	}

	public void setBanc3(String banc3) {
		this.banc3 = banc3;
	}

	public String getBanc4() {
		return banc4;
	}

	public void setBanc4(String banc4) {
		this.banc4 = banc4;
	}

	public String getBanc5() {
		return banc5;
	}

	public void setBanc5(String banc5) {
		this.banc5 = banc5;
	}

	public String getBanc6() {
		return banc6;
	}

	public void setBanc6(String banc6) {
		this.banc6 = banc6;
	}

	public String getBanc7() {
		return banc7;
	}

	public void setBanc7(String banc7) {
		this.banc7 = banc7;
	}

	public String getBanc8() {
		return banc8;
	}

	public void setBanc8(String banc8) {
		this.banc8 = banc8;
	}

	public String getBanc9() {
		return banc9;
	}

	public void setBanc9(String banc9) {
		this.banc9 = banc9;
	}

	public String getBanc10() {
		return banc10;
	}

	public void setBanc10(String banc10) {
		this.banc10 = banc10;
	}

	public ArrayList<String> getBan() {
		return ban;
	}

	public void setBan(ArrayList<String> ban) {
		this.ban = ban;
	}

	public BigDecimal getCurrbalance() {
		return currbalance;
	}

	public void setCurrbalance(BigDecimal currbalance) {
		this.currbalance = currbalance;
	}

}

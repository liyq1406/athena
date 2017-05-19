package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Lingjck extends PageableSupport implements Domain{

		private static final long serialVersionUID = 1234523452345234444L;
		
		private String usercenter      ;//用户中心
		
		private String lingjbh         ;//零件编号
		
		private String cangkbh         ;//仓库编号
		
		private String zickbh          ;//子仓库编号
		
		private String xiehztbh        ;//卸货站台编号
		
		private Double anqkcts         ;//安全库存天数
		
		private Double anqkcsl         ;//安全库存数量
		
		private Double zuidkcts        ;//最大库存天数
		
		private Double zuidkcsl        ;//最大库存数量
		
		private Double dingdbdzl       ;//订单表达总量
		
		private Double dingdzzzl	   ;//订单终止总量
		
		private Double yijfzl          ;//已交付总量
		
		private Double xittzz          ;//系统调整值(年末 表达总量-交付总量)
		
		private String danqkbh         ;//单取库编号
		
		private String danqkw          ;//单取库位编号(面列层010001/010101)
		
		private Integer zuidsx         ;//单取库位最大上限(包装个数)
		
		private Integer zuixxx         ;//单取库位最小下限(包装个数)
		
		private Double jistzz          ;//计算调整数值
		
		private String shengxsj        ;//生效时间
		
		private Double zuixqdl         ;//最小起订量(仓库)
		
		private String shifxlh         ;//是否有零件序列号
		
		private String beiykbh         ;//备用子仓库编号
		
		private String dingzkw         ;//定置库位
		
		private String usbzlx          ;//仓库US的包装类型
		
		private Double usbzrl          ;//仓库US的包装容量
		
		private String uclx            ;//上线UC类型
		
		private Double ucrl            ;//上线UC容量
		
		private String fifo			   ;//FIFO
		
		private String yuanckbh;		//原仓库编号
		
		private String zidfhbz		   ;//是否自动发送
		
		private String creator		   ;//增加人
		
		private String create_time	   ;//增加时间
		
		private String editor          ;//修改人
		
		private String edit_time	   ;//修改时间
		
		private String zuh			   ;//组号
		
		private String wulgyyz		   ;//物流工艺员组代码
		
		private String yuanxiehztbh    ;//原卸货站台编号
		
		
		public String getYuanxiehztbh() {
			return yuanxiehztbh;
		}
		public void setYuanxiehztbh(String yuanxiehztbh) {
			this.yuanxiehztbh = yuanxiehztbh;
		}
		public void setZuh(String zuh) {
			this.zuh = zuh;
		}
		public String getZuh() {
			return zuh;
		}
		
		public String getUsercenter() {
			return usercenter;
		}
		public void setUsercenter(String usercenter) {
			this.usercenter = usercenter;
		}
		public String getLingjbh() {
			return lingjbh;
		}
		public void setLingjbh(String lingjbh) {
			this.lingjbh = lingjbh;
		}
		public String getCangkbh() {
			return cangkbh;
		}
		public void setCangkbh(String cangkbh) {
			this.cangkbh = cangkbh;
		}
		public String getZickbh() {
			return zickbh;
		}
		public void setZickbh(String zickbh) {
			this.zickbh = zickbh;
		}
		public Double getAnqkcts() {
			return anqkcts;
		}
		public void setAnqkcts(Double anqkcts) {
			this.anqkcts = anqkcts;
		}
		public Double getAnqkcsl() {
			return anqkcsl;
		}
		public void setAnqkcsl(Double anqkcsl) {
			this.anqkcsl = anqkcsl;
		}
		public Double getZuidkcts() {
			return zuidkcts;
		}
		public void setZuidkcts(Double zuidkcts) {
			this.zuidkcts = zuidkcts;
		}
		public Double getZuidkcsl() {
			return zuidkcsl;
		}
		public void setZuidkcsl(Double zuidkcsl) {
			this.zuidkcsl = zuidkcsl;
		}
		public Double getDingdbdzl() {
			return dingdbdzl;
		}
		public void setDingdbdzl(Double dingdbdzl) {
			this.dingdbdzl = dingdbdzl;
		}
		public Double getYijfzl() {
			return yijfzl;
		}
		public void setYijfzl(Double yijfzl) {
			this.yijfzl = yijfzl;
		}
		public Double getXittzz() {
			return xittzz;
		}
		public void setXittzz(Double xittzz) {
			this.xittzz = xittzz;
		}
		public String getDanqkbh() {
			return danqkbh;
		}
		public void setDanqkbh(String danqkbh) {
			this.danqkbh = danqkbh;
		}
		public String getDanqkw() {
			return danqkw;
		}
		public void setDanqkw(String danqkw) {
			this.danqkw = danqkw;
		}
		public Integer getZuidsx() {
			return zuidsx;
		}
		public void setZuidsx(Integer zuidsx) {
			this.zuidsx = zuidsx;
		}
		public Integer getZuixxx() {
			return zuixxx;
		}
		public void setZuixxx(Integer zuixxx) {
			this.zuixxx = zuixxx;
		}
		public Double getJistzz() {
			return jistzz;
		}
		public void setJistzz(Double jistzz) {
			this.jistzz = jistzz;
		}
		public String getShengxsj() {
			return shengxsj;
		}
		public void setShengxsj(String shengxsj) {
			this.shengxsj = shengxsj;
		}
		public Double getZuixqdl() {
			return zuixqdl;
		}
		public void setZuixqdl(Double zuixqdl) {
			this.zuixqdl = zuixqdl;
		}
		public String getShifxlh() {
			return shifxlh;
		}
		public void setShifxlh(String shifxlh) {
			this.shifxlh = shifxlh;
		}
		public String getBeiykbh() {
			return beiykbh;
		}
		public void setBeiykbh(String beiykbh) {
			this.beiykbh = beiykbh;
		}
		public String getDingzkw() {
			return dingzkw;
		}
		public void setDingzkw(String dingzkw) {
			this.dingzkw = dingzkw;
		}
		public String getUsbzlx() {
			return usbzlx;
		}
		public void setUsbzlx(String usbzlx) {
			this.usbzlx = usbzlx;
		}
		public Double getUsbzrl() {
			return usbzrl;
		}
		public void setUsbzrl(Double usbzrl) {
			this.usbzrl = usbzrl;
		}
		public String getUclx() {
			return uclx;
		}
		public void setUclx(String uclx) {
			this.uclx = uclx;
		}
		public Double getUcrl() {
			return ucrl;
		}
		public void setUcrl(Double ucrl) {
			this.ucrl = ucrl;
		}
		
		public String getXiehztbh() {
			return xiehztbh;
		}
		public String getFifo() {
			return fifo;
		}
		public void setZidfhbz(String zidfhbz) {
			this.zidfhbz = zidfhbz;
		}
		public String getZidfhbz() {
			return zidfhbz;
		}
		public String getCreator() {
			return creator;
		}
		public String getCreate_time() {
			return create_time;
		}
		public String getEditor() {
			return editor;
		}
		public String getEdit_time() {
			return edit_time;
		}
		public void setXiehztbh(String xiehztbh) {
			this.xiehztbh = xiehztbh;
		}
		public void setFifo(String fifo) {
			this.fifo = fifo;
		}
		public void setDingdzzzl(Double dingdzzzl) {
			this.dingdzzzl = dingdzzzl;
		}
		public Double getDingdzzzl() {
			return dingdzzzl;
		}
		public void setYuanckbh(String yuanckbh) {
			this.yuanckbh = yuanckbh;
		}
		public String getYuanckbh() {
			return yuanckbh;
		}
		public void setCreator(String creator) {
			this.creator = creator;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public void setEditor(String editor) {
			this.editor = editor;
		}
		public void setEdit_time(String edit_time) {
			this.edit_time = edit_time;
		}
		public void setWulgyyz(String wulgyyz) {
			this.wulgyyz = wulgyyz;
		}
		public String getWulgyyz() {
			return wulgyyz;
		}
		public void setId(String id) {
			
		}
		public String getId() {
			return null;
		}

}

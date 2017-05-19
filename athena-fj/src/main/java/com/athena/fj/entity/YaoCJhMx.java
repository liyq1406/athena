package com.athena.fj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-10
 * @time 下午02:38:24
 * @description 要车计划明细
 */
/**
 * @author dsimedd001
 *
 */
public class YaoCJhMx implements Serializable {

	/* description: 开始时间 */
	private String fysj = "";
	/* description:   要车时间*/
	private String ycsj = "";
	/* description: 客户编码 */
	private String khbm = "";
	/* description: 车辆类型 */
	private String cllx = "";
	/* description:   运输商*/
	private String cys ="";
	/* description:   车辆名称*/
	private String clName = "";
	/* description: 备注 */
	private String note = "";
	/*满载率*/
	private float zmy=0.00000f;
	/*计划要贷令数量*/
	private int jhsl = 0;
	/*实际要贷令数量数量*/
	private int sjsl = 0;
	/*计划包装基数*/
	private int jhjs=0;
	/*实际包装数量*/
	private int sjjs = 0;
	/*是否装满*/
	private boolean isFull = false;
	/* description:   要贷令编号*/
	private Set<String> yhlbh = new HashSet<String>();
	/* description:   包装组计划数量*/
	private int tmpBaoZJhsl = 0;
	/* description:   包装组实际数量*/
	private int tmpBaoZsjsl = 0;
	/*策略编号*/
	private String celbh = "";
	/* description:   是否装满*/
	private int sfzm = 0;
	
	/* description:   零件编号 零件数量*/
	private Map<String, Integer> linj = new HashMap<String, Integer>();
	
	/* description:   包装编号集合*/
	private Map<String, Integer> bzbh = new HashMap<String, Integer>() ;
	
//	/* description:   路线组编号*/
//	private String lxbh = "";
	
	/* description:   此车要货令状态,用于计算未装满的车辆*/
	private Map<String,HashMap<String,Integer>> yaoHLstate = new HashMap<String, HashMap<String,Integer>>();
	
	
	/* description:   要货令明细*/
	private List<HashMap<String, String>> yhl = new ArrayList<HashMap<String,String>>() ;
	/* description:   路线组*/
	private WarpLXZ lxz = new WarpLXZ();
	

	
	
	

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 下午03:00:38
	 * @return  lxz
	 */
	public WarpLXZ getLxz() {
		return lxz;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 下午03:00:38
	 * @description 赋值lxz
	 */
	public void setLxz(WarpLXZ lxz) {
		this.lxz = lxz;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:20:40
	 * @return  yhl
	 */
	public List<HashMap<String, String>> getYhl() {
		return yhl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:20:40
	 * @description 赋值yhl
	 */
	public void setYhl(List<HashMap<String, String>> yhl) {
		this.yhl = yhl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午01:10:59
	 * @return  cys
	 */
	public String getCys() {
		return cys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午01:10:59
	 * @description 赋值cys
	 */
	public void setCys(String cys) {
		this.cys = cys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午10:02:34
	 * @return  yaoHLstate
	 */
	public Map<String, HashMap<String, Integer>> getYaoHLstate() {
		return yaoHLstate;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午10:02:34
	 * @description 赋值yaoHLstate
	 */
	public void setYaoHLstate(Map<String, HashMap<String, Integer>> yaoHLstate) {
		this.yaoHLstate = yaoHLstate;
	}
//	/**
//	 * @author 王冲
//	 * @email jonw@sina.com
//	 * @date 2012-3-21
//	 * @time 下午04:31:01
//	 * @return  lxbh
//	 */
//	public String getLxbh() {
//		return lxbh;
//	}
//	/**
//	 * @author 王冲
//	 * @email jonw@sina.com
//	 * @date 2012-3-21
//	 * @time 下午04:31:01
//	 * @description 赋值lxbh
//	 */
//	public void setLxbh(String lxbh) {
//		this.lxbh = lxbh;
//	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-1
	 * @time 下午01:53:41
	 * @return  bzbh
	 */
	public Map<String, Integer> getBzbh() {
		return bzbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-1
	 * @time 下午01:53:41
	 * @description 赋值bzbh
	 */
	public void setBzbh(Map<String, Integer> bzbh) {
		this.bzbh = bzbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午04:47:24
	 * @return  linj
	 */
	public Map<String, Integer> getLinj() {
		return linj;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午04:47:24
	 * @description 赋值linj
	 */
	public void setLinj(Map<String, Integer> linj) {
		this.linj = linj;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:14:37
	 * @return  jhjs
	 */
	public int getJhjs() {
		return jhjs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:14:37
	 * @description 赋值jhjs
	 */
	public void setJhjs(int jhjs) {
		this.jhjs = jhjs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:14:37
	 * @return  sjjs
	 */
	public int getSjjs() {
		return sjjs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:14:37
	 * @description 赋值sjjs
	 */
	public void setSjjs(int sjjs) {
		this.sjjs = sjjs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:55:50
	 * @return  celbh
	 */
	public String getCelbh() {
		return celbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:55:50
	 * @description 赋值celbh
	 */
	public void setCelbh(String celbh) {
		this.celbh = celbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:55:50
	 * @return  sfzm
	 */
	public int getSfzm() {
		//如果计划数量与实际数量相等,则此车装满
		if(this.getJhsl()==this.getSjsl()){
			return 1;
		}else{
			return sfzm;			
		}
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:55:50
	 * @description 赋值sfzm
	 */
	public void setSfzm(int sfzm) {
		this.sfzm = sfzm;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:17:33
	 * @return  jhsl
	 */
	public int getJhsl() {
		return jhsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:17:33
	 * @description 赋值jhsl
	 */
	public void setJhsl(int jhsl) {
		this.jhsl = jhsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:17:33
	 * @return  sjsl
	 */
	public int getSjsl() {
		return sjsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:17:33
	 * @description 赋值sjsl
	 */
	public void setSjsl(int sjsl) {
		this.sjsl = sjsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:58:02
	 * @return  tmpBaoZJhsl
	 */
	public int getTmpBaoZJhsl() {
		return tmpBaoZJhsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:58:02
	 * @description 赋值tmpBaoZJhsl
	 */
	public void setTmpBaoZJhsl(int tmpBaoZJhsl) {
		this.tmpBaoZJhsl = tmpBaoZJhsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:58:02
	 * @return  tmpBaoZsjsl
	 */
	public int getTmpBaoZsjsl() {
		return tmpBaoZsjsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:58:02
	 * @description 赋值tmpBaoZsjsl
	 */
	public void setTmpBaoZsjsl(int tmpBaoZsjsl) {
		this.tmpBaoZsjsl = tmpBaoZsjsl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:12:48
	 * @return  clName
	 */
	public String getClName() {
		return clName;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:12:48
	 * @description 赋值clName
	 */
	public void setClName(String clName) {
		this.clName = clName;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午04:38:07
	 * @return  yhlbh
	 */
	public Set<String> getYhlbh() {
		return yhlbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午04:38:07
	 * @description 赋值yhlbh
	 */
	public void setYhlbh(Set<String> yhlbh) {
		this.yhlbh = yhlbh;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 下午04:07:20
	 * @return  ycsj
	 */
	public String getYcsj() {
		return ycsj;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 下午04:07:20
	 * @description 赋值ycsj
	 */
	public void setYcsj(String ycsj) {
		this.ycsj = ycsj;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 上午10:57:48
	 * @return  isFull
	 */
	public boolean isFull() {
		return isFull;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 上午10:57:48
	 * @description 赋值isFull
	 */
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午01:45:37
	 * @return  zmy
	 */
	public float getZmy() {
		return zmy;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午01:45:37
	 * @description 赋值zmy
	 */
	public void setZmy(float zmy) {
		this.zmy = zmy;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @return  fysj
	 */
	public String getFysj() {
		return fysj;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @description 赋值fysj
	 */
	public void setFysj(String fysj) {
		this.fysj = fysj;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @return  khbm
	 */
	public String getKhbm() {
		return khbm;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @description 赋值khbm
	 */
	public void setKhbm(String khbm) {
		this.khbm = khbm;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @return  cllx
	 */
	public String getCllx() {
		return cllx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @description 赋值cllx
	 */
	public void setCllx(String cllx) {
		this.cllx = cllx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @return  note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午02:42:56
	 * @description 赋值note
	 */
	public void setNote(String note) {
		this.note = note;
	}


	
	
	
	
}

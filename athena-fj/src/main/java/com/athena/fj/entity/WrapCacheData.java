package com.athena.fj.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-3-21
 * @time 下午01:18:41
 * @description 要车计划数据缓存区
 */
@SuppressWarnings("serial")
public class WrapCacheData implements Serializable {
	
	/**数据归集,封装**/
	/* description:   路线组(key:GYS:KEH)*/
	private Map<String,ArrayList<WarpLXZ>> mapLXZ = null;
	/* description:   路线组-仓库(key:lxzbh)*/
	private Map<String,HashSet<String>> mapCK = new HashMap<String, HashSet<String>>();
	/* description:   路线组交付时刻*/
	private Map<String,ArrayList<LinkedList<String>>> mapJiaoFSK = null;
	/* description:   车辆申报资源*/
	private Map<String,WrapCelL> mapCLBySB = null;
	/* description:   用户中心车辆资源*/
	private Map<String,WrapCelL> mapCLByUC = null;
	/* description:   策略组*/
	private Map<String,WarpCelZ> mapCELZ = new HashMap<String, WarpCelZ>();
	/*参数  {UC,DATE,KSSJ,JSSJ,KEH,GYSDM}*/
	private Map<String,String > param = new HashMap<String, String>();
	/*车辆总数*/
	private int cls = 0;
	/*车辆名称*/
	private Set<String> clmc = new HashSet<String>();
	/*车辆明细*/
	private Map<String,Integer> clmx = new HashMap<String, Integer>();
	/*下一天的第一个交付时刻*/
	private Map<String,String > nextDayJiaoFSK = new HashMap<String, String>();
	/*要车计划序号*/
	private String jhxh = "";
	/*满载的*/
	private List<YaoCJhMx> yaoMZ = new ArrayList<YaoCJhMx>();

	
	
	/**根据最大提前期归集要货令后，与未装满的车计算**/
	/* description:   发运时刻未装满的要车明细*/
	private List<YaoCJhMx> yaoSKD = new ArrayList<YaoCJhMx>();
	
	
	/***混装****/
	/* description:   已使用的策略组*/
	private Map<String,HashSet<String>> useCelZ =  new HashMap<String, HashSet<String>>();
	/* description:   所有未装满的要车明细*/
	private  Map<String,HashMap<String,List<YaoCJhMx>>>  YaoHz = new HashMap<String, HashMap<String,List<YaoCJhMx>>>();
	/* description:   已使用的时间点和路线组*/
	private Map<String,HashSet<WarpLXZ>> userSKD  = new HashMap<String,HashSet<WarpLXZ>>();
	/* description:   已使用的要货令*/
	private Map<String,ArrayList<HashMap<String, String>> > userYHL  = new HashMap<String, ArrayList<HashMap<String,String>>>();

	
	/**推荐配载,混装**/
	/* description:   要等待混装的要货令号*/
	private Set<String> yhlbh = new HashSet<String>();
	/* description:   要等待混装的要货令*/
	private List<HashMap<String, String>> yhl = new ArrayList<HashMap<String,String>>();
	/* description:   混装需要的策略*/
	private Set<String> pzcl = new HashSet<String>();

	private List<HashMap<String, String>> lxzList = new ArrayList<HashMap<String,String>>();	
	
	


	public Map<String, HashMap<String, List<YaoCJhMx>>> getYaoHz() {
		return YaoHz;
	}

	public void setYaoHz(Map<String, HashMap<String, List<YaoCJhMx>>> yaoHz) {
		YaoHz = yaoHz;
	}

	public Map<String, HashSet<String>> getMapCK() {
		return mapCK;
	}

	public void setMapCK(Map<String, HashSet<String>> mapCK) {
		this.mapCK = mapCK;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:37:56
	 * @return  pzcl
	 */
	public Set<String> getPzcl() {
		return pzcl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:37:56
	 * @description 赋值pzcl
	 */
	public void setPzcl(Set<String> pzcl) {
		this.pzcl = pzcl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:36:36
	 * @return  yhlbh
	 */
	public Set<String> getYhlbh() {
		return yhlbh;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:36:36
	 * @description 赋值yhlbh
	 */
	public void setYhlbh(Set<String> yhlbh) {
		this.yhlbh = yhlbh;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:36:36
	 * @return  yhl
	 */
	public List<HashMap<String, String>> getYhl() {
		return yhl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:36:36
	 * @description 赋值yhl
	 */
	public void setYhl(List<HashMap<String, String>> yhl) {
		this.yhl = yhl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:18:56
	 * @return  userYHL
	 */
	public Map<String, ArrayList<HashMap<String, String>>> getUserYHL() {
		return userYHL;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:18:56
	 * @description 赋值userYHL
	 */
	public void setUserYHL(Map<String, ArrayList<HashMap<String, String>>> userYHL) {
		this.userYHL = userYHL;
	}


	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 下午03:02:13
	 * @return  userSKD
	 */
	public Map<String, HashSet<WarpLXZ>> getUserSKD() {
		return userSKD;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 下午03:02:13
	 * @description 赋值userSKD
	 */
	public void setUserSKD(Map<String, HashSet<WarpLXZ>> userSKD) {
		this.userSKD = userSKD;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午02:58:15
	 * @return  yaoMZ
	 */
	public List<YaoCJhMx> getYaoMZ() {
		return yaoMZ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午02:58:15
	 * @description 赋值yaoMZ
	 */
	public void setYaoMZ(List<YaoCJhMx> yaoMZ) {
		this.yaoMZ = yaoMZ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:04:11
	 * @return  jhxh
	 */
	public String getJhxh() {
		return jhxh;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:04:11
	 * @description 赋值jhxh
	 */
	public void setJhxh(String jhxh) {
		this.jhxh = jhxh;
	}


	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:21:17
	 * @return  nextDayJiaoFSK
	 */
	public Map<String, String> getNextDayJiaoFSK() {
		return nextDayJiaoFSK;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:21:17
	 * @description 赋值nextDayJiaoFSK
	 */
	public void setNextDayJiaoFSK(Map<String, String> nextDayJiaoFSK) {
		this.nextDayJiaoFSK = nextDayJiaoFSK;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:27:58
	 * @return  clmc
	 */
	public Set<String> getClmc() {
		return clmc;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:27:58
	 * @description 赋值clmc
	 */
	public void setClmc(Set<String> clmc) {
		this.clmc = clmc;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:20:17
	 * @return  clmx
	 */
	public Map<String, Integer> getClmx() {
		return clmx;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:20:17
	 * @description 赋值clmx
	 */
	public void setClmx(Map<String, Integer> clmx) {
		this.clmx = clmx;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:09:54
	 * @return  cls
	 */
	public int getCls() {
		return cls;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:09:54
	 * @description 赋值cls
	 */
	public void setCls(int cls) {
		this.cls = cls;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:37:30
	 * @return  param
	 */
	public Map<String, String> getParam() {
		return param;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:37:30
	 * @description 赋值param
	 */
	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @return  mapLXZ
	 */
	public Map<String, ArrayList<WarpLXZ>> getMapLXZ() {
		return mapLXZ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @description 赋值mapLXZ
	 */
	public void setMapLXZ(Map<String, ArrayList<WarpLXZ>> mapLXZ) {
		this.mapLXZ = mapLXZ;
	}



	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:17:41
	 * @return  mapJiaoFSK
	 */
	public Map<String, ArrayList<LinkedList<String>>> getMapJiaoFSK() {
		return mapJiaoFSK;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:17:41
	 * @description 赋值mapJiaoFSK
	 */
	public void setMapJiaoFSK(Map<String, ArrayList<LinkedList<String>>> mapJiaoFSK) {
		this.mapJiaoFSK = mapJiaoFSK;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @return  mapCLBySB
	 */
	public Map<String, WrapCelL> getMapCLBySB() {
		return mapCLBySB;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @description 赋值mapCLBySB
	 */
	public void setMapCLBySB(Map<String, WrapCelL> mapCLBySB) {
		this.mapCLBySB = mapCLBySB;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @return  mapCLByUC
	 */
	public Map<String, WrapCelL> getMapCLByUC() {
		return mapCLByUC;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @description 赋值mapCLByUC
	 */
	public void setMapCLByUC(Map<String, WrapCelL> mapCLByUC) {
		this.mapCLByUC = mapCLByUC;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @return  mapCELZ
	 */
	public Map<String, WarpCelZ> getMapCELZ() {
		return mapCELZ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:30:35
	 * @description 赋值mapCELZ
	 */
	public void setMapCELZ(Map<String, WarpCelZ> mapCELZ) {
		this.mapCELZ.putAll(mapCELZ) ;
//		if(this.mapCELZ==null){
//			this.mapCELZ = mapCELZ;}
//		else{
//			for(Map.Entry<String, WarpCelZ> cel:mapCELZ.entrySet()){
//				this.mapCELZ.put(key, value)
//			}
//		}
		
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午09:57:45
	 * @return  yaoSKD
	 */
	public List<YaoCJhMx> getYaoSKD() {
		return yaoSKD;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午09:57:45
	 * @description 赋值yaoSKD
	 */
	public void setYaoSKD(List<YaoCJhMx> yaoSKD) {
		this.yaoSKD = yaoSKD;
	}


	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:05:06
	 * @return  useCelZ
	 */
	public Map<String, HashSet<String>> getUseCelZ() {
		return useCelZ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:05:06
	 * @description 赋值useCelZ
	 */
	public void setUseCelZ(Map<String, HashSet<String>> useCelZ) {
		this.useCelZ = useCelZ;
	}



	/**
	 * 深度复制 实现整个对象
	 * @return Serializable
	 */
	public static   Serializable deepCopy(Serializable src){
		
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oop = new ObjectOutputStream(bos);
			oop.writeObject(src);
			byte[] bytes = bos.toByteArray();
			oop.close();
			bos.close();
			
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Serializable newsrc = (Serializable) ois.readObject();
			ois.close();
			bis.close();
			
			return newsrc;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	public List<HashMap<String, String>> getLxzList() {
		return lxzList;
	}

	public void setLxzList(List<HashMap<String, String>> lxzList) {
		this.lxzList = lxzList;
	}
	

}

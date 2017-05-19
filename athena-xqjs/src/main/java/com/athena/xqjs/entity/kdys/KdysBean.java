/**
 * 
 */
package com.athena.xqjs.entity.kdys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.xqjs.entity.ilorder.Dingdlj;

/**
 * @author dsimedd001
 *
 */
public class KdysBean {
	private Map<String, String> ckbhMap = new HashMap<String, String>();
	private Map<String, BigDecimal> zhuangtMap = new HashMap<String, BigDecimal>();
	private Map<String, String> wuldMap = new HashMap<String, String>();
	private List<String> uttcList = new ArrayList<String>();
	private List<String> utList = new ArrayList<String>();
	
	private Map<String, String> keyuaMap = new HashMap<String, String>();
	private List<String> yaohlKeyList = new ArrayList<String>();
	private Map<String, String> yaohlUtMap = new HashMap<String, String>();
	
	private List<Dingdlj> dingdljList = new ArrayList<Dingdlj>();
	private Map<String, String> dingdljMap = new HashMap<String, String>();
	
	/**
	 * 取得 ckbhMap
	 * @return the ckbhMap
	 */
	public Map<String, String> getCkbhMap() {
		return ckbhMap;
	}
	/**
	 * @param ckbhMap the ckbhMap to set
	 */
	public void setCkbhMap(Map<String, String> ckbhMap) {
		this.ckbhMap = ckbhMap;
	}
	/**
	 * 取得 zhuangtMap
	 * @return the zhuangtMap
	 */
	public Map<String, BigDecimal> getZhuangtMap() {
		return zhuangtMap;
	}
	/**
	 * @param zhuangtMap the zhuangtMap to set
	 */
	public void setZhuangtMap(Map<String, BigDecimal> zhuangtMap) {
		this.zhuangtMap = zhuangtMap;
	}
	/**
	 * 取得 wuldMap
	 * @return the wuldMap
	 */
	public Map<String, String> getWuldMap() {
		return wuldMap;
	}
	/**
	 * @param wuldMap the wuldMap to set
	 */
	public void setWuldMap(Map<String, String> wuldMap) {
		this.wuldMap = wuldMap;
	}
	/**
	 * 取得 uttcList
	 * @return the uttcList
	 */
	public List<String> getUttcList() {
		return uttcList;
	}
	/**
	 * @param uttcList the uttcList to set
	 */
	public void setUttcList(List<String> uttcList) {
		this.uttcList = uttcList;
	}
	/**
	 * 取得 utList
	 * @return the utList
	 */
	public List<String> getUtList() {
		return utList;
	}
	/**
	 * @param utList the utList to set
	 */
	public void setUtList(List<String> utList) {
		this.utList = utList;
	}
	/**
	 * 取得 keyuaMap
	 * @return the keyuaMap
	 */
	public Map<String, String> getKeyuaMap() {
		return keyuaMap;
	}
	/**
	 * @param keyuaMap the keyuaMap to set
	 */
	public void setKeyuaMap(Map<String, String> keyuaMap) {
		this.keyuaMap = keyuaMap;
	}
	/**
	 * 取得 yaohlKeyList
	 * @return the yaohlKeyList
	 */
	public List<String> getYaohlKeyList() {
		return yaohlKeyList;
	}
	/**
	 * @param yaohlKeyList the yaohlKeyList to set
	 */
	public void setYaohlKeyList(List<String> yaohlKeyList) {
		this.yaohlKeyList = yaohlKeyList;
	}
	/**
	 * 取得 yaohlUtMap
	 * @return the yaohlUtMap
	 */
	public Map<String, String> getYaohlUtMap() {
		return yaohlUtMap;
	}
	/**
	 * @param yaohlUtMap the yaohlUtMap to set
	 */
	public void setYaohlUtMap(Map<String, String> yaohlUtMap) {
		this.yaohlUtMap = yaohlUtMap;
	}
	/**
	 * 取得 dingdljList
	 * @return the dingdljList
	 */
	public List<Dingdlj> getDingdljList() {
		return dingdljList;
	}
	/**
	 * @param dingdljList the dingdljList to set
	 */
	public void setDingdljList(List<Dingdlj> dingdljList) {
		this.dingdljList = dingdljList;
	}
	/**
	 * 取得 dingdljMap
	 * @return the dingdljMap
	 */
	public Map<String, String> getDingdljMap() {
		return dingdljMap;
	}
	/**
	 * @param dingdljMap the dingdljMap to set
	 */
	public void setDingdljMap(Map<String, String> dingdljMap) {
		this.dingdljMap = dingdljMap;
	}

	
	
}

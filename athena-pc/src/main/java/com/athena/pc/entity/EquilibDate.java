package com.athena.pc.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-6-14
 * @Time: 下午3:34:21
 * @version 1.0
 * @Description : 工作日
 */
public class EquilibDate {

	/* description: 日期 */
	private String date = null;
	/* description: 生产线 */
	private List<EquilibScx> scxs = null;
	/* description: 零件 */
	private Map<String, List<EquilibLJ>> ljMap = null;
	/* description: 工作日是否正常,（true ： 正常 ，false :非正常） */
	private boolean sfzc = false;
	/* description:   消耗量*/
	private HashMap<String,Integer> xhL = new HashMap<String, Integer>();
	/* description:   期初库存*/
	private Map<String, Integer> qcKc = null;
	/* description: 消息报警 */
//	private Map<String, EquilibMessage> chanxMessage = new HashMap<String, EquilibMessage>();
	private List<EquilibMessage> chanxMessage = new ArrayList<EquilibMessage>();
	/* description: 共线零件 */
	private static Map<String, HashSet<String>> gxLj = null;

	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:37:53
	 * @version 1.0
	 * @return  qcKc
	 * @Description: 返回qcKc的值
	 */
	public Map<String, Integer> getQcKc() {
		return qcKc;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:37:53
	 * @version 1.0
	 * @Description: 将参数qcKc的值赋值给成员变量qcKc
	 */
	public void setQcKc(Map<String, Integer> qcKc) {
		this.qcKc = qcKc;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:34:54
	 * @version 1.0
	 * @return  xhL
	 * @Description: 返回xhL的值
	 */
	public HashMap<String, Integer> getXhL() {
		return xhL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:34:54
	 * @version 1.0
	 * @Description: 将参数xhL的值赋值给成员变量xhL
	 */
	public void setXhL(HashMap<String, Integer> xhL) {
		this.xhL = xhL;
	}
	

//	/**
//	 * @Author: 王冲
//	 * @Email:jonw@sina.com
//	 * @Date: 2012-6-19
//	 * @Time: 上午10:03:19
//	 * @version 1.0
//	 * @return chanxMessage
//	 * @Description: 返回chanxMessage的值
//	 */
//	public Map<String, EquilibMessage> getChanxMessage() {
//		return chanxMessage;
//	}
//
//	/**
//	 * @Author: 王冲
//	 * @Email:jonw@sina.com
//	 * @Date: 2012-6-19
//	 * @Time: 上午10:03:19
//	 * @version 1.0
//	 * @Description: 将参数chanxMessage的值赋值给成员变量chanxMessage
//	 */
//	public void setChanxMessage(Map<String, EquilibMessage> chanxMessage) {
//		this.chanxMessage = chanxMessage;
//	}

	public List<EquilibMessage> getChanxMessage() {
		return chanxMessage;
	}

	public void setChanxMessage(List<EquilibMessage> chanxMessage) {
		this.chanxMessage = chanxMessage;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @return date
	 * @Description: 返回date的值
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @Description: 将参数date的值赋值给成员变量date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @return scxs
	 * @Description: 返回scxs的值
	 */
	public List<EquilibScx> getScxs() {
		return scxs;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @Description: 将参数scxs的值赋值给成员变量scxs
	 */
	public void setScxs(List<EquilibScx> scxs) {
		this.scxs = scxs;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @return ljMap
	 * @Description: 返回ljMap的值
	 */
	public Map<String, List<EquilibLJ>> getLjMap() {
		return ljMap;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @Description: 将参数ljMap的值赋值给成员变量ljMap
	 */
	public void setLjMap(Map<String, List<EquilibLJ>> ljMap) {
		this.ljMap = ljMap;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @return sfzc
	 * @Description: 返回sfzc的值
	 */
	public boolean isSfzc() {
		return sfzc;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:37:51
	 * @version 1.0
	 * @Description: 将参数sfzc的值赋值给成员变量sfzc
	 */
	public void setSfzc(boolean sfzc) {
		this.sfzc = sfzc;
	}


	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 上午9:55:22
	 * @version 1.0
	 * @return gxLj
	 * @Description: 返回gxLj的值
	 */
	public static Map<String, HashSet<String>> getGxLj() {
		return gxLj;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 上午9:55:22
	 * @version 1.0
	 * @Description: 将参数gxLj的值赋值给成员变量gxLj
	 */
	public static void setGxLj(Map<String, HashSet<String>> _gxLj) {
		// 如果共线零件为空
		if (EquilibDate.gxLj == null) {
			EquilibDate.gxLj = _gxLj;
		}
	}

}

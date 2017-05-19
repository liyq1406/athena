/**
 * 
 */
package com.athena.xqjs.module.lingjcx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.entity.lingjcx.LingjDingdhcxBean;
import com.athena.xqjs.entity.lingjcx.LingjbytcnoBean;
import com.athena.xqjs.entity.lingjcx.TCLingjbydingdhcxBean;
import com.athena.xqjs.entity.lingjcx.TcbyqysjcxBean;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * @author dsimedd001
 * 
 */
@Component
public class LingjcxService extends BaseService {

	/**
	 * 查询条件(必输)： 1、用户中心;2、零件号 查询SQL语句:
	 * 根据集装箱(XQJS_TC)、到货通知单(CK_DAOHTZD)、UA标签(CK_UABQ)三张表联合查询状态为2的数据
	 * 再用最新物理点和零件编号进行GROUP BY
	 * @param usercenter
	 * @param lingjbh
	 * @return
	 */
	public Map<String, Object> queryLingjcx(String usercenter, String lingjbh,Maoxqmx mx,Map pagemap) {
		//用户中心和零件号为必输量，如果为空则不进行查询
		Map<String, Object> map = new HashMap<String, Object>();
		/** modify by hzg mantis:0011498 2015.7.20  用户中心全集查询*/
		if (lingjbh != null	&& !"".equals(lingjbh)) {
			// 用户中心
			mx.setUsercenter(usercenter);
			// 零件编号
			mx.setLingjbh(lingjbh);
			//查询
			if("exportXls".equals(pagemap.get("exportXls"))){
				List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.lingjcx", pagemap);
//				for(int i=0;i<list.size();i++){
//					LingjztBean bean = (LingjztBean)list.get(i);
//					if(bean.getWuld()!=null&&!"".equals(bean.getWuld())){
//						bean.setWuld(this.getCache(bean.getWuld()));
//					}
//					_list.add(bean);
//				}
				map = CommonFun.listToMap(list);
			}else {
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.lingjcx", mx, mx);
			}
			
		}
		return map;
	}

	/**
	 * 根据物理点，查询零件信息: 
	 * 1、用户中心 2、零件编号 3、物理点 4、TC号，5状态为"2、在途"
	 * 查询表:
	 * 根据集装箱(XQJS_TC)、到货通知单(CK_DAOHTZD)、UA标签(CK_UABQ)联合查询
	 * @param usercenter
	 * @param lingjbh
	 * @param wuld
	 * @return
	 */
	public Map<String, Object> selectTCLingjByWuld(TC tc,Map<String, String> tjMap,Map pagemap) {
		//用户中心
		String usercenter = (String)tjMap.get("usercenter");
		Map<String, Object> map = new HashMap<String, Object>();
		//用户中心为必输条件
		if (usercenter != null && !"".equals(usercenter)) {
			if("exportXls".equals(pagemap.get("exportXls"))){
				List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTCLingjByWuld", pagemap);
				for(int i=0;i<list.size();i++){
					LingjbytcnoBean bean = (LingjbytcnoBean)list.get(i);
					if(bean.getWuld()!=null&&!"".equals(bean.getWuld())){
						bean.setWuld(this.getCache(bean.getWuld()));
					}
					_list.add(bean);
				}
				map = CommonFun.listToMap(_list);
				//map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTCLingjByWuld",pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectTCLingjByWuld", tjMap, tc);
			}
		}
		return map;
	}

	/**
	 * 条件参数：
	 *  1、用户中心 2、零件编号 3、TC号 4、要货令号 5、交付时间
	 * 查询表:
	 * 集装箱(XQJS_TC)、到货通知单(CK_DAOHTZD)、UA标签(CK_UABQ)、要货令(CK_YAOHL)
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @param yaohlh
	 * @param jiaofj
	 * @return
	 */
	public Map<String, Object> selectYaohlByYaohlh(Map tjMap,TC tc,Map pagemap) {
		//初始化集合
		Map<String, Object> map = new HashMap<String, Object>();
		if("exportXls".equals(pagemap.get("exportXls"))){
			map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectYaohlByYaohlh", tjMap));
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectYaohlByYaohlh", tjMap, tc);
		}
		
		return map;
	}

	/**
	 * 查询条件：
	 *  1、用户中心 2、零件编号 3、订单号
	 * 查询表：
	 * 1、TC表(XQJS_TC)；2、到货通知单(CK_DAOHTZD);3、UA标签(CK_UABQ);4、要货令(CK_YAOHL)
	 * @param usercenter
	 * @param lingjbh
	 * @param dingdh
	 * @return
	 */
	public Map<String, Object> selectlingjDingdhcx(String usercenter,
			String lingjbh, String dingdh,Maoxqmx mx,Map pagemap) {
		//初始化集合
		Map<String, Object> map = new HashMap<String, Object>();
		/** modify by hzg mantis:0011498 2015.7.20  用户中心全集查询*/
		if ( lingjbh != null && !"".equals(lingjbh)) {
			Map<String,String> tjMap = new HashMap<String,String>();
			tjMap.put("usercenter", usercenter);
			tjMap.put("lingjbh", lingjbh);
			tjMap.put("dingdh", dingdh);
			if("exportXls".equals(pagemap.get("exportXls"))){
				List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.lingjdingdhcx", pagemap);
				for(int i=0;i<list.size();i++){
					LingjDingdhcxBean bean = (LingjDingdhcxBean)list.get(i);
					if(bean.getWuld()!=null&&!"".equals(bean.getWuld())){
						bean.setWuld(this.getCache(bean.getWuld()));
					}
					_list.add(bean);
				}
				map = CommonFun.listToMap(_list);
				//map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.lingjdingdhcx", pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.lingjdingdhcx", tjMap, mx);
			}
		}
		return map;
	}

	/**
	 * 查询条件：
	 * 1、零件编号(lingjbh);2、用户中心(usercenter);3、物理点(wuld);4、TC号(tcNo)
	 * 查询表:
	 * 1、TC表(XQJS_TC)；2、到货通知单(CK_DAOHTZD);3、UA标签(CK_UABQ);4、要货令(CK_YAOHL)
	 * @param tjMap
	 * @return
	 */
	public Map<String,Object> selectTCLingjByDingdhcx(Map<String, String> tjMap,TC tc,Map pagemap) {
		//初始化集合
		/** modify by hzg mantis:0011498 2015.9.30 用户中心全集查询 */
		//String usercenter = (String)tjMap.get("usercenter");
		Map<String, Object> map = new HashMap<String, Object>();
		/*if (usercenter != null && !"".equals(usercenter)) {  */
			if("exportXls".equals(pagemap.get("exportXls"))){
				List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTCLingjByDingdhcx", pagemap);
				for(int i=0;i<list.size();i++){
					TCLingjbydingdhcxBean bean = (TCLingjbydingdhcxBean)list.get(i);
					if(bean.getWuld()!=null&&!"".equals(bean.getWuld())){
						bean.setWuld(this.getCache(bean.getWuld()));
					}
					_list.add(bean);
				}
				map = CommonFun.listToMap(_list);
				//map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTCLingjByDingdhcx", pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectTCLingjByDingdhcx", tjMap, tc);
			}
		/*}*/
		return map;
	}

	/**
	 * 查询条件：
	 * 1、零件编号(lingjbh);2、用户中心(usercenter);4、TC号(tcNo)
	 * 查询表:
	 * 1、TC表(XQJS_TC)；2、到货通知单(CK_DAOHTZD);3、UA标签(CK_UABQ);4、要货令(CK_YAOHL)
	 * @param tjMap
	 * @return
	 */
	public Map<String,Object> selectTccx(Map<String, String> tjMap,TC tc ,Map pagemap) {
		//初始化集合
		String tcNo = (String)tjMap.get("tcNo");
		Map<String, Object> map = new HashMap<String, Object>();
		if (tcNo != null && !"".equals(tcNo)) {
			if("exportXls".equals(pagemap.get("exportXls"))){
				map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTccx", pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectTccx", tjMap, tc);
			}
			
		}
		return map;
	}

	/**
	 * 查询条件：
	 * 1、用户中心(usercenter);2、启运时间开始(startQiysj);3、启运时间结束(endQiysj)
	 * 查询表:
	 * 1、集装箱(XQJS_TC);2、要货令(CK_DAOHTZD)
	 * @param tjMap
	 * @return
	 */
	public Map<String,Object> selectTcByQysjcx(Map<String, String> tjMap,TC tc,Map pagemap) {
		/** modify by hzg mantis:0011498 2015.7.20 用户中心全集查询 */
		//String usercenter = (String)tjMap.get("usercenter");
		Map<String, Object> map = new HashMap<String, Object>();
		/*if (usercenter != null && !"".equals(usercenter)) {  */
			if("exportXls".equals(pagemap.get("exportXls"))){ 
				List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTcByQysjcx", pagemap);
				for(int i=0;i<list.size();i++){
					TcbyqysjcxBean bean = (TcbyqysjcxBean)list.get(i);
					if(bean.getZuiswld()!=null&&!"".equals(bean.getZuiswld())){
						bean.setZuiswld(this.getCache(bean.getZuiswld()));
					}
					_list.add(bean);
				}
				map = CommonFun.listToMap(_list);
				//map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTcByQysjcx", pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectTcByQysjcx", tjMap, tc);
			}
			
		/*}*/
		return map;
	}

	/**
	 * 查询条件：
	 * 1、零件编号(lingjbh);2、用户中心(usercenter);
	 * 查询表：
	 * 1、TC表(XQJS_TC)；2、到货通知单(CK_DAOHTZD);3、UA标签(CK_UABQ);4、要货令(CK_YAOHL)
	 * @param tjMap
	 * @return
	 */
	public Map<String,Object> selectTcBywuldcx(Map<String, String> tjMap,TC tc,Map pagemap) {
		//初始化集合
//		String usercenter = (String)tjMap.get("usercenter");
		Map<String, Object> map = new HashMap<String, Object>();
		/** modify by hzg mantis:0011498  2015.7.20  用户中心全集查询*/
	/*	if (usercenter != null && !"".equals(usercenter)) { */
			if("exportXls".equals(pagemap.get("exportXls"))){
				//List _list = new ArrayList();
				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTcBywuldcx", pagemap);
//				for(int i=0;i<list.size();i++){
//					TcbywuldcxBean bean = (TcbywuldcxBean)list.get(i);
//					if(bean.getWuld()!=null&&!"".equals(bean.getWuld())){
//						bean.setWuld(this.getCache(bean.getWuld()));
//					}
//					_list.add(bean);
//				}
				map = CommonFun.listToMap(list);
				//map = CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.selectTcBywuldcx", pagemap));
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("lingjcx.selectTcBywuldcx", tjMap, tc);
			}
			
		/*}*/
		return map;
	}

	/**
	 * 根据物理点类型获取运输物理点
	 * @param tjMap
	 * @return
	 */
	public List getYunswuld(Map tjMap) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("lingjcx.getYunswuld", tjMap);
	}
	
	/**
	 * 获取缓存信息
	 * @param startwuldfw
	 * @param endwuldfw
	 * @return
	 */
	public String getCache(String wuld) {
		String param = "";
		CacheManager cm = CacheManager.getInstance();
		List list = (List) cm.getCacheInfo("queryWuld").getCacheValue();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CacheValue cacheValue = (CacheValue) list.get(i);
			String key = cacheValue.getKey();
			String value = cacheValue.getValue();
			if(wuld.equals(key)){
				param = value;
			}
		}
		return param;
	}
}

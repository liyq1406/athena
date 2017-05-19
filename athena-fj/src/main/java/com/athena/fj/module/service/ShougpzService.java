package com.athena.fj.module.service;


import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.WarpCelZ;
import com.athena.fj.entity.WarpLXZ;
import com.athena.fj.entity.WrapCacheData;
import com.athena.fj.entity.YaoCJhMx;
import com.athena.fj.entity.Yaocjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class ShougpzService extends BaseService<Yaocjh> {
	@Inject
	private YaocjhService yaocjhService = null;

	/*****************BY 王冲   date :2012-02-09*****************************/
	
	/**
	 * 归集供应商 客户
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGYSKEH(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("shougpz.queryGYSKEH",param);
	}
	
	
	/**
	 * 归集路线组所对应的仓库
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectCK(final Map<String,String> param)  throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("shougpz.queryck",param);
	}

	/**
	 * 归集策略
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectCelZ(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("shougpz.queryCelZ",param);
	}
	
	/**
	 * 归集策略
	 * @author 贺志国
	 * @date 2012-03-30
	 * @param param   
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizcl(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("shougpz.queryPeizcl",param);
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午04:21:10
	 * @param param 
	 * @return List<Map<String,String>> 
	 * @throws ServiceException
	 * @description   归集要贷令号
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,String>> selectYHL(final Map<String,String> param) throws ServiceException{
		//查询此时间段内的要贷令
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("shougpz.queryYHL",param);
	} 
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午03:29:39
	 * @param params  {
	 *                 必填项:UC 用户中心,LXZBH:路线组编号,PZBH:配载策略编号
	 *                } 
	 * @return List<YaoCJhMx>
	 * @description   返回推荐后的配载策略
	 */
	public List<YaoCJhMx> tuiJYaoCJHmx(Map<String, String>  params ) throws ServiceException
	{
         
		/* 要车明细 */
		List<YaoCJhMx> yaoCMx = new ArrayList<YaoCJhMx>();
		/* 归集供应商-客户*/
		List<Map<String, String>> gysKEHList = this.selectGYSKEH(params);
		/* 如果没有供应商-客户,终止退出 */
		if (gysKEHList == null || gysKEHList.size() == 0){
			return yaoCMx;			
		}
		//得到路线组所对应的仓库
		List<Map<String, String>> ckList = this.selectCK(params) ;
		//数据缓存
		WrapCacheData cache = new WrapCacheData();
		//路线组
		WarpLXZ lx = new WarpLXZ();
		lx.setLzxbh(params.get("LXZBH")) ; 
		
		for (Map<String, String> gyskeh : gysKEHList) {
			
			// 客户
			String keh = gyskeh.get("KEH");
			params.put("KEH",keh );
			cache.setParam(params) ;
			
			/**1.不考滤混装**/
			this.tjpz(lx, ckList, params, cache) ;
			/**1.如果已装入一车，则直接返回***/
			if(cache.getYaoMZ().size()==1){
//				break;
				return cache.getYaoMZ();
			}
			
	
		}
		
		/**2.考滤混装(满载)**/
		//如果没有要车明细
		if(cache.getYaoMZ().size()==0){
			//混装计算
			this.tjpzByHz(lx, ckList, params, cache) ;
			if(cache.getYaoMZ().size()==1){
				return cache.getYaoMZ();
			}
		}
		
		/**3.混装未满载***/
		if (cache.getYaoMZ().size() == 0) {
			tjpzByHzOfWMz(lx, ckList, params, cache);
			if (cache.getYaoSKD().size() > 0) {
				return cache.getYaoSKD();
			}
		}
		return yaoCMx;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:55:18
	 * @param lx 路线组
	 * @param ckList 仓库
	 * @param params 参数
	 * @param cache 数据中心
	 * @description  推荐配载,不考滤混装
	 */
	public void tjpz(WarpLXZ lx,List<Map<String, String>> ckList,
			Map<String, String>  params,WrapCacheData cache ) throws ServiceException{
		//循环路线组
		for (Map<String, String> ckMap : ckList) 
		{
			//设置仓库编号
			lx.setCkbh(ckMap.get("CANGKBH")) ;
			//设置仓库编号
			params.put("CKBH", ckMap.get("CANGKBH"));
			
			/* 归集策略 */
			List<Map<String, String>> celZList = this.selectCelZ(params);
			/* 如果没有该用户中心的配载策略,则终止退出 */
			if (celZList == null || celZList.size() == 0) {
				return ;
			}
			// 封装策略
			Map<String, WarpCelZ> celMap = this.yaocjhService.getWarpCelZ(celZList);
			// 写入数据缓存
			cache.setMapCELZ(celMap);

			// 策略号组
			Iterator<String> celHs = celMap.keySet().iterator();
			//迭代所有策略组
			while (celHs.hasNext()) {
				/* 归集要贷令号 */
				List<HashMap<String, String>> yaoHlList = this.selectYHL(params);
				//策略编号
				String celh = celHs.next();
				//记录策略号
				cache.getPzcl().add(celh) ;
				//策略计算
				this.yaocjhService.getYaoCjhMxOf_Full(celh, lx, yaoHlList, cache, YaocjhService.CLZY_YXJ_4,YaocjhService.WRITEDB_NO) ;
			}
			
			
			/**如果已装入一车，则直接返回***/
			if(cache.getYaoMZ().size()==1){
				break;
			}

		}
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:55:18
	 * @param lx 路线组
	 * @param ckList 仓库
	 * @param params 参数
	 * @param cache 数据中心
	 * @description  推荐配载,考滤混装,满载
	 */
	public void tjpzByHz(WarpLXZ lx,List<Map<String, String>> ckList,
			Map<String, String>  params,WrapCacheData cache ) throws ServiceException{
		//循环路线组
		for (Map<String, String> ckMap : ckList) 
		{
			//设置仓库编号
			lx.setCkbh(ckMap.get("CANGKBH")) ;
			// 策略号组
			Iterator<String> celHs = cache.getPzcl().iterator();
			//迭代所有策略组
			while (celHs.hasNext()) {
				/* 归集要贷令号 */
				List<HashMap<String, String>> yaoHlList = cache.getYhl();
				//策略编号
				String celh = celHs.next();
				//策略计算
				this.yaocjhService.getYaoCjhMxOf_Full(celh, lx, yaoHlList, cache, YaocjhService.CLZY_YXJ_4,YaocjhService.WRITEDB_NO) ;
				
			}
			/**如果已装入一车，则直接返回***/
			if(cache.getYaoMZ().size()==1){
				break;
			}
		}
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:55:18
	 * @param lx 路线组
	 * @param ckList 仓库
	 * @param params 参数
	 * @param cache 数据中心
	 * @description  推荐配载,考滤混装, 不满载
	 */
	public void tjpzByHzOfWMz(WarpLXZ lx,List<Map<String, String>> ckList,
			Map<String, String>  params,WrapCacheData cache ) throws ServiceException{
		//循环路线组
		for (Map<String, String> ckMap : ckList) 
		{
			//设置仓库编号
			lx.setCkbh(ckMap.get("CANGKBH")) ;
			//计算
			this.yaocjhService.getYaoCMxOf_NotFull(lx, cache.getPzcl(), cache.getYhl(), cache, YaocjhService.CLZY_YXJ_4) ;
			/**如果已装入一车，则直接返回***/
			if(cache.getYaoSKD().size()==1){
				break;
			}
		}
	}
	
}

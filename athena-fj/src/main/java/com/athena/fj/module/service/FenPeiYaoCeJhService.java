package com.athena.fj.module.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.YAOCMx;
import com.athena.fj.entity.Yaocjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-18
 * @time 下午02:20:48
 * @description 分配要车计划
 */
@Component
public class FenPeiYaoCeJhService   extends BaseService<Yaocjh> {

	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:24:10
	 * @param uc 用户中心
	 * @return List<Map<String,String>>
	 * @description  根据用户中心 查询0210要车计划总表   
	 */
	@Transactional
	public Map<String,Object> selectYaoCeJhZb(PageableSupport page,Map<String, String> params)
	{
		//要车计划总表明细
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("fenpeiyaocjh.queryYaocjhZbByUc",params,page);
		
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:24:10
	 * @param uc 用户中心
	 * @return List<Map<String,String>>
	 * @description  根据用户中心 查询0210要车计划总表   (导出)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYaoCMXForDownLoad(Map<String, String> params)
	{
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectYaoCMXForDownLoad", params) ;
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:24:59
	 * @param params {key: YAOCJHXH 要车计划总表主键,key:UC 用户中心}
	 * @return List<Map<String,String>>
	 * @description  查询车辆明细 by 要车计划总表主键
	 */
	@Transactional
	public  Map<String,Object> selectYaocjhCelMx(PageableSupport page,Map<String, String> params)
	{
		//车辆明细
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("fenpeiyaocjh.queryYaocjhCelMxByZb",params,page) ;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:45:27
	 * @param params {key:UC 用户中心,key:YAOCJHXH 要计划序号}
	 * @return List<Map<String,String>>
	 * @description   根据要车计划序号和用户中心查询所有的要车明细
	 */
	@Transactional
	public Map<String,Object> selectYaoCelMxAll(PageableSupport page,Map<String, String> params)
	{
		//所有的要车明细
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("fenpeiyaocjh.queryYaoCMXAll",params,page) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:45:27
	 * @param params {key:UC 用户中心,key:YAOCJHXH 要计划序号}
	 * @return List<Map<String,String>>
	 * @description   根据要车计划序号和用户中心查询尚未分配的要车明细
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>>  selectYaoCelMxAllNotAssign(PageableSupport page,Map<String, String> params)
	{
		//尚未分配运输商的要车明细
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.queryYaoCMXAllNotAssign",params) ;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:45:27
	 * @param params {key:UC 用户中心,key:YAOCJHXH 要计划序号}
	 * @return List<Map<String,String>>
	 * @description   根据要车计划序号和用户中心查询车辆申报资源
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> selectYaocjhYySByClSb(Map<String, String> params) throws ServiceException
	{
		//车辆申报资源
		return (List<Map<String,String>>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.queryYaocjhYySByClSb",params) ;
	} 
	
	/**
	 * 参考系运输商查询
	 * @date 2012-4-1
	 * @author hzg
	 * @return List<Map<String,String>>运输商集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunss(Map<String, String> params) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectYunss",params);
	}
	
	/**
	 * 查询要车明细表获得运输路线分组集合
	 * @author 贺志国
	 * @date 2012-8-9
	 * @param params 要车计划序号、用户中心
	 * @return List<Map<String,String>>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getYunslxOfYaocmx(Map<String, String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectYunslxOfYaocmx", params);
	}
	
	/**
	 * 运输商对应的车辆明细查询
	 * @author 贺志国
	 * @date 2012-4-1
	 * @param params
	 * @return List<Map<String,String>>运输商下的车辆明细
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunssMx(Map<String, String> params) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectYunssMx", params);
	}
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2012-4-1
	 * @param params
	 * @return 根据运输商得到所有的路线组
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectLxzByCys(Map<String, String> params) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectLxzByCys", params);
	} 
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:45:27
	 * @param params {key:UC 用户中心}
	 * @return List<Map<String,String>>
	 * @description   查询客户,运输商关系
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> queryYaocjhKehGys(Map<String, String> params) throws ServiceException
	{
		//查询客户,运输商关系
		return (List<Map<String,String>>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.queryYaocjhKehGys",params) ;
	} 
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:45:27
	 * @param params {key:UC 用户中心,key:GCBH 运输商编号}
	 * @return List<Map<String,String>>
	 * @description   根据运输商编号和用户中心查询要车明细
	 */
/*	@Transactional
	public Map<String,Object> selectYaoCelMx(PageableSupport page,Map<String, String> params)
	{
		//要车明细
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("fenpeiyaocjh.queryYaocjhCelMxByYyS",params,page) ;
	}*/
	
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2012-4-1
	 * @param lxzList
	 * @return 封装路线组
	 */
	public  Map<String,Set<String>> wrapLxzByCys(List<Map<String,String>>  lxzList) throws ServiceException{
	/*	Map<String,String> wrapLxz = new HashMap<String, String>();
		for(Map<String,String> lx:lxzList){
			//Set<String> lxbh = new HashSet<String>();
			//lxbh.add(lx.get("YUNSLXBH")) ;
			if(wrapLxz.containsKey(lx.get("GCBH"))){
				wrapLxz.put(lx.get("GCBH"), wrapLxz.get(lx.get("GCBH"))+","+lx.get("YUNSLXBH"));
			}else{
				wrapLxz.put(lx.get("GCBH"), lx.get("YUNSLXBH"));
			}
		}
		
		return wrapLxz;*/
		
		Map<String,Set<String>> wrapLxz = new HashMap<String, Set<String>>();
		for(Map<String,String> lx:lxzList){
				Set<String> lxbh = new HashSet<String>();
				if(wrapLxz.containsKey(lx.get("GCBH"))){
					lxbh.add(wrapLxz.get(lx.get("GCBH"))+","+lx.get("YUNSLXBH"));
					wrapLxz.put(lx.get("GCBH"), lxbh);
				}else{
					lxbh.add(lx.get("YUNSLXBH"));
					wrapLxz.put(lx.get("GCBH"), lxbh);
				}
			
		}
		
		return wrapLxz;
		
		
	}
	
	
	/**
	 * 删除要车明细
	 * @author 贺志国
	 * @date 2012-4-5
	 * @param params 要车明细IDs和用户中心
	 */
	@Transactional
	public void deleteYaoCMxBatch(Map<String,String> params,ArrayList<YAOCMx> list) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaohlOfIDs", params);
		for(YAOCMx ycmx: list ){
			params.put("CHEX", ycmx.getJIHCX());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateChelmx", params);
			//车辆明细表总车次
			String clmxZongcc = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("fenpeiyaocjh.selectChelmxZcc", params);
			//如果车辆明细表中的车型总车次为0，则删除车辆明细记录
			if("0".equals(clmxZongcc)){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteChelmx", params);
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaocjhzb", params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.delJihYHLmx", params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.delYaocmx", params);
		
	}
	
	
	/**
	 * 判断这天的要车计划是否完成
	 * @author 贺志国
	 * @date 2012-4-6
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> checkYaoCelJhZbIsComplete(Map<String, String> params) throws ServiceException
	{
		//要车计划总表总车次
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.queryShifwc",params);
	}
	
	/**
	 * 判断要车明细是否已分配
	 * @author 贺志国
	 * @date 2012-5-3
	 * @param param usercenter用户中心，id要车明细ID
	 * @return String 要车计划号
	 */
	public String checkYaocmxShiffp(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("fenpeiyaocjh.selectYaocjhhOfyaocmx", param);
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:50:45
	 * @param YAOCJHXH 要车计划序号
	 * @param ID 要车明细ID
	 * @description  更新要车计划明细的要车计划号
	 */
	@Transactional
	public void updateYaoCMx(Map<String,String> params ) throws ServiceException
	{
		//更新要车计划明细
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaoCMx",params ) ;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @param params {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @time 下午12:50:45
	 * @description  更新要车计划总表
	 */
	@Transactional
	public void updateYaoCZB(Map<String, String> params) throws ServiceException
	{
		//更新要车计划总表
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaoCZb",params ) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:52:34
	 * @param yao 要车计划bean
	 * @description   新增要车计划号
	 */
	@Transactional
	public void insertYAOCJH(Map<String,String> yaocjh) throws ServiceException
	{
		//新增要车计划明细
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.insertYAOCJH", yaocjh) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:57:07
	 * @description  新增配载计划
	 */
	@Transactional
	public void insertPeiZJH(Map<String,String> mapParams) throws ServiceException
	{
		//根据序列号生成配载计划号
		String peiZJH = this.getPeizdh(mapParams.get("UC"),String.valueOf( this.getSequence("seq_peizdh")),6,"P");
		mapParams.put("PEIZDH", peiZJH);
		//新增配载计划
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.insertPeiZJH", mapParams) ;
	}
	
	/**
	 * 返回指定格式的配载单号
	 * @author 贺志国
	 * @date 2012-7-26
	 * @param uc 用户中心
	 * @param xlh 序列号
	 * @param len 长度
	 * @return String PW0001
	 */
	public String getPeizdh(String uc,String xlh,int len,String pre){
		// 用户中心为空
		if (uc == null || uc.trim().length() <= 1){
			throw new ServiceException("用户中心为空,或者长度不正确!");
		}
		// 得到用户中心第二位字符
		String secondChar = uc.substring(1, 2);
		String pzdh = "";
		String xlhTemp = xlh;
		// 序列号不足4位补0
		if (xlhTemp.length() != 0) {
			while(xlhTemp.length()<len)
			{
				xlhTemp = "0"+xlhTemp;
			}
			pzdh = pre+secondChar+xlhTemp;
		}
		
		return pzdh;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午12:59:39
	 * @param yaohlmx
	 * @description  新增要贷令明细
	 */
	@Transactional
	public void insertYaoHLMX(Map<String, String> params) throws ServiceException
	{
		//新增要贷令明细
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.insertYaoHLMX",params ) ;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午01:02:07
	 * @param yysbm 运营商编码
	 * @return List<Map<String,String>>
	 * @description  查询车辆资源
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryCelZy(Map<String, String> params) throws ServiceException
	{
		//查询车辆资源
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.queryCelZy",params);
	}

	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午03:05:10
	 * @param params {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @return List<Map<String,String>> 
	 * @description  验证要车计划总表是否完成
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>>   checkYaoCelJhZbIsWc(Map<String, String> params) throws ServiceException
	{
		//查询车辆资源
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.checkYaoCelJhZbIsWc",params);
	}
	
	
	/**2012-02-22  重新分配要车计划 wangChong**/
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:20:44
	 * @param params  {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @description  删除0204要货令明细
	 */
	@Transactional
	public void   deleteYaohlmx(Map<String, String> params) throws ServiceException
	{
		//删除0204要货令明细
		 this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteYaohlmx",params);
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:20:56
	 * @param params  {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 删除0203配载计划 
	 */
	@Transactional
	public void   deletePeiZaiJh(Map<String, String> params) throws ServiceException
	{
		//删除0203配载计划
		 this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deletePeiZaiJh",params);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:21:06
	 * @param params  {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @description  删除0201要车计划
	 */
	@Transactional
	public void   deleteYaoCeJh(Map<String, String> params) throws ServiceException
	{
		//删除0201要车计划
		 this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteYaoCeJh",params);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:21:23
	 * @param params  {key :UC  用户中心,key:YAOCJHXH 要车计划序号}
	 * @description  置空0202要车明细表的要车序号
	 */
	@Transactional
	public void  updateYaoCeMxOfYaoCJHHToNull (Map<String, String> params) throws ServiceException
	{
		//置空0202要车明细表的要车序号
		 this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaoCeMxOfYaoCJHHToNull",params);
	}
	
	
	/**2012-02-22  重新计算要车计划  by wangChong**/
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:17:58
	 * @param params
	 *            {key :UC 用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 解除锁定配载
	 */
	@Transactional
	public void updateYaoHL(Map<String, String> params) throws ServiceException {
		// 解除锁定配载
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.updateYaoHL", params);
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:18:36
	 * @param params
	 *            {key :UC 用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 清空计 划要贷令
	 */
	@Transactional
	public void deleteJhYaoL(Map<String, String> params) throws ServiceException {
		// 清空计划要贷令
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteJhYaoL", params);
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:18:55
	 * @param params
	 *            {key :UC 用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 清空要车明细
	 */
	@Transactional
	public void deleteYaoCmx(Map<String, String> params) throws ServiceException {
		// 清空要车明细
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteYaoCmx", params);
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:19:28
	 * @param params
	 *            {key :UC 用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 清空车辆明细
	 */
	@Transactional
	public void deleteCelMx(Map<String, String> params) throws ServiceException {
		// 清空车辆明细
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteCelMx", params);
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:19:54
	 * @param params
	 *            {key :UC 用户中心,key:YAOCJHXH 要车计划序号}
	 * @description 清空 要车计划总表
	 */
	@Transactional
	public void deleteJHZb(Map<String, String> params) throws ServiceException {
		// 清空要车计划总表
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fenpeiyaocjh.deleteJHZb", params);
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:52:21
	 * @param params
	 * @return List<Map<String,String>>
	 * @description 统计每种车型的数量 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectCelXx(Map<String, String> params) throws ServiceException
	{
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fenpeiyaocjh.selectCelXx",params) ;
	}
	
	
	/**
	 * @author 王国首
	 * @email gswang
	 * @date 2013-9-6
	 * @time 下午03:52:21
	 * @param params
	 * @return List<Map<String,String>>
	 * @description 检查如果配置计划开始执行，不允许重新分配此要车计划
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPeizjhzt(Map<String, String> params) throws ServiceException
	{
		String re = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("fenpeiyaocjh.checkPeizjhzt", params);
		return "0".equals(re) ? true : false;
	}
	
}

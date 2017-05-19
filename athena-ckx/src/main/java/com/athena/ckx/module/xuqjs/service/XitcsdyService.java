package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 系统参数定义
 * @author denggq
 * 2012-3-22
 */
@Component
public class XitcsdyService extends BaseService<Xitcsdy>{
	
	@Inject
	private  CacheManager cacheManager;//缓存
	
	private static Map<String,String> map;
	
	public String getCacheName(String zidlx){
		return XitcsdyService.getInstance().get(zidlx)==null?"":XitcsdyService.getInstance().get(zidlx);	
	}
	
	public static Map<String,String> getInstance(){
		if(null != map){
			return map;
		}
		map = new HashMap<String,String>();
		map.put("LJLX", "queryLjlx");
		map.put("CKLX", "queryCklx");
		map.put("BHLX", "queryBHLX");
		map.put("DGDW", "queryDGDW");
		map.put("XQ", "queryXQ");
		map.put("CZLX", "queryCzlx");
		map.put("BZLB", "queryBzlb");
		map.put("LXJLB", "queryLxjlb");
		map.put("BFFX", "queryBffx");
		map.put("ZZLX", "queryZZLX");

		map.put("LJSX", "queryLjsx");
		map.put("DWLX", "queryLjdw");
		map.put("GUANJLJJB", "queryGuanjljjb");
		map.put("GSLX", "queryGslx");
		map.put("DINGHLX", "queryDinghlx");
		map.put("DHZQ", "queryDinghzq");
		map.put("GHMS", "queryGonghms");
		map.put("PSBS", "queryPeisbz");
		map.put("XUQLY", "queryXuqly");
		map.put("WGMS", "queryWaibghms");

		map.put("YLGX", "queryYilgx");
		map.put("JSFF", "queryJisff");
		map.put("CKMS", "queryCKMS");
		map.put("YSMS", "queryYSMS");
		map.put("NBMS", "queryNBMS");
		map.put("WBMS", "queryWBMS");
		map.put("SXMS", "querySXMS");
		map.put("XDMS", "queryXDMS");
		map.put("NIANXM", "queryNianxm");
		
		//0007100 部门
		map.put("DEPT", "queryAllDept");
		
		//新增模式
		map.put("KBWBMS", "queryKBWBMS");
		map.put("KBNBMS", "queryKBNBMS");
		map.put("GHMS1", "queryGHMS1");
		map.put("GHMS2", "queryGHMS2");
		map.put("GHMS3", "queryGHMS3");
		map.put("GHMS4", "queryGHMS4");
		map.put("GHMS5", "queryGHMS5");
		map.put("LSDDLB", "queryLSDDLB");
		//mantis:0013085 是否混托 by csy 20170104
		map.put("SFHT", "querySFHT");
		
		//0012510 需求来源
		map.put("XQLY", "convertXqlyForChn");
		
		return map;
	}
	
	
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存系统参数定义
	 * @param xitcsdy
	 * @param userId
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(Xitcsdy xitcsdy, Integer operant, ArrayList<Xitcsdy> insert,ArrayList<Xitcsdy> edit,ArrayList<Xitcsdy> delete,String userId) throws ServiceException{
		if(2 == operant){
			inserts(xitcsdy,insert,userId);
			edits(xitcsdy,edit,userId);
			deletes(xitcsdy,delete,userId);
		}
		String cacheName = getCacheName(xitcsdy.getZidlx());
		if(!"".equals(cacheName) && null != cacheName){
			cacheManager.refreshCache(cacheName);//刷新缓存 
		}	
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-3-19
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(Xitcsdy bean,List<Xitcsdy> insert,String userId)throws ServiceException{

		for(Xitcsdy xitcsdy:insert){
//			checkBZZTOrZTSX(bean.getZidlx(),xitcsdy);
			xitcsdy.setZidlx(bean.getZidlx());			//字典类型
			xitcsdy.setZidlxmc(bean.getZidlxmc());		//字典类型名称
			xitcsdy.setCreator(userId);
			xitcsdy.setCreate_time(DateTimeUtil.getAllCurrTime());
			xitcsdy.setEditor(userId);
			xitcsdy.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXitcsdy",xitcsdy);
		}
		return "";
	}
	/**
	 * 检测BZZT 包装状态  类型可以填写的值
	 * ZTSX 状态属性 类型可以填写的值
	 * @param zidlx
	 * @param zhidbm
	 */
	/*
	private void checkBZZTOrZTSX(String zidlx,Xitcsdy xitcsdy){
		Map<String,String> checkBZZTOrZTSX = new HashMap<String, String>();
		//BZZT 包装状态  类型可以填写的值
		checkBZZTOrZTSX.put("BZZT",   "包装状态");
		checkBZZTOrZTSX.put("BZZT4",  "库存");
		checkBZZTOrZTSX.put("BZZT13", "质检");
		checkBZZTOrZTSX.put("BZZT5",  "收货改包装");
		checkBZZTOrZTSX.put("BZZT6",  "等待交付");
		checkBZZTOrZTSX.put("BZZT8",  "不合格品");
		checkBZZTOrZTSX.put("BZZT9",  "转不合格品");
		checkBZZTOrZTSX.put("BZZT10", "退供应商");
		checkBZZTOrZTSX.put("BZZT11", "退供应商维修");
		checkBZZTOrZTSX.put("BZZT12", "待消耗");		
		
		//ZTSX 状态属性 类型可以填写的值
		checkBZZTOrZTSX.put("ZTSX",   "状态属性");
		checkBZZTOrZTSX.put("ZTSX1",  "正常");
		checkBZZTOrZTSX.put("ZTSX2",  "冻结");
		checkBZZTOrZTSX.put("ZTSX3",  "收货质检");
		checkBZZTOrZTSX.put("ZTSX4",  "库内质检");
		checkBZZTOrZTSX.put("ZTSX5",  "挑选返修");
		checkBZZTOrZTSX.put("ZTSX6",  "工废");
		checkBZZTOrZTSX.put("ZTSX7",  "随废");
		checkBZZTOrZTSX.put("ZTSX8",  "料废");
		checkBZZTOrZTSX.put("ZTSX9",  "移库备货");
		checkBZZTOrZTSX.put("ZTSX10", "返修");
		checkBZZTOrZTSX.put("ZTSX11", "仓库-仓库");
		
		if(checkBZZTOrZTSX.get(zidlx) != null ){
			if(checkBZZTOrZTSX.get(zidlx+xitcsdy.getZidbm()) == null){
				throw new ServiceException("操作非法，不能添加字典类型："+zidlx+",字典编码："+xitcsdy.getZidbm()+"的数据");
			}else{
				xitcsdy.setZidmc(checkBZZTOrZTSX.get(zidlx+xitcsdy.getZidbm()));
			}
		}
	}*/
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(Xitcsdy bean,List<Xitcsdy> edit,String userId) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateZidlxmc",bean);//字典类型名称
		for(Xitcsdy xitcsdy:edit){
			xitcsdy.setZidlx(bean.getZidlx());			//字典类型
			xitcsdy.setZidlxmc(bean.getZidlxmc());
			xitcsdy.setEditor(userId);
			xitcsdy.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXitcsdy",xitcsdy);
		}
		return "";
	}
	
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(Xitcsdy bean,List<Xitcsdy> delete,String userId)throws ServiceException{
		for(Xitcsdy xitcsdy:delete){
			xitcsdy.setZidlx(bean.getZidlx());			//字典类型
			xitcsdy.setEditor(userId);
			xitcsdy.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXitcsdy",xitcsdy);
		}
		return "";
	}
	
	/**
	 * 单条记录删除
	 * @param xitcsdy
	 * @return String
	 * @author denggq
	 * @time 2012-3-19
	 */
	@Transactional
	public String doDelete(Xitcsdy xitcsdy) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXitcsdy", xitcsdy);
		String cacheName = getCacheName(xitcsdy.getZidlx());
		if(!"".equals(cacheName) && null != cacheName){
			cacheManager.refreshCache(cacheName);//刷新缓存 
		}
		return "deletesuccess";
	}

	
	/**
	 * 复制
	 * @param String,String
	 * @return String
	 * @author denggq
	 * @time 2012-3-20
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String copy(String yusercenter,String zidlx, String xusercenter, String userId) {
		
		//原用户中心下原字典类型是否存在
		Xitcsdy old = new Xitcsdy();//系统参数定义实体类
		old.setUsercenter(yusercenter);//用户中心
		old.setZidlx(zidlx);//字典类型
		List<Xitcsdy> oldList=(List<Xitcsdy>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXitcsdy", old);
		if(0 == oldList.size()){
			throw new ServiceException( GetMessageByKey.getMessage("yuanusercenetr")+yusercenter+GetMessageByKey.getMessage("xzidlx")+zidlx+GetMessageByKey.getMessage("bcz"));
		}
		
		//现用户中心下原字典类型是否已存在
		Xitcsdy news = new Xitcsdy();//系统参数定义实体类
		news.setUsercenter(xusercenter);//用户中心
		news.setZidlx(zidlx);//字典类型
		List<Xitcsdy> newList=(List<Xitcsdy>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXitcsdy", old);
		if(0 != newList.size()){
			throw new ServiceException( GetMessageByKey.getMessage("xianusercenter")+xusercenter+GetMessageByKey.getMessage("xzidlx")+zidlx+GetMessageByKey.getMessage("ycz"));
		}
		
		for(Xitcsdy xitcsdy:oldList){
			xitcsdy.setUsercenter(xusercenter);
			xitcsdy.setCreator(userId);
			xitcsdy.setCreate_time(DateTimeUtil.getAllCurrTime());
			xitcsdy.setEditor(userId);
			xitcsdy.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXitcsdy", xitcsdy);
		}
		return "copysuccess";
	}

	
	/**
	 * 获得字典类型名称
	 * @return String
	 * @author denggq
	 * @time 2012-3-20
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Xitcsdy> getSelectTeamCode(Xitcsdy bean) {
//		List<Xitcsdy> list =new ArrayList<Xitcsdy>();
//		List<Xitcsdy> slist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.getSelectTeamCode",bean);
//		for (Xitcsdy s : slist) {//字典类型信息集合
//			Xitcsdy c=new Xitcsdy();
//			c.setZidlx(s.getZidlx());
//			c.setZidlxmc(s.getZidlxmc());
//			list.add(c);
//		}
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZidlxmc", bean);
	}
	
}

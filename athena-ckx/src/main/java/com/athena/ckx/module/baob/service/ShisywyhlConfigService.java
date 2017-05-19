package com.athena.ckx.module.baob.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.JihuChengys;
import com.athena.ckx.entity.baob.Shisywyhl;
import com.athena.ckx.entity.baob.Yanwyhlconfig;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

@Component
public class ShisywyhlConfigService extends BaseService<Yanwyhlconfig> {
 //11458
	@Override
	protected String getNamespace() {
		return "baob";
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> query(Yanwyhlconfig bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryConfig",bean);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,String>> queryCangkByUsercenter(String usercenter) throws ServiceException {
		// TODO Auto-generated method stub
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryCangkByUsercenter",usercenter);;
		if(0 == list.size()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("KEY", "");
			map.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add(map);
		}
		return list;
	}
	
	public String save(Yanwyhlconfig bean)
	{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts.ckx.saveConfig",bean);
		return "success";
	}

	@SuppressWarnings({ "unchecked", "null" })
	public List<HashMap<String, String>> queryFactoryByUsercebter(String usercenter) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryFactoryByUsercebter",usercenter);;
		if(0 == list.size()){
			Map<String,String> map1 = new HashMap<String,String>();
			map1.put("KEY", "");
			map1.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add((HashMap<String, String>) map1);
			return list;
		}
		return list;
	}

	public Map<String,Object> queryYwyhlByParam(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
		//对要货令状态进行处理 原数据
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts.ckx.queryYwyhlByParam", bean, bean.getCurrentPage(),bean.getPageSize());
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object>   getBaobiaoByJihy(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
        return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts.ckx.queryNumGroupByJihy", bean,bean.getCurrentPage(),bean.getPageSize());
	}
	
	
//	@SuppressWarnings("unchecked")
//	public List<JihuChengys>   getBaobiaoByJihy(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
//		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());	
//		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryNumGroupByJihy", bean);
//	}
	
	@SuppressWarnings("unchecked")
	public List<JihuChengys>   getBaobiaoByChengys(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
        return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryNumGroupByChengys", bean);
	}

	public List getFactorys(String usercenter) {
		// TODO Auto-generated method stub
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryFactory",usercenter);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String,String>> queryFactoryNameByBs(String string) {
		// TODO Auto-generated method stub
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryFactoryNameByBs",string);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>   getBaobiaoByJihy_export(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<JihuChengys> rows = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryNumGroupByJihy", bean);
		resultMap.put("rows", rows);
		resultMap.put("total", rows.size());
        return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>   getBaobiaoByChengys_export(Yanwyhlconfig bean) {
		// TODO Auto-generated method stub
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
        Map<String, Object> resultMap = new HashMap<String, Object>();
		List<JihuChengys> rows = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts.ckx.queryNumGroupByChengys", bean);
		resultMap.put("rows", rows);
		resultMap.put("total", rows.size());
		return resultMap;
	}
}

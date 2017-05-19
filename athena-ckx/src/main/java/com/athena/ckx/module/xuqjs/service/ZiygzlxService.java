package com.athena.ckx.module.xuqjs.service;


import com.athena.ckx.entity.xuqjs.Ziygzlx;
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
 * 资源跟踪类型对应表Service
 * @author denggq
 * @date 2012-4-17
 */
@Component
public class ZiygzlxService extends BaseService<Ziygzlx>{
	@Inject
	private  CacheManager cacheManager;//缓存
	
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 单数据保存
	 * @author denggq
	 * @date 2012-5-21
	 * @param bena
	 * @return String
	 * */
	@Transactional
	public String save(Ziygzlx bean,Integer operant,String userName)throws ServiceException{
		
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		if(1 == operant){//增加
			
			//计算类型编号是否存在
			String jslxmc = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getJslxmcByjislxbh", bean.getJislxbh());
			if(null != jslxmc && !jslxmc.equals(bean.getJslxmc())){
				throw new ServiceException(GetMessageByKey.getMessage("jslxbh")+bean.getJislxbh()+GetMessageByKey.getMessage("mcbx")+jslxmc+"]");
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertZiygzlx", bean);//增加数据库
			 
		}else{//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateZiygzlx", bean);//修改数据库
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateJslxmcByjislxbh", bean);//修改数据库
		}
		cacheManager.refreshCache("cacheJslx");//刷新缓存 
		return "success";
	}

	/**
	 * 单数据删除
	 * @author denggq
	 * @date 2012-5-21
	 * @param bena
	 * @return String
	 * */
	@Override
	public String doDelete(Ziygzlx bean) throws ServiceException {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteZiygzlx", bean);//删除数据库
		return "delSuccess";
	}
	
}

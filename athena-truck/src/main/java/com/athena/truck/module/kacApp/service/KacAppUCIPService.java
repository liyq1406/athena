package com.athena.truck.module.kacApp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Ucip;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 用户中心坐标IP
 * @author CSY
 * 2016-9-7
 */
@Component
public class KacAppUCIPService extends BaseService<Ucip>{
	
	static Logger log = Logger.getLogger(KacAppUCIPService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "kc_app";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @author CSY
	 * @date 2016-09-07	 
	 */
	public Map query(Ucip bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kc_app.getUCIP",bean,bean);
	}
	
	/**
	 * 保存
	 * @param list
	 * @param username
	 * @return String
	 * @author CSY
	 * @time 2016-9-7
	 */
	@Transactional
	public String save(ArrayList<Ucip> insert,ArrayList<Ucip> edit, ArrayList<Ucip> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		log.info("参考系-卡车入厂-用户中心坐标IP-删除数据");
		deletes(delete,username);
		log.info("参考系-卡车入厂-用户中心坐标IP-增加数据");
		inserts(insert,username);
		log.info("参考系-卡车入厂-用户中心坐标IP-修改数据");
		edits(edit,username);
		log.info("参考系-卡车入厂-刷新用户中心坐标IP缓存");
		cacheManager.refreshCache("queryUserCenterMap");//刷新缓存
		return "success";
	}
	
	/**
	 * 私有批量增加方法
	 * @author CSY
	 * @date 2016-9-7
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Ucip> insert,String username)throws ServiceException{
		for(Ucip bean:insert){
			bean.setCreator(username);
			bean.setEditor(username);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.insertUCIP",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author CSY
	 * @date 2016-9-7
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String edits(List<Ucip> edit,String username) throws ServiceException{
		for(Ucip bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.updateUCIP",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author CSY
	 * @date 2016-9-9
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Ucip> delete,String username)throws ServiceException{
		for(Ucip bean:delete){
			bean.setEditor(username);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.deleteUcip",bean);
		}
		return "";
	}
	
}

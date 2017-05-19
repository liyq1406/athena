package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.cangk.Zerzt;
import com.athena.ckx.module.xuqjs.service.UsercenterService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 责任主体
 * @author wangyu
 * @date 2014-2-19
 */
@Component
public class ZerztService extends BaseService<Zerzt>{
	
	static Logger log = Logger.getLogger(UsercenterService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存包装
	 * @param list
	 * @param username
	 * @return String
	 * @author denggq
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(ArrayList<Zerzt> insert,ArrayList<Zerzt> edit, ArrayList<Zerzt> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		log.info("参考系-责任主体-删除数据");
		deletes(delete,username);
		log.info("参考系-责任主体-增加数据");
		inserts(insert,username);
		log.info("参考系-责任主体-修改数据");
		edits(edit,username);
		log.info("参考系-刷新用户中心缓存");
		cacheManager.refreshCache("queryUserCenterMap");//刷新缓存
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Zerzt> insert,String username)throws ServiceException{
		for(Zerzt bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertZerzt",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<Zerzt> edit,String username) throws ServiceException{
		for(Zerzt bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateZerzt",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-19
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Zerzt> delete,String username)throws ServiceException{
		for(Zerzt bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteZerzt",bean);
		}
		return "";
	}
	
	
}

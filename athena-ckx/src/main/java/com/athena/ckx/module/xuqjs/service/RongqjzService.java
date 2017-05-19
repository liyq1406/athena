package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Rongqjz;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 用户中心|容器记账
 * @author xss
 * 2015-2-3
 * 0010495
 */
@Component
public class RongqjzService extends BaseService{
	
	static Logger log = Logger.getLogger(RongqjzService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	public Map queryRongqjz(Rongqjz rongqjz){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("rongqjz.queryRongqjz",rongqjz,rongqjz);
	}

	public Rongqjz selectRongqjz(Rongqjz rongqjz){
		return (Rongqjz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("rongqjz.queryRongqjz",rongqjz,rongqjz);
	}
	
	public int deleteRongqjz(Rongqjz rongqjz){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqjz.deleteRongqjz",rongqjz);

	}
	

	public int saveRongqjz(Rongqjz rongqjz){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqjz.saveRongqjz", rongqjz);
	}
	
	public int updateRongqjz(Rongqjz rongqjz){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqjz.updateRongqjz", rongqjz);
	}

	
}

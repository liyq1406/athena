package com.athena.ckx.module.xuqjs.service;

import java.util.List;

import com.athena.ckx.entity.xuqjs.Cmj;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * CMJ
 * @author denggq
 * @date 2012-3-26
 */
@Component
public class CmjService extends BaseService<Cmj>{

	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-3-26
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 保存
	 * @author denggq
	 * @date 2012-4-5
	 * @param userId
	 * @return String
	 */
	@Transactional
	public String calculateCmj(String userId) throws ServiceException{
		
		Cmj bean = new Cmj();
		bean.setJisrq(DateTimeUtil.getCurrDate());
		bean.setCreator(userId);		
		bean.setEditor(userId);		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteTempCmj");//清空CMJ临时表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCmj");//清空CMJ表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertTempCmj",bean);//按产线归集cmj总量：临时表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCmjByChanx",bean);//按产线归集cmj
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCmjByCangk",bean);//按仓库归集cmj
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckKucl",bean);// Bug 0002467 根据CMJ更新零件仓库安全库存和最大库存
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjxhdKucl",bean);// Bug 0002467  根据CMJ更新零件消耗点安全库存
		return "success";
	}
	
	
	/**
	 * 获得多个CMJ
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Cmj bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCmj",bean);
	}
	
}

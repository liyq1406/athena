package com.athena.ckx.module.cangk.service;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.ChengpkJihyz;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 仓库
 * @author denggq
 * @date 2012-1-12
 */
@Component
public class ChengpkJihyzService extends BaseService<ChengpkJihyz> {
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-8-15
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-8-15
	 * @return bean
	 */
	@Transactional
	public String save(ChengpkJihyz bean , Integer operant ,String userId) throws ServiceException{
		
		//一个用户中心下，仓库和计划员组关系一对一
		ChengpkJihyz c = new ChengpkJihyz();
		c.setUsercenter(bean.getUsercenter());
		c.setJihyzbh(bean.getJihyzbh());
		Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryChengpkJihyz",c);
		if(null != obj){
			throw new ServiceException(GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("jihuayzbh")+bean.getJihyzbh()+GetMessageByKey.getMessage("yicunzai"));
		}
		
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		if (1 == operant){
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertChengpkJihyz", bean);
		}else if(2 == operant){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateChengpkJihyz", bean);
		}
		return "success";
	}
	
	/**
	 * 删除
	 * @author denggq
	 * @param bean
	 * @date 2012-8-15
	 * @return String
	 */
	@Transactional
	public String doDelete(Cangk bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteChengpkJihyz", bean);
		return "deletesuccess";
	}

}

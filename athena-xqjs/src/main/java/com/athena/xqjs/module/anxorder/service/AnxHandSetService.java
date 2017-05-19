package com.athena.xqjs.module.anxorder.service;

import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.AnxHandSet;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;


/**
 * 按需手动下发设置
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-7-7
 */
@Component
public class AnxHandSetService extends BaseService<AnxHandSet> {

	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "anxhand";
	}
	
	/**
	 * 
	 * 按需手动下发设置流水查询
	 * @param pageable
	 * 
	 * @return map
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anxhand.queryAnxHandSet", param, page);
	}

	/**
	 * 更新订单状态
	 * @author 贺志国
	 * @date 2015-7-9
	 * @param param
	 */
	public int updateDindzt(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anxhand.updateDingdzt", param);
		
	}
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2015-7-9
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public String saveHandSet(AnxHandSet bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anxhand.insertAnxHandSet", bean);
		return "success";
	}
	
	
}

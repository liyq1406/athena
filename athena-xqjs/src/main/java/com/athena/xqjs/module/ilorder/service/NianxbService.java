package com.athena.xqjs.module.ilorder.service;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;


/**
 * <p>
 * Title:年型类
 * </p>
 * <p>
 * Description:年型类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-13
 */
@Component("orderNumService")
public class NianxbService extends BaseService{
	
	/**
	 * 获取年型的实体方法
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public Nianxb getNian(Nianxb bean){
		Nianxb nx = new Nianxb() ;
		CommonFun.objPrint(bean, "年型参数类");
		CommonFun.logger.debug("获取年型的实体getNian方法的sql语句为：Nianxb.queryNianx");
		nx = (Nianxb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("Nianxb.queryNianx",bean) ;
		return nx;
		
	}
	
	
}

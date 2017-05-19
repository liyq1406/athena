package com.athena.xqjs.module.ilorder.service;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:外部要货令
 * </p>
 * <p>
 * Description:外部要货令
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李智
 * @version v1.0
 * @date 2012-02-14
 */
@Component
public class IlYaohlService extends BaseService {

	/**
	 * 更新要货令状态，返回
	 * 
	 * @author 李智
	 * @version v1.0
	 * @date 2012-02-14 参数说明：Yaohl bean 外部要货令
	 */
	public String updateYaohlZt(Yaohl bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateYaohlZt", bean);
		return "";
	}
}
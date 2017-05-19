package com.athena.xqjs.module.ilorder.service;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:资源快照
 * </p>
 * <p>
 * Description:资源快照
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
 * @date 2011-12-09
 */

@Component
public class ZiykzbService extends BaseService {

	/**
	 * 查找最早的时间
	 **/
	public String getFirstDay() {
		return CommonFun.strNull(this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryFirstDay"));
	}

}
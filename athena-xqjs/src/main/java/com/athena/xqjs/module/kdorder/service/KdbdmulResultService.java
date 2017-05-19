package com.athena.xqjs.module.kdorder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kdorder.KdbdmulResult;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:比对目录结果
 * </p>
 * <p>
 * Description:比对目录结果
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 陈骏
 * @version v1.0
 * @date 2012-1-4
 */
@Component
public class KdbdmulResultService extends BaseService {
	
	public Map<String, Object> bidResult(Pageable page, Map<String, String> param) {
		// ArrayList list = (ArrayList)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKdbdmlRes");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kdorder.queryKdbdmlRes", param, page);

	}
	public List selectResult() {
		return (ArrayList)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKdbdmlRes");
		

	}
	public void insertRes(KdbdmulResult kdbdmulresult) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdbdmlRes", kdbdmulresult);
	}
	/**
	 * <p>
	 * Title:清理结果表
	 * </p>
	 * <p>
	 * Description:比对目录结果表
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public void deleteKdbdmlResult() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteKdbdmlresult");
	}
}

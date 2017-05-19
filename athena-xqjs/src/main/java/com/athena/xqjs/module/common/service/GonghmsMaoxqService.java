package com.athena.xqjs.module.common.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.GonghmsMaoxq;
import com.toft.core3.container.annotation.Component;

/**
 * 供货模式-毛需求
 * @author 李智
 * @version v1.0
 * @date 2012-4-12
 */

@SuppressWarnings("rawtypes")
@Component
public class GonghmsMaoxqService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<GonghmsMaoxq> queryGonghmsMaoxq(Map<String,String> map){
		return (List<GonghmsMaoxq>)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryGonghmsMaoxq", map);
	}

	@SuppressWarnings("unchecked")
	public List<GonghmsMaoxq> queryGonghmsMaoxqDhlx(Map<String, String> map) {
		return (List<GonghmsMaoxq>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryGonghmsMaoxqDhlx", map);
	}
}
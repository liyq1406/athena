package com.athena.xqjs.module.maoxq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.maoxq.JLMaoxq;
import com.toft.core3.container.annotation.Component;

@Component
public class JLMaoxqService extends BaseService<JLMaoxq>{

	/**
	 * 查询JL毛需求
	 * @param params
	 * @param bean
	 * @return
	 */
	public Map<String, Object> selectJLMxq(Map<String, String> params, JLMaoxq bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if("exportXls".equals(params.get("exportXls"))){
				List<Object> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.select("jlmxq.queryJLMaoxq", bean);
				map.put("total", list.size());
				map.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					map.put("total", list.size());
					map.put("rows", list);
				}
			}else{
				map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.selectPages("jlmxq.queryJLMaoxq", bean, bean);
			}
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return map;
	}
}

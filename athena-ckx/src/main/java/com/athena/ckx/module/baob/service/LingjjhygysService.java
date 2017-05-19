package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.ckx.entity.baob.Lingjjhygys;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@Component
public class LingjjhygysService extends BaseService<Lingjjhygys> {
	
	
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Lingjjhygys bean,String exportXls){
		if("exportXls".equals(exportXls)){
			if(StringUtils.isNotEmpty(bean.getChengysbh())){
				bean.setChengysbh(bean.getChengysbh().replace("%20%20", "  "));
			}
			if(StringUtils.isNotEmpty(bean.getGongysbh())){
				bean.setGongysbh(bean.getGongysbh().replace("%20%20", "  "));
			}
			List<Lingjjhygys> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querylingjjhygys",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querylingjjhygys",bean,bean);
	}
	
	
}

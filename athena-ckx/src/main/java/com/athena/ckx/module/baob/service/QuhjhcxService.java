package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Quhjhcx;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

@Component
public class QuhjhcxService extends BaseService<Quhjhcx> {
	
	
	
	/**
	 * 获得命名空间
	 * @author lc
	 * @date 2016-11-24
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author lc
	 * @date 2016-11-24
	 */
	public Map<String,Object> query(Quhjhcx bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryqhjhcx",bean,bean);
	}	
	@SuppressWarnings("unchecked")
	public List<Quhjhcx> listImport(Quhjhcx bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryqhjhcx",bean);
	}
	
	/**
	 * 查询数据条数
	 * @author lc
	 * @date 2017-01-12
	 * @param params
	 * @return
	 */
	public int selectQuhjhcxCount(Map<String, String> params) {
		return (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryqhjhcxCount", params);
	}
	
	/**
	 * 分页查询在途跟踪
	 * @param bean
	 * @author lc
	 * @date 2016-11-25
	 */
	public Map<String,Object> queryzaitgzcx(Quhjhcx bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryztgzcx",bean,bean);			
	}
	@SuppressWarnings("unchecked")
	public List<Quhjhcx> listImportZaitgz(Quhjhcx bean){
		List<Quhjhcx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryztgzcx_download",bean);
		if(list.size() > 5000){
			list.clear();
		}
		return list;
	}
	
	/**
	 * 分页查询供应商取货计划完成率计算
	 * @param bean
	 * @author lc
	 * @date 2016-11-26
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> querygysqhjhwcl(Quhjhcx bean,String exportXls) throws ServiceException {
		Map<String,Object> m = new HashMap<String, Object>();
		try{
			if("1".equals(bean.getMonth())){
				bean.setStartmonth("01");
				bean.setEndmonth("03");
			}else if("2".equals(bean.getMonth())){
				bean.setStartmonth("04");
				bean.setEndmonth("06");
			}else if("3".equals(bean.getMonth())){
				bean.setStartmonth("07");
				bean.setEndmonth("09");
			}else if("4".equals(bean.getMonth())){
				bean.setStartmonth("10");
				bean.setEndmonth("12");
			}else{
				bean.setStartmonth(bean.getMonth());
				bean.setEndmonth(bean.getMonth());
			}
			if("exportXls".equals(exportXls)){
				List<Quhjhcx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querygysqhjhwcl",bean);			
				m.put("total", list.size());
				m.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					m.put("total", list.size());
					m.put("rows", list);
				}				
			}else{
				m = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querygysqhjhwcl",bean,bean);
			}			
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return m;
	}
			
}

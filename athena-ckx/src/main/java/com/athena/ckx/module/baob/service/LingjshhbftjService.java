package com.athena.ckx.module.baob.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Lingjshhbftj;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

@Component
public class LingjshhbftjService extends BaseService<Lingjshhbftj> {
	
	
	
	/**
	 * 获得命名空间
	 * @author lc
	 * @date 2016-11-1
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author lc
	 * @date 2016-11-1
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> query(Lingjshhbftj bean,String exportXls) throws ServiceException {
		Map<String,Object> m = new HashMap<String, Object>();
		try{
			if("exportXls".equals(exportXls)){
				List _list = new ArrayList();
				List<Lingjshhbftj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryljshhbftj",bean);			
				m.put("total", list.size());
				m.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					m.put("total", list.size());
					m.put("rows", list);
				}
				for(int i=0;i<list.size();i++){
					Lingjshhbftj ljshhbftj = (Lingjshhbftj)list.get(i);
					if("1".equals(ljshhbftj.getLingjzt())){
						ljshhbftj.setLingjzt("收货");
					}else if("2".equals(ljshhbftj.getLingjzt())){
						ljshhbftj.setLingjzt("报废");
					}
					_list.add(ljshhbftj);
				}
				m.put("total", _list.size());
				m.put("rows", _list);
			}else{
				m = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryljshhbftj",bean,bean);
			}			
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return m;
	}
	
	
	/**
	 * 分页查询收货明细
	 * @param bean
	 * @author lc
	 * @date 2016-11-4
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> queryshouhmx(Lingjshhbftj bean,String exportXls) throws ServiceException {
		Map<String,Object> m = new HashMap<String, Object>();
		try{
			if("exportXls".equals(exportXls)){
				List<Lingjshhbftj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryshouhmx",bean);			
				m.put("total", list.size());
				m.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					m.put("total", list.size());
					m.put("rows", list);
				}
			}else{
				m = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryshouhmx",bean,bean);
			}			
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return m;
	}
	
	
	/**
	 * 分页查询报废明细
	 * @param bean
	 * @author lc
	 * @date 2016-11-7
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> querybaofmx(Lingjshhbftj bean,String exportXls) throws ServiceException {
		Map<String,Object> m = new HashMap<String, Object>();
		try{
			if("exportXls".equals(exportXls)){
				List<Lingjshhbftj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querybaofmx",bean);			
				m.put("total", list.size());
				m.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					m.put("total", list.size());
					m.put("rows", list);
				}
			}else{
				m = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querybaofmx",bean,bean);
			}			
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return m;
	}
}

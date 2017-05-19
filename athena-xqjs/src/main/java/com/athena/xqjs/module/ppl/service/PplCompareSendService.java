package com.athena.xqjs.module.ppl.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.entity.maoxq.CompareQr;
import com.athena.xqjs.entity.ppl.ComparePpl;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Component
public class PplCompareSendService extends BaseService<CompareQr> {

	private final Log log = LogFactory.getLog(PplCompareSendService.class);

	
	//xss 20161017 v4_008 需求比较结果查询
	public Map<String, Object> select(CompareQr bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ppl.queryCompareQr", bean, bean);
	}
	
	//更新数据状态
	@Transactional
	public String doDelete(CompareQr bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deleteCompareQr",bean);//更新数据库状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deleteCompareQr_Mx",bean);//更新数据库状态	
		return "成功";
	}  
	

	//相同的版次、基准版次对比结果 是否已经发送给外部系统
	public boolean maoxqSffs(Map<String, String> map) { 
		boolean flag = false;
		CompareCyc bean = (CompareCyc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.maoxqSffs", map);
		 
	    if( bean!=null ){
		   flag = true; 
		} 
		
		return flag;
	}
	
	
	
	//查询是否存在相同的版次1、版次2、基准版次
	public boolean queryXuqbcJizExists(Map<String, String> map) {
		boolean flag = false;
		 
		CompareCyc bean = (CompareCyc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.queryXuqbcJizExists", map);
		
	    if( bean!=null ){
		   flag = true; 
	    } 
			
		return flag;
	}
	
	//删除原有 相同的版次1、版次2、基准版次 数据
	@Transactional
	public boolean doDeleteXuqbcJizExists(Map<String, String> map) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.doDeleteXuqbcJizExists",map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.doDeleteXuqbcJizExists_Mx",map);
		
		return true;
	}  
	
	
	
	//插入数据
	@Transactional
	public boolean insertCompareQr(List<ComparePpl> ls,Map<String, String> map,String actioner,String time) throws ServiceException{ 
		for (int i = 0;i<ls.size(); i++) {
			ComparePpl bean = ls.get(i);
			bean.setCreate_time(time);
			bean.setCreator(actioner); 
			 
			bean.setXuqbc1(map.get("xuqbc1"));
			bean.setXuqbc2(map.get("xuqbc2")); 
			bean.setJiz(map.get("xuqbc1"));
			
			map.put("usercenter", bean.getUsercenter()); 
			map.put("creator", bean.getCreator()); 
			map.put("create_time", bean.getCreate_time()); 
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertCompareQrMx",bean);
		}
		
		   baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertCompareQr",map);
		
		return true;
	}  
	
		
}

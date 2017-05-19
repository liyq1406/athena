package com.athena.xqjs.module.maoxq.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.entity.maoxq.CompareQr;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Component
public class MaoxqCompareSendService extends BaseService<CompareQr> {

	private final Log log = LogFactory.getLog(MaoxqCompareSendService.class);

	
	//xss 20161017 v4_008毛需求比较结果查询
	public Map<String, Object> select(CompareQr bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.queryCompareQr", bean, bean);
	}
	
	//更新数据状态
	@Transactional
	public String doDelete(CompareQr bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteCompareQr",bean);//更新数据库状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteCompareQr_Mx",bean);//更新数据库状态
		return "成功";
	}  
	
	
	//相同的版次、基准版次、比较方式的对比结果 是否已经发送给外部系统
	public boolean maoxqSffs(Map<String, String> map) { 
		boolean flag = false;
		CompareCyc bean = (CompareCyc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.maoxqSffs", map);
		 
	    if( bean!=null ){
		   flag = true; 
		} 
		
		return flag;
	}
	
	
	
	//查询是否存在相同的版次1、版次2、基准版次、比较方式
	public boolean queryXuqbcJizExists(Map<String, String> map) {
		boolean flag = false;
		 
		CompareCyc bean = (CompareCyc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.queryXuqbcJizExists", map);
		
	    if( bean!=null ){
		   flag = true; 
	    } 
			
		return flag;
	}
	
	//删除原有 相同的版次1、版次2、基准版次 数据
	@Transactional
	public boolean doDeleteXuqbcJizExists(Map<String, String> map) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.doDeleteXuqbcJizExists",map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.doDeleteXuqbcJizExists_Mx",map);
		
		return true;
	}  
	
	
	
	//插入数据
	@Transactional
	public boolean insertCompareQr(List<CompareCyc> ls,Map<String, String> map,String actioner,String time) throws ServiceException{ 
		for (int i = 0;i<ls.size(); i++) {
			CompareCyc bean = ls.get(i);
			bean.setCreate_time(time);
			bean.setCreator(actioner); 
			bean.setXuqbc(map.get("xuqbc"));
			bean.setXuqbc1(map.get("xuqbc1")); 
			bean.setXsfs(map.get("xsfs"));  
			
			map.put("usercenter", bean.getUsercenter()); 
			map.put("chanx", bean.getChanx());
			map.put("creator", bean.getCreator()); 
			map.put("create_time", bean.getCreate_time()); 
			map.put("xsfs", bean.getXsfs()); 
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insertCompareQrMx",bean);
		}
		
		   baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insertCompareQr",map);
		
		return true;
	}  
	
		
}

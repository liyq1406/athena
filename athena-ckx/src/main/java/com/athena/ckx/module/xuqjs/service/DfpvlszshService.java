package com.athena.ckx.module.xuqjs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Dfpvlszsh;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;



@Component
public class DfpvlszshService extends BaseService<Dfpvlszsh>{
	
	static Logger log = Logger.getLogger(DfpvlszshService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	private String result;

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}
	
	
	public Map queryDfpvlszsh(Dfpvlszsh dfpvlszsh){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("dfpvlszsh.queryDfpvlszsh",dfpvlszsh,dfpvlszsh);
	}
	public List<?> queryCaozm() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvlszsh.queryCaozm");
	}
	
	@Transactional
	public boolean updateShenh(List<Map<String,String>> list)  throws Exception
	{
		for (Map<String, String> map : list) {

		log.info("插入审核数据开始 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh")+"操作码"+map.get("caozm"));
		if(map.get("caozm").equals("A36"))
		{	
			if(!StringUtils.isNotBlank(map.get("cangkzck")) || map.get("cangkzck").equals("undefined"))
			{	
				throw new ServiceException("审核失败!仓库子仓库不能为空");
			}
			if(!StringUtils.isNotBlank(map.get("blh")) || map.get("blh").equals("undefined"))
			{	
				throw new ServiceException("审核失败!BL号不能为空");
			}
			
			log.info("插入DFPV_SHENH_DIAOB表开始 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh")+"操作码"+map.get("caozm"));
			try{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dfpvlszsh.insertA36",map);
			}catch(Exception e)
			{
				log.error("更新DFPV_SHENH_DIAOB表失败 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
				log.error(e.getMessage());
				throw new ServiceException("审核失败！添加流水账调拨失败");
			
			}
		}else{
			log.info("插入审核数据开始 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh")+"操作码"+map.get("caozm"));
			try{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dfpvlszsh.insertShenh",map);
			}catch(Exception e)
			{
				log.error("更新IN_DFPV_LIUSZ表失败 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
				log.error(e.getMessage());
				throw new ServiceException("审核失败！添加流水账失败");
			}
		}
		
		log.info("更新DFPV_SHENH表开始  用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
		try{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dfpvlszsh.updateShenh",map);	
		}catch(Exception e)
		{
			log.error("更新DFPV_SHENH表失败 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
			log.error(e.getMessage());
			throw new ServiceException("审核失败！修改状态失败");
		}
		//log.info("清除DFPV_SHENH表开始  用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
	//	try{
	//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dfpvlszsh.deleteShenh",map);	
	//	}catch(Exception e)
	//	{
	//		log.error("清除DFPV_SHENH表失败 用户中心"+map.get("usercenter")+"凭证单号"+map.get("pingzh")+"凭证序号"+map.get("pingzxmh"));
	//		log.error(e.getMessage());
	//		return "清除DFPV_SHENH表失败";
	//	}
		}
		 return true;
		
	}

}

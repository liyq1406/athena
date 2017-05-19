package com.athena.ckx.module.xuqjs.service;

import java.util.List;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.util.DBUtil;

import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 供应商|承运商|运输商Service
 * @author denggq
 * @Date 2012-3-22
 * 
 */
@Component
public class GongcyService extends BaseService<Gongcy> {

	@Inject
	private CkxShiwtxService ckxshiwtxService;
	protected String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 数据增加|修改保存操作
	 * @author denggq
	 * @Date 2012-3-22
	 * @return String
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Gongcy bean, Integer operant, String userId)throws ServiceException {
		
		if(null != bean.getNeibgys_cangkbh() && "1".equals(bean.getLeix())){//内部供应商
			//内部供应商仓库编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+bean.getNeibyhzx()+"' and cangkbh = '"+bean.getNeibgys_cangkbh()+"' and cangklx = '3' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("neibuyhzx")+bean.getNeibyhzx()+GetMessageByKey.getMessage("neibuckbh")+bean.getNeibgys_cangkbh()+GetMessageByKey.getMessage("notexist"));
			Cangk cangk = new Cangk();
			cangk.setUsercenter(bean.getNeibyhzx());
			cangk.setCangkbh(bean.getNeibgys_cangkbh());
			cangk.setCangklx("3");
			cangk.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", cangk, GetMessageByKey.getMessage("neibuyhzx")+bean.getNeibyhzx()+GetMessageByKey.getMessage("neibuckbh")+bean.getNeibgys_cangkbh()+GetMessageByKey.getMessage("notexist"));
		}
		
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		if(null!=bean.getGcbh()){
			bean.setGcbh(bean.getGcbh().replace("  ", "  "));
		}
//		bean.setGcbh(bean.getGcbh().replace("  ", "??"));
		if (1 == operant) {//insert
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertGongys", bean);
		} else {//update
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateGongys", bean);
			if(null != bean.getBeihzq() && null != bean.getKacbztj()){
				ckxshiwtxService.update(bean.getUsercenter(), CkxShiwtxService.TIXLX_GONGYS, bean.getGcbh(), null, "1");
			}
		}
		
		return "success";
	}

	
	/**
	 * 数据删除（逻辑删除）
	 * 
	 * @author denggq
	 * @Date 2012-3-22
	 * @param delete
	 * @param userId
	 * @throws ServiceException
	 */
	@Transactional
	public String removes(Gongcy bean, String userId)throws ServiceException {
		bean.setGcbh(bean.getGcbh().replace("  ", "  "));
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteGongys", bean);
		return "success";
	}
	
	/**
	 * 获得多个CMJ
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Gongcy bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongcy",bean);
	}
	
	@SuppressWarnings("unchecked")
	public List<Gongcy> listByImport(Gongcy bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongcyImport",bean);
	}
}

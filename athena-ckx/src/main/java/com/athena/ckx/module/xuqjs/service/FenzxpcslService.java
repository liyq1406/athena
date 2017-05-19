package com.athena.ckx.module.xuqjs.service;

import java.util.Map;

import com.athena.ckx.entity.xuqjs.Fenzxpcsl;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * 焊装分装线
 */
@Component
public class FenzxpcslService extends BaseService<Fenzxpcsl>{
	
	/**
	 * 获得命名空间
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 查询分装线排产数量
	 * @param page	分页信息
	 * @param param	查询参数
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("ts_ckx.queryFenzxpcsl",param,page);
	}
	
	/**
	 * 保存分装线排产数量
	 * @param bean 分装线排产数量信息
	 * @param operant 1：新增	2：修改
	 * @param userName 操作人
	 * @return
	 */
	public String save(Fenzxpcsl bean, Integer operant, String userName){
		bean.setEditor(userName);								//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
		if (1 == operant){	//增加
			//验证分装线属于大线
			Fenzx hanzfzx = FenzxService.getFenzx(baseDao, bean.getUsercenter(), bean.getFenzxh());
			if(hanzfzx == null){
				throw new ServiceException(bean.getFenzxh()+new Message("fenzxh_error_mes", "i18n.ckx.xuqjs.i18n_fenzxpcsl").getMessage());
			}
			if(!bean.getDaxxh().equals(hanzfzx.getDaxxh())){
				throw new ServiceException(new Message("fenzxh_daxxh_error_mes", "i18n.ckx.xuqjs.i18n_fenzxpcsl").getMessage());
			}
			bean.setCreator(userName);	//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertFenzxpcsl", bean);		//增加数据库
		}else if(2 == operant){	//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenzxpcsl", bean);		//修改数据库
		}
		return "success";
	}
	
	/**
	 * 删除分装线排产数量
	 * @param bean 分装线排产数量信息
	 * @return
	 */
	public String delete(Fenzxpcsl bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteFenzxpcsl", bean);
		return "success";
	}
}

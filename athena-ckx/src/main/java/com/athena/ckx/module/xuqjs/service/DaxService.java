package com.athena.ckx.module.xuqjs.service;

import com.athena.ckx.entity.xuqjs.Dax;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 焊装大线
 */
@Component
public class DaxService extends BaseService<Dax>{

	/**
	 * 获得命名空间
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 保存大线
	 * @param bean 大线信息
	 * @param operant 1：新增	2：修改
	 * @param userName 操作人
	 * @return
	 */
	public String save(Dax bean, Integer operant, String userName){
		//验证该产线不为分装线
		if(bean.getBiaos().equals("1")&& DBUtil.checkCount(baseDao,"ts_ckx.getCountFenzx", bean)){
			throw new ServiceException(new Message("fenzx_exist_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//拆分天数不可大于排产封闭期
		if(bean.getChaifts()> bean.getPaicfbq()){
			throw new ServiceException(new Message("chaifts_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//失效大线时，不能有所属分装线
		if(bean.getBiaos().equals("0") && DBUtil.checkCount(baseDao,"ts_ckx.getCountFenzxByDaxxh", bean)){
			throw new ServiceException(new Message("shix_dax_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		bean.setEditor(userName);								//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
		if (1 == operant){	//增加
			bean.setCreator(userName);	//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertHanzdx", bean);		//增加数据库
		}else if(2 == operant){	//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateHanzdx", bean);		//修改数据库
		}
		return "success";
	}
	
}

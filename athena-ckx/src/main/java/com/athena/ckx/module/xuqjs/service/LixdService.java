package com.athena.ckx.module.xuqjs.service;

import java.util.HashMap;
import java.util.List;

import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Lixd;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 离线点
 */
@Component
public class LixdService extends BaseService<Lixd>{

	/**
	 * 获得命名空间
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 保存离线点
	 * @param bean 离线点信息
	 * @param operant 1：新增	2：修改
	 * @param userName 操作人
	 * @return
	 */
	public String save(Lixd bean, Integer operant, String userName){
		//验证线号是否为大线或分装线
		bean.setLeix(judgeChanxLeix(bean));
		//验证消耗点存在且有效
		CkxGongyxhd xhd = new CkxGongyxhd();
		xhd.setGongyxhd(bean.getDuiyxhd());
		xhd.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCkxGongyxhd", xhd)){
			throw new ServiceException(new Message("duiyxhd_error_mes", "i18n.ckx.xuqjs.i18n_lixd").getMessage());
		}
		//验证对应消耗点属于线号
		Fenpq fenpq = getFenpq(bean.getUsercenter(), bean.getDuiyxhd().substring(0, 5));
		if(fenpq == null || 
				(bean.getLeix().equals("1") && !bean.getXianh().equals(fenpq.getShengcxbh())) ||
				(bean.getLeix().equals("2") && !bean.getXianh().equals(fenpq.getFenzxh()))){
			throw new ServiceException(new Message("duiyxhd_xianh_error_mes", "i18n.ckx.xuqjs.i18n_lixd").getMessage());
		}
		//验证消耗点没有被其他分装线或离线点使用
		Lixd dbData = this.get(bean);
		if(!(dbData != null && dbData.getDuiyxhd().equals(bean.getDuiyxhd()))){	//新增或消耗点发生修改
			if(DBUtil.checkCount(baseDao,"ts_ckx.getCountGongyxhdInFenzxAndLixd", bean.getDuiyxhd())){	//验证消耗点数量
				throw new ServiceException(new Message("xiaohd_used_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
			}
		}
		bean.setEditor(userName);								//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
		if (1 == operant){	//增加
			//验证用户中心+离线点唯一
			if(DBUtil.checkCount(baseDao,"ts_ckx.getCountLixd", bean)){
				throw new ServiceException(new Message("lixd_exist_error_mes", "i18n.ckx.xuqjs.i18n_lixd").getMessage());
			}
			bean.setCreator(userName);	//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLixd", bean);		//增加数据库
		}else if(2 == operant){	//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLixd", bean);		//修改数据库
		}
		return "success";
	}
	
	/**
	 * 判断生产线类型
	 * @param lixd 离线点信息
	 * @return 1：大线	2：分装线	3：其他
	 */
	@SuppressWarnings("unchecked")
	private String judgeChanxLeix(Lixd lixd){
		//获取生产线，并关联大线、分装线
		List<HashMap<String, Object>> resultList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getChanxLeix", lixd);
		if(resultList == null|| resultList.size() == 0){	//非生产线
			throw new ServiceException(new Message("xianh_error_mes", "i18n.ckx.xuqjs.i18n_lixd").getMessage());
		}
		HashMap<String, Object> result = resultList.get(0);
		if(result.get("DAXXH") != null){	//大线
			return "1";
		}else if(result.get("FENZXH") != null){		//分装线
			return "2";
		}else{	//其他
			throw new ServiceException(new Message("lixd_xianh_error_mes", "i18n.ckx.xuqjs.i18n_lixd").getMessage());
		}
	}
	
	/**
	 * 获取分配区
	 * @param usercenter	用户中心
	 * @param fenpqh		分配区号
	 * @return
	 */
	private Fenpq getFenpq(String usercenter,String fenpqh){
		Fenpq f = new Fenpq();
		f.setUsercenter(usercenter);
		f.setFenpqh(fenpqh);
		f.setBiaos("1");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getFenpq",f);
		if(0 == list.size()){
			return null;
		}
		if(1 < list.size()){
			throw new ServiceException(GetMessageByKey.getMessage("fenpeiqu")+fenpqh+GetMessageByKey.getMessage("duotcx"));
		}
		return (Fenpq) list.get(0) ;
	}
}

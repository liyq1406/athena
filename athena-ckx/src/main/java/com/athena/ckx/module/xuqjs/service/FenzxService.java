package com.athena.ckx.module.xuqjs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 焊装分装线
 */
@Component
public class FenzxService extends BaseService<Fenzx>{
	
	/**
	 * 获得命名空间
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 保存分装线
	 * @param bean 分装线信息
	 * @param operant 1：新增	2：修改
	 * @param userName 操作人
	 * @return
	 */
	@Transactional
	public String save(Fenzx bean, Integer operant, String userName){
		//验证消耗点存在且有效
		CkxGongyxhd xhd = new CkxGongyxhd();
		xhd.setGongyxhd(bean.getXiaohdbh());
		xhd.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCkxGongyxhd", xhd)){
			throw new ServiceException(new Message("xiaohd_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//验证消耗点为大线上的消耗点
		if(!bean.getDaxxh().equals(getshengcxbh(bean.getUsercenter(), bean.getXiaohdbh().substring(0, 5)))){
			throw new ServiceException(new Message("xiaohd_notBelongTo_dax_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//验证大线线号存在且有效
		Fenzx param1 = new Fenzx();
		param1.setUsercenter(bean.getUsercenter());
		param1.setDaxxh(bean.getDaxxh());
		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountDax", param1)){
			throw new ServiceException(new Message("dax_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//验证分装线号不为大线
		Fenzx param2 = new Fenzx();
		param2.setUsercenter(bean.getUsercenter());
		param2.setFenzxh(bean.getFenzxh());
		if(bean.getBiaos().equals("1")&& DBUtil.checkCount(baseDao,"ts_ckx.getCountDax", param2)){
			throw new ServiceException(new Message("dax_exist_error_mes", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
		}
		//修改对应零件消耗点、分配区的“分装线号”，“生产线编号”
		Fenzx dbData = this.get(param2);	//dbData：数据库中的分装线数据 	bean：表单传来的分装线数据
		//验证消耗点没有被离线点使用
		if(!(dbData != null && dbData.getXiaohdbh().equals(bean.getXiaohdbh()))){	//新增或消耗点发生修改
			if(DBUtil.checkCount(baseDao,"ts_ckx.getCountGongyxhdInLixd", bean.getXiaohdbh())){	//验证消耗点数量
				throw new ServiceException(new Message("xiaohd_used_error_mes1", "i18n.ckx.xuqjs.i18n_shengcx").getMessage());
			}
		}
		bean.setEditor(userName);								//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
		if(!bean.getBiaos().equals(dbData == null ? null : dbData.getBiaos())){		//标识发生了改变
			if(bean.getBiaos().equals("1")){	//生效分装线
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenpqByTakeEffectFenzx", bean);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjxhdByTakeEffectFenzx", bean);
			}else if(bean.getBiaos().equals("0")){		//失效分装线
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenpqByLoseEffectFenzx", bean);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjxhdByLoseEffectFenzx", bean);
			}
		}
		//修改分装线大线线号时，修改分配区和零件消耗点的生产线编号
		if(dbData != null && !bean.getDaxxh().equals(dbData.getDaxxh())){	//修改了大线线号
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usercenter", bean.getUsercenter());
			paramMap.put("fenzxh", bean.getFenzxh());
			paramMap.put("yuan_daxxh", dbData.getDaxxh());	//原大线线号
			paramMap.put("xian_daxxh", bean.getDaxxh());	//现大线线号
			paramMap.put("editor", bean.getEditor());
			paramMap.put("edit_time", bean.getEdit_time());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjxhdByModifyDaxxh", paramMap);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenpqByModifyDaxxh", paramMap);
		}
		if (1 == operant){	//增加
			bean.setCreator(userName);	//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertHanzfzx", bean);		//增加数据库
		}else if(2 == operant){	//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateHanzfzx", bean);		//修改数据库
		}
		return "success";
	}
	
	
	/**
	 * 获取生产线编号
	 * @param usercenter 用户中心
	 * @param fenpqh 分配区号
	 * @return 生产线编号
	 */
	@SuppressWarnings("rawtypes")
	private String getshengcxbh(String usercenter,String fenpqh){
		Fenpq f = new Fenpq();
		f.setUsercenter(usercenter);
		f.setFenpqh(fenpqh);
		f.setBiaos("1");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getShengcxbh",f);
		if(0 == list.size()){
			return "";
		}
		return list.get(0).toString() ;
	}

	/**
	 * 获取分装线
	 * @param usercenter	用户中心	
	 * @param xianh			线号
	 * @return				分装线
	 */
	@SuppressWarnings("rawtypes")
	public static Fenzx getFenzx(AbstractIBatisDao dao,String usercenter, String xianh){
		Fenzx param = new Fenzx();
		param.setUsercenter(usercenter);
		param.setFenzxh(xianh);
		List list = dao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getFenzx",param);
		if(0 == list.size()){
			return null;
		}
		return (Fenzx) list.get(0) ;
	}

}

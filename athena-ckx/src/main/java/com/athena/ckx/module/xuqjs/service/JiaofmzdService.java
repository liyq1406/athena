package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.entity.xuqjs.CkxJiaofrl;
import com.athena.ckx.entity.xuqjs.Jiaofmsd;
import com.athena.ckx.entity.xuqjs.Jiaofmzd;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 交互码字典
 * @author denggq
 * @date 2012-3-29
 */
@Component
public class JiaofmzdService extends BaseService<Jiaofmzd>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-3-29
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-29
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Jiaofmzd> insert,
	           ArrayList<Jiaofmzd> edit,
	   		   ArrayList<Jiaofmzd> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userId);
		edits(edit,userId);
//		deletes(delete,userId);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-3-29
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Jiaofmzd> insert,String userId)throws ServiceException{
		for(Jiaofmzd bean:insert){
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertJiaofmzd",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-29
	 * @param edit,userId
	 * @return ""
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String edits(List<Jiaofmzd> edit,String userId) throws ServiceException{
		for(Jiaofmzd bean:edit){
			if("0".equals(bean.getBiaos())){
				CkxWaibwl waibwl= new CkxWaibwl();//外部物流路径
				waibwl.setUsercenter(bean.getUsercenter());
				waibwl.setJiaofm(bean.getJiaofm());
				List<CkxWaibwl> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getCkxWaibwl",waibwl);
				if(0 != list.size()){//用到此 交付码
					throw new ServiceException(GetMessageByKey.getMessage("noshixiao"));
				}
			}
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateJiaofmzd",bean);
		}
		return "";
	}
	/**
	 * 私有批量失效方法
	 * @author denggq
	 * @date 2012-3-29
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Jiaofmzd> delete,String userId)throws ServiceException{
		for(Jiaofmzd bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJiaofmzd",bean);
		}
		return "";
	}
	
	
	/**
	 * 物理删除
	 * @author denggq
	 * @date 2012-3-29
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String remove(Jiaofmzd bean,String userId) throws ServiceException {
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		Jiaofmsd jiaofmsd = new Jiaofmsd();					//交付码设定(模板)
		jiaofmsd.setUsercenter(bean.getUsercenter());		//用户中心
		jiaofmsd.setJiaofm(bean.getJiaofm());				//交付码
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJiaofmsd", jiaofmsd);	//删除模板中的交付码
		
		CkxJiaofrl jiaofrl = new CkxJiaofrl();					//交付日历
		jiaofrl.setUsercenter(bean.getUsercenter());		//用户中心
		jiaofrl.setJiaofm(bean.getJiaofm());				//交付码
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJiaofrl", jiaofrl);	//删除交付日历中的交付码
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.removeJiaofmzd",bean);		//删除字典
		return "deletesuccess";
	}
	
}

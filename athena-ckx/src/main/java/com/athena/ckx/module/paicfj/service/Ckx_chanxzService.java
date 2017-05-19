package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.paicfj.Ckx_chanxz;
import com.athena.ckx.entity.paicfj.Ckx_chanxz_paiccs;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 产线组Service
 * @author hj
 * @Date 12/02/16
 */
@Component
public class Ckx_chanxzService extends BaseService<Ckx_chanxz> {

	/**
	 * 排产参数设置-产线组子表
	 */
	@Inject
	private Ckx_chanxz_paiccsService pc;
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 12/02/16
	 * @param bean
	 * @param operate
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_chanxz bean , Integer operate,
			List<Ckx_chanxz_paiccs> insert ,
			List<Ckx_chanxz_paiccs> edit ,
			List<Ckx_chanxz_paiccs> delete ,String userID) throws ServiceException{
		if(null==bean.getChanxzbh()&&0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		// 如果operate为1，则产线组进行录入操作，若为2，则产线组进行编辑操作
		if (1 == operate) {
			insert(bean, userID);
		} else {
			edit(bean, userID);
		}
		checkPCJHYUniqe(bean.getJihyzbh());
		pc.inserts(insert,userID, bean);
		pc.edits(edit,userID, bean);
		pc.deletes(delete, bean);
		return "success";
	}
	/**
	 * 检测排产计划员是否已存在对应的产线组
	 * @param pcjhy
	 */
	private void checkPCJHYUniqe(String pcjhy){
		if(null == pcjhy){return ;}
		Ckx_chanxz bean = new Ckx_chanxz();
		bean.setJihyzbh(pcjhy);
		Map<String,Object> map = super.select(bean);
		if(Integer.valueOf(map.get("total").toString())>1){
			//"一个产线组只能对应一个排产计划员组"
			throw new ServiceException(GetMessageByKey.getMessage("cxzdyjhyz"));
		}
	}
	/**
	 * 数据插入
	 * @author hj
	 * @Date 12/02/17
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	
	private String insert(Ckx_chanxz bean,String userID) {
		Date date = new Date();
		bean.setCreator(userID);
		bean.setCreate_time(date);
		bean.setEditor(userID);
		bean.setEdit_time(date);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_chanxz", bean);
		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 12/02/17
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edit(Ckx_chanxz bean,String userID){
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_chanxz", bean);
		return "";
	}
	@SuppressWarnings("rawtypes")
	public List queryPCJIIHY(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ckx.queryPCJIIHY");
	}
}

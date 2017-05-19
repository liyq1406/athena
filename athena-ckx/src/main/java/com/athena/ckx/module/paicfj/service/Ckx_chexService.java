package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.ChexYunss;
import com.athena.ckx.entity.paicfj.Ckx_chex;
import com.athena.ckx.util.GetMessageByKey;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

import com.toft.core3.transaction.annotation.Transactional;
/**
 * 车型Service
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_chexService extends BaseService<Ckx_chex> {

	@Inject 
	private ChexYunssService chexYunssService;
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_chex> insert,
			List<Ckx_chex> edit,
			List<Ckx_chex> delete,String userID)throws ServiceException {
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert, userID);
		edits(edit, userID);
		removes(delete);
		return "success";
	}
	/**
	 * 批量数据录入
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_chex> insert,String userID){
		Date date = new Date();
		for (Ckx_chex bean : insert) {
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_chex", bean);
		}
		return "";
	}
	/**
	 * 批量数据编辑
	 * @author hj
	 * @Date 12/02/21
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_chex> edit,String userID) throws ServiceException{
		Date date = new Date();
		for (Ckx_chex bean : edit) {
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_chex", bean);
		}
		return "";
	}
	/**
	 * 批量数据删除（物理删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@SuppressWarnings({  "rawtypes" })
	public String removes(List<Ckx_chex> delete){
		for (Ckx_chex bean : delete) {
			ChexYunss chexyunss = new ChexYunss();
			chexyunss.setChexbh(bean.getChexbh());
			List list = chexYunssService.list(chexyunss);
			if(0 < list.size()){
				//数据已在使用，不能删除
				 throw new ServiceException(GetMessageByKey.getMessage("ysybns"));
			}else{			
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_chex", bean);
			}
		}
		return "";
	}
	
}

package com.athena.ckx.module.paicfj.service;



import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.Ckx_chanxz_jhy;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;

import com.toft.core3.container.annotation.Component;

import com.toft.core3.transaction.annotation.Transactional;
/**
 * 产线组计划员service
 * @author hj
 * @Date 12/02/16
 *
 */
@Component
public class Ckx_chanxz_jhyService extends BaseService<Ckx_chanxz_jhy> {

	@Override
	public String getNamespace(){
		return "ts_ckx";
	}
	/**
	 * 数据操作（产线组计划员的增、删、改）
	 * @author hj
	 * @Date 12/02/16
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_chanxz_jhy> insert,
	           List<Ckx_chanxz_jhy> edit,
	   		   List<Ckx_chanxz_jhy> delete,String userID){
		//如果insert集合、edit集合、delete集合均为空，则没有数据改动，直接跳出此方法
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete);
	    return "success";
	}
	/**
	 * 私有批量增加方法
	 * @author hj
	 * @Date 12/02/16
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_chanxz_jhy> insert,String userID){
		for (Ckx_chanxz_jhy bean : insert) {
				bean.setCreator(userID);
				bean.setCreate_time(new Date());
				bean.setEditor(userID);
				bean.setEdit_time(new Date());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_chanxz_jhy", bean);
		}
		return "";
	}
	/**
	 * 私有批量编辑方法
	 * @author hj
	 * @Date 12/02/16
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_chanxz_jhy> edit,String userID){
		for (Ckx_chanxz_jhy bean : edit) {
				bean.setEditor(userID);
				bean.setEdit_time(new Date());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_chanxz_jhy", bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法（物理删除）
	 * @author hj
	 * @Date 12/02/16
	 * @param delete
	 * @return ""
	 * @throws ServiceException
	 */
	private String deletes(List<Ckx_chanxz_jhy> delete){
		for (Ckx_chanxz_jhy bean : delete) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_chanxz_jhy", bean);
		}
		return "";
	}
}

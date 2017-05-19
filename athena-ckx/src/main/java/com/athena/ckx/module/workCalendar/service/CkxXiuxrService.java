/**
 * 代码声明
 */
package com.athena.ckx.module.workCalendar.service;

import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxXiuxr;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 休息日业务逻辑层
 * @author kong
 */
@Component
public class CkxXiuxrService extends BaseService<CkxXiuxr>{
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "workCalendar";
	}
	/**
	 * 保存休息日
	 * @param addList
	 * @param editList
	 * @param delList
	 * @param user
	 * @return
	 */
	@Transactional
	public String save(List<CkxXiuxr> addList,List<CkxXiuxr> editList,List<CkxXiuxr> delList,LoginUser user) {
		add(addList,user);//添加
		edit(editList,user);//修改
		del(delList);//删除
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 增加数据
	 * @param addList
	 * @return
	 * @throws Exception 
	 */
	private void add(List<CkxXiuxr> addList,LoginUser user){
 		for (CkxXiuxr ckxXiuxr : addList) {
//			try {
				ckxXiuxr.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
				ckxXiuxr.setCreator(user.getUsername());//创建人
				ckxXiuxr.setEditor(user.getUsername());//修改人
				ckxXiuxr.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.insertCkxXiuxr", ckxXiuxr);
//			} catch (DataAccessException e) {
//				//日期{0}已存在，保存失败
//				throw new ServiceException(new Message("dateObjsaveError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{ckxXiuxr.getRiq()}));
//			}
		}
	}
	/**
	 * 修改数据
	 * @param editList
	 * @return
	 * @throws Exception 
	 */
	private void edit(List<CkxXiuxr> editList,LoginUser user) {
		for (CkxXiuxr ckxXiuxr : editList) {
			ckxXiuxr.setEditor(user.getUsername());//修改人
			ckxXiuxr.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.updateCkxXiuxr", ckxXiuxr);
		}
	}
	/**
	 * 删除数据
	 * @param delList
	 * @return
	 * @throws Exception 
	 */
	private void del(List<CkxXiuxr> delList){
		for (CkxXiuxr ckxXiuxr : delList) {
			try {
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.deleteCkxXiuxr", ckxXiuxr);
			} catch (DataAccessException e) {
				//日期{}删除失败
				throw new ServiceException(new Message("dateObjdelError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{ckxXiuxr.getRiq()}));
			}
		}
	}
}
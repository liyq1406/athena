/**
 * 代码声明
 */
package com.athena.print.module.sys.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.print.entity.sys.Printuser;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
@Component
public class PrintuserService extends BaseService<Printuser>{
	
	@Inject
	private PrintuserinfoService printuserinfoService;
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	//修改按钮的操作
	@Transactional
	public String doSave(Printuser bean , 
			List<Printuserinfo> insert ,
			List<Printuserinfo> edit ,
			List<Printuserinfo> delete,String userId) throws ServiceException{
		//先更新用户组表中的数据
		doUpdate(bean,userId);
		//插入用户明细子表
		printuserinfoService.doInsert(bean, insert,userId);
		//更新用户明细子表
		printuserinfoService.doUpdate(bean, edit,userId);
		//删除用户明细子表
		printuserinfoService.doDelete(bean,delete);
		return "success";
	}
	
	//新增按钮的操作
	@Transactional
	public String doAdd(Printuser bean , 
			List<Printuserinfo> insert,String userId) throws ServiceException{
		//新增用户组表
		doInsert(bean,userId);
		//新增用户明细子表
		printuserinfoService.doInsert(bean, insert,userId);
		return "success";
	}
	
	
	/**
	 * 插入
	 */
	public String doInsert(Printuser bean,String userId){
		//设置登录用户
		bean.setCreator(userId);
		//设置增加时间
		bean.setCreate_time(DateUtil.curDateTime());
		//用户组 单记录的插入
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintuser",bean);
		return bean.getUserscode();
	}
	
	/**
	 * 更新
	 */
	public String doUpdate(Printuser bean,String userId) {
		//设置登录用户
		bean.setEditor(userId);
		//设置修改时间
		bean.setEdit_time(DateUtil.curDateTime());
		//用户组单记录的修改
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintuser",bean);
		return bean.getUserscode();
	}
	
	/**
	 * 定位主查询
	 * @return 
	 */
	public Map<String, Object> selectU(Printuser bean) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintusers",bean,bean);
	}
	
	/**
	 * 用户组逻辑删除
	 */
	@Transactional
	public String doDelete(Printuser bean){
		//设置用户中中心
		//bean.setUsercenter(getLoginUser().getUsercenter());
		//用户组的逻辑删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintuser",bean);
		return "success";
	}
	
	/**
	 * 更新逻辑删除的列
	 */
	@Transactional
	public String doUpdate1(Printuser bean) {
		//设置用户中中心
		//bean.setUsercenter(getLoginUser().getUsercenter());
		//用户组表中的有效、无效的更新
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintuser1",bean);
		return "success";
	}
}
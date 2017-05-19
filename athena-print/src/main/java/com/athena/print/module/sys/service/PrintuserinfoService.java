/**
 * 代码声明
 */
package com.athena.print.module.sys.service;

import java.util.List;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.athena.print.entity.sys.Printuser;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
@Component
public class PrintuserinfoService extends BaseService<Printuserinfo>{
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	/**
	 * 查询用户明细
	 * @param page usercode
	 * @return 用户组编号
	 */
	public Printuserinfo selectUsersByuser(String usercode) {
		//查询出用户组的编号
		return (Printuserinfo)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.getPrintuserinfo",usercode);
	}
	
	/**
	 * 通过用户组编号来查询数据集
	 */
	@SuppressWarnings("unchecked")
	public List<Printuserinfo> list(String userscode) throws ServiceException{
		//用户组编号来查询的List集合
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintuserinfolist", userscode);
	}
	
	/**
	 * 插入
	 */
	public String doInsert(Printuser bean,List<Printuserinfo> insert,String userId) {
		if(null!=insert){
			//多条记录增加循环
			for (Printuserinfo printuserinfo : insert) {
				//获取登录用户
				printuserinfo.setCreator(userId);
				//获取主表中的ID  作为子表的一个字段来更新
				printuserinfo.setUserscode(bean.getUserscode());
				//设置增加时间
				printuserinfo.setCreate_time(DateUtil.curDateTime());
				//设置用户中中心
				printuserinfo.setUsercenter(bean.getUsercenter());
				
				//执行新增操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintuserinfo", printuserinfo);
				
			}
		}
		return "Data entry success";
	}
	/**
	 * 删除
	 */
	public String doDelete(Printuser bean,List<Printuserinfo> delete) {
		if(null!=delete){
			//多条记录删除循坏
			for (Printuserinfo printuserinfo : delete) {
				//获取登录用户
				printuserinfo.setEditor(bean.getEditor());
				//获取主表中的ID  作为子表的一个字段来更新
				printuserinfo.setUserscode(bean.getUserscode());
				//设置用户中中心
				printuserinfo.setUsercenter(bean.getUsercenter());
				//执行删除操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintuserinfo", printuserinfo);
			}
		}
		return "Data deleted success";
	}
	/**
	 * 更新
	 */
	public String doUpdate(Printuser bean,List<Printuserinfo> update,String userId) {
		if(null!=update){
			//多条记录更新循坏
			for (Printuserinfo printuserinfo : update) {
				//获取登录用户
				printuserinfo.setEditor(userId);
				//获取主表中的ID  作为子表的一个字段来更新
				printuserinfo.setUserscode(bean.getUserscode());
				//设置修改时间
				printuserinfo.setEdit_time(DateUtil.curDateTime());
				//设置用户中中心
				printuserinfo.setUsercenter(bean.getUsercenter());
				//执行更新的操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintuserinfo", printuserinfo);
			}
		}
		return "Data change success";
	}
}
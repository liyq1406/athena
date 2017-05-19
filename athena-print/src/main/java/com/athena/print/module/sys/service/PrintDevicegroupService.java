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
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.util.exception.ServiceException;
import com.athena.util.date.DateUtil;

@Component
public class PrintDevicegroupService extends BaseService<PrintDevicegroup>{
	
	@Inject
	private PrintDeviceService printDeviceService;
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	//修改按钮执行的操作
	@Transactional
	public String doSave(PrintDevicegroup bean , 
			List<PrintDevice> insert ,
			List<PrintDevice> edit ,
			List<PrintDevice> delete,String userId) throws ServiceException{
		//打印机组表更新
		doUpdate(bean,userId);
		//打印机表的插入
		printDeviceService.doInsert(bean, insert,userId);
		//打印机表的更新
		printDeviceService.doUpdate(bean, edit,userId);
		//打印机表的删除
		printDeviceService.doDelete(bean,delete,userId);
		return "success";
	}
	
	
	//新增按钮的操作
	@Transactional
	public String doAdd(PrintDevicegroup bean , 
			List<PrintDevice> insert,String userId) throws ServiceException{
		//打印机组   表的插入
		doInsert(bean,userId);
		//打印机表  的插入
		printDeviceService.doInsert(bean, insert,userId);
		return "success";
	}
	
	
	
	/**
	 * 插入
	 */
	public String doInsert(PrintDevicegroup bean,String userId){
		//登录用户
		bean.setCreator(userId);
		//插入增加时间
		bean.setCreate_time(DateUtil.curDateTime());
		//打印机组 表 记录的插入
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintDevicegroup",bean);
		return bean.getSpcodes();
	}
	
	/**
	 * 更新
	 */
	public String doUpdate(PrintDevicegroup bean,String userId) {
		//插入修改时间
		bean.setEdit_time(DateUtil.curDateTime());
		//登录用户
		bean.setEditor(userId);
		//打印机组 表 但记录的更新
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicegroup",bean);
		return bean.getSpcodes();
	}
	
	/**
	 * 定位主查询
	 * @return 
	 */
	public Map<String, Object> selectS(PrintDevicegroup bean) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintDevicegroups",bean,bean);
	}
	
	/**
	 * 更新逻辑删除的列
	 */
	@Transactional
	public String doUpdate1(PrintDevicegroup bean) {
		//插入用户中心
		//bean.setUsercenter(getLoginUser().getUsercenter());
		//打印机组 中 标识的 失效、有效
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicegroup1",bean);
		return "success";
	}
	
	/**
	 * 打印设备组逻辑删除
	 */
	@Transactional
	public String doDelete(PrintDevicegroup bean){
		//插入用户中心
		//bean.setUsercenter(bean.getUsercenter());
		//打印机组的逻辑删除 数据库中保留数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintDevicegroup",bean);
		return "success";
	}
	
}
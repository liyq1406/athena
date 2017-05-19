/**
 * 代码声明
 */
package com.athena.print.module.sys.service;


import java.util.List;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;


/**
 * @author dsimedd001
 *
 */
@Component
public class PrintDeviceService extends BaseService<PrintDevice>{
	
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	/**
	 * 插入
	 */
	public String doInsert(PrintDevicegroup bean,List<PrintDevice> insert,String userId) {
		if(null!=insert){
			//多条记录增加循环
			for (PrintDevice printDevice : insert) {
				//设置登录用户
				printDevice.setCreator(userId);
				//获取主表中的ID  作为子表的一个字段来更新
				printDevice.setSpcodes(bean.getSpcodes());
				//插入子表的用户中心
				printDevice.setUsercenter(bean.getUsercenter());
				//设置增加时间
				printDevice.setCreate_time(DateUtil.curDateTime());
				
				//执行新增操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintDevice", printDevice);
				
				//同步插入打印机状态表
				PrintDevicestatus printDevicestatus = new  PrintDevicestatus();
				printDevicestatus.setUsercenter(bean.getUsercenter());
				printDevicestatus.setSpcode(printDevice.getSpcode());
				printDevicestatus.setSpcodes(bean.getSpcodes());
				printDevicestatus.setSname(printDevice.getSname());
				printDevicestatus.setSip(printDevice.getSip());
				printDevicestatus.setSport(printDevice.getSport());
				printDevicestatus.setNlevel(printDevice.getNlevel());
				printDevicestatus.setStatus(0);
				printDevicestatus.setEnable(0);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintDevicestatus", printDevicestatus);
			}
		}
		return "Data entry success";
	}
	/**
	 * 删除
	 */
	public String doDelete(PrintDevicegroup bean,List<PrintDevice> delete,String userId) {
		if(null!=delete){
			//多条记录删除循环
			for (PrintDevice printDevice : delete) {
				//设置登录用户
				printDevice.setEditor(userId);
				//获取主表中的ID  作为子表的一个字段来更新
				printDevice.setSpcodes(bean.getSpcodes());
				//获取子表的用户中心
				printDevice.setUsercenter(bean.getUsercenter());
				//执行删除操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintDevice", printDevice);
				
				//同步删除打印机状态表
				PrintDevicestatus printDevicestatus = new  PrintDevicestatus();
				printDevicestatus.setUsercenter(bean.getUsercenter());
				printDevicestatus.setSpcode(printDevice.getSpcode());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintDevicestatus", printDevicestatus);
			}
		}
		return "Data deleted success";
	}
	/**
	 * 更新
	 */
	public String doUpdate(PrintDevicegroup bean,List<PrintDevice> update,String userId) {
		if(null!=update){
			//多条记录更新循环
			for (PrintDevice printDevice : update) {
				//设置登录用户
				printDevice.setEditor(userId);
				//获取主表中的ID  作为子表的一个字段来更新
				printDevice.setSpcodes(bean.getSpcodes());
				//设置 修改时间
				printDevice.setEdit_time(DateUtil.curDateTime());
				//获取子表的用户中心
				printDevice.setUsercenter(bean.getUsercenter());
				//后台校验替代设备是否存在
				PrintDevice printDevice1 = new PrintDevice();
				printDevice1.setUsercenter(bean.getUsercenter());
				printDevice1.setSpcode(printDevice.getReplacespcode());
				List<PrintDevice> rList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevice",printDevice1);
				Message m=new Message("NotExistDevice","i18n.print.i18n_print");
				if(0==rList.size()){
						throw new ServiceException(m.getMessage());
				}
				//执行更新操作
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevice", printDevice);
				
				//对打印机状态表进行同步更新
				PrintDevicestatus pds = new  PrintDevicestatus();
				pds.setUsercenter(bean.getUsercenter());
				pds.setSpcode(printDevice.getSpcode());
				pds.setNlevel(printDevice.getNlevel());
				pds.setSip(printDevice.getSip());
				pds.setSport(printDevice.getSport());
				pds.setSpcodes(printDevice.getSpcodes());
				pds.setReplacespcode(printDevice.getReplacespcode());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatus", pds);
			}
		}
		return "Data change success";
	}
	
	
	/**
	 * 通过编号来查询数据集
	 */
	@SuppressWarnings("unchecked")
	public List<PrintDevice> list(String pgid) throws ServiceException{
		//根据打印机组编号来查询 打印机集合
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicelist", pgid);
	}
	
}
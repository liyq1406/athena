/**
 * 
 */
package com.athena.print.module.sys.action;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.print.module.sys.service.PrintDeviceService;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintDeviceAction extends ActionSupport {
	
	@Inject 
	private AbstractIBatisDao baseDao;
	@Inject
	private PrintDeviceService printDeviceService;

	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	
	//查询返回主页面
	public String queryDevice(@Param PrintDevice bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PrintDevice bean) {
		//页面初始化时分页查询
		setResult("result", printDeviceService.select(bean));
		return AJAX;
	}
	public String querys(@Param PrintDevicestatus bean) {
		//查询打印设备的集合 
		setResult("result", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("sys.queryPrintDevices",bean));
		return AJAX;
	}
	
	/**
	 * 通用查询
	 * @param bean
	 */
	public String list(@Param PrintDevice bean) {
		//打印机表查询 返回List
		setResult("result", printDeviceService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param PrintDevice bean) {
		//打印机表单记录查询
		setResult("result", printDeviceService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param PrintDevice bean) {
		//打印机单记录的保存
		setResult("result", printDeviceService.save(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param PrintDevice bean) {
		//打印机表 的 物理删除
		setResult("result", printDeviceService.doDelete(bean));
		return AJAX;
	}
}

package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.DFPVLingjhz;
import com.athena.ckx.entity.xuqjs.DFPVLingjqh;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 零件汇总Service
 * @author CSY
 * @date 2016-05-04
 */
@Component
public class DFPVLingjhzService extends BaseService{
	
	protected static Logger logger = Logger.getLogger(DFPVLingjhzService.class);
	
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @author CSY
	 * @date 2016-05-04	 
	 */
	public Map queryLingjhz(DFPVLingjhz bean){
		Map res = new HashMap<String, String>();
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			res = this.getLingjhz(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return res;
	}
	
	/**
	 * 分页查询（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return
	 */
	private Map getLingjhz(DFPVLingjhz bean){
		Map res = new HashMap<String, String>();
		try {
			res = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("dfpvlingjhz.queryLingjhz",bean,bean);
		} catch (Exception e) {
			throw new ServiceException("零件切换-查询近十次版本号出现异常"+e.getMessage());
		}
		return res;
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 查询最近十次版本号（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @author CSY
	 * @date 2016-05-04	 
	 */
	public List<DFPVLingjqh> queryLingjhzBBH(DFPVLingjhz bean){
		List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			list = this.getLingjhzBBH(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return list;
	}
	
	/**
	 * 查询最近十次版本号（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return
	 */
	private List<DFPVLingjqh> getLingjhzBBH(DFPVLingjhz bean){
		List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
		try {
			List<String> qhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("dfpvlingjhz.queryZUIJBANBHhz");
			for (String string : qhList) {
				DFPVLingjqh d=new DFPVLingjqh();
				d.setBanbh(string);
				list.add(d);
			}
		} catch (Exception e) {
			throw new ServiceException("零件切换-查询近十次版本号出现异常"+e.getMessage());
		}
		return list;
	}
	
}

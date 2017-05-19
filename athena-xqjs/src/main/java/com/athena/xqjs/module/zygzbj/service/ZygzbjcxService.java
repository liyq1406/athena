package com.athena.xqjs.module.zygzbj.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.zygzbj.ZiygzbjHz;
import com.athena.xqjs.entity.zygzbj.Ziygzbjmx;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzbjcxService
 * <p>
 * 类描述：资源跟踪报警查询service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-02-13
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component
@SuppressWarnings({"rawtypes","unchecked"})
public class ZygzbjcxService extends BaseService {

	/**
	 * 查询资源跟踪报警汇总信息
	 * @param page 返回对象 
	 * @param param 查询参数
	 * @return 查询结果
	 */
	public Map selZygzbjhz(Pageable page,Map<String, String> param){
		try {
		//如果有资源供应
			if("exportXls".equals(param.get("exportXls"))){
				return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selZygzbjhz",param));
			}else{
				return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selZygzbjhz",param,page);
			}
		}catch (Exception e) {
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selZygzbjhz",param,page);
		}
	}
	
	/**
	 * 资源跟踪比较
	 * @param pd 已交付
	 * @param hz 汇总信息
	 * @param zygy 资源供应
	 * @return true:正常结果/false:短缺或过量结果
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public boolean ziYgy(PropertyDescriptor pd,ZiygzbjHz hz,String zygy) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		BigDecimal yijf = (BigDecimal) pd.getReadMethod().invoke(hz);
		if(yijf != null){
			//查询短缺列表
			if (zygy.equals("1")) {
				//已交付小于安全库存,为短缺
				if(yijf.compareTo(hz.getAnqkc()) < 0){
					return false;
				}
			//查询过量列表
			}else if (zygy.equals("2")){
				//已交付大于最大库存,为过量
				if(yijf.compareTo(hz.getZuidkcsl()) > 0){
					return false;
				}
			//查询正常
			}else if(zygy.equals("0")){
				//已交付大于安全库存,并且小于最大库存,为正常
				if(yijf.compareTo(hz.getZuidkcsl()) < 0 && yijf.compareTo(hz.getAnqkc()) > 0){
					return false;
				}
			}
		}
		return true; 
	}
	
	/**
	 * 查询资源跟踪报警明细
	 * @param page 返回对象
	 * @param param 查询参数
	 * @return 查询结果
	 */
	public Ziygzbjmx selZygzbjmx(Map<String, String> param){
		return (Ziygzbjmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selZygzbjmx", param);
	}
	
	/**
	 * 查询资源跟踪报警明细
	 * @param page 返回对象
	 * @param param 查询参数
	 * @return 查询结果
	 */
	public Map selZygzbjmxs(PageableSupport page,Map<String, String> param){
		if("4".equals(param.get("baojlx"))){
			//时段报警明细起止时间需要带时分秒
			return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selZygzbjmxs", param,page);
		}else{
			return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selZygzbjmxsZ", param,page);
		}
		
	}
}

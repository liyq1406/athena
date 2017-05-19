package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Zuiddhsl;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;


@Component
public class ZuiddhslService extends BaseService{
	
	/**
	 *@方法： 查询实体
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-6
	 * @参数说明：年周期，供应商编号，用户中心，零件编号
	 */
	public Zuiddhsl  queryObject(String nianzq,String gongysbh,String usercenter,String lingjbh){
		Map<String,String> map = new HashMap<String,String>() ;
		Zuiddhsl bean = new Zuiddhsl();
		map.put("nianzq", nianzq) ;
		map.put("gongysbh", gongysbh) ;
		map.put("usercenter", usercenter) ;
		map.put("lingjbh", lingjbh) ;
		//CommonFun.mapPrint(map, "查询Zuiddhsl实体queryObject方法参数map");
		CommonFun.logger.debug("查询Zuiddhsl实体queryObject方法的sql语句为：common.queryZuiddhslObject");
		bean = (Zuiddhsl) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryZuiddhslObject", map)  ;
		map.clear() ;
		return bean ;
	}

////////////wuyichao//////////////
	public List<Zuiddhsl>  queryList(){
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryZuiddhslList")  ;
	}
////////////wuyichao//////////////
}

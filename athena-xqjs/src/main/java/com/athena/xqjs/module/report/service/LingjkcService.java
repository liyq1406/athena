package com.athena.xqjs.module.report.service;

import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * 零件库存Service
 * @author WL
 *
 */
@Component
public class LingjkcService extends BaseService {

	/**
	 * 查询零件库存信息
	 * @param page 查询对象
	 * @param param 查询参数
	 * @return
	 */
	public Map queryLingjkc(PageableSupport page,Map<String,String> param){
		//报表类型
		String baoblx = param.get("baoblx");
		//零件类型
		String lingjlx = param.get("lingjlx");
		if("1".equals(lingjlx)){
			param.put("gonghlx", "'97W'");
		}else{
			param.put("gonghlx","'97X','97D'");
		}
		if(!"exportXls".equals(param.get("exportXls"))){//非导出报表
			if("2".equals(baoblx)){//报表类型为2,按仓库汇总
				return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("report.queryLingjkc", param,page);
			}else{//按用户中心汇总
				return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("report.queryLingjkcByUc", param,page);
			}
		}else{//导出报表查询
			if("2".equals(baoblx)){//报表类型为2,按仓库汇总
				return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingjkc",param));
			}else{//按用户中心汇总
				return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingjkcByUc",param));
			}
		}
	}
	
}

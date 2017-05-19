package com.athena.xqjs.module.anxorder.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * Title:库存计算参数service
 * @author 李明
 * @version v1.0
 * @date 2012-03-19
 */
@SuppressWarnings({ "rawtypes" })
@Component
public class KucjscsbService extends BaseService {
	
	/*
	 * 查询实体信息得到待消耗，理论库存，异常消耗，到库量
	 * 参数：用户中心，零件编号，jn,j0
	 * */
	public Kucjscsb  queryAllKuc(Map<String,String> map){
		Kucjscsb bean = (Kucjscsb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryYcxhDh",map) ;
		if(bean == null){
			bean = new Kucjscsb();	
			Yicbj yicbj = new Yicbj();
			yicbj.setCuowlx("200");
			yicbj.setCuowxxxx(map.toString()+"库存数量为空");
			yicbj.setUsercenter(map.get("usercenter"));
			yicbj.setLingjbh(map.get("lingjbh"));
			yicbj.setJismk("33");
			// 插入到异常
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
		}
		return bean ;
	}
	
	
	/**
	 * @see   返回按照param参数查询的盘点历史数据
	 * @param params
	 * @return
	 */
	public Object queryPandls(PageableSupport pageBean , Map<String, String> params) 
	{
		
		if(!"exportXls".equals(params.get("exportXls")))
		{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anx.queryPandls", params , pageBean);
		}
		else
		{
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryPandls",params);
			if( null != list )
			{
				if(list.size() > 5000)
					return "<script>alert(\"导出数量不能大于5000!请做调整\");window.close();</script>";
				else
					return CommonFun.listToMap(list);
			}
			return list;
		}
		
	}
	
	
}
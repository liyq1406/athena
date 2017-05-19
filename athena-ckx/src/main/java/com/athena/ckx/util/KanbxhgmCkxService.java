package com.athena.ckx.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.entity.Domain;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
@Component
public class KanbxhgmCkxService extends BaseService<Domain> {
    
	/**
	 * 检测数据的原编码对应的看板是否是失效的，如果是失效的，则不能替换
	 * @param usercenter
	 * @param lingjbh
	 * @param kehd（消耗点|仓库）
	 */
	@SuppressWarnings("unchecked")
	public void getKanbxhgm(String usercenter,String lingjbh,String kehd){
    	Map<String,String> map = new HashMap<String, String>(); 
    	map.put("usercenter", usercenter);
    	map.put("lingjbh",lingjbh);
    	map.put("kehd",kehd);
    	
    	map.put("shengxzt","1");
    	List<Map<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xqjs_ckx.queryKanbxhgm",map);
    	map.put("shengxzt","0");
    	List<Map<String, String>> list1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xqjs_ckx.queryKanbxhgm",map);
    	//取新建的和有效的
    	if (list.size()+list1.size() < 1 ) {
    		throw new ServiceException("对不起，该数据的原编码对应的看板循环规模不存在或已失效，不能替换！");
		}
     }
	
}

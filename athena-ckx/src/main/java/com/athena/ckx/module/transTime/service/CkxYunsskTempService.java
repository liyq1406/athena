package com.athena.ckx.module.transTime.service;


import java.util.Date;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.transTime.CkxYunssk;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.ckx.entity.transTime.CkxYunssk;
import com.athena.ckx.entity.cangk.Xiehztbz;
import java.util.List;

/**
 * 运输时刻(手工计算)
 * @author xss
 *
 */
@Component
public class CkxYunsskTempService extends BaseService<CkxYunssk> {

	@Inject
	private CkxYunssjMbService ckxYunssjMbService;//运输时间模板（实际）
	//获取sqlmap的表空间
	protected String getNamespace() {
		return "transTime";
	}
	
	@Transactional
	public Map<String, Object> select(CkxYunssk bean) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			String xiehztbzs = "";
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(bean.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			bean.setCreator(xiehztbzs);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("transTime.queryCkxYunsskTemp",bean,bean);
	}
	
}

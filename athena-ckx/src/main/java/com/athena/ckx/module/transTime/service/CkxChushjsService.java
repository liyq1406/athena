package com.athena.ckx.module.transTime.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;


import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxCsChushjs;
import com.athena.ckx.entity.transTime.CkxCsUser;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * authuser
 * @author xss
 *
 */
@Component
public class CkxChushjsService extends BaseService<CkxCsUser> {
	//获取sqlmap的表空间
	protected String getNamespace() {
		return "transTime";
	}
	
			/**
	 * 验证用户名和密码
	 * 
	 * @param param
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<CkxCsUser> selectUser(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.authUser", map);		
	}
		
	

}

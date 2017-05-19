package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class XqjsAuthority extends BaseService {

	/**
	 * <p>
	 * 取得登录人所在物流组相关仓库
	 * </p>
	 * 
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCangkWulz(LoginUser loginUser, String keh) {
		Map<String, String> params = getWulzParam(loginUser);
		params.put("keh", keh);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryCangkWulz", params);
	}

	/**
	 * <p>
	 * 取得登录人所在物流组相关仓库
	 * </p>
	 * 
	 * @param loginUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getFenqWulz(LoginUser loginUser, String keh) {
		Map<String, String> params = getWulzParam(loginUser);
		params.put("keh", keh);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryFenpqWulz", params);
	}

	private Map<String, String> getWulzParam(LoginUser loginUser) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> mapQxz = loginUser.getPostAndRoleMap();
		String wulz = mapQxz.get(Const.QX_WULGYYZ);
		params.put("usercenter", loginUser.getUsercenter());
		params.put("wulgyyz", wulz);
		return params;
	}

	/**
	 * <p>
	 * 是否是POA(准备层或执行层)
	 * </p>
	 * 2013-07-24
	 * @param loginUser
	 * @return boolean
	 */
	public boolean isPoa(LoginUser loginUser) {
		boolean flag = false;
		if (loginUser.hasRole(Const.QUANX_POA)||loginUser.hasRole(Const.QUANX_POA_CANGK)) {
			flag = true;
		}
		return flag;
	}

	public List<Map<String, Object>> getWulgyyz(String ghms, String keh) {
		List<Map<String, Object>> result;
		if (ghms.equalsIgnoreCase("RD") || ghms.equalsIgnoreCase("C0")|| ghms.equalsIgnoreCase("CD") || "CS".equalsIgnoreCase(ghms)){
			result = getFenqWulz(AuthorityUtils.getSecurityUser(), keh);
		} else if (ghms.equalsIgnoreCase("MD")) {
			// RM
			result = getCangkWulz(AuthorityUtils.getSecurityUser(), keh);
			result.addAll(getFenqWulz(AuthorityUtils.getSecurityUser(), keh));
		} else {
			result = getCangkWulz(AuthorityUtils.getSecurityUser(), keh);
		}
		return result;
	}
}

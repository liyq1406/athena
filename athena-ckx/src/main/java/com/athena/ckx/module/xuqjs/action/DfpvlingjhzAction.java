package com.athena.ckx.module.xuqjs.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.DFPVLingjhz;
import com.athena.ckx.module.xuqjs.service.DFPVLingjhzService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 零件汇总Action
 * @author CSY
 * @date 2016-05-04
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DfpvlingjhzAction extends ActionSupport{
	
	@Inject
	private DFPVLingjhzService DFPVlingjhzService;
	
	@Inject
	private UserOperLog userOperLog;
	
	protected static Logger logger = Logger.getLogger(DfpvlingjhzAction.class);
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 分页查询
	 * @author CSY
	 * @Date 2016-05-04
	 * @return String
	 * */
	public String queryLingjhz(@Param DFPVLingjhz bean){
		try {
			setResult("result", DFPVlingjhzService.queryLingjhz(bean));
			logger.info("零件汇总-查询成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件汇总", "数据查询-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件汇总-查询异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件汇总", "数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 查询最近十次版本号
	 * @author CSY
	 * @Date 2016-05-04
	 * @return String
	 * */
	public String queryLingjhzBBH(@Param DFPVLingjhz bean){
		try {
			setResult("result", DFPVlingjhzService.queryLingjhzBBH(bean));
			logger.info("零件汇总-最近十次版本号查询成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件汇总", "查询最近十次版本号-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件汇总-最近十次版本号查询异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件汇总", "查询最近十次版本号-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String execute() {
		Map<String,String> map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map) ;
		setResult("role", key);//角色（更新保存时用）
		return "select";
	}
	
}

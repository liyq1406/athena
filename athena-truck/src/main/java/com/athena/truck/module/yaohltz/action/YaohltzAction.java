package com.athena.truck.module.yaohltz.action;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Yaohltzcx;
import com.athena.truck.module.yaohltz.service.YaohltzService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 要货令调整
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YaohltzAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private YaohltzService yaohltz;

	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	public String tqyw(){
		Calendar   calendar   =   Calendar.getInstance();
		SimpleDateFormat str=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		setResult("currDate",str.format(calendar.getTime()));
		calendar.add(Calendar.DATE,   -7);
		setResult("currDate1",str.format(calendar.getTime()));
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	public String queryYaohltqyw(@Param Yaohltzcx bean){
		Map map=getParams();
		try {
			setResult("result", yaohltz.queryYaohlxx(bean,bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "要货令查询", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "要货令查询", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 根据大站台查询卸货站台
	 * @param bean
	 * @return String
	 */
	public String xiehztList() {
		Map params=getParams();
		params.put("usercenter", getLoginUser().getUsercenter());
		try {
			setResult("result", yaohltz.queryXiehzt(params));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 根据用户中心查询卸货站台编组
	 */
	public String queryXiehztbz(){
		try {
			Map params=getParams();
			setResult("result", yaohltz.queryXiehztbz(params));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台编组", "多数据查询");
		}catch (Exception e){
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台编组", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	//整体要货令提前/延误
	public String yaohltzcz(@Param Yaohltzcx yhl ){
	     yhl.setOperator(getLoginUser().getUsername());
		try {
			boolean flag=yaohltz.insertYaohltz(yhl);
			if(flag){
				setResult("result", "操作成功！");
			}else{
				setResult("result", "要货令整体提前/延误失败！");
			}
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "要货令整体提起/延误", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}

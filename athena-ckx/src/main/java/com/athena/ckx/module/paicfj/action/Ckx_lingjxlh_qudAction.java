package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_lingjxlh_qud;
import com.athena.ckx.module.paicfj.service.Ckx_lingjxlh_qudService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 零件序列号-区段action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_lingjxlh_qudAction extends ActionSupport {
	@Inject
	private Ckx_lingjxlh_qudService lingjxlh_qudService;//零件序列号-区段service
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取用户信息
	 * @author hj
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 跳转页面
	 * @author hj
	 * @Date 12/02/28
	 * @return String
	 */
	public String execute(){
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query(@Param Ckx_lingjxlh_qud bean){
		try {
			setResult("result",lingjxlh_qudService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件序列号-区段", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件序列号-区段", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param("insert") ArrayList<Ckx_lingjxlh_qud> insert,
					   @Param("edit") ArrayList<Ckx_lingjxlh_qud> edit,
					   @Param("delete") ArrayList<Ckx_lingjxlh_qud> delete){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m=new Message(lingjxlh_qudService.save(insert, edit, delete, getLoginUser()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件序列号-区段", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件序列号-区段", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}

package com.athena.ckx.module.paicfj.action;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_shengcx;
import com.athena.ckx.module.paicfj.service.Ckx_shengcxService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 生产线action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_shengcxAction extends ActionSupport {
	@Inject
	private Ckx_shengcxService ckx_shengcx;//生产线service
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
	public String execute() {
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
	// @Log(description="query(shengcx)",content="{Toft_SessionKey_UserData.userName}执行分页查询成功",whenException="{Toft_SessionKey_UserData.userName}执行分页查询失败")
	public String query(@Param Ckx_shengcx bean) {
		try {
			setResult("result", ckx_shengcx.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}

	public String list(@Param Ckx_shengcx bean) {
		try {
			setResult("result", ckx_shengcx.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线list", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线list", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}

	// @Log(description="save(shengcx)",content="{Toft_SessionKey_UserData.userName}执行保存方法成功",whenException="{Toft_SessionKey_UserData.userName}执行保存方法失败")
//	public String save(@Param("insert") ArrayList<Ckx_shengcx> insert,
//			@Param("edit") ArrayList<Ckx_shengcx> edit,
//			@Param("delete") ArrayList<Ckx_shengcx> delete) {
//		try {
//			// setResult("result", ckx_shengcx.save(insert, edit,
//			// delete,getUserInfo().getUserId()));
//			Message m = new Message(
//					ckx_shengcx.save(insert, edit, delete, getLoginUser().getUsername()),
//					"i18n.ckx.paicfj.i18n_shengcx");
//			Map<String, String> message = new HashMap<String, String>();
//			message.put("message", m.getMessage());
//			setResult("result", message);
//		} catch (Exception e) {
//			throw new ActionException(e.getMessage());
//		}
//		return AJAX;
//	}
	/**
	 * 保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param Ckx_shengcx bean,@Param("operant") Integer operate) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(ckx_shengcx.save(bean,operate,getLoginUser().getUsername()),
					"i18n.ckx.paicfj.i18n_shengcx");			
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	/**
	 * 删除方法（逻辑删除）
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return
	 */
	public String remove(@Param Ckx_shengcx bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(ckx_shengcx.remove(bean,getLoginUser().getUsername()),
					"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "生产线", "删除数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "生产线", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));

		}
		setResult("result", message);
		return AJAX;
	}
}

package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_yongh_chengys;
import com.athena.ckx.module.paicfj.service.Ckx_yongh_chengysService;
import com.athena.component.service.Message;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 用户承运商关系action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_yongh_chengysAction extends ActionSupport {
	@Inject
	private Ckx_yongh_chengysService yhcysService;//运输商service
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
	public String query(@Param Ckx_yongh_chengys bean){
		setResult("result", yhcysService.select(bean));
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
	public String save(@Param("insert") ArrayList<Ckx_yongh_chengys> insert,
			           @Param("delete") ArrayList<Ckx_yongh_chengys> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(yhcysService.save(insert, delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
		}
		setResult("result",message);
		return AJAX;
	}
}

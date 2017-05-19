package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_peizbz;
import com.athena.ckx.entity.paicfj.Ckx_peizbzbh;
import com.athena.ckx.module.paicfj.service.Ckx_peizbzService;
import com.athena.ckx.module.paicfj.service.Ckx_peizbzbhService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 配载包装组action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_peizbzAction extends ActionSupport {
	@Inject
	private Ckx_peizbzService peizbzService;//配载包装组service
	@Inject
	private Ckx_peizbzbhService peizbzbhService;//配载包装-包含包装service
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
		return "select";
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query(@Param Ckx_peizbz bean,@Param("baozlx") String baozlx){
		try {
			if(!"".equals(baozlx)){
				//如果包装类型不为""，则根据包装类型查到包装组编号，主表根据包装组编号查询
				Ckx_peizbzbh bh=new Ckx_peizbzbh();
				bh.setBaozlx(baozlx);
				Ckx_peizbzbh bhs=peizbzbhService.get(bh);
				bean.setBaozzbh(bhs.getBaozzbh());
			}
			setResult("result", peizbzService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();			
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载包装定义", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query_bh(@Param Ckx_peizbzbh bean){
		try {
			Assert.notNull(bean.getBaozzbh(),GetMessageByKey.getMessage("addbygsx"));
			if("&&*&".equals(bean.getBaozzbh())){
				bean.setBaozzbh(null);
			}
			setResult("result", peizbzbhService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义子表", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载包装定义子表", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(@Param Ckx_peizbz bean,
			@Param("operant") Integer operate,
			@Param("peizbzbh_insert") ArrayList<Ckx_peizbzbh> insert,
			@Param("peizbzbh_delete") ArrayList<Ckx_peizbzbh> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(peizbzService.save(bean,operate,insert,delete,getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义", "保存数据结束");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载包装定义", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 删除方法（逻辑删除）
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return
	 */
	public String remove(@Param Ckx_peizbz bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(peizbzService.remove(bean,getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义", "删除数据结束");
		} catch (Exception e) {			
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载包装定义", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	public String list(@Param Ckx_peizbz bean){
		try {
			setResult("result", peizbzService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义list", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();			
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载包装定义list", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	public String get(@Param Ckx_peizbz bean){
		try {
			setResult("result", peizbzService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载包装定义get", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX,"配载包装定义get", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
}

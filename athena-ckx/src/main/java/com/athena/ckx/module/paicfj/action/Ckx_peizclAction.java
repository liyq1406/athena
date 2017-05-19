package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_peizcl;
import com.athena.ckx.entity.paicfj.Ckx_peizclzb;
import com.athena.ckx.module.paicfj.service.Ckx_peizclService;
import com.athena.ckx.module.paicfj.service.Ckx_peizclzbService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 配载策略action
 * @author hj
 * @Date 12/02/28
 */
@Component
public class Ckx_peizclAction extends ActionSupport {
	@Inject
	private Ckx_peizclService peizclService;//配载策略service
	@Inject
	private Ckx_peizclzbService peizclzbService;//配载策略子表service
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
	@SuppressWarnings("unchecked")
	public String query(@Param Ckx_peizcl bean,@Param("baozzbh") String baozzbh){
		try {
			if(!"".equals(baozzbh)){
				Ckx_peizclzb zb=new Ckx_peizclzb();
				zb.setBaozzbh(baozzbh);
				List<Ckx_peizclzb> list=peizclzbService.list(zb);
				String celbhs="";
				for (Ckx_peizclzb obj : list) {
					celbhs += "'"+obj.getCelbh()+"',";
				}
				if(!"".equals(celbhs)){
					bean.setCelbhs(celbhs.substring(0,celbhs.length()-1));
				}
			}
			setResult("result", peizclService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载策略", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载策略", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String query_zb(@Param Ckx_peizclzb bean){
		try {
			Assert.notNull(bean.getCelbh(),GetMessageByKey.getMessage("addbygsx"));
			setResult("result", peizclzbService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载策略子表", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载策略子表", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 
	 * @author hj
	 * @Date 12/07/18
	 * @param bean
	 * @return 
	 */
	public String list_zb(@Param Ckx_peizclzb bean){
		try {			
			setResult("result", peizclzbService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载策略子表", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载策略子表", "查询数据错误",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(@Param Ckx_peizcl bean,
			@Param("operant") Integer operate,
			@Param("pzcl_insert") ArrayList<Ckx_peizclzb> insert,
			@Param("pzcl_edit") ArrayList<Ckx_peizclzb> edit,
			@Param("pzcl_delete") ArrayList<Ckx_peizclzb> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(peizclService.save(bean,operate ,insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载策略", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载策略", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 删除方法（物理删除）
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return
	 */
	public String remove(@Param Ckx_peizcl bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(peizclService.remove(bean),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配载策略", "删除数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配载策略", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}

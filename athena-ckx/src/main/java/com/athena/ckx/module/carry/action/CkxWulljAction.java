package com.athena.ckx.module.carry.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxWullj;
import com.athena.ckx.module.carry.service.CkxWulljService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
/**
 * 物流路径总图
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxWulljAction extends ActionSupport{
	@Inject
	private CkxWulljService ckxWulljService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param CkxWullj bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @param rdoInnerType 查询类型，0为生效数据，1为模板数据
	 * @return
	 */
	public String query(@Param CkxWullj bean,@Param("rdoInnerType") String rdoInnerType) {
		if("0".equals(rdoInnerType)){
			setResult("result", ckxWulljService.select(bean));
		}else{
			setResult("result", ckxWulljService.selectTemp(bean));
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "数据查询结束");
		return AJAX;
	}
	
	
	/**
	 * 参数检查
	 * @return
	 */
	public String checkWulljTemp(@Param CkxWullj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			String  mes= ckxWulljService.checkWulljTemp(bean);
			map.put("message",mes);
		} catch (Exception e) {
			System.out.println(e);
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 模板计算
	 * @return
	 */
	public String addWulljTemp(){
		Map<String,String> map = new HashMap<String,String>();
		try {
			ckxWulljService.addWulljTemp(getLoginUser());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物流路径总图临时", "数据计算结束");
			map.put("message","模板计算成功！");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "物流路径总图临时", "数据计算结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			map.put("message","模板计算失败！"+e.getMessage());
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 模板生效
	 * @return
	 */
	public String effectiveWullj(){
		Map<String,String> map = new HashMap<String,String>();
		try {
//			ckxWulljService.effectiveWullj(getLoginUser());
			ckxWulljService.addWullj(getLoginUser().getUsername());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物流路径总图临时", "数据生效结束");
			map.put("message","模板生效成功！");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "物流路径总图临时", "数据生效结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			map.put("message","模板生效失败！"+e.getMessage());
		}
		setResult("result",map );
		return AJAX;
	}
	
	
	/**
	 * 数据导出功能  
	 * mantis  1971
	 * @param bean 查询条件实体
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exports(@Param CkxWullj bean,@Param("rdoInnerType") String rdoInnerType){
		Map<String,String> map = new HashMap<String,String>();
		Map<String, Object> dataSurce = new HashMap<String, Object>();
		try {
			List<CkxWullj> list = null;
			if("1".equals(rdoInnerType)){
				list = ckxWulljService.listTemp(bean);
			}else{
				list = ckxWulljService.list(bean);
			}
			dataSurce.put("rows", list);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("wullj.ftl", dataSurce, response, "wullj", ExportConstants.FILE_XLS, false);
			map.put("message",GetMessageByKey.getMessage("shujdccg"));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "wullj", "数据导出");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "wullj", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			map.put("message",GetMessageByKey.getMessage("shujdcsb")+e.getMessage());
		}
		
		setResult("result", map);
		return "downLoad";
	}
}

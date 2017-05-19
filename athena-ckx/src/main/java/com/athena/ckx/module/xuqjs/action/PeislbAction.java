package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.module.xuqjs.service.PeislbService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.Message;
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
 * 配送类别Action
 * @author qizhongtao
 * @date 2012-4-07
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PeislbAction extends ActionSupport{
	
	@Inject
	private PeislbService peislbService;
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	  * 主页面
	  * @author qizhongtao
	  * @Date 2012-4-07
	  * @return String
	  */
	@SuppressWarnings("rawtypes")
	public String execute(){
		
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String value = (String) map.get(key);
		
		String  params = 
			"edit_usercenter:root,WULGYY,ZXCPOA;"+
		    "edit_peislx:   root,WULGYY,ZXCPOA;"+
		    "edit_peislxmc: root,WULGYY,ZXCPOA;"+
		    "edit_baozlx:   root,WULGYY,ZXCPOA;"+
		    "edit_baozsl:   root,WULGYY,ZXCPOA;"+
		    "edit_zuicddcws:root,WULGYY,ZXCPOA;"+
		    "edit_tongbjpbs:root,WULGYY,ZXCPOA;"+
		    "edit_shangxd:  root,WULGYY,ZXCPOA;"+
		    "edit_peitsxbs: root,WULGYY,ZXCPOA;"+
		    "edit_beihtqq:  root,WULGYY,ZXCPOA;"+
		    "edit_xiaohccxc:root,WULGYY,ZXCPOA;"+
		    "edit_shifgj: 	root,WULGYY,ZXCPOA;"+
		    "edit_beiz:    	root,WULGYY,ZXCPOA;"+
		    "edit_shifbhd:  root,WULGYY,ZXCPOA;"+
		    "edit_cangkbh:  root,WULGYY,ZXCPOA;"+
		    "edit_zickbh:   root,WULGYY,ZXCPOA;"+
		    "edit_biaos:    root,WULGYY,ZXCPOA";
	
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		
		setResult("hidden_wlgyy", true);	//权限字段隐藏
		setResult("hidden_role", true);		//物流工艺员角色代码
		
		if("ZXCPOA".equals(key) || "root".equals(key)){//执行层POA,root
			setResult("hidden_wlgyy", false);
		}else if("WULGYY".equals(key)){//物流工艺员组
			setResult("WULGYY",value);
		}
		
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String query(@Param Peislb bean){
		try {
			setResult("result",peislbService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author qizhongtao
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String get(@Param Peislb bean){
		try {
			setResult("result",peislbService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "单数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author qizhongtao
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String list(@Param Peislb bean){
		try {
			setResult("result",peislbService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "多数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author qizhongtao
	 * @Date 2012-4-07
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Peislb> insert,@Param("edit") ArrayList<Peislb> edit,@Param("delete") ArrayList<Peislb> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(peislbService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 配送类别关联项同步数据校验
	 * @author hanwu
	 * @Date 2015-06-26
	 * @param list
	 * @return
	 */
	public String check(@Param("list") ArrayList<String> list,@Param("peislb") Peislb peislb){
		Map<String,String> message = new HashMap<String,String>();
		boolean success = false;
		try {
			//校验数据，获取关联项错误数据
			List<HashMap<String, Object>> errorList = peislbService.check(list,peislb);
			success = errorList.size() == 0;
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "关联项校验");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "关联项校验", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("success", success);
		return AJAX;
	}
	
	/**
	 * 下载校验的错误信息
	 * @author hanwu
	 * @Date 2015-06-26
	 * @return
	 */
	public String downloadErrorList(@Param("list") ArrayList<String> list,@Param("peislb") Peislb peislb){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("rows", peislbService.check(list,peislb));
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("guanlxjy.ftl", dataSurce, response, "配送类别-关联项校验", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "配送类别", "下载校验错误信息");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "配送类别", "下载校验错误信息", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return "downLoad";
	}
}

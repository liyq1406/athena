package com.athena.truck.module.liuccz.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.truck.entity.Chelpd;
import com.athena.truck.entity.Yund;
import com.athena.truck.module.liuccz.service.LiucczService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 *  流程操作(放空操作)
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LiucczAction extends ActionSupport {

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private LiucczService liuccz;
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(LiucczAction.class);
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
   //车辆排队进入页面
	public String execute(){
		Map<String,String> map = getParams();
		setResult("quybh",map.get("quybh"));
		setResult("daztbh",map.get("daztbh"));
		setResult("paidzt",map.get("paidzt"));
		setResult("usercenter", getUserInfo().getUsercenter());
		return "select";
	}
	//车辆排队分页查询
	public String queryChewyd(@Param Chelpd pd) {
		//添加日志-执行开始
		logger.info("开始-车位放空-查询（LiucczAction.queryChewyd）");
		Map<String,String> map = getParams();
		map.put("usercenter", getUserInfo().getUsercenter());//用户中心
		Map<String,String> postMap = getUserInfo().getPostAndRoleMap();
		String chacz = postMap.get("CHACGLY")==null ? "":postMap.get("CHACGLY");
		map.put("chacz",chacz);
		Map chewcclist = new HashMap();
		try {
			if(liuccz.queryChewccgx(map)>0){
			    chewcclist=liuccz.queryChewccydxx(pd, map);
			  }else{
			    chewcclist=liuccz.queryChewydxx(pd, map); 
			  }
			setResult("result", chewcclist);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车车位运单信息", "多数据查询-成功");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车车位运单信息", "多数据查询-失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//添加日志-执行结束
		logger.info("结束-车位放空-查询（LiucczAction.queryChewyd）");
		return AJAX;
	}
	//运单撤销
	public String yundChex(@Param("yd")ArrayList<Yund> yundList) {
		//添加日志-执行开始
		logger.info("开始-车位放空-撤销（LiucczAction.yundChex）");
		Map<String,String> params = getParams();
		params.put("username",getUserInfo().getUsername());
		Map<String,String> message = new HashMap<String,String>();
		try {
			liuccz.yundChex(yundList,params);
			setResult("result", "撤销成功！");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车辆放空-运单撤销", "车辆放空-运单撤销-成功");
		} catch (Exception e) {
			setResult("result", "撤销异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车辆放空-运单撤销", "车辆放空-运单撤销-失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//添加日志-执行结束
		logger.info("结束-车位放空-撤销（LiucczAction.yundChex）");
		return AJAX;
	}
	
	//流程操作
	public String liuccz(@Param("yd") Yund yd) {
		//添加日志-执行开始
		logger.info("开始-车位放空-流程操作（LiucczAction.liuccz）");
		Map<String,String> params = getParams();
		params.put("username",getUserInfo().getUsername());
		Map<String,String> message = new HashMap<String,String>();
		try {
			liuccz.liuccz(yd,params);
			setResult("result", "操作成功！");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "流程操作", "流程操作-成功");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "流程操作", "流程操作-失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		    setResult("result", "操作异常：" + e.getMessage());
		}
		//添加日志-执行结束
		logger.info("结束-车位放空-流程操作（LiucczAction.liuccz）");
		return AJAX;
	}

	public String queryYundzt() {
		try {
			List<Object> zt = liuccz.queryLicdy();
			setResult("result", zt);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车辆放空-流程列表", "车辆放空-运单状态-获取成功");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车辆放空-流程列表", "车辆放空-运单状态-获取失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}

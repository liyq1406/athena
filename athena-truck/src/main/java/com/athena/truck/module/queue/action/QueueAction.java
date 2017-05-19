package com.athena.truck.module.queue.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.truck.entity.Chelpd;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Shijdzt;
import com.athena.truck.entity.Yund;
import com.athena.truck.module.kcckx.service.ChewService;
import com.athena.truck.module.kcckx.service.DengdqyService;
import com.athena.truck.module.kcckx.service.ShijdztService;
import com.athena.truck.module.queue.service.QueueService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 *  车辆排队管理
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class QueueAction extends ActionSupport {

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private QueueService queue;
	
	@Inject
	private  DengdqyService dengdqy;
	
	@Inject
	private  ShijdztService  dazt;
	
	@Inject
	private ChewService cw;
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(QueueAction.class);
	
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
		setResult("chewbh",map.get("chewbh"));
		setResult("usercenter", getUserInfo().getUsercenter());
		return "select";
	}
	//等待区域下拉框列表
	public String dengdqyList() {
		Dengdqy bean =new Dengdqy();
		bean.setUsercenter(getUserInfo().getUsercenter());
		bean.setBiaos("1");
		try {
			setResult("result", dengdqy.list(bean));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	//查询大站台信息
	public String daztList() {
		Map<String,String> map = getParams();
		Shijdzt bean =new Shijdzt();
		bean.setUsercenter(getUserInfo().getUsercenter());
		bean.setBiaos("1");
		bean.setQuybh(map.get("quybh"));
		try {
			setResult("result", dazt.list(bean));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "大站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	//车辆排队分页查询
	public String queryQueue(@Param Chelpd pd) {
		Map<String,String> map = getParams();
		map.put("usercenter", getUserInfo().getUsercenter());//用户中心
		try {
			setResult("result", queue.queryQueuexxPage(pd, map));
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "排队查询", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	//车辆排队置顶操作
	public String chelpdTop(@Param("pd") Chelpd pd) {
		Map<String,String> map = getParams();
		try {
			//查询当前大站台的排队最小序号
			int paidxuh=queue.queryDaztxuh(pd);
			pd.setNeweditor(getUserInfo().getUsername());
			pd.setPaidxh(paidxuh);
			queue.updateChelpd(pd);
			setResult("result", "操作成功！");
			userOperLog.addError(CommonUtil.MODULE_CKX, "置顶操作", "车辆排队置顶操作", CommonUtil.getClassMethod(), "车辆排队置顶操作");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "置顶操作", "车辆排队置顶操作", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		    setResult("result", "置顶操作失败！");
		}
		return AJAX;
	}
	//查询大站台下车位信息
	public  String queryChew(@Param Chew chew) {
		chew.setPageSize(10000);
		setResult("result",queue.chewxx(chew));
		return AJAX;
	}
	//车辆排队指定操作操作
	public String zhidcw(@Param("pd") Chelpd pd) {
		Map<String,String> map = getParams();
		try {
			pd.setNeweditor(getUserInfo().getUsername());
			pd.setZdchew(pd.getChewbh());
			queue.updateChelpd(pd);
			setResult("result", "操作成功！");
			userOperLog.addError(CommonUtil.MODULE_CKX, "指定车位", "指定车位操作", CommonUtil.getClassMethod(), "指定车位操作");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "指定车位", "指定车位操作", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		    setResult("result", "指定车位操作失败！");
		}
		return AJAX;
	}

	
	
}

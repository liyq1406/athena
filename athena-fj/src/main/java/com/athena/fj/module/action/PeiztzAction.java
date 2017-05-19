package com.athena.fj.module.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.fj.module.common.CollectionUtil;
import com.athena.fj.module.service.PeiztzService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-3-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PeiztzAction extends ActionSupport {
	@Inject
	private PeiztzService peiztzService;
	
	@Inject
	private UserOperLog userOperLog;
	LoginUser user = AuthorityUtils.getSecurityUser();
	
	/**
	 * 执行跳转页面方法
	 * @author 贺志国
	 * @date 2012-3-20
	 * @return String 返回跳转
	 */
	@Log(description="accessPeiztz",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessPeiztz(){

		return "select";
	}
	
	/**
	 * 不分页查询配载计划
	 * @author 贺志国
	 * @date 2012-3-23
	 * @return AJAX
	 */
	@Log(description="queryPeizjhOfTiaoz",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String queryPeizjhOfTiaoz(){
		try{
			Map<String,String> param = this.getParams();
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> list = peiztzService.selectPeizjhOfTiaoz(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",list.size());
			mapObj.put("rows", list);
			setResult("result", mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 根据配载单号查询要货令
	 * @author 贺志国
	 * @date 2012-3-23
	 * @return AJAX
	 */
	@Log(description="queryYaohlOfPeizd",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String queryYaohlOfPeizd(){
		try{	
			Map<String,String> param = this.getParams();
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> list = peiztzService.selectYaohlOfPeizd(param); 
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",list.size());
			mapObj.put("rows", list);
			setResult("result", mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 配载计划下的要货令转移
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX
	 */
	public String moveYaohlToPeizjh(@Param("yaohlhList")ArrayList<String> yaohlhList){
		try{
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "配载计划下的要货令转移开始");
			Map<String,String> param = this.getParams();
			Map<String,String> msgMap = new HashMap<String,String>();
			String yaohls = CollectionUtil.listToString(yaohlhList);
			param.put("usercenter", user.getUsercenter());
			param.put("yaohls", yaohls);
			//转移配载计划下的要货令
			peiztzService.moveYaohlToPeizjh(param);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "配载计划下的要货令转移结束");
			//转移后查询左边列表中配载计划下的要货令数量
			String count_left = peiztzService.selectCountYaohlOfPeizjh(param.get("peizdh_left"));
			//如果配载计划下的要货令全部转移则删除配载计划
			if("0".equals(count_left)){
				peiztzService.deletePeizjhOfTiaoz(param);
			}
			
			//根据策略编号查询包装组数量
			String listCel = peiztzService.selectCelBaozsl(param);
			if(listCel==null){
				throw new ServiceException("策略编号为空");
			}
			//转移后查询右边列表中配载计划下的要货令数量
			String count_right = peiztzService.selectCountYaohlOfPeizjh(param.get("peizdh"));
			//如果满载
			if(Integer.parseInt(count_right)>=Integer.parseInt(listCel)){
				userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "更新要车明细是否满载状态开始");
				//更新要车计划明细表中的是否满载状态为1
				peiztzService.updateYaocmxShifmz(param.get("id"));
				userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "更新要车明细是否满载状态结束");
			}
			
			
			Message message = new Message("move_success","i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "配载调整结束");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_PC, "配载调整", "配载调整出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 如果配载计划下的要货令全部转移则删除配载计划
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX;
	 */
	public String deletePeizjhOfTiaoz(){
		try{
			Map<String,String> param = this.getParams();
			Map<String,String> msgMap = new HashMap<String,String>();
			param.put("usercenter", user.getUsercenter());
			peiztzService.deletePeizjhOfTiaoz(param);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载调整", "删除转移后的配载计划结束");
			Message message = new Message("move_allYaohlOfpeizd","i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_PC, "配载调整", "删除转移后的配载计划出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 配载计划下的要货令零件汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX
	 */
	public String queryPeizjhOfLingj(){
		Map<String,String> param = this.getParams();
		try{
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> listLj = peiztzService.selectPeizjhOfLingj(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",listLj.size());
			mapObj.put("rows",listLj);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	/**
	 * 配载计划下包装组汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX
	 */
	public String queryPeizjhOfBaozz(){
		try{
			Map<String,String> param = this.getParams();
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> listLj = peiztzService.selectPeizjhOfBaozz(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",listLj.size());
			mapObj.put("rows",listLj);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
}

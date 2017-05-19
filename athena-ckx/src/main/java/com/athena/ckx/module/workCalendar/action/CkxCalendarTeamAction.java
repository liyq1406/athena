/**
 * 
 */
package com.athena.ckx.module.workCalendar.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.workCalendar.CkxCalendarTeam;
import com.athena.ckx.module.workCalendar.service.CkxCalendarTeamService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 工作时间编组
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCalendarTeamAction extends ActionSupport {
	@Inject
	private CkxCalendarTeamService ckxCalendarTeamService;

	@Inject
	private UserOperLog userOperLog;
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
	public String execute(@Param CkxCalendarTeam bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxCalendarTeam bean) {
		if(bean.getBianzh()!=null){
			bean.setBianzh(bean.getBianzh().toUpperCase());
		}
		if(bean.getBan()!=null){
			bean.setBan(bean.getBan().toUpperCase());
		}
		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
			StringBuffer sb=new StringBuffer();
			for (String s : uclist) {
			sb.append("'"+s.substring(1, 2)+"',");
			}
			sb.delete(sb.length()-1, sb.length());
			bean.setUclist(sb.toString());
		}
		
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据查询开始");
		setResult("result", ckxCalendarTeamService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkxCalendarTeam bean) {
		setResult("result", ckxCalendarTeamService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkxCalendarTeam bean) {
		setResult("result", ckxCalendarTeamService.get(bean));
		return AJAX;
	}
	
	/**
	 * 行编辑数据保存
	 * @param editList
	 * @param delList
	 * @return
	 */
	public String save(@Param("editList") ArrayList<CkxCalendarTeam> editList,@Param("delList") ArrayList<CkxCalendarTeam> delList) {
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据编辑开始");
		map.put("message", ckxCalendarTeamService.edit(editList,  getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据编辑结束");
		setResult("result",map);
		return AJAX;
	}
	
	/**
	 * 添加编组号
	 * @param bean
	 * @param usercode
	 * @return
	 */
	public String addTeam(@Param CkxCalendarTeam bean,@Param("usercode") String usercode){
		Map<String,String> map = new HashMap<String,String>();
		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
			StringBuffer sb=new StringBuffer();
			for (String s : uclist) {
			sb.append("'"+s.substring(1, 2)+"',");
			}
			if(StringUtils.isNotBlank(usercode)){
			String usercodeone=usercode.substring(0, 1);
			if(!sb.toString().contains(usercodeone)){
				map.put("message",	 new Message("yonghzwq","i18n.ckx.workCalendar.i18n_workCalendar").getMessage());
				setResult("result",map);
				return AJAX;
			}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据添加开始");
		map.put("message", ckxCalendarTeamService.addTeam(bean,usercode,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据添加结束");
		setResult("result",map);
		setResult("bianzh",bean.getBianzh());
		return AJAX;
	}
	
	
	/**
	 * 复制编组号
	 * @param usercode
	 * @param teamcode
	 * @return
	 */
	public String copyTeam(@Param("usercode") String usercode,@Param("teamcode") String teamcode){
		Map<String,String> map = new HashMap<String,String>();
		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
			StringBuffer sb=new StringBuffer();
			for (String s : uclist) {
			sb.append("'"+s.substring(1, 2)+"',");
			}
			if(StringUtils.isNotBlank(usercode)){
			String usercodeone=usercode.substring(0, 1);
			if(!sb.toString().contains(usercodeone)){
				map.put("message",	 new Message("yonghzwq","i18n.ckx.workCalendar.i18n_workCalendar").getMessage());
				setResult("result",map);
				return AJAX;
			}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据复制开始");
		map.put("message", ckxCalendarTeamService.copyTeam(usercode,teamcode,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据复制结束");
		setResult("result",map);
		return AJAX;
	}
	
	
	
	/**
	 * 根据编组号逻辑删除
	 * @param teamcode
	 * @return
	 */
	public String delTeam(@Param("teamcode") String teamcode){
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据删除开始");
		map.put("message",ckxCalendarTeamService.delTeam(teamcode,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据删除结束");
		setResult("result",map);
		return AJAX;
	}
	/**
	 * 物理删除
	 * @param teamcode
	 * @return
	 * mantis12 0002695
	 */
	public String remove(@Param CkxCalendarTeam bean){
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据物理删除开始");
		map.put("message",ckxCalendarTeamService.physicalTeam(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工作时间", "数据物理删除结束");
		setResult("result",map);
		return AJAX;
	}
	              
	/**
	 * 获取编组号数据集合
	 * （用于下拉框选择）
	 * @return
	 */
	public String getSelectTeamCode(){
		CkxCalendarTeam bean=new CkxCalendarTeam();
		bean.setBiaos("1");
		Map<String,String> params = getParams();
		if(params.get("usercenter")==null){
		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
			StringBuffer sb=new StringBuffer();
			for (String s : uclist) {
			sb.append("'"+s.substring(1, 2)+"',");
			}
			sb.delete(sb.length()-1, sb.length());
			bean.setUclist(sb.toString());
		}
		}else{
			bean.setUclist("'"+params.get("usercenter").substring(1, 2)+"'");
		}
		setResult("result", ckxCalendarTeamService.getSelectTeamCode(bean));
		return AJAX;
	}
}

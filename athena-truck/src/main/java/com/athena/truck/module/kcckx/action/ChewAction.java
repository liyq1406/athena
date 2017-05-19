package com.athena.truck.module.kcckx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.ChacChew;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.ChewChengys;
import com.athena.truck.module.kcckx.service.ChewChengysService;
import com.athena.truck.module.kcckx.service.ChewService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 车位
 * @author wangliang
 * @date 2015-01-24
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChewAction extends ActionSupport{

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private ChewService chewService;
	
	@Inject
	private ChewChengysService chewChengysService;
	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	/**
	 * 登录人信息
	 * @author wangliang
	 * @date 0215-01-24
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String query(@Param Chew bean,@Param ChewChengys chewcys){
		try {
			if(null!=chewcys.getChengysbh() && !"".equals(chewcys.getChengysbh()) &&(!"".equals(chewcys.getChewbh())&&null!=bean.getChewbh()) ){
				List<ChewChengys> cc = chewChengysService.getx(chewcys);
				if( cc.size()==0){
					return AJAX;
				}else{
					if("2".equals(bean.getChewsx()) || null==bean.getChewsx() ){
						
						if(!"0".equals(bean.getBiaos())){
							setResult("result", chewChengysService.querychewcc(chewcys));
							userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据查询");
						}
						return AJAX;
					}
				}
			}else{
				setResult("result", chewService. selectx(bean));
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据查询");
			}
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String get(@Param Chew bean){
		try {
			setResult("result", chewService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String list(@Param Chew bean) {
		try {
			setResult("result", chewService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 失效车位
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String remove(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			setResult("result", chewService.doDeletebs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 失效车位  并且删除chengys
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 * @return String
	 */
	public String removechewCys(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			chewService.doDeletebChewcys(bean);
			setResult("result", chewService.doDeletebs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 删除chengys
	 * @param bean
	 * @author wangliang
	 * @date 0215-02-03
	 * @return String
	 */
	public String removeChengys(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			chewService.doDeletebChewcys(bean);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "承运商", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "承运商", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
		
	/**
	 * 删除chengys
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 * @return String
	 */
	public String removeCys(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			chewService.doDeletebChewcys(bean);
			setResult("result", chewService.doDeletebs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 失效车位  并且删除chache关系
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 * @return String
	 */
	public String removeChachew(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			chewService.doDeletechacw(bean);
			setResult("result", chewService.doDeletebs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
		
	
	/**
	 * 失效车位  并且删除chache关系
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 * @return String
	 */
	public String removeChachewCys(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			chewService.doDeletebChewcys(bean);
			chewService.doDeletechacw(bean);
			setResult("result", chewService.doDeletebs(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
		
	
	
	
	
	
	
	/**
	 * @description 保存chewchengys
	 * @param bean
	 * @param Shijdzts
	 * @return
	 * @author wangliang
	 * @date 0215-01-24
	 */
	public String saveChewChengys(@Param Chew bean,@Param("operant") Integer operant,@Param("guanlcys_insert") ArrayList<ChewChengys> insert,@Param("guanlcys_edit") ArrayList<ChewChengys> edit , @Param("guanlcys_delete") ArrayList<ChewChengys> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
				Message m = new Message(chewService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"系统参数定义");
				map.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据保存");
		
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
	
	
	/**
	 * 查询车位叉车
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 * @return String
	 */
	public String queryChachew(@Param ChacChew bean){
		try{
			setResult("result", chewService.selectchache(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车车位", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车车位", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	//大站台下拉框列表
	public String queryChewDaztbh(@Param("usercenter") String usercenter,@Param("flag") String flag) {
		Map<String,String> params = getParams();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
		params.put("post_code", quygly);
		params.put("usercenter", usercenter);
		try {
			if(null==flag  || " ".equals(flag)){
				setResult("result", chewService.queryChewDaztbh(params));
			}else{
				List<HashMap<String,String>> ll = new ArrayList<HashMap<String,String>>();
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("daztbh", flag);
				ll.add(map);
				setResult("result", ll);
			}
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位-大站台", "车位-大站台", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}

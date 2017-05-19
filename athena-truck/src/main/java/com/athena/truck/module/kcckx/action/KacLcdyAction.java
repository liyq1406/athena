package com.athena.truck.module.kcckx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.Liucdy;
import com.athena.truck.entity.Shijdzt;
import com.athena.truck.module.kcckx.service.KacLcdyService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-1-28
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KacLcdyAction extends ActionSupport {

	@Inject
	private KacLcdyService liucdyService;
	@Inject
	private UserOperLog userOperLog;
	
	LoginUser user = AuthorityUtils.getSecurityUser();
	/**
	 * 初始化页面
	 * @author 贺志国
	 * @date 2015-1-28
	 * @return 
	 */
	public String accessLiucdy(){
		setResult("usercenter",user.getUsercenter());
		//获取ckx_xitcsdy默认流程 hzg 2015.3.12
		String [] strArr = liucdyService.selectMorlcOfxitcsdy();
		setResult("morliucbh",strArr[0]);
		setResult("morliucmc",strArr[1]);
		return "success";
	}
	
	
	/**
	 * 流程定义列表查询
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean 流程定义实体类
	 * @return AJAX
	 */
	public String queryKacLcdy(@Param Liucdy bean){ 
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卡车入厂", "卡车入厂流程定义");
			setResult("result", liucdyService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卡车入厂", "卡车入厂流程定义");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "卡车入厂", "卡车入厂流程定义查询异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 增加 | 修改  流程定义
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean
	 * @param operate
	 * @return
	 */
	public String saveKac(@Param Liucdy bean,@Param("operant") Integer operate) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卡车入厂", "流程定义保存开始");
			Message m = new Message(liucdyService.save(bean,operate,user.getUsername()),
					"i18n.kac.kac_message");			
			message.put("message", m.getMessage());
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卡车入厂", "流程定义保存异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 失效  流程定义
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean
	 * @return
	 */
	public String removeKac(@Param Liucdy bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(liucdyService.removes(bean,user.getUsername()),
					"i18n.kac.kac_message");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卡车入厂", "失效数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卡车入厂", "失效数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));

		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 参考系区域编号集合
	 * @return AJAX
	 */
	public String queryQuybh(@Param("usercenter") String usercenter){
		try {
			setResult("result",liucdyService.selectQuybh(usercenter));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系大站台集合
	 * @return AJAX
	 */
	public String queryQyDaztbh(@Param("usercenter") String usercenter,@Param("flag") String flag,@Param("quybh") String quybh){
		Map<String,String> params = getParams();
		Map<String,String> postMap = user.getPostAndRoleMap();
		String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
		params.put("post_code", quygly);
		Shijdzt dzt = new Shijdzt();
		dzt.setParams(params);
		dzt.setUsercenter(usercenter);
		dzt.setQuybh(quybh);
		
		try {
			if(null==flag  || " ".equals(flag)){
				setResult("result", liucdyService.selectQyDaztbh(dzt));
			}else{
				List<HashMap<String,String>> ll = new ArrayList<HashMap<String,String>>();
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("daztbh", flag);
				ll.add(map);
				setResult("result", ll);
			}
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	  /**
     * 流程编号查询
     * @author 贺志国
     * @date 2015-1-29
     * @param LCDY MR字典类型默认流程，LCDY ZDY字典类型为自定义流程
     * @param liucbs流程标识  
     * @return 
     */
	public String queryLiucbh(@Param("liucbs") String liucbs ){
		try {
			Map<String,String> param  = new HashMap<String,String>();
			if("0".equals(liucbs)){
				param.put("zidlx", "LCDY");
				param.put("beiz", "MR");
			}else{
				param.put("zidlx", "LCDY");
				param.put("beiz", "ZDY");
			}
			setResult("result",liucdyService.selectLiucbh(param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
}

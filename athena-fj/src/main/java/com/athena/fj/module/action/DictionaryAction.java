package com.athena.fj.module.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.fj.module.service.DictionaryService;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * Title:数据字典类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-2-21
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DictionaryAction extends ActionSupport {

	@Inject
	private DictionaryService dictionaryService;
	LoginUser user = AuthorityUtils.getSecurityUser();
	/**
	 * 参考系客户集合
	 * @return AJAX 
	 */
	public String queryKeh(){
		try {	
			setResult("result",dictionaryService.selectKeh());
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系运输商集合
	 * @return AJAX
	 */
	public String queryYunss(){
		try {	
			setResult("result",dictionaryService.selectYunss(user.getUsercenter()));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系车型集合
	 * @return AJAX
	 */
	public String queryChex(){
		try {	
			setResult("result",dictionaryService.selectChex());
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系运输路线集合
	 * @return AJAX
	 */
	public String queryYunslx(){
		try {
			setResult("result",dictionaryService.selectYunslx(user.getUsercenter()));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系客户成品库集合
	 * @param String yunslxbh 运输路线编号
	 * @return AJAX
	 */
	public String queryKehCpk(@Param("yunslxbh") String yunslxbh ){
		try {
			Map<String,String> param  = new HashMap<String,String>();
			param.put("yunslxbh", yunslxbh);
			param.put("usercenter",user.getUsercenter());
			setResult("result",dictionaryService.selectKehCpk(param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系客户成品库运输商集合
	 * @param String yunslxbh 运输路线编号
	 * @return AJAX
	 */
	public String queryYunssCpk(@Param("yunslxbh") String yunslxbh ){
		try {
			Map<String,String> param  = new HashMap<String,String>();
			param.put("yunslxbh", yunslxbh);
			param.put("usercenter",user.getUsercenter());
			setResult("result",dictionaryService.selectYunssCpk(param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 参考系运输商下的车型集合
	 * @param String yunslxbh 运输路线编号
	 * @return AJAX
	 */
	public String queryChexYunss(@Param("yunssbm") String yunssbm ){
		try {
			Map<String,String> param  = new HashMap<String,String>();
			param.put("yunssbm", yunssbm);
			param.put("usercenter",user.getUsercenter());
			setResult("result",dictionaryService.selectChexYunss(param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
}

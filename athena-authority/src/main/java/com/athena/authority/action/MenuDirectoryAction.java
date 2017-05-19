/**
 * 
 */
package com.athena.authority.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.PageButton;
import com.athena.authority.service.MenuDirectoryService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.cache.CacheManager;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MenuDirectoryAction extends ActionSupport {
	@Inject
	private MenuDirectoryService menuDirectoryService;
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private  CacheManager cacheManager;//缓存
	/**
	 * 1：主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param MenuDirectory bean) {
		this.setRequestAttribute("menuTree", menuDirectoryService.getMenuTreeinfo());
		return "select";
	}
	
	/**
	 * 菜单批量维护
	 * @param bean
	 * @return
	 */
	public String batch(@Param MenuDirectory bean) {
		//设置页面变量
		this.setRequestAttribute("menuTree", menuDirectoryService.getMenuTree());
		return "select";
	}

	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String query(@Param MenuDirectory user) {
		setResult("result", menuDirectoryService.select(user));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 * @return
	 */
	public String get(@Param MenuDirectory user) {
		setResult("result", menuDirectoryService.get(user));
		return AJAX;
	}
	
	/**
	 * 单记录保存
	 * @param bean
	 * @return
	 */
	public String save(@Param MenuDirectory menu) {
		menu.setBiaos("1");
		setResult("result", menuDirectoryService.save(menu));
		return AJAX;
	}
	
	/**
	 * 单记录保存,同时保存对应的按钮集合
	 * @param bean
	 * @return
	 */
	public String saveWithButtons(@Param MenuDirectory menuDirectory,
			@Param("buttons_insert") ArrayList<PageButton> addButtons,
			@Param("buttons_edit") ArrayList<PageButton> editButtons,
			@Param("buttons_delete") ArrayList<PageButton> deleteButtons) {
		try{
			menuDirectory.setBiaos("1");
			setResult("result", menuDirectoryService.saveWithButtons(menuDirectory,
							addButtons,editButtons,deleteButtons));
			if((addButtons != null && addButtons.size()>0) || (editButtons != null && editButtons.size()>0)){
				cacheManager.refreshCache("getAllCachePageButton");//刷新缓存 	
			}
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"地址管理","新增地址"+menuDirectory.getDirName()+"数据操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"地址管理","新增地址"+menuDirectory.getDirName()+"数据操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 删除记录
	 * @param bean
	 * @return
	 */
	public String remove(@Param MenuDirectory bean) {
		try{
			setResult("result", menuDirectoryService.doDelete((bean)));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"地址管理","地址"+bean.getDirName()+"删除成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"地址管理","地址"+bean.getDirName()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 批量更新菜单
	 * @param bean
	 * @return
	 */
	public String batchUpdate(
			@Param("dels") ArrayList<String> dels,
			@Param("adds") ArrayList<MenuDirectory> adds,
			@Param("edits") ArrayList<MenuDirectory> edits) {
		try{
			setResult("result", menuDirectoryService.batchUpdate(dels,adds,edits));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"地址管理","地址更新成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"地址管理","地址更新失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String changeMenuActive(@Param("currentId") String currentId,@Param("ids") ArrayList<String> ids,@Param("biaos") String biaos){
		String msg = "";
		String tempBiaos = "";
		if(biaos!=""&&biaos.equals("0")){
			tempBiaos =  "1";
			msg = "启用";
		}else if(biaos!=""&&biaos.equals("1")){
			tempBiaos = "0";
			msg = "停用";
		}
		try{
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", currentId);
			map.put("biaos", tempBiaos);
			menuDirectoryService.changeMenuActive(map);
			for(int i=0;i<ids.size();i++){
				Map<String, String> newmap = new HashMap<String,String>();
				newmap.put("id", ids.get(i));
				newmap.put("biaos", tempBiaos);
				menuDirectoryService.changeMenuActive(newmap);
			}
			if("0".equals(tempBiaos)){
				menuDirectoryService.removeAuth(map);
			}
			setResult("rebiaos", tempBiaos);
			setResult("result", msg+"成功!");
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"地址管理","地址"+msg+"成功");
		}catch(Exception e){
			setResult("result", msg+"失败!");
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"地址管理","地址"+msg+"失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}

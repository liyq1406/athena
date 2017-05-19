/**
 * 
 */
package com.athena.authority.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.PostGroup;
import com.athena.authority.service.MenuDirectoryService;
import com.athena.authority.service.PostGroupService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
import com.toft.ui.tags.tree.HtmlTreeNode;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PostGroupAction extends ActionSupport {
	@Inject
	private PostGroupService postGroupService;
	
	@Inject
	private MenuDirectoryService menuDirectoryService;
	
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 角色管理查询
	 * @param bean
	 * @return
	 */
	public String execute(@Param PostGroup bean) {
		//功能授权时，生成功能按钮树
		this.setRequestAttribute("menuTree", menuDirectoryService.getMenuTreeWithButtonsChild(new MenuDirectory()));
		return  "select";
	}
	/**
	 * 角色管理执行查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PostGroup bean) {
			String postGroupName = bean.getPostGroupName();
			String temp = "";
			if(postGroupName!=null){
				temp = postGroupName.trim();
			}
			bean.setPostGroupName(temp);
			setResult("result", postGroupService.select(bean));
		
		return AJAX;
	}
	/**
	 * AJAX执行根据父ID展开子结点
	 * @param bean
	 * @return
	 */
	public String postGroupExpandByparentId() {
		//获取当前结点上级结点的ID
		String _id = this.getParam("id" ) ;
		//ID为用户中心(USERCENTER)与MENU_ID以“-”字符串
		//String uc = _id.substring(0,_id.indexOf("-"));
		//String id = _id.substring(_id.indexOf("-")+1) ;
		MenuDirectory menu = new MenuDirectory() ;
		menu.setId(_id) ;
		//menu.setUsercenter(uc) ;
		menu.setParentId(this.getParam("parentId")) ;
		//获取所选择当前结点与下级节点树
		HtmlTreeNode node = menuDirectoryService.getMenuTreeWithButtonsChild(menu);
		setResult("result", node.getChildren());
		return  AJAX;
	}
	/**
	 * 根据父节点ID(parent_id)获取该角色已经拥有的子结点的菜单和按钮
	 * @param postGroupId
	 * @param parentId
	 * @return
	 */
	public String getChildMenuByparentid(@Param("postGroupId") String postGroupId,@Param("parentId") String parentId){
		//String uc = parentId.substring(0,parentId.indexOf("-"));
		//String id = parentId.substring(parentId.indexOf("-")+1) ;
		Map<String,String> map = new HashMap<String,String>();
		//map.put("usercenter", uc);
		map.put("parentId", parentId);
		map.put("postGroupId", postGroupId);
		setResult("result", menuDirectoryService.getChildMenuByparentid(map));
		return AJAX;
	}

	
	/**
	 * 执行listPostGroup.ajax对应的方法，根据条件查询
	 * @param bean
	 */
	public String list(@Param PostGroup bean) {
		setResult("result", postGroupService.list(bean));
		return AJAX;
	}
	/**
	 * 执行listAllPostGroup.ajax对应的方法，查出所有的角色信息
	 * 与上述list不同的是：POST_GROUP_NAME为POST_GROUP_ID||'('||POST_GROUP_NAME ||')'的连接字符
	 * @return
	 */
	public String listAllPostGroup(){
		setResult("result", postGroupService.listAllPostGroup());
		return AJAX;
	}
	
	/**
	 * 角色单记录查询
	 * @param bean
	 */
	public String get(@Param PostGroup bean) {
		setResult("result", postGroupService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param("operant") String operant,@Param PostGroup bean) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		if(operant.equals("1")){
			//角色维一性验证
			try{
				String msg = postGroupService.validateOnly(bean.getPostGroupId());
				if(msg.equals("0")){
					postGroupService.save(bean,loginUser);
					userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"角色管理","新增数据"+bean.getPostGroupId()+"操作成功");
				}else{
					setErrorMessage("保存失败，该角色标识已存在!");
				}
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"角色管理","新增数据"+bean.getPostGroupId()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
		}else if(operant.equals("2")){
			try{
				postGroupService.update(bean,loginUser);
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"角色管理","新增数据"+bean.getPostGroupId()+"操作成功");
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"业务管理","新增数据"+bean.getPostGroupId()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
		}
		CacheManager cm = CacheManager.getInstance();
		Object object = postGroupService.refreshAllData();
		cm.refreshCache("getAllPostGroup", object);
		return AJAX;
	}
	
	/**
	 * 数据删除(暂时未用)
	 * @return
	 */
	public String remove(@Param PostGroup bean) {
		postGroupService.doDelete(bean);
		setResult("result", new Message("删除成功!"));
		return AJAX;
	}
	/**
	 * 自定义数据删除
	 * 逻辑性判断：
	 * 查询该角色是否在用户组中被使用
	 * @param groupId
	 * @return
	 */
	public String delPostGroup(@Param("postGroupId") String postGroupId,@Param("usercenter") String usercenter){
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("postGroupId", postGroupId);
			map.put("usercenter", usercenter);
			setResult("result", postGroupService.delPostGroup(map));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"角色管理","新增数据"+postGroupId+"操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"角色管理","新增数据"+postGroupId+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获取角色分配的菜单和按钮
	 * @return
	 */
	public String getRoleMenuAndButtonIds(@Param PostGroup bean){
		Map<String,String> map = new HashMap<String,String>();
		map.put("postGroupId", bean.getPostGroupId());
		setResult("result", postGroupService.getAuthorizedIds(map));
		return AJAX;
	}
	
	/**
	 * 保存功能授权
	 * @return
	 */
	public String saveAuthorized(@Param("postGroupId") String postGroupId,
			@Param("menuAndButtonIds") ArrayList<String> menuAndButtonIds,
			@Param("menuAndButtonIdsType") ArrayList<String> menuAndButtonIdsType){
		try{
			PostGroup bean = new PostGroup();
			bean.setPostGroupId(postGroupId);
			setResult("result", postGroupService.saveAuthorized(bean,menuAndButtonIds,menuAndButtonIdsType));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"角色管理",postGroupId+"保存功能授权操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"角色管理",postGroupId+"保存功能授权操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	/**
	 * Login.jsp页面中根据loginname获取该loginname的角色信息
	 * @param loginName
	 * @param password
	 * @return
	 */
	public String getUsercenterByLoginName(@Param("loginname") String loginname){
		setResult("result", postGroupService.getUsercenterByLoginName(loginname));
		return AJAX;
	}
	
	/**
	 * postGroup.jsp页面得到菜单赋权的菜单项
	 * @user  gswang
	 * @param MenuDirectory bean
	 * @return
	 */
	public String queryPostGroupAuth(@Param MenuDirectory bean) {
		Map<String,String> params = this.getParams();
		String postGroupId = params.get("postGroupId");
		try{
			if(params.get("treeId") != null && params.get("treeId").length()>0){
				String[] pid = params.get("treeId").split("_");
				params.put("parentId", pid[pid.length-1]);
			}
			List<Map<String,String>> list;
			if("0".equals(params.get("flag"))){
				list = menuDirectoryService.listTopButton(params);
			}else{
				list = menuDirectoryService.listTopMenu(params);
			}
		
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",list.size());
			mapObj.put("rows", list);
			setResult("result", mapObj);
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"角色管理",postGroupId+"查询授权操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * postGroup.jsp页面更新菜单按钮赋权
	 * @user  gswang
	 * @return
	 */
	public String updateMenuAuth() {
		Map<String,String> params = this.getParams();
		if(params.get("treeId") != null && params.get("treeId").length()>0){
			String[] pid = params.get("treeId").split("_");
			String parentId = "";
			if(pid.length>1){
				parentId = pid[pid.length-1];
			}
			params.put("parentId", parentId);
		} 
		try{
			menuDirectoryService.updateMenuButtonAuth(params);
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"角色管理",params.get("postGroupId")+"保存功能授权操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}

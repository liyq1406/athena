/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.ResourceManager;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.PageButton;
import com.athena.authority.entity.User;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.component.utils.PropertyUtils;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.ui.tags.tree.DefaultHtmlTreeNode;
import com.toft.ui.tags.tree.HtmlTreeNode;
import com.toft.ui.utils.TreeDesc;
import com.toft.ui.utils.TreeNodeCallBack;
import com.toft.ui.utils.TreeUtils;

@Component
public class MenuDirectoryService extends BaseService<MenuDirectory> implements
		ResourceManager {

	@Inject
	private PageButtonService pageButtonService;// 按钮
	private final Log log = LogFactory.getLog(MenuDirectoryService.class); 
	private static HtmlTreeNode sysMenuTree;// 系统菜单树

	/**
	 * 获取系统菜单树
	 * 
	 * @return
	 */
	public HtmlTreeNode getSysMenuTree() {
		// if(sysMenuTree==null){
		sysMenuTree = this.getMenuTree();
		log.info("初始化菜单树");
		// }
		return sysMenuTree;
	}

	/**
	 * 清空缓存的系统菜单树
	 */
	public void clearSysMenuTree() {
		sysMenuTree = null;
	}

	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}

	/**
	 * 生成全部URL的过滤控制定义
	 */
	public Map<String, String> getFilterChainDefinitionMap() {
		// 初始化map
		Map<String, String> map = new HashMap<String, String>();
		// 获取所有的用户组以及对应的URL信息
		//List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".queryAllPosturl", null);
		// 遍历数据
		/*
		 * for(int i=0;i<list.size();i++){ Map urlMap = (Map)list.get(i);
		 * //获取用户组ID String postGroupId = (String)urlMap.get("POST_GROUP_ID");
		 * //获取URL String url = (String)urlMap.get("MENU_PATH"); //shiro设置权限
		 * String roleStr = "roles['"+ +"']"; if(url!=null&&!"".equals(url)){
		 * map.put(url, roleStr);// TODO 从数据库读取角色和地址的定义 } }
		 */
		map.put("/**", "authc");// 登录控制
		return map;
	}

	/**
	 * 生成菜单树
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HtmlTreeNode getMenuTreeinfo() {
		List<MenuDirectory> menus = this.list(new MenuDirectory());
		return TreeUtils.listToTree(menus, null, "tree_root_menu", "系统菜单");
	}
	/**
	 * 生成菜单树
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HtmlTreeNode getMenuTree() {
		MenuDirectory dir = new MenuDirectory();
		dir.setDirType(0l);
		dir.setDirIsCK("0");
		List<MenuDirectory> menus = this.list(dir);
		return TreeUtils.listToTree(menus, null, "tree_root_menu", "系统菜单");
	}
	/**
	 * 生成带菜单的树
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HtmlTreeNode getMenuTreeWithButtons() {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String username = loginUser.getUsername();
		List<MenuDirectory> menus = new ArrayList<MenuDirectory>();
		List<Map<String,String>> ucList = new ArrayList<Map<String,String>>();
		CacheManager cm = CacheManager.getInstance();
		List<CacheValue> list = (List<CacheValue>) cm.getCacheInfo("queryUserCenterMap").getCacheValue();
		int size = list.size();
		Map ucKeyValueMap = new HashMap();
		List ucValueList = new ArrayList();
		List ucKeyList = new ArrayList();
		for (int i = 0; i < size; i++) {
			CacheValue cacheValue = (CacheValue) list.get(i);
			String key = cacheValue.getKey();
			String value = cacheValue.getValue();
			Map<String,String> map = new HashMap<String,String>();
			map.put("usercenter", key);
			map.put("ucname", value);
			ucList.add(map);
			ucKeyList.add(key);
			ucKeyValueMap.put(key, value);
		}
		if(username.equals("ROOT")){
			menus = this.list(new MenuDirectory());
			ucValueList = ucList;
		}else{
			//初始化查询参数对象
			User param = new User();
			//设置登录名loginname
			param.setLoginname(username);
			User foundedUser = null;//存储登录的用户
			// 根据用户名loginname,执行查询,获取User
			foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace() + ".getUserByLoginname",param);
			if (foundedUser == null){
				return null;//如果用户为空，则返回空
			}
			//获取用户菜单和按钮信息
			//初始化集合List
			List<Map<String, String>> menuAndButtons = new ArrayList<Map<String, String>>();
			// 执行查询
			menuAndButtons = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getUserMenuTree",
					foundedUser);
			List tempUcList = new ArrayList();
			for(Map<String,String> map:menuAndButtons){
				String usercenter = map.get("USERCENTER");
				String funcId = map.get("FUNC_ID");
				String parentId = map.get("PARENT_ID");
				String dirName = map.get("DIR_NAME");
				MenuDirectory menuDirectory = new MenuDirectory();
				menuDirectory.setUsercenter(usercenter);
				menuDirectory.setId(funcId);
				menuDirectory.setParentId(parentId);
				menuDirectory.setDirName(dirName);
				menus.add(menuDirectory);
				if(!tempUcList.contains(usercenter)){
					Map<String,String> ucmap = new HashMap<String,String>();
					ucmap.put("usercenter", usercenter);
					ucmap.put("ucname", (String)ucKeyValueMap.get(usercenter));
					ucValueList.add(ucmap);
					tempUcList.add(usercenter);
				}
			}
		}
		
		//
		List<PageButton> buttons = pageButtonService.list(new PageButton());
		return this.listToMenuTree(menus, null, "tree_root_menu", "系统菜单",
				new AddPageButtonCallBack(buttons));
		/*return TreeUtils.listToTree(menus, null, "tree_root_menu", "系统菜单",
				new AddPageButtonCallBack(buttons));*/
	}
	
	
	/**********start**/
	/**
	 * 生成带菜单的树
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HtmlTreeNode getMenuTreeWithButtonsChild(MenuDirectory _menu) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String username = loginUser.getUsername();
		String ucLogin = loginUser.getUsercenter();
		List<MenuDirectory> menus = new ArrayList<MenuDirectory>();
		List<Map<String,String>> ucList = new ArrayList<Map<String,String>>();
		if(username.equals("ROOT")){
			menus = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryMenuDirectoryAll",_menu); 
			//ucValueList = ucList;
		}else{
			//初始化查询参数对象
			User param = new User();
			//设置登录名loginname
			param.setLoginname(username);
			User foundedUser = null;//存储登录的用户
			// 根据用户名loginname,执行查询,获取User
			foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace() + ".getUserByLoginname",param);
			if (foundedUser == null){
				return null;//如果用户为空，则返回空
			}
			//获取用户菜单和按钮信息
			//初始化集合List
			List<Map<String, String>> menuAndButtons = new ArrayList<Map<String, String>>();
			// 执行查询
			Map tjmap = new HashMap();
			tjmap.put("id", foundedUser.getId());
			String tjparentId = _menu.getParentId();
			if(tjparentId!=null&&!"".equals(tjparentId)&&tjparentId.indexOf("-")>0){
				int start = tjparentId.indexOf("-");
				tjparentId = tjparentId.substring(start+1);
			}
			tjmap.put("parentId", tjparentId);
			tjmap.put("usercenter", ucLogin);
			
			menuAndButtons = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getUserMenuTree",
					tjmap);
			//List tempUcList = new ArrayList();
			for(Map<String,String> map:menuAndButtons){
				String usercenter = map.get("USERCENTER");
				String funcId = map.get("FUNC_ID");
				String parentId = map.get("PARENT_ID");
				String dirName = map.get("DIR_NAME");
				MenuDirectory menuDirectory = new MenuDirectory();
				menuDirectory.setUsercenter(usercenter);
				menuDirectory.setId(funcId);
				menuDirectory.setParentId(parentId);
				menuDirectory.setDirName(dirName);
				menus.add(menuDirectory);
			}
		}
		
		//
		List<PageButton> buttons = pageButtonService.list(new PageButton());
		//如果是根结点
		if(_menu.getId()==null){
			return this.listToMenuTree(menus, null, "tree_root_menu", "系统菜单",
					new AddPageButtonCallBack(buttons));
		}else{
			//如果是子结点
			return this.childObjectToMenuTree(_menu, _menu.getParentId(), menus, new AddPageButtonCallBack(buttons)) ;
		}
		/*return TreeUtils.listToTree(menus, null, "tree_root_menu", "系统菜单",
				new AddPageButtonCallBack(buttons));*/
	}
	/********end*/
	
	
	
	public HtmlTreeNode listToMenuTree(List<?> beans,
			String parentId, String rootId, String rootText,TreeNodeCallBack callBack) {
		HtmlTreeNode root = 
			new DefaultHtmlTreeNode(parentId==null?rootId:parentId,rootText);
		root.setGroup("tree_root_menu");
		//for (int i = 0; i < ucValueList.size(); i++) {
			//Map<String,String> map = (Map<String,String>) ucValueList.get(i);
			//String key = (String)map.get("usercenter");
			//String value = (String)map.get("ucname");
			//HtmlTreeNode childUc = new DefaultHtmlTreeNode(key,value);
			HtmlTreeNode child;
			for(Object bean:beans){
				child = childObjectToMenuTree(bean,parentId,new ArrayList<Object>(beans),callBack);
				if(child!=null){
					root.addChild(child);
				}
				child = null;
			}
		//}
		return root;
	}
	public static HtmlTreeNode childObjectToMenuTree(Object bean,
			String parentId,List<?> beans,TreeNodeCallBack callBack){
		HtmlTreeNode treeNode = null;
		TreeDesc treeDesc = bean.getClass().getAnnotation(TreeDesc.class);
		if(treeDesc!=null){
			Object id = getPropertyValue(bean, treeDesc.idAttr());
			Object pid = getPropertyValue(bean, treeDesc.parentIdAttr());
			Object text = getPropertyValue(bean, treeDesc.textAttr());
			Object code = getPropertyValue(bean, treeDesc.codeAttr());
			Object group = getPropertyValue(bean, treeDesc.codeAttr());
			Object src = getPropertyValue(bean, treeDesc.srcAttr());
			//String nodeId = uc+"-"+id;
			//构造树对象
			if(isChildObject(id,parentId,pid)){
				if(text==null){
					text = id;
				}
				treeNode = new DefaultHtmlTreeNode((String)id+"menu",text.toString());
				/******************其它值设置*********************/
				if(code!=null){//设置编码
					treeNode.setCode(code.toString());
				}
				if(group!=null){
					treeNode.setGroup(group.toString());
				}
				if(src!=null){
					treeNode.setSrc(src.toString());
				}
				
				beans.remove(bean);//生成节点后从数组中删除
				
				//listToTree
				if(beans.size()>0){
					HtmlTreeNode childNode = null;
					for(Object childBean:beans){
						childNode = childObjectToMenuTree(childBean,id==null?null:id.toString(),new ArrayList<Object>(beans),callBack);
						if(childNode!=null){
							treeNode.addChild(childNode);	
						}
						childNode = null;
					}
				}
				treeNode.setBean(bean);
				if(callBack!=null){
					callBack.after(treeNode);
				}
			}
		}else{
			beans.remove(bean);//直接从数组中删除
		}
		return treeNode;
	}
	/**
	 * 判断是否可以生成子节点
	 * @param parentId
	 * @param pid
	 * @return
	 */
	private static boolean isChildObject(Object id,String parentId,Object pid) {
		boolean flag = (parentId==null&&pid==null)||(pid!=null&&pid.equals(parentId));
		return id!=null&&flag;
	}

	private static Object getPropertyValue(Object bean,String property){
		if(property==null||"".equals(property)){
			return null;
		}
		return  PropertyUtils.getPropertyValue(bean,property);
	}
	
	/**
	 * 
	 */
	class AddPageButtonCallBack implements TreeNodeCallBack {

		private Map<String, List<PageButton>> menuButtonsMap;// 全部的按钮

		public AddPageButtonCallBack(List<PageButton> buttons) {
			this.initButtonsMap(buttons);
		}

		/**
		 * 
		 * @param buttons
		 */
		private void initButtonsMap(List<PageButton> buttons) {
			// 返回了一个线程安全的实现了Map接口的 实现类 HashMap
			menuButtonsMap = Collections
					.synchronizedMap(new HashMap<String, List<PageButton>>());
			String menuId;
			List<PageButton> menuButtons;
			for (PageButton pageButton : buttons) {
				if (pageButton.getMenuDirectory() != null) {
					menuId = pageButton.getMenuDirectory().getId();
					//for(String key:ucKeyList){
						//String keys = key +"-"+menuId;
						menuButtons = menuButtonsMap.get(menuId);

						if (menuButtons == null) {
							menuButtons = new ArrayList<PageButton>();
						}
						//pageButton.setButtonCode(key+":"+pageButton.getButtonCode());
						menuButtons.add(pageButton);

						menuButtonsMap.put(menuId, menuButtons);

					//}
					menuButtons = null;
					menuId = null;

				}
			}
		}

		public void after(HtmlTreeNode treeNode) {
			String menuId = treeNode.getId();
			//gswang 2012-09-27 mantis 0004307
			/*String[] ids = menuId.split("-");
			String uc = ids[0];*/
			String _menuId = menuId.substring(0, menuId.length()-4);
			List<PageButton> menuButtons = menuButtonsMap.get(_menuId);
			if (menuButtons != null) {
				for (PageButton pageButton : menuButtons) {
					pageButton.setId(pageButton.getId()+"menu");
					treeNode.addChild(buildButtonTreeNode(pageButton));
				}
			}
		}

		/**
		 * 创建按钮节点
		 * 
		 * @param pageButton
		 * @return
		 */
		private HtmlTreeNode buildButtonTreeNode(PageButton pageButton) {
			//gswang 2012-09-27 mantis 0004307
			HtmlTreeNode treeNode = new DefaultHtmlTreeNode("button_"
					+ pageButton.getButtonCode(), pageButton.getButtonCaption());
			treeNode.setGroup("button " + pageButton.getButtonName());
			return treeNode;
		}

	}

	/**
	 * 保存菜单，同时保存按钮
	 * 
	 * @param menu
	 * @param deleteButtons
	 * @param editButtons
	 * @param addButtons
	 * @return
	 */
	public Object saveWithButtons(MenuDirectory menu,
			List<PageButton> insertButtons,
			List<PageButton> editButtons,
			List<PageButton> deleteButtons) {
		Object id = this.save(menu);
		//gswang 2012-10-12
		createMnueTreeId(menu);
		// 保存按钮
		List<PageButton> savedButtons = new ArrayList<PageButton>();
		if (insertButtons != null){
			savedButtons.addAll(insertButtons);
		}
		if (editButtons != null){
			savedButtons.addAll(editButtons);
		}
		for (PageButton delButton : deleteButtons) {
			if(delButton.getId()!=null){
				pageButtonService.delete(delButton);// 删除按钮
			}
		}

		for (PageButton pageButton : savedButtons) {
			pageButton.setMenuDirectory(menu);
			pageButtonService.save(pageButton);
		}
		//gswang 2012-10-12
		if(insertButtons.size()>0 || (insertButtons.size()==0 && editButtons.size()==1 && editButtons.get(0).getButtonName().equals("1q2w3e4r4r"))){
			createTreeId(insertButtons);
		}
		clearSysMenuTree();
		return id;
	}

	/**
	 * 
	 * @param dels
	 *            删除的菜单
	 * @param adds
	 *            新增的菜单
	 * @param edits
	 *            修改的菜单
	 * @return
	 */
	public Object batchUpdate(List<String> dels,
			List<MenuDirectory> adds, List<MenuDirectory> edits) {
		// TODO Auto-generated method stub
		clearSysMenuTree();
		return "";
	}

	public Object getChildMenuByparentid(Map map) {
		List list = new ArrayList();
		list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getChildMenuByparentid",map);
		if(list.size()==0){
			map.put("parentId", "");
			list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getChildButtonByparentid",map);
		}
		return list;
	}

	public HtmlTreeNode getUsercenterTree() {
		CacheManager cm = CacheManager.getInstance();
		List<CacheValue> list = (List<CacheValue>) cm.getCacheInfo("queryUserCenterMap").getCacheValue();
		int size = list.size();
		HtmlTreeNode root = 
			new DefaultHtmlTreeNode("tree_root_uc","用户中心");
		root.setGroup("tree_root_uc");
		for (int i = 0; i < size; i++) {
			CacheValue cacheValue = (CacheValue) list.get(i);
			String key = cacheValue.getKey();
			String value = cacheValue.getValue();
			HtmlTreeNode childUc = new DefaultHtmlTreeNode(key,value);
			root.addChild(childUc);
		}
		return root;
	}

	public void changeMenuActive(Map<String, String> map) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".changeMenuActive", map);
	}

	/**
	 * 查询一级菜单
	 * @date 2012-10-12
	 * @param params
	 * @return
	 */
	public List<Map<String,String>> listTopMenu(Map<String,String> params) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryMenuAuth",params);
	}
	
	/**
	 * 查询一级按钮
	 * @date 2012-10-12
	 * @author gswang
	 * @param params
	 * @return
	 */
	public List<Map<String,String>> listTopButton(Map<String,String> params) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryButtonAuth",params);
	}
	
	/**
	 * 更新菜单权限
	 * @date 2012-10-12
	 * @author gswang
	 * @param params
	 * @return
	 */
	public Map<String,String> updateMenuButtonAuth(Map<String,String> params)throws ServiceException {
		Map<String,String> re = new HashMap<String,String>();
		String treeId = params.get("treeId");
		String ischeck = params.get("ischeck");
		String postGroupId = params.get("postGroupId");
		String flag = params.get("flag");
		if("0".equals(ischeck)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteLowerMenu", params);
			if(params.get("treeId") != null && params.get("treeId").length()>0 && !"0".equals(flag)){
				List<String> pidList = Arrays.asList(params.get("treeId").split("_"));
				ArrayList<String> pid = new ArrayList<String>();
				pid.addAll(pidList);
				for(int i = pid.size()-1;i>=0;i--){
					pid.remove(pid.size()-1);
					params.put("treeId", this.spellingString(pid, "_"));
					String upperMenuNum = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".queryPGMenuAuthNum",params);
					if(i==0){params.remove("treeId");}
					if("0".equals(upperMenuNum) || "1".equals(upperMenuNum) || (i==0 && "1".equals(upperMenuNum))){
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteLowerMenu", params);
					}else{
						break;
					}
				}
			}
		}else if("1".equals(ischeck)){
			List<Map<String,String>> lowerMenuList = new ArrayList<Map<String,String>>();
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteLowerMenu", params);
			if(!"0".equals(flag)){
				//查询所选菜单的下级菜单
				lowerMenuList= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryLowerMenuAuth",params);
				for( Map<String,String> lowerMenuMap: lowerMenuList){
					lowerMenuMap.put("postGroupId", postGroupId);
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".insertMenuAuth",lowerMenuList);
			}
			lowerMenuList= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryLowerButtonAuth",params);
			for( Map<String,String> lowerMenuMap: lowerMenuList){
				lowerMenuMap.put("postGroupId", postGroupId);
				lowerMenuMap.put("id", lowerMenuMap.get("bid"));
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".insertMenuAuth",lowerMenuList);
			if(params.get("treeId") != null && params.get("treeId").length()>0){
				List<String> pidList = Arrays.asList(params.get("treeId").split("_"));
				ArrayList<String> pid = new ArrayList<String>();
				pid.addAll(pidList);
				for(int i = pid.size()-1;i>=0;i--){
					pid.remove(pid.size()-1);
					params.put("treeId", this.spellingString(pid, "_"));
					if(pid.size()>0){params.put("id", pid.get(pid.size()-1));}
					if(i==0){
						params.put("treeId", "tree_root_menu");
						params.put("id", "tree_root_menu");
					}
					String upperMenuNum= (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".queryAddPGMenuAuthNum",params);
					if("0".equals(upperMenuNum)){
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".insertMenuAuth", params);
					}else{
						break;
					}
				}
			}
		}
		return re;
	}
	
	/**
	 * 连接树型id
	 * @author gswang
	 * @date 2012-10-12
	 * @param src
	 * @param sep
	 * @return String
	 */
	public String spellingString(ArrayList<String> src,String sep){
		StringBuffer result = new StringBuffer();
		String flag = "";
		for(String s : src){
			result.append(flag).append(s);
			flag = sep;
		}
		return result.toString();
	}
	
	/**
	 * 更新按钮树型id
	 * @author gswang
	 * @date 2012-10-12
	 * @param src
	 * @param sep
	 * @return
	 */
	public String createTreeId(List<PageButton> button) {
		String StrReturn = "";
		List<MenuDirectory> menuDirectoryList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryMenuDAll");
		List<PageButton> pageButtonList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryPageButton");
		if(button.size()>0){
			for(PageButton buttonBean : button){
				for(PageButton pageButton : pageButtonList){
					if(buttonBean.getButtonCode().equals(pageButton.getId())){
						String tid = oneTreeId(pageButton.getButtonCode(),pageButton.getMenuDirectory().getId(),menuDirectoryList);
						pageButton.setTreeId(tid);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updatePageButtonTree",pageButtonList);
						break;
					}
				}
			}
		}else{
			for(MenuDirectory menuDirectory : menuDirectoryList){
				String tid = oneTreeId(menuDirectory.getId(),menuDirectory.getParentId(),menuDirectoryList);
				menuDirectory.setTreeId(tid);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updateMenuDirectoryTree",menuDirectoryList);
			for(PageButton pageButton : pageButtonList){
				String tid = oneTreeId(pageButton.getButtonCode(),pageButton.getMenuDirectory().getId(),menuDirectoryList);
				pageButton.setTreeId(tid);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updatePageButtonTree",pageButtonList);
			
			List<Map<String,String>> sysRPostGroupList  = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryUpdateMenuAuth");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updateSysRPostGroupAuthTree",sysRPostGroupList);
			List<Map<String,String>> sysRPostGroupButtonList  = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryUpdateButtonAuth");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updateSysRPostGroupAuthTree",sysRPostGroupButtonList);
			List<Map<String,String>> sysRPostGroupTopList  = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryUpdateTopMenuAuth");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updateSysRPostGroupAuthTree",sysRPostGroupTopList);
		}
		
		return StrReturn;
	}
	
	/**
	 * 更新菜单树型id
	 * @author gswang
	 * @date 2012-10-12
	 * @param src
	 * @param sep
	 * @return
	 */
	public MenuDirectory createMnueTreeId(MenuDirectory menu) {
		List<MenuDirectory> menuDirectoryList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".queryMenuDAll");
		for(MenuDirectory menuDirectory : menuDirectoryList){
			if(menu.getId().equals(menuDirectory.getId())){
				String tid = oneTreeId(menuDirectory.getId(),menuDirectory.getParentId(),menuDirectoryList);
				List<MenuDirectory> m = new ArrayList<MenuDirectory>();
				menuDirectory.setTreeId(tid);
				m.add(menuDirectory);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).executeBatch(getNamespace()+".updateMenuDirectoryTree",m);
				break;
			} 
		}
		return menu;
	}
	
	/**
	 * 连接菜单树型id
	 * @author gswang
	 * @date 2012-10-12
	 * @param src
	 * @param sep
	 * @return String
	 */
	public String oneTreeId(String id,String pid,List<MenuDirectory> menuDirectoryList){
		String parentId = pid;
		String treeId = id;
		int i = 0;
		while(parentId != null && !"".equals(parentId) && parentId.length()>0 && i<1000){
			for(MenuDirectory menu : menuDirectoryList){
				if(parentId.equals(menu.getId())){
					treeId = menu.getId()+"_"+treeId;
					parentId = menu.getParentId();
					break;
				}
			}
			i++;
		}
		return treeId;
	}
	
	/**
	 * 当菜单停用时，删除菜单的权限。
	 * @author gswang
	 * @date 2012-10-12
	 * @param map
	 * @return void
	 */
	public void removeAuth(Map<String, String> map) {
		MenuDirectory dir = (MenuDirectory)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getMenuDirectory", map);
		map.put("treeId", dir.getTreeId());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteLowerMenu", map);
	}
}
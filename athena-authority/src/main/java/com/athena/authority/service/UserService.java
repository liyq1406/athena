/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import com.athena.authority.AccountStopException;
import com.athena.authority.AthenaAccount;
import com.athena.authority.AuthorityConst;
import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.Dic;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.Post;
import com.athena.authority.entity.User;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;
import com.toft.ui.tags.tree.DefaultHtmlTreeNode;
import com.toft.ui.tags.tree.HtmlTreeNode;

@Component
public class UserService extends BaseService<User> {

	@Inject
	private PostService postService;

	@Inject
	private PageButtonService pageButtonService;
	
	@Inject
	private DicService dicService;
	
	@Inject
	private MenuDirectoryService menuDirectoryService;
	private final Log log = LogFactory.getLog(UserService.class); 
	/**
	 * 获取表空间
	 */
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}

	/**
	 * 保存
	 */
	public Object save(User user) {
		// 使用MD5加密用户密码
		String password = user.getPassword();
		if (password == null || "".equals(password)) {
			// 设置默认密码
			password = AuthorityConstants.DEFAULT_USRR_PASSWORD;
		}
		// 密码加密并设置到保存对象中
		user.setPassword(new Md5Hash(password).toHex());
		return super.save(user);
	}

	/**
	 * 根据登录用户名称查询用户信息
	 * 
	 * @param loginname
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public LoginUser getUserByLoginname(String loginname,String usercenter) throws AccountStopException {
		new Message("username","athena-core");
		//初始化查询参数对象
		User param = new User();
		//设置登录名loginname
		param.setLoginname(loginname);
		User foundedUser = null;//存储登录的用户
		// 根据用户名loginname,执行查询,获取User
		foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace() + ".getUserByLoginname",param);
		if (foundedUser == null){
			return null;//如果用户为空，则返回空
		}else if(foundedUser.getBiaos().equals("0")){
			throw new AccountStopException("用户帐户被停用!");
		}
		//获取主键USER_ID
		String id = foundedUser.getId();
		//初始化map值
		Map<String,String> map = new HashMap<String,String>();
		//查询上述USER_ID(即ID)拥有的用户组，查询对象：SYS_R_USER_POST；查询条件：USER_ID
		map.put("id", id);
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".listUserPosts",map);
		//获取缓存实例
		CacheManager cm = CacheManager.getInstance();
		//初始化用户组管理缓存对象集合
		List<CacheValue> postDicCache = new ArrayList<CacheValue>();
		LoginUser loginUser = new LoginUser();// 组织登录对象
		//建立缓存的必要：SYS_R_USER_POST查询出来时，只有POST_CODE，如果要知道其业务标识（DIC_CODE），则从缓存中取出SYS_POST信息，将DIC_CODE与POST_CODE进行对应取值
		if(cm.getCacheInfo("queryPostDicCache")!=null){
			//业务缓存：KEY为DIC_CODE,VALUE为POST_CODE
			postDicCache =(List<CacheValue>) cm.getCacheInfo("queryPostDicCache").getCacheValue();
			//获取缓存数据长度
			int size = postDicCache.size();
			Map<String,String> postDicMap = new HashMap<String,String>();
			//循环缓存，建立DIC_CODE与POST_CODE的MAP信息
			for (int i = 0; i < size; i++) {
				CacheValue cacheValue = (CacheValue) postDicCache.get(i);
				//POST_CODE
				String key = cacheValue.getKey();
				//DIC_CODE
				String value = cacheValue.getValue();
				postDicMap.put(key, value);
			}
			//初始化计划组MAP信息
			Map<String,String> jihyzMap = new HashMap<String,String>();
			for(int i=0;i<list.size();i++){
				Map<String,String> resultmap = (Map<String,String>)list.get(i);
				//获取POST_CODE
				String postCode = resultmap.get("POST_CODE");
				if(postDicMap.get(postCode)!=null){
					String dicCode = (String)postDicMap.get(postCode);
					jihyzMap.put(dicCode, postCode);
					//如果DIC_CODE为JIHUAY，则表示该人拥有计划员组信息，则设置组号信息
					if(dicCode.equals(AuthorityConst.ZU_JIHUAY)&&loginUser.getZuh()==null){
						loginUser.setZuh(postCode);
					}
					loginUser.setJihyz(postCode);
				}
			}
		}
		//获取用户菜单和按钮信息
		List<Map<String, String>> menuAndButtons = new ArrayList<Map<String, String>>();
		//初始化条件MAP值
		Map<String,String> tjMap = new HashMap<String,String>();
		//设置loginname条件登录名
		tjMap.put("loginname", foundedUser.getLoginname());
		//设置usercenter用户中心
		tjMap.put("usercenter", usercenter);
		//获取该用户所登录的用户中心下的菜单信息
		try{
			menuAndButtons = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getNewMenuAndButtons",
					tjMap);
		}catch(Exception e){
			log.error("菜单和按钮获取失败");
		}
		// 初始化菜单按钮集合
		Set<String> menuAndButtonsIds = new HashSet<String>();
		Map<String,String> menuAndDirPath = new HashMap<String,String>();
		//用户组分类和用户组的映射
		Map<String, String> postAndRoleMap = queryPostAndRoleMap(foundedUser);
		/**
		 * 设置用户左边菜单栏信息
		 */
		for (Map<String, String> menuAndButton : menuAndButtons) {
			menuAndButtonsIds.add(menuAndButton.get("FUNC_ID"));//用户分配的菜单和按钮
			String dirPath = menuAndButton.get("DIR_PATH");
			if(!"null".equals(dirPath)){
				if(dirPath.indexOf(".do")>-1){
					parseDirPath(menuAndDirPath,dirPath,menuAndButton.get("FUNC_ID"));
				}
			}
		}
		//查询扩展属性信息，仓库使用
		List<String> attrList = queryPostAndAttrMap(foundedUser);
		
		// 设置user的caption,即name
		loginUser.setCaption(foundedUser.getName());
		// 设置user的password
		loginUser.setPassword(foundedUser.getPassword());
		// 设置user的loginname
		Set<String> postIds = new HashSet<String>();
		postIds.addAll(postAndRoleMap.values());//用户组集合转换 Collection -> Set
		
		loginUser.setUsername(foundedUser.getLoginname());//登录用户名
		loginUser.setPostIds(postIds);// 设置用户组集合
		loginUser.setRoleIds(postAndRoleMap.keySet());//设置角色集合
		loginUser.setPostAndRoleMap(postAndRoleMap);
		loginUser.setMenuAndButtonsIds(menuAndButtonsIds);// 设置菜单树集合
		loginUser.setAttrList(attrList);
		/**
		 * 获取零件流系统所有的按钮信息,将所有按钮和菜单放入SESSION中，
		 * 再在AthenaIniShiroFilter中将其获取出来，为控制按钮权限作准备
		 */
		//List<PageButton> buttons = pageButtonService.list(new PageButton());
		//初始化用户组管理缓存对象集合
		List<CacheValue> pageButtonCache = new ArrayList<CacheValue>();
		Map<String,String> buttonMap = new HashMap<String,String>();
		Map<String,String> pageButtonMap = new HashMap<String,String>();
		Map<String,List<String>> menuButtonMap = new HashMap<String,List<String>>();
		if(cm.getCacheInfo("getAllCachePageButton")!=null){
			//业务缓存：KEY为MENU_ID,VALUE为BUTTON_ID:BUTTON_CODE
			pageButtonCache =(List<CacheValue>) cm.getCacheInfo("getAllCachePageButton").getCacheValue();
			List<String> buttonList = new ArrayList<String>();
			for (int i = 0; i < pageButtonCache.size(); i++) {
				CacheValue cacheValue = (CacheValue) pageButtonCache.get(i);
				String menuId = cacheValue.getKey();
				String value = cacheValue.getValue();
				String[] values = value.split(":");
				buttonMap.put(values[0], values[1]);
				pageButtonMap.put(values[0], menuId);
				if(menuButtonMap.get(menuId) != null ){
					buttonList = menuButtonMap.get(menuId);
					buttonList.add(values[1]);
				}else{
					buttonList = new ArrayList<String>();
					buttonList.add(values[1]);
				}
				menuButtonMap.put(menuId, buttonList);
			}
		}
		SecurityUtils.getSubject().getSession().setAttribute("buttonMap", buttonMap);
		SecurityUtils.getSubject().getSession().setAttribute("pageButtonMap", pageButtonMap);
		SecurityUtils.getSubject().getSession().setAttribute("menuButtonMap", menuButtonMap);
		List<MenuDirectory> menuDirectoryList = menuDirectoryService.list(new MenuDirectory());
		Map menuBeanMap = new HashMap();
		Map<String,String> menuNameAll = new HashMap<String,String>();
		Map<String,String> menuDirPathAll = new HashMap<String,String>();
		for(MenuDirectory bean:menuDirectoryList){
			String menuId = bean.getId();
			String menuParentId = bean.getParentId();
			Long dirType = bean.getDirType();
			if(menuParentId!=null&&dirType!=null){
				if(dirType.equals(1l)){
					menuBeanMap.put(menuParentId, menuId);
				}
			}
			//生产菜单名称Map
			menuNameAll.put(menuId, bean.getDirName());
			String dirPath = bean.getDirPath();
			if( bean.getDirPath() != null && !"".equals(bean.getDirPath()) && "1".equals(bean.getBiaos())){
				if(dirPath.indexOf(".do")>-1){
					menuDirPathAll.put(dirPath.substring(0, dirPath.indexOf(".do")+3), bean.getId());
					parseDirPath(menuDirPathAll,dirPath,bean.getId());
				}
			}
		}
		
		if("ROOT".equals(foundedUser.getLoginname())){
			menuAndDirPath.putAll(menuDirPathAll);
		}
		SecurityUtils.getSubject().getSession().setAttribute("menuBeanMap", menuBeanMap);
		SecurityUtils.getSubject().getSession().setAttribute("menuDirPathAllMap", menuDirPathAll);
		SecurityUtils.getSubject().getSession().setAttribute("postAuthDirPathMap", menuAndDirPath);
		SecurityUtils.getSubject().getSession().setAttribute("menuNameAllMap", menuNameAll);
		//设置用户中心
		loginUser.setUsercenter(usercenter);
		//设置用户所属用户组授权后的用户中心
		List ucList = new ArrayList();
		if(!loginname.equals("ROOT")){
			ucList = postService.getRoleucByPostCode(loginname);
		}else{
			ucList = postService.getAllUsercenter();
		}
		loginUser.setUcList(ucList);
		
		List<Map<String,String>> postCodeUcList = new ArrayList<Map<String,String>>();
		postCodeUcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("authority.getAllPostCodeUsercenter",param);
		Map dicCodeUcMap = new HashMap();
		for(Map<String,String> mapPostCode:postCodeUcList){
			String dicCode = mapPostCode.get("DIC_CODE");
			String uc = mapPostCode.get("USERCENTER");
			if(dicCode!=null){
				if(dicCodeUcMap.get(dicCode)==null){
					List tempUcList = new ArrayList();
					tempUcList.add(uc);
					dicCodeUcMap.put(dicCode, tempUcList);
				}else{
					List tempUcList = (List)dicCodeUcMap.get(dicCode);
					tempUcList.add(uc);
					dicCodeUcMap.put(dicCode, tempUcList);
				}
			}
		}
		loginUser.setDicCodeUcMap(dicCodeUcMap);
		return loginUser;
	}
	
	@SuppressWarnings("unchecked")
	public LoginUser ckGetUserByLoginname(String loginname,String usercenter) {
		//初始化查询参数对象
		User param = new User();
		//设置登录名loginname
		param.setLoginname(loginname);
		User foundedUser = null;//存储登录的用户
		// 根据用户名loginname,执行查询,获取User
		foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace() + ".getUserByLoginname",param);
		if (foundedUser == null){
			return null;//如果用户为空，则返回空
		}else if(foundedUser.getBiaos().equals("0")){
			throw new AccountStopException("用户帐户被停用!");
		}
		LoginUser loginUser = new LoginUser();// 组织登录对象
		
		//获取用户菜单和按钮信息
		List<Map<String, String>> menuAndButtons = new ArrayList<Map<String, String>>();
		// 执行查询
		Map tjMap = new HashMap();
		tjMap.put("loginname", foundedUser.getLoginname());
		tjMap.put("usercenter", usercenter);
		try{
			menuAndButtons = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getNewMenuAndButtons",
					tjMap);
		}catch(Exception e){
			log.error("菜单和按钮获取失败");
		}
		//用户组分类和用户组的映射
		Map<String, String> postAndRoleMap = queryPostAndRoleMap(foundedUser);
		// 设置user的caption,即name
		loginUser.setCaption(foundedUser.getName());
		loginUser.setUsername(foundedUser.getLoginname());//登录用户名
		// 设置user的loginname
		Set<String> postIds = new HashSet<String>();
		postIds.addAll(postAndRoleMap.values());//登录用户所拥有的用户组集合
		loginUser.setPostIds(postIds);// 设置用户组集合
		loginUser.setRoleIds(postAndRoleMap.keySet());//设置角色集合
		loginUser.setPostAndRoleMap(postAndRoleMap);
		// 初始化菜单按钮集合
		Set<String> menuAndButtonsIds = new HashSet<String>();
		/**
		 * 设置用户左边菜单栏信息
		 */
		for (Map<String, String> menuAndButton : menuAndButtons) {
			menuAndButtonsIds.add(menuAndButton.get("FUNC_ID"));//用户分配的所有的菜单ID和按钮ID
		}
		loginUser.setMenuAndButtonsIds(menuAndButtonsIds);// 设置菜单树集合
		List<String> attrList = queryPostAndAttrMap(foundedUser);
		loginUser.setAttrList(attrList);

		//设置用户中心
		loginUser.setUsercenter(usercenter);
		//设置用户所属用户组授权后的用户中心
		List ucList = new ArrayList();
		if(!loginname.equals("ROOT")){
			ucList = postService.getRoleucByPostCode(loginname);
		}else{
			ucList = postService.getAllUsercenter();
		}
		loginUser.setUcList(ucList);
		return loginUser;
	}
	private List<String> queryPostAndAttrMap(User user) {
		Assert.notNull(user,"user is null!");//
		List<Map<String, String>> postAndAttrs = new ArrayList<Map<String, String>>();
		//获取登录用户的用户组分类和用户组信息
		postAndAttrs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getLoginAttr", user);
		/*
		 * 从查询的对象中转换为Map<用户组分类,用户组>
		 */
		List<String> attrList = new ArrayList<String>();
		for (Map<String, String> postAndAttr : postAndAttrs) {
			String attr = postAndAttr.get("ATTR");
			attrList.add(attr);
		}
		return attrList;
	}

	/**
	 * 根据用户查找用户组分类和用户组信息
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> queryPostAndRoleMap(User user){
		Assert.notNull(user,"user is null!");//
		List<Map<String, String>> postAndRoles = new ArrayList<Map<String, String>>();
		//获取登录用户的用户组分类和用户组信息
		postAndRoles = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".getLoginPosts", user);
		Map<String, String> postAndRoleMap = new HashMap<String,String>();
		//把查询到的用户组和用户组分类加入当前对象
		String roleId,postId;//用户组分类id和用户组id
		/*
		 * 从查询的对象中转换为Map<用户组分类,用户组>
		 */
		for (Map<String, String> postAndRole : postAndRoles) {
			roleId = postAndRole.get("DIC_CODE");
			postId = postAndRole.get("POST_CODE");
			if(roleId!=null&&postId!=null){//都不为空的情况下
				postAndRoleMap.put(roleId, postId);
			}
			postId = null;
			roleId = null;
		}
		return postAndRoleMap;
	}

	/**
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	public AthenaAccount getAccount(String username, String realmName,String usercenter) throws Exception {
		// 从数据库查找用户名和密码信息
		LoginUser loginUser = this.getUserByLoginname(username,usercenter);

		if (loginUser == null){
			return null;
		}
		AthenaAccount account = new AthenaAccount(loginUser, realmName);
		Set<String> menuAndButtonsIds = loginUser.getMenuAndButtonsIds();
		account.setMenuAndButtonsIds(menuAndButtonsIds);
		account.addRole(loginUser.getPostIds());
		return account;
	}


	/**
	 * 重置密码
	 * 
	 * @param user
	 */
	public void resetPassword(User user) {
		// 执行重置密码
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace() + ".resetPassword", user);
	}

	/**
	 * 保存用户组与用户对应信息
	 * 
	 * @param postIds
	 * @param userId
	 */
	@Transactional
	public void saveRPostUser(List<String> sysPosts, String userId) {
		// 先删除之前的userId的用户组与用户对应的关系
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace() + ".deleteRPostUser", userId);
		List<String> list = new ArrayList<String>();
		//由于 页面业务和用户组的ID有可能会重复，他们都会赋值RADIO，则先将其过滤一遍，再新生成新的List
		for (int i = 0; i < sysPosts.size(); i++) {
			String postId = sysPosts.get(i);
			if(!list.contains(postId)){
				list.add(postId);
			}
		}
		for(int i=0;i<list.size();i++){
			// 执行保存
			String postId = list.get(i);
			Map map = new HashMap();
			map.put("postCode", postId);
			map.put("userId", userId);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace() + ".saveRPostUser", map);
		}
	}

	/**
	 * 获取系统用户组与业务用户组树信息
	 * 
	 * @return
	 */
	public HtmlTreeNode getPostGroupTree() {
		// 初始化PostGroup
		Dic bean = new Dic();
		// 获取用户组分类信息
		List<Dic> dics = dicService.list(bean);
		// 设置根结点
		HtmlTreeNode treeNode = new DefaultHtmlTreeNode("root", "用户组");
		// 初始化用户组
		Post post = new Post();
		// 设置用户组为有效
		post.setBiaos("1");
		// 执行查询
		List<Post> posts = baseDao
				.select(getNamespace() + ".listPostAll", post);
		Map map = new HashMap();
		// 设置用户组分组与用户组对应信息
		for (Post postBean : posts) {
			String parentId = postBean.getDicCode();
			/**
			 * 判断用户组分类ID为空，则初始化List,加入bean,并将parentId设为key,value则为list;
			 * 如果不为空，则获取该parentId的List信息，并加入新的bean
			 */
			if(parentId!=null){
				if (map.get(parentId) == null || map.get(parentId).equals("")) {
					List list = new ArrayList();
					list.add(postBean);
					map.put(parentId, list);
				} else if (map.get(parentId) != null
						&& !map.get(parentId).equals("")) {
					List list = (List) map.get(parentId);
					list.add(postBean);
					map.put(parentId, list);
				}
			}
		}
		// 建立业务用户组Tree
		for (Dic dic : dics) {
			String id = dic.getDicCode();
			String text = dic.getDicName();
			HtmlTreeNode node = new DefaultHtmlTreeNode(id, text);
			if (map.get(id) != null && !map.get(id).equals("")) {
				List<Post> list = (List) map.get(id);
				for (Post postBean : list) {
					String postCode = postBean.getPostCode();
					String postuc = postBean.getUsercenter();
					String postName = postBean.getPostName();
					String nodeId = postuc+"-"+postCode;
					HtmlTreeNode newNode = new DefaultHtmlTreeNode(nodeId,
							postName, "", "post");
					node.addChild(newNode);
				}
			}
			treeNode.addChild(node);
		}
		//
		return treeNode;
	}
	public HtmlTreeNode getPostUsercenterTree() {
		HtmlTreeNode treeNode = new DefaultHtmlTreeNode("root", "业务用户组");
		Post bean = new Post();
		bean.setBiaos("1");
		List<Post> postList = (List<Post>) postService.listAll(bean);
		// 设置用户组分组与用户组对应信息
		Map map = new HashMap();
		Dic dicBean = new Dic();
		//获取所有业务信息
		List<Dic> dics = dicService.list(dicBean);
		//以业务标识(dicCode)为KEY，以业务名称(dicName)为NAME
		Map<String,String> dicMap = new HashMap<String,String>();
		for(Dic dic:dics){
			String dicCode = dic.getDicCode();
			String dicName = dic.getDicName();
			dicMap.put(dicCode, dicName);
		}
		List<String> dicList = new ArrayList<String>();
		for (Post post : postList) {
			String dicCode = post.getDicCode();
			/*
			  判断用户组分类ID为空，则初始化List,加入bean,并将parentId设为key,value则为list;
			  如果不为空，则获取该parentId的List信息，并加入新的bean
			 */
			if (map.get(dicCode) == null || map.get(dicCode).equals("")) {
				List templist = new ArrayList();
				templist.add(post);
				map.put(dicCode, templist);
			} else if (map.get(dicCode) != null
					&& !map.get(dicCode).equals("")) {
				List templist = (List) map.get(dicCode);
				templist.add(post);
				map.put(dicCode, templist);
			}
			if(!dicList.contains(dicCode)){
				dicList.add(dicCode);
			}
		}
		int size = dicList.size();
		for (int i = 0; i < size; i++) {
			String  dicCode= (String) dicList.get(i);
			//以业务标识（DIC_CODE）为树点标识，以业务标识+业务名称为树结点名称
			String dicName = "";
			dicName = dicMap.get(dicCode);
			String tempDicName = "";
			if(dicName!=null&&!"".equals(dicName)){
				tempDicName = "("+dicName+") &nbsp;&nbsp;<span class='qc' onclick=zk('"+dicCode+"')>清除</span>";
			}
			HtmlTreeNode node = new DefaultHtmlTreeNode(dicCode, dicCode+tempDicName);
			if (map.get(dicCode) != null && !map.get(dicCode).equals("")) {
				List<Post> postTreeList = (List) map.get(dicCode);
				for (Post postBean : postTreeList) {
					String postCode = postBean.getPostCode();
					String postName = "";
					postName = postBean.getPostName();
					String temp = "";
					if(postName!=null){
						temp = "("+postName+")";
					}
					HtmlTreeNode newNode = new DefaultHtmlTreeNode(postCode,
							postCode+temp, "", "post");
					node.addChild(newNode);
				}
			}
			treeNode.addChild(node);
		}
		
		
		
		return treeNode;
	}
	/**
	 * 获取用户对应的用户组信息
	 * 
	 * @param id
	 * @return
	 */
	public Object listUserPosts(String id) {
		List<Map<String, String>> list = new ArrayList();
		Map map = new HashMap();
		map.put("id", id);
		list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace() + ".listUserPosts", map);
		return list;
	}
	/**
	 * 改变用户状态
	 * @param user
	 */
	public void changeUserActive(User user) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".changeUserActive", user);		
	}
	/**
	 * 验证用户维一性
	 * @param user
	 * @return
	 */
	public String validateOnlyUser(User user) {
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".validateOnlyUser", user);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg; 
	}

	public String updateUserPassword(Map<String, String> map) {
		// 使用MD5加密用户密码
		String password = map.get("password");
		// 密码加密并设置到保存对象中
		map.put("password", new Md5Hash(password).toHex());
		password = map.get("oldpassword");
		map.put("oldpassword", new Md5Hash(password).toHex());
		map.put("loginname", map.get("username"));
		User foundedUser = null;//存储登录的用户
		// 根据用户名loginname,执行查询,获取User
		foundedUser = (User)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace() + ".getUserByLoginname",map);
		//修改密码前验证旧密码
		if(map.get("oldpassword") != null && map.get("oldpassword").equals(foundedUser.getPassword())){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updateUserPassword",map);
			return "修改成功!";
		}else{
			throw new ServiceException("旧密码不正确，无法修改，请重新输入!");
		}
	}

	/**
	 * 解析菜单id和DIR_PATH
	 * @param user
	 * @return
	 */
	public void parseDirPath(Map<String, String> map,String dirpath,String id) {
		String head = dirpath.substring(0, dirpath.indexOf(".do")+3);
		String param = dirpath.substring(dirpath.indexOf(".do")+3, dirpath.length());
		if(param.indexOf("SYSAUTH")>-1){
			String temp = param.substring(param.indexOf("SYSAUTH"));
			int end = temp.indexOf("&")>-1?temp.indexOf("&"):temp.length();
			String sysAuth = temp.substring(temp.indexOf("=")+1, end).trim();
			map.put(head+"_"+sysAuth, id);
		}else{
			map.put(head, id);
		}
	}
	
}
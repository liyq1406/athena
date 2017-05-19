/**
 * 
 */
package com.athena.authority.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.CkDataType;
import com.athena.authority.entity.DataType;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.Post;
import com.athena.authority.entity.PostDataItem;
import com.athena.authority.service.DataTypeService;
import com.athena.authority.service.MenuDirectoryService;
import com.athena.authority.service.PostService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PostAction extends ActionSupport {
	@Inject
	private PostService postService;
	@Inject
	private DataTypeService dataTypeService;
	@Inject
	private MenuDirectoryService menuDirectoryService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private  CacheManager cacheManager;//缓存
	/**
	 * 用户组管理查询页面
	 * 
	 * @param bean
	 * @return
	 */
	public String execute(@Param Post bean) {
		//获取用户中心树
		this.setRequestAttribute("usercenterTree", menuDirectoryService.getUsercenterTree());
		return "select";
	}

	/**
	 * 用户组管理执行查询
	 * 
	 * @param bean
	 * @return
	 */
	public String query(@Param Post bean) {
		/**
		 * 获取所有用户组信息
		 * 过滤空格
		 */
			String postCode = bean.getPostCode();
			if(postCode!=null){
				String tempPostCode = postCode.trim();
				bean.setPostCode(tempPostCode);
			}
			String postName = bean.getPostName();
			String temp = "";
			if (postName != null) {
				temp = postName.trim();
			}
			bean.setPostName(temp);
			setResult("result", postService.select(bean));
		return AJAX;
	}

	/**
	 * listPost.ajax对应的方法
	 * @param bean
	 * @return
	 */
	public String list(@Param Post bean) {
		/**
		 * 设置状态为1、有效
		 */
		bean.setBiaos("1");
		/**
		 * 获取所有有效用户组信息,返回map
		 */
		setResult("result", postService.listPostAll(bean));
		return AJAX;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 */
	public String listAll(@Param Post bean) {
		/**
		 * 设置状态为1、有效
		 */
		bean.setBiaos("1");
		/**
		 * 设置所有用户组信息,返回list
		 */
		setResult("result", postService.listAll(bean));
		return AJAX;
	}

	/**
	 * 单记录查询
	 * 
	 * @param bean
	 */
	public String get(@Param Post bean) {
		/**
		 * 获取单记录用户组
		 */
		setResult("result", postService.get(bean));
		this.setRequestAttribute("method", "1");
		return AJAX;
	}

	/**
	 * 数据保存
	 * 
	 * @param bean
	 */
	public String save(@Param("operant") String operant, @Param Post bean) {
		/**
		 * 保存用户组
		 */
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		if (operant.equals("1")) {
			String msg = postService.validateOnlyPost(bean.getPostCode());
			if(msg.equals("0")){
				try{
					bean.setCreator(loginUser.getUsername());
					bean.setCreateTime(DateUtil.curDateTime());
					postService.save(bean);
					setResult("result", new Message("保存成功!"));
					userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","新增数据"+bean.getPostCode()+"操作成功");
				}catch(Exception e){
					userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","新增数据"+bean.getPostCode()+"操作结束",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
				}
			}else{
				setErrorMessage("保存失败，该用户组标识已存在!");
			}
		} else if (operant.equals("2")) {
			try{
				bean.setMender(loginUser.getUsername());
				bean.setModifyTime(DateUtil.curDateTime());
				if(!postService.getPostByPostCode(bean)){
					setResult("result", new Message("必须取消用户组下所有用户，才能修改所属业务!"));
					return AJAX;
				}
				postService.update(bean);
				setResult("result", new Message("保存成功!"));
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","修改数据"+bean.getPostCode()+"操作成功");
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","新增数据"+bean.getPostCode()+"操作成功",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
			//mantis 0004915 gswang 2012-10-23
//			CacheManager cm = CacheManager.getInstance();
//			Object object = postService.refreshAllData();
//			cm.refreshCache("queryDicCode", object);
			cacheManager.refreshCache("queryDicCode");//刷新缓存 	
		}
		//gswang 2014-05-06 刷新用户组缓存
		cacheManager.refreshCache("queryPostDicCache");//刷新用户组缓存 	
		cacheManager.refreshCache("getJihz");//刷新计划员组缓存 	
		return AJAX;
	}

	/**
	 * 数据删除（暂时未用）
	 * 
	 * @return
	 */
	public String remove(@Param Post bean) {
		/**
		 * 删除用户组信息
		 */
		postService.doDelete(bean);
		return AJAX;
	}
	public String delPost(@Param("postCode") String postCode,@Param("usercenter") String usercenter){
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("postCode", postCode);
			map.put("usercenter", usercenter);
			setResult("result", postService.delPost(map));
			//gswang 2014-05-06 刷新用户组缓存
			cacheManager.refreshCache("queryPostDicCache");//刷新用户组缓存 	
			cacheManager.refreshCache("getJihz");//刷新计划员组缓存 	
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理","修改数据"+postCode+"操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理","删除数据"+postCode+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 获取用户组数据权限项
	 * 
	 * @param bean
	 * @return
	 */
	public String getPostDataItems(@Param Post bean) {
		/**
		 * 获取用户组数据权限项
		 */
		setResult("result", postService.getPostDataItems(bean));
		return AJAX;
	}

	/**
	 * 保存用户组数据权限项
	 * 
	 * @param bean
	 * @return
	 */
	public String savePostDataItems(
			@Param("postDataItems") ArrayList<PostDataItem> postDataItems,
			@Param("postCode") String postCode, @Param("dataId") String dataId) {
		/**
		 * 保存用户组数据权限项
		 */
		String msg = postService.savePostDataItems(postDataItems, postCode, dataId);
		Message message = null;
		if(msg.equals("1")){
			message = new Message("保存成功!");
		}else{
			message = new Message("保存失败，请确认业务数据表是否配置数据表、用户组字段、权限字段等信息!");
		}
		setResult("result",message);
		return AJAX;
	}

	/**
	 * 启停用户组
	 * 
	 * @param postIds
	 * @param actives
	 * @return
	 */
	public String changePostActive(@Param("postCodes") ArrayList<String> postCodes,@Param("usercenters") ArrayList<String> usercenters,
			@Param("biaoss") ArrayList<String> biaoss) {
		Post post = new Post();
		// 设置用户组ID主键
		post.setPostCode(postCodes.get(0));
		//mantis 0004918 gswang 2012-10-23
//		post.setUsercenter(usercenters.get(0));
		// 设置所选择用户组状态
		String active = biaoss.get(0);
		String oper = "";
		String nowActive = "";
		if (active == null || "".equals(active)) {
			nowActive = "1";
			oper = "启动";
		}
		if (active != null && !"".equals(active) && active.equals("0")) {
			nowActive = "1";
			oper = "启动";
		}
		if (active != null && !"".equals(active) && active.equals("1")) {
			nowActive = "0";
			oper = "停用";
		}
		post.setBiaos(nowActive);
		// 启/停用户
		try{
			int count = postService.checkPostUser(post);
			if(count==0){
				postService.changePostActive(post);
				setResult("rebiaos","1");
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理",oper+"数据"+post.getPostCode()+"操作成功");
			}else{
				Message message = null;
				message = new Message("用户组下有对应的用户，无法停用该用户组!请取消用户组下用户后再停用!");
				setResult("result",message);
				setResult("rebiaos","-1");
			}
			//gswang 2014-05-06 刷新用户组缓存
			cacheManager.refreshCache("queryPostDicCache");//刷新用户组缓存 	
			cacheManager.refreshCache("getJihz");//刷新计划员组缓存 	
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理",oper+"数据"+post.getPostCode()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}

	/**
	 * 获取已经分配的数据权限项
	 * 
	 * @param itemBean
	 * @return
	 */
	public String getAllPostDataItems(@Param PostDataItem itemBean,@Param("ucid") String ucid) {
		// 数据权限类型ID
		String dataId = itemBean.getDataId();
		if (dataId != null && !"".equals(dataId)) {
			// 数据文本
			String text = itemBean.getText();
			DataType bean = new DataType();
			bean.setDataId(dataId);
			// 获取数据权限类型
			DataType dataType = (DataType) dataTypeService.get(bean);
			String dataParam = dataType.getDataParam();
			String usercenter = dataType.getUsercenter();
			// 获取已经分配的数据权限项
			Map map = postService
			.getAllPostDataItems(dataParam, itemBean, text,usercenter);
			setResult("result", map);
		} else {
			setResult("result", new Message("所属数据权限不能为空!"));
		}

		return AJAX;
	}

	/**
	 * 获取用户组信息
	 * 
	 * @param groupId
	 * @return
	 */
	public String listPostByGroupid(@Param("groupId") String groupId,@Param("usercenter") String usercenter) {
		// 设置用户组分类ID值
		Post post = new Post();
		post.setPostGroupId(groupId);
		post.setUsercenter(usercenter);
		Map map = postService.listPostByGroupid(post);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 全部停用指定用户组分类下的用户组信息
	 * 
	 * @param postGropuId
	 * @return
	 */
	public String disabledPostByGroupid(@Param("postGropuId") String postGropuId,@Param("biaos") String biaos) {
		postService.disabledPostByGroupid(postGropuId,biaos);
		return AJAX;
	}

	/**
	 * 验证POST_CODE的唯一性
	 * 
	 * @param postCode
	 * @return
	 */
	public String validateOnlyPost(@Param("postCode") String postCode) {
		setResult("result", postService.validateOnlyPost(postCode));
		return AJAX;
	}
	/**
	 * 保存仓库数据授权的信息
	 * @param ckPostDataItems
	 * @param postCode
	 * @param cuncTableName
	 * @return
	 */
	public String savePostDataItemsCk(
			@Param("ckPostDataItems") ArrayList<String> ckPostDataItems,
			@Param("postCodeForm") String postCode,
			@Param("cuncTableNameForm") String cuncTableName) {
		/**
		 * 保存用户组数据权限项
		 */
		try{
			LoginUser loginUser = AuthorityUtils.getSecurityUser();
			Message msg = postService.savePostDataItemsCk(ckPostDataItems, postCode,
					cuncTableName,loginUser);
			setResult("result", msg);
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理",postCode+"保存仓库数据操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理",postCode+"保存仓库数据操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 仓库数据授权时，获取该用户组已经拥有的数据授权信息
	 */
	public String listSysDataGroup(@Param("id") String ckDTId,@Param("postCode") String postCode){
		CkDataType bean = new CkDataType();
		bean.setId(ckDTId);
		setResult("result",postService.listSysDataGroup(bean,postCode));
		return AJAX;
	}
	/**
	 * 保存用户中心
	 * @param postCode
	 * @param ucIds
	 * @return
	 */
	public String saveAuthUsercenter(@Param("postCode") String postCode,
			@Param("ucIds") ArrayList<String> ucIds){
		try{
			setResult("result", postService.saveAuthUsercenter(postCode,ucIds));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户组管理",postCode+"保存用户中心操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户组管理",postCode+"保存仓库数据操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 根据POST_CODE获取该用户组已经分配过的用户中心
	 * @param postCode
	 * @return
	 */
	public String getRoleuc(@Param("postCode") String postCode){
		setResult("result", postService.getRoleuc(postCode));
		return AJAX;
	}
	/**
	 * 未用
	 * @return
	 */
	public String getRoleucByPostCode(){
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String loginName = loginUser.getUsername();
		List list = new ArrayList();
		if(!loginName.equals("ROOT")){
//			List<String> uClist = postService.getRoleucByPostCode(loginName);
			List<String> uClist = postService.getAllUsercenter();
			for (int i = 0; i < uClist.size(); i++) {
				String key = uClist.get(i);
				Map map = new HashMap();
				map.put("USERCENTER", key);
				list.add(map);
			}
		}else{
			CacheManager cm = CacheManager.getInstance();
			List cachelist = (List<CacheValue>) cm.getCacheInfo("queryUserCenterMap").getCacheValue();
			int size = cachelist.size();
			for (int i = 0; i < size; i++) {
				CacheValue cacheValue = (CacheValue) cachelist.get(i);
				String key = cacheValue.getKey();
				Map map = new HashMap();
				map.put("USERCENTER", key);
				list.add(map);
			}
		}
		setResult("result",list);
		return AJAX;
	}
}

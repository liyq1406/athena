/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.MenuDirectory;
import com.athena.authority.entity.PageButton;
import com.athena.authority.entity.PostGroup;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class PostGroupService extends BaseService<PostGroup>{
	// log4j日志初始化
	private final Log log = LogFactory.getLog(PostGroupService.class);
	@Inject 
	private MenuDirectoryService menuDirectoryService;
	@Inject
	private PageButtonService pageButtonService;// 按钮
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}
	
	/**
	 * 查找角色授权的菜单和按钮
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getAuthorizedIds(Map<String,String> bean){
		//获取用户组分类下的菜单和按钮信息
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getAuthorized", bean);
		//返回 数组
		return list;
	}
	/**
	 * 保存角色信息
	 * @param bean
	 * @param loginUser
	 * @return
	 */
	public Integer save(PostGroup bean,LoginUser loginUser){
		String postGroupId = bean.getPostGroupId();
		log.info("POST_GROUP_ID:"+postGroupId);
		int result = 0;
		if(postGroupId!=null&&!"".equals(postGroupId)){
			//重新设置PostGroup中的主键，由用户输入
			bean.setId(bean.getId());
			String username = loginUser.getUsername();
			bean.setCreator(username);
			String currentDatetime = DateUtil.curDateTime();
			bean.setCreateTime(currentDatetime);
			//执行插入
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".insertPostGroup", bean);
		}else{
			log.error("postGroupId is null ");
		}
		
		return result;
	}
	/**
	 * 更新用户组
	 * @param bean
	 * @return
	 */
	public Integer update(PostGroup bean,LoginUser loginUser){
		//重新设置PostGroup中的主键，由用户输入
		String postGroupId = bean.getPostGroupId();
		log.info("POST_GROUP_ID:"+postGroupId);
		int result = 0;
		if(postGroupId!=null&&!"".equals(postGroupId)){
			bean.setId(bean.getId());
			String username = loginUser.getUsername();
			bean.setMender(username);
			String currentDatetime = DateUtil.curDateTime();
			bean.setModifyTime(currentDatetime);
			//执行插入
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updatePostGroup", bean);
		}else{
			log.error("postGroupId is null ");
		}
		return result;
	}
	/**
	 * 删除用户 组
	 * @param bean
	 * @return
	 */
	public Integer delPostGroup(Map<String,String> map){
		//查出该用户组是否在其他表中使用
		Integer postCounts = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".countPostGroup", map);
		int result = 0;
		if(postCounts==0){
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deletePostGroup", map);
		}else{
			result = -1;
		}
		return result;
	}
	/**
	 * 保存角色授权信息
	 * 角色授权时，由于该角色授权为AJAX操作并且菜单比较多，而点开每一级菜单，则会比较烦琐
	 * 故有些菜单只选择一级菜单，则需要选择其下级所有菜单
	 * @param bean
	 * @param menuAndButtonIds
	 * @param menuAndButtonIdsType
	 * @return
	 */
	@Transactional
	public Message saveAuthorized(PostGroup bean, ArrayList<String> menuAndButtonIds,ArrayList<String> menuAndButtonIdsType) {
		Date currentDate = new Date();
		log.info("开始保存功能权限："+currentDate);
		/**
		 * 获取所有的菜单信息
		 * 将菜单根据PARENT_ID进行分类，以PARENT_ID为KEY,menu_ID为VALUE
		 */
		List<MenuDirectory> menus = menuDirectoryService.list(new MenuDirectory());//查询所有菜单信息
		Map<String,List<String>> menuMap = new HashMap<String,List<String>>();
		for(MenuDirectory menu:menus){
			String id = menu.getId();//菜单主键
			String parentId = menu.getParentId();//菜单父ID
			//如果菜单父ID不为空并且menuMap中获取菜单父ID的值为空,则新创建List，并将菜单ID加入List中，反之则获取menuMap中的父ID所对应的List
			if(parentId!=null&&menuMap.get(parentId)==null){
				//新创建List对象
				List<String> list = new ArrayList<String>();
				//List中加入ID
				list.add(id);
				//以parentId为KEY，LIST为VALUE，加入List中
				menuMap.put(parentId, list);
			}else if(parentId!=null&&menuMap.get(parentId)!=null){
				//获取menuMap中parentId所对应的list
				List<String> list = menuMap.get(parentId);
				//将list中加入id
				list.add(id);
				menuMap.put(parentId, list);
			}
		}
		/**
		 * 获取系统中所有初始化BUTTON信息
		 * 以menu_id为KEY,MENU_ID下的BUTTON_ID集合为VALUE
		 */
		List<PageButton> buttons = pageButtonService.list(new PageButton());
		Map<String,List<String>> buttonMap = new HashMap<String,List<String>>();
		for(PageButton button:buttons){
			String id = button.getId();//获取BUTTON_ID
			String menuId = button.getMenuDirectory().getId();//获取MENU_ID
			//如果MENU_ID不为空，并且buttonMap中获取menuId的值为空，则新创建List，并且在list中加入数据button_id,反之不为空，则获取buttonMap中的menuId下的List集合
			if(menuId!=null&&buttonMap.get(menuId)==null){
				//新建List集合对象
				List<String> list = new ArrayList<String>();
				//加入数据
				list.add(id);
				buttonMap.put(menuId, list);
			}else if(menuId!=null&&buttonMap.get(menuId)!=null){
				//获取menuId下的对应的List集合
				List<String> list = buttonMap.get(menuId);
				//在LIST中加入button_id
				list.add(id);
				buttonMap.put(menuId, list);
			}
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("postGroupId", bean.getPostGroupId());
		//删除角色授权信息
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".clearAuthorized", params);
		List<String> newCon = new ArrayList<String>();
		for(String id:menuAndButtonIdsType){
			if(id.indexOf("-")>0){
				String[] ids = id.split("-");
				/**
				 * id以"-"为分隔符，usercenter,funcid,type
				 * type有两个值，0为不全选下级菜单，1为全选下级菜单
				 */
				
				if(ids.length==2){
					String funcId = ids[0];
					String type = ids[1];
					if(!newCon.contains(funcId)){
						newCon.add(funcId);
					}
					if(type.equals("0")){
						if(!newCon.contains(funcId)){
							newCon.add(funcId);
						}
					}else if(type.equals("1")){
						this.getFuncIds(newCon, menuMap, buttonMap, funcId);
					}
				}
			}
		}
		//插入关系表SYS_R_POST_AUTH中
		for(String id:newCon){
				Map<String,Object> paramInfos = new HashMap<String,Object>();
				paramInfos.put("postGroupId", bean.getPostGroupId());
				paramInfos.put("funcId", id);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".saveAuthorized", paramInfos);
		}
		Date currentDate1 = new Date();
		log.info("结束保存功能权限："+currentDate1);
		log.info("结束保存功能权限耗时："+(currentDate1.getTime()-currentDate.getTime()));
		return new Message("保存成功!");
	}
	/**
	 * 递归获取所有下级菜单
	 * 递归条件：
	 * 菜单MENU_ID不存在下级菜单为递归结束条件
	 * @param newCon
	 * @param menuMap
	 * @param buttonMap
	 * @param usercenter
	 * @param funcId
	 * @return
	 */
	public List getFuncIds(List<String> newCon,Map<String,List<String>> menuMap,Map<String,List<String>> buttonMap,String funcId){
		List<String> newlist = menuMap.get(funcId);
		String newString = "";
		if(newlist!=null&&newlist.size()>0){
			for(String newId:newlist){
				if(!newCon.contains(newId)){
					newCon.add(newId);
				}
				if(menuMap.get(newId)!=null){
					this.getFuncIds(newCon, menuMap, buttonMap, newId);
				}else if(menuMap.get(newId)==null){
					if(buttonMap.get(newId)!=null){
						List<String> newButton = buttonMap.get(newId);
						for(String buttonId:newButton){
							newString = "button_"+buttonId;
							if(!newCon.contains(newString)){
								newCon.add(newString);
							}
						}
					}
				}
			}
		}
		return newCon;
	}
	/**
	 * 验证维一性
	 * @param postGroupId
	 * @return
	 */
	public String validateOnly(String postGroupId) {
		PostGroup postGroup = new PostGroup();
		postGroup.setPostGroupId(postGroupId);
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".validateOnly", postGroup);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg; 
	}
	/**
	 * 根据用户名获取其用户中心
	 * @param loginname
	 * @return
	 */
	public Object getUsercenterByLoginName(String loginname) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loginName", loginname);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getUsercenterByLoginName", map);
	}

	public List listAllPostGroup() {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".listAllPostGroup");
	}

	public Object refreshAllData() {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.getAllPostGroup");
	}

	
}
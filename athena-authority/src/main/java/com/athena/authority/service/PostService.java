/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.CkDataType;
import com.athena.authority.entity.DataTable;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.Post;
import com.athena.authority.entity.PostDataItem;
import com.athena.authority.util.AuthorityUtils;
import com.athena.authority.util.PropertyLoader;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;
import com.toft.ui.tags.tree.HtmlTreeNode;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

@Component
public class PostService extends BaseService<Post>{
	
	private final Log log = LogFactory.getLog(PostService.class); 
	//注入dataTypeService
	@Inject 
	private DataTypeService dataTypeService;
	//注入dataTableService
	@Inject 
	private DataTableService dataTableService;
	
	/**
	 * 获取空间
	 */
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}

	/**
	 * @return
	 */
	public List<HtmlTreeNode> getPostDataItemTrees() {
		List<HtmlTreeNode> trees = 
			new ArrayList<HtmlTreeNode>();
		//TODO 从岗位组取数据权限类型
		trees.add(dataTypeService.getDataItemTree("warehouse"));
		trees.add(dataTypeService.getDataItemTree("supplier"));
		trees.add(dataTypeService.getDataItemTree("part"));
		
		return trees;
	}

	/**
	 * 保存用户组数据
	 * @param post
	 * @param dataItems
	 * @return
	 */
	@Transactional
	public String savePostDataItems(ArrayList<PostDataItem> dataItems,String postCode,String dataId) {
		//初始化PostDataItem
		PostDataItem dataItem = null;
		dataItem = new PostDataItem();
		//设置用户组ID
		dataItem.setPostCode(postCode);
		//设置数据权限类型ID
		dataItem.setDataId(dataId);
		//存储未删除之前的数据项的集合
		List oldListValue = new ArrayList();
		//查出未删除之前关于dataItem的数据集合
		oldListValue = this.getOldListValue(dataItem);
		//先删除
		//存储新需要保存的数据项集合
		List newListValue = new ArrayList();
		//执行删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deletePostDataItem", dataItem);
		//再保存PostDataItem
		for(PostDataItem postDataItem:dataItems){
			postDataItem.setId(getUUID());
			postDataItem.setPostCode(postCode);
			postDataItem.setDataId(dataId);
			postDataItem.setUsercenter(postDataItem.getUsercenter());
			//执行增加
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".insertPostDataItem", postDataItem);
			newListValue.add(postDataItem.getValue());
		}
		
		//TODO 修改相关业务表的关联表的数据
		DataTable table = new DataTable();
		//设置用户组ID
		table.setDataId(dataId);
		//初始化DataTable
		DataTable tableBean = new DataTable();
		tableBean = this.getDataTableByGroupId(table);
		/**
		 * 修改业务表中的数据
		 */
		String msg = "";
		try{
			this.saveDataTable(oldListValue, newListValue, tableBean,postCode);
			msg = "1";
		}catch(Exception e){
			msg = "0";
		}
		return msg;
	}
	/**
	 * 获取数据库中历史DataItem中的数据
	 * @param dataItem
	 * @return
	 */
	private List getOldListValue(PostDataItem dataItem){
		List oldListValue = new ArrayList();
		List oldList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".listPostDataItemByDataId", dataItem);;
		for(int i=0;i<oldList.size();i++){
			PostDataItem item = (PostDataItem)oldList.get(i);
			String value = item.getValue();
			oldListValue.add(value);
		}
		return oldListValue;
	}
	/**
	 * 获取初始化业务权限表中的数据
	 * @param dTable
	 * @return
	 */
	private DataTable getDataTableByGroupId(DataTable dTable){
		return (DataTable)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getDataTableByGroupId", dTable);
	}
	/**
	 * 保存业务组信息
	 * @param oldListValue
	 * @param newListValue
	 * @param tableByGroupid
	 * @param postId
	 */
	private void saveDataTable(List oldListValue,List newListValue,DataTable tableByGroupid,String postId){
		String tableName = tableByGroupid.getTableName();
		PropertyLoader pl = new PropertyLoader();
		String newParam = pl.getPropertyInfo(tableName);
		String dataField = tableByGroupid.getDataField();
		String postField = tableByGroupid.getPostField();
		String tableCl = tableByGroupid.getTableCl();
		List allList = new ArrayList();
		List addList = new ArrayList();
		List delList = new ArrayList();
		//将新旧元素加入集合allList中
		allList.addAll(oldListValue);
		allList.addAll(newListValue);
		for(int i=0;i<allList.size();i++){
			String value = (String)allList.get(i);
			if(oldListValue.contains(value)&&newListValue.contains(value)&&!addList.contains(value)){
				//新增的数据
				addList.add(value);
			}else if(!oldListValue.contains(value)&&newListValue.contains(value)&&!addList.contains(value)){
				//新增的数据
				addList.add(value);
			}else if(oldListValue.contains(value)&&!newListValue.contains(value)&&!delList.contains(value)){
				//删除的数据
				delList.add(value);
			}
		}
		if(tableCl.equals("0")){
			//更新新增加的业务表表中的信息
			for(int i=0;i<addList.size();i++){
				StringBuffer sqlParam = new StringBuffer();
				String value = (String)addList.get(i);
				String newTj = this.getNewTJ(dataField, value);
				sqlParam.append(" update "+newParam+" set "+postField+" = '" +postId+"' where 1=1  "+newTj);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updateTablePost", sqlParam.toString());
			}
			//将已删除的业务表中的数据权限字段置空
			for(int i=0;i<delList.size();i++){
				StringBuffer delSqlParam = new StringBuffer();
				String value = (String)delList.get(i);
				String newTj = this.getNewTJ(dataField, value);
				delSqlParam.append(" update "+newParam+" set "+postField+" ='' where 1=1 "+newTj);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".delTablePost", delSqlParam.toString());
			}
		}else if(tableCl.equals("1")){
			for(int i=0;i<addList.size();i++){
				StringBuffer sqlParam = new StringBuffer();
				String value = (String)addList.get(i);
				String newTj = this.getNewTJ(dataField, value);
				sqlParam.append(" update "+newParam+" set "+postField+" = CONCAT("+postField+",'," +postId+"') where 1=1  "+newTj);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updateTablePost", sqlParam.toString());
			}
			//将已删除的业务表中的数据权限字段置空
			for(int i=0;i<delList.size();i++){
				StringBuffer delSqlParam = new StringBuffer();
				String value = (String)delList.get(i);
				String newTj = this.getNewTJ(dataField, value);
				delSqlParam.append(" update "+newParam+" set "+postField+" = REPLACE("+postField+",',"+postId+"','') where 1=1  "+newTj);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".delTablePost", delSqlParam.toString());
			}
		}
		
	}
	public String getNewTJ(String dataField,String value){
		String newTj = "";
		if(dataField.indexOf(",")>0){
			String[] fields = dataField.split(",");
			String[] values = value.split(",");
			int length = fields.length;
			for(int i=0;i<length;i++){
				newTj = newTj +" and "+fields[i]+" = '"+values[i]+"'";
			}
		}else{
			newTj= " and " +dataField+" = '"+value+"'";
		}
		return newTj;
	}
	@SuppressWarnings("unchecked")
	public Map<String,List<String>> getPostDataItems(Post bean) {
		PostDataItem pDataItem = new PostDataItem();
		pDataItem.setPost(bean);
		List<PostDataItem> dataItems = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.queryPostDataItem", pDataItem);
		
		Map<String,List<String>> results = new HashMap<String,List<String>>();
		List<String> typeItems;
		String dataTypeId;
		for(PostDataItem dataItem:dataItems){
			dataTypeId = dataItem.getDataType().getId();
			typeItems = results.get(dataTypeId);
			if(typeItems==null){
				typeItems = new ArrayList<String>();
			}
			typeItems.add(dataItem.getValue());
			results.put(dataTypeId, typeItems);
			typeItems = null;
		}
		return results;
	}
	
	/**
	 * 根据业务表编码和（系统登录信息）获取查询的岗位
	 * @param tableCode
	 * @return
	 */
	public String getQueryPostCode(String tableCode) throws ServiceException{
		DataTable dataTable = 
			dataTableService.getDataTableByCode(tableCode);
		Assert.notNull(dataTable, "业务表["+tableCode+"]没有配置！");
		Assert.notNull(dataTable.getPostGroup().getId(), "dataTable.getPostGroupId() is not null!");
		String postGroupId = 
			dataTable.getPostGroup().getId();
		
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		return loginUser.getPostAndRoleMap().get(postGroupId);
	}
	private List getPostByGroupId(Post post){
		return (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getPostByGroupId", post);
	}
	/**
	 * 根据业务数据表编码和（系统登录信息）获取保存的岗位（未用）
	 * @param tableCode
	 * @return
	 */
	public String getSavePostCode(String tableCode,String value) throws ServiceException{
		//TODO
		DataTable dataTable = 
				dataTableService.getDataTableByCode(tableCode);
		Assert.notNull(dataTable, "业务表["+tableCode+"]没有配置！");
		
		String  dataId = dataTable.getDataId();
		
		PostDataItem postDataItem = new PostDataItem();
		postDataItem.setDataId(dataId);
		postDataItem.setValue(value);
		PostDataItem newItem = this.getPostDataItem(postDataItem);
		return newItem.getPostCode();
	}
	private PostDataItem getPostDataItem(PostDataItem postDataItem) {
		PostDataItem item = (PostDataItem) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getDataItem", postDataItem);
		return item;
	}

	/**
	 * 启/停用户组信息
	 * @param post
	 */
	public void changePostActive(Post post) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".changePostActive", post);
	}
	/**
	 * 权限已分配的用户组数据权限信息
	 * @param dataParamInfo
	 * @param page
	 * @param text
	 * @return
	 */
	public Map getAllPostDataItems(String dataParamInfo,PostDataItem itemBean,String text,String usercenter) {
		Map<String, Object> items = new HashMap<String, Object>();
		
		PropertyLoader pl = new PropertyLoader();
		String newParam = pl.getPropertyInfo(dataParamInfo);
		String systableName = pl.getPropertyInfo("[dbSchemal0]SYS_POST_DATA_ITEM");
		String param = "select * from ("+newParam+") param_  left outer join "+systableName+" item on param_.VALUE = item.VALUE where 1=1 AND item.ID is null and (item.POST_CODE is null or InStr(item.POST_CODE,'"+itemBean.getPostCode()+"')<0 )";
		param +=" and param_.usercenter= '"+usercenter+"'";
		if(text!=null&&!"".equals(text)){
			param += " and (param_.text like '%"+text+"%' or param_.value like '%"+text+"%')";
		}
		String dataParam = param;
		items =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectPages(getNamespace()+".getDataParam", dataParam,itemBean);
		return items;
	}
	/**
	 * 获取所有用户组信息
	 * @param post
	 * @return
	 */
	public Map listPostAll(Post post){
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".listPostAll",post);
		int size = list.size();
		Map map =  new HashMap();
		map.put("total", size);
		map.put("rows", list);
		return map;
	}
	/**
	 * 权限groupId获取用户组信息
	 * @param post
	 * @return
	 */
	public Map listPostByGroupid(Post post){
		List list = this.getPostByGroupId(post);
		int size = list.size();
		Map map =  new HashMap();
		map.put("total", size);
		map.put("rows", list);
		return map;
	}
	/**
	 * 停用用户组分类下的用户组
	 * @param postGroupId
	 */
	public void disabledPostByGroupid(String postGroupId,String biaos) {
		Post post = new Post();
		post.setPostGroupId(postGroupId);
		post.setBiaos(biaos);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".disabledPostByGroupid", post);
	}
	/**
	 * 获取所有的用户组信息,通用查询
	 * @param bean
	 * @return
	 */
	public Object listAll(Post bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".listPostAll",bean);
	}
	/**
	 * 验证唯一性
	 * @param postCode
	 * @return
	 */
	public String validateOnlyPost(String postCode) {
		Post post = new Post();
		post.setPostCode(postCode);
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".validateOnlyPost", post);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg; 
		
	}
	/**
	 * 仓库数据保存
	 * @param ckPostDataItems
	 * @param postCode
	 * @param cuncTableName
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Message savePostDataItemsCk(ArrayList<String> ckPostDataItems,
			String postCode, String cuncTableName, LoginUser loginUser) {
		String username = loginUser.getUsername();
		String createTime = DateUtil.curDateTime();
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCacheInfo("queryUserCenterMap");
		List<CacheValue> cacheValue = (List<CacheValue>) cache.getCacheValue();
		Map<String,String> cacheMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheValue){
			cacheMap.put(cvalue.getValue(),cvalue.getKey());
		}
		Message msg = null;
		if(cuncTableName!=null&&!"".equals(cuncTableName)){
			try{
				if(cuncTableName.equals("SYS_CK_GROUP")){
					Map<String,String> tjMap = new HashMap<String,String>();
					tjMap.put("postId", postCode);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.deleteSysCKGroup",tjMap);
					for(String value:ckPostDataItems){
						String[] values = value.split(":");
						String usercenter = values[2];
						String tempvalue = values[1];
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("id", this.getUUID());
						map.put("postId", postCode);
						map.put("usercenter", cacheMap.get(usercenter));
						map.put("cangkbh", tempvalue);
						map.put("creator", username);
						map.put("createTime", createTime);
						map.put("active", "1");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.insertSysCkGroup",map);
					}
				}else if(cuncTableName.equals("SYS_RQC_GROUP")){
					Map<String,String> tjMap = new HashMap<String,String>();
					tjMap.put("postId", postCode);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.deleteSysRQCGroup",tjMap);
					for(String value:ckPostDataItems){
						String[] values = value.split(":");
						String usercenter = values[2];
						String tempvalue = values[1];
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("id", this.getUUID());
						map.put("postId", postCode);
						map.put("usercenter",  cacheMap.get(usercenter));
						map.put("rqcbh", tempvalue);
						map.put("creator", username);
						map.put("createTime", createTime);
						map.put("active", "1");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.insertSysRQCGroup",map);
					}
				}else if(cuncTableName.equals("SYS_KH_GROUP")){
					Map<String,String> tjMap = new HashMap<String,String>();
					tjMap.put("postId", postCode);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.deleteSysKHGroup",tjMap);
					for(String value:ckPostDataItems){
						String[] values = value.split(":");
						String usercenter = values[2];
						String tempvalue = values[1];
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("id", this.getUUID());
						map.put("postId", postCode);
						map.put("usercenter",  cacheMap.get(usercenter));
						map.put("keh", tempvalue);
						map.put("creator", username);
						map.put("createTime", createTime);
						map.put("active", "1");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.insertSysKHGroup",map);
					}
				}else if(cuncTableName.equals("SYS_QY_GROUP")){
					Map<String,String> tjMap = new HashMap<String,String>();
					tjMap.put("postId", postCode);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.deleteSysQYGroup",tjMap);
					for(String value:ckPostDataItems){
						String[] values = value.split(":");
						String usercenter = values[2];
						String tempvalue = values[1];
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("id", this.getUUID());
						map.put("postId", postCode);
						map.put("usercenter",  cacheMap.get(usercenter));
						map.put("quybh", tempvalue);
						map.put("creator", username);
						map.put("createTime", createTime);
						map.put("active", "1");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute("authority.insertSysQYGroup",map);
					}
				}
				msg = new Message("保存成功");
			}catch(Exception e){
				msg = new Message("保存失败"+e.getMessage()+"请与管理员联系!");
			}
		}
		return msg;
	}

	public Map<String,Object> listSysDataGroup(CkDataType bean,String postCode) {
		CkDataType dt = (CkDataType)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getCkDataType", bean);
		String  cuncTableName = dt.getCuncTableName();
		List<String> newlist = new ArrayList<String>();
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCacheInfo("queryUserCenterMap");
		List<CacheValue> cacheValue = (List<CacheValue>) cache.getCacheValue();
		Map<String,String> cacheMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheValue){
			cacheMap.put(cvalue.getKey(),cvalue.getValue());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(cuncTableName!=null&&!"".equals(cuncTableName)){
			Map<String,String> tjMap = new HashMap<String,String>();
			tjMap.put("postCode", postCode);
			if(cuncTableName.equals("SYS_CK_GROUP")){
				List<Map<String,Object>> ckPostDataList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("authority.getCkGroupData",tjMap);
				for(Map<String,Object> map:ckPostDataList){
					String usercenter = (String)map.get("USERCENTER");
					String cangkbh = (String)map.get("CANGKBH");
					Map<String,String> newMap = new HashMap<String,String>();
					newMap.put("usercenter", usercenter);
					newMap.put("ucname", (String)cacheMap.get(usercenter));
					newMap.put("text", cangkbh);
					newMap.put("value", cangkbh);
					String resultJson = "";
					try {
						resultJson = JSONUtils.toJSON(newMap);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log.error("对象转化JSON数据有误");
					}
					newlist.add(resultJson);
				}
			}else if(cuncTableName.equals("SYS_RQC_GROUP")){
				List<Map<String,Object>> rqcPostDataList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.getRQCGroupData",tjMap);
				for(Map<String,Object> map:rqcPostDataList){
					String usercenter = (String)map.get("USERCENTER");
					//gswang 2012-09-27 mantis 0004307
					String rqcbh = (String)map.get("RONGQCBH");
					Map<String,String> newMap = new HashMap<String,String>();
					newMap.put("usercenter", usercenter);
					newMap.put("ucname", (String)cacheMap.get(usercenter));
					newMap.put("text", rqcbh);
					newMap.put("value", rqcbh);
					String resultJson = "";
					try {
						resultJson = JSONUtils.toJSON(newMap);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log.error("对象转化JSON数据有误");
					}
					newlist.add(resultJson);
				}
			}else if(cuncTableName.equals("SYS_KH_GROUP")){
				List<Map<String,Object>> rqcPostDataList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.getKHGroupData",tjMap);
				for(Map<String,Object> map:rqcPostDataList){
					String usercenter = (String)map.get("USERCENTER");
					String keh = (String)map.get("KEH");
					Map<String,String> newMap = new HashMap<String,String>();
					newMap.put("usercenter", usercenter);
					newMap.put("ucname", (String)cacheMap.get(usercenter));
					newMap.put("text", keh);
					newMap.put("value", keh);
					String resultJson = "";
					try {
						resultJson = JSONUtils.toJSON(newMap);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log.error("对象转化JSON数据有误");
					}
					newlist.add(resultJson);
				}
			}else if(cuncTableName.equals("SYS_QY_GROUP")){
				List<Map<String,Object>> rqcPostDataList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.getQYGroupData",tjMap);
				for(Map<String,Object> map:rqcPostDataList){
					String usercenter = (String)map.get("USERCENTER");
					String keh = (String)map.get("QUYBH");
					Map<String,String> newMap = new HashMap<String,String>();
					newMap.put("usercenter", usercenter);
					newMap.put("ucname", (String)cacheMap.get(usercenter));
					newMap.put("text", keh);
					newMap.put("value", keh);
					String resultJson = "";
					try {
						resultJson = JSONUtils.toJSON(newMap);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log.error("对象转化JSON数据有误");
					}
					newlist.add(resultJson);
				}
			}
		}
		resultMap.put("rows", newlist);
		return resultMap;
	}
	/**
	 * 更新用户组信息
	 * @param bean
	 * @return
	 */
	public int update(Post bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updatePost", bean);
	}
	/**
	 * 验证用户组是否能删除条件：
	 * 查看该用户组是否已经分配
	 * @param map
	 * @return
	 */
	public Integer delPost(Map<String, String> map) {
		String postCode = map.get("postCode");
		int result = 0;
		if(postCode!=null&&!"".equals(postCode)){
			Integer postCounts = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".countUser", map);
			if(postCounts==0){
				result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deletePost", map);
			}else{
				result = -1;
			}
		}else{
			log.error("postCode is null");
		}
		
		return result;
	}
	/**
	 * 授权用户中心保存
	 * @param postCode
	 * @param ucIds
	 * @return
	 */
	public Message saveAuthUsercenter(String postCode, List<String> ucIds) {
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("postCode", postCode);
		if(postCode!=null&&!"".equals(postCode)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteRoleuc",tjMap);
			for(String uc:ucIds){
				Map<String,String> map = new HashMap<String,String>();
				map.put("postCode", postCode);
				map.put("usercenter", uc);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".saveAuthUsercenter",map);
			}
		}else{
			log.error("postCode is null");
		}
		
		return new Message("保存成功");
	}

	public List<Map<String,String>> getRoleuc(String postCode) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("postCode", postCode);
		List<Map<String,String>> mapRoleUc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getRoleuc",map);
		return mapRoleUc;
	}

	public List getRoleucByPostCode(String id) {
		Map map = new HashMap();
		map.put("userId", id);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getRoleucByPostCode",map);
	}

	public List getAllUsercenter() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getAllUsercenter");
	}

	public Object refreshAllData() {
		/**
		 * 刷新缓存queryDicCache
		 * @return
		 */
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.getJihz");
	}
	
	/**
	 * 验证用户组是否能删除条件：
	 * 查看该用户组是否已经分配
	 * @param map
	 * @return
	 */
	public Integer checkPostUser(Post post) {
		String postCode = post.getPostCode();
		Map<String,String> map = new HashMap<String,String>();
		map.put("postCode", postCode);
		int result = 0;
		if(postCode!=null&&!"".equals(postCode)){
			result = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".countUser", map);
		}else{
			log.error("postCode is null");
		}
		
		return result;
	}

	/**
	 * 验证用户组下是否有用户：
	 * 查看该用户组下是否有用户
	 * @param bean
	 * @return boolean
	 */
	public boolean getPostByPostCode(Post bean) {
		boolean r = false;
		Post post= (Post)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getPost",bean);
		if(!post.getDicCode().equals(bean.getDicCode())){
			int result = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".validatePostNotUser", bean);
			if(result == 0){
				r = true;
			}
		}else{
			r = true;
		}
		return r;
	}
}
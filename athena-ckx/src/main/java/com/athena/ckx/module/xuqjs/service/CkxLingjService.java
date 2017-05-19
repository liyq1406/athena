package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Post;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;

/**
 * 零件
 * @author denggq
 * @date 2012-2-7
 */
@Component
public class CkxLingjService extends BaseService<CkxLingj> {

	@Inject
	private CkxShiwtxService ckxshiwtxService;
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	@SuppressWarnings("rawtypes")
	public List listAllLingj(CkxLingj bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryAllLingj",bean);
	}
	/**
	 * 分页查询（左连接车间，通用零件分页查询专用）
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectPages(CkxLingj bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryCkxLingjBychej",bean,bean);
	}
	
	/**
	 * 保存零件方法
	 * @param bean,operant
	 * @return String
	 * @author denggq
	 * @date 2012-3-23
	 */
	@Transactional
	public String save(CkxLingj bean, Integer operant, String userId) throws ServiceException {
		
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		Assert.notNull(bean.getKaisrq(), GetMessageByKey.getMessage("kaishirq"));
		Assert.notNull(bean.getJiesrq(), GetMessageByKey.getMessage("jieshurq"));
		
		Assert.isTrue(DateTimeUtil.compare(bean.getKaisrq(),bean.getJiesrq()),GetMessageByKey.getMessage("kaishixyjieshu"));
		
		if(null!=bean.getZhongwmc()&&!"".equals(bean.getZhongwmc())&&bean.getZhongwmc().indexOf(",")!=-1){
			bean.setZhongwmc(bean.getZhongwmc().replaceAll(",", "，"));
		}
//		if(null!=bean.getZhongwmc()&&!"".equals(bean.getZhongwmc())&&bean.getZhongwmc().indexOf("'")>0){
//			bean.setZhongwmc(bean.getZhongwmc().replaceAll("'", " "));
//		}
		
		//开始日期未未来日期 或者结束日期已过 则失效状态
//		String currentDate = DateTimeUtil.getCurrDate();
//		if(DateTimeUtil.compare(currentDate, bean.getKaisrq()) || DateTimeUtil.compare(bean.getJiesrq(), currentDate)){
//			bean.setBiaos("0");
//		}else{
//			bean.setBiaos("1");
//		}
		if(1 == operant){//增加
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingj", bean);
		}else{//修改
			CkxLingj l = new CkxLingj();
			l.setUsercenter(bean.getUsercenter());
			l.setLingjbh(bean.getLingjbh());
			Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingj", l);
			CkxLingj ckxlj = (CkxLingj)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingj", l);
			if(null == obj){
				throw new ServiceException(GetMessageByKey.getMessage("yonghuzhongxin")+bean.getUsercenter()+GetMessageByKey.getMessage("notxialj")+bean.getLingjbh());
			}
			if("ZHIJIA".equals(bean.getRole())){
				Map map = getLoginUser().getPostAndRoleMap();
				String key = GetPostOnly.getPostOnly(map);
				String value = (String) map.get(key);
				if(null!=bean.getZhijy()&&!" ".equals(bean.getZhijy())&&!bean.getZhijy().equals(value)){
					throw new ServiceException("用户不能修改质检组不属于自己的岗位编号");
				}else{
					if(null!=ckxlj.getZhijy()&&!" ".equals(ckxlj.getZhijy())&&!ckxlj.getZhijy().equals(value)){
						throw new ServiceException("用户不能修改质检组不属于自己的岗位编号");
					}
				}
			}
			if("ZBCPOA".equals(bean.getRole()) || "JIHUAY".equals(bean.getRole())){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjByZbcpoa", bean);
			}else if("ZXCPOA".equals(bean.getRole())){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjByZxcpoa", bean);
			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingj", bean);
			}
			ckxshiwtxService.update(bean.getUsercenter(), CkxShiwtxService.TIXLX_LINGJ, bean.getLingjbh(), null, "1");
		}
		
		return "success";
	}
	
	
	/**
	 * 失效零件方法
	 * @param bean,operant
	 * @return String
	 * @author denggq
	 * @date 2012-3-23
	 */
	@Transactional
	public String doDelete(CkxLingj bean) throws ServiceException {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxLingj",bean);
		return "success";
	}

	/**
	 * 获得多个零件
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(CkxLingj bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingj",bean);
	}
	@SuppressWarnings("unchecked")
	public List<CkxLingj> listImport(CkxLingj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjImport",bean);
	}
	/**
	 * 数据权限
	 * @author denggq
	 * @date 2012-8-2
	 * @param 组代码 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public List<Map<String,String>> getZudmByAthority(String role,String usercenter) throws ServiceException  {
		CkxLingj bean =new CkxLingj();
		bean.setUsercenter(usercenter);//当前用户中心
		bean.setRole(role);//当前角色
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZudmByAthority",bean);;
		if(0 == list.size()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("KEY", "");
			map.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 数据权限
	 * @author chenpeng
	 * @date 2015-3-3
	 * @param 组代码 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public List<Map<String,String>> getZJYQX(String role,String usercenter,String zhijy) throws ServiceException  {
		CkxLingj bean =new CkxLingj();
		bean.setUsercenter(usercenter);//当前用户中心
		bean.setRole(role);//当前角色
		bean.setZhijy(zhijy);//质检员
		
		Map<String,String> maps = getLoginUser().getPostAndRoleMap();
		List list = new ArrayList<Map>();
		if((null == maps.get("ZXCPOA")||"".equals((String)maps.get("ZXCPOA")))&&(zhijy!=null && zhijy.length()>0)){
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZJZQX",bean);
		}else{
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZudmByAthority",bean);
		}
		if(0 == list.size()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("KEY", "");
			map.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String,String>> edit(CkxLingj beans){
		
		CkxLingj bean = new CkxLingj();
		bean.setUsercenter(beans.getUsercenter());
		bean.setLingjbh(beans.getLingjbh());
		@SuppressWarnings("rawtypes")
		List zhijz = new ArrayList();
		@SuppressWarnings("rawtypes")
		List result = new ArrayList<Map>();
		String role = "ZHIJIA";
		String z = "";
		z = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjXX",bean);
		
		if(StringUtils.isNotBlank(z))
		{
			bean.setZhijy(z);
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZJZQXNew",bean);
		}
		else
		{
			Map<String,String> mapFlag = getLoginUser().getPostAndRoleMap();
			z = mapFlag.get(role);
			bean.setZhijy(z);
			bean.setRole(role);
			if((null == mapFlag.get("ZXCPOA")||"".equals((String)mapFlag.get("ZXCPOA")))&&(z!=null && z.length()>0)){
				result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZJZQXNew",bean);
			}else{
				result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getZudmByAthority",bean);
			}
		}
		return result;
	}
	
	/**
	 * 数据权限
	 * @author hj
	 * @param 组代码 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public List<Map<String,String>> queryJihyByLingj(String usercenter) throws ServiceException  {
		CkxLingj bean =new CkxLingj();
		bean.setUsercenter(usercenter);//当前用户中心
		bean.setBiaos("1");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryJihyByLingj",bean);;
		if(0 == list.size()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("KEY", "");
			map.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add(map);
		}
		return list;
	}
	
	/**校验计划员的存在性
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getJihyMap(String usercenter){
		Post post = new Post();
		post.setDicCode("JIHUAY");
		post.setBiaos("1");
		post.setUsercenter(usercenter);
		List<Post>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCountPost",post);
		Map<String,String> map=new HashMap<String,String>();
		for (Post pt : list) {
			map.put(pt.getPostCode(), pt.getPostCode());
		}
		return map;
	}
	
	/**校验用户中心的存在性
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
//	@SuppressWarnings("unchecked")
//	public Map<String,String> getUsercenterMap(){
//		CkxLingj usercenter = new Usercenter();
//		usercenter.setBiaos("1");
//		List<Usercenter>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryUsercenter",usercenter);
//		Map<String,String> map=new HashMap<String,String>();
//		for (Usercenter us : list) {
//			map.put(us.getUsercenter(), us.getUsercenter());
//		}
//		return map;
//	}
	
}

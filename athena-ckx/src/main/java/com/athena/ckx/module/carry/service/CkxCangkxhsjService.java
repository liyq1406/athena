package com.athena.ckx.module.carry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.carry.CkxCangkxhsj;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;


/**
 * 仓库循环时间
 * @author kong
 * 2012-02-14
 */
@Component
public class CkxCangkxhsjService extends BaseService<CkxCangkxhsj>{
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "carry";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-18
	 */
	public Map<String, Object> select(CkxCangkxhsj bean,LoginUser user) throws ServiceException {
		Map<String,String> map = user.getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			if(null != bean.getMos() && ("RD".endsWith(bean.getMos())||"SY".endsWith(bean.getMos()))){
				bean.setCreator("fpq");
//				Fenpq fenpq = new Fenpq();
//				fenpq.setUsercenter(bean.getUsercenter());
//				fenpq.setFenpqh(bean.getFenpqhck());
//				fenpq.setWulgyyz(value);
//				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fenpq);
//				if(0 == list.size()){
//					//无分配区数据权限
//					throw new ServiceException(GetMessageByKey.getMessage("wfpqsjqx"));
//				}
			}else if(null != bean.getMos() && ("RM".endsWith(bean.getMos())||"M1".equals(bean.getMos()))){
				bean.setCreator("ck");
//				Cangk cangk =new Cangk();
//				cangk.setUsercenter(bean.getUsercenter());
//				cangk.setWulgyyz(value);
//				cangk.setCangkbh(bean.getFenpqhck()==null?null:bean.getFenpqhck().substring(0, 3));
//				List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk",cangk);
//				if(0 == list.size()){
//					//"无仓库数据权限"
//					throw new ServiceException(GetMessageByKey.getMessage("wcksjqx"));
//				}
			}
			bean.setWulgyyz(value);
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("carry.queryCkxCangkxhsjByWlgyy",bean,bean);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("carry.queryCkxCangkxhsj",bean,bean);
	}
	
	/**
	 * 添加仓库循环时间
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String addCkxCangkxhsj(CkxCangkxhsj bean,LoginUser user){
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String postCode = "";
		if(map.get(key)!=null ){
			postCode = (String)map.get(key);
		}
		bean.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
		bean.setCreator(user.getUsername());//创建人
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间 
//		try {
			
			/*---------   仓库验证     -----------*/
			//验证仓库有效性
//			Map<String,String> map1 = new HashMap<String,String>(),map2 = new HashMap<String,String>();
//			String mes1 = "子仓库表中不存在当前用户中心下仓库号为："+bean.getCangkbh().substring(0,3)+
//						  "且子仓库号为："+bean.getCangkbh().substring(3,6)+"的数据或该数据已失效";
//			
//			map1.put("tableName", "ckx_zick");
//			map1.put("cangkbh", bean.getCangkbh().substring(0, 3));
//			map1.put("zickbh", bean.getCangkbh().substring(3, 6));
//			map1.put("usercenter", bean.getUsercenter());
//			map1.put("biaos", "1");	
//			DBUtilRemove.checkYN(map1, mes1);
//			map1.clear();
			Zick zick=new Zick();
			zick.setCangkbh(bean.getCangkbh().substring(0, 3));
			zick.setZickbh(bean.getCangkbh().substring(3, 6));
			zick.setUsercenter(bean.getUsercenter());
			zick.setBiaos("1");
			//物流工艺员："+postCode+",不存在此用户中心下的仓库编号或仓库编号已失效
			String mes = GetMessageByKey.getMessage("wlgyybczcckbh",new String[]{postCode});
			String mes1 =GetMessageByKey.getMessage("zckzbczckhzckh",new String[]{bean.getCangkbh().substring(0,3),
					bean.getCangkbh().substring(3,6)});
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountZick", zick)){
				throw new ServiceException(mes1);
			}
			//仓库权限验证//以物流工艺员身份验证仓库
			Cangk cangk =new Cangk();
			cangk.setUsercenter(bean.getUsercenter());
			cangk.setCangkbh(bean.getCangkbh().substring(0, 3));
			cangk.setBiaos("1");
//			if(null!=postCode&&!"".equals(postCode)&&0 > key.indexOf("ZBCPOA")){
//				cangk.setWulgyyz(postCode);
//			}
			
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCangk", cangk)){
				throw new ServiceException("不存在此用户中心下的仓库编号或仓库编号已失效");
			}
			
			
			
			
			
			/*---------   仓库或分配区验证     -----------*/
			Object cond=null;
			String sqlId="", mes2 = "";
			//如果物流模式为rd或者md，则验证分配区是否有效
			if("RD".equals(bean.getMos())||"SY".equals(bean.getMos())||("MD".equals(bean.getMos())&& 5 == bean.getFenpqhck().length())){
				if("SY".equals(bean.getMos())){
					if(!StringUtils.isNotEmpty(bean.getBeihsj()==null?"":bean.getBeihsj().toString())){
						throw new ServiceException("请填写备货时间");
					}
				}
				Fenpq fenpq=new Fenpq();
				fenpq.setUsercenter(bean.getUsercenter());
				fenpq.setFenpqh(bean.getFenpqhck());
				fenpq.setBiaos("1");
//				map2.put("tableName", "ckx_fenpq");				
//				map2.put("fenpqh", bean.getFenpqhck());				
				if(!"".equals(postCode)&&0 > key.indexOf("ZBCPOA")){
					fenpq.setWulgyyz(postCode);
//					map2.put("wulgyyz", postCode);
				}
				cond=fenpq;
				sqlId="ts_ckx.getCountFenpq";
				//"分配区表中不存在当前用户中心下分配区号为："+bean.getFenpqhck()+"的数据或该数据已失效或没有该权限";
				mes2=GetMessageByKey.getMessage("fpqbzbczfbpbh",new String[]{bean.getFenpqhck()});
			}else {//物流模式为rm或者m1,则验证仓库是否有效
				if(6 != bean.getFenpqhck().length()){
					//"若模式为：RM,M1则循环仓库只能为仓库子仓库号"
					throw new ServiceException(GetMessageByKey.getMessage("rmswrmm1zxhckznwckzck"));
				}
				zick.setCangkbh(bean.getFenpqhck().substring(0, 3));
				zick.setZickbh(bean.getFenpqhck().substring(3, 6));
				zick.setUsercenter(bean.getUsercenter());
				zick.setBiaos("1");
				cond=zick;
				sqlId="ts_ckx.getCountZick";
				mes2 = GetMessageByKey.getMessage("zckzbczckhzckh",new String[]{bean.getFenpqhck().substring(0,3),
						bean.getFenpqhck().substring(3,6)});
//				map2.put("tableName", "ckx_zick");
//				map2.put("cangkbh", bean.getFenpqhck().substring(0, 3));
//				map2.put("zickbh", bean.getFenpqhck().substring(3, 6));
				
				
				
//				String sql = "select count(*) from ckx_cangk where usercenter='"
//					+bean.getUsercenter()+"' and cangkbh='"+bean.getFenpqhck().substring(0, 3)+
//					"' and biaos='1'";
//				if(null!=postCode&&!"".equals(postCode)&&0 > key.indexOf("ZBCPOA")){
//					sql+=" and wulgyyz='"+postCode+"'";
//				}
//				
//				DBUtilRemove.checkBH(sql, mes);
			}
			if(!DBUtil.checkCount(baseDao,sqlId, cond)){
				throw new ServiceException(mes2);
			}
			
			//仓库权限验证//以物流工艺员身份验证仓库
			if(bean.getFenpqhck().length()==6){
				cangk.setCangkbh(bean.getFenpqhck().substring(0, 3));
				if(!"".equals(postCode)&&0 > key.indexOf("ZBCPOA")){
					cangk.setWulgyyz(postCode);
				}
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCangk", cangk)){
					throw new ServiceException(mes);
				}
			}
			
			
			
//			map2.put("usercenter", bean.getUsercenter());
//			map2.put("biaos", "1");
//			
//			
//			DBUtilRemove.checkYN(map2, mes2);
//			map2.clear();
			
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxCangkxhsj", bean);//添加
			return GetMessageByKey.getMessage("addsuccess");
//		} catch (DataAccessException e) {//数据冲突异常
//			throw new ServiceException(GetMessageByKey.getMessage("addfail")+e.getMessage());
//		}
	}
	
	/**
	 * 修改仓库循环时间
	 * @param bean
	 * @param user
	 * @return
	 */
	public String save(CkxCangkxhsj bean,LoginUser user){
		if("SY".equals(bean.getMos())){
			if(!StringUtils.isNotEmpty(bean.getBeihsj()==null?"":bean.getBeihsj().toString())){
				throw new ServiceException("请填写备货时间");
			}
		}
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间 
//		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxCangkxhsj", bean);//修改
			return GetMessageByKey.getMessage("savesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("savefail")+e.getMessage());
//		}
	}
	/**
	 * 删除仓库循环时间
	 * @param bean
	 * @param user
	 * @return
	 */
	public String deleteLogic(CkxCangkxhsj bean,LoginUser user){
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间 
//		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteLogicCkxCangkxhsj", bean);//删除
			return GetMessageByKey.getMessage("deletesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("deletefail")+e.getMessage());
//		}
	}
	
	/**
	 * 查询带子仓库的仓库编号和分配区编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CkxCangkxhsj> listCangkXunh(CkxCangkxhsj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.listCangkXunh",bean);
	}
	/**
	 * 查询带子仓库的仓库编号
	 * @param bean
	 * @return 6位仓库编码（带子仓库编号）
	 */
	@SuppressWarnings("unchecked")
	public List<CkxCangkxhsj> listCangk(CkxCangkxhsj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.listCangk",bean);
	}
	
	/**
	 * 根据用户中心+仓库编号
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getCangkMap(){
		Cangk cangk = new Cangk();
		cangk.setBiaos("1");
		List<Cangk>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk",cangk);
		Map<String,String> map=new HashMap<String,String>();
		for (Cangk ck : list) {
			map.put(ck.getUsercenter()+ck.getCangkbh(), ck.getUsercenter()+ck.getCangkbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+仓库编号+子仓库编号
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getCkzickMap(){
		Zick zick=new Zick();
		zick.setBiaos("1");
		List<Zick>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryZick",zick);
		Map<String,String> map=new HashMap<String,String>();
		for (Zick zck : list) {
			map.put(zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh(), zck.getUsercenter()+zck.getCangkbh()+zck.getZickbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+分配区
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getfenpqMap(){
		Fenpq fep = new Fenpq();
		fep.setBiaos("1");
		List<Fenpq>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fep);
		Map<String,String> map=new HashMap<String,String>();
		for (Fenpq fq : list) {
			map.put(fq.getUsercenter()+fq.getFenpqh(), fq.getUsercenter()+fq.getFenpqh());
		}
		return map;
	}
	
}
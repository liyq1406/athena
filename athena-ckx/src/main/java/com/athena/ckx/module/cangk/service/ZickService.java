package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Fahzt;
import com.athena.ckx.entity.cangk.Xiehzt;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.module.xuqjs.service.LingjckService;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 子仓库
 * @author denggq
 * @date 2012-1-12
 */
@Component
public class ZickService extends BaseService<Zick> {
	
	@Inject
	private LingjckService lingjckService;
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-18
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Zick bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryZick",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Zick> insert,
	           ArrayList<Zick> edit,
	   		   ArrayList<Zick> delete,LoginUser user,Cangk cangk) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,user,cangk);
		edits(edit,user,cangk);
		deletes(delete,user,cangk);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,userID
	 * @return  ""
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String inserts(List<Zick> insert,LoginUser user,Cangk cangk)throws ServiceException{
		Map map = user.getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		
		
		for(Zick bean:insert){
			
			//mantis    5455
			if("ZXCPOA".equals(key)){
				if(null==bean.getBaohd()&&("B".equals(bean.getZickbh().substring(2,3))||"S".equals(bean.getZickbh().substring(2,3)))){
					throw new ServiceException("饱和度不能为空");
				}
				if(null==bean.getShifelgl()&&("B".equals(bean.getZickbh().substring(2,3))||"S".equals(bean.getZickbh().substring(2,3)))){
					throw new ServiceException("是否按EL号管理不能为空");
				}
				// mantis编号 0005243 子仓库新增时，P类型的不用输入卸货站台。
				if(!"P".equals(bean.getZickbh().substring(2,3))&& null==bean.getZhantbh()){
					throw new ServiceException("站台编号不能为空");
				}
			}
			
			if( null != bean.getZhantbh()){
				
				//一个站台编号只能对应一个大仓库
				Zick zk = new Zick();
				zk.setUsercenter(cangk.getUsercenter());
				zk.setZhantbh(bean.getZhantbh());
				String cangkbh =(String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountCangkbh", zk);
//				String sql0 = "select distinct cangkbh from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+cangk.getUsercenter()+"' and zhantbh = '"+bean.getZhantbh()+"'";
//				Connection conn = DbUtils.getConnection(ConstantDbCode.DATASOURCE_CKX);
//				String cangkbh =  (String) DbUtils.selectValue(sql0,conn );
				if("1".equals(cangk.getZhantlx())){//发货站台
					//发货站台编号是否存在
					Fahzt ft = new Fahzt();
					ft.setUsercenter(cangk.getUsercenter());
					ft.setFahztbh(bean.getZhantbh());
					ft.setBiaos("1");
					String mes = GetMessageByKey.getMessage("fahuoztbh")+bean.getZhantbh()+GetMessageByKey.getMessage("notexist");
					if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountFahzt", ft)){
						throw new ServiceException(mes); 
					}
//					String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_fahzt where usercenter = '"+cangk.getUsercenter()+"' and fahztbh = '"+bean.getZhantbh()+"' and biaos = '1'";
//					DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("fahuoztbh")+bean.getZhantbh()+GetMessageByKey.getMessage("notexist"));
					
					//一个发货站台编号只能对应一个仓库(非子仓库)
					if(null != cangkbh && !cangk.getCangkbh().equals(cangkbh)){
						throw new ServiceException(GetMessageByKey.getMessage("fahuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("yibeick")+"["+cangkbh+"]"+GetMessageByKey.getMessage("zhanyong"));
					}
					
				}else{//卸货站台
					//卸货站台编号是否存在
					if(null != cangk.getXiehztbz() && !"".equals(cangk.getXiehztbz())&&null != bean.getZhantbh() && !"".equals(bean.getZhantbh())){
						Xiehzt xht = new Xiehzt();
						xht.setUsercenter(cangk.getUsercenter());
						xht.setXiehztbzh(cangk.getXiehztbz());
						xht.setXiehztbh(bean.getZhantbh());
						xht.setBiaos("1");
						String mse =  GetMessageByKey.getMessage("xiehztbzbhbczhysx",new String[]{cangk.getXiehztbz(),bean.getZhantbh()});
						if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountXiehztbzhs", xht)){
							throw new ServiceException(mse); 
						}
//						String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiehzt where usercenter = '"+cangk.getUsercenter()+"' and xiehztbzh='"+cangk.getXiehztbz()+"' and xiehztbh = '"+bean.getZhantbh()+"' and biaos = '1'";
//						DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiehztbzbhbczhysx",new String[]{cangk.getXiehztbz(),bean.getZhantbh()}));
					}
					if(null != bean.getZhantbh() && !"".equals(bean.getZhantbh())){
						Xiehzt xt = new Xiehzt();
						xt.setUsercenter(cangk.getUsercenter());
						xt.setXiehztbh(bean.getZhantbh());
						xt.setBiaos("1");
						String mes1 = GetMessageByKey.getMessage("xiehuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("notexist");
						if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountXiehzt", xt)){
							throw new ServiceException(mes1); 
						}
//						String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiehzt where usercenter = '"+cangk.getUsercenter()+"' and xiehztbh = '"+bean.getZhantbh()+"' and biaos = '1'";
//						DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiehuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("notexist"));
					}
					
					//一个卸货站台编号只能对应一个仓库(非子仓库)
					if(null != cangkbh && !cangk.getCangkbh().equals(cangkbh)){
						throw new ServiceException(GetMessageByKey.getMessage("xiehuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("yibeick")+"["+cangkbh+"]"+GetMessageByKey.getMessage("zhanyong"));
					}
					
				}
			}
			bean.setUsercenter(cangk.getUsercenter());
			bean.setCangkbh(cangk.getCangkbh());
			bean.setCreator(user.getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(user.getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertZick",bean);
			
			updateZhant(cangk.getUsercenter(),cangk.getCangkbh(),cangk.getZhantlx(),bean.getZhantbh(),key);
			
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param edit,userID
	 * @return ""
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String edits(List<Zick> edit,LoginUser user,Cangk cangk) throws ServiceException{
		Map map = user.getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		
		String yuanZhantbh = "";
		for(Zick bean:edit){
//			if("0".equals(cangk.getZhantlx())&&null==bean.getZhantbh()){
//				throw new ServiceException("站台编号不能为空");
//			}
			
			if("ZXCPOA".equals(key)){
			
				if(null==bean.getBaohd()&&("B".equals(bean.getZickbh().substring(2,3))||"S".equals(bean.getZickbh().substring(2,3)))){
					throw new ServiceException("饱和度不能为空");
				}
				
				if(null==bean.getShifelgl()&&("B".equals(bean.getZickbh().substring(2,3)))){
					throw new ServiceException("是否按EL号管理不能为空");
				}
				// mantis编号 0005243 子仓库新增时，P类型的不用输入卸货站台。
				if(!"P".equals(bean.getZickbh().subSequence(2,3))&& null==bean.getZhantbh()){
					throw new ServiceException("站台编号不能为空");
				}
			}
			if( null != bean.getZhantbh()){
				//一个站台编号只能对应一个大仓库
				Zick zk = new Zick();
				zk.setUsercenter(cangk.getUsercenter());
				zk.setZhantbh(bean.getZhantbh());
				String cangkbh =(String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountCangkbh", zk);
//				String sql0 = "select distinct cangkbh from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+cangk.getUsercenter()+"' and zhantbh = '"+bean.getZhantbh()+"'";
//				Connection conn = DbUtils.getConnection(ConstantDbCode.DATASOURCE_CKX);
//				String cangkbh =  (String) DbUtils.selectValue(sql0,conn );
				
				if("1".equals(cangk.getZhantlx())){//发货站台
					//发货站台编号是否存在
					Fahzt ft = new Fahzt();
					ft.setUsercenter(cangk.getUsercenter());
					ft.setFahztbh(bean.getZhantbh());
					ft.setBiaos("1");
					String mes = GetMessageByKey.getMessage("fahuoztbh")+bean.getZhantbh()+GetMessageByKey.getMessage("notexist");
					if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountFahzt", ft)){
						throw new ServiceException(mes); 
					}
//					String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_fahzt where usercenter = '"+cangk.getUsercenter()+"' and fahztbh = '"+bean.getZhantbh()+"' and biaos = '1'";
//					DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("fahuoztbh")+bean.getZhantbh()+GetMessageByKey.getMessage("notexist"));
					
					//一个发货站台编号只能对应一个仓库(非子仓库)
					if(null != cangkbh && !cangk.getCangkbh().equals(cangkbh)){
						throw new ServiceException( GetMessageByKey.getMessage("fahuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("yibeick")+"["+cangkbh+"]"+GetMessageByKey.getMessage("zhanyong"));
					}
				}else{//卸货站台
					yuanZhantbh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiehztbh",bean);
					//卸货站台编号是否存在
					Xiehzt xht = new Xiehzt();
					xht.setUsercenter(cangk.getUsercenter());
					xht.setXiehztbzh(cangk.getXiehztbz());
					xht.setXiehztbh(bean.getZhantbh());
					xht.setBiaos("1");
					String mse =  GetMessageByKey.getMessage("xiehztbzbhbczhysx",new String[]{cangk.getXiehztbz(),bean.getZhantbh()});
					if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountXiehztbzhs", xht)){
						throw new ServiceException(mse); 
					}
//					String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiehzt where usercenter = '"+cangk.getUsercenter()+"' and xiehztbzh='"+cangk.getXiehztbz()+"' and  xiehztbh = '"+bean.getZhantbh()+"' and biaos = '1'";
//					DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiehztbzbhbczhysx",new String[]{cangk.getXiehztbz(),bean.getZhantbh()}));
//					DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiehuoztbh")+bean.getZhantbh()+GetMessageByKey.getMessage("notexist"));
				
					//一个卸货站台编号只能对应一个仓库(非子仓库)
					if(null != cangkbh && !cangk.getCangkbh().equals(cangkbh)){
						throw new ServiceException( GetMessageByKey.getMessage("xiehuoztbh")+"["+bean.getZhantbh()+"]"+GetMessageByKey.getMessage("yibeick")+"["+cangkbh+"]"+GetMessageByKey.getMessage("zhanyong"));
					}
				}
			}
			
			bean.setUsercenter(cangk.getUsercenter());
			bean.setCangkbh(cangk.getCangkbh());
			bean.setEditor(user.getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateZick",bean);
			
			updateZhant(cangk.getUsercenter(),cangk.getCangkbh(),cangk.getZhantlx(),bean.getZhantbh(),key);
			yuanZhantbh = StringUtils.defaultIfEmpty(yuanZhantbh,  "");
			
			lingjckService.checkXiehztbhChange(bean.getUsercenter(), yuanZhantbh, bean.getZhantbh(), bean.getCangkbh(), bean.getZickbh(), null, user.getUsername());
			
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Zick> delete,LoginUser user,Cangk cangk)throws ServiceException{
		for(Zick bean:delete){
			bean.setUsercenter(cangk.getUsercenter());
			bean.setCangkbh(cangk.getCangkbh());
			bean.setEditor(user.getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteZick",bean);
		}
		return "";
	}
	
	
	public void updateZhant(String usercenter,String cangkbh,String zhantlx,String zhantbh,String key) throws ServiceException{
		if( null == zhantbh){
			zhantbh="";
		}
		if("1".equals(zhantlx)){//发货站台
			if("ZXCPOA".equals(key)){
				Fahzt fahzt = new Fahzt();
				fahzt.setUsercenter(usercenter);
				fahzt.setFahztbh(zhantbh);
				fahzt.setCangkbh(cangkbh);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangkbhOfFahzt", fahzt);
			}
		}else if("0".equals(zhantlx)){//卸货站台
			Xiehzt xiehzt = new Xiehzt();
			xiehzt.setUsercenter(usercenter);
			xiehzt.setXiehztbh(zhantbh);
			xiehzt.setCangkbh(cangkbh);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangkbhOfXiehzt", xiehzt);
		}
	}
	
}

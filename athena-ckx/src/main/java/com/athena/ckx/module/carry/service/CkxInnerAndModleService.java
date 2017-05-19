package com.athena.ckx.module.carry.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxInnerPathAndModle;
import com.athena.ckx.entity.carry.CkxShengcxXianb;
import com.athena.ckx.entity.carry.CkxWaibms;
import com.athena.ckx.entity.carry.CkxXianbDingh;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;

import com.toft.core3.transaction.annotation.Transactional;



/**
 * 内部物流路径逻辑业务类
 * @author kong
 * 2012-02-14
 */
@Component
public class CkxInnerAndModleService extends BaseService<CkxInnerPathAndModle>{
	@Override
	protected String getNamespace() {
		return "carry";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-18
	 * @update lc 2016.10.31
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> select(Map<String, String> params, CkxInnerPathAndModle bean) throws ServiceException {
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		if(!"WULGYY".equals(key) && !"ZBCPOA".equals(key) && !"root".equals(key)){
			throw new ServiceException(GetMessageByKey.getMessage("wcxdcsjqx"));
		}else if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			Fenpq fenpq = new Fenpq();
			fenpq.setUsercenter(bean.getUsercenter());
			fenpq.setFenpqh(bean.getFenpqh());
			fenpq.setWulgyyz(value);
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fenpq);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("wfpqsjqx"));
			}else{
				bean.setWulgyyz(value);
			}
		}
		Map<String, Object> message = new HashMap<String, Object>();
		try{
			if("exportXls".equals(params.get("exportXls"))){
				List<Object> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxInnerPathAndModleExport",bean);
				message.put("total", list.size());
				message.put("rows", list);
				if(list.size() > 5000){
					list.clear();
					message.put("total", list.size());
					message.put("rows", list);
				}
			}else{
				message = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("carry.queryCkxInnerPathAndModle",bean,bean);
			}
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return message;
	}
	
	/**
	 * 添加内部物流路径，外部模式
	 * @param bean
	 * @param list  零件消耗点关系集合
	 * @return
	 */
	@Transactional
	public String addCkxInnerPath(CkxInnerPathAndModle bean,List<CkxLingjxhd> list,LoginUser user){
		String lingjian="";
		
		for (CkxLingjxhd c : list) {
			//定义外部模式的目的地,可能是线边库，或者是订货库
			//String mudd="";
			lingjian=c.getLingjbh();
			//验证零件-供应商关系是否存在
			if(null != bean.getZhidgys()&& !"".equals(bean.getZhidgys()) ){
				Lingjgys lingjgys = new Lingjgys();
				lingjgys.setBiaos("1");
				lingjgys.setLingjbh(lingjian);
				lingjgys.setUsercenter(c.getUsercenter());
				lingjgys.setGongysbh(bean.getZhidgys());
				Object lingjobj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjgys", lingjgys);
				if (lingjobj == null) {
					//return "零件["+lingjian+"]的循环起点仓库不存在";
					//"零件:"+lingjian+",供应商："+bean.getZhidgys()+" 零件供应商不存在此对应关系！"
					throw new ServiceException(GetMessageByKey.getMessage("ljgysbczcgx",new String[]{
							lingjian,bean.getZhidgys()
					}));
				} 
			}
			
			//检查零件-仓库（循环起点）关系是否存在
			Lingjck lingjck=new Lingjck();
			lingjck.setUsercenter(c.getUsercenter());
			lingjck.setLingjbh(c.getLingjbh());
			lingjck.setCangkbh(bean.getXianbk());//仓库是循环的起点（线边库）
			Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", lingjck);
			if (obj == null) {
				//return "零件["+lingjian+"]的循环起点仓库不存在";
				//"零件["+lingjian+"]的循环起点仓库不存在"
				throw new ServiceException(GetMessageByKey.getMessage("ljdxhqdckbcz",new String[]{lingjian}));
			} else {
				lingjck=(Lingjck) obj;
			}
//			try {
				//产线-线边库
				CkxShengcxXianb inner1 =new CkxShengcxXianb();
				inner1.setUsercenter(c.getUsercenter());//用户中心
				inner1.setLingjbh(c.getLingjbh());//零件编号
				inner1.setFenpqh(c.getFenpqbh());//循环
				inner1.setShengcxbh(c.getShengcxbh());//产线编号
				inner1.setQid(bean.getXianbk());//循环起点
				//mantis 5950 inner1.setZick(bean.getXianbk()+lingjck.getZickbh());//从零件仓库中带入子仓库,形成6位仓库编码
				inner1.setQidlx(bean.getQidlx());//起点类型
				inner1.setMos(bean.getMos());//模式
				inner1.setJianglms(bean.getJianglms());//将来模式
				checkModel(bean.getMos(),bean.getJianglms(),1);
				inner1.setJianglmssxsj(bean.getJianglmssxsj());//将来模式生效时间
				checkDate(bean.getJianglmssxsj());
				inner1.setCreator(user.getUsername());
				inner1.setCreateTime(DateTimeUtil.getAllCurrTime());
				inner1.setEditor(user.getUsername());
				inner1.setEditTime(DateTimeUtil.getAllCurrTime());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxShengcxXianb", inner1);
			
				//线边库-定货库 
				if(bean.getDinghk()!=null&&!"".equals(bean.getDinghk())){
					CkxXianbDingh inner2=new CkxXianbDingh();
					//检查零件-仓库（线边库起点）关系是否存在
					lingjck=new Lingjck();
					lingjck.setUsercenter(c.getUsercenter());
					lingjck.setLingjbh(c.getLingjbh());
					lingjck.setCangkbh(bean.getDinghk());//仓库是线边库的起点（订货库）
					obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", lingjck);
					if (obj == null) {
						//return "零件["+lingjian+"]的线边库起点仓库不存在";
						//"零件["+lingjian+"]的线边库起点仓库不存在"
						throw new ServiceException(GetMessageByKey.getMessage("ljdxbqdckbcz",new String[]{lingjian}));
					} else {
						lingjck=(Lingjck) obj;
					}
					inner2.setUsercenter(c.getUsercenter());//用户中心
					inner2.setLingjbh(c.getLingjbh());//零件编号
					inner2.setXianbck(bean.getXianbk());//线边库
					//mantis 5950 inner2.setXianbkZick(inner1.getZick());//线边库子仓库
					inner2.setQid(bean.getDinghk());//订货库
					//mantis 5950 inner2.setZick(bean.getDinghk()+lingjck.getZickbh());//定货库子仓库
					inner2.setQidlx(bean.getQidlx2());//起点类型
					inner2.setMos(bean.getMos2());//模式
					inner2.setJianglms(bean.getJianglms2());//将来模式
					checkModel(bean.getMos2(),bean.getJianglms2(),3);
					inner2.setJianglmssxsj(bean.getJianglmssxsj2());//将来模式生效时间
					checkDate(bean.getJianglmssxsj2());
					inner2.setCreator(user.getUsername());
					inner2.setCreateTime(DateTimeUtil.getAllCurrTime());
					inner2.setEditor(user.getUsername());
					inner2.setEditTime(DateTimeUtil.getAllCurrTime());
					
					
					
					
					
					//根据线边库检查已有的路径2
					CkxXianbDingh cond =new CkxXianbDingh();
					cond.setUsercenter(c.getUsercenter());
					cond.setLingjbh(c.getLingjbh());//零件编号
					cond.setXianbck(bean.getXianbk());//线边库
					obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getCkxXianbDingh", cond);
					if(obj==null){//如果不存在此线边库的路径2，则添加 
						//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getCkxXianbDingh", parameterObject)
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxXianbDingh", inner2);
					}else{//如果存在此线边库的路径2，则更新路径2
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDingh", inner2);
					}
				}

				//外部模式
				CkxWaibms wm=new CkxWaibms();
				wm.setUsercenter(c.getUsercenter());//用户中心
				wm.setLingjbh(c.getLingjbh());//零件编号
				//wm.setMudd(mudd);//目的地（线边仓库/订货库）
				wm.setFenpqh(c.getFenpqbh());//设置外部模式的分配区号  使外部模式与内部路径一一对应
				wm.setZhidgys(bean.getZhidgys());//指定供应商
				wm.setMos(bean.getWms());//外部模式
				wm.setJianglms(bean.getWjlms());//将来模式
				checkModel(bean.getWms(),bean.getWjlms(),2);
				wm.setShengxsj(bean.getShengxsj());//生效时间
				checkDate(bean.getShengxsj());
				wm.setCreator(user.getUsername());
				wm.setCreateTime(DateTimeUtil.getAllCurrTime());
				wm.setEditor(user.getUsername());
				wm.setEditTime(DateTimeUtil.getAllCurrTime());
				//---------------------------更新外部模式--------------begin
				bean.setLingjbh(c.getLingjbh());
				bean.setUsercenter(c.getUsercenter());
				updateInnerPathMos(bean);
				//---------------------------更新外部模式--------------end
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibms", wm);
				
//			} catch (DataAccessException e) {
//				//"零件["+lingjian+"]的物流路径添加失败"
//				throw new ServiceException(GetMessageByKey.getMessage("ljdwlljaddfail",new String[]{lingjian})+e.getMessage());
//			} 
		}
		return GetMessageByKey.getMessage("savesuccess");
	}
	
	
	
	

	/**
	 * 修改内部物流路径，外部模式
	 * @param bean
	 * @param user
	 * @return
	 */

	@Transactional
	public String saveCkxInnerPath(CkxInnerPathAndModle bean,LoginUser user){
		
//		try {
			//验证零件-供应商关系是否存在
			if(null != bean.getZhidgys()&& !"".equals(bean.getZhidgys()) ){
				Lingjgys lingjgys = new Lingjgys();
				lingjgys.setBiaos("1");
				lingjgys.setLingjbh(bean.getLingjbh());
				lingjgys.setUsercenter(bean.getUsercenter());
				lingjgys.setGongysbh(bean.getZhidgys());
				Object lingjobj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjgys", lingjgys);
				if (lingjobj == null) {
					//return "零件["+lingjian+"]的循环起点仓库不存在";
					//"零件:"+bean.getLingjbh()+",供应商："+bean.getZhidgys()+" 零件供应商不存在此对应关系！"
					throw new ServiceException(GetMessageByKey.getMessage("ljgysbczcgx",new String[]{
							bean.getLingjbh(),bean.getZhidgys()
					}));
				} 
			}
			
			String lingjian=bean.getLingjbh();
			//产线-线边库
			CkxShengcxXianb inner1 =new CkxShengcxXianb();
			inner1.setUsercenter(bean.getUsercenter());//用户中心
			inner1.setLingjbh(bean.getLingjbh());//零件编号
			inner1.setFenpqh(bean.getFenpqh());//循环
			inner1.setShengcxbh(bean.getShengcxbh());//产线编号
			inner1.setQid(bean.getXianbk());//循环起点
			inner1.setQidlx(bean.getQidlx());//起点类型
			inner1.setMos(bean.getMos());//模式
			inner1.setJianglms(bean.getJianglms());//将来模式
			checkModel(bean.getMos(),bean.getJianglms(),1);
			inner1.setJianglmssxsj(bean.getJianglmssxsj());//将来模式生效时间
			checkDate(bean.getJianglmssxsj());
			inner1.setEditor(user.getUsername());
			inner1.setEditTime(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxShengcxXianb", inner1);
			
			
			//检查零件-仓库（线边库）关系是否存在
			Lingjck lingjck=new Lingjck();
			lingjck.setUsercenter(bean.getUsercenter());
			lingjck.setLingjbh(bean.getLingjbh());
			lingjck.setCangkbh(bean.getXianbk());//仓库是循环的起点（线边库）
			Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", lingjck);
			if (obj == null) {
				////"零件["+lingjian+"]的循环起点仓库不存在"
				throw new ServiceException(GetMessageByKey.getMessage("ljdxhqdckbcz",new String[]{lingjian}));
			} else {
				lingjck=(Lingjck) obj;
			}
			//mantis 5950 inner1.setZick(bean.getXianbk()+lingjck.getZickbh());
			
			
			
			
			//线边库-订货库
			//如果修改时选择了订货库，则修改或增加线边库-订货库关系
			if(bean.getDinghk()!=null&&!"".equals(bean.getDinghk())){
				CkxXianbDingh inner2=new CkxXianbDingh();
				inner2.setUsercenter(bean.getUsercenter());//用户中心
				inner2.setLingjbh(bean.getLingjbh());//零件编号
				inner2.setXianbck(bean.getXianbk1());//原线边库
				inner2.setTempId(bean.getXianbk());//修改后的线边库
				inner2.setQid(bean.getDinghk());//订货库
				inner2.setQidlx(bean.getQidlx2());//起点类型
				inner2.setMos(bean.getMos2());//模式
				inner2.setJianglms(bean.getJianglms2());//将来模式
				checkModel(bean.getMos2(),bean.getJianglms2(),3);
				inner2.setJianglmssxsj(bean.getJianglmssxsj2());//将来模式生效时间
				checkDate(bean.getJianglmssxsj2());
				inner2.setCreator(user.getUsername());
				inner2.setCreateTime(DateTimeUtil.getAllCurrTime());
				inner2.setEditor(user.getUsername());
				inner2.setEditTime(DateTimeUtil.getAllCurrTime());
				
				//检查零件-仓库（订货库）关系是否存在
				lingjck=new Lingjck();
				lingjck.setUsercenter(bean.getUsercenter());
				lingjck.setLingjbh(bean.getLingjbh());
				lingjck.setCangkbh(bean.getDinghk());//仓库是线边库的起点（订货库）
				obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjck", lingjck);
				if (obj == null) {
					//"零件["+lingjian+"]的线边库起点仓库不存在"
					throw new ServiceException(GetMessageByKey.getMessage("ljdxbqdckbcz",new String[]{lingjian}));
				} else {
					lingjck=(Lingjck) obj;
				}
				//mantis 5950 inner2.setZick(bean.getDinghk()+lingjck.getZickbh());
				//mantis 5950 inner2.setXianbkZick(inner1.getZick());
				//根据原线边库检查是否存在已有的旧 【线边库-订货库】路径，
				CkxXianbDingh oldinner2=(CkxXianbDingh)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getCkxXianbDingh", inner2);
						
				/*
				 * 以下算法判断“线边库--订货库”路径修改规则
				 * 路径1：生产线--线边库路径
				 * 路径2：线边库--订货库路径
				 * 
				 */
				
				//如果路径2为空,增加路径2(主要为 （ 路径1）修改为 （ 路径1+路径2）的情况下修妖增加路径2)
				if(oldinner2==null){
					inner2.setXianbck(bean.getXianbk());
					try {
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxXianbDingh", inner2);
					} catch (DataAccessException e) {
						/*
						 * 干扰数据 ：f2-x2-d2
						 * 修改数据：f1-x1 --> f1-x2-d2
						 * 此类型添加，路径2存在主键冲突 
						 */
						throw new ServiceException("零件："+lingjian+" 已存在线边库"+inner2.getTempId()+"与订货库的对应关系，您无需指定订货库");
					}
				}else{

					//如果原线边库不存在路径1则把原线边库（xianbck）更新为新线边库(tempId),如果新线边库已存在（主键冲突 ），则删除已存在数据，再修改 
					//如果路径2是专用路径。可直接修改而不会影响其他路径1
					if(checkRepublicInner2(bean)){
						try {
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDinghForID", inner2);
						} catch (DataAccessException e) {
							//此修改可能会修改主键，即路径2中的线边库,因此如果路径2中已存在此线边库需要先删除，再修改
							CkxXianbDingh cond1=new CkxXianbDingh();
							cond1.setUsercenter(bean.getUsercenter());//用户中心
							cond1.setLingjbh(bean.getLingjbh());//零件编号
							cond1.setXianbck(bean.getXianbk());//新线边库
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxXianbDingh", cond1);//删除原有的路径2
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDinghForID", inner2);//修改路径2
						}
					}else{
						inner2.setXianbck(bean.getXianbk());//更新线边库
						try {						
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxXianbDingh", inner2);
						} catch (DataAccessException e) {
							//0013023:此插入可能会主键重复，因此如果路径2中已存在此线边库，则只做修改
							baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDinghForID", inner2);//修改路径2
						}
					}
				}
			}else{//如果修改时没有选择订货库
				//判断路径2是否有其他路径1使用，如果没有，则可以删除路径2
				if(checkRepublicInner2(bean)){
					CkxXianbDingh inner2=new CkxXianbDingh();
					inner2.setUsercenter(bean.getUsercenter());//用户中心
					inner2.setLingjbh(bean.getLingjbh());//零件编号
					inner2.setXianbck(bean.getXianbk1());//原线边库
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxXianbDingh", inner2);
				}
			}

			//外部模式
			CkxWaibms wm=new CkxWaibms();
			wm.setUsercenter(bean.getUsercenter());//用户中心
			wm.setLingjbh(bean.getLingjbh());//零件编号
			wm.setFenpqh(bean.getFenpqh());//分配区号
			wm.setZhidgys(bean.getZhidgys());//指定供应商
			
			
			//wm.setMudd(bean.getMudd());//原目的地
			//wm.setMudd1(mudd);//目的地（线边仓库/订货库）
			wm.setMos(bean.getWms());//外部模式
			wm.setJianglms(bean.getWjlms());//将来模式
			checkModel(bean.getWms(),bean.getWjlms(),2);
			wm.setShengxsj(bean.getShengxsj());//生效时间
			checkDate(bean.getShengxsj());
			wm.setEditor(user.getUsername());
			wm.setEditTime(DateTimeUtil.getAllCurrTime());
			
			//修改其他分配区上同一种指向同一线边库零件的内部物流路径1
//			CkxShengcxXianb cond =new CkxShengcxXianb();
//			cond.setUsercenter(bean.getUsercenter());//用户中心
//			cond.setLingjbh(bean.getLingjbh());//零件编号
//			cond.setQid(bean.getXianbk1());//原循环起点
//			//获取同一用户中心，同一零件，同一原循环起点的路径集合
//			List<CkxShengcxXianb> list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxShengcxXianb", cond);
//			for (CkxShengcxXianb ckxShengcxXianb : list) {
//				ckxShengcxXianb.setQid(bean.getXianbk());
//				//mantis 5950 ckxShengcxXianb.setZick(bean.getXianbk()+lingjck.getZickbh());
//				ckxShengcxXianb.setEditor(user.getUsername());
//				ckxShengcxXianb.setEditTime(DateTimeUtil.getAllCurrTime());
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxShengcxXianb", ckxShengcxXianb);
//			}
			//---------------------------更新外部模式--------------begin
			updateInnerPathMos(bean);
			//---------------------------更新外部模式--------------end
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibms", wm);
			return GetMessageByKey.getMessage("savesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("savefail")+e.getMessage());
//		}
		
	}
	
	
	/**
	 * 物理删除
	 * @param bean
	 * @return
	 * @update lc 2016.10.18
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String deleteCkxInnerPath(ArrayList<CkxInnerPathAndModle> ckxInnerPathAndModle){
//		try {
		for (int i = 0; i < ckxInnerPathAndModle.size(); i++){
			CkxShengcxXianb inner1 =new CkxShengcxXianb();
			inner1.setUsercenter(ckxInnerPathAndModle.get(i).getUsercenter());//用户中心
			inner1.setLingjbh(ckxInnerPathAndModle.get(i).getLingjbh());//零件编号
			inner1.setFenpqh(ckxInnerPathAndModle.get(i).getFenpqh());//循环
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxShengcxXianb", inner1);
			
			//如果存在订货库
			if(ckxInnerPathAndModle.get(i).getDinghk()!=null&&!"".equals(ckxInnerPathAndModle.get(i).getDinghk())){
				CkxShengcxXianb cond =new CkxShengcxXianb();
				cond.setUsercenter(ckxInnerPathAndModle.get(i).getUsercenter());//用户中心
				cond.setLingjbh(ckxInnerPathAndModle.get(i).getLingjbh());//零件编号
				cond.setQid(ckxInnerPathAndModle.get(i).getXianbk());//循环起点
				//获取同一用户中心，同一零件，同一循环起点的路径集合(生产线-线边库路径)
				List<CkxShengcxXianb> list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxShengcxXianb", cond);
				
				//如果没有另外的【生产线-线边库】的循环起点（线边库）引用 此【线边库-定货库】的线边库，则这段路径可以删除
				if(list==null||list.size()==0){
					CkxXianbDingh inner2=new CkxXianbDingh();
					inner2.setUsercenter(ckxInnerPathAndModle.get(i).getUsercenter());//用户中心
					inner2.setLingjbh(ckxInnerPathAndModle.get(i).getLingjbh());//零件编号
					inner2.setXianbck(ckxInnerPathAndModle.get(i).getXianbk());//原线边库
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxXianbDingh", inner2);
				}
			}
			
			
			//外部物流模式与【生产线-线边库】是一一对应的关系，所以可以直接删除
			CkxWaibms wm=new CkxWaibms();
			wm.setUsercenter(ckxInnerPathAndModle.get(i).getUsercenter());//用户中心
			wm.setLingjbh(ckxInnerPathAndModle.get(i).getLingjbh());//零件编号
			wm.setFenpqh(ckxInnerPathAndModle.get(i).getFenpqh());//分配区号
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxWaibms", wm);			
		}
		return GetMessageByKey.getMessage("deletesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("deletefail")+e.getMessage());
//		}
		
	}
	
	/**
	 * 检查输入日期是否小于当前日期
	 * @param date
	 */
	private void checkDate(String date){
		if(date!=null&&!"".equals(date)){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currdate = new Date();
			Date shengxTime = null;
			try {
				shengxTime = sf.parse(date+" 23:59:59");
			} catch (Exception e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijgscw"));
			} 
			if(shengxTime.getTime()< currdate.getTime()){
				//"生效时间必须大于当前时间"
				throw new ServiceException(GetMessageByKey.getMessage("sxsjbxdydqsj"));
			}
		}
	}
	
	/**
	 * 检查两模式是否相等
	 * @param oldModel
	 * @param newModel
	 */
	private void checkModel(String oldModel,String newModel,int key){
		if(oldModel.equals(newModel)){
			//"将来模式不能等于当前模式"
			String mes = "";
			switch (key) {
			case 1:
				mes = GetMessageByKey.getMessage("mos1yjlms1bnxt");
				break;
			case 2:
				mes = GetMessageByKey.getMessage("wbmsywbjlmsbnxt");
				break;
			case 3:
				mes = GetMessageByKey.getMessage("mos2yjlms2bnxt");
				break;
			default:
				mes = GetMessageByKey.getMessage("jsmsbndydqms");
				break;
			}
			throw new ServiceException(mes);
		}
	}
	/**
	 * 检查零件编号是否存在
	 * 检查分配区编号是否存在
	 * 检查内部物流是否已存在
	 * 手工添加内部物流时使用
	 * @param bean
	 */
	@SuppressWarnings("rawtypes")
	public void checkLingjXHD(CkxLingjxhd bean){
		
//		String sql = "SELECT COUNT(*) FROM "+DBUtilRemove.getdbSchemal()+"CKX_LINGJ L WHERE L.USERCENTER = '"
//			+bean.getUsercenter()+"' AND L.LINGJBH = '"+bean.getLingjbh()+"' and L.BIAOS='1' ";
//		//"零件表中不存在零件编号："+bean.getLingjbh()+" 的数据或数据已失效";
//		String mes =GetMessageByKey.getMessage("ljbzbczljbh",new String[]{bean.getLingjbh()});
//		DBUtilRemove.checkBH(sql, mes);
		
		//--------------检查零件编号是否存在
		CkxLingj lingj=new CkxLingj();
		lingj.setUsercenter(bean.getUsercenter());
		lingj.setLingjbh(bean.getLingjbh());
		lingj.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountLingj", lingj)){
			//"零件表中不存在零件编号："+bean.getLingjbh()+" 的数据或数据已失效";
			String mes =GetMessageByKey.getMessage("ljbzbczljbh",new String[]{bean.getLingjbh()});
			throw new ServiceException(mes);
		}
		
		
		//----------------检查分配区编号是否存在
//		String sql1 = "SELECT COUNT(*) FROM "+DBUtilRemove.getdbSchemal()+"CKX_FENPQ F WHERE F.USERCENTER = '"+
//		bean.getUsercenter()+"' AND F.SHENGCXBH = '"+
//		bean.getShengcxbh()+"' AND F.FENPQH = '"+
//		bean.getFenpqbh()+"'  AND F.BIAOS='1'";
//		//"分配区表中不存在分配区编号："+bean.getFenpqbh()+" 的数据或数据已失效";
//		String mes1 = GetMessageByKey.getMessage("fpqzbczfpqbh",new String[]{bean.getFenpqbh()});
//		DBUtilRemove.checkBH(sql1, mes1);
		
        Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色		
		String key = GetPostOnly.getPostOnly(map);
		
		Fenpq fenpq=new Fenpq();
		fenpq.setUsercenter(bean.getUsercenter());
		fenpq.setFenpqh(bean.getFenpqbh());
		fenpq.setBiaos("1");
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			fenpq.setWulgyyz(value);
		}
		fenpq = (Fenpq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getFenpq_Inner", fenpq);
		if(fenpq == null){
//		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountFenpq", fenpq)){
			//"分配区表中不存在生产线编号："+bean.getShengcxbh()+"分配区编号："+bean.getFenpqbh()+" 的数据或数据已失效";
//			String mes1 = GetMessageByKey.getMessage("fpqzbczscxfpqbh",new String[]{bean.getShengcxbh(),bean.getFenpqbh()});
			String mes1 = "分配区表中不存在用户中心："+bean.getUsercenter()+"分配区编号："+bean.getFenpqbh()+" 的数据或数据已失效或无该分配区数据权限";;
			throw new ServiceException(mes1);
		}
		bean.setShengcxbh(fenpq.getShengcxbh());
		
		
		
		//------------------检查内部物流是否已存在
//		String sql2 = "SELECT COUNT(*) FROM CKX_SHENGCX_XIANB  WHERE USERCENTER||LINGJBH||FENPQH = '"
//			+bean.getUsercenter()+bean.getLingjbh()+bean.getFenpqbh()+"'";
//		//"此内部物流已存在，请重新输入";
//		String mes2 = GetMessageByKey.getMessage("cnbwlycz");
//		try {
//			DBUtilRemove.checkBH(sql2, "");
//		} catch (Exception e) {
//			return;
//		}
//		throw new ServiceException(mes2);
		
		CkxShengcxXianb inner=new CkxShengcxXianb();
		inner.setUsercenter(bean.getUsercenter());
		inner.setLingjbh(bean.getLingjbh());
		inner.setFenpqh(bean.getFenpqbh());
		if(DBUtil.checkCount(baseDao,"carry.getCountCkxShengcxXianb", inner)){
			//"此内部物流已存在，请重新输入";
			String mes = GetMessageByKey.getMessage("cnbwlycz");
			throw new ServiceException(mes);
		}
	}
	/**
	 * hj
	 * 更新外部模式
	 * @param bean
	 */
	public void updateInnerPathMos(CkxInnerPathAndModle bean){
		if(null == bean.getDinghk()||"".equals(bean.getDinghk())){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxShengcxXianbMos", bean);
		}else{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXianbDinghMos", bean);
		}
	}
	/**
	 * 检测有多少条外部模式被更新
	 * @param bean
	 * @param list
	 * @return
	 */
	public String checkUpdateInnerPath(CkxInnerPathAndModle bean, List<CkxLingjxhd> list,Integer operate){
		List<CkxLingjxhd> listBean = forByList(list);
		if(null == bean.getDinghk()||"".equals(bean.getDinghk())){
			return forCheckUpdate(listBean, "carry.checkUpdateCkxShengcxXianb", bean, GetMessageByKey.getMessage("xianbk"), operate);
		}else{
			return forCheckUpdate(listBean, "carry.checkUpdateCkxXianbDingh", bean, GetMessageByKey.getMessage("dinghk"), operate);
		}
	}
	/**
	 * 循环检测有多少外部模式被更新	 * 
	 * @return
	 */
	public String forCheckUpdate(List<CkxLingjxhd> listBean,String sqlId,CkxInnerPathAndModle beans,String name,Integer operate){
		StringBuffer sb = new StringBuffer();
		for (CkxLingjxhd ckxLingjxhd : listBean) {
			CkxInnerPathAndModle bean = new CkxInnerPathAndModle();
			bean.setUsercenter(ckxLingjxhd.getUsercenter());
			bean.setLingjbh(ckxLingjxhd.getLingjbh());
			bean.setXianbk(beans.getXianbk());		
			bean.setDinghk(beans.getDinghk());		
			Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(sqlId,bean);
			Integer param = (count-operate)>0?(count-operate):0;
			if(0 == param.intValue()){
				continue;
			}
			//"用户中心："+bean.getUsercenter()+",零件："+bean.getLingjbh()+","+name+":"+ck+",将会有"+param+"条数据被修改"
			sb.append(GetMessageByKey.getMessage("yhzxljckjhysjsyx",new String[]{
					bean.getUsercenter(),bean.getLingjbh(),name,"".equals(beans.getDinghk())?beans.getXianbk():beans.getDinghk(),param.toString()
			}));
		}
		if("".equals(sb.toString())){
			//"未有数据受影响"
			sb.append(GetMessageByKey.getMessage("wysjsyx"));
		}
		return sb.toString();
	}
	/**
	 * 过滤 （同一零件，只需计算一次）
	 * @param list
	 */
	public List<CkxLingjxhd> forByList(List<CkxLingjxhd> list){
		List<CkxLingjxhd> listBean = new ArrayList<CkxLingjxhd>();
		Map<String,String> map = new HashMap<String, String>();
		for (CkxLingjxhd ckxLingjxhd : list) {
			if(!map.containsKey(ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh())){
				map.put(ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh(), "");
				listBean.add(ckxLingjxhd);
			}			
		}
		return listBean;
	}
	
	/**
	 * 检查带原线边库的路径2是否专用路径（是否只有当前路径1与之匹配）  kong  2012-12-21
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkRepublicInner2(CkxInnerPathAndModle bean){
		//根据原线边库检查是否存在已有的旧 【生产线-线边库】路径，
		CkxShengcxXianb cond=new CkxShengcxXianb();
		cond.setUsercenter(bean.getUsercenter());
		cond.setLingjbh(bean.getLingjbh());
		cond.setQid(bean.getXianbk1());//使用原线边库
		//带原线边库的内部路径1
		List<CkxShengcxXianb> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxShengcxXianb", cond);
		for (CkxShengcxXianb s : list) {
			//如果路径2被多条路径1共用,并且修改了线边库（主键）则路径2为非专用路径
			if(!bean.getFenpqh().equals(s.getFenpqh())&&!bean.getXianbk1().equals(bean.getXianbk())){return false;}
		}
		return true;
	}
	
	/**
	 * 分配区权限校验
	 */
	public int checkFenpq(Fenpq fenpq){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountFenpq",fenpq);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}
}

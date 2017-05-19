package com.athena.truck.module.churcsb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Yund;
import com.athena.util.CommonUtil;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.annotaions.Param;
/**
 * 运单service，包含运单明细的相关业务处理
 * @author huilit
 *
 */
@Component
public class YundService extends BaseService<Yund> {

	/**
	 * 获得命名空间
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	@Override
	protected String getNamespace() {
		return "kac_yund";
	}
	
	public void insertYund(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertYund", param);
	}
	
	public void insertYundmx(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertYundmx", param);
	}
	
	/**
	 * 一次申报的标准过程，运单生成整体业务
	 * @param yundParam 运单数据
	 * @param yundmxParams 运单明细数据，包含BL单号，因此可有多个
	 */
	@Transactional
	public void insertShenbao(Yund yundParam,List<Yund> yundmxParams){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertYund", yundParam);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".insertYundmx", yundmxParams);
	}
	
	/**
	 * 申报标准过程的双重复数形式，运单生成整体业务
	 * @param yundParam 运单数据
	 * @param yundmxParams 运单明细数据，包含BL单号，因此可有多个
	 */
	@Transactional
	public void insertShenbao(List<Yund> yundParams,List<Yund> yundmxParams){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".insertYund", yundParams);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".insertChurcls", yundParams);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".insertYundmx", yundmxParams);
	}
	
	/**
	 * 通过条件（卡车号、大站台号等）查找BL单
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findBldsByParams(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".findBldsByParams", params);
	}
	/**
	 * 通过条件（卡车号、用户中心等）查找BL单
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findBldsByKach(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".findBldsByKach", params);
	}
	
	/**
	 * 通过条件（用户中心、等待区域）查找流程定义（本模块只查找流程号为1即申报的流程）
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryLiucdy(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryLiucdy", params);
	}
	
	/**
	 * 通过条件（大站台编号）查大站台是否已失效
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryBiaosFlag(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryBiaosFlag", params);
	}
	
	/**
	 * 通过条件（BL号）查大站台
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryHunzFlag(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryHunzFlag", params);
	}
	/**
	 * 通过条件（BL号）查卡车号是否有不同
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryKachFlag(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryKachFlag", params);
	}
	
	/**
	 * 通过条件（usercenter,区域组id）查当前用户有权区域
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryCurUserQuy(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryCurUserQuy", params);
	}
	
	/**
	 * 通过条件（卡车号）查当前卡车上一次的出厂时间，用于出厂标记功能
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryLastChucsj(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryLastChucsj", params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryRongqdj(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryRongqdj", params);
	}
	
	/**
	 * 出厂标记
	 * @param param
	 */
	@Transactional
	public void updateChucbj(Yund param,String str){
		if("2".equals(str)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".deleteChelpd", param);	//删除排队表中的数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.insertChlepdls",param);;	//将排队表中删除的数据插入历史表中
		}
		if(!"80".equals(str)&&!"90".equals(str)){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateChucbj", param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertKachChuc", param);
		}
	}
	
	public void updateRongqzt(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateDanjzt", param);
	}
	
	/**
	 * 出厂申报编辑运单
	 * @param param
	 */
	public void updateYundChuc(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateYundChuc", param);
	}

	/**
	 * 出厂申报编辑卡车出入厂流程
	 * @param param
	 */
	public void updateKachChuc(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateKachChuc", param);
	}

	/**
	 * 出厂申报插入卡车出入厂流程
	 * @param param
	 */
	public void insertKachChuc(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertKachChuc", param);
	}

	/**
	 * 查询容器返空情况
	 * ，这里需要注意 卡车号 的参数值是否需要暂时设为'%'加上后六位卡车号的形式
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryRongqfkqk(Map<String,Object> params){
//		String kach = (String)params.get("kach");
//		kach = "%" + kach.substring(1);
//		params.put("kach", kach);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryRongqfkqk", params);
	}

	/**
	 * （不使用）扫描或输入单据编号时的查询操作。danjlx 容器返空单 或 空车出厂单
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryRongqfkDanjbh(Map<String,Object> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryRongqfkDanjbh", params);
	}
	
	/**
	 * 查询某一卡车最近一次入厂申报的运单
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Yund> queryYundForChuc(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryYundForChuc", params);
	}
	@SuppressWarnings("unchecked")
	public List<Yund> queryAllYundForChuc(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".queryAllYundForChuc", params);
	}
	

	/**
	 * 出厂申报整个流程
	 * @param param
	 */
	@Transactional
	public void updateChuc(Yund param){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateYundChuc", param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertKachChuc", param);
	}
	/**
	 * 出厂申报整个流程整体操作
	 * @params params
	 * @djlist djlist
	 */
	@Transactional
	public void updateChucBatch(List<Yund> params,List<Yund> djlist){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".updateYundChuc", params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".insertKachChuc", params);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(getNamespace()+".updateDanjzt", djlist);
	}
	
	/**
	 * 申报时查看运单
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryYundCk(Map<String, String> params,Yund bean){
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(getNamespace()+".queryYundCk", params, bean);
	}
	
	/**
	 * 运单删除
	 */
	@Transactional
	public String yundSC(ArrayList<Yund> list,Map<String,String> params) throws ServiceException {
		//System.out.println(list.size()+"***************");
		//String message = "";
		for( int i = list.size()-1;i>=0;i--){
			Yund bean = list.get(i);
			bean.setEditTime(DateUtil.curDateTime());
			bean.setEditor(params.get("username"));
			bean.setUsercenter(params.get("usercenter"));
			//System.out.println(bean.getYundh()+"***************");
			//bean.setYundh(params.get("YUNDH"));
			//System.out.println(bean.getYUNDH()+"***************");
			Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(getNamespace()+".queryYundChecks",bean);

			if(yd==null ){
				list.remove(i);
			}else{
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.deleteYundmxss",bean);
				if("1".equals(yd.getZhuangt())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".updateyunds",bean);
					
					yd.setBeiz1(params.get("username"));
					yd.setBeiz2(DateUtil.curDateTime());
					yd.setBeiz3("D");
					yd.setCreateTime(DateUtil.curDateTime());
					yd.setCreator(params.get("username"));
					yd.setUsercenter(params.get("usercenter"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".insertChurclss",yd);
					//message ="0000";
				}else{
					throw new ServiceException("运单"+yd.getYundh()+"状态已经改变为"+"'"+yd.getZhuangtmc()+"'"+"，不能删除！");
				}
			}
		}	
		return "0000";
	}
	
	//查询BL单信息是否已申报
	public boolean queryBLH(Yund yd){
		boolean flag=false;
	    Integer count=	(Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(getNamespace()+".queryBLH",yd);
		if(count>0){
			flag = true;
	    }
		return flag;
		
	}
}

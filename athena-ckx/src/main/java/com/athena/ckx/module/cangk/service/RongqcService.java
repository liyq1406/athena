package com.athena.ckx.module.cangk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.entity.cangk.Rongqcxhd;
import com.athena.ckx.entity.cangk.Rongqzz;
import com.athena.ckx.entity.cangk.Wuldrqgx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 空容器场
 * @author denggq
 * @date 2012-2-2
 * @modify 2012-2-18
 */
@Component
public class RongqcService extends BaseService<Rongqc>{

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
	 * @date 2012-2-17
	 * @param bean
	 * @return Map 分页的结果
	 * @modify 2012-2-18
	 */
	@Transactional
	public Map<String, Object> select(Rongqc bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryRongqc",bean,bean);
	}

	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-2-2
	 * @param insert,userID
	 * @return  ""
	 * @modify 2012-2-18
	 */
	@Transactional
	public String inserts(Rongqc bean,String userID)throws ServiceException{
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//如果新增是否返空 为 1 的话, 则直接保存通过,如果为 0 的话,则保证记账区编号不能为空
			if((bean.getShiffk().equals("R")||bean.getShiffk().equals("F"))&& null==bean.getJizqbh()){
				throw new ServiceException("容器场"+bean.getRongqcbh()+"必须填写记账区编号");
			}
			//在对记账区编号不为空   校验 存在性 和  唯一性
			if((bean.getShiffk().equals("R")||bean.getShiffk().equals("F"))&& null != bean.getJizqbh()&&!"".equals(bean.getJizqbh())){
				Rongqc rongqzz = new Rongqc();
				rongqzz.setUsercenter(bean.getUsercenter());
				rongqzz.setShiffk("J");
				rongqzz.setRongqcbh(bean.getJizqbh());
				String mes1 = "记账区编号"+bean.getJizqbh()+"不存在";
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountJizq", rongqzz)){
					throw new ServiceException(mes1);
				}
//				Rongqc rongqc1  = (Rongqc)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getRongqc",bean);
//				if(null!=rongqc1){
//					throw new ServiceException("记账区编号已被占用");
//				}
			}
			//如果修改是否返空 为 1 的话则直接清掉记账区编号 也不用验证存在性和唯一性
			if(bean.getShiffk().equals("J")){
				bean.setJizqbh("");
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertRongqc",bean);
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-2-2
	 * @param edit,userID
	 * @return ""
	 * @modify 2012-2-18
	 */
	@Transactional
	public String edits(Rongqc bean,String userID) throws ServiceException{
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			if(bean.getShiffk().equals("J")&&!bean.getYcshiffk().equals(bean.getShiffk())){
				throw new ServiceException("不能修改为记账区");
			}
			if(bean.getShiffk().equals("J")){
				bean.setJizqbh("");
			}
			//修改容器场 要先判断是否修改了 是否返空,是的话 要针对不同的情况进行处理 
			if((bean.getShiffk().equals("R")||bean.getShiffk().equals("F"))&&null==bean.getJizqbh()){
				throw new ServiceException("容器场"+bean.getRongqcbh()+"必须填写记账区编号");
			}
			// 如果没修改是否返空   则需要校验 记账编号的存在性和唯一性
			if((bean.getShiffk().equals("R")||bean.getShiffk().equals("F"))&& null != bean.getJizqbh()&&!"".equals(bean.getJizqbh())){
				Rongqc rongqc = new Rongqc();
				rongqc.setUsercenter(bean.getUsercenter());
				rongqc.setShiffk("J");
				rongqc.setRongqcbh(bean.getJizqbh());
				String mes1 = "记账区编号"+bean.getJizqbh()+"不存在";
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountJizq", rongqc)){
					throw new ServiceException(mes1);
				}
			}
			
			//如果修改是否返空 为 1 的话,需要检验该记账区对应的容器数量是否为0,如果为0 则直接清掉记账区编号 也不用验证存在性和唯一性
			//否则 则修改不成功
			if(!bean.getYcshiffk().equals(bean.getShiffk())||"0".equals(bean.getBiaos())){
				Rongqzz rongqzz = new Rongqzz();
				rongqzz.setUsercenter(bean.getUsercenter());
				rongqzz.setWuld(bean.getRongqcbh());
				String rongqsl =(String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getRongqzz",rongqzz);
				if(null!=rongqsl && 0!=Integer.parseInt(rongqsl)){
					throw new ServiceException("区域中对应的容器数量不为0");
				}
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateRongqc",bean);
		return "success";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-2-2
	 * @param delete,userID
	 * @return ""
	 * @modify 2012-2-18
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String deletes(Rongqc bean) throws ServiceException{
		Rongqc rongqc = new Rongqc();
		rongqc.setUsercenter(bean.getUsercenter());
		rongqc.setJizqbh(bean.getRongqcbh());
//		rongqc.setRongqcbh(bean.getRongqcbh());
//		rongqc.setShiffk("J");
		List<Rongqc>  rongqcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryRongqc",rongqc);
		if(0 != rongqcList.size()){
			throw new ServiceException("容器场"+bean.getRongqcbh()+"已被其它容器场类型的容器场使用");
		}
		Wuldrqgx wuldrqgx = new Wuldrqgx();
		wuldrqgx.setUsercenter(bean.getUsercenter());
		wuldrqgx.setWuld2(bean.getRongqcbh());
		List<Wuldrqgx> wuldrqgxlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryWuldrqgx", wuldrqgx);
		Rongqcxhd rongqcxhd = new Rongqcxhd();
		rongqcxhd.setRongqcbh(bean.getRongqcbh());
		List<Rongqcxhd> rongqcxhddlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryRongqcxhd", rongqcxhd);
		if(0!=wuldrqgxlist.size() || 0!=rongqcxhddlist.size()){
			throw new ServiceException("容器场"+bean.getRongqcbh()+"已关联其他物理点");
		}
		//验证仓库里面是否使用了此容器厂   0006204
		Cangk cangk = new Cangk();
		cangk.setUsercenter(bean.getUsercenter());
		cangk.setRongqcbh(bean.getRongqcbh());
		List<Cangk> cangklist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk", cangk);
		if(0!=cangklist.size() ){
			throw new ServiceException("容器场"+bean.getRongqcbh()+"已被仓库使用");
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteRongqcs", bean);
		return "删除成功";
	}

	
	/**
	 * 失效
	 * @author denggq
	 * @date 2012-2-22
	 * @return 主键
	 */
	@Transactional
	public String doDelete(Rongqc bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteRongqc", bean);
		return bean.getRongqcbh();
	}
}
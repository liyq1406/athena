package com.athena.ckx.module.carry.service;


import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.carry.CkxXiehztxhsj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;

/**
 * 卸货站台循环时间
 * @author kong
 * 2012-02-28
 */
@Component
public class CkxXiehztxhsjService  extends BaseService<CkxXiehztxhsj>{
	
	/**
	 * 获取命名空间
	 */
	@Override
	protected String getNamespace() {
		return "carry";
	}
	
	/**
	 * 添加卸货站台时间
	 * @param bean
	 * @param user
	 * @return
	 */
	public String addCkxXiehztxhsj(CkxXiehztxhsj bean,LoginUser user){
		bean.setCreateTime(DateTimeUtil.getAllCurrTime());
		bean.setCreator(user.getUsername());
		bean.setEditor(user.getUsername());
		bean.setEditTime(DateTimeUtil.getAllCurrTime());
//		try {
			
			/*  -----  验证子仓库   */
			Zick zick=new Zick();
			zick.setCangkbh(bean.getCangkbh().substring(0, 3));
			zick.setZickbh(bean.getCangkbh().substring(3, 6));
			zick.setUsercenter(bean.getUsercenter());
			zick.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountZick", zick)){
				//"子仓库表中不存在当前用户中心下仓库号为："+bean.getCangkbh().substring(0,3)"且子仓库号为："+bean.getCangkbh().substring(3,6)+"的数据或该数据已失效";
				String mes1 = GetMessageByKey.getMessage("zckzbczckhzckh",new String[]{
						bean.getCangkbh().substring(0,3),bean.getCangkbh().substring(3,6)
				});
				throw new ServiceException(mes1);
			}
			
			/*---------  验证卸货站台编组   */
			Xiehztbz xiehztbz=new Xiehztbz();
			xiehztbz.setUsercenter(bean.getUsercenter());
			xiehztbz.setXiehztbzh(bean.getXiehztbh());
			xiehztbz.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXiehztbz", xiehztbz)){
				//"卸货站台编组表中不存在当前用户中心下编组号为："+bean.getXiehztbh()+"的数据或该数据已失效";
				String mes2 = GetMessageByKey.getMessage("xiehztbzbczbzhsj",new String[]{bean.getXiehztbh()});
				throw new ServiceException(mes2);
			}
			
//			Map<String,String> map1 = new HashMap<String,String>();
//			Map<String,String> map2 = new HashMap<String,String>();
//			map1.put("tableName", "ckx_zick");
//			map1.put("cangkbh", bean.getCangkbh().substring(0, 3));
//			map1.put("zickbh", bean.getCangkbh().substring(3, 6));
//			map1.put("biaos", "1");
//			map1.put("usercenter", bean.getUsercenter());
//			
//			map2.put("tableName", "ckx_xiehztbz");//验证卸货站台编组号
//			map2.put("xiehztbzh", bean.getXiehztbh());
//			map2.put("usercenter", bean.getUsercenter());
//			map2.put("biaos", "1");
//			//map2.put("usercenter", bean.getUsercenter());
//			
//			DBUtilRemove.checkYN(map1, mes1);
//			map1.clear();
//			DBUtilRemove.checkYN(map2, mes2);
//			map2.clear();
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxXiehztxhsj", bean);
			return GetMessageByKey.getMessage("addsuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("addfail")+"卸货站台编组号已存在!");
//		}
		
	}

	/**
	 * 修改卸货站台时间
	 * @param bean
	 * @param user
	 * @return
	 */
	public String save(CkxXiehztxhsj bean,LoginUser user){
		bean.setEditor(user.getUsername());//便捷式
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		bean.setCangkbh(bean.getCangkbh());
		bean.setXiehztbh(bean.getXiehztbh());
//		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxXiehztxhsj", bean);
			return GetMessageByKey.getMessage("savesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("savefail")+e.getMessage());
//		}
	}

	/**
	 * 逻辑删除卸货站台时间
	 * @param bean
	 * @param user
	 * @return
	 */
	public String deleteLogic(CkxXiehztxhsj bean,LoginUser user){
		bean.setEditor(user.getUsername());
		bean.setEditTime(DateTimeUtil.getAllCurrTime());
//		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteLogicCkxXiehztxhsj", bean);
			return GetMessageByKey.getMessage("deletesuccess");
//		} catch (DataAccessException e) {
//			throw new ServiceException(GetMessageByKey.getMessage("deletefail")+e.getMessage());
//		}
	}
}

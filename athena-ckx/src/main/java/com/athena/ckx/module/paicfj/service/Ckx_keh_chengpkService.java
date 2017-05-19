package com.athena.ckx.module.paicfj.service;

import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.paicfj.Ckx_keh_chengpk;
import com.athena.ckx.entity.paicfj.Ckx_kehb;
import com.athena.ckx.entity.paicfj.Yunslx;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 客户成品库关系
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_keh_chengpkService extends BaseService<Ckx_keh_chengpk> {


	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 单条数据录入
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_keh_chengpk bean, Integer operant, String userId) throws ServiceException {
		
		bean.setEditor(userId);
		bean.setEdit_time(new Date());
		
		
		if (null != bean.getYunslxbh()&&!"".equals(bean.getYunslxbh())) {// 如果运输路线编号为null ，则不需验证运输路线编号是否存在运输路线表中
			Yunslx yunslx=new Yunslx();
			yunslx.setUsercenter(bean.getUsercenter());
			yunslx.setYunslxbh( bean.getYunslxbh());
			yunslx.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountYunslx", yunslx)){
				// "运输路线表中不存在运输路线编号为" + bean.getYunslxbh() + "的数据或数据已失效";
				String mes4 = GetMessageByKey.getMessage("ysxlbcz")+bean.getYunslxbh()+GetMessageByKey.getMessage("sjsx");
				throw new ServiceException(mes4);
			}
			
			
//			String mes4 = GetMessageByKey.getMessage("ysxlbcz")+bean.getYunslxbh()+GetMessageByKey.getMessage("ysxlbcz");
//			map4.put("tableName", "ckx_yunslx");
//			map4.put("yunslxbh", bean.getYunslxbh());
//			map4.put("biaos", "1");
//			DBUtilRemove.checkYN(map4, mes4);
//			map4.clear();
		}
		//如果是否更新为1则需要更新其他的数据
		if("1".equals(bean.getShifupdate())){
			Ckx_keh_chengpk kehChengpk = new Ckx_keh_chengpk();
			kehChengpk.setCangkbh(bean.getCangkbh());
			kehChengpk.setUsercenter(bean.getUsercenter());
			kehChengpk.setKehbh(bean.getKehbh());	
			kehChengpk.setYunslxbh(bean.getYunslxbh());
			kehChengpk.setKehtqq(bean.getKehtqq());
			kehChengpk.setShifpz(bean.getShifpz());
			kehChengpk.setBeihlx(bean.getBeihlx());
			kehChengpk.setDingdtqq(bean.getDingdtqq());
			
			kehChengpk.setEdit_time(new Date());
			kehChengpk.setEditor(userId);
			//将所有的仓库编号、客户编号相同的数据的备货类型、运输路线编号、是否需要配载 、客户提前期、订单提前期修改为同一种
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_keh_chengpkYSB", kehChengpk);
		}
		if(1 == operant){//增加
			bean.setCreator(userId);
			bean.setCreate_time(bean.getEdit_time());
			
			
			
			
			//检测仓库编号是否存在
			Cangk cangk =new Cangk();
			cangk.setUsercenter(bean.getUsercenter());
			cangk.setCangkbh(bean.getCangkbh());
			cangk.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCangk", cangk)){
				//"仓库表中不存在仓库编号为"+bean.getCangkbh()+"的数据或数据已失效";			
				String mes1 = GetMessageByKey.getMessage("ckbczckbh")+bean.getCangkbh()+GetMessageByKey.getMessage("sjsx");
				throw new ServiceException(mes1);
			}
			
			
			
//			Map<String,String> map1 = new HashMap<String,String>();
//			map1.put("tableName", "ckx_cangk");
//			map1.put("usercenter", bean.getUsercenter());
//			map1.put("cangkbh", bean.getCangkbh());
//			map1.put("biaos", "1");
//			DBUtilRemove.checkYN(map1, mes1);
//			map1.clear();
			//检测客户编号是否存在
			
			Ckx_kehb kehbh=new Ckx_kehb();
			kehbh.setKehbh(bean.getKehbh());
			kehbh.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountKehbh", kehbh)){
				//"客户表中不存在客户编号为"+bean.getKehbh()+"的数据或数据已失效";
				String mes2 = GetMessageByKey.getMessage("khbczkhbh")+bean.getKehbh()+GetMessageByKey.getMessage("sjsx");
				throw new ServiceException(mes2);
			}
//			Map<String,String> map2 = new HashMap<String,String>();
//			map2.put("tableName", "ckx_kehb");		
//			map2.put("kehbh", bean.getKehbh());
//			map2.put("biaos", "1");
//			DBUtilRemove.checkYN(map2, mes2);
//			map2.clear();
			//检测供应商编号是否存在
			
			Gongcy gongys=new Gongcy();
			gongys.setUsercenter(bean.getUsercenter());
			gongys.setGcbh(bean.getChengysbh());
//			gongys.setLeix("3");//类型不等于承运商
			gongys.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountGongys", gongys)){
				//"供应商表中不存在承运商编号编号为"+bean.getChengysbh()+"的数据或数据已失效";
				String mes3 = GetMessageByKey.getMessage("gysbczcys")+bean.getChengysbh()+GetMessageByKey.getMessage("ysxlbcz");
				throw new ServiceException(mes3);
			}
			
			
//			Map<String,String> map3 = new HashMap<String,String>();
//			map3.put("tableName", "ckx_gongys");
//			map3.put("usercenter", bean.getUsercenter());
//			map3.put("gcbh", bean.getChengysbh());
//			map3.put("leix","3");
//			map3.put("biaos", "1");
//			DBUtilRemove.checkYN(map3, mes3);
//			map3.clear();
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_keh_chengpk", bean);
		}else{//修改				
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_keh_chengpk", bean);
		}
		return "success";
	}
	/**
	 * 行编辑数据操作
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String saves(List<Ckx_keh_chengpk> insert,
			List<Ckx_keh_chengpk> edit,
			List<Ckx_keh_chengpk> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert, userID);
		edits(edit, userID);
		removes(delete);
		return "success";
	}
	/**
	 * 数据录入
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_keh_chengpk> insert,String userID) throws ServiceException{
		Date date = new Date();
		for(Ckx_keh_chengpk bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_keh_chengpk",bean);
		}
		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 12/02/21
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_keh_chengpk> edit,String userID) throws ServiceException{
		Date date = new Date();
		for (Ckx_keh_chengpk bean : edit) {
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_keh_chengpk",bean);
		}
		return "";
	}
	/**
	 * 数据删除（物理删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String removes(List<Ckx_keh_chengpk> delete) throws ServiceException{
		for (Ckx_keh_chengpk bean : delete) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_keh_chengpk",bean);
		}
		return "";
	}
	
	/**
	 *  验证同一仓库、客户对应的运输路线、备货类型和是否需要配载必须相同 
	 * @param bean
	 * @return
	 */
	
	/**
	 * 单条数据删除（物理删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String doDelete(Ckx_keh_chengpk bean) throws ServiceException {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_keh_chengpk", bean);
		return bean.getKehbh();
	}
	
	
}

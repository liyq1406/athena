package com.athena.ckx.module.carry.service;


import java.util.ArrayList;
import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.entity.xuqjs.CkxJiaofrl;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;

/**
 * 外部物流详细
 * 逻辑业务层
 * @author kong 
 *2012-02-26
 */
@Component
public class CkxWaibwlService extends BaseService<CkxWaibwl>{
	@Override
	protected String getNamespace() {
		return "carry";
	}

	/**
	 * 保存外部物流路径
	 * @param bean 被修改的实体
	 * @param operant 操作标识 1.添加 2.修改
	 * @param user 操作人信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String save(CkxWaibwl bean, Integer operant,LoginUser user) {
		String success = "";
//		Map<String,String> map4 = new HashMap<String,String>();	
//		String mes4 = "外部物流详细表中不存在路径编号为"+bean.getLujbh()+"的数据";			
//		map4.put("tableName", "ckx_waibwlxx");
//		map4.put("lujbh", bean.getLujbh());
//		map4.put("usercenter", bean.getUsercenter());
//		map4.put("shengxbs", "1");
//		DBUtilRemove.checkYN(map4, mes4);
//		map4.clear();
		
		
		
		//验证交付码（从交付日历中获取）        kong    2012-09-29 
		CkxJiaofrl jfm=new CkxJiaofrl();
		jfm.setUsercenter(bean.getUsercenter());
		jfm.setJiaofm(bean.getJiaofm());
		List<CkxJiaofrl> list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxJiaofrl",jfm);
		if(list==null||list.size()==0){
			//\n请确认交付日历中有此交付码！
			throw new ServiceException("交付码"+bean.getJiaofm()+"不存在！请重新输入");
		}
		//验证交付码（从交付日历中获取）     kong    2012-09-29 
		
		
		if(operant==1){//添加
			bean.setGongysbh(bean.getGongysbh().toUpperCase());//供应商编号
			bean.setMudd(bean.getMudd().trim());//目的地
			bean.setCreateTime(DateTimeUtil.getAllCurrTime());//创建人
			bean.setCreator(user.getUsername());//创建时间
			bean.setEditor(user.getUsername());//修改人
			bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间 
//			try {				
				
				
				//验证供承编号是否存在
//				if(bean.getGongysbh()!=null&&!"".equals(bean.getGongysbh())){
//					String sql = "select count(*) from " + DBUtilRemove.getdbSchemal()
//							+ "ckx_gongys where usercenter = '" + bean.getUsercenter()
//							+ "' and gcbh = '" + bean.getGongysbh() + "' and leix = '1' or leix='2' and biaos='1'";
//					//供应商编号" + bean.getGongysbh() + "不存在
//					String mes = GetMessageByKey.getMessage("gongysbhbcz");
//					DBUtilRemove.checkBH(sql, mes);
//				}
//				
				Gongcy gongys=new Gongcy();
				gongys.setUsercenter(bean.getUsercenter());
				gongys.setGcbh(bean.getGongysbh());
				gongys.setLeix("3");//类型不等于承运商
				gongys.setBiaos("1");
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountGongys", gongys)){
					//供应商编号" + bean.getGongysbh() + "不存在
					throw new ServiceException(GetMessageByKey.getMessage("gongysbhbcz"));
				}
				
				
//				Map<String,String> map3 = new HashMap<String,String>();
//				
//				map3.put("tableName", "ckx_cangk");
//				map3.put("cangkbh", bean.getMudd());
//				map3.put("usercenter", bean.getUsercenter());
//				map3.put("biaos", "1");
//				String mes3 = "仓库表中不存在仓库编号为"+bean.getMudd()+"的目的地";
//				
//				DBUtilRemove.checkYN(map3, mes3);
//				map3.clear();
				
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibwl", bean);
				success =GetMessageByKey.getMessage("addsuccess");
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("addfail")+"数据已存在");
//			}
		}else{//修改
			bean.setEditor(user.getUsername());
			bean.setEditTime(DateTimeUtil.getAllCurrTime());
//			try {
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibwl", bean);
				success = GetMessageByKey.getMessage("savesuccess");
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("savefail")+"数据已存在");
//			}
		}
		//-----> mantis:0012759 hanwu 20160425 start	由于业务变更，不再更新备货周期和运输周期
		//super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibwlByXiehztbz", bean);
		//-----> mantis:0012759 hanwu 20160425 end
		return success;
	}
	
	public String checkEditCkxOuterPath(CkxWaibwl bean,Integer operate){
		Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.checkEditCkxOuterPath",bean);
		Integer param = (count-operate)>0?(count-operate):0;
		String mes = "";
		if(0 == param.intValue()){
			//未有数据受影响确定继续？
			mes = GetMessageByKey.getMessage("wysjsyxqdjx");
		}else{
			//"承运商编号："+bean.getChengysbh()+",卸货站台编组："+bean.getXiehztbz()+"  ,将会有"+param.intValue()+"条数据受影响.\n\n\t确定继续？";
			mes = GetMessageByKey.getMessage("cysxhztbzjhysjsyx",new String[]{
					bean.getChengysbh(),bean.getXiehztbz(),param.toString()	
			});
		}
		return mes;
	}
	/** 0007120
	 * 根据目的地对应的卸货站台变更，更新所有相同的目的地对应的卸货站台
	 */
	public void updateXiehztbzByMudd(String mudd,String xiehztbz,String userId){
		CkxWaibwl bean = new CkxWaibwl();
		bean.setMudd(mudd);
		bean.setXiehztbz(xiehztbz);
		bean.setEditor(userId);
		bean.setEditTime(DateTimeUtil.getAllCurrTime());
		super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibwlByMudd", bean);
	}
    
	/**
	 * 多条记录删除
	 * @author lc
	 * @time 2016-10-17
	 */
	public String removeCkxOuterPath(ArrayList<CkxWaibwl> ckxwaibwl) throws ServiceException{
		CkxWaibwl bean = new CkxWaibwl();
		for (int i = 0; i < ckxwaibwl.size(); i++){
			bean.setUsercenter(ckxwaibwl.get(i).getUsercenter());//用户中心
			bean.setGongysbh(ckxwaibwl.get(i).getGongysbh());//供应商编号
			bean.setFahd(ckxwaibwl.get(i).getFahd());//发货地
			bean.setMudd(ckxwaibwl.get(i).getMudd());//目的地
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxWaibwl",bean);//删除数据库
		}
		return "success";		
	}
}

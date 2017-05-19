package com.athena.ckx.module.transTime.service;


import java.util.Date;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.transTime.CkxYunssk;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 运输时刻
 * @author hj
 *
 */
@Component
public class CkxYunsskService extends BaseService<CkxYunssk> {

	@Inject
	private CkxYunssjMbService ckxYunssjMbService;//运输时间模板（实际）
	//获取sqlmap的表空间
	protected String getNamespace() {
		return "transTime";
	}
	/**
	 * 定时任务，运输时刻数据导入
	 * @return
	 */
	public String insertTimeOut(String creator){
		//清空表		
//		remove(new CkxYunssk());	
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.truncateCkxYunssk");
		//获取运输时刻表并写入数据库
		//0008252  运输时刻算法改变
		ckxYunssjMbService.addYunsskList(creator);
		return "";
	}
	/**
	 * 数据删除
	 * @param bean
	 * @return
	 */
	public String remove(CkxYunssk bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxYunssk",bean);
		return "success";
	}
	/**
	 * 数据操作
	 * @param bean
	 * @param operant
	 * @param userID
	 * @return
	 */
	@Transactional
	public String save(CkxYunssk bean,Integer operant){
		//如果operant=1，则执行增加操作，否则执行修改操作
		if (1 == operant) {
			inserts(bean);
		} else {
			edits(bean);
		}
		return "success";		
	} 
	/**
	 * 数据录入
	 * @param bean
	 * 
	 */
	private void inserts(CkxYunssk bean){
		//若序号为null，则为之创建序号
		if(null == bean.getXuh()){
			bean.setXuh(getMaxXuh(bean));
		}
		check(bean);
		
		bean.setCreate_time(new Date());//设置创建时间
		bean.setEdit_time(new Date());//设置修改时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssk",bean);
	}
	/**
	 * 数据编辑
	 * @param bean
	 * @param userID
	 */
    private void edits(CkxYunssk bean){
		bean.setEdit_time(new Date());//设置修改时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxYunssk",bean);
	}
   /**
    * 获取当前日期的最大序号+1 ，得到当前对象的序号
    * @param bean
    * @return
    */
    private Integer getMaxXuh(CkxYunssk bean){
    	//将日期的“-”去掉
    	String date = bean.getDaohsj().substring(0,10).replace("-", "");
    	//将日期赋值给序号，以便模糊查询
    	bean.setXuh(Integer.parseInt(date));
    	Integer xuh = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("transTime.getMaxCkxYunsskXuh",bean);
    	if( null == xuh){//若不存在，则在此日期后加1
    		xuh = Integer.parseInt(date+"1");
    	}else{//如果存在此日期的序号，则在原序号基础上加1
    		xuh += 1;
    	}
    	return xuh;
    }
    
    /**
	 * 验证是否存在卸货站台、承运商相对应关系
	 * 查询需求归集
	 * @param bean
	 */
    @SuppressWarnings("rawtypes")
	private void check(CkxYunssk bean){
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String postCode = "";
		if(map.get(key)!=null){
			postCode = (String)map.get(key);
		}
		if("ZXCPOA".equals(postCode)){
			return;
		}
		CkxGongysChengysXiehzt ckxGongysChengysXiehzt = new CkxGongysChengysXiehzt();
		ckxGongysChengysXiehzt.setXiehztbh(bean.getXiehztbh());
		ckxGongysChengysXiehzt.setGcbh(bean.getGcbh());
		ckxGongysChengysXiehzt.setUsercenter(bean.getUsercenter());
		ckxGongysChengysXiehzt.setBiaos("1");
		CkxGongysChengysXiehzt ckxGCX = (CkxGongysChengysXiehzt) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("transTime.getCkxGongysChengysXiehzt",ckxGongysChengysXiehzt);
		if(null == ckxGCX){
			throw new ServiceException(GetMessageByKey.getMessage("buczyhzxxxhztbz"));
		}
		
//		List<CkxYunssjMb> list =list(bean);
//		if(1>list.size()){
//			throw new ServiceException("不存在相对应的卸货站台、承运商");
//		}
	}
}

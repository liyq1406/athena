package com.athena.xqjs.module.common.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class LingjGongysService extends BaseService{
	/**
	 * 插入
	 */
	public String doInsert(LingjGongys bean) {
		bean.setLingjbh(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.insertLingjGongys", bean);
		return bean.getLingjbh();
	}
	/**
	 * 删除
	 */
	public String doDelete(LingjGongys bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.deleteLingjGongys", bean);
		return bean.getLingjbh();
	}
	/**
	 * 更新
	 */
	public boolean doUpdate(LingjGongys bean,Map<String,String> map) {
		List<LingjGongys> all = this.queryAll(map) ;
		if(all.isEmpty()){
			return false ;
		}else{
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.updateLingjGongys", bean);
			return count>0;
		}
	}
	
//	public boolean updateLjgys(LingjGongys bean,Map<String,String> map) {
//		List<LingjGongys> all = this.queryAll(map) ;
//		if(all.isEmpty()){
//			return false ;
//		}else{
//			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.updateGys", bean);
//			return count>0;
//		}
//	}
	/**
	 * 查询
	 */
	public Map<String, Object> select(Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("CKX.queryLingjGongys", page);
	}
	
	/**
	 * 查询,list
	 */
	public List<LingjGongys> queryAll(Map<String,String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLingjGongys",map);
	}
	
	public LingjGongys select (String usercenter,String gongys,String lingjbh){
		Map<String,String> map = new HashMap<String,String>();
		if(lingjbh!=null){
		map.put("lingjbh", lingjbh);
		}
		if(gongys!=null){
		map.put("gongysbh", gongys);
		}
		if(usercenter!=null){
		map.put("usercenter", usercenter);
		}
		map.put("biaos", Const.ACTIVE_1);
		LingjGongys lingjgongys = (LingjGongys)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryOneLingjGongys", map);
		map.clear();
		return lingjgongys;
	}
	
	public List selectLingjGongys (String usercenter,String lingjbh,String zhizlx){
		Map<String,String> map = new HashMap<String,String>();
		if(lingjbh!=null){
		map.put("lingjbh", lingjbh);
		}
		
		if(usercenter!=null){
		map.put("usercenter", usercenter);
		}
		if(null!=zhizlx){
			map.put("gonghlx", zhizlx);
		}
		map.put("biaos", Const.SHENGXIAO);
		ArrayList<LingjGongys> list = (ArrayList)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLingjgysForMulu", map);
		map.clear();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<LingjGongys> query(String usercenter, String lingjbh, String biaos) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter);
		map.put("biaos", biaos);
		return (List<LingjGongys>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLjgysObj", map);
	}

	public List<LingjGongys> query(LingjGongys bean) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", bean.getLingjbh());
		map.put("usercenter", bean.getUsercenter());
		map.put("gongysbh", bean.getGongysbh());
		map.put("biaos", Const.ACTIVE_1);
		return (List<LingjGongys>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLjgysObj", map);
	}
	public List<LingjGongys> queryLjgysLike(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLjgysLike", map);
	}
}
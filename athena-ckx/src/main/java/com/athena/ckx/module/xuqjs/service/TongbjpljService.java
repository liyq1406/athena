package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Lingjxhdxh;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Tongbjplj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 同步集配零件分类Service
 * @author qizhongtao
 * @date 2012-4-11
 */
@Component
public class TongbjpljService extends BaseService<Tongbjplj>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Tongbjplj bean,String exportXls){
		if("exportXls".equals(exportXls)){
			List<Tongbjplj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTongbjplj",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryTongbjplj",bean,bean);
	}
	
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-11
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Tongbjplj> insert,ArrayList<Tongbjplj> edit,ArrayList<Tongbjplj> delete,String userName){
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			edits(edit,userName);
			deletes(delete,userName);
		}
		return "success";
	}
	
	/**
	 * 批量insert
	 * @author denggq
	 * @date 2012-5-30
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Tongbjplj> insert,String userName)throws ServiceException{
		for (Tongbjplj bean : insert) {
			
			//零件编号是否存在 校验
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			
			//生产线编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
			Shengcx shengcx = new Shengcx();
			shengcx.setUsercenter(bean.getUsercenter());
			shengcx.setShengcxbh(bean.getShengcxbh());
			shengcx.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
	
			//配送类型是否存在
			if(null != bean.getPeislx()){
//				String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_peislb where peislx = '"+bean.getPeislx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("peisonglx")+bean.getPeislx()+GetMessageByKey.getMessage("notexist"));
			
				//通过配送类型去检验 这个零件是否在零件仓库中 设置了 对应的仓库
				Peislb peislb = new Peislb();
				peislb.setUsercenter(bean.getUsercenter());
				peislb.setPeislx(bean.getPeislx());
				peislb.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountPeislb", peislb, GetMessageByKey.getMessage("peisonglx")+bean.getPeislx()+GetMessageByKey.getMessage("notexist"));
				
				Peislb pslb =(Peislb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryPeislb", peislb);
				Lingjck ljck = new Lingjck();
				ljck.setUsercenter(bean.getUsercenter());
				ljck.setLingjbh(bean.getLingjbh());
				ljck.setCangkbh(pslb.getCangkbh());
				ljck.setZickbh(pslb.getZickbh());
				Lingjck  lingjck = (Lingjck)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjck", ljck);
				if(null==lingjck){
					throw new ServiceException("配送类型"+bean.getPeislx()+"对应的仓库与零件"+bean.getLingjbh()+"在零件-仓库中未设置");
				}
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertTongbjplj", bean);
		}
		return "";
	}
	
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-11
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Tongbjplj> edit,String userName)throws ServiceException{
		for (Tongbjplj bean : edit) {
			
			//配送类型是否存在
			if(null != bean.getPeislx()){
//				String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_peislb where peislx = '"+bean.getPeislx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("peisonglx")+bean.getPeislx()+GetMessageByKey.getMessage("notexist"));
				//通过配送类型去检验 这个零件是否在零件仓库中 设置了 对应的仓库
				Peislb peislb = new Peislb();
				peislb.setUsercenter(bean.getUsercenter());
				peislb.setPeislx(bean.getPeislx());
				peislb.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountPeislb", peislb, GetMessageByKey.getMessage("peisonglx")+bean.getPeislx()+GetMessageByKey.getMessage("notexist"));
				
				Peislb pslb =(Peislb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryPeislb", peislb);
				Lingjck ljck = new Lingjck();
				ljck.setUsercenter(bean.getUsercenter());
				ljck.setLingjbh(bean.getLingjbh());
				ljck.setCangkbh(pslb.getCangkbh());
				ljck.setZickbh(pslb.getZickbh());
				Lingjck  lingjck = (Lingjck)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjck", ljck);
				if(null==lingjck){
					throw new ServiceException("配送类型"+bean.getPeislx()+"对应的仓库与零件"+bean.getLingjbh()+"在零件-仓库中未设置");
				}
			}
			
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateTongbjplj", bean);
		}
		return "";
	}
	
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-11
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Tongbjplj> delete,String userName)throws ServiceException{
		for (Tongbjplj bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteTongbjplj", bean);
		}
		return "";
	}
	
	/**
	 * 获得多个
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Tongbjplj bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTongbjplj",bean);
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getLingjMaps(String usercenter){
		CkxLingj ckxlj = new CkxLingj();
		ckxlj.setUsercenter(usercenter);
		List<CkxLingj>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingj",ckxlj);
		Map<String,Object> map=new HashMap<String,Object>();
		for (CkxLingj lingj : list) {
			map.put(lingj.getUsercenter()+lingj.getLingjbh(),lingj);
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getLingjMap(){
		CkxLingj ckxlj = new CkxLingj();
		ckxlj.setBiaos("1");
		List<CkxLingj>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingj",ckxlj);
		Map<String,Object> map=new HashMap<String,Object>();
		for (CkxLingj lingj : list) {
			map.put(lingj.getUsercenter()+lingj.getLingjbh(),lingj);
		}
		return map;
	}
	
	/**
	 * 根据用户中心+配送类型
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPeislxMap(){
		Peislb pslx = new Peislb();
		pslx.setBiaos("1");
		List<Peislb>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryPeislb",pslx);
		Map<String,Object> map=new HashMap<String,Object>();
		for (Peislb pb : list) {
			map.put(pb.getUsercenter()+pb.getPeislx(), pb);
		}
		return map;
	}
	
	/**
	 * 根据用户中心+配送类型
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getTbjpljMap(){
		Tongbjplj tbjplj = new Tongbjplj();
		List<Tongbjplj>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTongbjplj",tbjplj);
		Map<String,String> map=new HashMap<String,String>();
		for (Tongbjplj tblj : list) {
			map.put(tblj.getUsercenter()+tblj.getLingjbh()+tblj.getShengcxbh()+tblj.getNclass(), tblj.getUsercenter()+tblj.getLingjbh()+tblj.getShengcxbh()+tblj.getNclass());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件 +仓库+子仓库
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getLingjckMap(){
		Lingjck cklj = new Lingjck();
		List<Lingjck>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjck",cklj);
		Map<String,String> map=new HashMap<String,String>();
		for (Lingjck lingj : list) {
			map.put(lingj.getUsercenter()+lingj.getLingjbh()+lingj.getCangkbh()+lingj.getZickbh(), lingj.getZickbh());
		}
		return map;
	}
	
	/**
	 * 查询零件名称
	 * @param Map
	 * @return String
	 */
	public String selectLingjmc(Map<String, String> map) {
		return  (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querylingjmc", map);
	}
}

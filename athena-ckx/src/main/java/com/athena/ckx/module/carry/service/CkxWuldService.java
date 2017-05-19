package com.athena.ckx.module.carry.service;

import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWaibwlxx;
import com.athena.ckx.entity.carry.CkxWuldlx;
import com.athena.ckx.entity.carry.CkxYunswld;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 物理点逻辑业务长
 * @author kong
 * 2012-02-12
 */
@Component
public class CkxWuldService extends BaseService<CkxYunswld>{
	@Override
	protected String getNamespace() {
		return "carry";
	}

	/**
	 * 保存物理点集合
	 * @param addList 
	 * @param editList
	 * @param delList
	 * @param user
	 * @return
	 */
	@Transactional
	public String saveWuld(List<CkxYunswld> addList,List<CkxYunswld> editList,List<CkxYunswld> delList,LoginUser user) {
		add(addList,user);//增极爱
		edit(editList,user);//修改
		del(delList);//删除
		return GetMessageByKey.getMessage("savesuccess");
	}

	/**
	 * 增加数据
	 * @param addList
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public void add(List<CkxYunswld> addList, LoginUser user) {
		for (CkxYunswld wld : addList) {// 循环增加数据
//			try {
				wld.setCreateTime(DateTimeUtil.getAllCurrTime());// 创建时间
				wld.setCreator(user.getUsername());// 创建人
				wld.setEditor(user.getUsername());// 修改人
				wld.setEditTime(DateTimeUtil.getAllCurrTime());// 修改时间s
				
				//验证物理点类型的有效性
				CkxWuldlx wuldlx=new CkxWuldlx();
				wuldlx.setWuldlxbh(wld.getWuldlx());
				wuldlx.setBiaos("1");
				if(!DBUtil.checkCount(baseDao,"carry.getCountWuldlx", wuldlx)){
					//"物理点类型表中不存在物理点编号为："+ckxXiuxr.getWuldlx()+"的数据或该数据已失效";
					String mes = GetMessageByKey.getMessage("wuldlxzbczwldbhsj",new String[]{wld.getWuldlx()});	
					throw new ServiceException(mes);
				}
				
//				Map<String,String> map = new HashMap<String,String>();
//				map.put("tableName", "ckx_wuldlx");
//				map.put("wuldlxbh", wld.getWuldlx());
//				map.put("biaos", "1");
//				//"物理点类型表中不存在物理点编号为："+ckxXiuxr.getWuldlx()+"的数据或该数据已失效";
//				String mes = GetMessageByKey.getMessage("wuldlxzbczwldbhsj",new String[]{wld.getWuldlx()});
//				DBUtilRemove.checkYN(map, mes);
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertWuld", wld);
//			} catch (DataAccessException e) {
//				throw new ServiceException("物理点【"+wld.getWuldbh()+"】添加失败"+e.getMessage());
//			}
		}
	}

	/**
	 * 修改数据
	 * @param editList
	 * @return
	 */
	@Transactional
	public void edit(List<CkxYunswld> editList,LoginUser user){
		for (CkxYunswld wld : editList) {
//			try {
				wld.setEditor(user.getUsername());
				wld.setEditTime(DateTimeUtil.getAllCurrTime());
				
				
				//验证物理点类型的有效性
				CkxWuldlx wuldlx=new CkxWuldlx();
				wuldlx.setWuldlxbh(wld.getWuldlx());
				wuldlx.setBiaos("1");
				if(!DBUtil.checkCount(baseDao,"carry.getCountWuldlx", wuldlx)){
					//"物理点类型表中不存在物理点编号为："+ckxXiuxr.getWuldlx()+"的数据或该数据已失效";
					String mes = GetMessageByKey.getMessage("wuldlxzbczwldbhsj",new String[]{wld.getWuldlx()});	
					throw new ServiceException(mes);
				}
				
				
				
//				Map<String,String> map = new HashMap<String,String>();
//				map.put("tableName", "ckx_wuldlx");
//				map.put("wuldlxbh", wld.getWuldlx());
//				map.put("biaos", "1");
//				//"物理点类型表中不存在物理点编号为："+ckxXiuxr.getWuldlx()+"的数据或该数据已失效";				
//				String mes = GetMessageByKey.getMessage("wuldlxzbczwldbhsj",new String[]{wld.getWuldlx()});
//				DBUtilRemove.checkYN(map, mes);
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateWuld", wld);
//			} catch (DataAccessException e) {
//				//"物理点【"+ckxXiuxr.getWuldbh()+"】修改失败"
//				throw new ServiceException(GetMessageByKey.getMessage("wuldbcsb",new String[]{wld.getWuldbh()})+e.getMessage());
//			}
		}
	}

	/**
	 * 删除数据
	 * 物理删除
	 * @param delList
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void del(List<CkxYunswld> delList){
		for (CkxYunswld wld : delList) {
//			try {
				CkxWaibwlxx detail =new CkxWaibwlxx();
				detail.setWuldbh(wld.getWuldbh());
				/* 获取物流路径详细信息，看看物理点是否被使用 */
				List<CkxWaibwlxx> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWaibwlxx", detail);
				/* 如果使用则无法删除 */
				if(list.size()>0){
					throw new ServiceException(GetMessageByKey.getMessage("wldbhbsywfsc",new String[]{wld.getWuldbh()}));
				}
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteWuld", wld);
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("wuldbcsb",new String[]{wld.getWuldbh()})+e.getMessage());
//			}
		}
	}
}

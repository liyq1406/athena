package com.athena.ckx.module.carry.service;

import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWuldlx;
import com.athena.ckx.entity.carry.CkxYunswld;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 物理点类型
 * @author kong
 * 2012-02-16
 */
@Component
public class CkxWuldlxService extends BaseService<CkxWuldlx>{
	/**
	 * 获取命名空间
	 */
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
	public String saveType(List<CkxWuldlx> addList,List<CkxWuldlx> editList,List<CkxWuldlx> delList,LoginUser user) {
		del(delList);//删除
		add(addList,user);//添加
		edit(editList,user);//修改
		return GetMessageByKey.getMessage("savesuccess");
	}
	/**
	 * 增加数据
	 * @param addList
	 * @return
	 * @throws Exception 
	 */
	private void add(List<CkxWuldlx> addList,LoginUser user) {
		for (CkxWuldlx ckxXiuxr : addList) {// 循环添加数据
			checkSXH(ckxXiuxr);
//			try {				
				ckxXiuxr.setCreateTime(DateTimeUtil.getAllCurrTime());// 创建时间
				ckxXiuxr.setCreator(user.getUsername());// 创建人
				ckxXiuxr.setEditor(user.getUsername());// 修改人
				ckxXiuxr.setEditTime(DateTimeUtil.getAllCurrTime());// 修改时间
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWuldlx", ckxXiuxr);// 添加
//			} catch (DataAccessException e) {
//				//"物理点类型【"+ckxXiuxr.getWuldlxbh()+"】添加失败 ："
//				throw new ServiceException(GetMessageByKey.getMessage("wldlxbcsb",new String[]{ckxXiuxr.getWuldlxbh()})+"数据已存在");
//			}
		}
	}
	/**
	 * 修改数据
	 * @param editList
	 * @return
	 * @throws Exception 
	 */
	private void edit(List<CkxWuldlx> editList, LoginUser user) {
		for (CkxWuldlx ckxXiuxr : editList) {
			checkSXH(ckxXiuxr);
//			try {				
				ckxXiuxr.setEditor(user.getUsername());//修改人
				ckxXiuxr.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWuldlx", ckxXiuxr);
//			} catch (DataAccessException e) {
//				//"物理点类型【"+ckxXiuxr.getWuldlxbh()+"】修改失败 ："
//				throw new ServiceException(GetMessageByKey.getMessage("wldlxbcsb",new String[]{ckxXiuxr.getWuldlxbh()})+e.getMessage());
//			}
		}
	}
	/**
	 * 物理删除数据
	 * @param delList
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void del(List<CkxWuldlx> delList){
		for (CkxWuldlx type : delList) {
//			try {
				CkxYunswld wuld=new CkxYunswld();
				wuld.setWuldlx(type.getWuldlxbh());
				/* 获取物理点中的物理点类型集合 */
				List<CkxYunswld> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxYunswld", wuld);
				if(list!=null&&list.size()>0){
					//"物理点类型编号【"+type.getWuldlxbh()+"】正在使用，无法删除"
					throw new ServiceException(GetMessageByKey.getMessage("wldlxbhbsywfsc",new String[]{type.getWuldlxbh()}));
				}
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxWuldlx", type);
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("wldlxbcsb",new String[]{type.getWuldlxbh()})+e.getMessage());
//			}
		}
	}
	
	/**
	 * 验证物理点类型顺序号是否重复 mantis 0004526
	 * @param bean
	 */
	private void checkSXH(CkxWuldlx bean){
		if(Integer.parseInt(bean.getQissxh())>= Integer.parseInt(bean.getJiessxh())){
			//"结束顺序号必须大于起始顺序号"
			throw new ServiceException(GetMessageByKey.getMessage("jssxhbxdyqssxh"));
		}
		Integer o = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.checkWULDLXQSXSXH", bean);		
		if(o > 0){
			//"起始顺序号到结束顺序号区段不能重合"
			throw new ServiceException(GetMessageByKey.getMessage("qissxhdjssxhqdbnch"));
		}
	}
}
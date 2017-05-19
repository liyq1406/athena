package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.module.cangk.service.YansblljInterface;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;

/**
 * 零件供应商Service
 * @author denggq
 * 2012-3-29
 */
@Component
public class LingjgysService extends BaseService<Lingjgys>{
	
	/**
	 * 注入LingjgysInterface
	 * @author denggq
	 * @date 2012-2-6
	 * @return bean
	 */
	@Inject
	private YansblljInterface yansblljInteface;//零件验收比例设置
	
	/**
	 * 获取命名空间
	 * @author denggq
	 * @time 2012-3-29
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 多数据保存零件供应商
	 * @param insert,edit,delete,String
	 * @return String
	 * @author denggq
	 * @time 2012-3-29
	 */
	@Transactional
	public String save(ArrayList<Lingjgys> insert,ArrayList<Lingjgys> edit,ArrayList<Lingjgys> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userId);
			edits(edit,userId);
			//deletes(delete,userId);
		}
		checkGongyfe(insert, edit, delete);
		return "success";
	}
	/**
	 * 分页查询
	 * hj
	 * @param bean
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryLinjgysDHLX(Lingjgys bean,String exportXls){
		Map<String,Object> map = new HashMap<String, Object>();
		if("exportXls" .equals(exportXls)){			
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjgysDHLX_zw", bean);
			map.put("totas", list.size());
			map.put("rows", list);
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryLingjgysDHLX", bean, bean);
		}
		return map;
	}
	/**
	 * 批量insert
	 * @author denggq
	 * @date 2012-3-29
	 * @param insert,userId
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Lingjgys> insert, String userId){
		for (Lingjgys bean : insert) {
			
			//零件编号是否存在
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
	
			//供应商编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where usercenter = '"+bean.getUsercenter()+"' and gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			Gongcy gongcy = new Gongcy();
			gongcy.setUsercenter(bean.getUsercenter());
			gongcy.setGcbh(bean.getGongysbh());
			gongcy.setBiaos("1");
			gongcy.setLeix("3");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountGongys", gongcy, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
	
			//带出发运地
			Gongcy gongys = new Gongcy();
			gongys.setUsercenter(bean.getUsercenter());
			gongys.setGcbh(bean.getGongysbh());
			String fayd = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.selectFayd", gongys);
			bean.setFayd(fayd);
			
			//生效时间未未来日期 或者失效时间已过 则失效状态
//			String currentDate = DateTimeUtil.getCurrDate();
//			if(DateTimeUtil.compare(currentDate, bean.getShengxsj()) || DateTimeUtil.compare(bean.getShengxsj(), currentDate)){
//				bean.setBiaos("0");
//			}else{
//				bean.setBiaos("1");
//			}
			
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLingjgys", bean);
		}
		return "";
	}
	public String commit(Lingjgys bean,String userId){
		yansblljInteface.commit(bean.getUsercenter(), bean.getLingjbh(), bean.getGongysbh(), userId);
		return "";
	}
	/**
	 * 批量edit
	 * @author denggq
	 * @date 2012-3-29
	 * @param edit,userId
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Lingjgys> edit,String userId)throws ServiceException{
		for (Lingjgys bean : edit) {
			
			Assert.notNull(bean.getGongyfe(), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()
					+GetMessageByKey.getMessage("gongyingfene"));
			Assert.notNull(bean.getShengxsj(), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()
					+GetMessageByKey.getMessage("shengxiaoshijian"));
			Assert.notNull(bean.getShixsj(), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()
					+GetMessageByKey.getMessage("shixiaoshijian"));
			
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//带出发运地
			Gongcy gongys = new Gongcy();
			gongys.setUsercenter(bean.getUsercenter());
			gongys.setGcbh(bean.getGongysbh());
			String fayd = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.selectFayd", gongys);
			bean.setFayd(fayd);
			
			//生效时间未未来日期 或者失效时间已过 则失效状态
//			String currentDate = DateTimeUtil.getCurrDate();
//			if(DateTimeUtil.compare(currentDate, bean.getShengxsj()) || DateTimeUtil.compare(bean.getShengxsj(), currentDate)){
//				bean.setBiaos("0");
//			}else{
//				bean.setBiaos("1");
//			}
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjgys", bean);
		}
		return "";
	}
	
	/**
	 * 批量delete
	 * @author denggq
	 * @date 2012-3-29
	 * @param delete,userId
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Lingjgys> delete,String userId) throws ServiceException{
		for (Lingjgys bean : delete) {
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteLingjgys", bean);
		}
		return "";
	}
	
	
	/**
	 * 判断同一用户中心下的同一零件的供应份额是否为100%
	 * @param bean
	 * @author denggq
	 * @Date 2012-4-23
	 *  
	 */

	@SuppressWarnings("unchecked")
	private void checkGongyfe(ArrayList<Lingjgys> insert , ArrayList<Lingjgys> edit , ArrayList<Lingjgys> delete){
		Map<String,Lingjgys> map = new HashMap<String, Lingjgys>();
		List<String> list = new  ArrayList<String>();
		for (Lingjgys lingjgys : insert) {
			Gongcy gongcy = new Gongcy();
			gongcy.setUsercenter(lingjgys.getUsercenter());
			gongcy.setGcbh(lingjgys.getGongysbh());
			gongcy.setBiaos("1");
			Gongcy bean = (Gongcy) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getGongcy",gongcy);
			String key = lingjgys.getUsercenter()+lingjgys.getLingjbh()+bean.getGonghlx();// 0006145
			lingjgys.setDinghlx(bean.getGonghlx());
			if(!map.containsKey(key)){
				map.put(key, lingjgys);
				list.add(key);
			}
		}
		for (Lingjgys lingjgys : edit) {
			String key = lingjgys.getUsercenter()+lingjgys.getLingjbh()+lingjgys.getDinghlx();// 0006145
			if(!map.containsKey(key)){
				map.put(key, lingjgys);
				list.add(key);
			}
		}
		for (Lingjgys lingjgys : delete) {
			String key = lingjgys.getUsercenter()+lingjgys.getLingjbh()+lingjgys.getDinghlx(); // 0006145
			if(!map.containsKey(key)){
				map.put(key, lingjgys);
				list.add(key);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i =0 ;i<list.size() ;i++) {
			Lingjgys lingjgys = map.get(list.get(i));
			List<Lingjgys> lists =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.checkGongyfe",lingjgys);//根据用户中心查同一零件同一订货路线的消耗比例之和不为0或1的数据
			if(0 != lists.size()){
				sb.append("\t"+GetMessageByKey.getMessage("yonghuzx")+lingjgys.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+lingjgys.getLingjbh()+GetMessageByKey.getMessage("gongyifene"));
				sb.append("\n");
			}
		}
		if(0 < sb.length()){
			throw new ServiceException(sb.toString());
		}
		
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
	public List list(Lingjgys bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjgys",bean);
	}
}





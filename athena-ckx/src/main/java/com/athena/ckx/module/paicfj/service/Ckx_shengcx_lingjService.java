package com.athena.ckx.module.paicfj.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.paicfj.Ckx_shengcx_lingj;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.jdbc.support.MultiDataSource;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 产线零件
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_shengcx_lingjService extends BaseService<Ckx_shengcx_lingj> {


	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return  null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_shengcx_lingj> insert,
			List<Ckx_shengcx_lingj> edit,
			List<Ckx_shengcx_lingj> delete,String userID) throws ServiceException{
		if(0 == insert.size()&& 0 == edit.size() && 0 == delete.size()){
			return "null";
		}
		checkLingjlx(insert, edit);
		inserts(insert, userID);
		edits(edit, userID);
		removes(delete);
//		checkShengcBil();
		checkZhuxfx(insert,edit,delete);
		return "success";
	}
	
	/**
	 * 批量数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_shengcx_lingj> insert,String userID)throws ServiceException{
		Date date = new Date();
		for (Ckx_shengcx_lingj bean : insert) {			
			checkShengcx(bean);
			checkLingjbh(bean);	
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_shengcx_lingj",bean);
		}
		return "success";
	}
	 /**
	  * 验证零件编号是否存在零件表中
	  * @param bean
	  * @return
	  */
	private String checkLingjbh(Ckx_shengcx_lingj bean){
		CkxLingj ckxLingj = new CkxLingj();
		ckxLingj.setUsercenter(bean.getUsercenter());
		ckxLingj.setLingjbh(bean.getLingjbh());
		ckxLingj.setBiaos("1");
		CkxLingj ckxLingjs =  (CkxLingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingj",ckxLingj);
		if(null == ckxLingjs){
			//"零件表中不存在此零件编号或者数据已失效，请重新输入"
			throw new ServiceException(GetMessageByKey.getMessage("ljbhcxsr"));
		}
		return "";
	}
	
	/**
	 * 校验侧围线的零件类型不能为空
	 * @param insert
	 * @param edit
	 */
	private void checkLingjlx(List<Ckx_shengcx_lingj> insert,
			List<Ckx_shengcx_lingj> edit) {
		try{
			for (Ckx_shengcx_lingj bean : insert) {
				Fenzx fzx = getFenzx(baseDao, bean.getUsercenter(), bean.getShengcxbh());
				if(fzx != null && fzx.getFenzxxs() > 1 && bean.getLingjlx() == null){
					throw new ServiceException(new Message("cewxljlxisnull", "i18n.ckx.paicfj.i18n_shengcxlingj").getMessage());
				}
			}
			for (Ckx_shengcx_lingj bean : edit) {
				Fenzx fzx = getFenzx(baseDao, bean.getUsercenter(), bean.getShengcxbh());
				if(fzx != null && fzx.getFenzxxs() > 1 && bean.getLingjlx() == null){
					throw new ServiceException(new Message("cewxljlxisnull", "i18n.ckx.paicfj.i18n_shengcxlingj").getMessage());
				}
			}
		}catch(Exception e){
			//抛出查询失败的异常
			 throw new ServiceException(e.getMessage());
		}
		
	}
	/**
	 * 批量数据编辑
	 * @author hj
	 * @Date 2012-02-21
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_shengcx_lingj> edit,String userID)throws ServiceException{
		Date date = new Date();
		for (Ckx_shengcx_lingj bean : edit) {			
				bean.setEditor(userID);
				bean.setEdit_time(date);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_shengcx_lingj", bean);
		}
		return "success";
	}
	/**
	 * 批量删除（物理删除）
	 * @author hj
	 * @Date 2012-02-21
	 * @param delete
	 * @return ""
	 * @throws ServiceException
	 */
	private String removes(List<Ckx_shengcx_lingj> delete)throws ServiceException{
		for (Ckx_shengcx_lingj bean : delete) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_shengcx_lingj",bean);
		}
		return "success";
	}
	
	private void checkShengcx(Ckx_shengcx_lingj bean){
//		String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where biaos='1'and flag='1' and usercenter='"+bean.getUsercenter()+"' and shengcxbh='"+bean.getShengcxbh()+"'";
		//"产线表中不存在整车产线编号："+bean.getShengcxbh()+" 的数据或数据已失效";
//		String mes = GetMessageByKey.getMessage("cxbczzc")+bean.getShengcxbh()+GetMessageByKey.getMessage("sjsx");
//		DBUtilRemove.checkBH(sql, mes);	
		Shengcx shengcx = new Shengcx();
		shengcx.setUsercenter(bean.getUsercenter());
		shengcx.setShengcxbh(bean.getShengcxbh());
		shengcx.setBiaos("1");
		DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("cxbczzc")+bean.getShengcxbh()+GetMessageByKey.getMessage("sjsx"));

	}
	  
	/**
	 * 验证同一用户中心下统一零件的主线辅线有且仅有一条主线，且各辅线级别不能相同
	 * */
	@SuppressWarnings("unchecked")
	private void checkZhuxfx(List<Ckx_shengcx_lingj> insert,
			List<Ckx_shengcx_lingj> edit,
			List<Ckx_shengcx_lingj> delete){
		List<Ckx_shengcx_lingj> listBean = new ArrayList<Ckx_shengcx_lingj>();
		Map<String,String> map = new HashMap<String, String>();
		forByList(insert,listBean,map);
		forByList(edit,listBean,map);
		forByList(delete,listBean,map);
		StringBuffer sb = new StringBuffer();
		for (Ckx_shengcx_lingj bean : listBean) {
			List<Ckx_shengcx_lingj> shengcx_LingjList1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.checkZhuxfx",bean);
			if(0<shengcx_LingjList1.size()){
				//"用户中心:"+bean.getUsercenter()+",零件:"+bean.getLingjbh()+"的各【产线优先级】的级别不能相同\n");
				sb.append(GetMessageByKey.getMessage("yhzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lj")+bean.getLingjbh()+GetMessageByKey.getMessage("dcxyxjdjbbnxt"));
//				throw new ServiceException("同一用户中心同一零件下主线有且仅有一条，且各辅线的级别不能相同 ");
			}
			shengcx_LingjList1.clear();
		}
		if(!"".equals(sb.toString())){
			throw new ServiceException(sb.toString());
		}
		
		
	}
	/**
	 * 过滤（相同零件的，只计算一个）
	 * @param list
	 * @param listBean
	 * @param map
	 */
	public void forByList(List<Ckx_shengcx_lingj> list,List<Ckx_shengcx_lingj> listBean,Map<String,String> map){
		for (Ckx_shengcx_lingj ckx_shengcx_lingj : list) {
			if(!map.containsKey(ckx_shengcx_lingj.getUsercenter()+ckx_shengcx_lingj.getLingjbh())){
				map.put(ckx_shengcx_lingj.getUsercenter()+ckx_shengcx_lingj.getLingjbh(), "");
				listBean.add(ckx_shengcx_lingj);
			}			
		}
	}
	
	/**
	 * 获取分装线
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Fenzx> getFenzxMap(){
		Map<String,Fenzx> FenzxMap=new HashMap<String,Fenzx>();
		try{
			List<Fenzx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryAllFenzxInDDBH");
			//分装线  key：分装线号  value：分装线对象
			for (Fenzx fzx : list) {
				FenzxMap.put(fzx.getFenzxh(), fzx);
			}
		}catch(Exception e){
			//抛出查询失败的异常
			 throw new ServiceException(e.getMessage());
		}
		return FenzxMap;
	}
	
	/**
	 * 获取分装线
	 * @param usercenter	用户中心	
	 * @param xianh			线号
	 * @return				分装线
	 */
	@SuppressWarnings("rawtypes")
	private Fenzx getFenzx(AbstractIBatisDao dao,String usercenter, String xianh){
		Fenzx param = new Fenzx();
		param.setUsercenter(usercenter);
		param.setFenzxh(xianh);
		List list = dao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.getHanzfzxInDDBH",param);
		if(0 == list.size()){
			return null;
		}
		return (Fenzx) list.get(0) ;
	}

	
}

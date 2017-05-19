package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 包装
 * @author denggq
 * 2012-3-19
 */
@Component
public class LingjgcbService {

	@Inject
	private AbstractIBatisDao baseDao;
	@Inject
	private CkxShiwtxService ckxshiwtxService;
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 * @update lc 2016.10.27
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String copy(Lingjgys bean,String userId)throws ServiceException{
		
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjgysBaoz", bean);//修改同一零件供应商的包装信息

		Lingjck lingjck = new Lingjck();//零件仓库
		lingjck.setUsercenter(bean.getUsercenter());//用户中心
		lingjck.setLingjbh(bean.getLingjbh());//零件编号
		
//		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjck",lingjck);
//		if(0 == list.size()){
//			return "复制到零件仓库包装无效，没有零件"+bean.getLingjbh()+"的仓库信息";
//		}
		if(null == bean.getUcbzlx()){
			throw new ServiceException(GetMessageByKey.getMessage("UCbaozhuang"));
		}
		
		if(null == bean.getUcrl()){
			throw new ServiceException(GetMessageByKey.getMessage("UCrongliang"));
		}
		
		if(null == bean.getUabzlx()){
			throw new ServiceException(GetMessageByKey.getMessage("UAbaozhuang"));
		}
		
		if(null == bean.getUaucgs()){
			throw new ServiceException(GetMessageByKey.getMessage("UCgeshu"));
		}
		
		lingjck.setUclx(bean.getUcbzlx());//供应商UC的包装类型  == 上线UC类型
		lingjck.setUcrl(bean.getUcrl());//供应商UC的容量 == 上线UC容量
		lingjck.setUsbzlx(bean.getUabzlx());//供应商UA的包装类型 == 仓库US包装类型
		lingjck.setUsbzrl(bean.getUaucgs()*bean.getUcrl());//仓库US包装容量 == 供应商UA里的UC个数*供应商UC的容量
		
		lingjck.setEditor(userId);
		lingjck.setEdit_time(DateTimeUtil.getAllCurrTime());
		       
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			Cangk cangk =new Cangk();
			cangk.setUsercenter(lingjck.getUsercenter());
			cangk.setWulgyyz(value);
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk",cangk);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("nwcljckzljdsjxgqx",new String[]{lingjck.getLingjbh()}));
			}else{
				lingjck.setWulgyyz(value);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckBaoz1", lingjck);//修改同一零件仓库的包装信息
		}else if("ZBCPOA".equals(key) || "ZXCPOA".equals(key)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckBaoz1", lingjck);//修改同一零件仓库的包装信息
		}else{
			throw new ServiceException(GetMessageByKey.getMessage("nwcljckzljdsjxgqx",new String[]{lingjck.getLingjbh()}));
		}		
		return "copysuccess";
	}
	
	
	/**
	 * 修改零件供应商包装
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public void updateLingjgysBaoz(ArrayList<Lingjgys> edit, String username) throws ServiceException{
		for(Lingjgys bean:edit){
			
			if(null == bean.getUcbzlx()){
				throw new ServiceException(GetMessageByKey.getMessage("UCbaozhuang"));
			}else{
				//供应商UC的包装类型是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getUcbzlx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("UCbaoz")+bean.getUcbzlx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getUcbzlx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("UCbaoz")+bean.getUcbzlx()+GetMessageByKey.getMessage("notexist"));
			}
			
			if(null == bean.getUcrl()){
				throw new ServiceException(GetMessageByKey.getMessage("UCrongliang"));
			}
			
			if(null == bean.getUabzlx()){
				throw new ServiceException(GetMessageByKey.getMessage("UAbaozhuang"));
			}else{
				//供应商UA的包装类型是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getUabzlx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("UAbaoz")+bean.getUabzlx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getUabzlx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("UAbaoz")+bean.getUabzlx()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			if(null == bean.getUaucgs()){//bug 0003272 原来为Uarl 供应商UA里的UC个数*供应商UC的容量算出来的
				throw new ServiceException(GetMessageByKey.getMessage("UCgeshu"));
			}
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
//			if(null == bean.getUabzlx() && )
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjgysBaoz", bean);
			ckxshiwtxService.update(bean.getUsercenter(), CkxShiwtxService.TIXLX_LINGJGYS, bean.getLingjbh(), bean.getGongysbh(), "1");
		}
	}
	
	
	/**
	 * 修改零件仓库包装
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public void updateLingjckBaoz(ArrayList<Lingjck> edit, String username)  throws ServiceException{
		for(Lingjck bean:edit){
			if(null == bean.getUsbzlx()){
				throw new ServiceException(GetMessageByKey.getMessage("USbaozhuang"));
			}else{
				//仓库US包装类型是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getUsbzlx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("USbaoz")+bean.getUsbzlx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getUsbzlx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("USbaoz")+bean.getUsbzlx()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			if(null == bean.getUsbzrl()){
				throw new ServiceException(GetMessageByKey.getMessage("USrongliang"));
			}
			
			if(null == bean.getUclx()){
				throw new ServiceException(GetMessageByKey.getMessage("UCshangxianlx"));
			}else{
				//上线UC类型是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getUclx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("UCsxlx")+bean.getUclx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getUclx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("UCsxlx")+bean.getUclx()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			if(null == bean.getUcrl()){
				throw new ServiceException(GetMessageByKey.getMessage("UCshangxianrongliang"));
			}
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckBaoz", bean);//修改数据库
		}
	}
	
	/**
	 * 根据用户中心+包装
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getBaozsMap(){
		Baoz bz = new Baoz();
		bz.setBiaos("1");
		List<Baoz>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjBaoz",bz);
		Map<String,String> map=new HashMap<String,String>();
		for (Baoz baoz : list) {
			map.put(baoz.getBaozlx(),baoz.getBaozlx());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getLingjsMap(){
		CkxLingj ckxlj = new CkxLingj();
		ckxlj.setBiaos("1");
		List<CkxLingj>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingj",ckxlj);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxLingj lingj : list) {
			map.put(lingj.getUsercenter()+lingj.getLingjbh(),lingj.getUsercenter()+lingj.getLingjbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+供/承/运
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getGongcysMap(){
		Gongcy gcy = new Gongcy();
		gcy.setBiaos("1");
		List<Gongcy>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongcyEXport",gcy);
		Map<String,String> map=new HashMap<String,String>();
		for (Gongcy gongcy : list) {
			map.put(gongcy.getUsercenter()+gongcy.getGcbh(),gongcy.getUsercenter()+gongcy.getGcbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+包装
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getlingjgysMaps(){
		Lingjgys gys = new Lingjgys();
		//gys.setBiaos("1");
		List<Lingjgys>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjgys",gys);
		Map<String,String> map=new HashMap<String,String>();
		for (Lingjgys lingjgys : list) {
			map.put(lingjgys.getUsercenter()+lingjgys.getLingjbh()+lingjgys.getGongysbh(),lingjgys.getUsercenter()+lingjgys.getLingjbh()+lingjgys.getGongysbh());
		}
		return map;
	}
}

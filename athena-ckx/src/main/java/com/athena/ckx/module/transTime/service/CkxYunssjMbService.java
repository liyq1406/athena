package com.athena.ckx.module.transTime.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.transTime.CkxYunssjMBSx;
import com.athena.ckx.entity.transTime.CkxYunssjMb;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.entity.xuqjs.Yicbj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.GetYicbj;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 运输时间模板
 * @author hj
 *
 */

@Component
public class CkxYunssjMbService extends BaseService<CkxYunssjMb> {

	@Inject
	private CkxFrequencyService ckxFrequencyService;//承运商-卸货站台-送货频次
	protected String getNamespace() {
		return "transTime";
	}
	/**
	 * 查询数据（根据参数）表
	 * @param bean
	 * @param id
	 * @return ArrayList<CkxYunssjMb>
	 */
	@SuppressWarnings("unchecked")
	private List<CkxYunssjMb> getList(CkxYunssjMb bean,String id){
		List<CkxYunssjMb> list = (List<CkxYunssjMb>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(id,bean);
		return null==list?new ArrayList<CkxYunssjMb>():list;
	}
	/**
	 * 物理删除运输时间（实际）
	 * @param bean
	 * @return
	 */
	private void removes(CkxYunssjMb bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxYunssjMb", bean);
	}
	/**
	 * 清空表 运输时间（计算）
	 */
	public void removeTemp(CkxYunssjMb bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxYunssjMbTemp",bean);
	}
	/**
	 * 比较同一卸货站台、承运商下，后一个到货时间偏量必须大于前一个到货时间偏量
	 * 比较同一卸货站台、承运商下，后一个发出时间偏量必须大于前一个发出时间偏量
	 * @param list
	 */
	private void compare(List<CkxYunssjMb> list,String type){
		for (CkxYunssjMb ckxYunssjMb : list) {
			List<CkxYunssjMb> listBean = null;
			if("sj".equals(type)){
				listBean = this.list(ckxYunssjMb);
			}else{
				listBean = this.list_Temp(ckxYunssjMb);
			}
			doCompare(listBean);
			listBean.clear();
		}
		
	}
	/**
	 * 比较  到货时间 和发出时间是否满足规则来
	 * @param list
	 */
	private void doCompare(List<CkxYunssjMb> list){
		//保留上一个对象
		CkxYunssjMb ckxYunssjMbTemp = null;
		for (CkxYunssjMb bean : list) {
			// 0008297 到货时间偏量必须大于发出时间偏量
			String message = "卸货站台："+bean.getXiehztbh()+",承运商："+bean.getGcbh()+"---" ;
			int sjpl1 = 0 , sjp2 = 0;
			if(bean.getDaohsj() <= bean.getFacsj() ){
				sjpl1 = bean.getDaohsj().intValue();
				sjp2 = bean.getFacsj().intValue() ;
				message += "到货时间偏量"+sjpl1+"必须大于发出时间偏量"+sjp2 ; 
				throw new ServiceException(message);
			}
			if(null != ckxYunssjMbTemp){
				//同一卸货站台、承运商下
				if(bean.getXiehztbh().equals(ckxYunssjMbTemp.getXiehztbh())
						&& bean.getGcbh().equals(ckxYunssjMbTemp.getGcbh())){
					
					//后一个到货时间偏量必须大于前一个到货时间偏量
					if(bean.getDaohsj() <= ckxYunssjMbTemp.getDaohsj()){
						sjpl1 = bean.getDaohsj().intValue();
						sjp2 =  ckxYunssjMbTemp.getDaohsj().intValue() ;
						message += "到货时间偏量"+sjpl1+"必须大于上一个到货时间偏量"+sjp2 ; 
						throw new ServiceException(message);
//						throw new ServiceException(GetMessageByKey.getMessage("tongyxhztbzdhsjxt"));
					}
					//后一个发出时间偏量必须大于前一个发出时间偏量
					if(bean.getFacsj() <= ckxYunssjMbTemp.getFacsj()){
						sjpl1 = bean.getFacsj().intValue();
						sjp2 =  ckxYunssjMbTemp.getFacsj().intValue() ;
						message += "发出时间偏量"+sjpl1+"必须大于上一个发出时间偏量" +sjp2; 
						throw new ServiceException(message);
//						throw new ServiceException(GetMessageByKey.getMessage("tongyxhztbzfcsjxt"));
					}
				}
			}
			//将这个对象保存，以便于下一个循环使用
			ckxYunssjMbTemp=bean;
		}
		//释放内存
		ckxYunssjMbTemp=null;
	}
	/**
	 * 数据录入 运输时间（计算）
	 */
	public void insertTemp(String usercenter){
		CkxYunssjMb bean = new CkxYunssjMb();
		bean.setUsercenter(usercenter);
		//按照用户中心清除数据
		removeTemp(bean);
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMbTemp",new Date());
//		CkxTempPingjpc ckxTempPingjpc = new CkxTempPingjpc();
//		//获取最小的模拟次数的数据
//		List<CkxTempPingjpc> ckxTempPingjpclist = ckxTempPingjpcService.list(new CkxTempPingjpc());
//		完成运输时间（计算）的数据录入
//		for (CkxTempPingjpc ckxTempPingjpc : ckxTempPingjpclist) {
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMbTemp",ckxTempPingjpc);
//		}
	}
//	/**
//	 * 数据录入 运输时间（计算）
//	 */
//	private String insert_Temp(List<CkxYunssjMb> list,String userID){
//		for (CkxYunssjMb bean : list) {
//			//检测是否存在对应的卸货站台、承运商关系
//			check(bean);
//			//如果没有序号，给序号自动生成数据，若存在，则跳过
//			if(null == bean.getXuh()){
//			    bean.setXuh(getMaxXuh(bean));
//			}
//			bean.setShengcsj(DateTimeUtil.getAllCurrTime().substring(0,19));
//			bean.setCreator(userID);
//			bean.setCreate_time(new Date());
//			bean.setEditor(userID);
//			bean.setEdit_time(new Date());
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMbTempImport", bean);
//		}
//		return "";
//	}
	/**
	 * 得到数据权限内的卸货站台编组
	 * ('','L01X','L02X')
	 */
	@SuppressWarnings("unchecked")
	public String getXiehztbzhs(String usercenter){
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String xiehztbzs = null;
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(usercenter);
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			
		}
		return xiehztbzs;
	}
	/**
	 * 查询运输时间计算（分页）
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> select_Temp(CkxYunssjMb bean) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			String xiehztbzs = "";
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(bean.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			bean.setCreator(xiehztbzs);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("transTime.queryCkxYunssjMbTemp",bean,bean);
	}
	
	@SuppressWarnings("unchecked")
	public List<CkxYunssjMb> query_Temp(CkxCalendarGroup bean) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			String xiehztbzs = "";
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(bean.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			bean.setCreator(xiehztbzs);
		}
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getdistinctxiehztbh",bean);
	}
	/**
	 * 查询运输时间实际（分页）
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> select(CkxYunssjMb bean) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			String xiehztbzs = "";
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(bean.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			bean.setCreator(xiehztbzs);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("transTime.queryCkxYunssjMb",bean,bean);
	}
	/**
	 * 查询运输时间计算
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public List<CkxYunssjMb> list_Temp(CkxYunssjMb bean) throws ServiceException{
		return getList(bean,"transTime.queryCkxYunssjMbTemp");
	}
	/**
	 * 数据导入（运输时间（计算））
	 * @param filePath
	 * @param userID
	 * @return
	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	@Transactional
//	public String ImportAdd(String filePath,String userID)throws Exception{
//		//读取服务器上的文件，将内容存储在list里面
//		FileXls fileXls = new FileXls();
//		List<CkxYunssjMb> list = (List<CkxYunssjMb>) fileXls.read(filePath, CkxYunssjMb.class);
//		//清空表
//		removeTemp(new CkxYunssjMb());
//		//将读取的文件内容录入到数据库里
//		insert_Temp(list,userID);
//		return "success";
//	}
	/**
	 * 修改运输时间计算
	 * @param list
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String edit_Temp(List<CkxYunssjMb> list,String userID)throws ServiceException{
		if(0 == list.size()){
			return "null";
		}
		for (CkxYunssjMb bean : list) {
			bean.setEditor(userID);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxYunssjMbTemp", bean);
		}
		//比较同一卸货站台、承运商下，后一个到货时间偏量、发出时间偏量必须大于前一个到货时间偏量、发出时间偏量
		List<CkxYunssjMb> listBean = new ArrayList<CkxYunssjMb>();
		Map<String,String> map = new HashMap<String, String>();
		forByList(list, listBean, map);
		compare(listBean,"js");
		return "success";
	}
	/**
	 * 查询运输时间（实际）
	 */
	public List<CkxYunssjMb> list(CkxYunssjMb bean) throws ServiceException{
		return getList(bean,"transTime.queryCkxYunssjMb");
	}
	/**
	 * 操作（运输时间（实际））
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return
	 */
	@Transactional
	public String save(List<CkxYunssjMb> insert,
			List<CkxYunssjMb> edit,
			List<CkxYunssjMb> delete,String userID){
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		removes(delete);
		inserts(insert,userID);
		edits(edit,userID);
	
		//比较同一卸货站台、承运商下，后一个到货时间偏量、发出时间偏量必须大于前一个到货时间偏量、发出时间偏量
		List<CkxYunssjMb> ListBean = new ArrayList<CkxYunssjMb>();
		Map<String,String> map = new HashMap<String, String>();
		forByList(insert,ListBean,map);
		forByList(edit,ListBean,map);
		forByList(delete,ListBean,map);
		compare(ListBean,"sj");
		return "success";
	}
	/**
	 * 生效模板
	 * @param userID
	 * @return
	 */
	public String effect(LoginUser loginUser){
		CkxYunssjMb ckxYunssjMb = new CkxYunssjMb();
		List<CkxYunssjMb> list = list_Temp(ckxYunssjMb);
		if(0 < list.size()){
			inserts(loginUser);
		}else{
			throw new ServiceException( GetMessageByKey.getMessage("ysskjsmbwk"));
		}
		return "生效成功";
	}
	
	/**xss-0011489
	 * 运输时刻（手工计算）
	 * @return
	 */
	public String sgjs(LoginUser loginUser){			
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.truncateCkxYunsskTemp");
		//获取运输时刻表并写入数据库
		//0008252  运输时刻算法改变
		addYunsskList2(loginUser);
		return "手工计算成功";
	}
	
	
	/**
	 * 数据录入(运输时间（实际）)
	 * @param list
	 * @param userID
	 * @return
	 */
	private String inserts(List<CkxYunssjMb> list,String userID){
		
		for (CkxYunssjMb bean : list) {
			//0007099新增时验证 数据权限
			String creator = getXiehztbzhs(bean.getUsercenter()); 
			if(creator != null && creator.indexOf(bean.getXiehztbh()) < 0){
				throw new ServiceException("对不起，您没有卸货站台编组："+bean.getXiehztbh()+"的数据权限");
			}
			//检测是否存在对应的卸货站台、承运商关系
			check(bean);	
			//给序号设置
			bean.setXuh(getMaxXuh(bean));
			bean.setShengcsj(DateTimeUtil.getAllCurrTime());
			bean.setCreator(userID);
			bean.setCreate_time(new Date());
			bean.setEditor(userID);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMb", bean);
		}
		return "";
	}
	/**
	 * 数据修改
	 * @param list
	 * @param userID
	 * @return
	 */
	private String edits(List<CkxYunssjMb> list,String userID){
		for (CkxYunssjMb bean : list) {
			bean.setShengcsj(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxYunssjMb", bean);
		}
		return "";
	}
	/**
	 * 数据删除（物理）运输时间（实际）
	 * @param list
	 * @return
	 */
	private String removes(List<CkxYunssjMb> list){
		for (CkxYunssjMb bean : list) {
			removes(bean);
		}
		return "";
	}
   /**
    * 将运输时间（计算）的所有数据导入到运输时间（实际）
    * @return
    */
	@SuppressWarnings("unchecked")
	private String inserts(LoginUser loginuser){
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String xiehztbzs = "";
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(loginuser.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
		}
		// <!-- 0007140 按照用户中心 、和物流工艺员所对应的卸货站台 编组删除数据-->
		Map<String,String> mapValue = new HashMap<String, String>();
		mapValue.put("xiehztbzs", xiehztbzs);
		mapValue.put("usercenter", loginuser.getUsercenter());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.DeleteCkxYunssjMbByTemp",mapValue);
		/*  备用方案2
		List<CkxYunssjMb> list_Temp = list_Temp(new CkxYunssjMb());
		for (CkxYunssjMb ckxYunssjMb : list_Temp) {
			ckxYunssjMb.setShengcsj(DateTimeUtil.getAllCurrTime());		
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("transTime.updateCkxYunssjMb", list_Temp);
		*/
		
		mapValue.put("creator", loginuser.getUsername());
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMbByTemp",mapValue);
		return "";
	}
	/**
	 * 获取序号
	 * @param bean
	 * @return
	 */
	private Double getMaxXuh(CkxYunssjMb bean){
		Object obj =   baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("transTime.getMaxXuh",bean);
		Double xuh = Double.parseDouble(null == obj?"0":obj.toString());
		xuh = null == xuh?0:xuh+1;
		return xuh;
	}
	/**
	 * 验证是否存在卸货站台、承运商相对应关系
	 * 查询需求归集
	 * @param bean
	 */
	private void check(CkxYunssjMb bean){
		if(3 == bean.getGcbh().length()){
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()
//			+"ckx_xiehztbz where usercenter='"+bean.getUsercenter()+"' and  xiehztbzh = '"
//			+bean.getXiehztbh()+"' and biaos='1'" ;
			//"卸货站台编组表中不存在卸货站台编组号："+bean.getXiehztbh()+" 的数据, 或数据已失效";
//			String mes = GetMessageByKey.getMessage("xiehztbzzbczsj",new String[]{bean.getXiehztbh()});
//			DBUtilRemove.checkBH(sql, mes);
			Xiehztbz xiehztbz = new Xiehztbz();
			xiehztbz.setUsercenter(bean.getUsercenter());
			xiehztbz.setXiehztbzh(bean.getXiehztbh());
			xiehztbz.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountXiehztbz", xiehztbz, GetMessageByKey.getMessage("xiehztbzzbczsj",new String[]{bean.getXiehztbh()}));
			
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()
//			+"ckx_cangk where usercenter='"+bean.getUsercenter()+"' and cangkbh = '"
//			+bean.getGcbh()+"' and biaos='1'" ;
			//仓库表中不存在仓库："+bean.getGcbh()+" 的数据, 或数据已失效";
//			String mes1 = GetMessageByKey.getMessage("cangkzbczsj",new String[]{bean.getGcbh()});
//			DBUtilRemove.checkBH(sql1, mes1);
			Cangk ck = new Cangk();
			ck.setUsercenter(bean.getUsercenter());
			ck.setCangkbh(bean.getGcbh());
			ck.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck, GetMessageByKey.getMessage("cangkzbczsj",new String[]{bean.getGcbh()}));
			
		}else{
			CkxGongysChengysXiehzt ckxGongysChengysXiehzt = new CkxGongysChengysXiehzt();
			ckxGongysChengysXiehzt.setXiehztbh(bean.getXiehztbh());
			ckxGongysChengysXiehzt.setGcbh(bean.getGcbh());
			ckxGongysChengysXiehzt.setUsercenter(bean.getUsercenter());
			ckxGongysChengysXiehzt.setBiaos("1");
			if(null == ckxFrequencyService.get(ckxGongysChengysXiehzt)){
				throw new ServiceException(GetMessageByKey.getMessage("bczxhztbzhcysgx"));
			}
		}
	}  
	
	
	/**
	 * 计算运输时刻
	 * 0008252  运输时刻算法改变
	 */
	@SuppressWarnings("unchecked")
	public void addYunsskList3(String userID) throws ServiceException{
		//获取卸货站台集合
		List<CkxYunssjMb> ckxYunssjMblist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getyunsskXiehztGroup");  
		if(0 == ckxYunssjMblist.size()){
			throw new ServiceException(GetMessageByKey.getMessage("notdata"));
		}
		Map<String,String> map = new HashMap<String, String>();
		//循环每个卸货站台和承运商关系
		for (CkxYunssjMb ckxYunssjMb : ckxYunssjMblist) {
			//获取卸货站台所属的物流工艺员
//			Xiehztbz  xiehztbz = (Xiehztbz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiehztbzByBh",ckxYunssjMb.getXiehztbh());	
			Ticxxsj t1 = new Ticxxsj();//剔除休息时间实体类
			t1.setUsercenter(ckxYunssjMb.getUsercenter());//用户中心
			t1.setChanxck(ckxYunssjMb.getXiehztbh());//生产线编号			
			List<Ticxxsj> gongzrList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongzr", t1);//按照用户中心+生产线+工作日获得七天的工作日
			if(0 == gongzrList.size()){
				map.put(ckxYunssjMb.getUsercenter()+","+ckxYunssjMb.getXiehztbh(), ckxYunssjMb.getUsercenter());
				continue;
				//throw new ServiceException(GetMessageByKey.getMessage("time")+xiaohcmb.getShengcxbh()+GetMessageByKey.getMessage("data"));
			}
			//根据卸货站台获取单个卸货站台的承运商的集合
			List<CkxYunssjMb> listGcbh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getyunsskGcbhGroup",ckxYunssjMb);
			new TransTimeService(super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX),t1,listGcbh,gongzrList).execute(userID);//启动单线程
		}
		//记录异常报警
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for(String usercenterChanx:map.keySet()){
			String key[] = usercenterChanx.split(",");
			String exceptionMes = "运输时刻：用户中心：" +key[0]+",卸货站台：" +key[1] +"没有对应的工作日历";
			listYicbc.add(new GetYicbj().getYicbj(map.get(usercenterChanx), "200", "CKX_20",null, exceptionMes,"POA"));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
	}
	
	@SuppressWarnings({ "unchecked" })
	public String addYunsskList(String userID){
		//获取卸货站台集合
		List<CkxYunssjMb> ckxYunssjMblist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getyunsskXiehztGroup");  
		if(0 == ckxYunssjMblist.size()){
			throw new ServiceException(GetMessageByKey.getMessage("notdata"));
		}
		Map<String,String> map = new HashMap<String, String>();
		//循环每个卸货站台和承运商关系
		for (CkxYunssjMb ckxYunssjMb : ckxYunssjMblist) {
			Map<String , List<CkxYunssjMb>> mapBean = null;
			Map<String , List<Ticxxsj>> mapTicxxsj = null;
			Ticxxsj t1 = new Ticxxsj();//剔除休息时间实体类
			t1.setUsercenter(ckxYunssjMb.getUsercenter());//用户中心
			t1.setChanxck(ckxYunssjMb.getXiehztbh());//卸货站台编号			
			List<Ticxxsj> listTicxxsj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTicxxsj", t1);
			if(0 == listTicxxsj.size()){
				map.put(ckxYunssjMb.getUsercenter()+","+ckxYunssjMb.getXiehztbh(), ckxYunssjMb.getUsercenter());
				continue;
				//throw new ServiceException(GetMessageByKey.getMessage("time")+xiaohcmb.getShengcxbh()+GetMessageByKey.getMessage("data"));
			}
			//根据卸货站台获取单个卸货站台的承运商的集合 
			List<CkxYunssjMb> listGcbh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryYunsskGcbh",ckxYunssjMb);
			mapBean = getMapByList(listGcbh);
			//按顺序记录工作日
			List<String> listGongzr = new ArrayList<String>();
			//按照用户中心+卸货站台编组 获得工作日-工作时间的集合
			mapTicxxsj = getGongzrMapByList(listTicxxsj,listGongzr);
			//进行计算
			new TransTimeTreadService(super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX) , mapBean , mapTicxxsj , listGongzr).execute(userID);//启动单线程
		}
		
		//记录异常报警
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for(String usercenterChanx:map.keySet()){
			String key[] = usercenterChanx.split(",");
			String exceptionMes = "运输时刻：用户中心：" +key[0]+",卸货站台：" +key[1] +"没有对应的工作日历";
			listYicbc.add(new GetYicbj().getYicbj(map.get(usercenterChanx), "200", "CKX_20",null, exceptionMes,"POA"));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
		return "success";
	}
	
	/*xss-0011489
	 * 运输时间-手工计算
	 */
	@SuppressWarnings({ "unchecked" })
	public String addYunsskList2(LoginUser loginuser){
		//查询当前用户对应的 物流工艺员组的  所有卸货站台编组集合
		Map<String,String> map3 = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map3);
		String xiehztbzs = "";
		if(1 > key.indexOf("POA")&&!"root".equals(key)){
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map3.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(loginuser.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}	
			
		}
		
		Map<String,String> mapValue2 = new HashMap<String, String>();
		mapValue2.put("xiehztbzs", xiehztbzs.trim());
		mapValue2.put("usercenter", loginuser.getUsercenter());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxYunsskTemp",mapValue2);//删除ckx_yunssk_temp当前用户下的数据
		
		//获取卸货站台集合
		List<CkxYunssjMb> ckxYunssjMblist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getyunsskXiehztGroupTemp",mapValue2);  
		if(0 == ckxYunssjMblist.size()){
			throw new ServiceException(GetMessageByKey.getMessage("没有数据"));
		}
		
		Map<String,String> map = new HashMap<String, String>();
		//循环每个卸货站台和承运商关系
		for (CkxYunssjMb ckxYunssjMb : ckxYunssjMblist) {
			Map<String , List<CkxYunssjMb>> mapBean = null;
			Map<String , List<Ticxxsj>> mapTicxxsj = null;
			Ticxxsj t1 = new Ticxxsj();//剔除休息时间实体类
			t1.setUsercenter(ckxYunssjMb.getUsercenter());//用户中心
			t1.setChanxck(ckxYunssjMb.getXiehztbh());//卸货站台编号			
			List<Ticxxsj> listTicxxsj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTicxxsjSgjs", t1);
			if(0 == listTicxxsj.size()){
				map.put(ckxYunssjMb.getUsercenter()+","+ckxYunssjMb.getXiehztbh(), ckxYunssjMb.getUsercenter());
				continue;
				//throw new ServiceException(GetMessageByKey.getMessage("time")+xiaohcmb.getShengcxbh()+GetMessageByKey.getMessage("data"));
			}
			//根据卸货站台获取单个卸货站台的承运商的集合 
			List<CkxYunssjMb> listGcbh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryYunsskGcbhTemp",ckxYunssjMb);
			mapBean = getMapByList(listGcbh);
			//按顺序记录工作日
			List<String> listGongzr = new ArrayList<String>();
			//按照用户中心+卸货站台编组 获得工作日-工作时间的集合
			mapTicxxsj = getGongzrMapByList(listTicxxsj,listGongzr);
			//进行计算
			new TransTimeTreadService(super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX) , mapBean , mapTicxxsj , listGongzr).execute2(loginuser.getUsername());//启动单线程
		}
		
		//记录异常报警
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for(String usercenterChanx:map.keySet()){
			String key2[] = usercenterChanx.split(",");
			String exceptionMes = "运输时刻-手工计算：用户中心：" +key2[0]+",卸货站台：" +key2[1] +"没有对应的工作日历";
			listYicbc.add(new GetYicbj().getYicbj(map.get(usercenterChanx), "200", "CKX_20",null, exceptionMes,"POA"));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
		return "success";
	}
	
	
	/**
	 * 过滤（相同卸货站台编组的，只计算一个）
	 * @param list
	 * @param listBean
	 * @param map
	 */
	public void forByList(List<CkxYunssjMb> list,List<CkxYunssjMb> listBean,Map<String,String> map){
		for (CkxYunssjMb ckxYunssjMb : list) {
			if(!map.containsKey(ckxYunssjMb.getGcbh()+ckxYunssjMb.getXiehztbh())){
				map.put(ckxYunssjMb.getGcbh()+ckxYunssjMb.getXiehztbh(), "");
				CkxYunssjMb bean = new CkxYunssjMb();
				bean.setXiehztbh(ckxYunssjMb.getXiehztbh());
				bean.setGcbh(ckxYunssjMb.getGcbh());
				listBean.add(bean);
			}			
		}
	}
	/**
	 * 根据承运商转化成承运商-趟次的Map
	 * @param listGcbh
	 */
	public Map<String , List<CkxYunssjMb>> getMapByList(List<CkxYunssjMb> listGcbh){
		Map<String , List<CkxYunssjMb>> mapBean = new HashMap<String, List<CkxYunssjMb>>();
		for (CkxYunssjMb ckxYunssjMb : listGcbh) {
			String key = ckxYunssjMb.getUsercenter()+ckxYunssjMb.getXiehztbh()+ckxYunssjMb.getGcbh();
			List<CkxYunssjMb> list;
			if(mapBean.containsKey(key)){
				list = mapBean.get(key);
			}else{
				list = new ArrayList<CkxYunssjMb>();
			}
			list.add(ckxYunssjMb);
			mapBean.put(key, list);
		}
		return mapBean;
	}
	/**
	 * 根据卸货站台编组转化成 工作日-工作时间的Map
	 * @param listGcbh
	 */
	public Map<String , List<Ticxxsj>> getGongzrMapByList(List<Ticxxsj> listXiehztbz , List<String> listGongzr)
	{
		Map<String , List<Ticxxsj>> mapBean = new HashMap<String, List<Ticxxsj>>();
		
		for (Ticxxsj bean : listXiehztbz)
		{
			String key =  bean.getGongzr();
			List<Ticxxsj> list;
			if(mapBean.containsKey(key)){
				list = mapBean.get(key);
			}else{
				list = new ArrayList<Ticxxsj>();
				listGongzr.add(key);
			}
			if( listGongzr.size() >= 12)
			{
				listGongzr.remove(key);
				break;
			}
			list.add(bean);
			mapBean.put(key, list);
		}
		return mapBean;
	}
	
	/**
	 * 查询定时生效数据
	 */
	public Map<String, Object> queryDingssx(CkxYunssjMBSx bean,LoginUser loginuser) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String xiehztbzs = "";
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(loginuser.getUsercenter());
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
		}
		bean.setXiehztbzh(xiehztbzs);
		bean.setUsercenter(loginuser.getUsercenter());
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("transTime.queryDingssx",bean,bean);
	}
	
	/**
	 * 操作（运输时间（实际））
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return
	 */
	@Transactional
	public String saveDingssx(List<CkxYunssjMBSx> insert,
			List<CkxYunssjMBSx> edit,
			List<CkxYunssjMBSx> delete,String userID,LoginUser loginuser){
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		dingdsxEdits(edit,userID,loginuser);
		return "success";
	}
	
	/**
	 * 数据修改
	 * @param list
	 * @param userID
	 * @return
	 */
	private String dingdsxEdits(List<CkxYunssjMBSx> list,String userID,LoginUser loginuser){
		String xhztbzhs = getXiehztbzhs(loginuser.getUsercenter());
		for (CkxYunssjMBSx bean : list) {
			if(xhztbzhs!= null && !"".equals(xhztbzhs) && 0 > xhztbzhs.indexOf(bean.getXiehztbzh())){
				//不存在此卸货站台编组或没有该卸货站台编组的权限
				throw new ServiceException("用户无卸货站台编组"+bean.getXiehztbzh()+"权限");
			}
			if(bean.getShengxsj()!=null && bean.getShengxsj().length()>0 && dateCompare(bean.getShengxsj())){
				throw new ServiceException("卸货站台编组"+bean.getXiehztbzh()+"设置生效时间无效，必须大于当前日期");
			}
			bean.setEditor(userID);
			bean.setCreator(userID);
			bean.setEdit_time(new Date());
			List<Map<String, String>> xhzt =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.selectYsskMbSxXiehztbz", bean);
			if(xhzt.size() == 0){
				throw new ServiceException("卸货站台编组号"+ bean.getXiehztbzh() +"不存在，或者已经失效，请重新查询后再修改");
			}
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxYunssjMbDingssx", bean);
			if(num==0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjMbDingssx", bean);
			}
		}
		return "success";
	}
	
	public boolean dateCompare(String myString){
		boolean r = false;
		java.util.Date nowdate=new java.util.Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date d;
		try {
			d = sdf.parse(myString);
			r = d.before(nowdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * 得到数据权限内的卸货站台编组
	 * ('','L01X','L02X')
	 */
	@SuppressWarnings("unchecked")
	public String getXiehztbzhsUpdate(String usercenter){
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = "";
		if(null != map.get("WULGYY") && !"".equals((String)map.get("WULGYY"))){//物流工艺员
			key= "WULGYY";
		}
		String xiehztbzs = null;
		if(0 > key.indexOf("WULGYY")){
			xiehztbzs = "";
		}else{		
			Xiehztbz xiehztbz = new Xiehztbz();				
			String value = (String) map.get("WULGYY");
			xiehztbz.setWulgyyz(value);
			xiehztbz.setUsercenter(usercenter);
			xiehztbz.setBiaos("1");
			List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
			xiehztbzs ="('',";
			for (Xiehztbz xiehztbz2 : list) {
				xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
			}
			if(xiehztbzs.endsWith(",")){
				xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
			}				
		}
		return xiehztbzs;
	}
}

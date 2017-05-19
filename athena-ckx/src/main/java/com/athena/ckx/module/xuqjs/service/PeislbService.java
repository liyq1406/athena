package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 配送类别Service
 * @author qizhongtao
 * @date 2012-4-10
 */
@Component
public class PeislbService extends BaseService<Peislb>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-10
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Peislb> insert,ArrayList<Peislb> edit,ArrayList<Peislb> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			edits(edit,userName);
//			deletes(delete,userName);
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-10
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Peislb> insert,String userName)throws ServiceException{
		for (Peislb bean : insert) {
			//包装类型是否存在
			if(null != bean.getBaozlx()){
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getBaozlx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("baozhuangleixing")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getBaozlx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("baozhuangleixing")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			//仓库编号是否存在
			if(null != bean.getCangkbh()){
//				Map map = new HashMap();
//				map.put("cangkbh", bean.getCangkbh());
//				map.put("usercenter", bean.getUsercenter());
//				map.put("peislx", bean.getPeislx());
//				List<Peislb> peislbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.yzckbh", map);
//				if(0==peislbList.size()){
//					throw new ServiceException("该配送类别下零件仓库的仓库编号"+bean.getCangkbh()+"不存在");
//				}
//				String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+AuthorityUtils.getSecurityUser().getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql1,GetMessageByKey.getMessage("cangkubianhao")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
				Cangk cangk = new Cangk();
				cangk.setUsercenter(bean.getUsercenter());
				cangk.setCangkbh(bean.getCangkbh());
				cangk.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", cangk, GetMessageByKey.getMessage("cangkubianhao")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			
			}
			//子仓库编号是否存在
			if(null != bean.getCangkbh() && null != bean.getZickbh()){
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+AuthorityUtils.getSecurityUser().getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh ='"+bean.getZickbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("ck")+bean.getCangkbh()+GetMessageByKey.getMessage("xiazick")+bean.getZickbh()+GetMessageByKey.getMessage("notexist"));
				Zick zick=new Zick();
				zick.setCangkbh(bean.getCangkbh());
				zick.setZickbh(bean.getZickbh());
				zick.setUsercenter(bean.getUsercenter());
				zick.setBiaos("1");
				DBUtil.checkCount(baseDao,"ts_ckx.getCountZick", zick,GetMessageByKey.getMessage("ck")+bean.getCangkbh()+GetMessageByKey.getMessage("xiazick")+bean.getZickbh()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			if("是".equals(bean.getShifgj())){
				bean.setShifgj("1");
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertPeislb", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-10
	 * @param edit,userName
	 * @return ""
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String edits(ArrayList<Peislb> edit,String userName)throws ServiceException{
		for (Peislb bean : edit) {
			if(null != bean.getBaozlx()){
				//包装类型是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getBaozlx()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("baozhuangleixing")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
				Baoz baoz = new Baoz();
				baoz.setBaozlx(bean.getBaozlx());
				baoz.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz, GetMessageByKey.getMessage("baozhuangleixing")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
			
			}
			
			//仓库编号是否存在    mantis编号 0005425: 配送类别页面修改一条记录时，仓库编号、子仓库编号校验
			if(null != bean.getCangkbh()){
				Map map = new HashMap();
				map.put("cangkbh", bean.getCangkbh());
				map.put("usercenter", bean.getUsercenter());
				map.put("peislx", bean.getPeislx());
				int count  = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.yzckbh", map);
				if(0<count){
					throw new ServiceException("配送类型与零件的仓库不对应");
				}
//				String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+AuthorityUtils.getSecurityUser().getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("cangkubianhao")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			}
			//子仓库编号是否存在
			if(null != bean.getCangkbh() && null != bean.getZickbh()){
				Map map = new HashMap();
				map.put("cangkbh", bean.getCangkbh());
				map.put("usercenter", bean.getUsercenter());
				map.put("zickbh", bean.getZickbh());
				map.put("peislx", bean.getPeislx());
				int count  = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.yzzckbh", map);
				if(0<count){
					throw new ServiceException("配送类型与零件的子仓库不对应");
				}
				
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+AuthorityUtils.getSecurityUser().getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and zickbh ='"+bean.getZickbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("ck")+bean.getCangkbh()+GetMessageByKey.getMessage("xiazick")+bean.getZickbh()+GetMessageByKey.getMessage("notexist"));
			}
			
			if("是".equals(bean.getShifgj())){
				bean.setShifgj("1");
			}
			
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			
			Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
			
			String key = GetPostOnly.getPostOnly(map);
			
			if("ZXCPOA".equals(key) || "root".equals(key)){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updatePeislbZxcpoa", bean);
			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updatePeislb", bean);
			}
			
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-10
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Peislb> delete,String userName)throws ServiceException{
		for (Peislb bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deletePeislb", bean);
		}
		return "";
	}
	
	/**
	 * 配送类别关联项校验
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> check(ArrayList<String> list,Peislb peislb){
		//配送类别关联项信息list
		List<HashMap<String, Object>> peislbList = new ArrayList<HashMap<String, Object>>();
		//错误信息list
		List<HashMap<String, Object>> errorList = new ArrayList<HashMap<String, Object>>();
		//参数map
		Map<String, String> map = new HashMap<String, String>();
		if(list.size() == 0){
			map.put("usercenter", peislb.getUsercenter());
			map.put("peislx", peislb.getPeislx());
			map.put("peislxmc", peislb.getPeislxmc());
			map.put("baozlx", peislb.getBaozlx());
			map.put("shangxd", peislb.getShangxd());
			map.put("biaos", peislb.getBiaos());
		}else{
			//将list拼接为字符串
			map.put("condition", convertListToString(list));
		}
		//选择了记录则对指定的配送类别进行校验;没有选择一条记录，就对所有的配送类别进行关联项校验
		peislbList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getGuanlx", map);
		//mantis 0011649 hanwu 20150831 AB点零件会重复报错
		addMos(peislbList);
		for (int i = 0; i < peislbList.size(); i++) {
			HashMap<String, Object> guanlx = peislbList.get(i);
			//判断零件消耗点是否存在
			if(guanlx.get("LJXHD") == null){
				HashMap<String, Object> error = new HashMap<String, Object>();
				error.put("usercenter", guanlx.get("USERCENTER"));
				error.put("peislx", guanlx.get("PEISLX"));
				error.put("shengcxbh", guanlx.get("SHENGCXBH"));
				error.put("lingjbh", guanlx.get("LINGJBH"));
				error.put("nclass", guanlx.get("NCLASS"));
				error.put("errorMsg", "零件消耗点"+(guanlx.get("SHANGXD") == null ? "" : guanlx.get("SHANGXD").toString().substring(0, 9))
						+"不存在！");
				errorList.add(error);
			}
			//判断零件消耗点是否失效
			else if ("0".equals(guanlx.get("XIAOHDBS"))){
				HashMap<String, Object> error = new HashMap<String, Object>();
				error.put("usercenter", guanlx.get("USERCENTER"));
				error.put("peislx", guanlx.get("PEISLX"));
				error.put("shengcxbh", guanlx.get("SHENGCXBH"));
				error.put("lingjbh", guanlx.get("LINGJBH"));
				error.put("nclass", guanlx.get("NCLASS"));
				error.put("errorMsg", "零件消耗点"+guanlx.get("LJXHD")+"已失效！");
				errorList.add(error);
			}
			//判断零件仓库是否存在
			if(guanlx.get("LJCK") == null){
				HashMap<String, Object> error = new HashMap<String, Object>();
				error.put("usercenter", guanlx.get("USERCENTER"));
				error.put("peislx", guanlx.get("PEISLX"));
				error.put("shengcxbh", guanlx.get("SHENGCXBH"));
				error.put("lingjbh", guanlx.get("LINGJBH"));
				error.put("nclass", guanlx.get("NCLASS"));
				error.put("errorMsg", "零件仓库"+guanlx.get("CANGKBH")+"不存在！");
				errorList.add(error);
			}
			//判断零件子仓库是否匹配
			else if(!String.valueOf(guanlx.get("LJZCK")).equals(String.valueOf(guanlx.get("ZICKBH")))){
				HashMap<String, Object> error = new HashMap<String, Object>();
				error.put("usercenter", guanlx.get("USERCENTER"));
				error.put("peislx", guanlx.get("PEISLX"));
				error.put("shengcxbh", guanlx.get("SHENGCXBH"));
				error.put("lingjbh", guanlx.get("LINGJBH"));
				error.put("nclass", guanlx.get("NCLASS"));
				error.put("errorMsg", "配送类别的子仓库"+String.valueOf(guanlx.get("ZICKBH"))
						+"与零件仓库的子仓库不匹配");
				errorList.add(error);
			}
			//判断物流路径SY是否存在
			if(!containsSY(guanlx.get("MOS")) && !containsSY(guanlx.get("MOS2")) &&
					!containsSY(guanlx.get("WAIBMS")) && !containsSY(guanlx.get("JIANGLMS")) &&
					!containsSY(guanlx.get("JIANGLMS2")) && !containsSY(guanlx.get("WJIANGLMS"))){
				HashMap<String, Object> error = new HashMap<String, Object>();
				error.put("usercenter", guanlx.get("USERCENTER"));
				error.put("peislx", guanlx.get("PEISLX"));
				error.put("shengcxbh", guanlx.get("SHENGCXBH"));
				error.put("lingjbh", guanlx.get("LINGJBH"));
				error.put("nclass", guanlx.get("NCLASS"));
				error.put("errorMsg", "分配区为"+
						(guanlx.get("SHANGXD") == null ? "" : guanlx.get("SHANGXD").toString().substring(0, 5))
						+"的物流路径SY不存在！");
				errorList.add(error);
			}
		}
		return errorList;
	}
	
	/**
	 * 把list拼接为 ('','','')形式的字符串
	 * @param list
	 * @return
	 */
	private String convertListToString(List<String> list){
		StringBuilder condition = new StringBuilder();
		condition.append("(");
		for (int i = 0; i < list.size(); i++) {
			condition.append("'").append(list.get(i)).append("'");
			if(i != list.size() - 1){
				condition.append(",");
			}
		}
		condition.append(")");
		return condition.toString();
	}
	
	/**
	 * 将相同的配送类别的模式叠加（即AB点供应商情况）
	 * @param peislbList
	 * @return
	 */
	private void addMos(List<HashMap<String, Object>> peislbList){
		//key:用户中心+上线点+零件编号+供应商	value:配送类别
		HashMap<String, HashMap<String, Object>> temp = new HashMap<String, HashMap<String, Object>>();
		for (HashMap<String, Object> peislb : peislbList) {
			String key = ""+peislb.get("USERCENTER")+peislb.get("LINGJBH")+peislb.get("SHENGCXBH")+peislb.get("NCALSS");
			//供应商不同即AB点供应商
			if(temp.containsKey(key)){
				//获取原有的配送类别，并将模式放进去
				HashMap<String, Object> peislb2= temp.get(key);
				peislb2.put("MOS", peislb2.get("MOS")+","+peislb.get("MOS"));
				peislb2.put("MOS2", peislb2.get("MOS2")+","+peislb.get("MOS2"));
				peislb2.put("WAIBMS", peislb2.get("WAIBMS")+","+peislb.get("WAIBMS"));
				peislb2.put("JIANGLMS", peislb2.get("JIANGLMS")+","+peislb.get("JIANGLMS"));
				peislb2.put("JIANGLMS2", peislb2.get("JIANGLMS2")+","+peislb.get("JIANGLMS2"));
				peislb2.put("WJIANGLMS", peislb2.get("WJIANGLMS")+","+peislb.get("WJIANGLMS"));
			}else{
				temp.put(key, peislb);
			};
		}
		peislbList.clear();
		for (Map.Entry<String, HashMap<String, Object>> entry : temp.entrySet()) {
			peislbList.add(entry.getValue());
		}
	}
	
	/**
	 * 判断字符串是否包含"SY"
	 * @param str
	 * @return
	 */
	private boolean containsSY(Object obj){
		if(obj != null && obj instanceof String){
			String[] moss = ((String)obj).split(",");
			for (String mos : moss) {
				if("SY".equals(mos)){
					return true;
				}
			}
		}
		return false;
	}
}

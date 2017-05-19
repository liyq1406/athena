package com.athena.ckx.module.transTime.service;



import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.transTime.CkxTempMon;
import com.athena.ckx.entity.transTime.CkxYunssjMBSx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 模拟计算
 * @author hj
 *
 */
@Component
public class CkxTempMonService extends BaseService<CkxTempMon> {

	@Inject
	private CkxTempSonghpcService ckxTempSonghpcService;//送货频次
	
	protected String getNamespace() {
		return "transTime";
	}
	/**
	 * 数据操作（模拟计算）
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param jisfs
	 * @param userID
	 * @return
	 * @Transactional
	 */
	@Transactional
	public String save(List<CkxTempMon> insert,		
			List<CkxTempMon> delete,
			String jisfs,LoginUser loginUser,Map<String,String> map){
		if(0==insert.size()&&0==delete.size()){
			return "null";
		}
		map.put("usercenter", loginUser.getUsercenter());
		map.put("dingszt", jisfs);
		map.put("creator", loginUser.getUsername());
		//检查是否存在重复计算卸货站台，和 承运商-卸货站台-送货频次表是否存在此卸货站台
		ckxTempSonghpcService.checkXiehzt(insert,loginUser.getUsercenter());
		inserts(insert,loginUser.getUsername(),jisfs,map);
		checkShifShengxpc(insert);
		//即时计算
		nowJisuan(map);
		return "success";
	}
	/**
	 * 数据录入，插入运输时刻模板计算
	 * @param dingszt
	 */
	private void insertPro_yunssk(Map<String,String> map){	
		checkChulsj(map.get("dingszt"));
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertpro_yunssk",map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteYunssjmbTempJs",map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertYunssjmbTempJs",map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteDingsjspzJs",map);	
	}
	
	/**
	 * 数据录入，插入运输时刻模板计算
	 * @param dingszt
	 */
	public void deletedsjspzJS(Map<String,String> map){	
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteDingsjspzJs",map);	
	}
	/**
	 * 即时计算方法入口
	 * @param insert
	 * @param userID
	 */
	private void nowJisuan(Map<String,String> map){
		//0008693 计算时检测车数必须大于实际频次
		checkChesPinc("2");
		insertPro_yunssk(map);
	}
	/**
	 * 0008693
	 * 计算时检测车数必须大于实际频次
	 * leix :1,为定时，2,为即时
	 */
	@SuppressWarnings("unchecked")
	private void checkChesPinc(String leix){
		CkxTempMon bean = new CkxTempMon();
		bean.setDingszt(leix);
		List<CkxTempMon> list = super.list(bean);
		String mes = "";
		for (CkxTempMon ckxTempMon : list) {
			CkxGongysChengysXiehzt gcx = new CkxGongysChengysXiehzt();
			gcx.setUsercenter(ckxTempMon.getUsercenter());
			gcx.setXiehztbh(ckxTempMon.getXiehztbh());
			gcx.setBiaos("1");
			List<CkxGongysChengysXiehzt> gcbhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.checkCkxGongysChengysXiehzt",gcx);
			int i=0;
			String gcbhs = "" ; 
			for (CkxGongysChengysXiehzt ckxGongysChengysXiehzt : gcbhList) {
				i++;
				gcbhs += ckxGongysChengysXiehzt.getGcbh();
				if(i == 9){
					gcbhs += "...";
					break;
				}
				if(i == gcbhList.size()){
					break;
				}
				gcbhs += ",";
			}
			if(StringUtils.isNotEmpty(gcbhs)){
				mes +="请检查用户中心   "+ gcx.getUsercenter() +"   卸货站台编组    "+ gcx.getXiehztbh() +"   下如下承运商的车次和频次并重新生效：\n" + gcbhs+"\n";
			}
			
		}
		if(StringUtils.isNotEmpty(mes)){
			mes = "卸货站台送货频次的车次不能小于频次。\n" +mes;
			throw new ServiceException(mes);
		}
		
	}
	/**
	 * 检测处理时间问题
	 * @param dingszt
	 */
	@SuppressWarnings("unchecked")
	private void checkChulsj(String dingszt){
		CkxTempMon bean = new CkxTempMon();		
		bean.setDingszt(dingszt);
		List<CkxTempMon> list = super.list(bean);
		for (CkxTempMon ckxTempMon : list) {
//			String sql = "select chulsj from "+DBUtilRemove.getdbSchemal()+
//			"ckx_xiehztbz x inner join (  select gongzsj/sum(ches) clsj ,xiehztbh from "+DBUtilRemove.getdbSchemal()
//			+"ckx_gongys_chengys_xiehzt t  where usercenter='"+ckxTempMon.getUsercenter()+
//			"' and xiehztbh='"+ckxTempMon.getXiehztbh()+"' group by xiehztbh, usercenter,  gongzsj "
//			+"  ) g  on  x.xiehztbzh=g.xiehztbh   where chulsj>g.clsj ";
			//卸货站台编组:"+ckxTempMon.getXiehztbh()+" 的单车所需时间小于处理时间
//			String mes = GetMessageByKey.getMessage("xhztbzdcsjxyclsj",new String []{ckxTempMon.getXiehztbh()});
			String mes = "卸货站台编组:"+ckxTempMon.getXiehztbh()+" 的单车时间小于或等于处理时间";
			checkChulsj(ckxTempMon, mes);
			
		}		  
	}
	/**
	 * 4210定时生效计算
	 */
	@Transactional
	public void dingsjisuan(String creator){
		Map<String,String> map = new HashMap<String, String>();
		map.put("creator", creator);
		dingsShengx(map);
	}
	
	/**
	 * 4210定时生效计算
	 */

	public void dingsShengx(Map<String,String> map){
		String currDate = DateTimeUtil.getCurrDate();
		map.put("shengxsj", currDate);
		List<CkxYunssjMBSx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.selectCkxYunssjmbDSSX", map);
		if(list != null && list.size()>0){
			for(CkxYunssjMBSx bean  : list){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteYunssjmbdssx",bean);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertYunssjmbdssx",bean);	
			}
		}
	}	
	
	
	/**
	 * 数据录入
	 * @param insert
	 * @param userID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String inserts(List<CkxTempMon> insert,String userID,String dingszt,Map<String,String> mapparam){
		Date date = new Date();
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String xiehztbzs = "";
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "NULL";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs +=xiehztbz2.getXiehztbzh()+",";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+"";
				}	
				if("".equals(xiehztbzs)){
					xiehztbzs = "NULL";
				}
			}
		}
		String xhzts ="('',";
		for (CkxTempMon bean : insert) {
			if(!"".equals(xiehztbzs)&&0 > xiehztbzs.indexOf(bean.getXiehztbh())){
				//不存在此卸货站台编组或没有该卸货站台编组的权限
				throw new ServiceException(bean.getXiehztbh()+GetMessageByKey.getMessage("bczcxhztbzhmygqx"));
			}
			Xiehztbz xiehztbz = new Xiehztbz();				
			xiehztbz.setXiehztbzh(bean.getXiehztbh());
			xiehztbz.setUsercenter(bean.getUsercenter());
			xiehztbz.setBiaos("1");
			List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
			if(list.size()==0){
				throw new ServiceException(bean.getXiehztbh()+"卸货站台编组不存在或已失效");
			}
			mapparam.put("xiehztbh", bean.getXiehztbh());
			clearLinssj(mapparam);
			bean.setDingszt(dingszt);
			bean.setCreator(userID);//设置数据创建人
			bean.setCreate_time(date);//设置数据创建时间		
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertTempMon",bean);
			xhzts += "'"+bean.getXiehztbh()+"',";
		}
		mapparam.remove("xiehztbh");
		xhzts = xhzts.substring(0,xhzts.length()-1)+")";
		mapparam.put("xiehztbhs", xhzts);
		return "";
	}
	
	/**
	 * 比较处理时间是否正确（理论卸货站台的处理时间是否大于需要的处理时间）
	 * @param mon
	 * @param mes
	 */
	private void checkChulsj(CkxTempMon mon,String mes){
		Xiehztbz xiehztbz = new Xiehztbz();
		xiehztbz.setUsercenter(mon.getUsercenter());
		xiehztbz.setXiehztbzh(mon.getXiehztbh());
		Object bd =   baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountXiehztbzchulsj",xiehztbz);
		if(null != bd){
			throw new ServiceException(mes);		
		}		
	}
	/**
	 * 清除即时计算的卸货站台编组所在的用户中心的数据
	 * @param usercenter
	 * @param jisfs
	 */
	public void clearLinssj(Map<String,String> mapparam){
		List<CkxTempMon> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.selectCkxTempMonJs", mapparam);
		if(list.size() > 0){
			CkxTempMon mon = list.get(0);
			throw new ServiceException("卸货站台编组"+mon.getXiehztbh() +"已锁定，请等待");
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteTempMon",mapparam);
	}

	@SuppressWarnings("unchecked")
	public void checkShifShengxpc(List<CkxTempMon> insert ){
		String message = "";int i = 0;
		for (CkxTempMon bean : insert) {
			CkxGongysChengysXiehzt x = new CkxGongysChengysXiehzt();
			x.setUsercenter(bean.getUsercenter());
			x.setXiehztbh(bean.getXiehztbh());
			List<CkxGongysChengysXiehzt> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.checkShifShengxpc", x);
			if(0 == list.size()){
				continue;
			}else{
				if(0 == i){
					message += "卸货站台编组：" + list.get(0).getXiehztbh() + "承运商编号：" + list.get(0).getGcbh() ;
					if(list.size()>1){
						message += "...";
					}
				}else{
					if(!message.endsWith("...")){
						message += "...";
					}
				}
			}
			i++;
		}
		if(StringUtils.isNotEmpty(message)){
			message += "的频次未维护，请维护后重新计算";
			throw new ServiceException(message);
		}
	}
	
}

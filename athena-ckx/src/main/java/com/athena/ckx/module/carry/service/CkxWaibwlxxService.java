package com.athena.ckx.module.carry.service;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.entity.carry.CkxWaibwlxx;
import com.athena.ckx.entity.carry.CkxYunswld;
import com.athena.ckx.entity.xuqjs.Gongcy;
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
 * 外部物流详细信息
 * @author kong
 *2012-02-15
 */
@Component
public class CkxWaibwlxxService extends BaseService<CkxWaibwlxx>{
	/**
	 * 获取命名空间
	 */
	@Override
	protected String getNamespace() {
		return "carry";
	}
	/**
	 * 保存物流路径详细
	 * @param bean 实体
	 * @param newlujbh  新路径编号
	 * @param operant 操作标识
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String save(CkxWaibwlxx bean,String newlujbh,Integer operant,LoginUser user){
		//判断路径编号是否填写
		if(null==bean.getLujbh()&&"".equals(newlujbh)){
			throw new ServiceException(GetMessageByKey.getMessage("qignsrljbh"));
		}
		//验证供承编号是否存在
		Gongcy gongys=new Gongcy();
		gongys.setUsercenter(bean.getUsercenter());
		gongys.setGcbh(bean.getGcbh());
		gongys.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountGongys", gongys)){
			//"供承编号" + bean.getGcbh() + "不存在"
			String mes = GetMessageByKey.getMessage("gongysbhbcz",new String[]{ bean.getGcbh()});
			throw new ServiceException(mes);
		}
		
//		
//		if(bean.getGcbh()!=null&&!"".equals(bean.getGcbh())){
//			String sql = "select count(*) from " + DBUtilRemove.getdbSchemal()
//					+ "ckx_gongys where usercenter = '" + bean.getUsercenter()
//					+ "' and gcbh = '" + bean.getGcbh() + "' and leix <> '4' and biaos='1'";
//			//"供承编号" + bean.getGcbh() + "不存在"
//			String mes = GetMessageByKey.getMessage("gongysbhbcz",new String[]{ bean.getGcbh()});
//			DBUtilRemove.checkBH(sql, mes);
//		}
		//验证物理点编号是否存在
		CkxYunswld wuld=new CkxYunswld();
		wuld.setWuldbh(bean.getWuldbh());
		wuld.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"carry.getCountWuld", wuld)){
			//"运输物理点表中不存在物理点编号为："+bean.getWuldbh()+" 的数据";			
			String mes = GetMessageByKey.getMessage("yswldzbczwldbh",new String[]{bean.getWuldbh()});		
			throw new ServiceException(mes);
		}
//		if(null !=bean.getWuldbh()&& !"".equals(bean.getWuldbh()) ){
//			Map<String,String> map2 = new HashMap<String,String>();		
//			//"运输物理点表中不存在物理点编号为："+bean.getWuldbh()+" 的数据";			
//			String mes2 = GetMessageByKey.getMessage("yswldzbczwldbh",new String[]{bean.getWuldbh()});				
//			map2.put("tableName", "ckx_yunswld");
//			map2.put("wuldbh", bean.getWuldbh());
//			map2.put("biaos", "1");
//			DBUtilRemove.checkYN(map2, mes2);
//			map2.clear();
//		}
		List<CkxWaibwlxx> list=null;
		if(null!=bean.getLujbh()&&!"".equals(bean.getLujbh())){
			CkxWaibwlxx cond=new CkxWaibwlxx();
			cond.setLujbh(bean.getLujbh());
			cond.setWuldbh(bean.getWuldbh());
			cond.setUsercenter(user.getUsercenter());
			list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWaibwlxx",cond);
			
		}
		checkYaohlzk(bean);
		if(bean.getXuh()==null){//添加
			
			String no="01";
			if("".equals(newlujbh)){
				//验证物理点编号是否重复
				if(list.size()>0){
					//"在路径编号["+bean.getLujbh()+"]中，物理点编号"+bean.getWuldbh()+"已存在！"
					throw new ServiceException(GetMessageByKey.getMessage("zljbhzwldbhycz",new String[]{bean.getLujbh(),bean.getWuldbh()}));
				}
				//获取当前路径编号下最大序号
				Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuh",bean);
				if(o!=null){//序号累加
					CkxWaibwlxx t=(CkxWaibwlxx) o;//获取对象
					no=t.getXuh();//获取序号
					no=String.valueOf(Integer.valueOf(no)+1);//序号加一
					no=no.length()==1?"0"+no:no;//序号补0
				}
			}else{//添加新编组
				bean.setLujbh(newlujbh);
				list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWaibwlxx",bean);
				if(list.size()>0){
					//"新路径编号["+newlujbh+"]已存在！"
					throw new ServiceException(GetMessageByKey.getMessage("xinljbhycz",new String[]{newlujbh}));
				}
			}
			//如果是新编号则序号为01
			bean.setXuh(no);
			bean.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
			bean.setCreator(user.getUsername());//创建人
			bean.setEditor(user.getUsername());//修改人
			bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
//			try {
				//增加了新的外部物流详细之后 需要判断是否是在同一用户中心的同一路径编码 的基础上新增了一条，如果是新增记录不是唯一，并且填写的 供承编号与原存在的最大的记录不一样，则需要更新
				String maxxuh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuhBywbwl", bean);
				CkxWaibwlxx maxwaibwlxx= (CkxWaibwlxx)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuh", bean);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibwlxx", bean);
				if(null!=maxwaibwlxx){
					if(maxwaibwlxx.getUsercenter().equals(bean.getUsercenter())&& maxwaibwlxx.getLujbh().equals(bean.getLujbh())&& !"01".equals(bean.getXuh())&& !maxxuh.equals(bean.getXuh().substring(1, 2)) && !maxwaibwlxx.getGcbh().equals(bean.getGcbh())){
						//根据用户中心 和 路径编码去更新 外部物流中的 承运商编号
						Map wbwlmap = new HashMap();
						wbwlmap.put("usercenter", bean.getUsercenter());
						wbwlmap.put("lujbh", bean.getLujbh());
						wbwlmap.put("chengysbh", bean.getGcbh());
						wbwlmap.put("editor", user.getUsername());
						wbwlmap.put("editTime", DateTimeUtil.getAllCurrTime());
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxwaibwlbywaibwlxx", wbwlmap);
					}
				}
				return GetMessageByKey.getMessage("addsuccess");
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("addfail")+e.getMessage());
//			}
		}else{//修改
			if(list.size()>0){
				 CkxWaibwlxx beans= list.get(0);
				 //如果数据库中查出的物理点不是自己，则证明物理点编号重复
				 if(!beans.getXuh().equals(bean.getXuh())){
					 //"在路径编号["+bean.getLujbh()+"]中，物理点编号"+bean.getWuldbh()+"已存在！"
					 throw new ServiceException(GetMessageByKey.getMessage("zljbhzwldbhycz",new String[]{bean.getLujbh(),bean.getWuldbh()}));
				 }
			}
			bean.setEditor(user.getUsername());//修改人
			bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
//			try {
				//判断修改的路径编号是不是序号最大的，如果是，则同时需要更新 外部物流中的 承运商编号(更新条件为 用户中心+路径编码)。如果不是，则什么操作都不做
				String maxxuh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuhBywbwl", bean);
				//在次根据 用户中心+路径编码+查询出来的 序号 在去查询对应的以前的记录
				CkxWaibwlxx conds=new CkxWaibwlxx();
				conds.setLujbh(bean.getLujbh());
				conds.setXuh(bean.getXuh());
				//mantis 0011688 hanwu 20150907   查询时取的用户中心应为表单带来的用户中心，而非当前用户所在的用户中心
				//conds.setUsercenter(user.getUsercenter());
				conds.setUsercenter(bean.getUsercenter());
				CkxWaibwlxx xx=(CkxWaibwlxx)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.queryCkxWaibwlxx",conds);
				//当序号是最大并且又修改了 供承编号,则满足条件
					if(bean.getXuh().equals(maxxuh) && !bean.getGcbh().equals(xx.getGcbh())){
						//根据用户中心 和 路径编码去更新 外部物流中的 承运商编号
						Map wbwlmap = new HashMap();
						wbwlmap.put("usercenter", bean.getUsercenter());
						wbwlmap.put("lujbh", bean.getLujbh());
						wbwlmap.put("chengysbh", bean.getGcbh());
						wbwlmap.put("editor", user.getUsername());
						wbwlmap.put("editTime", DateTimeUtil.getAllCurrTime());
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxwaibwlbywaibwlxx", wbwlmap);
					}
			    baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibwlxx", bean);
				return GetMessageByKey.getMessage("savesuccess");
//			} catch (DataAccessException e) {
//				throw new ServiceException(GetMessageByKey.getMessage("savefail")+e.getMessage());
//			}
		}
	}
	/**
	 * 获取路径编号集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CkxWaibwlxx> getSelectLujbhCode(LoginUser user){
		List<CkxWaibwlxx> olist= new ArrayList<CkxWaibwlxx>();
		/* 获取路径编号集合，并放入详细信息中 */
		CkxWaibwlxx cond=new CkxWaibwlxx();
		cond.setUsercenter(user.getUsercenter());
		List<String>list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getSelectLujbhCode",cond);
		//if(list==null||list.size()==0){return null;}
		for (String string : list) {
			CkxWaibwlxx x=new CkxWaibwlxx();
			x.setLujbh(string);
			olist.add(x);//组成物理点详细对象集合
		}
		return olist;
	}
	/**
	 * 物理删除
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String doDelete(CkxWaibwlxx bean){
		CkxWaibwl w=new CkxWaibwl();
		w.setLujbh(bean.getLujbh());//路径编号
		List<CkxWaibwl> list=super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWaibwl", w);
		//检查当前路径编号是否被外部物流路径使用
//		try {
			if(list.size()>0){
				//"路径编号【"+bean.getLujbh()+"】被使用，无法删除！"
				throw new ServiceException(GetMessageByKey.getMessage("ljbhbsywfsc",new String[]{bean.getLujbh()}));
			}
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteCkxWaibwlxx", bean);
			//获取上一个物理点对象集合并修改序号
			List<CkxWaibwlxx> afterlist=super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getAfterCkxWaibwlxx", bean);
			for (CkxWaibwlxx ckxWaibwlxx : afterlist) {
				//更新后的序号 
				Integer xuh=Integer.valueOf(ckxWaibwlxx.getXuh())-1;//所有序号减一
				ckxWaibwlxx.setNewXuh( xuh.toString());//序号
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxWaibwlxxForXuh", ckxWaibwlxx);
			}
			return GetMessageByKey.getMessage("deletesuccess");
//		} catch (Exception e) {
//			throw new ServiceException(GetMessageByKey.getMessage("deletefail")+e.getMessage());
//		}
	}
	public CkxWaibwlxx getWaibwlxxMaxXuh(CkxWaibwlxx bean){
		return (CkxWaibwlxx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuh", bean);
	}
//	public CkxWaibwlxx getFastWuldByLujbh(CkxWaibwlxx bean){
//		return (CkxWaibwlxx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getFastWuldByLujbh", bean);
//	}
	/**
	 * 同一路径的同一承运商 ,最多有一个要货令展开为[是]
	 */
	@SuppressWarnings("unchecked")
	public void checkYaohlzk(CkxWaibwlxx obj){
		if(!"1".equals(obj.getYaohlzkbs())){
			return ;
		}
		CkxWaibwlxx bean = new CkxWaibwlxx();
		bean.setUsercenter(obj.getUsercenter());
		bean.setLujbh(obj.getLujbh());
		bean.setGcbh(obj.getGcbh());
		bean.setYaohlzkbs("1");		
		List<CkxWaibwlxx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWaibwlxx",bean);
		int count = 0;
		  //如果修改的数据与查询的数据一致,则可以通过
		if(null != obj.getXuh()&& 1 == list.size() && list.get(0).getXuh().equals(obj.getXuh())){			
			count = 1;		
		}
		if(count < list.size()){
			throw new ServiceException("同一路径"+bean.getLujbh()+"的同一承运商"+bean.getGcbh()+" ,最多有一个要货令展开为[是]");
		}
		
	}
	
}
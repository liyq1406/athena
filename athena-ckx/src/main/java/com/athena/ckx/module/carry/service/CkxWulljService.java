package com.athena.ckx.module.carry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxShengcxXianb;
import com.athena.ckx.entity.carry.CkxWaibms;
import com.athena.ckx.entity.carry.CkxWullj;
import com.athena.ckx.module.xuqjs.service.UsercenterService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 物流路径总图 业务逻辑层
 * @author kong
 *
 */
@Component
public class CkxWulljService extends BaseService<CkxWullj>{
	
	static Logger log = Logger.getLogger(CkxWulljService.class);
	
	@Override
	protected String getNamespace() {
		return "carry";
	}
	/**
	 * 分页查询物流路径临时总图
	 * @param bean
	 * @return
	 */
	public Map<String,Object> selectTemp(CkxWullj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("carry.queryCkxWulljTemp", bean,bean);
	}
	/**
	 * 查询物流路径临时总图
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CkxWullj> listTemp(CkxWullj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWulljTemp", bean);
	}
	
	/**
	 * 将物流路径写入数据表(定时任务)
	 */
	@Transactional
	public void addWullj(String userName){
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", userName);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteWullj");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.addWullj",map);
		//检查数据，如果模式中出现C0，C1，CD，M1，MD模式，则修改对应的零件-消耗点表字段pdsbz为‘C’	
		//按需新增 CV、MV模式	 mantis:0012590 by hanwu 20160318
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdForCarry",map);
		//检查数据，如果模式中不出现C0，C1，CD，M1，MD模式，则修改对应的零件-消耗点表字段pdsbz为‘’
		//按需新增 CV、MV模式	 mantis:0012590 by hanwu 20160318
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdForCarry1",map);
		
		//在生成虚拟物流路径之前，先对之前存在的所有虚拟路径清空处理  ckx_waibms、ckx_shengcx_xianb、ckx_wullj
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteXulwulljbywaibms");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteXulwulljbyshengcxxb");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteXulwulljbywullj");
		//对于周期零件不齐的内部物流信息和物流路径信息进行补充 只要循环为空的  都补充
		List<CkxWullj> listinsert = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryWulljscxxb");
		if(listinsert.size()!=0){
			for(CkxWullj ckxwullj : listinsert){
				//每次遍历的时候先根据用户中心\供应商编号\零件编号去 在物流路径中心去查询,如果存在记录 则跳出循环.
				CkxWullj wullj = new CkxWullj();
				wullj.setUsercenter(ckxwullj.getUsercenter());
				wullj.setLingjbh(ckxwullj.getLingjbh());
				wullj.setGongysbh(ckxwullj.getGcbh());
				List<CkxWullj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxWullj",wullj);
				if(list.size()!=0){
					continue;
				}
			     //在根据供货类型来判断插入的模式为 PP或SG	
				if("97X".equals(ckxwullj.getGonghlx())||"97D".equals(ckxwullj.getGonghlx())){
					//ckx_waibms 插入数据
					CkxWaibms ckxwaibms = new  CkxWaibms();
					ckxwaibms.setUsercenter(ckxwullj.getUsercenter());
					ckxwaibms.setLingjbh(ckxwullj.getLingjbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					} //xh 2015-10-19 VD用户中心新增虚拟物流路径
					ckxwaibms.setMos("PP");
					ckxwaibms.setCreator("4030");
					ckxwaibms.setCreateTime(DateTimeUtil.getAllCurrTime());
					try{
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibms",ckxwaibms);
					}catch (DataAccessException e) {
						//同一个用户中心、同一零件、同一个供货类型 插入外部模式的时候  会出现主键冲突  所以把报出的错误用CATCH捕捉 并记录
						insertwullj(ckxwullj);
						log.info(e.getMessage());
					}
					//CKX_SHENGCX_XIANB插入数据
					CkxShengcxXianb ckxShengcxXianb = new CkxShengcxXianb();
					ckxShengcxXianb.setUsercenter(ckxwullj.getUsercenter());
					ckxShengcxXianb.setLingjbh(ckxwullj.getLingjbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					}
					ckxShengcxXianb.setQidlx("");//
					ckxShengcxXianb.setMos("SG");
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("UWXLX");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("UXXLX");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("ULXLX");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("VDXLX");
					}
					ckxShengcxXianb.setQid(ckxwullj.getMudd());
					ckxShengcxXianb.setCreator("4030");
					ckxShengcxXianb.setCreateTime(DateTimeUtil.getAllCurrTime());
					try{
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxShengcxXianb",ckxShengcxXianb);
					}catch (DataAccessException e) {
						//同一个用户中心、同一零件、同一个供货类型 插入生产线-线边的的时候  会出现主键冲突  所以把报出的错误用CATCH捕捉 并记录
						log.info(e.getMessage());
					}
					//ckx_wullj 插入数据
					CkxWullj ckxwulljs = new CkxWullj();
					ckxwulljs.setUsercenter(ckxwullj.getUsercenter());
					ckxwulljs.setLingjbh(ckxwullj.getLingjbh());
					ckxwulljs.setGongysbh(ckxwullj.getGcbh());
					ckxwulljs.setLujbh(ckxwullj.getLujbh());
					ckxwulljs.setLujmc(ckxwullj.getLujmc());
					ckxwulljs.setFahd(ckxwullj.getFahd());
					ckxwulljs.setJiaofm(ckxwullj.getJiaofm());
					ckxwulljs.setBeihzq(ckxwullj.getBeihzq());
					ckxwulljs.setYunszq(ckxwullj.getPanysj());
					ckxwulljs.setZhizlx(ckxwullj.getGonghlx());
					ckxwulljs.setWaibms("PP");
					ckxwulljs.setMos("SG");
					ckxwulljs.setMudd(ckxwullj.getMudd());
					ckxwulljs.setGongyfe(ckxwullj.getGongyfe());
					ckxwulljs.setGcbh(ckxwullj.getChengysbh());
					ckxwulljs.setXianbck(ckxwullj.getCangkbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					}
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("UWXLX");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("UXXLX");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("ULXLX");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("VDXLX");
					}
					ckxwulljs.setXiehztbh(ckxwullj.getXiehztbh());
					ckxwulljs.setCreator("4030");
					ckxwulljs.setCreateTime(DateTimeUtil.getAllCurrTime());
				try{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertWulljshengcx",ckxwulljs);
				}catch (DataAccessException e){
					log.info(e.getMessage());
				}
			}else{
					//ckx_waibms 插入数据
					CkxWaibms ckxwaibms = new  CkxWaibms();
					ckxwaibms.setUsercenter(ckxwullj.getUsercenter());
					ckxwaibms.setLingjbh(ckxwullj.getLingjbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwaibms.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					}
					ckxwaibms.setMos("SG");
					ckxwaibms.setCreator("4030");
					ckxwaibms.setCreateTime(DateTimeUtil.getAllCurrTime());
				try{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxWaibms",ckxwaibms);
				}catch (DataAccessException e) {
					//同一个用户中心、同一零件、同一个供货类型 插入外部模式的时候  会出现主键冲突  所以把报出的错误用CATCH捕捉 并记录
					insertwullj(ckxwullj);
					log.info(e.getMessage());
				}
					//CKX_SHENGCX_XIANB插入数据
					CkxShengcxXianb ckxShengcxXianb = new CkxShengcxXianb();
					ckxShengcxXianb.setUsercenter(ckxwullj.getUsercenter());
					ckxShengcxXianb.setLingjbh(ckxwullj.getLingjbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					}
					ckxShengcxXianb.setQidlx("");//
					ckxShengcxXianb.setMos("SG");
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("UWXLX");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("UXXLX");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("ULXLX");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxShengcxXianb.setShengcxbh("VDXLX");
					}
					ckxShengcxXianb.setQid(ckxwullj.getMudd());
					ckxShengcxXianb.setCreator("4030");
					ckxShengcxXianb.setCreateTime(DateTimeUtil.getAllCurrTime());
				try{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxShengcxXianb",ckxShengcxXianb);
				}catch (DataAccessException e) {
					//同一个用户中心、同一零件、同一个供货类型 插入生产线-线边的的时候  会出现主键冲突  所以把报出的错误用CATCH捕捉 并记录
					log.info(e.getMessage());
				}
					//ckx_wullj 插入数据
					CkxWullj ckxwulljs = new CkxWullj();
					ckxwulljs.setUsercenter(ckxwullj.getUsercenter());
					ckxwulljs.setLingjbh(ckxwullj.getLingjbh());
					ckxwulljs.setGongysbh(ckxwullj.getGcbh());
					ckxwulljs.setLujbh(ckxwullj.getLujbh());
					ckxwulljs.setLujmc(ckxwullj.getLujmc());
					ckxwulljs.setFahd(ckxwullj.getFahd());
					ckxwulljs.setJiaofm(ckxwullj.getJiaofm());
					ckxwulljs.setBeihzq(ckxwullj.getBeihzq());
					ckxwulljs.setYunszq(ckxwullj.getPanysj());
					ckxwulljs.setZhizlx(ckxwullj.getGonghlx());
					ckxwulljs.setWaibms("SG");
					ckxwulljs.setMos("SG");
					ckxwulljs.setMudd(ckxwullj.getMudd());
					ckxwulljs.setGongyfe(ckxwullj.getGongyfe());
					ckxwulljs.setGcbh(ckxwullj.getChengysbh());
					ckxwulljs.setXianbck(ckxwullj.getCangkbh());
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
					}
					if("UW".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("UWXLX");
					}else if("UX".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("UXXLX");
					}else if("UL".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("ULXLX");
					}else if("VD".equals(ckxwullj.getUsercenter())){
						ckxwulljs.setShengcxbh("VDXLX");
					}
					ckxwulljs.setXiehztbh(ckxwullj.getXiehztbh());
					ckxwulljs.setCreator("4030");
					ckxwulljs.setCreateTime(DateTimeUtil.getAllCurrTime());
					try{
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertWulljshengcx",ckxwulljs);
					}catch (DataAccessException e){
						log.info(e.getMessage());
					}
					
				}
			}
		}
	}
	
	
	//插入CKX_WULLJ
	public void insertwullj(CkxWullj ckxwullj) throws ServiceException{
		//ckx_wullj 插入数据
		CkxWullj ckxwulljs = new CkxWullj();
		ckxwulljs.setUsercenter(ckxwullj.getUsercenter());
		ckxwulljs.setLingjbh(ckxwullj.getLingjbh());
		ckxwulljs.setGongysbh(ckxwullj.getGcbh());
		ckxwulljs.setLujbh(ckxwullj.getLujbh());
		ckxwulljs.setLujmc(ckxwullj.getLujmc());
		ckxwulljs.setFahd(ckxwullj.getFahd());
		ckxwulljs.setJiaofm(ckxwullj.getJiaofm());
		ckxwulljs.setBeihzq(ckxwullj.getBeihzq());
		ckxwulljs.setYunszq(ckxwullj.getPanysj());
		ckxwulljs.setZhizlx(ckxwullj.getGonghlx());
		ckxwulljs.setWaibms("SG");
		ckxwulljs.setMos("SG");
		ckxwulljs.setMudd(ckxwullj.getMudd());
		ckxwulljs.setGongyfe(ckxwullj.getGongyfe());
		ckxwulljs.setGcbh(ckxwullj.getChengysbh());
		ckxwulljs.setXianbck(ckxwullj.getCangkbh());
		if("UW".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setFenpqh("W"+ckxwullj.getGonghlx()+"X");
		}else if("UX".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setFenpqh("X"+ckxwullj.getGonghlx()+"X");
		}else if("UL".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setFenpqh("L"+ckxwullj.getGonghlx()+"X");
		}else if("VD".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setFenpqh("D"+ckxwullj.getGonghlx()+"X");
		}
		if("UW".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setShengcxbh("UWXLX");
		}else if("UX".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setShengcxbh("UXXLX");
		}else if("UL".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setShengcxbh("ULXLX");
		}else if("VD".equals(ckxwullj.getUsercenter())){
			ckxwulljs.setShengcxbh("VDXLX");
		}
		ckxwulljs.setXiehztbh(ckxwullj.getXiehztbh());
		ckxwulljs.setCreator("4030");
		ckxwulljs.setCreateTime(DateTimeUtil.getAllCurrTime());
		try{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertWulljshengcx",ckxwulljs);
		}catch (DataAccessException e){
			log.info(e.getMessage());
		}
	}
	
	
	
	
	/**
	 * 增加模板数据
	 * @param user
	 */
	@Transactional
	public void addWulljTemp(LoginUser user){
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", user.getUsername());
		map.put("createTime", DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteWulljTemp");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.addWulljTemp",map);
	}
	
	
	/**
	 * 生效模板
	 */

	@Transactional
	public void effectiveWullj(LoginUser user){
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", user.getUsername());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.deleteWullj");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.effectiveWullj",map);
		
		//检查数据，如果模式中出现C0，C1，CD，M1，MD模式，则修改对应的零件-消耗点表字段pdsbz为‘C’
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdForCarry",map);
		//检查数据，如果模式中不出现C0，C1，CD，M1，MD模式，则修改对应的零件-消耗点表字段pdsbz为‘’
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdForCarry1",map);

	}
	
	/**
	 * 非主键参数检查
	 * @param bean 主键查询条件
	 * @return
	 */
	public String checkWulljTemp(CkxWullj bean){
		//bean.setGongysbh(bean.getGongysbh().replace("  ", "  "));
		String mes=(String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.checkTemp" ,bean);
		String result="参数齐全";
		if(null==mes){ return result; }
		
		
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("usercenter", "缺失通用零件");
		map.put("gongysbh", "缺失零件供应商");
		map.put("xianbck", "缺失零件仓库关系");
		map.put("wulgyyz1", "缺失通用仓库");
		map.put("lujbh", "缺失外部物流路径");
		map.put("xiehztbh", "缺失卸货站台");
		String[] sList=mes.split(",");
		
		StringBuffer rt=new StringBuffer();
		for (int i = 1; i < sList.length; i++) {
			rt.append(map.get(sList[i]));
			rt.append("|");
		}
		return rt.toString();
	}
	/**
	 * 检测消耗点|仓库 -零件对应的数据的模式是否为看板
	 * @param usercenter
	 * @param lingjbh
	 * @param bh
	 * @param type
	 * @return
	 */
	public boolean checkWulljXhdCk(String usercenter,String lingjbh,String bh,String type){
		CkxWullj bean = new CkxWullj();
		bean.setUsercenter(usercenter);
		bean.setLingjbh(lingjbh);
		String message = "" ;
		if("X".equals(type)){//如果为X，则为消耗点
			//取消耗点的前5位为分配区
			bean.setFenpqh(bh.substring(0,bh.length()-4));
			//取用户中心+零件+分配区 能得到唯一模式
			bean = (CkxWullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.checkXiaohdmssfkb" ,bean);
			message = "消耗点:"+bh+"还未生成物流路径" ;
		}else if("C".equals(type)){//如果为C,则为仓库
			bean.setDinghck(bh);
			bean.setXianbck(bh);
			//取用户中心+零件+订货库|线边库  能得到唯一模式
			//零件仓库替换，根据零件-仓库找物流路径中线边库和订货库，
			//如为二级供货（如R2+RD），则找线边库对应的外部模式，判断是否为看板；
			//如为三级供货（如PP+RM+RD），则找到线边库就取中间模式，判断是否为看板，
			                          //找到订货库就取外部模式判断是否看板。 
			try {
				bean = (CkxWullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.checkcangkmssfkb" ,bean);
			} catch (DataAccessException e) {
				//查询的数据有可能返回多条 所以把报出的错误用CATCH捕捉 然后转换为其他的提示抛出
				throw new ServiceException("仓库在物流路径中只能为线边库或订货库");
			}
			message = "仓库:"+bh+"还未生成物流路径" ;
		}
		if(bean == null){
			throw new ServiceException(message);
		}
		if(bean.getMos().startsWith("R")){
			return true;
		}
		return false;
	}
	/**
	 * 修改零件消耗点
	 * @param list 物流路径总图集合
	 */
//	public void editLingjxhd(List<CkxWullj> list){
//		for (CkxWullj ckxWullj : list) {
//			//如果这条路径中有C模式的运输路径，则对应
//			if(ckxWullj.getMos()!=null&&!"".equals(ckxWullj.getMos())&&ckxWullj.getMos().toUpperCase().indexOf("C")==0
//				||ckxWullj.getMos2()!=null&&!"".equals(ckxWullj.getMos2())&&ckxWullj.getMos2().toUpperCase().indexOf("C")==0
//				||ckxWullj.getWaibms()!=null&&!"".equals(ckxWullj.getWaibms())&&ckxWullj.getWaibms().toUpperCase().indexOf("C")==0){
//				//修改零件消耗点(主键：用户中心，零件，消耗点)
//				CkxLingjxhd  cond=new CkxLingjxhd();
//				cond.setUsercenter(ckxWullj.getUsercenter());
//				cond.setLingjbh(ckxWullj.getLingjbh());
////				cond.setXiaohdbh(ckxWullj.getx)
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("getCkxLingjxhd", cond);
//				
//				
//			}
//		}
//	}
	
	
	  
//	/**
//	 * 从各个物流路径表中汇总一张物流路径总表
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<CkxWullj> getWullj(){
//		List<Map<String,Object>> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getWullj");
//		List<CkxWullj> wList=new ArrayList<CkxWullj>();
//		for (Map<String, Object> map : list) {
//			CkxWullj w=new CkxWullj();
//			w.setUsercenter(map.get("USERCENTER")!=null?map.get("USERCENTER").toString():"");//用户中心
//			w.setLingjbh(map.get("LINGJBH")!=null?map.get("LINGJBH").toString():"");//零件编号
//			w.setLujbh(map.get("LUJBH")!=null?map.get("LUJBH").toString():"");//路径编号
//			w.setLujmc(map.get("LUJMC")!=null?map.get("LUJMC").toString():"");//路径名称
//			w.setGongysbh(map.get("GONGYSBH")!=null?map.get("GONGYSBH").toString():"");//供应商编号
//			w.setFahd(map.get("FAHD")!=null?map.get("FAHD").toString():"");//发货地
//			w.setJiaofm(map.get("JIAOFM")!=null?map.get("JIAOFM").toString():"");//交付码
//			w.setZhizlx(map.get("GONGHLX")!=null?map.get("GONGHLX").toString():"");//供货类型（制造路线）
//			w.setBeihzq(Double.valueOf(map.get("BEIHZQ")!=null?map.get("BEIHZQ").toString():"0"));//备货周期
//			w.setYunszq(Double.valueOf(map.get("YUNSZQ")!=null?map.get("YUNSZQ").toString():"0"));//运输周期
//			w.setWaibms(map.get("WMS")!=null?map.get("WMS").toString():"");//外部模式
//			w.setZhidgys(map.get("ZHIDGYS")!=null?map.get("ZHIDGYS").toString():"");//指定供应商
//			w.setXiehztbh(map.get("XIEHZTBH")!=null?map.get("XIEHZTBH").toString():"");//卸货站台编号
//			w.setGcbh(map.get("GCBH")!=null?map.get("GCBH").toString():"");//承运商编号
//			w.setSonghpc(Integer.valueOf(map.get("SHIJPC")!=null?map.get("SHIJPC").toString():"0"));//送货频次
//			w.setMudd(map.get("MUDD")!=null?map.get("MUDD").toString():"");//目的地
//			w.setDinghck(map.get("DINGHCK")!=null?map.get("DINGHCK").toString():"");//订货库
//			w.setMos2(map.get("MOS2")!=null?map.get("MOS2").toString():"");//模式
//			w.setJianglms2(map.get("JIANGLMS2")!=null?map.get("JIANGLMS2").toString():"");//将来模式2
//			w.setShengxsj2(map.get("JIANGLMSSXSJ2")!=null?map.get("JIANGLMSSXSJ2").toString():"");//将来模式生效时间2
//			w.setCangkshpc2(Integer.valueOf(map.get("CANGKSHPC2")!=null?map.get("CANGKSHPC2").toString():"0"));//仓库送货频次2
//			w.setCangkshsj2(Double.valueOf(map.get("CANGKSHSJ2")!=null?map.get("CANGKSHSJ2").toString():"0"));//仓库送货时间
//			w.setCangkfhsj2(Double.valueOf(map.get("CANGKFHSJ2")!=null?map.get("CANGKFHSJ2").toString():"0"));//仓库返回时间
//			w.setBeihsj2(Double.valueOf(map.get("BEIHSJ2")!=null?map.get("BEIHSJ2").toString():"0"));//备货时间
//			w.setIbeihsj2(Double.valueOf(map.get("IBEIHSJ2")!=null?map.get("IBEIHSJ2").toString():"0"));//I备货时间
//			w.setPbeihsj2(Double.valueOf(map.get("PBEIHSJ2")!=null?map.get("PBEIHSJ2").toString():"0"));//P备货时间
//			w.setXianbck(map.get("XIANBCK")!=null?map.get("XIANBCK").toString():"");//线边库
//			w.setMos(map.get("MOS")!=null?map.get("MOS").toString():"");//模式
//			w.setJianglms(map.get("JIANGLMS")!=null?map.get("JIANGLMS").toString():"");//将来模式2
//			w.setShengxsj(map.get("JIANGLMSSXSJ")!=null?map.get("JIANGLMSSXSJ").toString():"");//将来模式生效时间2
//			w.setCangkshpc(Integer.valueOf(map.get("CANGKSHPCF")!=null?map.get("CANGKSHPCF").toString():"0"));//仓库送货频次
//			w.setCangkshsj(Double.valueOf(map.get("CANGKSHSJ")!=null?map.get("CANGKSHSJ").toString():"0"));//仓库送货时间
//			w.setCangkfhsj(Double.valueOf(map.get("CANGKFHSJ")!=null?map.get("CANGKFHSJ").toString():"0"));//仓库返回时间
//			w.setBeihsj(Double.valueOf(map.get("BEIHSJ")!=null?map.get("BEIHSJ").toString():"0"));//备货时间
//			w.setIbeihsj(Double.valueOf(map.get("IBEIHSJ")!=null?map.get("IBEIHSJ").toString():"0"));//I备货时间
//			w.setPbeihsj(Double.valueOf(map.get("PBEIHSJ")!=null?map.get("PBEIHSJ").toString():"0"));//P备货时间
//			w.setFenpqh(map.get("FENPQH")!=null?map.get("FENPQH").toString():"");//分配区
//			w.setShengcxbh(map.get("SHENGCXBH")!=null?map.get("SHENGCXBH").toString():"");//生产线编号
//			w.setBeihsjc(Double.valueOf(map.get("BEIHSJC")!=null?map.get("BEIHSJC").toString():"0"));//备货时间C(仓库-卸货站台备货时间)
//			wList.add(w);
//		}
//		return wList;
//	}
}
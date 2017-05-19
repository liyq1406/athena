package com.athena.xqjs.module.denglswtx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.common.CkxGongyxhd;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.denglswtx.Denglswtx;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * 登录事务提醒
 * @author dsimedd001
 *
 */
@WebService(endpointInterface="com.athena.xqjs.module.denglswtx.service.DenglswtxInter", serviceName="/denglswtxService")
@Component
public class DenglswtxService extends BaseService implements DenglswtxInter {

	/**
	 * 根据计算模块查询最近一次计算产生的异常报警数量
	 * @param jismk 计算模块代码
	 * @return 异常报警数量
	 */
	public String queryYicbj(String jismk,String usercenter){
		Map<String,String> param = new HashMap<String, String>();
		param.put("jismk", jismk);//计算模块
		param.put("usercenter", usercenter);//用户中心
		if(jismk.equals(Const.JISMK_ANX_CD)){//按需模块异常报警数量汇总
			if(Const.WTC_CENTER_UW.equals(usercenter)){//用户中心UW
				param.put("dingdh", Const.ANX_UW_DINGDH);//根据UW用户中心订单号查询上次按需计算时间
			}else if(Const.WTC_CENTER_UL.equals(usercenter)){//用户中心UL
				param.put("dingdh", Const.ANX_UL_DINGDH);//根据UL用户中心订单号查询上次按需计算时间
			}else{//UW和UL用户中心之外没有按需,返回数量0
				return "0";
			}
			//查询按需上次计算时间,上次计算时间查询不到返回0
			Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",param);
			if(dingd != null){
				if(!StringUtils.isEmpty(dingd.getDingdsxsj())){
					//上次计算时间
					param.put("jissj", dingd.getDingdsxsj().substring(0,10));//上次计算时间
				}else{
					return "0";
				}
			}else{
				return "0";
			}
		}else{
			param.put("jissj", CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryKanbjssj")));//上次计算时间
		}
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryYicbj",param));
	}
	
	/**
	 * 解决事物提醒
	 * @param edit 参数
	 * @return 结果
	 */
	public int jiejCkxShiwtx(ArrayList<Denglswtx> edit){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("denglswtx.jiejCkxShiwtx",edit);
	}
	
	/**
	 * 查询登录事务提醒
	 * @param page 查询对象
	 * @param param 查询参数
	 * @return 登录事务提醒信息集合
	 */
	public Map queryCkxShiwtx(PageableSupport page, Map<String, String> param){
		if("exportXls".equals(param.get("exportXls"))){
			return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryCkxShiwtxXls",param));
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("denglswtx.queryCkxShiwtx",param,page);
		}
	}
		
	/**
	 * 保存参考系事务提醒
	 */
	public void insertCkxShiwtx(){
		List<Denglswtx> listDenglswtx = new ArrayList<Denglswtx>();//登录事务提醒信息集合\
		//查询事物定时任务上次执行的时间
		
		String lasttime=(String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryLastZxTime");
		SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currtime = DateUtil.curDateTime();
		//System.out.println("----------------------------"+lasttime);
		//System.out.println("----------------------------"+currtime);
		//查询今天新增的所有零件
		Map<String, String> paramMap = new HashMap();
		paramMap.put("lasttime",lasttime);
		paramMap.put("currtime",currtime);
		List<Lingj> listLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewLingj",paramMap);
		//创建新增零件参考系事务提醒信息
		for (int i = 0; i < listLingj.size(); i++) {
			Denglswtx denglswtx = new Denglswtx();
			Lingj lingj = listLingj.get(i);
			denglswtx.setId(getUUID());//主键ID
			denglswtx.setUsercenter(lingj.getUsercenter());//用户中心h 
			denglswtx.setYonghz("alljhy");//新建零件提醒给全部计划员
			denglswtx.setZhuangt("0");//待解决状态
			denglswtx.setTixlx(Const.SWTXLX_NEWLJ);//提醒类型新零件
			denglswtx.setGuanjz1(lingj.getLingjbh());//关键字1零件编号
			denglswtx.setTixnr(Const.SWTXLX_NEWLJNR);//提醒内容
			denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间
			listDenglswtx.add(denglswtx);//添加登录事务提醒
		}
		
		//查询新增的所有供应商
		List<Gongys> listGys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewGys",paramMap);
		//创建新增供应商参考系事务提醒
		for (int i = 0; i < listGys.size(); i++) {
			Denglswtx denglswtx = new Denglswtx();
			Gongys gongys = listGys.get(i);
			denglswtx.setId(getUUID());//主键ID
			denglswtx.setUsercenter(gongys.getUsercenter());//用户中心
			denglswtx.setYonghz("alljhy");//新建供应商提醒给全部计划员
			denglswtx.setZhuangt("0");//待解决状态
			denglswtx.setTixlx(Const.SWTXLX_NEWGYS);//提醒类型新供应商
			denglswtx.setGuanjz1(gongys.getGcbh());//关键字1供应商编号
			denglswtx.setTixnr(Const.SWTXLX_NEWGYSNR);//提醒内容
			denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间
			listDenglswtx.add(denglswtx);//添加登录事务提醒
		}
		
		//查询新增的所有零件供应商
		List<LingjGongys> listLingjGys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewLjGys",paramMap);
		//创建新增零件供应商参考系事务提醒
		for (int i = 0; i < listLingjGys.size(); i++) {
			Denglswtx denglswtx = new Denglswtx();
			LingjGongys lingjGongys = listLingjGys.get(i);
			denglswtx.setId(getUUID());//主键ID
			denglswtx.setUsercenter(lingjGongys.getUsercenter());//用户中心
			denglswtx.setYonghz("all");//给所有计划员组
			denglswtx.setZhuangt("0");//待解决状态
			denglswtx.setTixlx(Const.SWTXLX_NEWLJGYS);//提醒类型新供应商
			denglswtx.setGuanjz1(lingjGongys.getLingjbh());//关键字1零件编号
			denglswtx.setGuanjz2(lingjGongys.getGongysbh());//关键字2供应商编号
			denglswtx.setTixnr(Const.SWTXLX_NEWLJGYSNR);//提醒内容
			denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间
			listDenglswtx.add(denglswtx);//添加登录事务提醒
		}
		
		//查询新增的消耗点
		List<CkxGongyxhd> listXhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewXhd",paramMap);
		//创建新增消耗点的参考系事务提醒
		for (int i = 0; i < listXhd.size(); i++) {
			Denglswtx denglswtx = new Denglswtx();
			CkxGongyxhd ckxGongyxhd = listXhd.get(i);
			//查询消耗点所在分配循环的对应的物流工艺组
			Map<String,String> map = new HashMap<String, String>();
			map.put("fenpqh", ckxGongyxhd.getGongyxhd().substring(0,5));
			//查询消耗点对应的循环的物流工艺员组
			Wullj wullj = (Wullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryWulgyyz",map);
			if(wullj != null && !StringUtils.isEmpty(wullj.getWulgyyz())){
				denglswtx.setYonghz(wullj.getWulgyyz());//提醒给分配循环对应的物流工艺员组
			}else{
				denglswtx.setYonghz("allwlgyy");//提醒分配给全部物流工艺员
			}
			//工艺消耗点的用户中心为U+消耗点首位
			//获取用户中心 csy 20160421
			String usercenter = CommonUtil.getUsercenter(ckxGongyxhd.getGongyxhd().substring(0,1));
			denglswtx.setUsercenter(usercenter);
			denglswtx.setId(getUUID());//主键ID
			denglswtx.setZhuangt("0");//待解决状态
			denglswtx.setTixlx(Const.SWTXLX_NEWXHD);//提醒类型新消耗点
			denglswtx.setGuanjz1(ckxGongyxhd.getGongyxhd());//关键字1消耗点编号
			denglswtx.setTixnr(Const.SWTXLX_NEWXHDNR);//提醒内容
			denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间
			listDenglswtx.add(denglswtx);//添加登录事务提醒
		}
		
		//ckx_xitcsdy表消耗点生效/失效提前报警范围查询  2015.5.21 update
		List<Map<String,String>> listXhdbjsj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryXiaohdBjsj");
		//按ckx_xitcsdy表所定义的用户中心查询报警数据 
		for(Map<String,String> xcMap :listXhdbjsj ){
			paramMap.put("usercenter", xcMap.get("USERCENTER"));
			paramMap.put("zidmc", xcMap.get("ZIDMC")); //提前报警天数
			//查询新增(及生效)零件消耗点信息
			List<Lingjxhd> listLingjXhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewLjXhd",paramMap);
			for (int i = 0; i < listLingjXhd.size(); i++) {
				Denglswtx denglswtx = new Denglswtx();
				Lingjxhd lingjxhd = listLingjXhd.get(i);
				//查询零件消耗点（用户中心、零件、消耗点）在事务表中是否存在 hzg 2015.5.21 update
				String shiwtxID = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryLjXhdNum",lingjxhd);
				if(null!=shiwtxID){ //存在则 更新
					updateShiwtxOfLingjxhd(shiwtxID,lingjxhd.getPdsshengxsj());
				}else{//不存在则插入
					//查询零件所对应的计划员
					Map<String,String> map = new HashMap<String, String>();
					map.put("usercenter", lingjxhd.getUsercenter());
					map.put("fenpqh", lingjxhd.getFenpqbh());
					//查询消耗点对应的循环的物流工艺员组
					Wullj wullj = (Wullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryWulgyyz",map);
					if(wullj != null && !StringUtils.isEmpty(wullj.getWulgyyz())){
						denglswtx.setYonghz(wullj.getWulgyyz());//提醒给分配循环对应的物流工艺员组
					}else{
						denglswtx.setYonghz("allwlgyy");//提醒分配给全部物流工艺员
					}
					denglswtx.setId(getUUID());//主键ID
					denglswtx.setUsercenter(lingjxhd.getUsercenter());//用户中心
					denglswtx.setZhuangt("0");//待解决状态
					denglswtx.setTixlx(Const.SWTXLX_NEWLJXHD);//提醒类型新零件消耗点
					denglswtx.setGuanjz1(lingjxhd.getLingjbh());//关键字1零件编号
					denglswtx.setGuanjz2(lingjxhd.getXiaohdbh());//关键字2消耗点编号
					denglswtx.setTixnr(Const.SWTXLX_NEWLJXHDNR);//提醒内容
					denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间
					denglswtx.setChulsj(lingjxhd.getPdsshengxsj());//处理时间
					listDenglswtx.add(denglswtx);//添加登录事务提醒 
				}
			}
			
			// -------查询失效零件消耗点信息 add 2015.5.21--------------
			List<Lingjxhd> listLingjXhdShix = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("denglswtx.queryNewLjXhdOfShix",paramMap);
			for (int i = 0; i < listLingjXhdShix.size(); i++) {
				Denglswtx denglswtx = new Denglswtx();
				Lingjxhd lingjxhd = listLingjXhdShix.get(i);
				//查询零件消耗点（用户中心、零件、消耗点）在事务表中是否存在 hzg 2015.5.21 update
				String shiwtxID = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryLjXhdNum",lingjxhd);
				if(null!=shiwtxID){ //存在则 更新
					updateShiwtxOfLingjxhd(shiwtxID,lingjxhd.getPdsshixsj());
				}else{//不存在则插入
					Map<String,String> map = new HashMap<String, String>();
					map.put("usercenter", lingjxhd.getUsercenter());
					map.put("fenpqh", lingjxhd.getFenpqbh());
					//查询消耗点对应的循环的物流工艺员组
					Wullj wullj = (Wullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("denglswtx.queryWulgyyz",map);
					if(wullj != null && !StringUtils.isEmpty(wullj.getWulgyyz())){
						denglswtx.setYonghz(wullj.getWulgyyz());//提醒给分配循环对应的物流工艺员组
					}else{
						denglswtx.setYonghz("allwlgyy");//提醒分配给全部物流工艺员
					}
					denglswtx.setId(getUUID());//主键ID
					denglswtx.setUsercenter(lingjxhd.getUsercenter());//用户中心
					denglswtx.setZhuangt("0");//待解决状态
					denglswtx.setTixlx(Const.SWTXLX_SHIXLJXHD);//提醒类型失效零件消耗点
					denglswtx.setGuanjz1(lingjxhd.getLingjbh());//关键字1零件编号
					denglswtx.setGuanjz2(lingjxhd.getXiaohdbh());//关键字2消耗点编号
					denglswtx.setTixnr(Const.SWTXLX_NEWLJXHDNR);//提醒内容
					denglswtx.setBaojsj(CommonFun.getJavaTime());//报警时间]
					denglswtx.setChulsj(lingjxhd.getPdsshixsj()); //处理时间
					listDenglswtx.add(denglswtx);//添加登录事务提醒
				}
			}	

		}
		
		
		
		//保存参考系事务提醒
		if(listDenglswtx != null && !listDenglswtx.isEmpty()){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("denglswtx.insertDenglswtx",listDenglswtx);
		}
		//执行成功后将记录的执行截止时间存入数据库作为下次执行开始时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("denglswtx.update_currtime",currtime);
		/*
		for (Denglswtx bean : listDenglswtx) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("denglswtx.insertDenglswtx", bean);
		}
		*/
	}

	/**
	 * 用户中心\零件编号\消耗点编号相同，则根据ID更新事务提醒表
	 * @author 贺志国
	 * @date 2015-5-21
	 * @param lingjxhd 
	 */
	private void updateShiwtxOfLingjxhd(String shiwtxID,String chulsj) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("shiwtxID", shiwtxID);
		params.put("chulsj", chulsj);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("denglswtx.update_shiwtxOfLingjxhd",params);
		
	}
}

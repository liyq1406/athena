package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.transTime.CkxCsChushlk;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.XiaohcmbCk;
import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * @description小火车运输时刻模板(执行层)
 * @author hj
 * @date 2013-12-6
 */
@Component
public class XiaohcmbCkService extends BaseService<XiaohcmbCk> {
	private  Logger logger =Logger.getLogger(XiaohcmbCkService.class);
	private String creator;
	
	@Inject
	XiaohcmbCkTempService xiaohcmbCkTempService;
	
	/**
	 * @description获得命名空间
	 * @author denggq
	 * @date 2012-4-11
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	public String calculate(String creator){
		logger.info("执行层小火车模板计算开始---------------");
		this.creator = creator;
		//UW 计算
		calculateByUsercenter("UW");
		//UX 计算
		calculateByUsercenter("UX");
		// UL 计算
		calculateByUsercenter("UL");
		
		logger.info("执行层小火车模板计算结束---------------计算成功");
		changePianyl(creator);
		return "";
	}
	/**
	 * 刷消耗点偏移量
	 * @param creator
	 * @return
	 */
	
	public String changePianyl(String creator){
		logger.info("执行层小火车刷拆分结果偏移量开始---------------");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohdPyl",creator);
		logger.info("执行层小火车刷拆分结果偏移量结束---------------");
		return "";
	}
	/**
	 * 自动根据将来参数生效日赋值将来参数
	 * @param creator
	 * @return
	 */
	public String autoUpdateXiaohc(String creator){
		logger.info("执行层小火车将来参数更新 开始---------------");
		Xiaohc bean = new Xiaohc();
		bean.setEditor(creator);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_ck.updateXiaohcByshengxr",bean);
		logger.info("执行层小火车将来参数更新 完成---------------");
		return "success";
	}
    /**
     * 根据用户中心，查询小火车的循环车身数>0 的数据开始计算
     * @param usercenter
     */
	@SuppressWarnings("unchecked")
	private void calculateByUsercenter(String usercenter){
		try {
			Xiaohc xiaohcbean = new Xiaohc();
			xiaohcbean.setUsercenter(usercenter);//通过传递的用户中心计算
			xiaohcbean.setXunhcss(0+"");//取循环车身数大于0 的数据
			xiaohcbean.setBiaos("1");//取有效的小火车表数据进行计算
			//取到该用户中心下的所有产线
			List<Xiaohc> listChanx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx_ck.xiaohc_GroupByChanx",xiaohcbean);
			for (Xiaohc bean : listChanx) {
				bean.setXunhcss(0+"");//取循环车身数大于0 的数据
				bean.setBiaos("1");//取有效的小火车表数据进行计算
				//取到该产线下的所有小火车进行计算
				List<Xiaohc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx_ck.queryXiaohc",bean);
				//生产线 ，取车位数
				Shengcx shengcx = new Shengcx();
				shengcx.setUsercenter(bean.getUsercenter());
				shengcx.setShengcxbh(bean.getShengcxbh());
				shengcx.setBiaos("1");
				
				//如果此产线的计算出错，则跳过此产线的计算
				whileAdd(list,shengcx);
				
			}
			logger.info("执行层小火车模板计算   "+usercenter+" 计算完成---------------");
		} catch (Exception e) {
			logger.error(e.toString());			
			logger.info(e.toString()+"执行层小火车模板计算   "+usercenter+" 计算失败---------------");
			throw new ServiceException(e.toString());
		}
		
	}
	/**
	 * 循环将数据插入小火车模板
	 * @param list
	 * shengcx
	 */
	private void whileAdd(List<Xiaohc> list,Shengcx shengcx){
		List<XiaohcmbCk> listXiaohcmbCk = new ArrayList<XiaohcmbCk>();
		String mes = "未找到用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的数据或数据已失效" ;
		shengcx = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getShengcx",shengcx);
		if(null == shengcx){
			//如果生产线不存在或数据已失效，则跳过此生产线的计算
			logger.error(mes);		
			logger.info(mes);
			return ;//跳过计算该产线
//			throw new ServiceException(mes);
		}
		if(shengcx.getChews() == null){
			//如果生产线的车位数未空，则跳过此生产线的计算
			logger.error("用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的车位数未维护，请维护后重新计算");		
			logger.info("用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的车位数未维护，请维护后重新计算");
			return ;//跳过计算该产线
		}
		Xitcsdy xitcsdy = new Xitcsdy();
		xitcsdy.setZidlx("CSXH");
		xitcsdy.setZidbm(shengcx.getShengcxbh());
		xitcsdy = (Xitcsdy)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXitcsdy", xitcsdy);
		//如果没有配置该产线的循环次数，则默认为30
		int xhtc = 30;
		if(xitcsdy == null){
			logger.info("未找到生产线："+shengcx.getShengcxbh()+"的CS每日循环次数");
		}else{
			if(!(xitcsdy.getQujzdz() == null || "".equals(xitcsdy.getQujzdz()))){
				xhtc =xitcsdy.getQujzdz().intValue();
			}
		}
		for (Xiaohc xiaohc : list) {			
			if(xiaohc.getDangqtc() == null
					|| xiaohc.getJieslsh() == null){
				logger.info("生产线："+shengcx.getShengcxbh()+"的未配置对应的模板控制");
				continue ;
			}
			// 小火车模板
			XiaohcmbCk xiaohcmbCk = new XiaohcmbCk();
			xiaohcmbCk.setUsercenter(xiaohc.getUsercenter());
			xiaohcmbCk.setChanx(xiaohc.getShengcxbh());
			xiaohcmbCk.setXiaohcbh(xiaohc.getXiaohcbh());
			xiaohcmbCk.setTangc(Integer.parseInt(xiaohc.getDangqtc()));
			// 根据小火车模板控制表对应的当前趟次和结束流水号，找到对应的小火车模板数据
			xiaohcmbCk = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXiaohcmbkz", xiaohcmbCk);
			// 如果未找到，则从1开始生成
			if (null == xiaohcmbCk) {
				xiaohcmbCk = new XiaohcmbCk();
				xiaohcmbCk.setUsercenter(xiaohc.getUsercenter());
				xiaohcmbCk.setChanx(xiaohc.getShengcxbh());
				xiaohcmbCk.setXiaohcbh(xiaohc.getXiaohcbh());
				// 当前趟次
				xiaohcmbCk.setTangc(Integer.parseInt(xiaohc.getDangqtc()));
				// 结束流水号
				xiaohcmbCk.setJieslsh(Integer.parseInt(xiaohc.getJieslsh()));
				Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getMaxLiushByXiaohc", xiaohcmbCk);
				// 小火车模板流水号
				xiaohcmbCk.setLiush(Integer.valueOf(obj.toString()));
			}
			// 根据当前趟次和结束流水号删除小火车模板以下的数据
			//cs优化变更010466 gswang 2014-09-10
			xiaohcmbCkTempService.removeMbByQislsh(xiaohcmbCk);
			//重新查询，避免并发
			// 小火车模板
			XiaohcmbCk xiaohcmbCkBean = new XiaohcmbCk();
			xiaohcmbCkBean.setUsercenter(xiaohc.getUsercenter());
			xiaohcmbCkBean.setChanx(xiaohc.getShengcxbh());
			xiaohcmbCkBean.setXiaohcbh(xiaohc.getXiaohcbh());
			xiaohcmbCkBean.setTangc(Integer.parseInt(xiaohc.getDangqtc()));
			// 根据小火车模板控制表对应的当前趟次和结束流水号，找到对应的小火车模板数据
			xiaohcmbCkBean = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryXiaohcmbkz", xiaohcmbCkBean);
			// 如果未找到，则从1开始生成
			if (null == xiaohcmbCkBean) {
				xiaohcmbCkBean = new XiaohcmbCk();
				xiaohcmbCkBean.setUsercenter(xiaohc.getUsercenter());
				xiaohcmbCkBean.setChanx(xiaohc.getShengcxbh());
				xiaohcmbCkBean.setXiaohcbh(xiaohc.getXiaohcbh());
				// 当前趟次
				xiaohcmbCkBean.setTangc(Integer.parseInt(xiaohc.getDangqtc()));
				// 结束流水号
				xiaohcmbCkBean.setJieslsh(Integer.parseInt(xiaohc.getJieslsh()));
				Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getMaxLiushByXiaohc", xiaohcmbCk);
				// 小火车模板流水号
				xiaohcmbCkBean.setLiush(Integer.valueOf(obj.toString()));
			}
			// 根据公式，循环计算出备货流水号。。。，将循环30次得到的流水号插入小火车模板，
			formulaCalculate( xiaohcmbCkBean, xiaohc, shengcx,xhtc,listXiaohcmbCk);
		}
		//按照产线将数据批量插入到小火车模板表中
		//cs优化变更010466 gswang 2014-09-10
		xiaohcmbCkTempService.insertXiaohcmb(listXiaohcmbCk,shengcx);
		
	}
	/**
	 * 根据公式，循环计算出备货流水号。。。，将循环30次得到的流水号插入小火车模板，
	 * @param liush
	 * @param temp
	 * @param xunhcss
	 * @param xiaohc
	 * @param listXiaohcmbCk
	 */
	private void formulaCalculate(XiaohcmbCk xiaohcmbCk,Xiaohc xiaohc,Shengcx shengcx,int xhtc,List<XiaohcmbCk> listXiaohcmbCk){
		// 循环车身数
		int xunhcss = Integer.parseInt(xiaohc.getXunhcss());
		// 流水号
		int liush = xiaohcmbCk.getLiush();
		// 小火车临时趟次
		int tangc = xiaohcmbCk.getTangc();
		int jieslsh = xiaohcmbCk.getJieslsh();
		for(int i = 1 ; i <= xhtc ; i++){
			//趟次1-99为一个轮回
			if(tangc >= 99){
				tangc = 0;
			}
			tangc++;
			//流水号顺序生成
			liush++;
			XiaohcmbCk bean = new XiaohcmbCk();
			bean.setUsercenter(xiaohc.getUsercenter());
			bean.setChanx(xiaohc.getShengcxbh());
			bean.setXiaohcbh(xiaohc.getXiaohcbh());
			bean.setLiush(liush);
			bean.setTangc(tangc);
			//起始流水号
			bean.setQislsh(jieslsh+1);
			//结束流水号
			bean.setJieslsh(bean.getQislsh()-1+xunhcss);
			
			//备货流水号E = 偏移车位数-备货提前车身数+循环车身数（第一次不加）
			bean.setEmonbhlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getBeihtqcss()) + jieslsh);
			//上线流水号E = 偏移车位数-上线提前车身数+循环车身数（第一次不加）
			bean.setEmonsxlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getShangxtqcss())+jieslsh);
			//备货流水号S = 备货流水号E - 车位数
			bean.setSmonbhlsh(bean.getEmonbhlsh() - Integer.parseInt(shengcx.getChews()));
			//上线流水号S = 上线流水号E-车位数
			bean.setSmonsxlsh(bean.getEmonsxlsh() - Integer.parseInt(shengcx.getChews()));
			bean.setFlag("0");
			bean.setCreator(creator);
			bean.setEditor(creator);
			listXiaohcmbCk.add(bean);
			jieslsh = bean.getJieslsh();
			
		}
	}
	
	/**
	 * 分页查询小火车模板页面
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectXiaohcmb(CkxCsChushlk ckxCsChushlk) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryXiaohcmbOflkcs",ckxCsChushlk,ckxCsChushlk);
	}
	
	/**
	 * 小火车模板拉空重算
	 * @param bean
	 * @return -1表示异常，0表示正常
	 * @throws ServiceException
	 */
	@Transactional
	public String xiaohcmbLk(ArrayList<CkxCsChushlk> ckxCsChushlkList) throws ServiceException {
		
		Map param = new HashMap<String,String>();
		for(CkxCsChushlk ckxCsChushlk:ckxCsChushlkList){
			//未输入jieslsh的记录不更新
			if(ckxCsChushlk.getJieslsh()==null||"".equals(ckxCsChushlk.getJieslsh())){
				continue;
			}
			param.put("usercenter", ckxCsChushlk.getUsercenter());
			param.put("chanx", ckxCsChushlk.getShengcxbh());
			param.put("xiaohcbh", ckxCsChushlk.getXiaohcbh());
			param.put("zongzlsh", ckxCsChushlk.getJieslsh());
			param.put("editor", ckxCsChushlk.getEditor());
			//查询shangxd
			String shangxd = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryShangxd", param);
			if(shangxd==null){
				return "未找到shangxd";
			}
			param.put("wuld", shangxd);
			//查询zhengcxh
			String zhengcxh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryZhengcxh", param);
			if(zhengcxh==null){
				return "未查出整车序号";
			}
			param.put("zhengcxh", zhengcxh);
			try{
				//更新ck_xiaohcmb表	
				int result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohcmbOflkcs", param);

				if(result==0){
					return "小火车模板数据未查到";
				}
				//更新ck_xiaohcmbkz表
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohcmbKzOflkcs", param);
											
			}catch(Exception e){	
				logger.error("执行出错"+e.getStackTrace());
				return "更新出错";
			}	
		}
		return "success";
	}

}

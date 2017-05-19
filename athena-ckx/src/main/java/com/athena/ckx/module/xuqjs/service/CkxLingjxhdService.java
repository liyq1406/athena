package com.athena.ckx.module.xuqjs.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.log4j.Logger;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxInnerPathAndModle;
import com.athena.ckx.entity.xuqjs.Biangjlb;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.carry.service.CkxWulljService;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.KanbxhgmCkxService;
import com.athena.ckx.util.PeislxWebservice;
import com.athena.component.service.BaseService;
import com.athena.component.utils.LoaderProperties;
import com.athena.component.wtc.WtcResponse;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 消耗点-零件Service
 * @author denggq
 * @date 2012-4-18
 */
@Component
public class CkxLingjxhdService extends BaseService<CkxLingjxhd>{
	@Inject
	private CkxShiwtxService ckxshiwtxService;
	@Inject 
	private KanbxhgmCkxService kanbxhgmService;
	@Inject
	private CkxWulljService ckxWulljService;
	
	protected static Logger logger = Logger.getLogger(CkxLingjxhdService.class);
	
	//配置文件路径
	private final String fileName="urlPath.properties"; 
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-18
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> select(CkxLingjxhd bean) throws ServiceException {
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			Fenpq fenpq = new Fenpq();
			fenpq.setUsercenter(bean.getUsercenter());
			fenpq.setFenpqh(bean.getFenpqbh());
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fenpq);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("wufenpeisj"));
			}else{
				bean.setWulgyyz(value);
			}
			fenpq.setWulgyyz(value);
		}
		Map<String, Object> mapList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryCkxLingjxhd",bean,bean);
		List<CkxLingjxhd> listValue = (List<CkxLingjxhd>) mapList.get("rows");
		for (CkxLingjxhd ckxLingjxhd : listValue) {
			ckxLingjxhd.setYuanbiaos(ckxLingjxhd.getBiaos());
		}
		mapList.put("rows", listValue);
		return mapList;
	}


	/**
	 * 单记录保存
	 * @author denggq
	 * @date 2012-4-18
	 * @param bean,operant
	 * @return String
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public String save(CkxLingjxhd bean,String userName,Integer operant,WtcResponse wtc ) throws ServiceException{
		
		if(null != bean.getLingjbh()){									//零件编号是否存在
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+ GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+ GetMessageByKey.getMessage("notexist"));
		}
		
		//WTC调用
		if(null != wtc){									//配送类型是否存在
			String response = wtc.get("response").toString();
			if(!response.equals(CkxLingjxhd.WTC_SUCCESS_PSLX)){
				throw new ServiceException(GetMessageByKey.getMessage("peisonglx")+bean.getPeislxbh()+GetMessageByKey.getMessage("notexist"));
			}
		}
		
		
		if(null != bean.getGongysbh()&&!"".equals(bean.getGongysbh().trim())){									//供应商编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where usercenter = '"+bean.getUsercenter()+"' and gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			Gongcy gongcy = new Gongcy();
			gongcy.setUsercenter(bean.getUsercenter());
			gongcy.setGcbh(bean.getGongysbh());
			gongcy.setBiaos("1");
			gongcy.setLeix("3");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountGongys", gongcy, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
		}
		
		String fenpqh = bean.getXiaohdbh().substring(0, 5);				//分配循环为消耗点前五位
		if(null != fenpqh){												//分配循环是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_fenpq where usercenter = '"+bean.getUsercenter()+"' and fenpqh = '"+fenpqh+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("fenpxhbh")+fenpqh+GetMessageByKey.getMessage("notexist"));
			Fenpq fenpq = new Fenpq();
			fenpq.setUsercenter(bean.getUsercenter());
			fenpq.setFenpqh(fenpqh);
			fenpq.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountFenpq", fenpq, GetMessageByKey.getMessage("fenpxhbh")+fenpqh+GetMessageByKey.getMessage("notexist"));
		}
		bean.setFenpqbh(fenpqh);										//分配区编号
		
//		String shengcxbh = getshengcxbh(bean.getUsercenter(), fenpqh);	//获得生产线编号
//		bean.setShengcxbh(shengcxbh);
		
		Fenpq fenpqObj = getFenpq(bean.getUsercenter(), fenpqh);	//获得生产线编号
		String shengcxbh = fenpqObj.getShengcxbh();
		bean.setShengcxbh(shengcxbh);					//生产线编号
		bean.setFenzxh(fenpqObj.getFenzxh());
		
		Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		
		if(!"ZXCPOA".equals(key)){
			//小火车的生产线编号为分装线号
			String fenzxh = fenpqObj.getFenzxh() != null && !fenpqObj.getFenzxh().equals("") ? fenpqObj.getFenzxh() : shengcxbh;
			if(null != bean.getXiaohcbh()){									//小火车编号是否存在
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiaohc where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbh+"' and xiaohcbh = '"+bean.getXiaohcbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
				Xiaohc xiaohc = new Xiaohc();
				xiaohc.setUsercenter(bean.getUsercenter());
				xiaohc.setShengcxbh(fenzxh);
				xiaohc.setXiaohcbh(bean.getXiaohcbh());
				xiaohc.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountXiaohc", xiaohc, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
			}
			
			if(null != bean.getXiaohccxbh()){								//小火车车厢编号是否存在
				
				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiaohccx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+fenzxh+"' and xiaohcbh = '"+bean.getXiaohcbh()+"' and chexh = '"+bean.getXiaohccxbh()+"' and biaos = '1'";
				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiaohccxh")+bean.getXiaohccxbh()+GetMessageByKey.getMessage("notexist"));
			}
		}
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		if (1 == operant) {												//增加
			//验证消耗点是否存在
			CkxGongyxhd gyxhd= new CkxGongyxhd();
			gyxhd.setGongyxhd(bean.getXiaohdbh());
			gyxhd.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxGongyxhd", gyxhd,"不存在工艺消耗点："+bean.getXiaohdbh()+",或数据已失效");
			
			if(null != shengcxbh && !"".equals(shengcxbh)){				//根据默认值设置安全库存天数 Bug 0002465
				Shengcx s = new Shengcx();
				s.setUsercenter(bean.getUsercenter());
				s.setShengcxbh(shengcxbh);
				s =  (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getShengcx",s);
				if(null != s && null == bean.getAnqkcts()){
					bean.setAnqkcts(s.getAnqkctsmrz());
				}
			}
			
			String currentDate = DateTimeUtil.getCurrDate();
			if(null != bean.getShengxr() && DateTimeUtil.compare(currentDate, bean.getShengxr())){			//消耗点起始日为未来日期则失效状态
				bean.setBiaos("0");
			}else{							//消耗点起始日已过为生效状态
				bean.setBiaos("1");
			}
			if("1".equals(bean.getBiaos())){
				if(null == bean.getWulbh() || "".equals(bean.getWulbh())){
					throw new ServiceException("若数据为有效，则物流描述编号必填");
				}
			}
			bean.setJiesr("9999-12-31");	//消耗点结束日
			bean.setXiaohbl(0.0);			//消耗比例
			bean.setXittzz(bean.getYifyhlzl()-bean.getJiaofzl());//系统调整值(已发要货令总数量 - 交付总量)
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingjxhd",bean);//增加数据库
		}else if (2 == operant){			//修改
			if("1".equals(bean.getBiaos())){
				if(null == bean.getWulbh() || "".equals(bean.getWulbh())){
					throw new ServiceException("若数据为有效，则物流描述编号必填");
				}
			}else if("2".equals(bean.getBiaos())){
				bean.setJiesr("9999-12-31");	//消耗点结束日
				bean.setXiaohbl(0.0);			//消耗比例
				bean.setBiaos("1");
			}
			
			//验证消耗点是否存在
			CkxGongyxhd gyxhd= new CkxGongyxhd();
			gyxhd.setGongyxhd(bean.getXiaohdbh());
			gyxhd.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxGongyxhd", gyxhd,"不存在工艺消耗点："+bean.getXiaohdbh()+",或数据已失效");
			
			//复制功能，只能复制零件消耗点关系存在的数据
			CkxLingjxhd ckxLingjxhd = new CkxLingjxhd();
			ckxLingjxhd.setUsercenter(bean.getUsercenter());
			ckxLingjxhd.setLingjbh(bean.getLingjbh());
			ckxLingjxhd.setXiaohdbh(bean.getXiaohdbh());
			bean.setXittzz(bean.getYifyhlzl()-bean.getJiaofzl());//系统调整值(已发要货令总数量 - 交付总量)
			List<CkxLingjxhd> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCkxLingjxhd", ckxLingjxhd);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("yuxhdbh")+bean.getXiaohdbh()+GetMessageByKey.getMessage("guanxbcz"));
			}
			
			//先查询出对应的记录存储的版本号
			//更新前线查询对应的零件消耗点 查询出他的 VERSION 版本的值
			CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdjp", bean);
			//当条记录对应的version版本号
			String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion",bean);
			if (Integer.valueOf(versionmax) == 999999999) {
				//版本号自动循环 如果超过了最大的上限  则在从 1 开始
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",bean);
				bean.setVersion(1);
				//versionxhd.setVersion(1);
			}else{
				bean.setVersion(versionxhd.getVersion());
			}
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhd",bean);//修改数据库
			//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
			if(0==num){
				return  "数据中有某一行数据正在被多个用户同时操作，请稍后在更新!";
			}
			ckxshiwtxService.update(bean.getUsercenter(), CkxShiwtxService.TIXLX_LINGJXHD, bean.getLingjbh(), bean.getXiaohdbh(), "1");
			//验证消耗比例是否为100%   lc   2016-8-19
			if("1".equals(bean.getBiaos())){
				return this.checkXiaohdbl(bean);
			}			
		}
		//修改的弹出窗口需要去掉校验消耗比例为100%。0007128
//		//检测消耗比例
//		ArrayList<CkxLingjxhd> listbean = new ArrayList<CkxLingjxhd>();
//		listbean.add(bean);
//		checkXiaohbl(listbean, listbean, listbean, bean.getUsercenter());
		return "success";
	}
	
	
	/**
	 * 验证消耗比例
	 * @author lc
	 * @date 2016-8-19
	 */
	public String checkXiaohdbl(CkxLingjxhd bean) {
		//验证消耗比例是否为100%
		String str = "";
		String num = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountCkxLingjxhdbl",bean);
		if(Integer.parseInt(num) != 100){
			str = "\t\t\t\t保存成功\n该零件消耗点的消耗比例之和不为100%，请及时维护";
		}else{
			str = "保存成功";
		}
		return str;
	}	
	
	
	/**
	 * 删除数据
	 * @author denggq
	 * @date 2012-4-18
	 * @param bean,userName
	 * @return ""
	 * */
	@Transactional
	public String doDelete(CkxLingjxhd bean,String userName) throws ServiceException{
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxLingjxhd", bean);	//删除数据库
		
		Biangjlb b = new Biangjlb();			//变更记录表
		b.setUsercenter(bean.getUsercenter());	//用户中心
		b.setLingjbh(bean.getLingjbh());		//零件编号
		b.setXianbh(bean.getXiaohdbh());		//消耗点编号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBiangjlb", b);		//删除变更
		
		return "success";
	}

	
	/**
	 * 单记录保存
	 * @author denggq
	 * @date 2012-4-18
	 * @param bean,operant
	 * @return String
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public String savejipxx(CkxLingjxhd bean,String userName,Integer operant,WtcResponse wtc ) throws ServiceException{
	
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		if("ZBC".equals(zbczxc)){
			
			if(null != wtc){									//配送类型是否存在
				String response = wtc.get("response").toString();
				if(!response.equals(CkxLingjxhd.WTC_SUCCESS_PSLX)){
					throw new ServiceException(GetMessageByKey.getMessage("peisonglx")+bean.getPeislxbh()+GetMessageByKey.getMessage("notexist"));
				}
			}
			}else{
				int i= (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querypeislx", bean);
				if(i == 0){
				throw new ServiceException(bean.getPeislxbh()+"配送类型不是集配标识！");
			}
		}
		
	
		//防止并发的产生，使用 乐观锁 的原则来对 零件消耗点的表 进行更新。
		//更新前线查询对应的零件消耗点 查询出他的 VERSION 版本的值
		logger.info("查询版本号开始");
		CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdjp", bean);
		logger.info("查询版本号结束"+versionxhd);
		//当条记录对应的version版本号
		logger.info("查询当前记录对应的版本号开始");
		String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion", bean);
		logger.info("查询当前记录对应的版本号结束"+versionmax);
		logger.info("与最大的版本号之间比较开始"+versionxhd.getVersion());
		if (Integer.valueOf(versionmax) == 9999999) {
			//版本号自动循环 如果超过了最大的上限  则在从 1 开始
			logger.info("与最大的版本号之间比较中############################");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",bean);
			bean.setVersion(1);
			//versionxhd.setVersion(1);
		}else{
			logger.info("与最大的版本号之间比较中××××××××××××××××××××××××××");
			bean.setVersion(versionxhd.getVersion());
		}
		logger.info("与最大的版本号之间比较结束");
		//bean.setVersion(versionxhd.getVersion());
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		logger.info("更新集配信息字段开始");
		int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxJipxx",bean);//修改数据库
		logger.info("更新集配信息字段结束");
		//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
		logger.info("更新集配信息字段失败开始");
		if(0==num){
			return  "数据正在被多个用户同时操作，请稍后在更新!!";
		}
		logger.info("更新集配信息字段失败结束");
		return "success";
	}
	
	/**
	 * 同步信息验证
	 * @param bean
	 * @return
	 */
	public Integer getTongbxxPeiz(CkxLingjxhd bean){
		String urlPath = "";
		if ("UL".equals(bean.getUsercenter())||"UX".equals(bean.getUsercenter())) {
			urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathUL");
		}
		if ("UW".equals(bean.getUsercenter())) {
			urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathUW");
		}
		
		//添加VDwebservie 调用 start
		if ("VD".equals(bean.getUsercenter())) {
			urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathVD");
		}
		//添加VDwebservie 调用 end
				
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(PeislxWebservice.class);
		factory.setAddress(urlPath+"peislxWebservice");
		PeislxWebservice client = (PeislxWebservice) factory.create();
		return client.querypeislxTB(bean);
		
	}
	
	/**
	 * 单记录保存
	 * @author CSY
	 * @date 2016-03-18
	 * @param bean,operant
	 * @return String
	 * */
	@Transactional
	public String savetongbxx(CkxLingjxhd bean,String userName,Integer operant,Integer pslbCount ) throws ServiceException{
	
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		if("ZBC".equals(zbczxc)){
			if(pslbCount == 0){
				throw new ServiceException(GetMessageByKey.getMessage("peisonglx")+bean.getPeislxbh()+GetMessageByKey.getMessage("notexist"));
			}
		}else{
			int i= (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querypeislxTB", bean);
			if(i == 0){
				throw new ServiceException(bean.getPeislxbh()+"配送类型不是同步标识！");
			}
		}
		
	
		//防止并发的产生，使用 乐观锁 的原则来对 零件消耗点的表 进行更新。
		//更新前线查询对应的零件消耗点 查询出他的 VERSION 版本的值
		logger.info("查询版本号开始");
		CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdTB", bean);
		logger.info("查询版本号结束"+versionxhd);
		//当条记录对应的version版本号
		logger.info("查询当前记录对应的版本号开始");
		String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion", bean);
		logger.info("查询当前记录对应的版本号结束"+versionmax);
		logger.info("与最大的版本号之间比较开始"+versionxhd.getVersion());
		if (Integer.valueOf(versionmax) == 9999999) {
			//版本号自动循环 如果超过了最大的上限  则在从 1 开始
			logger.info("与最大的版本号之间比较中############################");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",bean);
			bean.setVersion(1);
			//versionxhd.setVersion(1);
		}else{
			logger.info("与最大的版本号之间比较中××××××××××××××××××××××××××");
			bean.setVersion(versionxhd.getVersion());
		}
		logger.info("与最大的版本号之间比较结束");
		//bean.setVersion(versionxhd.getVersion());
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		logger.info("更新同步信息字段开始");
		int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxTongbxx",bean);//修改数据库
		logger.info("更新同步信息字段结束");
		//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
		logger.info("更新同步信息字段失败开始");
		if(0==num){
			return  "数据正在被多个用户同时操作，请稍后在更新!!";
		}
		logger.info("更新集配信息字段失败结束");
		return "success";
	}
	
	
	/**
	 * @description保存
	 * @param bean
	 * @author denggq
	 * @date 2012-4-25
	 * @return bean
	 */
	@Transactional
	public String saves(ArrayList<CkxLingjxhd> insert , ArrayList<CkxLingjxhd> edit , ArrayList<CkxLingjxhd> delete ,String usercenter,String userId) throws ServiceException{
		
//		inserts(insert,userId);		//增加
		edits(edit,userId);			//修改
		//deletes(delete,userId);	//删除
		
		checkXiaohbl(insert, edit, delete, usercenter);
		return "success";
		
		
	}
	
	
	/**
	 * @description私有批量update方法
	 * @author denggq
	 * @date 2012-4-25
	 * @param edit,userId
	 * @return ""
	 */
	public String editsjipxx(List<CkxLingjxhd> edit,String userId,WtcResponse wtc) throws ServiceException{
		for(CkxLingjxhd bean:edit){
			//只需要校验配送类型的存在性
			//WTC调用
			if(null != wtc){									//配送类型是否存在
				String response = wtc.get("response").toString();
				if(!response.equals(CkxLingjxhd.WTC_SUCCESS_PSLX)){
					throw new ServiceException(GetMessageByKey.getMessage("peisonglx")+bean.getPeislxbh()+GetMessageByKey.getMessage("notexist"));
				}
			}
			
			
		}
		return "";
	}
	
	/**
	 * @description私有批量insert方法
	 * @author denggq
	 * @date 2012-4-25
	 * @param insert,userId
	 * @return  ""
	 */
	public String inserts(List<CkxLingjxhd> insert,String userId)throws ServiceException{
		for(CkxLingjxhd bean:insert){
			
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			checkYuanxhd(bean);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingjxhd",bean);	//增加数据库
		}
		return "";
	}
	
	
	/**
	 * @description私有批量update方法
	 * @author denggq
	 * @date 2012-4-25
	 * @param edit,userId
	 * @return ""
	 */
	public String edits(List<CkxLingjxhd> edit,String userId) throws ServiceException{
		
		for(CkxLingjxhd bean:edit){
			//bug0002462 begin
			Assert.notNull(bean.getShengcxbh(),GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")
					+bean.getXiaohdbh()+GetMessageByKey.getMessage("shengcxnotnull"));
			Assert.notNull(bean.getXiaohbl(),GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")
					+bean.getXiaohdbh()+GetMessageByKey.getMessage("xiaohblnotnull"));
			Assert.notNull(bean.getShengxr(),GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")
					+bean.getXiaohdbh()+GetMessageByKey.getMessage("xiaohdqsrnotnull"));
			Assert.notNull(bean.getJiesr(), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")
					+bean.getXiaohdbh()+GetMessageByKey.getMessage("jiesrqnotnull"));
			
			Assert.isTrue(DateTimeUtil.compare( bean.getShengxr() , bean.getJiesr()), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")
					+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")+bean.getXiaohdbh()+GetMessageByKey.getMessage("xiaohdxiaoyjiesrq"));
			
			String currentDate = DateTimeUtil.getCurrDate();//当前日期
			if(DateTimeUtil.compare(currentDate, bean.getShengxr()) || !"9999-12-31".equals(bean.getJiesr())){//起始日未到或者结束日不为9999-12-31 消耗比例只能输入0
				Assert.isTrue(0 == bean.getXiaohbl(), GetMessageByKey.getMessage("yonghuzx")+bean.getUsercenter()+GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("xiaohaodbh")
						+bean.getXiaohdbh()+GetMessageByKey.getMessage("xiaohbliszero"));
			}
			//验证消耗点是否存在
			CkxGongyxhd gyxhd= new CkxGongyxhd();
			gyxhd.setGongyxhd(bean.getXiaohdbh());
			gyxhd.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxGongyxhd", gyxhd,"不存在工艺消耗点："+bean.getXiaohdbh()+",或数据已失效");
			
			//bug0002462 end
			if( DateTimeUtil.compare(bean.getJiesr(), currentDate)){//起始日未到或者结束日已过
				bean.setBiaos("0");
			}else{//0004694
				bean.setBiaos("1");
			}
			if("1".equals(bean.getBiaos())){
				if(null == bean.getWulbh() || "".equals(bean.getWulbh())){
					throw new ServiceException("若数据为有效，则物流描述编号必填");
				}
			}
			bean.setEditor(userId);
			//0007775当零件-消耗点 从失效恢复成有效时，交付、终止、表达、系统调整值4个值都要置0.
			if("0".equals(bean.getYuanbiaos()) && "1".equals(bean.getBiaos())){
				bean.setJiaofzl(0.0);//交付
				bean.setZhongzzl(0.0);//终止
				bean.setXittzz(0.0); //系统调整值
				bean.setYifyhlzl(0.0);//已发要货令总量	
				bean.setXianbllkc(0.0);//线边理论库存
				CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdsx", bean);
				//当条记录对应的version版本号
				String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion",bean);
				if (Integer.valueOf(versionmax) == 999999999) {
					//版本号自动循环 如果超过了最大的上限  则在从 1 开始
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",bean);
					bean.setVersion(1);
				}else{
					bean.setVersion(versionxhd.getVersion());
				}
				int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdZzzl",bean);	//修改数据库
				//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
				if(0==num){
					throw new ServiceException ("该数据正在被多个用户同时操作，请稍后在更新!");
				}
			}
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			checkYuanxhd(bean);
			Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
			
			String key = GetPostOnly.getPostOnly(map);
			if(!"ZXCPOA".equals(key)){
				//零件消耗点必变更优化  原消耗点编号不为空   只在准备层
				if(null!=bean.getYuanxhdbh()&&!"".equals(bean.getYuanxhdbh())){
					bean.setCreator(userId);
					biangLingjxhd(bean);
				}
			}
			CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdsx", bean);
			//当条记录对应的version版本号
			String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion",bean);
			if (Integer.valueOf(versionmax) == 999999999) {
				//版本号自动循环 如果超过了最大的上限  则在从 1 开始
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",bean);
				bean.setVersion(1);
			}else{
				bean.setVersion(versionxhd.getVersion());
			}
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdEdit",bean);	//修改数据库
			//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
			if(0==num){
				throw new ServiceException ("该数据正在被多个用户同时操作，请稍后在更新!");
			}
			ckxshiwtxService.update(bean.getUsercenter(), CkxShiwtxService.TIXLX_LINGJXHD, bean.getLingjbh(), bean.getXiaohdbh(), "1");
		}
		return "";
	}
	
	
	/**
	 * 在零件消耗点页面，做消耗点的替换后，根据新老消耗点去查询对应的PDS拆分标记 字段，如果该字段值为 按需 的标记。则需要把相应的字段插入表ckx_lingjxhd_s表中
	 * @param bean
	 */
	private void biangLingjxhd(CkxLingjxhd ckxLingjxhd){
		//根据新老消耗点去查询对应的PDS拆分标识
		Map map = new HashMap();
		map.put("usercenter", ckxLingjxhd.getUsercenter());
		map.put("lingjbh", ckxLingjxhd.getLingjbh());
		map.put("xiaohdbh", ckxLingjxhd.getXiaohdbh());
		map.put("biaos", "1");
		CkxLingjxhd Lingjxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map);
		Map map1 = new HashMap();
		map1.put("usercenter", ckxLingjxhd.getUsercenter());
		map1.put("lingjbh", ckxLingjxhd.getLingjbh());
		map1.put("xiaohdbh", ckxLingjxhd.getYuanxhdbh());
		map1.put("biaos", "1");
		CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
		//必须判断新消耗点编号是否存在
		if(null==Lingjxhds){
			throw new ServiceException ("原消耗点编号 "+ckxLingjxhd.getYuanxhdbh()+" 不存在或已失效");
		}
		if(null==Lingjxhd){
			throw new ServiceException ("消耗点编号 "+ckxLingjxhd.getXiaohdbh()+" 不存在或已失效");
		}
		//增加 新老消耗点对应的产线是否一致以及新消耗点对应的分配区必须在内部物流中存在且有效
		String yfenpq = ckxLingjxhd.getYuanxhdbh().substring(0, 5);
		String fenpq = ckxLingjxhd.getXiaohdbh().substring(0, 5);
		Map<String,String> cxmap = getFenpqToChanxMap();
		String ys = cxmap.get(ckxLingjxhd.getUsercenter()+yfenpq);
		String s = cxmap.get(ckxLingjxhd.getUsercenter()+fenpq);
		if(null==s){
			throw new ServiceException ("用户中心："+ckxLingjxhd.getUsercenter()+"分配区："+fenpq+"没有对应有效产线");
		}
		if(!ys.equals(s)){
			throw new ServiceException ("消耗点编号"+ckxLingjxhd.getXiaohdbh()+" 与原消耗点编号"+ckxLingjxhd.getYuanxhdbh()+" 在同一用户中心："+ckxLingjxhd.getUsercenter()+" 下对应的产线不一致");
		}
		CkxInnerPathAndModle InnerPath = new CkxInnerPathAndModle();
		InnerPath.setUsercenter(ckxLingjxhd.getUsercenter());
		InnerPath.setLingjbh(ckxLingjxhd.getLingjbh());
		InnerPath.setFenpqh(fenpq);
		InnerPath.setShengcxbh(s);
		List Innerlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.queryCkxInnerPathAndModle",InnerPath);
		if(Innerlist.size()==0){
			throw new ServiceException ("消耗点编号："+ckxLingjxhd.getXiaohdbh()+" 分配区："+fenpq+" 在内部物流中不存在");
		}
		//根据新老消耗点去查询对应的PDS拆分标识  如果标识为 按需  则 对应的插入表ckx_lingjxhd_s
		//插入的时候 需要注意判断 如果 ckx_lingjxhd_s 表中不存在此条记录 则 直接插入 初始化为 未生效状态,并且 shengxr 和 jiesr 默认都是空.待用户去该页面手动生效时填写
		//如ckx_lingjxhd_s表中存在此条记录,则清空 shengxr 和 jiesr  并且标识为 未生效
		//只要PDS拆分标识中的值不为空 就可以替换。新消耗点或老消耗点 中的任意一个
		if((!"".equals(Lingjxhd.getPdsbz())&& null!=Lingjxhd.getPdsbz())||(!"".equals(Lingjxhds.getPdsbz())&& null!=Lingjxhds.getPdsbz())){
			//if(Lingjxhd.getPdsbz().equals("按需")||Lingjxhds.getPdsbz().equals("按需")){
				CkxLingjxhds ckxLingjxhds = new CkxLingjxhds();
				ckxLingjxhds.setUsercenter(ckxLingjxhd.getUsercenter());
				ckxLingjxhds.setLingjbh(ckxLingjxhd.getLingjbh());
				ckxLingjxhds.setXiaohd(ckxLingjxhd.getXiaohdbh());
				ckxLingjxhds.setYuanxhd(ckxLingjxhd.getYuanxhdbh());
				ckxLingjxhds.setBiaos("0");
				ckxLingjxhds.setCreator(ckxLingjxhd.getCreator());
				ckxLingjxhds.setEditor(ckxLingjxhd.getEditor());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.mergelingjXhds",ckxLingjxhds);
			//}
		}
//		else{
//			throw new ServiceException("零件消耗点变更中的 新老消耗点的PDS拆分标识不能全部为空,并且PDS拆分标识只能为  按需");
//		}
	}
	
	
	/**
	 * 检测原消耗点是否存在，起始日期是否合法
	 * @param bean
	 */
	private void checkYuanxhd(CkxLingjxhd ckxLingjxhd){
		if(null == ckxLingjxhd.getYuanxhdbh() || "".equals(ckxLingjxhd.getYuanxhdbh())){
			return ;
		}
		//0007552 消耗点替换功能--新老消耗点不能相同
		if(ckxLingjxhd.getXiaohdbh().equals(ckxLingjxhd.getYuanxhdbh())){
			throw new ServiceException("消耗点替换功能--新老消耗点不能相同");
		}
		if(null == ckxLingjxhd.getShengxr()){
			//"用户中心："+ckxLingjxhd.getUsercenter()+",零件编号："	+ckxLingjxhd.getLingjbh()+",消耗点："+ckxLingjxhd.getXiaohdbh()+"的数据的生效日不能为空"
			throw new ServiceException(GetMessageByKey.getMessage("lingjxhddsxrqnotNull",new String[]{
					ckxLingjxhd.getUsercenter(),ckxLingjxhd.getLingjbh(),ckxLingjxhd.getXiaohdbh()
			}));
			
		}
		CkxLingjxhd bean = new CkxLingjxhd();
		bean.setUsercenter(ckxLingjxhd.getUsercenter());
		bean.setLingjbh(ckxLingjxhd.getLingjbh());
		bean.setXiaohdbh(ckxLingjxhd.getYuanxhdbh());
		bean.setBiaos("1");
		CkxLingjxhd lingjxhd = this.get(bean);
		if(null == lingjxhd){
			//"不存在用户中心："+bean.getUsercenter()+",零件编号："+bean.getLingjbh()+",消耗点："	+bean.getXiaohdbh()+"的数据或数据已失效"
			throw new ServiceException(GetMessageByKey.getMessage("lingjxhdbczdataexistfailure",new String[]{
					bean.getUsercenter(),bean.getLingjbh(),bean.getXiaohdbh()
			}));
		}
		if(null == lingjxhd.getShengxr() || null == lingjxhd.getJiesr()){
			//"用户中心："+bean.getUsercenter()+",零件编号："	+bean.getLingjbh()+",消耗点："+bean.getXiaohdbh()+"的数据的生效日和结束日不能为空"
			throw new ServiceException(GetMessageByKey.getMessage("lingjxhddsxrqhjsrqnotNull",new String[]{
					bean.getUsercenter(),bean.getLingjbh(),bean.getXiaohdbh()
			}));
		}
		if(!(DateTimeUtil.strToDate(ckxLingjxhd.getShengxr()).getTime()
						>=DateTimeUtil.strToDate(lingjxhd.getShengxr()).getTime()
				&& DateTimeUtil.strToDate(lingjxhd.getShengxr()).getTime()
						<=DateTimeUtil.strToDate(ckxLingjxhd.getJiesr()).getTime())){
			//"起始日期："+ckxLingjxhd.getShengxr()+"不在原消耗点："+ckxLingjxhd.getYuanxhdbh()+"对应的零件消耗点的生效日和结束日之间"
			throw new ServiceException(GetMessageByKey.getMessage("qisrqbzyxhddydljxhddsxrhjsrzjbetween",new String[]{
					ckxLingjxhd.getShengxr(),ckxLingjxhd.getYuanxhdbh()
			}));
		}
		if(ckxWulljService.checkWulljXhdCk(ckxLingjxhd.getUsercenter(),ckxLingjxhd.getLingjbh(),ckxLingjxhd.getYuanxhdbh(),"X")){
			kanbxhgmService.getKanbxhgm(ckxLingjxhd.getUsercenter(),ckxLingjxhd.getLingjbh(),ckxLingjxhd.getYuanxhdbh());
		}
	}
	/**
	 * @description私有批量删除方法
	 * @author denggq
	 * @date 2012-4-25
	 * @param delete,userId
	 * @return ""
	 */
	public String deletes(List<CkxLingjxhd> delete,String userId)throws ServiceException{
		for(CkxLingjxhd bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxLingjxhd",bean);	//删除数据库
			
			if( null != bean.getYuanxhdbh()){			//原消耗点编号存在
				Biangjlb b = new Biangjlb();			//变更记录表
				b.setUsercenter(bean.getUsercenter());	//用户中心
				b.setLingjbh(bean.getLingjbh());		//零件编号
				b.setXianbh(bean.getXiaohdbh());		//消耗点编号
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBiangjlb", b);	//删除变更
			}
		}
		return "";
	}
	
	/**
	 * @description 同用户中心下同一零件在某个生产线下的消耗比例为100%
	 * @author denggq
	 * @date 2012-4-25
	 * @return ""
	 */
	@SuppressWarnings("unchecked")
	private void checkXiaohbl(ArrayList<CkxLingjxhd> insert , ArrayList<CkxLingjxhd> edit , ArrayList<CkxLingjxhd> delete,String usercenter){
		Map<String,CkxLingjxhd> map = new HashMap<String, CkxLingjxhd>();
		List<String> list = new  ArrayList<String>();
		for (CkxLingjxhd ckxLingjxhd : insert) {
			String key = ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh()+ckxLingjxhd.getShengcxbh();
			if(!map.containsKey(key)){
				map.put(key, ckxLingjxhd);
				list.add(key);
			}
		}
		for (CkxLingjxhd ckxLingjxhd : edit) {
			String key = ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh()+ckxLingjxhd.getShengcxbh();
			if(!map.containsKey(key)){
				map.put(key, ckxLingjxhd);
				list.add(key);
			}
		}
		for (CkxLingjxhd ckxLingjxhd : delete) {
			String key = ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh()+ckxLingjxhd.getShengcxbh();
			if(!map.containsKey(key)){
				map.put(key, ckxLingjxhd);
				list.add(key);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i =0 ;i<list.size() ;i++) {
			CkxLingjxhd ckxLingjxhd = map.get(list.get(i));
			//根据用户中心查同一零件在同一生产线下得消耗比例之和不为0或1的数据
			List<CkxLingjxhd> lists =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.checkXiaohblSum",ckxLingjxhd);
			if(0 != lists.size()){
				sb.append("\t"+GetMessageByKey.getMessage("yonghuzhongxin")+ckxLingjxhd.getUsercenter()+GetMessageByKey.getMessage("xlj")+ckxLingjxhd.getLingjbh()+GetMessageByKey.getMessage("scx")+ckxLingjxhd.getShengcxbh()+GetMessageByKey.getMessage("xiaohzhwei"));
				sb.append("\n");
			}
		}
		if(0 < sb.length()){
			throw new ServiceException(sb.toString());
		}
		
	}

	
	
	
	@SuppressWarnings("rawtypes")
	private String getshengcxbh(String usercenter,String fenpqh) throws ServiceException {
		Fenpq f = new Fenpq();
		f.setUsercenter(usercenter);
		f.setFenpqh(fenpqh);
		f.setBiaos("1");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getShengcxbh",f);
		if(0 == list.size()){
			return "";
		}
		if(1 < list.size()){
			throw new ServiceException(GetMessageByKey.getMessage("fenpeiqu")+fenpqh+GetMessageByKey.getMessage("duotcx"));
		}
		return list.get(0).toString() ;
	}
	
	/**
	 * 获取分配区
	 * @param usercenter	用户中心
	 * @param fenpqh		分配区号
	 * @return
	 */
	private Fenpq getFenpq(String usercenter,String fenpqh){
		Fenpq f = new Fenpq();
		f.setUsercenter(usercenter);
		f.setFenpqh(fenpqh);
		f.setBiaos("1");
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getFenpq",f);
		if(0 == list.size()){
			return null;
		}
		if(1 < list.size()){
			throw new ServiceException(GetMessageByKey.getMessage("fenpeiqu")+fenpqh+GetMessageByKey.getMessage("duotcx"));
		}
		return (Fenpq) list.get(0) ;
	}
	/**
	 * 查询零件消耗点,且在内部物流路径中不存在
	 * <br/>物流路径专用
	 * @param bean
	 * @return
	 * @author kong
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryLingjxhdByCarry(CkxLingjxhd bean){
        Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		if("WULGYY".equals(key)){
			String value = (String) map.get(key);
			Fenpq fenpq = new Fenpq();
			fenpq.setUsercenter(bean.getUsercenter());
			fenpq.setFenpqh(bean.getFenpqbh());
			fenpq.setWulgyyz(value);
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fenpq);
			if(0 == list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("wufenpeisj"));
			}else{
				bean.setWulgyyz(value);
			}
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryLingjxhdByCarry", bean, bean);
	}
	
	
	/**
	 * 生效
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public String junfen(List<CkxLingjxhd> updateCkxLingjxhd,String username) throws ServiceException{
		Map<String,CkxLingjxhd> map = new HashMap<String, CkxLingjxhd>();
		List<String> list = new  ArrayList<String>();
		 for (CkxLingjxhd ckxLingjxhd : updateCkxLingjxhd){
			 //先对需要均分比例的零件  按用户中心+零件+产线  统计出来 
			 String key = ckxLingjxhd.getUsercenter()+ckxLingjxhd.getLingjbh()+ckxLingjxhd.getShengcxbh();
			 if(!map.containsKey(key)){
					map.put(key, ckxLingjxhd);
					list.add(key);
				}
		 }
		 //在对统计的不同的零件来进行均分
		 	for (int i = 0; i < list.size(); i++) {
				//均分的规则 是 根据查询出来的 同一用户中心+零件+生产线  的个数 进行均分 均分的时候 向下取整，多余的消耗比例按照 消耗点的升序排序后，给第一个
				//先根据 用户中心+零件编号+产线  查询出个数
				CkxLingjxhd lingjxhd = new CkxLingjxhd();
				lingjxhd.setUsercenter(list.get(i).substring(0, 2));
				lingjxhd.setLingjbh(list.get(i).substring(2, 12));
				lingjxhd.setShengcxbh(list.get(i).substring(12, 17));
				lingjxhd.setBiaos("1");
				//先查询出需要均分的消耗点比例  按消耗点编号的 升序 排序
				List<CkxLingjxhd> lingjxhdlist =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryjunfCkxLingjxhd", lingjxhd);
				int count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.querycountLingjxhd", lingjxhd);
				if(0!=count){
					BigDecimal m = new BigDecimal(100);
					BigDecimal n = new BigDecimal(count);
					BigDecimal[] shuls = m.divideAndRemainder(n); 
					System.out.println(shuls[0]);
					System.out.println(shuls[1]);
					//如果余数为0,则表示数量整除,直接均分
					if(shuls[1].compareTo(BigDecimal.ZERO) == 0){
						//直接更新消耗点的比例值
						for (int k = 0; k < lingjxhdlist.size(); k++){
							lingjxhdlist.get(k).setXiaohbl((double)(shuls[0].intValue()/100.0));
							lingjxhdlist.get(k).setEditor(username);
							lingjxhdlist.get(k).setEdit_time(DateTimeUtil.getAllCurrTime());
						}
					}else{
						for (int j = 0; j < lingjxhdlist.size(); j++) {
							if(j==0){
								lingjxhdlist.get(j).setXiaohbl((double)shuls[0].add(shuls[1]).intValue()/100.0);
								lingjxhdlist.get(j).setEditor(username);
								lingjxhdlist.get(j).setEdit_time(DateTimeUtil.getAllCurrTime());
							}else{
								lingjxhdlist.get(j).setXiaohbl((double)shuls[0].intValue()/100.0);
								lingjxhdlist.get(j).setEditor(username);
								lingjxhdlist.get(j).setEdit_time(DateTimeUtil.getAllCurrTime());
							}
						}
					}
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.updatejunfxhd", lingjxhdlist);
				}
		 }
		return "success";
	}
	
	
	/**
	 * 失效
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public String shixiao(List<CkxLingjxhd> updateCkxLingjxhd,String username) throws ServiceException{
		//失效的操作 是 直接 将 结束日期修改为 当前日期-1     消耗比例设置为0      标识变为  无效  并且出发均分其下有效的消耗比例
		for (CkxLingjxhd ckxLingjxhd : updateCkxLingjxhd) {
			if("2".equals(ckxLingjxhd.getBiaos()) || "0".equals(ckxLingjxhd.getBiaos())){
				throw new ServiceException("新建 或  无效的  数据不能失效");
			}
			ckxLingjxhd.setJiesr(DateUtil.dateAddDays(DateTimeUtil.getCurrDate(), -1)); //获取当前日期前一天
			ckxLingjxhd.setXiaohbl(0.0);			//消耗比例
			ckxLingjxhd.setBiaos("0");  //标识 表为 无效
			ckxLingjxhd.setEditor(username);
			ckxLingjxhd.setEdit_time(DateTimeUtil.getAllCurrTime());
			CkxLingjxhd versionxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryCkxLjxhdsx", ckxLingjxhd);
			//当条记录对应的version版本号
			String versionmax = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLjxhdversion",ckxLingjxhd);
			if (Integer.valueOf(versionmax) == 999999999) {
				//版本号自动循环 如果超过了最大的上限  则在从 1 开始
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateversion",ckxLingjxhd);
				ckxLingjxhd.setVersion(1);
			}else{
				ckxLingjxhd.setVersion(versionxhd.getVersion());
			}
			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdbyshixiao",ckxLingjxhd);
			//如果更新失败，则表示同时有人在操作该条数据  应该提示给用户
			if(0==num){
				return  "数据正在被多个用户同时操作，请稍后在更新!";
			}
		}
		//修改完成后 自动触发均分比例方法
		junfen(updateCkxLingjxhd,username);
		return "success";
	}
	
	
	/**
	 * 获得多个零件消耗点
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(CkxLingjxhd bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjxhd",bean);
	}
	@SuppressWarnings("unchecked")
	public List<CkxLingjxhd> listImport(CkxLingjxhd bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjxhdImport",bean);
	}
	
	/**
	 * 根据用户中心，分配区，获取产线
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getFenpqToChanxMap(){
		Fenpq bean = new Fenpq();
		bean.setBiaos("1");
		List<Fenpq>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (Fenpq fenpq : list) {
			map.put(fenpq.getUsercenter()+fenpq.getFenpqh(), fenpq.getShengcxbh());
		}
		return map;
	}
	
	/**
	 * 查询 零件消耗点表中有效且存在的数据
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getCkxlingjxhToJipxx(){
		CkxLingjxhd bean = new CkxLingjxhd();
		//bean.setBiaos("1");
		List<CkxLingjxhd>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLjxhdjp",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxLingjxhd jipxx : list) {
			map.put(jipxx.getUsercenter()+jipxx.getLingjbh()+jipxx.getXiaohdbh(), jipxx.getUsercenter()+jipxx.getLingjbh()+jipxx.getXiaohdbh());
		}
		return map;
	}
	
	/**
	 * 查询 零件消耗点表中有效且存在的数据
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getCkxlingjxhToTongbxx(){
		CkxLingjxhd bean = new CkxLingjxhd();
		//bean.setBiaos("1");
		List<CkxLingjxhd>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLjxhdTB",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxLingjxhd tongbxx : list) {
			map.put(tongbxx.getUsercenter()+tongbxx.getLingjbh()+tongbxx.getXiaohdbh(), tongbxx.getUsercenter()+tongbxx.getLingjbh()+tongbxx.getXiaohdbh());
		}
		return map;
	}
	
	
	/**
	 * 查询出2个层的配送类别后存在一个集合里面看，然后在存在缓存里面
	 */
	
	public Map<String,Object> getPeislbToWebs(){
		List<Peislb> peislbsum =  new ArrayList<Peislb>();
		String urlPathuw = LoaderProperties.getPropertiesMap(fileName).get("urlPathUW");
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(PeislxWebservice.class);
		factory.setAddress(urlPathuw+"peislxWebservice");
		PeislxWebservice client = (PeislxWebservice) factory.create();
		List<Peislb> str = client.queryPeislx(); 
		for (Peislb psbuw : str){
			peislbsum.add(psbuw);
		}
		String urlPathul = LoaderProperties.getPropertiesMap(fileName).get("urlPathUL");
		ClientProxyFactoryBean factoryul = new ClientProxyFactoryBean();
		factoryul.setServiceClass(PeislxWebservice.class);
		factoryul.setAddress(urlPathul+"peislxWebservice");
		PeislxWebservice clientul = (PeislxWebservice) factoryul.create();
		List<Peislb> strul = clientul.queryPeislx(); 
		for (Peislb psbuw : strul){
			peislbsum.add(psbuw);
		}
		
		//添加VDwebservie 调用 start
		String urlPathvd = LoaderProperties.getPropertiesMap(fileName).get("urlPathVD");
		ClientProxyFactoryBean factoryvd = new ClientProxyFactoryBean();
		factoryvd.setServiceClass(PeislxWebservice.class);
		factoryvd.setAddress(urlPathvd+"peislxWebservice");
		PeislxWebservice clientvd = (PeislxWebservice) factoryvd.create();
		List<Peislb> strvd = clientvd.queryPeislx(); 
		for (Peislb psbuw : strvd){
			peislbsum.add(psbuw);
		}
		//添加VDwebservie 调用 end
		
		
		Map<String,Object> map=new HashMap<String,Object>();
		for (Peislb pslbzh : peislbsum) {
			map.put(pslbzh.getUsercenter()+pslbzh.getPeislx(), pslbzh);
		}
		return map;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getGyxhdMap(){
		CkxGongyxhd bean = new CkxGongyxhd();
		bean.setBiaos("1");
		List<CkxGongyxhd>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxGongyxhd",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxGongyxhd gyxhd : list) {
			map.put(gyxhd.getGongyxhd(), gyxhd.getGongyxhd());
		}
		return map;
	}
	

	@SuppressWarnings("unchecked")
	public Map<String,String> getXhdMap(){
		CkxLingjxhd bean = new CkxLingjxhd();
		bean.setBiaos("1");
		List<CkxLingjxhd>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjxhd",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (CkxLingjxhd ljxhd : list) {
			map.put(ljxhd.getUsercenter()+ljxhd.getLingjbh()+ljxhd.getXiaohdbh(), ljxhd.getUsercenter()+ljxhd.getLingjbh()+ljxhd.getXiaohdbh());
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getXiaohMap(){
		Xiaohc bean = new Xiaohc();
		bean.setBiaos("1");
		List<Xiaohc>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohc",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (Xiaohc xhc : list) {
			map.put(xhc.getUsercenter()+xhc.getShengcxbh()+xhc.getXiaohcbh(), xhc.getUsercenter()+xhc.getShengcxbh()+xhc.getXiaohcbh());
		}
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getXiaohcbhMap(){
		Xiaohccx bean = new Xiaohccx();
		bean.setBiaos("1");
		List<Xiaohccx>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohccx",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (Xiaohccx xhccx : list) {
			map.put(xhccx.getUsercenter()+xhccx.getShengcxbh()+xhccx.getXiaohcbh()+xhccx.getChexh(), xhccx.getUsercenter()+xhccx.getShengcxbh()+xhccx.getXiaohcbh()+xhccx.getChexh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getLingjMap(){
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
	 * 根据用户中心+零件
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getGongysMap(){
		Gongcy ckxgongys = new Gongcy();
		ckxgongys.setBiaos("1");
		List<Gongcy>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongcy",ckxgongys);
		Map<String,String> map=new HashMap<String,String>();
		for (Gongcy gys : list) {
			map.put(gys.getUsercenter()+gys.getGcbh(),gys.getUsercenter()+gys.getGcbh());
		}
		return map;
	}
	
	/**
	 * PDS 生效/失效时间
	 */
	@SuppressWarnings("unchecked")
	public  Map<String, Object> queryPdssj(Map m){
		List<Gongcy>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryPdssj",m);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}

	
	
}

package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.component.wtc.WtcResponse;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;


/**
 * 用户中心
 * @author denggq
 * 2012-3-19
 */
@Component
public class LingjxhdbgService extends BaseService<CkxLingjxhds>{
	
	static Logger log = Logger.getLogger(LingjxhdbgService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存包装
	 * @param list
	 * @param username
	 * @return String
	 * @author denggq
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(ArrayList<CkxLingjxhds> insert,ArrayList<CkxLingjxhds> edit, ArrayList<CkxLingjxhds> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
//		log.info("参考系-用户中心-失效数据");
		//deletes(delete,username);
		//log.info("参考系-打印单据控制-增加数据");
		inserts(insert,username);
		log.info("参考系-零件消耗点变更-修改标识");
		edits(edit,username);
		log.info("参考系-刷新用户中心缓存");
		cacheManager.refreshCache("queryUserCenterMap");//刷新缓存
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
	public String saves(CkxLingjxhds bean,String userName,Integer operant) throws ServiceException{
		
		if(null != bean.getLingjbh()){									//零件编号是否存在
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+ GetMessageByKey.getMessage("notexist"));
		}									
		bean.setEditor(userName);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		if (1 == operant) {												//增加
			//验证消耗点是否存在
			CkxGongyxhd gyxhd= new CkxGongyxhd();
			gyxhd.setGongyxhd(bean.getXiaohd());
			gyxhd.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxGongyxhd", gyxhd,"不存在工艺消耗点："+bean.getXiaohd()+",或数据已失效");
			
			String currentDate = DateTimeUtil.getCurrDate();
			if(null != bean.getShengxr() && DateTimeUtil.compare(currentDate, bean.getShengxr())){			//消耗点起始日为未来日期则失效状态
				bean.setBiaos("0");
			}else{							//消耗点起始日已过为生效状态
				bean.setBiaos("1");
			}
			bean.setJiesr("9999-12-31");	//消耗点结束日
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingjxhd",bean);//增加数据库
		}else if (2 == operant){			//修改
			if("1".equals(bean.getBiaos())){
				
			}else if("2".equals(bean.getBiaos())){
				bean.setJiesr("9999-12-31");	//消耗点结束日
				bean.setBiaos("1");
			}
			
		}
		return "success";
	}
	
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<CkxLingjxhds> insert,String username)throws ServiceException{
		for(CkxLingjxhds bean:insert){
			//生效之前 必须保证 消耗点起始日和结束日都不为空
			if(null == bean.getShengxr() || "".equals(bean.getShengxr())){
				throw new ServiceException("消耗点起始日不能为空");
			}
			if((null == bean.getJiesr() || "".equals(bean.getJiesr()))){
				throw new ServiceException("消耗点结束日不能为空");
			}
			if(null==bean.getBiaos()){
				throw new ServiceException("标识不能为空");
			}
			//新增的时候 标识只能填写有效 新增默认有效
			if(!"1".equals(bean.getBiaos())){
				throw new ServiceException("新增的数据只能为有效");
			}
			//新零件编号不为空的时候    有3种情况   原零件编号+原消耗点 不能等于 新零件编号+新消耗点
			if(null!=bean.getXinljbh()){
				if(bean.getLingjbh().equals(bean.getXinljbh())){
					if(bean.getXiaohd().equals(bean.getYuanxhd())){
						throw new ServiceException("新老零件一样， 消耗点编号 不能相同");
					}
				}
				if( bean.getXiaohd().equals(bean.getYuanxhd())){
					if(bean.getLingjbh().equals(bean.getXinljbh())){
						throw new ServiceException("消耗点编号一样， 新老零件 不能相同");
					}
				}
				if(bean.getLingjbh().equals(bean.getXinljbh()) && bean.getXiaohd().equals(bean.getYuanxhd())){
					throw new ServiceException("同一用户中心下,原零件编号+原消耗点 不能等于 新零件编号+新消耗点");
				}
			}else{
				if(bean.getXiaohd().equals(bean.getYuanxhd())){
					throw new ServiceException("消耗点编号 和 原消耗点编号 不能相同");
				}
			}
			//校验 用户中心+零件编号+原消耗点编号的 存在性 
			CkxLingjxhd yuanljxhd= new CkxLingjxhd();
			yuanljxhd.setUsercenter(bean.getUsercenter());
			yuanljxhd.setLingjbh(bean.getLingjbh());
			yuanljxhd.setXiaohdbh(bean.getYuanxhd());
			DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxyljxhd", yuanljxhd,"用户中心:"+bean.getUsercenter() +" 零件编号:"+bean.getLingjbh() +" 原消耗点："+bean.getYuanxhd()+", 的数据不存在");
			//判断 新零件编号是否为空
			if(null!=bean.getXinljbh()){
				//不为空   则需要校验 用户中心+新零件编号+消耗点 的存在有效性
				CkxLingjxhd xhd= new CkxLingjxhd();
				xhd.setUsercenter(bean.getUsercenter());
				xhd.setLingjbh(bean.getXinljbh());
				xhd.setXiaohdbh(bean.getXiaohd());
				xhd.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxyljxhd", xhd,"用户中心:"+bean.getUsercenter() +" 新零件编号:"+bean.getXinljbh() +" 消耗点："+bean.getXiaohd()+", 的数据不存在或已失效");
			}else{
				//为空 则需要校验 用户中心+零件编号+消耗点编号 的存在有效性
				CkxLingjxhd xhd1= new CkxLingjxhd();
				xhd1.setUsercenter(bean.getUsercenter());
				xhd1.setLingjbh(bean.getLingjbh());
				xhd1.setXiaohdbh(bean.getXiaohd());
				xhd1.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxyljxhd", xhd1,"用户中心:"+bean.getUsercenter() +" 零件编号:"+bean.getLingjbh() +" 消耗点："+bean.getXiaohd()+"不存在消耗点："+bean.getXiaohd()+", 的数据或已失效");
			}
			
			if(DateTimeUtil.strToDate(bean.getShengxr()).getTime()>=DateTimeUtil.strToDate(bean.getJiesr()).getTime()){
				throw new ServiceException("用户中心"+bean.getUsercenter()+"下零件编号为"+bean.getLingjbh()+"的消耗点"
						+bean.getXiaohd()+" 起始日期不能大于结束日期");
			}
			//新增的时候 必填的字段填了后  标识默认为 有效
			bean.setBiaos("1");
			bean.setCreator(username);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//增加的时候，必填项都填了后  表示默认给有效
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxLingjxhds",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<CkxLingjxhds> edit,String username) throws ServiceException{
		for(CkxLingjxhds bean:edit){
			//先查询原数据中的标识的值 如果没有变化  则需要给提示
			CkxLingjxhds ljxhds = (CkxLingjxhds)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingjxhds", bean);
			//失效 和 有效的数据不能修改为 未生效 
			if(null==bean.getBiaos()){
				throw new ServiceException("标识不能为空");
			}
			if("0".equals(bean.getBiaos()) && !"0".equals(ljxhds.getBiaos())){
				throw new ServiceException("失效 和 已生效的数据不能修改为 未生效");
			}
			if("0".equals(bean.getBiaos()) && "0".equals(ljxhds.getBiaos())){
				throw new ServiceException("标识必须修改为生效");
			}
			if("2".equals(bean.getBiaos()) && "2".equals(ljxhds.getBiaos())){
				throw new ServiceException("标识必须修改为已生效才能保存");
			}
			//生效之前 必须保证 消耗点起始日和结束日都不为空
			if(null == bean.getShengxr() || "".equals(bean.getShengxr())){
				throw new ServiceException("消耗点起始日不能为空");
			}
			if((null == bean.getJiesr() || "".equals(bean.getJiesr()))){
				throw new ServiceException("消耗点结束日不能为空");
			}
			//判断 新零件编号是否为空
			if(null!=bean.getXinljbh()){
				if(bean.getLingjbh().equals(bean.getXinljbh())){
					if(bean.getXiaohd().equals(bean.getYuanxhd())){
						throw new ServiceException("新老零件一样， 消耗点编号 不能相同");
					}
				}
				if( bean.getXiaohd().equals(bean.getYuanxhd())){
					if(bean.getLingjbh().equals(bean.getXinljbh())){
						throw new ServiceException("消耗点编号一样， 新老零件 不能相同");
					}
				}
				if(bean.getLingjbh().equals(bean.getXinljbh()) && bean.getXiaohd().equals(bean.getYuanxhd())){
					throw new ServiceException("同一用户中心下,原零件编号+原消耗点 不能等于 新零件编号+新消耗点");
				}
				//不为空   则需要校验 用户中心+新零件编号+消耗点 的存在有效性
				CkxLingjxhd xhd= new CkxLingjxhd();
				xhd.setUsercenter(bean.getUsercenter());
				xhd.setLingjbh(bean.getXinljbh());
				xhd.setXiaohdbh(bean.getXiaohd());
				xhd.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxyljxhd", xhd,"用户中心:"+bean.getUsercenter() +" 新零件编号:"+bean.getXinljbh() +" 消耗点："+bean.getXiaohd()+", 的数据不存在或已失效");
			}else{
				if(bean.getXiaohd().equals(bean.getYuanxhd())){
					throw new ServiceException("消耗点编号 和 原消耗点编号 不能相同");
				}
			}
			//判断 原消耗点编号的存在性
			if(null!=bean.getYuanxhd()){
//				if(bean.getXiaohd().equals(bean.getYuanxhd())){
//					throw new ServiceException("消耗点编号 和 原消耗点编号 不能相同");
//				}
				//如果不相同  需要校验用户中心+零件编号+原消耗点编号的 存在性 
				CkxLingjxhd yuanljxhd= new CkxLingjxhd();
				yuanljxhd.setUsercenter(bean.getUsercenter());
				yuanljxhd.setLingjbh(bean.getLingjbh());
				yuanljxhd.setXiaohdbh(bean.getYuanxhd());
				DBUtil.checkCount(baseDao, "ts_ckx.getCountCkxyljxhd", yuanljxhd,"用户中心:"+bean.getUsercenter() +" 零件编号:"+bean.getLingjbh() +" 原消耗点："+bean.getYuanxhd()+", 的数据不存在");
			}
			if(DateTimeUtil.strToDate(bean.getShengxr()).getTime()>=DateTimeUtil.strToDate(bean.getJiesr()).getTime()){
				throw new ServiceException("用户中心"+bean.getUsercenter()+"下零件编号为"+bean.getLingjbh()+"的消耗点"
						+bean.getXiaohd()+" 起始日期不能大于结束日期");
			}
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//有效变为 无效的时候  需要跟 失效的按钮一样 清空 起始日 和 结束日
			if("2".equals(bean.getBiaos()) && !"0".equals(ljxhds.getBiaos())){
				CkxLingjxhds beans = new CkxLingjxhds();
				if(null!=bean.getXinljbh()){
					beans.setXinljbh(bean.getXinljbh());
				}
				beans.setUsercenter(bean.getUsercenter());
				beans.setLingjbh(bean.getLingjbh());
				beans.setXiaohd(bean.getXiaohd());
				beans.setYuanxhd(bean.getYuanxhd());
				beans.setBiaos(bean.getBiaos());
				beans.setEditor(username);
				beans.setEdit_time(DateTimeUtil.getAllCurrTime());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdshbysx",beans);
			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhdsh",bean);
			}
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-19
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<CkxLingjxhds> delete,String username)throws ServiceException{
		for(CkxLingjxhds bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxLingjxhd",bean);
		}
		return "";
	}
	
	
	/**
	 * 失效
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public String doDelete(CkxLingjxhds bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxLingjxhds", bean);
		return "success";
	}
	
	
	/**
	 * 生效
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public String shengxiao(List<CkxLingjxhds> updateCkxLingjxhds,String username) throws ServiceException{
		CkxLingjxhds bean = new CkxLingjxhds();
		 for (int i = 0; i < updateCkxLingjxhds.size(); i++){
			 //首先判断标识是否为已生效的    因为有效只针对 失效的未生效
			if("1".equals(updateCkxLingjxhds.get(i).getBiaos())){
				throw new ServiceException("已生效的不能进行有效");
			}
			if(!updateCkxLingjxhds.get(i).getShengxr().equals("undefined")){
					bean.setShengxr(updateCkxLingjxhds.get(i).getShengxr());
			}else{
				bean.setShengxr("");
			}
			if(!updateCkxLingjxhds.get(i).getJiesr().equals("undefined")){
				bean.setJiesr(updateCkxLingjxhds.get(i).getJiesr());
			}else{
				bean.setJiesr("");
			}
			//生效之前 必须保证 消耗点起始日和结束日都不为空
			if(null == bean.getShengxr() || "".equals(bean.getShengxr())){
				throw new ServiceException("消耗点起始日不能为空");
			}
			if((null == bean.getJiesr() || "".equals(bean.getJiesr()))){
				throw new ServiceException("消耗点结束日不能为空");
			}
			if(DateTimeUtil.strToDate(bean.getShengxr()).getTime()>=DateTimeUtil.strToDate(bean.getJiesr()).getTime()){
				throw new ServiceException("用户中心"+updateCkxLingjxhds.get(i).getUsercenter()+"下零件编号为"+updateCkxLingjxhds.get(i).getLingjbh()+"的消耗点"
						+updateCkxLingjxhds.get(i).getXiaohd()+" 起始日期不能大于结束日期");
			}
			//生效条件都满足后，必须判定 原消耗点 是否在 消耗点-零件 存在着有效的记录 如果有 则不能生效
//			CkxLingjxhd ckxLingj= new CkxLingjxhd();
//			ckxLingj.setUsercenter(updateCkxLingjxhds.get(i).getUsercenter());
//			ckxLingj.setLingjbh(updateCkxLingjxhds.get(i).getLingjbh());
//			ckxLingj.setXiaohdbh(updateCkxLingjxhds.get(i).getYuanxhd());
//			ckxLingj.setBiaos("1");
//			CkxLingjxhd ckxLingjxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingjxhds", ckxLingj);
//			if(null!=ckxLingjxhd){
//				throw new ServiceException("原消耗点编号为 "+updateCkxLingjxhds.get(i).getYuanxhd()+" 在  消耗点-零件  中已存在生效的记录");
//			}
//			if(!(DateTimeUtil.strToDate(DateTimeUtil.getAllCurrTime()).getTime()>=DateTimeUtil.strToDate(updateCkxLingjxhds.get(i).getShengxr()).getTime()
//					&& DateTimeUtil.strToDate(DateTimeUtil.getAllCurrTime()).getTime()<=DateTimeUtil.strToDate(updateCkxLingjxhds.get(i).getJiesr()).getTime())){
//					//"起始日期："+ckxLingjxhd.getShengxr()+"不在原消耗点："+ckxLingjxhd.getYuanxhdbh()+"对应的零件消耗点的生效日和结束日之间"
//					throw new ServiceException("生效该记录的时间必须在该零件消耗点的生效日和结束日之间");
//			}
			bean.setUsercenter(updateCkxLingjxhds.get(i).getUsercenter());
			bean.setLingjbh(updateCkxLingjxhds.get(i).getLingjbh());
			bean.setXiaohd(updateCkxLingjxhds.get(i).getXiaohd());
			bean.setYuanxhd(updateCkxLingjxhds.get(i).getYuanxhd());
			bean.setBiaos("1");
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//如果都满足 则将记录更新到数据库中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhds", bean);
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
	public String shixiao(List<CkxLingjxhds> updateCkxLingjxhds,String username) throws ServiceException{
		CkxLingjxhds bean = new CkxLingjxhds();
		 for (int i = 0; i < updateCkxLingjxhds.size(); i++){
			 //首先判断标识是否为已失效和 未生效的    因为失效只针对 已生效的进行失效
			if("0".equals(updateCkxLingjxhds.get(i).getBiaos())||"2".equals(updateCkxLingjxhds.get(i).getBiaos())){
				throw new ServiceException("失效的和未生效的不能进行失效");
			}
//			if(!updateCkxLingjxhds.get(i).getShengxr().equals("undefined")){
//					bean.setShengxr(updateCkxLingjxhds.get(i).getShengxr());
//			}else{
//				bean.setShengxr("");
//			}
//			if(!updateCkxLingjxhds.get(i).getJiesr().equals("undefined")){
//				bean.setJiesr(updateCkxLingjxhds.get(i).getJiesr());
//			}else{
//				bean.setJiesr("");
//			}
			//生效之前 必须保证 消耗点起始日和结束日都不为空
//			if(null == bean.getShengxr() || "".equals(bean.getShengxr())){
//				throw new ServiceException("消耗点起始日不能为空");
//			}
//			if((null == bean.getJiesr() || "".equals(bean.getJiesr()))){
//				throw new ServiceException("消耗点结束日不能为空");
//			}
//			if(DateTimeUtil.strToDate(bean.getShengxr()).getTime()>DateTimeUtil.strToDate(bean.getJiesr()).getTime()){
//				throw new ServiceException("用户中心"+updateCkxLingjxhds.get(i).getUsercenter()+"下零件编号为"+updateCkxLingjxhds.get(i).getLingjbh()+"的消耗点"
//						+updateCkxLingjxhds.get(i).getXiaohd()+" 起始日期不能大于结束日期");
//			}
//			if(!(DateTimeUtil.strToDate(DateTimeUtil.getAllCurrTime()).getTime()>=DateTimeUtil.strToDate(updateCkxLingjxhds.get(i).getShengxr()).getTime()
//					&& DateTimeUtil.strToDate(DateTimeUtil.getAllCurrTime()).getTime()<=DateTimeUtil.strToDate(updateCkxLingjxhds.get(i).getJiesr()).getTime())){
//					//"起始日期："+ckxLingjxhd.getShengxr()+"不在原消耗点："+ckxLingjxhd.getYuanxhdbh()+"对应的零件消耗点的生效日和结束日之间"
//					throw new ServiceException("生效该记录的时间必须在该零件消耗点的生效日和结束日之间");
//			}
			bean.setUsercenter(updateCkxLingjxhds.get(i).getUsercenter());
			bean.setLingjbh(updateCkxLingjxhds.get(i).getLingjbh());
			bean.setXiaohd(updateCkxLingjxhds.get(i).getXiaohd());
			bean.setYuanxhd(updateCkxLingjxhds.get(i).getYuanxhd());
			bean.setBiaos("2");
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//如果都满足 则将记录更新到数据库中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhds", bean);
		}
		//bean.setBiaos("2");
		//bean.setEditor(username);
		//bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		//如果都满足 则将记录更新到数据库中
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxLingjxhds", bean);
		return "success";
	}
}

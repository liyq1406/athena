package com.athena.xqjs.module.anxorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.frontend.ClientProxyFactoryBean;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.component.utils.LoaderProperties;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * Title:初始化资源表service
 * @author 李智
 * @version v1.0
 * @date 2012-03-23
 */
@SuppressWarnings("rawtypes")
@Component
public class ChushzybService extends BaseService {
	
	//配置文件路径 hzg 2015.7.16
	private final String fileName="urlPath.properties"; 
	
	/**
	 * 插入操作
	 * @author 李智
	 * @version v1.0
	 * @date 2012-3-23
	 * 
	 */
	@Transactional
	public int doInsert(Map<String,String> param,Chushzyb bean,LoginUser user) {
		int count = 0 ;
		bean.setCreator(user.getUsername());
		String time = CommonFun.getJavaTime();
		bean.setCreate_time(time);
		bean.setEditor(user.getUsername());
		bean.setEdit_time(time);
		//校验流水号是否存在
		String liush = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.checkLsh",param));
		if(!StringUtils.isEmpty(liush)){
			Chushzyb chushzyb =  (Chushzyb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryChushzybByParam", param);
			if(chushzyb == null){
				/**** 调用webservice取值  start  ****/
				bean = this.getYicxhOfAnxcsh(bean);
				/************** 调用webservice end  **********/
				count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertChushzyb", bean);
			}else{
				count = 2;
			}
		}else{
			count = 3;
		}
		return count; 
	}
	/************************** mantis:0011510  modify by hzg 2015.7.15 start ***********************************/	
	/**
	 * 按需初始化修改
	 * @author 贺志国
	 * @date 2015-7-15
	 * @param param
	 * @param bean
	 * @param user
	 * @return
	 */
	@Transactional
	public String doEdit(Chushzyb bean,LoginUser user) {
		String liushStr = "";
		bean.setCreator(user.getUsername());
		String time = CommonFun.getJavaTime();
		bean.setCreate_time(time);
		bean.setEditor(user.getUsername());
		bean.setEdit_time(time);
		String xhdbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.checkULX",bean));
		if(StringUtils.isEmpty(xhdbh)){
			throw new ServiceException(bean.getLingjbh()+"零件编号,"+bean.getShengcxbh()+"生产线编号,"+bean.getXiaohdbh()+"消耗点编号不存在，无法修改！");
		}else{
			String liush = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.checkLsh",bean));
			if(!StringUtils.isEmpty(liush)){
				/**** 调用webservice取值  start  ****/
				bean = this.getYicxhOfAnxcsh(bean);
				/************** 调用webservice end  **********/
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.updateChushzyb", bean) ;
			}else{
				liushStr = bean.getLiush(); 
			}
		}
		return liushStr; 
	}
	
	
	/**
	 * 按需初始化编辑功能webservice调用ZXC
	 * @author 贺志国
	 * @date 2015-7-16
	 * @param bean
	 * @return
	 */
	public Chushzyb getYicxhOfAnxcsh(Chushzyb bean){
		//判断用户中心为哪个执行层
		Map<String,String> param = new HashMap<String, String>();
		if("UL".equals(bean.getUsercenter())||"UX".equals(bean.getUsercenter())){
			//上次计算时间
			param.put("dingdh", Const.ANX_UL_DINGDH);
			Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",param);
			param.remove("dingdh");
			bean.setDingdjssj(dingd.getDingdjssj());
			bean = this.getwebserviceBean(bean, "urlPathUL");
		}else if("UW".equals(bean.getUsercenter())){
			//上次计算时间
			param.put("dingdh", Const.ANX_UW_DINGDH);
			Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",param);
			param.remove("dingdh");
			bean.setDingdjssj(dingd.getDingdjssj());
			bean = this.getwebserviceBean(bean, "urlPathUW");
		}else if("VD".equals(bean.getUsercenter())){//添加VDwebservie 调用 
			//上次计算时间
			param.put("dingdh", Const.ANX_VD_DINGDH);
			Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",param);
			param.remove("dingdh");
			bean.setDingdjssj(dingd.getDingdjssj());
			bean = this.getwebserviceBean(bean, "urlPathVD");
		}
		return bean;
	}
	
	/**
	 * 通过webservice获取ZXC初始化资源的异常消耗值
	 * @author 贺志国
	 * @date 2015-7-16
	 * @param bean
	 * @param urlpath
	 * @return
	 */
	public Chushzyb getwebserviceBean(Chushzyb bean,String urlpath){
		String urlPathAddress = LoaderProperties.getPropertiesMap(fileName).get(urlpath);
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(AnxCshWebservice.class);
		factory.setAddress(urlPathAddress+"anxCshWebservice");
		AnxCshWebservice client = (AnxCshWebservice) factory.create();
		bean = client.queryAnxcshYcxhl(bean);    
		return bean;
	}
	
	
	/************************** mantis:0011510  modify by hzg 2015.7.15 end ***********************************/	
	
	/**
	 * 初始化资源删除操作
	 * @author 李智
	 * @version v1.0
	 * @date 2012-3-23
	 * @参数：实体
	 */
	@Transactional
	public boolean doRemove(List<Chushzyb> chushzybs){
		//baseDao.    // map封装成list 批量提交  
		boolean flag = false;
		try{
			this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("anx.deleteChushzyb",chushzybs);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 初始化资源修改操作
	 * @author 李智
	 * @version v1.0
	 * @date 2012-3-23
	 * @参数：实体
	 */
	@Transactional
	public StringBuilder doUpdate(List<Chushzyb> chushzybs,LoginUser user){
		String time = CommonFun.getJavaTime();
		StringBuilder str = new StringBuilder("");
		for(int i=0; i<chushzybs.size(); i++) {
			Chushzyb chushzyb = chushzybs.get(i);
			chushzyb.setEdit_time(time);
			chushzyb.setEditor(user.getUsername());
			Map<String,String> param = new HashMap<String, String>();
			param.put("usercenter", chushzyb.getUsercenter());
			param.put("lingjbh", chushzyb.getLingjbh());
			param.put("shengcxbh", chushzyb.getShengcxbh());
			param.put("xiaohdbh", chushzyb.getXiaohdbh());
			param.put("liush", chushzyb.getLiush());
			
			String xhdbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.checkULX",param));
			if(StringUtils.isEmpty(xhdbh)){
				throw new ServiceException(chushzyb.getLingjbh()+"零件编号,"+chushzyb.getShengcxbh()+"生产线编号,"+chushzyb.getXiaohdbh()+"消耗点编号不存在，无法修改！");
			}else{
				String liush = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.checkLsh",param));
				if(!StringUtils.isEmpty(liush)){
					this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.updateChushzyb", chushzyb) ;
				}else{
					str.append(chushzyb.getLiush()+","); 
				}
			}
		}
		return str; 
	}
	
	/**
	 * 按条件查询初始化资源
	 * 
	 * @author 李智
	 * @date 2012-3-23
	 * @param page 分页显示
	 * @param param 查询条件
	 * @return Map 检索结果
	 */
	public Map<String, Object> queryChushzybByParam(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anx.queryChushzybByParam", param, page);
	}
	
	@SuppressWarnings("unchecked")
	public List<Chushzyb> queryChushzybByParam(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryChushzybByParam", param);
	}

	/**
	 * 按条件查询单条初始化资源
	 * 
	 * @author 李智
	 * @date 2012-3-28
	 * @return Map 检索结果
	 */
	public Chushzyb queryOneChushzyb(Map<String, String> param) {
		return (Chushzyb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryOneChushzyb", param);
	}
	
	/**
	 * 根据id更新flag
	 */
	public boolean updateObject(Chushzyb bean){
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.updateById",bean) ;
		return count>0 ;
	}
	
	/**
	 * 根据用户中心和零件编号查询生产线编号
	 * 
	 * @author 李智
	 * @date 2012-3-22
	 * @param param
	 *            查询条件
	 * @return List<Lingjxhd>
	 */
	public List<Lingjxhd> queryShengcxByParam(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryShengcxByParam", param);
	}
}
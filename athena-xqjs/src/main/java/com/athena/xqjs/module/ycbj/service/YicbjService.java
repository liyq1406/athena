package com.athena.xqjs.module.ycbj.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * 异常报警查询相关方法
 * 
 * @author dsimedd001
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class YicbjService extends BaseService {

	/**
	 * 异常报警查询
	 * 
	 * @params
	 * @author Xiahui
	 * @throws Exception 
	 * @date 2012-1-11
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) throws Exception {
		//0013563:查询取货运费计算异常报警时，查询quhys_message表数据  lc 2017-04-14
		if("110".equals(params.get("jismk"))){
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ycbj.queryYicbj_qhyfjs",params,page);
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ycbj.queryYicbj",params,page);
		}		
	}

	public void insert(Yicbj yicbj) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
	}

	@SuppressWarnings("unchecked")
	public List<Yicbj> select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ycbj.queryAllYicbj");
	}
	public void saveYicInfo(String jismk,String lingjbh,String cuowlx,String cuowxxxx){
		Yicbj bean = new Yicbj();
		LoginUser loginUser = AuthorityUtils.getSecurityUser() ;
		bean.setUsercenter(loginUser.getUsercenter()) ;
		bean.setCreator(loginUser.getUsername()) ;
		bean.setCreate_time(CommonFun.getJavaTime()) ;
		bean.setEditor(loginUser.getUsername()) ;
		bean.setEdit_time(bean.getCreate_time());
		bean.setCuowlx(cuowlx);
		bean.setJismk(jismk);
		bean.setJihyz(loginUser.getZuh());
		bean.setJihydm(bean.getJihyz()) ;
		bean.setCuowxxxx(cuowxxxx);
		bean.setLingjbh(lingjbh);
		CommonFun.logger.info("异常报警插入 --saveYicInfo");
		CommonFun.objPrint(bean, "异常报警插入时saveYicInfo方法bean");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", bean);
	}
	//0004321
	public void insertManyYic(String jismk,String cuowlx,String cuowxxxx,String jihyz,String jihydm,String usercenter,String lingjbh){
		Yicbj yicbj = new Yicbj();
		yicbj.setJismk(jismk);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(cuowxxxx);
		yicbj.setJihyz(jihyz);
		yicbj.setJihydm(jihydm);
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
	}

	public Yicbj saveYicInformation(String jismk, String lingjbh, String cuowlx, String cuowxxxx, String jihyz) {
		Yicbj bean = new Yicbj();
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		bean.setUsercenter(loginUser.getUsercenter());
		bean.setCreator(loginUser.getUsername());
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setEditor(loginUser.getUsername());
		bean.setEdit_time(bean.getCreate_time());
		bean.setCuowlx(cuowlx);
		bean.setJismk(jismk);
		bean.setJihyz(jihyz);
		bean.setJihydm(loginUser.getUsername());
		bean.setCuowxxxx(cuowxxxx);
		bean.setLingjbh(lingjbh);
		return bean;
	}
	/**
	 * 异常报警单个信息插入
	 * @param 错误类型，错误信息，计划员组，替换字符数组，用户中心，零件编号，登录者类
	 * @return
	 * 陈骏
	 */
	public  void  insertError(String cuowlx,String message,String zu,String [] paramStr,String usercenter,String lingjbh,LoginUser loginuser,String jismk){
		
		for(int i=0;i<paramStr.length;i++){
			message = message.replace("%" + (i + 1), paramStr[i]);
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		if(null != loginuser)
		{
			yicbj.setJihydm(loginuser.getUsername());
			yicbj.setCreator(loginuser.getUsername());
		}
		else
		{
			yicbj.setJihydm("sys");
			yicbj.setCreator("sys");
		}
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		this.insert(yicbj);
	}
	
	public void insertError(String cuowlx, String message, String zu, List<String> paramStr, String usercenter, String lingjbh, LoginUser loginuser, String jismk) {
		if(paramStr!=null){
			for (int i = 0; i < paramStr.size(); i++) {
				message = message.replace("%" + (i + 1), CommonFun.strNull(paramStr.get(i)));
			}
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		yicbj.setJihydm(loginuser.getUsername());
		yicbj.setCreator(loginuser.getUsername());
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setEditor(loginuser.getUsername());
		yicbj.setEdit_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		try{
			this.insert(yicbj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void insertError(String cuowlx, String message, String zu, List<String> paramStr, String usercenter, String lingjbh, String loginuser, String jismk) {
		for (int i = 0; i < paramStr.size(); i++) {
			message = message.replace("%" + (i + 1), paramStr.get(i));
		}
		Yicbj yicbj = new Yicbj();
		yicbj.setUsercenter(usercenter);
		yicbj.setLingjbh(lingjbh);
		yicbj.setCuowlx(cuowlx);
		yicbj.setCuowxxxx(message);
		yicbj.setJihyz(zu);
		yicbj.setJihydm(loginuser);
		yicbj.setCreator(loginuser);
		yicbj.setCreate_time(CommonFun.getJavaTime());
		yicbj.setEditor(loginuser);
		yicbj.setEdit_time(CommonFun.getJavaTime());
		yicbj.setJismk(jismk);
		try{
			this.insert(yicbj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void insertAll(List<Yicbj> erroList){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", erroList);
	}
	
	/**
	 * 保存异常报警
	 * @param map 保存参数(用户中心,零件编号)
	 * @param cuowxx 错误信息
	 * @param user 操作用户
	 * @param jismk 计算模块
	 * @param cuowlx 错误类型
	 * @param jihy 计划员组
	 */
	public void saveYicbj(String lingjbh,String usercenter, String cuowxx, String user,String jismk,String cuowlx,String jihy) {
		Yicbj yicbj = new Yicbj();
		// 用户中心
		yicbj.setUsercenter(CommonFun.strNull(usercenter));
		// 零件编号
		yicbj.setLingjbh(CommonFun.strNull(lingjbh));
		// 错误类型
		yicbj.setCuowlx(cuowlx);
		// 错误详细信息
		yicbj.setCuowxxxx(cuowxx);
		// 计划员代码
		yicbj.setJihydm(user);
		// 计划员组
		yicbj.setJihyz(jihy);
		// 计算模块
		yicbj.setJismk(jismk);
		//创建人
		yicbj.setCreator(user);
		String time = CommonFun.getJavaTime();
		//创建时间
		yicbj.setCreate_time(time);
		//编辑人
		yicbj.setEditor(user);
		//编辑时间
		yicbj.setEdit_time(time);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
	}

	/**
	 * <p>
	 * 按计算模块和计算人保留最新一版
	 * </p>
	 * 
	 * @param jismk
	 * @param loginUser
	 */
	public void clearYcbjXxByUser(String jismk, LoginUser loginUser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("jismk", jismk);
		map.put("jihydm", loginUser.getUsername());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.clearYcbjxxByUser", map);
	}
	
	/**
	 * 统计异常报警数量
	 * @param param 查询参数
	 * @return 数据数量
	 */
	public Integer countYicbj(Map param){
		//0013563:查询取货运费计算异常报警时，查询quhys_message表数据  lc 2017-04-14
		if("110".equals(param.get("jismk"))){
			return (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ycbj.countYicbj_qhyfjs",param);
		}else{
			return (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ycbj.countYicbj",param);
		}		
	}

}
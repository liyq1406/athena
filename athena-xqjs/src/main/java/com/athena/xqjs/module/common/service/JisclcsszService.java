package com.athena.xqjs.module.common.service;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;


@Component
public class JisclcsszService extends BaseService{

	/**
	 * 查询处理状态:
	 * 10:未计算；20：执行中；90：正常结束；99异常结束；
	 * @param map 查询参数
	 * @return true-有计算在进行；false-没有计算进行中
	 */
	public boolean checkState(Map map){
		boolean state = false;
		//查询处理状态,如果为0,表示没有计算进行中,如果为1,表示有计算进行中
		CommonFun.logger.debug("国产计算锁查询sql语句为：common.queryJisclState");
		if(CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryJisclState",map)).equals(Const.JSZT_EXECUTING)){
			state = true;
		}
		CommonFun.logger.debug("国产计算锁查询返回值为："+state);
		return state;
	}
	
	public boolean checkState(String str){
		boolean state = false;
		//查询处理状态,如果为0,表示没有计算进行中,如果为1,表示有计算进行中
		CommonFun.logger.debug("国产计算锁查询sql语句为：common.checkJisclState");
		if(CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.checkJisclState",str)).equals(Const.JSZT_EXECUTING)){
			state = true;
		}
		CommonFun.logger.debug("国产计算锁查询返回值为："+state);
		return state;
	}

	/**
	 * 更新处理状态
	 * @param map 更新参数
	 * @param state 处理状态
	 */
	public int updateState(Map<String,String> map,String state){
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String caozz = "sys";
		if(loginUser != null){
			caozz = loginUser.getUsername();
		}
		String time = CommonFun.getJavaTime();
		map.put("state", state);
		map.put("updatetime", time);
		map.put("edit_time", time);
		map.put("editor", caozz);
		int count =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.updateState", map);
		return count;
	}
	
	public Map<String, Object> queryAllStats(Pageable page){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("common.queryAllJisclState", page);
	}
}

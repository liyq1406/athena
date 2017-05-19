package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Liucdy;
import com.athena.truck.entity.Shijdzt;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-1-28
 */

@Component
public class KacLcdyService  extends BaseService<Liucdy>{

	/**
	 * @author 贺志国
	 * @date 2015-1-28
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}

	/**
	 * 流程定义分页查询
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.queryLiucdy",param,page);
	}
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean 流程定义实体
	 * @param operate 操作类型（1增加，2编辑）
	 * @param userID 用户ID
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Liucdy bean, Integer operate, String userID)
			throws ServiceException {
		if (1 == operate) {
			inserts(bean, userID);
		} else {
			edits(bean, userID);
		}
		return "success";
	}

	/**
	 * 数据录入
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	private String inserts(Liucdy bean, String userID)throws ServiceException {
		if("".equals(bean.getLiucbh())||null==bean.getLiucbh()){
			throw new ServiceException("流程编号为空！");
		}
		if("0".equals(bean.getLiucbs())){//默认流程，同时插入多个条记录
			batchInsertMor(bean,userID);
		}else{//自定义流程
			bean.setCreator(userID);
			bean.setEditor(userID);
			//查询是否插入了默认流程
			String count = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.queryMorCount",bean);
			if("0".equals(count)){//无默认流程，增加批量默认流程并且增加自定义流程
				batchInsertMor(bean,userID);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertLiucdy", bean);	
			}else{ //有默认流程，则只增加自定义流程
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertLiucdy", bean);				
			}
		}
		
		return "success";
	}
	
	/**
	 * 默认流程插入
	 * @author 贺志国
	 * @date 2015-1-30
	 * @param bean
	 * @param userID
	 */
	public void batchInsertMor(Liucdy bean,String userID){
		List<Liucdy> list = new ArrayList<Liucdy>();
		String liucbhs = bean.getLiucbh();
		String liucmcs = bean.getLiucmc();
		String liucbs = bean.getLiucbs();
		if(liucbhs.indexOf(",")==-1){//不存在默认流程 
			bean.setLiucbs("0");
			liucbhs = "1,2,3,8,9";
			liucmcs = "申报,排队,入厂,放空,出厂";
		}
		String[] strBHs = liucbhs.split(",");
		String[] strMCs = liucmcs.split(",");
		for(int i=0; i<strBHs.length;i++){
			Liucdy lcBean = new Liucdy();
			lcBean.setUsercenter(bean.getUsercenter());
			lcBean.setQuybh(bean.getQuybh());
			lcBean.setDaztbh(bean.getDaztbh());
			lcBean.setLiucbh(strBHs[i]);
			lcBean.setLiucmc(strMCs[i]);
			lcBean.setLeix(bean.getLeix());
			lcBean.setBiaozsj(bean.getBiaozsj());
			lcBean.setLiucbs(bean.getLiucbs());
			lcBean.setBiaos(bean.getBiaos());
			lcBean.setCreator(userID);
			lcBean.setEditor(userID);
			list.add(lcBean);
		}
		bean.setLiucbs(liucbs);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_kac.insertLiucdy", list);
	}

	
	/**
	 * 数据编辑
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	private String edits(Liucdy bean, String userID) throws ServiceException{
		bean.setEditor(userID);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateLiucdy", bean);
		return "";
	}


	/**
	 * 逻辑删除
	 * @author 贺志国
	 * @date 2015-1-28
	 * @param bean
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String removes(Liucdy bean, String userID)throws ServiceException {
		bean.setEditor(userID);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteLiucdy", bean);
		return "success";
	}
	

	
	/**
	 * 参考系运输路线查询
	 * @return List<Map<String,String>>运输路线集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectQuybh(String usercenter) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryLiucQuybh",usercenter);
	}
	
	
	/**
	 * 参考系大站台编号查询
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectQyDaztbh(Shijdzt shijdzt) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryQyDaztbh",shijdzt);
	}
	

	/**
	 * 系统参数定义流程编号查询
	 * @return List<Map<String,String>>流程编号集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectLiucbh(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryLiucbh",param);
	}

	/**
	 * 获取系统参数定义默认流程
	 * 1,2,3,8,9
	 * 申报,排队,准入,放空,出厂
	 * @author 贺志国
	 * @date 2015-3-12
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String[] selectMorlcOfxitcsdy() {
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryMorlcOfxitcsdy");
		//将list转换成以“,”分隔的字符串
		return listToString(list);
	}
	
	/**
	 * List转换成String
	 * @author 贺志国
	 * @date 2015-3-12
	 * @param list 
	 * @return
	 */
	public String[] listToString(List<Map<String,String>> list){
		StringBuilder strbuf = new StringBuilder();
		String[] strArr = new String[2];
		String sperate = "";
		for(Map<String,String> map :list){
			strbuf.append(sperate).append(map.get("ZIDBM"));
			sperate=",";
		}
		strArr[0] = strbuf.toString();
		sperate = "";
		StringBuilder strbuf1 = new StringBuilder();
		for(Map<String,String> map :list){
			strbuf1.append(sperate).append(map.get("ZIDMC"));
			sperate=",";
		}
		strArr[1] = strbuf1.toString();
		return strArr;
	}
	
}

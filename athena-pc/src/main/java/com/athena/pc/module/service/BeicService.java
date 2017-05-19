package com.athena.pc.module.service;



import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.Beic;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 备储计划service
 * @author gswang
 * @date 2011-11-30
 */
@Component
public class BeicService extends BaseService<Beic> {
	
	
	/**
	 * 备储明细查询
	 * @author hzg
	 * @date 2012-2-24
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("beic.queryBeicmx",param,page);
	}
	
	/**
	 * 批量保存
	 * @author hzg
	 * @date 2012-2-25
	 * @param bean 备储实体集
	 * @param insertList 备储明细增加数据集合
	 * @param editList 备储明细修改数据集合
	 * @param deleteList 备储明细删除数据集合
	 * @param username 用户名
	 * @return String null,success数据没有改变返回null，有数据增加或修改则返回success
	 */
	@Transactional
	public String  saveBeic(Beic beicBean,Integer operate,List<Beic> insertList,List<Beic> editList,
			List<Beic> deleteList,String username)throws ServiceException{
		if(null==beicBean.getBeicjhh()&&insertList.size()==0&&editList.size()==0&&deleteList.size()==0){
			return "null";
		}
		checkLingjh(beicBean,insertList);
		deleteBeicmx(deleteList,username);
		if(1 == operate){
			insertBeic(beicBean,username);
		}
		editBeicmx(editList,beicBean,username);
		insertBeicmx(insertList,beicBean,username);
		return "success";
	}
	
	/**
	 * 备储增加
	 * @author hzg
	 * @date 2012-2-25
	 * @param bean 备储实体集
	 * @param username 用户名
	 */
	public void insertBeic(Beic beicBean,String username)throws ServiceException{
		beicBean.setCreator(username);
		beicBean.setCreateTime(DateUtil.curDateTime());
		beicBean.setEditor(username);
		beicBean.setEditTime(DateUtil.curDateTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.insertBeic", beicBean);
	}
	/**
	 * 备储明细增加
	 * @author hzg
	 * @date 2012-2-25
	 * @param insertList 备储明细增加数据集合
	 * @param username 用户名
	 */
	public void insertBeicmx(List<Beic> insertList,Beic Beicbean,String username)throws ServiceException{
		String bejcjhh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("beic.queryBeicjhmxhMax", Beicbean);
		if(bejcjhh != null && bejcjhh.length()>0){
			bejcjhh = bejcjhh.substring(bejcjhh.length()-5);
		}else{
			bejcjhh  = "00000";
		}
		int i = Integer.parseInt(bejcjhh);
		for(Beic Beicmx : insertList){
			i = i+1;
			Beicmx.setBeicjhmxh(Beicbean.getBeicjhh()+String.format("%05d", i));
			Beicmx.setBeicjhh(Beicbean.getBeicjhh());
			Beicmx.setCreator(username);
			Beicmx.setCreateTime(DateUtil.curDateTime());
			Beicmx.setEditor(username);
			Beicmx.setEditTime(DateUtil.curDateTime());
			checkBeicshij(Beicbean,Beicmx);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.insertBeicmx", Beicmx);
		}
	}
	
	/**
	 * 备储明细修改
	 * @author hzg
	 * @date 2012-2-25
	 * @param editList 备储明细修改数据集合
	 * @param username 用户名
	 */
	public void editBeicmx(List<Beic> editList,Beic Beicbean,String username)throws ServiceException{
		for(Beic beanmx : editList){
			checkBeicshij(Beicbean,beanmx);
			beanmx.setCreator(username);
			beanmx.setCreateTime(DateUtil.curDateTime());
			beanmx.setEditor(username);
			beanmx.setEditTime(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.updateBeicmx", beanmx);
		}
	}
	/**
	 * 备储明细删除
	 * @author hzg
	 * @date 2012-2-25
	 * @param deleteList 备储明细删除数据集合
	 * @param username 用户名
	 */
	public void deleteBeicmx(List<Beic> deleteList,String username)throws ServiceException{
		for(Beic beanmx : deleteList){
			beanmx.setCreator(username);
			beanmx.setCreateTime(DateUtil.curDateTime());
			beanmx.setEditor(username);
			beanmx.setEditTime(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.deleteBeicmx", beanmx);
		}
	}
	
	/**
	 * 删除备储及备储明细
	 * @param Beicbean 备储实体集
	 */
	@Transactional
	public void batchDelete(Beic Beicbean) throws ServiceException{
		//根据备储计划号查询备储明细表中是否存在记录
		String counts = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("beic.findById", Beicbean.getBeicjhh());
		if(Integer.parseInt(counts)>0){//删除备储明细
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.delBeicmxOfBeic", Beicbean);
		}
		//删除备储信息
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("beic.deleteBeic", Beicbean);
	}
	
	/**
	 * 查询备储计划号是否存在
	 * @author 王国首
	 * @date 2012-5-17
	 * @param param
	 * @return 备储计划号
	 */
	public String selectBeicjhh(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("beic.queryBeicjhh", param);
	}

	/**
	 * 查询零件号是否存在
	 * @author 王国首
	 * @date 2012-5-17
	 * @param param
	 * @return 备储计划号
	 */
	public String checkLingjh(Beic beicBean,List<Beic> insertList){
		String result = "";
		for(Beic Beicmx : insertList){
			Beicmx.setUsercenter(beicBean.getUsercenter());
			String counts = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("beic.checkLingjh", Beicmx);
			if("0".equals(counts)){
				throw new ServiceException("零件"+Beicmx.getLingjbh()+"不存在");
			}
		}
		return result;
	}
	
	/**
	 * 查询零件号是否存在
	 * @author 王国首
	 * @date 2012-5-17
	 * @param param
	 * @return 备储计划号
	 */
	public String checkBeicshij(Beic beicBean,Beic Beicmx){
		String result = "";
		Beicmx.setUsercenter(beicBean.getUsercenter());
		List<Beic> beic = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("beic.checkBeicshij", Beicmx);
		if(beic.size()>0){
			Beic beicb = beic.get(0);
			throw new ServiceException("零件"+beicb.getLingjbh()+"与备储明细号为"+beicb.getBeicjhmxh()+"的零件备储时间重叠,请调整");
		}
		return result;
	}
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 * @author hzg
	 * @date 2012-2-24
	 */
	@Override
	protected String getNamespace(){
		return "beic";
	}
}

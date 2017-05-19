package com.athena.ckx.module.cangk.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.entity.cangk.Kuwdj;
import com.athena.ckx.entity.cangk.Kuwtemp;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 库位
 * @author denggq
 * @date 2012-2-8
 */
@Component
public class KuwService extends BaseService<Kuw>{
	
	private static Logger logger =Logger.getLogger(KuwService.class);
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-2-8
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-2-8
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(ArrayList<Kuw> inserts, Kuw kuw ,String userID)throws ServiceException{
		if(0 == inserts.size()){
			throw new ServiceException("【起始列号】【结束列号】非空");
		}
		List<String> tcKuwdjList = new ArrayList();
		
		for(Kuw bean:inserts){
			String kuwbh="";
			String zickh=kuw.getZickbh();
			String zickbh=zickh.substring(2,3);
			Kuw k=new Kuw();
			k.setCangkbh(kuw.getCangkbh());
			k.setZickbh(kuw.getZickbh());
			k.setUsercenter(kuw.getUsercenter());
			if(!zickbh.equals("P")&&!zickbh.equals("S")&&!zickbh.equals("D")){
				k.setKuwdjbh(bean.getKuwdjbh());
			}
			k.setKuwzt(bean.getKuwzt());
			k.setCreator(userID);
			k.setCreate_time(DateTimeUtil.getAllCurrTime());
			k.setEditor(userID);
			k.setEdit_time(DateTimeUtil.getAllCurrTime());
			String kuwzt=k.getKuwzt();
			if(kuwzt.equals("1")){
				kuwzt="0";
			}else{
				kuwzt="1";
			}
			
			for(int i=Integer.parseInt(bean.getQislh());i<=Integer.parseInt(bean.getJieslh());i++){
				
				if(i<10){
					k.setKuwbh(kuw.getMian()+"0"+i+kuw.getCeng());
					kuwbh=kuw.getMian()+"0"+i+kuw.getCeng();
				}else{
					k.setKuwbh(kuw.getMian()+i+kuw.getCeng());
					kuwbh=kuw.getMian()+i+kuw.getCeng();
				}
				
				//库位等级编号存不存在
				if(!zickbh.equals("P")&&!zickbh.equals("S")&&!zickbh.equals("D")){
					Kuwdj kuwdj = new Kuwdj();
					kuwdj.setUsercenter(kuw.getUsercenter());
					kuwdj.setCangkbh(kuw.getCangkbh());
					kuwdj.setKuwdjbh(k.getKuwdjbh());
					kuwdj.setBiaos("1");
					String mse = GetMessageByKey.getMessage("kuweidjbh")+k.getKuwdjbh()+GetMessageByKey.getMessage("notexist");
					if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountKuwdj", kuwdj)){
						throw new ServiceException(mse); 
					}
//					String sql="select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_kuwdj_baoz where usercenter='"+kuw.getUsercenter()+"' and cangkbh='"+kuw.getCangkbh()+"' and kuwdjbh='"+k.getKuwdjbh()+"'";
//					DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("kuweidjbh")+k.getKuwdjbh()+GetMessageByKey.getMessage("notexist"));
				}
				
				//仓库编号、子仓库编号不存在
				Zick zck = new Zick();
				zck.setUsercenter(kuw.getUsercenter());
				zck.setCangkbh(kuw.getCangkbh());
				zck.setZickbh(kuw.getZickbh());
				zck.setBiaos("1");
				String mes = GetMessageByKey.getMessage("usercentercangkzick",new String[]{kuw.getUsercenter(),kuw.getCangkbh(),kuw.getZickbh()});
				if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountZick", zck)){
					throw new ServiceException(mes); 
				}
//				String sql2="select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter='"+kuw.getUsercenter()+"' and cangkbh='"+kuw.getCangkbh()+"' and zickbh='"+kuw.getZickbh()+"'";
//				DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("usercentercangkzick",new String[]{kuw.getUsercenter(),kuw.getCangkbh(),kuw.getZickbh()}));
				
				//新增库位表中
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuw",k);
				k.setKuwbh(kuwbh);
				List<Kuw> list2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querykuwbaoz", k);
				if(list2.size()!=0){
					for(int j=0;j<list2.size();j++){
						Kuw s=new Kuw();
						s.setUsercenter(list2.get(j).getUsercenter());
						s.setCangkbh(list2.get(j).getCangkbh());
						s.setZickbh(list2.get(j).getZickbh());
						s.setKuwbh(kuwbh);
						s.setKuwdjbh(list2.get(j).getKuwdjbh());
						s.setShifky(kuwzt);
						s.setBaozlx(list2.get(j).getBaozlx());
						s.setDingybzgs(list2.get(j).getDingybzgs());
						s.setBaozgs("0");
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwxx",s);
					}
				}
				//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjbhxx",k);
			}
			String tempKey = kuw.getUsercenter()+","+kuw.getCangkbh()+","+kuw.getZickbh()+","+k.getKuwdjbh();
			if(!tcKuwdjList.contains(tempKey)){
				tcKuwdjList.add(tempKey);
			}
//			List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", k);
//			k.setDingykwgs(list.get(0).getDingykwgs());
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw", k);	
		}
		for (String s :tcKuwdjList){
			String[] keyStrs = s.split(",");
			Kuw kw=new Kuw();
			kw.setUsercenter(keyStrs[0]);
			kw.setCangkbh(keyStrs[1]);
			kw.setZickbh(keyStrs[2]);
			kw.setKuwdjbh(keyStrs[3]);
			//List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", kw);
			//kw.setDingykwgs(list.get(0).getDingykwgs());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw1", kw);	
		}
		return "增加成功";
	}

	/**
	 * 查询库位
	 * @param bean
	 * @author denggq
	 * @date 2012-2-3
	 * @return bean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Map kuwlist(Kuw kuw) throws ServiceException{
		kuw.setKuwbh(kuw.getMian()+kuw.getQislh()+kuw.getCeng());
		int jiangl=Integer.parseInt(kuw.getJiangl());
		//return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKuwdjbh",kuw,kuw);
		int pagesize=kuw.getPageSize();
		kuw.setPageSize(pagesize*(Integer.parseInt(kuw.getJiangl())+1));
		Map<String,Object> map =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKuwdjbh",kuw,kuw);
		List<Kuw> list = (List<Kuw>)map.get("rows");
		List arraylist=new ArrayList();	
		for (int i = 0; i < list.size(); i++) {
			arraylist.add(list.get(i));
				i=i+jiangl;
		}
		map.put("rows", arraylist);
		int ss=Integer.parseInt(map.get("total").toString())/(Integer.parseInt(kuw.getJiangl())+1);
		map.put("total", ss);
		return map;
	}
	/**
	 * 修改库位
	 * @param bean
	 * @author denggq
	 * @date 2012-2-3
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String update(ArrayList<Kuw> inserts, Kuw kuw ,String userID) throws ServiceException{
		for(Kuw bean:inserts){
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			if(bean.getKuwzt()=="I"){
				bean.setKuwzt("1");
			}else if(bean.getKuwzt().equals("L")){
				bean.setKuwzt("0");
			}else if(bean.getKuwzt().equals("A")){
				bean.setKuwzt("0");
			}else if(bean.getKuwzt().equals("P")){
				bean.setKuwzt("0");
			}
			if(Integer.parseInt(bean.getKuwzt())==1){
				bean.setShifky("0");
				bean.setKuwzt("1");
			}else{
				bean.setShifky("1");
				bean.setKuwzt("0");
			}
			String zickh=bean.getZickbh();
			String zickbh=zickh.substring(2,3);
			if(!zickbh.equals("P")&&!zickbh.equals("S")){
				bean.setKuwdjbh(bean.getKuwdjbh());
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuw", bean);
			String beankuwdjbh=bean.getKuwdjbh();
			List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querykuwbKuwdjbh", bean);
			if(list.size()!=0){
				String kuwdjbh=list.get(0).getKuwdjbh();
				bean.setKuwdjbh(kuwdjbh);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKwbzxx", bean);
				bean.setKuwdjbh(beankuwdjbh);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKwbzxx", bean);
			}
			
		}
		return "修改成功";
	}
	/**
	 * 库位编号修改包装个数
	 * @author denggq
	 * @date 2012-2-21
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public String kuwupdate(ArrayList<Kuw> inserts,Kuw kuw) throws ServiceException{
		for(Kuw bean:inserts){
			bean.setUsercenter(kuw.getUsercenter());
			bean.setCangkbh(kuw.getCangkbh());
			bean.setZickbh(kuw.getZickbh());
			//删除原数据
			//logger.info("删除库位编号为"+bean.getKuwbh()+"的库位包装信息 开始");
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKwbzxxkuw", bean);
			//logger.info("删除用户中心"+bean.getKuwbh()+"的库位包装信息   结束");
			String ykuwdjbh=bean.getYkuwdjbh();
			//String kuwdjbh=bean.getKuwdjbh();
			String kuwdjbhtd=bean.getKuwdjbhtd();
			//String dingybzgs=bean.getDingybzgs();
			//String kuwzt=bean.getKuwzt();
			String kuwzttd=bean.getKuwzttd();
			//bean.setKuwdjbh(kuwdjbh);
			bean.setKuwdjbhtd(kuwdjbhtd);
			if(bean.getKuwdjbh().equals(bean.getKuwdjbhtd())){
				throw new ServiceException("替代的库位等级编号不能与原库位等级编号相同");
			}
			//更新的同时必须判断库位是否已经被占用了
			bean.setKuwbh(bean.getKuwbh());
			List<Kuw> listkuw = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryKuwdjbh1",bean);
			if(listkuw.size()==0){
				throw new ServiceException("库位等级编号为 "+bean.getKuwdjbh()+" 的库位编号 "+bean.getKuwbh()+" 已被占用");
			}
			Kuw listkuws = (Kuw)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryKuwdjbhbs",bean);
			//如果用户取消了 替代的库位等级编号 则相应的也要还原到之前的状态 加个相应的标识来判断 Y/N 
			//已经被替换过的等级编号会打上标识为 Y   所以只用查询对应的标识及 替代的库位编号
			if(!"".equals(bean.getKuwdjbhtd())&& null!=bean.getKuwdjbhtd()){
				logger.info("1:根据库位编号"+bean.getKuwbh()+"来更新库位中的 库位等级 和 库位状态  开始");
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwzdjbh", bean);
				logger.info("1:根据库位编号"+bean.getKuwbh()+"来更新库位中的 库位等级 和 库位状态  结束");
			//如果没有被占用并且没有被取消   则更新这个库位编号下的 库位包装信息中的是否可用状态为   0(不可用)
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxsfky", bean);
			}else{
				if(null!=listkuws){
					if(!"".equals(listkuws.getKuwzttd())&&null!=listkuws.getKuwzttd()&& "Y".equals(listkuws.getKuwzttd())&&("".equals(bean.getKuwdjbhtd())||null==bean.getKuwdjbhtd())){
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwzdjbhbs", bean);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxsfky1", bean);
				   }
				}
			}
//			logger.info("1:根据库位编号"+bean.getKuwbh()+"来更新库位中的 库位等级 和 库位状态  开始");
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwzdjbh", bean);
//			logger.info("1:根据库位编号"+bean.getKuwbh()+"来更新库位中的 库位等级 和 库位状态  结束");
			//插入库位包装信息表中的时候 会冻结这个库位编号   切记
//			if(kuwzttd.equals("1")){
//				kuwzttd="0";
//			}else{
//				kuwzttd="1";
//			}
	
			//查询新增的数据
//			logger.info("在库位表和库位包装表中查询数据 开始");
//			List<Kuw> list2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querykuwbaoz", bean);
//			logger.info("在库位表和库位包装表中查询数据 结束");
//			if(list2.size()>0){
//				for(int j=0;j<list2.size();j++){
//					bean.setUsercenter(list2.get(j).getUsercenter());
//					bean.setCangkbh(list2.get(j).getCangkbh());
//					bean.setZickbh(list2.get(j).getZickbh());
//					bean.setKuwbh(list2.get(j).getKuwbh());
//					bean.setShifky(kuwzt);
//					bean.setBaozlx(list2.get(j).getBaozlx());
//					bean.setDingybzgs(list2.get(j).getDingybzgs());
//					bean.setBaozgs("0");
//					logger.info("插入记录 仓库库位包装信息表中 开始");
//					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwxx",bean);
//					logger.info("插入记录 仓库库位包装信息表中 结束");
//				}
//				
//				
//			}
			//bean.setDingybzgs(dingybzgs);
			//修改定义包装个数
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbxxgss",bean);
			//查询库位等级编号查询出定义库位个数
//			logger.info("根据库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   开始");
//			List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", bean);
//			logger.info("根据库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   结束");
//			String dingykwgs="0";
//			if(list.size()!=0){
//				bean.setDingykwgs(list.get(0).getDingykwgs());
//				logger.info("2:根据库位等级编号"+bean.getKuwdjbh()+"更新  库位包装信息表中定义库位个数    开始");
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw",bean);
//				logger.info("2:根据库位等级编号"+bean.getKuwdjbh()+"更新   库位包装信息表中定义库位个数    结束");
//			}
//			bean.setKuwdjbh(ykuwdjbh);
//			logger.info("根据原库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   开始");
//			List<Kuw> list3=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", bean);
//			logger.info("根据原库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   结束");
//			if(list3.size()>0){
//				bean.setDingykwgs(list3.get(0).getDingykwgs());
//				logger.info("3:根据库位等级编号"+bean.getKuwdjbh()+"更新  库位包装信息表中定义库位个数      开始");
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw",bean);
//				logger.info("3:根据库位等级编号"+bean.getKuwdjbh()+"更新   库位包装信息表中定义库位个数      结束");
//			}
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjbh", bean);
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjbhxx", bean);
		}
		  //updatekuwbzxx();
		return "修改成功";
	}
	
	/**
	 * 物理删除方法
	 * @author denggq
	 * @date 2012-2-21
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public String doDelete(ArrayList<Kuw> inserts,Kuw kuw) throws ServiceException{
		List<String> deKuwdjList = new ArrayList();
		//先删除库位和库位包装信息 
		for(Kuw bean:inserts){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuw", bean);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKwbzxx", bean);
			String tempKey = bean.getUsercenter()+","+bean.getCangkbh()+","+bean.getZickbh()+","+bean.getKuwdjbh();
			if(!deKuwdjList.contains(tempKey)){
				deKuwdjList.add(tempKey);
			}
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjbhxx", bean);
		}
		//最后汇总后在对库位包装信息中的定义库位个数字段来进行更新 减少更新的次数 进而减少归档的日志
		for (String s :deKuwdjList){
			String[] keyStrs = s.split(",");
			Kuw kw=new Kuw();
			kw.setUsercenter(keyStrs[0]);
			kw.setCangkbh(keyStrs[1]);
			kw.setZickbh(keyStrs[2]);
			kw.setKuwdjbh(keyStrs[3]);
			//List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", kw);
			//kw.setDingykwgs(list.get(0).getDingykwgs());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw1", kw);	
		}
		return "删除成功";
	}
	
	/**
	 * 修改库位状态为解冻
	 * @author denggq
	 * @date 2012-2-21
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public String updatekuwztjied(ArrayList<Kuw> inserts,Kuw kuw) throws ServiceException{
		for(Kuw bean:inserts){
			//修改ckx_kuw表中的库位状态
			bean.setKuwzt("0");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwzdjbh1", bean);
			bean.setShifky("1");
			//修改ck_kwbzxx表中的是否可用状态
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwztdongj", bean);
		}
		
		return "修改库位状态为解冻成功";
	}
	
	/**
	 * 修改库位状态为冻结
	 * @author denggq
	 * @date 2012-2-21
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public String updatekuwztdongj(ArrayList<Kuw> inserts,Kuw kuw) throws ServiceException{
		for(Kuw bean:inserts){
			//修改ckx_kuw表中的库位状态
			bean.setKuwzt("1");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwzdjbh1", bean);
			bean.setShifky("0");
			//修改ck_kwbzxx表中的是否可用状态
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwztdongj", bean);
		}
		
		return "修改库位状态为冻结成功";
	}
	
	/**
	 * 获得多个生产线
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Kuw bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryKuw",bean);
	}
	
	/**
	 * 更新仓库库位包装信息批量
	 * @author wangyu
	 * @date 2014-5-17
	 * @param bean
	 * @return 主键
	 */
	public void updatekuwbzxx(){
		//先查询出需要更新的用户中心\仓库\库位等级编号 将数据插入到 临时表 ckx_kuw_temp
		 insertKuwtemp();
		
		//根据在库位的页面上 填入的 库位等级编号替代 和 库位状态替代 2个值 来更新到真实的 库位等级编号  和 库位状态  并将 替代的值全部清空
		List<Kuw> inserts = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkkwbzxx");
		for(Kuw bean:inserts){
			//更新仓库库位包装信息方法
			updateCkkubzxx(bean);
		}
		updatekuwtemp();
    }
	@Transactional
	public void updatekuwtemp(){
		//先查询出需要更新的用户中心\仓库\库位等级编号 将数据插入到 临时表 ckx_kuw_temp
		 //insertKuwtemp();
		//最后在统计替换的库位等级编号 来更新对应的 定义库位个数 
		List<Kuwtemp> temps = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryKuwtemp");
		 for(Kuwtemp beans:temps){
			 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjbhxxtemp",beans);
		 }
		 //删除临时表 ckx_kuw_temp
		 deleteKuwtemp();
	}
	
	/**
	 * 新增临时表
	 * @author wangyu
	 * @date 2014-5-17
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public void insertKuwtemp(){
		//先查询出需要更新的用户中心\仓库\库位等级编号 将数据插入到 临时表 ckx_kuw_temp
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwtemp");
		
	}
	
	/**
	 * 删除临时表
	 * @author wangyu
	 * @date 2014-5-17
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public void deleteKuwtemp(){
		//先查询出需要更新的用户中心\仓库\库位等级编号 将数据插入到 临时表 ckx_kuw_temp
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuwtemp");
		
	}
	
	
	
	/**
	 * 更新仓库库位包装信息方法
	 * @author wangyu
	 * @date 2014-5-17
	 * @param bean
	 * @return 主键
	 */
	@Transactional
	public void updateCkkubzxx(Kuw bean){
		//先获取原库位等级编号  否则在更新后，会被替换掉
		String ykuwdjbh=bean.getKuwdjbh();
		String kuwdjbhtd = bean.getKuwdjbhtd();
		
		//将查询出来的值 先更新到真实的字段中 更新到库位等级编号 和 库位状态  开始
		logger.info("更新到库位等级编号 和 库位状态  开始");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkkwbzxx", bean);
		logger.info("更新到库位等级编号 和 库位状态  结束");
		
		//删除原数据
		logger.info("删除库位编号为"+bean.getKuwbh()+"的库位包装信息 开始");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKwbzxxkuw", bean);
		logger.info("删除用户中心"+bean.getKuwbh()+"的库位包装信息   结束");
		
		//String kuwdjbh=bean.getKuwdjbh();
		String kuwzt=bean.getKuwzt();
		bean.setKuwdjbh(kuwdjbhtd);
		//插入库位包装信息表中的时候 会冻结这个库位编号   
		//if(kuwzt.equals("1")){
			//kuwzt="0";
		//}else{
			//kuwzt="1";
		//}
		//查询新增的数据
		//logger.info("在库位表和库位包装表中查询数据 开始");
		//List<Kuw> list2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querykuwbaoz", bean);
		//logger.info("在库位表和库位包装表中查询数据 结束");
		//if(list2.size()>0){
			//for(int j=0;j<list2.size();j++){
				//bean.setUsercenter(list2.get(j).getUsercenter());
				//bean.setCangkbh(list2.get(j).getCangkbh());
				//bean.setZickbh(list2.get(j).getZickbh());
				//bean.setKuwbh(list2.get(j).getKuwbh());
				//bean.setShifky(kuwzt);
				//bean.setBaozlx(list2.get(j).getBaozlx());
				//bean.setDingybzgs(list2.get(j).getDingybzgs());
				//bean.setBaozgs("0");
				logger.info("插入记录 仓库库位包装信息表中 开始");
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwxxs",bean);
				logger.info("插入记录 仓库库位包装信息表中 结束");
			//}
		
	    //}
		//查询库位等级编号查询出定义库位个数
		//logger.info("根据库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   开始");
		//List<Kuw> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", bean);
		//logger.info("根据库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   结束");
		//String dingykwgs="0";
		//if(list.size()!=0){
			//bean.setDingykwgs(list.get(0).getDingykwgs());
			//logger.info("2:根据库位等级编号"+bean.getKuwdjbh()+"更新  库位包装信息表中定义库位个数    开始");
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw",bean);
			//logger.info("2:根据库位等级编号"+bean.getKuwdjbh()+"更新   库位包装信息表中定义库位个数    结束");
		//}
		//bean.setKuwdjbh(ykuwdjbh);
		//logger.info("根据原库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   开始");
		//List<Kuw> list3=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydingykwgs", bean);
		//logger.info("根据原库位等级编号"+bean.getKuwdjbh()+"查询 库位表 查出定义库位个数   结束");
		//if(list3.size()>0){
			//bean.setDingykwgs(list3.get(0).getDingykwgs());
			//logger.info("3:根据库位等级编号"+bean.getKuwdjbh()+"更新  库位包装信息表中定义库位个数      开始");
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwbzxxkuw",bean);
			//logger.info("3:根据库位等级编号"+bean.getKuwdjbh()+"更新   库位包装信息表中定义库位个数      结束");
		//}
		
	}
}
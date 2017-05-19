package com.athena.truck.module.queue.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chelpd;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Yund;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class QueueService extends BaseService {
   
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(QueueService.class);
	
	/**
	 * 查询车辆排队信息(分页操作)
	 */
	public Map<String, Object>  queryQueuexxPage(Pageable page, Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("queue.queryQueuexx", params, page);
	}
	
	/**
	 * 查询车辆排队信息
	 */
	public List<Chelpd> queryQueuexx(Map map){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryQueuexx", map);
	}
	/**
	 *查询当前车辆排队最大序号
	 */
	public Map queryQueuemax(){
		List xuhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryQueuemax");
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != xuhlist&& xuhlist.size() > 0)
		{
			for (int i=0;i<xuhlist.size();i++) 
			{
				Map m = (Map) xuhlist.get(i);
				if(null==m.get("VALUE")){
					map.put(m.get("KEY").toString(),1);
				}else{
					map.put(m.get("KEY").toString(),((BigDecimal)m.get("VALUE")).intValue());
				}
			}
		}
		return map;
	}
	/**
	 *查询当前车辆卸货最大序号
	 */
	public Map queryXiehxhmax(){
		List xuhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryXiehxhmax");
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != xuhlist&& xuhlist.size() > 0)
		{
			for (int i=0;i<xuhlist.size();i++) 
			{
				Map m = (Map) xuhlist.get(i);
				if(null==m.get("VALUE")){
					map.put(m.get("KEY").toString(),1);
				}else{
					map.put(m.get("KEY").toString(),((BigDecimal)m.get("VALUE")).intValue());
				}
				
			}
		}
		return map;
	}
	/**
	 * 查询当前大站台最小序号
	 */
	public Integer queryDaztxuh(Chelpd pd){
		int min=0;
	    Object num = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("queue.queryQueuemin",pd);
	    if(num !=null){
	    	min=Integer.parseInt(num.toString()) -1;
	    }
		return min;
	}
	/**
	 * 查询当前需要排队的运单信息
	 */
	public List<Yund> queryQueue(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryQueue");
	}
	/**
	 * 查询急件运单信息
	 */
	public List<Yund> queryJijyd(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryJijyd");
	}
	/**
	 *查询混装的卡车及运单号信息
	 *(当前已经有卡车中的其它运单在排队，其它的混装运单不能进行排队) 
	 */
	public Map queryHunzyd(){
		List<Yund> list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryHunzyd");
		Map<String, String> map = new HashMap<String, String>();
		if(null != list && list.size() > 0)
		{
			for (int i=0;i<list.size();i++) 
			{
				Yund bean =(Yund) list.get(i);
				map.put(bean.getUsercenter()+bean.getYundh(),bean.getYundh());
			}
		}
		return map;
	}
	/**
	 * 查询大站台对应的流程标示的标准时间
	 */
	public  Map  queryBzsj(){
		List liuc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryBzsj");
		Map<String, String> map = new HashMap<String, String>();
		if(null != liuc && liuc.size() > 0)
		{
			for (int i=0;i<liuc.size();i++) 
			{
				Map m = (Map) liuc.get(i);
				map.put(m.get("KEY").toString(),String.valueOf(m.get("VALUE")));
			}
		}
		return map;
	}
	/**
	 * 查询承运商车位（判断排队属性（专用或通用））
	 */
	public Map  queryChengyscw(){
		List cw = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryChengyscw");
		Map<String, String> map = new HashMap<String, String>();
		if(null != cw && cw.size() > 0)
		{
			for (int i=0;i<cw.size();i++) 
			{
				Map m = (Map) cw.get(i);
				map.put(m.get("KEY").toString(),(String) m.get("VALUE"));
			}
		}
		return map;
	}
	/**
	 * 车辆排队
	 */
	@Transactional
	public void InsertChelpd(Chelpd chelpd){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("queue.insertChelpd", chelpd);
	
	}
	/**
	 * 查询空闲车位信息
	 */
	public List<Chew> queryKongxcw(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryKongxcw");
	}
	/**
	 * 查询急件车辆信息
	 */
	public List<Chew> queryJijcw(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryJijcw");
	}
	/**
	 * 查询指定车位信息
	 */
	public List<Chelpd> queryZhidcw(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryZhidcw");
	}
	/**
	 * 修改车位状态信息
	 */
	public Integer updateChewzt(Chew chew){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("queue.updateChewzt", chew);
	}
	/**
	 * 修改车辆排队信息
	 */
	public void updateChelpd(Chelpd pd){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("queue.updateChelpd", pd);
	}
	/**
	 * 记录出入厂流水
	 */
	public void insertChurcls(Yund yund){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.insertChurcls", yund);
	}
	/**
	 * 修改运单状态
	 */
	public  void updateYundzt(Yund yund){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("queue.updateYundzt", yund);
	}
	/**
	 * 排队队列任务
	 */
	@Transactional
	public void queueTask() throws ServiceException{
		logger.info("车辆入厂排队任务开始执行！");
		//急件运单列表
		List<Yund> jjyd=this.queryJijyd();
		//正常的运单列表
		List<Yund> zhengcyd=new ArrayList();
		//延误的运单列表
		List<Yund> yanwyd=new ArrayList();
		//提前排队
		List<Yund> tiqyd=new ArrayList();
		//查询大站台排序的最大值
		Map xuh=queryQueuemax();
		//查询承运商商车位属性
		Map shux=queryChengyscw();
		//混装的卡车当前有其它运单在排队所以当前运单不能排队
		Map Hunzyd=queryHunzyd();
		//查询流程标准时间
		Map liuc=queryBzsj();
		//查询当前需要排队的运单数据
		List<Yund> queue=queryQueue();
		for(Yund bean:queue){
			//验证当前是否是混装的运单且有其他运单未完成收货操作
			if(!Hunzyd.containsKey(bean.getUsercenter()+bean.getYundh())){
				//如果实际时间小于提前上限则表示当前延误
				if(bean.getMin()<bean.getPaidtqqsx()){
					yanwyd.add(bean);
				}else if(bean.getMin()>=bean.getPaidtqqsx()){
					zhengcyd.add(bean);
				}else if("1".equals(bean.getTiqpdbs()) || "2".equals(bean.getTiqpdbs())){
					tiqyd.add(bean);
				}
		     }
		}
		//急件运单插入车辆排队表中
		for(Yund jj:jjyd){
			Chelpd clpd=new Chelpd();
			jj.setEditor("SYS");
			jj.setZhuangt("2");
			jj.setBeiz("");
			jj.setBeiz1("SYS");
			clpd.setUsercenter(jj.getUsercenter());
			clpd.setYundh(jj.getYundh());
			clpd.setKach(jj.getKach());
			clpd.setDaztbh(jj.getDaztbh());
			clpd.setQuybh(jj.getQuybh());
			clpd.setPaidzt("0");
			clpd.setChengysbh(jj.getChengysdm());
			clpd.setCreator("SYS");
			clpd.setEditor("SYS");
		    //设置排队序号 如果当期存在当天大站台排队序号则在当期序号上加1 否则从1开始
			if(xuh.containsKey(jj.getUsercenter()+jj.getDaztbh())){
				 clpd.setPaidxh(Integer.parseInt(String.valueOf(xuh.get(jj.getUsercenter()+jj.getDaztbh())))+1);
				 xuh.remove(jj.getUsercenter()+jj.getDaztbh());
				 xuh.put(jj.getUsercenter()+jj.getDaztbh(), clpd.getPaidxh());
				}else{
				 xuh.put(jj.getUsercenter()+jj.getDaztbh(), 1);
				 clpd.setPaidxh(1);
				}
			//查询流程的标准操作时间
			if(liuc.containsKey(jj.getUsercenter()+jj.getQuybh()+jj.getDaztbh()+"2")){
				jj.setBiaozsj((String) liuc.get(jj.getUsercenter()+jj.getQuybh()+jj.getDaztbh()+"2"));
			}
	        //排队属性（为急件）	
			 clpd.setPaidsx(String.valueOf(4));
			this.InsertChelpd(clpd);
			this.updateYundzt(jj);
			this.insertChurcls(jj);
		}
		//正常的运单列表插入车辆排队队列表中
		for(Yund zcyd:zhengcyd){
			Chelpd clpd=new Chelpd();
			zcyd.setEditor("SYS");
			zcyd.setZhuangt("2");
			zcyd.setBeiz("");
			zcyd.setBeiz1("SYS");
			clpd.setUsercenter(zcyd.getUsercenter());
			clpd.setYundh(zcyd.getYundh());
			clpd.setKach(zcyd.getKach());
			clpd.setDaztbh(zcyd.getDaztbh());
			clpd.setQuybh(zcyd.getQuybh());
			clpd.setPaidzt("0");
			clpd.setChengysbh(zcyd.getChengysdm());
			clpd.setCreator("SYS");
			clpd.setEditor("SYS");
		    //设置排队序号 如果当期存在当天大站台排队序号则在当期序号上加1 否则从1开始
			if(xuh.containsKey(zcyd.getUsercenter()+zcyd.getDaztbh())){
				 clpd.setPaidxh(Integer.parseInt(String.valueOf(xuh.get(zcyd.getUsercenter()+zcyd.getDaztbh())))+1);
				 xuh.remove(zcyd.getUsercenter()+zcyd.getDaztbh());
				 xuh.put(zcyd.getUsercenter()+zcyd.getDaztbh(), clpd.getPaidxh());
				}else{
				 clpd.setPaidxh(1);
				 xuh.put(zcyd.getUsercenter()+zcyd.getDaztbh(), 1);
				}
			//查询流程的标准操作时间
			if(liuc.containsKey(zcyd.getUsercenter()+zcyd.getQuybh()+zcyd.getDaztbh()+"2")){
				zcyd.setBiaozsj((String) liuc.get(zcyd.getUsercenter()+zcyd.getQuybh()+zcyd.getDaztbh()+"2"));
			}
		    //判断排队属性（当前承运商是否专有或通用）	
			if(shux.containsKey(zcyd.getUsercenter()+zcyd.getDaztbh()+zcyd.getChengysdm())){
				 clpd.setPaidsx(String.valueOf(2));
			 }else{
			 clpd.setPaidsx(String.valueOf(1));
			 }
			this.InsertChelpd(clpd);
			this.updateYundzt(zcyd);
			this.insertChurcls(zcyd);
		}
        //延误的运单列表插入到排队队列表中
		for(Yund ywyd:yanwyd){
			Chelpd clpd=new Chelpd();
			clpd.setUsercenter(ywyd.getUsercenter());
			clpd.setYundh(ywyd.getYundh());
			clpd.setKach(ywyd.getKach());
			clpd.setDaztbh(ywyd.getDaztbh());
			clpd.setQuybh(ywyd.getQuybh());
			clpd.setPaidzt("0");
			clpd.setChengysbh(ywyd.getChengysdm());
			clpd.setCreator("SYS");
			clpd.setEditor("SYS");
			ywyd.setEditor("SYS");
			ywyd.setZhuangt("2");
			ywyd.setBeiz("");
			ywyd.setBeiz1("SYS");
		    //设置排队序号 如果当期存在当天大站台排队序号则在当期序号上加1 否则从1开始
			if(xuh.containsKey(ywyd.getUsercenter()+ywyd.getDaztbh())){
			 clpd.setPaidxh( Integer.parseInt(String.valueOf(xuh.get(ywyd.getUsercenter()+ywyd.getDaztbh())))+1);
			 xuh.remove(ywyd.getUsercenter()+ywyd.getDaztbh());
			 xuh.put(ywyd.getUsercenter()+ywyd.getDaztbh(), clpd.getPaidxh());
			}else{
			 clpd.setPaidxh(1);
			 xuh.put(ywyd.getUsercenter()+ywyd.getDaztbh(), 1);
			}
			//查询流程的标准操作时间
			if(liuc.containsKey(ywyd.getUsercenter()+ywyd.getQuybh()+ywyd.getDaztbh()+"2")){
				ywyd.setBiaozsj((String) liuc.get(ywyd.getUsercenter()+ywyd.getQuybh()+ywyd.getDaztbh()+"2"));
			}
		    //判断排队属性（当前承运商是否专用或通用）	
			if(shux.containsKey(ywyd.getUsercenter()+ywyd.getDaztbh()+ywyd.getChengysdm())){
				 clpd.setPaidsx(String.valueOf(2));
			}else{
				 clpd.setPaidsx(String.valueOf(1));
			}
			this.InsertChelpd(clpd);
			this.updateYundzt(ywyd); //修改运单的状态为排队
			this.insertChurcls(ywyd);
		}
		 //提前的运单列表插入到排队队列表中
		for(Yund tiq:tiqyd){
			Chelpd clpd=new Chelpd();
			clpd.setUsercenter(tiq.getUsercenter());
			clpd.setYundh(tiq.getYundh());
			clpd.setKach(tiq.getKach());
			clpd.setCreator("SYS");
			clpd.setEditor("SYS");
			clpd.setDaztbh(tiq.getDaztbh());
			clpd.setQuybh(tiq.getQuybh());
			clpd.setPaidzt("0");
			clpd.setChengysbh(tiq.getChengysdm());
            tiq.setBeiz("");
            tiq.setBeiz1("SYS");
			tiq.setEditor("SYS");
			tiq.setZhuangt("2");
		    //设置排队序号 如果当期存在当天大站台排队序号则在当期序号上加1 否则从1开始
			if(xuh.containsKey(tiq.getUsercenter()+tiq.getDaztbh())){
				 clpd.setPaidxh(Integer.parseInt(String.valueOf(xuh.get(tiq.getUsercenter()+tiq.getDaztbh())))+1);
				 xuh.remove(tiq.getUsercenter()+tiq.getDaztbh());
				 xuh.put(tiq.getUsercenter()+tiq.getDaztbh(), clpd.getPaidxh());
				}else{
				  clpd.setPaidxh(1);
				  xuh.put(tiq.getUsercenter()+tiq.getDaztbh(), 1);
				}
			//查询流程的标准操作时间
			if(liuc.containsKey(tiq.getUsercenter()+tiq.getQuybh()+tiq.getDaztbh()+"2")){
				tiq.setBiaozsj((String) liuc.get(tiq.getUsercenter()+tiq.getQuybh()+tiq.getDaztbh()+"2"));
			}
		    //判断排队属性（当前承运商是否专用或通用）	
			if(shux.containsKey(tiq.getUsercenter()+tiq.getDaztbh()+tiq.getChengysdm())){
				 clpd.setPaidsx(String.valueOf(2));
				}else{
				 clpd.setPaidsx(String.valueOf(1));
				}
			this.InsertChelpd(clpd);
			this.updateYundzt(tiq); //修改运单的状态为排队
			this.insertChurcls(tiq);
		}
		logger.info("车辆入厂排队任务执行结束!");
	}
	/**
	 * 车辆指定车位任务
	 */
	@Transactional
	public   void  zhidCWTask() throws ServiceException{
		logger.info("车辆入厂分配车位任务开始执行!");
		//车辆指定车位入厂程序
		//查询当前空闲车位非急件车位
		List<Chew> kongxcw= queryKongxcw();
	    //查询当前指定车位的车位信息
		List<Chelpd> zhidcw= queryZhidcw();
		//查询大站台卸货排序的最大值
		Map xiehxh=queryXiehxhmax();
		//查询流程标准时间
		Map liuc=queryBzsj();
		//查询当前等待入厂的车辆信息
		Map<String, String> param=new HashMap<String, String>();
		param.put("paidzt","0");
		param.put("zdchew", "1");
		List<Chelpd> dengdrc=queryQueuexx(param);
		//查询急件排队的车辆信息
		Map<String, String> jjparam=new HashMap<String, String>();
		jjparam.put("paidzt","0");
		jjparam.put("paidsx", "4");
		List<Chelpd> jijpd=queryQueuexx(jjparam);
		//查询急件车位信息
		List<Chew> jijcw=queryJijcw();
		for(Chelpd jpd:jijpd){
			for(Chew jcw:jijcw){
				if(jpd.getUsercenter().equals(jcw.getUsercenter())  && jpd.getDaztbh().equals(jcw.getDaztbh())){
					 //设置卸货序号 如果当期存在当天大站台卸货序号则在当期序号上加1 否则从1开始
					if(xiehxh.containsKey(jpd.getUsercenter()+jpd.getDaztbh())){
							 jpd.setXiehxh(strformat(Integer.parseInt(String.valueOf( xiehxh.get(jpd.getUsercenter()+jpd.getDaztbh())))+1));
							 xiehxh.remove(jpd.getUsercenter()+jpd.getDaztbh());
							 xiehxh.put(jpd.getUsercenter()+jpd.getDaztbh(),jpd.getXiehxh());
					   }else{
							xiehxh.put(jpd.getUsercenter()+jpd.getDaztbh(), 1);
							jpd.setXiehxh(strformat(1));
					 }
					
					//急件车位
					jpd.setChewbh(jcw.getChewbh());
					jpd.setPaidzt("1");//排队状态准入
					jpd.setNeweditor("SYS"); //当前登录的用户
					Yund yund= new Yund();
					if(liuc.containsKey(jpd.getUsercenter()+jpd.getQuybh()+jpd.getDaztbh()+"3")){
						yund.setBiaozsj((String) liuc.get(jpd.getUsercenter()+jpd.getQuybh()+jpd.getDaztbh()+"3"));
					}
					yund.setChengysdm(jpd.getChengysbh());
					yund.setEditor("SYS");
					yund.setUsercenter(jpd.getUsercenter());
					yund.setYundh(jpd.getYundh());
					yund.setQuybh(jpd.getQuybh());
					yund.setDaztbh(jpd.getDaztbh());
					yund.setBeiz1("SYS");
					yund.setBeiz(jcw.getChewbh());
					yund.setJijbs("1");
					yund.setKach(jpd.getKach());
					yund.setZhuangt("3");
					yund.setEditTime(jpd.getEdit_time());
					updateChelpd(jpd); //修改车辆排队的状态为准入
					this.updateYundzt(yund); //修改运单的状态为入厂
					this.insertChurcls(yund);
					break;
				}
			   }
		}
		for(Chelpd pd:zhidcw){
			for(int i=0;i<kongxcw.size();i++){
				Chew cw=kongxcw.get(i);
				if(pd.getUsercenter().equals(cw.getUsercenter()) && pd.getZdchew().equals(cw.getChewbh())
				  && pd.getDaztbh().equals(cw.getDaztbh())){
					 //设置卸货序号 如果当期存在当天大站台卸货序号则在当期序号上加1 否则从1开始
					if(xiehxh.containsKey(pd.getUsercenter()+pd.getDaztbh())){
						 pd.setXiehxh(strformat(Integer.parseInt(String.valueOf( xiehxh.get(pd.getUsercenter()+pd.getDaztbh())))+1));
						 xiehxh.remove(pd.getUsercenter()+pd.getDaztbh());
						 xiehxh.put(pd.getUsercenter()+pd.getDaztbh(), pd.getXiehxh());
						}else{
						xiehxh.put(pd.getUsercenter()+pd.getDaztbh(), 1);
						pd.setXiehxh(strformat(1));
						}
					
					//指定车位
					pd.setChewbh(cw.getChewbh());
					pd.setPaidzt("1");//排队状态准入
					pd.setNeweditor("SYS"); //当前登录的用户
					cw.setChewzt("1");
					cw.setEditor("SYS"); //当前登录的用户
					cw.setZhuangt("0");
					Yund yund= new Yund();
					if(liuc.containsKey(pd.getUsercenter()+pd.getQuybh()+pd.getDaztbh()+"3")){
						yund.setBiaozsj((String) liuc.get(pd.getUsercenter()+pd.getQuybh()+pd.getDaztbh()+"3"));
					}
					yund.setEditor("SYS");
					yund.setUsercenter(pd.getUsercenter());
					yund.setYundh(pd.getYundh());
					yund.setQuybh(pd.getQuybh());
					yund.setDaztbh(pd.getDaztbh());
					yund.setBeiz1("SYS");
					yund.setBeiz(pd.getChewbh());
					yund.setJijbs("0");
					yund.setKach(pd.getKach());
					yund.setZhuangt("3");
					yund.setEditTime(pd.getEdit_time());
					yund.setChengysdm(pd.getChengysbh());
					int num=updateChewzt(cw); //修改车位状态为 忙
					if(1==num){
						updateChelpd(pd); //修改车辆排队的状态为准入
						this.updateYundzt(yund); //修改运单的状态为入厂
						this.insertChurcls(yund);
						removeCysxx(kongxcw,cw);
						break;	
					}else{
						break;
					}
					
				}
				
			}
		}	
		 //正常指定车位
		for(Chelpd rcpd:dengdrc){
			for(int n=0;n<kongxcw.size();n++){
				Chew kxcw=kongxcw.get(n);
				Yund yund= new Yund();
				if(rcpd.getUsercenter().equals(kxcw.getUsercenter()) && rcpd.getDaztbh().equals(kxcw.getDaztbh()) &&  ( rcpd.getPaidsx().equals(kxcw.getChewsx()) ||  kxcw.getChewsx().equals("3"))){
					 //设置卸货序号 如果当期存在当天大站台卸货序号则在当期序号上加1 否则从1开始
					if("2".equals(rcpd.getPaidsx())){
						if(rcpd.getChengysbh().equals(kxcw.getChengysbh())){
							//rcpd.setChewbh(kxcw.getChewbh());
							//yund.setBeiz(kxcw.getChewbh());
						}else if("3".equals(kxcw.getChewsx())){
							//rcpd.setChewbh(kxcw.getChewbh());
							//yund.setBeiz(kxcw.getChewbh());
							//rcpd.setYuanpdsx(rcpd.getPaidsx());
							rcpd.setPaidsx("3");
						}else{
							continue;
						}
					}else{
						if("1".equals(rcpd.getPaidsx())){
							if("3".equals(kxcw.getChewsx())){
								rcpd.setPaidsx("3");
							}
						}else{
							continue;
						} 	
					}
					rcpd.setChewbh(kxcw.getChewbh());
					yund.setBeiz(kxcw.getChewbh());
					if(xiehxh.containsKey(rcpd.getUsercenter()+rcpd.getDaztbh())){
						rcpd.setXiehxh(strformat(Integer.parseInt(String.valueOf( xiehxh.get(rcpd.getUsercenter()+rcpd.getDaztbh())))+1));
						xiehxh.remove(rcpd.getUsercenter()+rcpd.getDaztbh());
						xiehxh.put(rcpd.getUsercenter()+rcpd.getDaztbh(), rcpd.getXiehxh());
					 }else{
						xiehxh.put(rcpd.getUsercenter()+rcpd.getDaztbh(), 1);
						rcpd.setXiehxh(strformat(1));
					 }
					rcpd.setPaidzt("1");//排队状态准入
					rcpd.setNeweditor("SYS"); //当前登录的用户
					kxcw.setChewzt("1");
					kxcw.setEditor("SYS"); //当前登录的用户
					kxcw.setZhuangt("0");  //车位状态空闲
					if(liuc.containsKey(rcpd.getUsercenter()+rcpd.getQuybh()+rcpd.getDaztbh()+"3")){
						yund.setBiaozsj((String) liuc.get(rcpd.getUsercenter()+rcpd.getQuybh()+rcpd.getDaztbh()+"3"));
					}
					yund.setEditor("SYS");
					yund.setUsercenter(rcpd.getUsercenter());
					yund.setYundh(rcpd.getYundh());
					yund.setQuybh(rcpd.getQuybh());
					yund.setDaztbh(rcpd.getDaztbh());
					yund.setBeiz1("SYS");
					yund.setJijbs("0");
					yund.setKach(rcpd.getKach());
					yund.setZhuangt("3");
					yund.setEditTime(rcpd.getEdit_time());
					yund.setChengysdm(rcpd.getChengysbh());
				    int num=updateChewzt(kxcw);     //修改车位状态为 忙
				    if(1==num){
					    updateChelpd(rcpd);     //修改车辆排队的状态为准入
						this.updateYundzt(yund);//修改运单的状态为入厂
						this.insertChurcls(yund);
						removeCysxx(kongxcw,kxcw);
						break;   
				   }else{
					    break;
				   }
					
				}
			}
		 }
		logger.info("车辆入厂分配车位任务执行结束!");
    }
	//当某一车位占用后，移除同一车位对应的供应商信息
	public void  removeCysxx(List<Chew> list,Chew cc){
		for(int i=list.size()-1;i>=0;i--){
			Chew kxcw=list.get(i);
			if(kxcw.getUsercenter().equals(cc.getUsercenter()) && kxcw.getDaztbh().equals(cc.getDaztbh()) 
					&& kxcw.getChewbh().equals(cc.getChewbh())){
				list.remove(i);
			}
		}
	}
	
	public String strformat(int xuh){
		DecimalFormat df = new DecimalFormat("000");
		String str = df.format(xuh);
	return str;
	}
	//排队入厂任务
	public  void queueRCTask()throws Exception{
			queueTask();
			zhidCWTask();
	}
	//查询车位信息
	public Map<String, Object> chewxx(Chew bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("queue.queryChewxx", bean,bean);
	}
	
	
}

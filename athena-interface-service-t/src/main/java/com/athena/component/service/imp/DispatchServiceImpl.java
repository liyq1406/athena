package com.athena.component.service.imp;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.jws.WebService;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;

import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.FileNotFindException;
import com.athena.component.exchange.InziDbUtils; 
import com.athena.component.service.AnxJsInter;
import com.athena.component.service.CkxCommonFunc;
import com.athena.component.service.DispatchService;
import com.athena.component.service.DenglswtxInter;
import com.athena.component.service.IFJScheduleService;
import com.athena.component.service.IPCDailyProduceService;
import com.athena.component.service.IPCDailyRollService;
import com.athena.component.service.IPCLeijjfService;
import com.athena.component.service.IPCScheduleService;
import com.athena.component.service.IYaoHLJhService;
import com.athena.component.service.Kanbyhl;
import com.athena.component.service.Kdys;
import com.athena.component.service.PJCalculate;
import com.athena.component.service.Ziykz;
import com.athena.component.service.bean.InterfaceConfig;
import com.athena.component.service.bean.OrderConfig;
import com.athena.component.service.utls.InfaceParserConfig;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

@Component
@WebService(endpointInterface="com.athena.component.service.DispatchService",serviceName="/dispatchServiceImpl")
public class DispatchServiceImpl implements DispatchService {
	protected static Logger logger = Logger.getLogger(DispatchServiceImpl.class);	//定义日志方法
	private static String urlPath = null;
	@Inject
	private DataExchange dataEchange;
	@Inject 
	protected AbstractIBatisDao baseDao;//注入baseDao
	
	private final static int NOT_FOUND_FILE =  99;  //没有找到文件报错
	private final static int EXP_ERROR =  1;  //程序错误
	
	private final static String RUN_ERROR_STATE = "2"; //接口运行出错
	
	
	/**
	 * 一次执行一条接口命令
	 * 	返回值： 1 说明运行出现严重错误
	 *          99：说明找到文件
	 *          0：运行结束
	 */
	@Override
	public int dispatchTask(String taskName) {
		if(!taskName.startsWith("4")){
			return doInterface(taskName);
		}else{
			return doZBC(taskName);
		}
	}
	
	/**
	 * 调用准备层业务-webservice
	 * @param taskName
	 * @return
	 */
	private int doZBC(String taskName) {
		logger.info("开始调用批量"+taskName);
		int result = 0;
		
		if(urlPath==null){
			InputStream  in = DispatchServiceImpl.class.getResourceAsStream("/config/exchange/urlPath.properties");
			Properties pp = new Properties();
			try {
				pp.load(in);
				urlPath = pp.getProperty("urlPath");
//				System.out.println("urlPath =  " + urlPath);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		try{
			ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
			logger.info("执行接口"+taskName);
			if("4010".equals(taskName)){
				//参考系：未来年份日历生成
			}else if("4020".equals(taskName)){
				//参考系：未来几日处理
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.calculate();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4030".equals(taskName)){
				//参考系：物流路径总图处理
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.addWullj();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4040".equals(taskName)){
				//参考系：小火车时刻表生成
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.calculateXiaohcYssk();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4050".equals(taskName)){
				//参考系：生效期处理
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.updateUtilControlBiaos();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4060".equals(taskName)){
				//看板循环规模计算
				factory.setServiceClass(Kanbyhl.class);
				factory.setAddress(urlPath+"kanbjsService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				Kanbyhl client = (Kanbyhl) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.numerationAllUsercenter();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4070".equals(taskName)){
				//看板规模自动生效处理
				factory.setServiceClass(Kanbyhl.class);
				factory.setAddress(urlPath+"kanbjsService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				Kanbyhl client = (Kanbyhl) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.updateXiafgm();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4080".equals(taskName)){
				//资源快照删除
				factory.setServiceClass(Ziykz.class);
				factory.setAddress(urlPath+"ziykz");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				Ziykz client = (Ziykz) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.ziyhqrqManage();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4090".equals(taskName)){
				//周期订单计算（IL日）
				factory.setServiceClass(PJCalculate.class);
				factory.setAddress(urlPath+"pjCalculate");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				PJCalculate client = (PJCalculate) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.pjCalculate();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4100".equals(taskName)){
				//按需订单计算 97W sys
				factory.setServiceClass(AnxJsInter.class);
				factory.setAddress(urlPath+"anxOrderService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				AnxJsInter client = (AnxJsInter) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.anxOrderMethodUL();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4101".equals(taskName)){ //hzg 增加 给王凌测试用 2013-5-4
				//按需订单计算 97W sys
				factory.setServiceClass(AnxJsInter.class);
				factory.setAddress(urlPath+"anxOrderService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				AnxJsInter client = (AnxJsInter) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.anxOrderMethodUW();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4110".equals(taskName)){
				//KDYS 执行层调度
				factory.setServiceClass(Kdys.class);
				factory.setAddress(urlPath+"kdysService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				Kdys client = (Kdys) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.executeKdysService();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4120".equals(taskName)){
				logger.info("正在调用批量4120......");
				// 计算累计交付差额
				factory.setServiceClass(IPCLeijjfService.class);
				factory.setAddress(urlPath+"pclService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IPCLeijjfService client = (IPCLeijjfService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.callPcSchedule(null);
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4130".equals(taskName)){
				// 日滚动排产
				factory.setServiceClass(IPCDailyRollService.class);
				factory.setAddress(urlPath+"pcdService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IPCDailyRollService client = (IPCDailyRollService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.callPcDailyRoll(null);
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4140".equals(taskName)){
				// 滚动周期模拟排产
				factory.setServiceClass(IPCScheduleService.class);
				factory.setAddress(urlPath+"pcmService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IPCScheduleService client = (IPCScheduleService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.callPcSchedule(null);
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4150".equals(taskName)){
				logger.info("正在调用批量4150......");
				// 更新入库明细
				factory.setServiceClass(IPCDailyProduceService.class);
				factory.setAddress(urlPath+"pcpService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IPCDailyProduceService client = (IPCDailyProduceService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.callPcDailyProduce(null);
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4160".equals(taskName)){
				// 计算要车计划
				factory.setServiceClass(IYaoHLJhService.class);
				factory.setAddress(urlPath+"yaocjhService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IYaoHLJhService client = (IYaoHLJhService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.createYaoHLJhMx();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4170".equals(taskName)){
				// 内部要货令表仓库子仓库批量
				factory.setServiceClass(IFJScheduleService.class);
				factory.setAddress(urlPath+"fjService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				IFJScheduleService client = (IFJScheduleService) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.scheduleRun(null);
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4180".equals(taskName)){
				//定时计算cmj
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				//设置连接超时
				recieveTimeOutWrapper(client);
				client.calculateCmj();
			}else if("4190".equals(taskName)){
				//定时将模板数据导入运输时刻表
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.insertTimeOut();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4210".equals(taskName)){
				//运输时刻定时计算方法
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.jisYunssk();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4220".equals(taskName)){
				//定时检查版次号信息，如果没有与别的表关联则删除
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.timingTask();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4230".equals(taskName)){
				logger.info("开始调用批量4230......");
				//清除日历版次(执行层专用)
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.clearVersion();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4240".equals(taskName)){
				//将未来编组号按照生效时间更新到编组号中，并清空未来编组号和生效时间(执行层专用)
				factory.setServiceClass(CkxCommonFunc.class);
				factory.setAddress(urlPath+"CkxCommonFunc");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				CkxCommonFunc client = (CkxCommonFunc) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.updateWeilbzhTobianzh();
				logger.info("批量"+taskName+"调用服务端结束");
			}else if("4250".equals(taskName)){
				//准备层提醒零件，零件供应商的数量
				factory.setServiceClass(DenglswtxInter.class);
				factory.setAddress(urlPath+"denglswtxService");
				logger.info("批量"+taskName+"加载ServiceClass服务及urlPath结束");
				DenglswtxInter client = (DenglswtxInter) factory.create();
				logger.info("批量"+taskName+"创建客户端实例结束");
				//设置连接超时
				recieveTimeOutWrapper(client);
				logger.info("批量"+taskName+"调用服务端开始");
				client.insertCkxShiwtx();
				logger.info("批量"+taskName+"调用服务端结束");
			}
			logger.info("执行"+taskName+"成功!");
		}catch (Exception e) {
			result = 1;
			logger.error("批量异常"+e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 连接时间为10分钟
	 * @param o
	 */
	 private  void recieveTimeOutWrapper(Object o)  
	 {  
	     Conduit conduit = (ClientProxy.getClient(o).getConduit());  
	     HTTPConduit hc = (HTTPConduit)conduit;  
	     HTTPClientPolicy client = new HTTPClientPolicy();  
	     // 5分钟超时时间
	     client.setReceiveTimeout(1000 * 60 * 10);    
	     hc.setClient(client);  
	 }
	
	/**
	 * 调用接口程序
	 * @param taskName
	 * @return
	 */
	private int doInterface(String taskName){
		//定义一个返回值 给C 0：表示执行成功，1：表示执行失败,99说明找到文件
		int result = 0;
		//定义一个值记录接口编号，进行输出数据源判断
		String codeId = "";
		
		//定义接口的开始运行时间
		String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		/**
		 * 接口开始运行  
		 * 	将接口的状态  
		 * 		设置为 1  运行
		 * 		开始时间  设置为 当前时间 
		 */
		//InziDbUtils.UpdateInZIDB_ZT("1", baseDao.getConnection(), taskName);
		InziDbUtils.getInstance().UpdateInZIDB_ZT("1", baseDao.getConnection(), taskName,nowTime);
		
		System.out.println(taskName);		
		int top = 0;
		try {
			//1：解析 enchange-interface.xml 得到此taskName为id的配置信息
			InterfaceConfig inter = InfaceParserConfig.getInstance()
					.getDataParserConfig(taskName);
			if (inter != null) {
				List<OrderConfig> orders = inter.getOrders();
				//执行
				if (inter.getType().equals("in")) {
					//是输入接口  按照输入处理				
					List<OrderConfig> txtOrders = new ArrayList<OrderConfig>(); //处理txt的配置
					List<OrderConfig> tableOrders = new ArrayList<OrderConfig>(); //向业务表分发的配置

					for (OrderConfig order : orders) {
						if ("true".equals(order.getIstxt())
								|| order.getIstxt() == null) {
							//为true  或者  不填  则为处理txt
							txtOrders.add(order);
						} else {
							tableOrders.add(order);
						}
					}

					//1: txt--->中间表 单线程处理
					for (int i = 0; i < txtOrders.size(); i++) {
						try{
							dataEchange.doExchange(txtOrders.get(i).getId());
						}catch (FileNotFindException e1) {
							throw e1;
						}catch(Exception e){
							//看此order配置 是否是继续执行
							String isgoon = dataEchange.getIsGoon();
							if ("true".equals(isgoon) || isgoon == null) {
								continue;
							} else {
								//中止后面的执行 
								top = 1;
								throw new RuntimeException("文件解析出错,分发中止.");
							}
						}
					}

					//2; 中间表--->业务表  多线程
					if (top <= 0) {
						//才执行分发					
						for (int j = 1; j <= tableOrders.size(); j++) {
							//设置文件移动的标记  为true  则移动文件  .  执行最后一个order后，移动 
							dataEchange.doExchange(tableOrders.get(j - 1).getId());
						}
					}

				} else {
					//或则是out 按照输出处理
					//现在默认 是一个接着一个 默认执行
					for (OrderConfig orderCon : orders) {
						codeId = orderCon.getId();
						dataEchange.doExchange(orderCon.getId());
					}
				}
			}
		}catch(FileNotFindException e){
			/**
			 * 接口结束或者发生异常
			 * 	将接口状态更改为结束
			 * 	结束时间 维护为当前时间
			 */
			top=1;
			baseDao.getSdcDataSource(getSourceId(codeId));
			InziDbUtils.getInstance().UpdateInZIDB_ZT(RUN_ERROR_STATE, baseDao.getConnection(), taskName,nowTime);
			dataEchange.total=0;
			logger.error("没有找到文件,状态99");
			return NOT_FOUND_FILE;
		}catch (Exception ex){
			//接口直接异常 总表状态设置为 运行出错
			/**
			 * 接口结束或者发生异常
			 * 	将接口状态更改为出错
			 * 	结束时间 维护为当前时间
			 */
			top=1;
			baseDao.getSdcDataSource(getSourceId(codeId));
			InziDbUtils.getInstance().UpdateInZIDB_ZT(RUN_ERROR_STATE, baseDao.getConnection(), taskName,nowTime);
			dataEchange.total=0;
			logger.error("程序出现异常,状态1");
			return EXP_ERROR;
		}
		
		//没有出错 则 向接口总表中  将状态设置为 完成 
		/**
		 * 接口结束或者发生异常
		 * 	将接口状态更改为结束
		 * 	结束时间 维护为当前时间
		 */
		if(top<=0){
			//根据接口总表的ID和开始时间，结合'接口文件错误记录信息'表  如果有记录 则认为此接口运行是有异常的
			baseDao.getSdcDataSource(getSourceId(codeId));
			InziDbUtils.getInstance().UpdateInZIDB_ZTByMx( nowTime, baseDao.getConnection(), taskName);
			result = InziDbUtils.getInstance().getZIDB_ZTByid(baseDao.getConnection(),taskName);	
			logger.info("当前接口:"+taskName+",状态为："+result);
		}
		dataEchange.total=0;
		logger.info("当前接口:"+taskName+",状态为："+result);
		return result;
	}
	/**
	 * 批量执行接口命令
	 */
	@Override
	public void dispatchTasks(List<String> taskNames) {	
		if(taskNames!=null){
			for(String taskName : taskNames){
				dispatchTask(taskName);
			}
		}
	}
	
	/**
	 * 接口数据源取值
	 */
	public String getSourceId(String codeId){
		String sourceId = codeId.startsWith("3")?"2":"1";
		return sourceId;
	}
	

}

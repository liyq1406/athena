package com.athena.print.controller;


import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.db.ConstantDbCode;
import com.athena.print.StaticPrintTaskExecutor;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.service.PreparQtaskService;
import com.athena.util.cache.CacheManager;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * WebService调用时触发的接口实现类
 * @author dsimedd001
 *
 */

@Component
@WebService(endpointInterface="com.athena.print.controller.GetPrintService",serviceName="/GetPrintService")
public class  PrintServcieImpl implements GetPrintService {
	private static Logger logger=Logger.getLogger(PrintServcieImpl.class);
	@Inject
	private  AbstractIBatisDao baseDao;
	
	//缓存 
	@Inject
	private CacheManager cacheManager;

	@Inject 
	private PreparQtaskService  preparQtaskService ;
	
	
	//WwbService 直接触发作业子线程
	@Override
	public void getQtaskController(String qid)throws RuntimeException{
			logger.info("作业"+qid+"调用getQtaskController开始");
			try{
				PrintQtaskmain printQtaskmain = (PrintQtaskmain)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.queryPrintQtaskmainWs",qid);
 				StaticPrintTaskExecutor.getPrintTaskExecutor().execute
				(new PrintChildTaskExecutorService(printQtaskmain,cacheManager,preparQtaskService,baseDao));
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			logger.info("作业"+qid+"调用getQtaskController结束");
		}
	@Override
	public PrintQtaskmain addTaskSequence(String printUser, String printSheet,
			String printCangk, String deviceGroup, String printContent,
			String printModel, int printNum, int printCount, String printType,
			String usercenter, int count, String quyzs, int areasheet,
			int arealist, String seq) {
		// TODO Auto-generated method stub
		return null;
	}
}

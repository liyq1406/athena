package com.athena.print.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.print.StaticPrintTaskExecutor;
import org.apache.log4j.Logger;

import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.service.PreparQtaskService;
import com.athena.util.athenalog.impl.BackgroundRunLog;
import com.athena.util.cache.CacheManager;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * 对打印作业取主任务
 * @author dsimedd001
 *
 */
@Component
public class GetQtaskController {

	private static Logger logger=Logger.getLogger(GetQtaskController.class);
	
	@Inject
	private BackgroundRunLog backgroundRunLog;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	
	//缓存 只能从服务器拿  从测试方法里 取不到 
	@Inject
	private CacheManager cacheManager;
	
	@Inject 
	private PreparQtaskService  preparQtaskService ;
	
	/**
	 * 获取主作业服务类
	 */
	public void startQtaskController(){
		try{
			//先获取需要打印作业对应的打印机组 和用户 进行分组
			logger.info("获取主任务开始");	
			List<PrintQtaskmain> printPgid = preparQtaskService.getPrintPgid();
			
			for (PrintQtaskmain printpgid : printPgid){
				//在分组的 打印机组中分别取每组下的10条主任务,状态为-1（待分配打印），0（待分配），1（打印中断）
				Map map = new HashMap();
				map.put("pgid",printpgid.getPgid() );
				map.put("saccount",printpgid.getSaccount());
				List<PrintQtaskmain> prepareTaskList = preparQtaskService.getPrepareTaskList(map);
				for (PrintQtaskmain printQtaskmain : prepareTaskList){
					StaticPrintTaskExecutor.getPrintTaskExecutor().execute
					(new PrintChildTaskExecutorService(printQtaskmain,cacheManager,preparQtaskService,baseDao));
				}
			}
			logger.info("获取主任务结束");
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
	}

	
	
	
}

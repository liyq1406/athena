package com.athena.print.controller;

import org.apache.log4j.Logger;
import org.apache.xpath.operations.String;

import com.athena.db.ConstantDbCode;
import com.athena.print.PrintableTask;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.service.PreparQtaskService;
import com.athena.print.service.PrinterTaskExecutorService;
import com.athena.print.template.AnalysisPrintTemplate;
import com.athena.util.cache.CacheManager;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 开启线程池 ,子线程执行打印子任务
 * @author dsimedd001
 *
 */
public class PrintChildTaskExecutorService implements PrintableTask{

	private static Logger logger=Logger.getLogger(PrintChildTaskExecutorService.class);
	
	private PrintQtaskmain printQtaskmain;
	
	private CacheManager cacheManager;
	
	private PreparQtaskService  preparQtaskService ;
	
	private AbstractIBatisDao baseDao;

	private PrinterTaskExecutorService printerTaskExecutor;
	/**
	 * 构造函数
	 * @param printQtaskmain 打印主任务对象
	 * @param cacheManager  模板缓存
	 * @param preparQtaskService 调用注入的准备作业Service
	 * @param baseDao 获取数据源datasource
	 */
	public PrintChildTaskExecutorService(PrintQtaskmain printQtaskmain,CacheManager cacheManager,
			PreparQtaskService  preparQtaskService,AbstractIBatisDao baseDao) {
		this.printQtaskmain = printQtaskmain;
		this.cacheManager = cacheManager;
		this.preparQtaskService = preparQtaskService;
		this.baseDao = baseDao;
        this.printerTaskExecutor = new PrinterTaskExecutorService(baseDao);
	}
	
	/**
	 * 子线程进行的任务
	 */
	@Override
	public void run() {
		//生成打印文档
			//根据主表作业ID  查询出其下对应的子任务
            if(printQtaskmain.getStatus() == -1){
                logger.info("作业"+printQtaskmain.getQid()+"获取子任务开始");
                List<PrintQtaskinfo> plist = preparQtaskService.getTaskinfoList(printQtaskmain.getQid());
                logger.info("作业"+printQtaskmain.getQid()+"获取子任务结束");
                
                long startTime = System.currentTimeMillis();
                logger.info("作业"+printQtaskmain.getQid()+"生成打印文档开始时间为"+startTime);
			   // for (int i = 0; i < plist.size(); i++) {
                if(plist.size()>0){
                    new AnalysisPrintTemplate(printQtaskmain.getQid(), plist,baseDao,cacheManager).execute();
                    //更新主列表中的作业的状态为  新作业
                    baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain2",printQtaskmain.getQid());
                }
				//}
			    long endTime = System.currentTimeMillis();
                logger.info("作业"+printQtaskmain.getQid()+"生成打印文档结束时间为"+endTime);
                logger.info("************************打印文档生成时间为************:"+ ( (endTime-startTime)/1000) );
			}

		    //给作业分配打印机(分配打印机时，根据作业状态来判断，打印中断  待分配 和 待分配打印) 并连接socket发送文件打印

			printerTaskExecutor.runTasks(printQtaskmain);
	}

	@Override
	public void cancel() {
		try {
            this.printerTaskExecutor.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

    @Override
	public String toString(){
		return printQtaskmain.getQid();
	}

	
	public PrintQtaskmain getPrintQtaskmain() {
		return printQtaskmain;
	}

	public void setPrintQtaskmain(PrintQtaskmain printQtaskmain) {
		this.printQtaskmain = printQtaskmain;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public PreparQtaskService getPreparQtaskService() {
		return preparQtaskService;
	}

	public void setPreparQtaskService(PreparQtaskService preparQtaskService) {
		this.preparQtaskService = preparQtaskService;
	}

	public AbstractIBatisDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(AbstractIBatisDao baseDao) {
		this.baseDao = baseDao;
	}
	
}

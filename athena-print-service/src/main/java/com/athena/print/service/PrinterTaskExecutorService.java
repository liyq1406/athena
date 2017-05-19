/**
 * 
 */
package com.athena.print.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.db.ConstantDbCode;
import com.athena.print.CustomException;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * Title:打印监管平台
 * </p>
 * 
 * <p>
 * Description:打印任务执行服务(新作业)
 * </p>
 * 
 * <p>
 * Copyright:Copyright (c) 2011.9
 * </p>
 * 
 * <p>
 * Company:iSoftstone
 * </p>
 * 
 * @author Administrator
 * @version 1.0
 */
public class PrinterTaskExecutorService{
	
	private static Logger logger=Logger.getLogger(PrinterConnectorService.class);
	
	private AbstractIBatisDao baseDao;
	private PrinterConnectorService printerConnector;
	/**
	 * @param baseDao 数据源
	 */
	//构造函数
	public PrinterTaskExecutorService(AbstractIBatisDao baseDao){
		 this.baseDao = baseDao;
	}
	
	//map 用来存贮 用户以前使用过的打印机
	private Map<String,String> DeviceMap = new HashMap<String, String>();
	
	/**
	 * 执行新作业打印任务
	 */
	public void runTasks(PrintQtaskmain printQtaskmain) {

		try {
			//根据用户获取上一次完成任务所使用的打印机
			logger.info("主作业"+printQtaskmain.getQid()+"查找以前使用过的打印机");
			java.util.List<PrintQtaskmain> pTaskList =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskmainstatus2",printQtaskmain.getSaccount());
			if(pTaskList.size()!=0 && pTaskList.get(0) != null){
				//把用户 和 打印机编号 以 key,value 存入Map 中
				DeviceMap.put(pTaskList.get(0).getSaccount(),pTaskList.get(0).getSdevicecode());
			}
			//分配 主作业到打印机  （根据用户    打印机组  作业编号 来分配打印机）
			logger.info("主作业"+printQtaskmain.getQid()+"分配打印机开始");
			assignTaskToPrinter(printQtaskmain);
			logger.info("主作业"+printQtaskmain.getQid()+"分配打印机结束");
		} catch (CustomException ex) {
			logger.error(ex.getMessage());
		}
	}
	
	/*
	 * 分配 主作业到打印机  （根据用户    打印机组   作业编号 来分配打印机） 
	 */
	@SuppressWarnings("unused")
	private void assignTaskToPrinter(PrintQtaskmain printQtaskmain){

        String lastDevice = DeviceMap.get(printQtaskmain.getSaccount());    //上次用户使用打印机
        PrintDevice thisDevice = null;
        if(StringUtils.isNotEmpty(lastDevice)){		//上次使用
            //判断上次使用打印机是否空闲
            PrintDevicestatus bean = new PrintDevicestatus();
            bean.setSpcode(lastDevice);
            bean.setSpcodes(printQtaskmain.getPgid());
            bean.setUsercode(printQtaskmain.getSaccount());
            //根据 打印机编号 、打印机组编号、 用户、 查询出 该用户上次使用的打印机是否空闲
            PrintDevicestatus pDevice = (PrintDevicestatus)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintDevicestatus2",bean);
            if(pDevice != null){		//直接分配打印机
                thisDevice = (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.getPrintDevice",lastDevice);
                logger.info("作业"+printQtaskmain.getQid()+"使用上次的打印机,编号为"+lastDevice);
            }
        }
        if (thisDevice == null) {
        	thisDevice = assignTaskDevice(printQtaskmain.getPgid());
        	//如果该打印机组发生了故障 则会查询不到空闲的打印机组  则需要根据替代设备在次判断
        	//先根据打印机组去查询出该打印机组对象
    		PrintDevice pgid = (PrintDevice) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPDevicegroup",printQtaskmain.getPgid());
        	 if(StringUtils.isNotEmpty(pgid.getReplacespcode())){
        		 logger.info("作业"+printQtaskmain.getQid()+"分配到打印机"+pgid.getReplacespcode());
        			//需要在判断是否有打印设备的替换
                    thisDevice = getReplacedevice(pgid);
                    logger.info("作业"+printQtaskmain.getQid()+"使用替代的打印机"+thisDevice.getSpcode());
                }
        } 		
        if (thisDevice != null) {
            //需要在判断是否有打印设备的替换
//        	logger.info("作业"+printQtaskmain.getQid()+"分配到打印机"+thisDevice.getSpcode());
//            if(StringUtils.isNotEmpty(thisDevice.getReplacespcode())){
//                thisDevice = getReplacedevice(thisDevice);
//                logger.info("作业"+printQtaskmain.getQid()+"使用替代的打印机"+thisDevice.getSpcode());
//            }
			//同时将设备的打印机修改为占用
			engrossDevice(printQtaskmain, thisDevice);
            //执行打印
            executorPrint(printQtaskmain, thisDevice);
        } else {
            logger.info("用户"+printQtaskmain.getSaccount()+"对应的作业"+printQtaskmain.getQid()+"在打印机组"+printQtaskmain.getPgid()+"中没有找到空闲打印机");
        }

	}

		/**
		 * 抢占打印机是否成功
		 */
    private void engrossDevice(PrintQtaskmain printQtaskmain, PrintDevice thisDevice) throws CustomException{
        //修改 打印机主状态为占用
        //把分配的打印机编号写入主作业表中  并更新主作业的状态为  启动
        printQtaskmain.setSdevicecode(thisDevice.getSpcode());
        int number  = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice", printQtaskmain.getSdevicecode());
        logger.info("作业"+printQtaskmain.getQid()+"占用编号为"+printQtaskmain.getSdevicecode()+"打印机");
        if(number==0){
           throw new CustomException(printQtaskmain.getQid());
        } 
//        else {
//        	 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain5", printQtaskmain);
//        }
    }
	
		/**
		 * 未使用打印机
		 * @param thisDeviceGroup
		 */
	
    private  PrintDevice assignTaskDevice(String thisDeviceGroup){
        //走上次未使用流程
        boolean flag = getDeviceflag(thisDeviceGroup);   //根据打印机组编号来判断是否开启优先级
        return getPrintDevice(thisDeviceGroup, flag);  //根据打印机组号，组是否开启优先级，查询打印机列表
    }


    /*
     * 获取空闲打印机后执行打印
     */
    private void executorPrint(PrintQtaskmain printQtaskmain,PrintDevice thisDevice){
        //连接打印机执行打印
    	logger.info("作业"+printQtaskmain.getQid()+"准备开始执行打印");
        this.printerConnector = new PrinterConnectorService( printQtaskmain, thisDevice, baseDao);
        this.printerConnector.execute();
    }

    /*
     * 获取存在空闲打印机的打印机组
     */
    private PrintDevicestatus getDeviceGroup(PrintQtaskmain printQtaskmain){
        //获取存在空闲打印机的打印机组
        return  (PrintDevicestatus)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintDevicestatusByspcodes",printQtaskmain.getPgid());
    }

    /**
     * 根据打印机组编号来判断是否开启优先级
     * @param pgid 打印机组编号
     * @return true 开启
     *         false 不开启
     */
    private boolean getDeviceflag(String pgid){

        //根据打印机组编号来判断是否开启优先级
        Long nflag = (Long)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.getPrintDevicegroup",pgid);

        if( nflag.intValue() == 1 ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据打印机组号，组是否开启优先级，查询打印机列表
     * @param pgid  打印机组号
     * @param flag  优先级
     * @return
     */
    private PrintDevice getPrintDevice( String pgid , boolean flag) {
        return (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintDevicestatus" + (flag ? "4":"3"),pgid);
    }

    /**
     * 根据打印机对象 去查询是否有 替代的打印机设备
     * @param device  打印机对象
     * @return
     */
    private PrintDevice getReplacedevice(PrintDevice device){
    	 return (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPDevice",device.getReplacespcode());
    }

	public void close() throws IOException {
       if (printerConnector != null) {
           printerConnector.close();
       }
    }
}

package com.athena.print.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import com.athena.component.runner.Command;
import com.athena.db.ConstantDbCode;
import com.athena.print.Constants;
import com.athena.print.ExceptionConstant;
import com.athena.print.coon.MultithreadingTool;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * <p>
 * Title:打印子系统
 * </p>
 * 
 * <p>
 * Description:
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
 * 打印机连接器
 * 
 * @author Administrator
 * @version 1.0
 */
public class PrinterConnectorService{
	
	private static Logger logger=Logger.getLogger(PrinterConnectorService.class);
	
	private PrintDevice printDevice;
	
	/**
	 * @return the printDevice
	 */
	public PrintDevice getPrintDevice() {
		return printDevice;
	}

	/**
	 * @param printDevice the printDevice to set
	 */
	public void setPrintDevice(PrintDevice printDevice) {
		this.printDevice = printDevice;
	}

	private PrintQtaskmain printQtaskmain;
	
	

	/**
	 * @return the printQtaskmain
	 */
	public PrintQtaskmain getPrintQtaskmain() {
		return printQtaskmain;
	}

	/**
	 * @param printQtaskmain the printQtaskmain to set
	 */
	public void setPrintQtaskmain(PrintQtaskmain printQtaskmain) {
		this.printQtaskmain = printQtaskmain;
	}
	// sockeck客户端
	private Socket socket = null;
	//字符流
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private PrintWriter socketWriter = null;


	private AbstractIBatisDao baseDao;

	/**
	 * @param printQtaskmain 打印的主作业
	 * @param printDevice 打印作业的打印设备
	 * @param baseDao 数据源
	 */
	public PrinterConnectorService( PrintQtaskmain printQtaskmain , PrintDevice printDevice , AbstractIBatisDao baseDao) {
		this.printQtaskmain = printQtaskmain;
		this.printDevice = printDevice;
		this.baseDao = baseDao;
	}
	
	/**
	 * 执行打印作业的总方法
	 */
	public void execute() {
		
		if(1==printQtaskmain.getStatus()){//针对打印中断作业
			logger.info("作业"+printQtaskmain.getQid()+"为打印中断作业");
			//根据打印机编号查询出    IP 及 对应的 打印设备辅状态
			PrintDevicestatus printDevciestatus = (PrintDevicestatus) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintDevicestatusByspcode", printQtaskmain.getSdevicecode());

			//依据打印机的状态来判断
			if(		//Constants.PIRNTER_BUSY.equals(printDevciestatus.getStatus())||	//繁忙直接过，下次再分
					Constants.PIRNTER_OUT_PAPER==printDevciestatus.getStatus()||
					Constants.PIRNTER_JAM_PAPER==printDevciestatus.getStatus()||
					Constants.PIRNTER_CONN_BROKEN==printDevciestatus.getStatus()||
					Constants.PIRNTER_STATUS_ALERT==printDevciestatus.getStatus())	{
				
				//打印机状态为 缺纸、卡纸、网络故障、打印机故障    则更新该作业为打印中断  辅状态为停止
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain4", printQtaskmain.getQid());

			}else if( Constants.PIRNTER_IDLE==printDevciestatus.getStatus() ){		//空闲处理
               sendFreePrintDevcie(printDevciestatus.getSip(), printDevciestatus.getSport());
			}
		}else{
            //针对 待分配 和 待分配打印作业
			 sendFreePrintDevcie(printDevice.getSip(), printDevice.getSport());
		}
	}

    private void sendFreePrintDevcie(String ip, String port) {
        try{
            //连接打印机
            open( ip, port );
            logger.info("作业"+printQtaskmain.getQid()+"在打印机IP为"+ip+"连接socket成功");
            //获取主作业下    一条状态为   未发送  的子任务path  发送文件流
            List<PrintQtaskinfo>  printQtaskinfo = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfo1",printQtaskmain.getQid());

            if( printQtaskinfo.size()!=0 ){
            	logger.info("作业"+printQtaskmain.getQid()+"开始发送文件流到打印机IP为"+ip);
            	if(printQtaskinfo.size()>200){
            			int n=printQtaskinfo.size();
            			int m=n/200;
            			m = n%200==0?m:m+1;
            			for (int i = 0; i < m; i++) {
            				//每次取200条来发送打印 
                    		List<PrintQtaskinfo> p300 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfok",printQtaskmain.getQid());
                    		sendFileStream(p300);
                    		MultithreadingTool.getInstance().newTaskOperate(p300, printQtaskmain, baseDao);
						}
            	}else{
            		 sendFileStream(printQtaskinfo);
                     //作业同步操作的工作
                     MultithreadingTool.getInstance().newTaskOperate(printQtaskinfo, printQtaskmain, baseDao);
            	}
            }else{
                //更新主作业的状态为   停止
                baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain6",printQtaskmain.getQid());
                // 主作业 下未获取到子作业   则释放打印机并更新到打印机状态表中
                PrintDevicestatus bean = new PrintDevicestatus();
                bean.setLastqid(printQtaskmain.getQid());
                bean.setUsercode(printQtaskmain.getSaccount());
                bean.setSpcode(printQtaskmain.getSdevicecode());
                baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice3",bean);
            }
        } catch (SocketException socket ) {
            logger.error("作业"+printQtaskmain.getQid()+"的socket异常"+socket.getMessage());
            // 修改主作业的状态为 打印中断 、修改主作业的辅状态为停止  同时修改打印机的状态为 不占用
            baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain4",printQtaskmain.getQid());
        } catch (IOException e) {
            logger.error("作业"+printQtaskmain.getQid()+"的IO异常"+ExceptionConstant.readfile_error);
            baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice1", printQtaskmain.getSdevicecode());
        } catch (SQLException  e) {
            logger.error("作业"+printQtaskmain.getQid()+"的SQL异常"+e.getMessage());
        } finally {
            try {
                //关闭socket连接
                close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
	 * 打包发送文件流
	 * @throws IOException 
	 */
	private void sendFileStream(List<PrintQtaskinfo>  printQtaskinfo) throws IOException{
		Vector<FileInputStream> v = new Vector<FileInputStream>();
		for (int i = 0; i < printQtaskinfo.size(); i++) {
			v.add(new FileInputStream(new File(printQtaskinfo.get(i).getSfilename())));
		}
		Enumeration<FileInputStream> en = v.elements();	
		inputStreamReader = new InputStreamReader(new SequenceInputStream(en));
		bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
		if( null != inputStreamReader ){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain3",printQtaskmain.getQid());
			//同时更新打印机为繁忙
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice2",printQtaskmain.getSdevicecode());
			while(  null != (line = bufferedReader.readLine())){ 
				socketWriter.println(line);
			}	
		}
	}
	
	
	
	/**
	 * 打开socket连接
	 * 
	 * @throws IOException
	 */
	private void open(String ip, String port) throws IOException {
		//if (socket == null) {
			socket = new java.net.Socket();
			try {
				socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), 2000);
			} catch (SocketTimeoutException e) {
				throw new SocketException("与打印机建立连接超时！");
			}
			socketWriter = new PrintWriter(socket.getOutputStream());
		//}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if ( socket != null) {
			if (socketWriter != null) {
				socketWriter.close();
			}
			socket.close();
		}
	}

}

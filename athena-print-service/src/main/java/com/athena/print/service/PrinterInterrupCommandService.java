package com.athena.print.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
import com.athena.print.entity.sys.PrintDevicestatus;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

public class PrinterInterrupCommandService{
	
	private static Logger logger=Logger.getLogger(PrinterInterrupCommandService.class);
	

	
	private boolean closed;//状态
	
	private Socket socket = null;//sockeck客户端
	

	
	private AbstractIBatisDao baseDao; //数据源
	
	//private List<PrintQtaskmain> pList;
	
	private InputStreamReader inputStreamReader;
	
	private BufferedReader bufferedReader;
	
	private PrintWriter socketWriter = null;
	
	private PrintQtaskmain printQtask;
	
	/**
	 * @return the printQtask
	 */
	public PrintQtaskmain getPrintQtask() {
		return printQtask;
	}

	/**
	 * @param printQtask the printQtask to set
	 */
	public void setPrintQtask(PrintQtaskmain printQtask) {
		this.printQtask = printQtask;
	}

	public PrinterInterrupCommandService() {
		super();
	}
	
	public PrinterInterrupCommandService( PrintQtaskmain printQtask, AbstractIBatisDao baseDao ) {
		this.printQtask = printQtask;
		this.baseDao=baseDao;
	}

	//执行作业的打印
	public void execute(){

		String line ="";
		
		//根据打印机编号查询出    IP 及 对应的 打印设备辅状态
		PrintDevicestatus printDevciestatus = (PrintDevicestatus) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintDevicestatusByspcode", printQtask.getSdevicecode());
		//IP
		String ip = printDevciestatus.getSip();
		//port
		String port = printDevciestatus.getSport();
		

		//依据打印机的状态来判断
		if(		//Constants.PIRNTER_BUSY.equals(printDevciestatus.getStatus())||	//繁忙直接过，下次再分
				Constants.PIRNTER_OUT_PAPER==printDevciestatus.getStatus()||
				Constants.PIRNTER_JAM_PAPER==printDevciestatus.getStatus()||
				Constants.PIRNTER_CONN_BROKEN==printDevciestatus.getStatus()||
				Constants.PIRNTER_STATUS_ALERT==printDevciestatus.getStatus())	{
			
			//打印机状态为 缺纸、卡纸、网络故障、打印机故障    则更新该作业为打印中断  辅状态为停止
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain4", printQtask.getQid());

		}else if( Constants.PIRNTER_IDLE==printDevciestatus.getStatus() ){		//空闲处理
			
			try{
				
				try{	
					open( ip, port );//连接打印机
				}catch(IOException e){
					throw new Exception(e.getMessage());
				}
				
				//获取主作业下    一条状态为   未发送  的子任务path  发送文件流
				List<PrintQtaskinfo>  printQtaskinfo = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfo1",printQtask.getQid());
				
				if( printQtaskinfo.size()!=0 ){

					try{
						Vector<FileInputStream> v = new Vector<FileInputStream>();
						
						for (int i = 0; i < printQtaskinfo.size(); i++) {
							//FileInputStream input = new FileInputStream(new File(printQtaskinfo.get(i).getSfilename()));	//获取第一条		
							v.add(new FileInputStream(new File(printQtaskinfo.get(i).getSfilename())));
						}
						
						Enumeration<FileInputStream> en = v.elements();	
						
						inputStreamReader = new InputStreamReader(new SequenceInputStream(en));
						
						bufferedReader = new BufferedReader(inputStreamReader);
						if( null != inputStreamReader ){
							while(  null != (line = bufferedReader.readLine())){ 
								socketWriter.println(line);
							}	
						}
					}catch(Exception e){
						throw new Exception(ExceptionConstant.readfile_error);
					}

					//打印中或者打印中断同步操作的工作
					//MultithreadingTool.getInstance().interrupTaskOperate(printQtaskinfo, printQtask, baseDao);
				}else {
					//更新主作业的状态为   停止 
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain6",printQtask.getQid());
					// 主作业 下未获取到子作业   则释放打印机并更新到打印机状态表中
					PrintDevicestatus bean = new PrintDevicestatus();
					bean.setLastqid(printQtask.getQid());
					bean.setUsercode(printQtask.getSaccount());
					bean.setSpcode(printQtask.getSdevicecode());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice3",bean);
				}
			
			}catch(Exception e){	//异常
			
				logger.error(Thread.currentThread().getName() + e.getMessage());
				//修改主作业的状态为 打印中断 
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain4", printQtask.getQid());
				
				//清空打印机状态表 上 的最后任务  释放打印机(占用->不占用)
				//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatelastqid",printDevciestatus.getSip());

		}finally{
			try {
				close();
			} catch (IOException e1) {
				}
		    }
		}

		
	}
	
	/**
	 * 打开socket连接
	 * @throws IOException
	 */
	private void open(String ip,String port) throws IOException{
		if(socket==null){
			socket =new java.net.Socket();
			try {
				socket.connect(new InetSocketAddress(ip ,Integer.parseInt(port)),2000);
			} catch (SocketTimeoutException e) {
				throw new SocketException(ExceptionConstant.connect_drop);
			}
			socketWriter = new PrintWriter(socket.getOutputStream());
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException{
		
		if( false == closed && null != socket ){
			if( null != socketWriter ){
				socketWriter.close();
			}
			socket.close();
			closed = true;
		}
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}	

}

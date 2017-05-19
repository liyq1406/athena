/**
 * 解析模板替换参数类
 */
package com.athena.print.core;


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
import com.athena.print.ExceptionConstant;
import com.athena.print.MultithreadingTool;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevice;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author ZL
 *
 */
@Component
public class PrinterConnectorq extends Command<Object>{
	//Log4j 
	private static Logger logger=Logger.getLogger(AnalysisPrintTemplate.class);
	
	//打印机对象
	private PrintDevice printDevice;
	
	
	//主作业队列 实体
	private PrintQtaskmain printQtaskmain;
	 
	// 状态
	private boolean closed;
	// sockeck客户端
	private Socket socket = null;
	//字符流
	private InputStreamReader inputStreamReader;
	//bufferedReader
	private BufferedReader bufferedReader;
	//socketWriter
	private PrintWriter socketWriter = null;

	//数据源以参数的方式注入
	private AbstractIBatisDao baseDao;

	
	public PrinterConnectorq(){
	}
	
	//构造函数(带参数)
	public PrinterConnectorq( PrintQtaskmain printQtaskmain , PrintDevice printDevice , AbstractIBatisDao baseDao) {
		super();
		this.baseDao=baseDao;
		this.printQtaskmain = printQtaskmain;
		this.printDevice = printDevice;
	}

	
	@Override
	public void execute() {
		String line = "";

		
		//修改 打印机主状态为占用
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatePrintDevicestatusBydevice", printQtaskmain.getSdevicecode());
		
		try{
			try {
				// 连接打印机
				open(printDevice.getSip(), printDevice.getSport() );
			} catch (Exception e) {
				//抛出异常信息
				throw new Exception(e.getMessage());
			}
			
			//获取主作业下 一条状态为 未发送 的子任务path 发送文件流
			List<PrintQtaskinfo> printQtaskinfo = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pcenter.queryPrintQtaskinfo1", printQtaskmain.getQid());
			//判断是否有未发送的子任务
			if (printQtaskinfo.size() != 0) {
				
				try {
					Vector<FileInputStream> v = new Vector<FileInputStream>();
					
					for (int i = 0; i < printQtaskinfo.size(); i++) {
						v.add(new FileInputStream(new File(printQtaskinfo.get(i).getSfilename())));
					}
					
					//发送文件流
					//FileInputStream input = new FileInputStream(new File(printQtaskinfo.get(0).getSfilename()));
					//发送后 读取文件流
					Enumeration<FileInputStream> en = v.elements();	
					inputStreamReader = new InputStreamReader(new SequenceInputStream(en));
					//一行一行的读取文件流
					bufferedReader = new BufferedReader(inputStreamReader);
					//判读读取的是否为空  
					if (inputStreamReader != null) {
						//如果不为空 则循环一行一行读取
						while ((line = bufferedReader.readLine()) != null) {
							socketWriter.println(line);
						}
					}
				} catch (Exception e) {
					throw new Exception(ExceptionConstant.readfile_error);
				}
					//新作业同步操作的工作
					MultithreadingTool.getInstance().newTaskOperate(printQtaskinfo, printQtaskmain,baseDao);
					
			} else {
					// 主作业 下未获取到子作业 则释放打印机
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatePrintDevicestatusBydevice1", printQtaskmain.getSdevicecode());
			}
		} catch (Exception e) {
			
			logger.error(e.getMessage());
			// 修改主作业的状态为 打印中断    
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain4", printQtaskmain.getQid());
			
			//清空打印机状态表 上 的最后任务  释放打印机(占用->不占用)
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatelastqid",printDevice.getSip());
	
			//对应的文档从服务器上删除掉   
			//先取得存放在服务器磁盘上的路径 在删除主队列表中   子任务 对应的TXT文本
//			List<PrintQtaskinfo> printQtaskList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("pcenter.queryPrintQtaskinfo1",printQtaskmain.getQid());
//				for (int i = 0; i < printQtaskList.size(); i++) {
//					File dir = new File(printQtaskList.get(i).getSfilename());
//					Delete(dir);
//				}
		} finally {
				try {
					//关闭socket连接
					close();
				} catch (IOException e) {
					e.getMessage();
				}
		}
		
	}
	
	/**
	 * 打开socket连接
	 * 
	 * @throws IOException
	 */
	private void open(String ip, String port) throws IOException {
		if (socket == null) {
			//建立Socket连接
			socket = new java.net.Socket();
			try {
				//根据IP PORT 来进行 Socket的连接
				socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), 2000);
			} catch (SocketTimeoutException e) {
				throw new SocketException("与打印机建立连接超时！");
			}
			//连接后 new PrintWriter 中
			socketWriter = new PrintWriter(socket.getOutputStream());
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (!closed &&  null!=socket) {
			//如果socketWriter 不为空 则关闭发送
			if (socketWriter != null) {
				socketWriter.close();
			}
			socket.close();
			//置换状态为 true
			closed = true;
		}
	}
	
	//删除备份在服务器上的作业的文档 txt
	@SuppressWarnings("unused")
	private void Delete(File dir){
		if(dir.isFile()){
			//该路径下是否是一个标准的文件
				dir.delete();
		}else{
			logger.info("文件不存在");
		}
		
	}

	//返回closed
	public boolean isClosed() {
		return closed;
	}
	//set closed
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	

	@Override
	public Object result() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}

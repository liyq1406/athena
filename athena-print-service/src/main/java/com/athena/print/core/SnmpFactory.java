/**
 * 
 */
package com.athena.print.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.athena.print.coon.PrintStatus;
import com.athena.print.entity.sys.PrintDevicestatus;

import com.toft.core3.util.Assert;

/**
 * <p>Title:打印监控平台</p>
 *
 * <p>Description:snmp工厂</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class SnmpFactory {
	
	private static Logger logger=Logger.getLogger(SnmpFactory.class);
	
	private static SnmpFactory instance;
	//snmp 工厂 snmp 简单网络管理协议
	private Map<String, Target> targetAddresses = Collections.synchronizedMap(new HashMap<String, Target>()) ;
	
	private Snmp snmp;//

	private final static String DEVICE_STATUS_OID = "1.3.6.1.2.1.25.3.2.1.5.1";// 打印机设备状态

	private final static String PRINTER_STATUS_OID = "1.3.6.1.2.1.25.3.5.1.1.1";// 打印状态

	private final static String PRINTER_ERROR_STATUS_OID = "1.3.6.1.2.1.25.3.5.1.2.1";// 打印异常状态（alert）
	
	public  SnmpFactory(){
		try {
			TransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			snmp.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
	 /**
	 * 启动snmp监听器
	 * @throws IOException
	 */
//	public void init() throws IOException{
//		TransportMapping transport = new DefaultUdpTransportMapping();
//		snmp = new Snmp(transport);
//		snmp.listen();
//	}
	
	public static synchronized void initInstance(){
		if( null == instance ){              
        	instance = new SnmpFactory();   
        	
		} 
	}
	
	public static SnmpFactory getInstance(){
		
		if( null == instance ){
			initInstance();    //保证了同一时间只能只能有一个对象访问此同步块                
		}
		return instance;
	}
	
	/**
	 * 获取打印机状态
	 * @return
	 */

	public PrintStatus getPrintStatus(PrintDevicestatus printDevicestatus){
		//System.out.println("aaaaaaaaa");

		long begTime = System.currentTimeMillis();
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(DEVICE_STATUS_OID)));
		pdu.add(new VariableBinding(new OID(PRINTER_STATUS_OID)));
		pdu.add(new VariableBinding(new OID(PRINTER_ERROR_STATUS_OID)));
		pdu.setType(PDU.GET);
		
		Target target = getTarget(printDevicestatus);
		
		//打印机状态监听
		PrinterStatusResponseListener statusListener = new PrinterStatusResponseListener(begTime);
		
		Object userHandle = null;
		try {
			snmp.send(pdu, target, userHandle, statusListener);
		} catch (IOException e) {
			//连接中断
			logger.error(e.getMessage());
			return PrintStatus.getBrokenStatus();
		}
		while(!statusListener.isComplete()){
			//等待打印机获取状态完成
			try {
				Thread.sleep(10);//等待10毫秒
			} catch (InterruptedException e) {				
			}
		}
		return statusListener.getStatus();
	}
	
	private Target getTarget(PrintDevicestatus printDevicestatus){
		
		Assert.notNull(printDevicestatus, "打印机信息不能为空！");
		Target target;
		String printerIp = printDevicestatus.getSip();
		
		if(targetAddresses.containsKey(printerIp)){
			target = targetAddresses.get(printerIp);
		}else{
			target = this.createTarget(printDevicestatus);
			targetAddresses.put(printerIp, target);
		}
		return target;
	}
	
	/**
	 * @param smnpAddress
	 * @return
	 */
	private Target createTarget(PrintDevicestatus printDevicestatus){
		String smnpAddress = printDevicestatus.getSnmpAddress();
		Address targetAddress = GenericAddress.parse(smnpAddress);
		CommunityTarget target = null;
		target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(targetAddress);
		target.setRetries(1);
		target.setTimeout(500);
		target.setVersion(SnmpConstants.version1);
		return target;
	}
	
	/**
	 * <p>Title:</p>
	 *
	 * <p>Description:</p>
	 *
	 * <p>Copyright:Copyright (c) 2011.9</p>
	 *
	 * <p>Company:iSoftstone</p>
	 *
	 * @author Administrator
	 * @version 1.0
	 */
	class PrinterStatusResponseListener implements ResponseListener{
		@SuppressWarnings("unused")
		private long begTime;//开始时间
		
		private boolean complete = false;//是否返回响应
		
		private long timeout = 1000;//响应超时时间
		
		private PrintStatus status;//打印状态

		public PrintStatus getStatus() {
			return status;
		}

		public void setStatus(PrintStatus status) {
			this.status = status;
		}

		public boolean isComplete() {
			return complete;
		}

		public long getTimeout() {
			return timeout;
		}

		public void setTimeout(long timeout) {
			this.timeout = timeout;
		}

		public void setComplete(boolean complete) {
			this.complete = complete;
		}

		public PrinterStatusResponseListener(long begTime){
			this.begTime = begTime;
		}
		
		public void onResponse(ResponseEvent event){
			// Always cancel async request when response has been received
			// otherwise a memory leak is created! Not canceling a request
			// immediately can be useful when sending a request to a
			// broadcast
			((Snmp) event.getSource()).cancel(event.getRequest(), this);
			status = parseStatus(event.getResponse());
			complete = true;
		}
		
		@SuppressWarnings("unchecked")
		private PrintStatus parseStatus(PDU response){
			if(response==null){
				return PrintStatus.getBrokenStatus();
			}
			Vector<VariableBinding> vbs = response.getVariableBindings();
			String hrDeviceStatus = "";//
			String hrPrinterStatus = "";//
			String hrPrinterDetectedErrorState = "";//
			
			String oid;
			String value;
			for(VariableBinding vb:vbs){
				oid = vb.getOid().toString();//
				value = vb.getVariable().toString();//
				if(DEVICE_STATUS_OID.equals(oid)){//打印机设备状态
					hrDeviceStatus = value;
				}else if(PRINTER_STATUS_OID.equals(oid)){//打印机状态
					hrPrinterStatus = value;
				}else if(PRINTER_ERROR_STATUS_OID.equals(oid)){//打印机
					hrPrinterDetectedErrorState = value;
				}
				oid = null;
				value = null;
			}
			return new PrintStatus(hrDeviceStatus,hrPrinterStatus,hrPrinterDetectedErrorState);
		}
	}
}

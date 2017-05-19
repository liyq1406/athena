/**
 * 
 */
package com.athena.print.coon;

/**
 * <p>Title:</p>
 *
 * <p>Description:打印机状态</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class PrintStatus {
	private String deviceStatus; 
	private String printerStatus;
	private String printerDetectedErrorState;
	
	private boolean broken;
	
	public PrintStatus(boolean broken){
		this.broken = broken;
	}
	
	public PrintStatus(String deviceStatus, String printerStatus,
			String printerDetectedErrorState) {
		this.deviceStatus = deviceStatus;
		this.printerStatus = printerStatus;
		this.printerDetectedErrorState = printerDetectedErrorState;
	}

	
	
	@Override
	public String toString() {
		if(broken){
			return  "STOP";
		}
		return 	deviceStatus+printerStatus+printerDetectedErrorState;
	}

	public String value(){
		if(broken){
			return  "STOP";
		}
		//TODO 解析打印机状态规则
		return 	"deviceStatus:"+deviceStatus+
				",printerStatus:"+printerStatus+
				",printerDetectedErrorState:"+printerDetectedErrorState;
	}
	/**
	 * 打印机各种状态在数据库中对应的编号
	 */
	public int finalValue(PrintStatus status){
		
			if("STOP".equals(status.value())){
				//网络故障
				return 4;
			}else if(status.getPrinterDetectedErrorState().equals("06")&&
					Integer.parseInt(status.getDeviceStatus())==5&&
					Integer.parseInt(status.getPrinterStatus())==1){
				//卡纸
				return 3;
			}else if(status.getPrinterDetectedErrorState().equals("B")
					&&Integer.parseInt(status.getDeviceStatus())==5
					&&Integer.parseInt(status.getPrinterStatus())==1){
				//缺纸
				return 2;
			}else if(status.getPrinterDetectedErrorState().equals("00:00")&&
					Integer.parseInt(status.getDeviceStatus())==2
					&&Integer.parseInt(status.getPrinterStatus())==4){
				//繁忙
				return 1;
			}else if(status.getPrinterDetectedErrorState().equals("00")&&
					Integer.parseInt(status.getDeviceStatus())==2
					&&Integer.parseInt(status.getPrinterStatus())==4){
				//繁忙
				return 1;
			}else if(status.getPrinterDetectedErrorState().equals("00:00")&&
					Integer.parseInt(status.getDeviceStatus())==2
					&&Integer.parseInt(status.getPrinterStatus())==3){
				//空闲
				return 0;
			}else if(status.getPrinterDetectedErrorState().equals("00")&&
					Integer.parseInt(status.getDeviceStatus())==2
					&&Integer.parseInt(status.getPrinterStatus())==3){
				//空闲
				return 0;
			}else{
				//打印机故障
				return 5;
			}
	}
	
	
	public static PrintStatus getBrokenStatus() {
		return new PrintStatus(true);
	}
	
	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getPrinterStatus() {
		return printerStatus;
	}

	public void setPrinterStatus(String printerStatus) {
		this.printerStatus = printerStatus;
	}

	public String getPrinterDetectedErrorState() {
		return printerDetectedErrorState;
	}

	public void setPrinterDetectedErrorState(String printerDetectedErrorState) {
		this.printerDetectedErrorState = printerDetectedErrorState;
	}
}

/**
 * 
 */
package com.athena.component.monitor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * <p>Title:SDC </p>
 *
 * <p>Description: 系统监控</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
/*
 * 
 * TODO 神龙心跳警告处理类  - 重构
 * <UL>
 * <LI>系统名 :SAGR系统</LI>
 * <LI>模块名 :心跳警告处理</LI>
 * <LI>对应的界面: 画面名</LI>
 * <LI>Author :raoguofa</LI>
 * <LI>Version :1.0.0</LI>
 * </UL>
 * <DL>
 * <DT><b>类概要：</b></DT>
 * <DD>
 * 神龙心跳警告处理类<BR>
 * </DD><BR>
 * </DL>
 */
public class SystemMonitor {
	private static final String EXE_FILE_NAME = "TRKUTIL.EXE";
	
    // 说明系统所处的机器名
	private String WORKSTATION = "SAGR";

	boolean LimitedAlert = true;

	// 说明每个心跳周期各事件的最大报警次数
	private int MAXCOUNTER = 0;

	//零件流系统号
	private String sysid = "ATHENA";
	
	private String FILEPATH = "";

	// 警告周期(分钟)
	private int CYCLE = 60;

	private String I12FRT = "";
	private int I12RT = 0;

	private String I15FRT = "";
	private int I15RT = 0;

	private String I25FRT = "";
	private int I25RT = 0;

	private String I35FRT = "";
	private int I35RT = 0;

	private String I45FRT = "";
	private int I45RT = 0;
	/**
	 * 获取配置文件内容
	 */
	private void getProperties() {
		
		String filePath =getClass().getResource("").getPath().replaceFirst("/", "")+"EventAlert.properties";
		
		filePath  = filePath.replace('/', '\\');
		InputStream Input = null;
		try {
			Input = new BufferedInputStream (new FileInputStream(filePath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		Properties Prop = new Properties();
		try {
			Prop.load(Input);
			this.setLimitedAlert("true"
					.equals(Prop.getProperty("LimitedAlert")) ? true : false);
			this.setMAXCOUNTER(Integer.valueOf(Prop.getProperty("MAXCOUNTER"))
					.intValue());
			this
					.setCYCLE(Integer.valueOf(Prop.getProperty("CYCLE"))
							.intValue());

			this.setI12FRT(Prop.getProperty("I12FRT"));
			this
					.setI12RT(Integer.valueOf(Prop.getProperty("I12RT"))
							.intValue());

			this.setI15FRT(Prop.getProperty("I15FRT"));
			this
					.setI15RT(Integer.valueOf(Prop.getProperty("I15RT"))
							.intValue());

			this.setI25FRT(Prop.getProperty("I25FRT"));
			this
					.setI25RT(Integer.valueOf(Prop.getProperty("I25RT"))
							.intValue());

			this.setI35FRT(Prop.getProperty("I35FRT"));
			this
					.setI35RT(Integer.valueOf(Prop.getProperty("I35RT"))
							.intValue());

			this.setI45FRT(Prop.getProperty("I45FRT"));
			this
					.setI45RT(Integer.valueOf(Prop.getProperty("I45RT"))
							.intValue());
			
			this.setFILEPATH(filePath);

		} catch (Exception e) {
			//LogConst.outLog("", LogConst.LOG_TYPE_ERROR_STR,
			//		"读取配置文件出错:" + "\r\n");
		} finally {
			if (Input != null)
				try {
					Input.close();
				} catch (IOException e) {
				}
		}
	}

	/**
	 * 设置配置文件内容
	 */
	private void setProperties() {

		OutputStream output = null;
		try {
			output = new FileOutputStream(getFILEPATH());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Properties Prop = new Properties();

		try {
			Prop.setProperty("LimitedAlert", String.valueOf(isLimitedAlert()));
			Prop.setProperty("MAXCOUNTER", String.valueOf(getMAXCOUNTER()));
			Prop.setProperty("CYCLE", String.valueOf(getCYCLE()));

			Prop.setProperty("I12FRT", getI12FRT());
			Prop.setProperty("I12RT", String.valueOf(getI12RT()));

			Prop.setProperty("I15FRT", getI15FRT());
			Prop.setProperty("I15RT", String.valueOf(getI15RT()));

			Prop.setProperty("I25FRT", getI25FRT());
			Prop.setProperty("I25RT", String.valueOf(getI25RT()));

			Prop.setProperty("I35FRT", getI35FRT());
			Prop.setProperty("I35RT", String.valueOf(getI35RT()));

			Prop.setProperty("I45FRT", getI45FRT());
			Prop.setProperty("I45RT", String.valueOf(getI45RT()));
			
			Prop.store(output, "");

		} catch (Exception e) {
			e.printStackTrace();
			//LogConst.outLog("", LogConst.LOG_TYPE_ERROR_STR,
			//		"设置配置文件出错:" + "\r\n");
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}

	// 初始化
	public SystemMonitor() {
		try {
			WORKSTATION = "SAGR#";
		} catch (Exception e) {
			e.printStackTrace();
			//LogConst.outLog("", LogConst.LOG_TYPE_ERROR_STR,
			//		"调用报警模块初始化数据错误(读配置文件错误):" + "\r\n");
		} finally {

		}
	}

	// 发送心跳信息
	public int sendHeartbeat(String eventcode) {
		// 执行TRKUTIL.EXE SendEvent
		String cycleid = WORKSTATION + eventcode;
		if ("".equals(eventcode.trim())) {
			eventcode = WORKSTATION+"#004#8000";
			cycleid = WORKSTATION+"#004";
		} else
			eventcode = WORKSTATION + eventcode + "#8000";
		String stime = "\""
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()) + "\"";
		String command = "SendEvent OBJNAME=APPINTF,EVENTCODE=" + eventcode
				+ ",CHARDATE=" + stime + ",CYCLEID=" + cycleid;
		int rtn = -1;

		rtn = waitCall(EXE_FILE_NAME + " " + command);
		return rtn;
	}

	// 发送报警信息
	public int sendAlert(String sysid, String eventcode, String severity,
			String msg) {

		if ("".equals(sysid.trim()))
			sysid = "PROD"; // 缺省生产系统
		String command = "SendEvent OBJNAME=APPINTFALERT,EVENTCODE="
				+ eventcode + ",ISALERT=1,CYCLEID=" + eventcode + ",SEVERITY="
				+ severity + ",AREA=" + sysid + ",MSG=" + "\"" + msg + "\"";
		int rtn = -1;
		rtn = waitCall(EXE_FILE_NAME + " " + command);
		return rtn;
	}

	// 是否需要发送警报
	public void checkSendAlert(String sysid, String eventcode, String severity,
			String msg) {

		eventcode = WORKSTATION + eventcode;
		this.getProperties();
		if (isLimitedAlert()) {
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date FRT = null;
			int COUNT = 0;
			int TimeOut = 0;
			try {
				if ((WORKSTATION+"#001#0002").equals(eventcode)) {
					FRT = df.parse(getI12FRT());
					COUNT = getI12RT();
				} else if ((WORKSTATION+"#001#0005").equals(eventcode)) {
					FRT = df.parse(getI15FRT());
					COUNT = getI15RT();
				} else if ((WORKSTATION+"#002#0005").equals(eventcode)) {
					FRT = df.parse(getI25FRT());
					COUNT = getI25RT();
				} else if ((WORKSTATION+"#003#0005").equals(eventcode)) {
					FRT = df.parse(getI35FRT());
					COUNT = getI35RT();
				} else if ((WORKSTATION+"#004#0005").equals(eventcode)) {
					FRT = df.parse(getI45FRT());
					COUNT = getI45RT();
				}
				TimeOut = (int) ((now.getTime() - FRT.getTime()) / 1000 / 60);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if ((TimeOut <= getCYCLE() && COUNT < getMAXCOUNTER())
					|| TimeOut > getCYCLE()) {
				sendAlert(sysid, eventcode, severity, msg);
				if (TimeOut <= getCYCLE() && COUNT < getMAXCOUNTER()) {
					if ("SAGR#001#0002".equals(eventcode)) {
						if (COUNT == 0) {
							setI12FRT(df.format(now));
						} else
							setI12FRT(df.format(FRT));
						setI12RT(COUNT + 1);
					} else if ("SAGR#001#0005".equals(eventcode)) {
						if (COUNT == 0) {
							setI15FRT(df.format(now));
						} else
							setI15FRT(df.format(FRT));

						setI15RT(COUNT + 1);
					} else if ("SAGR#002#0005".equals(eventcode)) {
						if (COUNT == 0) {
							setI25FRT(df.format(now));
						} else
							setI25FRT(df.format(FRT));
						setI25RT(COUNT + 1);
					} else if ("SAGR#003#0005".equals(eventcode)) {
						if (COUNT == 0) {
							setI35FRT(df.format(now));
						} else
							setI35FRT(df.format(FRT));
						setI35RT(COUNT + 1);
					} else if ("SAGR#004#0005".equals(eventcode)) {
						if (COUNT == 0) {
							setI45FRT(df.format(now));
						} else
							setI45FRT(df.format(FRT));
						setI45RT(COUNT + 1);
					}
					setProperties();
				} else if (TimeOut > getCYCLE()) {
					if ("SAGR#001#0002".equals(eventcode)) {
						setI12FRT(df.format(now));
						setI12RT(1);
					} else if ("SAGR#001#0005".equals(eventcode)) {
						setI15FRT(df.format(now));
						setI15RT(1);
					} else if ("SAGR#002#0005".equals(eventcode)) {
						setI25FRT(df.format(now));
						setI25RT(1);
					} else if ("SAGR#003#0005".equals(eventcode)) {
						setI35FRT(df.format(now));
						setI35RT(1);
					} else if ("SAGR#004#0005".equals(eventcode)) {
						setI45FRT(df.format(now));
						setI45RT(1);
					}
					setProperties();
				}
			}
		}else{
			sendAlert(sysid, eventcode, severity, msg);
		}

	}

	// 调用执行程序名及参数
	private int waitCall(String cmd) {
		int retValue = 0;
		Runtime rn = Runtime.getRuntime();
		try { 
			rn.exec(cmd);
		} catch (Exception e) {
			retValue = -1; 
			return retValue;
		}
		return retValue;
	}

 

	public int getMAXCOUNTER() {
		return MAXCOUNTER;
	}

	public void setMAXCOUNTER(int maxcounter) {
		MAXCOUNTER = maxcounter;
	}

	public String getWORKSTATION() {
		return WORKSTATION;
	}

	public void setWORKSTATION(String workstation) {
		WORKSTATION = workstation;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public int getCYCLE() {
		return CYCLE;
	}

	public void setCYCLE(int cYCLE) {
		CYCLE = cYCLE;
	}

	public String getI12FRT() {
		return I12FRT;
	}

	public void setI12FRT(String i12frt) {
		I12FRT = i12frt;
	}

	public int getI12RT() {
		return I12RT;
	}

	public void setI12RT(int i12rt) {
		I12RT = i12rt;
	}

	public String getI15FRT() {
		return I15FRT;
	}

	public void setI15FRT(String i15frt) {
		I15FRT = i15frt;
	}

	public int getI15RT() {
		return I15RT;
	}

	public void setI15RT(int i15rt) {
		I15RT = i15rt;
	}

	public String getI25FRT() {
		return I25FRT;
	}

	public void setI25FRT(String i25frt) {
		I25FRT = i25frt;
	}

	public int getI25RT() {
		return I25RT;
	}

	public void setI25RT(int i25rt) {
		I25RT = i25rt;
	}

	public String getI35FRT() {
		return I35FRT;
	}

	public void setI35FRT(String i35frt) {
		I35FRT = i35frt;
	}

	public int getI35RT() {
		return I35RT;
	}

	public void setI35RT(int i35rt) {
		I35RT = i35rt;
	}

	public String getI45FRT() {
		return I45FRT;
	}

	public void setI45FRT(String i45frt) {
		I45FRT = i45frt;
	}

	public int getI45RT() {
		return I45RT;
	}

	public void setI45RT(int i45rt) {
		I45RT = i45rt;
	}

	public boolean isLimitedAlert() {
		return LimitedAlert;
	}

	public void setLimitedAlert(boolean limitedAlert) {
		LimitedAlert = limitedAlert;
	}

	public String getFILEPATH() {
		return FILEPATH;
	}

	public void setFILEPATH(String fILEPATH) {
		FILEPATH = fILEPATH;
	}
	
	public static void main(String[] args){
		
	}
}

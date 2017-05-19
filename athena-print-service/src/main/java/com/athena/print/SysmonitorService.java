package com.athena.print;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-27
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */

public class SysmonitorService {
    private final Logger logger = Logger.getLogger(this.getClass());

    // 说明系统所处的机器名
    private String WORKSTATION = "";

    // 报警数量
    private String eventcodexx = "";

    boolean m_LimitedAlert = true;

    // 存放事件和报警数量的对象
    @SuppressWarnings("unchecked")
    Map map = new java.util.Hashtable();

    // 说明每个心跳周期各事件的最大报警次数
    private int MAXCOUNTER = 0;

    // 说明接收心跳信息
    private String HEARTOBJECT = "";

    //
    private String ALERTOBJECT = "";

    private String OBJCODE = "001";// 心跳对象码

    public String getValue(String key) {
        String filename = "EventAlert.properties";
        return PropertiesUtils.getValue(filename, key);
    }

    public SysmonitorService() throws Exception {
        try {
            //OBJCODE = this.getValue("OBJCODE");;
            WORKSTATION = this.getValue("WORKSTATION");
            MAXCOUNTER = NumberUtils.toInt(this.getValue("MAXCOUNTER"), 0);
            HEARTOBJECT = this.getValue("HEARTOBJECT");
            ALERTOBJECT = this.getValue("ALERTOBJECT");
        } catch (Exception e) {
            logger.info("==>>读取心跳配置文件出错：" + e.getMessage());
        }
    }

    public int SendHeartbeat(String eventcode, String works) {
    	WORKSTATION = works;
    	return SendHeartbeat(eventcode);
    }
    // 发送心跳信息
    public int SendHeartbeat(String eventcode) {
        // 执行TRKUTIL.EXE SendEvent
        String cycleid = "";

        if (WORKSTATION != null && WORKSTATION.endsWith("#")) {
            cycleid = WORKSTATION + OBJCODE;
        } else {
            cycleid = WORKSTATION + "#" + OBJCODE;
        }

        eventcode = cycleid + "#" + eventcode;

        String stime = "\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\"";

        //原指令
        String command = "SendEvent OBJNAME=APPINTF,EVENTCODE=" + eventcode + ",CHARDATE=" + stime
                + ",CYCLEID=" + cycleid + ",config=$trk_config_file";

        int rtn = -1;
        logger.info("心跳命令:"+command);
        rtn = WaitCall("TRKUTIL" + " " + command);

        ClearAlertCounter(eventcode);
        return rtn;
    }

    // 发送报警信息
    public int SendAlert(String sysid, String eventcode, String severity, String msg) {
        if ("".equals(sysid.trim()))
            sysid = "PROD"; // 缺省生产系统

        String cycleid = "";

        if (WORKSTATION != null && WORKSTATION.endsWith("#")) {
            cycleid = WORKSTATION + OBJCODE;
        } else {
            cycleid = WORKSTATION + "#" + OBJCODE;
        }

        eventcode = cycleid + "#" + eventcode;

        if (!IsOverAlertCounter(eventcode)) {
            // 执行TRKUTIL.EXE SendEvent
            // OBJNAME=APPINTFALERT,EVENTCODE=GXSA1#003#6000,ISALERT=1,SEVERITY=3,
            // MSG="Interface Data Verify Error"
            String command = "SendEvent OBJNAME=APPINTFALERT,EVENTCODE=" + eventcode + ",ISALERT=1,CYCLEID="
                    + cycleid + ",SEVERITY=" + severity + ",AREA=" + sysid + ",MSG=" + "\"" + msg + "\",config=$trk_config_file";
            int rtn = -1;
            rtn = WaitCall("TRKUTIL" + " " + command);
            // 计数器累加１
            AddAlertCounter(eventcode);

            return rtn;
        }
        return 0;
    }

    // 判断是否超出报警最大次数，传入事件参数
    // 可以报警返回FALSE
    boolean IsOverAlertCounter(String eventcode) {
        if (map != null && map.get(eventcode) != null) {
            int n = 0;
            if (map.get(eventcode) != null && !"null".equals(map.get(eventcode))) {
                n = Integer.parseInt(map.get(eventcode) + "");
            } else {
                n = 0;
            }
            if (n <= MAXCOUNTER) {
                return false;
            } else {
                return true;
            }
        } else {
            AddAlertCounter(eventcode);
            return false;
        }
    }

    // 报警事件计数器归零，一个心跳周期归零
    private void ClearAlertCounter(String eventcode) {
        if (map != null && map.get(eventcode) != null) {
            map.remove(eventcode);
        }
    }

    // 计数器根据事件累加
    @SuppressWarnings("unchecked")
    private void AddAlertCounter(String eventcode) {
        if (map != null && map.get(eventcode) != null) {
            int i = Integer.parseInt(map.get(eventcode) + "") + 1;
            map.remove(eventcode);
            map.put(eventcode, new Integer(i));
        } else {
            map.put(eventcode, new Integer(1));
        }
    }

    /**
     * 调用执行程序名及参数
     *
     * @data 2012-6-26
     * @author LXF
     * @param cmd
     * @return int 正常0，错误-1
     * @version v001
     * @description 调用执行程序名及参数
     */
    private int WaitCall(String cmd) {
        int retValue = 0;
        try {
            System.out.println(cmd);
            Runtime rn = Runtime.getRuntime();
            rn.exec(cmd);
        } catch (Exception e) {
            retValue = -1;
            System.out.println("Error exec AnyQ");
            e.printStackTrace();
            return retValue;
        }
        return retValue;
    }


}
class PropertiesUtils {
    private static String filename = "messages_zh_CN.properties";

    public static String getValue(String key) {
        return PropertiesUtils.getPropertie(key);
    }

    public static String getValue(String f, String key) {
        try {
            return PropertiesUtils.getPropertie(f, key);
        } catch (Exception e) {
            return "";
        }
    }

    public static Map getValueMap(String f) {
        return PropertiesUtils.getProValues(f);
    }

    public static String getPropertie(String key) {
        return PropertiesUtils.getPro(filename, key);
    }

    public static String getPropertie(String f, String key) {
        return PropertiesUtils.getPro(f, key);
    }

    private static String getPro(String sfile, String stype) {
        String url = PropertiesUtils.class.getClassLoader().getResource(sfile).getPath();
        File file = new File(url);
        Properties dbProps = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            dbProps.load(is);
            return dbProps.getProperty(stype);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    private static Map<String, String> getProValues(String sfile) {
        InputStream is = PropertiesUtils.class.getClass().getResourceAsStream(
                "/" + sfile);
        Properties dbProps = new Properties();
        Map<String, String> map = new HashMap<String, String>();
        try {
            dbProps.load(is);
            Iterator it = dbProps.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next() + "";
                String value = dbProps.get(key) + "";
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;

    }
}
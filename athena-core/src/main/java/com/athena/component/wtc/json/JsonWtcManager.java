/**
 * 
 */
package com.athena.component.wtc.json;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPReplyException;
import weblogic.wtc.jatmi.TypedCArray;
import weblogic.wtc.jatmi.TypedString;

import com.athena.authority.entity.PltTrscode;
import com.athena.component.utils.JsonUtils;
import com.athena.component.wtc.WtcConnUtils;
import com.athena.component.wtc.WtcException;
import com.athena.component.wtc.WtcManager;
import com.athena.component.wtc.WtcRequest;
import com.athena.component.wtc.WtcResponse;
import com.toft.core2.ToftException;

/**
 * @author Administrator
 */
//@Component
public class JsonWtcManager implements WtcManager {
	
	private final static Logger log = Logger.getLogger(JsonWtcManager.class);
	private static String sBCBW="";//补充报文,C后台需要
	static{
		String s = "                                                                                                    ";
		for (int i=0;i<200;i++){
			sBCBW += s;
		}
	}
	
	//private final static String TUXEDO_SERVICE_NAME = "PASPLT";//TUXEDO调用的服务名称
	
	
	/* (non-Javadoc)
	 * @see com.athena.component.wtc.WtcService#request(com.athena.component.wtc.WtcRequest)
	 */
	public WtcResponse request(WtcRequest wtcRequest, PltTrscode pltTrscode) throws WtcException{

		long beginManageTime = System.currentTimeMillis();
		
		TuxedoConnection conn = null;
		WtcResponse wtcResponse = null;
		String jsonStr = JSONObject.fromObject(wtcRequest).toString();//;JSONUtils.toJSON(wtcRequest);
		//jsonStr += sBCBW;
		//jsonStr = jsonStr.substring(0, 20000);
		if(jsonStr!=null){			
			Reply reply = null;
			String replyJson = "";
			conn = WtcConnUtils.getConn();
			
			log.info("(start)请求交易码：" + pltTrscode.getTrscode() + "-----请求JSON：" + jsonStr );
			
			//--------
			TypedString josnData = null;
			TypedCArray reqCA = null;
			long beginCallWTCTime = System.currentTimeMillis();
			try {
				
				if("0".equals(pltTrscode.getTrstype())){
					josnData = new TypedString(jsonStr);			
				}else{
			    	byte[] inbt = jsonStr.getBytes("GBK");
			    	reqCA = new TypedCArray();
					reqCA.carray = inbt;
					reqCA.sendSize = inbt.length;
			    }			
				//--------							
				
				//请求WTC服务   -- 编号
				if("0".equals(pltTrscode.getTrstype())){
					log.info("请求交易码：" + pltTrscode.getTrscode() + "-----请求service：" + pltTrscode.getService() + "(TypedString)：" );
					reply = conn.tpcall(pltTrscode.getService(), josnData, 0); //timeout时间
					replyJson = reply.getReplyBuffer().toString();
					log.info("WTC请求【"+pltTrscode.getTrsname()+"】成功(TypedString)");
				}else{
					log.info("请求交易码：" + pltTrscode.getTrscode() + "-----请求service：" + pltTrscode.getService()+ String.valueOf(reqCA.sendSize) + "(TypedCArray)："  );
					reply = conn.tpcall(pltTrscode.getService(), reqCA, 0);
					replyJson = new String(((TypedCArray)reply.getReplyBuffer()).carray,"GBK").trim();
					log.info("WTC请求【"+pltTrscode.getTrsname()+"】成功(TypedCArray)");
				}
				
				//log.debug("WTC请求【"+pltTrscode.getTrsname()+"】成功");		
			} catch (TPReplyException e) {
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bOut));
				log.error(bOut.toString());
				//设置返回值
				JSONObject jsonObj = JSONObject.fromObject(wtcRequest);
				jsonObj.put("response", "C_NoReply");
				replyJson = jsonObj.toString();
				//throw new WtcException("WTC请求【"+trsname+"】没有响应:"+e.getMessage());
			} catch (TPException e) {
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bOut));
				log.error(bOut.toString());
				//设置返回值
				JSONObject jsonObj = JSONObject.fromObject(wtcRequest);
				jsonObj.put("response", "C_TXD_"+ e.gettperrno());
				replyJson = jsonObj.toString();
				//throw new WtcException("WTC连接【"+trsname+"】发生异常:"+e.getMessage());
			} catch (Error e) {
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bOut));
				log.error(bOut.toString());
				//设置返回值
				JSONObject jsonObj = JSONObject.fromObject(wtcRequest);
				jsonObj.put("response", "C_ConnError");
				replyJson = jsonObj.toString();
				//throw new WtcException("WTC连接【"+trsname+"】发生异常:"+e.getMessage());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bOut));
				log.error(bOut.toString());
				//设置返回值
				JSONObject jsonObj = JSONObject.fromObject(wtcRequest);
				jsonObj.put("response", "C_NoReply");
				replyJson = jsonObj.toString();
			}
			
			Long endCallWTCTime = System.currentTimeMillis();
			Long wtcCallTime = endCallWTCTime - beginCallWTCTime;
			log.info("请求交易码：" + pltTrscode.getTrscode() + "-----返回JSON：" + replyJson );
			log.info("请求交易码：" + pltTrscode.getTrscode() + "-----请求service：" + pltTrscode.getService() + "-----WTC调用时长：" + wtcCallTime);
			
			try {
				wtcResponse = new WtcResponse(JsonUtils.jsonStrToMap(replyJson));
				conn.tpterm();
			} catch (ToftException e) {
				wtcResponse = new WtcResponse(new HashMap<String,Object>());
				wtcResponse.put("hasError", true);
				wtcResponse.put("errorMessage", e.getMessage());
			}
			wtcResponse.put("wtcCallTime", wtcCallTime);
		}
		
		Long endManageTime = System.currentTimeMillis();
		Long manageCallTime = endManageTime - beginManageTime;
		log.info("(end)请求交易码：" + pltTrscode.getTrscode() + "-----请求service：" + pltTrscode.getService() + "-----manage调用时长：" + manageCallTime);
		return wtcResponse;
	}
}

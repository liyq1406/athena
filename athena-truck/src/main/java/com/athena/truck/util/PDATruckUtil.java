package com.athena.truck.util;

import java.util.Map;

import com.athena.truck.entity.PDATruck;

import net.sf.json.JSONObject;

/**
 * PDA卡车相关
 * @author CSY
 *
 */
public class PDATruckUtil {
	
	public static final String SUCCESS_TRANSFER = "0000";		//返回消息正确，响应码0000
	public static final String ERROR_TRANSFER = "9999";			//返回消息错误，响应码9999
	
	/**
	 * 组装返回结果
	 * @param pdaTruck
	 * @return
	 */
	public static JSONObject getPDAResult(PDATruck pdaTruck){
		JSONObject PDA = new JSONObject();
		PDA.put("trscode", pdaTruck.getTrscode());
		PDA.put("response", pdaTruck.getResponse());
		PDA.put("parameter", pdaTruck.getParameter());
		PDA.put("trans_bran_code", pdaTruck.getTrans_bran_code());
		PDA.put("oper_no", pdaTruck.getOper_no());
		PDA.put("seqno", pdaTruck.getSeqno());
		PDA.put("host_seqno", pdaTruck.getHost_seqno());
		PDA.put("trsdate", pdaTruck.getTrsdate());
		PDA.put("buflen", pdaTruck.getBuflen());
		PDA.put("respdesc", pdaTruck.getRespdesc());
		PDA.put("op_name", pdaTruck.getOp_name());
		return PDA;
	}
	
	/**
	 * 拼装PDATruck
	 * @param param
	 * @return
	 */
	public static JSONObject gPdaTruck(Map<String, Object> param){
		PDATruck pda = new PDATruck();
		
		if (param.containsKey("buflen")) {
			pda.setBuflen(param.get("param").toString());
		}else {
			pda.setBuflen("");
		}
		if (param.containsKey("host_seqno")) {
			pda.setHost_seqno(param.get("host_seqno").toString());
		}else {
			pda.setHost_seqno("");
		}
		if (param.containsKey("oper_no")) {
			pda.setOper_no(param.get("oper_no").toString());
		}else {
			pda.setOper_no("");
		}
		if (param.containsKey("parameter")) {
			pda.setParameter((JSONObject) param.get("parameter"));
		}else {
			pda.setParameter("");
		}
		if (param.containsKey("response")) {
			pda.setResponse(param.get("response").toString());
		}else {
			pda.setResponse("");
		}
		if (param.containsKey("seqno")) {
			pda.setSeqno(param.get("seqno").toString());
		}else {
			pda.setSeqno("");
		}
		if (param.containsKey("total_page")) {
			pda.setTotal_page(param.get("total_page").toString());
		}else {
			pda.setTotal_page("");
		}
		if (param.containsKey("trans_bran_code")) {
			pda.setTrans_bran_code(param.get("trans_bran_code").toString());
		}else {
			pda.setTrans_bran_code("");
		}
		if (param.containsKey("trscode")) {
			pda.setTrscode(param.get("trscode").toString());
		}else {
			pda.setTrscode("");
		}
		if (param.containsKey("trsdate")) {
			pda.setTrsdate(param.get("trsdate").toString());
		}else {
			pda.setTrsdate("");
		}
		if (param.containsKey("respdesc")) {
			pda.setRespdesc(param.get("respdesc").toString());
		}else {
			pda.setRespdesc("");
		}
		if (param.containsKey("op_name")) {
			pda.setOp_name(param.get("op_name").toString());
		}else {
			pda.setOp_name("");
		}
		
		return getPDAResult(pda);
	}
	
}

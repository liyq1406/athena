package com.athena.print.controller;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.athena.print.entity.pcenter.PrintQtaskmain;

@WebService
public interface GetPrintService {

	//生成打印文档  并 启动打印服务 方法  及 需要的参数
	 public void getQtaskController(
			 @WebParam(name="qid") String qid);	//打印主表作业编号
	 
	 /**
		 * 添加打印任务接口
		 * @param printUser    打印用户编号
		 * @param printSheet   打印单据组编号
		 * @param printCangk   打印仓库编号
		 * @param deviceGroup  打印机组编号
		 * @param printContent 打印参数 
		 * @param printModel   打印模板编号
		 * @param printNum     打印份数
		 * @param printCount   打印联数
		 * @param printType    打印样式
		 * @param usercenter   用户中心
		 * @param seq          子任务编号
		 * @return true or false
		 * @throws Exception
		 */
		public PrintQtaskmain addTaskSequence(@WebParam(name="printUser") String printUser,	//用户编号
		 @WebParam(name="printSheet")String printSheet,		//打印单据组编号
		 @WebParam(name="printCangk")String printCangk,		//打印仓库编号
		 @WebParam(name="deviceGroup")String deviceGroup,	//打印机组编号
		 @WebParam(name="printContent")String printContent,	//打印参数 JSON
		 @WebParam(name="printModel")String printModel,  	//打印模板编号
		 @WebParam(name="printNum")int printNum,			//打印份数
		 @WebParam(name="printCount")int printCount,		//打印联数
		 @WebParam(name="printType")String printType,       //打印样式
		 @WebParam(name="usercenter")String usercenter,	//用户中心
		 @WebParam(name="count")int count,	//增加的子作业的条数
		 @WebParam(name="quyzs")String quyzs,	//区域标识 1单区域    2多区域
		 @WebParam(name="areasheet")int areasheet,	//表单区域号
		 @WebParam(name="arealist")int arealist,	//清单区域号
		 @WebParam(name="seq")String seq);	//子任务编号
}
	
package com.athena.print.core;



import java.sql.SQLException;

import com.athena.print.entity.sys.Printright;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.print.module.sys.service.PrintrightService;
import com.athena.print.module.sys.service.PrintuserinfoService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * 添加打印队列工具类
 * @author zhangl
 * @version v1.0
 * @date 20120113
 */
@Component
public class AddTaskTool {
	
	@Inject
	private PrintuserinfoService printuserinfoService;
	
	@Inject
	private PrintrightService printrightService;
	
	/**
	 * 通过用户得到用户组
	 * @param usercode 用户编号
	 * @return userscode 用户组编号
	 * @author zhangl
	 * @version v1.0
	 * @date 20120113
	 */
	public String getUserscode(String usercode)throws SQLException{
		//根据用户编号 来得到用户明细对象 
		Printuserinfo pinfo = printuserinfoService.selectUsersByuser(usercode);
		//判断是否为空
			if(null==pinfo.getUserscode()|| "".equals(pinfo.getUserscode()))
			{
				throw new SQLException("未找到用户组");
			}
		return pinfo.getUserscode();
	}
	
	/**
	 * 通过单据得到单据组
	 * @param scode 单据编号
	 * @return scodes 单据组编号
	 * @author wangy
	 * @version v1.0
	 * @date 20120118
	 */
//	public String getScode(String scode)throws Exception{
//		//根据打单据编号 得到单据类型实体
//		PrintDictinfo pdinfo = printDictinfoService.selectScodeBycode(scode);
//		//判断是否为空
//		if(null==pdinfo.getScodes()|| "".equals(pdinfo.getScodes()))
//		{
//			throw new Exception("未找到单据组");
//		}
//		return pdinfo.getScodes();
//	}
	
	
	/**
	 * 通过 用户组、单据组、仓库编号得到打印机组
	 * @param userscode 用户编号
	 * @param scodes 单据编号
	 * @param storagescode 仓库编号
	 * @return spcodes 打印机组编号
	 * @author wangy
	 * @version v1.0
	 * @date 20120118
	 */
	public String getSpcodes(String userscode,String scodes,String storagescode)throws SQLException{
		Printright bean = new Printright();
		//设置用户组
		bean.setUserscode(userscode);
		//设置单据组
		bean.setScodes(scodes);
		//设置仓库编号
		bean.setStoragescode(storagescode);
		//根据用户组 、单据组、仓库编号 来得到对应的 打印机组编号
		Printright pgt = printrightService.selectSpcodesBygroup(bean);
		
		if(null==pgt.getSpcodes()|| "".equals(pgt.getSpcodes()))
		{
			throw new SQLException("未找到打印机组编号");
		}
		return pgt.getSpcodes();
	}

}

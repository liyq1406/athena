/**
 * 
 */
package com.athena.print.port;

import java.sql.SQLException;
import java.util.UUID;

import com.athena.db.ConstantDbCode;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDictinfo;
import com.athena.print.entity.sys.Printright;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author Administrator
 *
 */

@Component
public class IAddTaskSequence {
	
	@Inject 
	private AbstractIBatisDao baseDao;
	
	
	/**
	 * 公用方法
	 */
	/**
	 * 通过 用户组、单据组、仓库编号得到打印机组 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("finally")
	public String getPrintGroupCode(String user, String filecode,String storagescode) throws SQLException {
		
		String groupid = "";
		try{
			//打印用户    得到   用户组编号
			String userscode= getUserscode(user);
			//单据   得到    单据组
			String socdes = getScode(filecode);
			//用户组、单据组、仓库编号  得到打印机组
			groupid = getSpcodes(userscode, socdes, storagescode);
		}catch(Exception e){
			throw new SQLException(e.getMessage());
		}finally{
			return groupid;
		}
	}
	
	/**
	 * 添加打印任务接口
	 * @param printUser    打印用户编号
	 * @param printSheet   打印单据编号
	 * @param printCangk   打印仓库编号
	 * @param deviceGroup  打印机组编号
	 * @param printContent 打印参数 JSON
	 * @param printModel   打印模板编号
	 * @param printNum     打印份数
	 * @param printCount   打印联数
	 * @param printType    打印样式
	 * @return true or false
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public boolean addTaskSequence(
			String printUser, 		//打印用户编号
			String printSheet,		//打印单据编号
			String printCangk,		//打印仓库编号
			String deviceGroup,		//打印机组编号
			String printContent,	//打印参数 JSON
			String printModel,  	//打印模板编号
			int printNum,			//打印份数
			int printCount,			//打印联数
			String printType,		//打印样式
			String usercenter		//用户中心
		) throws Exception{
		
		boolean flag = true;
		
		try{
			//主队列表
			PrintQtaskmain bean1 = new PrintQtaskmain();
			//用户中心
			bean1.setUsercenter(usercenter);
			//主任务主键
			bean1.setQid(UUID.randomUUID().toString().substring(0, 32));
			//打印用户编号
			bean1.setSaccount(printUser);
			//打印机组编号
			bean1.setPgid(deviceGroup);
			//单据编号
			bean1.setScode(printSheet);
			//目标仓库
			bean1.setStoragecode(printCangk);
			//打印机编号
			bean1.setSdevicecode("NONE");
			//作业状态
			bean1.setStatus(-1);		
			//任务创建时间
			bean1.setCreatetime(DateUtil.curDateTime());
			
			//主任务插入数据
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.insertPrintQtaskmain", bean1);
		
			//子队列表
			PrintQtaskinfo bean2 = new PrintQtaskinfo();
			for (int i = 0; i <2; i++) {
				//用户中心
				bean2.setUsercenter(bean1.getUsercenter());
				//子任务编号 自动增长 
				bean2.setSeq(i+1+"");
				bean2.setQid(bean1.getQid());
				//文件路径		
				bean2.setSfilename("NONE");
				//打印内容参数
				bean2.setSpars(printContent);
				//模板编号
				bean2.setModelnumber(printModel);
				//打印份数
				bean2.setPrintnumber(printNum);
				//打印联数
				bean2.setPrintunitcount(printCount);
				//打印样式
				bean2.setPrinttype(printType);	
				//子任务作业状态
				bean2.setStatus(0);
				
				
				//子任务插入数据 子列表插入的  次数  
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.insertPrintQtaskinfo", bean2);
			}
		}catch(Exception e){
			flag = false;
			throw new Exception(e.getMessage());
		}finally{
			return flag;
		}
	}
	
	
	
	
	/**
	 * 私有辅助方法
	 */
	
	/**
	 * 通过用户得到用户组
	 * @param usercode 用户编号
	 * @return userscode 用户组编号
	 * @author zhangl
	 * @version v1.0
	 * @date 20120113
	 */
	private String getUserscode(String usercode)throws SQLException{
		
		Printuserinfo pinfo = (Printuserinfo)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintuserinfo",usercode);
		
		if( null == pinfo || ("").equals(pinfo.getUserscode()) ){
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
	private String getScode(String scode)throws SQLException{
		PrintDictinfo pdinfo =(PrintDictinfo) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintDictinfo",scode);
		
		if(null==pdinfo||("").equals(pdinfo.getScodes())){
			throw new SQLException("未找到单据组");
		}
		return pdinfo.getScodes();
	}
	
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
	private String getSpcodes(String userscode,String scodes,String storagescode)throws SQLException{
		
		Printright bean = new Printright();
		bean.setUserscode(userscode);
		bean.setScodes(scodes);
		bean.setStoragescode(storagescode);
		Printright pgt = (Printright)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintright",bean);
		
		if(null ==pgt||("").equals(pgt.getSpcodes())){
			throw new SQLException("未找到打印机组");
		}
		return pgt.getSpcodes();
	}


}

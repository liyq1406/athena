/**
 * 代码声明
 */
package com.athena.print.module.pcenter.service;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.util.exception.ServiceException;

@Component
public class PrintQtaskmainService extends BaseService<PrintQtaskmain>{
	
	private static Logger logger=Logger.getLogger(PrintQtaskmainService.class);
	
	@Inject
	private PrintQtaskinfoService printQtaskinfoService;
	
	@Inject
	private PrintQtaskmainService printQtaskmainService;
	
	 @Override
	 //返回sqlMap printQtaskmain  的命名空间
		protected String getNamespace() {
			return "pcenter";
		}
//	//查看打印机按钮的操作
//	 @Transactional
//	 public List<PrintDevice> doDevice(List<PrintQtaskmain> updateQtask,String usercenter) throws ServiceException{
//		 List<PrintDevice> pdevice = new ArrayList<PrintDevice>();
//		 for (int i = 0; i < updateQtask.size(); i++) {
//			//根据打印机编号和用户中心，依次查询出对应的 打印机设备 对象
//			 String spcode = updateQtask.get(i).getSdevicecode();
//			 PrintDevice pd = new PrintDevice();
//			 pd.setSpcode(spcode);
//			 pd.setUsercenter(usercenter);
//			 PrintDevice printDevice = (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintDevice",pd);
//			 pdevice.add(printDevice);
//		}
//		 return pdevice;
//	 }
	 
	 
	 
	 
	 //取消打印按钮的操作
	 @Transactional
	 public String doDelete(List<PrintQtaskmain> updateQtask, String usercenter)
	    throws ServiceException
	  {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < updateQtask.size(); ++i)
	    {
	     //根据主作业的ID 号来查询此时该作业的状态是否已改变
	      PrintQtaskmain newPrintQtaskmain = this.printQtaskmainService.selectQtask(((PrintQtaskmain)updateQtask.get(i)).getQid(),usercenter);
	      if (("3".equals(newPrintQtaskmain.getStatus())) || ("4".equals(newPrintQtaskmain.getStatus()))) {
	        sb.append("作业 " + ((PrintQtaskmain)updateQtask.get(i)).getQid() + " 的状态已发生变化\n");
	        continue;
	      }
	      	Map map2 = new HashMap();
	       	map2.put("qid", ((PrintQtaskmain)updateQtask.get(i)).getQid());
	       	map2.put("usercenter", usercenter);
	     //根据主作业的ID号  来查询出是否有未发送的作业
	       List taskList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfo1",map2 );
	     //分配了设备的 需要释放打印设备
	      if (!("NONE".equals(((PrintQtaskmain)updateQtask.get(i)).getSdevicecode())) && 0 != taskList.size()) {
	          Map map = new HashMap();
	          map.put("spcode", ((PrintQtaskmain)updateQtask.get(i)).getSdevicecode());
	          map.put("usercenter", usercenter);
	          PrintDevice printDevice = (PrintDevice)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.getPrintDevice", map);
	          if (("1".equals(newPrintQtaskmain.getStatus())) || ("0".equals(newPrintQtaskmain.getStatus())) ||("2".equals(newPrintQtaskmain.getStatus())))
	          	{
	        	  //清空打印机状态表 上 的最后任务  释放打印机(占用->不占用)  根据打印机编号  打印机组编号 和用户中心
	        	 Map map1 = new HashMap();
		         map1.put("sip", printDevice.getSip());
		         map1.put("usercenter", usercenter);
		         map1.put("spcodes", newPrintQtaskmain.getPgid());
		         map1.put("spcode", newPrintQtaskmain.getSdevicecode());
	            this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatelastqid", map1);
	          }
	        }
	       if (0 != taskList.size())
	        {
	    		//更新未发送的子作业为 打印取消状态
	          //this.printQtaskinfoService.doDelete(taskList);
	    	   //一次删除所有qid对应打印子作业 mantis:0012836 20160822 by CSY
	    	   this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updateStatusOfPQInfoByQid", map2);
	        }
	       //更新主表的主作业状态为 打印取消
	          this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.deletePrintQtaskmain", map2);
	       }
	    if (!("".equals(sb.toString()))) {
	      return sb.toString();
	    }
	    return "success";
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
	 
		/**
		 * 作业调度
		 */
	 	@Transactional
	 	public String resetQtask(List<PrintQtaskmain> updateQtask,String spcodes,String usercenter) throws ServiceException {
	 		PrintQtaskmain printQtaskmains = new PrintQtaskmain();
	 		StringBuffer sb = new StringBuffer();
	 		for (int i = 0; i < updateQtask.size(); i++) {
				//根据QID 重新在查询一次  此时作业对应的状态    是否有变化
				PrintQtaskmain pq = (PrintQtaskmain) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.queryPrintQtaskmain1",updateQtask.get(i).getQid());
				if("2".equals(pq.getStatus())||"3".equals(pq.getStatus())||"4".equals(pq.getStatus())){
					sb.append("作业 "+updateQtask.get(i).getQid()+" 的状态已发生变化\n");
					continue;
				}
				//作业状态为 待分配打印 即状态为 -1 时
				if("-1".equals(updateQtask.get(i).getStatus()))
				{
					//此状态 只需要更新 打印机组 编号即可  其余的不变
					printQtaskmains.setQid(updateQtask.get(i).getQid());
					printQtaskmains.setPgid(spcodes);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmains", printQtaskmains);
				}
				//作业状态为 待打印 的状态  即状态为0 且设备为 NONE 
				if("0".equals(updateQtask.get(i).getStatus())&&"NONE".equals(pq.getSdevicecode()))
				{
//					//状态为 待打印 的作业，要判断是否已经分了设备,分了设备的，跳过
//					if(!"NONE".equals(pq.getSdevicecode())){
//						throw new ServiceException("作业 "+updateQtask.get(i).getQid()+" 已分配了打印设备");
//					}
//					else{
						//还未分配到打印机 更新打印机组编号 和  清空打印机   更新为 NONE
						printQtaskmains.setQid(updateQtask.get(i).getQid());
						printQtaskmains.setPgid(spcodes);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskms", printQtaskmains);
//					}
				}
				
				//作业状态为   打印中断  的状态  即状态为1  中断的子作业的状态也要改变为未发送
				if("1".equals(updateQtask.get(i).getStatus()))
				{
					//状态为   打印中断   的作业  已分配了 打印机  此时 先需要 释放之前 选定的打印机，后在更新打印机组编号  和  清空打印机 更新为 NONE
					 Map map = new HashMap();
			         map.put("spcode", pq.getSdevicecode());
			         map.put("usercenter", usercenter);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updateqtask", map);
					printQtaskmains.setQid(updateQtask.get(i).getQid());
					printQtaskmains.setPgid(spcodes);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmains", printQtaskmains);
					PrintQtaskinfo printQtaskinfos = new PrintQtaskinfo();
					printQtaskinfos.setQid(updateQtask.get(i).getQid());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfos", printQtaskinfos);
				}
				
			} 
	 		
	 		if(!"".equals(sb.toString())){
	 			return sb.toString();
	 			
	 		}else{
	 			return "success";
	 		}
		}
		
		
		
	 
	 	/**
		 * 插入
		 */
	 	@Transactional
	 	public String doInsert(PrintQtaskmain bean) throws ServiceException {
			//插入一条主表的数据
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.insertPrintQtaskmain", bean);
			return "success";
		}
		
		/**
		 * 根据主作业QID 来更新 打印机编号
		 */
	 	@Transactional
		public String doUpdate(PrintQtaskmain bean) throws ServiceException {
			//根据主作业QID 更新 打印机编号
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain5", bean);
			return "success";
		}
		
		/**
		 * 根据打印机编号来更新 打印机状态
		 */
	 	@Transactional
		public String updateByspcode(String spcode) throws ServiceException {
			//打印机编号来更新 打印机状态
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice", spcode);
			return "";
		}
		
		/**
		 * 根据作业的序列号     更新子任务作业状态为 已执行
		 */
	 	@Transactional
		public String updateQtaskinfo(PrintQtaskinfo printQtaskinfo) throws ServiceException {
			//根据作业的序列号  更新子任务作业状态
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo1", printQtaskinfo);
			return "";
		}
		
		/**
		 * 根据打印机编号      更新 辅状态为  繁忙
		 */
	 	@Transactional
		public String updateStatus(String spcode) throws ServiceException {
			//根据打印机编号      更新 辅状态
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice2", spcode);
			return "";
		}
		
		
		/**
		 * 打印机状态表更新主作业任务号、用户编号
		 */
	 	@Transactional
		public String updateMain(PrintDevicestatus bean) throws ServiceException {
			//打印机状态表更新主作业号、用户编号
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice3", bean);
			return "";
		}
		
		/**
		 * 根据主作业QID   更新主作业状态为 打印中
		 */
	 	@Transactional
		public String updateQtaskmain(String qid) throws ServiceException {
			//根据主作业QID   更新主作业状态
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain3", qid);
			return "";
		}
		
		/**
		 * 主作业 下未获取到子作业 则释放打印机
		 */
	 	@Transactional
		public String updateQtaskmain1(String spcode) throws ServiceException {
			//主作业 下未获取到子作业 则释放打印机
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDevicestatusBydevice1", spcode);
			return "";
		}
		
		/**
		 * 修改主作业的状态为 打印中断 
		 */
	 	@Transactional
		public String updateQtaskmain2(String qid) throws ServiceException {
			//修改主作业的状态为 打印中断 
	 		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskmain4", qid);
			return "";
		}
		
		
		/**
		 * 根据打印机编号 查询 打印机实体
		 */
		public PrintDevice selectDevice(String spcode){
			//根据打印机编号 查询 打印机对象
			return (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.getPrintDevice", spcode);
		}
		
		/**
		 * 根据主作业QID 查询    作业实体
		 */
		public PrintQtaskmain selectQtask(String qid,String usercenter){
			//根据主作业QID 查询    作业对象
			Map map = new HashMap();
			map.put("qid", qid);
			map.put("usercenter", usercenter);
			return (PrintQtaskmain)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.queryPrintQtaskmainByinfo", map);
		}
		
		/**
		 * 根据主作业QID 查询    下面的子作业
		 */
		public List<PrintQtaskinfo> selectQtaskinfo(String qid){
			//根据主作业QID 查询    下面的子作业集合
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfo1", qid);
		}
		
		/**
		 * 根据作业编号SEQ 查询    子作业
		 */
		public List<PrintQtaskinfo> selectQtaskinfobyseq(Map seq){
			//根据主作业QID 查询    下面的子作业集合
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfobyseq", seq);
		}
		
		/**
		 * 定位主查询
		 * @return 
		 */
		public Map<String, Object> selectByseq(PrintQtaskmain bean) {
			return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("pcenter.queryPrintQtaskmainSelect",bean,bean);
		}
}
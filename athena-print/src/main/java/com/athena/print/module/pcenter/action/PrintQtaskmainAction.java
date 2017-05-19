/**
 * 
 */
package com.athena.print.module.pcenter.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.runner.Runner;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.print.core.PrinterConnectorq;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtasklist;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.pcenter.PrintQtasksheet;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.print.module.pcenter.service.PrintQtaskinfoService;
import com.athena.print.module.pcenter.service.PrintQtaskmainService;
import com.athena.print.module.sys.service.PrintDevicestatusService;
import com.athena.print.core.AnalysisPrintTemplate;
import com.athena.util.CommonUtil;
import com.athena.util.cache.CacheManager;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintQtaskmainAction extends ActionSupport {
	@Inject
	private PrintQtaskmainService printQtaskmainService;
	
	@Inject
	private PrintQtaskinfoService printQtaskinfoService;
	
	@Inject
	private PrintDevicestatusService printDevicestatusService;
	
	@Inject 
	private AbstractIBatisDao baseDao;
	
	@Inject
	private CacheManager cacheManager;

	
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	/**
	 * 获取用户信息
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return String
	 */
	public String execute(@Param PrintQtaskmain bean) {
		//初始化显示的页面   获取到用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		//页面上的日期默认给当天日期
		setResult("defaultdate",DateUtil.getCurrentDate());
		return  "select";
	}


	/**
	 * 分页查询
	 * @param bean
	 * @return String
	 */
	public String query(@Param PrintQtaskmain bean,@Param PrintQtaskinfo printqtaskinfo) {
		//主作业的分页查询
		if(null!=bean.getCreatetime()){
			if(null!=bean.getCreatetimes()){
				//判断输入的时刻中 时 分 秒的最大值能为 23:59:59
				String shi = bean.getCreatetimes().substring(0,2);
				String fen = bean.getCreatetimes().substring(2,4);
				String miao = bean.getCreatetimes().substring(4,6);
				String createtime = bean.getCreatetime()+" "+shi+":"+fen+":"+miao;
				//String createtimes = bean.getCreatetime().substring(0,10)+" 00:00:00";
				//bean.setCreatetimes(createtimes);
				bean.setCreatetime(createtime);
			}else{
				String createtime = bean.getCreatetime()+" 00:00:00";
				//String createtimes = bean.getCreatetime().substring(0,10)+" 00:00:00";
				//bean.setCreatetimes(createtimes);
				bean.setCreatetime(createtime);
			}
		}
		
		if(null!=bean.getFinishedtime()){
			if(null!=bean.getFinishedtimes()){
				//判断输入的时刻中 时 分 秒的最大值能为 23:59:59
				String shis = bean.getFinishedtimes().substring(0,2);
				String fens = bean.getFinishedtimes().substring(2,4);
				String miaos = bean.getFinishedtimes().substring(4,6);
				String finishedtime = bean.getFinishedtime()+" "+shis+":"+fens+":"+miaos;
				//String finishedtimes = bean.getFinishedtime().substring(0,10)+" 23:59:59";
				//bean.setFinishedtimes(finishedtimes);
				bean.setFinishedtime(finishedtime);
			}else{
				String finishedtime = bean.getFinishedtime()+" 23:59:59";
				//String createtimes = bean.getCreatetime().substring(0,10)+" 00:00:00";
				//bean.setCreatetimes(createtimes);
				bean.setFinishedtime(finishedtime);
			}
		}
		if(null != printqtaskinfo.getSeq()&&!"".equals(printqtaskinfo.getSeq())){
			Map map = new HashMap();
			StringBuffer str = new StringBuffer();
			map.put("seq", printqtaskinfo.getSeq());
			map.put("usercenter", bean.getUsercenter());
			List<PrintQtaskinfo> printinfo = printQtaskmainService.selectQtaskinfobyseq(map);
			if(printinfo.size()==0){
				return AJAX;
			}else{
				if(printinfo.size()==1){
					str.append("('"+printinfo.get(0).getQid()+"')") ;
				}else{
					for (int i = 0; i < printinfo.size(); i++) {
						if(i==0){
							str.append("('"+printinfo.get(i).getQid()+"',") ;
						}else{
							if (printinfo.size()-i==1){
								str.append("'"+printinfo.get(i).getQid()+"')") ;
							}else{
								str.append("'"+printinfo.get(i).getQid()+"',") ;
							}
							
						}
					 }
				}
			  }
				bean.setQid(str.toString());
				bean.setStatus("");
				setResult("result", printQtaskmainService.selectByseq(bean));
				return AJAX;
		}else{
			if(null != bean.getQid()&&!" ".equals(bean.getQid())){
				bean.setStatus("");
				setResult("result", printQtaskmainService.select(bean));
				return AJAX;
			}else{
				setResult("result", printQtaskmainService.select(bean));
				return AJAX;
			}
		}
	}
	
	/**
	 * 查看打印机按钮
	 * @return String
	 */
//	public String seedevice(@Param("ckx_Qtask") List<PrintQtaskmain> updateQtask) {
//		//查看打印机按钮
//		List<PrintDevice> listdevice = printQtaskmainService.doDevice(updateQtask,getLoginUser().getUsercenter());
//		setResult("result",listdevice);
//		return AJAX;
//	}
	
	
	/**
	 * 取消打印按钮
	 * @return String
	 */
	public String remove(@Param("ckx_Qtask") ArrayList<PrintQtaskmain> updateQtask) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			//执行取消打印按钮
			Message m=new Message(printQtaskmainService.doDelete(updateQtask,getLoginUser().getUsercenter()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
		} catch (Exception e) {
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 作业调度按钮  选取打印机组    同时更新作业的状态
	 * @return
	 */
	public String printQtask(@Param("ckx_Qtask") ArrayList<PrintQtaskmain> updateQtask){
			Map<String,String> message = new HashMap<String,String>();
			String spcodes = this.getParam("spcodes");
			try {
				Message m=new Message(printQtaskmainService.resetQtask(updateQtask,spcodes,getLoginUser().getUsercenter()));
				message.put("message", m.getMessage());
			} catch (Exception e) {
				message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			} 
			//放在result 在前台页面给出提示
			setResult("result",message);	
		return AJAX;
	}
	
	

	/**
	 * 重打按钮  选取打印机后直接  开始打印
	 * @return
	 */
	public String print(){
			String spcode = this.getParam("spcode");
			String qid = this.getParam("qid");
			Runner runner = new Runner(20); 
			PrintQtaskmain newprintQtaskmain = new PrintQtaskmain();
			PrintQtaskinfo printQtaskinfo = new PrintQtaskinfo();
			PrintQtasksheet printQtasksheet = new PrintQtasksheet();
			//是否选取打印机
			if(null!=spcode){
				//根据打印机编号  获得打印机实体
				Map<String,String> map = new HashMap<String,String>();
				map.put("spcode",spcode);
				map.put("usercenter",getLoginUser().getUsercenter());
				PrintDevice pd = (PrintDevice)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.getPrintDevice", map);
				
				//根据打印机编号  获得打印机此时对应的状态
				PrintDevicestatus pds = (PrintDevicestatus)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryDevicestatus", spcode);
				
				//在重新叫校验一次是否设备的状态已经改变
				if(0==pds.getStatus()&&0==pds.getEnable()){
					
					//插入数据
					InsertData(qid,newprintQtaskmain,printQtasksheet,spcode,printQtaskinfo,"2");
					
					//更新主作业的编号到打印状态表中 
					pds.setLastqid(newprintQtaskmain.getQid());
					printDevicestatusService.doUpdate(pds);
					
					//在查出新的子表
					List<PrintQtaskinfo> pList1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfobyqid", newprintQtaskmain.getQid());
					
					//生成打印文档  及  文档的路径
					runner.addCommand(new AnalysisPrintTemplate(pList1,baseDao,cacheManager));				
					
					//生成文档后进入打印
					new PrinterConnectorq(newprintQtaskmain,pd,baseDao).execute();
				}else{
					return "devicechange";
				}
			}else{
				//插入数据
				InsertData(qid,newprintQtaskmain,printQtasksheet,"NONE",printQtaskinfo,"-1");
				//在查出新的子表
				//List<PrintQtaskinfo> pList1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfobyqid", newprintQtaskmain.getQid());
				
				//生成打印文档  及  文档的路径      最后执行打印服务(如果不直接分配打印设备,按轮询的服务)
				//runner.addCommand(new AnalysisPrintTemplate(pList1,baseDao,cacheManager));
			}
			//关闭多线程
			runner.shutdown();
		return AJAX;
	}
	/**
	 * 重打数据的重新插入
	 * @return
	 */
	public void InsertData(String qid,PrintQtaskmain newprintQtaskmain,PrintQtasksheet printQtasksheet,
						   String spcode,PrintQtaskinfo printQtaskinfo,String status){
		//根据主表的QID 获得此时对应的 打印主作业 实体
		PrintQtaskmain printQtaskmain =(PrintQtaskmain)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.queryPrintQtaskmainByinfo", qid);
		//PrintQtaskmain newprintQtaskmain = new PrintQtaskmain();
		
		//在把该数据插入一次到数据库 先插入主表
		//用户中心
		newprintQtaskmain.setUsercenter(getLoginUser().getUsercenter());
		//主任务主键
		newprintQtaskmain.setQid(UUID.randomUUID().toString().substring(0, 23));
		//打印用户编号
		newprintQtaskmain.setSaccount(printQtaskmain.getSaccount());
		//打印机组编号
		newprintQtaskmain.setPgid(printQtaskmain.getPgid());
		//单据编号
		newprintQtaskmain.setScode(printQtaskmain.getScode());
		//目标仓库
		newprintQtaskmain.setStoragecode(printQtaskmain.getStoragecode());
		//打印机编号 直接使用前台选中打印设备即可
		newprintQtaskmain.setSdevicecode(spcode);
		//作业状态
		newprintQtaskmain.setStatus(status);		
		//任务创建时间
		newprintQtaskmain.setCreatetime(DateUtil.curDateTime());
		
		//主表的插入
		printQtaskmainService.doInsert(newprintQtaskmain);
		
		//在插入子表
		List<PrintQtaskinfo> pList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryPrintQtaskinfobyqid", qid);
		for (int i = 0; i <pList.size(); i++) {
			//用户中心
			printQtaskinfo.setUsercenter(getLoginUser().getUsercenter());
			//子任务编号 自动增长 
			printQtaskinfo.setSeq(pList.get(i).getSeq());
			//主表的作业号
			printQtaskinfo.setQid(newprintQtaskmain.getQid());
			//文件路径		
			printQtaskinfo.setSfilename("NONE");
			//打印内容参数
			//printQtaskinfo.setSpars(pList.get(i).getSpars());
			//模板编号
			printQtaskinfo.setModelnumber(pList.get(i).getModelnumber());
			//打印份数
			printQtaskinfo.setPrintnumber(pList.get(i).getPrintnumber());
			//打印联数
			printQtaskinfo.setPrintunitcount(pList.get(i).getPrintunitcount());
			//打印样式
			printQtaskinfo.setPrinttype(pList.get(i).getPrinttype());	
			//子任务作业状态
			printQtaskinfo.setStatus(0);
			//子任务的打印批次
			printQtaskinfo.setDaypc(pList.get(i).getDaypc());
			//区域标识
			printQtaskinfo.setQuyzs(pList.get(i).getQuyzs());
			//循环的插入多条子数据
			printQtaskinfoService.doInsert(printQtaskinfo);
			
			//插入表单数据 并判断是否有多个区域
			if("1".equals(pList.get(i).getQuyzs())){
				//插入表单数据
				printQtasksheet.setQid(qid);
				printQtasksheet.setSeq(pList.get(i).getSeq());
				PrintQtasksheet pt =(PrintQtasksheet)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.querySheet",printQtasksheet);
				printQtasksheet.setQid(newprintQtaskmain.getQid());
				printQtasksheet.setSpars(pt.getSpars());
				printQtasksheet.setArea(pt.getArea());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.insertPrintQtasksheet", printQtasksheet);
			}else{
				//插入表单数据
				printQtasksheet.setQid(qid);
				printQtasksheet.setSeq(pList.get(i).getSeq());
				List<PrintQtasksheet> pt = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.querySheet",printQtasksheet);
					for (int j = 0; j < pt.size(); j++) {
						printQtasksheet.setQid(newprintQtaskmain.getQid());
						printQtasksheet.setSpars(pt.get(j).getSpars());
						printQtasksheet.setArea(pt.get(j).getArea());
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.insertPrintQtasksheet", printQtasksheet);
					}
				//插入清单数据
				PrintQtasklist printQtasklist = new PrintQtasklist();
				printQtasklist.setQid(qid);
				printQtasklist.setSeq(pList.get(i).getSeq());
				List<PrintQtasklist> pst = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryGrid1",printQtasklist);
					for (int j = 0; j < pst.size(); j++) {
						printQtasklist.setQid(newprintQtaskmain.getQid());
						printQtasklist.setArea(pst.get(j).getArea());
						printQtasklist.setXuh(pst.get(j).getXuh());
						printQtasklist.setSpars(pst.get(j).getSpars());
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.insertPrintQtasklist", printQtasklist);
					}
			}
		}
	}
}

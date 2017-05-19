/**
 * 浠ｇ爜澹版槑
 */
package com.athena.print.module.sys.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.Printright;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
@Component
public class PrintrightService extends BaseService<Printright>{
	
	
	@Override
	//杩斿洖sqlMap鐨勫懡鍚嶇┖闂�
	protected String getNamespace() {
		return "sys";
	}
	
	//鐢ㄦ埛椤甸潰鏌ヨ
	public Map<String,Object> selectS(Printright bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintright1",bean,bean);
	}
	
	
	public Map<String,Object> selectGly(Printright bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintrightGly",bean,bean);
	}
	
	
	//閫氳繃 鐢ㄦ埛缁勩�鍗曟嵁缁勩�浠撳簱缂栧彿寰楀埌鎵撳嵃鏈虹粍
	public Printright selectSpcodesBygroup(Printright bean) {
		return (Printright)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintright",bean);
	}
	
	//鏂板鎸夐挳鐨勬搷浣�
	@Transactional
	public String save(String usercenter ,String userscode ,
			List<Printright> insert ,
			List<Printright> edit ,
			List<Printright> delete,String userId) throws ServiceException{
		//鎵ц鏂板鎿嶄綔
		doInsert(usercenter,userscode,insert,userId);
		//鎵ц鍒犻櫎鎿嶄綔
		doDelete(usercenter,userscode,delete,userId);
		//鎵ц淇敼鎿嶄綔
		doUpdate(usercenter,userscode,edit,userId);
		return "success";
	}
	
	//鍗曡〃琛岀紪杈戠殑 澧�鍒犳敼 鎿嶄綔
	@Transactional
	public String saves(Printright bean,List<Printright> insert ,
			List<Printright> edit ,
			List<Printright> delete,String userId) throws ServiceException{
		String strm = "";
		//濡傛灉 鏂板 淇敼 鍒犻櫎 閮戒笉鎿嶄綔 灏辩洿鎺ヨ繑鍥�
		if(0==insert.size()&&0==delete.size()&&0 == edit.size()){
			return "null";
		}
		//鎵归噺鐨勬柊澧炴搷浣�
		strm = inserts(bean,insert,userId);
		//鎵归噺鐨勪慨鏀规搷浣�
		edits(bean,edit,userId);
		//鎵归噺鐨勫垹闄ゆ搷浣�
		deletes(bean,delete);
		
		if(!strm.equals("")){
			return strm;
		}
		
		return "success";
	}
	
	@Transactional
	public String savesgly(Printright bean,List<Printright> insert ,
			List<Printright> edit ,
			List<Printright> delete,String userId) throws ServiceException{
		//濡傛灉 鏂板 淇敼 鍒犻櫎 閮戒笉鎿嶄綔 灏辩洿鎺ヨ繑鍥�
		if(0==insert.size()&&0==delete.size()&&0 == edit.size()){
			return "null";
		}
		
		insertsgly(bean,insert,userId);
		
		edits(bean,edit,userId);
		
		deletes(bean,delete);
		
		return "success";
	}
	
	/**
	 * 鎻掑叆
	 */
	public String doInsert(String usercenter,String userscode,List<Printright> insert,String userId) {
		if(null!=insert){
			//澶氭潯璁板綍澧炲姞寰幆
			for (Printright printright : insert) {
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setCreator(userId);
				//璁剧疆鍒涘缓鏃堕棿
				printright.setCreate_time(DateUtil.curDateTime());
				//鍏堣幏鍙栫敤鎴风粍鐨勭紪鍙�
				printright.setUserscode(userscode);
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(usercenter);
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鏂板鐨勬搷浣�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintright", printright);
			}
		}
		return "Data entry success";
	}
	
	/**
	 * 鍒犻櫎
	 */
	public String doDelete(String usercenter,String userscode,List<Printright> delete,String userId) {
		if(null!=delete){
			//澶氭潯璁板綍鍒犻櫎寰幆
			for (Printright printright : delete) {
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setEditor(userId);
				//璁剧疆鍒涘缓鏃堕棿
				printright.setEdit_time(DateUtil.curDateTime());
				//鍏堣幏鍙栫敤鎴风粍鐨勭紪鍙�
				printright.setUserscode(userscode);
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(usercenter);
				//鎵ц鍒犻櫎鐨勬搷浣�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintright", printright);
			}
		}
		return "Data deleted success";
	}
	/**
	 * 鏇存柊
	 */
	public String doUpdate(String usercenter,String userscode,List<Printright> update,String userId) {
		if(null!=update){
			//澶氭潯璁板綍鏇存柊寰幆
			for (Printright printright : update) {
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setEditor(userId);
				//璁剧疆鍒涘缓鏃堕棿
				printright.setEdit_time(DateUtil.curDateTime());
				//鍏堣幏鍙栫敤鎴风粍鐨勭紪鍙�
				printright.setUserscode(userscode);
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(usercenter);
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鏇存柊鐨勬搷浣�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintright", printright);
			}
		}
		return "Data change success";
	}
	
	
	/**
	 * 绉佹湁鎵归噺insert鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param insert
	 * @return  ""
	 */
	private String inserts(Printright bean,List<Printright> insert,String userId)throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		String str ="";
		if(null!=insert){
			for(Printright printright:insert){
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setCreator(userId);
				//璁剧疆澧炲姞鏃堕棿
				printright.setCreate_time(DateUtil.curDateTime());
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鎵归噺鐨勬彃鍏�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintright", printright);
			}
			//如果在管理员组权限设置中配置了，在到 仓库权限设置中 去配置的话，必须提示给用户 但是还是让保存通过
			//根据 用户组  单据组  去查询 数据是否存在即可
			for(Printright printright:insert){
			   List<Printright>  plist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintrightbygly",printright);
			   if(0!=plist.size()){
				   str = "同一用户组下的同一单据组的子仓库中已有打印权限";
				   break;
			   }
			}
			if("".equals(str)){
				str="Data entry success";
			}
		}
		//return "Data entry success";
		return str;
	}
	/**
	 * 绉佹湁鎵归噺update鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param update
	 * @return ""
	 */
	private String edits(Printright bean,List<Printright> update,String userId) throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		if(null!=update){
			for(Printright printright:update){
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setEditor(userId);
				//璁剧疆淇敼鏃堕棿
				printright.setEdit_time(DateUtil.curDateTime());
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鎵归噺鐨勬洿鏂�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintright", printright);
			}
		}
		return "Data change success";
	}
	/**
	 * 绉佹湁鎵归噺鍒犻櫎鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param delete
	 * @return ""
	 */
	private String deletes(Printright bean,List<Printright> delete)throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		if(null!=delete){
			for(Printright printright:delete){
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				//鎵ц鎵归噺鐨勫垹闄�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintright", printright);
			
			}
		}
		return "Data deleted success";
	}
	
	
	/**
	 * 绉佹湁鎵归噺insert鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param insert
	 * @return  ""
	 */
	private String insertsgly(Printright bean,List<Printright> insert,String userId)throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		if(null!=insert){
			for(Printright printright:insert){
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setCreator(userId);
				//璁剧疆澧炲姞鏃堕棿
				printright.setCreate_time(DateUtil.curDateTime());
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鎵归噺鐨勬彃鍏�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.insertPrintrightgly", printright);
			}
		}
		return "Data entry success";
	}
	/**
	 * 绉佹湁鎵归噺update鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param update
	 * @return ""
	 */
	private String editsgly(Printright bean,List<Printright> update,String userId) throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		if(null!=update){
			for(Printright printright:update){
				//璁剧疆鐧诲綍鐢ㄦ埛
				printright.setEditor(userId);
				//璁剧疆淇敼鏃堕棿
				printright.setEdit_time(DateUtil.curDateTime());
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup1",printright);
				Message m=new Message("notspcodes","i18n.print.i18n_print");
				if(0==sList.size()){
							throw new ServiceException(m.getMessage());
				}
				//鎵ц鎵归噺鐨勬洿鏂�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintright", printright);
			}
		}
		return "Data change success";
	}
	/**
	 * 绉佹湁鎵归噺鍒犻櫎鏂规硶
	 * @author wy
	 * @date 2011-2-9
	 * @param delete
	 * @return ""
	 */
	private String deletesgly(Printright bean,List<Printright> delete)throws ServiceException{
		//鍒ゆ柇鏄惁涓虹┖
		if(null!=delete){
			for(Printright printright:delete){
				//璁剧疆鐢ㄦ埛涓績
				printright.setUsercenter(bean.getUsercenter());
				//鎵ц鎵归噺鐨勫垹闄�
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.deletePrintright", printright);
			
			}
		}
		return "Data deleted success";
	}
}
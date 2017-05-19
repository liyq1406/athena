package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.paicfj.Ckx_lingjxlh_qud;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 零件序列号-区段
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_lingjxlh_qudService extends BaseService<Ckx_lingjxlh_qud> {

	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param loginUser
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_lingjxlh_qud> insert,
			   List<Ckx_lingjxlh_qud> edit,
			   List<Ckx_lingjxlh_qud> delete,LoginUser loginUser) throws ServiceException {
		if(0==insert.size()&&0==edit.size()&&0==delete.size()){
			return "null";
		}
		inserts(insert, loginUser);
		edits(edit, loginUser);
//		removes(delete,loginUser);
		return "success";
	}
	/**
	 * 批量数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param loginUser
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_lingjxlh_qud> insert,LoginUser loginUser) throws ServiceException {
		Date date = new Date();
		for(Ckx_lingjxlh_qud bean:insert){			
			checkLingjbh(bean);	
			bean.setQid(this.getUUID());
			checkXuLHQuD(bean);		
			if(!DateTimeUtil.compare(bean.getQisrq(), bean.getJiesrq())){
				//"起始日期不能大于结束日期"
				throw new ServiceException(GetMessageByKey.getMessage("qsbndyjs"));
			}
			bean.setZhidr(loginUser.getUsername());	
			bean.setZhidsj(DateTimeUtil.getCurrDate());
			bean.setCreator(loginUser.getUsername());
			bean.setCreate_time(date);
			bean.setEditor(loginUser.getUsername());
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_lingjxlh_qud",bean);
			/*
			String sql = "select count(*) from ckx_lingjxlh_qud where usercenter='"
				+bean.getUsercenter()+"'and lingjbh='"
				+bean.getLingjbh()+"' and (xulhks between '"
				+bean.getXulhks()+"' and '"+bean.getXulhjs()+
				"' or xulhjs between '"+bean.getXulhks()+"' and '"
				+bean.getXulhjs()+"')"; 
			*/
		}
		return "";
	}
	 /**
	  * 验证零件编号是否存在零件表中
	  * @param bean
	  * @return
	  */
	private String checkLingjbh(Ckx_lingjxlh_qud bean){
		CkxLingj ckxLingj = new CkxLingj();
		ckxLingj.setUsercenter(bean.getUsercenter());
		ckxLingj.setLingjbh(bean.getLingjbh());
		ckxLingj.setBiaos("1");
		CkxLingj ckxLingjs =  (CkxLingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingj",ckxLingj);
		if(null == ckxLingjs){
			//"零件表中不存在当前用户中心下零件编号或者数据已失效，请重新输入"
			throw new ServiceException(GetMessageByKey.getMessage("cxsr"));
		}
		return "";
	}
	/**
	 * 验证同一序列号前缀，序列号开始到结束区段不能重合
	 * hj 2012-07-27
	 * @param bean
	 */
	private void checkXuLHQuD(Ckx_lingjxlh_qud bean){
		if(Integer.parseInt(bean.getXulhks())>= Integer.parseInt(bean.getXulhjs())){
			//"同一序列号前缀，序列号结束区段必须大于开始区段"
			throw new ServiceException(GetMessageByKey.getMessage("jsqdyks"));
		}
		Integer o = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.checkXuLHQuD", bean);		
		if(o > 0){
			//"同一序列号前缀，序列号开始到结束区段不能重合"
			throw new ServiceException(GetMessageByKey.getMessage("ksdjsbch"));
		}
	}
	/**
	 * 批量数据编辑
	 * @author hj
	 * @Date 2012-02-21
	 * @param edit
	 * @param loginUser
	 * @return "" 
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_lingjxlh_qud> edit,LoginUser loginUser) throws ServiceException {
		Date date = new Date();
		for(Ckx_lingjxlh_qud bean:edit){	
			checkLingjbh(bean);
			checkXuLHQuD(bean);
			if(!DateTimeUtil.compare(bean.getQisrq(), bean.getJiesrq())){
				//"起始日期不能大于结束日期"
				throw new ServiceException(GetMessageByKey.getMessage("qsbndyjs"));
			}
			bean.setZhidr(loginUser.getUsername());	
			bean.setZhidsj(DateTimeUtil.getCurrDate());
			bean.setEditor(loginUser.getUsername());
				bean.setEdit_time(date);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_lingjxlh_qud",bean);
		}
		return "";
	}
	/**
	 * 批量数据删除（物理删除）
	 * @author hj
	 * @Date 2012-02-21
	 * @param delete
	 * @return ""
	 * @throws ServiceException
	 */
	public String removes(List<Ckx_lingjxlh_qud> delete,String userID) throws ServiceException {
		Date date = new Date();
		for(Ckx_lingjxlh_qud bean:delete){	
			    bean.setCreator(userID);
		        bean.setEdit_time(date);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_lingjxlh_qud",bean);			
		}
		return "";
	}
}

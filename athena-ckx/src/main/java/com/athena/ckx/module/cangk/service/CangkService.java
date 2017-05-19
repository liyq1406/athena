package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.module.carry.service.CkxWaibwlService;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 仓库
 * @author denggq
 * @date 2012-1-12
 */
@Component
public class CangkService extends BaseService<Cangk> {
	
	@Inject
	private ZickService zickService;
	@Inject
	private CkxWaibwlService ckxWaibwlService;
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存仓库
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-2-3
	 * @return bean
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String save(Cangk bean , Integer operant , ArrayList<Zick> insert , ArrayList<Zick> edit , ArrayList<Zick> delete ,LoginUser user) throws ServiceException{
		
		Map map = user.getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		
		//容器场编号是否存在
		if(null != bean.getRongqcbh()&&!"".equals(bean.getRongqcbh())){
			Rongqc rongqc = new Rongqc();
			rongqc.setUsercenter(bean.getUsercenter());
			rongqc.setRongqcbh(bean.getRongqcbh());
			rongqc.setShiffk("R");
			rongqc.setBiaos("1");
			String mes1 = GetMessageByKey.getMessage("rongqcbh")+bean.getRongqcbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountRongqc", rongqc)){
				throw new ServiceException(mes1);
			}
		}
		
		if((null != bean.getAnqkctsmrz()) && (null != bean.getZuidkctsmrz()) && (bean.getAnqkctsmrz() > bean.getZuidkctsmrz())){
			throw new ServiceException(GetMessageByKey.getMessage("bundy"));
		}
		//0005458 mantis
		if("0".equals(bean.getZhantlx())&&null==bean.getXiehztbz()){
			throw new ServiceException("卸货站台编组不能为空");
		}
		
		if(null != bean.getXiehztbz()&&!"".equals(bean.getXiehztbz())){
			Xiehztbz xhztbz = new Xiehztbz();
			xhztbz.setXiehztbzh(bean.getXiehztbz());
			xhztbz.setBiaos("1");
			String mes =GetMessageByKey.getMessage("xiehuoztzbh")+bean.getXiehztbz()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXiehztbz", xhztbz)){
				throw new ServiceException(mes);
			}
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiehztbz where xiehztbzh = '"+bean.getXiehztbz()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("xiehuoztzbh")+bean.getXiehztbz()+GetMessageByKey.getMessage("notexist"));
		}
		
		//验证线号是否有效 hanwu 20150720
		if(null != bean.getXianh()&&!"".equals(bean.getXianh())){
			Cangk cangk = new Cangk();
			cangk.setUsercenter(bean.getUsercenter());
			cangk.setXianh(bean.getXianh());
			cangk.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXianh", cangk)){
				throw new ServiceException("【线号】用户中心"+bean.getUsercenter()+"对应的线号"+bean.getXianh()+"不存在或已失效");
			}
		}
		
		bean.setEditor(user.getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		if (1 == operant){
			bean.setCreator(user.getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCangk", bean);
		}else if(2 == operant){
			
			if(getzbcZxc()){
				
				Cangk cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk",bean);
				String xiehztz = cangk.getXiehztbz();
				//xss-0011896
				if (xiehztz==null){
					xiehztz="";
				}
				
				if("0".equals(bean.getZhantlx())){
					if(!xiehztz.equals(bean.getXiehztbz())){
						ckxWaibwlService.updateXiehztbzByMudd(bean.getCangkbh(), bean.getXiehztbz(), user.getUsername());
					}
				}
			}
			if("ZBCPOA".equals(bean.getRole())){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangkByZbcpoa", bean);
			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangk", bean);
			}
			
		}
		
		zickService.save(insert, edit, delete, user, bean);
		updateNUllByZhantbh(bean,key);
		//0007151  校验 卸货站台编号组变更时 验证 卸货站台编组和卸货站台编号的对应关系
		if(jiaoyxhzt(bean)){
			throw new ServiceException("仓库编号"+bean.getCangkbh()+"下的子仓库对应的卸货站台编号不属于卸货站台编组"+bean.getXiehztbz());
		}
		return "success";
	}
	
	
	private boolean jiaoyxhzt(Cangk bean){
		if(bean.getZhantlx().equals("0")){
			bean.setBiaos("1");
			List<Cangk>  zhantbhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.jiaoyztbh", bean);
			if(zhantbhlist.size()>0){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * 仓库失效
	 * @author denggq
	 * @param bean
	 * @date 2012-1-12
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDelete(Cangk bean) throws ServiceException{
		checkLingjCk(bean);
		Zick z=new Zick();
		z.setUsercenter(bean.getUsercenter());
		z.setCangkbh(bean.getCangkbh());
		z.setBiaos("1");
		List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryZick", z);
		if(list.size() == 0){//仓库是否存在子仓库，若不存在则可以失效
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCangk", bean);
		}else{
			throw new ServiceException("必须先失效掉全部仓库下的子仓库");
		}
		return "失效成功";
	}

	/**
	 * 获得多个仓库
	 * @author denggq
	 * @param bean
	 * @date 2012-1-12
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List list(Cangk bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangk", bean);
	}
	
	/**
	 * 获得多个仓库-导出
	 * @author hanwu
	 * @param bean
	 * @date 2015-07-21
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List listForExport(Cangk bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCangkForExport", bean);
	}
	
	/**
	 * 获得多个子仓库
	 * @param bean
	 * @return List
	 * @author wangyu
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List listZick(Cangk bean) throws ServiceException {
		List<Cangk> cangk =  list(bean);
		String str="";
		for (int i = 0; i < cangk.size(); i++) {
			Cangk ck = cangk.get(i);
			if( i != (cangk.size()-1)){
				str +="'"+ck.getCangkbh()+"',";	//参数递进： '1','2','3',
			}else{
				str +="'"+ck.getCangkbh()+"'";	//最后一个参数  不需要加 , :  '1','2','3','4'
			}	
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryDczick",str);
	}
	
	/**
	 * 清空卸货站台的仓库编号
	 * @param bean
	 * @param key
	 */
	private void updateNUllByZhantbh(Cangk bean,String key){
		if("1".equals(bean.getZhantlx())){
			if("ZXCPOA".equals(key)){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangkbhOfFayztNULL", bean);
			}
		}else{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCangkbhOfXiehztNULL", bean);
		}
	}
	/**
	 * 检测是否有仓库对应的零件-仓库数据，若有，则零件必须是失效的，
	 * 并删掉这些数据，否则仓库失效失败
	 * @param bean
	 */
	private void checkLingjCk(Cangk bean){
		Lingjck lingjck = new Lingjck();
		lingjck.setUsercenter(bean.getUsercenter());
		lingjck.setCangkbh(bean.getCangkbh());
		//检测是否有仓库对应的零件-仓库数据，若有，则零件必须是失效的
		if(DBUtil.checkCount(baseDao,"ts_ckx.getCheckCountLingj",lingjck)){
			throw new ServiceException("零件仓库中用户中心"+bean.getUsercenter()+",仓库："+bean.getCangkbh()+",对应的零件未失效，失效对应的零件才能失效该仓库");
		}
		//根据仓库删除仓库对应的零件-仓库
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteLingjck", lingjck);
	}
	
	/**
	 * 判断是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 查询用户中心对应的工厂
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,String>> queryGongc(String usercenter) {
		Cangk bean =new Cangk();
		bean.setUsercenter(usercenter);//当前用户中心
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongc",bean);;
//		if(0 == list.size()){
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("KEY", "");
//			map.put("VALUE", "未配置工厂");
//			list.add(map);
//		}
		return list;
	}
}

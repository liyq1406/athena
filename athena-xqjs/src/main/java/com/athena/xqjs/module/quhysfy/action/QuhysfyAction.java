package com.athena.xqjs.module.quhysfy.action;


import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.quhyuns.Rukmx;
import com.athena.xqjs.entity.quhyuns.Yunsfyhz;
import com.athena.xqjs.module.quhysfy.service.QuhysfyService;
import com.athena.xqjs.module.quhysfy.service.RukmxService;
import com.athena.xqjs.module.utils.xls.lingxjyfgl.XlsHandlerUtilslxjyfgl;
import com.athena.xqjs.util.GetPostOnly;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class QuhysfyAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出

	
	/**
	 * 取货运输费用
	 * @author denggq
	 * @date 2012-3-6
	 */
	@Inject 
	private QuhysfyService quhyunsService;
	
	@Inject 
	private RukmxService rukmxService;
	
	
	@Inject
	private AbstractIBatisDao baseDao;
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	//进入取货运输主界面
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	//进入紧急件管理界面
	public String executejinjjyfgl(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "selectjinjjyfgl";
	}
	
	//进入预审界面
	public String toYuShenPage(@Param Yunsfyhz bean){
		setResult("removeId", getParam("removeId"));
		setResult("usercenter", bean.getUsercenter());	//登录人所在的用户中心
		return "toyushen";
	}
	//进入重算界面
	public String tochongsuan(@Param Yunsfyhz bean){
		setResult("removeId", getParam("removeId"));
		setResult("usercenter", bean.getUsercenter());	//登录人所在的用户中心
		setResult("danjh", bean.getDanjh());
		setResult("banch", bean.getBanch());
		setResult("danjlx", bean.getDanjlx());
		if(bean.getDanjlx().equalsIgnoreCase("1")){
		return "tochongsuan";
		}else{
		return "tochongsuanJij";	
		}
	}
	//进入初审界面
	public String tochushenpage(@Param Yunsfyhz bean){
		setResult("removeId", getParam("removeId"));
		setResult("usercenter", bean.getUsercenter());	//登录人所在的用户中心
		setResult("danjh", bean.getDanjh());
		setResult("jinjjdjh", bean.getJinjjdjh());
		setResult("danjlx", bean.getDanjlx());
		return "tochushenpage";
	}
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-10
	 * @return String
	 */
	//进入零星件运费管理主界面
	public String lingxjyfgl(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	/**
	 * 运输汇总分页查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 * @return String
	 */
	public String query(@Param Yunsfyhz bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			GetPostOnly.checkqhqx(bean.getUsercenter());
			bean.setBiaos("1");
			setResult("result", quhyunsService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "汇总数据查询");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "汇总数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}	
		return AJAX;
	}
	
	
	public String queryRukmx(@Param Rukmx bean) {
		try{
			setResult("result", rukmxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "明细数据查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "明细数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	//查询急件
	public String queryRukmxJij(@Param Rukmx bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getUsercenter()==null){
				if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
					List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
					StringBuffer sb=new StringBuffer();
					for (String s : uclist) {
					sb.append("'"+s+"',");
					}
					sb.delete(sb.length()-1, sb.length());
					bean.setUclist(sb.toString());
				}
				}
			setResult("result", rukmxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "急件数据查询");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "急件数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String queryRukmxChus(@Param Rukmx bean) {
		try{
			if(StringUtils.isNotBlank(bean.getJinjjdjh())){
				bean.setDanjh("'"+bean.getDanjh()+"','"+bean.getJinjjdjh()+"'");
			}else{
				bean.setDanjh("'"+bean.getDanjh()+"'");
			}
			
			setResult("result", rukmxService.queryRukmxChus(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "入库明细初审查询");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "入库明细初审查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	//预审
	public String saveyushen(@Param Rukmx bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			bean.setBiaos("1");
			bean.setShenhzt("1");//未审核
			rukmxService.checkShenhkssj(bean);
			Yunsfyhz yunsfyhz= rukmxService.gettotalyunf(bean);
			yunsfyhz.setDanjmc(bean.getDanjmc());
			yunsfyhz.setUsercenter(bean.getUsercenter());
			String danjh="";
			yunsfyhz.setDanjlx(bean.getDanjlx());//单据类型
			Yunsfyhz maxdanjh=quhyunsService.getMaxDanjh(yunsfyhz);//获取当前类型数据库中的最大版次号
			if(maxdanjh!=null){
				DecimalFormat data = new DecimalFormat("00000000");			
				danjh=	maxdanjh.getDanjh().substring(0, 2)+data.format(Integer.parseInt(maxdanjh.getDanjh().substring(2))+1);	
			}else{
				if(bean.getDanjlx().equals("1")){
					danjh="Z"+bean.getUsercenter().substring(1)+"00000001";//正常
				}else{
					danjh="J"+bean.getUsercenter().substring(1)+"00000001";//紧急
				}
			}
			yunsfyhz.setBanch("第1版");
			yunsfyhz.setDanjh(danjh);//单据号
		    Date date = new Date();
	        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        yunsfyhz.setYus_time(formatter.format(date));//预审时间
			yunsfyhz.setYusr(getLoginUser().getUsername());//预审人
			yunsfyhz.setShenhzt("2");
			yunsfyhz.setShenhkssj(bean.getQisruksj());
			yunsfyhz.setShenhjssj(bean.getJisruksj());
			quhyunsService.save(yunsfyhz);
			bean.setShenhzt("2");
			bean.setDanjh(danjh);
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(formatter.format(date));
			rukmxService.updateRukmx(bean);
			
			Message m = new Message("成功");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运输预审", "数据修改");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运输预审", "数据修改", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	//初审
	public String savechushen(@Param("list") ArrayList<Yunsfyhz> yunsfyhz){

		
		Map<String,String> message = new HashMap<String,String>();
		try {
			   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Yunsfyhz zhengcj=new Yunsfyhz();
			Yunsfyhz jinjj=new Yunsfyhz();
			if(yunsfyhz.size()>1){//两条单据
				Double zfy=0.0;
				for (Yunsfyhz y : yunsfyhz) {
					Yunsfyhz yunsfyh  =quhyunsService.get(y);
					if(StringUtils.isNotBlank(yunsfyh.getFlag()) && yunsfyh.getFlag().equals("1")){
						throw new ServiceException("单据号"+yunsfyh.getDanjh()+"目前在重算中，不能操作该单据");
					}
					if((StringUtils.isNotBlank(yunsfyh.getSave_time()) && StringUtils.isBlank(yunsfyh.getChongs_time()))|| (StringUtils.isNotBlank(yunsfyh.getSave_time()) && StringUtils.isNotBlank(yunsfyh.getChongs_time()) && yunsfyh.getSave_time().compareTo(yunsfyh.getChongs_time())>0)){
						throw new ServiceException("单据号"+yunsfyh.getDanjh()+"重算时只做了保存没有做重算");
					}

					if(yunsfyh!=null && !yunsfyh.getShenhzt().equalsIgnoreCase("2")){
						throw new ServiceException("单据号"+yunsfyh.getDanjh()+"已经不是预审状态，不能初审");
					}
					if(yunsfyh!=null && !yunsfyh.getBiaos().equalsIgnoreCase("1")){
						throw new ServiceException("单据号"+yunsfyh.getDanjh()+"已经被撤回，不能初审");
					}
					 if(yunsfyh.getDanjlx().equals("1")){
						 zfy+=yunsfyh.getYunszfy();
						 zhengcj=yunsfyh; 
					 }else{
						 zfy+=yunsfyh.getJinjfy();
						 jinjj=yunsfyh;
					 }
					}
				zhengcj.setYunszfy(zfy);//正常件总费用加紧急件费用之和
				zhengcj.setJinjfy(jinjj.getJinjfy());
				zhengcj.setJinjjdjh(jinjj.getDanjh());
				jinjj.setBiaos("2");//紧急件合并失效
				jinjj.setEditor(getLoginUser().getUsername());
				jinjj.setEdit_time(formatter.format(new Date()));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updatechusYunsfyhz", jinjj);
			}else if(yunsfyhz.size()==1){//一条单据
				zhengcj =quhyunsService.get(yunsfyhz.get(0));
				if(StringUtils.isNotBlank(zhengcj.getFlag()) && zhengcj.getFlag().equals("1")){
					throw new ServiceException("单据号"+zhengcj.getDanjh()+"目前在重算中，不能操作该单据");
				}
				if((StringUtils.isNotBlank(zhengcj.getSave_time()) && StringUtils.isBlank(zhengcj.getChongs_time())) || (StringUtils.isNotBlank(zhengcj.getSave_time()) && StringUtils.isNotBlank(zhengcj.getChongs_time()) && zhengcj.getSave_time().compareTo(zhengcj.getChongs_time())>0)){
					throw new ServiceException("单据号"+zhengcj.getDanjh()+"在重算页面只做了保存没有做重算");
				}
				if(zhengcj!=null && !zhengcj.getShenhzt().equalsIgnoreCase("2")){
					throw new ServiceException("单据号"+zhengcj.getDanjh()+"已经不是预审状态，不能初审");
				}
				if(zhengcj!=null && !zhengcj.getBiaos().equalsIgnoreCase("1")){
					throw new ServiceException("单据号"+zhengcj.getDanjh()+"已经被撤回，不能初审");
				}
			}
			zhengcj.setShenhzt("3");	//初审	
			zhengcj.setChusr(getLoginUser().getUsername());
			zhengcj.setChus_time(formatter.format(new Date()));
			zhengcj.setEditor(getLoginUser().getUsername());//修改人
			zhengcj.setEdit_time(formatter.format(new Date()));//修改时间
		
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.updatechusYunsfyhz", zhengcj);
			Message m = new Message("成功");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "初审");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "初审", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
//打印
	public String toprint(@Param Yunsfyhz bean) {
	
		int page = 0;
		Yunsfyhz yunsfyhz=	quhyunsService.get(bean);
		
		Rukmx rukmx=new Rukmx();
		rukmx.setDanjh(bean.getDanjh());
		rukmx.setUsercenter(bean.getUsercenter());
		rukmx.setJinjjdjh(bean.getJinjjdjh());
		Map<String , List<Yunsfyhz>> zcmap=new LinkedHashMap<String, List<Yunsfyhz>>();//正常件
		Map<String , List<Yunsfyhz>> jjmap=new LinkedHashMap<String, List<Yunsfyhz>>();//紧急件
		Map<String , List<Yunsfyhz>> lxmap=new LinkedHashMap<String, List<Yunsfyhz>>();//零星件
		Map<String, Object> lingxmc=new LinkedHashMap<String, Object>();
		page=rukmxService.getprinttotalyunf(rukmx,zcmap,jjmap,lxmap,lingxmc);
	//	Map<String, Object> lingxzd=rukmxService.getlingxzd();

		Map<String, Object> result=new HashMap<String, Object>();
	
		int pagesize = 0;
	
		
		   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (yunsfyhz !=null){ 
			result.put("danjh", yunsfyhz.getDanjh());
			
			result.put("danjmc", yunsfyhz.getDanjmc());
			result.put("banch", yunsfyhz.getBanch());
			
			result.put("dayr", getLoginUser().getUsername());
			
			result.put("daysj", formatter.format(new Date()));
			
		
			result.put("pagesize", pagesize);	 
			
			result.put("page",page);	
		} 
		result.put("lingxmc", lingxmc);		
		result.put("zcmap", zcmap);
		result.put("jjmap", jjmap);
		result.put("lxmap", lxmap);
		setRequestAttribute("result", result);
		return "toprint";
}
	
	//重算修改包装型号和包装容量
	public String updateRukmxBaoz(@Param("insert") ArrayList<Rukmx> insert,@Param("edit") ArrayList<Rukmx> edit,@Param("delete") ArrayList<Rukmx> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(rukmxService.save(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "重算修改包装型号和包装容量");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "重算修改包装型号和包装容量", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 数据保存（多条数据操作）  急件
	 * @param bean
	 * @author lc
	 * @date 2016-11-29saves
	 */
	public String savesRukmxJinjj(@Param("insert") ArrayList<Rukmx> insert,@Param("edit") ArrayList<Rukmx> edit,@Param("delete") ArrayList<Rukmx> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(rukmxService.saveJinjj(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "紧急件", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "紧急件", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 急件重算页面修改趟次
	 * @param bean
	 * @author lc
	 * @date 2016-11-29saves
	 */
	public String chongsuanupdateRukmxJinjj(@Param("insert") ArrayList<Rukmx> insert,@Param("edit") ArrayList<Rukmx> edit,@Param("delete") ArrayList<Rukmx> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(rukmxService.updateJinjj(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "急件重算修改趟次");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "急件重算修改趟次", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 手工重算
	 * @param bean
	 * @author lc
	 * @date 2016-11-29saves
	 */
	public String chongsuan(@Param Yunsfyhz  bean) {
		Map<String,String> message = new HashMap<String,String>();
		try {	
			Yunsfyhz yunsfyhz=	quhyunsService.get(bean);
			if(yunsfyhz!=null && !yunsfyhz.getBanch().equalsIgnoreCase(bean.getBanch())){
				throw new ServiceException("该版数据已经被重算过,请刷新页面");
			}
			
			if(yunsfyhz!=null && !yunsfyhz.getShenhzt().equalsIgnoreCase("2")){
				throw new ServiceException("单据已经不是预审状态，不能重算");
			}
			if(yunsfyhz!=null && !yunsfyhz.getBiaos().equalsIgnoreCase("1")){
				throw new ServiceException("单据已经被撤回，不能重算");
			}

			Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.checksuo", bean);
			if (Integer.valueOf(obj.toString())>0) {
				throw new ServiceException("目前有单据在重算中，请等待重算结束后再操作");
			}
		
			Rukmx rukmx=new Rukmx();
			rukmx.setUsercenter(bean.getUsercenter());
			rukmx.setDanjh(bean.getDanjh());
			List<Rukmx>  rukmxs=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.queryRukmx", rukmx);
			Message m = new Message(rukmxService.chongsuan(rukmxs,bean));
			message.put("message",m.getMessage());	
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "手工重算", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	//撤回
	public String chehuiYunsfyhz(@Param Yunsfyhz  bean) {
		Map<String,String> message = new HashMap<String,String>();
		try {	
			Yunsfyhz yunsfyh  =(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", bean);
			if(StringUtils.isNotBlank(yunsfyh.getFlag()) && yunsfyh.getFlag().equals("1")){
				throw new ServiceException("单据号"+yunsfyh.getDanjh()+"目前在重算中，不能操作该单据");
			}
			Message m = new Message(quhyunsService.chehuiYunsfyhz(bean,getLoginUser().getUsername()));
			message.put("message",m.getMessage());	
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "撤回");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "撤回", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	//终审
	public String zhongshenYunsfyhz(@Param Yunsfyhz  bean) {
		Map<String,String> message = new HashMap<String,String>();
		try {	
			Yunsfyhz yunsfyh  =quhyunsService.get(bean);
			if(yunsfyh!=null && !yunsfyh.getShenhzt().equalsIgnoreCase("3")){
				throw new ServiceException("单据号"+yunsfyh.getDanjh()+"已经不是初审状态，不能终审");
			}
			if(yunsfyh!=null && !yunsfyh.getBiaos().equalsIgnoreCase("1")){
				throw new ServiceException("单据号"+yunsfyh.getDanjh()+"已经被撤回，不能终审");
			}
			Message m = new Message(quhyunsService.zhongshenYunsfyhz(bean,getLoginUser().getUsername()));
			message.put("message",m.getMessage());	
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "终审");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "终审", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 导出数据
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String downloadYunsfyhz(@Param Rukmx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			bean.setBiaos("1");
			bean.setJiszt("1");
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Rukmx> rows = 	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.queryRukmxDownload", bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			if(bean.getDanjlx().equalsIgnoreCase("1")){
			  downloadServices.downloadFile("yusrukmx.ftl", dataSurce, response, "运费", ExportConstants.FILE_XLS, false);
			}else{
			  downloadServices.downloadFile("yusrukmxJij.ftl", dataSurce, response, "运费", ExportConstants.FILE_XLS, false);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 初审导出数据
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String downloadchushen(@Param Rukmx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			bean.setBiaos("1");
			bean.setJiszt("1");
			if(StringUtils.isNotBlank(bean.getJinjjdjh())){
				bean.setDanjh("'"+bean.getDanjh()+"','"+bean.getJinjjdjh()+"'");
			}else{
				bean.setDanjh("'"+bean.getDanjh()+"'");
			}
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Rukmx> rows = 	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ts_quhysfy.queryRukmxChus", bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("yusrukmx.ftl", dataSurce, response, "运费", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "取货运费", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 零星件运费管理分页查询
	 * @param bean
	 * @author lc
	 * @date 2016-11-29
	 * @return String
	 */
	public String queryLxjfygl(@Param Rukmx bean) {
		Map<String,String> message = new HashMap<String,String>();
		try{
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getUsercenter()==null){
				if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
					List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
				StringBuffer sb=new StringBuffer();
				for (String s : uclist) {
				sb.append("'"+s+"',");
				}
				sb.delete(sb.length()-1, sb.length());
				bean.setUclist(sb.toString());
			}
			}
			setResult("result", rukmxService.queryLxjfygl(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零星件运费管理", "数据查询");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零星件运费管理", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 数据保存（多条数据操作）
	 * @param bean
	 * @author lc
	 * @date 2016-11-29
	 */
	public String savesLxjfygl(@Param("insert") ArrayList<Rukmx> insert,@Param("edit") ArrayList<Rukmx> edit,@Param("delete") ArrayList<Rukmx> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(rukmxService.savesLxjfygl(insert, edit ,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零星件运费管理", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零星件运费管理", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 零星件运费管理模板下载
	 * lc 2016-11-30
	 */
	public String downloadlxjfyglMob(@Param Rukmx bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingxjyfgl.ftl", dataSurce, response, "零星件运费管理-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零星件运费管理", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零星件运费管理", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零星件运费管理", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 零星件运费管理导入数据
	 * @author lc
	 * @Date 2016-11-30
	 * @Param bean
	 * @return String
	 * */
	public String uploadlxjfygl(@Param Rukmx bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String>  map=new HashMap<String,String>();
			String mes = XlsHandlerUtilslxjyfgl.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "零星件运费管理", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "零星件运费管理", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getLoginUser().getUsercenter());
		return "upload";
	}
	
	
	//大站台下拉查询
	public String queryPostLxjlb() {
		Map<String,String> params = getParams();
		try {
			setResult("result", rukmxService.queryPostLxjlb(params));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "取货运费", "取货运费-零星件类别下拉", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}

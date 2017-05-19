package com.athena.xqjs.module.dfpvdiaobl.action;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobsqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;



/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DfpvDiaobsqAction
 * <p>
 * 类描述：DFPV调拨令申请
 * </p>
 * 创建人：xss
 * <p>
 * 创建时间：2016-1-5
 * </p>
 * 
 * @version
 * 
 */

@Component
public class DfpvDiaobsqAction  extends  ActionSupport{
	   
	/**
	 * 注入DiaobsqService
	 */
	@Inject
	private DfpvDiaobsqService dfpvdiaobsqService;
	
	@Inject
	private XqjsLingjckService xLingjckService;
	
	@Inject
	private LingjService lingjService;
	   
	
	//log4j日志初始化
	private final Log log = LogFactory.getLog(DfpvDiaobsqAction.class); 
	/**
	 * getUserInfo获取用户信息方法
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}  
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 页面初始化
	 */
	public  String  execute(){
		String  forward ="success";
		String sysdate = CommonFun.getJavaTime();
		sysdate = sysdate.substring(0,10);
		setResult("result",sysdate );
		setResult("center", getUserInfo().getUsercenter());
		//setResult("center", getLoginUser().getUsercenter());
		return  forward;
	}

	/**
	 * 保存方法
	 * 
	 * @param bean
	 * @param diaobsqmxs
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public String save(@Param Diaobsq bean, @Param("diaobsqmxs") ArrayList<Diaobsqmx> diaobsqmxs) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException 
	{
		if(null != bean)
		{
			if(StringUtils.isNotBlank(bean.getBeiz()))
			{
				int length = bean.getBeiz().length();
				if(length > 50)
				{
					setResult("errorMessage","备注信息字符数量超过50,请调整!");
	    			return AJAX;
				}
			}
		}
		
		
		//xss_0012853
		if(StringUtils.isNotBlank(bean.getChengbzx() )){
			int length = bean.getChengbzx().length();
			if(length !=3 && length !=5){
				setResult("errorMessage","成本中心只能为3或5位,请调整!");
    			return AJAX;
			}
		}
		
		
		//0010605备注字段英文双引号转换为中文双引号 gswang 2014-10-31
		bean.setBeiz(bean.getBeiz().replace("\"", "”"));
		/**
		 * 零件效验信息
		 */
		Map<String,Lingj> lingjMap = new HashMap<String, Lingj>();
		List<Lingj> lingjs = lingjService.queryList(this.getParams());
		lingjMap = this.translateListToMap(lingjs, "lingjbh");
		
		
		String  diaobsqdh = dfpvdiaobsqService.getdiaosqdh(getUserInfo().getUsercenter());
		String secondOrderNum = this.secondOrderNum(diaobsqdh);
		String  time = com.athena.xqjs.module.common.CommonFun.getJavaTime();
		List<Diaobsq> sqlist = new ArrayList<Diaobsq>();
		//set 调拨申请单号
		bean.setDiaobsqdh(diaobsqdh);
		bean.setDiaobsqsj(time.substring(0,10));
		
		//DFPV类型
		bean.setLeix("1");		
		
		//申请人
		bean.setCreator(getUserInfo().getUsername());
    	//创建时间
    	bean.setCreate_time(time);
    	//修改人
    	bean.setEditor(getUserInfo().getUsername());
    	//修改时间
    	bean.setEdit_time(time);
		sqlist.add(bean);
        //遍历出Diaobsqmx
		boolean flagKd = false;
		boolean flagIl = false;
		boolean flag = false;
        for(int i=0;i<diaobsqmxs.size();i++){
        	Diaobsqmx  mxBean = diaobsqmxs.get(i);
        	if(null == lingjMap.get(mxBean.getLingjbh()))
        	{
        		setResult("errorMessage", "零件编号为:" + mxBean.getLingjbh() + "的零件信息有误！");
				return AJAX;
        	}
        	/*if(!mxBean.getLux().toString().equals(lingjMap.get(mxBean.getLingjbh()).getZhizlx()) ){
        		setResult("errorMessage", "零件编号为:" + mxBean.getLingjbh() + "的制造路线为:" + lingjMap.get(mxBean.getLingjbh()).getZhizlx());
				return AJAX;
        	}*/
			String yaohsj = mxBean.getYaohsj();
			String nowTime = time.substring(0, 10);
			if (yaohsj == null || yaohsj.compareTo(nowTime) < 0) {
				setResult("errorMessage", "要货时间不能为空或小于当前时间！");
				return AJAX;
			}
        	bean.setBanc("0001");
        	//放入用户中心
        	mxBean.setUsercenter(bean.getUsercenter());
        	//状态
        	mxBean.setZhuangt(Const.DIAOBL_ZT_APPLYING);
        	//申请人
        	mxBean.setCreator(getUserInfo().getUsername());
        	//创建时间
        	mxBean.setCreate_time(time);
        	//修改人
			mxBean.setEditor(getUserInfo().getUsername());
        	//修改时间
        	mxBean.setEdit_time(time);
			boolean kdflag = mxBean.getLux().equals(Const.ZHIZAOLUXIAN_KD_PSA) || mxBean.getLux().equals(Const.ZHIZAOLUXIAN_KD_AIXIN);
			boolean ilflag = !kdflag;
			if (!flagIl && kdflag) {
				// 放入调拨申请单号
				mxBean.setDiaobsqdh(diaobsqdh);
				flagKd = true;
			} else if (flagIl && kdflag) {
				// 放入调拨申请单号
				mxBean.setDiaobsqdh(secondOrderNum);
				flag = true;
			} else if (!flagKd && ilflag) {
				// 放入调拨申请单号
				mxBean.setDiaobsqdh(diaobsqdh);
				flagIl = true;
			} else if (flagKd && ilflag) {
				// 放入调拨申请单号
				mxBean.setDiaobsqdh(secondOrderNum);
				flag = true;
			}
        }
		if (flag) {
			Diaobsq secondBean = (Diaobsq) BeanUtilsBean.getInstance().cloneBean(bean);

			secondBean.setDiaobsqdh(secondOrderNum);
			sqlist.add(secondBean);
		}
       try {
    	   
    	   
			boolean info = dfpvdiaobsqService.sqInsert(sqlist, diaobsqmxs);
        	if(!info){
        		setResult("errorMessage","申请失败");
    			return AJAX;
        	}
        	
		} catch (Exception e) {
			log.info(e.toString());
			
			Map<String,Integer> ljmap= new HashMap<String, Integer>();
			for (int i = 0; i < diaobsqmxs.size(); i++) {
				String  mljbh = diaobsqmxs.get(i).getLingjbh();
				if(!ljmap.containsKey(mljbh)){
					ljmap.put(mljbh, i+1);
				}else{
					String message="第"+ljmap.get(mljbh) +"行和"+"第"+(i+1)+ "行，"+mljbh+"零件重复，请修改！";
					throw new ServiceException(message);
				}

			}
			return AJAX;
		}

		Diaobsq  sq = dfpvdiaobsqService.selectDiaobsq(bean);
		sq.setDiaobsqsj(sq.getDiaobsqsj().substring(0,10));
		setResult("Message", "申请成功，等待计划员审批！");  
		setResult("diaobsq", sqlist);
		return AJAX;
	}
	
	/**
	 * <p>
	 * 一个调拨申请按订货路线拆分成IL与KD两个调拨申请
	 * </p>
	 * 
	 * @param sqdh
	 * @return
	 */
	public String secondOrderNum(String sqdh) {
		return sqdh.substring(0, 5) + String.format("%03d", Integer.parseInt(sqdh.substring(5, 8)) + 1);
	}



	/**
	 * 获得零件名称
	 * @param bean
	 * @return lingjmc
	 */
    public  String  getLingjmc(@Param  Diaobsq bean){
		Map<String, String> map = getParams();
		map.put("biaos", "1");
    	   Diaobsqmx lingj = null;
    	   try {
			lingj = dfpvdiaobsqService.selectLingjmc(map);
		} catch (Exception e) {
			   log.info(e.toString());
			setResult("flag", "您输入的用户中心或零件号有误，请重新输入！");
			   return AJAX;
		}
		setResult("lingj", lingj);
    	   return  AJAX;
    }
    /**
	 * 校验零件制造路线是否正确
	 * @param bean
	 * @return 制造路线
	
    public  String  querylucz(){
		Map<String, String> map = getParams();
    	   try {
			dfpvdiaobsqService.querylucz(map);
			} catch (Exception e) {
				log.info(e.toString());
				setResult("flag", "您输入的制造路线有误，请重新输入！");
				return AJAX;
			}
	        return  AJAX;
    }
     */
    
    /**
	 * 调拨申请打印   页面初始化
	 * @param bean
	 * @return string
	 */
	public  String  initPrint(@Param  Diaobsq  bean){
		String  forward = "success";
		      setResult("usercenter", bean.getUsercenter());
		      setResult("diaobsqdh", bean.getDiaobsqdh());
		      Diaobsq  sq = dfpvdiaobsqService.selectDiaobsq(bean);
			  setResult("banc", sq.getBanc());
		      setResult("diaobsq",sq);
		      return  forward;
	}
	
	  /**
	 * 调拨申请打印   页面数据加载
	 * @param bean
	 * @return 
	 */
	public String print(@Param("sq") ArrayList<Diaobsq> sqlist) {

			/*
		 * diaobsqOperationService.updateBanc(bean); // 时间格式yyyy-mm-dd
		 * bean.setDiaobsqsj(bean.getDiaobsqsj().substring(0, 10)); // 获取申请明细
		 * Map<String, Object> map = diaobshService.sumShipsl(bean); // 转成数组
		 * ArrayList<Diaobsqmx> ls = (ArrayList<Diaobsqmx>) map.get("rows"); //
		 * 循环组装Json数组 JSONArray jsArray1 = new JSONArray(); JSONArray jsArray2 =
		 * new JSONArray(); JSONArray jsArray3 = new JSONArray(); JSONArray
		 * jsArray4 = new JSONArray(); JSONArray jsArray5 = new JSONArray(); //
		 * 创建Json对象 JSONObject jsObject = new JSONObject();
		 * jsObject.put("DIAOBSQDH", bean.getDiaobsqdh());
		 * jsObject.put("SHENGXSJ", bean.getDiaobsqsj()); jsObject.put("BEIZ",
		 * bean.getBeiz()); jsObject.put("XQSQR", bean.getCreator());
		 * jsObject.put("HUIJKM", bean.getHuijkm()); jsObject.put("CHENGBZX",
		 * bean.getChengbzx()); // 遍历list集合 for (int i = 0; i < ls.size(); i++)
		 * { Diaobsqmx mx = ls.get(i); jsArray1.add("" + (i + 1));
		 * jsArray2.add("" + mx.getLux()); jsArray3.add("" + mx.getLingjbh());
		 * jsArray4.add("" + mx.getLingjmc()); jsArray5.add("" +
		 * mx.getShenbsl()); } jsObject.put("XUH", jsArray1);
		 * jsObject.put("LUX", jsArray2); jsObject.put("LINGJBH", jsArray3);
		 * jsObject.put("LINGJMC", jsArray4); jsObject.put("SHENBSL", jsArray5);
		 * jsObject.put("YAOHSJ", jsArray5);
		 * 
		 * JSONArray jsArray = new JSONArray(); jsArray.add(jsObject); // 转换数据格式
		 * String json = jsArray.toString(); log.info(json); }
		 */
		setResult("json", dfpvdiaobsqService.dbsqprint(sqlist));
		 return AJAX;
	 }

//	public String getLjck() {
//		setResult("result", xLingjckService.querySingle(getParams()));
//		return AJAX;
//	}
//	
	
	public String getKcsl() {
		Diaobsqmx  mxBean = new Diaobsqmx(); 
		Map<String, String> map = getParams();
		
		mxBean.setUsercenter(map.get("usercenter"));
		mxBean.setCangkbh(map.get("cangkbh")); 
		mxBean.setLingjbh(map.get("lingjbh")); 
		
		setResult("result", dfpvdiaobsqService.queryKcsl(mxBean));
		return AJAX;
	}
	
	
	
	/**
	 * <p>
	 * 导入文件
	 * </p>
	 * 
	 * @author wuyichao
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String uploadDiaobsq() throws IOException 
	{
		
		log.debug("==============调拨单申请导入文件开始===============");
		//其他导入数据
		Map<String , String> paramMap = this.getParams();
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		String message = null;
		
		OutputStream output = null;
		reqResponse.setContentType("text/html");
		reqResponse.setCharacterEncoding("UTF-8");
		
		output = reqResponse.getOutputStream() ;
				
		String flagBeiz = paramMap.get("beiz");
		//0010605备注字段英文双引号转换为中文双引号 gswang 2014-10-31
		flagBeiz = flagBeiz.replace("\"", "”");
		paramMap.put("beiz",flagBeiz);
		LoginUser loginUser = getUserInfo();
		String username = "";
		String time = com.athena.xqjs.module.common.CommonFun.getJavaTime();
		paramMap.put("time", time);
		if(null != loginUser)
		{
			username = loginUser.getUsername();
			paramMap.put("username", username);
			//paramMap.put("usercenter", loginUser.getUsercenter());
		}
		if(StringUtils.isNotBlank(flagBeiz))
		{
			int length = flagBeiz.length();
			if(length > 50)
			{
				message = "<script>parent.callback('备注信息字符数量超过50,请调整!')</script>";
				if (output != null) 
				{
					output.write(message.getBytes("UTF-8"));
					output.flush();
					output.close();
				}
				return AJAX;
			}
		}
		else
		{
			StringBuffer flagSb = new StringBuffer();
			flagSb.append("在时间为:").append(time).append("由").append(username).append("导入调拨单信息") ;//,文件路径为:").append(fullFilePath);
			paramMap.put("beiz", flagSb.toString());
		}
		
	
		//存储文件名称
		String fullFilePath = "";
		//备注
		String result = null;
		
		
		//存储文件根目录
		String saveLj = System.getProperty("user.home")+File.separator+"tmp"; 
		
		RequestContext requestContext = new ServletRequestContext(req); 
		//是否有上传文件
		if (FileUpload.isMultipartContent(requestContext)) 
		{
			try 
			{
				FileItemFactory factory = new DiskFileItemFactory();
				//上传主件
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(40000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);
				
				for (FileItem item : fileItems) 
				{
					//如果不是文本项，则进行文件传输
					if (!item.isFormField()) {
						File fullFile = new File(saveLj);
						if (!fullFile.exists()) 
						{
							fullFile.mkdirs();
						}
						// 文件名称
						fullFilePath = saveLj + File.separator+String.valueOf(new Date().getTime())+".xls";
						log.info("上传文件路径："+fullFilePath);
						File fileOnServer = new File(fullFilePath); 
						item.write(fileOnServer); 
						log.info("上传文件成功,路径："+fullFilePath);
					}
				}
				
			} 
			catch (Exception e) 
			{
				log.info(e.getMessage());
			}
		}
		
		ArrayList<Diaobsqmx> datasretu=new ArrayList<Diaobsqmx>();
		String filename = "";
		try {
			IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
					ExportConstants.FILE_XLS);
			log.info(fullFilePath);

			 datasretu = dfpvdiaobsqService.importData(fullFilePath,paramMap);
			//如果返回集合是空，说明提交成功。如果有值，说明有错误信息，需要返回。
			if(datasretu==null || datasretu.size()<=0){
				message = "<script>parent.callback('导入成功!')</script>";
			}else{
						Map<String, Object> dataSource = new HashMap<String, Object>();
						dataSource.put("count", datasretu.size());
						 filename = String.valueOf(new Date().getTime());
						fullFilePath = saveLj +File.separator+ filename+".xls";

						OutputStream out = new FileOutputStream(fullFilePath);
						
						// 模版head
						out.write(prossData.processByteCache("dblmuban_head.ftl", dataSource));
						
						// 模版body					
							dataSource.put("list", datasretu);// 数据查询
						out.write(prossData.processByteCache("dblsc_body.ftl", dataSource));
						
						// 模版foot
						out.write(prossData.processByteCache("dblmuban_foot.ftl", dataSource));
						
						out.flush();
						out.close();
						
						message = "<script>parent.callbackdow('导入数据有问题，请修改!','"+filename+"')</script>";
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
				throw new ServiceException(ex.getMessage());
			} 
			if (output != null) 
			{
				output.write(message.getBytes("UTF-8"));
				output.flush();
				output.close();
			}
			return AJAX;
	}
	
	private Map<String,Lingj> translateListToMap(List<Lingj> lingjs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, Lingj> result = new HashMap<String, Lingj>();
		Lingj lingj = null;
		if(null != lingjs && lingjs.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=lingjs.size();i<j;i++) 
			{
				lingj = lingjs.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(lingj, propertys[k]));
				}
				result.put(flagKey.toString(), lingj);
			}
		}
		return result;
	}
	
	/**
	 * 模板导出
	 * 
	 */
	public String expMuban() {

		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
				ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "DBSQMUBAN", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			Map<String, Object> dataSource = new HashMap<String, Object>();

			dataSource.put("count", 0);
			// 模版head
			output.write(prossData.processByteCache("dblmuban_head.ftl", dataSource));
			output.flush();
			// 模版body
			output.write(prossData.processByteCache("dblmuban_body.ftl", dataSource));
			// 模版foot
			output.write(prossData.processByteCache("dblmuban_foot.ftl", dataSource));
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ServiceException(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new ServiceException(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException(e.getMessage());
			}
		}
		return "downLoad";
	}
	
	private void exportFileBefor(HttpServletResponse response, String filePrefix, String fileSuffix) {
		// 文件 名
		String downLoad = "";
		if (filePrefix != null) {
			try {
				downLoad = URLEncoder.encode(filePrefix, ExportConstants.CODE_UTF8);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e.getMessage());
			}
		}
		// 设置文件名
		StringBuffer buf = new StringBuffer();
		buf.append(ExportConstants.ATTACHMENT);
		buf.append("\"");
		buf.append(downLoad);
		buf.append("_");
		buf.append(DateUtil.getCurrentDate());
		buf.append(fileSuffix);
		buf.append("\"");

		response.setContentType(ExportConstants.CONTEXT_TYPE);
		response.setCharacterEncoding(ExportConstants.CODE_UTF8);
		response.setHeader(ExportConstants.CONTEXT_DISPOSITION, buf.toString());

	}
	
	public String errdownLoad() {
		Map<String, String> map = getParams();
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
				ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "DBSQ"+map.get("name"), ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			//存储文件根目录
			String saveLj = System.getProperty("user.home")+File.separator+"tmp"; 
			String fullFilePath = saveLj +File.separator+ map.get("name")+".xls";
			InputStream fis  = new FileInputStream(new File(fullFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            
            output = new BufferedOutputStream(response.getOutputStream());
            output.write(buffer);
            output.flush();
            output.close();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ServiceException(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new ServiceException(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException(e.getMessage());
			}
		}
		return "downLoad";
	}	
}

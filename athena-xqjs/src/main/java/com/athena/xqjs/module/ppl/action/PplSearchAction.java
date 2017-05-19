package com.athena.xqjs.module.ppl.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.entity.ppl.ComparePpl;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.entity.ppl.Niandygmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.ppl.service.NiandygService;
import com.athena.xqjs.module.ppl.service.NiandygmxService;
import com.athena.xqjs.module.ppl.service.PplCompareSendService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：PplSearchAction
 * <p>
 * 类描述：PPL预告查询及发送action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2011-12-28
 * </p>
 * 
 * @version 1.0
 * 
 */   
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PplSearchAction extends ActionSupport {

	@Inject
	private NiandygService ns;//年度预告服务类
	
	@Inject
	private NiandygmxService nmxs;//年度预告明细服务类
	 
	@Inject
	private PplCompareSendService pplCompareSendService;
	
	@Inject
	private UserOperLog userOperLog;

	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * getUserInfo获取用户信息方法
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}  
	
	/**
	 * PPL预告查询及发送页面初始化
	 * @return PPL预告查询及发送页面
	 */
	public String init(){
		return "pplcx";
	}
	
	/**
	 * PPL预告查询
	 * @return 查询结果
	 */
	public String searchNdPpl(@Param Niandyg ndyg){
		setResult("result", ns.select(ndyg, getParams()));
		return AJAX;
	}
	
	/**
	 * 查询PPL年度预告详细信息
	 * @return PPL年度预告详细信息
	 */
	public String initNdppl(@Param Niandyg ndyg){
		String forward = "pplInfo";
		setResult("usercenter", getUserInfo().getUsercenter());
		setResult("pplbc", ndyg.getPplbc());//设置PPL版次
		setResult("zhuangt", ndyg.getZhuangt());//设置状态
		setResult("p0xqzq", ndyg.getJisnf()+"01");//p0需求周期
		setResult("removeId", getParam("removeId"));
		if (ndyg.getZhuangt().equals(1)) {
			forward = "pplInfoSx";
		}
		return forward;
	}
	
	/**
	 * 查询年度PPL预告明细
	 * @param ndygmx 查询信息
	 * @return 查询结果
	 */
	public String searchNdPplMx(@Param Niandygmx ndygmx){
		setResult("result",nmxs.select(ndygmx, getParams()));	
		return AJAX;
	}
	/**
	 * 查询计划员组
	 * 
	 */
	public   String selectJihy(){
		setResult("result", ns.selectjihyz(getParams()));
		return AJAX;
		
	}
	/**
	 * 查询需求版次
	 */
   public String  selectXuqbc(){
	   setResult("result", ns.selectxuqbc());
		return AJAX;
   }
	/**
	 * 保存备注方法
	 * @param ndyg 备注信息
	 * @return 保存结果
	 */
	public String saveBeiz(@Param("ndyg") Niandyg ndyg){
		String result = "备注保存失败";
		ndyg.setNeweditor(getUserInfo().getUsername());
		ndyg.setEdittime(CommonFun.getJavaTime());
		try {
			ns.doUpdate(ndyg);//没有抛异常则表示更新成功
			result = "备注保存成功";
		} catch (Exception e) {
			
		}
		setResult("result", result);
		return AJAX;
	}
	
	/**
	 * 生效
	 * @return 生效结果
	 */
	public String shengX(@Param("ndyg") Niandyg ndyg){
		String result = "";
		ndyg.setNeweditor("edit");
		ndyg.setEdittime(CommonFun.getJavaTime());
		if (ndyg.getZhuangt()==1) {
			setResult("result", "不能对已生效的进行再操作！");
			return AJAX;
		}
		try {
			ndyg.setZhuangt(Const.NIANDYG_ZHUANGT_SX);//生效设置状态为01
			ns.doUpdate(ndyg);
		} catch (Exception e) {
			result = MessageConst.UPDATE_COUNT_0;
			setResult("result", result);
			return AJAX;
		}
		result = "生效成功";
		setResult("result", result);
		return AJAX;
	}
	
	/**
	 * 比较
	 * @return 比较页面
	 */
	public String biJ(@Param("pplbc1") String pplbc1, @Param("pplbc2") String pplbc2) {
		String foward = "pplBiJ";
		setResult("pplbc1", pplbc1);
		setResult("pplbc2", pplbc2);
		setResult("removeId", getParam("removeId"));
		return foward; 
	}
	
	/**
	 * 比较结果明细
	 * @return 比较结果明细 
	 */
	public String bjJMx(@Param Niandyg bean) {
		//比较数据
		Map<String, Object> result = nmxs.compare(getParams(), bean);
		 
		List<ComparePpl>  comparers = (List<ComparePpl>)(result.get("rows")); 
		 
 		
		for(int i =0; i<comparers.size(); i++){
			ComparePpl cycs = comparers.get(i); 
			
			if(cycs.getGongys()==null && cycs.getZhizlx()==null ){
				cycs.setGongys(" ");
			}else if( cycs.getGongys()==null && cycs.getZhizlx().equals("97W") ){
				cycs.setGongys("M000000000");
			}else if ( cycs.getGongys()==null && cycs.getZhizlx().equals("97D") ){
				cycs.setGongys("5200000000");
			}else if(cycs.getGongys()==null && cycs.getZhizlx().equals("97X")   ){
				cycs.setGongys("7800000000");
			}else if(cycs.getGongys()==null && cycs.getZhizlx().equals("UGB") ){
				cycs.setGongys("M000000000");
			}
			
			if( cycs.getGcbh()==null && cycs.getZhizlx()==null  ){
				cycs.setGcbh(" ");
			}else if( cycs.getGcbh()==null && cycs.getZhizlx().equals("97W")  ){
				cycs.setGcbh("M000000000");
			}else if ( cycs.getGcbh()==null && cycs.getZhizlx().equals("97D") ){
				cycs.setGcbh("5200000000");
			}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("97X") ){
				cycs.setGcbh("7800000000");
			}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("UGB") ){
				cycs.setGcbh("M000000000");
			}
			
		} 
		 
		setResult("result",result);
		return AJAX;
	}
	
	/**
	 * 保存明细
	 * @return 保存结果
	 */ 
	public String saveNdPplMx(@Param("insert") ArrayList<Niandygmx> insert, @Param("edit") ArrayList<Niandygmx> edit, @Param("delete") ArrayList<Niandygmx> delete) {
		String result = "操作失败！";
		try {
			String pplbc = getParam("pplbc");//取PPL版次
			String p0xqzq = getParam("p0xqzq");//获取p0需求周期
			String user = getUserInfo().getUsername();
			boolean bl = nmxs.saveMx(insert, edit, delete,user,pplbc,p0xqzq);
			if(bl){
				result = "操作成功";
			}
		} catch (Exception e) {
			result = MessageConst.UPDATE_COUNT_0;
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "PPL年度预告明细", "PPL年度预告明细-插入：" + insert.size() + "修改：" + edit.size() + "删除：" + delete.size());
		setResult("result", result);
		return AJAX;
	}
	
	/**
	 * 导出文本
	 * @return
	 * @throws IOException 
	 */
	public String exportTxt(){
		String fileurl = null;
		try {
			fileurl = ns.writeTxt(new Niandygmx(), getParams());
		} catch (Exception e) {
			fileurl = "导出TXT文本出错";
			log.info(e.getMessage());

		}
		setResult("result", fileurl);
		return AJAX;
	}

	/**
	 * 导出文本
	 * 
	 * @return
	 * @throws IOException
	 */
	public String downLoadTxt() {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse resp = ActionContext.getActionContext().getResponse();
		resp.reset();
		PrintWriter os = null;
		BufferedReader fis = null;
		try {
			resp.setCharacterEncoding("UTF-8");
			req.setCharacterEncoding("UTF-8");
			resp.setContentType("application/force-download");
			resp.setContentType("application/x-download");
			String fname = req.getParameter("fileName");

			resp.setContentLength(40000000);
			if (fname != null) {
				String file = fname;
				if (!(new File(file)).exists()) {
					return AJAX;
				}
				os = resp.getWriter();
				resp.setHeader("content-disposition", "attachment;filename=" + new String(fname.getBytes("GBK"), "ISO-8859-1"));
				resp.setContentType("application/octet-stream");// 八进制流 与文件类型无关
				char temp[] = new char[1024];
				fis = new BufferedReader(new FileReader(new File(file)));
				int n = 0;
				while ((n = fis.read(temp)) != -1) {
					os.write(temp, 0, n);

					os.flush();
				}
			}
		} catch (Exception e) {

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}

		return AJAX;
	}
	
	//相同的版次、基准版次对比结果 是否已经发送给外部系统
	public String maoxqSffs(@Param CompareCyc bean) { 
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("xuqbc", getParams().get("pplbc1"));
		map.put("xuqbc1", getParams().get("pplbc1")); 
		map.put("xuqbc2", getParams().get("pplbc2")); 
		 
		boolean flag = false; 
		
		flag = pplCompareSendService.maoxqSffs(map);
		
		if(flag){
			setResult("flag", "已经发送过相同的需求版次对比数据，不能再次发送！"); 
			return AJAX;
		}
		 
		return AJAX;
	}
	
	//xss 20161010 v4_008
	public String SendComparePage(@Param Niandyg bean) { 
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("xuqbc1", getParams().get("pplbc1"));  
		map.put("xuqbc2", getParams().get("pplbc2"));  
		map.put("jiz", getParams().get("pplbc1")); 
		
		getParams().put("exportEfi", "exportEfi"); 
		getParams().put("pageSize", "99999999"); 
		getParams().put("pageNo", "1"); 
		
		String  result = "失败！";
		String  biaos= getParams().get("biaos");
		boolean flag = false; //插入数据成功 
		boolean flag_delete = false;//删除标识；
		  		
		try {
				if(biaos.equals("1")){					
					Map<String, Object> comparer = nmxs.compare(getParams(), bean);
					//插入数据库表  
					
					List<ComparePpl>  comparers = (List<ComparePpl>)(comparer.get("rows")); 
					
					for(int i =0; i<comparers.size(); i++){
						ComparePpl cycs = comparers.get(i); 
						
						if(cycs.getGongys()==null && cycs.getZhizlx()==null ){
							cycs.setGongys(" ");
						}else if( cycs.getGongys()==null && cycs.getZhizlx().equals("97W") ){
							cycs.setGongys("M000000000");
						}else if ( cycs.getGongys()==null && cycs.getZhizlx().equals("97D") ){
							cycs.setGongys("5200000000");
						}else if(cycs.getGongys()==null && cycs.getZhizlx().equals("97X")  ){
							cycs.setGongys("78001H  P2");
						}else if(cycs.getGongys()==null && cycs.getZhizlx().equals("UGB")  ){
							cycs.setGongys("M000000000");
						}
						

						if( cycs.getGcbh()==null && cycs.getZhizlx()==null ){
							cycs.setGcbh(" ");
						}else if( cycs.getGcbh()==null && cycs.getZhizlx().equals("97W") ){
							cycs.setGcbh("M000000000");
						}else if ( cycs.getGcbh()==null && cycs.getZhizlx().equals("97D") ){
							cycs.setGcbh("5200000000");
						}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("97X")  ){
							cycs.setGcbh("78001H  P2");
						}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("UGB") ){
							cycs.setGcbh("M000000000");
						}
						
					} 
					
					//如果存在相同的版次1、版次2、基准版次， 就删除原来的数据 
					boolean flag_exists = false;
						
				    flag_exists = pplCompareSendService.queryXuqbcJizExists(map);
				    
					if(flag_exists){//删除原来存在的相同数据
						flag_delete =  pplCompareSendService.doDeleteXuqbcJizExists(map); 
					}
					  
						 flag = pplCompareSendService.insertCompareQr(comparers, map , getUserInfo().getUsername(), CommonFun.getJavaTime());		
 				 		 
				} 
		} catch (Exception e) {
			// e.printStackTrace();
		}
		
		if (flag) {
			result = "导出数据成功！";
		}
		setResult("result", result);
		return "success";
	}
}

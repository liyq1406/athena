package com.athena.xqjs.module.diaobl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DiaobsqService
 * <p>
 * 类描述：调拨令申请业务类
 * </p>
 * 修改人：Niesy
 * <p>
 * 修改时间：2011-12-1
 * </p>
 * 修改备注：
 * <p>
 * 
 * @version </p>
 */
@Component
public class DiaobsqService extends BaseService {
	// LOG4J日志打印信息
	private final Log log = LogFactory.getLog(getClass());
	@Inject
	private com.athena.xqjs.module.common.service.NianxmService nianxmService;

	@Inject
	private DiaobsqOperationService diaobsqOperationService;
	
	@Inject
	private LingjService lingjService;

	/**
	 * 调拨申请 查询单条数据
	 */
	public Diaobsq selectDiaobsq(Diaobsq bean) {

		return (Diaobsq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.test_selectDiaobsq", bean);
	}

	/**
	 * 调拨申请service
	 * 
	 * @author Niesy
	 * @date 2011-11-21
	 * 
	 * @return String
	 */
	public String getdiaosqdh(String usercenter) {
		// 取得年型
		String year = nianxmService.getYear(usercenter);
		// 实力化Calendar类对象
		Calendar calendar = new GregorianCalendar();
		// 获取当前月
		int month = calendar.get(Calendar.MONTH) + 1;
		String mon = null;
		// 月份转换为16进制
		mon = Integer.toHexString(month).toUpperCase();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String daystr = String.format("%02d", day);
		// 前5的调拨申请单号
		String dh = "S" + year + mon + daystr;
		// 查询调拨申请表里是否有传入的这个调拨单号
		String diaobsqdh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.querydiaobsqdh", dh);
		if (null == diaobsqdh) {
			diaobsqdh = dh + "001";
		} else {
			// 截取今天已有的最大调拨申请单号的后3位流水号
			String str = diaobsqdh.substring(5, 8);
			// 流水号加1，如001，变成002
			int i = Integer.parseInt(str) + 1;
			// 补0
			String str1 = String.format("%03d", i);
			// 截取前五位
			String top5 = diaobsqdh.substring(0, 5);
			// 生成新的调拨申请单号
			diaobsqdh = top5 + str1;
		}
		return diaobsqdh;
	}

	/**
	 * 
	 * 查询零件名称
	 * 
	 * @author Niesy
	 * @date 2011-11-22
	 * @param Map
	 *            <String, String>
	 * 
	 * @return String
	 * @edit Niesy
	 */
	public Diaobsqmx selectLingjmc(Map<String, String> map) {
		Diaobsqmx sqmx = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.querylingjmc", map);
		if (sqmx == null) {
			throw new ServiceException("您的输入有误！");
		}
		return sqmx;
	}

	/**
	 * 
	 * 插入调拨申请主表
	 * 
	 * @author Niesy
	 * @date 2011-11-22
	 * @param Diaobsq
	 * 
	 * @return boolean
	 * @edit Niesy
	 */
	public int doInsert(Diaobsq bean) {
		bean.setZhuangt(Const.DIAOBL_ZT_APPLYING);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.insertdiaobsq", bean);
	}

	/**
	 * 
	 * 插入调拨申请子表--调拨申请明细表
	 * 
	 * @author Niesy
	 * @date 2011-11-22
	 * @return boolean
	 * @edit Niesy
	 */
	public int doInsertmx(Diaobsqmx bean) {
		// 序号加1
		Integer xuh = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.select_sqxuh", bean);
		// 序号为空，初始为零
		int xuH = xuh == null ? 0 : xuh;
		bean.setXuh(xuH + 1);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.insertdiaobsqmx", bean);
	}

	/**
	 * 
	 * 合并方法 调拨申请新增
	 * 
	 * @author Niesy
	 * @date 2011-11-26
	 * @param Diaobsq
	 * @param List
	 *            <Diaobsqmx>
	 * 
	 * @return flag
	 */
	@Transactional
	public Boolean sqInsert(List<Diaobsq> sqlist, List<Diaobsqmx> ls) {
		boolean flag = true;
		for (Diaobsq dbsq : sqlist) {
			this.doInsert(dbsq);
		}
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx sqmx = ls.get(i);
			this.doInsertmx(sqmx);

		}
		return flag;
	}

	/**
	 * 调拨申请打印
	 * 
	 * @param sqlist
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String dbsqprint(List<Diaobsq> sqlist) {

		// 获取用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		Map<String, String> u_map = new HashMap<String, String>();
		u_map.put("loginname", loginUser.getUsername());
		List u_list = (ArrayList) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.queryUserByLoginname", u_map);

		// json数组
		JSONArray ar = new JSONArray();
		// json对象
		JSONObject jsonObject = null;
		for (Diaobsq diaobsq : sqlist) {
			// 获取调拨申请
			Diaobsq bean = this.selectDiaobsq(diaobsq);
			diaobsqOperationService.updateBanc(bean);
			// 时间格式yyyy-mm-dd
			bean.setDiaobsqsj(bean.getDiaobsqsj().substring(0, 10));
			// 获取申请明细
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", bean.getUsercenter());
			map.put("diaobsqdh", bean.getDiaobsqdh());
			ArrayList<Diaobsqmx> sqmxls = (ArrayList<Diaobsqmx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.select_Diaobsqmx", map);
			jsonObject = new JSONObject();
			jsonObject.put("USERCENTER", bean.getUsercenter());
			jsonObject.put("DIAOBSQDH", bean.getDiaobsqdh());
			jsonObject.put("DIAOBSQSJ", bean.getDiaobsqsj());
			jsonObject.put("XQSQR", bean.getCreator());
			jsonObject.put("HUIJKM", bean.getHuijkm());
			jsonObject.put("CHENGBZX", bean.getChengbzx());
			jsonObject.put("BEIZ", bean.getBeiz());
			// 设置用户信息
			if (u_list.size() == 1) {
				Map<String, String> u_rmap = (Map<String, String>) u_list.get(0);
				jsonObject.put("NAME", u_rmap.get("NAME"));
				jsonObject.put("LOGINNAME", u_rmap.get("LOGINNAME"));
				jsonObject.put("CELLPHONE", u_rmap.get("CELLPHONE"));
				jsonObject.put("OFFICEPHONE", u_rmap.get("OFFICEPHONE"));
				jsonObject.put("FAMILYPHONE", u_rmap.get("FAMILYPHONE"));
			}
			int rowCount = sqmxls.size();
			// 每页显示数
			int pageSize = 20;
			// 总页数
			int totalPage = (rowCount - 1) / pageSize + 1;
			for (int i = 1; i <= totalPage; i++) {
				// 开始行
				int pageEndRow;
				// 结束行
				int pageStartRow;
				// 大于总行数
				if (i == totalPage) {
					pageEndRow = pageSize * i;
					pageStartRow = pageSize * (totalPage - 1);
				} else if (i * pageSize < rowCount) {
					pageEndRow = i * pageSize;
					pageStartRow = pageEndRow - pageSize;
				} else {
					pageEndRow = rowCount;
					pageStartRow = pageSize * (totalPage - 1);
				}
				for (int j = pageStartRow; j < pageEndRow; j++) {
					// 每页序号从1到8开始
					int temp = j % pageSize + 1;
					if (j >= sqmxls.size()) {
						jsonObject.put("XUH" + temp, "");
						jsonObject.put("LUX" + temp, "");
						jsonObject.put("LINGJBH" + temp, "");
						jsonObject.put("LINGJMC" + temp, "");
						jsonObject.put("YAOHSJ" + temp, "");
						jsonObject.put("SHENBSL" + temp, "");
					} else {
						Diaobsqmx sqmx = sqmxls.get(j);
						jsonObject.put("XUH" + temp, j + 1);
						jsonObject.put("LUX" + temp, sqmx.getLux());
						jsonObject.put("LINGJBH" + temp, sqmx.getLingjbh());
						jsonObject.put("LINGJMC" + temp, sqmx.getLingjmc());
						jsonObject.put("YAOHSJ" + temp, sqmx.getYaohsj());
						jsonObject.put("SHENBSL" + temp, sqmx.getShenbsl());
					}

				}
				// 将jons对象放到数组里
				ar.add(jsonObject);
			}
		}
		return ar.toString();
	}

	/**
	 * @auth  wuyichao
	 * @see   导入文件
	 * @param fullFilePath
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Transactional
	public ArrayList<Diaobsqmx> importData(String fullFilePath, Map<String, String> paramMap) throws BiffException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException 
	{
		String result = "导入成功";
		//得到文件数据
		ArrayList<Diaobsqmx> datas =  (ArrayList<Diaobsqmx>) readExecl(fullFilePath);
		List<Lingj> lingjs = this.queryList(paramMap);
		List<Lingj> lingjsZh = this.queryListZh(paramMap);
		List<String> dinghlxs = this.queryDinglx();
		Map<String, Lingj> lingjMap = translateListToMap(lingjs, "lingjbh");
		Map<String, Lingj> lingjZhMap = translateListToMap(lingjsZh,"usercenter","lingjbh","zhizlx" );
		Map<String,String> dinghlxMap = new HashMap<String, String>();
		for (String string : dinghlxs) 
		{
			dinghlxMap.put(string, string);
		}
		int index = 2;
		//得到调拨申请其他的信息
		String usercenter = paramMap.get("usercenter");
		String username = paramMap.get("username");
		String chengbzx = paramMap.get("chengbzx");
		String huijkm = paramMap.get("huijkm");
		String time = paramMap.get("time");
		String beiz = paramMap.get("beiz");
		//初始化调拨单
		String  diaobsqdh = getdiaosqdh(usercenter);
		String secondOrderNum = diaobsqdh.substring(0, 5) + String.format("%03d", Integer.parseInt(diaobsqdh.substring(5, 8)) + 1);
		Diaobsq ilDiaobsq = new Diaobsq();
		ilDiaobsq.setUsercenter(usercenter);
		ilDiaobsq.setDiaobsqdh(diaobsqdh);
		ilDiaobsq.setChengbzx(chengbzx);
		ilDiaobsq.setHuijkm(huijkm);
		ilDiaobsq.setActive(Const.ACTIVE_1);
		ilDiaobsq.setBanc("0001");
		ilDiaobsq.setDiaobsqsj(time.substring(0,10));
		ilDiaobsq.setCreator(username);
		ilDiaobsq.setCreate_time(time);
		ilDiaobsq.setEditor(username);
		ilDiaobsq.setBeiz(beiz);
		ilDiaobsq.setEdit_time(time);
		Diaobsq kdDiaobsq = (Diaobsq) BeanUtilsBean.getInstance().cloneBean(ilDiaobsq);
		kdDiaobsq.setDiaobsqdh(secondOrderNum);
		
		//初始化调拨单申请明细
		Map<String, String> cachMap = new HashMap<String, String>();
		List<Diaobsq>   diaobsqs = new ArrayList<Diaobsq>();
		int il = 0,kd = 0;
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Diaobsqmx> wrongdatas =  new ArrayList<Diaobsqmx>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Diaobsqmx> datasnew =  new ArrayList<Diaobsqmx>();
		for (int i=0;i<datas.size();i++) 
		{
			StringBuffer wronginfo=new StringBuffer("");
			Diaobsqmx diaobsqmx=datas.get(i);
			if( null != cachMap.get(diaobsqmx.getLingjbh()))
			{
				//result = resultMessage(index,"零件编号为:" + diaobsqmx.getLingjbh() + "有重复数据,重复零件号在第"+ cachMap.get(diaobsqmx.getLingjbh()) + "行,请调整!");
				result = resultMessage(index,"有重复数据,重复零件号在第"+ cachMap.get(diaobsqmx.getLingjbh()) + "行,请调整!");
				//return result;
				wronginfo=wronginfo.append(result);
			}
			Lingj lingj = null;
			//查看在目标用户中心内是否有传入的零件信息
			if(StringUtils.isNotBlank(diaobsqmx.getLingjbh()))
			{
				lingj = lingjMap.get(diaobsqmx.getLingjbh());
			}
			if(null == lingj)
			{
				//result = resultMessage(index,"用户中心:" + usercenter + "中未找到零件编号为:" + diaobsqmx.getLingjbh() + "的零件,请检查零件编号是否输入正确!");
				result = resultMessage(index,"未找到零件编号为:" + diaobsqmx.getLingjbh() + "的零件,请检查零件编号是否输入正确!");
				//return result;
				wronginfo=wronginfo.append(result);
			}else{
				//xh 2015-07-28调拨制造路线手工输入校验制造路线
				if(StringUtils.isBlank(diaobsqmx.getLux()) || lingj ==null )
				{
					//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的制造路线有误,应为" + lingj.getZhizlx() + "!");
					result = resultMessage(index,"制造路线有误!");
					//return result;
					wronginfo=wronginfo.append(result);
				}
				else
				{       Map map=new HashMap();
				        map.put("usercenter", usercenter);
				        map.put("lingjbh", lingj.getLingjbh());
				        map.put("lux", diaobsqmx.getLux());
				        Integer num =  (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.querylucz", map);
						if(num == 0){
							result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的订货路线有误!");
							//result = resultMessage(index,"订货路线有误!");
							wronginfo=wronginfo.append(result);
							//diaobsqmx.setLux(lingj.getZhizlx());
						}
						/*if(null == dinghlxMap.get(diaobsqmx.getLux()))
						{
							//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的订货路线有误!");
							result = resultMessage(index,"订货路线有误!");
							//return result;
							wronginfo=wronginfo.append(result);
						}*/
				}
			}
			if(StringUtils.isBlank(diaobsqmx.getTempShenbsl()) || !StringUtils.isNumeric(diaobsqmx.getTempShenbsl()))
			{
				//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的申报数量有误,应为正整数!");
				result = resultMessage(index,"申报数量有误,应为正整数!");
				//return result;
				wronginfo=wronginfo.append(result);
			}
			if(StringUtils.isBlank(diaobsqmx.getYaohsj()) || !isDate(diaobsqmx.getYaohsj()))
			{
				//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的要货时间有误,应为如下格式   2013-10-23 !");
				result = resultMessage(index,"要货时间有误,应为如下格式   2013-10-23 !");
				//return result;
				wronginfo=wronginfo.append(result);
			}
			else
			{
				String yaohsj = diaobsqmx.getYaohsj();
				String nowTime = time.substring(0, 10);
				if (yaohsj.compareTo(nowTime) < 0) 
				{
					//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的要货时间有误,要货时间不能小于当前时间!");
					result = resultMessage(index,"要货时间有误,要货时间不能小于当前时间!");
					//return result;
					wronginfo=wronginfo.append(result);
				}
			}
			
			BigDecimal bigDecimal = new BigDecimal(diaobsqmx.getTempShenbsl());
			diaobsqmx.setShenbsl(bigDecimal);
			diaobsqmx.setUsercenter(usercenter);
			diaobsqmx.setZhuangt(Const.DIAOBL_ZT_APPLYING);
			diaobsqmx.setCreator(username);
			diaobsqmx.setCreate_time(time);
			diaobsqmx.setEditor(username);
			diaobsqmx.setEdit_time(time);
			
			if(diaobsqmx.getLux().equalsIgnoreCase(Const.ZHIZAOLUXIAN_KD_PSA) || diaobsqmx.getLux().equals(Const.ZHIZAOLUXIAN_KD_AIXIN))
			{
				diaobsqmx.setDiaobsqdh(kdDiaobsq.getDiaobsqdh());
				kd ++;
			}
			else
			{
				diaobsqmx.setDiaobsqdh(ilDiaobsq.getDiaobsqdh());
				il ++;
			}
			cachMap.put(diaobsqmx.getLingjbh(), index + "");
			index ++;
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wrongcoun=wrongcoun+1;
				diaobsqmx.setTishi(wronginfo.toString());
				wrongdatas.add(diaobsqmx);
			}
			datasnew.add(diaobsqmx);
		}
		//如果没有错误信息，正常插入，如果有则需要返回
		if(wrongdatas.size()<=0){
			if(kd > 0)
			{
				diaobsqs.add(kdDiaobsq);
			}
			if(il > 0)
			{
				diaobsqs.add(ilDiaobsq);
			}
			if(diaobsqs.size() > 0)
			{
				sqInsert(diaobsqs, datas);
			}
			ArrayList<Diaobsqmx> datasnull =  new ArrayList<Diaobsqmx>();
			return datasnull;
		}else{
			return datasnew;
		}
	}

	private boolean isDate(String yaohsj) 
	{
		// 读取时间格式，验证需求日期
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			sf.parse(yaohsj);
		} catch (ParseException e) 
		{
			log.info("调拨导入时间错误："+e.getMessage());
			return false;
		}
		return true;
	}

	private String resultMessage(int index, String message) 
	{
		StringBuffer flagSb = new StringBuffer();
		flagSb.append("导入文件中第").append(index).append("行,").append(message);
		return flagSb.toString();
	}

	/**
	 * @see   将excel内的数据组装成一个调拨单申请明细的集合
	 * @param fullFilePath
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	private List<Diaobsqmx> readExecl(String fullFilePath) throws BiffException, IOException 
	{
		List<Diaobsqmx> datas = new ArrayList<Diaobsqmx>();
		// 读入文件流
		InputStream is = new FileInputStream(new File(fullFilePath));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);	
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		// 行数、列数
		int rows = sheet.getRows();
		int columns = sheet.getColumns();
		
		Diaobsqmx diaobsqmx = null;
		String sheetString = null;
		for (int i = 1; i < rows; i++) 
		{
			diaobsqmx = new Diaobsqmx();
			for (int j = 0; j < columns; j++) 
			{
				sheetString = sheet.getCell(j, i).getContents().trim();
				if( j == 0)
				{
					diaobsqmx.setLingjbh(sheetString);
				}
				else if( j == 1)
				{
					diaobsqmx.setLux(sheetString);
				}
				else if( j == 2)
				{
					diaobsqmx.setTempShenbsl(sheetString);
				}
				else if( j == 3)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
					log.info("调拨导入第"+i+"行时间："+sheetString);
					sheetString = sheetString.replace("/", "-");
			    	try 
			    	{
			    		sheetString = CommonFun.getJavaTime(formatter.parse(sheetString));
					} 
			    	catch (ParseException e)
			    	{
			    		sheetString = sheet.getCell(j, i).getContents().trim();
					}
					diaobsqmx.setYaohsj(sheetString);
					log.info("调拨导入第"+i+"行时间1："+sheetString);
				}
			}
			datas.add(diaobsqmx);
		}
		return datas;
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
	 * @author wuyichao
	 * @see  按条件查询零件
	 * @param paramMap
	 * @return
	 */
	public List<Lingj> queryList(Map<String, String> paramMap)
	{
		paramMap.put("biaos", Const.SHENGXIAO);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.queryLingj", paramMap);
	}
	public List<Lingj> queryListZh(Map<String, String> paramMap)
	{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.queryDinghlxzhs", paramMap);
	}
	
	private List<String> queryDinglx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.queryDinghlxs");
	}
	
	/**
	 * 查询零件制造路线是否存在
	 * @author xh
	 * @date 2014-07-22
	 * @param Map<String, String>
	 * @return String
	 */
	public int querylucz(Map<String, String> map) {
		Integer num =  (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.querylucz", map);
		if (num == 0) {
			throw new ServiceException("您的输入制造路线有误！");
		}
		return num;
	}
	
}

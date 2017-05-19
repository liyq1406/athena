package com.athena.ckx.module.xuqjs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.Fenpq;

import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.core3.util.Assert;

/**
 * 工艺消耗点
 * @author denggq
 * 2012-4-17
 */
@Component
public class CkxGongyxhdService extends BaseService<CkxGongyxhd>{
	// LOG4J日志打印信息
	private final Log log = LogFactory.getLog(getClass());
	@Inject
	private CkxShiwtxService ckxshiwtxService;
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public Map<String, Object> queryGongyxhd(CkxGongyxhd bean,String exportXls){
		Map<String,Object> map = new HashMap<String, Object>();
		if("exportXls" .equals(exportXls)){			
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxGongyxhd", bean);
			map.put("totas", list.size());
			map.put("rows", list);
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryCkxGongyxhd", bean, bean);
		}
		return map;
	}
	
	/**
	 * 获得多个
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(CkxGongyxhd bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxGxhd",bean);
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<CkxGongyxhd> insert, ArrayList<CkxGongyxhd> edit,
	   		   ArrayList<CkxGongyxhd> delete,LoginUser user) throws ServiceException{
		inserts(insert,user);//手工增加消耗点  mantis  0005331
		edits(edit,user);
		//deletes(delete,user);
		return "success";
	}
	
	
	/**
	 * 获取产线编号，生产节拍，并算出偏移时间   
	 * @param bean
	 * @param user
	 * @author kong
	 */
	@SuppressWarnings("unchecked")
	private void getShengcx(CkxGongyxhd bean,LoginUser user){
		Fenpq fpq  = new Fenpq();
		String fenpq = bean.getGongyxhd().substring(0, 5);
		fpq.setFenpqh(fenpq);
		fpq.setBiaos("1");
		
		//验证分配区
		List<Fenpq> listscxbh =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",fpq);
		if(0==listscxbh.size()){throw new ServiceException("不存在"+fenpq+"分配区或已失效");}
//		if(1>listscxbh.size()){throw new ServiceException("分配区"+fenpq+"只能对应一个生产线编号");}
		
		//验证生产线
		Shengcx scx = new Shengcx();
		scx.setUsercenter(listscxbh.get(0).getUsercenter());
//		scx.setShengcxbh(listscxbh.get(0).getShengcxbh());
		//生产线编号优先取分配区的分装线号，没有则取生产线编号 hanwu 20151219
		scx.setShengcxbh(listscxbh.get(0).getFenzxh() == null ? listscxbh.get(0).getShengcxbh() : listscxbh.get(0).getFenzxh());
		scx.setBiaos("1");
		Shengcx sxcs = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryShengcxs", scx);
		if(null==sxcs){throw new ServiceException("分配区"+fenpq+"对应的产线"+listscxbh.get(0).getShengcxbh()+"不存在或已失效");}
		if(null==sxcs.getShengcjp()||"".equals(sxcs.getShengcjp())){throw new ServiceException("分配区"+fenpq+"对应的产线"+listscxbh.get(0).getShengcxbh()+"不存在整车生产节拍");}
		
		//完善消耗点实体类
		bean.setPianysj((int)Math.ceil((bean.getChessl()*60.0)/Integer.parseInt(sxcs.getShengcjp())));//计算偏移时间
		bean.setShengcxbh(sxcs.getShengcxbh());//产线编号
		bean.setCreator(user.getUsername());
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(user.getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
	}
	
	/**
	 * 私有批量insert方法  
	 * @author denggq
	 * @date 2012-4-17
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	private void inserts(List<CkxGongyxhd> insert,LoginUser user)throws ServiceException{
		for(CkxGongyxhd bean:insert){
			//获取产线编号，生产节拍，并算出偏移时间
			getShengcx(bean,user);
			bean.setGongybs("0");//工艺标识默认无效  
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxGongyxhd",bean);
		}
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param user
	 * @return ""
	 */
	@Transactional
	private void edits(List<CkxGongyxhd> edit,LoginUser user) throws ServiceException{
		for(CkxGongyxhd bean:edit){
			Assert.notNull(bean.getChessl(), GetMessageByKey.getMessage("cheshensl"));
			Assert.notNull(bean.getPianysj(), GetMessageByKey.getMessage("pianyisj"));
			bean.setBiaos("1");	//数据修改后默认改数据为有效状态
			getShengcx(bean,user);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxGongyxhd",bean);
			//拼接用户中心 csy 20160421
			String usercenter = CommonUtil.getUsercenter(bean.getGongyxhd().substring(0,1));
			ckxshiwtxService.update(usercenter, CkxShiwtxService.TIXLX_XIAOHD, bean.getGongyxhd(), null, "1");
		}
	}
	
	/**
	 * 根据用户中心，分配区，获取产线
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getFenpqToChanxMap(){
		Fenpq bean = new Fenpq();
		bean.setBiaos("1");
		List<Fenpq>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",bean);
		Map<String,String> map=new HashMap<String,String>();
		for (Fenpq fenpq : list) {
			map.put(fenpq.getUsercenter()+fenpq.getFenpqh(), fenpq.getShengcxbh());
		}
		return map;
	}
	
	/**
	 * 根据用户中心，分配区，获取产线
	 * @return map<key:usercent+fenppx,value:chanx>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getChanxMap(){
		Shengcx scx = new Shengcx();
		scx.setBiaos("1");
		List<Shengcx>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryShengcx",scx);
		Map<String,Object> map=new HashMap<String,Object>();
		for (Shengcx shengcx : list) {
			map.put(shengcx.getUsercenter()+shengcx.getShengcxbh(),shengcx);
		}
		return map;
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param delete,userID
	 * @return ""
	 */
//	@Transactional
//	private void deletes(List<CkxGongyxhd> delete,LoginUser user)throws ServiceException{
//		for(CkxGongyxhd bean:delete){
//			bean.setEditor(user.getUsername());
//			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxGongyxhd",bean);
//		}
//	}
	
	/**
	 * 单条数据失效
	 * @param bean
	 * @param userId
	 * @return 主键
	 * @author denggqs
	 * @time 2012-4-17
	 */
	public String doDelete(CkxGongyxhd bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxGongyxhd", bean);
		return "invalidateSuccess";
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
	public ArrayList<CkxGongyxhd> importData(String fullFilePath, Map<String, String> paramMap) throws BiffException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException 
	{
		String result = "导入成功";
		//得到文件数据
		ArrayList<CkxGongyxhd> datas =  (ArrayList<CkxGongyxhd>) readExecl(fullFilePath);
		//List<Lingj> lingjs = this.queryList(paramMap);
		//List<Lingj> lingjsZh = this.queryListZh(paramMap);
		//List<String> dinghlxs = this.queryDinglx(); 
		//Map<String, Lingj> lingjMap = translateListToMap(lingjs, "lingjbh");
		//Map<String, Lingj> lingjZhMap = translateListToMap(lingjsZh,"usercenter","lingjbh","zhizlx" );
		//Map<String,String> dinghlxMap = new HashMap<String, String>();
		//for (String string : dinghlxs) 
		//{
			//dinghlxMap.put(string, string);
		//}
		int index = 2;
		//得到调拨申请其他的信息
		//String usercenter = paramMap.get("usercenter");
		String username = paramMap.get("username");
		//String chengbzx = paramMap.get("chengbzx");
		//String huijkm = paramMap.get("huijkm");
		//String time = paramMap.get("time");
		//String beiz = paramMap.get("beiz");
		//初始化调拨单
		//String  diaobsqdh = getdiaosqdh(usercenter);
		//String secondOrderNum = diaobsqdh.substring(0, 5) + String.format("%03d", Integer.parseInt(diaobsqdh.substring(5, 8)) + 1);
		CkxGongyxhd ckxGongyxhd = new CkxGongyxhd();
		//ckxGongyxhd.setUsercenter(usercenter);
		//ckxGongyxhd.setDiaobsqdh(diaobsqdh);
		//ckxGongyxhd.setChengbzx(chengbzx);
		//ckxGongyxhd.setHuijkm(huijkm);
		//ckxGongyxhd.setActive(Const.ACTIVE_1);
		//ckxGongyxhd.setBanc("0001");
		//ckxGongyxhd.setDiaobsqsj(time.substring(0,10));
		//ckxGongyxhd.setCreator(username);
		//ckxGongyxhd.setCreate_time(time);
		//ckxGongyxhd.setEditor(username);
		//ckxGongyxhd.setBeiz(beiz);
		//ckxGongyxhd.setEdit_time(time);
		//ckxGongyxhd kdDiaobsq = (Diaobsq) BeanUtilsBean.getInstance().cloneBean(ilDiaobsq);
		//kdDiaobsq.setDiaobsqdh(secondOrderNum);
		
		//初始化调拨单申请明细
		//Map<String, String> cachMap = new HashMap<String, String>();
		List<CkxGongyxhd>   gyxhd = new ArrayList<CkxGongyxhd>();
		//int il = 0,kd = 0;
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxGongyxhd> wrongdatas =  new ArrayList<CkxGongyxhd>();
		ArrayList<CkxGongyxhd> datasnew =  new ArrayList<CkxGongyxhd>();
		for (int i=0;i<datas.size();i++) 
		{
			StringBuffer wronginfo=new StringBuffer("");
			CkxGongyxhd ckxGongyxhds=datas.get(i);
			//验证工艺消耗 
			if(StringUtils.isBlank(ckxGongyxhds.getGongyxhd())|| ckxGongyxhds.getGongyxhd().length()!=9){
				result = resultMessage(index,"工艺消耗点不能为空且长度为9位!");
				//return result;
				wronginfo=wronginfo.append(result);
			}
			if(StringUtils.isBlank(String.valueOf(ckxGongyxhds.getChessl())) || !StringUtils.isNumeric(String.valueOf(ckxGongyxhds.getChessl())))
			{
				//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的申报数量有误,应为正整数!");
				result = resultMessage(index,"车身数量有误,应为正整数!");
				//return result;
				wronginfo=wronginfo.append(result);
			}
//			if(StringUtils.isBlank(ckxGongyxhds.getCreate_time()) || !isDate(ckxGongyxhds.getCreate_time()))
//			{
//				//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的要货时间有误,应为如下格式   2013-10-23 !");
//				result = resultMessage(index,"创建时间有误,应为如下格式   2013-10-23 !");
//				//return result;
//				wronginfo=wronginfo.append(result);
//			}
//			if(StringUtils.isBlank(ckxGongyxhds.getCreate_time()) || !isDate(ckxGongyxhds.getCreate_time()))
//			{
//				//result = resultMessage(index,"用户中心:" + usercenter + "中零件编号为:" + diaobsqmx.getLingjbh() + "的要货时间有误,应为如下格式   2013-10-23 !");
//				result = resultMessage(index,"修改时间有误,应为如下格式   2013-10-23 !");
//				//return result;
//				wronginfo=wronginfo.append(result);
//			}
			//BigDecimal bigDecimal = new BigDecimal(diaobsqmx.getTempShenbsl());
			ckxGongyxhd.setGongyxhd(ckxGongyxhds.getGongyxhd());
			ckxGongyxhd.setChessl(ckxGongyxhds.getChessl());
			ckxGongyxhd.setPianysj(ckxGongyxhds.getPianysj());
			ckxGongyxhd.setBiaos(ckxGongyxhds.getBiaos());
			ckxGongyxhd.setCreator(username);
			ckxGongyxhd.setCreate_time(paramMap.get("time"));
			ckxGongyxhd.setEditor(username);
			ckxGongyxhd.setEdit_time(paramMap.get("time"));
			ckxGongyxhd.setShengcxbh(ckxGongyxhds.getShengcxbh());
			
			//cachMap.put(diaobsqmx.getLingjbh(), index + "");
			index ++;
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wrongcoun=wrongcoun+1;
				//ckxGongyxhd.setTishi(wronginfo.toString());
				wrongdatas.add(ckxGongyxhd);
			}
			datasnew.add(ckxGongyxhd);
		}
		//如果没有错误信息，则需要判断是插入还是更新 
		if(wrongdatas.size()<=0){
			//先判断导入的数据是否存在 如果存在则更新
			sqlInsertOrUpdate(datasnew);
			ArrayList<CkxGongyxhd> datasnull =  new ArrayList<CkxGongyxhd>();
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
	private List<CkxGongyxhd> readExecl(String fullFilePath) throws BiffException, IOException 
	{
		List<CkxGongyxhd> datas = new ArrayList<CkxGongyxhd>();
		// 读入文件流
		InputStream is = new FileInputStream(new File(fullFilePath));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);	
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		// 行数、列数
		int rows = sheet.getRows();
		int columns = sheet.getColumns();
		
		CkxGongyxhd ckxgongyxhd = null;
		String sheetString = null;
		for (int i = 1; i < rows; i++) 
		{
			ckxgongyxhd = new CkxGongyxhd();
			for (int j = 0; j < columns; j++) 
			{
				sheetString = sheet.getCell(j, i).getContents().trim();
				if( j == 0)
				{
					ckxgongyxhd.setGongyxhd(sheetString);
				}
				else if( j == 1)
				{
					ckxgongyxhd.setChessl(Integer.valueOf(sheetString));
				}
				else if( j == 2)
				{    
					//通过生产节拍计算出偏移时间
					String shengxcbh= sheetString = sheet.getCell(8, i).getContents().trim();
					//验证生产线
					Shengcx scx = new Shengcx();
					scx.setUsercenter(shengxcbh.substring(0,2));
					scx.setShengcxbh(shengxcbh);
					scx.setBiaos("1");
					Shengcx sxcs = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryShengcxs", scx);
					ckxgongyxhd.setPianysj((int)Math.ceil((ckxgongyxhd.getChessl()*60.0)/Integer.parseInt(sxcs.getShengcjp())));
				}
				else if( j == 3)
				{
					ckxgongyxhd.setBiaos(sheetString);
				}
				else if( j == 4)
				{
					ckxgongyxhd.setCreator(sheetString);
				}
				else if( j == 5)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
					log.info("调拨导入第"+i+"行时间："+sheetString);
					sheetString = sheetString.replace("/", "-");
			    	try 
			    	{
			    		sheetString = DateTimeUtil.getJavaTime(formatter.parse(sheetString));
					} 
			    	catch (ParseException e)
			    	{
			    		sheetString = sheet.getCell(j, i).getContents().trim();
					}
			    	ckxgongyxhd.setCreate_time(sheetString);
					log.info("调拨导入第"+i+"行时间1："+sheetString);
				}
				else if( j == 6)
				{
					ckxgongyxhd.setEditor(sheetString);
				}
				else if( j == 7)
				{
					SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
					log.info("调拨导入第"+i+"行时间："+sheetString);
					sheetString = sheetString.replace("/", "-");
			    	try 
			    	{
			    		sheetString = DateTimeUtil.getJavaTime(formatter.parse(sheetString));
					} 
			    	catch (ParseException e)
			    	{
			    		sheetString = sheet.getCell(j, i).getContents().trim();
					}
			    	ckxgongyxhd.setEdit_time(sheetString);
					log.info("调拨导入第"+i+"行时间1："+sheetString);
				}
				else if( j == 8)
				{
					ckxgongyxhd.setShengcxbh(sheetString);
				}
			}
			datas.add(ckxgongyxhd);
		}
		return datas;
	}
    
	
	public void sqlInsertOrUpdate(ArrayList<CkxGongyxhd> datasnew){
		for (CkxGongyxhd ckxGongyxhd : datasnew) {
			//判断导入的数据是新增还是更新后 在来做操作
			//是否需要带标识来判断 数据是否存在
			//CkxGongyxhd  gongyxhd = (CkxGongyxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountCkxGongyxhd", ckxGongyxhd);
			if(DBUtil.checkCount(baseDao,"ts_ckx.getCountGongyxhd", ckxGongyxhd)){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxGongyxhd",ckxGongyxhd);
			}else{
				//插入的时候 其他的字段为空或0  工艺标识是为0还是为空?
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxGongyxhds",ckxGongyxhd);
			}
		}
	}
	
//	private Map<String,Lingj> translateListToMap(List<Lingj> lingjs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
//	{
//		Map<String, Lingj> result = new HashMap<String, Lingj>();
//		Lingj lingj = null;
//		if(null != lingjs && lingjs.size() > 0 && null != propertys && propertys.length > 0)
//		{
//			for (int i=0,j=lingjs.size();i<j;i++) 
//			{
//				lingj = lingjs.get(i);
//				StringBuffer flagKey = new StringBuffer();
//				for (int k = 0,p = propertys.length; k < p; k++)
//				{
//					flagKey.append(BeanUtils.getProperty(lingj, propertys[k]));
//				}
//				result.put(flagKey.toString(), lingj);
//			}
//		}
//		return result;
//	}
}

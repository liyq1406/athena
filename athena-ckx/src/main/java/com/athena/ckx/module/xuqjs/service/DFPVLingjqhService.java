package com.athena.ckx.module.xuqjs.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.DFPVLingjqh;
import com.athena.ckx.entity.xuqjs.DFPVLingjqhLCDV;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 零件切换Service
 * @author CSY
 * @date 2016-05-04
 */
@Component
public class DFPVLingjqhService extends BaseService{
	
	protected static Logger logger = Logger.getLogger(DFPVLingjqhService.class);
	
	/*@Inject
	private CacheManager cacheManager;//缓存
	 */	
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @author CSY
	 * @date 2016-05-04	 
	 */
	public Map queryLingjqh(DFPVLingjqh bean){
		Map res = new HashMap<String, String>();
		try {
			//将数据源切换到2，非事务方法可以不写
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			res = this.getLingjqh(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}finally{
			//将数据源切换回1，保证该线程的默认数据源为1
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return res;
	}
	
	/**
	 * 分页查询（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return
	 */
	private Map getLingjqh(DFPVLingjqh bean){
		Map res = new HashMap<String, String>();
		try {
			res = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("dfpvlingjqh.queryLingjqh",bean,bean);
		} catch (Exception e) {
			throw new ServiceException("零件切换-分页查询出现异常"+e.getMessage());
		}
		return res;
	}
	
	///////////////////////////////////////////////////////////////////////

	
	/**
	 * 分页查询LCDV（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return
	 */
	public Map queryLingjqhLCDV(DFPVLingjqhLCDV bean){
		Map res = new HashMap<String, String>();
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			res = this.getLingjqhLCDV(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}finally {
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return res;
	}
	
	/**
	 * 分页查询LCDV（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @author CSY
	 * @date 2016-05-04	 
	 */
	private Map getLingjqhLCDV(DFPVLingjqhLCDV bean){
		Map res = new HashMap<String, String>();
		try {
			res = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("dfpvlingjqh.queryLingjqhLCDV",bean,bean);
		} catch (Exception e) {
			throw new ServiceException("零件切换LCDV-分页查询出现异常"+e.getMessage());
		}
		return res;
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 查询最近十次版本号（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @author CSY
	 * @date 2016-05-04	 
	 */
	public List<DFPVLingjqh> queryLingjqhBBH(DFPVLingjqh bean){
		List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			list = this.getLingjqhBBH(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}finally{
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return list;
	}
	
	/**
	 * 查询最近十次版本号（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return
	 */
	private List<DFPVLingjqh> getLingjqhBBH(DFPVLingjqh bean){
		List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
		try {
			List<String> qhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("dfpvlingjqh.queryZUIJBANBH");
			for (String string : qhList) {
				DFPVLingjqh d=new DFPVLingjqh();
				d.setBanbh(string);
				list.add(d);
			}
		} catch (Exception e) {
			throw new ServiceException("零件切换-查询近十次版本号出现异常"+e.getMessage());
		}
		return list;
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 失效（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public void shixiao(List<DFPVLingjqh> updateDFPVLingjqh,String username) throws ServiceException{
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			this.getShixiao(updateDFPVLingjqh, username);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}finally{
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
	}
	
	/**
	 * 失效（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param updateDFPVLingjqh
	 * @param username
	 * @throws ServiceException
	 */
	@Transactional
	private void getShixiao(List<DFPVLingjqh> updateDFPVLingjqh,String username) throws ServiceException{
		try {
			List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
			for (DFPVLingjqh lingjqh : updateDFPVLingjqh) {
				//只有状态为新建或生效的才能修改为失效
				if (lingjqh.getActive().equals("0")||lingjqh.getActive().equals("3")) {
					continue;
				}
				if (lingjqh.getEcom().equals("undefined")) {
					lingjqh.setEcom(null);
				}
				if (lingjqh.getQiehqslsh().equals("undefined")) {
					lingjqh.setQiehqslsh(null);
				}
				if (lingjqh.getLingjbh().equals("undefined")) {
					lingjqh.setLingjbh(null);
				}
				if (lingjqh.getXlingjbh().equals("undefined")) {
					lingjqh.setXlingjbh(null);
				}
				if (lingjqh.getYshul().equals("undefined")) {
					lingjqh.setYshul(null);
				}
				if (lingjqh.getXshul().equals("undefined")) {
					lingjqh.setXshul(null);
				}
				if (lingjqh.getYgongysdm().equals("undefined")) {
					lingjqh.setYgongysdm(null);
				}
				if (lingjqh.getXgongysdm().equals("undefined")) {
					lingjqh.setXgongysdm(null);
				}
				lingjqh.setEditor(username);
				lingjqh.setEdit_time(DateTimeUtil.getAllCurrTime());
				list.add(lingjqh);
			}
			if (list.size() > 0) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch("dfpvlingjqh.updateLingjqhShix", list);
			}
		} catch (Exception e) {
			throw new ServiceException("零件切换-失效出现异常"+e.getMessage());
		}
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 生效（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public void shengx(List<DFPVLingjqh> updateDFPVLingjqh,String username) throws ServiceException{
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			this.getShengx(updateDFPVLingjqh, username);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}finally{
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
	}
	
	/**
	 * 生效（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param updateDFPVLingjqh
	 * @param username
	 * @throws ServiceException
	 */
	@Transactional
	public void getShengx(List<DFPVLingjqh> updateDFPVLingjqh,String username) throws ServiceException{
		try {
			List<DFPVLingjqh> list = new ArrayList<DFPVLingjqh>();
			for (DFPVLingjqh lingjqh : updateDFPVLingjqh) {
				//只有状态为新建的才能修改为生效
				if (!lingjqh.getActive().equals("2")) {
					continue;
				}
				if (lingjqh.getEcom().equals("undefined")) {
					lingjqh.setEcom(null);
				}
				if (lingjqh.getQiehqslsh().equals("undefined")) {
					lingjqh.setQiehqslsh(null);
				}
				if (lingjqh.getLingjbh().equals("undefined")) {
					lingjqh.setLingjbh(null);
				}
				if (lingjqh.getXlingjbh().equals("undefined")) {
					lingjqh.setXlingjbh(null);
				}
				if (lingjqh.getYshul().equals("undefined")) {
					lingjqh.setYshul(null);
				}
				if (lingjqh.getXshul().equals("undefined")) {
					lingjqh.setXshul(null);
				}
				if (lingjqh.getYgongysdm().equals("undefined")) {
					lingjqh.setYgongysdm(null);
				}
				if (lingjqh.getXgongysdm().equals("undefined")) {
					lingjqh.setXgongysdm(null);
				}
				lingjqh.setEditor(username);
				lingjqh.setEdit_time(DateTimeUtil.getAllCurrTime());
				list.add(lingjqh);
			}
			if (list.size() > 0) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).executeBatch("dfpvlingjqh.updateLingjqhShengx",list);
			}
		} catch (Exception e) {
			throw new ServiceException("零件切换-生效出现异常"+e.getMessage());
		}
	}
	
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * 数据导入（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param request
	 * @return
	 */
	public String upload(HttpServletRequest request,String username){
		String error = "";
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			error = this.getUpload(request, username);
		}catch (Exception e) {
			logger.error(e.getMessage());
			error = "导入有误！";
		}finally{
			// 切换ckx数据源
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return error;
	}
	
	/**
	 * 导入（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @param request
	 * @param username
	 * @return
	 */
	@Transactional
	private String getUpload(HttpServletRequest request,String username){
		String error = "";
		int errorTimes = 0;
		int existTimes = 0;
		boolean impState = true;
		
		//得到一个FileIterm工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//得到解析器对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		try {
			List<FileItem> list = upload.parseRequest(request);
			Workbook book = null;
			//判断上传的是不是文件上传的方式 只处理单文件上传
			for(FileItem item : list)
			{
				if (!item.isFormField()) {
					try {
						List<Map<String, Object>> lingjList = new ArrayList<Map<String, Object>>();
						Set<Map<String, Object>> lingjSet = new HashSet<Map<String, Object>>();
						//获取当前日期
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
						String bbhDate = sdf.format(date);
						String newDate = sdf2.format(date);
						//获取将要使用的版本号（根据当前日期判断）
						String bbh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.queryNweBBH", bbhDate);
						book = Workbook.getWorkbook(item.getInputStream()) ;
						//解析sheet0
						Sheet sheet = book.getSheet(0);//零件切换明细
						int rows = sheet.getRows();
						if (rows == 0) {
							return "sheet:lingjqh数据有误，请检查后重试！ ";
						}
						int type = 0;
						int time = 0;
						//只有数据行数对2取模为0且数据行数大于0，才进行解析
						if ((rows-4)%2==0 && rows-4>0) {
							String sheetName = sheet.getCell(0, 0).getContents();//获取标题
							String yuany = sheet.getCell(2, 1).getContents();//获取原因
							String shenqr = sheet.getCell(6, rows-1).getContents();//获取申请人
							String gengggx1 = "", gengggx2 = "";//更改关系（AV/AP）
							String touflwjh1 = "", touflwjh2 = "";//投放令文件号
							String zongzlsh1 = "", zongzlsh2 = "";//总装流水号
							String lingjbh1 = "", lingjbh2 = "";//零件编号
							String lingjmc1 = "", lingjmc2 = "";//零件名称
							String lingjsl1 = "", lingjsl2 = "";//零件数量
							String shengcx1 = "", shengcx2 = "";//生产线号
							String gongys1 = "", gongys2 = "";//供应商
							String ecom1 = "", ecom2 = "";//ecom
							String shijqhrq1 = "", shijqhrq2 = "";//实际切换日期
							
							/**
							 * 如果标题为“零件切换BOM更改申报单”，则按车型切换A
							 */
							if (sheetName.equals("零件切换BOM更改申报单")) {
								logger.info("开始导入“零件切换BOM更改申报单”");
								type = 1;
								for (int i = 3; i < rows-1; i++) {
									time += 1;
									if (i%2 == 0) {
										continue;
									}
									Map<String, Object> params = new HashMap<String, Object>();
									params.clear();
									
									//每组第一行
									gengggx1 = sheet.getCell(1, i).getContents();
									touflwjh1 = sheet.getCell(9, i).getContents();
									zongzlsh1 = sheet.getCell(10, i).getContents();
									lingjbh1 = sheet.getCell(2, i).getContents();
									lingjmc1 = sheet.getCell(3, i).getContents(); 
									lingjsl1 = sheet.getCell(4, i).getContents();
									shengcx1 = sheet.getCell(5, i).getContents(); 
									gongys1 = sheet.getCell(6, i).getContents();
									Cell ecomCell = sheet.getCell(7, i);
									Cell sjqhrqCell = sheet.getCell(8, i);
									ecom1 = "";
									if(ecomCell.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)ecomCell;         
										ecom1 = sdf3.format(dc.getDate()); 
									}
									shijqhrq1 = ""; 
									if(sjqhrqCell.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)sjqhrqCell;         
										shijqhrq1 = sdf3.format(dc.getDate()); 
									}
									params.put("yuany", yuany);
									params.put("shenbr", shenqr);
									params.put("bbh", bbh);
									params.put("creator", username);
									params.put("editor", username);
									params.put("create_time", newDate);
									params.put("edit_time", newDate);
									params.put("gengggx1", gengggx1);
									params.put("touflwjh1", touflwjh1);
									params.put("zongzlsh1", zongzlsh1);
									params.put("lingjbh1", lingjbh1);
									params.put("lingjmc1", lingjmc1);
									params.put("lingjsl1", lingjsl1);
									params.put("shengcx1", shengcx1);
									params.put("gongys1", gongys1);
									params.put("ecom1", ecom1);
									params.put("shijqhrq1", shijqhrq1);
									params.put("leix", "A");
									params.put("line", i);
									
									//每组第二行
									gengggx2 = sheet.getCell(1, i+1).getContents();
									touflwjh2 = sheet.getCell(9, i+1).getContents();
									zongzlsh2 = sheet.getCell(10, i+1).getContents();
									lingjbh2 = sheet.getCell(2, i+1).getContents();
									lingjmc2 = sheet.getCell(3, i+1).getContents(); 
									lingjsl2 = sheet.getCell(4, i+1).getContents();
									shengcx2 = sheet.getCell(5, i+1).getContents(); 
									gongys2 = sheet.getCell(6, i+1).getContents();
									Cell ecomCell2 = sheet.getCell(7, i+1);
									Cell sjqhrqCell2 = sheet.getCell(8, i+1);
									ecom2 = "";
									if(ecomCell2.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)ecomCell2;         
										ecom2 = sdf3.format(dc.getDate()); 
									}
									shijqhrq2 = ""; 
									if(sjqhrqCell2.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)sjqhrqCell2;         
										shijqhrq2 = sdf3.format(dc.getDate()); 
									}
									params.put("gengggx2", gengggx2);
									params.put("touflwjh2", touflwjh2);
									params.put("zongzlsh2", zongzlsh2);
									params.put("lingjbh2", lingjbh2);
									params.put("lingjmc2", lingjmc2);
									params.put("lingjsl2", lingjsl2);
									params.put("shengcx2", shengcx2);
									params.put("gongys2", gongys2);
									params.put("ecom2", ecom2);
									params.put("shijqhrq2", shijqhrq2);
									if (gengggx1.equals("AV")&&gengggx2.equals("AP")) {
										int flag1 = 0, flag2 = 0;
										boolean flag = true;
										if (   touflwjh1.equals("")
											&& zongzlsh1.equals("")
											&& lingjbh1.equals("")
											&& lingjsl1.equals("")
											&& shengcx1.equals("") 
											&& gongys1.equals("")
											&& ecom1.equals("") 
											&& shijqhrq1.equals("")) {//第一行数据全为空时
											flag1 = 1;
										}
										if (   touflwjh2.equals("") 
											&& zongzlsh2.equals("")
											&& lingjbh2.equals("")
											&& lingjsl2.equals("")
											&& shengcx2.equals("")
											&& gongys2.equals("")
											&& ecom2.equals("")
											&& shijqhrq2.equals("")) {//第二行数据全为空时
											flag2 = 1;
										}
										if (flag1 == 1 && flag2 == 1) {
											//如果第一行第二行同时全为空，则跳过
											logger.info("第"+(i+1)+"行和"+(i+2)+"数据中关键数据同时为空，跳过");
											continue;
										}else {
											Map<String, Object> map = check(flag1, flag2, "A", params);
											if (errorTimes < 5 && !map.get("error").toString().equals("")) {
												errorTimes ++;
												error += map.get("error").toString();
												error += "\n";
											}
											//errorCheck = map.get("error").toString();
											flag = (Boolean) map.get("flag");
										}
										//表格验证通过，则向数据库中查询表格内容是否存在
										if (flag) {
											//整合投放令文件号
											if (!params.get("touflwjh1").toString().equals("")) {
												params.put("touflwjh", params.get("touflwjh1").toString());
											}
											if (!params.get("touflwjh2").toString().equals("")) {
												params.put("touflwjh", params.get("touflwjh2").toString());
											}
											//整合总装流水号
											if (!params.get("zongzlsh1").toString().equals("")) {
												params.put("zongzlsh", params.get("zongzlsh1").toString());
											}
											if (!params.get("zongzlsh2").toString().equals("")) {
												params.put("zongzlsh", params.get("zongzlsh2").toString());
											}
											//整合生产线编号
											if (!params.get("shengcx1").toString().equals("")) {
												params.put("shengcx", params.get("shengcx1").toString());
											}
											if (!params.get("shengcx2").toString().equals("")) {
												params.put("shengcx", params.get("shengcx2").toString());
											}
											//整合ecom
											if (!params.get("ecom1").toString().equals("")) {
												params.put("ecom", params.get("ecom1").toString());
											}
											if (!params.get("ecom2").toString().equals("")) {
												params.put("ecom", params.get("ecom2").toString());
											}
											//整合实际切换日期
											if (!params.get("shijqhrq1").toString().equals("")) {
												params.put("shijqhrq", params.get("shijqhrq1").toString());
											}
											if (!params.get("shijqhrq2").toString().equals("")) {
												params.put("shijqhrq", params.get("shijqhrq2").toString());
											}
											String count = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfBom", params);
											if (count.equals("0")) {
												params.remove("line");
												lingjSet.add(params);
											}else {
												//errorCheck += "第"+(i+1)+"行和第"+(i+2)+"行数据已存在！ ";
												/*if (errorTimes <5) {
													errorTimes ++;
													error += "第"+(i+1)+"行和第"+(i+2)+"行数据已存在！\n";
												}*/
												existTimes ++;
											}
										}
										/*if (!errorCheck.equals("")) {
											errorCheck += "\n";
										}*/
									}else {
										//errorCheck += "第"+(i+1)+"行和第"+(i+2)+"行对应关系有误";
										if (errorTimes <5) {
											errorTimes ++;
											error += "第"+(i+1)+"行和第"+(i+2)+"行对应关系有误\n ";
										}
									}
								}
							}
							/**
							 * 如果标题为“零件预批量BOM更改申报单”，则按流水号切换B
							 */
							else if (sheetName.equals("零件预批量BOM更改申报单")) {
								type = 2;
								for (int i = 3; i < rows-1; i++) {
									time += 1;
									if (i%2 == 0) {
										continue;
									}
									Map<String, Object> params = new HashMap<String, Object>();
									params.clear();
									
									//每组第一行
									gengggx1 = sheet.getCell(1, i).getContents();
									touflwjh1 = sheet.getCell(8, i).getContents();
									lingjbh1 = sheet.getCell(2, i).getContents();
									lingjmc1 = sheet.getCell(3, i).getContents();
									lingjsl1 = sheet.getCell(4, i).getContents();
									shengcx1 = sheet.getCell(5, i).getContents();
									gongys1 = sheet.getCell(6, i).getContents();
									Cell sjqhrqCell = sheet.getCell(7, i);
									shijqhrq1 = "";
									if(sjqhrqCell.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)sjqhrqCell;         
										shijqhrq1 = sdf3.format(dc.getDate()); 
									}
									params.put("yuany", yuany);
									params.put("shenbr", shenqr);
									params.put("bbh", bbh);
									params.put("creator", username);
									params.put("editor", username);
									params.put("create_time", newDate);
									params.put("edit_time", newDate);
									params.put("lingjbh1", lingjbh1);
									params.put("lingjmc1", lingjmc1);
									params.put("lingjsl1", lingjsl1);
									params.put("shengcx1", shengcx1);
									params.put("gongys1", gongys1);
									params.put("shijqhrq1", shijqhrq1);
									params.put("touflwjh1", touflwjh1);
									params.put("leix", "B");
									params.put("line", i);
									
									//每组第二行
									gengggx2 = sheet.getCell(1, i+1).getContents();
									touflwjh2 = sheet.getCell(8, i+1).getContents();
									lingjbh2 = sheet.getCell(2, i+1).getContents();
									lingjmc2 = sheet.getCell(3, i+1).getContents();
									lingjsl2 = sheet.getCell(4, i+1).getContents();
									shengcx2 = sheet.getCell(5, i+1).getContents();
									gongys2 = sheet.getCell(6, i+1).getContents();
									Cell sjqhrqCell2 = sheet.getCell(7, i+1);
									shijqhrq2 = "";
									if(sjqhrqCell2.getType()==CellType.DATE){ 
										DateCell dc = (DateCell)sjqhrqCell2;         
										shijqhrq2 = sdf3.format(dc.getDate()); 
									}
									params.put("lingjbh2", lingjbh2);
									params.put("lingjmc2", lingjmc2);
									params.put("lingjsl2", lingjsl2);
									params.put("shengcx2", shengcx2);
									params.put("gongys2", gongys2);
									params.put("shijqhrq2", shijqhrq2);
									params.put("touflwjh2", touflwjh2);
									
									if (gengggx1.equals("AV")&&gengggx2.equals("AP")) {
										int flag1 = 0, flag2 = 0;
										boolean flag = true;
										if (	touflwjh1.equals("")
											&&	lingjbh1.equals("")
											&&	lingjsl1.equals("")
											&&	shengcx1.equals("")
											&&	gongys1.equals("")
											&&	shijqhrq1.equals("")) {//第一行全为空
											flag1 = 1;
										}
										if (	touflwjh2.equals("")
											&&	lingjbh2.equals("")
											&&	lingjsl2.equals("")
											&&	shengcx2.equals("")
											&&	gongys2.equals("")
											&&	shijqhrq2.equals("")) {//第二行全为空
											flag2 = 1;
										}
										if (flag1 == 1 && flag2 == 1) {
											//如果第一行第二行同时全为空，则跳过
											logger.info("第"+(i+1)+"行和"+(i+2)+"数据中关键数据同时为空，跳过");
											continue;
										}else {
											Map<String, Object> map = check(flag1, flag2, "B", params);
											if (errorTimes < 5 && !map.get("error").toString().equals("")) {
												errorTimes ++;
												error += map.get("error").toString();
												error += "\n";
											}
											//errorCheck = map.get("error").toString();
											flag = (Boolean) map.get("flag");
										}
										//表格数据验证通过，则查询是否已存在
										if (flag) {
											//整合投放令文件号
											if (!params.get("touflwjh1").toString().equals("")) {
												params.put("touflwjh", params.get("touflwjh1").toString());
											}
											if (!params.get("touflwjh2").toString().equals("")) {
												params.put("touflwjh", params.get("touflwjh2").toString());
											}
											//整合生产线编号
											if (!params.get("shengcx1").toString().equals("")) {
												params.put("shengcx", params.get("shengcx1").toString());
											}
											if (!params.get("shengcx2").toString().equals("")) {
												params.put("shengcx", params.get("shengcx2").toString());
											}
											//整合实际切换日期
											if (!params.get("shijqhrq1").toString().equals("")) {
												params.put("shijqhrq", params.get("shijqhrq1").toString());
											}
											if (!params.get("shijqhrq2").toString().equals("")) {
												params.put("shijqhrq", params.get("shijqhrq2").toString());
											}
											String count = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfBomYPL", params);
											if (count.equals("0")) {
												params.remove("line");
												lingjSet.add(params);
											}else {
												//errorCheck += "第"+(i+1)+"行和第"+(i+2)+"行数据已存在！ ";
												/*if (errorTimes <5) {
													errorTimes ++;
													error += "第"+(i+1)+"行和第"+(i+2)+"行数据已存在！\n";
												}*/
												existTimes ++;
											}
										}
										/*if (!errorCheck.equals("")) {
											errorCheck += "\n";
										}*/
									}else {
										//errorCheck += "第"+(i+1)+"行和第"+(i+2)+"行对应关系有误！";
										if (errorTimes <5) {
											errorTimes ++;
											error += "第"+(i+1)+"行和第"+(i+2)+"行对应关系有误！\n ";
										}
									}
								}
							}else{
								return "文件标题有误，请确认！";
							}
						}else {
							return "导入文件数据行数不符合两行一组，请检查后重试！ ";
						}
						//只有错误为""时才进行后续操作
						if (lingjSet.size() > 0) {
							lingjList.addAll(lingjSet);
							//解析sheet1
							Sheet sheetLCDV = book.getSheet(1);//零件切换流水号
							int rowsLCDV = sheetLCDV.getRows();//零件切换流水号行数
							int test = 0;
							Set<Map<String, Object>> lcdv = new HashSet<Map<String, Object>>();
							for (int i = 1; i < rowsLCDV; i++) {
								if (sheetLCDV.getCell(1, i).getContents().equals("")) {
									break;
								}
								Map<String, Object> paramsLCDV = new HashMap<String, Object>();//流水号所用参数
								if (type == 1) {
									paramsLCDV.put("LEIX", "A");
								}else if (type == 2) {
									paramsLCDV.put("LEIX", "B");
								}
								paramsLCDV.put("BANBH", bbh);
								test ++;
								paramsLCDV.put("LIUSH", sheetLCDV.getCell(1, i).getContents());
								lcdv.add(paramsLCDV);
							}
							if (test < 1) {
								return "sheet：lingjqhLCDV数据有误，请检查后重试！ ";
							}
							List<Map<String, Object>> pl = new ArrayList<Map<String,Object>>();
							pl.addAll(lcdv);
							if (lingjList.size()>0 && type == 1) {
								for (int i = 0; i < pl.size(); i++) {
									if (!pl.get(i).get("LIUSH").toString().matches("^[A-Za-z0-9]{24}$")) {
										//error += "sheet:lingjqhLCDV第"+(i+2)+"行 LCDV24有误！ ";
										//errorCheck += "sheet:lingjqhLCDV第"+(i+2)+"行 LCDV24有误！ ";
										if (errorTimes <5) {
											errorTimes ++;
											error += "sheet:lingjqhLCDV第"+(i+2)+"行 LCDV24有误！ \n ";
										}
									}
								}
							}else if (lingjList.size()>0 && type == 2) {
								for (int i = 0; i < pl.size(); i++) {
									if (!pl.get(i).get("LIUSH").toString().matches("^[A-Za-z0-9]{9}$")) {
										//error += "sheet:lingjqhLCDV第"+(i+2)+"行 总装流水号有误！ ";
										//errorCheck += "sheet:lingjqhLCDV第"+(i+2)+"行 总装流水号有误！ ";
										if (errorTimes <5) {
											errorTimes ++;
											error += "sheet:lingjqhLCDV第"+(i+2)+"行 总装流水号有误！\n ";
										}
									}
								}
								for (int i = 0; i < lingjList.size(); i++) {
									for (int j = 0; j < pl.size(); j++) {
										if (!lingjList.get(i).get("shengcx").toString().substring(4).equals(pl.get(j).get("LIUSH").toString().substring(0, 1))) {
											//errorCheck += "第" + (2*i+4) +"行产线号"+lingjList.get(i).get("shengcx").toString()+"最后一位与lingjqhLCDV中总装流水号"+pl.get(j).get("LIUSH").toString()+"第一位不符！";
											if (errorTimes <5) {
												errorTimes ++;
												error += "第" + (2*i+4) +"行产线号"+lingjList.get(i).get("shengcx").toString()+"最后一位与lingjqhLCDV中总装流水号"+pl.get(j).get("LIUSH").toString()+"第一位不符！\n ";
											}
										}
										if (!lingjList.get(i).get("shengcx").toString().substring(4).equals(pl.get(j).get("LIUSH").toString().substring(0, 1))) {
											//errorCheck += "第" + (2*i+5) +"行产线号"+lingjList.get(i).get("shengcx").toString()+"最后一位与lingjqhLCDV中总装流水号"+pl.get(j).get("LIUSH").toString()+"第一位不符！";
											if (errorTimes <5) {
												errorTimes ++;
												error += "第" + (2*i+5) +"行产线号"+lingjList.get(i).get("shengcx").toString()+"最后一位与lingjqhLCDV中总装流水号"+pl.get(j).get("LIUSH").toString()+"第一位不符！\n ";
											}
										}
									}
								}
							}
							Map<String, Object> ljqh = new HashMap<String, Object>();
							//如果错误信息依然为空，则向两张表中插入数据
							if (lingjList.size() > 0 && pl.size() > 0 ) {
								ljqh.put("lingjList", lingjList);
								ljqh.put("pl", pl);
								if (type == 1) {
									//插入明细表和条件表
									try {
										baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("dfpvlingjqh.insertLingjqh_LCDV",ljqh);
									} catch (Exception e) {
										impState = false;
										error = e.getMessage();
									}
								}else if (type ==2 ) {
									//插入明细表和条件表
									try {
										baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("dfpvlingjqh.insertLingjqhLS_LCDV",ljqh);
									} catch (Exception e) {
										impState = false;
										error = e.getMessage();
									}
								}
							}else {
								if (lingjList.size() <= 0) {
									error += "切换信息数量错误\n ";
								}
								if (pl.size() <= 0) {
									error += "LCDV/流水信息数量错误\n ";
								}
								
							}
							//根据错误信息返回提示
							if (!error.equals("") && error != null) {
								if (impState) {
									error += "......\n";
									error += "通过验证的数据导入成功\n";
									error += "已存在的数据共有"+ existTimes +"对\n";
									error += "导入切换信息：" + lingjList.size() + "对\n";
									error += "导入LCDV/流水信息：" + pl.size() + "条\n";
								}else {
									error += "已存在的数据共有"+ existTimes +"对\n";
									error += "......\n";
									error += "本次无数据导入";
								}
							}else{
								error = "导入成功！\n";
								error += "已存在的数据共有"+ existTimes +"对\n";
								error += "导入切换信息：" + lingjList.size() + "对\n";
								error += "导入LCDV/流水信息：" + pl.size() + "条\n";
							}
						}else {
							error += "已存在的数据共有"+ existTimes +"对\n";
							error += "......\n";
							error += "本次无数据导入";
							return error;
						}
					} catch (BiffException e) {
						logger.error(e.getMessage());
						return "导入格式有误，请将文件另存为“97-03版本”xls格式后再尝试";
					} finally {
						try {
							if (book!=null) {
								book.close();
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			return "导入有误！";
		}
		return error;
	}
	
	/**
	 * 导入模板字段验证
	 * @param flag1值为0时，验证第一行
	 * @param flag2值为0时，验证第二行
	 * @param leix值为A时，按车型验证；值为B时，按流水号验证
	 * @param params
	 * @return
	 */
	private Map<String, Object> check(int flag1, int flag2 , String leix, Map<String, Object> params){
		boolean flag = true;
		String error = "";
		Map<String, Object> map = new HashMap<String, Object>();
		int i = Integer.parseInt(params.get("line").toString());
		if (leix.equals("A")) {//按车型验证（模板1）
			if (flag1 == 0) {
				if (params.get("lingjbh1").toString().equals("")) {
					error += "第"+(i+1)+"行零件编号不能为空！ ";
					flag = false;
				}else if (!params.get("lingjbh1").toString().matches("^[A-Za-z0-9]{10}$")) {
					error += "第"+(i+1)+"行零件编号不符合条件！ ";
					flag = false;
				}
				if (params.get("lingjsl1").toString().equals("")) {
					error += "第"+(i+1)+"行零件数量不能为空！ ";
					flag = false;
				}else if (!params.get("lingjsl1").toString().matches("^\\d+(\\.\\d+)?$")) {
					error += "第"+(i+1)+"行零件数量不符合条件！ ";
					flag = false;
				}
				if (params.get("gongys1").toString().equals("")) {
					error += "第"+(i+1)+"行供应商代码不能为空！ ";
					flag = false;
				}else {
					String countYGYS = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfYGYS", params);
					if (countYGYS.equals("0")) {
						error += "第"+(i+1)+"行供应商代码不存在或已失效！ ";
						flag = false;
					}
				}
				if (params.get("shengcx1").toString().equals("")) {
					error += "第"+(i+1)+"行生产线编号不能为空！ ";
					flag = false;
				}else if (!params.get("shengcx1").toString().matches("^[A-Za-z0-9]{5}$")) {
					error += "第"+(i+1)+"行生产线编号不符合条件！ ";
					flag = false;
				}
				if (params.get("ecom1").toString().equals("")) {
					error += "第"+(i+1)+"行ecom必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("shijqhrq1").toString().equals("")) {
					error += "第"+(i+1)+"行实际切换日期必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("touflwjh1").toString().equals("")) {
					error += "第"+(i+1)+"行投放令文件号不能为空！ ";
					flag = false;
				}else if (!params.get("touflwjh1").toString().matches("^[A-Za-z0-9]{11}$")) {
					error += "第"+(i+1)+"行投放令文件号不符合条件！ ";
					flag = false;
				}
				if (params.get("zongzlsh1").toString().equals("")) {
					error += "第"+(i+1)+"行总装流水号不能为空！ ";
					flag = false;
				}else if (!params.get("zongzlsh1").toString().matches("^[A-Za-z0-9]{9}$")) {
					error += "第"+(i+1)+"行总装流水号不符合条件！ ";
					flag = false;
				}
			}
			if (flag2 == 0) {
				if (params.get("lingjbh2").toString().equals("")) {
					error += "第"+(i+2)+"行零件编号不能为空！ ";
					flag = false;
				}else if (!params.get("lingjbh2").toString().matches("^[A-Za-z0-9]{10}$")) {
					error += "第"+(i+2)+"行零件编号不符合条件！ ";
					flag = false;
				}
				if (params.get("lingjsl2").toString().equals("")) {
					error += "第"+(i+2)+"行零件数量不能为空！ ";
					flag = false;
				}else if (!params.get("lingjsl2").toString().matches("^\\d+(\\.\\d+)?$")) {
					error += "第"+(i+2)+"行零件数量不符合条件！ ";
					flag = false;
				}
				if (params.get("gongys2").toString().equals("")) {
					error += "第"+(i+2)+"行供应商代码不能为空！ ";
					flag = false;
				}else {
					String countYGYS = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfXGYS", params);
					if (countYGYS.equals("0")) {
						error += "第"+(i+2)+"行供应商代码不存在或已失效！ ";
						flag = false;
					}
				}
				if (params.get("shengcx2").toString().equals("")) {
					error += "第"+(i+2)+"行生产线编号不能为空！ ";
					flag = false;
				}else if (!params.get("shengcx2").toString().matches("^[A-Za-z0-9]{5}$")) {
					error += "第"+(i+2)+"行生产线编号不符合条件！ ";
					flag = false;
				}
				if (params.get("ecom2").toString().equals("")) {
					error += "第"+(i+2)+"行ecom必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("shijqhrq2").toString().equals("")) {
					error += "第"+(i+2)+"行实际切换日期必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("touflwjh2").toString().equals("")) {
					error += "第"+(i+2)+"行投放令文件号不能为空！ ";
					flag = false;
				}else if (!params.get("touflwjh2").toString().matches("^[A-Za-z0-9]{11}$")) {
					error += "第"+(i+2)+"行投放令文件号不符合条件！ ";
					flag = false;
				}
				if (params.get("zongzlsh2").toString().equals("")) {
					error += "第"+(i+2)+"行总装流水号不能为空！ ";
					flag = false;
				}else if (!params.get("zongzlsh2").toString().matches("^[A-Za-z0-9]{9}$")) {
					error += "第"+(i+2)+"行总装流水号不符合条件！ ";
					flag = false;
				}
			}
			if (params.get("shenbr").toString().equals("")) {
				error += "申报人不能为空！ ";
				flag = false;
			}
			if (flag) {
				if (flag1 == 0 && !params.get("shengcx1").toString().substring(4).equals(params.get("zongzlsh1").toString().substring(0, 1))) {
					error += "第"+(i+1)+"行产线号最后一位与总装流水号第一位不相等！ ";
					flag = false;
				}
				if (flag2 == 0 && !params.get("shengcx2").toString().substring(4).equals(params.get("zongzlsh2").toString().substring(0, 1))) {
					error += "第"+(i+2)+"行产线号最后一位与总装流水号第一位不相等！ ";
					flag = false;
				}
			}
		}else if (leix.equals("B")) {//按流水号验证（模板2）
			if (flag1 == 0) {
				if (params.get("lingjbh1").toString().equals("")) {
					error += "第"+(i+1)+"行零件编号不能为空！ ";
					flag = false;
				}else if (!params.get("lingjbh1").toString().matches("^[A-Za-z0-9]{10}$")) {
					error += "第"+(i+1)+"行零件编号不符合条件！ ";
					flag = false;
				}
				if (params.get("lingjsl1").toString().equals("")) {
					error += "第"+(i+1)+"行零件数量不能为空！ ";
					flag = false;
				}else if (!params.get("lingjsl1").toString().matches("^\\d+(\\.\\d+)?$")) {
					error += "第"+(i+1)+"行零件数量不符合条件！ ";
					flag = false;
				}
				if (params.get("gongys1").toString().equals("")) {
					error += "第"+(i+1)+"行供应商代码不能为空！ ";
					flag = false;
				}else {
					String countYGYS = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfYGYS", params);
					if (countYGYS.equals("0")) {
						error += "第"+(i+1)+"行供应商代码不存在或已失效！ ";
						flag = false;
					}
				}
				if (params.get("shengcx1").toString().equals("")) {
					error += "第"+(i+1)+"行生产线编号不能为空！ ";
					flag = false;
				}else if (!params.get("shengcx1").toString().matches("^[A-Za-z0-9]{5}$")) {
					error += "第"+(i+1)+"行生产线编号不符合条件！ ";
					flag = false;
				}
				if (params.get("shijqhrq1").toString().equals("")) {
					error += "第"+(i+1)+"行实际切换日期必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("touflwjh1").toString().equals("")) {
					error += "第"+(i+1)+"行投放令文件号不能为空！ ";
					flag = false;
				}else if (!params.get("touflwjh1").toString().matches("^[A-Za-z0-9]{11}$")) {
					error += "第"+(i+1)+"行投放令文件号不符合条件！ ";
					flag = false;
				}
			}
			if (flag2 == 0) {
				if (params.get("lingjbh2").toString().equals("")) {
					error += "第"+(i+2)+"行零件编号不能为空！ ";
					flag = false;
				}else if (!params.get("lingjbh2").toString().matches("^[A-Za-z0-9]{10}$")) {
					error += "第"+(i+2)+"行零件编号不符合条件！ ";
					flag = false;
				}
				if (params.get("lingjsl2").toString().equals("")) {
					error += "第"+(i+2)+"行零件数量不能为空！ ";
					flag = false;
				}else if (!params.get("lingjsl2").toString().matches("^\\d+(\\.\\d+)?$")) {
					error += "第"+(i+2)+"行零件数量不符合条件！ ";
					flag = false;
				}
				if (params.get("gongys2").toString().equals("")) {
					error += "第"+(i+2)+"行供应商代码不能为空！ ";
					flag = false;
				}else {
					String countYGYS = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("dfpvlingjqh.getCountOfXGYS", params);
					if (countYGYS.equals("0")) {
						error += "第"+(i+2)+"行供应商代码不存在或已失效！ ";
						flag = false;
					}
				}
				if (params.get("shengcx2").toString().equals("")) {
					error += "第"+(i+2)+"行生产线编号不能为空！ ";
					flag = false;
				}else if (!params.get("shengcx2").toString().matches("^[A-Za-z0-9]{5}$")) {
					error += "第"+(i+2)+"行生产线编号不符合条件！ ";
					flag = false;
				}
				if (params.get("shijqhrq2").toString().equals("")) {
					error += "第"+(i+2)+"行实际切换日期必须为日期格式且不能为空！ ";
					flag = false;
				}
				if (params.get("touflwjh2").toString().equals("")) {
					error += "第"+(i+2)+"行投放令文件号不能为空！ ";
					flag = false;
				}else if (!params.get("touflwjh2").toString().matches("^[A-Za-z0-9]{11}$")) {
					error += "第"+(i+2)+"行投放令文件号不符合条件！ ";
					flag = false;
				}
			}
			if (params.get("shenbr").toString().equals("")) {
				error += "申报人不能为空！ ";
				flag = false;
			}
		}
		map.put("flag", flag);
		map.put("error", error);
		return map;
	}
	
}

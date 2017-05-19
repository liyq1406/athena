package com.athena.xqjs.module.maoxq.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Gongyzx;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.entity.maoxq.CompareWeek;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ilorder.service.GongyzxService;
import com.athena.xqjs.module.maoxq.action.NullCalendarCenterException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：MaoxqService
 * <p>
 * 类描述： 毛需求
 * </p>
 * 创建人：Niesy
 * <p>
 * 创建时间：2012-02-08
 * </p>
 * 
 * @version
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class MaoxqCompareService extends BaseService {

	@Inject
	private DiaobshService diaobshService;

	private final Log log = LogFactory.getLog(MaoxqCompareService.class);

	@Inject
	private GongyzxService gongyzxService;

	@Inject
	private GongyzqService gongyzqService;

	/**
	 * <p>
	 * 读取KD周毛需求文件
	 * </p>
	 * 
	 * @author NIESY
	 * @param path
	 * @param xuqly
	 * @param beiz
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Object readExecl(String path, String beiz) throws BiffException, IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		// 读入文件流
		InputStream is = new FileInputStream(new File(path));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		String name = sheet.getName();
		// 行数、列数
		int rows = sheet.getRows();
		int columns = sheet.getColumns();
		List<CompareCyc> list = new ArrayList<CompareCyc>();
		CompareCyc ck = null;
		for (int i = 2; i < rows; i++) {
			// 用户中心
			String usercenter = null;
			// 使用车间
			String shiycj = null;
			// 零件号
			String lingjbh = null;
			// 制造路线
			String zhizlx = null;
			// 单位
			String danw = null;
			for (int j = 0; j < columns; j++) {
				ck = new CompareCyc();
				if (j == 0) {
					shiycj = sheet.getCell(j, i).getContents().trim();
					if (shiycj.isEmpty() || shiycj.length() < 2) {
						usercenter = "";
					} else {
						usercenter = shiycj.substring(0, 2);
					}
				} else if (j == 1) {
					lingjbh = sheet.getCell(j, i).getContents().trim();
				} else if (j == 2) {
					zhizlx = sheet.getCell(j, i).getContents().trim();
				} else if (j == 3) {
					danw = sheet.getCell(j, i).getContents().trim();
				} else {
					ck.setUsercenter(usercenter);
					ck.setLingjbh(lingjbh);
					ck.setZhizlx(zhizlx);
					ck.setShiycj(shiycj);
					String xuqsl = sheet.getCell(j, i).getContents().trim();
					String xuqrq = sheet.getCell(j, 0).getContents().trim();
					// 将读取到数据放入，统一对该行该列进行数据有效性校验
					Map<String, String> mapChk = new HashMap<String, String>();
					// 行号
					mapChk.put("row", "" + (i + 1));
					// 列号
					mapChk.put("column", "" + (j + 1));
					// 用户中心
					mapChk.put("usercenter", usercenter);
					// 零件编号
					mapChk.put("lingjbh", lingjbh);
					// 使用车间
					mapChk.put("shiycj", shiycj);
					// 单位
					mapChk.put("danw", danw);
					// 需求日期
					mapChk.put("xuqrq", xuqrq);
					// 需求数量
					mapChk.put("xuqsl", xuqsl);
					// 制造路线
					mapChk.put("zhizlx", zhizlx);
					if (checkImpData(mapChk).length() != 0) {
						return checkImpData(mapChk);
					}
					// 读取时间格式
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
					// 格式化成YYYY-MM-DD
					SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
					// 日历
					Calendar calendar = sf1.getCalendar();
					// 需求所属周
					String xuqz = calendar.get(Calendar.YEAR)
							+ String.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
					String rq = sf1.format(sf.parse(xuqrq));
					// 需求所属周期
					String xuqsszq = calendar.get(Calendar.YEAR)
							+ String.format("%02d", calendar.get(Calendar.MONTH) + 1);
					BigDecimal sl = new BigDecimal(xuqsl);
					// 添加数据
					ck.setXuqz(xuqz);
					ck.setXuqsszq(xuqsszq);
					ck.setDanw(danw);
					ck.setXuqsl(sl);
					ck.setXuqrq(rq);
					list.add(ck);
				}
			}
		}
		map.put("oldBc", name);
		map.put("list", list);
		return map;
	}

	/**
	 * <p>
	 * 必须项及数据有效性校验
	 * </p>
	 * 
	 * @author NIESY
	 * @param map
	 * @return
	 */
	public String checkImpData(Map<String, String> map) {
		String message = null;
		StringBuffer str = new StringBuffer(1024);
		// 行号
		String row = map.get("row");
		// 列号
		String column = map.get("column");
		// 用户中心
		String usercenter = map.get("usercenter");
		// 零件编号
		String lingjbh = map.get("lingjbh");
		// 使用车间
		String shiycj = map.get("shiycj");
		// 单位
		String danw = map.get("danw");
		// 需求日期
		String xuqrq = map.get("xuqrq");
		// 需求数量
		String xuqsl = map.get("xuqsl");
		// 制造路线
		String zhizlx = map.get("zhizlx");
		// 用户中心
		if (usercenter.equals("") || usercenter.getBytes().length > 3) {
			str.append("\t").append("用户中心数据有误;");
		}
		// 验证零件编号
		if (lingjbh.equals("") || lingjbh.getBytes().length != 10) {
			str.append("\t").append("零件号数据有误;");
		}
		// 验证使用车间
		if (shiycj.equals("") || shiycj.getBytes().length != 3) {
			str.append("\t").append("使用车间数据有误;");
		}
		// 验证零件单位
		if (danw.equals("") || danw.getBytes().length > 3) {
			str.append("\t").append("零件单位数据有误;");
		}
		// 验证制造路线
		if (zhizlx.equals("") || zhizlx.getBytes().length > 3) {
			str.append("\t").append("制造路线数据有误;");
		}
		// 读取时间格式，验证需求日期
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		try {
			sf.parse(xuqrq);
		} catch (ParseException e) {
			str.append("第").append(column).append("列").append("需求日期数据有误;");
		}
		// 验证需求数量
		if (xuqsl.equals("") || xuqsl.getBytes().length != xuqsl.length()) {
			str.append("第").append(column).append("列").append("需求数量数据有误;");
		}
		if (str.length() != 0) {
			message = "第" + row + "行:" + str.toString();
		} else {
			message = "";
		}

		return message;
	}

	/**
	 * <p>
	 * 将读取的数据插入数据库
	 * </p>
	 * 
	 * @author NIESY
	 * @param path
	 * @param xuqly
	 * @param beiz
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String insExcelData(String path, String beiz, String actioner) throws BiffException, IOException,
			ParseException {
		// 当前登录人信息
		// LoginUser loginUser = AuthorityUtils.getSecurityUser();
		Object object = readExecl(path, beiz);
		// 返回值类型
		String type = object.getClass().getName();
		if (type.equals("java.lang.String")) {
			return (String) object;
		}
		Map<String, Object> map = (Map<String, Object>) object;
		// 根据旧版次去查询信息
		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc((String) map.get("oldBc"));
		Maoxq bean = (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.selmxq", maoxq);
		String xuqly = null;
		if (bean != null) {
			xuqly = bean.getXuqly();
		}
		// 创建者
		// String actioner = loginUser.getUsername();
		// 创建时间
		String time = CommonFun.getJavaTime();
		// 生成需求版次
		String xuqbc = this.maoxqBancGeneration(xuqly == null ? "DKS" : xuqly);
		maoxq.setXuqbc(xuqbc);
		maoxq.setXuqly(xuqly == null ? "DKS" : xuqly);
		maoxq.setShengxbz("0");
		maoxq.setCreate_time(time);
		maoxq.setCreator(actioner);
		maoxq.setEdit_time(time);
		maoxq.setEditor(actioner);
		String biaos = beiz;
		beiz += " " + xuqbc + "为复制," + (String) map.get("oldBc") + "为原始版本号。";
		maoxq.setBeiz(beiz);
		maoxq.setActive("1");
		maoxq.setXuqcfsj(time.substring(0, 10));
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insMaoxq", maoxq);
		// List<String> rqLs = new ArrayList<String>();
		// 插入毛需求明细
		List<?> ls = (List<?>) map.get("list");
		/*
		 * for (int i = 0, len = ls.size(); i < len; i++) { CompareCyc c =
		 * (CompareCyc) ls.get(i); rqLs.add(c.getXuqrq()); } Object[] obj =
		 * rqLs.toArray(); Arrays.sort(obj); // 需求开始日期 String xuqksrq = (String)
		 * obj[0]; // 需求结束日期 String xuqjsrq = (String) obj[obj.length - 1];
		 */
		// 插入毛需求明细表
		String rilbc = "";
		int kdIndex = biaos.indexOf("KD");
		int axIndex = biaos.indexOf("AX");
		int lxIndex = biaos.indexOf("LX");
		if (lxIndex == 0) {
			rilbc = "NA01LX01";
		} else if (axIndex == 0) {
			rilbc = "NA01AX01";
		} else if (kdIndex != -1) {
			rilbc = "NA01KD01";
		}
		// TODO
		Map<String,String> vrilMap = new HashMap<String,String>();
		vrilMap.put("usercenter", "UW");
		vrilMap.put("rilbc", rilbc);
		List<Gongyzx> zxls = gongyzxService.queryVGongyzx(vrilMap);
		List<Gongyzq> zqls = gongyzqService.queryVGongyzq(vrilMap);
		for (int i = 0, len = ls.size(); i < len; i++) {
			CompareCyc c = (CompareCyc) ls.get(i);
			
			for (int j = 0; j < zxls.size(); j++) {
				if (c.getXuqrq().compareTo(zxls.get(j).getKaissj()) >= 0 && c.getXuqrq().compareTo(zxls.get(j).getJiessj()) <= 0) {
					c.setXuqz(zxls.get(j).getGongyzx());
					c.setXuqksrq(zxls.get(j).getKaissj());
					c.setXuqjsrq(zxls.get(j).getJiessj());
					break;
				}
			}

			for (int j = 0; j < zqls.size(); j++) {
				if (c.getXuqrq().compareTo(zqls.get(j).getKaissj()) >= 0 && c.getXuqrq().compareTo(zqls.get(j).getJiessj()) <= 0) {
					c.setXuqsszq(zqls.get(j).getGongyzq());
					break;
				}
			}
			c.setXuqbc(xuqbc);
			c.setCreate_time(time);
			c.setCreator(actioner);
			c.setEdit_time(time);
			c.setEditor(actioner);
			c.setId(getUUID());
			// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insMX",
			// c);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("maoxqCompare.insMX", ls);
		return xuqbc;
	}

	/**
	 * <p>
	 * 生成需求版次
	 * </p>
	 * 
	 * @author NIESY
	 * @date 2012-03-07
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean saveAs(CompareCyc cm, String beiz) throws ServiceException {
		// 根据旧版次去查询信息
		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc(cm.getXuqbc());
		Maoxq bean = (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.selmxq", maoxq);

		// 生成需求版次
		String xuqbc = this.maoxqBancGeneration(bean.getXuqly());
		maoxq.setXuqbc(xuqbc);
		maoxq.setXuqly(bean.getXuqly());
		maoxq.setCreate_time(cm.getCreate_time());
		maoxq.setCreator(cm.getCreator());
		maoxq.setEdit_time(cm.getCreate_time());
		maoxq.setEditor(cm.getCreator());
		String bz = beiz + " " + xuqbc + "为复制," + cm.getXuqbc() + "为原始版本号。";
		maoxq.setBeiz(bz);
		maoxq.setShengxbz("0");
		maoxq.setXuqcfsj(cm.getCreate_time().substring(0, 10));
		// ----0012724: 毛需求复制时未复制用户中心 hanwu 20160412 start----
		maoxq.setUsercenter(bean.getUsercenter());
		// ----0012724: 毛需求复制时未复制用户中心 hanwu 20160412 end----
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insMaoxq", maoxq);

		Map<String, String> map = new HashMap<String, String>();
		map.put("xuqbc", xuqbc);
		map.put("create_time", cm.getCreate_time());
		map.put("edit_time", cm.getCreate_time());
		map.put("creator", cm.getCreator());
		map.put("editor", cm.getCreator());
		map.put("oldxuqbc", cm.getXuqbc());
		// 查询
		List<CompareCyc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.saveAsQuery", map);
		for (CompareCyc compareCyc : list) {
			compareCyc.setId(getUUID());
			compareCyc.setXuqbc(xuqbc);
		}
		// 另存明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("maoxqCompare.saveAs", list);
		return true;
	}

	/**
	 * <p>
	 * 生成需求版次
	 * </p>
	 * 
	 * @author NIESY
	 * @date 2012-03-07
	 * @return 版次号
	 */
	public String maoxqBancGeneration(String xuqly) {
		String xuqbc = "";
		Calendar calendar = new GregorianCalendar();
		xuqbc = xuqly + calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.MONTH) + 1)
				+ String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		// 当前时间的最大版次号
		String maxBanc = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.generationBanc", xuqbc);
		if (maxBanc == null) {
			xuqbc = xuqbc + "01";
		} else {
			xuqbc = xuqbc + String.format("%02d", Integer.parseInt(maxBanc.substring(maxBanc.length() - 2)) + 1);
		}
		return xuqbc;
	}

	/**
	 * <p>
	 * 判断毛需求是否生效
	 * </p>
	 * 
	 * @param bean
	 * @return
	 */
	public boolean shifSx(Maoxq bean) {
		Maoxq res = (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.selmxq", bean);
		return "1".equals(res.getShengxbz());
	}
	/**	
	 * 按需毛需求查询
	 * 0007182: 增加按需毛需求查询界面  按需 毛需求查询
	 */
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectAnxMxq(Map<String, String> params, AnxMaoxq bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		if("exportXls".equals(params.get("exportXls"))){
			//0007182  转换字符串，把%20 替换成 空格 ,把%3A替换成:
			bean.setXhsjTo(CommonFun.getStrByAsc(bean.getXhsjTo()));
			bean.setXhsjFrom(CommonFun.getStrByAsc(bean.getXhsjFrom()));
			//0007182 根据条件筛选获取数据量，如果数据量小于5000，则导出全部，否则导出0条
			List<Object> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.selAnxMxqsExport", bean);
			map.put("total", list.size());
			map.put("rows", list);
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.selAnxMxqs", bean, bean);
		}
		return map;
	}
	/**
	 * <p>
	 * 毛需求查询
	 * </p>
	 * 
	 * @author NIESY
	 * @param xqbean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectMxq(Map<String, String> params, Maoxq xqbean) {

		// 毛需求list
		Map<String, Object> map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.selmxq", params, xqbean);
		List<Maoxq> ls = (List<Maoxq>) map.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Maoxq bean = ls.get(i);
			String xuqly = bean.getXuqly() == null ? " " : bean.getXuqly();
			// 周期毛需求
			if (xuqly.lastIndexOf("P") != -1) {
				bean.setXuqlx(Const.MAOXQ_XUQLX_CYC);
				// 周毛需求
			} else if (xuqly.lastIndexOf("S") != -1) {
				bean.setXuqlx(Const.MAOXQ_XUQLX_WEEK);
				// 日毛需求
			} else {
				bean.setXuqlx(Const.MAOXQ_XUQLX_DAYS);
			}
		}
		map.put("rows", ls);
		return map;
	}

	/**
	 * <p>
	 * 毛需求修改备注
	 * </p>
	 * 
	 * @author NIESY
	 * @param list
	 * @param newEditor
	 * @param newEditTime
	 * @return
	 */
	@Transactional
	public boolean updateComment(List<Maoxq> list, String newEditor, String newEditTime) {
		for (int i = 0; i < list.size(); i++) {
			Maoxq bean = list.get(i);
			bean.setNewEditor(newEditor);
			bean.setNewEdit_time(newEditTime);
			// 修改备注
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
					.execute("maoxqCompare.updateMxq", bean);
			if (count == 0) {
				throw new ServiceException(MessageConst.UPDATE_COUNT_0);
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 修改零件数量
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean updateSl(List<CompareCyc> ls, String actioner, String time) {
		boolean flag = true;
		// 修改零件数量
		for (int i = 0, len = ls.size(); i < len; i++) {
			CompareCyc bean = ls.get(i);
			bean.setNewEdit_time(time);
			bean.setNewEditor(actioner);
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.updateSl", bean);
			if (count == 0) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * <p>
	 * 修改跳转查询
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public Map<String, Object> editSlQuery(CompareCyc bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.judge_mode", bean, bean);
	}

	@Transactional
	public void delMx(String newEditor, String newEdittime, List<CompareCyc> ls) {
		for (int i = 0; i < ls.size(); i++) {
			CompareCyc bean = ls.get(i);
			Integer rowCount = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.checkMxUnique", bean);
			if (rowCount != null && rowCount != 0) {
				bean.setNewEditor(newEditor);
				bean.setNewEdit_time(newEdittime);
				bean.setActive("0");
				// 先删毛需求明细子表记录
				int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deletMXFg", bean);
				if (count == 0) {
					throw new ServiceException(MessageConst.DELETE_COUNT_0);
				}
			}

		}
	}

	/**
	 * <p>
	 * 删除毛需求明细
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean delMx(List<CompareCyc> ls, String newEditor, String newEdittime) {
		for (int i = 0; i < ls.size(); i++) {
			CompareCyc bean = ls.get(i);
			Integer rowCount = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.checkMxUnique", bean);
			if (rowCount != null && rowCount != 0) {
				bean.setNewEditor(newEditor);
				bean.setNewEdit_time(newEdittime);
				bean.setActive("0");
				// 先删毛需求明细子表记录
				int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deletMXFg",
						bean);
				if (count == 0) {
					throw new ServiceException(MessageConst.DELETE_COUNT_0);
				}
			}

		}
		return true;

	}

	/**
	 * 选择一版毛需求点击删除 毛需求删除及毛需求明细的删除
	 */
	@Transactional
	public boolean delete(List<CompareCyc> ls, String newEditor, String newEdittime) {
		// 删除毛需求明细
		delMx(newEditor, newEdittime, ls);
		// 删除毛需求主表
		for (int i = 0; i < ls.size(); i++) {
			CompareCyc bean = ls.get(i);
			// 先删毛需求明细子表记录
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deletMaoxqFg", bean);
			if (count == 0) {
				throw new ServiceException(MessageConst.DELETE_COUNT_0);
			}
		}
		return true;
	}

	/**
	 * 判断是存储方式：按用户中心或按产线
	 */
	@SuppressWarnings("unchecked")
	public String storageMode(CompareCyc bean) {
		bean.setRownums("1");
		String mode = null;
		List<CompareCyc> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.judge_mode", bean);
		if (ls.size() == 0) {
			mode = "";
			return mode;
		}
		if (ls.get(0).getChanx() == null) {
			// 按用户中心存储
			mode = "usercenter";
		} else {
			// 按产线存储
			mode = "chanx";
		}
		return mode;
	}

	/**
	 * <p>
	 * 周期、周、日滚动毛需求查询
	 * </p>
	 * <p>
	 * @param mode
	 * </p>
	 * <p>
	 * @param CompareCyc
	 * </p>
	 * <p>
	 * @return map
	 * </p>
	 * <p>
	 * @author NIESY
	 * </p>
	 * <p>
	 * @throws NullCalendarCenterException 
	 * @date 2012-03-07
	 * </p>
	 */
	public Map<String, Object> query(String mode, CompareCyc bean,boolean downFlag) throws NullCalendarCenterException {
		Map<String, Object> ls = null;
		// 取的改版次的开始年份
		String minYear = this.startYearRq(bean.getXuqbc());
		if (minYear == null) {
			return null;
		}
		Map<String, String> map = null;
		// 需求类型
		String xuqlx = bean.getXuqlx();
		if (xuqlx.equalsIgnoreCase("Cyc")) {
			// 周期
			map = this.getCyc(minYear, 15);
		} else if (xuqlx.equalsIgnoreCase("Week")) {
			// 周 //TODO
			map = this.getWeek(minYear, 64, bean);
		} else if (xuqlx.equalsIgnoreCase("Days")) {
			// 日滚动
			Map<String, String> parem = new HashMap<String, String>();
			parem.put("xuqksrq", bean.getXuqksrq());
			map = this.getDays(bean.getXuqbc(), 9,parem);
			if(map==null || map.size() == 0){
				throw new ServiceException("查询时间范围内需求为空！");
			}
		}
		// 需求版次
		map.put("xuqbc", bean.getXuqbc());
		// 用户中心
		map.put("usercenter", bean.getUsercenter());
		// 零件号
		map.put("lingjbh", bean.getLingjbh());
		// 制造路线
		map.put("zhizlx", bean.getZhizlx());
		// 计划员组
		map.put("jihyz", bean.getJihyz());
		boolean flag = bean.getXsfs().equalsIgnoreCase("1");
		boolean flagMode = mode.equalsIgnoreCase("usercenter");
		// 判断是按产线存储还用户中心存储
		if (!downFlag && xuqlx.equalsIgnoreCase("Week") && flagMode && flag) {
			// 按用户中心存储,以需求版次、用户中心、零件号、制作路线、需求所属周期汇总
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.hzChanx" + xuqlx, map, bean);
		} else if (!downFlag && ((!flagMode && !flag) || flagMode)) {
			// 按用户中心存储,以需求版次、用户中心、零件号、制作路线、需求所属周期汇总
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.hzUsercenter" + xuqlx, map, bean);
		} else if (!downFlag&&!flagMode && flag) {
			// 按产线存储,以需求版次、用户中心、零件号、制作路线、使用车间、产线、需求所属周期汇总
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.hzChanx" + xuqlx, map, bean);
		} else if (downFlag && xuqlx.equalsIgnoreCase("Week") && flagMode && flag) {
			ls = new HashMap<String, Object>();
			ls.put("list", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.hzChanx" + xuqlx, map));
		} else if (downFlag && ((!flagMode && !flag) || flagMode)) {
			ls = new HashMap<String, Object>();
			ls.put("list", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.hzUsercenter" + xuqlx, map));
		} else if (downFlag && !flagMode && flag) {
			ls = new HashMap<String, Object>();
			ls.put("list", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.hzChanx" + xuqlx, map));
		}
		return ls;
	}

	/**
	 * <p>
	 * 拼15个月需求所属周期顺序往后推
	 * </p>
	 * <p>
	 * @param minYear
	 * </p>
	 * <p>
	 * @param maxIndex
	 * </p>
	 * <p>
	 * @return map
	 * </p>
	 * <p>
	 * @author NIESY
	 * </p>
	 * <p>
	 * @date 2012-03-07
	 * </p>
	 */
	public Map<String, String> getCyc(String minYear, int maxIndex) {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sf.format(sf.parse(minYear));
		} catch (ParseException e) {
			log.error("日期时间解析错误", e);
			throw new ServiceException("日期时间解析错误", e);
		}
		// 获取日历
		Calendar calendar = sf.getCalendar();
		calendar.set(Calendar.DATE, 1);
		for (int i = 0; i < maxIndex; i++) {
			// 需求所属周期
			String xuqsszq = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.MONTH) + 1);
			map.put("P" + i, xuqsszq);
			// 从开始月顺序加1
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		}

		return map;
	}

	/**
	 * 查询某版毛需求的开始日期
	 */
	@SuppressWarnings("unchecked")
	public String startYearRq(String xuqbc) {
		CompareCyc cyc = new CompareCyc();
		cyc.setXuqbc(xuqbc);
		// 查询该版次里的开始年份
		List<Map<String, String>> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.startYearRQ", cyc);
		// System.out.println(ls.get(0).get("MINYEAR"));
		log.info(ls.get(0).get("MINYEAR"));
		return ls.get(0).get("MINYEAR");
	}

	/**
	 * <p>
	 * 毛需求明细添加记录
	 * </p>
	 * 
	 * @param ls
	 * @param creator
	 * @param creatTime
	 * @return
	 */
	// TODO
	@Transactional
	public String insMxqmx(List<CompareCyc> ls, String creator, String creatTime) {
		log.debug("insMxqmx=============>开始");
		StringBuffer str = new StringBuffer(1024);
		for (int i = 0; i < ls.size(); i++) {
			// 查询添加数据的唯一性
			Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.checkMxUnique", ls.get(i));
			if (count != 0) {
				str.append("需求版次：" + ls.get(i).getXuqbc() + ",");
				str.append("用户中心：" + ls.get(i).getUsercenter() + ",");
				str.append("零件号：" + ls.get(i).getLingjbh() + ",");
				if (ls.get(i).getShiycj() != null) {
					str.append("使用车间：" + ls.get(i).getShiycj() + ",");
				}
				if (ls.get(i).getChanx() != null) {
				str.append("产线：" + ls.get(i).getChanx() + ",");
				}
				str.append("订货路线：" + ls.get(i).getZhizlx() + ",");
				str.append("需求日期：" + ls.get(i).getXuqrq() + "\n");
			}
		}
		// 如果str为空，就做插入操作
		if (str.length() == 0) {
			Map<String, String> vrilMap = new HashMap<String, String>();
			vrilMap.put("xuqbc", ls.get(0).getXuqbc());
			// 毛需求备注
			Maoxq mxqbean = (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.selmxq", vrilMap);
			vrilMap.clear();
			String bz = mxqbean.getBeiz();
			int lxIndex = StringUtils.isEmpty(bz) ? -1 : bz.indexOf("LX");
			for (int i = 0; i < ls.size(); i++) {
				if (ls.get(i).getZhizlx().equals(Const.ZHIZAOLUXIAN_IL) || ls.get(i).getZhizlx().equals(Const.ZHIZAOLUXIAN_GL)) {
					Gongyzx gyzx = gongyzxService.queryGongyzxByRq(ls.get(i).getXuqrq());
					Gongyzq gyzq = gongyzqService.queryGongyzqbyRq(ls.get(i).getXuqrq());
					ls.get(i).setXuqz(gyzx == null ? "" : gyzx.getGongyzx());
					ls.get(i).setXuqsszq(gyzq == null ? "" : gyzq.getGongyzq());
				} else if (lxIndex == 0) {
					vrilMap.put("usercenter", "UW");
					vrilMap.put("rilbc", "NA01LX01");
					vrilMap.put("riq", ls.get(i).getXuqrq());
					List<Gongyzx> zxls = gongyzxService.queryVGongyzx(vrilMap);
					List<Gongyzq> zqls = gongyzqService.queryVGongyzq(vrilMap);
					ls.get(i).setXuqz(zxls.get(0).getGongyzx());
					ls.get(i).setXuqsszq(zqls.get(0).getGongyzq());
					ls.get(i).setXuqksrq((zxls.get(0).getKaissj()).replace("-", ""));
					ls.get(i).setXuqjsrq((zxls.get(0).getJiessj()).replace("-", ""));
				} else if (ls.get(i).getZhizlx().equals(Const.ZHIZAOLUXIAN_KD_PSA)) {
					vrilMap.put("usercenter", "UW");
					vrilMap.put("rilbc", "NA01KD01");
					vrilMap.put("riq", ls.get(i).getXuqrq());
					List<Gongyzx> zxls = gongyzxService.queryVGongyzx(vrilMap);
					List<Gongyzq> zqls = gongyzqService.queryVGongyzq(vrilMap);
					ls.get(i).setXuqz(zxls.get(0).getGongyzx());
					ls.get(i).setXuqsszq(zqls.get(0).getGongyzq());
					ls.get(i).setXuqksrq((zxls.get(0).getKaissj()).replace("-", ""));
					ls.get(i).setXuqjsrq((zxls.get(0).getJiessj()).replace("-", ""));
				} else {
					vrilMap.put("usercenter", "UW");
					vrilMap.put("rilbc", "NA01AX01");
					vrilMap.put("riq", ls.get(i).getXuqrq());
					List<Gongyzx> zxls = gongyzxService.queryVGongyzx(vrilMap);
					List<Gongyzq> zqls = gongyzqService.queryVGongyzq(vrilMap);
					ls.get(i).setXuqz(zxls.get(0).getGongyzx());
					ls.get(i).setXuqsszq(zqls.get(0).getGongyzq());
					ls.get(i).setXuqksrq((zxls.get(0).getKaissj()).replace("-", ""));
					ls.get(i).setXuqjsrq((zxls.get(0).getJiessj()).replace("-", ""));
				}
				// 使用车间
				ls.get(i).setShiycj(ls.get(i).getShiycj() != null ? ls.get(i).getShiycj() : ls.get(i).getChanx().substring(0, 3));
				// 创建人
				ls.get(i).setCreator(creator);
				// 创建时间
				ls.get(i).setCreate_time(creatTime);
				// 修改时间
				ls.get(i).setEdit_time(creatTime);
				// 修改人
				ls.get(i).setEditor(creator);
				ls.get(i).setId(getUUID());
			}
			// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.insMX",
			// ls.get(i));
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("maoxqCompare.insMX", ls);
			str.append("新增成功！");
		} else {
			str.append("已经存在!");
		}
		log.debug("insMxqmx=============>结束");
		return str.toString();
	}

	/**
	 * 毛需求新增明细零件验证
	 * 
	 * @param map
	 * @return
	 */
	public Diaobsqmx getMaoxqLjlx(Map<String, String> map) {
		Diaobsqmx sqmx = (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.querylingjmc", map);
		if (sqmx != null) {
			sqmx.setZhizlx(sqmx.getLux());
			sqmx.setUsercenter(map.get("usercenter"));
			sqmx.setZhizlx((String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.queryMaoxqljlx", sqmx));
		}
		return sqmx;
	}

	public Map<String, String> getWeek(String minYearRq, int maxIndex, CompareCyc bean) throws NullCalendarCenterException  {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xuqbc", bean.getXuqbc());
		// 毛需求备注
		Maoxq mxqbean = (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.selmxq", map);
		map.clear();
		String bz = mxqbean.getBeiz();
		if (bean.getXuqbc().indexOf("DKS") != -1) {
			map = this.getWeekForkd(minYearRq, maxIndex, bean.getZhizlx(), bz);
		} else {
			map = getWeekIl(minYearRq, maxIndex);
		}
		return map;
	}
	/**
	 * <p>
	 * 拼64周
	 * </p>
	 * 
	 * @param minYear
	 * @param maxIndex
	 * @return map
	 * @author Niesy
	 * @throws NullCalendarCenterException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getWeekIl(String minYearRq, int maxIndex) throws NullCalendarCenterException {
		// 该版次毛需求第一年有多少周
		// 开始年份
		String startY = minYearRq.substring(0, 4);
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, Integer.parseInt(startY) + 1);
		// 下一年份
		int nextY = calendar.get(Calendar.YEAR);
		List<CalendarCenter> minWeeks = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryNianZx", startY);
		Map<String, String> map = new HashMap<String, String>();
		try{
			// 该版次毛需求开始日期所在的年周序
			CalendarCenter bgWeek = (CalendarCenter) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.queryNianZx", minYearRq);
			// 获得周
			int index = Integer.parseInt(bgWeek.getNianzx().substring(4, 6));
			for (int i = index; i <= minWeeks.size(); i++) {
				map.put("S" + (i - index), minWeeks.get(i - 1).getNianzx());
			}
			// 下一年，拼maxIndex 减 第一年周数
			for (int i = minWeeks.size() - index + 1; i <= maxIndex; i++) {
				int j = minWeeks.size() - index + 1;
				map.put("S" + i, nextY + String.format("%02d", i - j + 1));
			}
		}catch(Exception e){
			throw new NullCalendarCenterException("无法找到中心日历年周序,请检查中心日历设置");
		}

		
		return map;
	}

	// TODO
	@SuppressWarnings("unchecked")
	public Map<String, String> getWeekForkd(String minYearRq, int maxIndex, String zhizlx, String bz) {
		Map<String, String> map = new HashMap<String, String>();
		String banc = "";
		int lxIndex = StringUtils.isEmpty(bz) ? -1 : bz.indexOf("LX");
		if (lxIndex == 0) {
			banc = "NA01LX01";
		} else if ("97X".equals(zhizlx)) {
			banc = "NA01KD01";
		} else if ("97D".equals(zhizlx)) { // xh 11708 2015-09-10
			banc = "NA01AX01";
		} else {
			banc = "NA01KD01";
		}
		map.put("rq", minYearRq);
		map.put("usercenter", "UW");// AX   LX    KD   日历班次都是国定用户中心UW的
		map.put("banc", banc);
		List<Map<String, String>> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.querykdweek", map);
		map.clear();
		for (int i = 0; i < ls.size(); i++) {
			map.put("S" + i, ls.get(i).get("NIANZX"));
		}
		return map;
	}

	/**
	 * <p>
	 * 需求类型为日J0-J8
	 * </p>
	 * 
	 * @author NIESY
	 * @param beginDate
	 *            PJ开始日期
	 * @param maxIndex
	 * @date 2012-03-07
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDays(String xuqbc, int maxIndex,Map<String,String> paream ) {
		Map<String, String> map = new HashMap<String, String>();
		paream.put("xuqbc", xuqbc);
		List<Map<String, String>> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryJrqDay", paream);
//		if(ls.size()==0){
//			if(paream.get("kaissj")!=null && paream.get("kaissj").length()>0){
//				ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryJrq", xuqbc);
//			}
//		}
		if(ls.size()==0){
			return map;
		}
		
		for (int i = 0; i < maxIndex; i++) {
			// 从JO起始日期顺序递增
			if (ls.size() < maxIndex && i >= ls.size()) {
				if(ls.size()==1 && i==0){
					map.put("J" + i, ls.get(i).get("XUQRQ"));
				}else{
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = new GregorianCalendar();
					if (i >= ls.size() - 1) {
						try {
							sf.format(sf.parse(map.get("J" + (i - 1))));
							calendar = sf.getCalendar();
						} catch (ParseException e) {
							log.error("日期时间解析错误", e);
							throw new ServiceException("日期时间解析错误", e);
						}
					}
					// 获取日期
					calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
					map.put("J" + i, sf.format(calendar.getTime()));
				}
			} else {

				map.put("J" + i, ls.get(i).get("XUQRQ"));
			}

		}
		return map;
	}

	/**
	 * <p>
	 * 周期P0-P4
	 * </p>
	 * <p>
	 * OR周S0-S64
	 * </p>
	 * 
	 * @author NIESY
	 * @param c
	 *            基准
	 * @param c2
	 * @param maxPp
	 * @date 2012-03-08
	 * @return
	 */
	public String setPcompare(BigDecimal c1, BigDecimal c2) {
		// 基准数
		BigDecimal cpi = c1 == null ? BigDecimal.ZERO : c1;
		// 比较数
		BigDecimal c2pi = c2 == null ? BigDecimal.ZERO : c2;
		// 差异
		String pMargin = null;
		// 都为零
		if (cpi == BigDecimal.ZERO && c2pi == BigDecimal.ZERO) {
			pMargin = "0%";
			// 基准为零
		} else if (cpi == BigDecimal.ZERO && c2pi != BigDecimal.ZERO) {
			pMargin = "100%";
			// 基准小于等于比较数
		} else if (cpi.compareTo(c2pi) <= 0) {
			pMargin = c2pi.subtract(cpi).divide(cpi, 2, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100)).setScale(0) + "%";
			// 基准数大于比较数
		} else if (cpi.compareTo(c2pi) > 0) {
			pMargin = c2pi.subtract(cpi).divide(cpi, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(0) + "%";
		}

		return pMargin;
	}

	/**
	 * <p>
	 * 当比较为空时
	 * </p>
	 * 
	 * @param ls
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<?> compareFsZero(List<?> ls) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String className = ls.get(0).getClass().getName();
		PropertyDescriptor pd1 = null;
		PropertyDescriptor pd2 = null;
		PropertyDescriptor pMargin = null;
		List compareList = new ArrayList();
		// 传值BEAN DTO
		Object result = null;
		if (className.equals("com.athena.xqjs.entity.maoxq.CompareCyc")) {
			for (int i = 0; i < ls.size(); i++) {
				CompareCyc c = (CompareCyc) ls.get(i);
				for (int j = 0; j < 5; j++) {
					pd1 = new PropertyDescriptor("p" + j + "Cyc1", c.getClass());
					pd2 = new PropertyDescriptor("p" + j + "Cyc2", c.getClass());
					pMargin = new PropertyDescriptor("p" + j + "Margin", c.getClass());
					// get PI or Si
					Method piGet = pd1.getReadMethod();
					Method piSet = pd1.getWriteMethod();
					// set比较数量
					Method p2Set = pd2.getWriteMethod();
					// set差异方法
					Method piMargin = pMargin.getWriteMethod();
					// 基准数量
					BigDecimal jzsl = (BigDecimal) piGet.invoke(c) == null ? BigDecimal.ZERO : (BigDecimal) piGet.invoke(c);
					piSet.invoke(c, jzsl);
					p2Set.invoke(c, BigDecimal.ZERO);
					piMargin.invoke(c, setPcompare(jzsl, BigDecimal.ZERO));
					result = c;
				}
				compareList.add(result);
			}
		} else {
			for (int i = 0; i < ls.size(); i++) {
				for (int j = 0; j < 64; j++) {
					CompareWeek c = (CompareWeek) ls.get(i);
					pd1 = new PropertyDescriptor("p" + j + "Week1", c.getClass());
					pd2 = new PropertyDescriptor("p" + j + "Week2", c.getClass());
					pMargin = new PropertyDescriptor("p" + j + "Margin", c.getClass());
					// get PI or Si
					Method piGet = pd1.getReadMethod();
					Method piSet = pd1.getWriteMethod();
					// set比较数量
					Method p2Set = pd2.getWriteMethod();
					// set差异方法
					Method piMargin = pMargin.getWriteMethod();
					// 基准数量
					BigDecimal jzsl = (BigDecimal) piGet.invoke(c) == null ? BigDecimal.ZERO : (BigDecimal) piGet
							.invoke(c);
					piSet.invoke(c, jzsl);
					p2Set.invoke(c, BigDecimal.ZERO);
					piMargin.invoke(ls.get(i), setPcompare(jzsl, BigDecimal.ZERO));
					result = c;
				}
				compareList.add(result);
			}
		}

		return compareList;
	}

	/**
	 * <p>
	 * 毛需求比较，显示全部差异
	 * </p>
	 * 
	 * @author NIESY
	 * @param cFirst
	 * @param cSecond
	 * @param index
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> compareListMerge(List<?> cFirst, List<?> cSecond) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		List compareList = new ArrayList();
		// 获取对象的类名
		String className = null;
		if (cFirst.size() == 0 && cSecond.size() != 0) {
			className = cSecond.get(0).getClass().getName();
		} else if (cFirst.size() == 0 && cSecond.size() == 0) {
			return diaobshService.listToMap(compareList);
		} else if (cSecond.size() == 0 && cFirst.size() != 0) {
			return diaobshService.listToMap(compareFsZero(cFirst));
		} else {
			className = cFirst.get(0).getClass().getName();
		}
		if (className.equals("com.athena.xqjs.entity.maoxq.CompareCyc")) {
			List<CompareCyc> fc = (List<CompareCyc>) cFirst;
			List<CompareCyc> fs = (List<CompareCyc>) cSecond;
			// 将基准的用户中心、零件号、制作路线作为Key放入Map
			for (int i = 0, len = fc.size(); i < len; i++) {
				CompareCyc c = (CompareCyc) fc.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				map.put(key, c);
			}
			// 将比较的用户中心、零件号、制作路线作为Key放入Map2
			for (int i = 0, len = fs.size(); i < len; i++) {
				CompareCyc c = (CompareCyc) fs.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				map2.put(key, c);
			}
			// 如果该零件在基准里有，在比较里没有
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Iterator<Map.Entry<String, Object>> iterator = set.iterator(); iterator.hasNext();) {
				Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
				if (!map2.containsKey(entry.getKey())) {
					List ls = new ArrayList();
					ls.add(entry.getValue());
					List rs = compareFsZero(ls);
					compareList.addAll(rs);
					ls = null;
					rs = null;
				}

			}

			// 将两个list的记录合并到新到list
			for (int i = 0, len = fs.size(); i < len; i++) {
				// 传值BEAN DTO
				CompareCyc result = null;
				CompareCyc c = (CompareCyc) fs.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				PropertyDescriptor pd1 = null;
				PropertyDescriptor pd2 = null;
				PropertyDescriptor pde = null;
				PropertyDescriptor pMargin = null;
				// 获取基准bean
				// 判断基准版次里是否有这个Key
				CompareCyc e = (CompareCyc) map.get(key);
				for (int j = 0; j < 5; j++) {
					if (map.containsKey(key)) {
						pd1 = new PropertyDescriptor("p" + j + "Cyc1", c.getClass());
						pd2 = new PropertyDescriptor("p" + j + "Cyc2", e.getClass());

						pde = new PropertyDescriptor("p" + j + "Cyc1", e.getClass());

						pMargin = new PropertyDescriptor("p" + j + "Margin", e.getClass());
						// get PI or Si
						Method piGet = pd1.getReadMethod();
						Method peGet = pde.getReadMethod();
						Method peSet = pde.getWriteMethod();
						// set pi2 OR SI2
						Method piSet = pd2.getWriteMethod();
						// set差异方法
						Method piMargin = pMargin.getWriteMethod();
						// 基准数量
						BigDecimal jzsl = (BigDecimal) peGet.invoke(e) == null ? BigDecimal.ZERO : (BigDecimal) peGet
								.invoke(e);
						peSet.invoke(e, jzsl);
						// 取得比较数量
						BigDecimal slpi = (BigDecimal) piGet.invoke(c);
						piSet.invoke(e, slpi);
						piMargin.invoke(e, setPcompare(jzsl, slpi));
						result = e;
					} else {
						pd1 = new PropertyDescriptor("p" + j + "Cyc1", c.getClass());
						pd2 = new PropertyDescriptor("p" + j + "Cyc2", c.getClass());
						pMargin = new PropertyDescriptor("p" + j + "Margin", c.getClass());
						// get PI or Si
						Method piGet = pd1.getReadMethod();
						Method piSet = pd1.getWriteMethod();
						// set pi2 OR SI2
						Method piSet2 = pd2.getWriteMethod();
						// set差异方法
						Method piMargin = pMargin.getWriteMethod();
						// 取得数量,将第基准设置为零；将基准数量设给比较数量
						BigDecimal slpi = (BigDecimal) piGet.invoke(c) == null ? BigDecimal.ZERO : (BigDecimal) piGet
								.invoke(c);
						piSet.invoke(c, BigDecimal.ZERO);
						piSet2.invoke(c, slpi);
						piMargin.invoke(c, setPcompare(BigDecimal.ZERO, slpi));
						result = c;
					}
				}

				compareList.add(result);
			}
		} else {
			List<CompareWeek> fc = (List<CompareWeek>) cFirst;
			List<CompareWeek> fs = (List<CompareWeek>) cSecond;
			// 将基准的用户中心、零件号、制作路线作为Key放入Map
			for (int i = 0, len = fc.size(); i < len; i++) {
				CompareWeek c = (CompareWeek) fc.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				map.put(key, c);
			}
			// 将基准的用户中心、零件号、制作路线作为Key放入Map
			for (int i = 0, len = fs.size(); i < len; i++) {
				CompareWeek c = (CompareWeek) fs.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				map2.put(key, c);
			}
			// 如果该零件在基准里有，在比较里没有
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Iterator<Map.Entry<String, Object>> iterator = set.iterator(); iterator.hasNext();) {
				Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
				if (!map2.containsKey(entry.getKey())) {
					List ls = new ArrayList();
					ls.add(entry.getValue());
					List rs = compareFsZero(ls);
					compareList.addAll(rs);
					ls = null;
					rs = null;
				}

			}
			// 将两个list的记录合并到新到list
			for (int i = 0, len = fs.size(); i < len; i++) {
				// 传值BEAN
				CompareWeek result = null;
				CompareWeek c = (CompareWeek) fs.get(i);
				String key = c.getUsercenter() + c.getLingjbh() + c.getZhizlx() + c.getChanx();
				PropertyDescriptor pd1 = null;
				PropertyDescriptor pd2 = null;
				PropertyDescriptor pde = null;
				PropertyDescriptor pMargin = null;
				// 获取基准bean
				// 判断基准版次里是否有这个Key
				CompareWeek e = (CompareWeek) map.get(key);
				for (int j = 0; j < 64; j++) {
					if (map.containsKey(key)) {
						pd1 = new PropertyDescriptor("s" + j + "Week1", c.getClass());
						pde = new PropertyDescriptor("s" + j + "Week1", e.getClass());
						pd2 = new PropertyDescriptor("s" + j + "Week2", e.getClass());
						pMargin = new PropertyDescriptor("s" + j + "Margin", e.getClass());
						// get PI or Si
						Method piGet = pd1.getReadMethod();
						// 获取基准bean 里的基准数量
						Method peGet = pde.getReadMethod();
						Method peSet = pde.getWriteMethod();
						// set 基准bean里的数量2
						Method piSet = pd2.getWriteMethod();
						// set差异方法
						Method piMargin = pMargin.getWriteMethod();
						// 基准数量
						BigDecimal jzsl = (BigDecimal) peGet.invoke(e) == null ? BigDecimal.ZERO : (BigDecimal) peGet
								.invoke(e);
						peSet.invoke(e, jzsl);
						// 取得数量
						BigDecimal slpi = (BigDecimal) piGet.invoke(c) == null ? BigDecimal.ZERO : (BigDecimal) piGet
								.invoke(c);
						piSet.invoke(e, slpi);
						piMargin.invoke(e, setPcompare(jzsl, slpi));
						result = e;
					} else {
						pd1 = new PropertyDescriptor("s" + j + "Week1", c.getClass());
						pd2 = new PropertyDescriptor("s" + j + "Week2", c.getClass());
						pMargin = new PropertyDescriptor("s" + j + "Margin", c.getClass());
						// get PI or Si
						Method piGet = pd1.getReadMethod();
						Method piSet = pd1.getWriteMethod();
						// set差异方法
						Method piMargin = pMargin.getWriteMethod();
						// set pi2 OR SI2
						Method piSet2 = pd2.getWriteMethod();
						// 取得数量,将第基准设置为零；将基准数量设给比较数量
						BigDecimal slpi = (BigDecimal) piGet.invoke(c) == null ? BigDecimal.ZERO : (BigDecimal) piGet
								.invoke(c);
						piSet.invoke(c, BigDecimal.ZERO);
						piSet2.invoke(c, slpi);
						piMargin.invoke(c, setPcompare(BigDecimal.ZERO, slpi));
						result = c;
					}

				}
				// 如果该零件在基准里有，在比较里没有
				if (!map.containsKey(key) && e != null) {
					List ls = new ArrayList();
					ls.add(e);
					List rs = compareFsZero(ls);
					compareList.addAll(rs);
					ls = null;
				}
				compareList.add(result);
			}
		}

		return diaobshService.listToMap(compareList);
	}

	/**
	 * <p>
	 * 查询两个版次及差异比较
	 * </p>
	 * 
	 * @param cFirst
	 * 
	 * @param cSecond
	 * 
	 * @param sqlId
	 * 
	 * @param map
	 * 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 */
	public Map<String, Object> queryComTwo(String sqlId, Map<String, String> map) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		String xuqbc1 = map.get("xuqbc1");
		map.remove("xuqbc1");
		// 基准数据
		List<?> cFirst = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(sqlId, map);
		map.remove("xuqbc");
		map.put("xuqbc1", xuqbc1);

		// 比较数据
		List<?> cSecond = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(sqlId, map);
		return compareListMerge(cFirst, cSecond);

	}

	/**
	 * <p>
	 * 需求类型为周期P0-P4的比较
	 * </p>
	 * <p>
	 * 需求类型为周期s0-s64的比较
	 * </p>
	 * 
	 * @author NIESY
	 * @param beginDate
	 *            PJ开始日期
	 * @param maxIndex
	 * @date 2012-03-07
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws NullCalendarCenterException 
	 * @throws Exception
	 */
	// TODO
	public Map<String, Object> comparePpOrPs(Map<String, String> map, CompareCyc bean, boolean downLoadFlag) throws IllegalArgumentException, IntrospectionException, IllegalAccessException,
			InvocationTargetException, NullCalendarCenterException {
		// 比较返回结果
		Map<String, Object> mapResult = null;
		// 以cFirst为基准进行比较
		String sqlIdCenterCyc = "maoxqCompare.compareCenterCyc";
		String sqlIdChanxCyc = "maoxqCompare.compareChanxCyc";
		String sqlIdUsercenterWeek = "maoxqCompare.compareUsercenterWeek";
		String sqlIdhzChanxWeek = "maoxqCompare.compareChanxWeek";
		// SQL id
		String id = null;
		boolean flag = map.get("xuqlx").equalsIgnoreCase("Cyc");
		boolean flagMode = map.get("mode").equalsIgnoreCase("usercenter");
		boolean xsflag = ("1").equalsIgnoreCase(map.get("xsfs"));
		// 取的改版次的开始年份
		String minYear1 = this.startYearRq(map.get("xuqbc"));
		String minYear2 = this.startYearRq(map.get("xuqbc1"));
		String rq = null;
		// 如果两个明细都为空，直接返回
		if (minYear1 == null && minYear2 == null) {
			List ls = new ArrayList();
			mapResult = diaobshService.listToMap(ls);
			return mapResult;
		} else if (minYear1 == null) {
			rq = minYear2;
		} else if (minYear2 == null) {
			rq = minYear1;
		} else {
			int k = minYear1.compareTo(minYear2);
			rq = k <= 0 ? minYear1 : minYear2;
		}
		Map<String, String> maprq = null;

		if ((flag && flagMode) || (flag && !flagMode && !xsflag)) {
			maprq = this.getCyc(rq, 15);
			map.putAll(maprq);
			id = sqlIdCenterCyc;
		} else if (flag && !flagMode && xsflag) {
			maprq = this.getCyc(rq, 15);
			map.putAll(maprq);
			id = sqlIdChanxCyc;
		} else if ((!flag && flagMode) || (!flag && !flagMode && !xsflag)) {
			// 周
			maprq = this.getWeek(rq, 64, bean);
			map.putAll(maprq);
			id = sqlIdUsercenterWeek;
		} else if (!flag && !flagMode && xsflag) {
			// 周
			maprq = this.getWeek(rq, 64, bean);
			map.putAll(maprq);
			id = sqlIdhzChanxWeek;
		}

		if (!downLoadFlag) {
			mapResult = queryCompare(id, map, bean);
			mapResult.put("kaisrq", rq);
			mapResult.put("rows", compareResult((List<?>) mapResult.get("rows"))); 
		} else {
			mapResult = new HashMap<String, Object>();
			List<?> list = queryCompareDownLoad(id, map);
			mapResult.put("list", compareResult(list));
		}
		return mapResult;
	}

	/**
	 * 毛需求比较-全连接查询
	 * 
	 * @param id
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> queryCompare(String id, Map<String, String> map, Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(id, map, page);
	}

	/**
	 * 毛需求比较-全连接查询
	 * 
	 * @param id
	 * @param map
	 * @param page
	 * @return
	 */
	public List<?> queryCompareDownLoad(String id, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(id, map);
	}

	/**
	 * 比较结果
	 * 
	 * @param ls
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	public List<?> compareResult(List<?> ls) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		List<?> resultLs = null;
		String className = ls.get(0).getClass().getName();
		if (className.equals("com.athena.xqjs.entity.maoxq.CompareCyc")) {

			//xss 20161014 v4_008
			List<CompareCyc> cyc_list = (List<CompareCyc>) ls;
			for (int i=0; i<cyc_list.size();i++){
				CompareCyc cycs = cyc_list.get(i); 
				
				if(cycs.getGongysbh()==null && cycs.getZhizlx()==null  ){
					cycs.setGongysbh(" ");
				}else if( cycs.getGongysbh()==null && cycs.getZhizlx().equals("97W")  ){
					cycs.setGongysbh("M000000000");
				}else if (cycs.getGongysbh()==null && cycs.getZhizlx().equals("97D") ){
					cycs.setGongysbh("5200000000");
				}else if(cycs.getGongysbh()==null && cycs.getZhizlx().equals("97X") ){
					cycs.setGongysbh("7800000000");
				}else if(cycs.getGongysbh()==null && cycs.getZhizlx().equals("UGB")  ){
					cycs.setGongysbh("M000000000");
				} 
				
				if(cycs.getGcbh()==null && cycs.getZhizlx()==null ){
					cycs.setGcbh(" ");
				}else if( cycs.getGcbh()==null && cycs.getZhizlx().equals("97W") ){
					cycs.setGcbh("M000000000");
				}else if ( cycs.getGcbh()==null && cycs.getZhizlx().equals("97D") ){
					cycs.setGcbh("5200000000");
				}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("97X") ){
					cycs.setGcbh("7800000000");
				}else if(cycs.getGcbh()==null && cycs.getZhizlx().equals("UGB")  ){
					cycs.setGcbh("M000000000");
				}
				
			}
			
			resultLs = cycResult(ls, 4);
		} else {
			resultLs = weekResult(ls, 64);
		}
		return resultLs;
	}

	/**
	 * 毛需求周比较
	 * 
	 * @param ls
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CompareWeek> weekResult(List<?> ls, int zs) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<CompareWeek> week = (List<CompareWeek>) ls;
		for (int j = 0; j <= zs; j++) {
			PropertyDescriptor pd1 = null;
			PropertyDescriptor pd2 = null;
			PropertyDescriptor pMargin = null;
			for (CompareWeek object : week) {
				// 获取基准bean
				pd1 = new PropertyDescriptor("s" + j + "Week1", object.getClass());
				pd2 = new PropertyDescriptor("s" + j + "Week2", object.getClass());
				pMargin = new PropertyDescriptor("s" + j + "Margin", object.getClass());
				// get PI or Si
				Method piGet = pd1.getReadMethod();
				Method peGet = pd2.getReadMethod();
				// set差异方法
				Method piMargin = pMargin.getWriteMethod();
				// 基准数量
				BigDecimal jzsl = (BigDecimal) piGet.invoke(object);
				// 取得比较数量
				BigDecimal slpi = (BigDecimal) peGet.invoke(object);
				piMargin.invoke(object, setPcompare(jzsl, slpi));
			}
		}
		return week;
	}

	/**
	 * 毛需求周期比较
	 * 
	 * @param ls
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CompareCyc> cycResult(List<?> ls, int zqs) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<CompareCyc> cyc = (List<CompareCyc>) ls;
		for (int j = 0; j <= zqs; j++) {
			PropertyDescriptor pd1 = null;
			PropertyDescriptor pd2 = null;
			PropertyDescriptor pMargin = null;
			for (CompareCyc object : cyc) {
				pd1 = new PropertyDescriptor("p" + j + "Cyc1", object.getClass());
				pd2 = new PropertyDescriptor("p" + j + "Cyc2", object.getClass());
				pMargin = new PropertyDescriptor("p" + j + "Margin", object.getClass());
				// get PI or Si
				Method piGet = pd1.getReadMethod();
				Method peGet = pd2.getReadMethod();
				// set差异方法
				Method piMargin = pMargin.getWriteMethod();
				// 基准数量
				BigDecimal jzsl = (BigDecimal) piGet.invoke(object);
				// 取得比较数量
				BigDecimal slpi = (BigDecimal) peGet.invoke(object);
				piMargin.invoke(object, setPcompare(jzsl, slpi));
			}
		}
		return cyc;
	}

	/**
	 * <p>
	 * 是否指定CMJ版本
	 * </p>
	 * <p>
	 * 是否指定运输时刻
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean appointTc(List<Maoxq> mls, List<CompareCyc> ls, Map<String, String> map) {
		boolean flag = true;
		// 产线是否为空
		// String appoint = "and chanx is null";
		for (CompareCyc compareCyc : ls) {
			Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.checkMxUnique", compareCyc);
			compareCyc.setXuqlx(map.get("appoint"));
			Integer rowCount = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.checkMxUnique", compareCyc);
			if (rowCount != 0 && count != 0) {
				flag = false;
				break;
			}
		}
		// 如果产线不为空，更新是否指定CMJ版本
		if (flag) {
			for (Maoxq compareCyc : mls) {
				// 修改人
				compareCyc.setNewEdit_time(map.get("newEdittime"));
				// 修改时间
				compareCyc.setNewEditor(map.get("newEditor"));
				// 指定工业开始时间
				compareCyc.setZdgyzqfrom(map.get("zdgyzqfrom"));
				// 指定结束月份
				compareCyc.setZdgyzqto(map.get("zdgyzqto"));
				compareCyc.setShifjscmj(map.get("shifjscmj"));
				compareCyc.setShifzdyssk(map.get("shifzdyssk"));

				// 更新SHIFZDCMJ字段
				int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.updateMxq", compareCyc);
				if (count < 1) {
					throw new ServiceException(MessageConst.UPDATE_COUNT_0);
				} else {
					compareCyc.setShifjscmj(map.get("shifjscmj").equals("1") ? "0" : "");
					compareCyc.setShifzdyssk(map.get("shifzdyssk").equals("1") ? "0" : "");
					compareCyc.setEdit_time("");
					compareCyc.setEditor("");
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.updateMxqAtOnly", compareCyc);
				}
			}
		}

		return flag;
	}
	
	
	//xss_0012943
	//更新当前用户中心的锁状态
	@Transactional
	public boolean updateJisState(Map<String, String> map) { 
	   boolean flag = false; 
	  
	   int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.updateJisState", map);
	   
	   if (count == 0) {
		   throw new ServiceException(MessageConst.DELETE_COUNT_0); 
	   }else{
		   flag = true; 
	   } 
	   
	   return flag;
	}
	
	
	//是否有其他用户中心正在比较周期毛需求
	public boolean maoxqSfbj() { 
		boolean flag = false; 
		Map<String, String> map = null;
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.QueryMaoxqSfbj", map);
		 
	    if( count > 0 ){//存在
		   flag = true; 
		} 
		
		return flag;
	}
	
	
	
}

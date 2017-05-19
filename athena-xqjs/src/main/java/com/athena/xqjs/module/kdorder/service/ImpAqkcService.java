package com.athena.xqjs.module.kdorder.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ImpAqkcService
 * <p>
 * 类描述：导入订货安全库存
 * </p>
 * 创建人：NIESY
 * <p>
 * 创建时间：2012-03-05
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class ImpAqkcService extends BaseService {
	@Inject
	private YicbjService yService;

	/**
	 * 导入订货安全库存分页查询
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> select(Pageable page, Map<String, String> map) {
		
		Map<String, String> kcmap = (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdimp.queryZykzb");
		if (null==map.get("dinghsj") || "".equals(map.get("dinghsj"))) {
			map.put("ziyhqrq", kcmap.get("ZIYHQRQ") == null ? "" : kcmap.get("ZIYHQRQ"));
		} else {
			map.put("ziyhqrq", map.get("dinghsj"));
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kdimp.querykc", map, page);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> select(Map<String, String> map) {
		Map<String, String> kcmap = (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdimp.queryZykzb");

		if ("".equals(map.get("dinghsj"))) {
			map.put("ziyhqrq", kcmap.get("ZIYHQRQ") == null ? "" : kcmap.get("ZIYHQRQ"));
		} else {
			map.put("ziyhqrq", map.get("dinghsj"));
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdimp.expkc", map);
	}

	/**
	 * <p>
	 * 修改安全库存
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean updateKc(List<Lingjck> ls, String newEditor, String time) {
		for (Lingjck ck : ls) {
			// 修改人
			ck.setNewEditor(newEditor);
			// 修改时间
			ck.setNewEditTime(time);
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdimp.updatekc", ck);
			if (ck.getEditor() != null && count == 0) {
				throw new ServiceException("KD件导入订货安全库存刷新零件仓库设置表" + ck.getUsercenter() + "用户中心" + ck.getLingjbh() + "零件编号" + ck.getCangkbh() + "仓库编号有误!" );
			} else if (count == 0) {
				String jismk = "32";
				String lingjbh = ck.getLingjbh();
				String cuowlx = Const.CUOWLX_200;
				String cuowxxxx = "KD件导入订货安全库存刷新零件仓库设置表" + ck.getUsercenter() + "用户中心" + ck.getLingjbh() + "零件编号" + ck.getCangkbh() + "仓库编号有误!";
				yService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			}
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdimp.updatezykzb", ck);
			if(count == 0)
			{
				String jismk = "32";
				String lingjbh = ck.getLingjbh();
				String cuowlx = Const.CUOWLX_200;
				String cuowxxxx = "KD件导入订货安全库存刷新资源快照表" + ck.getUsercenter() + "用户中心" + ck.getLingjbh() + "零件编号" + ck.getCangkbh() + "仓库编号" + ck.getZiyhqrq() +"资源获取日期有误!";
				yService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 读取导入的订货安全库存
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 **/

	public List<Lingjck> importExc(String filePath) throws BiffException, IOException {

		// 读入文件流
		InputStream is = new FileInputStream(new File(filePath));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		// 行数、列数
		int rows = sheet.getRows();
		int columns = sheet.getColumns();
		List<Lingjck> list = new ArrayList<Lingjck>();
		Lingjck ck = null;
		// 从第三行开始读
		for (int i = 2; i < rows; i++) {
			ck = new Lingjck();
			ck.setXittzz(BigDecimal.ZERO);
		
			for (int j = 0; j < columns; j++) {
				switch (j) {
				case 0:
					// 读取用户中心
					String usercenter = sheet.getCell(j, i).getContents().trim();
					ck.setUsercenter(usercenter);
					break;
				case 1:
					// 读取零件编号
					String lingjbh = sheet.getCell(j, i).getContents().trim();
					ck.setLingjbh(lingjbh);
					break;
				case 2:
					// 读取仓库编号
					String cangkbh = sheet.getCell(j, i).getContents().trim();
					ck.setCangkbh(cangkbh);
					break;
				case 3:
					// 读取车间
					String chej = sheet.getCell(j, i).getContents().trim();
					ck.setChej(chej);
					break;
				case 4:
					// 读取车间
					String kuc = sheet.getCell(j, i).getContents().trim();
					ck.setKuc(kuc);
					break;
				case 5:
					// 读取安全库存
					String jistzz = sheet.getCell(j, i).getContents().trim();
					ck.setJistzzStr(jistzz);
					//ck.setJistzz(anqkcsl.isEmpty() ? BigDecimal.ZERO : new BigDecimal(anqkcsl));
					break;
				case 6:
					// 读取订单累计
					String dingdlj = sheet.getCell(j, i).getContents().trim();
					ck.setDingdljStr(dingdlj);
					//ck.setDingdlj(dingdlj.isEmpty() ? BigDecimal.ZERO : new BigDecimal(dingdlj));
					break;
				case 7:
					// 读取交付累计
					String jiaoflj = sheet.getCell(j, i).getContents().trim();
					ck.setJiaofljStr(jiaoflj);
					//ck.setJiaoflj(jiaoflj.isEmpty() ? BigDecimal.ZERO : new BigDecimal(jiaoflj));
					break;
				case 8:
					// 读取终止累计
					String zhongzlj = sheet.getCell(j, i).getContents().trim();
					ck.setZhongzljStr(zhongzlj);
					//ck.setZhongzlj(zhongzlj.isEmpty() ? BigDecimal.ZERO : new BigDecimal(zhongzlj));
					break;
				case 9:
					// 读取终止累计
					String jihy = sheet.getCell(j, i).getContents().trim();
					ck.setJihy(jihy);
					break;
				case 10:
					// 读取资源获取日期
					String ziyhqrq = sheet.getCell(j, i).getContents().trim();
					if(StringUtils.isNotBlank(ziyhqrq))
					{
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
						try 
						{
							ziyhqrq = CommonFun.getJavaTime(formatter.parse(ziyhqrq));
						} 
						catch (ParseException e)
						{
							formatter = new SimpleDateFormat("yy-MM-dd");
							try {
								ziyhqrq = CommonFun.getJavaTime(formatter.parse(ziyhqrq));
							} catch (ParseException e1) {
								ziyhqrq = "error";
							}
							
						}
					}
					ck.setZiyhqrq(ziyhqrq);
					break;
				default:
					break;
				}
			}
			if(StringUtils.isBlank(ck.getUsercenter()) 
					&& StringUtils.isBlank(ck.getLingjbh())
					&& StringUtils.isBlank(ck.getCangkbh())
					&& StringUtils.isBlank(ck.getZiyhqrq())
					&& StringUtils.isBlank(ck.getDingdljStr())
					&& StringUtils.isBlank(ck.getJiaofljStr())
					&& StringUtils.isBlank(ck.getZhongzljStr())
					&& StringUtils.isBlank(ck.getJiaofljStr())
					&& StringUtils.isBlank(ck.getKuc())
					&& StringUtils.isBlank(ck.getChej())
					&& StringUtils.isBlank(ck.getJihy())
			)
				continue;
			list.add(ck);
		}
		return list;
	}

}

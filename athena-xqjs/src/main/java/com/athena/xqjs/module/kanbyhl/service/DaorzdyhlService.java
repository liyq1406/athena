package com.athena.xqjs.module.kanbyhl.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 看板计算导入最大要货量
 * 
 * @date 2012-1-4
 */
@Component
public class DaorzdyhlService extends BaseService {

	/**
	 * 查询表中所有的看板循环规模
	 * 
	 * @return list
	 */
	public List<?> queryKanbxhgm() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.selectAll");
	}

	/**
	 * 读取execl将execl中的数据存放到list中
	 * 
	 * @param lujing
	 * @return list
	 * @throws BiffException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Kanbxhgm> readMulu(String lujing) throws BiffException, IOException, ParseException {
		List<Kanbxhgm> list = new ArrayList<Kanbxhgm>();
		InputStream is = new FileInputStream(new File(lujing));
		// 取得工作薄
		jxl.Workbook rwb = Workbook.getWorkbook(is);
		// 取得工作表
		Sheet st = rwb.getSheet(Const.KANBXH_ZUIDYHL);
		// 获得execl行数
		int i = st.getRows();
		// 获得execl列数
		int j = st.getColumns();
		for (int x = 0; x < i; x++) {
			Kanbxhgm kb = new Kanbxhgm();
			for (int y = 0; y < j; y++) {
				switch (y) {
				// 获取excel第一列的值
				case Const.SHEETCELL1:
					// 获取单元格内容并去空格
					// 循环编码
					kb.setXunhbm(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL2:
					// 获取excel第二列的值
					// 零件号
					kb.setLingjbh(st.getCell(y, x).getContents().trim());
					break;
				case Const.SHEETCELL3:
					// 获取exel第三列的值
					BigDecimal bd = new BigDecimal(st.getCell(y, x).getContents().trim());
					// 最大要货量
					kb.setZuidyhl(bd);
					break;
				}
			}
			// 加入list
			list.add(kb);
		}
		return list;
	}

	/**
	 * 将导入execl中数据更新到数据库中
	 * 
	 * @param lujing
	 * @return message
	 * @throws BiffException
	 * @throws IOException
	 * @throws ParseException
	 */
	
	public String updateKanb(String lujing) throws BiffException, IOException, ParseException {
		String message = "导入成功！";
		int count = 0;
		List<?> list = this.queryKanbxhgm();
		// 获得从execl里读取的数据
		List<Kanbxhgm> kanbList = this.readMulu(lujing);
		// breakPoint k
		K: for (int i = 0; i < list.size(); i++) {
			Kanbxhgm kanbxh = (Kanbxhgm) list.get(i);
			for (int n = 0; n < kanbList.size(); n++) {
				Kanbxhgm kanbxhgm = (Kanbxhgm) kanbList.get(n);
				// 比对
				if (kanbxh.getXunhbm().equals(kanbxhgm.getXunhbm())
						&& kanbxh.getLingjbh().equals(kanbxhgm.getLingjbh())) {
					// 维护人
					kanbxhgm.setWeihr("AAA");
					// 获取java时间,设置维护时间
					kanbxhgm.setWeihsj(CommonFun.getJavaTime());
					// 更新看板计算最大要货量
					count =updateZuidyhl(kanbxhgm);
					if (count == 0) {
						// count等于零，跳出所有循环
						message = Const.KANBXH_DATAERRO;
						break K;
					}
				}
			}

		}
		return message;
	}
	@Transactional(rollbackFor = Exception.class)
	public Integer updateZuidyhl(Kanbxhgm kanbxhgm)throws BiffException, IOException, ParseException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateYjfzl",
				kanbxhgm);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateZuidyhl",
				kanbxhgm);
		return count;
	} 
	
}
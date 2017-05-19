package com.athena.ckx.module.baob.service;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.athena.ckx.entity.baob.Lingjus;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;


@Component
public class LingjusService extends BaseService<Lingjus> {
	
	
	
	/**
	 * 获得命名空间
	 * @author xryuan
	 * @date 2013-3-25
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	public Map<String,Object> query(Lingjus bean,Map<String, String> map){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querylingjus",map, bean);
	}
	
	
	/**
	 * 查询零件状态
	 */
	public List queryztsx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryzhuangtsx");
	}
	
	
	/**
	 * 零件US/KD件
	 */
	public List<Map<String, String>> select(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryexplingjus", map);
	}
	
	/**
	 * <p>
	 * 读取导出零件US/KD件
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean exportExcel(HttpServletResponse response, List<Map<String, String>> list) {
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename=lingjus.xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "零件US/KD件"; // 标题
			WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			// wcfFC.setBackground(Colour.AQUA);
			wsheet.addCell(new Label(4, 0, tmptitle, wcfFC));
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("USERCENTER", "用户中心");
			map.put("LINGJBH", "零件号");
			map.put("ZHUANGTSX", "状态");
			map.put("LINGJSL", "零件数量");
			map.put("USXH", "US代码");
			map.put("USRL", "US容量");
			map.put("CANGKBH", "仓库代码");
			map.put("ZICKBH", "子仓库代码");
			map.put("CANGKLX", "仓库类型");
			map.put("USH", "US编码");
			map.put("ELH", "EL号");
			map.put("RUKRQ", "入库时间");
			// 开始生成主体内容
			Set<Map.Entry<String, String>> set = map.entrySet();
			int temp = 0;
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				wsheet.addCell(new Label(temp++, 1, entry.getValue()));
			}

			temp = 0;// 向excel单元格里写数据
			for (int i = 0; i < list.size(); i++) {
				temp = temp == map.size() ? 0 : temp;
				for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
					Object obj = list.get(i).get(entry.getKey());
					String content = obj == null ? "" : String.valueOf(obj);
					wsheet.addCell(new Label(temp++, i + 2, content));
				}
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	
}

package com.athena.xqjs.module.kdorder.service;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.athena.component.service.BaseService;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ExpAqkcService
 * <p>
 * 类描述：导出库存
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
public class ExpAqkcService extends BaseService {


	/**
	 * <p>
	 * 读取导出订货安全库存
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
			response.setHeader("Content-disposition", "attachment; filename=dinghanqkc.xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "订货安全库存"; // 标题
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
			map.put("CANGKBH", "订货仓库");
			map.put("DINGHCJ", "订货车间");
			map.put("LINGJSL", "库存");
			map.put("JISTZZ", "计算调整值");
			map.put("DINGDBDZL", "订单累计");
			map.put("YIJFZL", "交付累计");
			map.put("ZHONGZZL", "终止累计");
			map.put("JIHY", "计划员代码");
			map.put("SYSTIME", "资源的截止日期");

			// 开始生成主体内容
			Set<Map.Entry<String, String>> set = map.entrySet();
			int temp = 0;
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				wsheet.addCell(new Label(temp++, 1, entry.getValue()));
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			WritableCellFormat wcf = new WritableCellFormat(new DateFormat("yyyy/MM/dd"));
			temp = 0;// 向excel单元格里写数据
			for (int i = 0; i < list.size(); i++) {
				temp = temp == map.size() ? 0 : temp;
				for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
					Object obj = list.get(i).get(entry.getKey());
					String content = obj == null ? "" : String.valueOf(obj);
					if("SYSTIME".equalsIgnoreCase(entry.getKey()))
					{
						try {
							 date = dateFormat.parse(content);
						} catch (ParseException e) {
							throw new Exception("com.athena.xqjs.module.common.CommonFun.getDayTime() Error!!!");
						}
						wsheet.addCell(new DateTime(temp++, i + 2, date,wcf));
					}
					else
					{
						wsheet.addCell(new Label(temp++, i + 2, content));
					}
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

package com.athena.xqjs.module.ilorder.service;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * IL周期订单修改及生效
 * </p>
 * 
 * @author NIESY
 * 
 * @date 2012-03-26
 */
@SuppressWarnings("rawtypes")
@Component
public class ExpEffectOrderService extends BaseService {

	// 日志打印信息
	private final Log log = LogFactory.getLog(ExpEffectOrderService.class);

	private final Map<String, String> ddmap = new LinkedHashMap<String, String>();

	public ExpEffectOrderService() {
		this.ddmap.put("USERCENTER", "用户中心");
		this.ddmap.put("DINGDH", "订单号");
		this.ddmap.put("JISZQ", "计算周期");
		this.ddmap.put("HETH", "合同号");
		this.ddmap.put("DINGDLX", "订单类型");
		this.ddmap.put("GONGYSDM", "供应商代码");
		this.ddmap.put("GONGYSLX", "供应商类型");
		this.ddmap.put("CHULLX", "订单类别");
		this.ddmap.put("ZHUANGT", "订单状态");
		this.ddmap.put("DINGDSXCZR", "订单生效操作人");
		this.ddmap.put("DINGDSXSJ", "订单生效时间");
		this.ddmap.put("DINGDFSSJ", "订单发送时间");
		this.ddmap.put("SHIFFSGYS", "是否发送供应商");
		this.ddmap.put("BEIZ", "备注");
		this.ddmap.put("DINGDJSSJ", "订单计算时间");
		this.ddmap.put("DINGDNR", "订单内容");
		this.ddmap.put("ZIYHQRQ", "资源获取日期");
		this.ddmap.put("FAHZQ", "发货周期");
		this.ddmap.put("MAOXQBC", "毛需求版次");
		this.ddmap.put("JISLX", "系统计算(admin)、用户提交(提交者用户名)");
		this.ddmap.put("SHIFZFSYHL", "是否只发送要货令");
		this.ddmap.put("SHIFYJSYHL", "是否已计算要货令");
		this.ddmap.put("EDITOR", "修改人");
		this.ddmap.put("EDIT_TIME", "修改时间");
		this.ddmap.put("CREATOR", "创建人");
		this.ddmap.put("CREATE_TIME", "创建时间");
	}
	/**
	 * <p>
	 * 导出订货安全库存
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean exportExcel(HttpServletResponse response, List<Map<String, Object>> list, List<Map<String, Object>> dingd) {
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename=dingdexp.xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "IL周期订单"; // 标题
			WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			// wcfFC.setBackground(Colour.AQUA);

			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("DINGDH", "订单号");
			map.put("LINGJBH", "零件号");
			map.put("USERCENTER", "用户中心");
			map.put("GONGYSDM", "供应商代码");
			map.put("GONGYSLX", "供应商类型");
			map.put("YAOHQSRQ", "要货起始日期");
			map.put("YAOHJSRQ", "要货结束日期");
			map.put("CANGKDM", "仓库代码");
			map.put("XIEHZT", "卸货站台");
			map.put("FAHD", "发货地");
			map.put("DINGHCJ", "订货车间");
			map.put("SHUL", "数量（订单明细表达数量）");
			map.put("DANW", "单位");
			map.put("UABZLX", "UA包装类型");
			map.put("UABZUCLX", "UA包装内UC类型");
			map.put("UABZUCSL", "UA包装内UC数量");
			map.put("UABZUCRL", "UA包装内UC容量");
			map.put("FAYRQ", "发运日期");
			map.put("JIAOFRQ", "交付日期");
			map.put("ZHUANGT", "状态");
			// map.put("YUGSFQZ", "预告是否取整");
			map.put("LEIX", "类型(9既定，8预告)");
			map.put("LUJDM", "路径代码");
			map.put("JIHYZ", "计划员组");
			map.put("ZUIHWHR", "最后维护人");
			map.put("ZUIHWHSJ", "最后维护时间");
			map.put("JISSL", "计算数量（订单明细）");
			map.put("GONGHLX", "供货类型");
			map.put("YIJFL", "已交付量（零件仓库）");
			map.put("YIZZL", "已终止量（零件仓库）");
			map.put("yaohlgszl", "要货令个数总量（订单明细）");
			map.put("yaohlgsyjf", "要货令个数已交付（订单明细）");
			map.put("yaohlgsyzz", "要货令个数已终止（订单明细）");
			map.put("YIJFLDDMX", "已交付量订单明细");
			map.put("YIZZLDDMX", "已终止量订单明细");
			map.put("SHID", "时段");
			map.put("ZUIZDHSJ", "最早到货时间");
			map.put("ZUIWDHSJ", "最晚到货时间");
			map.put("XIAOHSJ", "消耗时间");
			map.put("XIAOHCBHSJ", "小火车备货时间");
			map.put("XIAOHCSXSJ", "小火车上线时间");
			map.put("XIEHD", "按需卸货点");
			map.put("XIANBYHLX", "线边要货类型：K/R");
			map.put("USBAOZLX", "US包装类型");
			map.put("USBAOZRL", "US包装容量");
			map.put("XIAOHCH", "小火车号");
			map.put("XIAOHCCXH", "小火车车厢号");
			map.put("CHANX", "产线");
			map.put("XIAOHD", "消耗点");
			map.put("FENPXH", "分配循环");
			wsheet.addCell(new Label((map.size() - 1) / 2 + 1, 0, tmptitle, wcfFC));
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);
			int temp = 0;
			// 开始生成主体内容,订单
			int len = dingd.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> mapDd = dingd.get(i);
				mapDd.remove("ROWNUM_");
				mapDd.remove("JIHYZ");
				Set<Entry<String, Object>> setDd = mapDd.entrySet();
				for (Iterator<Map.Entry<String, Object>> iterator = setDd.iterator(); iterator.hasNext();) {
					Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
					Object obj = entry.getValue();
					String content = obj == null ? "" : String.valueOf(obj);
					if (ddmap.get(entry.getKey()) != null) {
						wsheet.addCell(new Label(temp, i + 1, ddmap.get(entry.getKey())));
						wsheet.addCell(new Label(temp++, i + 2, content));
					}
				}
			}
			// 开始生成主体内容,订单明细列名
			temp = 0;
			Set<Map.Entry<String, String>> set = map.entrySet();
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				wsheet.addCell(new Label(temp++, len + 2, entry.getValue()));
			}

			temp = 0;// 向excel单元格里写数据
			for (int i = 0; i < list.size(); i++) {
				temp = temp == map.size() ? 0 : temp;
				for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
					Object obj = list.get(i).get(entry.getKey());
					String content = obj == null ? "" : String.valueOf(obj);
					wsheet.addCell(new Label(temp++, len + i + 3, content));
				}
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
			return true;
		} catch (Exception ex) {
			log.info(ex.getMessage());
			return false;
		}
	}

}

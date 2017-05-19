package com.athena.pc.module;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.pc.entity.Beic;
import com.athena.pc.module.exchange.ExportFactory;
import com.athena.pc.module.exchange.ExporterHeadInfo;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;

public class ZXCTestGood extends AbstractCompomentTests {

	@Inject
	protected AbstractIBatisDao baseDao;

	private Map<String, String> params;
	private Map<String, Object> jsonResult;
	private ExporterHeadInfo headerInfo;

	private int sNum = 0;
	@Inject
	private ExportFactory exportFactory;

	@Rule
	public ExternalResource resource = new ExternalResource() {
		@Override
		protected void before() throws Throwable {
			params = new HashMap<String, String>();
//			params.put("FilePath", "users" + File.separator + "athOut");
			params.put("FilePath", "/users/athOut");
			params.put("FileName", "zxc_test_good.xls");
			params.put("UC", "UW");
			params.put("CREATOR", "INITUW");
			params.put("type", "2");
		};
	};

	@Test
	public void testCKX_LINGJ() {
		String[] head = { "用户中心", "零件编号", "零件中文名称", "零件法文名称", "零件类型(A,B,C)",
				"零件属性(A零件,K卷料,M辅料)", "计划员组", "单位", "制造路线", "开始日期", "结束日期",
				"装车系数", "安全码", "零件重量", "标识", "关键零件级别", "订货车间", "工艺编码", "总成类代码",
				"第一次启运时间", "创建人", "创建时间", "修改人", "修改时间", "物流员", "质检员", "排产计划员",
				"物流工艺员" };
		String[] properties = { "USERCENTER", "LINGJBH", "ZHONGWMC", "FAWMC",
				"LINGJLX", "LINGJSX", "JIHY", "DANW", "ZHIZLX", "KAISRQ",
				"JIESRQ", "ZHUANGCXS", "ANQM", "LINGJZL", "BIAOS", "GUANJLJJB",
				"DINGHCJ", "GONGYBM", "ZONGCLDM", "DIYCQYSJ", "CREATOR",
				"CREATE_TIME", "EDITOR", "EDIT_TIME", "WULY", "ZHIJY", "PCJHY",
				"WLGYY" };
		String[] propertiesshow = { "USERCENTER", "LINGJBH", "ZHONGWMC",
				"FAWMC", "LINGJLX", "LINGJSX", "JIHY", "DANW", "ZHIZLX",
				"KAISRQ", "JIESRQ", "ZHUANGCXS", "ANQM", "LINGJZL", "BIAOS",
				"GUANJLJJB", "DINGHCJ", "GONGYBM", "ZONGCLDM", "DIYCQYSJ",
				"CREATOR", "CREATE_TIME:date", "EDITOR", "EDIT_TIME:date", "WULY",
				"ZHIJY", "PCJHY", "WLGYY" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_LINGJ");
		params.put("msg", "零件表");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_LINGJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_PEISLB() {
		String[] head = { "配送类型", "配送类型名称", "包装类型", "包装零件数量/流水号数量", "最长等待车位数",
				"同步/集配/包装同步/标识", "上线点（物流消耗点）", "配套上线标识", "备货提前期", "小货车车厢层",
				"是否归集(按零件) 1是 0否", "备注(左/右/车型等)", "是否生成备货单 1是 0否", "仓库编号",
				"标识", "创建人", "创建时间", "修改人", "修改时间", "子仓库编号", "物流工艺员", "用户中心" };
		String[] properties = { "PEISLX", "PEISLXMC", "BAOZLX", "BAOZSL",
				"ZUICDDCWS", "TONGBJPBS", "SHANGXD", "PEITSXBS", "BEIHTQQ",
				"XIAOHCCXC", "SHIFGJ", "BEIZ", "SHIFBHD", "CANGKBH", "BIAOS",
				"CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME", "ZICKBH",
				"WLGYY", "USERCENTER" };
		String[] propertiesshow = { "PEISLX", "PEISLXMC", "BAOZLX", "BAOZSL",
				"ZUICDDCWS", "TONGBJPBS", "SHANGXD", "PEITSXBS", "BEIHTQQ",
				"XIAOHCCXC", "SHIFGJ", "BEIZ", "SHIFBHD", "CANGKBH", "BIAOS",
				"CREATOR", "CREATE_TIME:date", "EDITOR", "EDIT_TIME:date", "ZICKBH",
				"WLGYY", "USERCENTER" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_PEISLB");
		params.put("msg", "配送类别");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_PEISLB", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_TONGBJPLJ() {
		String[] head = { "用户中心", "零件编号", "生产线编号", "CLASS", "VALUE", "零件名称",
				"配送类型", "前后车标识,-1前车,0当前车", "集配包装位置", "创建人", "创建时间", "修改人",
				"修改时间", "组号" };
		String[] properties = { "USERCENTER", "LINGJBH", "SHENGCXBH", "NCLASS",
				"NVALUE", "LINGJMC", "PEISLX", "QIANHCBS", "JIPBZWZ",
				"CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME", "ZUH" }; 
		String[] propertiesshow = { "USERCENTER", "LINGJBH", "SHENGCXBH",
				"NCLASS", "NVALUE", "LINGJMC", "PEISLX", "QIANHCBS", "JIPBZWZ",
				"CREATOR", "CREATE_TIME:date", "EDITOR", "EDIT_TIME:date", "ZUH" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_TONGBJPLJ");
		params.put("msg", "同步集配零件分类");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_TONGBJPLJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KUW() {
		String[] head = { "用户中心", "仓库编号", "子仓库编号", "库位编号", "库位等级编号", "库位状态",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "CANGKBH", "ZICKBH", "KUWBH",
				"KUWDJBH", "KUWZT", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "CANGKBH", "ZICKBH", "KUWBH",
				"KUWDJBH", "KUWZT", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KUW");
		params.put("msg", "库位");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KUW", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KUWDJ() {
		String[] head = { "用户中心", "仓库编号", "库位等级编号", "长", "宽", "高",
				"标识 1有效 0失效", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "CANGKBH", "KUWDJBH", "CHANG",
				"KUAN", "GAO", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "CANGKBH", "KUWDJBH",
				"CHANG", "KUAN", "GAO", "BIAOS", "CREATOR", "CREATE_TIME:date",
				"EDITOR", "EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KUWDJ");
		params.put("msg", "库位等级");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KUWDJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KUWDJ_BAOZ() {
		String[] head = { "用户中心", "仓库编号", "库位等级编号", "包装类型", "包装个数", "创建人",
				"创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "CANGKBH", "KUWDJBH", "BAOZLX",
				"BAOZGS", "CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "CANGKBH", "KUWDJBH",
				"BAOZLX", "BAOZGS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KUWDJ_BAOZ");
		params.put("msg", "库位等级与包装");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KUWDJ_BAOZ", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_WULDRQGX() {
		String[] head = { "用户中心", "", "1-产线，2-其他", "容器区编号", "创建人", "创建时间",
				"修改人", "修改时间" };
		String[] properties = { "USERCENTER", "WULD", "WULDLX", "WULD2",
				"CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "WULD", "WULDLX", "WULD2",
				"CREATOR", "CREATE_TIME:date", "EDITOR", "EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_WULDRQGX");
		params.put("msg", " ");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_WULDRQGX", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_GONGYXHD() {
		String[] head = { "工艺消耗点", "距EMON点的车身数量", "车身数*(节拍的倒数)*60", "按需截止流水号",
				"工艺是否失效,1有效,0失效", "是否失效,1有效,0失效", "创建人", "创建时间", "修改人", "修改时间",
				"组号", "生产线编号", "容器场编号" };
		String[] properties = { "GONGYXHD", "CHESSL", "PIANYSJ", "LIUSH",
				"GONGYBS", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME", "ZUH", "SHENGCXBH", "RONGQCBH" };
		String[] propertiesshow = { "GONGYXHD", "CHESSL", "PIANYSJ", "LIUSH",
				"GONGYBS", "BIAOS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date", "ZUH", "SHENGCXBH", "RONGQCBH" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_GONGYXHD");
		params.put("msg", "工艺消耗点");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_GONGYXHD", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_YANSBLLJ() {
		String[] head = { "用户中心", "零件编号", "供应商编号", "验收项编号", "验收项说明", "验收比例",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "LINGJBH", "GONGYSBH", "YANSXBH",
				"YANSXSM", "YANSBL", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "LINGJBH", "GONGYSBH",
				"YANSXBH", "YANSXSM", "YANSBL", "CREATOR", "CREATE_TIME:date",
				"EDITOR", "EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_YANSBLLJ");
		params.put("msg", "零件验收比例设置");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_YANSBLLJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KEHCZM() {
		String[] head = { "用户中心", "客户编号", "账户",
				"跟踪成本:A35,不跟踪成本:A36,移库:A70（客户类型为仓库时，才能选择移库）",
				"客户类型:1:分配循环，2:仓库，3:其它客户", "标识 1有效 0失效", "创建人", "创建时间", "修改人",
				"修改时间" };
		String[] properties = { "USERCENTER", "KEHBH", "ZHANGH", "CAOZM",
				"KEHLX", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "KEHBH", "ZHANGH", "CAOZM",
				"KEHLX", "BIAOS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KEHCZM");
		params.put("msg", "客户操作码");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KEHCZM", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_PRINT_DEVICEGROUP() {
		String[] head = { "用户中心", "打印机组编号", "打印机组名称", "打印机组描述",
				"是否启用优先级,1为启用，0为不启用，默认0", "标识,0无效，1有效，默认1", "创建人", "创建时间",
				"修改人", "修改时间" };
		String[] properties = { "USERCENTER", "SPCODES", "SNAME", "SDESC",
				"NFLAG", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "SPCODES", "SNAME", "SDESC",
				"NFLAG", "BIAOS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_PRINT_DEVICEGROUP");
		params.put("msg", "03打印机组：逻辑删除");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"outputgoodzxc.queryCKX_PRINT_DEVICEGROUP", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_PRINT_DEVICE() {
		String[] head = { "用户中心", "打印机编号", "打印机组编号", "打印机名称", "打印机IP", "端口",
				"优先级", "描述", "创建人", "创建时间", "修改人", "修改时间", "替代打印机编号" };
		String[] properties = { "USERCENTER", "SPCODE", "SPCODES", "SNAME",
				"SIP", "SPORT", "NLEVEL", "SDESC", "CREATOR", "CREATE_TIME",
				"EDITOR", "EDIT_TIME", "REPLACESPCODE" };
		String[] propertiesshow = { "USERCENTER", "SPCODE", "SPCODES", "SNAME",
				"SIP", "SPORT", "NLEVEL", "SDESC", "CREATOR", "CREATE_TIME:date",
				"EDITOR", "EDIT_TIME:date", "REPLACESPCODE" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_PRINT_DEVICE");
		params.put("msg", "打印设备：物理删除");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_PRINT_DEVICE", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_PRINT_USERINFO() {
		String[] head = { "用户中心", "用户编号", "用户组编号", "用户名称", "用户类型:1,员工;2,仓库",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "USERCODE", "USERSCODE", "SNAME",
				"USERTYPE", "CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "USERCODE", "USERSCODE",
				"SNAME", "USERTYPE", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_PRINT_USERINFO");
		params.put("msg", "用户明细");
		jsonResult = new HashMap<String, Object>();
		jsonResult
				.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
						"outputgoodzxc.queryCKX_PRINT_USERINFO", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_FAHZT() {
		String[] head = { "用户中心", "发货站台编号", "发货站台名称", "仓库编号", "标识 1有效 0失效",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "FAHZTBH", "FAHZTMC", "CANGKBH",
				"BIAOS", "CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "FAHZTBH", "FAHZTMC",
				"CANGKBH", "BIAOS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_FAHZT");
		params.put("msg", "发货站台");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_FAHZT", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KEHB() {
		String[] head = { "客户编号", "客户名称", "客户性质(内/外)", "联系人", "地址", "邮政编号",
				"电话", "传真", "备注", "标识", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "KEHBH", "KEHMC", "KEHXZ", "LIANXR", "DIZ",
				"YOUZBH", "DIANH", "CHUANZ", "BEIZ", "BIAOS", "CREATOR",
				"CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "KEHBH", "KEHMC", "KEHXZ", "LIANXR", "DIZ",
				"YOUZBH", "DIANH", "CHUANZ", "BEIZ", "BIAOS", "CREATOR",
				"CREATE_TIME:date", "EDITOR", "EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KEHB");
		params.put("msg", "客户表");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KEHB", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_KEH_CHENGPK() {
		String[] head = { "用户中心(出发地)", "仓库编号(出发地)", "客户编号(目的地)",
				"供应商编号/承运商编号/运输商编号", "运输路线编号", "要车提前期(分钟)", "客户提前期(分钟)",
				"是否需要配载", "备货类型", "订单提前期", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "CANGKBH", "KEHBH", "CHENGYSBH",
				"YUNSLXBH", "YAOCTQQ", "KEHTQQ", "SHIFPZ", "BEIHLX",
				"DINGDTQQ", "CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "CANGKBH", "KEHBH",
				"CHENGYSBH", "YUNSLXBH", "YAOCTQQ", "KEHTQQ", "SHIFPZ",
				"BEIHLX", "DINGDTQQ", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_KEH_CHENGPK");
		params.put("msg", "客户成品库关系");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_KEH_CHENGPK", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_LINGJKH() {
		String[] head = { "用户中心", "零件编号", "客户编号", "客户零件号", "客户零件名称", "发货站台",
				"零件包装UA", "UA包装中零件数量", "零件包装UC", "UC包装中零件数量", "生效日期", "失效日期",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "LINGJBH", "KEHBH", "KEHLJH",
				"KEHLJMC", "FAHZT", "UABAOZ", "UALJSL", "UCBAOZ", "UCLJSL",
				"SHENGXRQ", "SHIXRQ", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "LINGJBH", "KEHBH", "KEHLJH",
				"KEHLJMC", "FAHZT", "UABAOZ", "UALJSL", "UCBAOZ", "UCLJSL",
				"SHENGXRQ", "SHIXRQ", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_LINGJKH");
		params.put("msg", "零件客户关系");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_LINGJKH", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_LINGJGYS() {
		String[] head = { "用户中心", "供应商编号/承运商编号/运输商编号", "零件编号", "供应合同号", "供应份额",
				"有效期", "发运地", "生效时间", "失效时间", "最小起订量", "参考阀值", "质检抽检比例",
				"是否验证批次号", "供应商UC的包装类型", "供应商UC的容量", "供应商UA的包装类型",
				"供应商UA里UC的个数", "盖板", "内衬", "是否存在临时包装 1是 0否", "创建人", "创建时间",
				"修改人", "修改时间", "标识（1 有效", "0 失效）", "是否失效管理", "SAP份额",
				"份额是否变化标识（0未变化 1变化）" };
		String[] properties = { "USERCENTER", "GONGYSBH", "LINGJBH",
				"GONGYHTH", "GONGYFE", "YOUXQ", "FAYD", "SHENGXSJ", "SHIXSJ",
				"ZUIXQDL", "CANKFZ", "ZHIJCJBL", "SHIFYZPCH", "UCBZLX", "UCRL",
				"UABZLX", "UAUCGS", "GAIB", "NEIC", "SHIFCZLSBZ", "CREATOR",
				"CREATE_TIME", "EDITOR", "EDIT_TIME", "BIAOS", "SHIFSXGL",
				"SAP_FENE", "FLAG" };
		String[] propertiesshow = { "USERCENTER", "GONGYSBH", "LINGJBH",
				"GONGYHTH", "GONGYFE", "YOUXQ", "FAYD", "SHENGXSJ", "SHIXSJ",
				"ZUIXQDL", "CANKFZ", "ZHIJCJBL", "SHIFYZPCH", "UCBZLX", "UCRL",
				"UABZLX", "UAUCGS", "GAIB", "NEIC", "SHIFCZLSBZ", "CREATOR",
				"CREATE_TIME:date", "EDITOR", "EDIT_TIME:date", "BIAOS", "SHIFSXGL",
				"SAP_FENE", "FLAG" };
		
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_LINGJGYS");
		params.put("msg", "接口只有国产件");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_LINGJGYS", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_RONGQC() {
		String[] head = { "用户中心", "容器场编号", "描述", "标识 1有效 0失效", "创建人", "创建时间",
				"修改人", "修改时间", "容器场类型 R, F, J", "记账区编号" };
		String[] properties = { "USERCENTER", "RONGQCBH", "MIAOS", "BIAOS",
				"CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME", "SHIFFK",
				"JIZQBH" };
		String[] propertiesshow = { "USERCENTER", "RONGQCBH", "MIAOS", "BIAOS",
				"CREATOR", "CREATE_TIME:date", "EDITOR", "EDIT_TIME:date", "SHIFFK",
				"JIZQBH" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_RONGQC");
		params.put("msg", "容器场");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_RONGQC", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

	@Test
	public void testCKX_DANJDY() {
		String[] head = { "用户中心", "客户编号", "6:位仓库+子仓库", "UC,US,备货单,发货通知",
				"是否打印", "打印联数", "打印份数", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "KEHBH", "CANGKBH", "DANJLX",
				"SHIFDY", "DAYLS", "DAYFS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		String[] propertiesshow = { "USERCENTER", "KEHBH", "CANGKBH", "DANJLX",
				"SHIFDY", "DAYLS", "DAYFS", "CREATOR", "CREATE_TIME:date", "EDITOR",
				"EDIT_TIME:date" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);

		params.put("sheetName", "PC_CKX_DANJDY");
		params.put("msg", "单据打印");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgoodzxc.queryCKX_DANJDY", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}

}

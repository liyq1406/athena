package com.athena.pc.module;


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

public class BATestGood extends AbstractCompomentTests {

	@Inject
	protected AbstractIBatisDao baseDao;

	private Map<String,String> params;
	private Map<String, Object> jsonResult;
	private ExporterHeadInfo headerInfo;

	private int sNum = 0;
	@Inject
	private ExportFactory exportFactory;

    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
        	params = new HashMap<String,String>();
        	params.put("FilePath", "/users/athOut");
        	params.put("FileName", "zbc_test_good.xls");
        	params.put("UC", "UW");
        	params.put("CREATOR", "INITUW");
        	params.put("type", "2");
        };   
    }; 

	@Test
	public void testCKX_CALENDAR_TEAM(){
		String [] head = {"编组号","星期序号","序号","开始时间","截止时间","班","调整时间","创建人","创建时间","修改人","修改时间","标识"};
		String [] properties = {"BIANZH","XINGQXH","XUH","KAISSJ","JIEZSJ","BAN","TIAOZSJ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","BIAOS"};
		String [] propertiesshow = {"BIANZH","XINGQXH","XUH","KAISSJ","JIEZSJ","BAN","TIAOZSJ","CREATOR","CREATE_TIME:date","EDITOR","EDIT_TIME:date","BIAOS"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		headerInfo.setPropertiesshow(propertiesshow);
		
		params.put("sheetName", "PC_CKX_CALENDAR_TEAM");
		params.put("msg", "工作时间");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_CALENDAR_TEAM", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_JIAOFMZD(){
		String [] head = {"用户中心","交付码","说明","备注","标识","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","JIAOFM","SHUOM","BEIZ","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_JIAOFMZD");
		params.put("msg", "交付码字典");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_JIAOFMZD", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_JIAOFMSD(){
		String [] head = {"用户中心","交付码","周序号","星期序号","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","JIAOFM","ZHOUXH","XINGQXH","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_JIAOFMSD");
		params.put("msg", "交付码设定");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_JIAOFMSD", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_CANGKXHSJ(){
		String [] head = {"用户中心","仓库编号","循环/仓库","模式","仓库送货频次F","仓库送货时间","仓库返回时间","备货时间","I类型备货时间","P类型备货时间","是否自动补货","标识","创建人","创建时间","修改时间","组号"};
		String [] properties = {"USERCENTER","CANGKBH","FENPQHCK","MOS","CANGKSHPCF","CANGKSHSJ","CANGKFHSJ","BEIHSJ","IBEIHSJ","PBEIHSJ","SHIFZDBH","SHENGXBS","CREATOR","CREATE_TIME","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_CANGKXHSJ");
		params.put("msg", "仓库循环时间");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_CANGKXHSJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_WAIBMS(){
		String [] head = {"用户中心","零件编号","目的地","模式","指定供应商","将来模式","生效时间","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","LINGJBH","FENPQH","MOS","ZHIDGYS","JIANGLMS","SHENGXSJ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_WAIBMS");
		params.put("msg", "外部模式");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_WAIBMS", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_LINGJCK(){
		String [] head = {"用户中心","零件编号","仓库编号","子仓库编号","原仓库编号","卸货站台编号","安全库存天数","安全库存数量","最大库存天数","最大库存数量","订单表达总量","订单终止总量","已交付总量","系统调整值","单取库编号","单取库位编号","单取库位最大上限","单取库位最小下限","计算调整数值","最小起订量 (仓库)","是否有零件序列号","备用子仓库编号","定置库位","仓库US的包装类型","仓库US的包装容量","上线UC类型","上线UC容量","FIFO","看板是否自动发送","创建人","创建时间","修改人","修改时间","组号"};
		String [] properties = {"USERCENTER","LINGJBH","CANGKBH","ZICKBH","YUANCKBH","XIEHZTBH","ANQKCTS","ANQKCSL","ZUIDKCTS","ZUIDKCSL","DINGDBDZL","DINGDZZZL","YIJFZL","XITTZZ","DANQKBH","DANQKW","ZUIDSX","ZUIXXX","JISTZZ","ZUIXQDL","SHIFXLH","BEIYKBH","DINGZKW","USBZLX","USBZRL","UCLX","UCRL","FIFO","ZIDFHBZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_LINGJCK");
		params.put("msg", "零件仓库");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryckx_lingjck", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

//	@Test
//	public void testCKX_BAOZ(){
//		String [] head = {"包装类型","长度","宽度","高度","包装重量","材质","包装名称","是否回收","类别","折叠高度","堆垛高度","摆放方向","标识","创建人","创建时间","修改人","修改时间"};
//		String [] properties = {"BAOZLX","CHANGD","KUAND","GAOD","BAOZZL","CAIZ","BAOZMC","SHIFHS","LEIB","ZHEDGD","DUIDCS","BAIFFX","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "PC_CKX_BAOZ");
//		params.put("msg", "包装");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_BAOZ", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//	}
	
	@Test
	public void testCKX_FENPQ(){
		String [] head = {"用户中心","生产线编号","分配区编号","分配区名称","标识","创建人","创建时间","修改人","修改时间","物流工艺员组编号"};
		String [] properties = {"USERCENTER","SHENGCXBH","FENPQH","FENPQMC","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","WULGYYZ"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_FENPQ");
		params.put("msg", "分配区");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_FENPQ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_GONGYXHD(){
		String [] head = {"工艺消耗点","生产线编号","距EMON点的车身数量","距EMON点偏移时间","按需截至流水号","工艺标识","标识","创建人","创建时间","修改人","修改时间","组号"};
		String [] properties = {"GONGYXHD","SHENGCXBH","CHESSL","PIANYSJ","LIUSH","GONGYBS","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_GONGYXHD");
		params.put("msg", "工艺消耗点 ");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_GONGYXHD", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_LINGJXHD(){
		String [] head = {"用户中心","零件编号","消耗点编号","物流描述编号","分配循环编号","生产线编号","配送类型","前后车标识","集配包装位置","消耗点起始日","结束日期","管理员编号","提前计算比例(%)","消耗比例(%)","顺序管理标志","自动发货标志","线边要货类型","是否延迟上线","是否产生备货单","小火车编号","小火车车厢编号","原消耗点","安全库存天数","安全库存数","线边理论库存","已发要货令总量","交付总量","年末(已发要货令总数量 - 交付总量)","PDS拆分标记(同步/集配/按需)","标识","终止总量","创建人","创建时间","修改人","修改时间","组号","工艺标识","层"};
		String [] properties = {"USERCENTER","LINGJBH","XIAOHDBH","WULBH","FENPQBH","SHENGCXBH","PEISLXBH","QIANHCBS","JIPBZWZ","SHENGXR","JIESR","GUANLYBH","TIQJSXHBL","XIAOHBL","SHUNXGLBZ","ZIDFHBZ","XIANBYHLX","YANCSXBZ","BEIHDBZ","XIAOHCBH","XIAOHCCXBH","YUANXHDBH","ANQKCTS","ANQKCS","XIANBLLKC","YIFYHLZL","JIAOFZL","XITTZZ","PDSBZ","BIAOS","ZHONGZZL","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH","GONGYBS","CENG"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_LINGJXHD");
		params.put("msg", "零件消耗点");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_LINGJXHD", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}
	
	@Test
	public void testCKX_LINGJGYS(){
		String [] head = {"用户中心","供应商编号","零件编号","供应商合同","供应商份额（100%）","有效期(天)","发运地","生效时间","失效时间","最小起订量","参考阀值(%)","质检抽检比例(%)","是否验证批次号","供应商UC的包装类型","供应商UC的容量","供应商UA的包装类型","供应商UA里UC的个数","盖板","内衬","是否存在零时包装","创建人","创建时间","修改人","修改时间","标识","是否失效管理"};
		String [] properties = {"USERCENTER","GONGYSBH","LINGJBH","GONGYHTH","GONGYFE","YOUXQ","FAYD","SHENGXSJ","SHIXSJ","ZUIXQDL","CANKFZ","ZHIJCJBL","SHIFYZPCH","UCBZLX","UCRL","UABZLX","UAUCGS","GAIB","NEIC","SHIFCZLSBZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","BIAOS","SHIFSXGL"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_LINGJGYS");
		params.put("msg", "零件供应商");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_LINGJGYS", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_XIEHZT(){
		String [] head = {"卸货站台编号","用户中心","卸货站台编组号","卸货站台名称","允许提前到货时间（分钟）","收货间隔时间（分钟）","工厂编码","仓库编号","同时接待车数","处理时间(分钟)","标识","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"XIEHZTBH","USERCENTER","XIEHZTBZH","XIEHZTMC","YUNXTQDHSJ","SHJGSJ","GONGCBM","CANGKBH","TONGSJDCS","CHULSJ","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIEHZT");
		params.put("msg", "卸货站台");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIEHZT", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_WAIBWL(){
		String [] head = {"用户中心","供应商编号","发货地","目的地","路径编号","路径名称","交付码","备货周期(天)","运输周期(天)","创建人","创建时间","修改人","修改时间","卸货站台编组","承运商编号"};
		String [] properties = {"USERCENTER","GONGYSBH","FAHD","MUDD","LUJBH","LUJMC","JIAOFM","BEIHZQ","PANYSJ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","XIEHZTBZ","CHENGYSBH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_WAIBWL");
		params.put("msg", "外部物流：供应商编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_WAIBWL", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_WAIBWLXX(){
		String [] head = {"用户中心","路径编码","序号","物理点编号","物理点名称","1否监控","关键点标识","计划到货剩余时间","承运商/供应商编号","运输模式","备货周期（天）","运输时间","要货令展开","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","LUJBH","XUH","WULDBH","WULDMC","SHIFJK","GUANJDBS","JIHDHSYSJ","GCBH","YUNSMS","BEIHZQ","YUNSSJ","YAOHLZKBS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_WAIBWLXX");
		params.put("msg", "外部物流详细");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_WAIBWLXX", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_SHENGCX_XIANB(){
		String [] head = {"用户中心","零件编号","分配区","循环起点","起点类型1","模式1","生效标识","将来模式1","生效时间1","生产线编号","子仓库","创建人","创建时间","修改人","修改时间","组号"};
		String [] properties = {"USERCENTER","LINGJBH","FENPQH","QID","QIDLX","MOS","SHENGXBS","JIANGLMS","JIANGLMSSXSJ","SHENGCXBH","ZICK","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_SHENGCX_XIANB");
		params.put("msg", "内部物流路径");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_SHENGCX_XIANB", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_SHENGCX(){
		String [] head = {"用户中心","生产线编号","产线组编号","特殊时间1星期","特殊时间1小时","特殊时间2星期","特殊时间2小时","特殊时间3星期","特殊时间3小时","整车生产节拍","整车未来生产节拍","整车生产节拍切换时间","排产计划处理天数","标识 1有效 0失效","创建人","创建时间","修改人","修改时间","成品生产节拍","1:车间,2：成品","安全天数默认值","车位数","空车身数"};
		String [] properties = {"USERCENTER","SHENGCXBH","CHANXZBH","TESSJXQ1","TESSJXS1","TESSJXQ2","TESSJXS2","TESSJXQ3","TESSJXS3","SHENGCJP","WEILSCJP","QIEHSJ","CHULTS","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","CPSHENGCJP","FLAG","ANQKCTSMRZ","CHEWS","KONGCSS"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_SHENGCX");
		params.put("msg", "生产线");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_SHENGCX", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_CHEJ(){
		String [] head = {"用户中心","车间编码","车间名称","备注","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","CHEJBM","CHEJMC","BEIZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_CHEJ");
		params.put("msg", "车间");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_CHEJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_XIEHZTBZ(){
		String [] head = {"卸货站台编组号","同时接待车数","处理时间(分钟)","创建人","创建时间","修改人","修改时间","标识 1有效 0失效","物流工艺员组","用户中心"};
		String [] properties = {"XIEHZTBZH","TONGSJDCS","CHULSJ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","BIAOS","WULGYYZ","USERCENTER"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIEHZTBZ");
		params.put("msg", "卸货站台编组");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIEHZTBZ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_CANGK(){
		String [] head = {"用户中心","仓库编号","站台类型","仓库类型,","到车提前期(分钟)(排产)","容器场编号","标识"," 创建人","创建时间","修改人","修改时间","物流工艺员组","安全天数默认值","最大库存天数默认值","卸货站台编组","出库模式"};
		String [] properties = {"USERCENTER","CANGKBH","ZHANTLX","CANGKLX","DAOCTQQ","RONGQCBH","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","WULGYYZ","ANQKCTSMRZ","ZUIDKCTSMRZ","XIEHZTBZ","CHUKMS"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_CANGK");
		params.put("msg", "仓库");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_CANGK", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_ZICK(){
		String [] head = {"用户中心","仓库编号","子仓库编号","饱和度百分比","是否按EL号管理","管理员组编号","站台编号","标识"," 创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","CANGKBH","ZICKBH","BAOHD","SHIFELGL","GUANLYZBH","ZHANTBH","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_ZICK");
		params.put("msg", "子仓库");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_ZICK", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
	@Test
	public void testCKX_GONGYS(){
		String [] head = {"用户中心","供应商编号/承运商编号/运输商编号","类型（内部供应商/外部供应商/承运商/运输商）","供货类型（制造路线）","公司名称","地址","邮编","联系人","电话","传真","备货周期","发运周期","卡车标准体积","每天送货最大频次","每天送货最小频次","内部供应商对应仓库编号","额定装载率","波动放大系数","供应商发运地","内部供应商用户中心","标识 1有效 0失效","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","GCBH","LEIX","GONGHLX","GONGSMC","DIZ","YOUB","LIANXR","DIANH","CHUANZ","BEIHZQ","FAYZQ","KACBZTJ","SONGHZDPC","SONGHZXPC","NEIBGYS_CANGKBH","EDZZL","BODFDXS","FAYD","NEIBYHZX","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_GONGYS");
		params.put("msg", "供应商");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_GONGYS", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

//	@Test
	public void testCKX_BAOZ() {
		String[] head = { "包装类型", "长度(单位:毫米)", "宽度(单位:毫米)", "高度(单位:毫米)",
				"包装重量(单位:千克)", "材质", "包装说明", "是否可回收 1是 0 否", "类别(GV/PC/SAS)",
				"折叠高度(单位:毫米)", "堆垛层数", "摆放方向", "标识 1有效 0失效", "创建人", "创建时间",
				"修改人", "修改时间" };
		String[] properties = { "BAOZLX", "CHANGD", "KUAND", "GAOD", "BAOZZL",
				"CAIZ", "BAOZMC", "SHIFHS", "LEIB", "ZHEDGD", "DUIDCS",
				"BAIFFX", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_BAOZ");
		params.put("msg", "包装");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_BAOZ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_CALENDAR_GROUP() {
		String[] head = { "用户中心", "车间产线编号/仓库编号", "工作时间编组号", "日历版次", "备注", "标识",
				"未来编组号", "生效时间", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "APPOBJ", "BIANZH", "RILBC",
				"BEIZ", "BIAOS", "WEILBZH", "SHENGXSJ", "CREATOR",
				"CREATE_TIME", "EDITOR", "EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_CALENDAR_GROUP");
		params.put("msg", "产线/仓库 -日历版次-工作时间编组");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_CALENDAR_GROUP", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_HUOCMB() {
		String[] head = { "用户中心", "生产线编号", "小火车编号", "趟次", "根据开班时间",
				"上线距备货偏移时间(分钟)", "创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "SHENGCXBH", "XIAOHCBH", "TANGC",
				"BEIHPYSJ", "SHANGXPYSJ", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_HUOCMB");
		params.put("msg", "小火车运输时刻模板");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_HUOCMB", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

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
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_LINGJ");
		params.put("msg", "零件表");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_LINGJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

//	@Test
	public void testCKX_LINGJKH() {
		String[] head = { "用户中心", "零件编号", "客户编号", "客户零件号", "客户零件名称", "发货站台",
				"零件包装UA", "UA包装中零件数量", "零件包装UC", "UC包装中零件数量", "生效日期", "失效日期",
				"创建人", "创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "LINGJBH", "KEHBH", "KEHLJH",
				"KEHLJMC", "FAHZT", "UABAOZ", "UALJSL", "UCBAOZ", "UCLJSL",
				"SHENGXRQ", "SHIXRQ", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_LINGJKH");
		params.put("msg", "零件客户关系");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_LINGJKH", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_XIANB_DINGH() {
		String[] head = { "用户中心", "零件编号", "线边仓库", "起点", "起点类型(内部)", "模式",
				"提交计算", "将来模式", "将来模式生效时间", "子仓库", "创建人", "创建时间", "修改人",
				"修改时间", "子仓库" };
		String[] properties = { "USERCENTER", "LINGJBH", "XIANBK", "QID",
				"QIDLX", "MOS", "SHENGXBS", "JIANGLMS", "JIANGLMSSXSJ", "ZICK",
				"CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME", "XIANBKZICK" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIANB_DINGH");
		params.put("msg", "线边仓库-订货仓库");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIANB_DINGH", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_XIAOHC() {
		String[] head = { "用户中心", "生产线编号", "小火车编号", "小火车名称", "标识 1有效 0无效",
				"创建人", "创建时间", "修改人", "修改时间", "偏移车位数", "循环车身数", "备货提前车位数",
				"上线提前车身数", "将来参数生效日期", "将来偏移车位数", "将来循环车身数", "将来备货提前车身数",
				"将来上线提前车身数" };
		String[] properties = { "USERCENTER", "SHENGCXBH", "XIAOHCBH",
				"XIAOHCMC", "BIAOS", "CREATOR", "CREATE_TIME", "EDITOR",
				"EDIT_TIME", "PIANYCWS", "XUNHCSS", "BEIHTQCSS", "SHANGXTQCSS",
				"JIANGLCSSXR", "JIANGLPYCWS", "JIANGLXHCSS", "JIANGLBHTQCSS",
				"JIANGLSXTQCSS" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIAOHC");
		params.put("msg", "小火车");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIAOHC", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_XIAOHCCX() {
		String[] head = { "用户中心", "生产线编号", "小火车编号", "车厢号", "标识 1有效 0失效", "创建人",
				"创建时间", "修改人", "修改时间" };
		String[] properties = { "USERCENTER", "SHENGCXBH", "XIAOHCBH", "CHEXH",
				"BIAOS", "CREATOR", "CREATE_TIME", "EDITOR", "EDIT_TIME" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIAOHCCX");
		params.put("msg", "小火车车厢");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIAOHCCX", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}

	@Test
	public void testCKX_XIEHZTXHSJ() {
		String[] head = { "用户中心","仓库编号","卸货站台编号","模式","备货时间C","生效标识","创建人","创建时间","修改人","修改时间","组号" };
		String[] properties = { "USERCENTER","CANGKBH","XIEHZTBH","MOS","BEIHSJ","SHENGXBS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH" };
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);

		params.put("sheetName", "PC_CKX_XIEHZTXHSJ");
		params.put("msg", "卸货站台循环时间");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows",
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputgood.queryCKX_XIEHZTXHSJ", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
}

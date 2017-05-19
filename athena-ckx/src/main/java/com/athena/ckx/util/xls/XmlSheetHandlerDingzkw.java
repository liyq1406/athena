package com.athena.ckx.util.xls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.db.ConstantDbCode;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */
public class XmlSheetHandlerDingzkw extends SheetHandlerDingzkw {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> mapUsercenter;	//用户中心集合
	private Map<String,String> mapLingj;		//零件集合
	private Map<String,Kuw> mapKuw;				//库位集合
	private Map<String, Zick> mapDim;			//地面库
	private Map<String,Lingjck> mapLingjck;		//零件仓库集合
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerDingzkw.class);	//定义日志方法
	public XmlSheetHandlerDingzkw(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String, String>  mapUsercenter,Map<String,String>  mapLingj,Map<String, Kuw>  mapKuw,Map<String, Zick> mapDim, Map<String, Lingjck>  mapLingjck,AbstractIBatisDao baseDao) {
		try{
			this.editor = editor;
			this.mapUsercenter=mapUsercenter;
			this.mapLingj=mapLingj;
			this.mapKuw=mapKuw;
			this.mapDim=mapDim;
			this.mapLingjck=mapLingjck;
			this.baseDao = baseDao;
			// 对sheet处理 保存入库
			// 对sheet处理 保存入库
			Element sheetElment = getSheetElment(sheet);
			int rowNum = getRowNumber(sheetElment);
			int coluNum = getCellNumber(sheetElment);
			String result = "";
			//导入的数据超过5000行 提示不让插入
			if(rowNum>5002){
				String  message = "导入数据超过5000行,请调整数据行数";
				return message;
			}
			ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
			ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
			List<Lingjck> datas = new ArrayList<Lingjck>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			for (int i = 2; i < list.size(); i++) {
				Lingjck lingjck = new Lingjck();
				Element rowElement =  (Element) list.get(i);
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0 && j< fieldValue.size())
					{
						//用户中心
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z]+$")){
							lingjck.setUsercenter(fieldValue.get(j).toUpperCase());
						}
					}
					else if( j == 1 && j< fieldValue.size())
					{
						//零件编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9]+$")){
							lingjck.setLingjbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j == 2 && j< fieldValue.size()){
						//仓库编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9]+$")){
							lingjck.setCangkbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j == 3 && j< fieldValue.size()){
						//子仓库编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9]+$")){
							lingjck.setZickbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j == 4 && j< fieldValue.size()){
						//定置库位
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9]+$")){
							lingjck.setDingzkw(fieldValue.get(j).toUpperCase());
						}
					}
			   }
			    datas.add(lingjck);
			}		
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Lingjck> wrongdatas =  new ArrayList<Lingjck>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Lingjck> datasnew =  new ArrayList<Lingjck>();
			//定置库位集合，每次存入模板中不重复的定置库位，并用来验证模板中下一个定置库位是否已存在
			List<String> dingzkwList = new ArrayList<String>();
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				Lingjck ljck = datas.get(i);
				//String s="";
				//校验数据在零件仓库是否存在，不存在的不执行操作
				String ljckKey = ljck.getUsercenter() + ljck.getLingjbh() + ljck.getCangkbh() + ljck.getZickbh();
				if (!mapLingjck.containsKey(ljckKey)) {//不存在，给出提示，忽略这一条数据的更新
					//0013135: 零件仓库定置库位导入---报错信息
					result = resultMessage(index,"用户中心："+ljck.getUsercenter()+"，零件编号："+ljck.getLingjbh()+"，仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+" 不存在");
					wronginfo=wronginfo.append(result);
				}else {//存在，再做以下校验
					//校验用户中心
					if(null==ljck.getUsercenter() || StringUtils.isBlank(ljck.getUsercenter()) || ljck.getUsercenter().length()!=2){
						if(null==ljck.getUsercenter()){
							ljck.setUsercenter("");
						}
						result = resultMessage(index,"用户中心："+ljck.getUsercenter()+" 必填 只能由数字、字母以及下划线组成并且长度必须为2位");
						wronginfo=wronginfo.append(result);
					}else {
						if (!mapUsercenter.containsKey(ljck.getUsercenter())) {
							//导入文件的用户中心不存在
							result = resultMessage(index,"用户中心："+ljck.getUsercenter()+" 用户中心不存在");
							wronginfo=wronginfo.append(result);
						}
					}
					//校验零件编号必填
					if(null==ljck.getLingjbh() || StringUtils.isBlank(ljck.getLingjbh()) || ljck.getLingjbh().length()!=10){
						if(null==ljck.getLingjbh()){
							ljck.setLingjbh("");
						}
						result = resultMessage(index,"零件编号："+ljck.getLingjbh()+" 必填 只能由数字、字母以及下划线组成并且长度必须为10位");
						wronginfo=wronginfo.append(result);
					}else{
						//校验零件编号的存在有效性
						 if (!mapLingj.containsKey(ljck.getUsercenter()+ljck.getLingjbh())){
							result = resultMessage(index," 零件编号："+ljck.getLingjbh()+" 不存在或已失效");
							wronginfo=wronginfo.append(result);
						 }
					}
					//校验 仓库、子仓库和库位的关系
					if (null!=ljck.getZickbh() && !"".equals(ljck.getZickbh()) && ljck.getZickbh().length()>=3) {
						if (ljck.getZickbh().substring(2, 3).equals("D")) {
							//使用mapDim中数据校验
							String ckKey = ljck.getUsercenter() + ljck.getCangkbh() + ljck.getZickbh();
							if (!mapDim.containsKey(ckKey)) {//地面库中不存在这样的数据
								if (null == ljck.getCangkbh()) {
									ljck.setCangkbh("");
								}
								if (null == ljck.getDingzkw()) {
									ljck.setDingzkw("");
								}
								result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+" 在地面库中不存在");
								wronginfo=wronginfo.append(result);
							}else {
								if (null == ljck.getDingzkw() || "".equals(ljck.getDingzkw())) {
									result = resultMessage(index,"定置库位："+ljck.getDingzkw()+"不能为空");
									wronginfo=wronginfo.append(result);
								}else if (ljck.getDingzkw().length() != 6) {
									result = resultMessage(index,"定置库位："+ljck.getDingzkw()+"必须为6位");
									wronginfo=wronginfo.append(result);
								}
							}
						}else if(ljck.getZickbh().substring(2, 3).equals("S") || ljck.getZickbh().substring(2, 3).equals("P")){
							if (null == ljck.getDingzkw() || "".equals(ljck.getDingzkw()) || ljck.getDingzkw().length() != 6) {
								result = resultMessage(index,"定置库位："+ljck.getDingzkw()+"不能为空且必须为6位");
								wronginfo=wronginfo.append(result);
							}else {
								String ckKey = ljck.getUsercenter() + ljck.getCangkbh() + ljck.getZickbh() + ljck.getDingzkw();
								if (dingzkwList.contains(ckKey)) {
									result = resultMessage(index,"定置库位："+ljck.getDingzkw()+"在模板中已存在，不能同时为多个零件设置");
									wronginfo=wronginfo.append(result);
								}else {
									dingzkwList.add(ckKey);
									//使用mapKuw中数据校验
									if (!mapKuw.containsKey(ckKey)) {//库位不存在
										if (null == ljck.getCangkbh()) {
											ljck.setCangkbh("");
										}
										if (null == ljck.getDingzkw()) {
											ljck.setDingzkw("");
										}
										result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+"，库位编号："+ljck.getDingzkw()+" 不存在");
										wronginfo=wronginfo.append(result);
									}else {//库位存在且为空
										//需要判断是否被其他零件占用
										Kuw kw = mapKuw.get(ckKey);
										if (null != kw.getLingjbh() && !"".equals(kw.getLingjbh())) {
											if (ljck.getLingjbh().equals(kw.getLingjbh())) {
												result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+"，库位编号："+ljck.getDingzkw()+" 是零件:"+ljck.getLingjbh()+"的定置库位，请勿重复设置");
												wronginfo=wronginfo.append(result);
											}else {
												result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+"，库位编号："+ljck.getDingzkw()+" 已被零件:"+kw.getLingjbh()+"占用");
												wronginfo=wronginfo.append(result);
											}
										}
									}
								}
							}
						}else {
							if (null == ljck.getCangkbh()) {
								ljck.setCangkbh("");
							}
							if (null == ljck.getDingzkw()) {
								ljck.setDingzkw("");
							}
							result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+"，子仓库编号："+ljck.getZickbh()+" 不可设置定置库位");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				ljck.setCreator(editor);
				ljck.setCreate_time(DateTimeUtil.getAllCurrTime());
				ljck.setEditor(editor);
				ljck.setEdit_time(DateTimeUtil.getAllCurrTime());
				index ++;
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(ljck);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
				}else {
					datasnew.add(ljck);
				}
				if(wrongdatas.size()>0 && wrongcoun==5){
					break;
				}
			}
			
			String error = errorBuffer.toString() + sqlUpdate(datasnew);
			
			return error;
		}finally{
			
		}
	}
	
	public  String  sqlUpdate(ArrayList<Lingjck> datasnew){
		int countSuss = 0;
		try {
			for (Lingjck lingjck : datasnew) {
				countSuss += baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckDingzkw",lingjck);
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "更新失败";
		}
		return "成功更新了定置库位的数据共有"+ countSuss + "条";
	}
	
	/**
	 * 根据sheet的名字去从XML拿出此sheet的对象
	 * @param sheetName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Element getSheetElment(String sheetName){
		Element sheetElement = null;
		Node node = document.selectSingleNode("Workbook");
		List list = node.selectNodes("xmlns:Worksheet");
		for(int i=0;i<list.size();i++){
			Element ele = (Element) list.get(i);
			if(sheetName.equals(ele.attributeValue("Name"))){
				sheetElement = ele;
				break;
			}
		}
		return sheetElement;
	}
	
	/**
	 * 拿出此sheet中包含的列数  默认取 第一个行的包含列数
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getCellNumber(Element sheetElement){
		Element firstRowElment = (Element) sheetElement.selectNodes("xmlns:Table/xmlns:Row").get(0);
		return firstRowElment.selectNodes("xmlns:Cell").size();
	}
	/**
	 * 
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getRowNumber(Element sheetElement){
		return sheetElement.selectNodes("xmlns:Table/xmlns:Row").size();
	}
	
	/**
	 * 得到此行的 列的值
	 * @param rowElement
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<String> getCellValue(Element rowElement){
		int top = 0;
		ArrayList<String> resultList = new ArrayList<String>();
		//拿出此行包含的列
		List cellList = rowElement.selectNodes("xmlns:Cell");
		for(int i=0;i<cellList.size();i++){
			Element cellElement = (Element) cellList.get(i);
			
			if(top==0){
				if(cellElement.attributeValue("Index")==null){
					//没有序列号 则认为此列为第一列
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上前面的值 统一补null
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					for(int j=0;j<b-1;j++){
						resultList.add(null);
						top++;
					}
					//再将本身添加进去
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}else{
				if(cellElement.attributeValue("Index")==null){
					//就是默认后面的
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上 Integer.parseInt(cellElement.attributeValue("Index"))-top-1 个值
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					int top2 = top;
					for(int j=0;j<b-1-top2;j++){
						resultList.add(null);
						top++;
					}
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}
		}
		
		return resultList;
	}
	/**
	 * 
	 * @param rowElement
	 * @param fieldSize
	 * @return如果最后列为空 则要放入null
	 */
	private ArrayList<String> getCellValue(Element rowElement,int fieldSize){
		ArrayList<String> resultList = getCellValue(rowElement);
		if(resultList.size()<fieldSize){
			for(int i=0;i<fieldSize-resultList.size();){
				resultList.add(null);
			}
		}
		return resultList;
	}
	
	/**
	 * 如果data标签不为空 则返回此value  为空则返回Null
	 * @param dataElement
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private String getDataValue(Element dataElement){
		String result = null;
		if(dataElement!=null){		
			result = dataElement.getText();
		}
		return result;
	}
	
	private String resultMessage(int index, String message) 
	{
		StringBuffer flagSb = new StringBuffer();
		flagSb.append("  导入文件中第").append(index).append("行,").append(message);
		return flagSb.toString();
	}


}

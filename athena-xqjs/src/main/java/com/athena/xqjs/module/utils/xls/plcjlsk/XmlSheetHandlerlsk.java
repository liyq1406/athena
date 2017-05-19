package com.athena.xqjs.module.utils.xls.plcjlsk;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.yaohl.Plcjlsk;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * 按照XML文件处理
 * @author huxy_V4_023
 * @vesion 1.0
 * @date 2016-10-14
 */

public class XmlSheetHandlerlsk extends SheetHandlerlsk {

	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlsk.class);	//定义日志方法
	public XmlSheetHandlerlsk(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,String> cmap,AbstractIBatisDao baseDao) throws BiffException, IOException, ParseException {
		this.editor = editor;
		this.cmap=cmap;
		this.baseDao = baseDao;
		String result = "";
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过100行 提示不让插入
		if(rowNum>102){
			String  message = "导入数据超过100行,请调整数据行数";
			return message;
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		List<Plcjlsk> datas = new ArrayList<Plcjlsk>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			Plcjlsk lsk = new Plcjlsk();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						lsk.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 1 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						lsk.setXunhbm(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 2  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						lsk.setShiffsgys(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 3  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))&& (fieldValue).get(j).matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$")){
						lsk.setYaohsl(Double.valueOf((fieldValue.get(j))));
					}else{
						lsk.setYaohsl(-1);
					}
				}else if( j == 4  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						lsk.setZuiwjfsj(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 5  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						lsk.setGongysdm(fieldValue.get(j).toUpperCase());
					}
				}
			}
			datas.add(lsk);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Plcjlsk> wrongdatas =  new ArrayList<Plcjlsk>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Plcjlsk> datasnew =  new ArrayList<Plcjlsk>();
		//校验每个字段
		for (int i=0;i<datas.size();i++) 
		{
			Plcjlsk lsk = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==lsk.getUsercenter() || StringUtils.isBlank(lsk.getUsercenter())){
				if(null==lsk.getUsercenter()){
					lsk.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+lsk.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}else if(!lsk.getUsercenter().matches("^[A-Za-z]{2}$")){
				lsk.setUsercenter("");
				result = resultMessage(index,"用户中心：长度必须为2位只能由字母组成");
				wronginfo=wronginfo.append(result);
			}
			if(null==lsk.getZuiwjfsj() || StringUtils.isBlank(lsk.getZuiwjfsj())){
				//最晚交付时间非必输
			}else if(!lsk.getZuiwjfsj().matches("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29) ([0-1][0-9]|[2][0-3]):[0-5][0-9]$")){
				lsk.setZuiwjfsj("");
				result = resultMessage(index,"最晚交付时间有误，请重新输入");
				wronginfo=wronginfo.append(result);
			}
			if(null==lsk.getXunhbm() || StringUtils.isBlank(lsk.getXunhbm())){
				if(null==lsk.getXunhbm()){
					lsk.setXunhbm("");
				}
				result = resultMessage(index,"循环编码："+lsk.getXunhbm()+"  必填");
				wronginfo=wronginfo.append(result);
			}else if(!lsk.getXunhbm().matches("^([A-Za-z0-9]{8})$")){
				lsk.setXunhbm("");
				result = resultMessage(index,"循环编码：长度必须为8位只能由数字、字母组成");
				wronginfo=wronginfo.append(result);
			}
			if(null==lsk.getShiffsgys() || StringUtils.isBlank(lsk.getShiffsgys())){
			 //是否发送供应商非必输
			}else if(!lsk.getShiffsgys().matches("^(Y|N)$")){
				lsk.setXunhbm("");
				result = resultMessage(index,"是否发送供应商必须为Y或者N");
				wronginfo=wronginfo.append(result);
			}else{
				if(lsk.getShiffsgys().equals("Y"))
				{
					lsk.setShiffsgys("1");
				}else if(lsk.getShiffsgys().equals("N")){
					lsk.setShiffsgys("0");
				}
			}
			//12929
			if(null==lsk.getGongysdm() || StringUtils.isBlank(lsk.getGongysdm())){
				//供应商非必输
				}else if(!lsk.getGongysdm().matches("^[0-9A-Z]{6}[\\s0-9A-Z]{2}[0-9A-Z]{2}$")){
					lsk.setGongysdm("");
					result = resultMessage(index,"供应商:长度必须为10位只能由数字、字母组成");
					wronginfo=wronginfo.append(result);
			}
			if(lsk.getYaohsl() <=0 )
			{
				//12929
				result = resultMessage(index,"要货量：不能超过10位，不能为空，必须大于0");
				wronginfo=wronginfo.append(result);
			}

			lsk.setCreator(editor);
			
			for(int n=0;n<datasnew.size();n++)
			{
				Plcjlsk y = datasnew.get(n);
				if(y.getUsercenter().equals(lsk.getUsercenter()) && y.getXunhbm().equals(lsk.getXunhbm()) ){
					result = resultMessage(index,"导入的要货令数据重复，请确认！");
					wronginfo=wronginfo.append(result);
				}
			}
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(lsk);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
			}
			
			datasnew.add(lsk);
			index ++;
			if(wrongdatas.size()>0 && wrongcoun==7){
				break;
			}
		}
		
		//如果没有错误信息，正常操作，如果有则需要返回
		if(wrongdatas.size()<=0)
		{
			errorBuffer=new StringBuffer("");
			long liush = 0;
			for (int i=0;i<datasnew.size();i++) {
				Plcjlsk lsk = datasnew.get(i);
				liush = (Long) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("yhl.queryLiush");
				
				lsk.setLiush(liush);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("yhl.insertPlcjlskdr",lsk);
			}
			index++;
		}
		if(wrongdatas.size()<=0){
			return sqlInsertOrUpdate(datasnew,editor);
		}else{
			return errorBuffer.toString();
		}
	}
  
	public  String sqlInsertOrUpdate(ArrayList<Plcjlsk> datasnew,String editor){
		String message = "导入成功！";
		return message;
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

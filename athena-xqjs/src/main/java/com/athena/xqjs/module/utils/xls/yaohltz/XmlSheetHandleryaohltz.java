package com.athena.xqjs.module.utils.xls.yaohltz;

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
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */

public class XmlSheetHandleryaohltz extends SheetHandleryaohltz {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandleryaohltz.class);	//定义日志方法
	public XmlSheetHandleryaohltz(Document document, String sheet, String table,
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
		//导入的数据超过10000行 提示不让插入
		if(rowNum>10002){
			String  message = "导入数据超过10000行,请调整数据行数";
			return message;
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<Yaohl> datas = new ArrayList<Yaohl>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			Yaohl yhl = new Yaohl();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						yhl.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 1 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						yhl.setYaohlh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 2  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						yhl.setZuiwsj(fieldValue.get(j).toUpperCase());
					}
				}
			}
			datas.add(yhl);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Yaohl> wrongdatas =  new ArrayList<Yaohl>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Yaohl> datasnew =  new ArrayList<Yaohl>();
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			Yaohl yhl = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==yhl.getUsercenter() || StringUtils.isBlank(yhl.getUsercenter())){
				if(null==yhl.getUsercenter()){
					yhl.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+yhl.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}else if(!yhl.getUsercenter().matches("^[A-Za-z]{2}$")){
				yhl.setUsercenter("");
				result = resultMessage(index,"用户中心：长度必须为2位只能由字母组成");
				wronginfo=wronginfo.append(result);
			}
			if(null==yhl.getZuiwsj() || StringUtils.isBlank(yhl.getZuiwsj())){
				if(null==yhl.getZuiwsj()){
					yhl.setLingjbh("");
				}
				result = resultMessage(index,"最晚交付时间："+yhl.getZuiwsj()+" 格式为HH-MM-dd HH24:mm:ss");
				wronginfo=wronginfo.append(result);
			}else if(!yhl.getZuiwsj().matches("^^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29) ([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9]$")){
				yhl.setZuiwsj("");
				result = resultMessage(index,"最晚交付时间有误，请重新输入");
				wronginfo=wronginfo.append(result);
			}
			if(null==yhl.getYaohlh() || StringUtils.isBlank(yhl.getYaohlh())){
				if(null==yhl.getYaohlh()){
					yhl.setYaohlh("");
				}
				result = resultMessage(index,"要货令号："+yhl.getYaohlh()+"  必填");
				wronginfo=wronginfo.append(result);
			}else if(!yhl.getYaohlh().matches("^([A-Za-z0-9]{10}|[A-Za-z0-9]{12})$")){
				yhl.setYaohlh("");
				result = resultMessage(index,"要货令号：长度必须为10或12位只能由数字、字母组成");
				wronginfo=wronginfo.append(result);
			}		
			
			yhl.setEditor(editor);
			yhl.setCreate_time(CommonFun.getJavaTime());
			for(int n=0;n<datasnew.size();n++){
				Yaohl y=datasnew.get(n);
				if(y.getUsercenter().equals(yhl.getUsercenter()) && y.getYaohlh().equals(yhl.getYaohlh()) ){
					result = resultMessage(index,"导入的要货令数据重复，请确认！");
					wronginfo=wronginfo.append(result);
				}
			}
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(yhl);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			
			datasnew.add(yhl);
			index ++;
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		
		//如果没有错误信息，正常操作，如果有则需要返回
		if(wrongdatas.size()<=0){
			errorBuffer=new StringBuffer("");
			//如果数据格式正确则校验要货令号可用性
			StringBuffer yhlh=new StringBuffer(" ");
			//将要货导入到要货令调整表中，首先根据用户删除要货令调整表中的数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("yhl.deleteYaohltz",editor);
			for (int i=0;i<datasnew.size();i++) {
				Yaohl yhl = datasnew.get(i);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("yhl.insertYaohldr",yhl);
			}
		//校验要货令导入信息是否存在
		List<Yaohl> yaohllist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).select("yhl.queryYaohldrxx",editor);
		//判断要货令是否存在
		index=3;
		for (int i=0;i<yaohllist.size();i++) {
			Yaohl yhl = yaohllist.get(i);
			if(null !=yhl ){
				result = resultMessage(index,"用户中心："+yhl.getUsercenter()+"要货令号："+yhl.getYaohlh()+"  的数据不存在或不是表达状态");
				wronginfo=wronginfo.append(result);
			}
			
			index++;
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(yhl);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
			}
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		}
		if(wrongdatas.size()<=0){
			return sqlInsertOrUpdate(datasnew,editor);
		}else{
			return errorBuffer.toString();
		}
	}
  
	public  String sqlInsertOrUpdate(ArrayList<Yaohl> datasnew,String editor){
		String message = "导入成功！";
		int count = 0;
		count =yaohltz(datasnew,editor);
		if (count == 0) {
			message = Const.KANBXH_DATAERRO;
		}
		return message;
	}
	
	@Transactional
	public Integer yaohltz(ArrayList<Yaohl> datasnew,String editor){
		int count =0;
		for(Yaohl yhl:datasnew){
		  count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("yhl.updateYaohl",yhl);
		}
		return count;
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

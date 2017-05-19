package com.athena.xqjs.module.utils.xls.kanbyhl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
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

public class XmlSheetHandlerlingjjzxh extends SheetHandlerlingjjzxh {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,List<Kanbxhgm>> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlingjjzxh.class);	//定义日志方法
	public XmlSheetHandlerlingjjzxh(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,List<Kanbxhgm>> cmap,AbstractIBatisDao baseDao) throws BiffException, IOException, ParseException {
		try{
			
		this.editor = editor;
		this.cmap=cmap;
		this.baseDao = baseDao;
		String result = "";
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
			//throw new ServiceException("导入数据超过5000行,请调整数据行数");
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<Kanbxhgm> datas = new ArrayList<Kanbxhgm>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		Map<String,Integer> keyMap = new HashMap<String, Integer>(); //判断用户中心+零件编号是否重复
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			Kanbxhgm xhgm = new Kanbxhgm();
			//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						xhgm.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}
				else if( j == 1  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						xhgm.setLingjbh(fieldValue.get(j).toUpperCase());
					}
				}
			}
			datas.add(xhgm);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Kanbxhgm> wrongdatas =  new ArrayList<Kanbxhgm>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Kanbxhgm> datasnew =  new ArrayList<Kanbxhgm>();
		
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			//String pianyisj="";
			Kanbxhgm xhgm = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==xhgm.getUsercenter() || StringUtils.isBlank(xhgm.getUsercenter())){
				if(null==xhgm.getUsercenter()){
					xhgm.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}
			if(null==xhgm.getLingjbh() || StringUtils.isBlank(xhgm.getLingjbh())){
				if(null==xhgm.getLingjbh()){
					xhgm.setLingjbh("");
				}
				result = resultMessage(index,"零件编号："+xhgm.getLingjbh()+" 必填并且长度必须为10位");
				wronginfo=wronginfo.append(result);
			}else if(!xhgm.getLingjbh().matches("^[A-Za-z0-9]{10}$")){
				xhgm.setLingjbh("");
				result = resultMessage(index,"零件编号：必须为10位只能由数字、字母以组成");
				wronginfo=wronginfo.append(result);
			}
			if(!StringUtils.isBlank(xhgm.getUsercenter()) && !StringUtils.isBlank(xhgm.getLingjbh())){
				//用户中心+零件编号对应的看板循环规模集合
				List<Kanbxhgm> kanbList = cmap.get(xhgm.getUsercenter()+xhgm.getLingjbh());
				//验证对应的看板循环规模是否存在
				if(kanbList == null){
					result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"零件编号："+xhgm.getLingjbh()+"对应的看板循环规模不存在");
					wronginfo=wronginfo.append(result);
				}
				else{
					//是否外部模式
					boolean isWaibms = false;
					//是否生效
					boolean isShengx = false;
					for (Kanbxhgm kanbxhgm : kanbList) {
						String gonghms = kanbxhgm.getGonghms();	//供货模式
						String shengxzt = kanbxhgm.getShengxzt();	//生效状态
						if("R1".equalsIgnoreCase(gonghms) || "R2".equalsIgnoreCase(gonghms)){
							isWaibms = true;
						}
						if("0".equals(shengxzt) || "1".equals(shengxzt)){
							isShengx = true;
						}
						if(isWaibms && isShengx){
							break;
						}
					}
					//验证看板循环规模的供货模式
					if(!isWaibms){
						result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"零件编号："+xhgm.getLingjbh()+"对应的看板循环的供货模式非外部模式");
						wronginfo=wronginfo.append(result);
					}
					//验证看板循环规模的生效状态
					else if(!isShengx){
						result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"零件编号："+xhgm.getLingjbh()+"对应的看板循环规模已失效");
						wronginfo=wronginfo.append(result);
					}
					//验证用户中心+零件编号是否重复
					else if(keyMap.containsKey(xhgm.getUsercenter()+xhgm.getLingjbh())){
						result = resultMessage(index,"与第"+keyMap.get(xhgm.getUsercenter()+xhgm.getLingjbh())+"行的数据重复");
						wronginfo=wronginfo.append(result);
					}
					//验证通过，在keyMap中记录，便于判断是否重复
					else{
						keyMap.put(xhgm.getUsercenter()+xhgm.getLingjbh(), i+3);
					}
				}
			}
			
			index ++;
			xhgm.setCreator(editor);
			xhgm.setCreate_time(CommonFun.getJavaTime());
			
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(xhgm);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			datasnew.add(xhgm);
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		
		
			//如果没有错误信息，正常操作，如果有则需要返回
			if(wrongdatas.size()<=0){
				//每次导入前清空零件截止循环表
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.clear_lingjjzxh");
				//更新数据
				return sqlInsertOrUpdate(datasnew);
			}
			return errorBuffer.toString();
		}finally{
			//关闭数据库连接
			closeConnection();
		}
		
	
	}
	
	/**
	 * 关闭数据库连接
	 */
	private void closeConnection(){
		//关闭连接
		if(conn!=null){
			try {
				if(!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
  
	public  String sqlInsertOrUpdate(ArrayList<Kanbxhgm> datasnew){
		String message = "导入成功！";
		int count = 0;
		for (Kanbxhgm xhgm : datasnew) {
			count =insertLingjjzxh(xhgm);
			if (count == 0) {
				message = Const.KANBXH_DATAERRO;
				break;
			}
		}
		return message;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Integer insertLingjjzxh(Kanbxhgm kanbxhgm){
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.insertLingjjzxh",
				kanbxhgm);
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

package com.athena.ckx.util.xls.chanxlj;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

import com.athena.ckx.entity.paicfj.Ckx_shengcx_lingj;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.util.DBUtil;
import com.athena.db.ConstantDbCode;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.util.exception.ServiceException;
import java.sql.SQLException;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */

public class XmlSheetHandlerchanxlj extends SheetHandlerchanxlj {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,Fenzx> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerchanxlj.class);	//定义日志方法
	public XmlSheetHandlerchanxlj(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,Fenzx> cmap,AbstractIBatisDao baseDao) throws BiffException, IOException, ParseException {
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
		List<Ckx_shengcx_lingj> datas = new ArrayList<Ckx_shengcx_lingj>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		Map<String,Integer> keyMap = new HashMap<String, Integer>(); //判断用户中心+零件编号是否重复
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			Ckx_shengcx_lingj chanxlj = new Ckx_shengcx_lingj();
			//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0){
					//用户中心
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 1  && j< fieldValue.size()){
					//生产线编号
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setShengcxbh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 2  && j< fieldValue.size()){
					//零件编号
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setLingjbh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 3  && j< fieldValue.size()){
					//制造路线
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setZhizlx(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 4  && j< fieldValue.size()){
					//仓库编号
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setCangkbh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 5  && j< fieldValue.size()){
					//主线辅线
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setZhuxfx(fieldValue.get(j));
					}
				}else if( j == 6  && j< fieldValue.size()){
					//是否取用经济批量
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setShifqyjjpl(fieldValue.get(j));
					}
				}else if( j == 7  && j< fieldValue.size()){
					//经济批量
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setJingjpl(Double.valueOf(fieldValue.get(j)));
					}
				}else if( j == 8  && j< fieldValue.size()){
					//优先班次
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setYouxbc(fieldValue.get(j).toUpperCase());
					}
				}
				else if( j == 9  && j< fieldValue.size()){
					//零件类型
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						chanxlj.setLingjlx(fieldValue.get(j).toUpperCase());
					}
				}
			}
			datas.add(chanxlj);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Ckx_shengcx_lingj> wrongdatas =  new ArrayList<Ckx_shengcx_lingj>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<Ckx_shengcx_lingj> datasnew =  new ArrayList<Ckx_shengcx_lingj>();
		
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			//String pianyisj="";
			Ckx_shengcx_lingj chanxlj = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==chanxlj.getUsercenter() || StringUtils.isBlank(chanxlj.getUsercenter())){
				if(null==chanxlj.getUsercenter()){
					chanxlj.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+chanxlj.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}
			if(null==chanxlj.getLingjbh() || StringUtils.isBlank(chanxlj.getLingjbh())){
				if(null==chanxlj.getLingjbh()){
					chanxlj.setLingjbh("");
				}
				result = resultMessage(index,"零件编号："+chanxlj.getLingjbh()+" 必填并且长度必须为10位");
				wronginfo=wronginfo.append(result);
			}else if(!chanxlj.getLingjbh().matches("^[A-Za-z0-9]{10}$")){
				chanxlj.setLingjbh("");
				result = resultMessage(index,"零件编号：必须为10位只能由数字、字母以组成");
				wronginfo=wronginfo.append(result);
			}
			if(null==chanxlj.getShengcxbh() || !chanxlj.getShengcxbh().matches("^[A-Za-z0-9]{5}$")){
				chanxlj.setShengcxbh("");
				result = resultMessage(index,"生产线编号："+chanxlj.getShengcxbh()+" 必填并且长度必须为5位");
				wronginfo=wronginfo.append(result);
			}
			//侧围线必须有零件类型
			if(chanxlj.getShengcxbh() != null && !StringUtils.isBlank(chanxlj.getShengcxbh()) 
					&& cmap.containsKey(chanxlj.getShengcxbh())){
				Fenzx fzx = cmap.get(chanxlj.getShengcxbh());
				if(fzx != null && fzx.getFenzxxs() > 1 && chanxlj.getLingjlx() == null){
					result = resultMessage(index,"侧围线必须有零件类型");
					wronginfo=wronginfo.append(result);
				}
			}
			
			index ++;
			chanxlj.setCreator(editor);
			chanxlj.setCreate_time(new Date());
			chanxlj.setEditor(editor);
			chanxlj.setEdit_time(new Date());
			
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(chanxlj);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			datasnew.add(chanxlj);
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		
		//0012720 - 连接池释放

			//如果没有错误信息，正常操作，如果有则需要返回
			if(wrongdatas.size()<=0){
				//在判断是插入 还是更新
				//先判断导入的数据是否存在 如果存在则更新
				return sqlInsertOrUpdate(datasnew);
			}
			
			
			  return errorBuffer.toString();
		}finally{
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

	}
  
	public  String sqlInsertOrUpdate(ArrayList<Ckx_shengcx_lingj> datasnew){
		try {
			for (Ckx_shengcx_lingj chanxlj : datasnew) {
				//判断导入的数据是新增还是更新后 在来做操作
				if(DBUtil.checkCount(baseDao,"ts_ckx.getCountChanxlj", chanxlj)){
					 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_shengcx_lingj",chanxlj);
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_shengcx_lingj",chanxlj);
				}
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入或更新失败";
		}
		return "";
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

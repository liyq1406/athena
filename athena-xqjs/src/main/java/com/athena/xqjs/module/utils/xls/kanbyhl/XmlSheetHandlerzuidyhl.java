package com.athena.xqjs.module.utils.xls.kanbyhl;

import java.io.IOException;
import java.math.BigDecimal;
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

public class XmlSheetHandlerzuidyhl extends SheetHandlerzuidyhl {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerzuidyhl.class);	//定义日志方法
	public XmlSheetHandlerzuidyhl(Document document, String sheet, String table,
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
				}else if(j == 2 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						xhgm.setXiaohd(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 3 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))&& (fieldValue).get(j).matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$")){
						xhgm.setZuidyhl(BigDecimal.valueOf(Double.valueOf((fieldValue.get(j)))));
					}else{
						xhgm.setChanx("false");
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
			}
			if(null==xhgm.getXiaohd() || StringUtils.isBlank(xhgm.getXiaohd())){
				if(null==xhgm.getXiaohd()){
					xhgm.setXiaohd("");
				}
				result = resultMessage(index,"消耗点/仓库编号："+xhgm.getXiaohd()+"  必填");
				wronginfo=wronginfo.append(result);
			}
			//校验最大要货量不等于0
			//如何最大要货量输入有误
			if(null !=xhgm.getChanx()){
				result = resultMessage(index,"最大要货量： 整数部分最大10位数字，小数部分最大3位数字");
				wronginfo=wronginfo.append(result); 
			}
			if(null !=xhgm.getZuidyhl()){
					if((xhgm.getZuidyhl()).doubleValue()==0 || (xhgm.getZuidyhl()).doubleValue() < 0 ){
						result = resultMessage(index,"最大要货量：必须大于0");
						wronginfo=wronginfo.append(result);
					}
			}
			
		    //消耗点/仓库编号值需要进行验证
			if(null!=xhgm.getXiaohd()&& !" ".equals(xhgm.getXiaohd())){
				if((xhgm.getXiaohd().length()!=3 ||xhgm.getXiaohd().length()!=9) && !xhgm.getXiaohd().matches("^[A-Za-z0-9_]+$")){
					result = resultMessage(index,"消耗点/仓库编号："+xhgm.getXiaohd()+"  长度必须为3位或9位,只能由数字、字母以及下划线组成");
					wronginfo=wronginfo.append(result);
				}else{
					//如果规则通过 在校验消耗点/仓库编号的存在有效性
					if((xhgm.getXiaohd()).length()==9){
						if(null==cmap.get("R1"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getXiaohd())){
							result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"零件编号："+xhgm.getLingjbh()+"消耗点："+xhgm.getXiaohd()+"  的数据不存在或已失效、已冻结");
							wronginfo=wronginfo.append(result);
						}else{
							xhgm.setGonghms("R1");	
							xhgm.setXunhbm(cmap.get("R1"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getXiaohd()));
						}
					 }else{
						 if(null==cmap.get("R2"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getXiaohd())){
								result = resultMessage(index,"用户中心："+xhgm.getUsercenter()+"零件编号："+xhgm.getLingjbh()+"仓库编号："+xhgm.getXiaohd()+"  的数据不存在或已失效、已冻结");
								wronginfo=wronginfo.append(result);
							}else{
								 xhgm.setGonghms("R2");	
								 xhgm.setCangkdm(xhgm.getXiaohd());
								 xhgm.setXunhbm(cmap.get("R2"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getXiaohd()));
							}
					 }
				}
			}

			index ++;
			xhgm.setEditor(editor);
			
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
			//更新数据
			return sqlInsertOrUpdate(datasnew);
		}
		return errorBuffer.toString();
	}
  
	public  String sqlInsertOrUpdate(ArrayList<Kanbxhgm> datasnew){
		String message = "导入成功！";
		int count = 0;
		// breakPoint k
		K: for (int i = 0; i < datasnew.size(); i++) {
			Kanbxhgm kanbxh = (Kanbxhgm) datasnew.get(i);
			for (int n = 0; n < datasnew.size(); n++) {
				Kanbxhgm kanbxhgm = (Kanbxhgm) datasnew.get(n);
				// 比对
				if (kanbxh.getXunhbm().equals(kanbxhgm.getXunhbm())
						&& kanbxh.getLingjbh().equals(kanbxhgm.getLingjbh())) {
					// 维护人
					kanbxhgm.setWeihr("AAA");
					// 获取java时间,设置维护时间
					kanbxhgm.setWeihsj(CommonFun.getJavaTime());
					// 更新看板计算最大要货量
					count =updateZuidyhl(kanbxhgm);
					if (count == 0) {
						// count等于零，跳出所有循环
						message = Const.KANBXH_DATAERRO;
						break K;
					}
				}
			}

		}
		return message;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Integer updateZuidyhl(Kanbxhgm kanbxhgm){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateYjfzl",
				kanbxhgm);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateZuidyhl",
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

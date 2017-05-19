/**
 * 
 */
package com.athena.component.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class XlsTestDataGenerator extends AbstractTestDataGenerator implements TestDataGenerator{
	
	public XlsTestDataGenerator(String[] locations) {
		super(locations);
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InsertParameter> generateInsertParameter(File file) {
		List<InsertParameter> insertParameters = new ArrayList<InsertParameter>();
		Workbook book = null;
		try{
			try {
				book  =  Workbook.getWorkbook( file);
			} catch (BiffException e) {
				logger.error("excel文件格式错误："+e.getMessage());
			} catch (IOException e) {
				logger.error("IO异常："+e.getMessage());
			}
			if(book!=null){
				InsertParameter insertParameter;
				for(Sheet sheet:book.getSheets()){
					insertParameter = sheetToInsertParameter(sheet);
					if(insertParameter!=null){
						insertParameters.add(insertParameter);
					}
				}
				
			}
		}catch(Exception e){
			logger.error("文件读取异常："+e.getMessage());
		}finally{
			if(book!=null)book.close();
		}
		return insertParameters;
	}

	/**
	 * SQL插入的参数
	 * @param sheet
	 * @return
	 */
	private InsertParameter sheetToInsertParameter(Sheet sheet) {
		InsertParameter insertParameter = new InsertParameter();
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		
		String tableName = sheet.getName();//表名
		
		List<String> headers = new ArrayList<String>();//头
		List<String> headerDataTypes = new ArrayList<String>();//数据类型
		List<RowValues> rowValues = new ArrayList<RowValues>();
		//取第二行数据作为数据库列名称
		String header;
		for(int col = 0;col<cols;col++){
			Cell cell = sheet.getCell(col, 1);//
			header = cell.getContents();
			if(header.indexOf(":")==-1){
				header = header+":string";
			}
			headers.add(header.split(":")[0]);
			headerDataTypes.add(header.split(":")[1]);
		}
		//
		for(int row=2;row<rows;row++){
			RowValues rowValue = new RowValues(cols);
			boolean skipFlag = false;
			for(int col = 0;col<cols;col++){
				Cell cell = sheet.getCell(col, row);//
				String content = cell.getContents();
				if(content==null){
					content = "";
				}
				if(content.equals("")&&col==0){//约定第一列不能为空的数据才导入
					skipFlag = true;
					break;
				}
				rowValue.addValue(convertValue(content,headerDataTypes.get(col)));
			}
			if(!skipFlag){//加入非跳过的数据行
				rowValues.add(rowValue);
			}
			rowValue = null;
		}
		
		insertParameter.setTableName(tableName);
		insertParameter.setHeaders(headers);
		insertParameter.setRowValues(rowValues);
		
		return insertParameter;
	}


}
